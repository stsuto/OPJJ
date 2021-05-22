package hr.fer.zemris.java.custom.scripting.exec.demo;

import static hr.fer.zemris.java.util.Util.readFromDisk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Demo class used for demonstration of the functionality of the smart script protocols.
 * 
 * @author stipe
 *
 */
public class SmartScriptEngineDemo1 {

	public static void main(String[] args) {
	
		String documentBody = readFromDisk("examples/osnovni.smscr");
		
		Map<String,String> parameters = new HashMap<String, String>();
		Map<String,String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		
		parameters.put("broj", "4");
		
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)
			).execute();
	
	}

}
