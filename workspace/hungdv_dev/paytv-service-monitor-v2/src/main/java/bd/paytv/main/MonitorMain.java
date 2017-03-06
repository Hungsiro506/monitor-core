package bd.paytv.main;

import java.io.PrintStream;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import bd.paytv.log_tailer.LogTailer;
import bd.paytv.parser.Parser;
import main.bd.message.MetricsMessage;
import main.bd.queue.FtelQueue;
import main.bd.sender.ISender;
import main.bd.sender.SenderFactory;
import bd.paytv.keeper.ZabbixKeeper;
import bd.paytv.log_tailer.*;


public class MonitorMain
{
  private static String logFilePath = "";
  private static String serverIP = "";
  private static int serverPort = 0;
  private static String agentName = "";
  
  /* Error */
  public static void main(String[] args)
  {	
	  /*
	   * Debug : Testing random access file read . 	
	   */
	  
	  	/*LogTailerTest test = new LogTailerTest();
	  	ExecutorService ex = Executors.newFixedThreadPool(1);
	  	ex.execute(test);
	  	*/
		 /* logFilePath = "./info.log";
		  serverIP = "127.0.0.1";
		  serverPort = 10051;
		  agentName = "172.17.0.1";*/
	  	parsingArguments(args);
	  	System.out.println("Can't take my eyes off you! ");
        final ExecutorService executor 		= 	Executors.newFixedThreadPool(3);

          /*final Calendar c = Calendar.getInstance();
          c.add(Calendar.DAY_OF_MONTH, 1);
          c.set(Calendar.HOUR_OF_DAY, 0);
          c.set(Calendar.MINUTE, 0);
          c.set(Calendar.SECOND, 0);
          c.set(Calendar.MILLISECOND, 0);


        // Using hook ShutDown and CountDownLatch Technique.
	  	Runtime.getRuntime().addShutdownHook(new Thread("Shutdown thread"){
	  	    @Override
            public void run(){
                executor.shutdown();
                final long howManyTimeTillmidnight = (c.getTimeInMillis()-System.currentTimeMillis() - 3*60*60); // tru di 3 phut :))
                try{
                    if(executor.awaitTermination(howManyTimeTillmidnight, TimeUnit.MILLISECONDS)){
                        executor.shutdownNow();
                    }
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

	  	final CountDownLatch doneSignal = new CountDownLatch(3);*/

/*	  	ExecutorService executorKeeper	=	Executors.newFixedThreadPool(1);
	  	ExecutorService executorTailer	=	Executors.newFixedThreadPool(4);
		ExecutorService executorSender	=	Executors.newFixedThreadPool(4);*/
	  	/*ExecutorService executorKeeper	=	Executors.newSingleThreadExecutor();
	  	ExecutorService executorTailer	=	Executors.newSingleThreadExecutor();
		ExecutorService executorSender	=	Executors.newSingleThreadExecutor();*/

		
		FtelQueue<MetricsMessage> queue	=	new FtelQueue<MetricsMessage>();
		FtelQueue<MetricsMessage> immutableQueue	=	new FtelQueue<MetricsMessage>();
		try{
			Parser parser	=	new Parser(queue,immutableQueue);
			LogTailer tailer	=	new LogTailer(logFilePath, 2000,parser);
			SenderFactory<MetricsMessage> senderFactory	=	new SenderFactory<MetricsMessage>();
			ISender<MetricsMessage> sender	=	senderFactory.getSender("ZABBIX", serverIP, serverPort, queue, agentName);
			ISender<MetricsMessage> senderForKeeper	=	senderFactory.getSender("ZABBIX", serverIP, serverPort, immutableQueue, agentName);
			ZabbixKeeper keeper = new ZabbixKeeper(immutableQueue,senderForKeeper, serverIP, serverPort, agentName);
			//Zabbix_Sender sender = new Zabbix_Sender(zabbixDockerIP,zabbixDockerPort,parser,"172.17.0.1");
			// Start running log file tailer on crunchify.log file
			/*executorKeeper.execute(keeper);
			executorTailer.execute(tailer);
			executorSender.execute(sender);*/
			//executorKeeper.execute(keeper);
			executor.execute(tailer);
			executor.execute(sender);
			executor.execute(keeper);
			//LogTailer.appendData("/data/user/hungvd8/monitor/tailer.txt", true,2000);

		  }catch(Exception ex){
			  ex.printStackTrace();
		  }finally{
				/*executorTailer.shutdown();
				executorSender.shutdown();
				executorKeeper.shutdown();*/
			  	//executor.shutdown();
			  	System.out.println(" There an problem when execute tasks at " + System.currentTimeMillis());
		  }
    // Byte code:
    //   0: aload_0
    //   1: invokestatic 33	main/Main:parsingArguments	([Ljava/lang/String;)V
    //   4: iconst_2
    //   5: invokestatic 36	java/util/concurrent/Executors:newFixedThreadPool	(I)Ljava/util/concurrent/ExecutorService;
    //   8: astore_1
    //   9: iconst_2
    //   10: invokestatic 36	java/util/concurrent/Executors:newFixedThreadPool	(I)Ljava/util/concurrent/ExecutorService;
    //   13: astore_2
    //   14: new 42	main/bd/queue/FtelQueue
    //   17: dup
    //   18: invokespecial 44	main/bd/queue/FtelQueue:<init>	()V
    //   21: astore_3
    //   22: new 45	bd/paytv/parser/Parser
    //   25: dup
    //   26: aload_3
    //   27: invokespecial 47	bd/paytv/parser/Parser:<init>	(Lmain/bd/queue/FtelQueue;)V
    //   30: astore 4
    //   32: new 50	bd/paytv/log_tailer/LogTailer
    //   35: dup
    //   36: getstatic 16	main/Main:logFilePath	Ljava/lang/String;
    //   39: sipush 2000
    //   42: aload 4
    //   44: invokespecial 52	bd/paytv/log_tailer/LogTailer:<init>	(Ljava/lang/String;ILbd/paytv/parser/Parser;)V
    //   47: astore 5
    //   49: new 55	main/bd/sender/SenderFactory
    //   52: dup
    //   53: invokespecial 57	main/bd/sender/SenderFactory:<init>	()V
    //   56: astore 6
    //   58: aload 6
    //   60: ldc 58
    //   62: getstatic 18	main/Main:serverIP	Ljava/lang/String;
    //   65: getstatic 20	main/Main:serverPort	I
    //   68: aload_3
    //   69: getstatic 22	main/Main:agentName	Ljava/lang/String;
    //   72: invokevirtual 60	main/bd/sender/SenderFactory:getSender	(Ljava/lang/String;Ljava/lang/String;ILmain/bd/queue/FtelQueue;Ljava/lang/String;)Lmain/bd/sender/ISender;
    //   75: astore 7
    //   77: aload_1
    //   78: aload 5
    //   80: invokeinterface 64 2 0
    //   85: aload_2
    //   86: aload 7
    //   88: invokeinterface 64 2 0
    //   93: ldc 70
    //   95: iconst_1
    //   96: sipush 2000
    //   99: invokestatic 72	bd/paytv/log_tailer/LogTailer:appendData	(Ljava/lang/String;ZI)V
    //   102: goto +40 -> 142
    //   105: astore_3
    //   106: aload_3
    //   107: invokevirtual 76	java/lang/Exception:printStackTrace	()V
    //   110: aload_1
    //   111: invokeinterface 81 1 0
    //   116: aload_2
    //   117: invokeinterface 81 1 0
    //   122: goto +32 -> 154
    //   125: astore 8
    //   127: aload_1
    //   128: invokeinterface 81 1 0
    //   133: aload_2
    //   134: invokeinterface 81 1 0
    //   139: aload 8
    //   141: athrow
    //   142: aload_1
    //   143: invokeinterface 81 1 0
    //   148: aload_2
    //   149: invokeinterface 81 1 0
    //   154: return
    // Line number table:
    //   Java source line #22	-> byte code offset #0
    //   Java source line #23	-> byte code offset #4
    //   Java source line #24	-> byte code offset #9
    //   Java source line #26	-> byte code offset #14
    //   Java source line #27	-> byte code offset #22
    //   Java source line #28	-> byte code offset #32
    //   Java source line #29	-> byte code offset #49
    //   Java source line #30	-> byte code offset #58
    //   Java source line #32	-> byte code offset #77
    //   Java source line #33	-> byte code offset #85
    //   Java source line #34	-> byte code offset #93
    //   Java source line #35	-> byte code offset #102
    //   Java source line #36	-> byte code offset #106
    //   Java source line #38	-> byte code offset #110
    //   Java source line #39	-> byte code offset #116
    //   Java source line #37	-> byte code offset #125
    //   Java source line #38	-> byte code offset #127
    //   Java source line #39	-> byte code offset #133
    //   Java source line #40	-> byte code offset #139
    //   Java source line #38	-> byte code offset #142
    //   Java source line #39	-> byte code offset #148
    //   Java source line #42	-> byte code offset #154
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	155	0	args	String[]
    //   8	135	1	executorTailer	java.util.concurrent.ExecutorService
    //   13	136	2	executorSender	java.util.concurrent.ExecutorService
    //   21	48	3	queue	main.bd.queue.FtelQueue<main.bd.message.MetricsMessage>
    //   105	2	3	ex	Exception
    //   30	13	4	parser	bd.paytv.parser.Parser
    //   47	32	5	tailer	bd.paytv.log_tailer.LogTailer
    //   56	3	6	senderFactory	main.bd.sender.SenderFactory<main.bd.message.MetricsMessage>
    //   75	12	7	sender	main.bd.sender.ISender<main.bd.message.MetricsMessage>
    //   125	15	8	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   14	102	105	java/lang/Exception
    //   14	110	125	finally
	  
  }
  
