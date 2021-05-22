package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Class {@code PwdShellCommand} represents a command which takes no arguments 
 * and prints the current directory using its absolute path.
 * 
 * @author stipe
 *
 */
public class PwdShellCommand implements ShellCommand {

	/**
	 * Name of the command.
	 */
	private static final String COMMAND_NAME = "pwd";
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments != null) {
			env.writeln("Command pwd mustn't be followed by any arguments!");
		
		} else {
			env.writeln(env.getCurrentDirectory().toString());
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		
		description.add("Command pwd takes no arguments and prints the current directory.");
		description.add("Absolute path of the current directory is used.");
		
		return Collections.unmodifiableList(description);
	}

}
