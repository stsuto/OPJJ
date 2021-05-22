package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Class which represents an action whose properties (i.e. name and short description) 
 * are localized to the current language.
 * 
 * @author stipe
 *
 */
public abstract class LocalizableAction extends AbstractAction {

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
	public LocalizableAction(String key, ILocalizationProvider lp) {
		this.key = key;
		this.lp = lp;
		
		lp.addLocalizationListener(() -> updateAction());
		updateAction();
	}

	/**
	 * Updates the action's properties.
	 */
	private void updateAction() {
		putValue(NAME, lp.getString(key));
		putValue(Action.SHORT_DESCRIPTION, lp.getString(key + "Description"));
	}
	
}
