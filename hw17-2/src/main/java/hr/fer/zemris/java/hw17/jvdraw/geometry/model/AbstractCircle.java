package hr.fer.zemris.java.hw17.jvdraw.geometry.model;

import java.awt.Color;

import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObject;

/**
 * This class represent all circles with a center, radius and outline color.
 * 
 * @author stipe
 *
 */
public abstract class AbstractCircle extends GeometricalObject {

	/**
	 * X component of the circle center.
	 */
	private int xCenter;
	/**
	 * Y component of the circle center.
	 */
	private int yCenter;
	/**
	 * Color of the circle outline.
	 */
	private Color fgColor;
	/**
	 * Circle's radius.
	 */
	private int radius;
	
	/**
	 * Constructor.
	 * 
	 * @param xCenter {@link #xCenter}
	 * @param yCenter {@link #yCenter}
	 * @param radius {@link #radius}
	 * @param fgColor {@link #fgColor}
	 */
	public AbstractCircle(int xCenter, int yCenter, int radius, Color fgColor) {
		this.xCenter = xCenter;
		this.yCenter = yCenter;
		this.radius = radius;
		this.fgColor = fgColor;
	}

	@Override
	public String toString() {
		return String.format("(%d,%d), %d", xCenter, yCenter, radius);
	}
	
	/**
	 * Calculates the distance of the point with hte given coordinates to this
	 * circle's center.
	 * 
	 * @param x x of the given point
	 * @param y y of the given point
	 * @return distance between this circle's center and the given point
	 */
	public int getDistanceFromCenter(int x, int y) {
		int xDif = xCenter - x;
		int yDif = yCenter - y;
		
		return (int) Math.sqrt(xDif * xDif + yDif * yDif);
	}
	
	/**
	 * Sets this circle's radius to the distance between this circle's center 
	 * and the given point
	 * 
	 * @param x x of the given point
	 * @param y y of the given point
	 */
	public void setRadiusFromPoint(int x, int y) {
		setRadius(getDistanceFromCenter(x, y));
	}
	
	/**
	 * @return the xCenter
	 */
	public int getxCenter() {
		return xCenter;
	}

	/**
	 * @param xCenter the xCenter to set
	 */
	public void setxCenter(int xCenter) {
		this.xCenter = xCenter;
		notifyListeners();
	}

	/**
	 * @return the yCenter
	 */
	public int getyCenter() {
		return yCenter;
	}

	/**
	 * @param yCenter the yCenter to set
	 */
	public void setyCenter(int yCenter) {
		this.yCenter = yCenter;
		notifyListeners();
	}

	/**
	 * @return the fgColor
	 */
	public Color getFgColor() {
		return fgColor;
	}

	/**
	 * @param fgColor the fgColor to set
	 */
	public void setFgColor(Color fgColor) {
		this.fgColor = fgColor;
		notifyListeners();
	}

	/**
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(int radius) {
		this.radius = radius;
		notifyListeners();
	}
	
}