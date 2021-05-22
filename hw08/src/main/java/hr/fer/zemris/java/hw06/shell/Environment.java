package hr.fer.zemris.java.hw06.shell;

import java.nio.file.Path;
import java.util.SortedMap;

/**
 * The {@code Environment} interface is an abstraction which will be passed to each defined command.
 * It is used for communicating with the user.
 * 
 * @author stipe
 *
 */
public interface Environment {

	/**
	 * Reads the user's input as a line and returns it.
	 * 
	 * @return {@code String} of the next line input
	 * @throws ShellIOException in case of an error during reading
	 */
	String readLine() throws ShellIOException;
	
	/**
	 * Writes the given text to the shell.
	 * 
	 * @param text the text to be written
	 * @throws ShellIOException in case of an error during writing
	 */
	void write(String text) throws ShellIOException;
	
	/**
	 * Writes the given text to the shell and goes to a new line.
	 * 
	 * @param text the text to be written
	 * @throws ShellIOException in case of an error during writing
	 */
	void writeln(String text) throws ShellIOException;
	
	/**
	 * Creates and returns an unmodifiable sorted map of commands 
	 * defined for this environment.
	 * 
	 * @return unmodifiable map of commands
	 */
	SortedMap<String, ShellCommand> commands();
	
	/**
	 * Returns the current symbol used for informing the shell that 
	 * more lines are to be expected.
	 * 
	 * @return current multiline symbol
	 */
	Character getMultilineSymbol();
	
	/**
	 * Sets the multiline symbol to the given symbol.
	 * 
	 * @param symbol the symbol to be set
	 */
	void setMultilineSymbol(Character symbol);
	
	/**
	 * Returns the current symbol that the shell uses for informing 
	 * the user another input can be accepted.
	 * 
	 * @return current prompt symbol
	 */
	Character getPromptSymbol();
	
	/**
	 * Sets the prompt symbol to the given symbol.
	 * 
	 * @param symbol the symbol to be set
	 */
	void setPromptSymbol(Character symbol);
	
	/**
	 * Returns the current symbol that the shell uses for informing 
	 * the user that the line is a continuation of previous lines.
	 * 
	 * @return current more-lines symbol
	 */
	Character getMorelinesSymbol();
	
	/**
	 * Sets the more-lines symbol to the given symbol.
	 * 
	 * @param symbol the symbol to be set
	 */
	void setMorelinesSymbol(Character symbol);
	
	/**
	 * Returns the path to the current working directory.
	 * 
	 * @return {@code Path} of current directory
	 */
	Path getCurrentDirectory();
	
	/**
	 * Sets the directory with the given path as the current working directory.
	 * 
	 * @param path {@code Path} to directory
	 */
	void setCurrentDirectory(Path path);
	
	/**
	 * Returns the shared data saved on the given {@code String} key.
	 * 
	 * @param key data's key
	 * @return {@code Object} with shared data
	 */
	Object getSharedData(String key);
	
	/**
	 * Adds an object to shared data.
	 * 
	 * @param key key to which the data is saved
	 * @param value object saved
	 */
	void setSharedData(String key, Object value);
	
}
