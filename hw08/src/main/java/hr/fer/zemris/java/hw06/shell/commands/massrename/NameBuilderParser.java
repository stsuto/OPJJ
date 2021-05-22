package hr.fer.zemris.java.hw06.shell.commands.massrename;

import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.commands.massrename.lexer.NameBuilderLexer;
import hr.fer.zemris.java.hw06.shell.commands.massrename.lexer.NameBuilderLexerState;
import hr.fer.zemris.java.hw06.shell.commands.massrename.lexer.NameBuilderTokenType;

/**
 * Class {@code NameBuilderParser} represents a parser used for syntax 
 * analysis of shell input for {@link MassrenameShellCommand}. The parser 
 * uses tokens processed and created by {@link NameBuilderLexer}.
 * 
 * @author stipe
 */
public class NameBuilderParser {

	/**
	 * Lexer that creates the tokens for this parser to use.
	 */
	private NameBuilderLexer lexer;
	/**
	 * Composite {@link NameBuilder} used to save all builder objects.
	 */
	private CompositeNameBuilderImpl compositeNameBuilder;

	/**
	 * Constructor which accepts the text to be parsed and starts the 
	 * lexical and syntax analysis.
	 * 
	 * @param expression text to be parsed as expression
	 */
	public NameBuilderParser(String expression) {
		lexer = new NameBuilderLexer(expression);
		compositeNameBuilder = startParsing();
	}
	
	/**
	 * Returns the composite builder.
	 * 
	 * @return {@link #compositeNameBuilder}
	 */
	public NameBuilder getNameBuilder() {
		return compositeNameBuilder;
	}
	
	/**
	 * Starts the parsing of the expression.
	 * 
	 * @return composite builder with all builders created during parsing
	 */
	private CompositeNameBuilderImpl startParsing() {
		CompositeNameBuilderImpl composite = new CompositeNameBuilderImpl();
		
		while (true) {
			lexer.nextToken();
			if (compareType(NameBuilderTokenType.EOF)) {
				break;
			}
			
			if (compareType(NameBuilderTokenType.TEXT)) {
				composite.addBuilder(parseText());
				
			} else if (compareType(NameBuilderTokenType.START_TAG)) {
				composite.addBuilder(parseTag());
			
			} else {
				throw new ShellIOException("Invalid expression!");
			}
			
		}
		lexer.setState(NameBuilderLexerState.TEXT);
		
		return composite;
	}

	/**
	 * Parses a series of tag tokens.
	 * 
	 * @return a NameBuilder object
	 */
	private NameBuilder parseTag() {
		lexer.setState(NameBuilderLexerState.TAG);
		
		requireNextType(NameBuilderTokenType.NUMBER);
		int firstNumber = parseNumber(lexer.getToken().getValue());
		
		lexer.nextToken();
		if (compareType(NameBuilderTokenType.SEPARATOR)) {
			requireNextType(NameBuilderTokenType.NUMBER);
			
			String secondArg = lexer.getToken().getValue();
			char padding = getPadding(secondArg);
			int secondNumber = parseNumber(secondArg);
			
			requireNextType(NameBuilderTokenType.END_TAG);
			lexer.setState(NameBuilderLexerState.TEXT);	
			
			return NameBuilderImpls.group(firstNumber, padding, secondNumber);
			
//			If not separator, the token must close the command tag.
		} else if (compareType(NameBuilderTokenType.END_TAG)) {
			lexer.setState(NameBuilderLexerState.TEXT);			
			return NameBuilderImpls.group(firstNumber);
		
		} else {
			throw new ShellIOException("Tag end was expected!");
		}	
	}

	/**
	 * Returns the defined padding.
	 * 
	 * @param secondArg numbers containing padding definition.
	 * @return character to pad with
	 */
	private char getPadding(String secondArg) {
		char padding = secondArg.charAt(0);
		if (padding != '0') {
			padding = ' ';
		}
		return padding;
	}

	/**
	 * Parses a number from the given text and checks if it's valid.
	 * 
	 * @param string string containing the number
	 * @return number from string
	 */
	private int parseNumber(String string) {
		int number = Integer.parseInt(string);
		if (number < 0) {
			throw new ShellIOException("Group index must not be negative!");
		}
		return number;
	}

	/**
	 * Parses the normal text of the expression.
	 * 
	 * @return a NameBuilder object
	 */
	private NameBuilder parseText() {
		return NameBuilderImpls.text(lexer.getToken().getValue());
	}

	/**
	 * Gets the next token and checks if it is of correct type.
	 * 
	 * @param type type the next token needs to be
	 * @throws ShellIOException if the token is of wrong type
	 */
	private void requireNextType(NameBuilderTokenType type) {
		lexer.nextToken();
		if (!compareType(type)) {
			throw new ShellIOException("Expected another token type!");
		}
	}

	/**
	 * Compares the current token with the given type.
	 * 
	 * @param type type to be compared with the current token type
	 * @return {@code true} if the types are the same, {@code false} otherwise
	 */
	private boolean compareType(NameBuilderTokenType type) {
		return lexer.getToken().getType() == type;
	}
		
}
