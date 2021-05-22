package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Interface which is an abstract representation of a localization provider.
 * 
 * @author stipe
 *
 */
public interface ILocalizationProvider {

	/**
	 * Adds a new localization listener.
	 * 
	 * @param l listener
	 */
	void addLocalizationListener(ILocalizationListener l);
	/**
	 * Removes the given localization listener.
	 * 
	 * @param l listener
	 */
	void removeLocalizationListener(ILocalizationListener l);
	/**
	 * Gets the localized value behind the given key.
	 * 
	 * @param key key of localized value
	 * @return localized value mapped to key
	 */
	String getString(String key);
	/**
	 * Returns the current language of the localization provider.
	 * 
	 * @return current language
	 */
	String getCurrentLanguage();
}
