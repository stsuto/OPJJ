package hr.fer.zemris.java.custom.collections;

/**
 * This class is a model of an object capable of performing some operation on the passed object.
 * Represents a conceptual contract between clients which will have objects to be processed, and each concrete Processor which knows how to perform the selected operation.
 * @author stipe
 *
 */
public class Processor {

	/**
	 * Method that every processor uses to perform an operation on an object.
	 * It differs from one processor to another so each processor must implement it to perform its function.
	 * Here it is implemented to do nothing.
	 * 
	 * @param value object to be processed
	 */
	public void process(Object value) {
	}
	
}
