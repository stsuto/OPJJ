package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * The <code>SmartScriptLexerException</code> class represents an exception derived from <code>RuntimeException</code>.
 * It is sent in case of unwated behaviour during lexical analysis.

 * @author stipe
 */
public class SmartScriptLexerException extends RuntimeException {

	/**
	 * default serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public SmartScriptLexerException() {
		super();
	}
	
	/**
	 * Constructor that sends the given message.
	 * @param message
	 */
	public SmartScriptLexerException(String message) {
		super(message);
	}
	
}
