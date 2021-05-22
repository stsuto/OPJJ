package hr.fer.zemris.java.hw17.jvdraw.color;

import java.awt.Color;

/**
 * This interface is an abstraction of a color provider.
 * 
 * @author stipe
 *
 */
public interface IColorProvider {

	/**
	 * @return current color
	 */
	Color getCurrentColor();
	/**
	 * Registers the given listener to this object.
	 * 
	 * @param l listener
	 */
	void addColorChangeListener(ColorChangeListener l);
	/**
	 * Unregisters the given listener from this object.
	 * 
	 * @param l listener
	 */
	void removeColorChangeListener(ColorChangeListener l);
	
}
