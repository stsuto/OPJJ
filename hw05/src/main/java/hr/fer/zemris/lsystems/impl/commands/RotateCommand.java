package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * The <code>RotateCommand</code> class represents one of the commands the turtle is capable of performing.
 * By executing this command, the turtle changes its current direction, rotating it for the given angle 
 * in degrees. The rotation direction is counter-clockwise.
 * 
 * @author stipe
 *
 */
public class RotateCommand implements Command {

	/**
	 * The angle for which the turtle is rotated.
	 */
	private double angle;
	
	/**
	 * Constructor which accepts the angle for which the turtle will be rotated.
	 * 
	 * @param angle {@link #angle} of rotation
	 */
	public RotateCommand(double angle) {
		this.angle = angle;
	}
	
	/**
	 * The turtle rotates for an angle given to this class via constructor.
	 * The rotation is done in math-positive (counter-clockwise) direction.
	 * 
	 * @param ctx <code>Context</code> in which the action is performed
	 * @param painter <code>Painter</code> which is used for drawing lines
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().setDirection(
				ctx.getCurrentState().getDirection().rotated(Math.toRadians(angle))
			);
	}
	
}
