package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command symbol can accept one argument, the symbol whose current
 * character representation is then printed, or two arguments, one
 * for symbol whose character representation is to be changed, and 
 * the other for the character to be set as new value.
 * 
 * @author stipe
 *
 */
public class SymbolShellCommand implements ShellCommand {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = arguments.split("\\s+");

		try {
			if (args.length != 1 && args.length != 2) {
				throw new IllegalArgumentException("Symbol command expects 1 or 2 arguments!");
			}
			
			switch (args[0]) {
				case "PROMPT":
					env.writeln(getOrSetSymbol(args, env::getPromptSymbol, env::setPromptSymbol));
					break;
	
				case "MORELINES":
					env.writeln(getOrSetSymbol(args, env::getMorelinesSymbol, env::setMorelinesSymbol));
					break;
	
				case "MULTILINE":
					env.writeln(getOrSetSymbol(args, env::getMultilineSymbol, env::setMultilineSymbol));
					break;
	
				default:
					throw new IllegalArgumentException("Uknown symbol!");
			}

		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Depending on the arguments given by user, sets the wanted symbol to the given parameter,
	 * or returns the symbol currently stored.
	 * 
	 * @param args user input
	 * @param before character currently stored as some symbol
	 * @param after character to be set as some symbol
	 * @return String describing the result of the operation
	 */
	private String getOrSetSymbol(String[] args, Supplier<Character> before, Consumer<Character> after) {
		if (args.length == 2) {
			if (args[1].length() != 1) {
				throw new IllegalArgumentException("Symbol must be a single character!");
			}
			String output = String.format("Symbol for %s changed from '%c' to '%c'.", 
											args[0], before.get(), args[1].charAt(0));
			after.accept(args[1].charAt(0));
			return output;
				
		} else {
			return String.format("Symbol for %s is '%c'.", args[0], before.get());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCommandName() {
		return "symbol";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		
		description.add("Command symbol can accept one or two arguments.");
		description.add("If accepting one, it is the symbol whose current.");
		description.add("If two arguments are given, one is for symbol whose"
					+ " character representation is to be changed, and the other "
					+ "for the character to be set as new value.");
		
		
		return description;
	}

}
