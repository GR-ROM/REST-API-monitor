package apimon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class APIMethod implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6298886772687924524L;
	private String name=null;
	private String url;
	private String type=null;
	private ArrayList<HttpHeader> headers;
	private TreeMap<String, APIValue> responseMap;
	private HashMap<String, APIValue> payload;
	
	public APIMethod(String name, String url) {
		headers=new ArrayList<HttpHeader>();
		this.setName(name);
		this.url=url;
		this.setType("GET");
		this.responseMap=new TreeMap<String, APIValue>();
		this.payload=new HashMap<String, APIValue>();
	}
	
	public void addHeader(String key, String value) {
		headers.add(new HttpHeader(key, value));
	}
		
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public  List<HttpHeader> getHeaders() {
		return headers;
	}

	public void setHeaders(ArrayList<HttpHeader> header) {
		headers = header;
	}

	public TreeMap<String, APIValue> getResponseMap() {
		return responseMap;
	}

	public void setResponseMap(TreeMap<String, APIValue> responseMap) {
		this.responseMap = responseMap;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HashMap<String, APIValue> getPayload() {
		return payload;
	}

	public void setPayload(HashMap<String, APIValue> payload) {
		this.payload = payload;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
