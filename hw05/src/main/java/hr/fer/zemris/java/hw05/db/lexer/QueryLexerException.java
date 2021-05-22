package hr.fer.zemris.java.hw05.db.lexer;

/**
 * The <code>QueryLexerException</code> class represents an exception derived from <code>RuntimeException</code>.
 * It is sent in case of unwated behaviour during lexical analysis.

 * @author stipe
 */
public class QueryLexerException extends RuntimeException {

	/**
	 * default serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public QueryLexerException() {
		super();
	}
	
	/**
	 * Constructor that sends the given message.
	 * @param message
	 */
	public QueryLexerException(String message) {
		super(message);
	}
	
}
