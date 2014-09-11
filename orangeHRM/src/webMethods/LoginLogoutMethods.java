/*
Aim: Methods for Log-in/Log-out tests:
	validlLogin
	loginReliability&SigninAgain
	assertLoginElements
	maskedPasspword   
	emptyLogin
	partialFillLogin
	invalidLogin
	capsOnLogin (Suggestion: No message if caps lock is on)
	logoutSessionExpire
	
*/
	

package webMethods;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import jxl.Cell;
import jxl.LabelCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import utils.SelectBrowser;
import utils.WebCommonMethods;

public class LoginLogoutMethods  extends SelectBrowser {

	public static WebDriver driver;
	public LoginLogoutMethods(WebDriver driver){
        this.driver=driver;
    }
	
	static Workbook wrk1;
	static Sheet sheet1;
	static Cell colRow;
		
	@FindBy(id = "txtUsername")
		public static WebElement usernameField;
	@FindBy(id = "txtPassword")
		public static WebElement passwordField;
	
	@FindBy(id = "btnLogin")
		public static WebElement signinButton;
	@FindBy(id = "welcome")
		public static WebElement signinWelcomeMessage;
	@FindBy(xpath = ".//*[@id='divLoginButton']/span") //good relative xpath example
		public static WebElement signinErrorMessage;
 
	@FindBy(id="welcome")
		public static WebElement profileDropdown;
	@FindBy(xpath=".//*[@id='welcome-menu']/ul/li[2]/a")
		public static WebElement logoutButton;
 	@FindBy(id="logInPanelHeading")
 		public static WebElement loginPanelHeadingLabel;
 	
 	//Reset password pageobjects
	@FindBy(xpath=".//*[@id='welcome-menu']/ul/li[1]/a")
		public static WebElement resetPasswordOption;
	@FindBy(id="btnSave")
		public static WebElement resetPasswordEditModeButton;
	
	@FindBy(xpath=".//*[@id='frmChangePassword']/fieldset/ol/li[2]/label")  //bad relative xpath example
		public static WebElement oldPasswordLabel;
	@FindBy(id="changeUserPassword_currentPassword")
		public static WebElement oldPasswordField;
	
	@FindBy(xpath=".//*[@id='frmChangePassword']/fieldset/ol/li[3]/label[1]")
		public static WebElement newPasswordLabel;
	@FindBy(id="changeUserPassword_newPassword")
		public static WebElement newPasswordField;
	
	@FindBy(xpath=".//*[@id='frmChangePassword']/fieldset/ol/li[4]/label")
		public static WebElement confirmPasswordLabel;
	@FindBy(id="changeUserPassword_confirmNewPassword")
		public static WebElement confirmPasswordField;
	
	@FindBy(id="btnCancel")
		public static WebElement resetPasswordCancelButton;
	@FindBy(id="btnSave")
		public static WebElement resetPasswordSaveButton;

 
	//error handling messages
	@FindBy(id="UserHeading")
		public static WebElement resetPasswordSaveResponseAlert;
	
//	public static void callingImplicitSleep()
//    {
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//    }

//	public static void connectExcel(String sheetName) throws BiffException, IOException
//	{	
//		wrk1 = Workbook.getWorkbook(new File("../orangeHRM/testdata/webTestData.xls")); // Connecting to excel workbook.
//		sheet1 = wrk1.getSheet(sheetName); // Connecting to the sheet
//	}
	

	public static void validLogin(String username, String password, String profileName) throws IOException, InterruptedException, BiffException
	{
		usernameField.clear();
    	passwordField.clear();
    	usernameField.sendKeys(username);
    	passwordField.sendKeys(password);
    	signinButton.click();
    	WebCommonMethods.callingImplicitSleep();
    	
    	Cell[] homePageLabels = WebCommonMethods.readExcelNextRowOfUniqueValue("webTabsWithSubheading", "#homePage");
    	Assert.assertEquals(signinWelcomeMessage.getText(), homePageLabels[1].getContents());
    	System.out.println("Login successfull...");

//    	String actualProfileName;
//		int flag=0;
//    	actualProfileName=profileDropdown.getText();
//    	//System.out.println(actualProfileName + " / " + profileName);
//    	if(actualProfileName.contentEquals(profileName))
//    	{
//    		//System.out.println("Inside if...actualProfileName = profileName...");
//        	System.out.println("Login successfull...");
//        	flag=1;
//    	}
//    	Thread.sleep(WebCommonMethods.sleepTimeMin2);
//    	Assert.assertTrue(flag==1, "Login unsuccessful...");
    }
	
