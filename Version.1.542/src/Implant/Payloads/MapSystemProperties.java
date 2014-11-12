/**
 * @author Solomon Sonya
 */

package Implant.Payloads;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.io.File;
import java.security.*;
import javax.crypto.*;

import Implant.Driver;


/**
 * The purpose of this class is to map the System Properties this implant is runnin on, and return various information when needed
 * 
 * @author Solomon Sonya & Nick Kulesza
 *
 */


public class MapSystemProperties 
{

	public static Map systemMap = null; 
	static Set systemKeys = null; 
	static Iterator systemIterator = null; 
	
	static String OS = "UNKNOWN";
	static String CurrentWorkingDirectory = "UNKNOWN";//place where ReBACAR is running out of
	static String UserHomeDirectory = "UNKNOWN";
	static String JavaVersionNumber = "UNKNOWN";
	static String OSArcitecture = "UNKNOWN";
	static String UserDesktop = "UNKNOWN";
	
	static Provider [] providers = null; 
	
	static boolean thisIsAWindowsSystem = true; 
	static boolean thisIsUNIXSystem = false; 
	
	static boolean printToStdOut = false;
	
	static String 	map_USERPROFILE = "USERPROFILE", 
			map_ProgramData = "ProgramData", 
			map_PATHEXT = "PATHEXT",
			map_JAVA_HOME = "JAVA_HOME", 
			map_TEMP = "TEMP",
			map_SystemDrive = "SystemDrive", 
			map_ProgramFiles = "ProgramFiles",
			map_HOMEDRIVE = "HOMEDRIVE", 
			map_USERDOMAIN = "USERDOMAIN",
			map_ALLUSERSPROFILE = "ALLUSERSPROFILE", 
			map_PROCESSOR_IDENTIFIER = "PROCESSOR_IDENTIFIER",
			map_TMP = "TMP", 
			map_LOGONSERVER = "LOGONSERVER",
			map_PROCESSOR_ARCHITECTURE = "PROCESSOR_ARCHITECTURE", 
			map_OS_Type = "OS",
			map_PROCESSOR_ARCHITEW6432 = "PROCESSOR_ARCHITEW6432", 
			map_HOMEPATH = "HOMEPATH",
			map_PROCESSOR_LEVEL = "PROCESSOR_LEVEL", 
			map_LOCALAPPDATA = "LOCALAPPDATA",
			map_HOSTNAME = "COMPUTERNAME", 
			map_windir = "windir",
			map_SystemRoot = "SystemRoot", 
			map_NUMBER_OF_PROCESSORS = "NUMBER_OF_PROCESSORS",
			map_USERNAME = "USERNAME", 
			map_PUBLIC = "PUBLIC",
			map_PathToCMD = "ComSpec", 
			map_APPDATA = "APPDATA",
			map_LaunchPath = "=C:", 
			map_UserDir = "user.dir",//PWD
			map_UserName = "user.name", 
			map_OS_Name = "os.name",
			map_OsVersion = "os.version",
			map_ServicePack = "sun.os.patch.level", 
			map_OS_Arch = "os.arch";
					
