package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.Util;

/**
 * Command cat takes one or two arguments. The first argument is path to some 
 * file and is mandatory. The second argument is charset name that should be 
 * used to interpret chars from bytes. If not provided, a default platform 
 * charset should be used. This command opens given file and writes its content 
 * to console.
 * 
 * @author stipe
 *
 */
public class CatShellCommand implements ShellCommand {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			Arguments args = initializeArguments(arguments);
			readFile(env, args.path, args.charset);

		} catch (ShellIOException e) {
			env.writeln(e.getMessage());
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Initializes the arguments from the string given by the user.
	 * 
	 * @param arguments users' input
	 * @return {@code Arguments} structure containing a path and a charset
	 * 		   to be used for output
	 */
	private Arguments initializeArguments(String arguments) {
		List<String> processedArgs = null;
		try {
			processedArgs = Util.processArguments(arguments);
		} catch (IllegalArgumentException e) {
			throw new ShellIOException(e.getMessage());
		}

		if (processedArgs.size() == 2) {
			try {
				return new Arguments(Paths.get(processedArgs.get(0)), Charset.forName(processedArgs.get(1)));

			} catch (Exception e) {
				throw new ShellIOException("Unexistent charset name!");
			}

		} else if (processedArgs.size() == 1 && !processedArgs.get(0).isBlank()) {
			return new Arguments(Paths.get(processedArgs.get(0)), Charset.defaultCharset());

		} else {
			throw new ShellIOException("Wrong arguments!");
		}
	}

	/**
	 * Reads the file from the given path using the given charset and 
	 * writes the read content to command line.
	 * 
	 * @param env environment
	 * @param path path of the file
	 * @param charset used for decoding the file
	 */
	private void readFile(Environment env, Path path, Charset charset) {
		try {
			BufferedReader br = Files.newBufferedReader(path, charset);
			String line = null;
			while ((line = br.readLine()) != null) {
				env.writeln(line);
			}

		} catch (IOException e) {
			env.writeln("Unexpected error while reading from file!");
		}
	}

	/**
	 * Structure used for saving a single path and a single charset
	 * which are used in this command.
	 * 
	 * @author stipe
	 *
	 */
	private static class Arguments {
		/**
		 * Path of file which should be read.
		 */
		Path path;
		/**
		 * Charset used for decoding the file.
		 */
		Charset charset;

		/**
		 * Constructor of this structure.
		 * 
		 * @param path {@link #path}
		 * @param charset {@link #charset}
		 */
		private Arguments(Path path, Charset charset) {
			this.path = path;
			this.charset = charset;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCommandName() {
		return "cat";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		
		description.add("Command cat takes one or two arguments.");
		description.add("The first argument is path to some file and is mandatory.");
		description.add("The second argument is charset name that should be used to interpret chars from bytes.");
		description.add("If not provided, a default platform charset should be used.");
		description.add("This command opens given file and writes its content to console.");
		description.add("Command cat takes one or two arguments.");
		
		return description;
	}

}
