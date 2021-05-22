package hr.fer.zemris.java.gui.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

/**
 * Class which represents an implementation of a calculator model which is responsible 
 * for all backgfound functionality of a calculator in {@link Calculator}.
 * 
 * @author stipe
 *
 */
public class CalcModelImpl implements CalcModel {

	/**
	 * Allowed double difference.
	 */
	private static final double DELTA = 1e-6;
	/**
	 * Flag which shows whether or not the calculator is editable.
	 */
	private boolean isEditable;
	/**
	 * Flag which shows whether or not the result is positive.
	 */
	private boolean isPositive;
	/**
	 * String value of what the calculator is currently showing.
	 */
	private String string;
	/**
	 * Current value saved within the calculator.
	 */
	private double value;
	/**
	 * Active operand.
	 */
	private Double activeOperand;
	/**
	 * Pending operation.
	 */
	private DoubleBinaryOperator pendingOperation;
	/**
	 * Listeners connected to this calculator model.
	 */
	private List<CalcValueListener> listeners;
	
	/**
	 * Constructor.
	 */
	public CalcModelImpl() {
		this.string = "";
		this.isEditable = true;
		this.isPositive = true;
		this.value = 0d;
		this.listeners = new ArrayList<>();
	}
	
	@Override
	public void addCalcValueListener(CalcValueListener l) {
		listeners.add(Objects.requireNonNull(l, "Listener mustn't be null!"));
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		listeners.remove(Objects.requireNonNull(l, "Listener mustn't be null!"));
	}

	@Override
	public double getValue() {
		return value;
	}

	@Override
	public void setValue(double value) {
		this.value = value;
		string = Double.toString(value);
		isPositive = value >= 0;
		isEditable = false;
		notifyListeners();
	}

	private void notifyListeners() {
		listeners.forEach(l -> l.valueChanged(this));
	}

	@Override
	public boolean isEditable() {
		return isEditable;
	}

	@Override
	public void clear() {
		value = 0d;
		isEditable = true;
		isPositive = true;
		notifyListeners();
		string = "";
	}

	@Override
	public void clearAll() {
		clear();
		activeOperand = null;
		pendingOperation = null;
	}

	@Override
	public void swapSign() throws CalculatorInputException {
		if (!isEditable) {
			throw new CalculatorInputException("Calculator isn't editable!");
		}
		isPositive = !isPositive;
		value *= -1;
		string = Double.toString(value);
		notifyListeners();
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		checkDecPointValidity();
		string += ".";
	}

	/**
	 * Checks if the decimal point is valid.
	 */
	private void checkDecPointValidity() {
		if (string.equals("")) {
			throw new CalculatorInputException("Decimal point must follow a number!");
		}
		if (string.contains(".")) {
			throw new CalculatorInputException("Number can't contain more than one decimal point!");	
		}
		if (!isEditable) {
			throw new CalculatorInputException("Calculator isn't editable!");
		}
	}

	@Override
	public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
		checkDigitInsertValidity(digit);
		if (!string.equals("0")) {
			string += digit;			
		} else {
			string = String.valueOf(digit);
		}
		
		value = Double.valueOf(string);			
		notifyListeners();
	}

	/**
	 * Checks if a digit can be inserted.
	 * 
	 * @param digit digit to be inserted
	 */
	private void checkDigitInsertValidity(int digit) {
		double newValue = Double.valueOf(string + digit);
		if (Double.isInfinite(newValue)) {
			throw new CalculatorInputException("Unable to add digit as the number would be too big!");
		}
		if (!isEditable) {
			throw new CalculatorInputException("Calculator isn't editable!");
		}
		if (digit < 0 || digit > 9) {
			throw new IllegalArgumentException("Invalid digit!");
		}
	}

	@Override
	public boolean isActiveOperandSet() {
		return activeOperand != null;
	}

	@Override
	public double getActiveOperand() throws IllegalStateException {
		if (activeOperand == null) {
			throw new IllegalStateException("Active operand is not set!");
		}
		return activeOperand;
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = activeOperand;
		clear();
	}

	@Override
	public void clearActiveOperand() {
		activeOperand = null;
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return pendingOperation;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		pendingOperation = op;
	}

	@Override
	public String toString() {
		if (string.endsWith(".")) {
			string = string.substring(0, string.length() - 2);
		} else if (string.equals("")) {
			string = "0";
		} else if (string.equals("-")) {
			string = "-0";
		}
		
		if (string.contains(".") && (value % 1 < DELTA)) {
			string = string.substring(0, string.indexOf("."));
		}
		
		return string;
	}
	
}
