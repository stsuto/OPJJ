package hr.fer.zemris.java.hw05.db.demo;

import java.io.IOException;

import hr.fer.zemris.java.hw05.db.QueryParser;

/**
 * This is a demo class used for demonstrating the functionality of the {@link QueryParser} class.
 * In this class two instances of <code>QueryParser</code> are created, and their methods are used
 * to show the functions of the parser.
 * The parsers are given a <code>String</code> object representing a query, and are then checked 
 * if the queries are direct and if information retrieval is possible.
 * 
 * @author stipe
 *
 */
public class QueryParserDemo {

	public static void main(String[] args) throws IOException {
		
		QueryParser qp1 = new QueryParser(" jmbag =\"0123456789\" ");
		System.out.println("isDirectQuery(): " + qp1.isDirectQuery()); // true
		System.out.println("jmbag was: " + qp1.getQueriedJMBAG()); // 0123456789
		System.out.println("size: " + qp1.getQuery().size()); // 1
		
		System.out.println();
		
		QueryParser qp2 = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
		System.out.println("isDirectQuery(): " + qp2.isDirectQuery()); // false
//		 System.out.println(qp2.getQueriedJMBAG()); // would throw!
		System.out.println("size: " + qp2.getQuery().size()); // 2
	}
	
}
