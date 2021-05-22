package hr.fer.zemris.java.webserver;

/**
 * Interface which defines an apstract object capable of 
 * processing a request.
 * 
 * @author stipe
 *
 */
public interface IDispatcher {
	
	/**
	 * Starts the processing of the given url path.
	 * 
	 * @param urlPath url path
	 * @throws Exception in case of an I/O error
	 */
	void dispatchRequest(String urlPath) throws Exception;
}