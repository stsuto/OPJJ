package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.util.Util;

/**
 * The copy command expects two arguments: source file name and destination file name.
 * If destination file exists, user is asked whether or not the file should be overwritten.
 * Only files can be copied. If the second argument is directory, it is assumed that the 
 * user wants to copy the original file into that directory using the original file.
 * @author stipe
 *
 */
public class CopyShellCommand implements ShellCommand {

	/**
	 * Name of the command.
	 */
	private static final String COMMAND_NAME = "copy";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			List<String> processedArgs = initializeArguments(arguments);			

			Path src = Util.normalizePath(env, processedArgs.get(0));
			Path dest = Util.normalizePath(env, processedArgs.get(1));
			
			if (Files.isDirectory(dest)) {
				dest = Paths.get(dest.toString(), src.getFileName().toString());
			}
			if (Files.exists(dest)) {
				if (!checkIfOverwrite(env)) {
					env.writeln("Copying aborted.");
					return ShellStatus.CONTINUE;
				} else {
					env.writeln("Overwriting.");
				}
			}
			
			copy(src, dest);
		
		} catch (ShellIOException e) {
			env.writeln(e.getMessage());	
		} catch (IOException e1) {
			env.writeln("Unexpected error while copying!");
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Initializes the arguments given by the user.
	 * 
	 * @param arguments user's input
	 * @return List of String objects each containing one argument
	 * @throws ShellIOException if the number of arguments is wrong or arguments are invalid
	 */
	private List<String> initializeArguments(String arguments) {
		List<String> processedArgs = Util.processArguments(arguments);			

		if (processedArgs.size() != 2) {
			throw new ShellIOException("Copy command must be followed by exactly two arguments"
					+ " - source file and destination file!");
		}
		return processedArgs;
	}

	/**
	 * Communicates with the user that the file already exists and asks
	 * whether the file should be overwritten.
	 * 
	 * @param env environment used for communication with the user
	 * @return {@code true} if user responed with "yes", {@code false} 
	 * 		   if he responded with "no"
	 */
	private boolean checkIfOverwrite(Environment env) {
		env.writeln("Destination file already exists. Overwrite? (Yes / No)");
		while (true) {
			env.write(env.getPromptSymbol() + " ");
			String response = env.readLine().strip();
			if (response.equalsIgnoreCase("yes")) {
				return true;
			} else if (response.equalsIgnoreCase("no")) {
				return false;
			} else {
				env.writeln("Answer must be either \"Yes\" or \"No\". Overwrite?");
			}

		}
	}

	/**
	 * Copies the file from the source path to a file on the destination path, 
	 * creating the file if it didn't already exist.
	 * 
	 * @param src source file
	 * @param dest destination file
	 * @throws IOException if an error occurred during reading from or writing to file
	 */
	private void copy(Path src, Path dest) throws IOException {
		try (InputStream is = new BufferedInputStream(Files.newInputStream(src, StandardOpenOption.READ));
				OutputStream os = new BufferedOutputStream(
						Files.newOutputStream(dest, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {

			byte[] inputBuffer = new byte[4096];
			int i = is.read(inputBuffer);
			while (i > 0) {
				os.write(inputBuffer, 0, i);
				i = is.read(inputBuffer);
			}
		}
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		
		description.add("The copy command expects two arguments: source file name and destination file name.");
		description.add("If destination file exists, user is asked whether or not the file should be overwritten.");
		description.add("Only files can be copied.");
		description.add("If the second argument is directory, it is assumed that the user wants to copy the original file into that directory using the original file's name.");
		
		return Collections.unmodifiableList(description); 
	}
	
}
