package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.util.Util;

/**
 * Class {@code Complex} represents an unmodifiable complex number
 * and implements a support for working with complex numbers.
 * 
 * @author stipe
 */
public class Complex {

	/**
	 * Real part of the complex number.
	 */
	private double re;
	/**
	 * Imaginary part of the complex number.
	 */
	private double im;

	/**
	 * Delta used for comparing double values.
	 */
	private static final double DELTA = 1e-6;
	/**
	 * Smallest value differential possible in calculation and output.
	 */
	private static final double SMALLEST_DIFF = 0.05;

	/**
	 * Static factory method for creating a (0,0) Complex number.
	 * Equivalent to {@code new Complex(0,0)}.
	 */
	public static final Complex ZERO = new Complex(0, 0);
	/**
	 * Static factory method for creating a (1,0) Complex number.
	 * Equivalent to {@code new Complex(1,0)}.
	 */
	public static final Complex ONE = new Complex(1, 0);
	/**
	 * Static factory method for creating a (-1,0) Complex number.
	 * Equivalent to {@code new Complex(-1,0)}.
	 */
	public static final Complex ONE_NEG = new Complex(-1, 0);
	/**
	 * Static factory method for creating a (0,1) Complex number.
	 * Equivalent to {@code new Complex(0,1)}.
	 */
	public static final Complex IM = new Complex(0, 1);
	/**
	 * Static factory method for creating a (0,-1) Complex number.
	 * Equivalent to {@code new Complex(0,-1)}.
	 */
	public static final Complex IM_NEG = new Complex(0, -1);

	/**
	 * Default constructor which creates a (0,0) Complex number.
	 * Equivalent to {@code new Complex(0,0)}.
	 */
	public Complex() {
		this.re = 0;
		this.im = 0;
	}

	/**
	 * Constructor for creating a complex number with given values.
	 * 
	 * @param re real part of the complex number
	 * @param im imaginary part of the complex number
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}

	/**
	 * Returns the module of this number's polar form.
	 * 
	 * @return this number's module
	 */
	public double module() {
		return Math.sqrt(re * re + im * im);
	}

	/**
	 * Multiplies this complex number with the other.
	 * 
	 * @param c other complex number
	 * @return new instance of {@code Complex} representing the result of
	 *         multiplication
	 * @throws NullPointerException if the given number is null
	 */
	public Complex multiply(Complex c) {
		Util.requireNonNull(c, "Complex number");
		return new Complex(re * c.re - im * c.im, re * c.im + im * c.re);
	}

	/**
	 * Divides this complex number with the other.
	 * 
	 * @param c other complex number
	 * @return new instance of {@code Complex} representing the result of division
	 * @throws NullPointerException if the given number is null
	 */
	public Complex divide(Complex c) {
		Util.requireNonNull(c, "Complex number");

		Complex conjugateC = new Complex(c.re, -1 * c.im);
		// To divide two complex numbers, we need the conjugate of the divisor.
		double denominator = c.multiply(conjugateC).re; // The denominator will only have the real value.

		// The numerator is calculated by multiplying the dividend with the conjugate of
		// the divisor. It contains real and imaginary values.
		Complex numerator = multiply(conjugateC);

		// The final values of the division are calculated by dividing both real and
		// imaginary parts with the denominator.
		return new Complex(numerator.re / denominator, numerator.im / denominator);
	}

	/**
	 * Adds two complex numbers, leaving both unchanged.
	 * 
	 * @param c other number
	 * @return new instance of {@code Complex} gotten from adding two numbers
	 * @throws NullPointerException if the given number is null
	 */
	public Complex add(Complex c) {
		Util.requireNonNull(c, "Complex number");
		return new Complex(re + c.re, im + c.im);
	}

	/**
	 * Subtracts two complex numbers, leaving both unchanged.
	 * 
	 * @param c the number which is subtracted from this number
	 * @return new instance of {@code Complex} gotten from subtracting two numbers
	 * @throws NullPointerException if the given number is null
	 */
	public Complex sub(Complex c) {
		Util.requireNonNull(c, "Complex number");
		return new Complex(re - c.re, im - c.im);
	}

	// returns -this
	public Complex negate() {
		return new Complex(-re, -im);
	}