  private static void parsingArguments(String[] args)
  {
    Options options = new Options();
    
    Option logFile = new Option("l", "logFile", true, "path to log file");
    logFile.setRequired(true);
    options.addOption(logFile);
    
    Option serverIp = new Option("i", "serverIp", true, "received server IP");
    serverIp.setRequired(true);
    options.addOption(serverIp);
    
    Option Port = new Option("p", "Port", true, "received server Port");
    Port.setRequired(true);
    options.addOption(Port);
    
    Option hostName = new Option("h", "hostName", true, "Hostname - Agent ip");
    hostName.setRequired(true);
    options.addOption(hostName);
    
    CommandLineParser parser = new GnuParser();
    HelpFormatter formatter = new HelpFormatter();
    CommandLine cmd;
    try
    {
      cmd = parser.parse(options, args);
    }
    catch (ParseException e)
    {
      
      System.out.println(e.getMessage());
      formatter.printHelp("utility-name", options);
      System.exit(1); return;
    }
   
    logFilePath = cmd.getOptionValue("logFile");
    serverIP = cmd.getOptionValue("serverIp");
    serverPort = Integer.parseInt(cmd.getOptionValue("Port"));
    agentName = cmd.getOptionValue("hostName");
  }
  
  private void test()
  {
    String[] test = new String[8];
    test[0] = "-l";
    test[1] = "log";
    test[2] = "-i";
    test[3] = "serverIp";
    test[4] = "-p";
    test[5] = "90";
    test[6] = "-h";
    test[7] = "hostName";
    parsingArguments(test);
    System.out.println(serverIP);
  }

}

