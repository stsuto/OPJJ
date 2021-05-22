package hr.fer.zemris.java.hw17.jvdraw.drawing.tool;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.geometry.model.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.util.DrawingUtil;

/**
 * Tool which draws a filled circle.
 * 
 * @author stipe
 *
 */
public class FilledCircleTool extends ToolAdapter {

	/**
	 * Drawing model.
	 */
	private DrawingModel drawingModel;
	/**
	 * Color provider.
	 */
	private IColorProvider fgProvider;
	/**
	 * Bacground color provider.
	 */
	private IColorProvider bgProvider;
	/**
	 * Circle which is currently being drawn.
	 */
	private FilledCircle circle;
	/**
	 * Canvas.
	 */
	private JDrawingCanvas canvas;
	
	/**
	 * @param drawingModel
	 * @param fgProvider
	 * @param bgProvider
	 * @param circle
	 */
	public FilledCircleTool(DrawingModel drawingModel, IColorProvider fgProvider, 
				IColorProvider bgProvider, JDrawingCanvas canvas) {
		this.drawingModel = drawingModel;
		this.fgProvider = fgProvider;
		this.bgProvider = bgProvider;
		this.canvas = canvas;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (circle == null) {
			Point p = e.getPoint();
			Color fgColor = fgProvider.getCurrentColor();
			Color bgColor = bgProvider.getCurrentColor();
			circle = new FilledCircle(p.x, p.y, 0, fgColor, bgColor);
		
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
			DrawingUtil.drawFilledCircle(circle, g2d);
		}
	}
	
}
