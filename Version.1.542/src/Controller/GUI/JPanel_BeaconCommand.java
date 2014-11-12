/**
 * This class sets up the GUYI to allow the user to a command for a beacon agent to execute upon checkin
 * 
 * @author Solomon Sonya
 */

package Controller.GUI;

import Controller.Drivers.*;
import Controller.Thread.*;
import Implant.*;
import Implant.FTP.FTP_ServerSocket;
import Implant.FTP.FTP_Thread_Receiver;
import Implant.Payloads.Frame_SpoofUAC;
import Implant.Payloads.MapSystemProperties;
import Implant.Payloads.Worker_Thread_Payloads;


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

public class JPanel_BeaconCommand extends JPanel implements ActionListener
{
	public static String strMyClassName = "JPanel_BeaconCommand";
	
	public int myIndex = 0;
	
	public JLabel jlblExtMsg = new JLabel(" ");
	
	public JPanel jpnlAlignment_jpnlIndex = new JPanel(new BorderLayout());
		public JPanel jpnlIndex = new JPanel();	
			public JLabel jlblIndex = new JLabel("[ 0 ]");	
			public JLabel jlblCommand = new JLabel("Command:  ");
	
	public JPanel jpnlJTextField = new JPanel(new BorderLayout());
		public JTextField jtfCommand = new JTextField(25);
	
	public JPanel jpnlAlignment_jpnlOptions = new JPanel(new BorderLayout());
		public JPanel jpnlOptions = new JPanel();
			public JButton jbtnAddBeaconCommand = new JButton("Add New Command");
			public JButton jbtnDeleteThisCommand = new JButton("Delete This Command");
			public JCheckBox jcbEnable = new JCheckBox("Enable");
			public JComboBox jcbShortCuts = new JComboBox();
				
	
	public Font fntJLbl = new Font("Tahoma", Font.PLAIN, 16);
	public Font fntJtf = new Font("Tahoma", Font.PLAIN, 14);
	
	public JPanel_BeaconCommand()
	{
		this.setLayout(new BorderLayout());
		
		this.add(BorderLayout.NORTH, jlblExtMsg);
		
		jpnlIndex.add(jlblIndex);
		jpnlIndex.add(jlblCommand);
		jpnlAlignment_jpnlIndex.add(BorderLayout.NORTH, jpnlIndex);
			this.add(BorderLayout.WEST, jpnlAlignment_jpnlIndex);
			
		jpnlJTextField.add(BorderLayout.NORTH, jtfCommand);
			this.add(BorderLayout.CENTER, jpnlJTextField);
			
		jcbShortCuts.setModel(new DefaultComboBoxModel(Driver.arrShortCuts));
		jpnlOptions.add(jcbShortCuts);
		jpnlOptions.add(jbtnDeleteThisCommand);
		jpnlOptions.add(jbtnAddBeaconCommand);
		jpnlOptions.add(jcbEnable);
		jpnlAlignment_jpnlOptions.add(BorderLayout.NORTH, jpnlOptions);
			this.add(BorderLayout.EAST, jpnlAlignment_jpnlOptions);
		
		this.jlblCommand.setFont(fntJLbl);
		this.jlblIndex.setFont(fntJLbl);
		this.jtfCommand.setFont(this.fntJtf);
		
		//this.jlblCommand.setOpaque(true);
		this.jlblIndex.setOpaque(true);
		
		//this.jlblCommand.setBackground(Color.red);
		this.jlblIndex.setBackground(Color.red);
		
		//this.jlblCommand.setForeground(Color.white);
		this.jlblIndex.setForeground(Color.white);
		
		this.jcbEnable.addActionListener(this);
		this.jcbShortCuts.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if(ae.getSource() == this.jcbEnable)
			{
				if(this.jcbEnable.isSelected())
				{
					this.jlblIndex.setBackground(Color.green.darker().darker());
				}
				else
				{
					this.jlblIndex.setBackground(Color.red);
				}
			}
			
			else if(ae.getSource() == this.jcbShortCuts)
			{
				if(this.jcbShortCuts.getSelectedIndex() > 0)
				{
					this.jtfCommand.setText("" + this.jcbShortCuts.getSelectedItem());
				}
			}
			
			
		}
		catch(Exception e)
		{
			Driver.eop("ae", strMyClassName, e, "", false);
		}
		
		
	}
	
	public boolean setIndex(int index)
	{
		try
		{
			this.myIndex = index;
			
			if(myIndex < 0)
				myIndex = 0;
			
			if(myIndex < 10)
			{
				this.jlblIndex.setText("[ 0" + myIndex + " ]");
			}
			else
			{
				this.jlblIndex.setText("[ "+myIndex + " ]");
			}
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("setIndex", strMyClassName, e, "", false);
		}
		
		return false;
	}

}
