package hr.fer.zemris.java.hw06.shell.commands.massrename.lexer;

import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.util.Util;

/**
 * The {@code NameBuilderLexer} class represents a system for lexical analysis.
 * {@code NameBuilderLexer} gets an input of a document as {@code String} and
 * creates a sequence of {@link NameBuilderToken}.
 * 
 * @author stipe
 */
public class NameBuilderLexer {

	/**
	 * Input as array of characters.
	 */
	private char[] data;
	/**
	 * The current token.
	 */
	private NameBuilderToken token;
	/**
	 * The current index in input array.
	 */
	private int currentIndex;
	/**
	 * Lexer's current state.
	 */
	private NameBuilderLexerState state;

	/**
	 * Constructor that recieves the text to be tokenized as String and creates a
	 * new instance of {@code NameBuilderLexer}.
	 * 
	 * @param text String of the text to be tokenized
	 * @throws NullPointerExceptionException if given document is null
	 */
	public NameBuilderLexer(String text) {
		data = Util.requireNonNull(text, "Expression.").toCharArray();
		currentIndex = 0;
		state = NameBuilderLexerState.TEXT;
	}

	/**
	 * Returns current token without generating a new one. Can be called multiple
	 * times for each token.
	 * 
	 * @return current NameBuilderToken
	 */
	public NameBuilderToken getToken() {
		return token;
	}

	/**
	 * Sets the lexer's state.
	 * 
	 * @param state the state to which the lexer is set
	 * @see #changeState()
	 */
	public void setState(NameBuilderLexerState state) {
		this.state = Util.requireNonNull(state, "Lexer state");
	}

	/**
	 * Skips all whitespace as they should not be made into a token.
	 * 
	 * @see #nextToken()
	 */
	private void skipBlanks() {
		while (currentIndex < data.length) {
			if (Character.isWhitespace(data[currentIndex])) {
				currentIndex++;
				continue;
			}
			return;
		}
	}

	/**
	 * Generates and returns the next token of the input document.
	 * 
	 * @return new NameBuilderToken that was created from current position in input
	 *         document
	 * @throws ShellIOException if there are no more tokens, but another token is
	 *                          asked for, or in case of invalid input
	 */
	public NameBuilderToken nextToken() { // No more tokens, throws exception
		if (token != null && token.getType() == NameBuilderTokenType.EOF) {
			throw new ShellIOException("No more tokens left!");
		}
		skipBlanks();
		if (!isIndexLegal()) { // If the data is done, send one last token of type EOF
			return (token = new NameBuilderToken(NameBuilderTokenType.EOF, null));
		}

		if (state == NameBuilderLexerState.TEXT) {
			token = getTextStateToken();
		} else {
			token = getTagStateToken();
		}

		return token;
	}

	/**
	 * Gets the next token in text state.
	 * 
	 * @return next token
	 */
	private NameBuilderToken getTextStateToken() {
		if (isCommandStart()) {
			currentIndex += 2;
			return new NameBuilderToken(NameBuilderTokenType.START_TAG, "${");
		}

		int firstIndex = currentIndex++;
		while (isIndexLegal() && !isCommandStart()) {
			currentIndex++;
		}
		String value = new String(data, firstIndex, currentIndex - firstIndex);
		return new NameBuilderToken(NameBuilderTokenType.TEXT, value);
	}

	/**
	 * Gets the next token in tag state.
	 * 
	 * @return next token
	 */
	private NameBuilderToken getTagStateToken() {
		if (isCommandEnd()) {
			return new NameBuilderToken(NameBuilderTokenType.END_TAG, 
								String.valueOf(data[currentIndex++]));

		} else if (Character.isDigit(data[currentIndex])) {
			int firstIndex = currentIndex++;
			while (isIndexLegal() && Character.isDigit(data[currentIndex])) {
				currentIndex++;
			}
			String value = new String(data, firstIndex, currentIndex - firstIndex);
			return new NameBuilderToken(NameBuilderTokenType.NUMBER, value);

		} else if (isSeparator()) {
			currentIndex++;
			return new NameBuilderToken(NameBuilderTokenType.SEPARATOR, ",");
		}

		throw new ShellIOException("Invalid command within expression!");
	}

	/**
	 * Checks if the command tag is starting.
	 * 
	 * @return {@code true} if the command tag start is next, {@code false} otherwise
	 */
	private boolean isCommandStart() {
		return data[currentIndex] == '$' 
				&& currentIndex + 1 <= data.length 
				&& data[currentIndex + 1] == '{';
	}

	/**
	 * Checks if the command tag is ending.
	 * 
	 * @return {@code true} if the command tag end is next, {@code false} otherwise
	 */
	private boolean isCommandEnd() {
		return data[currentIndex] == '}';
	}

	/**
	 * Checks if the next token should be a separator token.
	 * 
	 * @return {@code true} if separator is next, {@code false} otherwise
	 */
	private boolean isSeparator() {
		return data[currentIndex] == ',';
	}

	/**
	 * Checks if the current index is legal.
	 * 
	 * @param data    array of characters which are being processed
	 * @param current the index which is tested if it is in the array
	 * @return {@code true} if the character is legal, {@code false} otherwise
	 */
	private boolean isIndexLegal() {
		return currentIndex < data.length;
	}

}
