package hr.fer.zemris.java.hw17.jvdraw.geometry.model;

import java.awt.Color;

import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.geometry.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometry.editor.LineEditor;

public class Line extends GeometricalObject {

	/**
	 * X component of this line's starting point.
	 */
	private int xStart;
	/**
	 * Y component of this line's starting point.
	 */
	private int yStart;
	/**
	 * X component of this line's ending point.
	 */
	private int xEnd;
	/**
	 * Y component of this line's ending point.
	 */
	private int yEnd;
	/**
	 * This line's color.
	 */
	private Color color;

	/**
	 * Constructor.
	 * 
	 * @param xStart {@link #xStart}
	 * @param yStart {@link #yStart}
	 * @param xEnd {@link #xEnd}
	 * @param yEnd {@link #yEnd}
	 * @param color {@link #color}
	 */
	public Line(int xStart, int yStart, int xEnd, int yEnd, Color color) {
		this.xStart = xStart;
		this.yStart = yStart;
		this.xEnd = xEnd;
		this.yEnd = yEnd;
		this.color = color;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new LineEditor(this);
	}

	@Override
	public String toString() {
		return String.format("Line (%d,%d)-(%d,%d)", xStart, yStart, xEnd, yEnd);
	}
	
	/**
	 * @return the xStart
	 */
	public int getxStart() {
		return xStart;
	}

	/**
	 * @param xStart the xStart to set
	 */
	public void setxStart(int xStart) {
		this.xStart = xStart;
		notifyListeners();
	}

	/**
	 * @return the yStart
	 */
	public int getyStart() {
		return yStart;
	}

	/**
	 * @param yStart the yStart to set
	 */
	public void setyStart(int yStart) {
		this.yStart = yStart;
		notifyListeners();
	}

	/**
	 * @return the xEnd
	 */
	public int getxEnd() {
		return xEnd;
	}

	/**
	 * @param xEnd the xEnd to set
	 */
	public void setxEnd(int xEnd) {
		this.xEnd = xEnd;
		notifyListeners();
	}

	/**
	 * @return the yEnd
	 */
	public int getyEnd() {
		return yEnd;
	}

	/**
	 * @param yEnd the yEnd to set
	 */
	public void setyEnd(int yEnd) {
		this.yEnd = yEnd;
		notifyListeners();
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
		notifyListeners();
	}
	
}
