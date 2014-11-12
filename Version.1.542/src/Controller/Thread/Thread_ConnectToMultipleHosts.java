/**
 * Thread class created to connect to multiple agents while not halting the controller gui
 * during the instantiation of each socket connect function
 * 
 * 
 * @author Solomon Sonya
 */



package Controller.Thread;

import java.io.*;
import java.net.Socket;

import Controller.Drivers.Drivers;
import Implant.Driver;

public class Thread_ConnectToMultipleHosts extends Thread implements Runnable
{
	public static final String myClassName = "Thread_ConnectToMultipleHosts";
	
	public File fleMultipleHosts = null;
	int index_bindToHostType = 0;
	int index_ParentCaller = 0;
	
	int number_of_successful_connections = 0;
	
	public static final int bindTo_Implant = 0, bindTo_Controller = 1, bindTo_Relay = 2, bindToCortana = 3;
	public static final int parentCaller_Controller = 0, parentCaller_Implant = 1;    
	
	public Thread_ConnectToMultipleHosts(File fleHosts, int bindOption, int parentCaller)
	{
		try
		{
			fleMultipleHosts = fleHosts;
			index_bindToHostType = bindOption; //indicate if we're binding to implant, controller, relay, cortana, etc
			index_ParentCaller = parentCaller; //indicates if we add the connected clients t0 the controller or to the implant thread pool
			
			this.start();
		}
		catch(Exception e)
		{
			Driver.eop("Constructor", myClassName, e, "", false);
		}
	}
	
	public void run()
	{
		try
		{
			int numAddedHosts = connectToMultipleHosts(fleMultipleHosts, index_bindToHostType, index_ParentCaller);
			
			
		}
		catch(Exception e)
		{
			Driver.eop("run", myClassName, e, "", false);
		}
	}
	
	
	//private boolean connectToMultipleHosts(boolean connectingToImplant, boolean connectingToController, boolean connectingToRelay, boolean connectToCortana, File fleMultipleHostsList)
	private int connectToMultipleHosts(File fleMultipleHostsList, int bindType, int idxParent)
	{
		boolean connectingToImplant = false;
		boolean connectingToController = false;
		boolean connectingToRelay = false;
		boolean connectToCortana = false;
		
		
		
		int successfulConnections = 0;
		
		try
		{
			//determine the connection type
			switch(bindType)
			{
				case bindTo_Implant:
				{
					connectingToImplant = true;
					
					break;
				}
				
				case bindTo_Controller:
				{
					connectingToController = true;
					
					break;
				}
				
				case bindTo_Relay:
				{
					connectingToRelay = true;
					
					break;
				}
				
				case bindToCortana:
				{
					connectToCortana = true;
					
					break;
				}
			}
			
			if(fleMultipleHostsList == null || !fleMultipleHostsList.exists() || !fleMultipleHostsList.isFile())
			{
				Driver.jop_Error("Invalid Import File Selected!", "Unable to Continue");
				return successfulConnections;
			}
			
			//Load the lit, for each line, attempt to connect to the system
			String line = "";
			BufferedReader brIn = new BufferedReader(new FileReader(fleMultipleHostsList));
			String []arrIP_and_Port = null;
			int lineNumber = 0;
			
			
			String IP = "";
			String prt = "";
			int PORT = 0;
			
			Socket skt = null;
			
			while((line = brIn.readLine()) != null)
			{
				try
				{
					arrIP_and_Port = line.trim().split(":");
					
					IP = arrIP_and_Port[0].trim();
					prt = arrIP_and_Port[1].trim();
					
					//normalize IP Address
					if(IP == null || IP.trim().equals("") /*|| !IP.contains(".")*/)
					{
						Driver.sop("Invalid IP Address Entered; Cannot Connect to Implant. line [" + lineNumber + "]: " + line);
						continue;
					}
					
					if(IP.trim().equalsIgnoreCase("localhost"))
					{
						IP = "127.0.0.1";
					}
					
					//normalize Port
					try
					{
						PORT = Integer.parseInt(prt);
						
						if(PORT < 1 || PORT > 65534)
							throw new Exception("Port out of range!");
					}
					catch(Exception e)
					{
						if(e.getLocalizedMessage().equals("Port out of range!"))
							Driver.sop("Invalid PORT Entered - PORT OUT OF RANGE!!!; Cannot Connect to Implant. Line [" + lineNumber + "]: " + line);
						
						else
							Driver.sop("Invalid PORT Entered; Cannot Connect to Implant. Line [" + lineNumber + "]: " + line);
						
						continue;
					}
					

					//welp from here, attempt to connect to the implant!
					try
					{
						//connect to implant!
						skt = new Socket(IP, PORT);
						
						//if we get here, then the connection was successful
						++successfulConnections;
					}
					catch(Exception e)
					{
						Driver.sop("Error Binding to Target - Unable to connect to client at " + IP + ":" + PORT);
						continue;
					}
					
					//successful connection, decide which where to add the thread
					switch(idxParent)
					{
						case parentCaller_Controller:
						{
							//socket was successful, throw it into a new thread and let it take off from there. if unsuccessful, it would have been handled in the exception below
							Thread_Terminal terminal = new Thread_Terminal(connectingToImplant, connectingToController, connectingToRelay, skt);
							terminal.start();				
							
							//"link" the thread to the arraylist
							Drivers.alTerminals.add(terminal);
															
							//update number connected agents
							Drivers.updateConnectedImplants();
							Drivers.jtblConnectedImplants.updateJTable = true;
							
							break;
						}
						
						case parentCaller_Implant:
						{
							
							break;
						}
					
					}
						
						
						
					
					
					
					
					
					++lineNumber;
				}
				catch(Exception ee)
				{
					Driver.sop("Skipping Invalid line: \"" + line + "\"");
					continue;
				}
			}
			
			//garbage collection
			try {	System.gc();}catch(Exception eee){}
			
			return successfulConnections;
		}
		catch(Exception e)
		{
			Driver.eop("connectToMultipleHosts", myClassName, e, "", false);
		}
		
		try {	System.gc();}catch(Exception eee){}
		
		return successfulConnections;
	}
	
	
	
	
	
	
	
	

}
