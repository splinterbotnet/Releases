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



package RunningProcess;

import java.util.Vector;

public class Node_RunningProcess
{
  String myImageName = "";
  String mySessionName = "";
  String myPID = "";
  String mySessionNumber = "";
  String myMemUsage = "";
  String myStatus = "";
  String myUserName = "";
  String myCPU_Time = "";
  String myWindowTitle = "";

  long PID = 0L;

  Vector<String> vctMyRowData = new Vector();

  public volatile boolean refreshJTable = false;

  public Node_RunningProcess(String ImageName, long procID, String SessionName, String SessionNumer, String MemUsage, String Status, String UserName, String CpuTime, String WindowTitle)
  {
    this.myImageName = ImageName;
    this.PID = procID;
    this.myPID = ""+this.PID;
    this.mySessionName = SessionName;
    this.mySessionNumber = SessionNumer;
    this.myMemUsage = MemUsage;
    this.myStatus = Status;
    this.myUserName = UserName;
    this.myCPU_Time = CpuTime;
    this.myWindowTitle = WindowTitle;
  }

  public Vector<String> getRowData()
  {
    try {
      this.vctMyRowData.clear();
    } catch (Exception localException) {
    }
    this.vctMyRowData.add(this.myImageName);
    this.vctMyRowData.add(""+this.PID);
    this.vctMyRowData.add(this.mySessionName);
    this.vctMyRowData.add(this.mySessionNumber);
    this.vctMyRowData.add(this.myMemUsage);
    this.vctMyRowData.add(this.myStatus);
    this.vctMyRowData.add(this.myUserName);
    this.vctMyRowData.add(this.myCPU_Time);
    this.vctMyRowData.add(this.myWindowTitle);

    return this.vctMyRowData;
  }
}