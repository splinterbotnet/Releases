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
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Frame_PrivateTerminal extends JFrame
{
  String strMyClassName = getName();

  JPanel jpnlMain = new JPanel(new BorderLayout());
  JPanel jpnlOverhead = new JPanel(new BorderLayout());
  JLabel jlblConnectedAgentInfo = null;
  JLabel jlblRunningProcessListAsOf = new JLabel("Running Process List", 0);
  public JPanel_MainControllerWindow jpnlTerminal;
  public JTable_Solomon jtblProcessList = null;
  public static String[] arrJTableProcessListColNames = { "Image Name", "PID", "Session Name", "Session#", "Mem Usage", "Status", "User Name", "CPU Time", "Window Title" };
  public static Vector<String> vctJTableProcessListColNames = new Vector(1, 1);
  public static Vector<String> vctJTableProcessListToolTips = new Vector(1, 1);

  Thread_Terminal myThread = null;

  int myIndex_in_threads_arraylist = 0;

  int myLoadIndex = 0;

  public Frame_PrivateTerminal(int loadIndex, Thread_Terminal thread)
  {
    try
    {
      this.myThread = thread;
      this.myLoadIndex = loadIndex;
      this.jpnlTerminal = new JPanel_MainControllerWindow(loadIndex, true, this.myThread);
      setTitle("Terminal @" + thread.getRHOST_IP());
      try {
        this.jpnlTerminal.myPrivateFrame = this; } catch (Exception e) { this.jpnlTerminal.myPrivateFrame = null; }


      try
      {
        setIconImage(Drivers.imgFrameIcon);
      } catch (Exception localException1) {
      }
      getContentPane().add(this.jpnlMain);

      this.jpnlMain.add("North", this.jpnlOverhead);
      this.jpnlMain.add("Center", this.jpnlTerminal);

      setBounds(500, 100, 650, 350);

      if (loadIndex != 4)
      {
        this.myIndex_in_threads_arraylist = thread.alJtxtpne_PrivatePanes.size();

        thread.alJtxtpne_PrivatePanes.add(this.jpnlTerminal.txtpne_broadcastMessages);
        try
        {
          this.jpnlTerminal.txtpne_broadcastMessages.myMainControllerWindow = this.jpnlTerminal;
        }
        catch (Exception localException2) {
        }
      }
      this.jlblConnectedAgentInfo = new JLabel("Terminal is connected to " + thread.getJListData(), 0);
      this.jlblConnectedAgentInfo.setFont(new Font("Courier", 1, 18));
      this.jlblRunningProcessListAsOf.setFont(new Font("Courier", 1, 14));

      this.jpnlOverhead.add("North", this.jlblConnectedAgentInfo);

      validate();
      try
      {
        this.jpnlTerminal.jtfTerminalCommand.grabFocus();
      } catch (Exception localException3) {
      }
    } catch (Exception e) {
      Drivers.eop("Frame_PrivateTerminal Constructor", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    setDefaultCloseOperation(0);

    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        Frame_PrivateTerminal.this.closeFrame();
      }
    });
  }

  public void closeFrame()
  {
    try
    {
      if (this.myLoadIndex == 4)
      {
        this.myThread.sendCommand_RAW("[SPLINTER_IMPLANT]%%%%%STOP_RUNNING_PS");
      }

      if (this.myLoadIndex != 4)
      {
        this.myThread.alJtxtpne_PrivatePanes.remove(this.jpnlTerminal.txtpne_broadcastMessages);
      }

      System.gc();
      setVisible(false);
      dispose();
    }
    catch (Exception e)
    {
      Drivers.eop("closeFrame", this.strMyClassName, e, e.getLocalizedMessage(), false);
      System.gc();
      dispose();
    }
  }
}