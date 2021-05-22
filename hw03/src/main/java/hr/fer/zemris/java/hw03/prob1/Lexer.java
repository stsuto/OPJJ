package hr.fer.zemris.java.hw03.prob1;

import java.util.Objects;

/**
 * The <code>Lexer</code> class represents a system for lexical analysis.
 * <code>Lexer</code> gets an input of a document as <code>String</code> and creates a sequence of <code>Tokens</code>.
 * <code>Lexer</code> has two states that differ in the rules of the analysis.
 * @see hr.fer.zemris.java.hw03.prob1.Token
 *   
 * @author stipe
 */
public class Lexer {
	
	/**
	 * input as array of characters
	 */
	private char[] data;
	/**
	 * current token
	 */
	private Token token;
	/**
	 * current index in input array
	 */
	private int currentIndex;
	/**
	 * current state of Lexer
	 */
	private LexerState state;
	
	/**
	 * Constructor that recieves the text to be tokenized as String and creates a new instance of Lexer.
	 * @param text String of the text to be tokenized
	 * @throws NullPointerExceptionException if given document is null
	 */
	public Lexer(String text) {
		this.data = Objects.requireNonNull(text).toCharArray();
		this.currentIndex = 0;
		this.state = LexerState.BASIC;
	}
	
	/**
	 * Generates and returns the next token of the input document.
	 * @return new Token that was created from current position in input document
	 * @throws LexerException if there are no more tokens, but another token is asked for, or if escape sequence is invalid, or if input contains a number not parsable to long
	 */
	public Token nextToken() {
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("No more tokens left!");
		}
		
		skipBlanks(); // Whitespace is not made into tokens.
		
		if (currentIndex >= data.length) { // If the data is done, send one last token of type EOF
			token = new Token(TokenType.EOF, null);
			return getToken();
		}
		
		// Two first of two states of Lexer that follow different analysis
		if (state.equals(LexerState.BASIC)) {
			// If the character is a letter or a part of an escape sequence.
			if (Character.isLetter(data[currentIndex]) || checkEscapeConditions()) {
				basicWordLexer(); // A word token is created
				
			} else if (Character.isDigit(data[currentIndex])) {
				basicNumberLexer(); // Number tokens follow different rules
				
			} else {
				symbolLexer(); // If the token isnt a word or a number, it's a symbol
			}
				
		// The second state of Lexer
		} else if (state.equals(LexerState.EXTENDED)) {
			if (data[currentIndex] != '#') { // If the character is anything except '#', make a word token out of it
				extendedWordLexer();
								
			} else { // If it's a '#' make a single character symbol token
				symbolLexer();
			}	
		}
		
		return getToken(); // return token; works too
	}
	
	/**
	 * Makes a word token with value of type String.
	 * @throws LexerException if escape sequence is invalid
	 * @see #nextToken()
	 */
	private void basicWordLexer() {
		int firstChar = currentIndex; // index of first char that fits the word criteria
		
		do {
			if (checkEscapeConditions()) { // If the character is the start of an escape sequence, "remove" that character from the array and increase the counter so the escaping character gets in
				data[currentIndex] = ' ';
				currentIndex += 2;
			} else {
				currentIndex++; // Otherwise just do the usual increment.
			}
		} while (currentIndex < data.length && (Character.isLetter(data[currentIndex]) || checkEscapeConditions())); // if its a letter or another escape sequence
			
		int lastChar = currentIndex; // index of first char that doesn't fit the word criteria
			
		String value = new String(data, firstChar, lastChar - firstChar); // String between the two indices
		token = new Token(TokenType.WORD, value.replace(" ", ""));
	}
	
	/**
	 * Makes a word token with value of type String.
	 * @throws LexerException if input contains a number not parsable to long
	 * @see #nextToken()
	 */
	private void basicNumberLexer() {
		int firstChar = currentIndex++; // index of first char that fits the word criteria
		
		while (currentIndex < data.length && Character.isDigit(data[currentIndex])) {
			currentIndex++;
		}
		int lastChar = currentIndex; // index of first char that doesn't fit the token criteria
		
		String stringValue = new String(data, firstChar, lastChar - firstChar);
		Long value;
		try {
			value = Long.parseLong(stringValue); // If it can be parsed to long, it's a proper value	
		} catch (NumberFormatException ex) {
			throw new LexerException("Improper input!"); // Otherwise throws exception
		}
		token = new Token(TokenType.NUMBER, value);
	}
	
	private void symbolLexer() {
		Character value = Character.valueOf(data[currentIndex++]); // Value of only one character
		token = new Token(TokenType.SYMBOL, value);
		
		if (value.equals('#')) { // If taht character is '#', the lexer changes state
			changeState();
		}
	}
	
	/**
	 * Makes a word token with value of type String. Can contain any character except '#'.
	 * @see #nextToken()
	 */
	private void extendedWordLexer() {
		int firstChar = currentIndex++;
		
		while (currentIndex < data.length && data[currentIndex] != ' ' && data[currentIndex] != '#') {
			currentIndex++;
		}
		
		int lastChar = currentIndex;
		
		String value = new String(data, firstChar, lastChar - firstChar);
		token = new Token(TokenType.WORD, value);
	}

	/**
	 * Returns current token without generating a new one. Can be called multiple times for each token.
	 * @return current Token
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Sets the lexer's state.
	 * @param state the state to which the lexer is set
	 * @see #changeState()
	 */
	public void setState(LexerState state) {
		this.state = Objects.requireNonNull(state);
	}
	
	/**
	 * Changes the lexer's state from one to the other.
	 * @see #nextToken()
	 */
	public void changeState() {
		if (state.equals(LexerState.BASIC)) {
			setState(LexerState.EXTENDED);
		} else {
			setState(LexerState.BASIC);
		}
	}
	
	/**
	 * Skips all whitespace as they should not be made into a token.
	 * @see #nextToken()
	 */
	private void skipBlanks() {
		while (currentIndex < data.length) {
			char c = data[currentIndex];
			if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
				currentIndex++;
				continue;
			}
			break;
		}
	}
	
	/**
	 * Checks if the upcoming sequence of chars form a valid escape sequence.
	 * @return <code>true</code> if the sequence is valid, <code>false</code> if there is no escape sequence
	 * @throws LexerException if the escape sequence is invalid
	 * @see #nextToken()
	 * @see #basicWordLexer()
	 */
	private boolean checkEscapeConditions() { 	// If the character is '\', then the character after it must be checked if it's a number or another '\' as they make an escape sequence.
		if (data[currentIndex] == '\\') {
			if (currentIndex == data.length - 1 || !(Character.isDigit(data[currentIndex + 1]) || data[currentIndex + 1] == '\\')) {
				throw new LexerException("Incorrect escape sequence!");
			} else {
				return true;
			}
		}
		return false;
	}
}