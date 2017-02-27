package bd.paytv.parser;
import java.util.ArrayList;
/**
 * 
 * @author hungdv
 *
 */
/*
 * Old method, Contruct by an ArrayList containing values. 
 */
public class LogLine {
	/*
	 * Log4j layout Pattern : [%p] %t %c %d{dd MMM yyyy HH:mm:ss} - %m%n
	 * refer  to : http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/PatternLayout.html
	 * 
	 * p : priority INFO, ERROR, FATAL
	 * space
	 * t : thread
	 * space
	 * c : category(basename) 
	 * space
	 * d : ISO8061_date with format
	 * space
	 * -
	 * space
	 * m : message
	 * n : line.separator
	 */
	private String priority;
	private String thread;
	private String category;
	private String date;
	private String time;
	private String message;
	
	public LogLine(String priority,String thread,String category,String date,String time,String message){
		this.priority 	= priority;
		this.thread 	= thread;
		this.category 	= category;
		this.date		= date;
		this.time 		= time;
		this.message	= message;
	}
	/*
	 * @input : ArrayList<String> List of properties
	 * @return : LogLine
	 */
	public  LogLine(ArrayList<String> values){
		this(values.get(0),values.get(1),values.get(2),values.get(3),values.get(4),values.get(5));	 
	}
	// getter
	public String getMessage()	{return this.message;}
	public String getDate()		{return this.date;}
	public String getTime()		{return this.time;}
	
}
