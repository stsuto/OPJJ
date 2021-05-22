package hr.fer.zemris.java.custom.scripting.parser;

import java.util.Arrays;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptTokenType;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

/**
 * Class <code>SmartScriptParser</code> represents a parser used for syntax analysis.
 * The parser uses tokens processed by {@link hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer} and creates expressions using nodes and elements.
 * 
 * @author stipe
 */
public class SmartScriptParser {

	/**
	 * the lexer processing the document and giving tokens to the parser
	 */
	private SmartScriptLexer lexer;
	/**
	 * the stack used for construction of the syntax tree
	 */
	private ObjectStack stack;
	/**
	 * node in which the processed text is saved
	 */
	private DocumentNode documentNode;
	
	/**
	 * Constructor that initializes this object's variables.
	 * @param document which is sent to lexer for processing
	 */
	public SmartScriptParser(String document) {
		  lexer = new SmartScriptLexer(document);
		  stack = new ObjectStack();
		  documentNode = parseDocument();
	}
	
	/**
	 * Returns the <code>DocumentNode</code> containing all elements of the syntax tree.
	 * @return this object's <code>DocumentNode</code>
	 */
	public DocumentNode getDocumentNode() {
		return documentNode;
	}
	
	/**
	 * Parses all tokens received from lexer and stores them on stack and in the <code>DocumentNode</code> that is returned at the end.
	 * @return the node containing all the parsed text
	 */
	private DocumentNode parseDocument() {
		stack.push(new DocumentNode());
		lexer.nextToken(); // gets the first token
		
		while (true) {
			if (compareTypeWith(SmartScriptTokenType.EOF)) { // if token is of type EOF, it is the last token
				break;
			}

			// checks if next token should be parsed with tag parsing or text parsing
			if (compareTypeWith(SmartScriptTokenType.TAG)) {
				lexer.nextToken();
				
				// after tag there must come either a keyword or '=' as tag name
				if (!compareTypeWith(SmartScriptTokenType.VARIABLE) && !compareTypeWith(SmartScriptTokenType.KEY)) {
					throw new SmartScriptParserException("Wrong input! It was " + lexer.getToken().getType() + ": " + lexer.getToken().getValue());
				}
				
				if ("FOR".equalsIgnoreCase(String.valueOf(lexer.getToken().getValue()))) { // start of for-loop
					lexer.nextToken();
					parseFor();
					continue;
				}
				if ("=".equals(String.valueOf(lexer.getToken().getValue()))) { // start of empty-tag
					lexer.nextToken();
					parseEcho();
					continue;
				}
				if ("END".equalsIgnoreCase(String.valueOf(lexer.getToken().getValue()))) { //end of for-loop
					lexer.nextToken();
					parseEnd();
					continue;
					
				// keyword unknows to this parser
				} else {
					throw new SmartScriptParserException("This parser doesn't know what to do with that tag!");
				}
				
			// if it isnt a tag, its simple text	
			} else {
				parseText();
			}
		}
		
		//returns the node in which everything is stored
		if (stack.size() != 1) {
			throw new SmartScriptParserException("Not all for-loops had an end.");
		}
		return (DocumentNode) stack.pop();
	}
	
	/**
	 * Parses the normal text and puts it in a <code>TextNode</code> which is pushed on the stack and written in the <code>DocumentNode</code>
	 * @throws SmartScriptParserException if there is anything wrong with the syntax analysis
	 */
	private void parseText() {
		StringBuilder sb = new StringBuilder();

		//if the previous token was the opening parenthesis and the current token is dollar sign, state should switch to TAG
		while (!compareTypeWith(SmartScriptTokenType.EOF)) {
			if (compareTypeWith(SmartScriptTokenType.OPEN_PARENTHESES)) {
				lexer.nextToken();	// if the current token is parenthesis but the next isnt a tag, it's a normal text symbol
				if (compareTypeWith(SmartScriptTokenType.TAG)) { // if it IS a tag, then the text part is over
					break;
				}
				sb.append('{');
			}

			sb.append(String.valueOf(lexer.getToken().getValue()));
			if (!compareTypeWith(SmartScriptTokenType.EOF)) {
				lexer.nextToken(); // gets the token which should be tag				
			}
		}
		
		// creates a text node with this text and adds it to the node higher in the hierarchy
		if (sb.toString() != null) {
			TextNode textNode = new TextNode(sb.toString());
			((Node) stack.peek()).addChildNode(textNode);
		}
	}

	/**
	 * Checks if the for-loop is ending properly and pops the for-loop from the stack
	 * @throws SmartScriptParserException if the tag is not properly closed or if there are more "END"s than there are for loops
	 */
	private void parseEnd() {
		if (!compareTypeWith(SmartScriptTokenType.TAG)) {
			throw new SmartScriptParserException("End of for loop is not correctly given!");
		}
		checkTagEnd();
		
		stack.pop(); // pops the for-loop node
		if (stack.isEmpty()) {
			throw new SmartScriptParserException("Error in document! It contains more {$END$}-s than opened non-empty tags");
		}
	}

