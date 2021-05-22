package hr.fer.zemris.java.hw17.jvdraw.drawing.tool;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * This interface represents an abstraction which draws objects.
 * 
 * @author stipe
 *
 */
public interface Tool {

	/**
	 * Happens when the mouse is pressed.
	 * 
	 * @param e event
	 */
	public void mousePressed(MouseEvent e);
	
	/**
	 * Happens when the mouse is released.
	 * 
	 * @param e event
	 */
	public void mouseReleased(MouseEvent e);
	
	/**
	 * Happens when the mouse is clicked.
	 * 
	 * @param e event
	 */
	public void mouseClicked(MouseEvent e);
	
	/**
	 * Happens when the mouse is moved.
	 * 
	 * @param e event
	 */
	public void mouseMoved(MouseEvent e);
	
	/**
	 * Happens when the mouse is dragged.
	 * 
	 * @param e event
	 */
	public void mouseDragged(MouseEvent e);
	
	/**
	 * Paints the component.
	 * 
	 * @param g2d
	 */
	public void paint(Graphics2D g2d);
	
}
