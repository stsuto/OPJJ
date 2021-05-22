package hr.fer.zemris.java.hw05.db;

import java.util.List;

/**
 * Class <code>QueryFilter</code> represents an object which filter {@link StudentRecord} objects
 * depending on if they satisfy every {@link ConditionalExpression} given in the query command.
 * 
 * @author stipe
 *
 */
public class QueryFilter implements IFilter {
	
	/**
	 * Expressions from the query command.
	 */
	private List<ConditionalExpression> expressions;
	
	/**
	 * Constructor which receives all expressions from one query.
	 * 
	 * @param expressions <code>ConditionalExpressions</code> from query
	 */
	public QueryFilter(List<ConditionalExpression> expressions) {
		this.expressions = expressions;
	}
	
	/**
	 * The given record is tested if it satisfies all conditional expressions from the query.
	 * Only if all expressions are satisfied will the record be satisfying.
	 * 
	 * @param record <code>StudentRecord</code> which is tested if it satisfies the conditions
	 * @return <code>true</code> if all expressions are satisfied, <code>false</code> otherwise
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		for (ConditionalExpression expr : expressions) {
			boolean recordSatisfies = expr.getComparisonOperator().satisfied(
					expr.getFieldGetter().get(record), 
					expr.getStringLiteral()
			);
			if (!recordSatisfies) {
				return false;
			}
		}
		return true;
	}

}
