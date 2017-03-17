package bd.paytv.keeper;

import java.util.ArrayList;

 class ThreeSigmaRule implements ILogicer<Float> {
	private ArrayList<Float> metrics;
	public	ThreeSigmaRule(){
		// max 20 elements. 
		this.metrics	=	new	ArrayList<Float>();
	}
	/**
	 * add metric to array
	 * loop through values and determine if new metric is Outlier or not
	 * Co the suy nghi viec re-reanger from 0 - (size - 4) 
	 * Tuc la metric moi vao chua xet ngay
	 * O day metric moi vao la xet ngay, co the ko dung. 
	 */
	public boolean isOutlier(Float metric){
		this.addMetric(metric);
		float sumTotal = 0;
		float mean = 0;
		float std = 0;
		int size = this.metrics.size();
		//printDebug(" Size of array list in sight logicer :  "  +  size);
		for(int i = 0; i < size; i ++){
			sumTotal += this.metrics.get(i);
			
		}
		//printDebug("sumTotal : " + sumTotal);
		//calculate mean of array.
		mean = sumTotal/size;
		//printDebug("mean : " + mean);
		float powerSum = 0;
		/*
		  	loop through values
		   	array value = (indexed value - mean)^2   
		  	calculate sum of the new array.
		  	divide the sum by the array length
	  		square root it 
		 */
		for(int i = 0; i< size; i ++){
			powerSum += Math.pow((this.metrics.get(i) - mean),2);
		}
		std = (float) Math.sqrt(powerSum/size);
		//printDebug("std : " + std);
		// (x - mean) > 3* std THEN x is outlier.
		if(Math.abs((metric - mean)) > (2.5*std)){
			//printDebug(metric +  " IS OUTLIER !");
			return true;}
		return false;
	}
	private void addMetric(Float metric){
		if(this.metrics.size() == 20){
			this.metrics.remove(0);
		}
		this.metrics.add(metric);
	}
	@SuppressWarnings("unused")
	private static void printDebug(String msg){
		System.out.println("[DEBUG] : " + msg);
	}

	
 }
