package hr.fer.zemris.java.hw17.jvdraw.geometry.model;

import java.awt.Color;

import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.geometry.editor.CircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometry.editor.GeometricalObjectEditor;

/**
 * This class represents a circle.
 * 
 * @author stipe
 *
 */
public class Circle extends AbstractCircle {
	
	/**
	 * Constructor.
	 * 
	 * @param xCenter {@link AbstractCircle#xCenter}
	 * @param yCenter {@link AbstractCircle#yCenter}
	 * @param radius {@link AbstractCircle#radius}
	 * @param fgColor {@link AbstractCircle#fgColor}
	 */
	public Circle(int xCenter, int yCenter, int radius, Color fgColor) {
		super(xCenter, yCenter, radius, fgColor);
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}
	
	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new CircleEditor(this);
	}
	
	@Override
	public String toString() {
		return "Circle " + super.toString();
	}
	
}
