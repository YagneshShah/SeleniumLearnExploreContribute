/*
 * Date: September 1st 2014
 * Author: Yagnesh Shah   
 * Twitter handle: @YagneshHShah
 * Organization: Moolya Software Testing Pvt Ltd
 * License Type: MIT
*/


/*
Aim: Tests for Log-in/Log-out features. Following tests are covered in this script:
	assertLoginElements
	maskedPassword
	emptyLogin
	partialFillLogin
	invalidLogin
	capsOnLogin (Suggestion: No message displayed if caps lock is on)
	validlLogin
	loginReliabilityAndSigninAgain	
	logoutSessionExpire
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


public class LoginReliabilityAndSigninAgainCheck extends SelectBrowser
{
	WebCommonMethods general;
	LoginLogoutMethods loginLogout;
		
	@BeforeMethod
    public void openTheBrowser() throws IOException 
    {
    	WebDriver d = getBrowser();
	    loginLogout = PageFactory.initElements(d, LoginLogoutMethods.class);
	    general = PageFactory.initElements(d, WebCommonMethods.class);// initiating the driver and the .class file (the pageObject script)	    
	    
	  	WebCommonMethods.openURL();
    } 

	// loginReliability&SigninAgain test :: data-driven, no overhead of list
	@Test(priority=1, groups={"loginlogout"})
	public void loginReliabilityAndSigninAgain() throws BiffException, IOException, InterruptedException
	{	
		System.out.println("Checking for LoginReliabilityAndSigninAgainCheck test...");
		LoginLogoutMethods.loginReliabilityAndSigninAgain("validLogin","#LoginReliabilityAndSigninAgainCheck");
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
