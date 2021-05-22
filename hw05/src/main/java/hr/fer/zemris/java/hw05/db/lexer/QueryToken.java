package hr.fer.zemris.java.hw05.db.lexer;

/**
 * Class <code>QueryToken</code> represents a single product of lexical analysis.
 * It has two read-only properties, type and value.
 * 
 * @author stipe
 */
public class QueryToken {

	/**
	 * type of the created token
	 */
	private QueryTokenType type;
	/**
	 * value of the created token
	 */
	private String value;
	
	/**
	 * Constructor that creates a new <code>QueryToken</code> with regards to the given parameters.
	 * @param type the type of the token to be created
	 * @param value Object value of the token to be created
	 * @throws IllegalArgumentException if the given type is null
	 */
	public QueryToken(QueryTokenType type, String value) {
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
	public String getValue() {
		return value;
	}
	
	/**
	 * Returns the type of this token.
	 * @return type of this token as TokenType
	 */
	public QueryTokenType getType() {
		return type;
	}

}

