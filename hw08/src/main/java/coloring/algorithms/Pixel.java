package coloring.algorithms;

import java.util.Objects;

/**
 * Class {@code Pixel} represents a single pixel on the screen.
 * Pixel has 2 read-only properties - its x and y coordinates.
 * 
 * @author stipe
 *
 */
public class Pixel {

	/**
	 * Pixel's x coordinate.
	 */
	public int x;
	/**
	 * Pixel's x coordinate.
	 */
	public int y;
	
	/**
	 * Constructor that initializes the properties with the given parameters.
	 * 
	 * @param x {@link #x}
	 * @param y {@link #y}
	 */
	public Pixel(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Pixel))
			return false;
		Pixel other = (Pixel) obj;
		return x == other.x && y == other.y;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

}
