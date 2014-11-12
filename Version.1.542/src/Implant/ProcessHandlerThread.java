
/**
 * NOTE: Executing a process in java using the process = Runtime.getRuntime().exec()
 * 
 * initiates a process to carry out the command, however data is returned in 2 seperate streams:
 * 	one stream for the process itself and the data returned, 
 *  and the other stream for the error stream to return error commands from executing the process
 * 
 * A problem occurs when you try to execute a command that may fail at times because it is not always
 * certain which will occur first, if the error stream will be filled, or if the process stream will 
 * be filled.  Next, some processes may return a large amount of data and the buffer must be cleared first
 * before more data can be entered.  To solve these problems, we instantiate 2 threads to read the 
 * input streams on the same process, and flush the contents back across the output stream we specify
 * (in this case, the socket output stream)
 * 
 * Therefore, with threads, we can handle the concurrent consummation of data in the process's 
 * output stream as well as data in the process's error output stream
 * 
 * I created the ProcessHandlerThread class to handle this overhead.
 * 
 *  Soon, I'll create a "Process" java package that does all of these calls for the user, 
 *  so all you'll have to do is instantiate my main caller class, and the command will be carried out
 * 
 * 
 * @author Solomon Sonya
 */

package Implant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ProcessHandlerThread extends Thread 
{
	String strMyClassName = "ProcessHandlerThread";
	
	Process process;
	BufferedReader brIn = null;
	PrintWriter pwOut = null;
	
	public volatile boolean continueRun = true;
	
	public volatile static boolean stopProcess = false;
	
	String cmdLine = "";
	
	
	
	/**
	 * it is ok to pass in null for the outStream, this means we will do nothing but ensure the buffer is clear from the process!
	 * @param command
	 * @param proc
	 * @param inStream
	 * @param outStream
	 */
	
	//HMM... IT TURNS OUT, I MISSED PROGRAMMING THE RUN MTD IN THIS CLASS
	
	public ProcessHandlerThread(String command, Process proc, BufferedReader inStream, PrintWriter outStream)
	{
		try
		{
			 stopProcess = false;
			
			 cmdLine = "";
			 process = proc;
			 pwOut = outStream;
			 brIn = inStream;
							
			 while ((cmdLine = brIn.readLine()) != null) 
			 {	
				 //i may come back later and combine the error message to be on only one line... that'll be in update 2.0
				 if(pwOut != null)
				 {
					 pwOut.println(cmdLine); pwOut.flush(); 
					 
					 
				 }
				 
				 if(stopProcess)
				 {
					 pwOut.println("IMMEDIATE HALT RECEIVED. TERMINATING CURRENT COMMAND: " + command); pwOut.flush();
					 break;
				 }
			 }
			 
			 //note, if the error of no space occurs, consider running the garbage collection!  "ERROR: Not enough storage is available to complete this operation."
			 System.gc();
			 			 
			 brIn.close();
		}
		catch(Exception e)
		{
			Driver.eop("ProcessHandlerThread Constructor", strMyClassName, e, e.getLocalizedMessage(), false);
		}
				
	}//end constructor mtd
	
	public ProcessHandlerThread(boolean start_thread, String command, Process proc, BufferedReader inStream, PrintWriter outStream)
	{
		try
		{
			stopProcess = false;
			
			 cmdLine = "";
			 process = proc;
			 pwOut = outStream;
			 brIn = inStream;
							
			 if(start_thread)
			 {
				 this.start();
			 }
			
		}
		catch(Exception e)
		{
			Driver.eop("constructor - true thread", strMyClassName, e, e.getLocalizedMessage(), false);
		}
	}
	
	/*public void run()
	{
		try
		{			
			 while (brIn != null && (cmdLine = brIn.readLine()) != null) 
			 {	
				 //i may come back later and combine the error message to be on only one line... that'll be in update 2.0
				 if(pwOut != null)
				 {
					 pwOut.println(cmdLine); pwOut.flush(); 					 					 
				 }
				 
				 if(stopProcess)
				 {
					 pwOut.println("IMMEDIATE HALT RECEIVED. TERMINATING CURRENT COMMAND"); pwOut.flush();
					 break;
				 }
			 }
			 
			 //note, if the error of no space occurs, consider running the garbage collection!  "ERROR: Not enough storage is available to complete this operation."
			 System.gc();
			 			 
			 brIn.close();
		}
		
//		catch(IOException ioe)
//		{
//			//do nothing here... not sure why were' getting here yet...
//		}
		
		catch(Exception e)
		{
			Driver.eop("run", strMyClassName, e, e.getLocalizedMessage(), false);
		}
	}*/
	
	/**
	 * This method is exactly as the above, only, we prepend a header to each line returned
	 * @param command
	 * @param proc
	 * @param inStream
	 * @param outStream
	 */
	public ProcessHandlerThread(String prependHeader, String command, Process proc, BufferedReader inStream, PrintWriter outStream)
	{
		try
		{
			
			 stopProcess = false;
			 
			 cmdLine = "";
			 process = proc;
			 pwOut = outStream;
			 brIn = inStream;
							
			 while ((cmdLine = brIn.readLine()) != null) 
			 {	
				 //i may come back later and combine the error message to be on only one line... that'll be in update 2.0
				 if(pwOut != null)
				 {
					 pwOut.println(prependHeader + cmdLine); pwOut.flush(); 
					 
					 
				 }
				 
				 if(stopProcess)
				 {
					 pwOut.println("IMMEDIATE HALT RECEIVED. TERMINATING CURRENT COMMAND: " + command); pwOut.flush();
					 break;
				 }
			 }
			 
			 //note, if the error of no space occurs, consider running the garbage collection!  "ERROR: Not enough storage is available to complete this operation."
			 System.gc();
			 			 
			 brIn.close();
		}
		catch(Exception e)
		{
			Driver.eop("ProcessHandlerThread Constructor", strMyClassName, e, e.getLocalizedMessage(), false);
		}
				
	}//end constructor mtd
	
	
	/**
	 * Just like above, execute command is called from the caller class, we exhaust the process buffer and provide a response back
	 * 
	 * @return
	 */
	public static ArrayList<String> getBufferedArrayList(BufferedReader inStream, boolean removeQuotes)
	{
		try
		{
			stopProcess = false;
			
			ArrayList<String> alToBuffer = new ArrayList<String>();
			String cmdLine = "";
			BufferedReader brIn = inStream;
						
			 while ((cmdLine = brIn.readLine()) != null) 
			 {	
				 if(removeQuotes)
				 {
					 cmdLine = cmdLine.replace("\"", "");
				 }
				 
				 alToBuffer.add(cmdLine);
				 
				 if(stopProcess)
				 {
					 break;
				 }
			 }
			 			 
			 brIn.close();
			 
			 return alToBuffer;
		}
		catch(Exception e)
		{
			Driver.eop("getBufferedArrayList", "ProcessHandlerThread", e, e.getLocalizedMessage(), false);
		}
		
		return null;
	}
}
