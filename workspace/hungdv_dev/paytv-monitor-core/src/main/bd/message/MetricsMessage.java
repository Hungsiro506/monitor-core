package main.bd.message;
import java.util.HashMap;
/**
 * 
 * @author hungdv
 *
 */
/*
 * De tang tinh linh dong nen sua lai MetricMessage la abtract class
 * Cac Metrics msg khac se ke thua lai base class nay. 
 */
public class MetricsMessage {
	private HashMap<String,String> metrics = null;
	public MetricsMessage(HashMap<String,String> metrics){
		this.metrics = metrics;
	}
	public HashMap<String,String> getMetrics(){
		return this.metrics;
	}
	
}
