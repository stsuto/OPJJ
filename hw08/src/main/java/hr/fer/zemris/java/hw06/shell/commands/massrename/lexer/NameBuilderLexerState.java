package hr.fer.zemris.java.hw06.shell.commands.massrename.lexer;

/**
 * Enumeration {@code NameBuilderLexerState} represent two possible states of {@code NameBuilderLexer}
 *
 * @author stipe
 */
public enum NameBuilderLexerState {

	/**
	 * The state in which the NameBuilderLexer is created.
	 * In this state NameBuilderLexer creates simple text tokens.
	 */
	TEXT,
	/**
	 * The state used for commands within expressions.
	 */
	TAG
	
}
