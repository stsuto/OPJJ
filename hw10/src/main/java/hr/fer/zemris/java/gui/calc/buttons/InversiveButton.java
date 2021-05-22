package hr.fer.zemris.java.gui.calc.buttons;

import static hr.fer.zemris.java.gui.calc.util.ListenerUtil.*;

import java.awt.event.ActionListener;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

/**
 * Class representing an inversive calculator button.
 * 
 * @author stipe
 *
 */
public class InversiveButton extends CalcButton {

	private static final long serialVersionUID = 1L;

	/**
	 * Flag showing whether or not the button is inverted.
	 */
	private boolean isInverted;
	/**
	 * Original text.
	 */
	private String ogName;
	/**
	 * Inverted name.
	 */
	private String invName;
	/**
	 * Original action.
	 */
	private ActionListener ogAction;
	/**
	 * Inverted action.
	 */
	private ActionListener invAction;	
	
	/**
	 * Constructor.
	 * 
	 * @param ogName original name
	 * @param ogOp original oepration
	 * @param invName inverted name
	 * @param invOp inverted operation
	 * @param model model
	 */
	public InversiveButton(String ogName, DoubleUnaryOperator ogOp, 
			String invName, DoubleUnaryOperator invOp, CalcModel model) {
		
		this(ogName, createUnaryAction(ogOp, model), 
				invName, createUnaryAction(invOp, model), model);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param ogName original name
	 * @param ogOp original oepration
	 * @param invName inverted name
	 * @param invOp inverted operation
	 * @param model model
	 */
	public InversiveButton(String ogName, DoubleBinaryOperator ogOp, 
			String invName, DoubleBinaryOperator invOp, CalcModel model) {
		
		this(ogName, createBinaryAction(ogOp, model), 
				invName, createBinaryAction(invOp, model), model);
	}

	/**
	 * Constructor.
	 * 
	 * @param ogName original name
	 * @param ogOp original oepration
	 * @param invName inverted name
	 * @param invOp inverted operation
	 * @param model model
	 */
	private InversiveButton(String ogName, ActionListener ogAction, 
			String invName, ActionListener invAction, CalcModel model) {
		super(ogName);
		this.ogName = ogName;
		this.invName = invName;
		this.ogAction = ogAction;
		this.invAction = invAction;
		addActionListener(ogAction);
	}

	/**
	 * Used for inverting buttons.
	 */
	public void invertButton() {
		if (isInverted) {
			setText(ogName);
			removeActionListener(invAction);
			addActionListener(ogAction);
		} else {
			setText(invName);
			removeActionListener(ogAction);
			addActionListener(invAction);
		}
		isInverted = !isInverted;
	}
	
}
