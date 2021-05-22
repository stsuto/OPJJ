package hr.fer.zemris.java.custom.collections;

/**
 * Interface <code>Tester</code> is a model of an object 
 * that receives another object and determines if it should be accepted.
 * 
 * @author stipe
 */
public interface Tester {

	/**
	 * Determines if the given object satisfies the condition in the method.
	 * @param obj Object to be tested
	 * @return <code>true</code> if the object satisfied the condition and is to be accepted, <code>false</code> otherwise
	 */
	boolean test(Object obj);
	
}
