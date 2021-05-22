package hr.fer.zemris.java.hw05.db.lexer;

/**
 * This enumeration represents the types of {@link QueryToken} produced by the {@link QueryLexer}.
 * 
 * @author stipe
 *
 */
public enum QueryTokenType {

	/**
	 * Type for the last token of the text.
	 */
	EOF,
	/**
	 * Type for a string literal token.
	 */	
	STRING,
	/**
	 * Type for a token determining the attribute of the record.
	 * Possible attributes are jmbag, lastName and firstName.
	 */
	ATTRIBUTE,
	/**
	 * Type for an operator in the query.
	 * Possible operators are >, <, =, !=, >=, <=, and LIKE.
	 */
	OPERATOR,
	/**
	 * Type for the logical operator AND.
	 */
	AND
	
}
