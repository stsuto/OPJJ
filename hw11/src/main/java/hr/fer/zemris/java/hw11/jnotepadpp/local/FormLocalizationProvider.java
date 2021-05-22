package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/**
 * Class derived from {@link LocalizationProviderBridge} which registers 
 * itself as a {@link WindowListener} to its {@link JFrame}.
 * 
 * @author stipe
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Constructor which adds a new listener to the given frame.
	 * 
	 * @param parent
	 * @param frame
	 */
	public FormLocalizationProvider(ILocalizationProvider parent, JFrame frame) {
		super(parent);
		addFrameListener(frame);
	}

	/**
	 * Adds a new listener to the given frame.
	 * 
	 * @param frame frame to which the listener is set
	 */
	private void addFrameListener(JFrame frame) {
		frame.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				disconnect();
			}
			
		});
	}
	
}
