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




package Implant.Payloads;

import Controller.Drivers.Drivers;
import Controller.Thread.Thread_Terminal;
import Implant.Driver;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Frame_Render_Captured_Screen extends JFrame
  implements ActionListener
{
  public static final String strMyClassName = "Render_Captured_Screen";
  JPanel jpnlHeading = new JPanel(new BorderLayout());
  JLabel jlblHeading = new JLabel("SCREEN SCRAPE", 0);
  JPanel jpnlRefreshTimes = new JPanel();
  JLabel jlblRefreshTime = new JLabel("Refresh Time: UKN");
  JLabel jlblRefreshRate = new JLabel("Refresh Rate: UKN");

  JPanel jpnlTabs = new JPanel(new GridLayout(1, 1));
  JTabbedPane tabbedPane_Main = new JTabbedPane(1);

  JPanel jpnlRenderOptions = new JPanel(new BorderLayout());
  JPanel jpnlButton_South = new JPanel();
  JButton jbtnSetRenderDirectory = new JButton("Set Render Directory");
  JLabel jlblRenderDirectory = new JLabel("Set Render Driectory");
  public static final String SCREEN_TITLE = "Screen Scrape [";
  public boolean deleteFileAfterRender = false;

  public File diretoryToScan = null;

  public Worker_Thread_Payloads myWorkerThread = null;
  public Thread_Terminal myTerminal = null;

  long myLastConnectionTime_millis = 0L;
  String interval = "0:00";

  Worker_Thread_Payloads parent_worker_thread = null;

  public Frame_Render_Captured_Screen(String title, File directoryLocationToScan, boolean delAfterRender, Worker_Thread_Payloads workerPar)
  {
    try
    {
      this.deleteFileAfterRender = delAfterRender;
      this.parent_worker_thread = workerPar;

      this.diretoryToScan = directoryLocationToScan;

      setBounds(60, 100, 600, 500);
      setLayout(new BorderLayout());

      this.jlblHeading.setText(title);

      this.jlblHeading.setForeground(Color.blue.darker());
      this.jlblHeading.setFont(new Font("Courier", 1, 18));
      this.jpnlHeading.add("North", this.jlblHeading);

      this.jlblRefreshRate.setFont(new Font("Courier", 1, 16));
      this.jlblRefreshTime.setFont(new Font("Courier", 1, 16));

      this.jpnlRefreshTimes.add(this.jlblRefreshTime);
      this.jpnlRefreshTimes.add(this.jlblRefreshRate);
      this.jlblRefreshRate.setVisible(false);

      this.jpnlHeading.add("South", this.jpnlRefreshTimes);
      add("North", this.jpnlHeading);

      add("Center", this.jpnlTabs);
      this.jpnlTabs.add(this.tabbedPane_Main);

      this.jpnlButton_South.add(this.jbtnSetRenderDirectory);
      this.jpnlRenderOptions.add("West", this.jpnlButton_South);
      this.jpnlRenderOptions.add("Center", this.jlblRenderDirectory);
      add("South", this.jpnlRenderOptions);
      this.jbtnSetRenderDirectory.addActionListener(this);

      Drivers.setJFrameIcon(this);
      setTitle("Splinter - RAT vrs 1.41 - BETA");

      Driver.sop("\nRendering Screen Captures in Directory: " + directoryLocationToScan + "\n");

      if (directoryLocationToScan != null)
      {
        this.jlblRenderDirectory.setText("Scrape DropBox: " + directoryLocationToScan.getCanonicalPath());
      }

      this.myLastConnectionTime_millis = Driver.getTime_Current_Millis();

      scanDirectory(this.diretoryToScan);

      addWindowListener(new WindowAdapter()
      {
        public void windowClosing(WindowEvent e)
        {
          Frame_Render_Captured_Screen.this.closeFrame();
        }
      });
      validate();
    }
    catch (Exception e)
    {
      Driver.eop("Constructor", "Render_Captured_Screen", e, "", false);
    }
  }

  public void actionPerformed(ActionEvent ae)
  {
    try
    {
      if (ae.getSource() == this.jbtnSetRenderDirectory)
      {
        File selectedFile = Driver.querySelectFile(false, "Please Select Directory to render captured files", 1, false, false);

        if (selectedFile != null)
        {
          this.diretoryToScan = selectedFile;
          this.jlblRenderDirectory.setText("Scrape DropBox: " + this.diretoryToScan.getCanonicalPath());

          if (this.parent_worker_thread != null)
          {
            this.parent_worker_thread.fleRenderDirectory = this.diretoryToScan;
          }
        }

      }

    }
    catch (Exception e)
    {
      Driver.eop("ae", "Render_Captured_Screen", e, "", false);
    }
  }

  public boolean closeFrame()
  {
    try
    {
      if (this.myWorkerThread != null)
      {
        this.myWorkerThread.killThisThread = true;
      }

      if (this.myTerminal != null)
      {
        Drivers.appendCommandTerminalStatusMessage("Terminal connected, sending Screen Scrape kill command now...");
        this.myTerminal.sendCommand_RAW("DISABLE RECORD SCREEN");
      }

      System.gc();
      dispose();
    }
    catch (Exception e)
    {
      Drivers.eop("closeFrame", "Render_Captured_Screen", e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean scanDirectory(File fleDirectoryToScan)
  {
    try
    {
      if ((fleDirectoryToScan == null) || (!fleDirectoryToScan.exists()) || (!fleDirectoryToScan.isDirectory()))
      {
        return false;
      }

      ArrayList allImages = Driver.getFilesUnderSelectedDirectory(fleDirectoryToScan, new String[] { "png" });

      if ((allImages == null) || (allImages.size() < 1))
      {
        return false;
      }

      ArrayList imagesToRender = new ArrayList();
      imagesToRender = allImages;

      if ((imagesToRender == null) || (imagesToRender.size() < 1))
      {
        return false;
      }

      File fleImage = null;
      JPanel_RenderedScreen renderedScreenObject = null;
      String strToCheck = "";

      for (int i = 0; i < imagesToRender.size(); i++)
      {
        fleImage = (File)imagesToRender.get(i);

        if ((fleImage != null) && (fleImage.exists()) && (fleImage.isFile()))
        {
          for (int j = 0; j < imagesToRender.size(); j++)
          {
            try
            {
              if (fleImage.getCanonicalPath().trim().endsWith("_" + j + "_" + "record" + "." + "png"))
              {
                int indexToModify = this.tabbedPane_Main.indexOfTab("Screen Scrape [" + j + "]");

                if (indexToModify < 0)
                {
                  if ((fleImage == null) || (!fleImage.isFile()) || (fleImage.length() < 1L))
                  {
                    continue;
                  }

                  renderedScreenObject = new JPanel_RenderedScreen(fleImage);

                  this.tabbedPane_Main.addTab("Screen Scrape [" + j + "]", renderedScreenObject);
                }
                else
                {
                  JPanel_RenderedScreen currTab = (JPanel_RenderedScreen)this.tabbedPane_Main.getComponent(indexToModify);
                  currTab.setImage(fleImage);
                }

                System.gc();

                if (this.deleteFileAfterRender) {
                  try {
                    fleImage.delete(); } catch (Exception e) { Driver.sop("Could not delete file: " + fleImage.getCanonicalPath()); }


                }

              }

              if (j == 0)
              {
                this.interval = Driver.getTimeInterval(Driver.getTime_Current_Millis(), this.myLastConnectionTime_millis);
                this.myLastConnectionTime_millis = Driver.getTime_Current_Millis();

                this.jlblRefreshTime.setText("Last Refresh Time: " + Driver.getTimeStamp_Without_Date());
              }

            }
            catch (Exception localException1)
            {
            }

          }

        }

      }

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("scanDirectory", "Render_Captured_Screen", e, "", true);
    }

    return false;
  }
}