	public static void invalidLogin(String username, String password) throws IOException, InterruptedException, BiffException
	{
		System.out.println(username + " / " + password);
    	usernameField.clear();
    	passwordField.clear();
    			
    	if(username.toLowerCase().contentEquals("empty"))
    	{
        	passwordField.sendKeys(password);    		
    		signinButton.click();
        	WebCommonMethods.callingImplicitSleep();
    		//Thread.sleep(WebCommonMethods.sleepTimeMin2);
        	
    		Cell[] signinAuthenticationFailureMessage = WebCommonMethods.webReadExcel("errorHandlingMessages", "signin.authenticationFailureUsername");
    		Assert.assertEquals(signinErrorMessage.getText(),signinAuthenticationFailureMessage[1].getContents());
    	}
    	else if(password.toLowerCase().contentEquals("empty"))
    	{
        	usernameField.sendKeys(username);
    		signinButton.click();
        	WebCommonMethods.callingImplicitSleep();
    		//Thread.sleep(WebCommonMethods.sleepTimeMin2);
        	
    		Cell[] signinAuthenticationFailureMessage = WebCommonMethods.webReadExcel("errorHandlingMessages", "signin.authenticationFailurePassword");
    		Assert.assertEquals(signinErrorMessage.getText(),signinAuthenticationFailureMessage[1].getContents());
    	}
    	else
    	{
        	usernameField.sendKeys(username);
        	passwordField.sendKeys(password);
        	signinButton.click();
        	WebCommonMethods.callingImplicitSleep();
    		//Thread.sleep(WebCommonMethods.sleepTimeMin2);
        	
    		Cell[] signinAuthenticationFailureMessage = WebCommonMethods.webReadExcel("errorHandlingMessages", "signin.authenticationFailure");
    		Assert.assertEquals(signinErrorMessage.getText(),signinAuthenticationFailureMessage[1].getContents());
    	}
    }
	
	//logout definition is currently using xPath until id are provided
	public static void logout() throws IOException, BiffException
	{
		profileDropdown.click();
		WebCommonMethods.callingImplicitSleep();
    	logoutButton.click();
    	WebCommonMethods.callingImplicitSleep();
    	
    	Cell[] loginPageLabels = WebCommonMethods.readExcelNextRowOfUniqueValue("webTabsWithSubheading", "#loginPage");
    	Assert.assertEquals(loginPanelHeadingLabel.getText(), loginPageLabels[1].getContents());
    	System.out.println("Successfully logged out...");
    }
	
	public static void login(String userName) throws IOException, BiffException, InterruptedException
	{	
		Cell[] record = WebCommonMethods.webReadExcel("validLogin",userName);  //sending userName, password

  	  	//System.out.println("record: "+ record.length);
        String username = record[0].getContents();
        String password = record[1].getContents();
        String profileName = record[3].getContents();
        validLogin(username,password,profileName);
    	
	}
	
	public static void loginReliabilityAndSigninAgain(String sheetName, String uniqueValue) throws IOException, BiffException, InterruptedException
	{	
		wrk1 = Workbook.getWorkbook(new File("../orangeHRM/testdata/webTestData.xls")); // Connecting to excel workbook.
		sheet1 = wrk1.getSheet(sheetName); // Connecting to the sheet
		LabelCell cell=sheet1.findLabelCell(uniqueValue);
		int row=cell.getRow();
		int i = row + 1;
		int reliabilityRound=0;
		
		while(true)
		{
			Cell uname = sheet1.getCell(0,i);  //Column=0=username,
	        String check = uname.getContents();
	        Boolean b1= check.isEmpty();
	        if(b1){
	        	break;// Script will  end when it found no entry in the Serial No. column of the excel sheet.
	        } 
	        
	        String username=uname.getContents();
	        Cell pwd = sheet1.getCell(1,i); //Column=1=Password
	        String password=pwd.getContents();
	        Cell proName = sheet1.getCell(3,i); //Column=3=ProfileName
	        String profileName=proName.getContents();
	            
	        validLogin(username,password,profileName);
	    	logout();
	    	reliabilityRound++;
	    	i++;
	    	System.out.println("reliability round ended: "+ reliabilityRound);
		}	
	}
	
