package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * The <code>PushCommand</code> class represents one of the commands the turtle is capable of performing.
 * By executing this command, the turtle deletes the latest state, 
 * changing the current state to a previously saved state.
 * 
 * @author stipe
 *
 */
public class PopCommand implements Command {

	/**
	 * The turtle deletes the latest state, changing the current state 
	 * to a previously saved state.
	 * 
	 * @param ctx <code>Context</code> in which the action is performed
	 * @param painter <code>Painter</code> which is used for drawing lines
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.popState();
	}
	
}
