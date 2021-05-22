package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Class <code>ConditionalExpression</code> represents a complete conditional expression 
 * containing a field value, a string literal, and a comparison operator.
 * 
 * @author stipe
 *
 */
public class ConditionalExpression {

	/**
	 * The strategy of obtaining a field value.
	 */
	private IFieldValueGetter fieldGetter;
	/**
	 * The string with which the field is compared.
	 */
	private String stringLiteral;
	/**
	 * The strategy of comparing two <code>Strings</code>.
	 */
	private IComparisonOperator comparisonOperator;
	
	/**
	 * Constructor which creates a conditional expression with the given parameters.
	 * 
	 * @param fieldGetter {@link #fieldGetter} of the new expression
	 * @param stringLiteral {@link #stringLiteral} of the new expression
	 * @param comparisonOperator {@link #comparisonOperator} of the new expression
	 */
	public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral, IComparisonOperator comparisonOperator) {
		this.fieldGetter = fieldGetter;
		this.stringLiteral = stringLiteral;
		this.comparisonOperator = comparisonOperator;
	}

	/**
	 * Getter for this <code>ConditionalExpression's</code> field value.
	 * 
	 * @return {@link #fieldGetter}
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * Getter for this <code>ConditionalExpression's</code> string literal.
	 * 
	 * @return {@link #stringLiteral}
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * Getter for this <code>ConditionalExpression's</code> comparison operator.
	 * 
	 * @return {@link #comparisonOperator}
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(comparisonOperator, fieldGetter, stringLiteral);
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
		if (!(obj instanceof ConditionalExpression))
			return false;
		ConditionalExpression other = (ConditionalExpression) obj;
		return Objects.equals(comparisonOperator, other.comparisonOperator)
				&& Objects.equals(fieldGetter, other.fieldGetter) && Objects.equals(stringLiteral, other.stringLiteral);
	}
	
	
}
