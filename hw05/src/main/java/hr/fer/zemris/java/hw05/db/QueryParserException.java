package hr.fer.zemris.java.hw05.db;

/**
 * The <code>QueryParserException</code> class represents an exception derived from <code>RuntimeException</code>.
 * It is sent in case of unwated behaviour during syntax analysis.

 * @author stipe
 */
public class QueryParserException extends RuntimeException {

	/**
	 * default serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public QueryParserException() {
		super();
	}
	
	/**
	 * Constructor that sends the given message.
	 * @param message
	 */
	public QueryParserException(String message) {
		super(message);
	}
	
}
