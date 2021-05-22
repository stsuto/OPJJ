package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

/**
 * Class <code>Node</code> is the base class for all graph nodes used for syntax analysis.
 * 
 * @author stipe
 */
public class Node {

	/**
	 * collection of children
	 */
	private ArrayIndexedCollection collection;
	
	/**
	 * Adds the given <code>Node</code> as a child of this node.
	 * @param child <code>Node</code> to be added as a child
	 */
	public void addChildNode(Node child) {
		if (collection == null) {
			collection = new ArrayIndexedCollection();
		}
		collection.add(child);
	}
	
	/**
	 * Returns the number of children this node has.
	 * @return number of children
	 */
	public int numberOfChildren() {
		return collection.size();
	}
	
	/**
	 * Gets and returns the child <code>Node</code> at position <code>index</code>
	 * @param index the position of the child
	 * @return the child at <code>index</code>
	 */
	public Node getChild(int index) {
		return (Node) collection.get(index);
	}

	@Override
	public int hashCode() {
		return Objects.hash(collection);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Node))
			return false;
		Node other = (Node) obj;
		for (int i = 0; i < collection.size(); i++) {
			if (Objects.equals(collection, other.collection)) {
				return false;
			}
		}
		return true;
	}
	
}
