package apimon;

import java.text.SimpleDateFormat;
import java.util.Date;

public class APIValue {
	public static enum WrappedType{ INT, STRING, TIME };
	private int code;
	private String message;
	private Date time;
	private WrappedType type;
	
	public APIValue(int code) {
		this.code=code;
		setType(WrappedType.INT);
	}
	
	public APIValue(String message) {
		this.message=message;
		setType(WrappedType.STRING);
	}
	
	public APIValue(Date time) {
		this.setTime(time);
		setType(WrappedType.TIME);
	}
	
	public APIValue(Object s) {
		if (s.getClass().equals(Integer.class)) {
			this.code=(Integer)s;
			setType(WrappedType.INT);
		} else
		if (s.getClass().equals(String.class)) {
			this.message=(String)s;
			setType(WrappedType.STRING);
		}
	}
	
	public Object get() {
		if (this.type==WrappedType.INT) return code;
		if (this.type==WrappedType.STRING) return message;
		if (this.type==WrappedType.TIME) return time;
		return null;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public WrappedType getType() {
		return type;
	}

	public void setType(WrappedType type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		if (this.type==WrappedType.INT) Integer.toString(code);
		if (this.type==WrappedType.STRING) return message;
		if (this.type==WrappedType.TIME) return new SimpleDateFormat("HH:mm:ss").format(time);
		return message;
	}
	
}
