 /*
 * Date: September 1st 2014
 * Author: Adil Imroz 
 * Twitter handle: @adilimroz
 * Organization: Moolya Software Testing Pvt Ltd
 * License Type: MIT
 */

package utils;

import io.appium.java_client.AppiumDriver;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import jxl.Cell;
import jxl.LabelCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import utils.WebCommonMethods;
import mobilePageObjects.SigninElements; //Update this as per your context

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;

public class MobileCommonMethods 
{

	public static AppiumDriver driver;

	public static int sleepTimeMin;
	public static int sleepTimeMin2;
	public static int sleepTimeAverage;
	public static int sleepTimeAverage2;
	public static int sleepTimeMax;
	public static int sleepTimeMax2;

	public static int Btn1;
	public static int Btn2;
	public static int Btn3;
	public static int Btn4;
	public static int Btn5;
	public static int Btn6;
	public static int Btn7;
	public static int Btn8;
	public static int Btn9;
	public static int Btn10;	

	public static int key1;
	public static int key2;
	public static int key3;
	public static int key4;
	public static int key5;
	public static int key6;
	public static int key7;
	public static int key8;
	public static int key9;
	public static int key10;

	static Workbook wrk1;
	static Sheet sheet1;

	//Method to dynamically retrieve any 1 specific record from any mobile excel sheet
	public static Cell[] mobileReadExcel(String sheetName, String uniqueValue) throws BiffException, IOException
	{
		wrk1 = Workbook.getWorkbook(new File("../seleniumWebdriverLearningSnippets/testdata/mobileTestData.xls")); // Connecting to excel workbook.
		sheet1 = wrk1.getSheet(sheetName); // Connecting to the sheet

		LabelCell cell=sheet1.findLabelCell(uniqueValue);
		int row=cell.getRow();
		Cell[] record = sheet1.getRow(row);
		return record;
	}
	
	//Method to dynamically retrieve any 1 specific record which is just below the uniqueValue specified
	public static Cell[] mobileReadExcelNextRowOfUniqueValue(String sheetName, String uniqueValue) throws BiffException, IOException 
	{
		wrk1 = Workbook.getWorkbook(new File("../seleniumWebdriverLearningSnippets/testdata/mobileTestData.xls")); // Connecting to excel workbook.
		sheet1 = wrk1.getSheet(sheetName); // Connecting to the sheet
		LabelCell cell=sheet1.findLabelCell(uniqueValue);
		int row=cell.getRow();
		Cell[] record = sheet1.getRow(row+1);
		return record;
	}



	public static void initializeSleepTimings() throws IOException
	{
		FileReader reader = new FileReader("../seleniumWebdriverLearningSnippets/config.properties"); //Reading configuration file
		Properties prop = new Properties();
		prop.load(reader);
		sleepTimeMin = Integer.parseInt(prop.getProperty("sleepTimeMin"));
		//System.out.println("sleepTimeMin:" + sleepTimeMin);
		sleepTimeMin2 = Integer.parseInt(prop.getProperty("sleepTimeMin2"));
		//System.out.println("sleepTimeMin2:" + sleepTimeMin2);
		sleepTimeAverage = Integer.parseInt(prop.getProperty("sleepTimeAverage"));
		//System.out.println("sleepTimeAverage:" + sleepTimeAverage);
		sleepTimeAverage2 = Integer.parseInt(prop.getProperty("sleepTimeAverage2"));
		sleepTimeMax = Integer.parseInt(prop.getProperty("sleepTimeMax"));
		//System.out.println("sleepTimeMax:" + sleepTimeMax);
		sleepTimeMax2 = Integer.parseInt(prop.getProperty("sleepTimeMax2"));
	}
	
	public static int mobileNumberKeyEventsEntry(int buttonValue){

		if (buttonValue == 0)
		{
			buttonValue = 7;
		}

		if (buttonValue == 1)
		{
			buttonValue = 8;
		}

		if (buttonValue == 2)
		{
			buttonValue = 9;
		}

		if (buttonValue == 3)
		{
			buttonValue = 10;
		}

		if (buttonValue == 4)
		{
			buttonValue = 11;
		}

		if (buttonValue == 5)
		{
			buttonValue = 12;
		}

		if (buttonValue == 6)
		{
			buttonValue = 13;
		}

		if (buttonValue == 7)
		{
			buttonValue = 14;
		}

		if (buttonValue == 8)
		{
			buttonValue = 15;
		}

		if (buttonValue == 9)
		{
			buttonValue = 16;
		}

		return buttonValue;
	}

