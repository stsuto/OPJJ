package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Singleton class which sets the language to English by default.
 * Loads the resource bundle for current language and stores a reference to it,
 * offering a method to use the loaded resource to translate requested keys.
 * 
 * @author stipe
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

	/**
	 * Singleton instance of the class.
	 */
	private static final LocalizationProvider instance = new LocalizationProvider();
	/*
	 * Package location of localization files.
	 */
	private static final String packLocation = "hr.fer.zemris.java.hw11.jnotepadpp.local.prijevodi";
	/**
	 * First language which is loaded on start up.
	 */
	private static final String firstLanguage = "en";
	
	/**
	 * Current language.
	 */
	private String language;
	/**
	 * Current resource bundle.
	 */
	private ResourceBundle bundle;
	
	/**
	 * Private constructor making this class a singleton class.
	 */
	private LocalizationProvider() {
		setLanguage(firstLanguage);
	}
	
	/**
	 * Static method used for retrieving the singleton instance of this class.
	 * 
	 * @return provider instance
	 */
	public static LocalizationProvider getInstance() {
		return instance;
	}
	
	/**
	 * Sets the current language to the given language.
	 * 
	 * @param language language to be set
	 */
	public void setLanguage(String language) {
		this.language = language;
		setBundle();
		fire();
	}
	
	/**
	 * Loads the bundle for the current language.
	 */
	private void setBundle() {
		bundle = ResourceBundle.getBundle(packLocation, 
				Locale.forLanguageTag(language));
	}

	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

	@Override
	public String getCurrentLanguage() {
		return language;
	}

}
