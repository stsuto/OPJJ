package hr.fer.zemris.java.util;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellIOException;

/**
 * Class {@code Util} is a utility class used for increasing simplicity and 
 * reducing duplications of code from multiple classes. 
 * <p>
 * It offers a method which asserts that the argument isn't null, and a method 
 * which processes arguments, depending on if they are surrounded by quotation
 * marks or not, and then creates a {@code List} of arguments created by processing.
 * <p>
 * The classes and methods using this method should then check if the arguments this 
 * method returns are valid for their case.
 * 
 * @author stipe
 *
 */
public class Util {
	
	/**
	 * 
	 * @param env
	 * @param pathName
	 * @return
	 * @throws ShellIOException if the path isn't valid for this OS
	 */
	public static Path normalizePath(Environment env, String pathName) {
		try {			
			return env.getCurrentDirectory().resolve(Paths.get(pathName)).normalize();
		
		} catch (InvalidPathException ex) {
			throw new ShellIOException("Illegal path name for your OS!");
		}
	}
	
	/**
	 * Asserts whether or not the given object is null and sends
	 * a customized message through an exception if it is null.
	 * 
	 * @param object to be checked if null
	 * @param objectName name of the object for exception output
	 * @return the given object if it is not null
	 * @throws NullPointerException if the given object is null
	 */
	public static <T> T requireNonNull(T object, String objectName) {
		return Objects.requireNonNull(object, objectName + " mustn't be null!");
	}
	
	/**
	 * Processes the given {@code String}, creating arguments separated by whitespace, 
	 * unless informed otherwise by using quotation marks.
	 * <p>
	 * The arguments are created and put into a {@code List} until the given String has 
	 * been fully processed.
	 * 
	 * @param arguments {@code String} object containing arguments.
	 * @return {@code List} of {@code String} objects representing processed arguments
	 * @throws ShellIOException if the given arguments were illegal 
	 * (null or contained illegal characters)
	 */
	public static List<String> processArguments(String arguments) {
		if (arguments == null) {
			throw new ShellIOException("Arguments given after the command must not be null!");
		}
		List<String> processedArgs = new ArrayList<>();
		
		while (!arguments.isEmpty()) {
			char[] data = arguments.strip().toCharArray();
			int current = 0;

			if (data[0] == '\"') {
				List<Integer> escapes = new ArrayList<>();
				current++;
				while (isIndexLegal(data, current) && !isQuoteEnd(data, current)) {
					if (isEscapeSequence(data, current)) {
						escapes.add(current - 1);
						current += 2;
					} else {
						current++;					
					}
				}
				checkForErrors(data, current++);
				processedArgs.add(getPath(data, current, escapes));
			
			} else {
				while (isIndexLegal(data, current) && !Character.isWhitespace(data[current])) {
					current++;
				}				
				processedArgs.add(new String(data, 0, current));
			}
			
			arguments = arguments.substring(current).strip();
		}
		
		return processedArgs;
	}

	
	/**
	 * Checks if errors have happened at current index of data.
	 * Errors could happen if the end of the argument string has been reached, 
	 * but the quotation marks were never closed, or if the quoted text is 
	 * immediately followed by some characters other than whitespace.
	 * 
	 * @param data
	 * @param current
	 */
	private static void checkForErrors(char[] data, int current) {
		if (!isIndexLegal(data, current)) {
			throw new ShellIOException("The quotation marks are never closed!");
		}
		// If next index is still legal, but it's not a whitespace character, the argument is illegal and exception is thrown.
		if (isIndexLegal(data, ++current) && !Character.isWhitespace(data[current])) {
			throw new ShellIOException("Quotation marks must be followed by at least one whitespace character.");
		}
	}

	/**
	 * Creates and returns a {@code Path} from the data processed until this moment, 
	 * deleting all escape sequence characters in the process.
	 * 
	 * @param data characters from which the path will be formed
	 * @param current index to which data has been processed
	 * @param escapes escape sequence characters which are to be ignored
	 * @return {@code Path} created from the given data
	 */
	private static String getPath(char[] data, int current, List<Integer> escapes) {
		StringBuilder sb = new StringBuilder();
		sb.append(new String(data, 1, current - 2)); // Subtract the two '"' characters.
		escapes.sort(Collections.reverseOrder());
		escapes.forEach(sb::deleteCharAt);
		
		return sb.toString();
	}
	
	/**
	 * Checks if the character at index {@code current} is the first character of
	 * an escape sequence.
	 * 
	 * @param data characters which are being processed
	 * @param current index of the character currently being tested
	 * @return {@code true} if the character is the beginning of an escape sequence, {@code false} otherwise
	 */
	private static boolean isEscapeSequence(char[] data, int current) {		
		return data[current] == '\\' 
				&& (data[current + 1] == '\"' || data[current + 1] == '\\');
	}

	/**
	 * Checks if the character at index {@code current} is the end-quote character.
	 * Character is defined to be the end of the quotation if it is not prefaced by an
	 * escape character, or if the would be escape character was escaped beforehand.
	 * 
	 * @param data characters which are being processed
	 * @param current index of the character currently being tested
	 * @return {@code true} if the character is end-quote, {@code false} otherwise
	 */
	private static boolean isQuoteEnd(char[] data, int current) {
		return data[current] == '"' 
				&& (data[current - 1] != '\\'
					|| isEscapeSequence(data, current - 2));
	}

	/**
	 * Checks if the current index is legal.
	 * 
	 * @param data array of characters which are being processed
	 * @param current the index which is tested if it is in the array
	 * @return {@code true} if the character is legal, {@code false} otherwise
	 */
	private static boolean isIndexLegal(char[] data, int current) {
		return current < data.length;
	}
	
	
}
