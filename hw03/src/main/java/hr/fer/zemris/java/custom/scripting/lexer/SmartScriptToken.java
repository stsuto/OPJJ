package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Class <code>SmartScriptToken</code> represents a single product of lexical analysis.
 * It has two read-only properties, type and value.
 * 
 * @author stipe
 */
public class SmartScriptToken {

	/**
	 * type of the created token
	 */
	private SmartScriptTokenType type;
	/**
	 * value of the created token
	 */
	private Object value;
	
	/**
	 * Constructor that creates a new <code>SmartScriptToken</code> with regards to the given parameters.
	 * @param type the type of the token to be created
	 * @param value Object value of the token to be created
	 * @throws IllegalArgumentException if the given type is null
	 */
	public SmartScriptToken(SmartScriptTokenType type, Object value) {
		if (type == null) {
			throw new IllegalArgumentException("Token type can't be null.");
		}
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Returns the value of this token.
	 * @return Object value  of this token
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Returns the type of this token.
	 * @return type of this token as TokenType
	 */
	public SmartScriptTokenType getType() {
		return type;
	}

}
