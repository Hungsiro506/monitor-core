package bd.paytv.log_tailer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import bd.paytv.parser.Parser;
import main.bd.sender.ISender;
import main.bd.sender.SenderFactory;
import main.bd.sender.ZabbixSender;
import main.bd.sender.ZabbixSenderImpl;
import main.bd.message.*;
import main.bd.queue.FtelQueue;


public class LogTailer implements Runnable {
	
	private boolean debug = true;
	private int 	runEverySeconds = 2000;
	private long 	lastKnownPosition =0;
	private boolean shouldIRun = true;
	private File	logFile = null;
	private static int counter =0;
	
	/**	Shared variable betweem producer and consumer.
	 Very critical, may be cause of confict and deathlock.
	 Need to sync and locking this variable. */
	
	private Parser parser;
	/*/
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
/*	public void run() {
		// TODO Auto-generated method stub
		try{
			while(shouldIRun){
				Thread.sleep(this.runEverySeconds);
				long fileLength = logFile.length();
				if(fileLength > lastKnownPosition){
					System.out.println("DEBUG: reading ....");
					RandomAccessFile readWriteFileAccess = new RandomAccessFile(logFile, "r");
					System.out.println("DEBUG : Last Know Position : " + this.lastKnownPosition);
					//RandomAccessFile readWriteFileAccess = new RandomAccessFile(logFile, "rw");
					readWriteFileAccess.seek(lastKnownPosition);
					String line = null;
					while((line = readWriteFileAccess.readLine()) != null){
						System.out.println("DEBUG : " + line);
						
						//printLine(line);
						counter ++;
						System.out.println("DEBUG : Counter : " + counter);
					}
					System.out.println("DEBUG : FILE POINTER + " + readWriteFileAccess.getFilePointer());
					lastKnownPosition = readWriteFileAccess.getFilePointer();
					System.out.println("DEBUG : New last know position " + this.lastKnownPosition);
					System.out.println("DEBUG : End of while !");
					readWriteFileAccess.close();
				}
				else{
					if(debug)
						this.printLine("Hmm.. Couldn't found new line after line # " + counter);
				}
			}
		}catch(Exception e){
			stopRunning();
		}
		if (debug)
			this.printLine("Exit the program...");
	}*/
	/////
	public void run() {
		// TODO Auto-generated method stub
		try{
			while(shouldIRun){
				Thread.sleep(this.runEverySeconds);
				long fileLength = logFile.length();
				//printDebug("File length " + fileLength);
				if(fileLength > lastKnownPosition){
					RandomAccessFile readWriteFileAccess = new RandomAccessFile(logFile, "r");
					//RandomAccessFile readWriteFileAccess = new RandomAccessFile(logFile, "rw");
					readWriteFileAccess.seek(lastKnownPosition);
					String line = null;
					while((line = readWriteFileAccess.readLine()) != null){
						printDebug("Line " + line);
						//Check parsable
						if(parser.parsealbe(line)){
							parser.parserAndPush(line);
							counter ++;
							//printDebug("Counter : " + counter);
						}
						//printLine(line);
					}
					//System.out.println("DEBUG : FILE POINTER + " + readWriteFileAccess.getFilePointer());
					lastKnownPosition = readWriteFileAccess.getFilePointer();
					//System.out.println("DEBUG : New last know position " + this.lastKnownPosition);
					//System.out.println("DEBUG : End of while !");
					readWriteFileAccess.close();
				}
				else{
					if(debug)
						this.printLine("Hmm.. Couldn't found new line after line # " + counter);
				}
			}
		}catch(Throwable e){
			stopRunning();
			System.out.print(e.toString());
		}finally{
		if (debug)
			this.printLine("Tailer thread has down ! Exit the program...");
			System.out.println("Tailer fail down " + System.currentTimeMillis()) ;
		
		}
		
	}
	private static void printDebug(String msg){
		System.out.println("[DEBUG] " +  msg);
	}
	private void printLine(String mesg){
		System.out.println(mesg);
	}
	private void stopRunning(){
		shouldIRun = false;
	}
	public LogTailer(String logFile,int interval, Parser parser){
		this.logFile = new File(logFile);
		this.runEverySeconds = interval;
		this.parser = parser;
		// Skip all existing line . Start from last line in the begining.
		this.lastKnownPosition = this.logFile.length();
	}
	
	
	/*public static void main(String argv[]) {
		 
		ExecutorService executor = Executors.newFixedThreadPool(4);
		ExecutorService executor2 = Executors.newFixedThreadPool(4);
		String filePath = "/home/hungdv/Desktop/tailer.txt";
		//String zabbixDockerIP = "127.0.0.1";
		String zabbixDockerIP = "183.80.132.21";
		int zabbixDockerPort = 10051;
		FtelQueue<MetricsMessage> queue = new FtelQueue<MetricsMessage>();		 
		Parser parser = new Parser(queue);
		LogTailer tailer = new LogTailer(filePath, 2000,parser);
		
		SenderFactory<MetricsMessage> senderFactory = new SenderFactory<MetricsMessage>();
		ISender<MetricsMessage> sender	=	senderFactory.getSender("ZABBIX",zabbixDockerIP,zabbixDockerPort,queue,"172.31.8.10");
		
		// Start running log file tailer on crunchify.log file
		executor.execute(tailer);
		executor2.execute(sender); 
			
		// Start pumping data to file crunchify.log file
		appendData(filePath, true,2000);
		executor.shutdown(); 
		executor2.shutdown();
	}*/
 
	/**TESTING PURPOSE
	 * Use appendData method to add new line to file, so above tailer method can print the same in Eclipse Console
	 * 
	 * @param filePath
	 * @param shouldIRun
	 * @param interval
	 */
	public static void appendData(String filePath, boolean shouldIRun, int interval) {
		FileWriter fileWritter;
 
		try {
			while (shouldIRun) {
				Thread.sleep(interval);
				fileWritter = new FileWriter(filePath, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
 
				//String data = "\nCrunchify.log file content: " + Math.random();
				String data = "[INFO] main InfoLog 09 Feb 2017 09:52:24 - Done parse /data/fbox/logs/2017/02/09/09/fbox_9.txt | total: 155555 | print: 144444 | time: 20585 | at: 1486608744014\n";
				
				bufferWritter.write(data);
				bufferWritter.close();
			}
 
		} catch (Exception e) {
			e.printStackTrace();
		}
 
	}
	
	
}
