/**
 * 
 * The purpose of this object is just to provide a feedback towards the total process from threads sending files to the user
 * 
 * @author Solomon Sonya
 */


package Implant.FTP;

import java.text.DecimalFormat;

import Implant.Driver;

public class IncrementObject 
{
	double completionStatus = 0;
	double stepValue = 1;
	DecimalFormat formatter = new DecimalFormat("##0.00");
	
	public IncrementObject(double step)
	{
		stepValue = step;
	}
	
	public void incrementStep()
	{
		try
		{
			if(stepValue > 0)
			{
				completionStatus += stepValue;
				
				Driver.sop("\n- - - > > > File Completion Status: " + formatter.format(completionStatus) + "%");
			}
		}catch(Exception e){}
	}
}




