package hr.fer.zemris.java.gui.calc.buttons;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

/**
 * Class representing a calculator button.
 * 
 * @author stipe
 *
 */
public class CalcButton extends JButton {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param text button text
	 */
	public CalcButton(String text) {
		super(text);
		setBackground(new Color(153, 181, 204));
		setFont(new Font("Comic Sans", Font.PLAIN, 15));
	}
	
}
