package hr.fer.zemris.math;

import java.util.Arrays;

import hr.fer.zemris.java.util.Util;

/**
 * Class {@code ComplexPolynomial} represents a polynomial of Complex numbers.
 * The polynomial is unmodifiable, and all methods return new objects.
 * 
 * @author stipe
 *
 */
public class ComplexPolynomial {
	
	/**
	 * Complex numbers of this polynomial.
	 */
	private Complex[] factors;
	
	/**
	 * Constructor that creates a complex polynomial from the given array
	 * of complex numbers. The numbers in the array represent coefficients
	 * of the polynomial, with the index within the array representing the
	 * exponent of the polynomial the coefficient is multypling.
	 * 
	 * @param factors complex number or an array of complex numbers
	 * @throws NullPointerException if the array is null or if any of
	 * the array's elements are null
	 */
	public ComplexPolynomial(Complex ...factors) {
		int len = Util.requireNonNull(factors, "Factors").length;
		this.factors = new Complex[len];
		for (int i = 0; i < len; i++) {
			this.factors[i] = Util.requireNonNull(factors[i], "Number in polynom");
		}
//		this.factors = Arrays.copyOf(factors, factors.length);
	}
	
	/**
	 * Returns the order of the polynomial.
	 * The order is equal to the greatest exponent in the polynomial.
	 * 
	 * @return polynomial's order
	 */
	public short order() {
		// If an exponent with a given coefficient of 0 is valid anyway, use this.
		return (short) (factors.length - 1);
		
		// If numbers should be checked if they are zero to get order, use this.
//		for (int i = factors.length - 1; i >= 0; i--) {
//			if (factors[i] != Complex.ZERO) {
//				return (short) i;
//			}
//		}
//		return 0;
	}

	/**
	 * Computes a new polynomial by multiplying this polynomial with the give one.
	 * Both polynomials are left unchanged.
	 * 
	 * @param p polynomial to be multiplied with this one
	 * @return new instance of polynomial gotten as a result of multiplication
	 * @throws NullPointerException if the given argument is null
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		int combinedLength = factors.length + Util.requireNonNull(p, "Polynomial").factors.length - 1;
        Complex[] result = new Complex[combinedLength];
        Arrays.fill(result, Complex.ZERO);

        int len = factors.length;
        int otherLen = p.factors.length;
        for (int i = 0; i < len; i++)
            for (int j = 0; j < otherLen; j++) {
                result[i + j] = result[i + j].add(factors[i].multiply(p.factors[j]));
            }
        return new ComplexPolynomial(result);
	}
	
	/**
	 * Computes the first derivative of this polynomial.
	 * This polynomial is left unchanged.
	 * 
	 * @return new polynomial equal to the derivative of this polynomial
	 */
	public ComplexPolynomial derive() {
		int len = factors.length;
		Complex[] resFactors = new Complex[len - 1];
		for (int i = 0; i < len - 1; i++) {
			resFactors[i] = factors[i + 1].multiply(new Complex(i + 1, 0));
		} 
		return new ComplexPolynomial(resFactors);
	}
	
	/**
	 * Computes polynomial value at given point z.
	 * 
	 * @param z the point of computing
	 * @return complex number gotten as a result of applying the given number
	 * @throws NullPointerException if the given argument is null
	 */
	public Complex apply(Complex z) {
		Util.requireNonNull(z, "Number");
		Complex result = new Complex();
		int len = factors.length;
		for (int i = 0; i < len; i++) {
			result = result.add(factors[i].multiply(z.power(i)));
		}
		return result;
	}
	
	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder();
        int len = factors.length;
        for (int i = len - 1; i > 0; i--) {
            sb.append(factors[i].toString());
            sb.append("z^");
            sb.append(Integer.toString(i));
            sb.append("+");
        }
        sb.append(factors[0]);
        return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(factors);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ComplexPolynomial))
			return false;
		ComplexPolynomial other = (ComplexPolynomial) obj;
		return Arrays.equals(factors, other.factors);
	}

}