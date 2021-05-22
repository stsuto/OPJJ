package hr.fer.zemris.java.hw06.shell;

/**
 * The {@code ShellStatus} enumeration represents the return values of
 * commands performed by the shell.
 * 
 * @author stipe
 *
 */
public enum ShellStatus {

	/**
	 * Status of command after which the shell continues working.
	 */
	CONTINUE,
	/**
	 * Status of command after which the shell is terminated.
	 */
	TERMINATE
	
}
