package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.util.Util;

/**
 * Class {@code CdShellCommand} represents a command which takes a single argument 
 * – directory – and sets it as the current directory. The directory can be given 
 * as an absolute path or relative to the current directory.
 * 
 * @author stipe
 *
 */
public class CdShellCommand implements ShellCommand {

	/**
	 * Name of the command.
	 */
	private static final String COMMAND_NAME = "cd";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			List<String> processedArgs = Util.processArguments(arguments);

			if (processedArgs.size() != 1) {
				env.writeln("cd command must be followed by exactly one argument!");
				return ShellStatus.CONTINUE;
			}

			env.setCurrentDirectory(Util.normalizePath(env, processedArgs.get(0)));

		} catch (ShellIOException ex) {
			env.writeln(ex.getMessage());
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

		description.add("Command cd takes a single argument – directory – and sets it as the current directory.");
		description.add("The directory can be given as an absolute path or relative to the current directory.");

		return Collections.unmodifiableList(description);
	}

}
