package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Sums two numbers.
 * 
 * @author stipe
 *
 */
public class SumWorker implements IWebWorker {

	/**
	 * First default value.
	 */
	private static final Integer DEFAULT_A = 1;
	/**
	 * Second default value.
	 */
	private static final Integer DEFAULT_B = 2;

	/**
	 * Gets integer value from string or default value if not parsable.
	 * 
	 * @param param
	 * @param defaultValue
	 * @return
	 */
	private int getInteger(String param, Integer defaultValue) {
		try {
			return Integer.parseInt(param);
		} catch (NumberFormatException | NullPointerException e) {
			return defaultValue;			
		}
	}

	@Override
	public void processRequest(RequestContext context) throws Exception {
		int a = getInteger(context.getParameter("a"), DEFAULT_A);
		int b = getInteger(context.getParameter("b"), DEFAULT_B);
		
		int sum = a + b;
		String sumString = Integer.toString(sum);
		
		context.setTemporaryParameter("zbroj", sumString);
		context.setTemporaryParameter("varA", String.valueOf(a));
		context.setTemporaryParameter("varB", String.valueOf(b));
		
		String evenPic = "images/giphy.gif";
		String oddPic = "images/boop.gif";
		
		String image = sum % 2 == 0 ? evenPic : oddPic;
		context.setTemporaryParameter("imgName", image);
		
		context.getDispatcher().dispatchRequest("/private/pages/calc.smscr");	
	}

}
