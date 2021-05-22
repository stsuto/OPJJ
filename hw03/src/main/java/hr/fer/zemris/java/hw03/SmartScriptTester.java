package hr.fer.zemris.java.hw03;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * Class used for demonstrating the functionality of parser and lexer defined in this project.
 * @author stipe
 *
 */
public class SmartScriptTester {

	public static void main(String[] args) {
			
		String docBody = null;
		try {
			docBody = new String(
					Files.readAllBytes(Paths.get("examples/doc1.txt")),
					StandardCharsets.UTF_8
					);
		} catch (IOException e1) {
			System.out.println("Reading file failed!");
		}
		
		SmartScriptParser parser = null;
		
		try {
			parser = new SmartScriptParser(docBody);
			
		} catch (SmartScriptParserException ex) {
			System.out.println("Unable to parse document!");
			System.exit(-1);
			
		} catch (Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}
		
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		System.out.println(originalDocumentBody); // should write something like original
		// content of docBody
			
	}
	
	/**
	 * Constructs a text document from the elements given by the parser, adding the elements lost during parsing
	 * @param document Node with information of the syntax tree
	 * @return String of text from the document node
	 */
	private static String createOriginalDocumentBody(Node document) {
		StringBuilder sb = new StringBuilder();
		int numberOfChildren = document.numberOfChildren(); // all children's values must be appended
		for (int i = 0; i < numberOfChildren; i++) {
			Node child = document.getChild(i);
			if (child instanceof ForLoopNode) { // if its a for-loop node, all of its children must be appended too
				ForLoopNode loopChild = (ForLoopNode) child;
				StringBuilder tempSb = new StringBuilder(); // a new, temporary string builder is necessary to change only the parts changed here
				tempSb.append("{$ FOR " + loopChild.getVariable().asText() + " " + loopChild.getStartExpression().asText() + " " + loopChild.getEndExpression().asText()
						  + " " + (loopChild.getStepExpression() == null ? "" : loopChild.getStepExpression().asText()) + " $}");
				sb.append(getLostEscapes(tempSb)); // rebuild the escapes lost in lexing and parsing
				sb.append(createOriginalDocumentBody(child)); // recursively using this method for this node's children
				sb.append("{$END$}");
			} else if (child instanceof TextNode) { // its simpler rebuilding teh lost escape sequences in text node
				sb.append(((TextNode) child).getText().replace("\\", "\\\\").replace("\"", "\\\""));
			} else if (child instanceof EchoNode) { // all of echonode's elements must be appended
				sb.append("{$ = ");
				StringBuilder tempSb = new StringBuilder();
				Element[] elements = ((EchoNode) child).getElements();
				for (Element element : elements) {
					if (element == null) {
						break;
					}
					tempSb.append(element.asText() + " ");
				}
				// Escape sequences should be rebuilt.				
				sb.append(getLostEscapes(tempSb));
				sb.append("$}");
			} else {
				throw new SmartScriptParserException("Unknown node in constructed tree!");
			}
		}
		return sb.toString();
	}

	/**
	 * Rebuilds the escape sequences lost in lexing and parsing so the final document is as similar to the original as possible.
	 * @param tempSb the given sting builder that contains the values of current child
	 * @return String with proper escape sequences like those in the original document
	 */
	private static Object getLostEscapes(StringBuilder tempSb) {
		String rebuiltString = tempSb.toString().replace("\\", "\\\\").replace("\"", "\\\""); // rebuilding lost escape sequences
		
		int first = rebuiltString.indexOf("\\\""); // With string escapes, all but the first and the last '"' were escaped, but they got a '\' added in front of them too
		int last = rebuiltString.lastIndexOf("\\\""); // the first and the last can't have been escaped as they wouldntt make a string then
		if (last != -1) {
			return rebuiltString.substring(0, first) + rebuiltString.substring(first + 1, last) + rebuiltString.substring(last + 1); // a string is created without the first and last escape character
		
		} else {
			return tempSb.toString();
		}
	}	
}
