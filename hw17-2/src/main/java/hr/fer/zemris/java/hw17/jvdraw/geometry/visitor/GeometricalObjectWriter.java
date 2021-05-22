package hr.fer.zemris.java.hw17.jvdraw.geometry.visitor;

import java.awt.Color;

import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.AbstractCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.Line;

/**
 * This clas represents a geometrical object visitor implementation which
 * writes all objects visited to a string that can be later saved to a document.
 * 
 * @author stipe
 *
 */
public class GeometricalObjectWriter implements GeometricalObjectVisitor {

	/**
	 * String builder.
	 */
	private StringBuilder sb;	
	
	/**
	 * Constructor.
	 */
	public GeometricalObjectWriter() {
		sb = new StringBuilder();
	}
	
	/**
	 * @return definition text
	 */
	public String getDefinitionText() {
		return sb.toString();
	}
	
	@Override
	public void visit(Line line) {
		Color color = line.getColor();
        
		sb.append(String.format("LINE %d %d %d %d %d %d %d\n", 
        		line.getxStart(), line.getyStart(), line.getxEnd(), line.getyEnd(), 
        		color.getRed(), color.getGreen(), color.getBlue()));
	}

	@Override
	public void visit(Circle circle) {
		sb.append(visitCircle(circle)).append('\n');
	}

	private String visitCircle(AbstractCircle circle) {
		Color color = circle.getFgColor();
        
		return String.format("CIRCLE %d %d %d %d %d %d", 
        		circle.getxCenter(), circle.getyCenter(), circle.getRadius(),
        		color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		Color bgColor = filledCircle.getBgColor();
		
		sb.append(String.format("F%s %d %d %d\n", visitCircle(filledCircle),
				bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue()));

	}

}
