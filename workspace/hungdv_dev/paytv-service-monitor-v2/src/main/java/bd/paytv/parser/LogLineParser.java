package bd.paytv.parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LogLineParser {

	private String logEntryPattern;
	private Pattern pattern;
	private int numberGroups;
		
	public LogLineParser(String logEntryPattern,int numberGroupts){
		this.logEntryPattern = logEntryPattern;
		this.numberGroups	 = numberGroupts;
		pattern = Pattern.compile(logEntryPattern);
	}
	
	public boolean parseable(String logLine){
		Matcher matcher = this.pattern.matcher(logLine);
		return matcher.matches();
	}
	private boolean match(String logEntryLine, int numberOfGroups){
		Matcher matcher = this.pattern.matcher(logEntryLine);
		return matcher.matches()&&matcher.groupCount()==numberOfGroups;
	}
	
	public ArrayList<String> extractValues(String logEntryLine){
		Matcher matcher = this.pattern.matcher(logEntryLine);
		if( matcher.matches() && matcher.groupCount() == this.numberGroups){
			ArrayList<String> groups = new ArrayList<String>();
			for(int i = 1; i <= this.numberGroups; i++){
				String value = matcher.group(i);
				groups.add(value);
			}
			return groups;
		}
		else return new ArrayList<String>();
	}
	
	/*
	 * Get entire LogLine object
	 * @input : String - Input log line
	 * @return : LogLine - Output LogLine object
	 */
	public LogLine getLogLineObject(String logEntryLine)
	{
		return new LogLine(this.extractValues(logEntryLine));
	}
	/*
	 * Get Message in LogLine only
	 * @input : String - Input log line
	 * @return : String - Output message string
	 */
	public String getLogMeassage(String logEntryLine){
		LogLine log  = this.getLogLineObject(logEntryLine);
		return log.getMessage();
	}
	
	
	public static void main(String[] args){
		String regex = "\\[(.*)\\] ([^ ]*) ([^ ]*) (\\d{2} \\w{3} \\d{4}) (\\d{2}:\\d{2}:\\d{2}) - (.*)$";

//		String regex = "\\[(.*)\\] ([^ ]*) ([^ ]*) (\\d{2} [^ ]* \\d{4}) (\\d{2}:\\d{2}:\\d{2}) - (.*)$";
		int groupsCount = 6; // each group bounded by ( ), let's count.msg. 
		String logLineExample =  "[INFO] main InfoLog 09 Feb 2017 10:02:16 - Done parse /data/fbox/logs/2017/02/09/09/fbox_11.txt | total: 161641 | print: 155081 | time: 13113 | at: 1486609336922";
		LogLineParser parser = new LogLineParser(regex,groupsCount);
		LogLine parsedLine = new LogLine(parser.extractValues(logLineExample));
		System.out.println(parsedLine.getMessage());
		System.out.println("Message : " + parser.getLogMeassage(logLineExample));
	}
	
}
