package hr.fer.zemris.java.hw17.jvdraw.drawing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Supplier;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw17.jvdraw.drawing.tool.Tool;
import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.geometry.visitor.GeometricalObjectPainter;

/**
 * This class is the central component of the appication.
 * Everything is drawn on this surface.
 * 
 * @author stipe
 *
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener {

	private static final long serialVersionUID = 1L;

	/**
	 * Drawing model.
	 */
	private DrawingModel drawingModel;
	/**
	 * Tool supplier.
	 */
	private Supplier<Tool> toolSupplier;
	
	/**
	 * @param drawingModel
	 * @param tool
	 */
	public JDrawingCanvas(DrawingModel drawingModel, Supplier<Tool> toolSupplier) {
		this.drawingModel = drawingModel;
		this.toolSupplier = toolSupplier;		
		
		drawingModel.addDrawingModelListener(this);
		addListeners();
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		repaint(); // #repaint calls #paintComponent
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		GeometricalObjectVisitor painter = new GeometricalObjectPainter(g2d);

		paintCanvasBackground(g2d);
		
		int size = drawingModel.getSize();
		for (int i = 0; i < size; i++) {
			drawingModel.getObject(i).accept(painter);
		}
		
		toolSupplier.get().paint(g2d);
	}

	/**
	 * Paints the canvas background white.
	 * 
	 * @param g2d graphics
	 */
	private void paintCanvasBackground(Graphics2D g2d) {
		Color old = g2d.getColor();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(old);
	}

	/**
	 * Adds the mouse listeners.
	 */
	private void addListeners() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				toolSupplier.get().mousePressed(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				toolSupplier.get().mouseReleased(e);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				toolSupplier.get().mouseClicked(e);
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				toolSupplier.get().mouseMoved(e);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				toolSupplier.get().mouseDragged(e);
			}
		});
	}
	
}
