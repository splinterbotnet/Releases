/**
 * Purpose of this class is to launch a process to show the thrad on the map
 * 
 * @author Solomon Sonya
 */

package GeoLocation;

import Controller.Drivers.Drivers;
import Controller.Thread.Thread_Terminal;
import Implant.Driver;

public class ShowMap extends Thread
{
	String strMyClassName = "ShowMap";
	
	Thread_Terminal threadToShow = null;
	
	public ShowMap(Thread_Terminal threadMap)
	{
		threadToShow = threadMap;
	}
	
	public void run()
	{
		try
		{
			//Assume all error checking has been completed by now
			Process process = Runtime.getRuntime().exec("cmd /c start http://maps.google.com/maps?q=" + threadToShow.myGEO_Latitude + "," + threadToShow.myGEO_Longitude);			
		}
		catch(Exception e)
		{
			Drivers.eop("Run", strMyClassName, e, e.getLocalizedMessage(), false);
		}
	}
}
