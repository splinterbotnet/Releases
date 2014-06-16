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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JPanel_BeaconCommand extends JPanel
  implements ActionListener
{
  public static String strMyClassName = "JPanel_BeaconCommand";

  public int myIndex = 0;

  public JLabel jlblExtMsg = new JLabel(" ");

  public JPanel jpnlAlignment_jpnlIndex = new JPanel(new BorderLayout());
  public JPanel jpnlIndex = new JPanel();
  public JLabel jlblIndex = new JLabel("[ 0 ]");
  public JLabel jlblCommand = new JLabel("Command:  ");

  public JPanel jpnlJTextField = new JPanel(new BorderLayout());
  public JTextField jtfCommand = new JTextField(25);

  public JPanel jpnlAlignment_jpnlOptions = new JPanel(new BorderLayout());
  public JPanel jpnlOptions = new JPanel();
  public JButton jbtnAddBeaconCommand = new JButton("Add New Command");
  public JButton jbtnDeleteThisCommand = new JButton("Delete This Command");
  public JCheckBox jcbEnable = new JCheckBox("Enable");
  public JComboBox jcbShortCuts = new JComboBox();

  public Font fntJLbl = new Font("Tahoma", 0, 16);
  public Font fntJtf = new Font("Tahoma", 0, 14);

  public JPanel_BeaconCommand()
  {
    setLayout(new BorderLayout());

    add("North", this.jlblExtMsg);

    this.jpnlIndex.add(this.jlblIndex);
    this.jpnlIndex.add(this.jlblCommand);
    this.jpnlAlignment_jpnlIndex.add("North", this.jpnlIndex);
    add("West", this.jpnlAlignment_jpnlIndex);

    this.jpnlJTextField.add("North", this.jtfCommand);
    add("Center", this.jpnlJTextField);

    this.jcbShortCuts.setModel(new DefaultComboBoxModel(Driver.arrShortCuts));
    this.jpnlOptions.add(this.jcbShortCuts);
    this.jpnlOptions.add(this.jbtnDeleteThisCommand);
    this.jpnlOptions.add(this.jbtnAddBeaconCommand);
    this.jpnlOptions.add(this.jcbEnable);
    this.jpnlAlignment_jpnlOptions.add("North", this.jpnlOptions);
    add("East", this.jpnlAlignment_jpnlOptions);

    this.jlblCommand.setFont(this.fntJLbl);
    this.jlblIndex.setFont(this.fntJLbl);
    this.jtfCommand.setFont(this.fntJtf);

    this.jlblIndex.setOpaque(true);

    this.jlblIndex.setBackground(Color.red);

    this.jlblIndex.setForeground(Color.white);

    this.jcbEnable.addActionListener(this);
    this.jcbShortCuts.addActionListener(this);
  }

  public void actionPerformed(ActionEvent ae)
  {
    try
    {
      if (ae.getSource() == this.jcbEnable)
      {
        if (this.jcbEnable.isSelected())
        {
          this.jlblIndex.setBackground(Color.green.darker().darker());
        }
        else
        {
          this.jlblIndex.setBackground(Color.red);
        }

      }
      else if (ae.getSource() == this.jcbShortCuts)
      {
        if (this.jcbShortCuts.getSelectedIndex() > 0)
        {
          this.jtfCommand.setText(""+this.jcbShortCuts.getSelectedItem());
        }

      }

    }
    catch (Exception e)
    {
      Driver.eop("ae", strMyClassName, e, "", false);
    }
  }

  public boolean setIndex(int index)
  {
    try
    {
      this.myIndex = index;

      if (this.myIndex < 0) {
        this.myIndex = 0;
      }
      if (this.myIndex < 10)
      {
        this.jlblIndex.setText("[ 0" + this.myIndex + " ]");
      }
      else
      {
        this.jlblIndex.setText("[ " + this.myIndex + " ]");
      }

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("setIndex", strMyClassName, e, "", false);
    }

    return false;
  }
}