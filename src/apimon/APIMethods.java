package apimon;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class APIMethods implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5530644288748967917L;
	private ArrayList<APIMethod> methods ;

	public APIMethods() {
		methods=new ArrayList<APIMethod>(); 
	}
	
	public void saveToFile(String file) {
	/*	boolean state=false;
		try (OutputStream os = new FileOutputStream(file, true);
	        BufferedOutputStream bos = new BufferedOutputStream(os, 10_000);
	        ObjectOutputStream oos = new ObjectOutputStream(bos)) {
			oos.writeObject(methods);
	        oos.flush();
	        state=true;
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		//return state;*/
		
		JSONObject jso=null;
		JSONArray jsa=new JSONArray();
		JSONObject tmp=null;
		try {
			FileWriter fw=new FileWriter(file);
			for (APIMethod m: methods) {
				jso=new JSONObject();
				jso.put("name", m.getName());
				jso.put("path", m.getUrl());
				tmp=new JSONObject();
				for (int j=0;j!=m.getHeaders().size();j++){
					tmp.put(m.getHeaders().get(j).getKey(), m.getHeaders().get(j).getValue());
				}
				jso.put("headers", tmp);
				jso.put("type", m.getType());
				jso.put("responseValueMap", m.getResponseMap());
				jso.put("payload", m.getPayload());
				jsa.put(jso);
			}
			
			fw.write(jsa.toString());
			fw.flush();
			fw.close();
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	@SuppressWarnings("unchecked")
	public void loadFromFile(String file) {
	/*	boolean state=false;
		try (InputStream is = new FileInputStream(file);
	             BufferedInputStream bis = new BufferedInputStream(is, 100_000);
	             ObjectInputStream ois = new ObjectInputStream(bis)) {
			try {
				methods=(ArrayList<APIMethod>) ois.readObject();
				state=true;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//return state;
		JSONObject jso=null;
		JSONArray jsa=null;
		JSONObject tmp=null;
		try {
			String line = new String(Files.readAllBytes(Paths.get(file)));
			jsa=new JSONArray(line);
			for (int i=0;i!=jsa.length();i++) {
				jso=jsa.getJSONObject(i);
				if (!jso.has("name")) break;
				
				APIMethod m=new APIMethod(jso.getString("name"), jso.getString("path"));
				if (!jso.has("type")) m.setType("GET"); else m.setType(jso.getString("type"));
				if (jso.has("headers")){
					tmp=(JSONObject) jso.get("headers");
					Iterator<String> keys = tmp.keys();

					while(keys.hasNext()) {
						String key = keys.next();
						m.getHeaders().add(new HttpHeader(key, tmp.getString(key)));
					}
				}
				tmp=(JSONObject) jso.get("responseValueMap");
				Iterator<String> keys = tmp.keys();
				while(keys.hasNext()) {
				    String key = keys.next();
				    
				    if (tmp.get(key).getClass().equals(Integer.class)) {
				    	m.getResponseMap().put(key, new APIValue(tmp.getInt(key)));
			    	}
			    	if (tmp.get(key).getClass().equals(String.class)) {
			    		m.getResponseMap().put(key, new APIValue(tmp.getString(key)));
			    	}
				}
				if (jso.has("payload")){
				tmp=(JSONObject) jso.get("payload");
					keys = tmp.keys();
					while(keys.hasNext()) {
				    	String key = keys.next();
				    	
				    	if (tmp.get(key).getClass().equals(Integer.class)) {
				    		m.getPayload().put(key, new APIValue(tmp.getInt(key)));
				    	}
				    	if (tmp.get(key).getClass().equals(String.class)) {
				    		m.getPayload().put(key, new APIValue(tmp.getString(key)));
				    	}
					}				
				}
				methods.add(m);
			}
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<APIMethod> getMethods() {
		return methods;
	}

	public void setMethods(ArrayList<APIMethod> methods) {
		this.methods = methods;
	}
	
}
