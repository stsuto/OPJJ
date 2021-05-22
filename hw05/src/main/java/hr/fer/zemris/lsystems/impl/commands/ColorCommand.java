package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * The <code>ColorCommand</code> class represents one of the commands the turtle is capable of performing.
 * By executing this command, the turtle changes the color of its trail.
 * 
 * @author stipe
 *
 */
public class ColorCommand implements Command {

	/**
	 * The color of the turtle's trail.
	 */
	private Color color;
	
	/**
	 * Constructor which creates a new <code>ColorCommand</code> object using the given parameter.
	 *
	 * @param color turtle's new {@link #color}
	 */
	public ColorCommand(Color color) {
		this.color = color;
	}

	/**
	 * Changes the color of the turtle's trail to the color given to this <code>ColorCommand</code>
	 * via constructor.
	 * 
	 * @param ctx <code>Context</code> in which the action is performed
	 * @param painter <code>Painter</code> which is used for drawing lines
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().setColor(color);
	}
	
}
