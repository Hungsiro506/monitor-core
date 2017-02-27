package  main.bd.sender;

import  java.io.IOException;
import  java.util.HashMap;
import  java.util.Iterator;
import  java.util.Map;
import  java.util.Set;
import  java.util.concurrent.ExecutorService;
import  java.util.concurrent.Executors;

import  main.bd.queue.*;
import  main.bd.message.*;
/**
  *  
  *  @author  hungdv
  *  @param  <T>  :  extends  MetricsMessage.  
  *
  */
public  class  ZabbixSenderImpl<T>  implements  ISender<T>,  Runnable  {
	
	private  String  zabbixServerIP;
	private  int        zabbixServerPort;
	private  FtelQueue<T>  queue;
	private  ZabbixSender  sender;
	private  String  agentIp;
	private  int  wattingNewMessage  =  2000;
	private  boolean  shouldIRun  =  true;
	
	
	/**
	  *  
	  *  @param  zabbixServerIP
	  *  @param  zabbixServerPort
	  *  @param  queu
	  */
	public  ZabbixSenderImpl(String  zabbixServerIP,int  zabbixServerPort,FtelQueue<T>  queue,String  agentIP){
		//  TODO  Auto-generated  constructor  stub
		this.zabbixServerIP	  =    zabbixServerIP;
		this.zabbixServerPort	=	zabbixServerPort;
		this.queue  =  queue;
		this.agentIp  =  agentIP;
		this.sender  =  new  ZabbixSender(zabbixServerIP,zabbixServerPort);
	}
	
	public  ZabbixSenderImpl()  {
		//  TODO  Auto-generated  constructor  stub
	}
	public  void  setZabbixServerIP(String  zabbixServerIP){
		this.zabbixServerIP  =  zabbixServerIP;
	}
	public  void  setZabbixServerPort(int  zabbixServerPort){
		this.zabbixServerPort	=	zabbixServerPort;
	}
	public  void  setQueue(FtelQueue<T>  queue){
		this.queue	=	queue;
	}
	public  void  setAgentIP(String  agentIP){
		this.agentIp	=	agentIP;
	}
	
	/**
	  *  Sending  metrics  to  Zabbix  server
	  *  @parram  metrics  HashMap  containing  metrics  key-value  pair
	  *  @return  void.
	  *  @Override
	  */
	public  void  send(HashMap<String,String>  metrics)  throws  Exception  {
		//  TODO  Auto-generated  method  stub
		Set  keySet  =  metrics.entrySet();
		Iterator  i  =  keySet.iterator();
		while(i.hasNext()){
			Map.Entry  me  =  (Map.Entry)  i.next();  
			DataObject  metricObject  =  new  DataObject();
			metricObject.setHost(this.agentIp);
			metricObject.setKey(me.getKey().toString());
			metricObject.setValue(me.getValue().toString());
			//System.out.println("Key  :  "  +  me.getKey().toString());
			//System.out.println("Value  :  "  +  me.getValue().toString());
			metricObject.setClock(System.currentTimeMillis()/1000);
			sender.send(metricObject);//Ignore  the  result.
			//SenderResult  result  =  sender.send(metricObject);  //For  debuging.
			//System.out.println(result.toString());
		}
	}
	/**
	  *  Sending  message  to  Zabbix  server  -  specifict  agent  ip
	  *  @pararm  agentIP  :  IP  of  agent  -  to  identify  an  agent  -  aka  HostName.
	  *  @parram  key  :  key  of  item,  must  same  as  Key  you  set  in  server.
	  *  @parram  value  :  payload  of  metrics.  
	  *  @throw  IOException.
	  *  
	  *  @return  SenderResults
	  */
	public  SenderResult  sendWithHostName(String  agentIP,String  key,String  value)  throws  IOException{
		DataObject  dataObject  =  new  DataObject();
		dataObject.setHost(agentIP);
		dataObject.setKey(key);
		dataObject.setValue(value);
		dataObject.setClock(System.currentTimeMillis()/1000);
		SenderResult  result  =  sender.send(dataObject);
                return  result;
	}
	/**
	  *  Send  just  one  key-value  pair.
	  *  @param  key
	  *  @param  value
	  *  @return
	  *  @throws  IOException
	  */
	public  SenderResult  send(String  key,String  value)  throws  IOException{
		DataObject  dataObject  =  new  DataObject();
		dataObject.setHost(this.agentIp);
		dataObject.setKey(key);
		dataObject.setValue(value);
		dataObject.setClock(System.currentTimeMillis()/1000);
		SenderResult  result  =  sender.send(dataObject);
                return  result;
	}
	/**
	  *  Send  just  one  key-value  pair.
	  *  @param  key
	  *  @param  value
	  *  @return  void
	  *  @throws  IOException
	  */
	public  void  sendIgnoreResult(String  key,String  value)  throws  IOException{
		DataObject  dataObject  =  new  DataObject();
		dataObject.setHost(this.agentIp);
		dataObject.setKey(key);
		dataObject.setValue(value);
		dataObject.setClock(System.currentTimeMillis()/1000);
		sender.send(dataObject);

	}

	@Override
	public  void  run()  {
		//  TODO  Auto-generated  method  stub
		try{
			while(shouldIRun){
				//  This  make  me  mad  !.  Need  to  refactor  !!!.  
				//  Everery  things  should  be  generic.  
				if(this.queue.count()  >  0){
					MetricsMessage  messageObject  =  (MetricsMessage)  this.queue.pull();
					HashMap<String,String>  metrics  =  messageObject.getMetrics();
					if(metrics  !=  null){
						try{
							this.send(metrics);
						}catch(IOException  e){
							e.printStackTrace();
						}
					}
				}
				if(this.queue.count()  ==  0){
					Thread.sleep(wattingNewMessage);
				}
			}	
		}catch(Exception  e){
			e.printStackTrace();
		}finally{
			shouldIRun  = false;
			System.out.println("Zabbix sender has fail down." + System.currentTimeMillis());
		}
	}



	/**
	  *  Sending  metrics  to  Zabbix  server
	  *  @parram  metrics  HashMap  containing  metrics  key-value  pair
	  *  @return  void.
	  *  @Override
	  *//*
	public  void  send(HashMap<String,String>  metrics)  throws  Exception  {
		//  TODO  Auto-generated  method  stub
		//  TODO  Auto-generated  method  stub
		Set  keySet  =  metrics.keySet();
		Iterator  i  =  keySet.iterator();
		while(i.hasNext()){
			Map.Entry  me  =  (Map.Entry)  i.next();  
			DataObject  metricObject  =  new  DataObject();
			metricObject.setHost(this.agentIp);
			metricObject.setKey(me.getKey().toString());
			metricObject.setValue(me.getValue().toString());
			metricObject.setClock(System.currentTimeMillis()/1000);
			sender.send(metricObject);  //Ignore  the  result.
		}
		
	}*/

}
