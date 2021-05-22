package hr.fer.zemris.java.hw06.crypto;

/**
 * The <code>CryptingException</code> class represents an exception derived from <code>RuntimeException</code>.
 * It is sent in case of unwated behaviour during the crypting process.

 * @author stipe
 */
public class CryptingException extends RuntimeException {

	/**
	 * default serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public CryptingException() {
		super();
	}
	
	/**
	 * Constructor that sends the given message.
	 * @param message
	 */
	public CryptingException(String message) {
		super(message);
	}
	
}
