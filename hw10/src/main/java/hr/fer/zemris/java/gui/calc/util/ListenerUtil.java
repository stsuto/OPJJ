package hr.fer.zemris.java.gui.calc.util;

import java.awt.event.ActionListener;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

/**
 * Utility class used for creating calculator actions.
 * 
 * @author stipe
 *
 */
public class ListenerUtil {

	/**
	 * Creates an action listener which performs the given unary operation with the given model.
	 * 
	 * @param op operation
	 * @param model model
	 * @return action listener
	 */
	public static ActionListener createUnaryAction(DoubleUnaryOperator op, CalcModel model) {
		return e -> model.setValue(op.applyAsDouble(model.getValue()));
	}

	/**
	 * Creates an action listener which performs the given binary operation with the given model.
	 * 
	 * @param op operation
	 * @param model model
	 * @return action listener
	 */
	public static ActionListener createBinaryAction(DoubleBinaryOperator op, CalcModel model) {
		return e -> {
			if (model.getPendingBinaryOperation() != null) {
				model.setValue(
					model.getPendingBinaryOperation()
						.applyAsDouble(model.getActiveOperand(), 
										model.getValue()
						)
				);
			}
			model.setActiveOperand(model.getValue());
			model.setPendingBinaryOperation(op);
		};
	}

}
