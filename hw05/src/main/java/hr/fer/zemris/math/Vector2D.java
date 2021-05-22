package hr.fer.zemris.math;

/**
 * Class <code>Vector2D</code> models a 2D vector whose components are x and y.
 * It offers methods for creating new vectors and performing operations on them.
 * 
 * @author stipe
 *
 */
public class Vector2D {
	
	/**
	 * components of the vector
	 */
	private double x, y;

	/**
	 * Constructor which creates a new vector with given components.
	 * 
	 * @param x x-axis component
	 * @param y y-axis component
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the <code>x</code> component of this vector.
	 * 
	 * @return <code>x</code> of this vector
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Returns the <code>y</code> component of this vector.
	 * 
	 * @return <code>y</code> of this vector
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Translates this vector with components of the given vector.
	 * Components of the other vector are added to those of this vector respectively.
	 * 
	 * @param offset vector with components to be added to this vector
	 */
	public void translate(Vector2D offset) {
		x += offset.getX();
		y += offset.getY();
	}
	
	/**
	 * Creates a new vector by translating this vector with components of the given vector, leaving this vector unchanged.
	 * Components of the other vector are added to those of this vector respectively, creating the components of the new vector.
	 * 
	 * @param offset vector with components to be added to this vector
	 * @return Vector created by translating this vector with the given one
	 */
	public Vector2D translated(Vector2D offset) {
		Vector2D translatedVector = new Vector2D(x, y);
		translatedVector.translate(offset);
		return translatedVector;
	}
	
	/**
	 * Rotates this vector counter-clockwise for the angle given as parameter.
	 * 
	 * @param angle this vector is to be rotated for
	 */
	public void rotate(double angle) {
		double x2 = Math.cos(angle) * x - Math.sin(angle) * y;
		double y2 = Math.sin(angle) * x + Math.cos(angle) * y;
		this.x = x2;
		this.y = y2;
	}
	
	/**
	 * Creates a new vector by rotating this vector counter-clockwise for the angle given as parameter,
	 * but leaves the first vector unchanged.
	 * 
	 * @param angle this vector is to be rotated for
	 * @return Vector created by rotating this vector for the given angle
	 */
	public Vector2D rotated(double angle) {
		Vector2D rotatedVector = new Vector2D(x, y);
		rotatedVector.rotate(angle);
		return rotatedVector;	
	}
	
	/**
	 * Scales this vector with the given scalar.
	 * 
	 * @param scaler number this vector is scaled with
	 */
	public void scale(double scaler) {
		x = x * scaler;
		y = y * scaler;
	}
	
	/**
	 * Creates a new vector by scaling this vector with the given scalar, but leaves this vector unchanged.
	 * 
	 * @param scaler number this vector is scaled with
	 * @return Vector created by scaling this vector
	 */
	public Vector2D scaled(double scaler) {
		Vector2D scaledVector = new Vector2D(x, y);
		scaledVector.scale(scaler);
		return scaledVector;
	
	}
	
	/**
	 * Creates and returns a vector with the same components as this vector.
	 * 
	 * @return Vector equal to this vector
	 */
	public Vector2D copy() {
		return new Vector2D(x, y);
	}
	
}
