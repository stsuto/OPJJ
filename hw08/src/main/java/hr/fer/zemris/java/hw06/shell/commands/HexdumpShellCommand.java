package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
 * The hexdump command expects a single argument: file name, and produces hex-output. 
 * On the right side of the image only a standard subset of characters is shown; 
 * for all other characters a '.' is printed instead.
 * 
 * @author stipe
 *
 */
public class HexdumpShellCommand implements ShellCommand {

	/**
	 * Name of the command.
	 */
	private static final String COMMAND_NAME = "hexdump";
	
	/**
	 * UTF-8 value of the character '.'.
	 */
	private static final int UTF_DOT = 46;

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			List<String> processedArgs = Util.processArguments(arguments);			
			
			if (processedArgs.size() != 1) {
				env.writeln("Hexdump command requires exactly one argument - file path.");
				return ShellStatus.CONTINUE;
			}
			
			Path file = Util.normalizePath(env, processedArgs.get(0));

			if (Files.isDirectory(file)) {
				env.writeln("Hexdump command expects a file argument, not a directory!");
				return ShellStatus.CONTINUE;
			}
			
			dump(env, file);
		
		} catch (ShellIOException e) {
			env.writeln(e.getMessage());
		}
		
		return ShellStatus.CONTINUE;
	}

	/**
	 * Reads the file and performs he hexdump.
	 * 
	 * @param env environment used for communcation
	 * @param file which is read
	 */
	private void dump(Environment env, Path file) {
		try (InputStream is = new BufferedInputStream(Files.newInputStream(file,
				StandardOpenOption.READ))) {
			byte[] buffer = new byte[16];
			int counter = 0;
			
			StringBuilder sb = new StringBuilder();
			while (true) {
				sb.setLength(0);
				sb.append(String.format("%08X:", counter));
				int i = is.read(buffer);
				if(i <= 0) break;
				counter += i;
				sb.append(getBytes(buffer, i));
				changeIfInvalidChar(buffer);
				sb.append(new String(buffer, 0, i, "UTF-8"));
				env.writeln(sb.toString());
			}
			
		} catch (IOException e) {
			env.writeln("Unexpected error during byte reading!");
		}
	}
	
	/**
	 * Gets the bytes in text form.
	 * 
	 * @param buffer bytes to be transformed to text
	 * @param i amount of bytes to be transformed
	 * @return String representation of the given bytes
	 */
	private String getBytes(byte[] buffer, int i) {
		StringBuilder sb = new StringBuilder();
		for (int index = 0; index < 8; index++) {
			if (index == i) {
				sb.append("   ".repeat(8 - i));
				break;
			}
			sb.append(String.format(" %02X", buffer[index]));
		}
		sb.append("|");
		for (int index = 8; index < 16; index++) {
			if (index == i) {
				sb.append("   ".repeat(16 - i));
				break;
			}
			sb.append(String.format("%02X ", buffer[index]));
		}
		sb.append("| ");
		return sb.toString();
	}
	
	/**
	 * Changes invalid character whose value is less than 32 or greater than 127 to 
	 * character '.'.
	 * 
	 * @param buffer
	 */
	private void changeIfInvalidChar(byte[] buffer) {
		int length = buffer.length;
		for (int index = 0; index < length; index++) {
			buffer[index] = (buffer[index] < 32 || buffer[index] > 127 ? UTF_DOT : buffer[index]);
		}
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		
		description.add("Dumps a file's content in shape of bytes.");
		description.add("16 bytes are presented per row.");
		description.add("The bytes are preceded by a hex counter.");
		description.add("The bytes are followed by their string representation.");
		description.add("Characters with value less than 32 or greater than 127 are replaced by '.'");
		
		return Collections.unmodifiableList(description); 
	}

}
