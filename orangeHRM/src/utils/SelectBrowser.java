/*
 * Date: September 1st 2014
 * Author: Yagnesh Shah   
 * Twitter handle: @YagneshHShah
 * Organization: Moolya Software Testing Pvt Ltd
 * License Type: MIT
 */

package utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SelectBrowser {

	public SelectBrowser()
	{
		super();
	}

	public static WebDriver getBrowser() throws IOException {
		WebDriver driver = null;

		FileReader reader = new FileReader("../orangeHRM/config.properties"); //Reading configuration file
		Properties prop = new Properties();
		prop.load(reader); 
		String browser = prop.getProperty("driverName"); // Assigning String value form configuraion file
		//String ver = prop.getProperty("version");        // Assigning String value form configuraion file


		if(browser.equalsIgnoreCase("IE"))
		{
			//String path=System.getProperty("user.dir") +File.separator + "Browsers"+ File.separator +"IEDriverServer.exe";
			System.setProperty("webdriver.ie.driver", "../orangeHRM/webDrivers/IEDriverServer.exe"); // setting path of the IEDriver
			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			driver = new InternetExplorerDriver(ieCapabilities);
		}
		else if(browser.equalsIgnoreCase("firefox")){
			driver = new FirefoxDriver();
		}
		else if(browser.equalsIgnoreCase("chrome")){
			//String path=System.getProperty("user.dir") +File.separator + "Browsers"+ File.separator +"chromedriver.exe";
			//System.out.println(path);
			System.setProperty("webdriver.chrome.driver", "../orangeHRM/webDrivers/chromedriver.exe"); // setting path of the ChromeDriver
			driver = new ChromeDriver();
		}
		return driver;
	}
}
