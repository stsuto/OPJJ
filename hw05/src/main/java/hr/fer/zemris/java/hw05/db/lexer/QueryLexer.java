package hr.fer.zemris.java.hw05.db.lexer;

import java.util.Objects;

/**
 * The <code>QueryLexer</code> class represents a system for lexical analysis.
 * <code>QueryLexer</code> gets an input of a document as <code>String</code> and creates a sequence of {@link QueryToken}.
 *   
 * @author stipe
 */
public class QueryLexer {

	/**
	 * Input as array of characters.
	 */
	private char[] data;
	/**
	 * The current token.
	 */
	private QueryToken token;
	/**
	 * The current index in input array.
	 */
	private int currentIndex;
	
	/**
	 * Constructor that recieves the text to be tokenized as String and creates a new instance of <code>QueryLexer</code>.
	 * @param text String of the text to be tokenized
	 * @throws NullPointerExceptionException if given document is null
	 */
	public QueryLexer(String text) {
		data = Objects.requireNonNull(text, "The query must not be null.").toCharArray();
		currentIndex = 0;
	}
	
	/**
	 * Returns current token without generating a new one. Can be called multiple times for each token.
	 * @return current SmartScriptToken
	 */
	public QueryToken getToken() {
		return token;
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
	 * @return new QueryToken that was created from current position in input document
	 * @throws QueryLexerException if there are no more tokens, but another token is asked for, or if escape sequence is invalid, or if input contains a number not parsable to int or double
	 */
	public QueryToken nextToken() { // No more tokens, throws exception
		if (token != null && token.getType() == QueryTokenType.EOF) {
			throw new QueryLexerException("No more tokens left!");
		}
		skipBlanks();
		if (currentIndex >= data.length) { // If the data is done, send one last token of type EOF
			token = new QueryToken(QueryTokenType.EOF, null);
			return getToken();
		}
		
		if (data[currentIndex] == '"') {
			int firstIndex = ++currentIndex;
			while (currentIndex < data.length && data[currentIndex] != '"') {
				currentIndex++;
			}
			int lastIndex = currentIndex++;
			String value = new String(data, firstIndex, lastIndex - firstIndex);
			token = new QueryToken(QueryTokenType.STRING, value);
		
		} else if (Character.isAlphabetic(data[currentIndex])) {
			int firstIndex = currentIndex++;
			while (currentIndex < data.length && Character.isAlphabetic(data[currentIndex])) {
				currentIndex++;
			}
			int lastIndex = currentIndex;
			String value = new String(data, firstIndex, lastIndex - firstIndex);
			if (value.equalsIgnoreCase("AND")) {
				token = new QueryToken(QueryTokenType.AND, value);	
			} else if (value.equals("LIKE")) {
				token = new QueryToken(QueryTokenType.OPERATOR, value);	
			} else if (value.equals("jmbag") || value.equals("firstName") || value.equals("lastName")) {
				token = new QueryToken(QueryTokenType.ATTRIBUTE, value);				
			} else {
				throw new QueryLexerException("Unknown attribute!");
			}
			
		} else if ((currentIndex < data.length - 1) 
					&& ((new String(data, currentIndex, 2)).equals("!=") 
					 || (new String(data, currentIndex, 2)).equals("<=")
					 || (new String(data, currentIndex, 2)).equals(">="))) {
			token = new QueryToken(QueryTokenType.ATTRIBUTE, 
						(new String(data, currentIndex, 2))
			); 
			currentIndex += 2;
		
		} else if (data[currentIndex] == '>' || data[currentIndex] == '<' || data[currentIndex] == '=') {
			token = new QueryToken(QueryTokenType.OPERATOR, Character.toString(data[currentIndex++]));
		
		} else {
			throw new QueryLexerException("Query must contain only string value attributes, known operators, or string literals!");
		}
	
		return token;
	}
	
}
