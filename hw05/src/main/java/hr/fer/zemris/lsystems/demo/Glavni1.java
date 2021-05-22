package hr.fer.zemris.lsystems.demo;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.commands.LSystemBuilderImpl;

/**
 * This is a demo class which offers a demonstration of classes from this package used
 * for drawing the Koch curve fractal using specified methods for configuration of 
 * the drawing object.
 * 
 * @author stipe
 *
 */
public class Glavni1 {

	public static void main(String[] args) {
		try {
			LSystemViewer.showLSystem(createKochCurve(LSystemBuilderImpl::new));
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * Creates an object specified to draw the Koch curve fractal using various 
	 * methods for specifications and alterations to the drawing object.
	 * 
	 * @param provider the object which by method specifications draw the wanted fractal
	 * @return
	 */
	private static LSystem createKochCurve(LSystemBuilderProvider provider) {
		return provider.createLSystemBuilder()
			.registerCommand('F', "draw 1")
			.registerCommand('+', "rotate 60")
			.registerCommand('-', "rotate -60")
			.setOrigin(0.05, 0.4)
			.setAngle(0)
			.setUnitLength(0.9)
			.setUnitLengthDegreeScaler(1.0/3.0)
			.registerProduction('F', "F+F--F+F")
			.setAxiom("F")
			.build();
	}
}
