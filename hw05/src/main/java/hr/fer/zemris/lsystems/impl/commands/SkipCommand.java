package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * The <code>SkipCommand</code> class represents one of the commands the turtle is capable of performing.
 * By executing this command, the turtle travels a certain distance in its current direction.
 * 
 * @author stipe
 *
 */
public class SkipCommand implements Command {

	/**
	 * The step the turtle will move. It is multiplied with the turtle's effective step to
	 * get the distance which the turtle will travel.
	 */
	private double step;
	
	/**
	 * Constructor which accepts the step the turtle should move for.
	 * 
	 * @param step {@link #step} for which the turtle will move
	 */
	public SkipCommand(double step) {
		this.step = step;
	}
	
	/**
	 * The turtle travels by the distance in its direction equal to 
	 * {@link #step} multiplied by the effective step of this turtle.
	 * 
	 * @param ctx <code>Context</code> in which the action is performed
	 * @param painter <code>Painter</code> which is used for drawing lines
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState state = ctx.getCurrentState();
		Vector2D position = state.getPosition();

		Vector2D destination = position.translated(
				// Unit vector pointing towards the destination scaled by the given step and the turtle's step
				state.getDirection().scaled(step * state.getEffectiveStep())
			);

		state.setPosition(destination);
	}
	
}
