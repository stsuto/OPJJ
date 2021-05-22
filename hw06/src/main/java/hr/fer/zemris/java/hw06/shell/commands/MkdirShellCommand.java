package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.Util;

/**
 * The mkdir command takes a single argument: directory name, 
 * and creates the appropriate directory structure.
 * 
 * @author stipe
 *
 */
public class MkdirShellCommand implements ShellCommand {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			List<String> processedArgs = Util.processArguments(arguments);

			if (processedArgs.size() != 1) {
				env.writeln("Mkdir command must be followed by exactly one argument!");
				return ShellStatus.CONTINUE;
			}

			Path dir = Paths.get(processedArgs.get(0));
			Files.createDirectories(Paths.get(dir.toString()));

		} catch (InvalidPathException e) {
			env.writeln("Illegal path name for your OS!");
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
		} catch (IOException e) {
			env.writeln("Unexpected error while creating directory!");
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCommandName() {
		return "mkdir";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		
		description.add("The mkdir command takes a single argument: directory name, and creates the appropriate directory structure.");
		
		return description;
		
		
	}

}
