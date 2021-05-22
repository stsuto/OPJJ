package hr.fer.zemris.java.custom.collections;

/**
 * Represents the exception thrown when a value is requested from the stack, but the stack is empty.
 * 
 * @author stipe
 */
public class EmptyStackException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public EmptyStackException() {
		super();
	}
	
	/**
	 * Constructor that sends the given message to the superclass
	 * @param message the String that is given as message
	 */
	public EmptyStackException(String message) {
		super(message);
	}
}
