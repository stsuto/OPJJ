package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Interface representing a single document.
 * 
 * @author stipe
 *
 */
public interface SingleDocumentListener {

	/**
	 * Lets all listeners know that the model's status has been updated.
	 * 
	 * @param model
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);
	/**
	 * Lets all listeners know that the model's path has been updated.
	 * 
	 * @param model
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
	
}