	public static String strUSERPROFILE = "",
			strProgramData = "",
			strPATHEXT = "",
			strJAVA_HOME = "",
			strTEMP = "",
			strSystemDrive = "",
			strProgramFiles = "",
			strHOMEDRIVE = "",
			strUSERDOMAIN = "",
			strALLUSERSPROFILE = "",
			strPROCESSOR_IDENTIFIER = "",
			strTMP = "",
			strLOGONSERVER = "",
			strPROCESSOR_ARCHITECTURE = "",
			strOS_Type = "",
			strPROCESSOR_ARCHITEW6432  = "",
			strHOMEPATH = "",
			strPROCESSOR_LEVEL = "",
			strLOCALAPPDATA = "",
			strHOSTNAME  = "",
			strwindir = "",
			strSystemRoot = "",
			strNUMBER_OF_PROCESSORS = "",
			strUSERNAME = "",
			strPUBLIC = "",
			strPathToCMD  = "",
			strAPPDATA = "",
			strLaunchPath = "",//full path including binary image name
			strLaunchDirectory = "",
			strLaunchBinary = "",
			strUserDir = "",
			strUserName  = "",
			strOsVersion = "",
			strOS_Arch  = "",
			strServicePack = "", //solo, will need to come back and implement the following using Process process = Runtime.getRuntime().exec(new String[] { "wmic", etc });  process.getOutputStream().close();
			strOS_Name = "",
			strSerialNumber = "-",
			strCountryCode = "-",
			strNumUsers = " - ";
					
					 
					
	
	public MapSystemProperties(boolean outputToStdOut)
	{
		printToStdOut = outputToStdOut;
		
		if(printToStdOut)
		{
			System.out.println("Displaying mapped System Environment...\n");
		}
		
		//Display the System Map, this will be important if we wish to navigate to the Desktop or any other folder later...
		mapSystemVariables();
				
		thisIsAWindowsSystem = this.determineIfThisIsWindows();
		
		
		
		this.mapOperatingSystem(thisIsAWindowsSystem);
		this.mapProcessorArchitecture(thisIsAWindowsSystem);
		this.mapJavaVersionNumber(thisIsAWindowsSystem);
		this.mapCurrentWorkingDirectory(thisIsAWindowsSystem);
		this.mapUserDesktop(thisIsAWindowsSystem);
		this.mapUserHomeDirectory(thisIsAWindowsSystem);
		this.mapSecurityProviders(thisIsAWindowsSystem);
		
		if(printToStdOut)
		{
			System.out.println("\n\nRequired Summary: ");
			System.out.println("This is Windows OS: " + this.systemIsWindows());
			System.out.println("OS: " + this.getOperatingSystem());
			System.out.println("Current Working Directory: " + this.getCurrentWorkingDirectory());
		
		}
		if(thisIsAWindowsSystem && printToStdOut)
			System.out.println("Path to Desktop: " + this.getUserDesktop());
		
		 
		 if(printToStdOut)
			 System.out.println("\n...Map Complete.\n\n");
	}
	
	
	/**
	 * This method returns true if ReBACAR is running from a Windows machine.  This is necessary when determining how to return the path to files, etc
	 * 
	 * @return
	 */
	public boolean determineIfThisIsWindows()
	{
		try
		{
			boolean thisIsWin = (System.getProperty("os.name")).startsWith("Windows");
			
			if(thisIsWin)
				this.thisIsUNIXSystem = false; 
			
			else//it is a unix system
				this.thisIsUNIXSystem = true;
			
			return thisIsWin;			
		}
		catch(Exception e)
		{
			System.out.println("Exception caught in determineIfThisIsWindows mtd.  \nCannot determine if this is Windows or UNIX OS");
			
			this.thisIsAWindowsSystem = false;
			this.thisIsUNIXSystem = false; 
			
			return false;
		}
	}
	
