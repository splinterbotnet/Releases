/**
 * Worker thread to update JLabels every second
 * @author Solomon Sonya 
 *
 */



package Controller.Thread;

import Controller.Drivers.*;
import Controller.Thread.*;
import Implant.*;


import java.awt.Frame;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.*;
import java.lang.*;
import java.awt.Frame;
import javax.swing.border.*;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.awt.Rectangle;
import java.awt.BorderLayout;
import javax.swing.JLabel ;
import java.lang.*;
import java.awt.Label;
import java.awt.event.KeyEvent;
import java.awt.Panel;
import java.awt.SystemColor;
import javax.swing.JRadioButton;
import java.text.*;
import java.util.*;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.event.*;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.ScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import java.awt.Button;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Choice;
import javax.swing.JComboBox;
import java.net.*;
import javax.swing.WindowConstants;
import javax.swing.table.*;

import java.io.*;
import java.awt.GridBagLayout;



public class Thread_JLabels extends Thread implements Runnable, ActionListener
{
	public static String strMyClassName = "Thread_JLabels";
	
	JLabel jlblCurrentHeap, jlblAvailableHeap, jlblConsumedHeap, jlblMaxHeap, jlblCurrentTime, jlblZuluTime, jlblUpTime, jlblUpTime_Day, jlblUpTime_Hour, jlblUpMinute, jlblUpTime_Second;
	
	Timer tmrUpdate = null;
	
	
	
Date dateTime;// = new Date();//get the current date, time, timezone  //  @jve:decl-index=0:

	
	String timeToSplit = "";
	String dateTime_To_Display = "";  //  @jve:decl-index=0:
	String [] arrSplitTime;
	String [] arrSplitTimeZone;
	String [] arrSplitHour;
	String strHourMin = "";
	String strAvailTimeZones = "";
	String strTimeZone = "";
	
	public volatile static long time_to_call_garbage_collection = 0;
	
	//MM/dd/yyyy HH:mm:ss z zzzz Z");
	//Get the Time Stamp in the raw format "Tue - 30 Sep 2008 - 23:58:21 - Central Daylight Time"
	SimpleDateFormat dateFormat = new SimpleDateFormat("EE - dd MMM yyyy - HH:mm \"ss - zzzz");
	static Date dateZulu;
	static TimeZone zulu;
	static SimpleDateFormat dateFormat_Zulu = new SimpleDateFormat("HH:mm \"ss");
	
	static int secs_millis  = 1000;
	static int mins_millis  = 1000 * 60; 
	static int hours_millis = 1000 * 60 * 60;
	static int days_millis  = 1000 * 60 * 60 * 24;
	static long elapsedTime = 0;
	
	
	public Thread_JLabels(JLabel jlblCurrentHp, JLabel jlblAvailHeap, JLabel jlblConsumedHp, JLabel jlblMaxHp, JLabel jlblCurrTime, JLabel jlblZulTime, JLabel jlblUptime)
	{
		
		
		jlblCurrentHeap = jlblCurrentHp;
		jlblAvailableHeap = jlblAvailHeap;
		jlblConsumedHeap = jlblConsumedHp;
		jlblMaxHeap = jlblMaxHp;
		jlblCurrentTime = jlblCurrTime;
		jlblZuluTime = jlblZulTime;
		
		jlblUpTime = jlblUptime;
	}
	