	public static void keyEventsForMobileNumber(String UserData) throws BiffException, IOException{

		Cell[] cashInDetails = MobileCommonMethods.mobileReadExcel("cashInData",UserData);

		String mobileNumber_btn1 = cashInDetails[2].getContents().substring(0, 1);
		Btn1 = Integer.parseInt(mobileNumber_btn1);
		key1 = MobileCommonMethods.mobileNumberKeyEventsEntry(Btn1);

		String mobileNumber_btn2 = cashInDetails[2].getContents().substring(1, 2);
		Btn2 = Integer.parseInt(mobileNumber_btn2);
		key2 = MobileCommonMethods.mobileNumberKeyEventsEntry(Btn2);

		String mobileNumber_btn3 = cashInDetails[2].getContents().substring(2, 3);
		Btn3 = Integer.parseInt(mobileNumber_btn3);
		key3 = MobileCommonMethods.mobileNumberKeyEventsEntry(Btn3);

		String mobileNumber_btn4 = cashInDetails[2].getContents().substring(3, 4);
		Btn4 = Integer.parseInt(mobileNumber_btn4);
		key4 = MobileCommonMethods.mobileNumberKeyEventsEntry(Btn4);

		String mobileNumber_btn5 = cashInDetails[2].getContents().substring(4, 5);
		Btn5 = Integer.parseInt(mobileNumber_btn5);
		key5 = MobileCommonMethods.mobileNumberKeyEventsEntry(Btn5);

		String mobileNumber_btn6 = cashInDetails[2].getContents().substring(5, 6);
		Btn6 = Integer.parseInt(mobileNumber_btn6);
		key6 = MobileCommonMethods.mobileNumberKeyEventsEntry(Btn6);

		String mobileNumber_btn7 = cashInDetails[2].getContents().substring(6, 7);
		Btn7 = Integer.parseInt(mobileNumber_btn7);
		key7 = MobileCommonMethods.mobileNumberKeyEventsEntry(Btn7);

		String mobileNumber_btn8 = cashInDetails[2].getContents().substring(7, 8);
		Btn8 = Integer.parseInt(mobileNumber_btn8);
		key8 = MobileCommonMethods.mobileNumberKeyEventsEntry(Btn8);

		String mobileNumber_btn9 = cashInDetails[2].getContents().substring(8, 9);
		Btn9 = Integer.parseInt(mobileNumber_btn9);
		key9 = MobileCommonMethods.mobileNumberKeyEventsEntry(Btn9);

		String mobileNumber_btn10 = cashInDetails[2].getContents().substring(9, 10);
		Btn10 = Integer.parseInt(mobileNumber_btn10);
		key10 = MobileCommonMethods.mobileNumberKeyEventsEntry(Btn10);
	}

	//	public static void sendMobileNumberKeyEvents(){
	//		
	//		driver.findElementByName(ObjectsCashIn.TextField_ConfirmMobileNumber).click();
	//		driver.sendKeyEvent(MobileCommonMethods.key1);
	//		driver.sendKeyEvent(MobileCommonMethods.key2);
	//		driver.sendKeyEvent(MobileCommonMethods.key3);
	//		driver.sendKeyEvent(MobileCommonMethods.key4);
	//		driver.sendKeyEvent(MobileCommonMethods.key5);
	//		driver.sendKeyEvent(MobileCommonMethods.key6);
	//		driver.sendKeyEvent(MobileCommonMethods.key7);
	//		driver.sendKeyEvent(MobileCommonMethods.key8);
	//		driver.sendKeyEvent(MobileCommonMethods.key9);
	//		driver.sendKeyEvent(MobileCommonMethods.key10);
	//	}


