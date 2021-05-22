package hr.fer.zemris.java.hw17.jvdraw.geometry.editor;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import hr.fer.zemris.java.hw17.jvdraw.geometry.model.AbstractCircle;

/**
 * This class represents an abstract circle editor.
 * 
 * @author stipe
 *
 */
public abstract class AbstractCircleEditor extends GeometricalObjectEditor {

	private static final long serialVersionUID = 1L;

	/**
	 * Minimum radius value.
	 */
	protected static final int MIN_RADIUS = 0;
	
	/**
	 * Abstract circle which is to be edited.
	 */
	protected AbstractCircle circle;
	/**
	 * Text area for the circle radius.
	 */
	protected JTextArea radiusText;
	/**
	 * Circle radius.
	 */
	protected int radius;
	
	/**
	 * Constructor.
	 * 
	 * @param circle {@link #circle}
	 */
	public AbstractCircleEditor(AbstractCircle circle) {
		this.circle = circle;
	}
	
	@Override
	public abstract void checkEditing();

	@Override
	public abstract void acceptEditing();

	protected int parseRadius(JTextArea area) {
		return parseText(area, MIN_RADIUS, Integer.MAX_VALUE, "Radius");
	}
	
	/**
	 * Creates a panel with circle center components.
	 * 
	 * @return center panel
	 */
	protected JPanel createStartPanel() {
		JPanel startPanel = new JPanel();
		
		startPanel.add(new JLabel("xCenter"));
		x1Text = initCoordTextArea(circle.getxCenter());
		startPanel.add(x1Text);
		
		startPanel.add(new JLabel("yCenter"));
		y1Text = initCoordTextArea(circle.getyCenter());
		startPanel.add(y1Text);
		
		return startPanel;
	}

	/**
	 * Creates a panel with circle radius component.
	 * 
	 * @return radius panel
	 */
	protected JPanel createRadiusPanel() {
		JPanel radiusPanel = new JPanel();
		
		radiusPanel.add(new JLabel("Radius"));
		radiusText = initCoordTextArea(circle.getRadius());
		radiusPanel.add(radiusText);
		
		return radiusPanel;
	}

	/**
	 * Creates a panel with circle color components.
	 * 
	 * @return color panel
	 */
	protected JPanel createColorPanel() {
		JPanel colorPanel = new JPanel();
		Color color = circle.getFgColor();
		
		colorPanel.add(new JLabel("FG color:"));
		
		colorPanel.add(new JLabel("Red: "));
		redColorText = initColorTextArea(color.getRed());
		colorPanel.add(redColorText);
		
		colorPanel.add(new JLabel("Green: "));
		greenColorText = initColorTextArea(color.getGreen());
		colorPanel.add(greenColorText);
		
		colorPanel.add(new JLabel("Blue: "));
		blueColorText = initColorTextArea(color.getBlue());
		colorPanel.add(blueColorText);
		
		return colorPanel;
	}
	
}
