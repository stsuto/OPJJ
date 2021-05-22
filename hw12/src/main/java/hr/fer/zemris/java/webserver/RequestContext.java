package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Context of the request.
 * 
 * @author stipe
 *
 */
public class RequestContext {

	/**
	 * Default encoding.
	 */
	private static final String DEFAULT_ENCODING = "UTF-8";
	/**
	 * Default status code.
	 */
	private static final int DEFAULT_STATUS_CODE = 200;
	/**
	 * Default status text.
	 */
	private static final String DEFAULT_STATUS_TEXT = "OK";
	/**
	 * Default mime type.
	 */
	private static final String DEFAULT_MIME_TYPE = "text/html";
	
	/**
	 * Context output stream.
	 */
	private OutputStream outputStream;
	/**
	 * Current charset.
	 */
	private Charset charset;
	/**
	 * Current encoding.
	 */
	private String encoding;
	/**
	 * Current status codee.
	 */
	private int statusCode;
	/**
	 * Current status text.
	 */
	private String statusText;
	/**
	 * Current mime type.
	 */
	private String mimeType;
	/**
	 * Length of the content to be output.
	 */
	private Long contentLength;
	/**
	 * Map of parameters.
	 */
	private Map<String, String> parameters;
	/**
	 * Map of temporary parameters.
	 */
	private Map<String, String> temporaryParameters;
	/**
	 * Parameters which persist.
	 */
	private Map<String, String> persistentParameters;
	/**
	 * List of cookies.
	 */
	private List<RCCookie> outputCookies;
	/**
	 * Flag showing whether or not a header has already been generated.
	 */
	private boolean headerGenerated;
	/**
	 * Dispatcher.
	 */
	private IDispatcher dispatcher;
	/**
	 * Session id.
	 */
	private String sid;

	/**
	 * Constructor.
	 * 
	 * @param outputStream {@link #outputStream}
	 * @param parameters {@link #parameters}
	 * @param persistentParameters {@link #persistentParameters}
	 * @param outputCookies {@link #outputCookies}
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
		
		this.outputStream = Objects.requireNonNull(outputStream, "Output stream mustn't be null!");
		this.parameters = initProperty(parameters, HashMap::new);
		this.persistentParameters = initProperty(persistentParameters, HashMap::new);
		this.outputCookies = initProperty(outputCookies, ArrayList::new);
		
		encoding = DEFAULT_ENCODING;
		statusCode = DEFAULT_STATUS_CODE;
		statusText = DEFAULT_STATUS_TEXT;
		mimeType = DEFAULT_MIME_TYPE;
		temporaryParameters = new HashMap<>();
	}
	
	/**
	 * Constructor defining a greater number of properties.
	 * 
	 * @param outputStream {@link #outputStream}
	 * @param parameters {@link #parameters}
	 * @param persistentParameters {@link #persistentParameters}
	 * @param outputCookies {@link #outputCookies}
	 * @param temporaryParameters {@link #temporaryParameters}
	 * @param dispatcher {@link #dispatcher}
	 * @param sid {@link #sid}
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies, 
			Map<String,String> temporaryParameters, IDispatcher dispatcher, String sid) {
		
		this(outputStream, parameters, persistentParameters, outputCookies);
		this.temporaryParameters = temporaryParameters;
		this.dispatcher = dispatcher;
		this.sid = sid;
	}

	/**
	 * Initializes the property.
	 * 
	 * @param param value to be set if not null
	 * @param sup supplier to be used if param is null
	 * @return supplied object if param is null, or param otherwise
	 */
	private <T> T initProperty(T param, Supplier<T> sup) {
		return param == null ? sup.get() : param;
	}

	/**
	 * Method that retrieves value from parameters map, or null if no association exists.
	 * 
	 * @param name parameter key
	 * @return value mapped to the given key
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * Method that retrieves names of all parameters in parameters map.
	 * 
	 * @return read only set of parameters
	 */
	public Set<String> getParameterNames(){
		return Collections.unmodifiableSet(parameters.keySet());
	}

	/**
	 * Method that retrieves value from persistent parameters map, or null if no association exists.
	 * 
	 * @param name parameter key
	 * @return value mapped to the given key
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}

	/**
	 * Method that retrieves names of all persistent parameters in parameters map.
	 * 
	 * @return read only set of parameters
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}

	/**
	 * Sets the value and key to persistent parameters.
	 * 
	 * @param name
	 * @param value
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}
	
	/**
	 * Removes the key from persistent parameters.
	 * 
	 * @param name key
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}

	/**
	 * Method that retrieves value from temporary parameters map, or null if no association exists.
	 * 
	 * @param name parameter key
	 * @return value mapped to the given key
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}

	/**
	 * Method that retrieves names of all temporary parameters in parameters map.
	 * 
	 * @return read only set of parameters
	 */
	public Set<String> getTemporaryParameterNames(){
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}

