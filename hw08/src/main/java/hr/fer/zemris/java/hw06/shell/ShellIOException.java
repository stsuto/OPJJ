package hr.fer.zemris.java.hw06.shell;

/**
 * The {@code ShellIOException} class represents an exception derived from <code>RuntimeException</code>.
 * It is sent in case of unwated behaviour during the execution of shell commands.

 * @author stipe
 */
public class ShellIOException extends RuntimeException {

	/**
	 * default serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public ShellIOException() {
		super();
	}
	
	/**
	 * Constructor that sends the given message.
	 * @param message
	 */
	public ShellIOException(String message) {
		super(message);
	}
	
}
