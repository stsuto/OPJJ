package hr.fer.zemris.java.hw05.db;

/**
 * Class <code>FieldValueGetters</code> contains public static final variables 
 * representing field getters of type <code>IFieldValueGetter</code> used to obtain
 * <code>String</code> values of fields.
 * The field values are either JMBAG, lastName, or firstName.
 * 
 * @author stipe
 *
 */
public class FieldValueGetters {

	/**
	 * Obtains the first name from the record.
	 */
	public static final IFieldValueGetter FIRST_NAME = StudentRecord::getFirstName;
	/**
	 * Obtains the last name from the record.
	 */
	public static final IFieldValueGetter LAST_NAME = StudentRecord::getLastName;
	/**
	 * Obtains the JMBAG from the record.
	 */
	public static final IFieldValueGetter JMBAG = StudentRecord::getJmbag;
	
}
