package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw05.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw05.db.lexer.QueryLexerException;
import hr.fer.zemris.java.hw05.db.lexer.QueryTokenType;

/**
 * Class <code>QueryParser</code> represents a parser used for syntax analysis of query command input.
 * The parser uses tokens processed and created by {@link hr.fer.zemris.java.hw05.db.lexer.QueryLexer}.
 * 
 * @author stipe
 */
public class QueryParser {

	/**
	 * The lexer processing the document and giving tokens to the parser.
	 */
	private QueryLexer lexer;
	/**
	 * <code>ConditionalExpressions</code> from this query.
	 */
	private List<ConditionalExpression> query;
	
	/**
	 * Constructor that initializes the lexer by giving it the text to be parsed, and 
	 * starts the parsing process.
	 * 
	 * @param text the query to be processed
	 * @throws NullPointerException if text to be parsed is null
	 */
	public QueryParser(String text) {
		lexer = new QueryLexer(Objects.requireNonNull(text, "The query must not be null."));
		query = parseText();
	}

	/**
	 * Parses all tokens received from lexer and uses them to create <code>ConditionalExpressions</code>.
	 * 
	 * @return <code>List</code> of all <code>ConditionalExpressions</code> parsed
	 * @throws QueryParserException if the query command has illegal arguments
	 */
	private List<ConditionalExpression> parseText() {
		List<ConditionalExpression> expressions = new ArrayList<>();
		nextToken();
		
		while (true) {
			if (compareType(QueryTokenType.EOF)) {
				break;
			}
			
			IFieldValueGetter fieldGetter = parseFieldGetter();
			if (fieldGetter == null) {
				throw new QueryParserException("First argument in expression must be an attribute!");
			}
			
			nextToken();
			IComparisonOperator comparisonOperator = parseComparisonOperator();
			if (comparisonOperator == null) {
				throw new QueryParserException("Second argument in expression must be an operator!");
			}
			
			nextToken();
			String stringLiteral = parseStringLiteral();
			if (stringLiteral == null) {
				throw new QueryParserException("Third argument in expression must be a string literal!");
			}
			
			expressions.add(new ConditionalExpression(fieldGetter, stringLiteral, comparisonOperator));
			
			nextToken();
			if (!compareType(QueryTokenType.AND) && !compareType(QueryTokenType.EOF)) {
				throw new QueryParserException("Multiple expressions must be composed with the logical operator \"AND\"");
			} else if (compareType(QueryTokenType.AND)) {
				nextToken();
			}
		}
		
		return expressions;
	}

	/**
	 * Lets lexer know to get the next token from the text and set it as current token.
	 * 
	 * @throws QueryParserException if something unwanted happened during lexical analysis
	 */
	private void nextToken() {
		try {
			lexer.nextToken();
		} catch (QueryLexerException ex) {
			throw new QueryParserException(ex.getMessage());
		}
		
	}

	/**
	 * Parses the value from the current token if its of type ATTRIBUTE,
	 * otherwise returns null which will trigger an exception.
	 * 
	 * @return <code>IFieldValueGetter</code> which the token's value represents, or null if the token is improper
	 */
	private IFieldValueGetter parseFieldGetter() {
		switch (lexer.getToken().getValue()) {
			case "jmbag":
				return FieldValueGetters.JMBAG;
			
			case "lastName":
				return FieldValueGetters.LAST_NAME;
				
			case "firstName":
				return FieldValueGetters.FIRST_NAME;
		}
		
		return null;
	}

	/**
	 * Parses the value from the current token if its of type OPERATOR,
	 * otherwise returns null which will trigger an exception.
	 * 
	 * @return <code>IComparisonOperator</code> which the token's value represents, or null if the token is improper
	 */
	private IComparisonOperator parseComparisonOperator() {
		switch (lexer.getToken().getValue()) {
			case "<":
				return ComparisonOperators.LESS;
				
			case "<=":
				return ComparisonOperators.LESS_OR_EQUALS;
			
			case ">":
				return ComparisonOperators.GREATER;
			
			case ">=":
				return ComparisonOperators.GREATER_OR_EQUALS;
				
			case "=":
				return ComparisonOperators.EQUALS;
			
			case "!=":
				return ComparisonOperators.NOT_EQUALS;
			
			case "LIKE":
				return ComparisonOperators.LIKE;
		}
		
		return null;
	}
	
	/**
	 * Parses the value from the current token if its of type STRING,
	 * otherwise returns null which will trigger an exception.
	 * 
	 * @return <code>String</code> of token's value, or null if the token is improper
	 */
	private String parseStringLiteral() {
		return compareType(QueryTokenType.STRING) ? lexer.getToken().getValue() : null;
	}
	
	/**
	 * Compares the type of the current token to the type given as argument.
	 * @param type to be compared with current token's type
	 * @return <code>true</code> if the types are equal and <code>false</code> otherwise
	 */
	private boolean compareType(QueryTokenType type) {
		return lexer.getToken().getType() == type;
	}

	/**
	 * Checks if parsed query is direct. A query is direct if the only expression it
	 * contains is checking if JMBAG is equal to a string literal.
	 * 
	 * @return <code>true</code> if the query is direct, and <code>false</code> otherwise
	 */
	public boolean isDirectQuery() {
		ConditionalExpression expr = query.get(0);
		return query.size() == 1 
				&& expr.getFieldGetter().equals(FieldValueGetters.JMBAG)
				&& expr.getComparisonOperator().equals(ComparisonOperators.EQUALS);
	}

	/**
	 * Finds and returns the JMBAG from this direct query.
	 * 
	 * @return <code>String</code> value of JMBAG which was given in equality comparison
	 * @throws IllegalStateException if the query is not direct
	 */
	public String getQueriedJMBAG() {
		if (!isDirectQuery()) {
			throw new IllegalStateException("Query is not a direct one!");
		}
		return query.get(0).getStringLiteral();
	}
	
	/**
	 * Returns all conditional expressions from this query.
	 * 
	 * @return <code>List</code> of all <code>ConditionalExpressions</code>
	 */
	public List<ConditionalExpression> getQuery(){
		return query;
	}
	
}
