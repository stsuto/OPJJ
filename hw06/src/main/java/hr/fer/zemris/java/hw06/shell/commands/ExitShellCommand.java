package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Exit command terminates the shell program. It mustn't be followed by
 * any arguments, otherwise the shell wont be terminated.
 * 
 * @author stipe
 *
 */
public class ExitShellCommand implements ShellCommand {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments != null) {
			env.writeln("The exit command mustn't be followed by any arguments!");
			return ShellStatus.CONTINUE;
		}

		return ShellStatus.TERMINATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCommandName() {
		return "exit";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		
		description.add("Exit command terminates the shell program.");
		description.add("It mustn't be followed by any arguments, otherwise the shell wont be terminated.");
		
		return description;
	}

}
