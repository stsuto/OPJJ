package hr.fer.zemris.java.hw05.db;

/**
 * Class <code>ComparisonOperators</code> contains public static final variables 
 * representing operators of type <code>IComparisonOperator</code> used to compare 
 * <code>String</code> objects.
 * 
 * @author stipe
 *
 */
public class ComparisonOperators {

	/**
	 * Checks if the first objects' value is less than the second one's.
	 */
	public static final IComparisonOperator LESS = (f, s) -> f.compareTo(s) < 0;
	/**
	 * Checks if the first objects' value is less or equal than the second one's.
	 */
	public static final IComparisonOperator LESS_OR_EQUALS = (f, s) -> f.compareTo(s) <= 0;
	/**
	 * Checks if the first objects' value is greater than the second one's.
	 */
	public static final IComparisonOperator GREATER = (f, s) -> f.compareTo(s) > 0;
	/**
	 * Checks if the first objects' value is greater or equals than the second one's.
	 */
	public static final IComparisonOperator GREATER_OR_EQUALS = (f, s) -> f.compareTo(s) >= 0;
	/**
	 * Checks if the first objects' value is equal to the second one's.
	 */
	public static final IComparisonOperator EQUALS = (f, s) -> f.compareTo(s) == 0;
	/**
	 * Checks if the first objects' value is not equal to the second one's.
	 */
	public static final IComparisonOperator NOT_EQUALS = (f, s) -> f.compareTo(s) != 0;
	/**
	 * Checks if the first <code>String</code> is equal to the second one, but the second one 
	 * can contain a wildcard character '*', representing zero or more of any characters.
	 * String can contain one wildard character at most.
	 */
	public static final IComparisonOperator LIKE = 
			(f, s) -> {
				if (!s.contains("*")) {
					return f.equals(s);
				} else {
					if (s.indexOf("*") != s.lastIndexOf("*")) {
						throw new IllegalArgumentException("String literal of LIKE operator can "
								+ "contain at most one wildcard character '*'");
					}
					if (s.charAt(0) == '*') {
						return f.endsWith(s.substring(1)); // or s.replace("*", ""), same as in the next case
					}
					else if (s.charAt(s.length() - 1) == '*') {
						return f.startsWith(s.replace("*", ""));
					}
					String[] parts = s.split("\\*");
					String first = parts[0];
					String second = parts[1];
					
					return (f.startsWith(first) && f.substring(first.length()).endsWith(second));
				}
			};
	
}
