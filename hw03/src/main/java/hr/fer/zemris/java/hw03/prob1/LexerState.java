package hr.fer.zemris.java.hw03.prob1;

/**
 * Enumeration <code>LexerState</code> represent two possible states of <code>Lexer</code>.
 *
 * @author stipe
 */
public enum LexerState {
	
	/**
	 * The state in which the Lexer is created. Differentiates words, numbers, and symbols.
	 */
	BASIC,
	/**
	 *  The state in which only words and a symbol '#' can be differentiated by type.
	 */
	EXTENDED
	
}