	public static void passwordMasked()
	{
		//checking masked password
		boolean passwordMasked = passwordField.getAttribute("type").equals("password");
		Assert.assertTrue(passwordMasked, "Login page :: Password field is accepting unmasked value...");	
	}
	
	public static void IncorrectUsernamePasswordCombos(String sheetName, String uniqueValue) throws IOException, BiffException, InterruptedException
	{
		wrk1 = Workbook.getWorkbook(new File("../orangeHRM/testdata/webTestData.xls")); // Connecting to excel workbook.
		sheet1 = wrk1.getSheet(sheetName); // Connecting to the sheet
		
		LabelCell cell=sheet1.findLabelCell(uniqueValue);
		int i = cell.getRow() + 1;
		
		//System.out.println("row i: " + i );
		while(true) //infinite loop until a empty cell is found
		{
			Cell uname = sheet1.getCell(0,i);  //Column=0=username,
	        String check = uname.getContents();
	        Boolean b1= check.isEmpty();
	        if(b1)
	        {
	        	break;// Script will  end when it found no entry in the Serial No. column of the excel sheet.
	        }
	        
	        String username= uname.getContents();
	        Cell pwd = sheet1.getCell(1,i);  //Column=1=password,
	        String password = pwd.getContents();
	        
	        //System.out.println(username + " / " + password);
	        invalidLogin(username,password);
	        i++;
		}
	}
	
	public static void resetPasswordToNewValueAndLogin(String uniqueValueUserName) throws BiffException, IOException, InterruptedException
	{
		Cell[] userNameRecord = WebCommonMethods.webReadExcel("validLogin", uniqueValueUserName);
		
		profileDropdown.click();
		WebCommonMethods.callingImplicitSleep();
		resetPasswordOption.click();
		WebCommonMethods.callingImplicitSleep();
    	resetPasswordEditModeButton.click();
    	WebCommonMethods.callingImplicitSleep();
    	
		//reset password to new value - userNameRecord[2].getContents()
    	oldPasswordField.sendKeys(userNameRecord[1].getContents());
    	newPasswordField.sendKeys(userNameRecord[2].getContents());
    	confirmPasswordField.sendKeys(userNameRecord[2].getContents());
    	resetPasswordSaveButton.click();
		//Thread.sleep(7000);
		WebCommonMethods.callingImplicitSleep();

//		//error message is like toast error message - doesn't stay visible for long...
//		Cell[] resetPasswordSuccessMessage = WebCommonMethods.webReadExcel("errorHandlingMessages", "resetPassword.successful");
//    	Assert.assertEquals(resetPasswordSaveResponseAlert.getText(), resetPasswordSuccessMessage[1].getContents());
    	
    	//now logout & login with new password value
    	LoginLogoutMethods.logout();
		//Thread.sleep(7000);
		WebCommonMethods.callingImplicitSleep();
		validLogin(uniqueValueUserName,userNameRecord[2].getContents(),userNameRecord[3].getContents());
	}
	
	public static void resetPasswordToOldValueAndLogin(String uniqueValueUserName) throws BiffException, IOException, InterruptedException
	{
		Cell[] userNameRecord = WebCommonMethods.webReadExcel("validLogin", uniqueValueUserName);
		
		profileDropdown.click();
		WebCommonMethods.callingImplicitSleep();
		resetPasswordOption.click();
    	WebCommonMethods.callingImplicitSleep();
    	resetPasswordEditModeButton.click();
    	WebCommonMethods.callingImplicitSleep();
    	
		//reset password to old value - userNameRecord[1].getContents()
    	oldPasswordField.sendKeys(userNameRecord[2].getContents());
    	newPasswordField.sendKeys(userNameRecord[1].getContents());
    	confirmPasswordField.sendKeys(userNameRecord[1].getContents());
    	resetPasswordSaveButton.click();
		//Thread.sleep(7000);
		WebCommonMethods.callingImplicitSleep();

//		//error message is like toast error message - doesn't stay visible for long...
//		Cell[] resetPasswordSuccessMessage = WebCommonMethods.webReadExcel("errorHandlingMessages", "resetPassword.successful");
//    	Assert.assertEquals(resetPasswordSaveResponseAlert.getText(), resetPasswordSuccessMessage[1].getContents());
    	
    	//now logout & login with old password value
    	LoginLogoutMethods.logout();
		WebCommonMethods.callingImplicitSleep();
		validLogin(uniqueValueUserName,userNameRecord[1].getContents(),userNameRecord[3].getContents());
	}
	
