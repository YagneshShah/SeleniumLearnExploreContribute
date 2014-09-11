// Jars needed for this example: json_simple-1.1.jar, testng-6.8.jar

/*
 * Test aim: Check if the actual data on website GUI is same as expected data from Json Api called by website GUI.
 * 
 * API Type: Get request without username or password
 * 
 * Example of a specific key & value from json api is: 
 * Host: "httpbin.org",
 * 
 * script part 1: Using JsonObject....Extract data from Json api call for 'Host' key value. Store the same in ExpectedHost String variable
 * script part 2: Using Selenium Webdriver....getText() for 'Host' field from GUI. Store the same in ActualHost String variable
 * script part 3: compare ActualHost string value to ExpectedHost string value. If matches, then the GUI is reflecting the correct data from Json api
 * 
 */

package webMainScripts.jsonApiExamples;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import utils.HttpClientUtil;
import utils.HttpClientUtil.StringResponse;
import utils.WebCommonMethods;
import utils.SelectBrowser;
import webMethods.jsonApiExamples;

public class ExtractJsonApiEx1_GetRequest_HttpBinOrgSite extends SelectBrowser {

	jsonApiExamples jsonApiExample;

	// Initializing or opening browser and pagefactory
    @BeforeMethod
    public void openTheBrowser() throws IOException 
    {
    	WebDriver d = getBrowser();
	  	//WebCommonMethods.openURL();

    	jsonApiExample = PageFactory.initElements(d, jsonApiExamples.class);// initiating the driver and the .class file (the pageObject script)	    
    } 
    
	@Test(groups="jsonApiExamples")
	public static void ExtractJsonApiEx1_GetRequest_HttpBinOrgSiteTest()
	{
		String uri = "http://httpbin.org/get";
		String headerKey1 = "Host";
		String expectedHeaderValue1;
		String actualHeaderValue1;
		
		expectedHeaderValue1 = webMethods.jsonApiExamples.getRequestToExtractJsonContentforSpecificHeaderKey(uri,headerKey1);	//Script Part 1
		System.out.println("expectedHeaderValue1: " + expectedHeaderValue1);

		actualHeaderValue1 = webMethods.jsonApiExamples.retrieveActualValueFromGuiForSpecificWebelement(webMethods.jsonApiExamples.orgName);	//Script Part 2
		System.out.println("actualHeaderValue1: " + actualHeaderValue1);

		webMethods.jsonApiExamples.compareExpectedApiContentWithActualGuiContent(expectedHeaderValue1, actualHeaderValue1);		
	}
	
    @AfterMethod(alwaysRun=true)
    public void catchExceptions(ITestResult result) throws IOException, InterruptedException 
    {    
    	String methodname = result.getName();
        if(!result.isSuccess()){            
        	WebCommonMethods.screenshot(methodname);
        }
        WebCommonMethods.quit(); // Calling function close to quit browser instance
    }
}