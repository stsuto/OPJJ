package hr.fer.zemris.java.hw05.db;

/**
 * Interface <code>IFilter</code> represents a filter which decides which record is to
 * be accepted by calling its <code>accept</code> method upon the record.
 * 
 * @author stipe
 *
 */
public interface IFilter {

	/**
	 * Checks if the given <code>StudentRecord</code> satisfies the condition
	 * within the method.
	 * 
	 * @param record <code>StudentRecord</code> to be checked
	 * @return <code>true</code> if the record satisfies the condition, and <code>false</code> otherwise
	 */
	boolean accepts(StudentRecord record);
	
}
