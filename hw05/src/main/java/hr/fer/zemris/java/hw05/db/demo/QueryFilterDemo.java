package hr.fer.zemris.java.hw05.db.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.hw05.db.QueryFilter;
import hr.fer.zemris.java.hw05.db.QueryParser;
import hr.fer.zemris.java.hw05.db.StudentDatabase;
import hr.fer.zemris.java.hw05.db.StudentRecord;

/**
 * This is a demo class used for demonstrating the functionality of the {@link QueryFilter} class 
 * and all classes necessary for its usage.
 * The class uses a text file to read information about students and creates a database of student records
 * formed from text. Those records are then filtered out using a predefined query command which contains 
 * one or multiple conditional expressions parsed via a query parser. Those filtered records
 * are stored and then printed out for the user to see the results of the queries.
 * <p>
 * The queries are legal if they contain expressions formed with a field value, followed by an operator,
 * and then a string literal. Multiple expressions can be chained using the logical operator "AND".
 * <p>
 * To exit the program, user should enter the "exit" command.
 * 
 * @author stipe
 *
 */
public class QueryFilterDemo {

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
		
		QueryParser parser = new QueryParser("jmbag >= \"0000000027\" aNd lastName>\"K\"");
		if(parser.isDirectQuery()) {
			StudentRecord r = db.forJMBAG(parser.getQueriedJMBAG());
			System.out.println(r);
		} else {
			try {
				for(StudentRecord r : db.filter(new QueryFilter(parser.getQuery()))) {
					System.out.println(r);
				}
			} catch (IllegalArgumentException ex) { // If LIKE operator was used incorrectly.
				System.out.println(ex.getMessage());
			}
		}
	}
	
}
