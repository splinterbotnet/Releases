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
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.io.PrintStream;
import javax.swing.JTextPane;
import javax.swing.plaf.TextUI;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class JPanel_TextDisplayPane extends JTextPane
{
  String strMyClassName = getName();

  StyleContext context = new StyleContext();
  public StyledDocument document = new DefaultStyledDocument(this.context);
  Style style = this.context.getStyle("default");

  StyleContext stlCtx = null;
  SimpleAttributeSet attrSet_Text = null;

  public boolean autoScroll = true;

  String[] textStyles = { "regular", "italic", "bold", "small", "large", "button", "icon" };

  int lenToColor = 0;

  Color clrSender = Color.black; Color clrReceiver = Color.black;

  public volatile boolean includeTimeStamp = true;

  public JPanel_MainControllerWindow myMainControllerWindow = null;

  public JPanel_TextDisplayPane()
  {
    setEditable(false);
    setBackground(Drivers.clrBackground);
    setDocument(this.document);

    this.stlCtx = StyleContext.getDefaultStyleContext();

    this.attrSet_Text = new SimpleAttributeSet();
    StyleConstants.setBold(this.attrSet_Text, false);
    StyleConstants.setItalic(this.attrSet_Text, false);
    StyleConstants.setUnderline(this.attrSet_Text, false);
    StyleConstants.setForeground(this.attrSet_Text, Color.BLACK);
    StyleConstants.setFontSize(this.attrSet_Text, Drivers.fontSize);

    StyleConstants.setAlignment(this.style, 0);
  }

  public boolean getScrollableTracksViewportWidth()
  {
    return getUI().getPreferredSize(this).width <= getParent().getSize().width;
  }

  public void clearTextPane()
  {
    try
    {
      this.document.remove(0, this.document.getLength() - 1);
    }
    catch (Exception localException)
    {
    }
  }

  public void appendString(boolean includeTimeStamp, String strChatMsg, Color Foreground, Color Background)
  {
    try
    {
      if (includeTimeStamp)
      {
        StyleConstants.setBold(this.attrSet_Text, false);
        StyleConstants.setItalic(this.attrSet_Text, false);
        StyleConstants.setUnderline(this.attrSet_Text, false);
        StyleConstants.setForeground(this.attrSet_Text, Foreground);
        StyleConstants.setBackground(this.attrSet_Text, Background);
        StyleConstants.setFontSize(this.attrSet_Text, Drivers.fontSize);

        this.lenToColor = this.document.getLength();

        this.document.insertString(this.lenToColor, "[" + Drivers.getTimeStamp_Without_Date() + "] ", this.attrSet_Text);
      }

      StyleConstants.setBold(this.attrSet_Text, true);
      StyleConstants.setItalic(this.attrSet_Text, false);
      StyleConstants.setUnderline(this.attrSet_Text, true);
      StyleConstants.setForeground(this.attrSet_Text, Foreground);
      StyleConstants.setBackground(this.attrSet_Text, Background);
      StyleConstants.setFontSize(this.attrSet_Text, Drivers.fontSize);

      this.lenToColor = this.document.getLength();

      StyleConstants.setBold(this.attrSet_Text, false);
      StyleConstants.setItalic(this.attrSet_Text, false);
      StyleConstants.setUnderline(this.attrSet_Text, false);
      StyleConstants.setForeground(this.attrSet_Text, Foreground);
      StyleConstants.setBackground(this.attrSet_Text, Background);
      StyleConstants.setFontSize(this.attrSet_Text, Drivers.fontSize);

      this.lenToColor = this.document.getLength();

      this.document.insertString(this.lenToColor, strChatMsg + "\n", this.attrSet_Text);
      try
      {
        if (this.autoScroll) {
          setCaretPosition(this.document.getLength());
        }
      }
      catch (Exception e)
      {
        System.out.println("Could not set the caret position in ReChatter_Display");
      }

    }
    catch (Exception e)
    {
      System.out.println("Invalid insert location from appendString in ReChatter_Display class\n" + e.getLocalizedMessage());
    }
  }

  public void appendString(boolean includeTimeStamp, boolean privateMessage, String strSender, String strReceiver, String strChatMsg, Color Background, Color senderForeground, Color receiverForeground)
  {
    try
    {
      if (privateMessage)
      {
        StyleConstants.setBold(this.attrSet_Text, true);
        StyleConstants.setItalic(this.attrSet_Text, true);
        StyleConstants.setUnderline(this.attrSet_Text, false);
        StyleConstants.setForeground(this.attrSet_Text, Color.white);
        StyleConstants.setBackground(this.attrSet_Text, Color.red.darker());
        StyleConstants.setFontSize(this.attrSet_Text, Drivers.fontSize);

        this.lenToColor = this.document.getLength();

        this.document.insertString(this.lenToColor, "PRIVATE ", this.attrSet_Text);
      }

      if (includeTimeStamp)
      {
        StyleConstants.setBold(this.attrSet_Text, false);
        StyleConstants.setItalic(this.attrSet_Text, false);
        StyleConstants.setUnderline(this.attrSet_Text, false);
        StyleConstants.setForeground(this.attrSet_Text, senderForeground);
        StyleConstants.setBackground(this.attrSet_Text, Background);
        StyleConstants.setFontSize(this.attrSet_Text, Drivers.fontSize);

        this.lenToColor = this.document.getLength();

        this.document.insertString(this.lenToColor, "[" + Drivers.getTimeStamp_Without_Date() + "] ", this.attrSet_Text);
      }

      StyleConstants.setBold(this.attrSet_Text, true);
      StyleConstants.setItalic(this.attrSet_Text, false);
      StyleConstants.setUnderline(this.attrSet_Text, true);
      StyleConstants.setForeground(this.attrSet_Text, senderForeground);
      StyleConstants.setBackground(this.attrSet_Text, Background);
      StyleConstants.setFontSize(this.attrSet_Text, Drivers.fontSize);

      this.lenToColor = this.document.getLength();

      this.document.insertString(this.lenToColor, strSender, this.attrSet_Text);

      if ((!strReceiver.trim().equalsIgnoreCase("ALL")) && (!strReceiver.trim().equalsIgnoreCase("EVERYONE")))
      {
        StyleConstants.setBold(this.attrSet_Text, true);
        StyleConstants.setItalic(this.attrSet_Text, false);
        StyleConstants.setUnderline(this.attrSet_Text, true);
        StyleConstants.setForeground(this.attrSet_Text, receiverForeground);
        StyleConstants.setBackground(this.attrSet_Text, Background);
        StyleConstants.setFontSize(this.attrSet_Text, Drivers.fontSize);

        this.lenToColor = this.document.getLength();
      }

      StyleConstants.setBold(this.attrSet_Text, false);
      StyleConstants.setItalic(this.attrSet_Text, false);
      StyleConstants.setUnderline(this.attrSet_Text, false);
      StyleConstants.setForeground(this.attrSet_Text, senderForeground);
      StyleConstants.setBackground(this.attrSet_Text, Background);
      StyleConstants.setFontSize(this.attrSet_Text, Drivers.fontSize);

      this.lenToColor = this.document.getLength();

      this.document.insertString(this.lenToColor, ":  " + strChatMsg + "\n", this.attrSet_Text);
      try
      {
        if (this.autoScroll) {
          setCaretPosition(this.document.getLength());
        }
      }
      catch (Exception e)
      {
        System.out.println("Could not set the caret position in ReChatter_Display");
      }

    }
    catch (Exception e)
    {
      System.out.println("Invalid insert location from appendString in ReChatter_Display class\n" + e.getLocalizedMessage());
    }
  }

  public void appendString(boolean includeTimeStamp, String speaker, String strChatMsg, Color Foreground, Color Background)
  {
    try
    {
      if (includeTimeStamp)
      {
        StyleConstants.setBold(this.attrSet_Text, false);
        StyleConstants.setItalic(this.attrSet_Text, false);
        StyleConstants.setUnderline(this.attrSet_Text, false);
        StyleConstants.setForeground(this.attrSet_Text, Foreground);
        StyleConstants.setBackground(this.attrSet_Text, Background);
        StyleConstants.setFontSize(this.attrSet_Text, Drivers.fontSize);

        this.lenToColor = this.document.getLength();

        this.document.insertString(this.lenToColor, "[" + Drivers.getTimeStamp_Without_Date() + "] ", this.attrSet_Text);
      }

      StyleConstants.setBold(this.attrSet_Text, true);
      StyleConstants.setItalic(this.attrSet_Text, false);
      StyleConstants.setUnderline(this.attrSet_Text, true);
      StyleConstants.setForeground(this.attrSet_Text, Foreground);
      StyleConstants.setBackground(this.attrSet_Text, Background);
      StyleConstants.setFontSize(this.attrSet_Text, Drivers.fontSize);

      this.lenToColor = this.document.getLength();

      this.document.insertString(this.lenToColor, speaker, this.attrSet_Text);

      StyleConstants.setBold(this.attrSet_Text, false);
      StyleConstants.setItalic(this.attrSet_Text, false);
      StyleConstants.setUnderline(this.attrSet_Text, false);
      StyleConstants.setForeground(this.attrSet_Text, Foreground);
      StyleConstants.setBackground(this.attrSet_Text, Background);
      StyleConstants.setFontSize(this.attrSet_Text, Drivers.fontSize);

      this.lenToColor = this.document.getLength();

      this.document.insertString(this.lenToColor, ":  " + strChatMsg + "\n", this.attrSet_Text);
      try
      {
        if (this.autoScroll) {
          setCaretPosition(this.document.getLength());
        }
      }
      catch (Exception e)
      {
        System.out.println("Could not set the caret position in ReChatter_Display");
      }

    }
    catch (Exception e)
    {
      System.out.println("Invalid insert location from appendString in ReChatter_Display class\n" + e.getLocalizedMessage());
    }
  }

  public void appendStatusMessageString(boolean includeTimeStamp, String strStatusMsg)
  {
    try
    {
      if (includeTimeStamp)
      {
        StyleConstants.setBold(this.attrSet_Text, false);
        StyleConstants.setItalic(this.attrSet_Text, false);
        StyleConstants.setUnderline(this.attrSet_Text, false);
        StyleConstants.setForeground(this.attrSet_Text, Drivers.clrForeground);
        StyleConstants.setBackground(this.attrSet_Text, Drivers.clrBackground);
        StyleConstants.setFontSize(this.attrSet_Text, 18);

        this.lenToColor = this.document.getLength();

        this.document.insertString(this.lenToColor, "[" + Drivers.getTimeStamp_Without_Date() + "] ", this.attrSet_Text);
      }

      StyleConstants.setBold(this.attrSet_Text, true);
      StyleConstants.setItalic(this.attrSet_Text, false);
      StyleConstants.setUnderline(this.attrSet_Text, true);
      StyleConstants.setForeground(this.attrSet_Text, Drivers.clrForeground);
      StyleConstants.setBackground(this.attrSet_Text, Drivers.clrBackground);
      StyleConstants.setFontSize(this.attrSet_Text, 18);

      this.lenToColor = this.document.getLength();

      StyleConstants.setBold(this.attrSet_Text, false);
      StyleConstants.setItalic(this.attrSet_Text, false);
      StyleConstants.setUnderline(this.attrSet_Text, false);
      StyleConstants.setForeground(this.attrSet_Text, Drivers.clrForeground);
      StyleConstants.setBackground(this.attrSet_Text, Drivers.clrBackground);
      StyleConstants.setFontSize(this.attrSet_Text, 18);

      this.lenToColor = this.document.getLength();

      this.document.insertString(this.lenToColor, strStatusMsg + "\n", this.attrSet_Text);
      try
      {
        if (this.autoScroll) {
          setCaretPosition(this.document.getLength());
        }
      }
      catch (Exception e)
      {
        System.out.println("Could not set the caret position in appendStatusMessageString");
      }

    }
    catch (Exception e)
    {
      System.out.println("Invalid insert location from appendString in appendStatusMessageString class\n" + e.getLocalizedMessage());
    }
  }

  public Color determineTeamColor(String strTeamNameToColor)
  {
    try
    {
      return Color.green;
    }
    catch (Exception e) {
    }
    return Color.black;
  }
}