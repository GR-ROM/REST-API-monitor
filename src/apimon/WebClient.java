package apimon;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class WebClient {
	
	private int last_status;
	private String useragent="ZBX WEB AGENT/v0.1";
	private Map<String, Object> status;
	private Map<String, APIValue> variables;
	
	public WebClient(){
		last_status=0;
		status=new HashMap<String, Object>();
	}
	
	public Map<String, Object> GetStatus(){
		return status;
	}
		
	public int get_last_status(){
		return last_status;
	}
	
	private String executeRequest(String url, List<HttpHeader> headers){
		HttpURLConnection connection;
		String line="";
		try {
			URL curl = new URL(replaceVars(url));
			connection = (HttpURLConnection)curl.openConnection();
			connection.setDoOutput(true);
			headers.add(new HttpHeader("cache-control", "no-cache"));
			headers.add(new HttpHeader("user-agent", this.useragent));


			for(HttpHeader header: headers) {
				String s=replaceVars(header.getValue());
				connection.setRequestProperty(header.getKey(), s);
			}
				
			connection.setUseCaches(false);
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			connection.setDoOutput(true);
			last_status=connection.getResponseCode();
			if (this.last_status==200) {
				try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
					String s=null;
					do {
						s=in.readLine();
						line+=s;
					} while(s!=null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.last_status=0;
		}
		return line;
	}
	
	private String executeRequest(String url, List<HttpHeader> headers, String data)
	{
		HttpURLConnection connection;
		DataOutputStream wr = null;
		String line="";
		try {
			URL curl = new URL(url);
			connection = (HttpURLConnection)curl.openConnection();
			headers.add(new HttpHeader("cache-control", "no-cache"));
			headers.add(new HttpHeader("user-agent", this.useragent));
			
			for(HttpHeader header: headers)
				connection.setRequestProperty(header.getKey(), replaceVars(header.getValue()));

			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			connection.setDoOutput(true);
			connection.getOutputStream();
			
			//Send request
            try {
            	wr = new DataOutputStream (connection.getOutputStream());
                wr.writeBytes(data);
            }
            catch (Exception e) {
            	e.printStackTrace();
            }
            finally {
            	wr.close();
            }
			last_status=connection.getResponseCode();
			if (this.last_status==200) {
				try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
					String s=null;
					do {
						s=in.readLine();
						line+=s;
					} while(s!=null);
					in.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.last_status=0;
		}
	/*	
		FileWriter fw;
		try {
			fw = new FileWriter("request.txt");
			fw.write(line);
			fw.flush();
			fw.close();		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		return line;
	}
	
	private String replaceVars(String input) {
		int spos=0;
		int epos=0;
		String s=null;
		String before=null;
		String after=null;
		do {
			spos=input.indexOf("$<");
			epos=input.indexOf('>');
			if ((spos!=-1) && (epos>spos)) {
				s=input.substring(spos+2, epos);
				s=variables.get(s).toString();
				before=input.substring(0, spos);
				after=input.substring(epos+1, input.length());
				input=before+s+after;
			} else break;
		} while(spos!=-1);
		return input;
	}
	
	private String extractVarName(String input) {
		int spos=0;
		int epos=0;
		String s=null;
		do {
			spos=input.indexOf("$<");
			epos=input.indexOf('>');
			if ((spos!=-1) && (epos>spos)) {
				s=input.substring(spos+2, epos);
				input=s;
			} else break;
		} while(spos!=-1);
		return input;
	}
	
	public void checkMethod(APIMethod method) {
		method.addHeader("Content-Type", "application/json");
		method.addHeader("Accept", "application/json");
		JSONObject json = null;
		String line=null;
		int now = 0;
		
		if (method.getName().equalsIgnoreCase("auchan_pay_get_balance")){
			long start=System.currentTimeMillis();
		}
	
		if (method.getType().equals("GET")){
			long start=System.currentTimeMillis();
			line=this.executeRequest(method.getUrl(), method.getHeaders());
			now=(int) (System.currentTimeMillis()-start);

		} else if (method.getType().equals("POST")) {
			JSONObject jpayload=null;
			try{
				jpayload=new JSONObject();
				for (String s: method.getPayload().keySet()) {
					if (method.getPayload().get(s).get().getClass().equals(String.class)) {
						jpayload.put(s, replaceVars((String)method.getPayload().get(s).get())); } 
					else 
					jpayload.put(s, method.getPayload().get(s).get());
				}
			}
			catch(Exception e){
				
			}
			long startTime = System.currentTimeMillis();
			line=this.executeRequest(method.getUrl(), method.getHeaders(), jpayload.toString());
			now=(int) (System.currentTimeMillis()-startTime);
		}
		
		method.getResponseMap().remove("httpResponseCode");
		method.getResponseMap().remove("requestTime");
		
		
		try	{	
			if (line!=null)
			if (line.charAt(0)=='['){
				JSONArray jsa=new JSONArray(line);
				json= (JSONObject) jsa.get(0);
			} else {
				json = new JSONObject(line);
			}
			if (json!=null) {
				TreeMap<String, APIValue> tmp = new TreeMap<String, APIValue>(method.getResponseMap());
				for(String k: tmp.keySet()){
						if (Utilities.getJSONPath(k, json)!=null) {
							if (!method.getResponseMap().get(k).toString().isEmpty()){
								if ((method.getResponseMap().get(k).toString().charAt(0)=='$') &&
										(method.getResponseMap().get(k).toString().charAt(1)=='<')){
										variables.put(extractVarName(method.getResponseMap().get(k).toString()), new APIValue(Utilities.getJSONPath(k, json)));
										method.getResponseMap().put(extractVarName(method.getResponseMap().get(k).toString()), new APIValue(Utilities.getJSONPath(k, json)));
								} else {
  									method.getResponseMap().put(extractVarName(method.getResponseMap().get(k).toString()), new APIValue(Utilities.getJSONPath(k, json)));
								}							
							}
						} else method.getResponseMap().put(k, new APIValue("none"));
				}
				if (json.has("Msg"))
					method.getResponseMap().put("Msg", new APIValue(Utilities.getJSONPath("Msg", json)));
				if (json.has("Error")) 
					method.getResponseMap().put("Error", new APIValue(Utilities.getJSONPath("Error", json)));
			}
		}catch (Exception e) {
			//e.printStackTrace();
			Logger.log("Type: "+method.getType()+" Path: "+method.getUrl()+" caused an exception!", Logger.ERROR);
		}
		method.getResponseMap().put("HTTPStatusCode", new APIValue(last_status));
		method.getResponseMap().put("RequestTime", new APIValue(now));
	}
		
	public Map<String, APIValue> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, APIValue> vars) {
		this.variables = vars;
	}
	
}