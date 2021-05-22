package hr.fer.zemris.java.hw17.jvdraw.util;

import java.awt.Graphics2D;

import hr.fer.zemris.java.hw17.jvdraw.geometry.model.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.Line;

/**
 * Utility class which offers drawing capabilities.
 * 
 * @author stipe
 *
 */
public class DrawingUtil {

	/**
	 * Draws the given line.
	 * 
	 * @param line
	 * @param g2d
	 */
	public static void drawLine(Line line, Graphics2D g2d) {
		g2d.setColor(line.getColor());
		g2d.drawLine(
			line.getxStart(), line.getyStart(), 
			line.getxEnd(), line.getyEnd()
		);
	}
	
	/**
	 * Draws the given circle.
	 * 
	 * @param circle
	 * @param g2d
	 */
	public static void drawCircle(Circle circle, Graphics2D g2d) {
		int radius = circle.getRadius();
		int left = circle.getxCenter() - radius;
		int up = circle.getyCenter() - radius; 

		g2d.setColor(circle.getFgColor());
		g2d.drawOval(left, up, 2 * radius, 2 * radius);
	}	
	
	/**
	 * Draws the given filled circle.
	 * 
	 * @param filledCircle
	 * @param g2d
	 */
	public static void drawFilledCircle(FilledCircle filledCircle, Graphics2D g2d) {
		int radius = filledCircle.getRadius();
		int left = filledCircle.getxCenter() - radius;
		int up = filledCircle.getyCenter() - radius; 
		
		g2d.setColor(filledCircle.getBgColor());
		g2d.fillOval(left, up, 2 * radius, 2 * radius);
		
		g2d.setColor(filledCircle.getFgColor());
		g2d.drawOval(left, up, 2 * radius, 2 * radius);
	}
	
	private DrawingUtil() {}
	
}
