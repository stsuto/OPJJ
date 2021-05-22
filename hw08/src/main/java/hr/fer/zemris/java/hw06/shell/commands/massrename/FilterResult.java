package hr.fer.zemris.java.hw06.shell.commands.massrename;

import java.util.regex.Matcher;

import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.util.Util;

/**
 * Class {@code FilterResult} represents a single file wrapped with 
 * other information such as matching groups.
 * 
 * @author stipe
 *
 */
public class FilterResult {

	/**
	 * Name of the file.
	 */
	private String fileName;
	/**
	 * Matcher that matches the file name to the pattern.
	 */
	private Matcher matcher;
	
	/**
	 * Constructor which initializes the properties.
	 * 
	 * @param fileName {@link #fileName}
	 * @param matcher {@link #matcher}
	 */
	public FilterResult(String fileName, Matcher matcher) {
		this.fileName= Util.requireNonNull(fileName, "File name");
		this.matcher = Util.requireNonNull(matcher, "Matcher");
	}
	
	@Override
	public String toString() {
		return fileName;
	}
	
	/**
	 * Returns how many groups were found.
	 * 
	 * @return number of groups
	 */
	public int numberOfGroups() {
		return matcher.groupCount();
	}
	/**
	 * Fetches the group at the given index.
	 * 
	 * @param index group index
	 * @return String from the group
	 * @throws ShellIOException if the given group index is invalid
	 */
	public String group(int index) {
		if (index < 0 || index > numberOfGroups()) {
			throw new ShellIOException("Invalid group number!");
		}

		return matcher.group(index);
	}
	
}
