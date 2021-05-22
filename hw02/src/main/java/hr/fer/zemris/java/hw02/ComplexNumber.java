package hr.fer.zemris.java.hw02;

import java.util.Objects;

/**
 * Class <code>ComplexNumber</code> represents an unmodifiable complex number
 * and implements a support for working with complex numbers.
 * 
 * @author stipe
 */
public class ComplexNumber {

	/**
	 * real part of the complex number
	 */
	private double real;
	/**
	 * imaginary part of the complex number
	 */
	private double imaginary;
	
	/**
	 * Constructor that accepts arguments for real and imaginary parts of the numeber
	 * and creates a new complex number from those parameters.
	 * @param real double value of the real part of the complex number
	 * @param imaginary double value of the imaginary part of the complex number
	 */
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	/**
	 * Static factory method that creates a complex number from the given parameter
	 * for the real part, and default 0 for the imaginary part of the number.
	 * @param real double value of the real part of the complex number
	 * @return new instance of <code>ComplexNumber</code> with above mentioned parameters
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0);
	}
	
	/**
	 * Static factory method that creates a complex number from the given parameter
	 * for the imaginary part, and default 0 for the real part of the number.
	 * @param imaginary double value of the imaginary part of the complex number
	 * @return new instance of <code>ComplexNumber</code> with above mentioned parameters
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0, imaginary);
	}
	
	/**
	 * Static factory method that creates a complex number from parameters representing magnitude and angle.
	 * Serves as a transformation from polar to rectangular form of complex numbers.
	 * The real and imaginary parts of the number are calculated with the formula z = r(cos(t)+isin(t)).
	 * @param magnitude double value of magnitude of the polar form
	 * @param angle double value of the angle of the polar form in radians
	 * @return new instance of <code>ComplexNumber</code> with above mentioned parameters
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		return new ComplexNumber(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}
	
	/**
	 * Static factory method that creates a complex number based on the String argument of the method.
	 * The given string is parsed and parameters of complex numbers are retrieved from it.
	 * @param s the String to be parsed and which should contain a proper complex number systax
	 * @return new instance of <code>ComplexNumber</code> with above mentioned parameters
	 */
	public static ComplexNumber parse(String s) {
		double real = 0;
		double imaginary = 0;
		
		if (s.contains("++") || s.contains("--") || s.contains("+-") || s.contains("-+")) { // Illegal arguments.
			throw new NumberFormatException("Incorrect input!");
			
		} else if (s.charAt(0) == '+') { // If the string starts with "+", it should be removed so the number can be parsed.
			s = s.substring(1);
		}
		
		if (!s.contains("i")) {	// If there is no imaginary part, just parse the string for the real part.
			real = Double.parseDouble(s);
			
		// All strings that go through next conditions MUST have an imaginary part.
		} else if (s.contains("+") && s.lastIndexOf("+") != 0) { // If "+" is present, the string contains both real and imaginary parts divided by the "+" character.
			String[] parts = s.split("\\+"); 
			real = Double.parseDouble(parts[0]); // The first part (before the "+") is real.
			if (parts[1].length() == 1) {
				parts[1] = "1i";
			}
			imaginary = Double.parseDouble(parts[1].substring(0, parts[1].length() - 1)); // The second (after the "+") is imaginary.
		
		} else if (s.contains("-") && s.lastIndexOf("-") != 0) {
			String realString = s.substring(0, s.lastIndexOf("-")); // The last (that is, the second) "-" is dividing the real and the imaginary parts.
			String imaginaryString = s.substring(s.lastIndexOf("-"), s.length() - 1);
			if (imaginaryString.length() == 1) {
				imaginaryString = "-1";
			}
			real = Double.parseDouble(realString);
			imaginary = Double.parseDouble(imaginaryString);
			
		} else { 															// If there are no "+" and "-", or they are only there as the first character, 
			imaginary = Double.parseDouble(s.substring(0, s.length() - 1));	// only one part of the complex number is present - the imaginary one!
		}		
		
		return new ComplexNumber(real, imaginary);
	}
	
	/**
	 * Returns the real part of this complex number.
	 * @return double value of this number's real part
	 */
	public double getReal() {
		return real;
	}
	
	/**
	 * Returns the imaginary part of this complex number.
	 * @return double value of this number's imaginary part
	 */
	public double getImaginary() {
		return imaginary;
	}
	
	/**
	 * Returns the magnitude of this number's polar form.
	 * @return double value of this number's magnitude
	 */
	public double getMagnitude() {
		return Math.sqrt(real * real + imaginary * imaginary);
	}
	
