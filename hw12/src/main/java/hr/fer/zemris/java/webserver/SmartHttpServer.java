package hr.fer.zemris.java.webserver;

import static hr.fer.zemris.java.util.Util.readFromDisk;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Class representing a http server capable of using smart scripts
 * and other types of files.
 * 
 * @author stipe
 *
 */
public class SmartHttpServer {
	
	/**
	 * Server address.
	 */
	private String address;
	/**
	 * Server domain name.
	 */
	private String domainName;
	/**
	 * Server port.
	 */
	private int port;
	/**
	 * Number of worker threads.
	 */
	private int workerThreads;
	/**
	 * Session timeout.
	 */
	private int sessionTimeout;
	/**
	 * Map of mime types.
	 */
	private Map<String, String> mimeTypes = new HashMap<String, String>();
	/**
	 * Server thread.
	 */
	private ServerThread serverThread;
	/**
	 * Flag used for stopping the server thread.
	 */
	private volatile boolean stop;
	/**
	 * Threadpool.
	 */
	private ExecutorService threadPool;
	/**
	 * Document's root.
	 */
	private Path documentRoot;
	/**
	 * Map of workers.
	 */
	private Map<String,IWebWorker> workersMap;
	/**
	 * Map of sessions.
	 */
	private Map<String, SessionMapEntry> sessions = new HashMap<>();
	/**
	 * Random generator used for session generating.
	 */
	private Random sessionRandom = new Random();

