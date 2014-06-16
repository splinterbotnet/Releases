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



package Implant;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ProcessHandlerThread extends Thread
{
  String strMyClassName = "ProcessHandlerThread";
  Process process;
  BufferedReader brIn = null;
  PrintWriter pwOut = null;

  public volatile boolean continueRun = true;

  public static volatile boolean stopProcess = false;

  String cmdLine = "";

  public ProcessHandlerThread(String command, Process proc, BufferedReader inStream, PrintWriter outStream)
  {
    try
    {
      stopProcess = false;

      this.cmdLine = "";
      this.process = proc;
      this.pwOut = outStream;
      this.brIn = inStream;

      while ((this.cmdLine = this.brIn.readLine()) != null)
      {
        if (this.pwOut != null)
        {
          this.pwOut.println(this.cmdLine); this.pwOut.flush();
        }

        if (stopProcess)
        {
          this.pwOut.println("IMMEDIATE HALT RECEIVED. TERMINATING CURRENT COMMAND: " + command); this.pwOut.flush();
          break;
        }

      }

      System.gc();

      this.brIn.close();
    }
    catch (Exception e)
    {
      Driver.eop("ProcessHandlerThread Constructor", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }
  }

  public ProcessHandlerThread(String prependHeader, String command, Process proc, BufferedReader inStream, PrintWriter outStream)
  {
    try
    {
      stopProcess = false;

      this.cmdLine = "";
      this.process = proc;
      this.pwOut = outStream;
      this.brIn = inStream;

      while ((this.cmdLine = this.brIn.readLine()) != null)
      {
        if (this.pwOut != null)
        {
          this.pwOut.println(prependHeader + this.cmdLine); this.pwOut.flush();
        }

        if (stopProcess)
        {
          this.pwOut.println("IMMEDIATE HALT RECEIVED. TERMINATING CURRENT COMMAND: " + command); this.pwOut.flush();
          break;
        }

      }

      System.gc();

      this.brIn.close();
    }
    catch (Exception e)
    {
      Driver.eop("ProcessHandlerThread Constructor", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }
  }

  public static ArrayList<String> getBufferedArrayList(BufferedReader inStream, boolean removeQuotes)
  {
    try
    {
      stopProcess = false;

      ArrayList alToBuffer = new ArrayList();
      String cmdLine = "";
      BufferedReader brIn = inStream;

      while ((cmdLine = brIn.readLine()) != null)
      {
        if (removeQuotes)
        {
          cmdLine = cmdLine.replace("\"", "");
        }

        alToBuffer.add(cmdLine);

        if (stopProcess)
        {
          break;
        }
      }

      brIn.close();

      return alToBuffer;
    }
    catch (Exception e)
    {
      Driver.eop("getBufferedArrayList", "ProcessHandlerThread", e, e.getLocalizedMessage(), false);
    }

    return null;
  }
}