package hr.fer.zemris.java.hw17.jvdraw.geometry.editor;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import hr.fer.zemris.java.hw17.jvdraw.geometry.model.FilledCircle;

/**
 * This class represents a filled circle editor.s
 * 
 * @author stipe
 *
 */
public class FilledCircleEditor extends AbstractCircleEditor {

	private static final long serialVersionUID = 1L;

	/**
	 * Text area for the red component of the circle's background color.
	 */
	private JTextArea redBgColorText;
	/**
	 * Text area for the green component of the circle's background color.
	 */
	private JTextArea greenBgColorText;
	/**
	 * Text area for the blue component of the circle's background color.
	 */
	private JTextArea blueBgColorText;
	/**
	 * Red component of the circle's background color.
	 */
	private int redBg;
	/**
	 * Green component of the circle's background color.
	 */
	private int greenBg;
	/**
	 * Blue component of the circle's background color.
	 */
	private int blueBg;
	
	/**
	 * Constructor
	 * 
	 * @param circle {@link AbstractCircleEditor#circle}
	 */
	public FilledCircleEditor(FilledCircle circle) {
		super(circle);
		initEditor();
	}

	/**
	 * Initializes the editor components.
	 */
	private void initEditor() {
		setLayout(new GridLayout(0, 1));
		
		add(createStartPanel());
		add(createRadiusPanel());
		add(createColorPanel());
		add(createBgColorPanel());
	}

	/**
	 * Creates a panel for background color components.
	 *  
	 * @return background color components
	 */
	private JPanel createBgColorPanel() {
		JPanel bgColorPanel = new JPanel();
		Color color = ((FilledCircle) circle).getBgColor();
		
		bgColorPanel.add(new JLabel("BG color:"));
		
		bgColorPanel.add(new JLabel("Red: "));
		redBgColorText = initColorTextArea(color.getRed());
		bgColorPanel.add(redBgColorText);
		
		bgColorPanel.add(new JLabel("Green: "));
		greenBgColorText = initColorTextArea(color.getGreen());
		bgColorPanel.add(greenBgColorText);
		
		bgColorPanel.add(new JLabel("Blue: "));
		blueBgColorText = initColorTextArea(color.getBlue());
		bgColorPanel.add(blueBgColorText);
		
		return bgColorPanel;
	}

	@Override
	public void checkEditing() {
		x1 = parseCoord(x1Text);
		y1 = parseCoord(y1Text);
		radius = parseRadius(radiusText);
		red = parseColor(redColorText);
		green = parseColor(greenColorText);
		blue = parseColor(blueColorText);
		redBg = parseColor(redBgColorText);
		greenBg = parseColor(greenBgColorText);
		blueBg = parseColor(blueBgColorText);
	}

	@Override
	public void acceptEditing() {
		circle.setxCenter(x1);
		circle.setyCenter(y1);
		circle.setRadius(radius);
		circle.setFgColor(new Color(red, green, blue));
		((FilledCircle) circle).setBgColor(new Color(redBg, greenBg, blueBg));
	}

}
