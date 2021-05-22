package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BiFunction;

/**
 * Class {@code ValueWrapper} represents a wrapper of any Object value.
 * If the value is of a type that allows it, {@code ValueWrapper} can be
 * used to perform some arithmetic operations or comparisons.
 * 
 * @author stipe
 *
 */
public class ValueWrapper {

	/**
	 * The wrapped value.
	 */
	private Object value;
	
	/**
	 * Constructor which accepts the value object.
	 * 
	 * @param value
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * Performs addition of this {@code ValueWrapper} value with the given value.
	 * The result of the operation is stored as new value of this {@code ValueWrapper},
	 * but the given parameter object is left unchanged.
	 * 
	 * @param incValue the other operand
	 * @throws RuntimeException if the value is illegal for operations
	 */
	public void add(Object incValue) {
		performOperation(incValue, (f,s) -> f + s);
	}

	/**
	 * Subtracts this {@code ValueWrapper} value by the given value.
	 * The result of the operation is stored as new value of this {@code ValueWrapper},
	 * but the given parameter object is left unchanged.
	 * 
	 * @param decValue the other operand
	 * @throws RuntimeException if the value is illegal for operations
	 */
	public void subtract(Object decValue) {
		performOperation(decValue, (f,s) -> f - s);
	}
	
	/**
	 * Performs multiplication of this {@code ValueWrapper} value with the given value.
	 * The result of the operation is stored as new value of this {@code ValueWrapper},
	 * but the given parameter object is left unchanged.
	 * 
	 * @param mulValue the other operand
	 * @throws RuntimeException if the value is illegal for operations
	 */
	public void multiply(Object mulValue) {
		performOperation(mulValue, (f,s) -> f * s);
	}
	
	/**
	 * Divides this {@code ValueWrapper} value by the given value.
	 * The result of the operation is stored as new value of this {@code ValueWrapper},
	 * but the given parameter object is left unchanged.
	 * 
	 * @param divValue the other operand
	 * @throws RuntimeException if the value is illegal for operations
	 */
	public void divide(Object divValue) {
		performOperation(divValue, (f,s) -> f / s);
	}
	
	/**
	 * Compares this {@code ValueWrapper} value to the given value.
	 * Null values are calculated to have a value of 0.
	 * 
	 * @param otherValue the value with which the wrapped value is compared to
	 * @return {@code -1} if the given value is greater, {@code 1} if the
	 * wrapped value is greater, and {@code 0} if they are equal
	 * @throws RuntimeException if the value is illegal for operations
	 */
	public int numCompare(Object otherValue) {
		return Double.compare(transformValue(value).value, transformValue(otherValue).value);
	}

	/**
	 * Performs the operation given as a function parameter upon the wrapped 
	 * value and the value given as parameter.
	 * 
	 * @param other the second operand
	 * @param function the operation to be performed
	 * @throws RuntimeException if the value is illegal for operations
	 */
	// Using Double type in function as it's easy to transform it into an Integer if necessary.
	private void performOperation(Object other, BiFunction<Double, Double, Double> function) {
		ValueAndType oldValue = transformValue(value);
		ValueAndType otherValue = transformValue(other);
		
		value = getResult(oldValue, otherValue, function.apply(oldValue.value, otherValue.value));
	}

	/**
	 * Checks if the type of the given value is legal for arithmetic operations.
	 * 
	 * @param value value to be checked
	 * @throws RuntimeException if the value is illegal for operations
	 */
	private void checkValueType(Object value) {
		if (!(value instanceof Integer || value instanceof Double
			  || value instanceof String || value == null)) {
			throw new RuntimeException("Illegal value type!");
		}
	}
	
	/**
	 * Transforms the given object to a value with which arithmetic operations
	 * can be performed.
	 * 
	 * @param object the object to be transformed
	 * @return {@code ValueAndType} object containing the transformed value
	 * and the value type
	 * @throws RuntimeException if the value is illegal for operations
	 */
	private ValueAndType transformValue(Object object) {
		checkValueType(object);
		
		if (object == null) {
			return new ValueAndType(0, Integer.class);
		
		} else if (object instanceof String) {
			// String must be specially processed.
			return transformString((String) object);
		
		} else if (object instanceof Integer) {
			// Integer is deboxed to int because Integer can't be converted to double, but int can.
			return new ValueAndType((int) object, Integer.class);
		}
		
		return new ValueAndType((double) object, Double.class);
	}
	
	/**
	 * Transforms the String to a suited value by parsing.
	 * 
	 * @param string string containing a value
	 * @return {@code ValueAndType} object containing the transformed value
	 * and the value type
	 * @throws RuntimeException if the value is illegal for operations
	 */
	private ValueAndType transformString(String string) {
		try {				
			if (string.contains(".") || string.toLowerCase().contains("e")) {
				return new ValueAndType(Double.parseDouble(string), Double.class);
			}
			return new ValueAndType(Integer.parseInt(string), Integer.class);
		
		} catch (NumberFormatException ex) {	
			throw new RuntimeException("Illegal number format in string value.");
		}
	}

	/**
	 * Returns the result of the operation as the correct type of value.
	 * The type is corrected using the types of the given operands.
	 * 
	 * @param oldValue first operand
	 * @param otherValue second operand
	 * @param result the result of the operation
	 * @return the result, but it's type is corrected if needed
	 */
	private Object getResult(ValueAndType oldValue, ValueAndType otherValue, double result) {
		if (oldValue.type.equals(Integer.class) && otherValue.type.equals(Integer.class)) {
			return (int) result;
		}
		return result;
	}
	
	/**
	 * Structure containing a value suited for performing operations and the
	 * type used for transforming the end result of the operation.
	 * 
	 * @author stipe
	 *
	 */
	private static class ValueAndType {
		double value;
		Class<? extends Object> type;
		
		public ValueAndType(double value, Class<? extends Object> type) {
			this.value = value;
			this.type = type;
		}
	}

	/**
	 * Getter for the wrapped value.
	 * 
	 * @return {@link #value}
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Setter for this wrapped value.
	 * 
	 * @param value {@link #value}
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
}