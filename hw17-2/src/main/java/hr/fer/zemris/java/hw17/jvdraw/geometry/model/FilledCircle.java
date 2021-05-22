package hr.fer.zemris.java.hw17.jvdraw.geometry.model;

import java.awt.Color;

import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.geometry.editor.FilledCircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometry.editor.GeometricalObjectEditor;

/**
 * This class represents a filled circle.
 * 
 * @author stipe
 *
 */
public class FilledCircle extends AbstractCircle {

	/**
	 * Filling color of this circle.
	 */
	private Color bgColor;
	
	/**
	 * Constructor.
	 * 
	 * @param xCenter {@link AbstractCircle#xCenter}
	 * @param yCenter {@link AbstractCircle#yCenter}
	 * @param radius {@link AbstractCircle#radius}
	 * @param fgColor {@link AbstractCircle#fgColor}
	 * @param bgColor {@link #bgColor}
	 */
	public FilledCircle(int xCenter, int yCenter, int radius, Color fgColor, Color bgColor) {
		super(xCenter, yCenter, radius, fgColor);
		this.bgColor = bgColor;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new FilledCircleEditor(this);
	}

	@Override
	public String toString() {
		return String.format("Filled circle %s, #%02X%02X%02X", super.toString(), 
				bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
	}

	/**
	 * @return the bgColor
	 */
	public Color getBgColor() {
		return bgColor;
	}

	/**
	 * @param bgColor the bgColor to set
	 */
	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
		notifyListeners();
	}
	
}
