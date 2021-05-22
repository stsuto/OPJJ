package hr.fer.zemris.java.hw17.jvdraw.geometry.editor;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public abstract class GeometricalObjectEditor extends JPanel {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Minimum color component value.
	 */
	protected static final int MIN_COLOR = 0;
	/**
	 * Maximum color component value.
	 */
	protected static final int MAX_COLOR = 255;
	
	/**
	 * Text area for the x component of starting point of the geometrical object drawing.
	 */
	protected JTextArea x1Text;
	/**
	 * Text area for the y component of starting point of the geometrical object drawing.
	 */
	protected JTextArea y1Text;
	/**
	 * Text area for the red component of the geometrical object outline color.
	 */
	protected JTextArea redColorText;
	/**
	 * Text area for the green component of the geometrical object outline color.
	 */
	protected JTextArea greenColorText;
	/**
	 * Text area for the blue component of the geometrical object outline color.
	 */
	protected JTextArea blueColorText;
	/**
	 * X component of the starting point of the geometrical object drawing.
	 */
	protected int x1;
	/**
	 * Y component of the starting point of the geometrical object drawing.
	 */
	protected int y1;
	/**
	 * Red component of the geometrical object outline color.
	 */
	protected int red;
	/**
	 * Green component of the geometrical object outline color.
	 */
	protected int green;
	/**
	 * Blue component of the geometrical object outline color.
	 */
	protected int blue;
	
	/**
	 * Checcks if the edited values are valid.
	 */
	public abstract void checkEditing();
	/**
	 * Accepts the edited values if they are valid.
	 */
	public abstract void acceptEditing();
	
	/**
	 * Parses an integer coordinate from the given text area.
	 * 
	 * @param area area
	 * @return int coordinate
	 */
	protected int parseCoord(JTextArea area) {
		return parseText(area, Integer.MIN_VALUE, Integer.MAX_VALUE, "Coordinate");
	}
	
	/**
	 * Parses an integer color component from the given text area.
	 * 
	 * @param area area
	 * @return int color component
	 */
	protected int parseColor(JTextArea area){
		return parseText(area, MIN_COLOR, MAX_COLOR, "Color");
	}
	
	/**
	 * Parses the text from the given area to an integer.
	 * 
	 * @param area text area
	 * @param minValue minimum valid value
	 * @param maxValue maximum valid value
	 * @param name name of the component
	 * @throws IllegalArgumentException if the parsd value isnt in the valid interval
	 * @throws NumberFormatException if the value can't be parsed to an integer
	 * @return parsed integer
	 */
	protected int parseText(JTextArea area, int minValue, int maxValue, String name) {
		int value = 0;
		try {
			value = Integer.parseInt(area.getText());
		} catch (Exception e) {
			throw new NumberFormatException(name + " values must be integer numbers!");
		}
		
		if (value < minValue || value > maxValue) {
			throw new IllegalArgumentException(
				name + String.format(" values must be in [%d, %d].", minValue, maxValue
			));
		}
		return value;
	}

	/**
	 * Creates a JTextArea for coordinate component and initializes it to the given coordinate value.
	 * 
	 * @param coord coordinate value
	 * @return text area
	 */
	protected JTextArea initCoordTextArea(int coord) {
		return initTextArea(Integer.toString(coord), 5);
	}
	
	/**
	 * Creates a JTextArea for color components and initializes it to the given color copmonent value.
	 * 
	 * @param color color component value
	 * @return text area
	 */
	protected JTextArea initColorTextArea(int color) {
		return initTextArea(Integer.toString(color), 3);
	}

	/**
	 * Creates a JTextArea with the given initial text and given length.
	 * 
	 * @param text initial text
	 * @param length length
	 * @return text area
	 */
	protected JTextArea initTextArea(String text, int length) {
		JTextArea area = new JTextArea(1, length);
		area.setText(text);
		return area;
	}
	
}
