package coloring.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import hr.fer.zemris.java.util.Util;
import marcupic.opjj.statespace.coloring.Picture;

/**
 * Class {@code Coloring} represents an action of coloring within the lines.
 * Contains methods that define all necessary actions for coloring.
 * 
 * @author stipe
 *
 */
public class Coloring implements Supplier<Pixel>, Consumer<Pixel>, Function<Pixel, List<Pixel>>, Predicate<Pixel> {

	/**
	 * Reference pixel.
	 */
	private Pixel reference;
	/**
	 * Picture on which the coloring is done.
	 */
	private Picture picture;
	/**
	 * Color with which the chosen pixels are to be colored with.
	 */
	private int fillColor;
	/**
	 * Color of the reference pixel.
	 */
	private int refColor;
	
	/**
	 * Constructor that initializes the properties with the given parameters.
	 * 
	 * @param reference {@link #reference}
	 * @param picture {@link #picture}
	 * @param fillColor {@link #fillColor}
	 */
	public Coloring(Pixel reference, Picture picture, int fillColor) {
		this.reference = Util.requireNonNull(reference, "Pixel");
		this.picture = Util.requireNonNull(picture, "Picture");
		this.fillColor = Util.requireNonNull(fillColor, "Fill color");
		this.refColor = picture.getPixelColor(reference.x, reference.y);
	}
	
	/**
	 * {@inheritDoc} <p>
	 * In this implementation, checks if the given Pixel is of the 
	 * same color as the reference pixel.
	 * 
	 * @param si Pixel to potentially be colored
	 * @return {@code true} if the pixel is of the same color as the 
	 * reference, and {@code false} otherwise
	 * @throws NullPointerException if the given pixel is null
	 */
	@Override
	public boolean test(Pixel si) {
		Util.requireNonNull(si, "Pixel");
		return picture.getPixelColor(si.x, si.y) == refColor;
	}

	/**
	 * {@inheritDoc} <p>
	 * Adds the surrounding pixel to pixels to be potentially colored.
	 * 
	 * @param si Pixel to potentially be colored
	 * @return list of surrounding Pixels that could be colored
	 * @throws NullPointerException if the given pixel is null
	 */
	@Override
	public List<Pixel> apply(Pixel si) {
		Util.requireNonNull(si, "Pixel");
		List<Pixel> succ = new ArrayList<>();
		
		if (si.x - 1 >= 0) {
			succ.add(new Pixel(si.x - 1, si.y));
		}
		if (si.x + 1 >= 0) {
			succ.add(new Pixel(si.x + 1, si.y));
		}
		if (si.y - 1 >= 0) {
			succ.add(new Pixel(si.x, si.y - 1));
		}
		if (si.y + 1 >= 0) {
			succ.add(new Pixel(si.x, si.y + 1));
		}
		
		return succ;
	}

	/**
	 * {@inheritDoc} <p>
	 * Colors the given pixel.
	 * 
	 * @param pixel to be colored
	 * @throws NullPointerException if the given pixel is null
	 */
	@Override
	public void accept(Pixel si) {
		Util.requireNonNull(si, "Pixel");
		picture.setPixelColor(si.x, si.y, fillColor);
	}

	/**
	 * Returns the reference pixel.
	 * 
	 * @return {@link #reference}
	 */
	@Override
	public Pixel get() {
		return reference;
	}
	
	
}
