package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class {@code GlasanjeGlasajServlet} is a servlet which 
 * defines the act of voting.
 * 
 * @author stipe
 *
 */
@WebServlet("/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = null;
		try {
			id = String.valueOf(Integer.parseInt(req.getParameter("id")));
			
		} catch (RuntimeException e) {
			return;
		}
		
		String fileName = req.getServletContext()
				.getRealPath("/WEB-INF/glasanje-rezultati.txt");
		Path path = Paths.get(fileName);
		
		if (!Files.exists(path)) {
			Files.createFile(path);
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i < 7; i++) sb.append(i).append("\t0\n");
			sb.setLength(sb.length() - 1);
	        try (OutputStream os = Files.newOutputStream(path)) {
	        	os.write(sb.toString().getBytes(StandardCharsets.UTF_8));        	
	        }
		}
		
		String text = null;
		final String ID = id;
		try (Stream<String> stream = Files.lines(path)) {
			text = stream.map(l -> l.split("\t")).map(l -> 
					l[0] + "\t" + (Integer.parseInt(l[1]) + (l[0].equals(ID) ? 1 : 0)))
			.collect(Collectors.joining("\n"));
		}

        try (OutputStream os = Files.newOutputStream(path)) {
        	os.write(text.getBytes(StandardCharsets.UTF_8));        	
        }
		
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}

}
