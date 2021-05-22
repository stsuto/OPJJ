package hr.fer.zemris.java.hw15.form;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.hw15.model.BlogComment;

/**
 * Class representing the form used for entries.
 * 
 * @author stipe
 *
 */
public class CommentForm extends Form {

	/**
	 * Commenter email.
	 */
	private String email;
	/**
	 * Comment message.
	 */
	private String message;
	
	/**
	 * Constructor.
	 */
	public CommentForm() {
		this.email = "";
		this.message = "";
	}
	
	@Override
	public void loadFromHttpRequest(HttpServletRequest req) {
		this.email = prepare(req.getParameter("email"));
		this.message = prepare(req.getParameter("message"));
	}

	public void loadFromComment(BlogComment comment) {
		this.email = comment.getUsersEMail();
		this.message = comment.getMessage();
	}

	public void saveToComment(BlogComment comment) {
		comment.setUsersEMail(email);
		comment.setMessage(message);
	}

	@Override
	public void validate() {
		errors.clear();
				
		if(email.isEmpty()) {
			errors.put("email", "email is necessary!");
		}
		
		if(message.isEmpty()) {
			errors.put("comment", "comment is necessary!");
		}
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	
}
