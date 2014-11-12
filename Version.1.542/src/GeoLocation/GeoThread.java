/**
 * the purpose of this thread class is to wait, query, and parse IP data from http://freegeoip.net/xml/ to return the 
 * reported GeoLocation of an IP address 
 * 
 * MUCH THANKS GOES TO http://freegeoip.net/xml/ AND http://checkip.dyndns.org/ FOR PROVIDING THE GEO LOCATION INFORMATION
 * WITHOUT THE USE OF THESE SITES, RETRIEVING THIS DATA WOULD NOT BE POSSIBLE (FROM THESE SITES)
 * 
 * 
 * LONG TERM: Port own DB to query for this information... however, since this is quick, low and dirty, 
 * we'll simply rely on a webservice to provide this data
 * 
 * note: do not exceed the quota specified by http://freegeoip.net/
 * 
 * "http://freegeoip.net/static/index.html"
 * "About

from freegeoip:

This is a public web service for searching geolocation of IP addresses and host names.

The system has an internal database with geolocation information, which is queried via the web service. There's no magic or tricky calculations, it's just a database. And although the database is very accurate, don't expect it to be perfect.

This service includes GeoLite data created by MaxMind, available from maxmind.com.

Last updated: February 2013

Usage (for developers)

Make HTTP GET requests to: http://freegeoip.net/{format}/{ip_or_hostname}

Supported formats are csv, xml or json.

The ip_or_hostname part is optional, and your own IP is searched if one is not provided.

Limits

Due to the high volume of queries we've implemented a throttling mechanism, which allows at most 1000 queries per hour. After reaching this hourly quota, all your requests result in HTTP 403 (Forbidden) until it clears up on the next roll over.

There's absolutely no plans of selling services here. If this limit is a problem for you, please consider running your very own instance of this system. It's open source and freely available at GitHub.

Check out the source code
Contact
Feel free to share comments, suggestions or any other feedback. I'm here.

Contribute with code, pizza, gifts or donations."
 * 
 * @author Solomon Sonya
 */

package GeoLocation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;

import Controller.Drivers.Drivers;
import Controller.Thread.Thread_Terminal;
import Implant.Driver;

public class GeoThread extends Thread implements Runnable
{
	static final String strMyClassName = "GeoThread";
	private static final String GEO_SITE = "http://freegeoip.net/xml/";
	private static final String EXTERNAL_IP_RESOLUTION_SITE = "http://checkip.dyndns.org/";
	
	public static boolean geoLookupEnabled = true;
	
	Thread_Terminal agentToQuery = null;
	
	public GeoThread(Thread_Terminal agent)
	{
		agentToQuery = agent;
		
	}
	
