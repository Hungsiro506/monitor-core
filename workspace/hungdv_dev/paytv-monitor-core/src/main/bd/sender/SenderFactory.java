package main.bd.sender;

import main.bd.queue.FtelQueue;

public class SenderFactory<T> {
	public ISender<T> getSender(String senderType,String receivedServerIP,int receivedServerPort,FtelQueue<T> queue,String senderAgentIP){
		if(senderType == null){
	         return null;
	      }		
	    if(senderType.equalsIgnoreCase("ZABBIX")){
	         return new ZabbixSenderImpl<T>(receivedServerIP, receivedServerPort,queue, senderAgentIP);
	    }
	    return null;   
	    
	    /**
	     * Add others sender here!...
	     */
	}
}
