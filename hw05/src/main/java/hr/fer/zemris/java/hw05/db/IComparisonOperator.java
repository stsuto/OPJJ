package hr.fer.zemris.java.hw05.db;

/**
 * Interface <code>IComparisonOperator</code> represents a strategy which compares two 
 * <code>String</code> objects and decides if the implemented condition is satisfied.
 * 
 * @author stipe
 *
 */
public interface IComparisonOperator {
	
	/**
	 * Compares the given <code>String</code> objects and checks if they
	 * satisfy the comparison equation given in the method's impelmentaion.
	 * 
	 * @param value1 the first <code>String</code>
	 * @param value2 the second <code>String</code>
	 * @return <code>true</code> if the comparison is satisfied, and <code>false</code> otherwise
	 */
	boolean satisfied(String value1, String value2);
	
}
