package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * The <code>Context</code> class enables execution of the fractal-displaying procedure.
 * It is used for saving and getting previously saved contexts of the procedure in a given time.
 * For that, this class offers methods that save the current <code>TurtleState</code>, 
 * return the current state in the context, or delete a saved state.
 * 
 * @author stipe
 *
 */
public class Context {

	/**
	 * Stack of states in this context.
	 * The current state is always the top-most state.
	 */
	private ObjectStack<TurtleState> stateStack;
	
	/**
	 * Constructor.
	 */
	public Context() {
		stateStack = new ObjectStack<>();
	}
	
	/**
	 * Getter of the current <code>TurtleState</code>.
	 * 
	 * @return {@link #stateStack}'s top most state
	 */
	public TurtleState getCurrentState() {
		return stateStack.peek();	
	}
	
	/**
	 * Saves the current <code>TurtleState</code> by pushing it onto {@link #stateStack}.
	 * 
	 * @param state the <code>TurtleState</code> to be pushed
	 */
	public void pushState(TurtleState state) {
		stateStack.push(state);
	}
	
	/**
	 * Deletes the current <code>TurtleState</code> by removing it from {@link #stateStack}.
	 */
	public void popState() {
		stateStack.pop();
	}
	
}
