package hr.fer.zemris.java.custom.scripting.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

class SmartScriptParserTest {

	@Test
	void testNullDoc() {
		assertThrows(NullPointerException.class, () -> new SmartScriptParser(null));
	}

	@Test
	void testProperForLoop() {
		String docBody = "{$ FOR sco_re \"-1\"10 \"1\" $} {$ END $}";
		assertDoesNotThrow(() -> new SmartScriptParser(docBody));
	}
	
	@Test
	void testTooLongForLoop() {
		String docBody = "{$ FOR year 1 10 \"1\" \"10\" $} {$ END $}";
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
	}
	
	@Test
	void testTooShortForLoop() {
		String docBody = "{$ FOR year 1 $} {$ END $}";
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
	}
	
	@Test
	void testFunctionInForLoop() {
		String docBody = "{$ FOR sco_re \"-1\"10 @sin $} {$ END $}";
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
	}
	
	@Test
	void testForLoopWithoutEnd() {
		String docBody = "{$ FOR sco_re \"-1\"10 \"1\" $}";
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
	}
	
	@Test
	void testIllegalVariableName() {
		String docBody = "{$ FOR 5sco_re \"-1\"10 \"1\" $} {$ END $}";
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
	}
	
	@Test
	void testForLoopWithoutVariable() {
		String docBody = "{$ FOR  \"-1\"10 \"1\" $} {$ END $}";
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
	}
	
	@Test
	void testIllegalFunctionName() {
		String docBody = "{$= i i * @2sin \"0.000\" @decfmt $}";
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
	}
	
	@Test
	void testParsedDocEquality() {
		isReplicatedCorrectly("doc1.txt");
		isReplicatedCorrectly("document1.txt");
		isReplicatedCorrectly("document40.txt");
		isReplicatedCorrectly("docx.txt");
	}
	
	private void isReplicatedCorrectly(String string) {
		String docBody = loader(string);
		SmartScriptParser parser = new SmartScriptParser(docBody);
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		assertTrue(document.equals(document2));
		String copiedDocumentBody = createOriginalDocumentBody(document2);
		assertTrue(originalDocumentBody.equals(copiedDocumentBody));
	}

	private String loader(String filename) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try(InputStream is =
				this.getClass().getClassLoader().getResourceAsStream(filename)) {
			byte[] buffer = new byte[1024];
			while(true) {
				int read = is.read(buffer);
				if(read<1) break;
				bos.write(buffer, 0, read);
			} 
				return new String(bos.toByteArray(), StandardCharsets.UTF_8);
		} catch(IOException ex) {
			return null;
		}
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
