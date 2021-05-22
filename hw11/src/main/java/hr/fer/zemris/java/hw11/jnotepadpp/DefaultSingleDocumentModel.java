package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Class which represents a single document.
 * 
 * @author stipe
 *
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
	
	/**
	 * Document's path.
	 */
	private Path path;
	/**
	 * Document's text area.
	 */
	private JTextArea textArea;
	/**
	 * Modification flag.
	 */
	private boolean modified;
	/**
	 * This document's isteners.
	 */
	private List<SingleDocumentListener> listeners;
	
	/**
	 * Constructor which initializes the fields and adds a listener to the document.
	 * 
	 * @param path document's path
	 * @param text document's text
	 */
	public DefaultSingleDocumentModel(Path path, String text) {
		this.path = path;
		// Using "this" for clarity.
		this.textArea = new JTextArea(text);
		this.modified = false;
		this.listeners = new ArrayList<>();
		
		addTextAreaListener();
	}

	/**
	 * Adds a listener to this document.
	 */
	private void addTextAreaListener() {
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				setModified(true);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				setModified(true);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				setModified(true);
			}
		});
	}

	@Override
	public JTextArea getTextComponent() {
		return textArea;
	}

	@Override
	public Path getFilePath() {
		return path;
	}

	@Override
	public void setFilePath(Path path) {
		this.path = path;
		notifyFilePathListeners();
	}

	/**
	 * Notifies all listeners that the document's path has been changed.
	 */
	private void notifyFilePathListeners() {
		listeners.forEach(l -> l.documentFilePathUpdated(this));
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean modified) {
		this.modified = modified;
		notifyModifyStatusListeners();
	}
	
	/**
	 * Notifies all listeners that the document's modification status has been changed.
	 */
	private void notifyModifyStatusListeners() {
		listeners.forEach(l -> l.documentModifyStatusUpdated(this));
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
	}

}
