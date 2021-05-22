package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

import hr.fer.zemris.java.hw03.prob1.LexerException;


/**
 * The <code>SmartScriptLexer</code> class represents a system for lexical analysis.
 * <code>SmartScriptLexer</code> gets an input of a document as <code>String</code> and creates a sequence of <code>SmartScriptToken</code>.
 * <code>SmartScriptLexer</code> has two states that differ in the rules of the analysis.
 * @see hr.fer.zemris.java.hw03.prob1.Token
 *   
 * @author stipe
 */
public class SmartScriptLexer {

	/**
	 * input as array of characters
	 */
	private char[] data;	/**
	 * current token
	 */
	private SmartScriptToken token;
	/**
	 * current index in input array
	 */
	private int currentIndex;
	/**
	 * current state of this lexer
	 */
	private SmartScriptLexerState state;
	
	/**
	 * Constructor that recieves the text to be tokenized as String and creates a new instance of SmartScriptLexer.
	 * @param text String of the text to be tokenized
	 * @throws NullPointerExceptionException if given document is null
	 */
	public SmartScriptLexer(String text) {
		this.data = Objects.requireNonNull(text).toCharArray();
		this.currentIndex = 0;
		this.state = SmartScriptLexerState.TEXT;
	}

	/**
	 * Returns current token without generating a new one. Can be called multiple times for each token.
	 * @return current SmartScriptToken
	 */
	public SmartScriptToken getToken() {
		return token;
	}
	
	/**
	 * Sets the lexer's state.
	 * @param state the state to which the lexer is set
	 * @see #changeState()
	 */
	public void setState(SmartScriptLexerState state) {
		this.state = Objects.requireNonNull(state);
	}
	