	// Method to launch the Application while it does not get uninstalled during script execution
	public static void launchApp1() throws BiffException, IOException { //Update this method based on your app context. This is just for code reference
		System.out.println("*****Launching the app*****");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "Android");
		capabilities.setCapability("appPackage","com.moolya.boi");
		capabilities.setCapability("appActivity","com.moolya.boi.ShellAppBOIMPay");
		driver = new AppiumDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities); // initializing the driver
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	}

	// Method for Agent sigin
	public static void appSignin(String username) throws InterruptedException, BiffException, IOException {//Update this method based on your app context. This is just for code reference

		// Text with signin button :: Sign in
		Cell[] record = MobileCommonMethods.mobileReadExcel("validLogin",username); // read from excel
		String mobileNumber = record[1].getContents();
		driver.findElementByName(SigninElements.textField_projectNameSignIn).sendKeys(mobileNumber);
		driver.findElementByName(SigninElements.button_textField_projectNameSignIn).click();
		MobileCommonMethods.mPinEntry(username); // method to enter the mpin
		driver.findElementByName(SigninElements.button_TermsOfServiceAccept).click();//No content desc for this button
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	}

	public static void mPinEntry(String username) throws BiffException, IOException{//Update this method based on your app context. This is just for code reference

		// Fetching the mPin of the mentioned user from the excel
		Cell[] record = MobileCommonMethods.mobileReadExcel("validLogin",username);
		String mPin_btn1 = record[2].getContents().substring(0, 1);
		String mPin_btn2 = record[2].getContents().substring(1, 2);
		String mPin_btn3 = record[2].getContents().substring(2, 3);
		String mPin_btn4 = record[2].getContents().substring(3, 4);

		driver.findElementByName("layout_mpin_entry_btnNum"+mPin_btn1).click();	//testing server password
		driver.findElementByName("layout_mpin_entry_btnNum"+mPin_btn2).click();	//testing server password
		driver.findElementByName("layout_mpin_entry_btnNum"+mPin_btn3).click();	//testing server password
		driver.findElementByName("layout_mpin_entry_btnNum"+mPin_btn4).click(); //testing server password
		driver.findElementByName("layout_mpin_entry_btnDone").click();//tap on tick button
	}


	//	public static void signout1() {
	//		
	//		driver.sendKeyEvent(4);
	//		if (driver.findElementByName("Do you wish to go back and retry?").isDisplayed())
	//		{
	//			driver.findElementByName("Retry").click();
	//			driver.sendKeyEvent(4);
	//			driver.findElementByName("Go Back").click();
	//			driver.sendKeyEvent(4);
	//			driver.findElementByName("Stop").click();
	//			driver.sendKeyEvent(4);
	//			driver.sendKeyEvent(4);
	//			driver.findElementByName("Sign Out").click();
	//			System.out.println("Signed Out Successfully");
	//		}
	//		
	//		else if (driver.findElementByName("Do you wish to stop this transaction?").isDisplayed())
	//		{
	//			driver.findElementByName("Stop").click();
	//			driver.sendKeyEvent(4);
	//			driver.sendKeyEvent(4);
	//			driver.findElementByName("Sign Out").click();
	//			System.out.println("Signed Out Successfully");
	//		}
	//		else if (driver.findElementByName("Do you wish to go back to the previous step?").isDisplayed())
	//		{
	//			driver.findElementByName("Go Back").click();
	//			driver.sendKeyEvent(4);
	//			driver.findElementByName("Stop").click();
	//			driver.sendKeyEvent(4);
	//			driver.sendKeyEvent(4);
	//			driver.findElementByName("Sign Out").click();
	//			System.out.println("Signed Out Successfully");
	//		}
	//		else
	//		{
	//			driver.findElementByName(ObjectsGeneral.Button_NavigateUp).click();
	//			driver.findElementByName("Stop").click();
	//			driver.sendKeyEvent(4);
	//			driver.sendKeyEvent(4);
	//			driver.findElementByName("Sign Out").click();
	//			System.out.println("Signed Out Successfully");
	////		}
	//	}

	//Method to signout from app
	public static void signout() throws InterruptedException { //Update this method based on your app context. This is just for code reference
		try{
			driver.navigate().back(); //back from boi app page
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); 
			driver.navigate().back(); //back from select partner page
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); 
			driver.findElementByName("Sign Out").isDisplayed();
			driver.findElementByName("Sign Out").click();// signout btn
		}
		catch(Exception e)
		{
			System.out.println("Caught exception during signout process...");
			boolean present;
			boolean namaskaar;
			try 
			{
				driver.findElement(By.name("Dismiss"));
				present = true;
			} catch (org.openqa.selenium.NoSuchElementException e1) {
				present = false;
			}
			try
			{
				driver.findElement(By.name("namaskaar"));
				namaskaar = true;
			}
			catch (org.openqa.selenium.NoSuchElementException e1) {
				namaskaar = false;
			}
			if(present == true)
			{
				driver.findElementByName("Dismiss").click();
				driver.findElementByName("Navigate up").click();
				driver.findElementByName("Stop").click(); // click on Stop button
				driver.navigate().back();
				driver.findElementByName("Sign Out").click();// signout
			}
			else if(namaskaar == true)	
			{
				driver.navigate().back();
				driver.findElementByName("Sign Out").click();// signout
			}
			else
			{
				driver.findElementByName("Cancel").click();
				driver.findElementByName("Navigate up").click();
				driver.findElementByName("Stop").click(); // click on Stop button
				driver.navigate().back();
				driver.findElementByName("Sign Out").click();// signout
			}
			// btn
		}
		finally {
			driver.quit();
		}
	}



}
