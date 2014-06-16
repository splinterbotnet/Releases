/**

	Copyright:
	==========
	
	Splinter - The RAT (Remote Administrator Tool)
	Developed By Solomon Sonya, Nick Kulesza, and Dan Gunter
	Copyright 2013 Solomon Sonya
	
	This copyright applies to the entire Splinter Project and all relating source code

	This program is free software: you are free to  redistribute 
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.       

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
	
	By executing this program, you assume full responsibility 
	and will hold zero responsibility, liability, damages, etc to the
	development team over this program or any variations of this program.
	This program is not meant to be harmful or used in a malicious manner.
	
	Notes:
	===========
	This program is 100% open source and still a very BETA version. 
	I don't know of any significant bugs.... but I'm sure they may exist ;-)
	If you find one, congratulations, please forward the data back to us 
	and we'll do our best to put a fix/workaround if applicable (and time permitting...)
	Finally, feature imprevements/updates, etc, please let us know what you would
	like to see, and we'll do my best to have it incorporated into the newer 
	versions of Splinter or new projects to come.  We're here to help.
	
	Thanks again, 
	
	Solomon
	
	Contact: 
	========
	Twitter	--> @splinter_therat, @carpenter1010
	Email	--> splinterbotnet@gmail.com
	GitHub	--> https://github.com/splinterbotnet
**/




package Controller.GUI;

