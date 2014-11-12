/**
 * @author Solomon Sonya
 */

package Implant.Payloads;


import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

import Implant.Driver;



public class MapNetworkInterface 
{
	public static final String myClassName = "MapNetworkInterface";
	
	private static Enumeration<NetworkInterface> enum_network_interfaces = null;
	
	private static ArrayList<NetworkInterface> alActiveInterfaces = null;
	
	public MapNetworkInterface()
	{
		try
		{
			listInterfaces();	
			
			sop("\n --> Network Interface Map Complete.");
		}		
		catch(Exception e)
		{
			sop("Exception caught in Constructor mtd in " + myClassName);
		}
	}
	
	
	public static ArrayList<NetworkInterface> listInterfaces()
	{
		try
		{
			sop("Listing Network Interfaces now...");
			
			alActiveInterfaces = new ArrayList<NetworkInterface>();
			
			//
			//List all Discoverable Interfaces
			//
			enum_network_interfaces = NetworkInterface.getNetworkInterfaces();
			
			//
			//Iterate through the list and only keep the interfaces that are active
			//
			for(NetworkInterface network_interface : Collections.list(enum_network_interfaces))
			{
				if(network_interface == null)
				{
					continue;
				}
				
				//
				//Acquire the IP Addresses assigned to each interface in the enumerations list
				//
				Enumeration<InetAddress> enum_IP_Addresses = network_interface.getInetAddresses();
				for( InetAddress IP_Address : Collections.list(enum_IP_Addresses))
				{
					//sop("IP Address: " + IP_Address);
				}
				
				//
				//Analyze and only keep interfaces that are up
				//
				if(network_interface.isUp())
				{
					alActiveInterfaces.add(network_interface);
				}
			}
			
			//
			//Up Interfaces and IP Addresses
			//
			for(int i = 0; i < alActiveInterfaces.size(); i++)
			{
				//sop(alActiveInterfaces.get(i).toString());
			}
			
			return alActiveInterfaces;
		}
		catch(SocketException se)
		{
			sop("ERROR: COULD NOT LIST INTERFACES");
		}
		catch(Exception e)
		{
			sop("Exception caught in listInterfaces mtd in " + myClassName);
		}
		
		return null;
	}
	
	public static int getInterfaceCount()
	{
		try
		{
			//
			//Ensure list is populated first
			//
			if(alActiveInterfaces == null || alActiveInterfaces.size() < 1)
			{
				
				listInterfaces();
			}
			
			//
			//If after that, still no list, return null
			//
			if(alActiveInterfaces == null || alActiveInterfaces.size() < 1)
			{
				
				return -1;
			}
			
			//otherwise, return the number of interfaces found
			return alActiveInterfaces.size();
			
			
		}
		catch(Exception e)
		{
			Driver.eop("getInterfaceCount", myClassName, e, "", false);
		}
		
		return -1;
	}
	
	public static InetAddress getInterfaceAddress(int index)
	{
		try
		{
			//
			//Ensure list is populated first
			//
			if(alActiveInterfaces == null || alActiveInterfaces.size() < 1)
			{
				
				listInterfaces();
			}
			
			//
			//If after that, still no list, return null
			//
			if(alActiveInterfaces == null || alActiveInterfaces.size() < 1)
			{
				
				return null;
			}
			
			//
			//Otherwise, grab the IP address of the interfae selected
			//
			NetworkInterface network_interface = alActiveInterfaces.get(index);
			
			//
			//Enumerate through the IP Addresses assigned to this interface
			//
			Enumeration<InetAddress> enum_inet_addresses = network_interface.getInetAddresses();
			
			//
			//Iterate through the address to return the appropriate one selected. Right now, we'll focus on IPv4 Addresses
			//
			ArrayList<InetAddress> al_IP_Addresses = new ArrayList<InetAddress>();
			for( InetAddress IP_Address : Collections.list(enum_inet_addresses))
			{
				//sop("IP Address: " + IP_Address);
				al_IP_Addresses.add(IP_Address);
			}
			
			//
			//Analyze Results
			//
			if(al_IP_Addresses.size() == 1)
			{
				return al_IP_Addresses.get(0);
			}
			
			//
			//Else, 2 found, perhaps one is an IPV6 and the other is an IPV4. Analyze and return the IPV4 Address
			//
			InetAddress inetToReturn = null;
			for(int i = 0; i < al_IP_Addresses.size(); i++)
			{
				inetToReturn = al_IP_Addresses.get(i);
				try
				{
					if(inetToReturn.toString().contains("."))
					{
						return inetToReturn;
					}
				}catch(Exception e){continue;}
			}
			
			//
			//Else, not sure, just return the first one!
			//
			return al_IP_Addresses.get(0);
			
		}
		catch(Exception e)
		{
			sop("Exception caught in getInterfaceAddress mtd in " + myClassName);
		}
		
		return null;
	}
	
	public static void sop(String out)
	{
		try
		{
			System.out.println(out);
		}catch(Exception e){}
	}
}
