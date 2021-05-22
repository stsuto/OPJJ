package hr.fer.zemris.java.hw17.jvdraw.geometry;

import hr.fer.zemris.java.hw17.jvdraw.geometry.model.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.Line;

/**
 * This interface represents an abstract object performing the role of
 * a geometrical object visitor.
 * 
 * @author stipe
 *
 */
public interface GeometricalObjectVisitor {

	/**
	 * Visits the given line.
	 * 
	 * @param line line
	 */
	public abstract void visit(Line line);
	
	/**
	 * Visits the given circle.
	 * 
	 * @param circle cirlce
	 */
	public abstract void visit(Circle circle);
	
	/**
	 * Visits the given filledcircle
	 * 
	 * @param filledCircle filled circle
	 */
	public abstract void visit(FilledCircle filledCircle);
	
}
