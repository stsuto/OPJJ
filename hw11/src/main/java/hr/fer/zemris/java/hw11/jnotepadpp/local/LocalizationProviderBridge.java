package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Decorator class for some other {@link ILocalizationProvider}.
 * Offers two additional methods with which it manages a connection status.
 * 
 * @author stipe
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	/**
	 * Decorated provider.
	 */
	private ILocalizationProvider parent;
	/**
	 * Listener.
	 */
	private ILocalizationListener listener;
	/**
	 * Connection status.
	 */
	private boolean connected;
	
	/**
	 * Constructor.
	 * 
	 * @param parent provider to be decorated
	 */
	public LocalizationProviderBridge(ILocalizationProvider parent) {
		this.parent = parent;
		this.listener = () -> fire();
	}
	
	/**
	 * Disconnects if not already disconnected.
	 */
	public void disconnect() {
		if (!connected) {
			return;
		}
		parent.removeLocalizationListener(listener);
		connected = false;
	}
	
	/**
	 * Connects if not already connected.
	 */
	public void connect() {
		if (connected) {
			return;
		}
		parent.addLocalizationListener(listener);
		connected = true;
	}
	
	@Override
	public String getString(String key) {
		return parent.getString(key);
	}

	@Override
	public String getCurrentLanguage() {
		return parent.getCurrentLanguage();
	}

}
