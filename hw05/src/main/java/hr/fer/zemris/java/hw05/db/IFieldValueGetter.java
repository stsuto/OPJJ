package hr.fer.zemris.java.hw05.db;

/**
 * Interface <code>IComparisonOperator</code> represents a strategy which is responsible
 * for obtaining a requested field value from given the <code>StudentRecord</code>.
 * 
 * @author stipe
 *
 */
public interface IFieldValueGetter {

	/**
	 * Obtains a requested field value from given the <code>StudentRecord</code>.
	 * The field values are either JMBAG, lastName, or firstName.
	 * 
	 * @param record from which the field value is obtained
	 * @return String of the obtained field value
	 */
	String get(StudentRecord record);
	
}