	/**
	 * Constructor
	 * 
	 * @param configFileName config file name
	 */
	public SmartHttpServer(String configFileName) {
		Properties config = new Properties();
		try {
			config.load(new FileInputStream(configFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		address = config.getProperty("server.address");
		domainName = config.getProperty("server.domainName");
		port = Integer.parseInt(config.getProperty("server.port"));
		workerThreads = Integer.parseInt(config.getProperty("server.workerThreads"));
		sessionTimeout = Integer.parseInt(config.getProperty("session.timeout"));
		initMimeTypes(config);
		serverThread = new ServerThread();
		documentRoot = Paths.get(config.getProperty("server.documentRoot"));
		initWorkersMap(config);
	}

	/**
	 * Initializes the worker map.
	 * 
	 * @param config
	 */
	private void initWorkersMap(Properties config) {
		// Using Files.lines as the size of the file could be large so Files.readAllLines might not be a good choice.
		try (Stream<String> stream = Files.lines(Paths.get(config.getProperty("server.workers")))) {
			workersMap = stream.map(l -> l.split("\\="))
					.collect(Collectors.toMap(
						l -> l[0].strip(),
						l -> instantiateWorker(l[1].strip())
					));
		//Exception when keys are duplicate.
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Instantiates a worker from the given class.
	 * 
	 * @param fqcn full qualifying class name
	 * @return instance of wanted worker
	 */
	@SuppressWarnings("deprecation")
	private IWebWorker instantiateWorker(String fqcn) {
		Class<?> referenceToClass = null;
		Object newObject = null;
		try {
			referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
			newObject = referenceToClass.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return (IWebWorker) newObject;
	}
	
	/**
	 * Initializes the map of mime types.
	 * 
	 * @param config
	 */
	private void initMimeTypes(Properties config) {
		// Using Files.lines as the size of the file could be large so Files.readAllLines might not be a good choice.
		try (Stream<String> stream = Files.lines(Paths.get(config.getProperty("server.mimeConfig")))) {
			mimeTypes = stream.map(l -> l.split("\\="))
					.collect(Collectors.toMap(l -> l[0].strip(), l -> l[1].strip()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starts the client serving.
	 */
	protected synchronized void start() {
		if (!serverThread.isAlive()) {
			serverThread.start();
		}
		threadPool = Executors.newFixedThreadPool(workerThreads);
		
		Thread sessionRemover = createRemover();
		sessionRemover.setDaemon(true);
		sessionRemover.start();
	}

	/**
	 * Creates a cleaner thhread.
	 * 
	 * @return
	 */
	private Thread createRemover() {
		return new Thread(() -> {
			try {
				Thread.sleep(300000);
			} catch (InterruptedException e) {
			}
			
			for (Entry<String, SessionMapEntry> e : sessions.entrySet()) {
				if (e.getValue().validUntil < System.currentTimeMillis() / 1000) {
					sessions.remove(e.getKey());
				}
			}
		});
	}

	/**
	 * Stops serving clients.
	 */
	protected synchronized void stop() {
		stop = true;
		threadPool.shutdown();
	}

	/**
	 * Server thread.
	 * 
	 * @author stipe
	 *
	 */
	protected class ServerThread extends Thread {
		@Override
		public void run() {
			try (ServerSocket serverSocket = new ServerSocket()) {
				serverSocket.bind(new InetSocketAddress(InetAddress.getByName(address), port));
				while (true) {
					if (stop) {
						break;
					}
					Socket client = serverSocket.accept();
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Class representing a worker.
	 * 
	 * @author stipe
	 *
	 */
	private class ClientWorker implements Runnable, IDispatcher {
		/**
		 * Default package of workers.
		 */
		private static final String WORKERS_PACKAGE = "hr.fer.zemris.java.webserver.workers.";
		/**
		 * Client socket.
		 */
		private Socket csocket;
		/**
		 * Input stream.
		 */
		private PushbackInputStream istream;
		/**
		 * Output stream.
		 */
		private OutputStream ostream;
		/**
		 * Version.
		 */
		private String version;
		/**
		 * Method.
		 */
		private String method;
		/**
		 * Host.
		 */
		private String host;
		/**
		 * Parameter map.
		 */
		private Map<String, String> params = new HashMap<String, String>();
		/**
		 * Temporary parameter map.
		 */
		private Map<String, String> tempParams = new HashMap<String, String>();
		/**
		 * Permanent parameter map.
		 */
		private Map<String, String> permPrams = new HashMap<String, String>();
		/**
		 * List of cookies.
		 */
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		/**
		 * Session id.
		 */
		private String SID;
		/**
		 * Worker context.
		 */
		private RequestContext context;

		/**
		 * Constructor.
		 * 
		 * @param csocket client socker
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}

		/**
		 * Handles the dispatch request.
		 * 
		 * @param urlPath url path
		 * @param directCall flag representing direct call
		 * @throws Exception
		 */
		public void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			getContext();
			
			if (directCall && isPrivate(urlPath)) {
				sendError(404, "Private");
				return;
			}
			
			if (urlPath.startsWith("/ext/")) {
				String fqcn = WORKERS_PACKAGE + urlPath.substring(5);
				IWebWorker worker = instantiateWorker(fqcn);
				worker.processRequest(context);
				closeEverything();
				return;
			}
			
			IWebWorker worker = workersMap.get(urlPath);
			if (worker != null) {
				worker.processRequest(context);
				// Mozda tu closeEverything() i return?
			} else {
				Path filePath = resolveChild(urlPath.substring(1));
				if (filePath == null) {
					sendError(403, "Forbidden");
					return;
				}
				if (!(Files.exists(filePath) && Files.isReadable(filePath))) {
					sendError(404, "File not found");
					return;
				}
				String extension = extractExtension(filePath);
				if ("smscr".equals(extension)) {
					executeScript(filePath);
				} else {
					executeNormal(filePath, extension);
				}				
			}
			
			closeEverything();
		}
		
		/**
		 * Checks if the url is private.
		 * 
		 * @param urlPath url
		 * @return
		 */
		private boolean isPrivate(String urlPath) {
			return urlPath.equals("/private") 
					|| urlPath.startsWith("/private/");
		}

		/**
		 * Executes smart script.
		 * 
		 * @param filePath path
		 */
		private void executeScript(Path filePath) {
			String documentBody = readFromDisk(filePath);
			
			new SmartScriptEngine(
					new SmartScriptParser(documentBody).getDocumentNode(),
					context
				).execute();
		}

		/**
		 * Updates context if necessary and returns it.
		 * 
		 * @return context
		 */
		private RequestContext getContext() {
			if (context == null) {
				context = new RequestContext(
						ostream, params, permPrams, 
						outputCookies, tempParams, 
						this, SID
					);
			}
			return context;
		}

		/**
		 * Executes teh normal file writing.
		 * 
		 * @param filePath file
		 * @param extension extension of the file
		 * @throws IOException
		 */
		private void executeNormal(Path filePath, String extension) throws IOException {
			String mimeType = determineMimetype(extension);
			context.setMimeType(mimeType);
			context.setStatusCode(200);
			
			byte[] bytes = Files.readAllBytes(filePath);
			context.write(bytes);
		}

		/**
		 * Dispatches request.
		 */
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}
		
		@Override
		public void run() {
			try {
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = new BufferedOutputStream(csocket.getOutputStream());
				
				List<String> request = readRequest();
				if (request == null || request.isEmpty()) {
					sendError(400, "Bad request");
					return;
				}
				
				String firstLine = request.get(0);
				String[] lineParts = firstLine.split(" ");
				if (lineParts.length != 3) {
					sendError(400, "Bad request");
					return;
				}
				
				method = lineParts[0].toUpperCase();
				if(!method.equals("GET")) {
					sendError(405, "Method Not Allowed");
					return;
				}
				
				version = lineParts[2].toUpperCase();
				if(!(version.equals("HTTP/1.1") || version.equals("HTTP/1.0"))) {
					sendError(505, "HTTP Version Not Supported");
					return;
				}
				
				String requestedPath = lineParts[1];
				
				host = request.stream()
					.filter(l -> l.startsWith("Host:"))
					.map(l -> l.split(":")[1].strip())
					.reduce((f,  s) -> s)
					.orElse(domainName);

				checkSession(request);
				
				String[] pathParts = requestedPath.split("\\?");
				String path = pathParts[0];
				if (pathParts.length > 1) {
					String paramString = pathParts[1];				
					parseParameters(paramString);					
				}
				
				internalDispatchRequest(path, true);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * Checks the session id.
		 * 
		 * @param request
		 */
		private synchronized void checkSession(List<String> request) {
			String sidCandidate = findCandidate(request);
			
			if (sidCandidate == null) {
				sidCandidate = createCandidate();
				createNewSession(sidCandidate);
				
			} else {
				checkExistingCandidate(sidCandidate);
			}
			
			permPrams = sessions.get(sidCandidate).map;
		}

		/**
		 * Checks the existing session.
		 * 
		 * @param sidCandidate
		 */
		private void checkExistingCandidate(String sidCandidate) {
			SessionMapEntry entry = sessions.get(sidCandidate);
			if (entry != null && entry.host.equals(host)) {
				long time = System.currentTimeMillis() / 1000;
				if (entry.validUntil > time) {
					entry.validUntil = time + sessionTimeout;
				} else {
					sessions.remove(sidCandidate);
					createNewSession(sidCandidate);
				}
			} else {
				createNewSession(sidCandidate);
			}
		}

		/**
		 * Creates a candidate session id.
		 * 
		 * @return
		 */
		private String createCandidate() {
			StringBuilder sb = new StringBuilder();
			int min = 'A';
			int max = 'Z';
			for (int i = 0; i < 20; i++) {
				int value = min + (int) sessionRandom.nextDouble() * (max - min + 1);
				sb.append((char) value);
			}
			return sb.toString();
		}
		
		/**
		 * Fins the candidate from the header.
		 * 
		 * @param request
		 * @return
		 */
		private String findCandidate(List<String> request) {
			String sidCandidate = null;
			for (String line : request) {
				if (!line.startsWith("Cookie:")) continue;
				if (!line.contains("sid=")) continue;
			
				int i = line.indexOf("sid=") + 4;
				if (i < line.length()) {
					sidCandidate = line.substring(i)
						.split("\\;")[0].split(" ")[0]
						.replace("\"", "");
				}
			}
			return sidCandidate;
		}

		/**
		 * Creates ne session with the given candidate.
		 * 
		 * @param sidCandidate
		 */
		private void createNewSession(String sidCandidate) {
			long time = System.currentTimeMillis() / 1000 + sessionTimeout;
			SessionMapEntry session = new SessionMapEntry(
					sidCandidate, host, time, new ConcurrentHashMap<>()
				);
			sessions.put(sidCandidate, session);
			outputCookies.add(new RCCookie("sid", sidCandidate, null, host, "/"));
		}

		/**
		 * Determines the mime type.
		 * 
		 * @param extension
		 * @return
		 */
		private String determineMimetype(String extension) {
			String mime = mimeTypes.get(extension);
			return mime == null ? "application/octet-stream" : mime;
		}

		/**
		 * Extracts teh extension from the file.
		 * 
		 * @param filePath
		 * @return
		 */
		private String extractExtension(Path filePath) {
			String name = filePath.getFileName().toString();
			int dotIndex = name.indexOf(".");
			if (dotIndex == -1 || dotIndex == name.length() - 1) {
				return null;
			}
			return name.substring(dotIndex + 1).toLowerCase();
		}
		
		/**
		 * Resolves the child path via the document root.
		 * 
		 * @param path child path
		 * @return resolved path
		 */
		private Path resolveChild(String path) {
			Path child = documentRoot.resolve(Paths.get(path));
			if (child.toAbsolutePath().startsWith(documentRoot.toAbsolutePath())) {
				return documentRoot.resolve(path);
			}
			return null;
		}

		/**
		 * Parses parameters.
		 * 
		 * @param paramString
		 */
		private void parseParameters(String paramString) {
			for (String param : paramString.split("\\&")) {
				String[] parts = param.split("\\=");
				params.put(parts[0], parts[1]);
			}
		}

		/**
		 * Reads header request.
		 * 
		 * @return list of header lines
		 * @throws IOException
		 */
		private List<String> readRequest() throws IOException {
			byte[] request = readRequest(istream);
			if (request == null) return null;
			
			String requestText = new String(
					request, 
					StandardCharsets.US_ASCII
				);
//			return requestText.lines().collect(Collectors.toList());
			return extractHeaders(requestText);
		}

		/**
		 * Reads bytes of the header.
		 * 
		 * @param is
		 * @return
		 * @throws IOException
		 */
		private byte[] readRequest(InputStream is) throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
			l: 
			while (true) {
				int b = is.read();
				if(b==-1) return null;
				if(b!=13) {
					bos.write(b);
				}
				switch(state) {
				case 0: 
					if(b==13) { state=1; } else if(b==10) state=4;
					break;
				case 1: 
					if(b==10) { state=2; } else state=0;
					break;
				case 2: 
					if(b==13) { state=3; } else state=0;
					break;
				case 3: 
					if(b==10) { break l; } else state=0;
					break;
				case 4: 
					if(b==10) { break l; } else state=0;
					break;
				}
			}
			return bos.toByteArray();
		}

		/**
		 * Extracts header elements.
		 * 
		 * @param requestHeader header text
		 * @return
		 */
		private List<String> extractHeaders(String requestHeader) {
			List<String> headers = new ArrayList<String>();
			String currentLine = null;
			for(String s : requestHeader.split("\n")) {
				if(s.isEmpty()) break;
				char c = s.charAt(0);
				if(c==9 || c==32) {
					currentLine += s;
				} else {
					if(currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}
			if(!currentLine.isEmpty()) {
				headers.add(currentLine);
			}
			return headers;
		}
		
		/**
		 * Error sending method.
		 * 
		 * @param statusCode error code
		 * @param statusText error text
		 * @throws IOException
		 */
		private void sendError(int statusCode, String statusText) throws IOException {
			ostream.write(
				("HTTP/1.1 " + statusCode + " " + statusText + "\r\n"+
				"Server: simple java server\r\n"+
				"Content-Type: text/plain;charset=UTF-8\r\n"+
				"Content-Length: 0\r\n"+
				"Connection: close\r\n"+
				"\r\n").getBytes(StandardCharsets.US_ASCII)
			);
			closeEverything();
		}

		/**
		 * Flushes output and closes socket.
		 * 
		 * @throws IOException
		 */
		private void closeEverything() throws IOException {
			ostream.flush();
			csocket.close();
		}
		
	}
	
	/**
	 * Static class representing session entry.
	 * 
	 * @author stipe
	 *
	 */
	private static class SessionMapEntry {
		@SuppressWarnings("unused")
		String sid;
		String host;
		long validUntil;
		Map<String, String> map;
		
		public SessionMapEntry(String sid, String host, long validUntil, Map<String, String> map) {
			this.sid = sid;
			this.host = host;
			this.validUntil = validUntil;
			this.map = map;
		}
	}
	
	/**
	 * Main method. Accepts only one argument which is the configuration file path.
	 * 
	 * @param args config
	 */
	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.out.println("Exactly one argument is expected!");
			return;
		}
		
		SmartHttpServer server = new SmartHttpServer(args[0]);
		server.start();
	}
	
}