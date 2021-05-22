package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JLabel;

/**
 * Decorator class offering a localized {@link JLabel}.
 * 
 * @author stipe
 *
 */
public class LJLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	/**
	 * Key of the localized value.
	 */
	private String key;
	/**
	 * Localization provider.
	 */
	private ILocalizationProvider lp;
	
	/**
	 * Constructor which initializes the fields and adds a listener.
	 * 
	 * @param key {@link #key}
	 * @param lp {@link #lp}
	 */
	public LJLabel(String key, ILocalizationProvider lp) {
		this.key = key;
		this.lp = lp;
		
		lp.addLocalizationListener(() -> updateLabel());
		updateLabel();
	}
	
	/**
	 * Updates the label's text.
	 */
	private void updateLabel() {
		setText(lp.getString(key));
	}
}
