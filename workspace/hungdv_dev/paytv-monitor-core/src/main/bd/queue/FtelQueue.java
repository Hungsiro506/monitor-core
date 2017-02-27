package main.bd.queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Deque containing message in communication
 *\\ /!\ WARNING :
 * Chu y : Neu toc do push msg vao qua nhanh thi se xay ra 2 truong hop :
 * Sender chua kip send msg di thi msg da bi remove last,
 * Keeper chua kip xu ly msg tiep theo thi co msg moi add first. 
 * Nen co co che quan ly offset hoac chuyen sang xu dung Kafka.
 * 
 * @author hungdv
 *
 * @param <T>
 */
public class FtelQueue<T> {
	//private static LinkedList<T> queue;
	// Change from LinkedList -> java.util.concurrentLinkedDeque to avoid concurrence issue. 
	private  ConcurrentLinkedDeque<T> queue;
	private final int MAXSIZE = 1000;
	public  FtelQueue()
	{
		this.queue = new ConcurrentLinkedDeque<T>();
	}
	private void addFirst(T msg){
		this.queue.addFirst(msg);
	}
	private void removeLast(){
		this.queue.removeLast();
	}
	
	/*
	 * Add new element to queue (at first), remove last if queue is full.
	 * By default size of queue  = 1000;
	 * IT IS CRISTICAL. IF MEASSE PRODUCE FASTER THAN IT COMSUMED, WHERE WILL GET NO MESG AT ALL.
	 */
	public void push(T msg){
		if(this.queue.size() == this.MAXSIZE){
			this.queue.removeLast();
		}
		this.queue.addFirst(msg);
	}
	
	//Get last one, and remove it after we got it (if size() > 1)
	public T pull(){
		 T msg = null;
		 if(this.queue.size() > 0){
		 msg = this.queue.getLast();
		 this.removeLast();
		 }
		/* if(this.queue.size() > 1){
			 this.queue.removeLast();
		 }*/
		 return msg;
	}
	//Get last one but not remove this one.
	public T getLastButNotRemove(){
		 T msg = null;
		 if(this.queue.size() > 0){
		 msg = this.queue.getLast();
		 }
		/* if(this.queue.size() > 1){
			 this.queue.removeLast();
		 }*/
		 return msg;
	}	
	public T getFirst(){
		 T msg = null;
		 if(this.queue.size() > 0){
		 msg = this.queue.getFirst();
		 }
		/* if(this.queue.size() > 1){
			 this.queue.removeLast();
		 }*/
		 return msg;
	}
	/*
	 * return number of element in dequeue.
	 */
	public int count(){
		return this.queue.size();
	}
	
}
