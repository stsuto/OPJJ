package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Echoes the context parameters formatting them in a table.
 * 
 * @author stipe
 *
 */
public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		StringBuilder sb = new StringBuilder();

		sb.append("<html>");
		sb.append("<head><title>Parameters</title></head>");

		sb.append("<body><table><thead><tr>");
		sb.append("<th>Parameter name</th>");
		sb.append("<th>Parameter value</th>");
		sb.append("</tr></thead><tbody>");

		for (String param : context.getParameterNames()) {
			sb.append("<tr><td>");
			sb.append(param);
			sb.append("</td><td>");
			sb.append(context.getParameter(param));
			sb.append("</td>");
		}
		
		sb.append("</tbody></table></body>\n");
		sb.append("</html>");

		context.write(sb.toString());
	}

}
