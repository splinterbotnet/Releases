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




package GeoLocation;

import Controller.Drivers.Drivers;
import Controller.Thread.Thread_Terminal;
import Implant.Driver;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class GeoThread extends Thread
  implements Runnable
{
  static final String strMyClassName = "GeoThread";
  private static final String GEO_SITE = "http://freegeoip.net/xml/";
  private static final String EXTERNAL_IP_RESOLUTION_SITE = "http://checkip.dyndns.org/";
  public static boolean geoLookupEnabled = true;

  Thread_Terminal agentToQuery = null;

  public GeoThread(Thread_Terminal agent)
  {
    this.agentToQuery = agent;
  }

  public void run()
  {
    try
    {
      if ((this.agentToQuery != null) && (geoLookupEnabled) && (this.agentToQuery.getRHOST_IP() != null) && (!this.agentToQuery.GEO_RESOLUTION_COMPLETE))
      {
        this.agentToQuery.myFullGeoData = "Resolving...";
        String IP = this.agentToQuery.getRHOST_IP();
        try
        {
          IP = IP.replaceFirst("/", "");
          IP = IP.replace("\\", "");
          IP = IP.trim();
          IP = IP.toUpperCase();
        }
        catch (Exception localException1)
        {
        }
        if ((IP.startsWith("0.0")) || 
          (IP.startsWith("10.0")) || 
          (IP.startsWith("100.64.0")) || 
          (IP.startsWith("127")) || 
          (IP.startsWith("169.254")) || 
          (IP.startsWith("192.0.0")) || 
          (IP.startsWith("192.0.2")) || 
          (IP.startsWith("192.168")) || 
          (IP.startsWith("198.18")) || 
          (IP.startsWith("198.51.100")) || 
          (IP.startsWith("203.0.113")) || 
          (IP.startsWith("240")) || 
          (IP.startsWith("255.255.255.255")) || 
          (IP.startsWith("0:0:0"))) {
          IP = "";
        }

        URL urlGeoLookup = new URL("http://freegeoip.net/xml/" + IP);

        URLConnection urlconnection = urlGeoLookup.openConnection();

        BufferedReader brIn = new BufferedReader(new InputStreamReader(urlconnection.getInputStream()));

        String line = "";
        String beginTag = "";
        String endTag = "";

        while ((line = brIn.readLine()) != null)
        {
          if (!line.trim().equals(""))
          {
            line = line.toUpperCase().trim();

            if (!line.startsWith("<Response>".toUpperCase()))
            {
              if (line.startsWith("/<Response>".toUpperCase()))
              {
                break;
              }

              beginTag = "<Ip>".toUpperCase();
              endTag = "</Ip>".toUpperCase();

              if (line.contains(beginTag))
              {
                line = line.replace(beginTag, "");
                line = line.replace(endTag, "");

                this.agentToQuery.myGEO_IP = line;
              }
              else
              {
                beginTag = "<CountryCode>".toUpperCase();
                endTag = "</CountryCode>".toUpperCase();

                if (line.contains(beginTag))
                {
                  line = line.replace(beginTag, "");
                  line = line.replace(endTag, "");

                  this.agentToQuery.myGEO_CountryCode = line;
                }
                else
                {
                  beginTag = "<CountryCode>".toUpperCase();
                  endTag = "</CountryCode>".toUpperCase();

                  if (line.contains(beginTag))
                  {
                    line = line.replace(beginTag, "");
                    line = line.replace(endTag, "");

                    this.agentToQuery.myGEO_CountryCode = line;
                  }
                  else
                  {
                    beginTag = "<CountryName>".toUpperCase();
                    endTag = "</CountryName>".toUpperCase();

                    if (line.contains(beginTag))
                    {
                      line = line.replace(beginTag, "");
                      line = line.replace(endTag, "");

                      this.agentToQuery.myGEO_CountryName = line;
                    }
                    else
                    {
                      beginTag = "<RegionCode>".toUpperCase();
                      endTag = "</RegionCode>".toUpperCase();

                      if (line.contains(beginTag))
                      {
                        line = line.replace(beginTag, "");
                        line = line.replace(endTag, "");

                        this.agentToQuery.myGEO_RegionCode = line;
                      }
                      else
                      {
                        beginTag = "<RegionName>".toUpperCase();
                        endTag = "</RegionName>".toUpperCase();

                        if (line.contains(beginTag))
                        {
                          line = line.replace(beginTag, "");
                          line = line.replace(endTag, "");

                          this.agentToQuery.myGEO_RegionName = line;
                        }
                        else
                        {
                          beginTag = "<City>".toUpperCase();
                          endTag = "</City>".toUpperCase();

                          if (line.contains(beginTag))
                          {
                            line = line.replace(beginTag, "");
                            line = line.replace(endTag, "");

                            this.agentToQuery.myGEO_City = line;
                          }
                          else
                          {
                            beginTag = "<Latitude>".toUpperCase();
                            endTag = "</Latitude>".toUpperCase();

                            if (line.contains(beginTag))
                            {
                              line = line.replace(beginTag, "");
                              line = line.replace(endTag, "");

                              this.agentToQuery.myGEO_Latitude = line;
                            }
                            else
                            {
                              beginTag = "<Longitude>".toUpperCase();
                              endTag = "</Longitude>".toUpperCase();

                              if (line.contains(beginTag))
                              {
                                line = line.replace(beginTag, "");
                                line = line.replace(endTag, "");

                                this.agentToQuery.myGEO_Longitude = line;
                              }
                              else
                              {
                                beginTag = "<MetroCode>".toUpperCase();
                                endTag = "</MetroCode>".toUpperCase();

                                if (line.contains(beginTag))
                                {
                                  line = line.replace(beginTag, "");
                                  line = line.replace(endTag, "");

                                  this.agentToQuery.myGEO_MetroCode = line;
                                }
                                else
                                {
                                  beginTag = "<AreaCode>".toUpperCase();
                                  endTag = "</AreaCode>".toUpperCase();

                                  if (line.contains(beginTag))
                                  {
                                    line = line.replace(beginTag, "");
                                    line = line.replace(endTag, "");

                                    this.agentToQuery.myGEO_AreaCode = line;
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        this.agentToQuery.myFullGeoData = (this.agentToQuery.myGEO_City + ", " + this.agentToQuery.myGEO_RegionCode + ", " + this.agentToQuery.myGEO_CountryName + ", " + "Area Code: " + this.agentToQuery.myGEO_AreaCode);
        this.agentToQuery.GEO_RESOLUTION_COMPLETE = true;
      }

      try
      {
        if (this.agentToQuery.i_am_Beacon_Agent)
        {
          Drivers.jtblBeaconImplants.updateJTable = true;
        }
        else if (this.agentToQuery.i_am_loggie_agent)
        {
          Drivers.jtblLoggingAgents.updateJTable = true;
        }
        else
        {
          Drivers.jtblConnectedImplants.updateJTable = true;
        }

      }
      catch (Exception localException2)
      {
      }

    }
    catch (UnknownHostException uhe)
    {
      Drivers.sop("Unable to resolve GeoLocation data for thread: " + this.agentToQuery.getId());
      this.agentToQuery.myFullGeoData = "UNKNOWN";
      this.agentToQuery.GEO_RESOLUTION_COMPLETE = true;
    }
    catch (Exception e)
    {
      if (e.getLocalizedMessage().contains("403"))
      {
        Driver.sop("* * * ERROR, requests have been forbidden to resolve GPS data at this time! ... Perhaps quota limit has been reached...?");
      }
      else
      {
        Drivers.eop("run", "GeoThread", e, e.getLocalizedMessage(), false);
      }

      this.agentToQuery.myFullGeoData = "UNKNOWN";
      this.agentToQuery.GEO_RESOLUTION_COMPLETE = true;
    }

    try
    {
      if (this.agentToQuery.i_am_Beacon_Agent)
      {
        Drivers.jtblBeaconImplants.updateJTable = true;
      }
      else if (this.agentToQuery.i_am_loggie_agent)
      {
        Drivers.jtblLoggingAgents.updateJTable = true;
      }
      else
      {
        Drivers.jtblConnectedImplants.updateJTable = true;
      }
    }
    catch (Exception localException3)
    {
    }
  }
}