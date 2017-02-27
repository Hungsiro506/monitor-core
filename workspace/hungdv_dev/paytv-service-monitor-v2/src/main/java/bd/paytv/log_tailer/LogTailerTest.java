package bd.paytv.log_tailer;

import java.io.File;
import java.io.RandomAccessFile;

public class LogTailerTest implements Runnable {
	
	private boolean debug = false;
	private int 	runEverySeconds = 2000;
	private long 	lastKnownPosition =0;
	private boolean shouldIRun = true;
	private File	logFile = null;
	private static int counter =0;
	
	public void run() {
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
	}
	private void printLine(String mesg){
		System.out.println(mesg);
	}
	private void stopRunning(){
		shouldIRun = false;
	}
	public LogTailerTest(){
		this.logFile = new File("/build/payTV/logs/ParseLogService/info.log");
		this.runEverySeconds = 1000;
		// Skip all existing line . Start from last line in the begining.
		this.lastKnownPosition = this.logFile.length();
		System.out.println("File Lenght : " + this.lastKnownPosition);
	}
	

}
