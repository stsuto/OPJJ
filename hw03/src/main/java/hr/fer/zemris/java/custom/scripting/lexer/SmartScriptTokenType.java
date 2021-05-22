package hr.fer.zemris.java.custom.scripting.lexer;

public enum SmartScriptTokenType {

	/**
	 * type for the last token of the text
	 */
	EOF,
	/**
	 * type for a token interpreted as a string
	 */	
	STRING,
	/**
	 * type for a token interpreted as a double
	 */	
	DOUBLE,
	/**
	 * type for a token interpreted as an int
	 */	
	INT,
	/**
	 * type for a token with a single character symbol
	 */
	SYMBOL,
	/**
	 * type for a token with a single character '{'
	 */
	OPEN_PARENTHESES,
	/**
	 * type for a token with a single character '$'
	 */
	TAG,
	/**
	 * type for a token with a single character '}'
	 */
	CLOSE_PARENTHESES,
	/**
	 * type for a token with a keyword or symbol '='
	 */
	KEY,
	/**
	 * type for a token of a variable
	 */
	VARIABLE,
	/**
	 * type for a token with a single character value representing an operator (+,-,*,/,^)
	 */
	OPERATOR,
	/**
	 * type for a token of a function which starts with '@'
	 */
	FUNCTION,
	/**
	 * type for a token with the value of any whitespace
	 */
	WHITESPACE
	
}
