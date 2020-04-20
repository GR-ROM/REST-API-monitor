package apimon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	final public static int SILENT=0;
	final public static int ERROR=1;
	final public static int WARNING=2;
	final public static int INFO=3;
	final public static int DEBUG=4;
	private static int severity;
	private static List<String> logList=null;
	//private static int limit;
	private static String logFileName;
	
	public static String getCurrentTimeStamp() {
	    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	
	public static void init(String fname) {
		severity=INFO;
		//limit=100*1024;
		if (fname==null) fname="error.log";
		logFileName=fname;
		logList=new ArrayList<String>();
	}
	
	public static void log(String text, int severity) {
			synchronized(Logger.class){
				if (Logger.severity>=severity) {
				switch (severity){
					case (DEBUG): logList.add(getCurrentTimeStamp()+" DEBUG "+text); break;
					case (INFO): logList.add(getCurrentTimeStamp()+" INFO "+text); break;
					case (WARNING): logList.add(getCurrentTimeStamp()+" WARNING "+text); break;
					case (ERROR): logList.add(getCurrentTimeStamp()+" ERROR "+text); break;
				}
				flushFile();
				logList.clear(); // purge fucking string list!
			}
		}
	}
	
	private static void flushFile() {
		FileWriter writer = null;
		try {
			writer = new FileWriter(Logger.logFileName, true);
			for(String str: logList) {
				writer.write(str + System.lineSeparator());
			}		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
