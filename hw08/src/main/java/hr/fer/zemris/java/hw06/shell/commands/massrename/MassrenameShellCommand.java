package hr.fer.zemris.java.hw06.shell.commands.massrename;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.util.Util;

/**
 * Class {@code MassrenameShellCommand} represents a command which renames 
 * or moves multiple files at once. Files to be renamed or moved are chosen
 * by matching the given regular expression with file names.
 * 
 * @author stipe
 *
 */
public class MassrenameShellCommand implements ShellCommand {

	/**
	 * Command's name.
	 */
	private static final String COMMAND_NAME = "massrename";
	private static final String FILTER_COMMAND = "filter";
	private static final String GROUPS_COMMAND = "groups";
	private static final String SHOW_COMMAND = "show";
	private static final String EXECUTE_COMMAND = "execute";
	
	/**
	 * Pattern options for this command.
	 */
	private static final int FLAGS = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			List<String> processedArgs = initializeArguments(arguments);

			String subcommand = processedArgs.get(2);

			switch (subcommand) {
				case FILTER_COMMAND:
					doFilterCommand(env, processedArgs);
					break;
	
				case GROUPS_COMMAND:
					doGroupsCommand(env, processedArgs);
					break;
					
				case SHOW_COMMAND:
					doShowCommand(env, processedArgs);
					break;
					
				case EXECUTE_COMMAND:
					doExecuteCommand(env, processedArgs);
					break;
				
				default:
					env.writeln("Unknown subcommand!");
					return ShellStatus.CONTINUE;
			}

		} catch (ShellIOException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		} catch (IOException e) {
			env.writeln("Was unable to perform copying/moving.");
			return ShellStatus.CONTINUE;
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Initializes the arguments by parsing the user input.
	 * 
	 * @param arguments user argument input
	 * @return {@code List} of processed arguments
	 * @throws ShellIOException if number of arguments isn't valid
	 */
	private List<String> initializeArguments(String arguments) {
		List<String> args = Util.processArguments(arguments);
		
		if (args.size() != 4 && !(args.size() == 5 && 
						(SHOW_COMMAND.equals(args.get(2)) || EXECUTE_COMMAND.equals(args.get(2))))) {
			throw new ShellIOException("Massrename command accepts 4 or 5 arguments, "
					+ "depending on the used subcommand.");
		}
		
		return args;
	}
	
	/**
	 * Performs the filter subcommand.
	 * 
	 * @param env Environment
	 * @param args arguments containing source and destination paths, mask and expression
	 * @throws IOException in case of an I/O error
	 */
	private void doFilterCommand(Environment env, List<String> args) throws IOException {
		Path source = Util.normalizePath(env, args.get(0));
		String mask = args.get(3);
		List<FilterResult> files = filter(source, mask);
		for (FilterResult file : files) {
			env.writeln(file.toString());
		}
	}
	
	/**
	 * Performs the groups subcommand.
	 * 
	 * @param env Environment
	 * @param args arguments containing source and destination paths, mask and expression
	 * @throws IOException in case of an I/O error
	 */
	private void doGroupsCommand(Environment env, List<String> args) throws IOException {
		Path source = Util.normalizePath(env, args.get(0));
		String mask = args.get(3);
		List<FilterResult> files = filter(source, mask);
		for (FilterResult file : files) {
			env.write(file.toString());
			for (int i = 0; i <= file.numberOfGroups(); i++) {
				env.write(" " + i + ": " + file.group(i));
			}
			env.writeln("");
		}
	}

	/**
	 * Performs the show subcommand.
	 * 
	 * @param env Environment
	 * @param args arguments containing source and destination paths, mask and expression
	 * @throws IOException in case of an I/O error
	 */
	private void doShowCommand(Environment env, List<String> args) throws IOException {
		Path source = Util.normalizePath(env, args.get(0));
		String mask = args.get(3);
		String expr = args.get(4);
		List<FilterResult> files = filter(source, mask);
		NameBuilderParser parser = new NameBuilderParser(expr);
		NameBuilder builder = parser.getNameBuilder();
		for (FilterResult file : files) {
			StringBuilder sb = new StringBuilder();
			builder.execute(file, sb);
			String newName = sb.toString();
			env.writeln(file.toString() + " => " + newName);
		}
	}

	/**
	 * Performs the execute subcommand.
	 * 
	 * @param env Environment
	 * @param args arguments containing source and destination paths, mask and expression
	 * @throws IOException in case of an I/O error
	 */
	private void doExecuteCommand(Environment env, List<String> args) throws IOException {
		Path source = Util.normalizePath(env, args.get(0));
		Path dest = Util.normalizePath(env, args.get(1));
		String mask = args.get(3);
		String expr = args.get(4);
		List<FilterResult> files = filter(source, mask);
		NameBuilderParser parser = new NameBuilderParser(expr);
		NameBuilder builder = parser.getNameBuilder();
		for (FilterResult file : files) {
			StringBuilder sb = new StringBuilder();
			builder.execute(file, sb);
			Path sourcePath = source.resolve(Paths.get(file.toString()));
			Path destPath = dest.resolve(Paths.get(sb.toString()));
			Files.move(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
			env.writeln(sourcePath.toString() + " => " + destPath.toString());
		}
	}

	/**
	 * Filters this directory's files by matching their names with the given pattern.
	 * 
	 * @param dir directory whose files are to be filtered
	 * @param pattern pattern used for filtering
	 * @return {@code List} of {@code FilterResult} objects containing filtered files
	 * @throws IOException in case of an I/O error
	 */
	private static List<FilterResult> filter(Path dir, String pattern) throws IOException {
		Pattern checker = Pattern.compile(pattern, FLAGS);
		List<FilterResult> filteredFiles = new ArrayList<>();

		for (Path path : Files.newDirectoryStream(dir)) {
			String fileName = path.getFileName().toString();
			Matcher matcher = checker.matcher(fileName);

			if (Files.isRegularFile(path) && matcher.matches()) {
				filteredFiles.add(new FilterResult(fileName, matcher));
			}
		}

		return filteredFiles;
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();

		description.add("Renames all files that fit the given criteria.");

		return Collections.unmodifiableList(description);
	}

}