import Implant.Driver;
import java.io.File;
import java.util.Date;
import java.util.Vector;

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
  long myFileSize = 0L;

  public String myFileExtension_withoutPeriod = " - ";
  public String myPermissions = "- - -";
  public int myIndexInArrayList = 0;

  public boolean i_am_a_directory = false;
  public boolean i_am_a_drive = false;
  public boolean i_am_a_file = false;

  public volatile Vector<String> vctMyJTableData = new Vector();

  public Object_FileBrowser(String isDrive, String fle, String driveDisplayName, String isFileSystem, String isFloppy, String isDirectory, String isFile, String isHidden, String isTraversable, String canRead, String canWrite, String canExecute, String fullPath, String parentDirectory, String dateLastModified_long, String totalSpace, String usableSpace, String fileSize, String upDirectory)
  {
    try
    {
      this.myFile_isDrive = isDrive.trim();
      this.myFile_fle = fle.trim();
      this.myFile_driveDisplayName = driveDisplayName.trim();
      this.myFile_isFileSystem = isFileSystem.trim();
      this.myFile_isFloppy = isFloppy.trim();
      this.myFile_isDirectory = isDirectory.trim();
      this.myFile_isFile = isFile.trim();
      this.myFile_isHidden = isHidden;
      this.myFile_isTraversable = isTraversable.trim();
      this.myFile_canRead = canRead.trim();
      this.myFile_canWrite = canWrite.trim();
      this.myFile_canExecute = canExecute.trim();
      this.myFile_fullPath = fullPath.trim();
      this.myFile_parentDirectory = parentDirectory.trim();
      this.myFile_dateLastModified_long = dateLastModified_long.trim();
      this.myFile_totalSpace = totalSpace.trim();
      this.myFile_usableSpace = usableSpace.trim();
      this.myFile_Size = fileSize.trim();
      this.myFile_UpDirectory = upDirectory.trim();

      if ((this.myFile_isDrive != null) && ((this.myFile_isDrive.trim().equalsIgnoreCase("true")) || (this.myFile_isDrive.trim().equalsIgnoreCase("yes"))))
      {
        this.i_am_a_drive = true;
      }
      if ((this.myFile_isDirectory != null) && ((this.myFile_isDirectory.trim().equalsIgnoreCase("true")) || (this.myFile_isDirectory.trim().equalsIgnoreCase("yes"))))
      {
        this.i_am_a_directory = true;
      }
      if ((this.myFile_isFile != null) && ((this.myFile_isFile.trim().equalsIgnoreCase("true")) || (this.myFile_isFile.trim().equalsIgnoreCase("yes"))))
      {
        this.i_am_a_file = true;
      }

      if (this.i_am_a_file)
      {
        if ((this.myFile_driveDisplayName != null) && (!this.myFile_driveDisplayName.trim().equals("")) && (this.myFile_driveDisplayName.contains(".")) && (!this.myFile_driveDisplayName.trim().endsWith(".")))
        {
          this.myFileExtension_withoutPeriod = this.myFile_driveDisplayName.substring(this.myFile_driveDisplayName.lastIndexOf(".") + 1);
        }

      }

      try
      {
        this.myFileSize = Long.parseLong(this.myFile_Size.trim());

        this.myFile_Size = Driver.getFormattedFileSize_String(this.myFileSize);

        if ((this.myFile_Size != null) && (this.myFile_Size.trim().startsWith("0.00")))
        {
          this.myFile_Size = " - ";
        }

      }
      catch (Exception e)
      {
        this.myFile_Size = " - ";
      }

      if ((this.myFile_canRead != null) && ((this.myFile_canRead.trim().equalsIgnoreCase("yes")) || (this.myFile_canRead.trim().equalsIgnoreCase("true"))))
      {
        this.myPermissions = "r";
      }
      else
      {
        this.myPermissions = " - ";
      }

      if ((this.myFile_canWrite != null) && ((this.myFile_canWrite.trim().equalsIgnoreCase("yes")) || (this.myFile_canWrite.trim().equalsIgnoreCase("true"))))
      {
        this.myPermissions += "w";
      }
      else
      {
        this.myPermissions += " - ";
      }

      if ((this.myFile_canExecute != null) && ((this.myFile_canExecute.trim().equalsIgnoreCase("yes")) || (this.myFile_canExecute.trim().equalsIgnoreCase("true"))))
      {
        this.myPermissions += "x";
      }
      else
      {
        this.myPermissions += " - ";
      }

      try
      {
        Date date = new Date(Long.parseLong(this.myFile_dateLastModified_long.trim()));
        this.myFile_dateLastModified_long = date.toString();
      }
      catch (Exception localException1)
      {
      }

    }
    catch (Exception e)
    {
      Driver.eop("Constructor called by Controller", "Object_FileBrowser", e, e.getLocalizedMessage(), false);
    }
  }

  public Object_FileBrowser(boolean isDrive, File fle, String driveDisplayName, boolean isFileSystem, boolean isFloppy, boolean isDirectory, boolean isFile, boolean isHidden, Boolean isTraversable, boolean canRead, boolean canWrite, boolean canExecute, String fullPath, File parentDirectory, long dateLastModified_long, long totalSpace, long usableSpace, long fileSize)
  {
    try
    {
      if (isDrive)
      {
        this.myFile_isDrive = "yes";
      }
      else
      {
        this.myFile_isDrive = "no";
      }

      if (fle == null)
      {
        this.myFile_fle = "unk";
      }
      else
      {
        this.myFile_fle = (" " + fle);
      }

      if (driveDisplayName == null)
      {
        this.myFile_driveDisplayName = "unk";
      }
      else if (driveDisplayName.trim().equals(""))
      {
        this.myFile_driveDisplayName = " - ";
      }
      else
      {
        this.myFile_driveDisplayName = (" " + driveDisplayName);
      }

      if (isFileSystem)
      {
        this.myFile_isFileSystem = "yes";
      }
      else
      {
        this.myFile_isFileSystem = "no";
      }

      if (isFloppy)
      {
        this.myFile_isFloppy = "yes";
      }
      else
      {
        this.myFile_isFloppy = "no";
      }

      if (isDirectory)
      {
        this.myFile_isDirectory = "yes";
      }
      else
      {
        this.myFile_isDirectory = "no";
      }

      if (isFile)
      {
        this.myFile_isFile = "yes";
      }
      else
      {
        this.myFile_isFile = "no";
      }

      if (isHidden)
      {
        this.myFile_isHidden = "yes";
      }
      else
      {
        this.myFile_isHidden = "no";
      }

      if (isTraversable.booleanValue())
      {
        this.myFile_isTraversable = "yes";
      }
      else
      {
        this.myFile_isTraversable = "no";
      }

      if (canRead)
      {
        this.myFile_canRead = "yes";
      }
      else
      {
        this.myFile_canRead = "no";
      }

      if (canWrite)
      {
        this.myFile_canWrite = "yes";
      }
      else
      {
        this.myFile_canWrite = "no";
      }

      if (canExecute)
      {
        this.myFile_canExecute = "yes";
      }
      else
      {
        this.myFile_canExecute = "no";
      }

      if (fullPath == null)
      {
        this.myFile_fullPath = "unk";
      }
      else
      {
        this.myFile_fullPath = (" " + fullPath);
      }

      if (parentDirectory == null)
      {
        this.myFile_parentDirectory = Driver.fileSeperator;
      }
      else
      {
        this.myFile_parentDirectory = (" " + parentDirectory);

        if (this.myFile_parentDirectory.trim().equalsIgnoreCase("Computer"))
        {
          this.myFile_parentDirectory = Driver.fileSeperator;
        }

        if (this.myFile_fullPath.contains(":" + Driver.fileSeperator))
        {
          this.myFile_parentDirectory = this.myFile_fullPath.substring(0, this.myFile_fullPath.lastIndexOf(Driver.fileSeperator));
        }

        if ((this.myFile_parentDirectory.length() > 1) && (this.myFile_parentDirectory.contains(Driver.fileSeperator)))
        {
          this.myFile_UpDirectory = this.myFile_parentDirectory.substring(0, this.myFile_parentDirectory.lastIndexOf(Driver.fileSeperator));
        }
        else
        {
          this.myFile_UpDirectory = this.myFile_parentDirectory;
        }

      }

      if (dateLastModified_long < 0L)
      {
        this.myFile_dateLastModified_long = "0";
      }
      else
      {
        this.myFile_dateLastModified_long = (" " + dateLastModified_long);
      }

      if (totalSpace < 0L)
      {
        this.myFile_totalSpace = "0";
      }
      else
      {
        this.myFile_totalSpace = (" " + totalSpace);
      }

      if (usableSpace < 0L)
      {
        this.myFile_usableSpace = "0";
      }
      else
      {
        this.myFile_usableSpace = (" " + usableSpace);
      }

      if (fileSize < 0L)
      {
        this.myFile_Size = "0";
      }
      else
      {
        this.myFile_Size = (" " + fileSize);
      }
    }
    catch (Exception e)
    {
      Driver.eop("Constructor mtd called from Implant", "Object_FileBrowser", e, e.getLocalizedMessage(), false);
    }
  }

  public Vector<String> getJTableRowData()
  {
    try
    {
      this.vctMyJTableData.clear(); } catch (Exception localException) {
    }
    this.vctMyJTableData.add(this.myFileExtension_withoutPeriod);
    this.vctMyJTableData.add(this.myFile_driveDisplayName);
    this.vctMyJTableData.add(this.myFile_fullPath);
    this.vctMyJTableData.add(this.myFile_Size);
    this.vctMyJTableData.add(this.myPermissions);
    this.vctMyJTableData.add(this.myFile_dateLastModified_long);
    this.vctMyJTableData.add(""+this.myIndexInArrayList);

    return this.vctMyJTableData;
  }

  public String getFileObjectData_Socket(String delimeter)
  {
    String myLine = "UNKNOWN";
    try
    {
      return this.myFile_isDrive + delimeter + 
        this.myFile_fle + delimeter + 
        this.myFile_driveDisplayName + delimeter + 
        this.myFile_isFileSystem + delimeter + 
        this.myFile_isFloppy + delimeter + 
        this.myFile_isDirectory + delimeter + 
        this.myFile_isFile + delimeter + 
        this.myFile_isHidden + delimeter + 
        this.myFile_isTraversable + delimeter + 
        this.myFile_canRead + delimeter + 
        this.myFile_canWrite + delimeter + 
        this.myFile_canExecute + delimeter + 
        this.myFile_fullPath + delimeter + 
        this.myFile_parentDirectory + delimeter + 
        this.myFile_dateLastModified_long + delimeter + 
        this.myFile_totalSpace + delimeter + 
        this.myFile_usableSpace + delimeter + 
        this.myFile_Size + delimeter + 
        this.myFile_UpDirectory;
    }
    catch (Exception e)
    {
      Driver.eop("getFileObjectData_Socket", "Object_FileBrowser", e, e.getLocalizedMessage(), false);
    }

    return "UNKNOWN";
  }

  String getJList_FileRoots()
  {
    return " " + this.myFile_fle + " \t" + this.myFile_driveDisplayName;
  }
}