	/**
	 * Returns the angle of this number's polar form in radians.
	 * @return double value of this number's angle
	 */
	public double getAngle() {
		double angle = Math.atan(imaginary / real);
		if (real > 0 && imaginary < 0) {
			angle += 2 * Math.PI;
		} else if ((real < 0 && imaginary > 0) || (real < 0 && imaginary < 0)) {
			angle += Math.PI;
		}
		return angle;
	}
	
	/**
	 * Adds this complex number to the other.
	 * @param c other complex number
	 * @return new instance of <code>ComplexNumber</code> representing the result of additon
	 */
	public ComplexNumber add(ComplexNumber c) {
		return new ComplexNumber(real + c.getReal(), imaginary + c.getImaginary());
	}
	
	/**
	 * Subtracts the other complex number from this one.
	 * @param c other complex number
	 * @return new instance of <code>ComplexNumber</code> representing the result of subtraction
	 */
	public ComplexNumber sub(ComplexNumber c) {
		return new ComplexNumber(real - c.getReal(), imaginary - c.getImaginary());
	}
	
	/**
	 * Multiplies this complex number with the other.
	 * @param c other complex number
	 * @return new instance of <code>ComplexNumber</code> representing the result of multiplication
	 */
	public ComplexNumber mul(ComplexNumber c) {
		double realPart = real * c.getReal() - imaginary * c.getImaginary();
		double imaginaryPart = real * c.getImaginary() + imaginary * c.getReal();
		
		return new ComplexNumber(realPart, imaginaryPart);
	}
			
	/**
	 * Divides this complex number with the other.
	 * @param c other complex number
	 * @return new instance of <code>ComplexNumber</code> representing the result of division
	 */
	public ComplexNumber div(ComplexNumber c) {
		ComplexNumber conjugateC = new ComplexNumber(c.getReal(), -1 * c.getImaginary()); // To divide two complex numbers, we need the conjugate of the divisor.
		double denominator = c.mul(conjugateC).getReal(); // The denominator will only have the real value.
		
		// The numerator is calculated by multiplying the dividend with the conjugate of the divisor. It contains real and imaginary values.
		ComplexNumber numerator = this.mul(conjugateC);
		double realPart = numerator.getReal() / denominator; // The final values of the division are calculated by dividing both real and imaginary parts with the denominator.
		double imaginaryPart = numerator.getImaginary() / denominator;
		
		return new ComplexNumber(realPart, imaginaryPart);
	}
	
	/**
	 * Raises this complex number to the power of the argument.
	 * @param n int value of the exponent
	 * @return new instance of <code>ComplexNumber</code> representing the result of the power operation
	 */
	public ComplexNumber power(int n) { // Polar form is more suitable for calculating power.
		double newMagnitude = Math.pow(getMagnitude(), n);
		double newAngle = n * getAngle();
		
		return fromMagnitudeAndAngle(newMagnitude, newAngle);
	}
	
	/**
	 * Calculates the n-th root of this complex number.
	 * @param n int value of the root level
	 * @return new instance of <code>ComplexNumber</code> representing the result of the root operation
	 */
	public ComplexNumber[] root(int n){
		if (n < 1) {
			throw new IllegalArgumentException("The root must be greater than 1!");
		}
		double newMagnitude = Math.pow(getMagnitude(), 1d / n);
		double angle = getAngle();
		
		ComplexNumber[] results = new ComplexNumber[n];
		
		for (int k = 0; k < n; k++) {
			double newAngle = (angle + 2 * k * Math.PI) / n;
			results[k] = fromMagnitudeAndAngle(newMagnitude, newAngle);
		}
		
		return results;
	}
	
	/**
	 * Changes the way <code>ComplexNumber</code> objects are printed out.
	 * @return the String representation of complex numbers
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (Math.abs(real - 0d) > 1e-4) {
			sb.append(String.format("%.2f", real));
			if (imaginary > 0) {
				sb.append("+");
			}
		}
		if (Math.abs(imaginary - 0d) > 1e-4) {
			sb.append(String.format("%.2fi", imaginary));			
		}
		return sb.toString();
	}
	
	/**
	 * Checks if this complex number is equal to the object given as the argument.
	 * @param obj the object which is being tested with this number
	 * @return <code>true</code> if both real and imaginary parts of the numbers are equal,
	 * 		   and <code>false</code> otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(Objects.requireNonNull(obj) instanceof ComplexNumber)) {
			return false;
		}
		ComplexNumber other = (ComplexNumber) obj;
		return (Math.abs(real - other.getReal()) < 1e-4)
				&& (Math.abs(imaginary - other.getImaginary()) < 1e-4);
	}

	@Override
	public int hashCode() {
		return Objects.hash(imaginary, real);
	}
}
