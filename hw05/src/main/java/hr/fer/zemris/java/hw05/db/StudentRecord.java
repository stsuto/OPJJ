package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Class <code>StudentRecord</code> represents one student's record.
 * It contains information about the student's JMBAG, first and last name,
 * and their final grade.
 * There are no duplicate records.
 * 
 * @author stipe
 *
 */
public class StudentRecord {

	/**
	 * The student's JMBAG.
	 */
	private String jmbag;
	/**
	 * The student's last name.
	 */
	private String lastName;
	/**
	 * The student's first name.
	 */
	private String firstName;
	/**
	 * The student's final grade.
	 */
	private int finalGrade;

	/**
	 * The constructor which accepts all record properties and creates
	 * a new instance of <code>StudentRecord</code> containing the
	 * given information.
	 * 
	 * @param jmbag {@link #jmbag} in the new record
	 * @param lastName {@link #lastName} in the new record
	 * @param firstName {@link #firstName} in the new record
	 * @param finalGrade {@link #finalGrade} in the new record
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}

	/**
	 * Getter for this <code>StudentRecord</code>'s JMBAG.
	 * 
	 * @return {@link #jmbag}
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Getter for this <code>StudentRecord</code>'s last name.
	 * 
	 * @return {@link #lastName}
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Getter for this <code>StudentRecord</code>'s first name.
	 * 
	 * @return {@link #firstName}
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Getter for this <code>StudentRecord</code>'s final grade.
	 * 
	 * @return {@link #finalGrade}
	 */
	public int getFinalGrade() {
		return finalGrade;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(jmbag);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof StudentRecord))
			return false;
		StudentRecord other = (StudentRecord) obj;
		return Objects.equals(jmbag, other.jmbag);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "StudentRecord [jmbag=" + jmbag + ", lastName=" + lastName + ", firstName=" + firstName + ", finalGrade="
				+ finalGrade + "]";
	}
	
	 
	
}
