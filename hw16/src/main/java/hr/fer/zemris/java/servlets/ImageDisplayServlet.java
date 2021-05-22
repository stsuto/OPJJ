package hr.fer.zemris.java.servlets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet which fetches and displays images.
 * 
 * @author stipe
 *
 */
@WebServlet("/servlet/imagedisplayer")
public class ImageDisplayServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constant containing thumbnail images folder location.
	 */
	private static final String THUMBNAIL_PATH = "/WEB-INF/thumbnails";
	/**
	 * Constant containing original images folder location.
	 */
	private static final String ORIGINAL_PATH = "/WEB-INF/slike";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/jpg");
		
		try (OutputStream os = resp.getOutputStream()) {
			String path = req.getParameter("path");
			String size = req.getParameter("size");
			
			String location = null;
			if ("small".equals(size)) {
				location = THUMBNAIL_PATH;
			} else if ("large".equals(size)) {
				location = ORIGINAL_PATH;
			} else {
				throw new RuntimeException("Invalid parameters.");
			}
			
			Path imagePath = Paths.get(req.getServletContext().getRealPath(location)).resolve(path);
			
			BufferedImage image = ImageIO.read(Files.newInputStream(imagePath));
			ImageIO.write(image, "jpg", os);
		}
	}
	
}
