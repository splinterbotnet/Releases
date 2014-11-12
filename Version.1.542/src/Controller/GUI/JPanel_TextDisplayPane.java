/**
 * The purpose of this class is to display the GUI for the Chat Client
 * 
 * 
 * @author Solomon Sonya 
 *
 */


package Controller.GUI;

import Controller.Drivers.*;

import javax.swing.*;
import java.awt.*;

import javax.swing.text.*;

import javax.swing.JFrame;
//import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;



public class JPanel_TextDisplayPane extends JTextPane
{
	String strMyClassName = this.getName();
	
	//overhead setup
	StyleContext context = new StyleContext();	 	
	public StyledDocument document = new DefaultStyledDocument(context);	
	Style style = context.getStyle(StyleContext.DEFAULT_STYLE);	
	
	StyleContext stlCtx = null;
	SimpleAttributeSet attrSet_Text = null;
	
	public boolean autoScroll = true;
	
	String textStyles [] = { "regular", "italic", "bold", "small", "large", "button", "icon"};
	
	int lenToColor = 0;
	
	Color clrSender = Color.black, clrReceiver = Color.black;
	
	public volatile boolean includeTimeStamp = true;
	
	//default
	
	//Pointers
	public JPanel_MainControllerWindow myMainControllerWindow = null;
	
	
	public JPanel_TextDisplayPane()
	{
		this.setEditable(false);
		this.setBackground(Drivers.clrBackground);
		this.setDocument(document);		
		
		//create the style of the text
		stlCtx = StyleContext.getDefaultStyleContext();
		
		//create the attribute set with the color
		attrSet_Text = new SimpleAttributeSet();
		StyleConstants.setBold(attrSet_Text, false);//set the points bold
		StyleConstants.setItalic(attrSet_Text, false);//don't italicize
	    StyleConstants.setUnderline(attrSet_Text, false);//underline it
	    StyleConstants.setForeground(attrSet_Text, Color.BLACK);//set the foreground
	    StyleConstants.setFontSize(attrSet_Text, Drivers.fontSize);//font
		
		//overhead setup
		StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
		
	}//end public ReChatter_Display constructor mtd
	
	/**
	 * @override 
	 * DISABLE LINE WRAPPING
	 * 
	 * 
	 */
	
	
	
	 public boolean getScrollableTracksViewportWidth()
	 {
		 return getUI().getPreferredSize(this).width <= getParent().getSize().width;
	 }
	
	 public void clearTextPane()
	 {
		 try
		 {
			 document.remove(0,  document.getLength()-1);
		 }
		 catch(Exception e)
		 {
			 
		 }
	 }
	 
	/**
	 * This is the actual method that performs the placement of the text onto the JTextPane
	 * @param strToAppend
	 * @param clr
	 */
	public void appendString(boolean includeTimeStamp, String strChatMsg, Color Foreground, Color Background)
	{
		try
		{		
			//Figure out the Sender and Receiver  colors
			//clrSender = determineTeamColor(strSender);
			//clrReceiver = determineTeamColor(strReceiver);

			
			
			//Place the time stamp if preferred
			if(includeTimeStamp)
			{
				//Set Attributes for UserName
				StyleConstants.setBold(attrSet_Text, false);//set the points bold
				StyleConstants.setItalic(attrSet_Text, false);//don't italicize
			    StyleConstants.setUnderline(attrSet_Text, false);//underline it
			    StyleConstants.setForeground(attrSet_Text, Foreground);//set the foreground
			    StyleConstants.setBackground(attrSet_Text, Background);//set the background
			    StyleConstants.setFontSize(attrSet_Text, Drivers.fontSize);//font*/
							
				//the len tells us where to place the pointer at the end of the document (caret position)
				lenToColor = document.getLength();
										
				//Place the black text first
				//document.insertString(lenToColor, "[" + parent.parent.timerThread.getTimeStamp_Without_Date() + "] ", attrSet_Text);
				document.insertString(lenToColor, "[" + Drivers.getTimeStamp_Without_Date() + "] ", attrSet_Text);
				
				
			}
							
			//Set Attributes for SENDER
			StyleConstants.setBold(attrSet_Text, true);//set the points bold
			StyleConstants.setItalic(attrSet_Text, false);//don't italicize
		    StyleConstants.setUnderline(attrSet_Text, true);//underline it
		    StyleConstants.setForeground(attrSet_Text, Foreground);//set the foreground
		    StyleConstants.setBackground(attrSet_Text, Background);//set the background
		    StyleConstants.setFontSize(attrSet_Text, Drivers.fontSize);//font*/
						
			//the len tells us where to place the pointer at the end of the document (caret position)
			lenToColor = document.getLength();
									
			
						
			//Set Attributes for Chat Message
			StyleConstants.setBold(attrSet_Text, false);//set the points bold
			StyleConstants.setItalic(attrSet_Text, false);//don't italicize
		    StyleConstants.setUnderline(attrSet_Text, false);//underline it
		    StyleConstants.setForeground(attrSet_Text, Foreground);//set the foreground
		    StyleConstants.setBackground(attrSet_Text, Background);//set the background
		    StyleConstants.setFontSize(attrSet_Text, Drivers.fontSize);//font
			
		    //Next, Place the rest of their message
			lenToColor = document.getLength();//get new length to begin coloring from
			
			//Place the Chat Message
			document.insertString(lenToColor, strChatMsg + "\n", attrSet_Text);
			
			try
			{
				//check for auto scroll
				if(this.autoScroll)//reset the scroll position to the bottom if this is selected
					this.setCaretPosition((document.getLength()));
				
			}
			catch(Exception e)
			{
				System.out.println("Could not set the caret position in ReChatter_Display");
			}
			
			
		}//end try
		catch(Exception e)
		{
			System.out.println("Invalid insert location from appendString in ReChatter_Display class\n" + e.getLocalizedMessage());
		}
		
	}
	
