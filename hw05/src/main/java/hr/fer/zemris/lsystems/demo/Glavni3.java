package hr.fer.zemris.lsystems.demo;

import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.commands.LSystemBuilderImpl;

/**
 * This is a demo class which offers a demonstration of classes from this package used
 * for drawing various fractals. The user can choose which fractal is to be drawn and
 * the depth of the fractal.
 * 
 * @author stipe
 *
 */
public class Glavni3 {

	public static void main(String[] args) {
		LSystemViewer.showLSystem(LSystemBuilderImpl::new);
	}
	
}
