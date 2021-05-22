package hr.fer.zemris.java.hw15.form;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.hw15.model.BlogUser;
import hr.fer.zemris.java.hw15.util.Util;

public class LoginForm extends Form {

	/**
	 * User's nickname.
	 */
	private String nick;
	/**
	 * 
	 */
	private String passwordHash;
	
	/**
	 * Constructor.
	 */
	public LoginForm() {
		this.nick = "";
	}
	
	@Override
	public void loadFromHttpRequest(HttpServletRequest req) {
		this.nick = prepare(req.getParameter("nick"));
		this.passwordHash = Util.encrypt(prepare(req.getParameter("password")));
	}

	/**
	 * Loads and initializes form properties using parameters from
	 * the given {@link BlogUser}.
	 * 
	 * @param user object which contains the original data
	 */
	public void loadFromUser(BlogUser user) {
		this.nick = user.getNick();
		this.passwordHash = user.getPasswordHash();
	}

	/**
	 * Saves the property values from this form to the given user's properties.
	 * Method shouldn't be called unless the form has previously been validated
	 * and no errors were found.
	 * 
	 * @param user object to be loaded
	 */
	public void saveToUser(BlogUser user) {
		user.setNick(nick);
		user.setPasswordHash(passwordHash);
	}

	@Override
	public void validate() {
		errors.clear();
				
		if(nick.isEmpty()) {
			errors.put("nick", "Nickname is necessary!");
		}
		
		if(passwordHash == null) {
			errors.put("password", "Password is necessary!");
		}
	}

	/**
	 * @return the nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * @param nick the nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @return the passwordHash
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * @param passwordHash the passwordHash to set
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

}
