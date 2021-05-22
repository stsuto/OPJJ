package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * If called with no arguments, lists all possible commands.
 * Can also be called with a single argument - a command name,
 * and then it describes the command to the user.
 * 
 * @author stipe
 *
 */
public class HelpShellCommand implements ShellCommand {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		SortedMap<String, ShellCommand> commands = env.commands();
		// Unnecessary to check arguments except if they are blank as the program 
		// will take care of incorrect input since there won't be a command with such name.
		if (arguments == null) {
			commands.forEach((k, v) -> env.writeln(k));

		} else if (commands.containsKey(arguments)) {
			ShellCommand command = commands.get(arguments);
			env.writeln(command.getCommandName());
			command.getCommandDescription().forEach(env::writeln);

		} else {
			env.writeln("Command " + arguments + " doesn't exist!");
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCommandName() {
		return "help";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		
		description.add("If called with no arguments, lists all possible commands.");
		description.add("Can also be called with a single argument - a command name, and then it describes the command to the user.");
		
		return description;
	}

}