	/**
	 * Parses the tokens from empty tags and saves it in a <code>EchoNode</code> which is pushed on the stack and written in the <code>DocumentNode</code>
	 * @throws SmartScriptParserException if there is anything wrong with the syntax analysis
	 */
	private void parseEcho() {
		Element[] array = new Element[5];
		
		// empty tag can contain many elements, so it ends once a token with tag is current
		for (int i = 0; !compareTypeWith(SmartScriptTokenType.TAG); i++) {
			if (i == array.length) {
				array = Arrays.copyOf(array, 2 * array.length); // if no more room, reallocate
			}
			
			// depending on the type of token, a different element type is chosen for the expression
			if (compareTypeWith(SmartScriptTokenType.VARIABLE)) { //variable and function have rules about names
				String variableName = (String) lexer.getToken().getValue();
				if (!isVariableValid(variableName)) {
					throw new SmartScriptParserException("Variable name is invalid!");
				}
				array[i] = new ElementVariable(variableName);
			} else if (compareTypeWith(SmartScriptTokenType.STRING)) {
				array[i] = new ElementString((String) lexer.getToken().getValue());
			} else if (compareTypeWith(SmartScriptTokenType.DOUBLE)) {
				array[i] = new ElementConstantDouble((double) lexer.getToken().getValue());
			} else if (compareTypeWith(SmartScriptTokenType.INT)) {
				array[i] = new ElementConstantInteger((int) lexer.getToken().getValue());
			} else if (compareTypeWith(SmartScriptTokenType.OPERATOR)) {
				array[i] = new ElementOperator(String.valueOf(lexer.getToken().getValue()));
			} else if (compareTypeWith(SmartScriptTokenType.FUNCTION)) {
				String functionName = ((String) lexer.getToken().getValue());
				if (!isVariableValid(functionName.replace("@", ""))) {
					throw new SmartScriptParserException("Function name is invalid!");
				}
				array[i] = new ElementFunction(functionName);
				
			// if the element is none of the above, its undefined
			} else {
				throw new SmartScriptParserException("Undefined argument in empty tag!");
			}
			lexer.nextToken();
		}
		checkTagEnd();
		
		// creates an echo node with this text and adds it to the node higher in the hierarchy
		EchoNode echoNode = new EchoNode(array);
		((Node) stack.peek()).addChildNode(echoNode);
	}

	/**
	 * Parses the tokens from for-loop tags and saves it in a <code>ForLoopNode</code> which is pushed on the stack and written in the <code>DocumentNode</code>
	 * @throws SmartScriptParserException if there is anything wrong with the syntax analysis
	 */
	private void parseFor() {
		Element[] array = new Element[4];
		if (!compareTypeWith(SmartScriptTokenType.VARIABLE)) { // first token must be a variable!
			throw new SmartScriptParserException(lexer.getToken().toString() + " is not a variable!");
		}
		
		if (!isVariableValid(lexer.getToken().getValue().toString())) { // variable must be properly named
			throw new SmartScriptParserException("Variable " + " is not a valid variable name!");
		}
		
		int i = 0;
		array[i++] = new ElementVariable((String) lexer.getToken().getValue()); // putting the variable in the array
		lexer.nextToken();

		try {	
			// arguments of for-loop after the first one can be string, double, int and variable
			// depending on the type, different elements are used
			for (; !compareTypeWith(SmartScriptTokenType.TAG); i++) {
				if (compareTypeWith(SmartScriptTokenType.VARIABLE)) {
					array[i] = new ElementVariable((String) lexer.getToken().getValue());
				} else if (compareTypeWith(SmartScriptTokenType.STRING)) {
					array[i] = new ElementString((String) lexer.getToken().getValue());
				} else if (compareTypeWith(SmartScriptTokenType.DOUBLE)) {
					array[i] = new ElementConstantDouble((double) lexer.getToken().getValue());
				} else if (compareTypeWith(SmartScriptTokenType.INT)) {
					array[i] = new ElementConstantInteger((int) lexer.getToken().getValue());
				} else {
					throw new SmartScriptParserException("Undefined argument in for loop tag!");
				}
				lexer.nextToken();
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new SmartScriptParserException("Too many arguments");
		}
		checkTagEnd();
		
		// for-loop can have 3 or 4 arguments, otherwise an exception is thrown
		if (i < 3) {
			throw new SmartScriptParserException("Too few arguments!");
		}
		
		// creates a for-loop node with this text and adds it to the node higher in the hierarchy
		ForLoopNode forNode = new ForLoopNode((ElementVariable) array[0], array[1], array[2], array[3]);
		((Node) stack.peek()).addChildNode(forNode);
		stack.push(forNode);
	}

	/**
	 * Checks if the tag ends properly with '$' followed by '}' and gets the next token ready to be processed
	 * @throws SmartScriptParserException if there is anything wrong with the syntax analysis
	 */
	private void checkTagEnd() {
		lexer.nextToken();
		if (!compareTypeWith(SmartScriptTokenType.CLOSE_PARENTHESES)) {
			throw new SmartScriptParserException("Undefined argument in tag!");
		}
		lexer.nextToken();
	}

	/**
	 * Checks if the variable or function name is valid.
	 * @param name variable or function name
	 * @return <code>true</code> if the name is valid and <code>false</code> otherwise
	 */
	private boolean isVariableValid(String name) {
		if (!Character.isLetter(name.charAt(0))) {
			return false;
		}
		char[] charArray = name.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (!Character.isLetter(charArray[i]) && !Character.isDigit(charArray[i]) && charArray[i] != '_') {
				return false;
			}
		}
		return true;
	}

	/**
	 * Compares the type of the current token to the type given as argument
	 * @param type to be compared with current token's type
	 * @return <code>true</code> if the types are equal and <code>false</code> otherwise
	 */
	private boolean compareTypeWith(SmartScriptTokenType type) {
		return lexer.getToken().getType().equals(type);
	}
}
