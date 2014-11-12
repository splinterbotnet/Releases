/**
 * @author Solomon Sonya
 */

package Controller.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;

import Implant.Driver;

public class JPanel_Text_And_JTextField extends JPanel
{
	public static final String strMyClassName = "JPanel_Text_And_JTextField";
	
	
	public JLabel jlbl = null;
	public JTextField jtf = null;
	
	public JPanel_Text_And_JTextField(String JlblText, String jtfText)
	{
		try
		{
			if(JlblText == null)
				JlblText = "";
			
			if(jtfText == null)
				jtfText = "";
		
			this.setLayout(null);
			this.setPreferredSize(new Dimension(100, 25));
			
			jlbl = new JLabel(JlblText, JLabel.RIGHT);
			jtf = new JTextField(20);
			
			jtf.setText(jtfText);
			
			this.add(jlbl);
			this.add(jtf);
			
			jlbl.setBounds(5,0, 200, 30);
			
			jtf.setBounds(205, 5, 340, 20);	
		}
		
		catch(Exception e)
		{
			Driver.eop("Constructor", strMyClassName, e, "", false);
		}
		
	}
	
	public void setText(String text)
	{
		try
		{
			this.jtf.setText(text);
		}
		catch(Exception e){}
	}
	
	public String getText()
	{
		try
		{
			return this.jtf.getText();
		}catch(Exception e){}
		
		return "";
	}
}
