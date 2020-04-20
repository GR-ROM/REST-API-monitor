package apimon;

import java.io.Serializable;

public class HttpHeader implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4715443234968486241L;
	private String key;
	private String value;

	@Override 
	public String toString() {
		return "HttpHeader{"+
				"key='"+key+'\''+
				", value='"+value+'\''+
				'}';
	}
	
	public HttpHeader(String key, String value) {
		this.value=value;
		this.key=key;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
