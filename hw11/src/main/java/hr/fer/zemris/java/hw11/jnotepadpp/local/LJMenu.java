package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JMenu;

/**
 * Decorator class offering a localized {@link JMenu}.
 * 
 * @author stipe
 *
 */
public class LJMenu extends JMenu {

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
	public LJMenu(String key, ILocalizationProvider lp) {
		this.key = key;
		this.lp = lp;
		
		lp.addLocalizationListener(() -> updateMenu());
		updateMenu();
	}
	
	/**
	 * Updates the menu's text.
	 */
	private void updateMenu() {
		setText(lp.getString(key));
	}
	
}
