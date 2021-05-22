package hr.fer.zemris.java.hw17.jvdraw.component;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;

/**
 * This class is a JToggleButton which is created with a listner adn the wanted width.
 * 
 * @author stipe
 *
 */
public class ToolButton extends JToggleButton {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Width.
	 */
	private int width;
	
	/**
	 * Constructor
	 * 
	 * @param text button text
	 * @param l button listener
	 * @param width button width
	 */
	public ToolButton(String text, ActionListener l, int width) {
		super(text);
		this.width = width;
		addActionListener(l);
	}
	
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(width, 25);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, 25);
	}
	
	@Override
	public Dimension getMaximumSize() {
		return new Dimension(width, 25);
	}
	
}
