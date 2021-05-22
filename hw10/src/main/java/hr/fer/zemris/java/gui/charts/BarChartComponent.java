package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;

/**
 * Class which represents a bar chart component.
 * 
 * @author stipe
 *
 */
public class BarChartComponent extends JComponent {

	/**
	 * Default serial ID.
	 */
	private static final long serialVersionUID = 1L;

	private static final int LINE_LENGTH = 5;
	private static final int FREE_SPACE = 15;
	private static final int AXIS_POINT_SIZE = 5;
	private static final Font FONT = new Font("Comic Sans", Font.PLAIN, 18);
	private static final Font NUMBER_FONT = new Font("Comic Sans", Font.BOLD, 12);
	
	private BarChart chart;

	public BarChartComponent(BarChart chart) {
		this.chart = chart;
	}

	@Override
	public void paintComponent(Graphics g) {
		Insets ins = getInsets();
		Dimension dim = getSize();
		Graphics2D graphics = (Graphics2D) g;
		
		graphics.setFont(FONT);
		FontMetrics fm = graphics.getFontMetrics();
		
		int largestNumOffset = fm.stringWidth(Integer.toString(chart.getMaxY()));
		int startX = fm.getAscent() + largestNumOffset + 3 * FREE_SPACE + ins.left;
		int startY = dim.height - fm.getAscent() - 3 * FREE_SPACE - ins.bottom;
		int endX = dim.width - ins.right - FREE_SPACE;
		int endY = ins.top + FREE_SPACE;
		
		drawData(graphics, startX, startY, endX, endY);
		drawAxes(graphics, startX, startY, endX, endY);
		drawAxesNames(graphics, startX, startY, endX, endY);
		drawNumbersAndLines(graphics, startX, startY, endX, endY);
	}
	
//	/**
//	 * Draws the data.
//	 * 
//	 * @param graphics
//	 * @param startX
//	 * @param startY
//	 * @param endX
//	 * @param endY
//	 */
//	private void drawData(Graphics2D graphics, int startX, int startY, int endX, int endY) {
//		int stepCountX = chart.getValues().size();
//		int stepX = (endX - startX) / stepCountX;
//		int scalar = (startY - endY) / (chart.getMaxY() - chart.getMinY());
//		
//		for (int i = 0; i < stepCountX; i++) {
//			int x = startX + i * stepX;
//			int value = chart.getValues().get(i).getY();
//			int height = value <= chart.getMaxY() ? value : chart.getMaxY();
//			int scaledHeight = height * scalar;
//			
//			graphics.setColor(Color.ORANGE);
//			graphics.fillRect(x, startY - scaledHeight, stepX, height * scalar);
//			graphics.setColor(Color.WHITE);
//			graphics.drawRect(x, startY - scaledHeight, stepX, height * scalar);
//		}
//		graphics.setColor(Color.BLACK);
//	}

	/**
	 * Draws the data.
	 * 
	 * @param graphics
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 */
	private void drawData(Graphics2D graphics, int startX, int startY, int endX, int endY) {
		int stepCountX = chart.getValues().size();
		int stepX = (endX - startX) / stepCountX;
		double scalar = (startY - endY) / (chart.getMaxY() - chart.getMinY());
		
		for (int i = 0; i < stepCountX; i++) {
			int x = startX + i * stepX;
			int value = chart.getValues().get(i).getY();
			int height = value <= chart.getMaxY() ? value : chart.getMaxY();
			int scaledHeight = (int) (height * scalar);
			
			graphics.setColor(Color.ORANGE);
			graphics.fillRect(x, startY - scaledHeight, stepX, scaledHeight);
			graphics.setColor(Color.WHITE);
			graphics.drawRect(x, startY - scaledHeight, stepX, scaledHeight);
		}
		graphics.setColor(Color.BLACK);
	}
	
	/**
	 * Draws numbers and lines.
	 * 
	 * @param graphics
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 */
	private void drawNumbersAndLines(Graphics2D graphics, int startX, int startY, int endX, int endY) {
		int stepCountY = (int) Math.floor(
				(chart.getMaxY() - chart.getMinY()) / chart.getDiffY());
		int diffY = (chart.getMaxY() - chart.getMinY()) / stepCountY;
		int stepY = (startY - endY) / stepCountY;
		int stepCountX = chart.getValues().size();
		int stepX = (endX - startX) / stepCountX;
		
		Font oldFont = graphics.getFont();
		graphics.setFont(NUMBER_FONT);
		FontMetrics fm = graphics.getFontMetrics();
		
		for (int i = 0; i <= stepCountX; i++) {
			int x = startX + i * stepX;
			graphics.drawLine(x, startY, x, startY + LINE_LENGTH);
			
			if (i != stepCountX) {
				String s = Integer.toString(i + 1);
				int w = fm.stringWidth(s);
				int h = fm.getAscent();
				graphics.drawString(s, x - (w - stepX) / 2, startY + FREE_SPACE / 2 + h);				
			}
		}
	
		for (int i = 0; i <= stepCountY; i++) {
			int y = startY - i * stepY;
			graphics.drawLine(startX - LINE_LENGTH, y, startX, y);
			String s = Integer.toString(chart.getMinY() + i * diffY);			
			int w = fm.stringWidth(s);
			int h = fm.getAscent();
			graphics.drawString(s, startX - LINE_LENGTH - FREE_SPACE / 2 - w, y + h / 2);
		}
		
		graphics.setFont(oldFont);
	}

	/**
	 * Draws axes.
	 * 
	 * @param graphics
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 */
	private void drawAxes(Graphics2D graphics, int startX, int startY, int endX, int endY) {
		drawAxis(graphics, startX, startY, endX, startY, true);
		drawAxis(graphics, startX, startY, startX, endY, false);
	}
	
	/**
	 * Draws an axis.
	 * 
	 * @param graphics
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param isHorizontal
	 */
	private void drawAxis(Graphics2D graphics, int startX, int startY, int endX, int endY, boolean isHorizontal) {
		graphics.drawLine(startX, startY, endX, endY);

		Polygon arrowHead = new Polygon();  
		if (isHorizontal) {
			arrowHead.addPoint(endX + AXIS_POINT_SIZE, startY);
			arrowHead.addPoint(endX, startY + AXIS_POINT_SIZE);
			arrowHead.addPoint(endX, startY - AXIS_POINT_SIZE);
		} else {
			arrowHead.addPoint(startX, endY - AXIS_POINT_SIZE);
			arrowHead.addPoint(startX + AXIS_POINT_SIZE, endY);
			arrowHead.addPoint(startX - AXIS_POINT_SIZE, endY);
		}
		
		graphics.fill(arrowHead);
	}

	/**
	 * Draws axes names.
	 * 
	 * @param graphics
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 */
	private void drawAxesNames(Graphics2D graphics, int startX, int startY, int endX, int endY) {
		FontMetrics fm = graphics.getFontMetrics();
		AffineTransform oldTransform = graphics.getTransform();
		
		int w = fm.stringWidth(chart.getxAxisDescription());
		int h = fm.getAscent();
		graphics.drawString(chart.getxAxisDescription(), (startX + endX - w) / 2, startY + 2 * FREE_SPACE + h);

		graphics.setTransform(AffineTransform.getQuadrantRotateInstance(3));
		
		w = fm.stringWidth(chart.getyAxisDescription());
		h = fm.getAscent();
		graphics.drawString(chart.getyAxisDescription(), -(startY + endY + w) / 2, h + FREE_SPACE);
		
		graphics.setTransform(oldTransform);
	}

}
