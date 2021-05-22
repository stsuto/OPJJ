package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumeration <code>SmartScriptLexerState</code> represent two possible states of <code>SmartScriptLexer</code>.
 *
 * @author stipe
 */
public enum SmartScriptLexerState {

	/**
	 * The state in which the SmartScriptLexer is created.
	 * In this state SmartScriptLexer creates a token for every non-escaped character.
	 */
	TEXT,
	/**
	 * The state in which the SmartScriptLexer ignores whitespace and generates tokens used for expressions.
	 */
	TAG
	
}