	/**
	 * This is the actual method that performs the placement of the text onto the JTextPane
	 * @param strToAppend
	 * @param clr
	 */
	public void appendString(boolean includeTimeStamp, boolean privateMessage, String strSender, String strReceiver, String strChatMsg, Color Background, Color senderForeground, Color receiverForeground)
	{
		try
		{		
			//Figure out the Sender and Receiver  colors
			//clrSender = determineTeamColor(strSender);
			//clrReceiver = determineTeamColor(strReceiver);
			
			
			//Indicate if this is a private message, if so, then display a red tag, otherwise, leave it blank because its a broadcast message
			if(privateMessage)
			{
				//Set Attributes for private message tag
				StyleConstants.setBold(attrSet_Text, true);//set the points bold
				StyleConstants.setItalic(attrSet_Text, true);//don't italicize
			    StyleConstants.setUnderline(attrSet_Text, false);//underline it
			    StyleConstants.setForeground(attrSet_Text, Color.white);//set the foreground
			    StyleConstants.setBackground(attrSet_Text, Color.red.darker());//set the background
			    StyleConstants.setFontSize(attrSet_Text, Drivers.fontSize);//font*/
							
				//the len tells us where to place the pointer at the end of the document (caret position)
				lenToColor = document.getLength();
										
				//Place the black text first
				document.insertString(lenToColor, "PRIVATE ", attrSet_Text);		
				
			}
			
			//Place the time stamp if preferred
			if(includeTimeStamp)
			{
				//Set Attributes for UserName
				StyleConstants.setBold(attrSet_Text, false);//set the points bold
				StyleConstants.setItalic(attrSet_Text, false);//don't italicize
			    StyleConstants.setUnderline(attrSet_Text, false);//underline it
			    StyleConstants.setForeground(attrSet_Text, senderForeground);//set the foreground
			    StyleConstants.setBackground(attrSet_Text, Background);//set the background
			    StyleConstants.setFontSize(attrSet_Text, Drivers.fontSize);//font*/
							
				//the len tells us where to place the pointer at the end of the document (caret position)
				lenToColor = document.getLength();
										
				//Place the black text first
				//document.insertString(lenToColor, "[" + parent.parent.timerThread.getTimeStamp_Without_Date() + "] ", attrSet_Text);
				document.insertString(lenToColor, "[" + Drivers.getTimeStamp_Without_Date() + "] ", attrSet_Text);
				
				
			}
							
			//Set Attributes for SENDER
			StyleConstants.setBold(attrSet_Text, true);//set the points bold
			StyleConstants.setItalic(attrSet_Text, false);//don't italicize
		    StyleConstants.setUnderline(attrSet_Text, true);//underline it
		    StyleConstants.setForeground(attrSet_Text, senderForeground);//set the foreground
		    StyleConstants.setBackground(attrSet_Text, Background);//set the background
		    StyleConstants.setFontSize(attrSet_Text, Drivers.fontSize);//font*/
						
			//the len tells us where to place the pointer at the end of the document (caret position)
			lenToColor = document.getLength();
									
			//Place the text
			document.insertString(lenToColor, strSender, attrSet_Text);
			
			//only indicate the Receiver(s) if the message is not being sent to ALL
			if(!(strReceiver.trim().equalsIgnoreCase("ALL") || strReceiver.trim().equalsIgnoreCase("EVERYONE")))
			{
			
				//Set Attributes for Set indicator for who the message is going to. e.g. ->
				StyleConstants.setBold(attrSet_Text, true);//set the points bold
				StyleConstants.setItalic(attrSet_Text, false);//don't italicize
			    StyleConstants.setUnderline(attrSet_Text, true);//underline it
			    StyleConstants.setForeground(attrSet_Text, receiverForeground);//set the foreground
			    StyleConstants.setBackground(attrSet_Text, Background);//set the background
			    StyleConstants.setFontSize(attrSet_Text, Drivers.fontSize);//font*/
							
				//the len tells us where to place the pointer at the end of the document (caret position)
				lenToColor = document.getLength();
										
				//Place the text
				//document.insertString(lenToColor, "-> ", attrSet_Text);
				
				
								
			}
			
			//Set Attributes for Chat Message
			StyleConstants.setBold(attrSet_Text, false);//set the points bold
			StyleConstants.setItalic(attrSet_Text, false);//don't italicize
		    StyleConstants.setUnderline(attrSet_Text, false);//underline it
		    StyleConstants.setForeground(attrSet_Text, senderForeground);//set the foreground
		    StyleConstants.setBackground(attrSet_Text, Background);//set the background
		    StyleConstants.setFontSize(attrSet_Text, Drivers.fontSize);//font
			
		    //Next, Place the rest of their message
			lenToColor = document.getLength();//get new length to begin coloring from
			
			//Place the Chat Message
			document.insertString(lenToColor, ":  " + strChatMsg + "\n", attrSet_Text);
			
			try
			{
				//check for auto scroll
				if(this.autoScroll)//reset the scroll position to the bottom if this is selected
					this.setCaretPosition((document.getLength()));
				
			}
			catch(Exception e)
			{
				System.out.println("Could not set the caret position in ReChatter_Display");
			}
			
			
		}//end try
		catch(Exception e)
		{
			System.out.println("Invalid insert location from appendString in ReChatter_Display class\n" + e.getLocalizedMessage());
		}
		
	}
		