	public void run()
	{
		try
		{
			if(agentToQuery != null && geoLookupEnabled && agentToQuery.getRHOST_IP() != null && !agentToQuery.GEO_RESOLUTION_COMPLETE)
			{
				agentToQuery.myFullGeoData = "Resolving...";
				String IP = agentToQuery.getRHOST_IP();
				
				//
				
				/*try //at least for now, it appears the site resolves our raw IP address for us if it is our box
				{
					Drivers.sop("Begining IP: " + agentToQuery.getRHOST_IP());
					
					if(IP.trim().startsWith("127") || IP.trim().startsWith("192"))//make an external to resolve external IP
					{
						URL urlExternal_IP_Resolution = new URL(EXTERNAL_IP_RESOLUTION_SITE);
						
						URLConnection urlconnection_ExternalResolution = urlExternal_IP_Resolution.openConnection();
						
						BufferedReader brIn = new BufferedReader(new InputStreamReader(urlconnection_ExternalResolution.getInputStream()));
						
						String line = brIn.readLine();
						
						IP = line.replace("Current IP Address:", "");							
					}
				}catch(Exception e){Driver.sop("Unable to resolve external IP");}*/
				
				try
				{
					IP = IP.replaceFirst("/", "");
					IP = IP.replace("\\", "");
					IP = IP.trim();
					IP = IP.toUpperCase(); //for ipv6 yes!
				}catch(Exception e){}
				
				//check if the IP is in the reserved space, if so, make the request as if coming from our current machine
				if  (
						IP.startsWith("0.0") 			||
						IP.startsWith("10.0") 			||
						IP.startsWith("100.64.0") 		||
						IP.startsWith("127") 			||
						IP.startsWith("169.254") 		||
						IP.startsWith("192.0.0") 		||
						IP.startsWith("192.0.2") 		||
						IP.startsWith("192.168") 		||
						IP.startsWith("198.18") 		||
						IP.startsWith("198.51.100") 	||
						IP.startsWith("203.0.113") 		||
						IP.startsWith("240") 			||
						IP.startsWith("255.255.255.255")||
						IP.startsWith("0:0:0")
					)										{	IP = "";	}
				
				//Drivers.sop("IP To Query: " + IP);
				
				//Here, we have a good IP addr to look up!
				URL urlGeoLookup = new URL(GEO_SITE + IP);
				
				//Drivers.sop("URL site: " + urlGeoLookup.toString());
				
				URLConnection urlconnection = urlGeoLookup.openConnection();
				
				BufferedReader brIn = new BufferedReader(new InputStreamReader(urlconnection.getInputStream()));
				
				//good made it here, we were able to open a connection
				//ArrayList<String> alGeoData = new ArrayList<String>();
				
				//i don't plan on doing anything with the arraylist now, but we'll add to it just incaase it's useful later
				
				String line = "";
				String beginTag = "";
				String endTag = "";
				
				while((line = brIn.readLine()) != null)
				{
					//Drivers.sop("Line: " + line);
					
					if(line.trim().equals(""))
					{
						continue;
					}
															
					line = line.toUpperCase().trim();
					
					if(line.startsWith("<Response>".toUpperCase()))
					{
						continue;
					}
					
					if(line.startsWith("/<Response>".toUpperCase()))
					{
						break;
					}
					
					//IP
					beginTag = "<Ip>".toUpperCase();
					endTag = "</Ip>".toUpperCase();
					
					if(line.contains(beginTag))
					{
						line = line.replace(beginTag, "");
						line = line.replace(endTag, "");
						
						agentToQuery.myGEO_IP = line;
						
						continue;
					}
					
					//CountryCode
					beginTag = "<CountryCode>".toUpperCase();
					endTag = "</CountryCode>".toUpperCase();
					
					if(line.contains(beginTag))
					{
						line = line.replace(beginTag, "");
						line = line.replace(endTag, "");
						
						agentToQuery.myGEO_CountryCode = line;
						
						continue;
					}
					
					//CountryCode
					beginTag = "<CountryCode>".toUpperCase();
					endTag = "</CountryCode>".toUpperCase();
					
					if(line.contains(beginTag))
					{
						line = line.replace(beginTag, "");
						line = line.replace(endTag, "");
						
						agentToQuery.myGEO_CountryCode = line;
						
						continue;
					}
					
					//CountryName
					beginTag = "<CountryName>".toUpperCase();
					endTag = "</CountryName>".toUpperCase();
					
					if(line.contains(beginTag))
					{
						line = line.replace(beginTag, "");
						line = line.replace(endTag, "");
						
						agentToQuery.myGEO_CountryName = line;
						
						continue;
					}
					
					//RegionCode
					beginTag = "<RegionCode>".toUpperCase();
					endTag = "</RegionCode>".toUpperCase();
					
					if(line.contains(beginTag))
					{
						line = line.replace(beginTag, "");
						line = line.replace(endTag, "");
						
						agentToQuery.myGEO_RegionCode = line;
						
						continue;
					}
					
					//RegionName
					beginTag = "<RegionName>".toUpperCase();
					endTag = "</RegionName>".toUpperCase();
					
					if(line.contains(beginTag))
					{
						line = line.replace(beginTag, "");
						line = line.replace(endTag, "");
						
						agentToQuery.myGEO_RegionName = line;
						
						continue;
					}
					
					//City
					beginTag = "<City>".toUpperCase();
					endTag = "</City>".toUpperCase();
					
					if(line.contains(beginTag))
					{
						line = line.replace(beginTag, "");
						line = line.replace(endTag, "");
						
						agentToQuery.myGEO_City = line;
						
						continue;
					}
					
					//Latitude
					beginTag = "<Latitude>".toUpperCase();
					endTag = "</Latitude>".toUpperCase();
					
					if(line.contains(beginTag))
					{
						line = line.replace(beginTag, "");
						line = line.replace(endTag, "");
						
						agentToQuery.myGEO_Latitude = line;
						
						continue;
					}
					
					//Longitude
					beginTag = "<Longitude>".toUpperCase();
					endTag = "</Longitude>".toUpperCase();
					
					if(line.contains(beginTag))
					{
						line = line.replace(beginTag, "");
						line = line.replace(endTag, "");
						
						agentToQuery.myGEO_Longitude = line;
						
						continue;
					}
					
					//MetroCode
					beginTag = "<MetroCode>".toUpperCase();
					endTag = "</MetroCode>".toUpperCase();
					
					if(line.contains(beginTag))
					{
						line = line.replace(beginTag, "");
						line = line.replace(endTag, "");
						
						agentToQuery.myGEO_MetroCode = line;
						
						continue;
					}
					
					//AreaCode
					beginTag = "<AreaCode>".toUpperCase();
					endTag = "</AreaCode>".toUpperCase();
					
					if(line.contains(beginTag))
					{
						line = line.replace(beginTag, "");
						line = line.replace(endTag, "");
						
						agentToQuery.myGEO_AreaCode = line;
						
						continue;
					}
							
				}//end while
				
				//done, store the information and then dispose softly
				agentToQuery.myFullGeoData = "" + agentToQuery.myGEO_City + ", " + agentToQuery.myGEO_RegionCode + ", " + agentToQuery.myGEO_CountryName + ", " + "Area Code: " + agentToQuery.myGEO_AreaCode;
				agentToQuery.GEO_RESOLUTION_COMPLETE = true;
				//agentToQuery.myFullGeoData = "Resolving...";
				
			}//end if
			
			try
			{
				if(agentToQuery.i_am_Beacon_Agent)
				{
					Drivers.jtblBeaconImplants.updateJTable = true;
				}
				
				else if(agentToQuery.i_am_loggie_agent)
				{
					Drivers.jtblLoggingAgents.updateJTable = true;
				}
				
				else					
				{
					Drivers.jtblConnectedImplants.updateJTable = true;
				}
				
			}
			
			catch(Exception e){}
			
			
		}//end try
		catch(UnknownHostException uhe)
		{
			Drivers.sop("Unable to resolve GeoLocation data for thread: " + agentToQuery.getId());
			agentToQuery.myFullGeoData = "UNKNOWN";	
			agentToQuery.GEO_RESOLUTION_COMPLETE = true;
		}
		catch(Exception e)
		{
			if(e.getLocalizedMessage().contains("403"))// requests are forbidden!!!!
			{
				Driver.sop("* * * ERROR, requests have been forbidden to resolve GPS data at this time! ... Perhaps quota limit has been reached...?");
			}
			else
			{
				Drivers.eop("run", strMyClassName, e, e.getLocalizedMessage(), false);
			}
			
			
			
			agentToQuery.myFullGeoData = "UNKNOWN";	
			agentToQuery.GEO_RESOLUTION_COMPLETE = true;
		}
		
		//try{Drivers.jtblConnectedImplants.updateJTable = true;}catch(Exception e){}
		
		try
		{
			if(agentToQuery.i_am_Beacon_Agent)
			{
				Drivers.jtblBeaconImplants.updateJTable = true;
			}
			
			else if(agentToQuery.i_am_loggie_agent)
			{
				Drivers.jtblLoggingAgents.updateJTable = true;
			}
			
			else					
			{
				Drivers.jtblConnectedImplants.updateJTable = true;
			}
			
		}
		
		catch(Exception e){}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
