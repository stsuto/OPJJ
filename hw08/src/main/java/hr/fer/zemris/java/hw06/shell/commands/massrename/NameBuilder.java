package hr.fer.zemris.java.hw06.shell.commands.massrename;

/**
 * Interface {@code NameBuilder} is an abstraction of an object 
 * which creates a file name from given pattern matches.
 * 
 * @author stipe
 *
 */
public interface NameBuilder {

	/**
	 * Performs the defined action.
	 * 
	 * @param result object containing the file name and group information
	 * @param sb {@code StringBuilder} which contains the new file name
	 */
	void execute(FilterResult result, StringBuilder sb);

}
