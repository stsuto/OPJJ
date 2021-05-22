package hr.fer.zemris.math;

import hr.fer.zemris.java.util.Util;

/**
 * Class {@code ComplexRootedPolynomial} represents a rooted polynomial of Complex numbers.
 * The polynomial is unmodifiable, and all methods return new objects.
 * 
 * @author stipe
 *
 */
public class ComplexRootedPolynomial {
	/**
	 * Constant od the rooted polynomial which multiplies the roots.
	 */
	private Complex constant;
	/**
	 * Roots of the polynomial.
	 */
	private Complex[] roots;
	
	/**
	 * Constructor that creates a rooted polynomial from the given arguments.
	 * 
	 * @param constant {@link #constant}
	 * @param roots {@link #roots}
	 * @throws NullPointerException if any argument is null, or if any
	 * element of the root array is null
	 */
	public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
		this.constant = Util.requireNonNull(constant, "Complex constant");
		int len = Util.requireNonNull(roots, "Complex roots").length;
		this.roots = new Complex[len];
		for (int i = 0; i < len; i++) {
			this.roots[i] = Util.requireNonNull(roots[i], "Root of polynom");
		}
	}
	
	/**
	 * Computes polynomial value at given point z.
	 * 
	 * @param z the value to be applied
	 * @return polynomial value at point z
	 * @throws NullPointerException if the given argument is null
	 */
	public Complex apply(Complex z) {
		return toComplexPolynom().apply(Util.requireNonNull(z, "Applied number"));
	}
	
	/**
	 * Converts this rooted polynomial to a normal polynomial.
	 * 
	 * @return {@code ComplexPolynomial} equivalent of this rooted polynomial
	 */
	public ComplexPolynomial toComplexPolynom() {
        ComplexPolynomial pol = new ComplexPolynomial(constant);
        for (Complex complex : roots) {
        	// (Z - Zn) = - Zn + 1*Z;
        	ComplexPolynomial value = new ComplexPolynomial(complex.negate(), Complex.ONE);
        	pol = pol.multiply(value);
        }
        return pol;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(constant);
		for (Complex root : roots) {
			sb.append("*(z-").append(root).append(")");
		}
		return sb.toString();
	}
	
	/**
	 * Finds the index of the closest root for given complex number that is within
	 * the threshold. First root has index 0, second index 1, etc.
	 * If there is no such root, returns
	 * 
	 * @param z the number whose closes root is being computed
	 * @param treshold the distance to the roots value which is acceptable
	 * @return index of the closest root, or -1 if such root wasn't found
	 * @throws NullPointerException if the given argument is null
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		int index = -1;
		double minDifference = treshold;
		int len = roots.length;
		for (int i = 0; i < len; i++) {
			double difference = Util.requireNonNull(z, "Given number").sub(roots[i]).module();
			if (difference <= minDifference) {
				index = i;
				minDifference = difference;
			}
		}
		return index;
	}
}