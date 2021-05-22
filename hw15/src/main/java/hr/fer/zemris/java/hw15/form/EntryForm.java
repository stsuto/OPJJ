package hr.fer.zemris.java.hw15.form;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.hw15.model.BlogEntry;

/**
 * Class representing the form used for entries.
 * 
 * @author stipe
 *
 */
public class EntryForm extends Form {

	/**
	 * User's nickname.
	 */
	private String title;
	/**
	 * 
	 */
	private String text;
	
	/**
	 * Constructor.
	 */
	public EntryForm() {
		this.title = "";
		this.text = "";
	}
	
	@Override
	public void loadFromHttpRequest(HttpServletRequest req) {
		this.title = prepare(req.getParameter("title"));
		this.text = prepare(req.getParameter("text"));
	}

	public void loadFromEntry(BlogEntry entry) {
		this.title = entry.getTitle();
		this.text = entry.getText();
	}

	public void saveToEntry(BlogEntry entry) {
		entry.setTitle(title);
		entry.setText(text);
	}

	@Override
	public void validate() {
		errors.clear();
				
		if(title.isEmpty()) {
			errors.put("title", "Title is necessary!");
		}
		
		if(text.isEmpty()) {
			errors.put("text", "Text is necessary!");
		}
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
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
}
