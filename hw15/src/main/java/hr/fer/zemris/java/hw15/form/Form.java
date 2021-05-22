package hr.fer.zemris.java.hw15.form;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public abstract class Form {

	/**
	 * Map containing errors in which keys are preperty names and values are
	 * error messages.
	 */
	protected Map<String, String> errors = new HashMap<>();

	/**
	 * Checks whether or not any of the properties contains an error.
	 * 
	 * @return <code>true</code> if an error is present, <code>false</code> otherwise.
	 */
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	/**
	 * Checks if the property has an error.
	 * 
	 * @param name name of the property which is being tested for errors
	 * @return <code>true</code> if an error is present, <code>false</code> otherwise.
	 */
	public boolean hasError(String name) {
		return errors.containsKey(name);
	}
	
	/**
	 * Returns the error message for the given property name.
	 * 
	 * @param name name of the property which is being tested for errors
	 * @return error message or <code>null</code> if the property doesn't have an error
	 */
	public String getError(String name) {
		return errors.get(name);
	}
	
	/**
	 * Maps an error message to the property name.
	 * 
	 * @param name property name
	 * @param message error message
	 */
	public void setError(String name, String message) {
		errors.put(name, message);
	}
	
	/**
	 * Loads and initializes form properties using parameters 
	 * from {@link HttpServletRequest}.
	 * 
	 * @param req object with parameters
	 */
	public abstract void loadFromHttpRequest(HttpServletRequest req);

	/**
	 * Performs the validation of the form. The form must be loaded beforehand.
	 * Checks the semantic correctness of all data and registers errors if necessary.
	 * 
	 */
	public abstract void validate();
	
	/**
	 * Method which null value {@code String} objects converts into blank strings.
	 * 
	 * @param s string
	 * @return the trimmed given string if it is not <code>null</code>, 
	 * an empty string otherwise.
	 */
	protected String prepare(String s) {
		if (s == null) return "";
		return s.trim();
	}

	/**
	 * @return the errors
	 */
	public Map<String, String> getErrors() {
		return errors;
	}

	/**
	 * @param errors the errors to set
	 */
	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

}
