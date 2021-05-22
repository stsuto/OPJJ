package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Class {@code PushdShellCommand} represents a command that 
 * lists all directory paths saved on stack.
 * 
 * @author stipe
 *
 */
public class ListdShellCommand implements ShellCommand {

	/**
	 * Name of the command.
	 */
	private static final String COMMAND_NAME = "listd";
	/**
	 * Key to the path stack.
	 */
	private static final String CD_STACK = "cdstack";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments != null) {
			env.writeln("Command listd mustn't be followed by any arguments!");
			return ShellStatus.CONTINUE;
		}
		
		@SuppressWarnings("unchecked")
		Stack<Path> stack = (Stack<Path>) env.getSharedData(CD_STACK);
		
		if (stack == null || stack.isEmpty()) {
			env.writeln("No paths on stack."); //"Nema pohranjenih direktorija."
			return ShellStatus.CONTINUE;
		
		} else {
			List<Path> paths = new ArrayList<>(stack);
			Collections.reverse(paths);
			for (Path path : paths) {
				env.writeln(path.toString());
			}			
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
		
		description.add("Lists all directory paths saved on stack.");
		
		return Collections.unmodifiableList(description);
	}

}
