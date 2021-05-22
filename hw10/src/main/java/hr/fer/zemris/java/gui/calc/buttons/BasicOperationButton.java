package hr.fer.zemris.java.gui.calc.buttons;

import java.awt.event.ActionListener;

/**
 * Class representing a button with a basic operation.
 * 
 * @author stipe
 *
 */
public class BasicOperationButton extends CalcButton {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param text button text
	 * @param l button listener
	 */
	public BasicOperationButton(String text, ActionListener l) {
		super(text);
		addActionListener(l);
	}

}