	/**
	 * The purpose of this method is to map the System variables
	 */
	public void mapSystemVariables()
	{
		try
		{
			systemMap = System.getenv();
			systemKeys = systemMap.keySet();
			systemIterator = systemKeys.iterator();
			
			//Specify how the output will be displayed
			if(printToStdOut)
			{
				System.out.println("Variable Name \t\t Variabe Values");
			}
			
			String strKey = "";
			String strVal = "";

			//Loop to get all recognized keys, then display recognized values for e/key			
			while(systemIterator.hasNext())
			{
				strKey = ((String) systemIterator.next());
				strVal = ((String) systemMap.get(strKey));//get the recognized values for the chosen key
				
				if(printToStdOut)
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
			strPROCESSOR_ARCHITEW6432 =  (String)systemMap.get(map_PROCESSOR_ARCHITEW6432); 
			strHOMEPATH = (String)systemMap.get(map_HOMEPATH);
			strPROCESSOR_LEVEL = (String)systemMap.get(map_PROCESSOR_LEVEL);
			strLOCALAPPDATA = (String)systemMap.get(map_LOCALAPPDATA);
			strHOSTNAME  = (String)systemMap.get(map_HOSTNAME );
			strwindir = (String)systemMap.get(map_windir);
			strSystemRoot = (String)systemMap.get(map_SystemRoot);
			strNUMBER_OF_PROCESSORS = (String)systemMap.get(map_NUMBER_OF_PROCESSORS);
			strUSERNAME = (String)systemMap.get(map_USERNAME);
			strPUBLIC = (String)systemMap.get(map_PUBLIC);
			strPathToCMD =  (String)systemMap.get(map_PathToCMD); 
			strAPPDATA = (String)systemMap.get(map_APPDATA);
			//strLaunchPath = (String)systemMap.get(map_LaunchPath);
			try
			{
				//strLaunchPath = (new File (".").getCanonicalPath());//this one may be more unix friendly  //<-- NOT: this works well to get the absolute launch path
				
				//these are tests we used, they work well also
				
					/*Drivers.jop("Working Directory: " + System.getProperty("user.dir"));
					String path = this.getClass().getResource("/" + this.getClass().getName().replaceAll("\\.", "/") + ".class").toString();
					path = path.substring(6).replaceFirst("/[^/]+\\.jar!.*$", "/");
					path = path.replaceAll("%20"," ");
					Driver.jop("Absolute Path to this Class File: " + path);
					Driver.jop("File: " + new File (".").getCanonicalPath());
					
					String executionPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
					executionPath = executionPath.replaceAll("%20"," ");
					executionPath = executionPath.replace("/",Driver.fileSeperator);
					if(executionPath != null && executionPath.startsWith(Driver.fileSeperator))
					{
						executionPath = executionPath.substring(1);
					}
					Driver.jop("Execution Path: " + executionPath);
				}catch(Exception e){e.printStackTrace(System.out);}*/
					
					strLaunchPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
					strLaunchPath = strLaunchPath.replace("%20"," ");
					strLaunchPath = strLaunchPath.replace("/",Driver.fileSeperator);
					
					if(strLaunchPath != null && strLaunchPath.startsWith(Driver.fileSeperator))
					{
						strLaunchPath = strLaunchPath.substring(1);
					}	
					
					try
					{
						//take the binary image name: look in the last 5 characters for the presence of a period as the extension!
						if(strLaunchPath.substring(strLaunchPath.length()-5).contains("."))
						{
							this.strLaunchBinary = strLaunchPath.substring(strLaunchPath.lastIndexOf(Driver.fileSeperator)+1);
							
							//since we were successful to extract the file name, remove it from the launch path
							strLaunchDirectory = strLaunchPath.substring(0,  strLaunchPath.lastIndexOf(Driver.fileSeperator));
							
							strLaunchDirectory = strLaunchDirectory + Driver.fileSeperator;
							
						}
						else
						{
							strLaunchDirectory = strLaunchPath;
						}
					}catch(Exception e){}
															
					
			}
			catch(Exception e)
			{
					strLaunchPath = System.getProperty("user.dir");
			}
			strUserDir = System.getProperty(map_UserDir);
			strUserName = System.getProperty(map_UserName );
			strOsVersion = System.getProperty(map_OsVersion );
			strOS_Arch  = System.getProperty(map_OS_Arch );
			strOS_Name = System.getProperty(map_OS_Name);
			strServicePack = System.getProperty(map_ServicePack);
			
			if(printToStdOut)
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
		         Driver.sop(" strHOSTNAME: " +strHOSTNAME );
		        Driver.sop(" strwindir: " + strwindir);
		        Driver.sop(" strSystemRoot: " + strSystemRoot);
		        Driver.sop(" strNUMBER_OF_PROCESSORS: " + strNUMBER_OF_PROCESSORS);
		        Driver.sop(" strUSERNAME: " + strUSERNAME);
		        Driver.sop(" strPUBLIC: " + strPUBLIC);
		        Driver.sop(" strPathToCMD: " +strPathToCMD ); 
		        Driver.sop(" strAPPDATA: " + strAPPDATA);
		        Driver.sop(" strLaunchPath: " + strLaunchPath);
		        Driver.sop(" strUserDir: " + strUserDir);
		        Driver.sop(" strUserName: " + strUserName);
		        Driver.sop(" strOsVersion: " + strOsVersion);
		        Driver.sop(" strOS_Arch: " + strOS_Arch); 
		        Driver.sop(" strOS_Name: " + strOS_Name);
			}
			
		}
		catch(Exception e)
		{
			System.out.println("Exception caught in mapSystemVariables in ReBACAR_Main");
		}
	}
	
	

	/**
	 * This method maps the Operating System ReBACAR is running on
	 */
	public boolean mapOperatingSystem(boolean thisIsWindows)
	{
		try
		{
			OS = System.getProperty("os.name");
			return true;
		}
		catch(Exception e)
		{
			return false; 
		}
		
	}
	
