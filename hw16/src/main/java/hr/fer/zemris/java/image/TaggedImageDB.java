package hr.fer.zemris.java.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Servlet context listener which offers static methods used for
 * keeping records about images, their tags and similar.
 * 
 * @author stipe
 *
 */
@WebListener
public class TaggedImageDB implements ServletContextListener {

	/**
	 * Constant containing the location of the descriptor.
	 */
	private static final String descriptor = "/WEB-INF/opisnik.txt";
	/**
	 * List of images.
	 */
	private static List<TaggedImage> images;
	/**
	 * List of tags.
	 */
	private static Map<String, List<TaggedImage>> tags;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		tags = new TreeMap<>();
		images = loadImages(sce);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	/**
	 * Loads the images using the given servlet context event to get
	 * the real path.
	 * 
	 * @param sce context event
	 * @return list of images
	 */
	private static List<TaggedImage> loadImages(ServletContextEvent sce) {
		List<String> descriptions = null;

		try {
			String path = sce.getServletContext().getRealPath(descriptor);
			descriptions = Files.readAllLines(Paths.get(path));
		} catch (IOException e) {
			throw new RuntimeException("Error while leading images!");
		}

		return parseDescriptions(descriptions);
	}

	/**
	 * Parses the read file to create images and load tags.
	 * 
	 * @param descriptions description lines
	 * @return list of images
	 */
	private static List<TaggedImage> parseDescriptions(List<String> descriptions) {
		List<TaggedImage> images = new ArrayList<>();
		int size = descriptions.size();

		for (int i = 0; i < size; i += 3) {
			TaggedImage image = createImage(descriptions, i);
			images.add(image);
			updateTags(image.getTags(), image);
		}

		return images;
	}

	/**
	 * Creates an image.
	 * 
	 * @param descriptions list of descriptor lines
	 * @param i index
	 * @return image
	 */
	private static TaggedImage createImage(List<String> descriptions, int i) {
		try {
			String[] imageTags = descriptions.get(i + 2).split(",");
			for (int j = 0; j < imageTags.length; j++) {
				imageTags[j] = imageTags[j].trim();
			}

			TaggedImage image = new TaggedImage(
				descriptions.get(i + 1), 
				descriptions.get(i), 
				imageTags
			);

			return image;

		} catch (IndexOutOfBoundsException e) {
			throw new RuntimeException("Invalid descriptor content.");
		}
	}

	/**
	 * Updates tags in a wy that if the tag is only now present for the 
	 * first time, it is added into the list and remembered.
	 * 
	 * @param imageTags tags
	 * @param image image
	 */
	private static void updateTags(String[] imageTags, TaggedImage image) {
		for (String tag : imageTags) {
			List<TaggedImage> taggedImages = tags.getOrDefault(tag, new ArrayList<>());
			taggedImages.add(image);
			tags.put(tag, taggedImages);
		}
	}

	/**
	 * Returns the number of images.
	 * @return
	 */
	public static int getNumberOfImages() {
		return images.size();
	}

	/**
	 * Returns the numebr of tags.
	 * 
	 * @return number of tags
	 */
	public static int getNumberOfTags() {
		return tags.size();
	}

	/**
	 * Gets images.
	 * 
	 * @return list of images
	 */
	public static List<TaggedImage> getImages() {
		return images;
	}

	/**
	 * Returns a set of tags.
	 * @return set of tags
	 */
	public static Set<String> getTags() {
		return tags.keySet();
	}

	/**
	 * Gets all images with the given tag.
	 * 
	 * @param tag tag
	 * @return list of images
	 */
	public static List<TaggedImage> getImagesWithTag(String tag) {
		return tags.get(tag);
	}

	/**
	 * Gets image present on the given path.
	 * 
	 * @param imagePath
	 * @return
	 */
	public static TaggedImage getImage(String imagePath) {
		for	(TaggedImage image : images) {
			if (image.getPath().equals(imagePath)) {
				return image;
			}
		}
		return null;
	}

}