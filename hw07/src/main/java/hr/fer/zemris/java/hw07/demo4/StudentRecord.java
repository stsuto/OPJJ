package hr.fer.zemris.java.hw07.demo4;

/**
 * Class {@code StudentRecord} represents a record of student's information,
 * such as IDs and exam results. 
 * 
 * @author stipe
 *
 */
public class StudentRecord {

	/**
	 * Student's JMBAG.
	 */
	private String jmbag;
	/**
	 * Student's last name.
	 */
	private String lastName;
	/**
	 * Student's first name.
	 */
	private String firstName;
	/**
	 * Student's result on the first exam.
	 */
	private double firstExamResult;
	/**
	 * Student's result on the second exam.
	 */
	private double secondExamResult;
	/**
	 * Student's lab results.
	 */
	private double labResult;
	/**
	 * Student's final grade.
	 */
	private int grade;
	
	/**
	 * Constructor which initializes all properties with the given parameters.
	 * 
	 * @param jmbag {@link #jmbag}
	 * @param lastName {@link #lastName}
	 * @param firstName {@link #firstName}
	 * @param firstExamResult {@link #firstExamResult}
	 * @param secondExamResult {@link #secondExamResultjmbag}
	 * @param labResult {@link #labResult}
	 * @param grade {@link #grade}
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, double firstExamResult, 
			double secondExamResult, double labResult, int grade) {
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.firstExamResult = firstExamResult;
		this.secondExamResult = secondExamResult;
		this.labResult = labResult;
		this.grade = grade;
	}
	
	@Override
	public String toString() {
		return String.format("%s\t%s\t%s\t%f\t%f\t%f\t%d", jmbag, lastName, 
				firstName, firstExamResult, secondExamResult, labResult, grade);
	}

	/**
	 * Getter for student's JMBAG.
	 * 
	 * @return {@link #jmbag}
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Getter for student's last name.
	 * 
	 * @return {@link #lastName}
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Getter for student's first name.
	 * 
	 * @return {@link #firstName}
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Getter for student's first exam result.
	 * 
	 * @return {@link #firstExamResult}
	 */
	public double getFirstExamResult() {
		return firstExamResult;
	}

	/**
	 * Getter for student's second exam result.
	 * 
	 * @return {@link #secondExamResult}
	 */
	public double getSecondExamResult() {
		return secondExamResult;
	}

	/**
	 * Getter for student's lab result.
	 * 
	 * @return {@link #labResult}
	 */
	public double getLabResult() {
		return labResult;
	}

	/**
	 * Getter for student's grade.
	 * 
	 * @return {@link #grade}
	 */
	public int getGrade() {
		return grade;
	}
	
}
