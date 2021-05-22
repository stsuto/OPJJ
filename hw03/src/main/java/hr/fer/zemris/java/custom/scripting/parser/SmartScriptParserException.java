package hr.fer.zemris.java.custom.scripting.parser;

/**
 * The <code>SmartScriptLexerException</code> class represents an exception derived from <code>RuntimeException</code>.
 * It is sent in case of unwated behaviour during lexical analysis.

 * @author stipe
 */
public class SmartScriptParserException extends RuntimeException {

	/**
	 * default serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public SmartScriptParserException() {
		super();
	}
	
	/**
	 * Constructor that sends the given message.
	 * @param message
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}
	
}
