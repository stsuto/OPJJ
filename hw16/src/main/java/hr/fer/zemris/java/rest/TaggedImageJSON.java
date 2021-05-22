package hr.fer.zemris.java.rest;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;

import hr.fer.zemris.java.image.TaggedImage;
import hr.fer.zemris.java.image.TaggedImageDB;

/**
 * Class {@code TaggedImageJSON} is a REST-enabling class which uses
 * Gson for work with JSON.
 * 
 * @author stipe
 *
 */
@javax.ws.rs.Path("/imagej")
public class TaggedImageJSON {

	/**
	 * Constant containing thumbnail folder location.
	 */
	private static final String THUMBNAILS_PATH = "/WEB-INF/thumbnails";
	/**
	 * Constant containing original images folder location.
	 */
	private static final String IMAGES_PATH = "/WEB-INF/slike";
	
	/**
	 * Servlet's context.
	 */
	@Context
	private ServletContext sc;
	
	/**
	 * Gets all tags.
	 * @return all tags
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTags() {
		Set<String> set = TaggedImageDB.getTags();
		
		String[] tags = new String[set.size()];
		set.toArray(tags);
		
		Gson gson = new Gson();
		String jsonText = gson.toJson(tags);
	
		return Response.status(Status.OK).entity(jsonText).build();
	}
	
	/**
	 * 
	 * Gets all thumbnails with the given tag.
	 * @param tag tag
	 * @return thumbnails with tag
	 * @throws IOException
	 */
	@javax.ws.rs.Path("{tag}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getThumbnails(@PathParam("tag") String tag) throws IOException {
		List<TaggedImage> list = TaggedImageDB.getImagesWithTag(tag);
	
		TaggedImage[] images = new TaggedImage[list.size()];
		list.toArray(images);

		Path thumbPath = Paths.get(sc.getRealPath(THUMBNAILS_PATH));
		Path imagesPath = Paths.get(sc.getRealPath(IMAGES_PATH));

		if (!Files.exists(thumbPath)) {
			Files.createDirectory(thumbPath);
		}
		
		for (TaggedImage image : images) {
			Path thPath = thumbPath.resolve(image.getPath());
			Path imgPath = imagesPath.resolve(image.getPath());

			if (!Files.exists(thPath)) {
				createThumbnail(thPath, imgPath);				
			}
		}
		
		Gson gson = new Gson();
		String jsonText = gson.toJson(images);
		
		return Response.status(Status.OK).entity(jsonText).build();
	}

	/**
	 * Creates a thumbnail if it is it's first usage and it wasn't previuosly created.
	 * 
	 * @param thPath thumbnail path
	 * @param imgPath original image path
	 * @throws IOException
	 */
	private void createThumbnail(Path thPath, Path imgPath) throws IOException {
		try (InputStream is = Files.newInputStream(imgPath);
			 OutputStream os = Files.newOutputStream(thPath)) {
				
			BufferedImage original = ImageIO.read(is);
			BufferedImage thumbnail = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);

			Graphics2D g2d = thumbnail.createGraphics();
			g2d.drawImage(original, 0, 0, 150, 150, null);
			ImageIO.write(thumbnail, "jpg", os);
			g2d.dispose();
		}			
	}
	
	/**
	 * Gets the image on the given path.
	 * 
	 * @param path path
	 * @return response with the image
	 */
	@javax.ws.rs.Path("/images/{path}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getImage(@PathParam("path") String path) {
		Gson gson = new Gson();
		String jsonText = gson.toJson(TaggedImageDB.getImage(path));
		
		return Response.status(Status.OK).entity(jsonText).build();
	}
	
}
