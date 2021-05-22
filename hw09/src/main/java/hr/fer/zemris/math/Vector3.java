package hr.fer.zemris.math;

import hr.fer.zemris.java.util.Util;

/**
 * Class {@code Vector3} represents an unmodifiable 3D vector.
 * 
 * @author stipe
 *
 */
public class Vector3 {

	/**
	 * Vector's x component.
	 */
	private double x;
	/**
	 * Vector's y component.
	 */
	private double y;
	/**
	 * Vector's z component.
	 */
	private double z;
	
	/**
	 * Constructor that creates a vector with the given coordinates.
	 * 
	 * @param x {@link #x}
	 * @param y {@link #y}
	 * @param z {@link #z}
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	} 
	
	/**
	 * Computes the norm of this vector.
	 * 
	 * @return this vector's norm
	 */
	public double norm() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	/**
	 * Computes the normalized version of this vector,
	 * leaving this vector unchanged.
	 * 
	 * @return normalized version of this vector
	 */
	public Vector3 normalized() {
		double length = norm();
		return new Vector3(x / length, y / length, z / length);
	}
	
	/**
	 * Adds the given vector to this one, leaving both unchanged.
	 * 
	 * @param other the other vector
	 * @return new vector gotten from addition
	 */
	public Vector3 add(Vector3 other) {
		Util.requireNonNull(other, "Vector");
		return new Vector3(x + other.x, y + other.y, z + other.z);
	}
	
	/**
	 * Subtracts the given vector from this one, leaving both unchanged.
	 * 
	 * @param other the other vector
	 * @return new vector gotten from subtraction
	 */
	public Vector3 sub(Vector3 other) {
		Util.requireNonNull(other, "Vector");
		return new Vector3(x - other.x, y - other.y, z - other.z);
	}
	
	/**
	 * Returns the scalar or 'dot' product of this and the given vector,
	 * leaving both unchanged.
	 * 
	 * @param other the other vector
	 * @return value of the scalar product
	 */
	public double dot(Vector3 other) {
		Util.requireNonNull(other, "Vector");
		return x * other.x + y * other.y + z * other.z;
	}
	
	/**
	 * Returns the vector or 'cross' product of this and the given vector,
	 * leaving both unchanged.
	 * 
	 * @param other the other vector
	 * @return value of the vector product
	 */
	public Vector3 cross(Vector3 other) {
		Util.requireNonNull(other, "Vector");
		double newX = y * other.z - z * other.y;
		double newY = z * other.x - x * other.z;
		double newZ = x * other.y - y * other.x;
		return new Vector3(newX, newY, newZ);
	}
	
	/**
	 * Computes a new vector by scaling this vector with the given value,
	 * without changing the vector.
	 * 
	 * @param s the scalar value
	 * @return new vector with scaled values
	 */
	public Vector3 scale(double s) {
		return new Vector3(s * x, s * y, s * z);
	}
	
	/**
	 * Computes the cosine of the angle between this and the given vector.
	 * 
	 * @param other the other vector
	 * @return the cosine of the two vectors
	 */
	public double cosAngle(Vector3 other) {
		Util.requireNonNull(other, "Vector");
		return dot(other) / (norm() * (other.norm()));
	}
	
	/**
	 * Getter for this vector's x component. 
	 * 
	 * @return {@link #x}
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Getter for this vector's y component. 
	 * 
	 * @return {@link #y}
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Getter for this vector's z component. 
	 * 
	 * @return {@link #z}
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Creates an array with the values of this vector's components.
	 * 
	 * @return array of this vector's components
	 */
	public double[] toArray() {
		return new double[] {x, y, z};
	}
	
	@Override
	public String toString() {
		return String.format("(%.6f, %.6f, %.6f)", x, y, z);
	}
	
	
}
