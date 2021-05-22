package hr.fer.zemris.java.hw06.shell.commands.massrename.lexer;

import hr.fer.zemris.java.hw06.shell.ShellIOException;

/**
 * Class <code>NameBuilderToken</code> represents a single product of lexical analysis.
 * It has two read-only properties, type and value.
 * 
 * @author stipe
 */
public class NameBuilderToken {

	/**
	 * type of the created token
	 */
	private NameBuilderTokenType type;
	/**
	 * value of the created token
	 */
	private String value;
	
	/**
	 * Constructor that creates a new <code>NameBuilderToken</code> with regards to the given parameters.
	 * @param type the type of the token to be created
	 * @param value Object value of the token to be created
	 * @throws ShellIOException if the given type is null
	 */
	public NameBuilderToken(NameBuilderTokenType type, String value) {
		if (type == null) {
			throw new ShellIOException("Token type can't be null.");
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
	public NameBuilderTokenType getType() {
		return type;
	}

}

