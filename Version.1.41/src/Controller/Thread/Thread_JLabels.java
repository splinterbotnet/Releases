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



package Controller.Thread;

import Controller.Drivers.Drivers;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Thread_JLabels extends Thread
  implements Runnable, ActionListener
{
  String strMyClassName = "Thread_JLabels";
  JLabel jlblCurrentHeap;
  JLabel jlblAvailableHeap;
  JLabel jlblMaxHeap;
  JLabel jlblCurrentTime;
  JLabel jlblZuluTime;
  Timer tmrUpdate = null;
  Date dateTime;
  String timeToSplit = "";
  String dateTime_To_Display = "";
  String[] arrSplitTime;
  String[] arrSplitTimeZone;
  String[] arrSplitHour;
  String strHourMin = "";
  String strAvailTimeZones = "";
  String strTimeZone = "";

  SimpleDateFormat dateFormat = new SimpleDateFormat("EE - dd MMM yyyy - HH:mm \"ss - zzzz");
  static Date dateZulu;
  static TimeZone zulu;
  static SimpleDateFormat dateFormat_Zulu = new SimpleDateFormat("HH:mm \"ss");

  public Thread_JLabels(JLabel jlblCurrentHp, JLabel jlblAvailHeap, JLabel jlblMaxHp, JLabel jlblCurrTime, JLabel jlblZulTime)
  {
    this.jlblCurrentHeap = jlblCurrentHp;
    this.jlblAvailableHeap = jlblAvailHeap;
    this.jlblMaxHeap = jlblMaxHp;
    this.jlblCurrentTime = jlblCurrTime;
    this.jlblZuluTime = jlblZulTime;
  }

  public void run()
  {
    initializeZuluTime();

    this.tmrUpdate = new Timer(1000, this);
    this.tmrUpdate.start();
  }

  public void actionPerformed(ActionEvent ae)
  {
    try
    {
      if (ae.getSource() == this.tmrUpdate)
      {
        updateJLabels();
      }

    }
    catch (Exception e)
    {
      Drivers.eop("ae", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }
  }

  public boolean updateJLabels()
  {
    try
    {
      this.jlblCurrentHeap.setText(getFormattedFileSize_String(Runtime.getRuntime().totalMemory()));
      this.jlblAvailableHeap.setText(getFormattedFileSize_String(Runtime.getRuntime().freeMemory()));
      this.jlblMaxHeap.setText(getFormattedFileSize_String(Runtime.getRuntime().maxMemory()));

      this.jlblCurrentTime.setText(getTimeStamp_Updated());
      this.jlblZuluTime.setText(getZuluTimeStamp_Without_Date());

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("updateJLabels", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public static String getFormattedFileSize_String(long fileSize_bytes)
  {
    try
    {
      double fileSize = fileSize_bytes;

      DecimalFormat formatter = new DecimalFormat("###.00");

      if (fileSize_bytes < 1000L)
      {
        return formatter.format(fileSize) + " B";
      }

      if ((fileSize_bytes > 1L) && (fileSize_bytes < 99000.0D))
      {
        return formatter.format(fileSize_bytes / 1000.0D) + " KB";
      }

      if ((fileSize_bytes > 1000.0D) && (fileSize_bytes < 99000000.0D))
      {
        return formatter.format(fileSize_bytes / 1000000.0D) + " MB";
      }

      if ((fileSize_bytes > 1000000.0D) && (fileSize_bytes < 99000000000.0D))
      {
        return formatter.format(fileSize_bytes / 1000000000.0D) + " GB";
      }

      if ((fileSize_bytes > 1000000000.0D) && (fileSize_bytes < 99000000000000.0D))
      {
        return formatter.format(fileSize_bytes / 1000000000000.0D) + " TB";
      }
      if ((fileSize_bytes > 1000000000000.0D) && (fileSize_bytes < 99000000000000000.0D))
      {
        return formatter.format(fileSize_bytes / 1000000000000000.0D) + " PB";
      }

      return formatter.format(fileSize_bytes) + " B";
    }
    catch (Exception e)
    {
      Drivers.eop("getFormattedFileSize_String", "Thread_JLabels", e, e.getMessage(), true);

      Drivers.sop("Unable to determine File Size");
    }
    return ""+fileSize_bytes;
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
      return strHourMin = arrSplitHour[0] + ":" + arrSplitHour[1] + "\"" + arrSplitHour[2];
    }
    catch (Exception e)
    {
      dateTime_To_Display = "Time: " + dateTime.toString();
    }

    return dateTime_To_Display;
  }

  public void initializeZuluTime()
  {
    try
    {
      dateZulu = new Date();
      zulu = TimeZone.getTimeZone("Zulu");
      dateFormat_Zulu.setTimeZone(zulu);
    }
    catch (Exception e)
    {
      System.out.println("Exception caught initializeZuluTime mtd");
    }
  }

  public static String getZuluTimeStamp_Without_Date()
  {
    String dateTime_To_Display = "";
    try
    {
      dateZulu = new Date();
      return dateFormat_Zulu.format(dateZulu) + " - Zulu";
    }
    catch (Exception e)
    {
      String timeToSplit = "";

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
        return strHourMin = arrSplitHour[0] + ":" + arrSplitHour[1] + "\"" + arrSplitHour[2];
      }
      catch (Exception ee)
      {
        dateTime_To_Display = "Time: " + dateTime.toString();
      }
    }

    return dateTime_To_Display;
  }

  public String getTimeStamp_Updated()
  {
    try
    {
      this.dateTime = new Date();

      this.timeToSplit = this.dateFormat.format(this.dateTime);

      this.arrSplitTime = this.timeToSplit.split("-");

      this.arrSplitTimeZone = this.arrSplitTime[3].split(" ");

      return this.arrSplitTime[0] + " - " + this.arrSplitTime[1] + " - " + this.arrSplitTime[2] + "- " + this.arrSplitTimeZone[1] + "         ";
    }
    catch (Exception e)
    {
      try
      {
        return getTimeStamp_Without_Date();
      }
      catch (Exception ee) {
      }
    }
    return this.dateTime_To_Display = "Time: " + this.dateTime.toString();
  }
}