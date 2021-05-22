package hr.fer.zemris.java.hw17.jvdraw.drawing.tool;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.Circle;
import hr.fer.zemris.java.hw17.jvdraw.util.DrawingUtil;

/**
 * Tool which draws circles.
 * 
 * @author stipe
 *
 */
public class CircleTool extends ToolAdapter {

	/**
	 * Drawing model.
	 */
	private DrawingModel drawingModel;
	/**
	 * Color provider.
	 */
	private IColorProvider provider;
	/**
	 * Circle which is currently being drawn.
	 */
	private Circle circle;
	/**
	 * Canvas.
	 */
	private JDrawingCanvas canvas;
	
	/**
	 * @param drawingModel
	 * @param provider
	 * @param circle
	 */
	public CircleTool(DrawingModel drawingModel, IColorProvider provider, JDrawingCanvas canvas) {
		this.drawingModel = drawingModel;
		this.provider = provider;
		this.canvas = canvas;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (circle == null) {
			Point p = e.getPoint();
			circle = new Circle(p.x, p.y, 0, provider.getCurrentColor());
		
		} else {
			drawingModel.add(circle);
			circle = null;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (circle != null) {
			Point p = e.getPoint();
			circle.setRadiusFromPoint(p.x, p.y);
			canvas.repaint();
		}
	}

	@Override
	public void paint(Graphics2D g2d) {
		if (circle != null) {
			DrawingUtil.drawCircle(circle, g2d);
		}
	}
	
}

