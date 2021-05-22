package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class BarChartDemo extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Chart.
	 */
	private BarChart chart;
	/**
	 * Path.
	 */
	private String path;
	
	/**
	 * Constructor.
	 * 
	 * @param chart
	 * @param path
	 */
	public BarChartDemo(BarChart chart, String path) {
		this.chart = chart;
		this.path = Paths.get(path).toAbsolutePath().toString();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocation(20, 20);
		setSize(500, 500);
		initGUI();
	}

	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		setLayout(new BorderLayout());
		add(new JLabel(path, SwingConstants.CENTER), BorderLayout.PAGE_START);
		add(new BarChartComponent(chart), BorderLayout.CENTER);
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Exactly one argument must be given!");
			System.exit(1);
		}
		
		List<String> input = null;
		try {
			input = Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8);
		} catch (IOException | InvalidPathException e) {
			System.out.println("Couldn't read from file " + args[0] + "!");
			System.exit(1);
		} 
		
		try {
			BarChart chart = parseChart(input);	
			SwingUtilities.invokeLater(() -> new BarChartDemo(chart, args[0]).setVisible(true));
		
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
//		BarChart chart = new BarChart(
//			Arrays.asList(
//				new XYValue(1, 8), new XYValue(2, 20), new XYValue(3, 22),
//				new XYValue(4, 10), new XYValue(5, 4)
//			), 
//			"Number of people in the car", 
//			"Frequency", 
//			0, 
//			22, 
//			2
//		);
//
//		SwingUtilities.invokeLater(() -> new BarChartDemo(chart).setVisible(true));
		
	}

	/**
	 * Parses a chart from the input.
	 * 
	 * @param input input
	 * @return BarChart
	 */
	private static BarChart parseChart(List<String> input) {
		if (input.size() < 5) {
			throw new RuntimeException("File must contain at least 5 rows of data.");
		}
		try {
			String[] values = input.get(2).split("\\s+");
			List<XYValue> xyValues = new ArrayList<>();
			for (String value : values) {
				String[] parts = value.split(",");
				if (parts.length != 2) {
					throw new RuntimeException("Invalid chart values in file.");
				}
				xyValues.add(new XYValue(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
			}
			
			return new BarChart(
					xyValues, 
					input.get(0), 
					input.get(1), 
					Integer.parseInt(input.get(3)), 
					Integer.parseInt(input.get(4)), 
					Integer.parseInt(input.get(5))
				);

		} catch (NumberFormatException e) {
			throw new RuntimeException("Incorrect format of number values.");
		}
	}

}
