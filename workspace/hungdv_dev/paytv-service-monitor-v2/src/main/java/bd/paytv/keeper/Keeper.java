package  bd.paytv.keeper;

import  main.bd.queue.FtelQueue;
import  main.bd.sender.ISender;

import  java.util.Timer;
import  java.util.TimerTask;
/**
  *  Base  class  for  all  keeper
  */
  abstract	class	Keeper<T>  {
	  //This  queue  never  pulled  out.  
	protected  	FtelQueue<T>	queue;
	protected  	ISender<T>	sender; // So funy :)) How the fuck can i use sender ??
	private	static	int	counter	=  	0;
	private	static	final int	INTERVAL	=	1000*60; 
	protected ILogicer<T> logic;
	private int lastKnowQueueOffSet = 0;
	/**
	  *  TimerTask,  check  offet  of  queue  every  minute
	  *  reset  counter  and  set  new  offset  if  has  new  incoming  Message
	  *  if  not  increase  counter.
	  *  @author  hungdv
	  *	
	  */
	class  RunCheckCounter  extends  TimerTask{
		private  int  lastOffset;
		private  void  setLastOffSet(int  os){
			this.lastOffset  =  os;
		}
		RunCheckCounter(){
			lastOffset  =  queue.count();
		}
		@Override
		public  void  run()  {
			//  TODO  Auto-generated  method  stub
			
			if(lastOffset  ==  queue.count()){
				increaseCounter();
			}
			
			else  if(lastOffset  <  queue.count()){
				resetCounter();
				this.setLastOffSet(queue.count());
			}
			System.out.println("Counter " + counter); 
		}
	}
	
	protected  Keeper(FtelQueue<T>  queue,ISender<T>  sender){
		this.queue=  queue;
		this.sender  =  sender;
		RunCheckCounter  runner  =  new  RunCheckCounter();
		Timer  timer  =  new  Timer();
		timer.schedule(runner, 0, INTERVAL);
		this.lastKnowQueueOffSet = this.queue.count();
		
	}
	/**
	 * Get first - First in first out.
	 * @return
	 */
	protected  T  getFirst()
	{
		return  this.queue.getFirst();
	}
	/**
	  *  Check  all  logic  for  a  metric  locally.
	  *  after  $minutes  mins,  if  has  no  new  metrics,  trigger  an  alert
	  *  @return
	  */
	protected  boolean  checkLocalLogic(int  minutes){
		if(counter  >=  minutes){return  true;} 
		// adding some logic here ...
		return  false;
	}
	/**
	 *   Check  if an metrics is outlier or not. 
	 */
	protected  boolean  isOutlier(T  metrics){
		return  logic.isOutlier(metrics);
	}

	private  static  void  increaseCounter(){
		counter  ++;
	}
	private  static  void  resetCounter(){
		counter  =  0;
	}
	public static int getCounter(){
		return counter;
	}
	/*
	 * Lien tuc kiem tra sau n phut. 
	 */
	protected boolean hasLogAfterNMinuties(int n){
		if(counter  >=  n){return  true;} 
		// adding some logic here ...
		return  false;
	}
	/*
	 * Kiem tra sau n phut va lap lai trong khoang thoi gian interval
	 */
	protected boolean hasLogAfterNMinuties(int n, int interval)
	{
		if(counter  >=  n && (counter % interval == 0 )){return  true;} 
		// adding some logic here ...
		return  false;
	}
	private int getQueueOffSet(){
		return this.queue.count();
	}
	/*protected boolean hasNewLogInQueue(){
		return( this.lastKnowQueueOffSet++ < this.getQueueOffSet() ? true :false);
	}*/
	protected boolean hasNewLogInQueue(){
		int queueOffSet  = this.getQueueOffSet();
		/*
			lastKnowQueue off set < current queue offset
			incase the queue is full (MAXSIZE == 1000) -> it may lead to lastKnowOffSet == QueueOffSet == 1000
		 */
		if(this.lastKnowQueueOffSet < queueOffSet || (this.lastKnowQueueOffSet == 1000 && queueOffSet == 1000) ){
			this.lastKnowQueueOffSet++;
			return true;
		}	
		return false;
	}
	
}
