package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * The <code>PushCommand</code> class represents one of the commands the turtle is capable of performing.
 * By executing this command, the turtle saves its current state, enabling position and state restoration.
 * 
 * @author stipe
 *
 */
public class PushCommand implements Command {

	/**
	 * The turtle saves its current state by pushing it onto the context stack.
	 * Saving states enables later restoring positions from previous states.
	 * 
	 * @param ctx <code>Context</code> in which the action is performed
	 * @param painter <code>Painter</code> which is used for drawing lines
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.pushState(ctx.getCurrentState().copy()); // is copy needed?
	}
	
}
