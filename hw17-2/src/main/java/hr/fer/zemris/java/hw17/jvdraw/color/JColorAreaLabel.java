package hr.fer.zemris.java.hw17.jvdraw.color;

import java.awt.Color;

import javax.swing.JLabel;

/**
 * This class is a label showing current colors of color providers.
 * 
 * @author stipe
 *
 */
public class JColorAreaLabel extends JLabel implements ColorChangeListener {

	private static final long serialVersionUID = 1L;

	/**
	 * Front color provider.
	 */
	private IColorProvider fgColorProvider;
	/**
	 * Back color provider.
	 */
	private IColorProvider bgColorProvider;	
	
	/**
	 * @param fgColorProvider
	 * @param bgColorProvider
	 */
	public JColorAreaLabel(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		this.fgColorProvider = fgColorProvider;
		this.bgColorProvider = bgColorProvider;
		connectWithListener(fgColorProvider, bgColorProvider);
	}

	/**
	 * Connects this to this objects as a listener.
	 * 
	 * @param providers object to be listening.
	 */
	private void connectWithListener(IColorProvider... providers) {
		for (IColorProvider provider : providers) {
			provider.addColorChangeListener(this);
		}
	}

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
		String fgColor = getColorText(fgColorProvider);
		String bgColor = getColorText(bgColorProvider);
		setText(String.format("Foreground color: %s, background color: %s.", fgColor, bgColor));
	}

	/**
	 * Gets the text representation of the current colors.
	 * 
	 * @param provider
	 * @return
	 */
	private String getColorText(IColorProvider provider) {
		Color col = provider.getCurrentColor();
		return String.format("(%d, %d, %d)", col.getRed(), col.getGreen(), col.getBlue());
	}

}
