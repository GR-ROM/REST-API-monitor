package apimon;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;


public class main {

	public static void saveMetrics(String fname, APIMethods methods) {

		JSONObject jso=null;
		JSONObject jsub=null;
		JSONObject tmp=null;
		try {
			FileWriter fw=new FileWriter(fname);
			jso=new JSONObject();
			for (APIMethod m: methods.getMethods()) {
				jsub=new JSONObject();
				tmp=new JSONObject();
				for (String k: m.getResponseMap().keySet()) {
					tmp.put(k, m.getResponseMap().get(k).get());	
				}
				jsub.put("response", tmp);
				jso.put(m.getName(), jsub);
			}
			
			fw.write(jso.toString());
			fw.flush();
			fw.close();
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
	public static void main(String[] args) {
		WebClient client=new WebClient();
		
		APIMethods methods=new APIMethods();
		Map<String, APIValue> vars=new HashMap<String, APIValue>();
		Logger.init("webagent.log");
		client.setVariables(vars);
		methods.loadFromFile("test.json");
		Logger.log("START!", Logger.INFO);
		
		for (APIMethod m: methods.getMethods()) {
			client.checkMethod(m);
			Logger.log("Type: "+m.getType()+" Path: "+m.getUrl(), Logger.DEBUG);
		}

		saveMetrics("out.json", methods);
		Logger.log("DONE!", Logger.INFO);
		
	}
}