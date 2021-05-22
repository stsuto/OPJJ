package hr.fer.zemris.java.hw17.trazilica;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class {@code Vector} represents an n-dimensional vector.
 * 
 * @author stipe
 *
 */
public class Vector implements Iterable<Double> {

	/**
	 * This vector's coordinates.
	 */
	private List<Double> coords;
	
	/**
	 * Constructor that creates a vector with the given coordinates.
	 * 
	 * @param coords {@link #coords}
	 */
	public Vector(List<Double> coords) {
		this.coords = coords;
	} 
	
	/**
	 * Constructor that creates a vector with no values.
	 */
	public Vector() {
		this(new ArrayList<>());
	} 
	
	/**
	 * Adds another dimension to thsi vector and initializes it to the given value.
	 * 
	 * @param value coordinate value
	 * @return true
	 */
	public boolean addCoord(double value) {
		return coords.add(value);
	}
	
	/**
	 * Gets the coordinate value from the dimension on the given index.
	 * 
	 * @param index index
	 * @return coordinate value
	 */
	public double getCoord(int index) {
		return coords.get(index);
	}
	
	/**
	 * @return vector's dimensions
	 */
	public int size() {
		return coords.size();
	}
	
	@Override
	public Iterator<Double> iterator() {
		return coords.iterator();
	}
	
	/**
	 * Computes the norm of this vector.
	 * 
	 * @return this vector's norm
	 */
	public double norm() {
		double norm = 0d;
		int size = coords.size();
		for (int i = 0; i < size; i++) {
			double value = coords.get(i);
			norm += value * value;
		}
		return Math.sqrt(norm);
	}
	
	/**
	 * Computes the normalized version of this vector,
	 * leaving this vector unchanged.
	 * 
	 * @return normalized version of this vector
	 */
	public Vector normalized() {
		double length = norm();
		List<Double> normValues = coords.stream()
				.map(c -> c / length)
				.collect(Collectors.toList());
		return new Vector(normValues);
	}
	
	/**
	 * Returns the scalar or 'dot' product of this and the given vector,
	 * leaving both unchanged.
	 * 
	 * @param other the other vector
	 * @return value of the scalar product
	 */
	public double dot(Vector other) {
		validateVector(other);
		double result = 0d;
		int len = size();
		for (int i = 0; i < len; i++) {
			result += this.getCoord(i) * other.getCoord(i);
		}
		return result;
	}
	
	/**
	 * Computes the cosine of the angle between this and the given vector.
	 * 
	 * @param other the other vector
	 * @return the cosine of the two vectors
	 */
	public double cosAngle(Vector other) {
		validateVector(other);
		return dot(other) / (norm() * (other.norm()));
	}
	
	private void validateVector(Vector other) {
		Objects.requireNonNull(other, "Vector mustn't be null!");
		
		if (this.size() != other.size()) {
			throw new IllegalArgumentException("Vectors must be of the same dimension!");
		}
	}

	/**
	 * Creates an array with the values of this vector's components.
	 * 
	 * @return array of this vector's components
	 */
	public double[] toArray() {
		int len = coords.size();
		double[] array = new double[len];
		for (int i = 0; i < len; i++) {
			array[i] = coords.get(i);
		}
		return array;
	}
	
}
