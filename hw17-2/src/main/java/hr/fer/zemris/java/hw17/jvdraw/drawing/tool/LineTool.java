package hr.fer.zemris.java.hw17.jvdraw.drawing.tool;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.Line;
import hr.fer.zemris.java.hw17.jvdraw.util.DrawingUtil;

/**
 * Tool which draws a line.
 * 
 * @author stipe
 *
 */
public class LineTool extends ToolAdapter {

	/**
	 * Drawing model.
	 */
	private DrawingModel drawingModel;
	/**
	 * Color provider.
	 */
	private IColorProvider provider;
	/**
	 * Currently drawn line.
	 */
	private Line line;
	/**
	 * Canvas.
	 */
	private JDrawingCanvas canvas;
	
	/**
	 * @param drawingModel
	 * @param provider
	 * @param canvas
	 */
	public LineTool(DrawingModel drawingModel, IColorProvider provider, JDrawingCanvas canvas) {
		this.drawingModel = drawingModel;
		this.provider = provider;
		this.canvas = canvas;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (line == null) {
			Point p = e.getPoint();			
			line = new Line(p.x, p.y, p.x, p.y, provider.getCurrentColor());
			
		} else {
			drawingModel.add(line);
			line = null;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (line != null) {
			Point p = e.getPoint();
			line.setxEnd(p.x);
			line.setyEnd(p.y);
			canvas.repaint();
		}
	}

	@Override
	public void paint(Graphics2D g2d) {
		if (line != null) {
			DrawingUtil.drawLine(line, g2d);			
		}
	}

}
