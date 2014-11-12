/**
 * Panel for each rendered image screen
 * 
 * @author Solomon Sonya
 */

package Implant.Payloads;


import Implant.Driver;

import Controller.Drivers.*;
import Controller.Thread.*;
import Implant.*;
import Implant.FTP.FTP_ServerSocket;
import Implant.FTP.FTP_Thread_Receiver;
import Implant.Payloads.Frame_SpoofUAC;
import Implant.Payloads.MapSystemProperties;
import Implant.Payloads.Worker_Thread_Payloads;


import java.awt.Frame;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.*;
import java.lang.*;
import java.awt.Frame;
import javax.swing.border.*;

import java.awt.*;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import java.awt.Rectangle;
import java.awt.BorderLayout;
import javax.swing.JLabel ;
import java.lang.*;
import java.awt.Label;
import java.awt.event.KeyEvent;
import java.awt.Panel;
import java.awt.SystemColor;
import javax.swing.JRadioButton;
import java.text.*;
import java.util.*;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.event.*;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.ScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import java.awt.Button;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Choice;
import javax.swing.JComboBox;
import java.net.*;
import javax.swing.WindowConstants;
import javax.swing.table.*;
import java.lang.*;

import java.io.*;
import java.awt.GridBagLayout;

public class JPanel_RenderedScreen extends JPanel
{
	public static final String strMyClassName = "JPanel_RenderedScreen";
	
	JLabel jlblImage = new JLabel("");
	
	JScrollPane jscrlpne_MyImage = null;
	
	ImageIcon myImageIcon = null;
	
	Image img = null;
	
	public JPanel_RenderedScreen(File fleToRender)
	{
		try
		{
			this.setLayout(new BorderLayout());
			jscrlpne_MyImage = new JScrollPane(jlblImage);					
			this.add(BorderLayout.CENTER, this.jscrlpne_MyImage);
			
			//myImageIcon = new ImageIcon(fleToRender.getCanonicalPath());			
			
			/*try
			{
				this.setPreferredSize(new Dimension(myImageIcon.getIconWidth()-300, myImageIcon.getIconHeight()-300));
			}
			catch(Exception e)
			{
				this.setPreferredSize(new Dimension(500, 500));
			}*/
									
			//this.jlblImage.setIcon(myImageIcon);
			
			this.setImage(fleToRender);
			
			
			
			//this.add(BorderLayout.CENTER, jlblImage);
			
			this.validate();
			
		}
		catch(Exception e)
		{
			jlblImage.setText("IMAGE COULD NOT BE DISPLAYED!");
			
			Driver.eop("Constructor", strMyClassName, e, "", false);
		}
		
		this.validate();
	}
	
	public boolean setImage(File fleImageToSet)
	{
		try
		{
			if(fleImageToSet == null || !fleImageToSet.exists() || !fleImageToSet.isFile())
			{
				jlblImage.setText("IMAGE COULD NOT BE DISPLAYED!");
				this.validate();
				return false;
			}
								
			/*if(myImageIcon == null || myImageIcon.getIconHeight() < 1 ||  myImageIcon.getIconWidth() < 1)
			{
				//invalid image
				return false;
			}*/
			
			//myImageIcon = new ImageIcon(fleImageToSet.getCanonicalPath());
		
			//else, set the image
			//this.jlblImage.setIcon(myImageIcon);
			
			System.gc();
			
			img = ImageIO.read(fleImageToSet);
			myImageIcon = new ImageIcon(img);
			this.jlblImage.setIcon(myImageIcon);
			
			
			this.validate();
			
			return true;
		}
		
		catch(NullPointerException npe)
		{
			//do nothing and dismiss this error!
		}
		
		catch(IndexOutOfBoundsException iobe)
		{
			//do nothing and dismiss this error!
		}
		
		catch(Exception e)
		{
			Driver.sop("Unable to render image!!!  - " + fleImageToSet);
			//Driver.eop("setImage", strMyClassName, e, "", true);
		}
		
		return false;
	}

}
