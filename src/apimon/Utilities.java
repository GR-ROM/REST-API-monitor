package apimon;

import org.json.JSONException;
import org.json.JSONObject;

public class Utilities {
	
	public static Object getJSONPath(String path, JSONObject json) throws JSONException {
		Object s=null;
		JSONObject js=json;
		String params[]=path.split("\\.");
		for (int c=0;c!=params.length;c++){
			if (js.has(params[c])){
				s=js.get(params[c]);
				if (s.getClass().equals(JSONObject.class)){
					js=(JSONObject)s;
				}
			} else {
				s=null;
			}
		}
		return s;
	}
	
}