	public static void resetPasswordAndLoginWithOldPassword(String uniqueValueUserName) throws BiffException, IOException, InterruptedException
	{
		Cell[] userNameRecord = WebCommonMethods.webReadExcel("validLogin", uniqueValueUserName);
		
		profileDropdown.click();
		WebCommonMethods.callingImplicitSleep();
		resetPasswordOption.click();
    	WebCommonMethods.callingImplicitSleep();
    	resetPasswordEditModeButton.click();
    	WebCommonMethods.callingImplicitSleep();
    	
		//reset password to new value - userNameRecord[2].getContents()
    	oldPasswordField.sendKeys(userNameRecord[1].getContents());
    	newPasswordField.sendKeys(userNameRecord[2].getContents());
    	confirmPasswordField.sendKeys(userNameRecord[2].getContents());
    	resetPasswordSaveButton.click();
		//Thread.sleep(7000);
		WebCommonMethods.callingImplicitSleep();

//		//error message is like toast error message - doesn't stay visible for long...
//		Cell[] resetPasswordSuccessMessage = WebCommonMethods.webReadExcel("errorHandlingMessages", "resetPassword.successful");
//    	Assert.assertEquals(resetPasswordSaveResponseAlert.getText(), resetPasswordSuccessMessage[1].getContents());
    	
    	//now logout & login with old password value for error validation - userNameRecord[1].getContents()
    	LoginLogoutMethods.logout();
		WebCommonMethods.callingImplicitSleep();
		invalidLogin(uniqueValueUserName,userNameRecord[1].getContents());
		Cell[] signinAuthenticationFailureMessage = WebCommonMethods.webReadExcel("errorHandlingMessages", "signin.authenticationFailure");
		Assert.assertEquals(signinErrorMessage.getText(),signinAuthenticationFailureMessage[1].getContents());
	}
	
	public static void resetPasswordErrorHandlingMessageValidation(String uniqueValueUserName) throws BiffException, IOException, InterruptedException
	{
		Cell[] userNameRecord = WebCommonMethods.webReadExcel("validLogin", uniqueValueUserName);
		
		profileDropdown.click();
		WebCommonMethods.callingImplicitSleep();
		resetPasswordOption.click();
		WebCommonMethods.callingImplicitSleep();
		
		//resetPassword.newAndConfirmPasswordMismatch
		oldPasswordField.sendKeys(userNameRecord[1].getContents());
    	newPasswordField.sendKeys(userNameRecord[2].getContents());
    	confirmPasswordField.sendKeys(userNameRecord[1].getContents());
    	resetPasswordSaveButton.click();
		WebCommonMethods.callingImplicitSleep();
		Thread.sleep(5000);
		Cell[] NewAndConfirmPasswordMismatchMessage = WebCommonMethods.webReadExcel("errorHandlingMessages", "resetPassword.newAndConfirmPasswordMismatch");
		Assert.assertEquals(resetPasswordSaveResponseAlert.getText(),NewAndConfirmPasswordMismatchMessage[1].getContents());
    	
		//clear all fields
		oldPasswordField.clear();
		newPasswordField.clear();
		confirmPasswordField.clear();
		
		//resetPassword.invalidOldPassword
		oldPasswordField.sendKeys("qwerty");
		newPasswordField.sendKeys(userNameRecord[1].getContents());
		confirmPasswordField.sendKeys(userNameRecord[1].getContents());
		resetPasswordSaveButton.click();
		WebCommonMethods.callingImplicitSleep();
		Thread.sleep(5000);
		Cell[] invalidOldPasswordMessage = WebCommonMethods.webReadExcel("errorHandlingMessages", "resetPassword.invalidOldPassword");
		Assert.assertEquals(resetPasswordSaveResponseAlert.getText(),invalidOldPasswordMessage[1].getContents());
	}
	

}
