package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

/**
 * Interface representing a model of multiple documents.
 * 
 * @author stipe
 *
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {

	/**
	 * Creates a new, blank document.
	 * 
	 * @return new document
	 */
	SingleDocumentModel createNewDocument();
	/**
	 * Gets the current document.
	 * 
	 * @return current document
	 */
	SingleDocumentModel getCurrentDocument();
	/**
	 * Loads the document from the given path.
	 * 
	 * @param path document's path
	 * @return document on path
	 */
	SingleDocumentModel loadDocument(Path path);
	/**
	 * Saves the given document on the given path.
	 * If the path is null, document is saved on it's current path.
	 * 
	 * @param model document
	 * @param newPath path to which the document should be saved
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);
	/**
	 * Closes the given document.
	 * 
	 * @param model document
	 */
	void closeDocument(SingleDocumentModel model);
	/**
	 * Adds the listener to this model of multiple documents.
	 * 
	 * @param l listener to be added
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);
	/**
	 * Removes the listener from this model of multiple documents.
	 * 
	 * @param l listener to be removed
	 */	
	void removeMultipleDocumentListener(MultipleDocumentListener l);
	/**
	 * Returns the number of documents open in this model.
	 * 
	 * @return number of documents
	 */
	int getNumberOfDocuments();
	/**
	 * Returns the document on the given index.
	 * 
	 * @param index document's index
	 * @return document on index
	 */
	SingleDocumentModel getDocument(int index);
	
}
