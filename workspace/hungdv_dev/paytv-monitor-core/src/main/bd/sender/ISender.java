package main.bd.sender;

import java.util.HashMap;

import main.bd.queue.FtelQueue;

/**
 * 
 * @author hungdv
 *
 */
/*
 * Base interface for all senders, zabbix, prometheus, datadogs, etc.
 */
public interface ISender<T> extends Runnable{
/*		public	String zabbixHost	=	"";
		public	int zabbixPort	=	0;
		public	String agentIp	=	"";
		public	FtelQueue<?>	queue = null;
		public  void setZabbixHost(String zabbixHost);
		public void setZabbixPort(int port);
		public void setQueue(FtelQueue<T> queue);
		public void setAgentIp(String agentIp);*/
		
	  void send(HashMap<String,String> metrics) throws Exception;
	  void sendIgnoreResult(String key,String value) throws Exception;
	  
}
