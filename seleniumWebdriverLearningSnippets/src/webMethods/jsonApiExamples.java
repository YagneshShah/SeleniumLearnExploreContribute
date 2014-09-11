package webMethods;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import utils.HttpClientUtil;
import utils.SelectBrowser;
import utils.WebCommonMethods;
import utils.HttpClientUtil.StringResponse;

public class jsonApiExamples extends SelectBrowser {	
	public static WebDriver driver;

	public jsonApiExamples(WebDriver driver){
		this.driver=driver;
	}
	
	@FindBy(xpath = ".//*[@id='orgNameLabel']")
	public static WebElement orgName;
	
	
	

	//Json API Script Part 1 starts:
	public static String getRequestToExtractJsonContentforSpecificHeaderKey(String uri, String headerKey)
	{
		//Connect to Json api & retrieve all data
		StringResponse content;
		content=HttpClientUtil.get2(uri);

		//Start extracting the specific key:value you wish from entire Json data
		JSONParser parser=new JSONParser();
		try
		{
			Object obj = parser.parse(content.getContent());
			//extract first layer from Json :: complete Json data
			JSONObject completeJson = (JSONObject)obj;
			System.out.println("\n completeJson data: \n" + completeJson);

			//extract 2nd layer from Json :: "Headers" section
			JSONObject headers=(JSONObject)completeJson.get("headers");
			System.out.println("\n Headers section data: \n" + headers);

			//extract 2nd layer > specific 'value' for the key 'specified'. Example: Host:"httpbin.org"
			System.out.println("Headers section : specific value for key 'Host': \n" + headers.get(headerKey) + "\nSuccessfully retrieved the value for specified key from header section...\n\n");
			return (String)headers.get(headerKey);
		}
		catch(ParseException pe)
		{
			System.out.println("position: " + pe.getPosition());
			System.out.println(pe);
		}
		return null;
	}//end method
	
	//Json API Script Part 2 starts: Now from website front-end i.e, GUI part, try to extract text for 'Host' field using selenium webdriver & store the same in actualHost variable 
	public static String retrieveActualValueFromGuiForSpecificWebelement(WebElement we)
	{
		String actualHeaderValue = we.getText();
		return actualHeaderValue;
	}
	
	//Script Part 3 starts: compare expected & actual data
	public static void compareExpectedApiContentWithActualGuiContent(String expectedHeaderValue, String actualHeaderValue)
	{
		int flag;
		if (actualHeaderValue.contentEquals(expectedHeaderValue))   //Update the code above in part2 and this error will be resolved...:)
		{
			flag = 1;//success
			System.out.println("GUI is reflecting correct data for 'Host' field value...");
		}
		else
		{
			flag=0; //failed
		}
		Assert.assertTrue(flag==1,"GUI failed to reflect the correct data from Json api...");
	}
}


