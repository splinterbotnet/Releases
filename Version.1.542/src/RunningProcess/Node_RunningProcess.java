/**
 * @author Solomon Sonya
 */

package RunningProcess;

import javax.swing.*;


import Implant.Driver;

import java.util.*;


public class Node_RunningProcess 
{
	String  myImageName = "",
			mySessionName = "",
			myPID = "",//the string representation of the long PID val
			mySessionNumber = "",
			myMemUsage = "",
			myStatus = "",
			myUserName = "",
			myCPU_Time = "",
			myWindowTitle = "";
	
	long PID = 0;
	
	Vector<String> vctMyRowData = new Vector<String>();
	
	public volatile boolean refreshJTable = false;

	
	public Node_RunningProcess(String ImageName, long procID, String SessionName, String SessionNumer, String MemUsage, String Status, String UserName, String CpuTime, String WindowTitle)
	{
		myImageName = ImageName;
		PID = procID;
		myPID = "" + PID;
		mySessionName = SessionName;
		mySessionNumber = SessionNumer;
		myMemUsage = MemUsage;
		myStatus = Status;
		myUserName = UserName;
		myCPU_Time = CpuTime;
		myWindowTitle = WindowTitle;
		
	}
	
	public Vector<String> getRowData()
	{
		try	{	vctMyRowData.clear();	} catch(Exception e){}
		
		//"Image Name","PID","Session Name","Session#","Mem Usage","Status","User Name","CPU Time","Window Title"
		this.vctMyRowData.add(this.myImageName);
		this.vctMyRowData.add(""+this.PID);
		this.vctMyRowData.add(this.mySessionName);
		this.vctMyRowData.add(this.mySessionNumber);
		this.vctMyRowData.add(this.myMemUsage);
		this.vctMyRowData.add(this.myStatus);
		this.vctMyRowData.add(this.myUserName);
		this.vctMyRowData.add(this.myCPU_Time);
		this.vctMyRowData.add(this.myWindowTitle);		
		
		return vctMyRowData;
	}
	
}
