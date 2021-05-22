package hr.fer.zemris.java.gui.charts;

/**
 * Class representing a chart value.
 * Y value is mapped to the x value.
 * 
 * @author stipe
 *
 */
public class XYValue {

	/**
	 * Y value.
	 */
	private int x;
	/**
	 * X value.
	 */
	private int y;
	
	/**
	 * Constructor.
	 * 
	 * @param x {@link #x}
	 * @param y {@link #y}
	 */
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

}
