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
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.*;
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


		if(browser.equalsIgnoreCase("ieWinx32"))
		{
			//String path=System.getProperty("user.dir") +File.separator + "Browsers"+ File.separator +"IEDriverServer.exe";
			System.setProperty("webdriver.ie.driver", "../orangeHRM/webdrivers/IEDriverServer_x32_v2.43.0.exe"); // setting path of the IEDriver
			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			driver = new InternetExplorerDriver(ieCapabilities);
//			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//			Alert alert = driver.switchTo().alert();
//			String alertText = alert.getText().trim(); 
//          alert.accept(); 
//			alert.dismiss()
		}
		else if(browser.equalsIgnoreCase("ieWinx64"))
		{
			//String path=System.getProperty("user.dir") +File.separator + "Browsers"+ File.separator +"IEDriverServer.exe";
			System.setProperty("webdriver.ie.driver", "../orangeHRM/webdrivers/IEDriverServer_x64_v2.43.0.exe"); // setting path of the IEDriver
			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			driver = new InternetExplorerDriver(ieCapabilities);
		}
		else if(browser.equalsIgnoreCase("firefox")){
			driver = new FirefoxDriver();
		}
		else if(browser.equalsIgnoreCase("safari")){
			driver = new SafariDriver();
		}
		else if(browser.equalsIgnoreCase("chromeWinx32")){
			//String path=System.getProperty("user.dir") +File.separator + "Browsers"+ File.separator +"chromedriver.exe";
			//System.out.println(path);
			System.setProperty("webdriver.chrome.driver", "../orangeHRM/webdrivers/chromedriver_x32_v2.10.exe"); // setting path of the ChromeDriver
			driver = new ChromeDriver();
		}
		else if(browser.equalsIgnoreCase("chromeMac32")){
			//String path=System.getProperty("user.dir") +File.separator + "Browsers"+ File.separator +"chromedriver.exe";
			//System.out.println(path);
			System.setProperty("webdriver.chrome.driver", "../orangeHRM/webdrivers/chromedriver_mac32_v2.10"); // setting path of the ChromeDriver
			driver = new ChromeDriver();
		}
		else if(browser.equalsIgnoreCase("chromeLinux64")){
			//String path=System.getProperty("user.dir") +File.separator + "Browsers"+ File.separator +"chromedriver.exe";
			//System.out.println(path);
			System.setProperty("webdriver.chrome.driver", "../orangeHRM/webdrivers/chromedriver_linux64_v2.10"); // setting path of the ChromeDriver
			driver = new ChromeDriver();
		}
		return driver;
	}
}
