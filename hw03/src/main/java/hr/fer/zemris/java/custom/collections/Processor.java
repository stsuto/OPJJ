package hr.fer.zemris.java.custom.collections;

/**
 * This interface contains methods capable of performing operations on the passed object.
 * Represents a conceptual contract between clients which will have objects to be processed, and each concrete Processor which knows how to perform the selected operation.
 * @author stipe
 *
 */
public interface Processor {

	/**
	 * Method that every processor uses to perform an operation on an object.
	 * @param value object to be processed
	 */
	void process(Object value);
	
}