	/**
	 * Raises this complex number to the power of the argument.
	 * 
	 * @param n int value of the exponent
	 * @return new instance of {@code Complex} representing the result of the power
	 *         operation
	 */
	public Complex power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("The power must be non-negative!");
		}
		double newMagnitude = Math.pow(module(), n);
		double newAngle = n * getAngle();

		return fromMagnitudeAndAngle(newMagnitude, newAngle);
	}

	/**
	 * Calculates the n-th root of this complex number.
	 * 
	 * @param n int value of the root level
	 * @return new instance of {@code Complex} representing the result of the root
	 *         operation
	 */
	public List<Complex> root(int n) {
		if (n < 1) {
			throw new IllegalArgumentException("The root must be greater than 0!");
		}
		double newMagnitude = Math.pow(module(), 1d / n);
		double angle = getAngle();

		List<Complex> results = new ArrayList<>();

		for (int k = 0; k < n; k++) {
			double newAngle = (angle + 2 * k * Math.PI) / n;
			results.add(fromMagnitudeAndAngle(newMagnitude, newAngle));
		}

		return results;
	}

	/**
	 * Static factory method that creates a complex number based on the String
	 * argument of the method. The given string is parsed and parameters of complex
	 * numbers are retrieved from it.
	 * 
	 * @param s the String to be parsed and which should contain a proper complex
	 *          number syntax
	 * @return new instance of {@code Complex} with above mentioned parameters
	 * @throws NullPointerException if the given string is null
	 * @throws NumberFormatException if the number cannot be parsed
	 */
	public static Complex parse(String s) {
		s = Util.requireNonNull(s, "String of complex number").replace(" ", "");
		double[] values = getValues(s);

		return new Complex(values[0], values[1]);
	}

	/**
	 * Gets the real and imaginary values contained in string.
	 * 
	 * @param s string to be parsed
	 * @return array of 2 elements, the real and imaginary value of the complex number
	 * @throws NumberFormatException if the number can't be parsed
	 */
	private static double[] getValues(String s) {
		checkValidity(s);
		double real = 0, imaginary = 0;
		int imIndex = s.indexOf("i");
		
		if (imIndex + 1 == s.length()) {
			s = s.replace("i", "1");
		}
		
		if (imIndex == -1) {
			real = Double.parseDouble(s);

		} else if (imIndex > 1) {
			real = Double.parseDouble(s.substring(0, imIndex - 1));
			imaginary = Double.parseDouble(s.substring(imIndex - 1).replace("i", "").replace("+", ""));

		} else {
			imaginary = Double.parseDouble(s.replace("i", ""));
		}
		
		return new double[] {real, imaginary};
	}

	/**
	 * Checks the validity of string representation of complex number.
	 * 
	 * @param s string to be parsed for complex number
	 * @throws NumberFormatException if the number can't be parsed
	 */
	private static void checkValidity(String s) {
		if (s.length() == 0) {
			throw new NumberFormatException("Number length must be a positive number!");
		}
		
		if (s.startsWith("+")) {
			throw new NumberFormatException("Number can't start with '+'!");
		} 

		if (s.contains("++") || s.contains("--") || s.contains("+-") || s.contains("-+")) { // Illegal arguments.
			throw new NumberFormatException("Too many operands!");
		}

		int imIndex = s.indexOf("i");
		if (imIndex != s.lastIndexOf("i")) {
			throw new NumberFormatException("Number can't contain more than one i!");
		} 
		
		if (imIndex != 0 && imIndex != -1  && s.charAt(imIndex - 1) != '+' && s.charAt(imIndex - 1) != '-') {
			throw new NumberFormatException("Real and imaginary numbers must be split with '+' or '-'!");
		}
	}

	/**
	 * Changes the way {@code Complex} objects are printed out.
	 * 
	 * @return the String representation of this number
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(isPositive(re) ? "(" : "(-");
		sb.append(String.format("%.1f", Math.abs(re)));
		sb.append(isPositive(im) ? "+" : "-");
		sb.append(String.format("i%.1f)", Math.abs(im)));
		return sb.toString();
	}

	/**
	 * Checks if the number should be printed as positive.
	 * If the number is negative, but with absolute value less than
	 * {@link #SMALLEST_DIFF}, it will be printed as positive 0.0.
	 * 
	 * @param num number to be checked
	 * @return {@code true} if it should be printed as positive, 
	 * {@code false} otherwise
	 */
	private boolean isPositive(double num) {
		return num > -SMALLEST_DIFF;
	}

	/**
	 * Returns the angle of this number's polar form in radians.
	 * 
	 * @return double value of this number's angle
	 */
	private double getAngle() {
		double angle = Math.atan(im / re);
		if (re > 0 && im < 0) {
			angle += 2 * Math.PI;
		} else if ((re < 0 && im > 0) || (re < 0 && im < 0)) {
			angle += Math.PI;
		}
		return angle;
	}

	/**
	 * Creates a complex number from parameters representing magnitude and angle.
	 * Serves as a transformation from polar to rectangular form of complex numbers.
	 * <p>
	 * The real and imaginary parts of the number are calculated with the formula z
	 * = r(cos(t)+isin(t)).
	 * 
	 * @param magnitude double value of magnitude of the polar form
	 * @param angle     double value of the angle of the polar form in radians
	 * @return new instance of {@code ComplexNumber} with above mentioned parameters
	 */
	private Complex fromMagnitudeAndAngle(double magnitude, double angle) {
		return new Complex(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}

	@Override
	public int hashCode() {
		return Objects.hash(im, re);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Complex))
			return false;
		Complex other = (Complex) obj;
		return Math.abs(re - other.re) < DELTA && Math.abs(im - other.im) < DELTA;
	}

}