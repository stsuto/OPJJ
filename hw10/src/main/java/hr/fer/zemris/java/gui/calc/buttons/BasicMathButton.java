package hr.fer.zemris.java.gui.calc.buttons;

import static hr.fer.zemris.java.gui.calc.util.ListenerUtil.createBinaryAction;
import static hr.fer.zemris.java.gui.calc.util.ListenerUtil.createUnaryAction;

import java.awt.event.ActionListener;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

/**
 * Class representing a button with a basic math operation.
 * 
 * @author stipe
 *
 */
public class BasicMathButton extends CalcButton {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for a button with a unary operation.
	 * 
	 * @param name button name
	 * @param op operation
	 * @param model model
	 */
	public BasicMathButton(String name, DoubleUnaryOperator op, CalcModel model) {
		this(name, createUnaryAction(op, model));
	}
	
	/**
	 * Constructor for a button with a binary operation.
	 * 
	 * @param name button name
	 * @param op operation
	 * @param model model
	 */
	public BasicMathButton(String name, DoubleBinaryOperator op, CalcModel model) {
		this(name, createBinaryAction(op, model));
	}
	
	/**
	 * Constructor.
	 * 
	 * @param name button name
	 * @param action button action
	 */
	private BasicMathButton(String name, ActionListener action) {
		super(name);
		addActionListener(action);
	}

}
