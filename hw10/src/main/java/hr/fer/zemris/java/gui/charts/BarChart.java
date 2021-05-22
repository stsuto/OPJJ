package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * Class which represents a bar chart.
 * 
 * @author stipe
 *
 */
public class BarChart {

	/**
	 * Chart values.
	 */
	private List<XYValue> values;
	/**
	 * X-axis description.
	 */
	private String xAxisDescription;
	/**
	 * Y-axis description.
	 */
	private String yAxisDescription;
	/**
	 * Minumum y value.
	 */
	private int minY;
	/**
	 * Maximum y value.
	 */
	private int maxY;
	/**
	 * Difference between consecutive y chart values.
	 */
	private int diffY;
	
	/**
	 * Constructor.
	 * 
	 * @param values
	 * @param xAxisDescription
	 * @param yAxisDescription
	 * @param minY
	 * @param maxY
	 * @param diffY
	 */
	public BarChart(List<XYValue> values, String xAxisDescription, 
				String yAxisDescription, int minY, int maxY, int diffY) {
		this.values = values;
		this.xAxisDescription = xAxisDescription;
		this.yAxisDescription = yAxisDescription;
		this.minY = requireCondition(minY, minY >= 0, "Minimal y mustn't be negative!");
		this.maxY = requireCondition(maxY, maxY > minY, "Maximal y mustn't be less than minimal y!");
		this.diffY = diffY;
		values.forEach(value -> requireCondition(value.getY(), value.getY() > minY, 
				"All y values must be greater or equal than minimal y!"));
		checkValues(values, minY); // mozda requireValidValues(values, minY);
	}

	/**
	 * Calculates the integer value taking into account the necessary conditions.
	 * 
	 * @param number
	 * @param condition
	 * @param condMessage
	 * @return
	 */
	private int requireCondition(int number, boolean condition, String condMessage) {
		if (!condition) {
			throw new IllegalArgumentException(condMessage);
		}
		return number;
	}

	private void checkValues(List<XYValue> values, int minY) {
		if (values.stream().filter(v -> v.getY() < minY).count() > 0) {
			throw new IllegalArgumentException("All y values must be greater or equal than minimal y!");
		}
	}

	/**
	 * @return the values
	 */
	public List<XYValue> getValues() {
		return values;
	}

	/**
	 * @return the xAxisDescription
	 */
	public String getxAxisDescription() {
		return xAxisDescription;
	}

	/**
	 * @return the yAxisDescription
	 */
	public String getyAxisDescription() {
		return yAxisDescription;
	}

	/**
	 * @return the minY
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * @return the maxY
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * @return the diffY
	 */
	public int getDiffY() {
		return diffY;
	}
	
}
