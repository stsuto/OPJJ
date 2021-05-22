package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * The <code>ScaleCommand</code> class represents one of the commands the turtle is capable of performing.
 * By executing this command, the turtle changes its effectiveStep value via the given scalar.
 * 
 * @author stipe
 *
 */
public class ScaleCommand implements Command {

	/**
	 * The scalar with which the turtle's effective step is multiplied with.
	 */
	private double factor;
	
	/**
	 * Constructor which accepts the multiplying factor.
	 * 
	 * @param factor {@link #factor} that multiplies the effective step
	 */
	public ScaleCommand(double factor) {
		this.factor = factor;
	}
	
	/**
	 * Changes the turtle's effective step by multiplying the current one with the factor given to 
	 * this <code>ColorCommand</code> via constructor.
	 * 
	 * @param ctx <code>Context</code> in which the action is performed
	 * @param painter <code>Painter</code> which is used for drawing lines
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().setEffectiveStep(
				ctx.getCurrentState().getEffectiveStep() * factor
			);
	}
	
}
