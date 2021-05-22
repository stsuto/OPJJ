package searching.algorithms;

import hr.fer.zemris.java.util.Util;

/**
 * Class {@code Node} represents a node containing a reference to its parent
 * node, the state, and the cost of the state solution.
 * 
 * @author stipe
 *
 * @param <S> type of value of state
 */
public class Node<S> {

	/**
	 * Parent node.
	 */
	private Node<S> parent;
	/**
	 * This node's state.
	 */
	private S state;
	/**
	 * Cost of this node's state.
	 */
	private double cost;
	
	/**
	 * Constructor initializing the properties with the given parameters.
	 * 
	 * @param parent {@link #parent}
	 * @param state {@link #state}
	 * @param cost {@link #cost}
	 * @throws NullPointerException if the given state is null
	 */
	public Node(Node<S> parent, S state, double cost) {
		this.parent = parent;
		this.state = Util.requireNonNull(state, "State");
		this.cost = cost;
	}

	/**
	 * Getter for this node's parent.
	 * 
	 * @return {@link #parent}
	 */
	public Node<S> getParent() {
		return parent;
	}

	/**
	 * Getter for this node's state.
	 * 
	 * @return {@link #state}
	 */
	public S getState() {
		return state;
	}

	/**
	 * Getter for this node's cost.
	 * 
	 * @return {@link #cost}
	 */
	public double getCost() {
		return cost;
	}

}
