package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Colors the background.
 * 
 * @author stipe
 *
 */
public class BgColorWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String color = context.getParameter("bgcolor");

		if (isColorValid(color)) {
			context.setPersistentParameter("bgcolor", color);
			context.setTemporaryParameter("message", "Color is updated.");
		} else {
			context.setTemporaryParameter("message", "Color is not updated.");
		}

		context.getDispatcher().dispatchRequest("/index2.html");
	}

	/**
	 * Checks if the color is valid.
	 * 
	 * @param color color
	 * @return true if valid, false otherwise
	 */
	private boolean isColorValid(String color) {
		if (color == null) return false;
		if (color.length() != 6) return false;
		try {
			Integer.parseInt(color, 16);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}
