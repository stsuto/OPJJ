package hr.fer.zemris.lsystems.demo;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.commands.LSystemBuilderImpl;

/**
 * This is a demo class which offers a demonstration of classes from this package used
 * for drawing the Koch curve fractal using configuration from a single String text array
 * for the creation of the drawing object.
 * 
 * @author stipe
 *
 */
public class Glavni2 {

	public static void main(String[] args) {
		try {
			LSystemViewer.showLSystem(createKochCurve2(LSystemBuilderImpl::new));
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * Creates an object specified to draw the Koch curve fractal by configuration
	 * from text.
	 * 
	 * @param provider the object which by method specifications draw the wanted fractal
	 * @return
	 */
	private static LSystem createKochCurve2(LSystemBuilderProvider provider) {
		String[] data = new String[] {
			"origin 0.05 0.4",
			"angle 0",
			"unitLength 0.9",
			"unitLengthDegreeScaler 1.0 / 3.0",
			"",
			"command F draw 1",
			"command + rotate 60",
			"command - rotate -60",
			"",
			"axiom F",
			"",
			"production F F+F--F+F"
		};
		
		return provider.createLSystemBuilder().configureFromText(data).build();
	}
	
}
