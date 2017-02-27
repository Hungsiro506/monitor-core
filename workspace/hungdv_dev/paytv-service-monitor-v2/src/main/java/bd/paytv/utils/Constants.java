package bd.paytv.utils;

public class Constants {
	// See more in parser.LogLine.java
	// Pattern matching for entire Log record
	public static final String PAYTV_SERVICE_INFO_LOG_REGEX = "\\[(.*)\\] ([^ ]*) ([^ ]*) (\\d{2} \\w{3} \\d{4}) (\\d{2}:\\d{2}:\\d{2}) - (.*)$";
	// Pattern matching for Message in Log record.
	public static final String PAYTV_SERVICE_INFO_MESSAGE_LOG_REGEX = "(\\bDone) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([\\d]+) ([^ ]*) ([^ ]*) ([\\d]+) (.*)$";
	// Number of Groups in pattern. Will be removed soon. 
	public static final int PAYTV_SERVICE_INFO_LOG_REGEX_GROUPS_COUNT = 6;
	// server Ip - used as a server identifier. Zabbix uses it .
	public static final String PAYTV_SERVICE_INFO_SERVER_IP =  "172.31.8.10";
	// Zabbix server ip
	public static final String ZABBIX_SERVER_IP =  "172.31.8.10";
	// Zabbix port
	public static final int ZABBIX_PORT = 10051;
}
