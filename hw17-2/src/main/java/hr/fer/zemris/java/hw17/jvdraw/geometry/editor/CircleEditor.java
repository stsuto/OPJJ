package hr.fer.zemris.java.hw17.jvdraw.geometry.editor;

import java.awt.Color;
import java.awt.GridLayout;

import hr.fer.zemris.java.hw17.jvdraw.geometry.model.Circle;

/**
 * This class represents a circle editor.
 * 
 * @author stipe
 *
 */
public class CircleEditor extends AbstractCircleEditor {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param circle {@link AbstractCircleEditor#circle}
	 */
	public CircleEditor(Circle circle) {
		super(circle);
		initEditor();
	}
	
	/**
	 * Initialzies editor components.
	 */
	private void initEditor() {
		setLayout(new GridLayout(0, 1));
		
		add(createStartPanel());
		add(createRadiusPanel());
		add(createColorPanel());
	}


	
	@Override
	public void checkEditing() {
		x1 = parseCoord(x1Text);
		y1 = parseCoord(y1Text);
		radius = parseRadius(radiusText);
		red = parseColor(redColorText);
		green = parseColor(greenColorText);
		blue = parseColor(blueColorText);
	}

	@Override
	public void acceptEditing() {
		circle.setxCenter(x1);
		circle.setyCenter(y1);
		circle.setRadius(radius);
		circle.setFgColor(new Color(red, green, blue));
	}

}
