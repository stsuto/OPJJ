package hr.fer.zemris.java.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Class {@link Util} is used for increasing simplicity 
 * and reducing duplications of code from multiple classes.
 * 
 * @author stipe
 *
 */
public class Util {

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
	 * Loads and reads file content from the given String 
	 * representation of file path.
	 * 
	 * @param path
	 * @return file content
	 */
	public static String readFromDisk(String path) {
		return readFromDisk(Paths.get(path));
	}
	
	/**
	 * Recovers lost escapes from parsing.
	 * 
	 * @param string
	 * @return text with recovered escapes
	 */
	public static String getLostEscapes(String string) {
		String rebuiltString = string.replace("\\", "\\\\").replace("\"", "\\\"");
		return rebuiltString.replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n");
	}

	/**
	 * Loads and reads file content from the given path.
	 * 
	 * @param path
	 * @return file content
	 */
	public static String readFromDisk(Path path) {
		String docBody = null;
		
		try {
			docBody = new String(
						Files.readAllBytes(path),
						StandardCharsets.UTF_8
					);
			
		} catch (IOException e) {
			System.out.println("Reading file failed!");
			System.exit(1);
		}
		
		return docBody;
	}
	
}
