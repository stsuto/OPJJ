package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which is an abstract representation of a localization 
 * provider offering listener handling methods.
 * 
 * @author stipe
 *
 */
public abstract class AbstractLocalizationProvider 
		implements ILocalizationProvider {

	/**
	 * Listeners.
	 */
	List<ILocalizationListener> listeners;
	
	/**
	 * Constructor.
	 */
	public AbstractLocalizationProvider() {
		listeners = new ArrayList<>();
	}
	
	@Override
	public void addLocalizationListener(ILocalizationListener l) {
		listeners.add(l);
	}
	
	@Override
	public void removeLocalizationListener(ILocalizationListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Notifies all listeners that the localization has changed.
	 */
	public void fire() {
		listeners.forEach(l -> l.localizationChanged());
	}
	
}
