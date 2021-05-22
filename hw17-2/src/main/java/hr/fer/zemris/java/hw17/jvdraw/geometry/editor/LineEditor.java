package hr.fer.zemris.java.hw17.jvdraw.geometry.editor;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import hr.fer.zemris.java.hw17.jvdraw.geometry.model.Line;

/**
 * This class represents a line editor.
 * 
 * @author stipe
 *
 */
public class LineEditor extends GeometricalObjectEditor {

	private static final long serialVersionUID = 1L;

	/**
	 * Line which is being edited.
	 */
	private Line line;
	/**
	 * Text area for the x component of ending point of the line drawing.
	 */
	private JTextArea x2Text;
	/**
	 * Text area for the x component of edning point of the line drawing.
	 */
	private JTextArea y2Text;	
	/**
	 * X component of the ending point of the line drawing.
	 */
	private int x2;
	/**
	 * Y component of the ending point of the line drawing.
	 */
	private int y2;
	
	/**
	 * Constructor.
	 * 
	 * @param line
	 */
	public LineEditor(Line line) {
		this.line = line;
		initEditor();
	}

	/**
	 * Initializes editor components.
	 */
	private void initEditor() {
		setLayout(new GridLayout(0, 1));
		
		add(createStartPanel());
		add(createEndPanel());
		add(createColorPanel());
	}

	/**
	 * Creates a panel with line start components.
	 * 
	 * @return start panel
	 */
	private JPanel createStartPanel() {
		JPanel startPanel = new JPanel();
		
		startPanel.add(new JLabel("x1"));
		x1Text = initCoordTextArea(line.getxStart());
		startPanel.add(x1Text);
		
		startPanel.add(new JLabel("y1"));
		y1Text = initCoordTextArea(line.getyStart());
		startPanel.add(y1Text);
		
		return startPanel;
	}

	/**
	 * Creates a panel with line end components.
	 * 
	 * @return end panel
	 */
	private JPanel createEndPanel() {
		JPanel endPanel = new JPanel();
		
		endPanel.add(new JLabel("x2"));
		x2Text = initCoordTextArea(line.getxEnd());
		endPanel.add(x2Text);
		
		endPanel.add(new JLabel("y2"));
		y2Text = initCoordTextArea(line.getyEnd());
		endPanel.add(y2Text);
		
		return endPanel;
	}

	/**
	 * Creates a panel with line color components.
	 * 
	 * @return color panel
	 */
	private JPanel createColorPanel() {
		JPanel colorPanel = new JPanel();
		Color color = line.getColor();
		
		colorPanel.add(new JLabel("Red: "));
		redColorText = initColorTextArea(color.getRed());
		colorPanel.add(redColorText);
		
		colorPanel.add(new JLabel("Green: "));
		greenColorText = initColorTextArea(color.getGreen());
		colorPanel.add(greenColorText);
		
		colorPanel.add(new JLabel("Blue: "));
		blueColorText = initColorTextArea(color.getBlue());
		colorPanel.add(blueColorText);
		
		return colorPanel;
	}
	
	@Override
	public void checkEditing() {
			x1 = parseCoord(x1Text);
			y1 = parseCoord(y1Text);
			x2 = parseCoord(x2Text);
			y2 = parseCoord(y2Text);
			red = parseColor(redColorText);
			green = parseColor(greenColorText);
			blue = parseColor(blueColorText);
	}

	@Override
	public void acceptEditing() {
		line.setxStart(x1);
		line.setyStart(y1);
		line.setxEnd(x2);
		line.setyEnd(y2);
		line.setColor(new Color(red, green, blue));
	}

	
}
