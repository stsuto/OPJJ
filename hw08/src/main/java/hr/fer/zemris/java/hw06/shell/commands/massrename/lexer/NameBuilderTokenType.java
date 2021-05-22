package hr.fer.zemris.java.hw06.shell.commands.massrename.lexer;

/**
 * This enumeration represents the types of {@link NameBuilderToken} produced by the {@link NameBuilderLexer}.
 * 
 * @author stipe
 *
 */
public enum NameBuilderTokenType {

	/**
	 * Type for the last token of the text.
	 */
	EOF,
	/**
	 * Type for a regular text token.
	 */	
	TEXT,
	/**
	 * Type for a number within the command.
	 */
	NUMBER,
	/**
	 * Type for the start of command within the expression.
	 */
	START_TAG,
	/**
	 * Type for the end of command.
	 */
	END_TAG,
	/**
	 * Type for the separator represented by ','.
	 */
	SEPARATOR
	
}
