package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Interface which is an abstract representation of a multiple document listener.
 * 
 * @author stipe
 *
 */
public interface MultipleDocumentListener {
	/**
	 * Listeners action to the current document being changed.
	 * 
	 * @param previousModel previous document
	 * @param currentModel current document
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);
	/**
	 * Listener's action to a new document being added.
	 * 
	 * @param model added document
	 */
	void documentAdded(SingleDocumentModel model);
	/**
	 * Listener's action to a document being removed.
	 * 
	 * @param model removed document
	 */	
	void documentRemoved(SingleDocumentModel model);
	
}
