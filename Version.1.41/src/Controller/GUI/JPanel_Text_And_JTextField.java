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




package Controller.GUI;

import Implant.Driver;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JPanel_Text_And_JTextField extends JPanel
{
  public static final String strMyClassName = "JPanel_Text_And_JTextField";
  public JLabel jlbl = null;
  public JTextField jtf = null;

  public JPanel_Text_And_JTextField(String JlblText, String jtfText)
  {
    try
    {
      if (JlblText == null) {
        JlblText = "";
      }
      if (jtfText == null) {
        jtfText = "";
      }
      setLayout(null);
      setPreferredSize(new Dimension(100, 25));

      this.jlbl = new JLabel(JlblText, 4);
      this.jtf = new JTextField(20);

      this.jtf.setText(jtfText);

      add(this.jlbl);
      add(this.jtf);

      this.jlbl.setBounds(5, 0, 200, 30);

      this.jtf.setBounds(205, 5, 340, 20);
    }
    catch (Exception e)
    {
      Driver.eop("Constructor", "JPanel_Text_And_JTextField", e, "", false);
    }
  }

  public void setText(String text)
  {
    try
    {
      this.jtf.setText(text);
    }
    catch (Exception localException)
    {
    }
  }

  public String getText() {
    try {
      return this.jtf.getText();
    } catch (Exception localException) {
    }
    return "";
  }
}