	public void run()
	{
		initializeZuluTime();
		
		tmrUpdate = new Timer(1000, this);
		tmrUpdate.start();
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if(ae.getSource() == tmrUpdate)
			{
				updateJLabels();
			}
			
		}
		catch(Exception e)
		{
			Drivers.eop("ae", strMyClassName, e, e.getLocalizedMessage(), false);
		}
	}
	
	public boolean updateJLabels()
	{
		try
		{
			jlblCurrentHeap.setText("" + this.getFormattedFileSize_String(Runtime.getRuntime().totalMemory()));
			jlblAvailableHeap.setText("" + this.getFormattedFileSize_String(Runtime.getRuntime().freeMemory()));
			jlblMaxHeap.setText("" + this.getFormattedFileSize_String(Runtime.getRuntime().maxMemory()));
			//jlblCurrentTime.setText("" + getTimeStamp_Without_Date());
			//jlblZuluTime.setText("" + getTimeStamp_Without_Date());
			jlblCurrentTime.setText("" + getTimeStamp_Updated());
			jlblZuluTime.setText("" + this.getZuluTimeStamp_Without_Date());
			
			try
			{
				this.jlblConsumedHeap.setText("" + this.getFormattedFileSize_String(Math.abs(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) ));
			}
			catch(Exception ee)
			{
				this.jlblConsumedHeap.setText(" - - -");
			}
			
						
			
			//
			//update garbage collection
			//
			try
			{
				time_to_call_garbage_collection = ((++time_to_call_garbage_collection)%10);
				
				if(time_to_call_garbage_collection == 0)
				{
					try	{ System.gc();	} catch(Exception e){}
				}
			}
			catch(Exception e)
			{
				
			}
			
			//
			//update up time
			//
			try
			{
				elapsedTime = Math.abs(System.nanoTime() - Driver.startTime);
				
				/*jlblUpTime_Day.setText("" + (int) elapsedTime / days_millis);
				
				//bleed off days
				elapsedTime %= days_millis;
				
				jlblUpTime_Hour.setText("" + (int) elapsedTime / hours_millis);
				
				//bleed off hours
				elapsedTime %= hours_millis;
				
				jlblUpMinute.setText("" + (int) elapsedTime / mins_millis);
				
				//bleed off mins
				elapsedTime %= mins_millis;
				
				jlblUpTime_Second.setText("" + (int) elapsedTime / secs_millis);*/
			
				
				jlblUpTime.setText(Driver.getTimeInterval_WithDays(System.currentTimeMillis(), (Driver.startTime)));
			}
			catch(Exception ee)
			{
				jlblUpTime_Day.setText("00");
				jlblUpTime_Hour.setText("00");
				jlblUpMinute.setText("00");
				jlblUpTime_Second.setText("00");
			}
			
			
			
			return true;
		}
		
		catch(Exception e)
		{
			Drivers.eop("updateJLabels", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	/**
	 * NOTE: DUPLICATE METHOD IS BEING PLACED IN THE DRIVER CLASS AS WELL!!!
	 * @param fileSize_bytes
	 * @return
	 */
	static public String getFormattedFileSize_String(long fileSize_bytes)    
    {
        try
        {
        	double fileSize = fileSize_bytes; 
        	
        	//DecimalFormat formatter = new DecimalFormat("###.##");
        	DecimalFormat formatter = new DecimalFormat("###.00");

             if (fileSize_bytes < 1000)
             {
                  return  formatter.format(fileSize) + " B";
             }

             else if (fileSize_bytes > 1 && fileSize_bytes < 99e3)//kb
             {
            	 return  formatter.format(fileSize_bytes / 1e3) + " KB";
             }

             else if (fileSize_bytes > 1e3 && fileSize_bytes < 99e6)//mb
             {
            	 return  formatter.format(fileSize_bytes / 1e6) + " MB";
             }

             else if (fileSize_bytes > 1e6 && fileSize_bytes < 99e9)//gb
             {
            	 return  formatter.format(fileSize_bytes / 1e9) + " GB";
             }

             else if (fileSize_bytes > 1e9 && fileSize_bytes < 99e12)//tb
             {
            	 return  formatter.format(fileSize_bytes / 1e12) + " TB";
             }
             else if (fileSize_bytes > 1e12 && fileSize_bytes < 99e15)//pb
             {
            	 return  formatter.format(fileSize_bytes / 1e15) + " PB";
             }
             else//default
             {
            	 return  formatter.format(fileSize_bytes) + " B";
             }                  
            
        }
        catch (Exception e)
        {
        	Drivers.eop("getFormattedFileSize_String", strMyClassName, e, e.getLocalizedMessage(), false);        	
        }

        Drivers.sop("Unable to determine File Size");
        
        return ""+fileSize_bytes;
    }
	
	/**
	 * This method just returns the current system clock time in the format HH:MM "SS
	 */
	public static String getTimeStamp_Without_Date()
	{
		Date dateTime;// = new Date();//get the current date, time, timezone  //  @jve:decl-index=0:

		String timeToSplit = "";
		String dateTime_To_Display = "";  //  @jve:decl-index=0:
		String [] arrSplitTime;
		String [] arrSplitHour;
		String strHourMin = "";
		
		dateTime = new Date();
		
		try
		{	
			timeToSplit = dateTime.toString();
						
			arrSplitTime = timeToSplit.split(" ");//return an array with Day_Name Mon Day_Num HH:MM:SS LocalTime YYYY
						
			if(arrSplitTime.length != 6)//ensure array was split properly; if it's length is not 6, then an error occurred, so just show the simple time by throwing the exception
				throw new Exception();
			
			arrSplitHour = (arrSplitTime[3]).split(":");
			
			if(arrSplitHour.length != 3)//again, ensure we split the time from 18:57:48 bcs we only want 18:57
				throw new Exception();
			
			return strHourMin = arrSplitHour[0] + ":" + arrSplitHour[1] + "\"" + arrSplitHour[2];  
			
			
		}//end try
		catch(Exception e)//incase an error is generated from the above parse of the time and date, simply display the generic date for the client
		{
			dateTime_To_Display = "Time: " + dateTime.toString();
		}
		
		return dateTime_To_Display;		
	}//end mtd getTimeStamp
	
	
	/**
	 * This method initizlizes the calls to get Zulu Time calculating for us automatically
	 */
	public void initializeZuluTime()
	{
		try
		{
			dateZulu = new Date();
			zulu = TimeZone.getTimeZone("Zulu");
			dateFormat_Zulu.setTimeZone(zulu);
		}
		catch(Exception e)
		{
			System.out.println("Exception caught initializeZuluTime mtd");
		}
	}
	
	/**
	 * This method just returns the current system clock time in the format HH:MM "SS
	 */
	public static String getZuluTimeStamp_Without_Date()
	{
		String dateTime_To_Display = "";  //  @jve:decl-index=0:
		
		try
		{
			//DISPLAY THE ZULU TIME!
			dateZulu = new Date();//must always re-init in order to get the curr date and time
			return dateFormat_Zulu.format(dateZulu) + " - Zulu";
		}
		catch(Exception e)
		{		
			Date dateTime;// = new Date();//get the current date, time, timezone  //  @jve:decl-index=0:
	
			String timeToSplit = "";
			
			String [] arrSplitTime;
			String [] arrSplitHour;
			String strHourMin = "";
			
			dateTime = new Date();
			
			try
			{	
				timeToSplit = dateTime.toString();
							
				arrSplitTime = timeToSplit.split(" ");//return an array with Day_Name Mon Day_Num HH:MM:SS LocalTime YYYY
							
				if(arrSplitTime.length != 6)//ensure array was split properly; if it's length is not 6, then an error occurred, so just show the simple time by throwing the exception
					throw new Exception();
				
				arrSplitHour = (arrSplitTime[3]).split(":");
				
				if(arrSplitHour.length != 3)//again, ensure we split the time from 18:57:48 bcs we only want 18:57
					throw new Exception();
				
				return strHourMin = arrSplitHour[0] + ":" + arrSplitHour[1] + "\"" + arrSplitHour[2];  
			}
			
			catch(Exception ee)//incase an error is generated from the above parse of the time and date, simply display the generic date for the client
			{
				dateTime_To_Display = "Time: " + dateTime.toString();
			}
		}		
		
		return dateTime_To_Display;		
	}//end mtd getTimeStamp
	
	
	/**
	 * This method returns the current time stamp for this system's time clock in the format: Sat - 12 Jul 2008 - 15:48 "13 - Eastern
	 */
	public String getTimeStamp_Updated()
	{
		try
		{	
			//Get the Date
			dateTime = new Date();//must always re-init in order to get the curr date and time
			
			//Get the formatted string from the date: in form of "Wed - 01 Oct 08 - 00:36 "28 - Central Daylight Time"
			timeToSplit = dateFormat.format(dateTime);
			
			arrSplitTime = timeToSplit.split("-");//return an array delimeted with Day of the Week, Date, Time, Time Zone
			
			//Note: we don't to display the entire time zone text, i.e. if time zone is Central Daylight Time, we only want Central to show, therefore split the last token in arrSplitTime and return only the first word
			arrSplitTimeZone = (arrSplitTime[3]).split(" ");

			//		 DAY OF WEEK		-	   DAY MON YEAR			 -		TIME		   -		TIME ZONE		
			return (arrSplitTime[0] + " - " + arrSplitTime[1] + " - " + arrSplitTime[2] + "- " + arrSplitTimeZone[1] + "         ");
			
			
		}//end try
		catch(Exception e)//incase an error is generated from the above parse of the time and date, simply display the generic date for the client
		{
			//go to the old way for calling the Rime:
			try
			{
				return getTimeStamp_Without_Date();
			}
			catch(Exception ee)
			{
				//all else failed, just return the time
				return dateTime_To_Display = "Time: " + dateTime.toString();
			}
			
		}
		
		//return dateTime_To_Display;		
	}
	
	
	
}
