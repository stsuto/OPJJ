package hr.fer.zemris.java.hw05.db.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import hr.fer.zemris.java.hw05.db.ComparisonOperators;
import hr.fer.zemris.java.hw05.db.ConditionalExpression;
import hr.fer.zemris.java.hw05.db.FieldValueGetters;
import hr.fer.zemris.java.hw05.db.IFilter;
import hr.fer.zemris.java.hw05.db.StudentDatabase;
import hr.fer.zemris.java.hw05.db.StudentRecord;

/**
 * This is a demo class used for demonstrating the functionality of the {@link ConditionalExpression} class.
 * It uses a text file to read information about students and creates a database of student records
 * formed from text. Those records are then filtered out using a <code>ConditionalExpression</code> and its 
 * properties. Those filtered records are stored and then printed out for the user to see the results of the filtration.
 * 
 * @author stipe
 *
 */
public class ConditionalExpressionDemo {

	public static void main(String[] args) throws IOException {
		StudentDatabase db = null;
		try {
			db =new StudentDatabase(
					Files.readAllLines(Paths.get("src/test/resources/database.txt"),
					StandardCharsets.UTF_8)
			);
		} catch (IOException e) {
			System.out.println("Couldn't read from file \"src/test/resources/database.txt\"!");
		}
		
		ConditionalExpression expr = new ConditionalExpression(
				FieldValueGetters.LAST_NAME,
				"Bos*",
				ComparisonOperators.LIKE
		);
		IFilter filter = null;
		try {
			filter = (rec) -> expr.getComparisonOperator().satisfied(
					expr.getFieldGetter().get(rec), // returns lastName from given record
					expr.getStringLiteral() // returns "Bos*"
				);
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		}
		List<StudentRecord> list = db.filter(filter);
		list.forEach(record -> {
			System.out.println("First name: " + FieldValueGetters.FIRST_NAME.get(record));
			System.out.println("Last name: " + FieldValueGetters.LAST_NAME.get(record));
			System.out.println("JMBAG: " + FieldValueGetters.JMBAG.get(record));
		});
	}
	
}