	/**
	 * Returns session id.
	 * 
	 * @return {@link #sid}
	 */
	public String getSessionID() {
		return sid;
	}

	/**
	 * Sets the value and key to temporary parameters.
	 * 
	 * @param name
	 * @param value
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}

	/**
	 * Removes the key from temporary parameters.
	 * 
	 * @param name key
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}

	/**
	 * Sets encoding.
	 * 
	 * @param encoding
	 */
	public void setEncoding(String encoding) {
		checkHeader();
		this.encoding = encoding;
	}

	/**
	 * Sets status code.
	 * 
	 * @param statusCode
	 */
	public void setStatusCode(int statusCode) {
		checkHeader();
		this.statusCode = statusCode;
	}

	/**
	 * Sets status text.
	 * 
	 * @param statusText
	 */
	public void setStatusText(String statusText) {
		checkHeader();
		this.statusText = statusText;
	}

	/**
	 * Sets mime type.
	 * 
	 * @param mimeType
	 */
	public void setMimeType(String mimeType) {
		checkHeader();
		this.mimeType = mimeType;
	}

	/**
	 * Sets content length.
	 * 
	 * @param contentLength
	 */
	public void setContentLength(Long contentLength) {
		checkHeader();
		this.contentLength = contentLength;
	}

	/**
	 * Checks if header has already been generated.
	 */
	private void checkHeader() {
		if (headerGenerated) {
			throw new RuntimeException("Cannot change properties after header construction!");
		}
	}

	/**
	 * Adds the given cookie to the list.
	 * 
	 * @param rcCookie
	 */
	public void addRCCookie(RCCookie rcCookie) {
		outputCookies.add(rcCookie);
	}
	
	/**
	 * Writes the given bytes.
	 * 
	 * @param data bytes to be written
	 * @return this instance of context 
	 * @throws IOException
	 */
	public RequestContext write(byte[] data) throws IOException {
		return write(data, 0, data.length);
	}
	
	/**
	 * Writes the given bytes.
	 * 
	 * @param data bytes to be written
	 * @param offset offset
	 * @param len length of bytes
	 * @return this instance of context
	 * @throws IOException
	 */
	public RequestContext write(byte[] data, int offset, int len) throws IOException {
		if (!headerGenerated) {
			constructHeader();
		}
		outputStream.write(data, offset, len);
		return this;
	}
	
	/**
	 * Constructs and outputs the header.
	 * 
	 * @throws IOException
	 */
	private void constructHeader() throws IOException {
		charset = Charset.forName(encoding);
		StringBuilder sb = new StringBuilder("HTTP/1.1 " + statusCode + " " + statusText + "\r\n");
		sb.append("Content-Type: " + mimeType);
		if (mimeType.startsWith("text/")) sb.append("; charset=" + encoding);
		sb.append("\r\n");
		if (contentLength != null) sb.append("Content-Length: " + contentLength + "\r\n");
		outputCookies.forEach(c -> sb.append(c.toString()));
		sb.append("\r\n");
		outputStream.write(sb.toString().getBytes(StandardCharsets.ISO_8859_1));
		headerGenerated = true;
	}
	
	/**
	 * Writes the given string.
	 * 
	 * @param text string to be written
	 * @return this instance of cntext
	 * @throws IOException
	 */
	public RequestContext write(String text) throws IOException {
		// If charset hasn't been initialized in header construction yet, uses encoding string.
		return charset == null ? write(text.getBytes(encoding)) 
							   : write(text.getBytes(charset));
	}
	
	/**
	 * Returns this context's dispatcher
	 * 
	 * @return {@link #dispatcher}
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * Static class representing a cookie.
	 * 
	 * @author stipe
	 *
	 */
	public static class RCCookie {
		/**
		 * Cookie name.
		 */
		private String name;
		/**
		 * Cookie value.
		 */
		private String value;
		/**
		 * Cookie max age.
		 */
		private Integer maxAge;
		/**
		 * Cookie domain.
		 */
		private String domain;
		/**
		 * Cookie path.
		 */
		private String path;
		
		/**
		 * Constructor.
		 * 
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}
		
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @return the maxAge
		 */
		public Integer getMaxAge() {
			return maxAge;
		}

		/**
		 * @return the domain
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * @return the path
		 */
		public String getPath() {
			return path;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Set-Cookie: " + name + "=\"" + value + "\"");
			if (domain != null) sb.append("; Domain=" + domain);
			if (path != null) sb.append("; Path=" + path);
			if (maxAge != null) sb.append("; Max-Age=" + maxAge);
			return sb.append("\r\n").toString();
		}
	}

}
