package hr.fer.zemris.java.hw06.shell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class {@code Util} is a utility class offering a method which processes arguments, depending on
 * if they are surrounded by quotation marks or not, and then creates a {@code List} of arguments
 * created by processing.
 * <p>
 * The classes and methods using this method should then check if the arguments this method returns
 * are valid for their case.
 * 
 * @author stipe
 *
 */
public class Util {
	
	/**
	 * Processes the given {@code String}, creating arguments separated by whitespace, 
	 * unless informed otherwise by using quotation marks.
	 * <p>
	 * The arguments are created and put into a {@code List} until the given String has 
	 * been fully processed.
	 * 
	 * @param arguments {@code String} object containing arguments.
	 * @return {@code List} of {@code String} objects representing processed arguments
	 * @throws IllegalArgumentException if the given arguments were illegal
	 * @throws NullPointerException if the given arguments were {@code null}
	 */
	public static List<String> processArguments(String arguments) {
		if (arguments == null) {
			throw new IllegalArgumentException("Given arguments must not be null!");
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
			throw new IllegalArgumentException("The quotation marks are never closed!");
		}
		// If next index is still legal, but it's not a whitespace character, the argument is illegal and exception is thrown.
		if (isIndexLegal(data, ++current) && !Character.isWhitespace(data[current])) {
			throw new IllegalArgumentException("Quotation marks must be followed by at least one whitespace character.");
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
