/*
 * Date: September 1st 2014
 * Author: Yagnesh Shah   
 * Twitter handle: @YagneshHShah
 * Organization: Moolya Software Testing Pvt Ltd
 * License Type: MIT
 */

package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


//import jxl.read.biff.BiffException;
import jxl.Cell;
import jxl.LabelCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.Reporter;

import utils.SelectBrowser;

public class WebCommonMethods extends SelectBrowser {

	static Workbook wrk1;
	static Sheet sheet1;
	//static Cell colRow;

	public static int sleepTimeMin;
	public static int sleepTimeMin2;
	public static int sleepTimeAverage;
	public static int sleepTimeAverage2;
	public static int sleepTimeMax;
	public static int sleepTimeMax2;

	public static WebDriver driver;	
	public WebCommonMethods(WebDriver driver){
		this.driver=driver;
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

	public static void windowMax() throws IOException {
		driver.manage().window().maximize();
		initializeSleepTimings();
	}

	public static void deletecookies() {
		driver.manage().deleteAllCookies();
	}

	public static boolean isElementPresent(WebDriver driver, By by)
	{
		try
		{
			return driver.findElements(by).size() > 0;
		}
		catch(Exception e) //NoSuchElementException
		{
			return false;
		}
	}

	public static void callingImplicitSleep() throws IOException{
		FileReader reader = new FileReader("../seleniumWebdriverLearningSnippets/config.properties"); 
		Properties prop = new Properties();
		prop.load(reader); 
		int implicitWaitTime = Integer.parseInt(prop.getProperty("implicitWaitTime"));

		driver.manage().timeouts().implicitlyWait(implicitWaitTime, TimeUnit.SECONDS);
	}

	public static void openURL() throws IOException
	{
		FileReader reader = new FileReader("../seleniumWebdriverLearningSnippets/config.properties"); 
		Properties prop = new Properties();
		prop.load(reader);    	  
		String appServer = prop.getProperty("URL");

		deletecookies();
		driver.get(appServer);
		windowMax();
		callingImplicitSleep();
	}

	public static void screenshot(String methodName) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");                   
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new
					File(("../seleniumWebdriverLearningSnippets/failure_screenshots/" + methodName + "_" + formater.format(calendar.getTime())+".png")));
			Reporter.log("<a href='" +
					"../seleniumWebdriverLearningSnippets/failure_screenshots/" + methodName + "_" + formater.format(calendar.getTime()) + ".png'> <imgsrc='" + "C:/failure_screenshots/" + methodName + "_" + formater.format(calendar.getTime()) + ".png' /> </a>");
			Reporter.setCurrentTestResult(null);
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	//Method to dynamically retrieve any 1 specific record from any web excel sheet
	public static Cell[] readExcel(String sheetName, String uniqueValue) throws BiffException, IOException
	{
		wrk1 = Workbook.getWorkbook(new File("../seleniumWebdriverLearningSnippets/testdata/webTestData.xls")); // Connecting to excel workbook.
		sheet1 = wrk1.getSheet(sheetName); // Connecting to the sheet

		LabelCell cell=sheet1.findLabelCell(uniqueValue);
		int row=cell.getRow();
		Cell[] record = sheet1.getRow(row);
		return record;
	}

	//Method to dynamically retrieve any 1 specific record which is just below the uniqueValue specified
	public static Cell[] readExcelNextRowOfUniqueValue(String sheetName, String uniqueValue) throws BiffException, IOException 
	{
		wrk1 = Workbook.getWorkbook(new File("../seleniumWebdriverLearningSnippets/testdata/webTestData.xls")); // Connecting to excel workbook.
		sheet1 = wrk1.getSheet(sheetName); // Connecting to the sheet
		LabelCell cell=sheet1.findLabelCell(uniqueValue);
		int row=cell.getRow();
		Cell[] record = sheet1.getRow(row+1);
		return record;
	}


//	//Method to dynamically retrieve "N" records from any excel sheet...This is still not complete & requires work to be done...
//	@SuppressWarnings("null")
//	public static String[][] readExcel(String sheetName, int nRecords) throws BiffException, IOException
//	{
//		//List<String> list1 = new ArrayList();
//		String records[][]=new String [nRecords][50];
//
//		wrk1 = Workbook.getWorkbook(new File("../SeleniumWebdriverSnippets/testdata/webTestData.xls")); // Connecting to excel workbook.
//		sheet1 = wrk1.getSheet(sheetName); // Connecting to the sheet
//
//		for(int i=1; i<nRecords; i++) //starting from i=1 since i=0 is a column name & not the value
//		{
//			Cell uname = sheet1.getCell(0,i);  //Column=0=username,
//			String check = uname.getContents();
//			Boolean b1= check.isEmpty();
//			if(b1){
//				break;// Script will  end when it found no entry in the Serial No. column of the excel sheet.
//			} 
//
//			Cell[] row = sheet1.getRow(i);
//			System.out.println("rows: "+row.length);
//
//			//System.out.println("record: " + record);
//			for (int j=0; j<row.length; j++)
//			{
//				records[i-1][j]=row[j].getContents();
//				//System.out.println("array: "+ records[i-1][j]);
//			}
//		}
//
//		//		for (int i=0; i<records.length; i++)
//		//        {	//String row[]=records[i];
//		//			System.out.println("rows: "+records.length);
//		////			System.out.println("column: "+records[i].length);
//		//			for (int j=0; j < 2; j++)
//		//			{
//		//	        	System.out.println("array: "+ records[i][j]);				
//		//			}
//		//        }	
//		return records;
//	}


