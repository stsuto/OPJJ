package hr.fer.zemris.java.hw14.model;

/**
 * Class representing a model of a poll table entry.
 * 
 * @author stipe
 *
 */
public class Poll {
	
	/**
	 * Poll id.
	 */
	private long id;
	/**
	 * Poll title.
	 */
	private String title;
	/**
	 * Poll message.
	 */
	private String message;
	
	/**
	 * Constructor initializing properties.
	 * 
	 * @param id
	 * @param title
	 * @param message
	 */
	public Poll(long id, String title, String message) {
		this.id = id;
		this.title = title;
		this.message = message;
	}

	public Poll() {
		super();
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "ID=" + id + ", Title=" + title + ", Message=" + message;
	}
	
}
