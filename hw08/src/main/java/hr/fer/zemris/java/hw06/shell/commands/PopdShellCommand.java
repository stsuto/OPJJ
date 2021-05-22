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
 * Class {@code PushdShellCommand} represents a command that removes the 
 * top-most directory from the stack and sets it as the current directory.
 * 
 * @author stipe
 *
 */
public class PopdShellCommand implements ShellCommand {

	/**
	 * Name of the command.
	 */
	private static final String COMMAND_NAME = "popd";
	/**
	 * Key to the path stack.
	 */
	private static final String CD_STACK = "cdstack";

	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments != null) {
			env.writeln("Command popd mustn't be followed by any arguments!");
			return ShellStatus.CONTINUE;
		}
		
		Stack<Path> stack = (Stack<Path>) env.getSharedData(CD_STACK);

		if (stack == null || stack.isEmpty()) {
			env.writeln("Stack is already empty!");

		} else {
			env.setCurrentDirectory(stack.pop());
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

		description.add("Removes the top-most directory from the stack and sets it as the current directory.");

		return Collections.unmodifiableList(description);
	}

}
