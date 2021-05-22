package hr.fer.zemris.java.hw17.trazilica;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * This class is the console implementation for this application.
 * It offers a shell user interface which the user can use to perform queries,
 * read documents, and calculate similarities.
 * 
 * @author stipe
 *
 */
public class Konzola {

	/**
	 * Main method.
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		checkArguments(args);
		
		try {
			SearchDictionary dictionary = new SearchDictionary(args[0]);
			dictionary.initializeDocuments();
			System.out.println("The size of the vocabulary is " + dictionary.getSize() + " words.");
			startUserInteraction(dictionary);			
		
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Checks if the arguments are valid.
	 * 
	 * @param args arguments
	 */
	private static void checkArguments(String[] args) {
		if (args.length != 1) {
			System.out.println("One command line argument representing the path to articles must be given!");
			System.exit(1);
		}
	}

	/**
	 * Starts user interaction.
	 * 
	 * @param dictionary dictionary used for queries.
	 * @throws IOException 
	 */
	private static void startUserInteraction(SearchDictionary dictionary) throws IOException {
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			System.out.print("\nEnter command > ");
			
			try {	
				if (sc.hasNextLine()) {
					String tokens[] = sc.nextLine().strip().split("\\s+", 2);
					
					if (tokens[0].equals("exit")) {
						if (tokens.length != 1) {
							System.out.println("Unknown command!\n"
								+ "If you wish to exit, type 'exit' with no other parameters!");
							continue;
						}
						break;
					}
				
					if (tokens[0].equals("query")) {
						if (tokens.length == 1) {
							System.out.println("Command query must be followed by arguments!");
							continue;
						}
						executeQuery(dictionary, tokens[1]);
						
					} else if (tokens[0].equals("type")) {
						if (tokens.length == 1) {
							System.out.println("Command 'type' must be followed by an index argument!");
							continue;
						}
						if (dictionary.getQueryResult() == null) {
							System.out.println("No query has been executed yet.");
							continue;
						}
						executeType(dictionary, tokens[1]);
						
					} else if (tokens[0].equals("results")) {
						if (tokens.length != 1) {
							System.out.println("Command 'results' mustn't be followed by any arguments!");
							continue;
						}
						if (dictionary.getQueryResult() == null) {
							System.out.println("No query has been executed yet.");
							continue;
						}
						printResult(dictionary.getQueryResult());
						
					} else {
						System.out.println("Unknown command!");
					}
				}
			
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
			}
		}

		sc.close();
		System.out.println("Goodbye!");
	}

	/**
	 * Executes the type command.
	 * 
	 * @param dictionary dictionary
	 * @param nextLine next line to be parsed
	 */
	private static void executeType(SearchDictionary dictionary, String nextLine) {
		int index = parseIndex(nextLine);
		Document chosenDoc = getDocumentOnIndex(dictionary, index);
		
		printLines();
		System.out.println("Document: " + chosenDoc.getPath());
		printLines();
		System.out.println(chosenDoc.getBody());
		printLines();
	}

	/**
	 * Prints lines.
	 */
	private static void printLines() {
		System.out.println("----------------------------------------------------------------");
	}

	/**
	 * Finds and returns the document form the dictionary on given index.
	 * 
	 * @param dictionary dictionary
	 * @param index index
	 * @return document on index in dictionary
	 */
	private static Document getDocumentOnIndex(SearchDictionary dictionary, int index) {
		Map<Document, Double> sims = dictionary.getQueryResult();
		if (sims.size() <= index) {
			throw new RuntimeException("Type index is out of bounds!");
		}
		
		Iterator<Document> iter = sims.keySet().iterator();
		for (int i = 0; i < index; i++) {
			iter.next();
		}
		return iter.next();
	}

	/**
	 * Parses integer index from line.
	 * 
	 * @param nextLine line of text
	 * @return parsed integer
	 */
	private static int parseIndex(String nextLine) {
		try {
			return Integer.parseInt(nextLine);
		} catch (Exception e) {
			throw new RuntimeException("Invalid type index!");
		}
	}

	/**
	 * Executes the query command.
	 * 
	 * @param dictionary dictionary
	 * @param text line of text
	 */
	private static void executeQuery(SearchDictionary dictionary, String text) {
		Document queryDoc = dictionary.createQueryDocument(text);
		dictionary.initializeDocument(queryDoc);
		
		Map<Document, Double> similarities = dictionary.calculateSimilarities(queryDoc);
		
		System.out.println("Query is: " + queryDoc.getDocWords());
		System.out.println("10 best results:");
		printResult(similarities);
	}

	/**
	 * Executes the results command.
	 * 
	 * @param similarities map of document similarities
	 */
	private static void printResult(Map<Document, Double> similarities) {
		int size = similarities.size();
		Iterator<Entry<Document, Double>> simIterator = similarities.entrySet().iterator();
		for (int i = 0; i < size && i < 10; i++) {
			Entry<Document, Double> entry = simIterator.next();
			System.out.printf("[%2d] (%.4f) %s\n", 
					i, entry.getValue(), entry.getKey().getPath());
		}
	}

}
