package    bd.paytv.keeper;

import    java.util.HashMap;
import  java.util.Iterator;
import  java.util.Map;
import  java.util.Set;

import    main.bd.message.MetricsMessage;
import    main.bd.queue.FtelQueue;
import    main.bd.sender.DataObject;
import    main.bd.sender.ISender;
import    main.bd.sender.SenderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
  *  Implementation  of  Zabbix  Keeper,  using  Zabbix  as  server
  *  Generic  type  :  MetricMessage
  *  @author  hungdv
  *
  */

public    class    ZabbixKeeper    extends    Keeper<MetricsMessage>    implements    Runnable{

	private final Logger LOGGER = LoggerFactory.getLogger(ZabbixKeeper.class);

	public    ZabbixKeeper(FtelQueue<MetricsMessage>    queue,    ISender<MetricsMessage>    sender
			,String  receivedServerIP,int  receivedServerPort,String  senderAgentIP)    {
		super(queue,    sender);
		this.logic    =    LogicFactory.getLogicer("THREESIGMARULE");
		SenderFactory<MetricsMessage>  senderFactory  =  new  SenderFactory<MetricsMessage>();
		this.sender  =  senderFactory.getSender("ZABBIX",  receivedServerIP,  receivedServerPort,  queue,  senderAgentIP);
		
		//    TODO    Auto-generated    constructor    stub
	}
	private    ILogicer<Float>    logic;
	/**
	    *    Check    if    a    metric    is    an    Outlier    or    not
	    *    @param    metrics    :    MetricsMessage    :    Contain    map    of    key-value    pairs
	    */
	protected    boolean    isOutlier(MetricsMessage    metrics){
		
		HashMap<String,String>    metricsMap    =    metrics.getMetrics();
		String  totalRead  =  this.getValueFromHashMapStringString(metricsMap,  "totalRead");
		//printDebug("  ----  ");
		//printDebug("HASH  MAP  :    "  +  metricsMap.toString());
		//String    totalRead    =    metricsMap.get(new  String("totalRead"));
		//printDebug("  ----    total  read  :  "  +  totalRead);
		float    metric    =    Float.parseFloat(totalRead);
		//printDebug("  ----    parsed  total  read  to  float  :  "  +  metric);
		return    this.logic.isOutlier(metric);
	}
	public    void    run()    {
		try{
		//    TODO    Auto-generated    method    stub
		while(true){
			
			/*
			 *  getLast  never  return  null,  unless  queue  is  empty.  
			 */
			MetricsMessage  last  =  this.getFirst();
			if(last  !=  null  &&  this.hasNewLogInQueue()){
				LOGGER.info("[KEEPER] get new message from queue  : " + last.getMetrics().toString());
				if(this.isOutlier(last)){
					try  {
						String  totalRead  =  this.getValueFromHashMapStringString(last.getMetrics(),  "totalRead");
						//printDebug("Outlier  total  read  :    "  +  totalRead);
						sender.sendIgnoreResult("totalReadOutlierAlert",  totalRead);
						LOGGER.info("[KEEPER] send outlier metric to server : " + totalRead);
					}  catch  (Exception  e)  {
						//  TODO  Auto-generated  catch  block  
						e.printStackTrace();
						LOGGER.warn(e.getMessage(),e);
					}
				}
			}
			if(this.checkLocalLogic(10)){
				try  {
					sender.sendIgnoreResult("totalReadAlert",  Integer.toString(getCounter()));
					LOGGER.info("[KEEPER] send alert metric to server : " + Integer.toString(getCounter()));
					Thread.sleep(1000*60);
					}  catch  (Exception  e)  {
					//  TODO  Auto-generated  catch  block
					e.printStackTrace();
				}
			}
			try{
				Thread.sleep(1000);
			}catch(InterruptedException  e){
				e.printStackTrace();
				LOGGER.warn(e.getMessage(),e);
				}
		}
			
		}catch(Throwable e){
			e.printStackTrace();
			}finally{
				System.out.println("Keeper has fail down at : " + System.currentTimeMillis());
				LOGGER.error("Keeper has fail down at : " + System.currentTimeMillis());
			}
		
	}
	private  static  void  printDebug(String  msg){
		System.out.println("[DEBUG]  :  "  +  msg);
	}
	private  String  getValueFromHashMapStringString(HashMap<String,String>  hashmap,String  key){
		String    value  =  "  ";
		Set  keySet  =  hashmap.entrySet();
		Iterator  i  =  keySet.iterator();
		while(i.hasNext()){
			Map.Entry  me  =  (Map.Entry)  i.next();  
			if(me.getKey().toString().equalsIgnoreCase(key)){
				value  =  me.getValue().toString();
			}
		}
		return  value;
	}
	
}
