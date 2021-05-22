package hr.fer.zemris.java.hw17.jvdraw.geometry.visitor;

import java.awt.Rectangle;

import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.AbstractCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.Line;

/**
 * This clas represents a geometrical object visitor implementation which
 * calculates the minimum sized box which contans all objects.
 * 
 * @author stipe
 *
 */
public class GeometricalObjectBBCalculator implements GeometricalObjectVisitor {

	/**
	 * Bounding box.
	 */
	private Rectangle boundingBox;
	
	@Override
	public void visit(Line line) {
		if (boundingBox == null) {
			boundingBox = createRectangle(
				line.getxStart(), line.getyStart(), 
				line.getxEnd(), line.getyEnd()
			);
		} else {
			boundingBox.add(line.getxStart(), line.getyStart());
			boundingBox.add(line.getxEnd(), line.getyEnd());
		}
	}

	@Override
	public void visit(Circle circle) {
		visitCircles(circle);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		visitCircles(filledCircle);
	}
	
	private void visitCircles(AbstractCircle circle) {
		int radius = circle.getRadius();
		int left = circle.getxCenter() - radius;
		int up = circle.getyCenter() - radius;
		int right = circle.getxCenter() + radius;
		int down = circle.getyCenter() + radius;
		
		if (boundingBox == null) {
			boundingBox = createRectangle(left, up, right, down);
		} else {
			boundingBox.add(left, up);
			boundingBox.add(left, down);
			boundingBox.add(right, up);
			boundingBox.add(right, down);
		}
	}

	/**
	 * @return {@link #boundingBox}
	 */
	public Rectangle getBoundingBox() {
		return boundingBox;
	}

	/**
	 * Creates a rectangle from the given coordinates.
	 * 
	 * @param x1 left coordinate
	 * @param y1 up coordinate
	 * @param x2 right coordinate
	 * @param y2 down coordinate
	 * @return
	 */
	private Rectangle createRectangle(int x1, int y1, int x2, int y2) {
		int x = x1 < x2 ? x1 : x2;
		int y = y1 < y2 ? y1 : y2;
		return new Rectangle(x, y, Math.abs(x2 - x1), Math.abs(y2 - y1));
	}
	
}
