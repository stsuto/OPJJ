package hr.fer.zemris.java.util;

import java.util.Objects;

/**
 * Class {@code Util} is a utility class used for increasing simplicity and 
 * reducing duplications of code from multiple classes. 
 * <p>
 * It offers a method which asserts that the argument isn't null, 
 * sending a customized exception if it is.
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
	
}
