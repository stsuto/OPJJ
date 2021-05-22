package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command charsets takes no arguments and lists names of supported 
 * charsets for your Java platform. A single charset name is written per line.
 * 
 * @author stipe
 *
 */
public class CharsetsShellCommand implements ShellCommand {

	/**
	 * Name of the command.
	 */
	private static final String COMMAND_NAME = "charsets";
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {			
			if (arguments != null) {
				env.writeln("Command charsets mustn't be followed by any arguments!");
			} else {		
				Charset.availableCharsets()
					.forEach((k, v) -> env.writeln(k));	
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
		
		description.add("Command charsets takes no arguments and lists names of supported charsets for your Java platform.");
		description.add("A single charset name is written per line.");
		
		return Collections.unmodifiableList(description); 
	}
	
}