	/**
	 * This is the actual method that performs the placement of the text onto the JTextPane
	 * @param strToAppend
	 * @param clr
	 */
	public void appendString(boolean includeTimeStamp, String speaker, String strChatMsg, Color Foreground, Color Background)
	{
		try
		{		
			//Figure out the Sender and Receiver  colors
			//clrSender = determineTeamColor(strSender);
			//clrReceiver = determineTeamColor(strReceiver);
			
			
			
			
			//Place the time stamp if preferred
			if(includeTimeStamp)
			{
				//Set Attributes for UserName
				StyleConstants.setBold(attrSet_Text, false);//set the points bold
				StyleConstants.setItalic(attrSet_Text, false);//don't italicize
			    StyleConstants.setUnderline(attrSet_Text, false);//underline it
			    StyleConstants.setForeground(attrSet_Text, Foreground);//set the foreground
			    StyleConstants.setBackground(attrSet_Text, Background);//set the background
			    StyleConstants.setFontSize(attrSet_Text, Drivers.fontSize);//font*/
							
				//the len tells us where to place the pointer at the end of the document (caret position)
				lenToColor = document.getLength();
										
				//Place the black text first
				//document.insertString(lenToColor, "[" + parent.parent.timerThread.getTimeStamp_Without_Date() + "] ", attrSet_Text);
				document.insertString(lenToColor, "[" + Drivers.getTimeStamp_Without_Date() + "] ", attrSet_Text);
				
				
			}
							
			//Set Attributes for SENDER
			StyleConstants.setBold(attrSet_Text, true);//set the points bold
			StyleConstants.setItalic(attrSet_Text, false);//don't italicize
		    StyleConstants.setUnderline(attrSet_Text, true);//underline it
		    StyleConstants.setForeground(attrSet_Text, Foreground);//set the foreground
		    StyleConstants.setBackground(attrSet_Text, Background);//set the background
		    StyleConstants.setFontSize(attrSet_Text, Drivers.fontSize);//font*/
						
			//the len tells us where to place the pointer at the end of the document (caret position)
			lenToColor = document.getLength();
									
			//Place the text
			document.insertString(lenToColor, ""+speaker, attrSet_Text);
			
						
			//Set Attributes for Chat Message
			StyleConstants.setBold(attrSet_Text, false);//set the points bold
			StyleConstants.setItalic(attrSet_Text, false);//don't italicize
		    StyleConstants.setUnderline(attrSet_Text, false);//underline it
		    StyleConstants.setForeground(attrSet_Text, Foreground);//set the foreground
		    StyleConstants.setBackground(attrSet_Text, Background);//set the background
		    StyleConstants.setFontSize(attrSet_Text, Drivers.fontSize);//font
			
		    //Next, Place the rest of their message
			lenToColor = document.getLength();//get new length to begin coloring from
			
			//Place the Chat Message
			document.insertString(lenToColor, ":  " + strChatMsg + "\n", attrSet_Text);
			
			try
			{
				//check for auto scroll
				if(this.autoScroll)//reset the scroll position to the bottom if this is selected
					this.setCaretPosition((document.getLength()));
				
			}
			catch(Exception e)
			{
				System.out.println("Could not set the caret position in ReChatter_Display");
			}
			
			
		}//end try
		catch(Exception e)
		{
			System.out.println("Invalid insert location from appendString in ReChatter_Display class\n" + e.getLocalizedMessage());
		}
		
	}
	
	
	/**
	 * This is the actual method that performs the placement of the text onto the JTextPane
	 * @param strToAppend
	 * @param clr
	 */
	public void appendStatusMessageString(boolean includeTimeStamp, String strStatusMsg)
	{
		try
		{		
			//Figure out the Sender and Receiver  colors
			//clrSender = determineTeamColor(strSender);
			//clrReceiver = determineTeamColor(strReceiver);

			
			
			//Place the time stamp if preferred
			if(includeTimeStamp)
			{
				//Set Attributes for UserName
				StyleConstants.setBold(attrSet_Text, false);//set the points bold
				StyleConstants.setItalic(attrSet_Text, false);//don't italicize
			    StyleConstants.setUnderline(attrSet_Text, false);//underline it
			    StyleConstants.setForeground(attrSet_Text, Drivers.clrForeground);//set the foreground
			    StyleConstants.setBackground(attrSet_Text, Drivers.clrBackground);//set the background
			    StyleConstants.setFontSize(attrSet_Text, 18);//font*/
							
				//the len tells us where to place the pointer at the end of the document (caret position)
				lenToColor = document.getLength();
										
				//Place the black text first
				//document.insertString(lenToColor, "[" + parent.parent.timerThread.getTimeStamp_Without_Date() + "] ", attrSet_Text);
				document.insertString(lenToColor, "[" + Drivers.getTimeStamp_Without_Date() + "] ", attrSet_Text);
				
				
			}
							
			//Set Attributes for SENDER
			StyleConstants.setBold(attrSet_Text, true);//set the points bold
			StyleConstants.setItalic(attrSet_Text, false);//don't italicize
		    StyleConstants.setUnderline(attrSet_Text, true);//underline it
		    StyleConstants.setForeground(attrSet_Text, Drivers.clrForeground);//set the foreground
		    StyleConstants.setBackground(attrSet_Text, Drivers.clrBackground);//set the background
		    StyleConstants.setFontSize(attrSet_Text, 18);//font*/
						
			//the len tells us where to place the pointer at the end of the document (caret position)
			lenToColor = document.getLength();
									
			
						
			//Set Attributes for Chat Message
			StyleConstants.setBold(attrSet_Text, false);//set the points bold
			StyleConstants.setItalic(attrSet_Text, false);//don't italicize
		    StyleConstants.setUnderline(attrSet_Text, false);//underline it
		    StyleConstants.setForeground(attrSet_Text, Drivers.clrForeground);//set the foreground
		    StyleConstants.setBackground(attrSet_Text, Drivers.clrBackground);//set the background
		    StyleConstants.setFontSize(attrSet_Text, 18);//font*/
			
		    //Next, Place the rest of their message
			lenToColor = document.getLength();//get new length to begin coloring from
			
			//Place the Chat Message
			document.insertString(lenToColor, strStatusMsg + "\n", attrSet_Text);
			
			try
			{
				//check for auto scroll
				if(this.autoScroll)//reset the scroll position to the bottom if this is selected
					this.setCaretPosition((document.getLength()));
				
			}
			catch(Exception e)
			{
				System.out.println("Could not set the caret position in appendStatusMessageString");
			}
			
			
		}//end try
		catch(Exception e)
		{
			System.out.println("Invalid insert location from appendString in appendStatusMessageString class\n" + e.getLocalizedMessage());
		}
		
	}
	
	
	/**
	 * The purpose of this method is to determine the color of the String name passed in here. 
	 * The team name's color will be returned.  Black will be the default color returned
	 * 
	 */
	public Color determineTeamColor(String strTeamNameToColor)
	{
		try
		{			
				return Color.green;
		}
		catch(Exception e)
		{
			return Color.black;
		}
	}
	
	

}//end class ReChatter_Display
