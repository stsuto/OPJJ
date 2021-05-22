package hr.fer.zemris.java.hw17.jvdraw.color;

import java.awt.Color;

/**
 * This interface is an abstraction of a listener of color changes.
 * 
 * @author stipe
 *
 */
public interface ColorChangeListener {
	
	/**
	 * Executes when a new color has been selected.
	 * 
	 * @param source source of change
	 * @param oldColor old color
	 * @param newColor new color
	 */
	void newColorSelected(IColorProvider source, Color oldColor, Color newColor);

}
