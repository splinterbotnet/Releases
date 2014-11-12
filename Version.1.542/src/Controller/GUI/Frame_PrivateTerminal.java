/**
 * The purpose of this class is to display a frame for the user to have a private terminal just with the connected client
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

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
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

import Controller.Thread.Thread_Terminal;
import Implant.Driver;

import java.io.*;
import java.awt.GridBagLayout;

public class Frame_PrivateTerminal extends JFrame //implements ActionListener <-- ActionEvents handled in the JPanel_MainControllerWindow!
{

	String strMyClassName = this.getName();
	
	JPanel jpnlMain = new JPanel(new BorderLayout());
	JPanel jpnlOverhead = new JPanel(new BorderLayout());
		JLabel jlblConnectedAgentInfo = null;
		JLabel jlblRunningProcessListAsOf = new JLabel("Running Process List", JLabel.CENTER);
	public JPanel_MainControllerWindow jpnlTerminal;
	public JTable_Solomon jtblProcessList = null;
	public static String [] arrJTableProcessListColNames  = new String[]{"Image Name","PID","Session Name","Session#","Mem Usage","Status","User Name","CPU Time","Window Title"};		
	public static Vector<String> vctJTableProcessListColNames = new Vector<String>(1,1);
	public static Vector<String> vctJTableProcessListToolTips = new Vector<String>(1,1);
	
	Thread_Terminal myThread = null;
	
	int myIndex_in_threads_arraylist = 0;
	
	int myLoadIndex = 0;
	
	public Frame_PrivateTerminal(int loadIndex, Thread_Terminal thread)
	{
		try
		{
			//save my thread
			myThread = thread;
			myLoadIndex = loadIndex;		
			jpnlTerminal = new JPanel_MainControllerWindow(loadIndex, true, myThread);
			this.setTitle("Terminal @" + thread.getRHOST_IP());
			
			try{jpnlTerminal.myPrivateFrame = this;}catch(Exception e){jpnlTerminal.myPrivateFrame = null;}
			
			
			try	
			{	//RAT ICON

				//ImageIcon img = new ImageIcon(this.getClass().getResource("/Images/RAT_Icon.gif"));				
				//this.setIconImage(img.getImage());
				this.setIconImage(Drivers.imgFrameIcon);
			}	catch(Exception e){}
			
			getContentPane().add(this.jpnlMain);
			
			this.jpnlMain.add(BorderLayout.NORTH, this.jpnlOverhead);
			this.jpnlMain.add(BorderLayout.CENTER, this.jpnlTerminal);
			
			setBounds(500, 100, 650, 350);
			
			if(loadIndex != JPanel_MainControllerWindow.INDEX_THIS_IS_RUNNING_PROCESS_)
			{
				//save my index to know where to remove from in the future
				myIndex_in_threads_arraylist = thread.alJtxtpne_PrivatePanes.size();
				
				//save my text pane to the new thread so it knows where to post messages to
				thread.alJtxtpne_PrivatePanes.add(jpnlTerminal.txtpne_broadcastMessages);
				
				//save a pointer to this frame as well
				try{jpnlTerminal.txtpne_broadcastMessages.myMainControllerWindow = jpnlTerminal;}catch(Exception e){}
			}
			
			//append notification message to the user of the private terminal
			//this.jpnlTerminal.txtpne_broadcastMessages.appendStatusMessageString(true, "Terminal is connected to " + thread.getJListData());
			jlblConnectedAgentInfo = new JLabel("Terminal is connected to " + thread.getJListData(), JLabel.CENTER);
			jlblConnectedAgentInfo.setFont(new Font("Courier", Font.BOLD, 18));
			jlblRunningProcessListAsOf.setFont(new Font("Courier", Font.BOLD, 14));
			
			this.jpnlOverhead.add(BorderLayout.NORTH, jlblConnectedAgentInfo);
			
			
			
			
			
			this.validate();
			
			
			//LEAVE THIS AS THE LAST STATEMENT
			try{	jpnlTerminal.jtfTerminalCommand.grabFocus();	}catch(Exception e){}	
		}
		catch(Exception e)
		{
			Drivers.eop("Frame_PrivateTerminal Constructor", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);


		this.addWindowListener(new java.awt.event.WindowAdapter() 
		{
			public void windowClosing(java.awt.event.WindowEvent e) 
			{
				closeFrame(); 			
			}
		});

	}
	
	
	
	public void closeFrame()
	{
		try
		{
			if(myLoadIndex == JPanel_MainControllerWindow.INDEX_THIS_IS_RUNNING_PROCESS_)
			{
				myThread.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.STOP_RUNNING_PROCESS_LIST);
			}
			
			//remove myself from the thread's arraylist
			if(this.myLoadIndex != JPanel_MainControllerWindow.INDEX_THIS_IS_RUNNING_PROCESS_)
			{
				myThread.alJtxtpne_PrivatePanes.remove(jpnlTerminal.txtpne_broadcastMessages);			
			}
						
			System.gc();
			this.setVisible(false);
			this.dispose();
			
		}
		catch(Exception e)
		{
			Drivers.eop("closeFrame", strMyClassName, e, e.getLocalizedMessage(), false);
			System.gc();
			this.dispose();
		}
	}
}
