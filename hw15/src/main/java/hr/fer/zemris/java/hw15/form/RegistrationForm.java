package hr.fer.zemris.java.hw15.form;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.hw15.model.BlogUser;
import hr.fer.zemris.java.hw15.util.Util;

public class RegistrationForm extends Form {

	/**
	 * User's first name.
	 */
	private String firstName;
	/**
	 * User's last name.
	 */
	private String lastName;
	/**
	 * User's email.
	 */
	private String email;
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
	public RegistrationForm() {
		this.firstName = "";
		this.lastName = "";
		this.email = "";
		this.nick = "";
	}
	
	/**
	 * Loads and initializes form properties using parameters 
	 * from {@link HttpServletRequest}.
	 * 
	 * @param req object with parameters
	 */
	@Override
	public void loadFromHttpRequest(HttpServletRequest req) {
		this.firstName = prepare(req.getParameter("firstName"));
		this.lastName = prepare(req.getParameter("lastName"));
		this.email = prepare(req.getParameter("email"));
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
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.nick = user.getNick();
		this.nick = user.getPasswordHash();
	}

	/**
	 * Saves the property values from this form to the given user's properties.
	 * Method shouldn't be called unless the form has previously been validated
	 * and no errors were found.
	 * 
	 * @param user object to be loaded
	 */
	public void saveToUser(BlogUser user) {
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setNick(nick);
		user.setPasswordHash(passwordHash);
	}
	
	/**
	 * Performs the validation of the form. The form must be loaded beforehand.
	 * Checks the semantic correctness of all data and registers errors if necessary.
	 * 
	 */
	@Override
	public void validate() {
		errors.clear();
		
		if(firstName.isEmpty()) {
			errors.put("firstName", "First name is necessary!");
		}
		
		if(lastName.isEmpty()) {
			errors.put("lastName", "Last name is necessary!");
		}

		if(this.email.isEmpty()) {
			errors.put("email", "EMail is necessary!");
		} else {
			int l = email.length();
			int p = email.indexOf('@');
			if(l<3 || p==-1 || p==0 || p==l-1) {
				errors.put("email", "EMail is not of proper format.");
			}
		}
		if(nick.isEmpty()) {
			errors.put("nick", "Nickname is necessary!");
		}
		
		if(passwordHash == null) {
			errors.put("password", "Password is necessary!");
		}
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
