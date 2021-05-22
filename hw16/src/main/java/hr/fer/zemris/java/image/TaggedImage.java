package hr.fer.zemris.java.image;

import java.util.Objects;

/**
 * Class {@code TaggedImage} represents an image which, in addition 
 * to it's name and path location, also contains tags which describe
 * the image.
 * 
 * @author stipe
 *
 */
public class TaggedImage {
	
	/**
	 * Image's name.
	 */
	private String name;
	/**
	 * Image's path.
	 */
	private String path;
	/**
	 * Image's tags.
	 */
	private String[] tags;
		
	/**
	 * Constructor.
	 * 
	 * @param name name
	 * @param path path
	 * @param tags tags
	 */
	public TaggedImage(String name, String path, String[] tags) {
		this.name = Objects.requireNonNull(name, "Image name mustn't be null!");
		this.path = Objects.requireNonNull(path, "Image path mustn't be null!");
		this.tags = Objects.requireNonNull(tags, "Image tags mustn't be null!");
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the tags
	 */
	public String[] getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(String[] tags) {
		this.tags = tags;
	}

}

