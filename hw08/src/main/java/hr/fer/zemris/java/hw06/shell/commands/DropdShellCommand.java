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
 * deletes the top-most directory path from the stack.
 * 
 * @author stipe
 *
 */
public class DropdShellCommand implements ShellCommand {

	/**
	 * Name of the command.
	 */
	private static final String COMMAND_NAME = "dropd";
	/**
	 * Key to the path stack.
	 */
	private static final String CD_STACK = "cdstack";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		@SuppressWarnings("unchecked")
		Stack<Path> stack = (Stack<Path>) env.getSharedData(CD_STACK);
		
		if (stack == null || stack.isEmpty()) {
			env.writeln("Stack is already empty!");
			
		} else {
			stack.pop();
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
		
		description.add("Deletes the top-most directory path from the stack.");
		
		return Collections.unmodifiableList(description);
	}

}
