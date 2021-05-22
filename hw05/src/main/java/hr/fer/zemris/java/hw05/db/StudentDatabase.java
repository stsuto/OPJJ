package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class <code>StudentDatabase</code> represents a database of {@link StudentRecord}.
 * The records are created by reading from a text file and are stored so the user can 
 * use the database to get records for certain JMBAGs and enables filtration of its data.
 * 
 * @author stipe
 *
 */
public class StudentDatabase {

	private List<StudentRecord> records;
	private Map<String, StudentRecord> index;
	
	/**
	 * Constructor which accepts a <code>List</code> of <code>String</code> objects, 
	 * each representing information about one student, creating a new
	 * instance of <code>StudentDatabase</code> which stores that 
	 * information into <code>StudentRecords</code>.
	 * 
	 * @param list <code>List</code> of <code>String</code> objects containing 
	 * 			   information about students
	 */
	public StudentDatabase(List<String> list) {
		records = initDatabase(list); // List is initialized with method's return value, map is initialized within the method.
	}
	
	/**
	 * Uses the given <code>List</code> of <code>String</code> objects containing 
	 * information about students without duplicates to create a <code>List</code> of <code>StudentRecords</code>
	 * and a <code>Map</code> for quick retrieval of records via JMBAG.
	 * 
	 * @param list <code>List</code> of <code>String</code> objects containing 
	 * 		  information about students
	 * @return <code>List</code> of <code>StudentRecords</code> made from the information
	 * 		   from Strings
	 * @throws IllegalArgumentException if the given information is a duplicate, or if final grade
	 * 		   is not a whole number in interval [1, 5]
	 */
	private List<StudentRecord> initDatabase(List<String> list) {
		// Using local variables is faster than constantly getting member variables.
		Map<String, StudentRecord> recordMap = new LinkedHashMap<>();
		List<StudentRecord> recordList = new ArrayList<>();
		
		for (String student : list) {
			String[] data = student.split("\\t");
			int grade = Integer.parseInt(data[3]);
			if (grade < 1 || grade > 5) {
				throw new IllegalArgumentException("The given text contains invalid final grades!");
			}
			String jmbag = data[0];
			if (recordMap.containsKey(jmbag)) {
				throw new IllegalArgumentException("The given text contains double record values!");
			}
			StudentRecord record = new StudentRecord(jmbag, data[1], data[2], grade);
			recordMap.put(jmbag, record);
			recordList.add(record);
		}
		
		index = recordMap;
		return recordList;
	}

	/**
	 * Finds and returns the <code>StudentRecord</code> containing 
	 * the given JMBAG in O(1) time. If the record doesn't exist,
	 * null is returned.
	 * 
	 * @param jmbag the JMBAG of the student whose record is to be returned
	 * @return <code>StudentRecord</code> containing the given JMBAG.
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return index.get(jmbag);
	}
	
	/**
	 * Accepts a reference to an object that will filter the records to 
	 * ones that satisfy its condition by looping through all student 
	 * records and calling its <code>accepts</code> method.
	 * 
	 * @param filter object which filters the records
	 * @return <code>List</code> of <code>StudentRecords</code> which
	 * 		   satisfy the filter's condition
	 */
	public List<StudentRecord> filter(IFilter filter){
		List<StudentRecord> filteredList = new ArrayList<>();
		records.forEach(record -> {
			if (filter.accepts(record)) {				
				filteredList.add(record);
			}
		});	
		return filteredList;
	}

}
