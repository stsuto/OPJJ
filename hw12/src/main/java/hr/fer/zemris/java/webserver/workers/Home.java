package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Creates a html home page.
 * 
 * @author stipe
 *
 */
public class Home implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String value = context.getPersistentParameter("bgcolor");
		
		if (value == null) {
			context.setTemporaryParameter("background", "7F7F7F");
		} else {
			context.setTemporaryParameter("background", value);
		}
		
		context.getDispatcher().dispatchRequest("/private/pages/home.smscr");
	}

}
