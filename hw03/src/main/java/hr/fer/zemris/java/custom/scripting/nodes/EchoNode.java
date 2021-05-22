package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * Class <code>EchoNode</code> is an extension of <code>Node</code>.
 * It is a representation of a command which generates some textual output dynamically.
 * 
 * @author stipe
 */
public class EchoNode extends Node {

	/**
	 * array of elements of a document
	 */
	private Element[] elements;

	/**
	 * Constructor that creates a new <code>EchoNode</code> and initializes its elements to those given as parameter
	 * @param elements the elements to be copied to this <code>EchoNode</code>
	 */
	public EchoNode(Element[] elements) {
		super();
		this.elements = elements;
	}

	/**
	 * Returns this <code>EchoNode's</code> elements
	 * @return elements
	 */
	public Element[] getElements() {
		return elements;
	}
	
}
