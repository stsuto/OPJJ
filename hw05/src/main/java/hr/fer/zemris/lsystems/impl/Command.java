package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * The <code>Command</code> interface represents a command that is executed when its
 * method {@link #execute(Context, Painter)} is called.
 * 
 * @author stipe
 *
 */
public interface Command {

	/**
	 * Performs an action when this command is executed.
	 * 
	 * @param ctx <code>Context</code> in which the action is performed
	 * @param painter <code>Painter</code> which is used for drawing lines
	 */
	void execute(Context ctx, Painter painter);
	
}