	/**
	 * Changes the lexer's state from one to the other.
	 * @see #nextToken()
	 */
	public void changeState() {
		if (state.equals(SmartScriptLexerState.TEXT)) {
			setState(SmartScriptLexerState.TAG);
		} else {
			setState(SmartScriptLexerState.TEXT);
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
	 * Generates and returns the next token of the input document.
	 * @return new SmartScriptToken that was created from current position in input document
	 * @throws SmartScriptLexerException if there are no more tokens, but another token is asked for, or if escape sequence is invalid, or if input contains a number not parsable to int or double
	 */
	public SmartScriptToken nextToken() { // No more tokens, throws exception
		if (token != null && token.getType() == SmartScriptTokenType.EOF) {
			throw new SmartScriptLexerException("No more tokens left!");
		}
		
		if (currentIndex >= data.length) { // If the data is done, send one last token of type EOF
			token = new SmartScriptToken(SmartScriptTokenType.EOF, null);
			return getToken();
		}
		
		
		// Two first of two states of Lexer that follow different analysis
		if (state.equals(SmartScriptLexerState.TEXT)) {
			
			if (Character.isLetter(data[currentIndex])) { // If the character is a letter, the token consists of all next characters that are also letters
				int firstChar = currentIndex; // index of first char that fits the word criteria
				
				while (currentIndex < data.length && Character.isLetter(data[currentIndex])) {
					currentIndex++;
				}
				
				int lastChar = currentIndex; // index of first char that doesn't fit the word criteria
				
				String value = new String(data, firstChar, lastChar - firstChar);
				token = new SmartScriptToken(SmartScriptTokenType.STRING, value);
				
			// Token with number is created if the first character is a digit or if it's a '-' immediately followed by a digit (negative number).
			} else if (Character.isDigit(data[currentIndex]) || (data[currentIndex] == '-' && Character.isDigit(data[currentIndex + 1]))) {
				setNumberToken();
			
			} else if (data[currentIndex] == ' ' || data[currentIndex] == '\t' || data[currentIndex] == '\n' || data[currentIndex] == '\r') {
				int firstChar = currentIndex++;
				
				// While the characters are blank, continue the token
				while (currentIndex < data.length && (data[currentIndex] == ' ' || data[currentIndex] == '\t' || data[currentIndex] == '\n' || data[currentIndex] == '\r')) {
					currentIndex++;
				}
				
				int lastChar = currentIndex;
				
				String stringValue = new String(data, firstChar, lastChar - firstChar);	
				token = new SmartScriptToken(SmartScriptTokenType.WHITESPACE, stringValue);
				
			} else if (data[currentIndex] == '{') {
				currentIndex++;
				token = new SmartScriptToken(SmartScriptTokenType.OPEN_PARENTHESES, Character.valueOf('{'));
			
			} else if (data[currentIndex] == '$') {
				if (currentIndex != 0 && data[currentIndex - 1] == '{') {
					setState(SmartScriptLexerState.TAG);
				}
				currentIndex++;
				token = new SmartScriptToken(SmartScriptTokenType.TAG, Character.valueOf('$'));
				
			} else {
				if (checkEscapeConditions()) {
					currentIndex++;
				}
				token = new SmartScriptToken(SmartScriptTokenType.SYMBOL, Character.valueOf(data[currentIndex++]));
			}
		
		
		// The second state of the lexer
		} else {
			
			skipBlanks(); // In this state, whitespace characters dont form a token and can be ignored
			
			if (data[currentIndex] == '"') { // String starts and lasts until another " comes
				int firstChar = currentIndex;
				
				do {
					if (checkEscapeConditions()) { // If an escape sequence happened, jump over it
						currentIndex += 2;
					} else {
						currentIndex++; // Otherwise just do the usual increment.
					}
				} while (currentIndex < data.length && data[currentIndex] != '"');
				
				int lastChar = ++currentIndex;
				
				String value = new String(data, firstChar, lastChar - firstChar);
				token = new SmartScriptToken(SmartScriptTokenType.STRING, value.replace("\\\"", "\"").replace("\\\\", "\\"));
			
			// If a token within a tag starts with a letter it's a variable
			} else if (Character.isLetter(data[currentIndex])) {
				int firstChar = currentIndex++;
				
				// after the first letter, variables can contain letters, numbers and underscores
				while (currentIndex < data.length && (Character.isLetter(data[currentIndex]) || Character.isDigit(data[currentIndex]) || data[currentIndex] == '_')) {
					currentIndex++;
				}
				
				int lastChar = currentIndex;
				
				String value = new String(data, firstChar, lastChar - firstChar);
				if (value.equalsIgnoreCase("FOR") || value.equalsIgnoreCase("END")) { //"FOR" and "END" are special keywords
					token = new SmartScriptToken(SmartScriptTokenType.KEY, value);
				} else {
					token = new SmartScriptToken(SmartScriptTokenType.VARIABLE, value); // other variables are normal variables
				}
				
				
			} else if (Character.isDigit(data[currentIndex]) || (data[currentIndex] == '-' && Character.isDigit(data[currentIndex + 1]))) {
				setNumberToken();
			
			} else if ("+-*/^".contains(String.valueOf(data[currentIndex]))){
				token = new SmartScriptToken(SmartScriptTokenType.OPERATOR, Character.valueOf(data[currentIndex++]));
			
			} else if (data[currentIndex] == '=') {
				currentIndex++;
				token = new SmartScriptToken(SmartScriptTokenType.KEY, Character.valueOf('='));
						
			} else if (data[currentIndex] == '$') {
				currentIndex++;
				token = new SmartScriptToken(SmartScriptTokenType.TAG, Character.valueOf('$'));
			
			} else if (data[currentIndex] == '}') {
				if (currentIndex != 0 && data[currentIndex - 1] == '$') {
					setState(SmartScriptLexerState.TEXT);
				}
				currentIndex++;
				token = new SmartScriptToken(SmartScriptTokenType.CLOSE_PARENTHESES, Character.valueOf('}'));
					
			} else if (data[currentIndex] == '@') {
				int firstChar = currentIndex++;
								
				while (currentIndex < data.length && (Character.isLetter(data[currentIndex]) || Character.isDigit(data[currentIndex]) || data[currentIndex] == '_')) {
					currentIndex++;
				}
				
				int lastChar = currentIndex;
				
				String value = new String(data, firstChar, lastChar - firstChar);
				token = new SmartScriptToken(SmartScriptTokenType.FUNCTION, value);
				
				/**
				 * If the character is nothing from above, it is a simple symbol.
				 */
			} else {
				if (checkEscapeConditions()) { // If escape sequence is ahead, just pass those characters
					currentIndex++;
				}
				token = new SmartScriptToken(SmartScriptTokenType.SYMBOL, Character.valueOf(data[currentIndex++]));
			}	
		}
		
		return token;
	}
	
	/**
	 * Goes through data and creates a token of a number that can be either positive or negative, and either integer or double
	 * @throws SmartScriptLexerException if the number is not parsable to neither int nor double
	 * @see #nextToken()
	 */
	private void setNumberToken() {
		int firstChar = currentIndex++; // index of first char that fits the token criteria
		boolean hasDot = false; //The number can be a decimal number, but it can't contain more than one decimal dot
		
		// Number continues while the characters are digits or a first dot in the number that is immediately followed by digits (the decimal part of the number)
		while (currentIndex < data.length && (Character.isDigit(data[currentIndex]) || (!hasDot && data[currentIndex] == '.' && Character.isDigit(data[currentIndex + 1])))) {
			if (data[currentIndex] == '.') {
				hasDot = true; // If it's a first dot, the token can continue
			}
			currentIndex++;
		}
		
		int lastChar = currentIndex;
		
		String stringValue = new String(data, firstChar, lastChar - firstChar);					
		try {
			if (hasDot) { // Whether or not the number has a dot, it's an int or a double
				double value = Double.parseDouble(stringValue);
				token = new SmartScriptToken(SmartScriptTokenType.DOUBLE, value);
			} else {							
				int value = Integer.parseInt(stringValue); 				
				token = new SmartScriptToken(SmartScriptTokenType.INT, value);
			}
		} catch (NumberFormatException ex) {
			throw new SmartScriptLexerException("Improper input!");
		}
	}

	/**
	 * Checks if the upcoming sequence of chars form a valid escape sequence.
	 * @return <code>true</code> if the sequence is valid, <code>false</code> if there is no escape sequence
	 * @throws LexerException if the escape sequence is invalid
	 * @see #nextToken()
	 */
	private boolean checkEscapeConditions() { // If the character is '\', then the character after it must be checked if they make an escape sequence.
		if (state.equals(SmartScriptLexerState.TEXT)) {			
			if (data[currentIndex] == '\\') {
				if ("nrt".contains(String.valueOf(data[currentIndex + 1]))) {
					return false;
				} else if (currentIndex == data.length - 1 || !(data[currentIndex + 1] == '\\' || data[currentIndex + 1] == '{')) {
					throw new SmartScriptLexerException("Incorrect escape sequence!");
				} else {
					return true;
				}
			}
			return false;
		
		} else {
			if (data[currentIndex] == '\\') {
				if ("nrt".contains(String.valueOf(data[currentIndex + 1]))) {
					return false;
				} else if (currentIndex == data.length - 1 || !(data[currentIndex + 1] == '\\' || data[currentIndex + 1] == '"')) {
					throw new SmartScriptLexerException("Incorrect escape sequence!");
				} else {
					return true;
				}
			}
			return false;
		}
	}
}
