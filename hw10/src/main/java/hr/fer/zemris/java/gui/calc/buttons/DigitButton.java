package hr.fer.zemris.java.gui.calc.buttons;

import java.awt.Font;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

/**
 * Class representing a calculator digit button.
 * 
 * @author stipe
 *
 */
public class DigitButton extends CalcButton {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param digit digit to be added
	 * @param model model
	 */
	public DigitButton(int digit, CalcModel model) {
		super(Integer.toString(digit));		
		addActionListener(e -> model.insertDigit(digit));
		setFont(getFont().deriveFont(Font.BOLD, 30f));
	}
	
}