	public static void quit()
	{
		driver.quit(); // Closing Browser instance
	}
	
	
	
	
	
	/*This method is helpful when there is a table created with N columns but the rows are created dynamically based on data.
	 * Example: suppose we wish to click on 'Edit' button (which is column 8 i.e, Html <Td> tab value is 8) for a username 'Yagnesh'.
	 * Now, username 'Yagnesh' could be on any row (ie, Html <tr> value could be anything as the row data is dynamic)
	 * This method aids to dynamically retrieve <tr> & <td> value for specific cell in a table structure. 
	 * Once it is obtained we may combine the xPath by using that dynamic <tr> & <td> value retrieved in order to click on 'Edit' button
	 */
	public static int RetrieveHeadingTdValue(String excelCellTabNameHeadingsTdValue, String excelCellTabNameUniqueHashValue) throws BiffException, IOException
	{
		//String orgHeading;
		String orgHeadingTdNumber;
		int headingTdValue=0;
		Cell[] orgTdValues = WebCommonMethods.readExcelNextRowOfUniqueValue("webTabsWithSubheading", excelCellTabNameUniqueHashValue);
		for(int i=1; i<=orgTdValues.length; i++)
		{
			//System.out.println("orgTdValues: " + orgTdValues[i].getContents());
			if(orgTdValues[i].getContents().contains(excelCellTabNameHeadingsTdValue))
			{
				orgHeadingTdNumber = orgTdValues[i].getContents();
				orgHeadingTdNumber = orgHeadingTdNumber.replaceAll("[a-zA-Z ]", "");
				headingTdValue = Integer.parseInt(orgHeadingTdNumber);
				//System.out.println(orgTdValues[i].getContents() + " / " + orgHeadingTdNumber + " / " + headingTdValue);
				break;
			}
		}
		Assert.assertTrue(headingTdValue!=0, "Failed to retrieve <td> value of Heading...");		
		return headingTdValue;
	}

	public static int RetrieveHeadingTrValue(String headingClassName, String orgCodeValue) throws BiffException, IOException
	{	
		int headingTrValue=0; 
		String orgCodeListValue;
		//		//xpath for each rows in Organization listing page .//*[@id='userTable']/tbody/tr[1]/td[3]/span/text()
		//		String start = ".//*[@id='userTable']/tbody/tr["; 
		//		String end = "]/td[" + headingTdValue + "]/span";
		//		String xpathExpression;
		//		List<String> headingColumnValuesFromAllRows = null;
		//		
		//		//retrieve first 10 names from page 1
		//		for (int i=1; i<=10; i++)
		//		{
		//			xpathExpression = start+i+end;
		//			//System.out.println("xpathExpression:" + xpathExpression);
		//		//	headingColumnValuesFromAllRows.add(driver.findElement(By.xpath(xpathExpression)).getText());
		//			//System.out.println(rowText1.findElement(By.className("orgCode")).getText());
		//			System.out.println(driver.findElement(By.xpath(xpathExpression)).getText());
		//		}

		List<WebElement> headingColumnValuesFromAllRowsList = driver.findElements(By.className(headingClassName));

		for (int i = 0; i < headingColumnValuesFromAllRowsList.size(); i++) 
		{
			System.out.println(headingColumnValuesFromAllRowsList.get(i).getText() + " / " + orgCodeValue);
			orgCodeListValue = headingColumnValuesFromAllRowsList.get(i).getText();
			if(orgCodeListValue.contains(orgCodeValue))
			{
				headingTrValue=i+1;
				break;
			}
		}
		Assert.assertTrue(headingTrValue!=0, "Failed to retrieve <tr> value of Heading...");
		return headingTrValue;
	}

}