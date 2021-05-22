package hr.fer.zemris.java.webserver;

/**
 * Interface which defines a web worker capable of processing requests.
 * 
 * @author stipe
 *
 */
public interface IWebWorker {
	
	/**
	 * Starts the processing of the request.
	 * 
	 * @param context context of the process
	 * @throws Exception in case of an I/O error
	 */
	public void processRequest(RequestContext context) throws Exception;
}