	/**
	 * This method maps the Security Providers present within the Java installed on this machine
	 * this code below was taken from an online source.  I can't recall where I found it
	 */
	public boolean mapSecurityProviders(boolean thisIsWindows)
	{
		try
		{
			if(printToStdOut)
			{
				System.out.print("SECURITY PROVIDERS:");
			}
			
			//get the possible security providers
			providers = Security.getProviders();
			
			String key = "", val = "";
			
			//loop to display the keys within e/provider
			for(int i = 0; i < providers.length; i++)
			{
				//Display Provider:
				if(printToStdOut)
				{
					System.out.print("\t\t" + providers[i] + ": " + "\n\t\t");
				}
				
				//Display the keys within this provider
				for(Iterator itr = ((Provider)providers[i]).keySet().iterator(); itr.hasNext();)
				{
					//Get the specific key used on the provider to get the actual val
					key = ((String)itr.next());
					
					//Convert to a text val 
					val = ((String)((Provider)providers[i]).get(key));
					
					if(printToStdOut)
					{
						System.out.print("Key: " + val + "\t");
					}
				}
				
			}
			
			if(printToStdOut)
			{
				System.out.println("\n");
			}
			
			return true;
		}
		
		catch(Exception e)
		{
			
		}
		
		return false;
	}
	
	/**
	 * This method maps the Processor System Architecture ReBACAR is running on e.g. x86
	 */
	public boolean mapProcessorArchitecture(boolean thisIsWindows)
	{
		try
		{
			OSArcitecture = System.getProperty("os.arch");
			return true;
		}
		catch(Exception e)
		{
			return false; 
		}
		
	}
	
	/**
	 * This method maps the CurrentWorkingDirectory ReBACAR is running from
	 */
	public boolean mapCurrentWorkingDirectory(boolean thisIsWindows)
	{
		try
		{
			if(thisIsWindows)
				CurrentWorkingDirectory = (System.getProperty("user.dir")).replace("\\", "/");
			
			else
				CurrentWorkingDirectory = System.getProperty("user.dir");
			
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
		
	}
	
	/**
	 * This method maps the UserHomeDirectory for the computer ReBACAR is running on e.g. /Documents and Settings/Owner
	 */
	public boolean mapUserHomeDirectory(boolean thisIsWindows)
	{
		try
		{
			if(thisIsWindows)
				UserHomeDirectory = (System.getProperty("user.home")).replace("\\", "/");
			
			else
				UserHomeDirectory = System.getProperty("user.home");
			
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
		
	}
	
	/**
	 * This method maps the UserDesktop ReBACAR is running from
	 */
	public boolean mapUserDesktop(boolean thisIsWindows)
	{
		try
		{
			if(thisIsWindows)
			{
				UserDesktop = (System.getProperty("user.home") + "/Desktop").replace("\\", "/");				
			}
			
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
		
	}
	
	/**
	 * This method maps the JavaVersionNumer for the computer ReBACAR is running on ]
	 */
	public boolean mapJavaVersionNumber(boolean thisIsWindows)
	{
		try
		{
			JavaVersionNumber = System.getProperty("java.version");
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
		
	}
	
	
	/**
	 * This method returns true if this sysetm is a Windows machine
	 */
	public boolean systemIsWindows()
	{
		return this.thisIsAWindowsSystem;
	}
	
	/**
	 * This method returns true if this sysetm is a UNIX machine
	 */
	public boolean systemIsUNIX()
	{
		return this.thisIsUNIXSystem;
	}	

	/**
	 * This method gets the Operating System ReBACAR is running on
	 */
	public String getOperatingSystem()
	{
		return OS;
	}
	
	/**
	 * This method gets the Processor System Architecture ReBACAR is running on e.g. x86
	 */
	public String getProcessorArchitecture()
	{
		return OSArcitecture;		
	}
	
	/**
	 * This method gets the CurrentWorkingDirectory ReBACAR is running from
	 */
	public String getCurrentWorkingDirectory()
	{
		return CurrentWorkingDirectory;		
	}
	
	/**
	 * This method gets the UserHomeDirectory for the computer ReBACAR is running on e.g. /Documents and Settings/Owner
	 */
	public String getUserHomeDirectory()
	{
		return UserHomeDirectory;		
	}
	
	/**
	 * This method gets the UserDesktop ReBACAR is running from
	 */
	public String getUserDesktop()
	{
		return UserDesktop;		
	}
	
	/**
	 * This method gets the JavaVersionNumer for the computer ReBACAR is running on ]
	 */
	public String getJavaVersionNumber()
	{
		return JavaVersionNumber;		
	}
	
	
	
	
	
}
