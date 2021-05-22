package hr.fer.zemris.java.hw17.jvdraw.color;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

/**
 * This class is a component which provides color.
 * 
 * @author stipe
 *
 */
public class JColorArea extends JComponent implements IColorProvider {

	private static final long serialVersionUID = 1L;

	/**
	 * Current color.
	 */
	private Color selectedColor;
	/**
	 * Registered listeners.
	 */
	private List<ColorChangeListener> listeners;
	
	/**
	 * Constructor.
	 * 
	 * @param selectedColor initial color
	 */
	public JColorArea(Color selectedColor) {
		this.selectedColor = selectedColor;
		this.listeners = new ArrayList<>();
		addListener();
	}

	/**
	 * Adds a mouse listener to know when this component was clicked.
	 */
	private void addListener() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Color newColor = chooseColor();
				if (newColor != null) {
					changeColor(newColor);					
				}
			}
		});
	}

	/**
	 * @return chosen color
	 */
	private Color chooseColor() {
		return JColorChooser.showDialog(this, "Choose the new color", selectedColor);
	}
	
	/**
	 * Changes the color.
	 * 
	 * @param newColor new color
	 */
	private void changeColor(Color newColor) {
		Color oldColor = selectedColor;
		selectedColor = newColor;
		listeners.forEach(l -> l.newColorSelected(this, oldColor, newColor));
		repaint();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(15, 15);
	}
	
	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(selectedColor);
		g2d.fillRect(0, 0, getWidth(), getHeight());
	}
	
	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}

	@Override
	public void addColorChangeListener(ColorChangeListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	@Override
	public void removeColorChangeListener(ColorChangeListener l) {
		listeners.remove(l);
	}
	
}
