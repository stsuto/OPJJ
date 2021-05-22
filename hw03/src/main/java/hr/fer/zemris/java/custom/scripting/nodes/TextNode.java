package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Class <code>TextNode</code> is an extension of <code>Node</code>. 
 * It is representing a piece of textual data.
 * 
 * @author stipe
 */
public class TextNode extends Node {

	/**
	 * node's text
	 */
	private String text;
	
	/**
	 * Constructor that adds the recieved text to the node's value.
	 * @param text
	 */
	public TextNode(String text) {
		this.text = text;
	}

	/**
	 * Returns this <code>TextNode's</code> text value.
	 * @return this node's text
	 */
	public String getText() {
		return text;
	}
	
	
}
