package hr.fer.zemris.java.hw03.prob1;

/**
 * The <code>LexerException</code> class represents an exception derived from <code>RuntimeException</code>.
 * It is sent in case of unwated behaviour during lexical analysis.

 * @author stipe
 */
public class LexerException extends RuntimeException {

	/**
	 * default serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public LexerException() {
		super();
	}
	
	/**
	 * Constructor that sends the given message.
	 * @param message
	 */
	public LexerException(String message) {
		super(message);
	}
	
}
