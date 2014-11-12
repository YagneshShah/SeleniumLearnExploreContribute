/*
 * Date: September 1st 2014
 * Author: Yagnesh Shah   
 * Twitter handle: @YagneshHShah
 * Organization: Moolya Software Testing Pvt Ltd
 * License Type: MIT
*/


package webMainScripts.loginLogout;

import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.gjt.mm.mysql.Driver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import utils.SelectBrowser;

import utils.WebCommonMethods;
import webMethods.LoginLogoutMethods;


public class LogoutAndSessionExpiresOnBackButton extends SelectBrowser
{
	WebCommonMethods general;
	LoginLogoutMethods loginLogout;
	
	String appserver; 

	@Test(priority=1, groups={"loginlogout"})
	public void LogoutAndSessionExpiresOnBackButtonClick() throws BiffException, IOException, InterruptedException
	{	
		System.out.println("----------------LogoutAndSessionExpiresOnBackButtonClick check started----------------");

		WebDriver d = getBrowser();
	    loginLogout = PageFactory.initElements(d, LoginLogoutMethods.class);
	    general = PageFactory.initElements(d, WebCommonMethods.class);// initiating the driver and the .class file (the pageObject script)	    
	    
	  	WebCommonMethods.openURL(); 	
	  	
		LoginLogoutMethods.login("admin");
		Thread.sleep(4000);
		LoginLogoutMethods.logout();
		
		//session expiry check
		d.navigate().back();
		//System.out.println("Current URL: " + d.getCurrentUrl());
		Assert.assertEquals(d.getCurrentUrl(), "http://localhost/orangehrm-3.1.2/orangehrm-3.1.2/symfony/web/index.php/pim/viewEmployeeList");
		
		Cell[] loginPageLabels = WebCommonMethods.readExcelNextRowOfUniqueValue("webTabsWithSubheading", "#loginPage");
		Assert.assertEquals(LoginLogoutMethods.loginPanelHeadingLabel.getText(), loginPageLabels[1].getContents());
		System.out.println("----------------LogoutAndSessionExpiresOnBackButtonClick check success----------------");
	}

	
	@AfterMethod(alwaysRun=true)
    public void catchExceptions(ITestResult result) throws IOException 
    {    
    	String methodname = result.getName();
        if(!result.isSuccess()){            
        	WebCommonMethods.screenshot(methodname);
        }
        WebCommonMethods.quit(); // Calling function close to quit browser instance
    }

}
