package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * The {@code ShellCommand} interface represents a command which the shell can execute.
 * Each command defines the action it will be executing.
 * 
 * @author stipe
 *
 */
public interface ShellCommand {

	/**
	 * Executes the action defined by the command.
	 * 
	 * @param env environment used for communication with the user
	 * @param arguments arguments the user has given with the command
	 * @return status of the shell after the command was executed
	 */
	ShellStatus executeCommand(Environment env, String arguments);
	
	/**
	 * Returns the name of the command.
	 * 
	 * @return command's name
	 */
	String getCommandName();
	
	/**
	 * Returns a list of descriptions which describe what 
	 * the command does or how it should be used.
	 * 
	 * @return {@code List} of {@code String} objects containing descriptions of the command
	 */
	List<String> getCommandDescription();
	
}
