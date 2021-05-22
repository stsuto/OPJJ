package hr.fer.zemris.java.hw17.jvdraw.drawing.tool;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Convenience class which offers default Tool methods
 * which do nothing.
 * 
 * @author stipe
 *
 */
public class ToolAdapter implements Tool {

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void paint(Graphics2D g2d) {
	}

}
