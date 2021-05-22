package hr.fer.zemris.java.hw05.db.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

import hr.fer.zemris.java.hw05.db.QueryFilter;
import hr.fer.zemris.java.hw05.db.QueryParser;
import hr.fer.zemris.java.hw05.db.QueryParserException;
import hr.fer.zemris.java.hw05.db.StudentDatabase;
import hr.fer.zemris.java.hw05.db.StudentRecord;

/**
 * This is a demo class used for demonstrating the functionality of all the classes and interfaces
 * created in the parent package.
 * It uses a text file to read information about students and creates a database of student records
 * formed from text. Those records are then filtered out using the query command which contains 
 * one or multiple conditional expressions parsed via a query parser. Those filtered records
 * are stored and then printed out for the user to see the results of the queries.
 * <p>
 * The user is asked to continuously input query commands and the program prints out the result
 * for every query until the program is exited.
 * <p>
 * The queries are legal if they contain expressions formed with a field value, followed by an operator,
 * and then a string literal. Multiple expressions can be chained using the logical operator "AND".
 * <p>
 * To exit the program, user should enter the "exit" command.
 * 
 * @author stipe
 *
 */
public class CompleteDemo {

	public static void main(String[] args) {
		StudentDatabase db = null;
		try {
			db =new StudentDatabase(
					Files.readAllLines(Paths.get("src/test/resources/database.txt"),
					StandardCharsets.UTF_8)
			);
		} catch (IOException e) {
			System.out.println("Couldn't read from file \"src/test/resources/database.txt\"!");
		}
		
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("> ");
			if (sc.hasNext()) {
				String token = sc.next();
				if (token.equals("exit")) {
					break;
				}
				if (token.equals("query")) {
					if (!sc.hasNextLine()) {
						System.out.println("Command query must be followed by arguments!");
						continue;
					}
					String query = sc.nextLine();
					QueryParser parser = null;
					try {
						parser = new QueryParser(query);
					} catch (QueryParserException ex) {
						System.out.println(ex.getMessage());
						continue;
					}
					List<StudentRecord> filtered = new ArrayList<>();
					if(parser.isDirectQuery()) {
						System.out.println("Using index for record retrieval.");
						StudentRecord r = db.forJMBAG(parser.getQueriedJMBAG());
						filtered.add(r);
					} else {
						try {
							for(StudentRecord r : db.filter(new QueryFilter(parser.getQuery()))) {
								filtered.add(r);
							}
						} catch (IllegalArgumentException exc) {
							System.out.println(exc.getMessage());
							continue;
						}
					}
					printQueriedData(filtered);
				
				} else {
					System.out.println("Unknown command!");
					token = sc.nextLine();
				}
			}
		}
		sc.close();
		System.out.println("Goodbye!");
	}

	/**
	 * Prints out the queried records filtered with the given expressions.
	 * 
	 * @param filtered <code>List</code> of <code>StudentRecords</code>
	 */
	private static void printQueriedData(List<StudentRecord> filtered) {
		if (filtered.size() == 0) {
			System.out.println("Records selected: 0");
			return;
		}
		int jmbagLength = getMaxLength(filtered, StudentRecord::getJmbag); // No where in this homework is jmbag specified to always be 10.
		int lastNameLength = getMaxLength(filtered, StudentRecord::getLastName);
		int firstNameLength = getMaxLength(filtered, StudentRecord::getFirstName);
		
		printLine(jmbagLength, lastNameLength, firstNameLength);
		for (StudentRecord r : filtered) {
			printStudent(r, jmbagLength, lastNameLength, firstNameLength);
		}
		printLine(jmbagLength, lastNameLength, firstNameLength);
		System.out.println("Records selected: " + filtered.size());
	}

	/**
	 * Prints out one student record from the queried data.
	 * 
	 * @param r the record to be printed
	 * @param jmbagLength the length of the JMBAG column
	 * @param lastNameLength the length of the last name column
	 * @param firstNameLength the length of the first name column
	 */
	private static void printStudent(StudentRecord r, int jmbagLength, int lastNameLength, int firstNameLength) {
		StringBuilder sb = new StringBuilder();
		sb.append("| " + r.getJmbag());
		for (int i = 0; i < jmbagLength - r.getJmbag().length(); i++) {
			sb.append(" ");
		}
		sb.append(" | " + r.getLastName());
		for (int i = 0; i < lastNameLength - r.getLastName().length(); i++) {
			sb.append(" ");
		}
		sb.append(" | " + r.getFirstName());
		for (int i = 0; i < firstNameLength - r.getFirstName().length(); i++) {
			sb.append(" ");
		}
		sb.append(" | " + r.getFinalGrade() +" |");
		System.out.println(sb.toString());
	}

	/**
	 * Prints out one line consisting of "+"s and "*"s which is used for formatting of the
	 * output of the queried data.
	 * 
	 * @param jmbagLength the length of the JMBAG column
	 * @param lastNameLength the length of the last name column
	 * @param firstNameLength the length of the first name column
	 */
	private static void printLine(int jmbagLength, int lastNameLength, int firstNameLength) {
		StringBuilder sb = new StringBuilder();
		sb.append("+");
		for (int i = 0; i < jmbagLength + 2; i++) {
			sb.append("=");
		}
		sb.append("+");
		for (int i = 0; i < lastNameLength + 2; i++) {
			sb.append("=");
		}
		sb.append("+");
		for (int i = 0; i < firstNameLength + 2; i++) {
			sb.append("=");
		}
		sb.append("+===+");
		System.out.println(sb.toString());
	}

	/**
	 * Calculates and returns the length of the longest given field value.
	 * 
	 * @param filtered <code>List</code> of <code>StudentRecords</code>
	 * @param function <code>Function</code> which decides what field value will be checked for length
	 * @return length of the longest value of the wanted field from the list
	 */
	private static int getMaxLength(List<StudentRecord> filtered, Function<StudentRecord, String> function) {
		return filtered.stream()
				.map(function)
				.mapToInt(s -> s.length())
				.max().getAsInt(); // OptionalInt is surely present because filtered.size() != 0.
	}
	
}
