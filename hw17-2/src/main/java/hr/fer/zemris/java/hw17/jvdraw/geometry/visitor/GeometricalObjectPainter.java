package hr.fer.zemris.java.hw17.jvdraw.geometry.visitor;

import java.awt.Graphics2D;

import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.Line;
import hr.fer.zemris.java.hw17.jvdraw.util.DrawingUtil;

/**
 * This clas represents a geometrical object visitor implementation which
 * draws the objects it visits.
 * 
 * @author stipe
 *
 */
public class GeometricalObjectPainter implements GeometricalObjectVisitor {

	/**
	 * Graphics.
	 */
	private Graphics2D g2d;
	
	/**
	 * Constructor
	 * 
	 * @param g2d {@link #g2d}
	 */
	public GeometricalObjectPainter(Graphics2D g2d) {
		this.g2d = g2d;
	}
	
	@Override
	public void visit(Line line) {
		DrawingUtil.drawLine(line, g2d);
	}

	@Override
	public void visit(Circle circle) {
		DrawingUtil.drawCircle(circle, g2d);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		DrawingUtil.drawFilledCircle(filledCircle, g2d);
	}

}
