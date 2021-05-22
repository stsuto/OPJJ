package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * This is a demonstrational class for the class <code>ObjectStack</code>.
 * <code>StackDemo</code> expects a single argument from the command line.
 * The argument should be started and ended with quotation marks and should be a valid post-fix calculation expression.
 * @author stipe
 */
public class StackDemo {

	/**
	 * Demonstration of post-fix calculations using the <code>ObjectStack</code> implementation of stack.
	 * @param args expression of the calculation
	 * @throws EmptyStackException if a value from stack is wanted, but the stack is empty
	 */
	public static void main(String[] args) {
		
		String expression = args[0];
		String[] parts = expression.split("\\s+");
		
		ObjectStack stack = new ObjectStack();
		try {
			for (int i = 0; i < parts.length; i++) {
				try {
					int number = Integer.parseInt(parts[i]);
					stack.push(number);		
					
				} catch (NumberFormatException ex) {
					if (stack.size() < 2) {
						throw new EmptyStackException("Wrong input!");
					}
					String operator = parts[i];
					int secondNumber = (int) stack.pop();
					int firstNumber = (int) stack.pop();
					
					int result = performOperation(firstNumber, secondNumber, operator);
					stack.push(result);
				}
			}
		
			if (stack.size() != 1) {
				System.out.println("Error in computation: missing result!");
			} else {
				System.out.println(stack.pop());
			}		
			
		} catch (EmptyStackException | IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * Performs the operation on two numbers. Allowed operators are +, -, /, *, %.
	 * @param firstNumber the first operand
	 * @param secondNumber the second operand
	 * @param operator indicator of which operation to perform
	 * @return the result of the operation
	 */
	private static int performOperation(int firstNumber, int secondNumber, String operator) {
		int result = 0;
		
		if (operator.equals("+")) {
			result = firstNumber + secondNumber;
			
		} else if (operator.equals("-")) {
			result = firstNumber - secondNumber;
			
		} else if (operator.equals("/")) {
			checkIfDividingByZero(secondNumber);
			result = firstNumber / secondNumber;
			
		} else if (operator.equals("*")) {
			result = firstNumber * secondNumber;
			
		} else if (operator.equals("%")) {
			checkIfDividingByZero(secondNumber);
			result = firstNumber % secondNumber;
			
		} else {
			throw new IllegalArgumentException("Operator " + operator + " is not supported!\n");
		}
		
		return result;
	}

	/**
	 * Checks if the number to be divided with is 0 and throws an exception if it is.
	 * @param secondNumber the number that divides the first number
	 * @throws IllegalArgumentException if the number is 0
	 */
	private static void checkIfDividingByZero(int secondNumber) {
		if (secondNumber == 0) {
			throw new IllegalArgumentException("Cannot divide by 0!");
		}
	}
	
}
