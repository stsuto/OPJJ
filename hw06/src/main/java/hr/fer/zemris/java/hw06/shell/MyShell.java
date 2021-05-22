package hr.fer.zemris.java.hw06.shell;

import java.util.SortedMap;

/**
 * Class {@code MyShell} represents a command-line program.
 * The program lets user know it's ready for interaction once the prompt symbol
 * is written in command line, after which the shell waits for user's input.
 * User is expected to enter a command and appropriate arguments. To check
 * which commands are defined, user should write "help".
 * <p>
 * The command can span across multiple lines. However, each line that is not 
 * the last line of command must end with a special symbol called a MORELINESSYMBOL,
 * that is used to inform the shell that more lines as expected. 
 * For each line that is part of multi-line command (except for the first one),
 * the shell will write the MULTILINESYMBOL instead of the PROMPTSYMBOL at the 
 * beginning of the line.
 * 
 * @author stipe
 *
 */
public class MyShell {

	public static void main(String[] args) {

		Environment env = new ShellEnvironmentImpl();
		SortedMap<String, ShellCommand> commands = env.commands();
		System.out.println("Welcome to MyShell v 1.0");

		while (true) {
			env.write(String.valueOf(env.getPromptSymbol() + " "));

			String[] input = readLineOrLines(env).split("\\s+", 2);
			String commandName = input[0];
			String arguments = getArguments(input);

			ShellCommand command = commands.get(commandName);
			if (command == null) {
				env.writeln("Wrong command input!");
				continue;
			}

			ShellStatus status = command.executeCommand(env, arguments);

			if (status == ShellStatus.TERMINATE) {
				env.writeln("Shell terminated. Goodbye!");
				break;
			}
		}
	}

	/**
	 * Checks if any non-whitespace characters were given after command name and
	 * returns them if they exist.
	 * 
	 * @param input the arguments given by the user
	 * @return arguments after the command name if they exist, {@code null},
	 *         otherwise
	 */
	private static String getArguments(String[] input) {
		if (input.length == 1 || input[1].isBlank()) {
			return null;
		} else {
			return input[1];
		}
	}

	/**
	 * Reads the users input and checks if the users informed the shell more
	 * lines are to follow.
	 * All user input, whether a single or more lines was input, is return
	 * in a single {@code String} object.
	 * If more lines were combined, symbols used for combining lines are omitted
	 * and the lines are concatenated.
	 * 
	 * @param env environment used for communcation between the shell and the user
	 * @return {@code String} of all user input
	 */
	private static String readLineOrLines(Environment env) {
		StringBuilder sb = new StringBuilder();
		boolean moreLines = true;
		do {
			String line = env.readLine();
			sb.append(line);
			if (line.endsWith(String.valueOf(env.getMorelinesSymbol()))) {
				sb.setLength(sb.length() - 1); // Faster than deletecharAt() because it doesn't do arraycopy
				moreLines = true;
				env.write(String.valueOf(env.getMultilineSymbol()) + " ");
			} else {
				moreLines = false;
			}

		} while (moreLines);

		return sb.toString();
	}

}
