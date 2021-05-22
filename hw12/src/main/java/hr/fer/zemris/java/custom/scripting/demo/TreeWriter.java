package hr.fer.zemris.java.custom.scripting.demo;

import static hr.fer.zemris.java.util.Util.getLostEscapes;
import static hr.fer.zemris.java.util.Util.readFromDisk;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * Class representing the functionality of the parser and visitor 
 * responsible for the reconstruction of the parsed tree.
 * 
 * @author stipe
 *
 */
public class TreeWriter {
	
	public static void main(String[] args) {
		
		String docBody = readFromDisk("examples/brojPoziva.smscr");
//		String docBody = readFromDisk("examples/docx.txt");
		
		SmartScriptParser p = new SmartScriptParser(docBody);
		WriterVisitor visitor = new WriterVisitor();
		p.getDocumentNode().accept(visitor);	
	}

	/**
	 * Cisitor responsible for recreating parsed tree.
	 * 
	 * @author stipe
	 *
	 */
	static class WriterVisitor implements INodeVisitor {
		StringBuilder sb = new StringBuilder();
		
		@Override
		public void visitTextNode(TextNode node) {
			sb.append(node.getText().replace("\\", "\\\\").replace("{$", "\\{$"));
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			sb.append("{$ FOR ");
			
			appendElement(node.getVariable());
			appendElement(node.getStartExpression());
			appendElement(node.getEndExpression());
			if (node.getStepExpression() != null) {
				appendElement(node.getStepExpression());
			}
			sb.append("$}");
			
			int childrenNumber = node.numberOfChildren();
			for (int i = 0; i < childrenNumber; i++) {
				node.getChild(i).accept(this);
			}
			
			sb.append("{$END$}");
		}

		private void appendElement(Element el) {
			String text = getLostEscapes(el.asText());
			sb.append(el instanceof ElementString ? "\"" + text + "\"" : text);
			sb.append(" ");
		}
		
		@Override
		public void visitEchoNode(EchoNode node) {
			sb.append("{$ = ");
			Element[] elements = node.getElements();
			for (Element element : elements) {
				if (element == null) {
					break;
				}
				appendElement(element);
			}
			sb.append("$}");
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			int childrenNumber = node.numberOfChildren();
			for (int i = 0; i < childrenNumber; i++) {
				node.getChild(i).accept(this);
			}
			System.out.println(sb);
		}
		
	}
	
}
