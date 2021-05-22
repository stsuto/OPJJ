package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.util.Util;

/**
 * Class {@code PushdShellCommand} represents a command that pushes the current 
 * directory to the stack and changes the current directory to the given path.
 * 
 * @author stipe
 *
 */
public class PushdShellCommand implements ShellCommand {

	/**
	 * Name of the command.
	 */
	private static final String COMMAND_NAME = "pushd";
	/**
	 * Key to the path stack.
	 */
	private static final String CD_STACK = "cdstack";
	
	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {		
		try {
			List<String> processedArgs = Util.processArguments(arguments);
			
			if (processedArgs.size() != 1) {
				env.writeln("pushd command must be followed by exactly one argument!");
				return ShellStatus.CONTINUE;
			}
			
			Path changedDir = Util.normalizePath(env, processedArgs.get(0));
			if (!Files.isDirectory(changedDir)) {
				env.writeln("Directory doesn't exist!");
				return ShellStatus.CONTINUE;
			}
			
			if (env.getSharedData(CD_STACK) == null) {
				env.setSharedData(CD_STACK, new Stack<>());
			}
			
			((Stack<Path>) env.getSharedData(CD_STACK)).push(env.getCurrentDirectory());
			env.setCurrentDirectory(changedDir);
			
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
		
		description.add("Pushes the current directory to the stack.");
		description.add("Changes the current directory to the given path.");
		
		return Collections.unmodifiableList(description); 
	}

}
