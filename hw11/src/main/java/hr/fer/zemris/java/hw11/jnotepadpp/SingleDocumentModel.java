package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * Interface which is an abstract representation of a single document model within the notepad.
 * 
 * @author stipe
 *
 */
public interface SingleDocumentModel {

	/**
	 * Returns the text component tied to this document.
	 * 
	 * @return text component
	 */
	JTextArea getTextComponent();
	/**
	 * Returns this document's file path.
	 * 
	 * @return file path
	 */
	Path getFilePath();
	/**
	 * Sets this document's file path to the given path.
	 * 
	 * @param path to be set
	 */
	void setFilePath(Path path);
	/**
	 * Checks if the document is modified.
	 * 
	 * @return {@code true} if the document is modified,
	 * 		   {@code false} otherwise
	 */
	boolean isModified();
	/**
	 * Sets the modification flag to the given value.
	 * 
	 * @param modified document's modify status
	 */
	void setModified(boolean modified);
	/**
	 * Adds the given listener to this document.
	 * 
	 * @param l listener to be added
	 */
	void addSingleDocumentListener(SingleDocumentListener l);
	/**
	 * Removes the given listener from this document
	 * 
	 * @param l listener to be removed
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);

}
