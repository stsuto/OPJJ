package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Class <code>ObjectStack</code> represents a functioning stack using an array based collection.
 * 
 * @author stipe
 */
public class ObjectStack {

	/**
	 * array in which values are stored; used for simulating stack operations
	 */
	private ArrayIndexedCollection array;
	
	/**
	 * Default constructor that creates an empty stack.
	 */
	public ObjectStack() {
		this.array = new ArrayIndexedCollection();
	}
	
	/**
	 * Checks if the stack is empty.
	 * @return <code>true</code> if the stack is empty, <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return array.isEmpty();
	}
	
	/**
	 * Method that returns the size of the stack.
	 * @return number of elements on stack as an int
	 */
	public int size() {
		return array.size();
	}
	
	/**
	 * Adds an element by putting it on top of the stack.
	 * Average complexity of O(1).
	 * @param value object to be put on stack
	 */
	public void push(Object value) {
		array.add(Objects.requireNonNull(value));
	}
	
	/**
	 * Gets the top-most value on the stack and removes it from the stack.
	 * Average complexity of O(1).
	 * @return value which is the top-most element of the stack
	 */
	public Object pop() {
		Object element = peek();
		array.remove(array.size() - 1);
		return element;
		
	}
	
	/**
	 * Method that returns the top-most value from stack, but doesn't remove it.
	 * Average complexity of O(1).
	 * @return
	 */
	public Object peek() {
		if (array.size() == 0) {
			throw new EmptyStackException();
		}
		return array.get(array.size() - 1);
	}
	
	/**
	 * Removes all elements from the stack.
	 */
	public void clear() {
		array.clear();
	}
	
}
