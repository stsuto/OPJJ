package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class {@code GlasanjeServlet} represents the main voting page.
 * 
 * @author stipe
 *
 */
@WebServlet("/glasanje")
public class GlasanjeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext()
				.getRealPath("/WEB-INF/glasanje-definicija.txt");
		
		List<Band> bands = Files.lines(Paths.get(fileName))
			.map(l -> l.split("\t"))
			.sorted((f,s) -> f[0].compareTo(s[0]))
			.map(l -> new Band(l[0], l[1]))
			.collect(Collectors.toList());
		
		req.setAttribute("bands", bands);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
	
	/**
	 * Structure representing some basic band informations.
	 * 
	 * @author stipe
	 *
	 */
	public static class Band {
		/**
		 * Band id.
		 */
		String id;
		/**
		 * Band name.
		 */
		String name;
		
		/**
		 * Constructor.
		 * 
		 * @param id {@link #id}
		 * @param name {@link #name}
		 */
		public Band(String id, String name) {
			this.id = id;
			this.name = name;
		}

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}	
	}
	
}
