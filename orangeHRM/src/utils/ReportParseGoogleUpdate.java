/*
 * Date: September 1st 2014
 * Author: Adil Imroz 
 * Twitter handle: @adilimroz
 * Organization: Moolya Software Testing Pvt Ltd
 * License Type: MIT
 */
package utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.testng.annotations.Test;

import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class ReportParseGoogleUpdate {
	private static SpreadsheetService service;
	private static FeedURLFactory factory;
	private static URL url;
	public static ReportParseGoogleUpdate spreadSheetDemo;
	public static SpreadsheetEntry spreadsheet;
	public static WorksheetEntry worksheetEntry;
	public static SpreadsheetQuery query;
	static int rowFromSearch;
	static int colFromSearch;
	static List<String> searchArr=new ArrayList<String>();
	static List<Object> objects = new ArrayList<Object>();


	@Test
	public static void ReportParsingGoogleDocUpdate() throws IOException, ServiceException {
		System.out.println("Script to parse test report and update google doc with results");

		FileReader reader = new FileReader("../orangeHRM/config.properties"); //Reading configuration file
		Properties prop = new Properties();
		prop.load(reader);
		String googleReportParserExecutionFlag = prop.getProperty("googleReportParserExecutionFlag");
		String parserGoogleEmail = prop.getProperty("parserGoogleEmail");
		String parserGooglePassword = prop.getProperty("parserGooglePassword");
		int parserIndexSpreadsheet = Integer.parseInt(prop.getProperty("parserIndexSpreadsheet"));
		int parserIndexWorksheet = Integer.parseInt(prop.getProperty("parserIndexWorksheet"));
		String parserInputFilePath = prop.getProperty("parserInputFilePath");


		if(googleReportParserExecutionFlag.contentEquals("true"))
		{
			//Auth to access google doc
			spreadSheetDemo = new ReportParseGoogleUpdate(parserGoogleEmail, parserGooglePassword); //gmail account via which doc will be accessed for updating test results

			spreadsheet = spreadSheetDemo.getSpreadSheet(parserIndexSpreadsheet);//give index to select the spreadsheet from the folder

			worksheetEntry = spreadsheet.getWorksheets().get(parserIndexWorksheet); //providing index to access the desired worksheet

			url = worksheetEntry.getCellFeedUrl();
			query = new SpreadsheetQuery(url);

			System.out.println("Title of spreadsheet being updated :: "+spreadsheet.getTitle().getPlainText());
			System.out.println("Title of worksheet being updated :: "+worksheetEntry.getTitle().getPlainText());


			//
			CellFeed feed = service.query(query, CellFeed.class);

			//Parsing the Test report from desired folder
			File input = new File(parserInputFilePath);
			System.out.println("Starting ....");
			Document doc = Jsoup.parse(input,null);
			System.out.println("midway ....");
			Elements testNames = doc.select("html>body>table>tbody>tr>td>a[href~=#t]");//gets the name of the test names from the report
			Elements testNameSiblings = doc.select("html>body>table>tbody>tr>td[class~=num]");//gets the siblings of all the test names...they are the test  results numbers

			System.out.println("testNames size ::"+testNames.size());
			System.out.println("testNameSiblings size :: "+testNameSiblings.size());

			/////
			for (CellEntry entry : feed.getEntries()) {
				searchArr.add(entry.getCell().getInputValue());
				objects.add(entry.getCell());
			}
			System.out.println(searchArr);
			System.out.println(objects);

			int j =0;

			for (int i = 0; i < testNames.size(); i++) {	

				System.out.println("test>"+testNames.get(i).text());	
				int passed = Integer.parseInt(testNameSiblings.get(j).text());	
				j=j+1;
				int skipped = Integer.parseInt(testNameSiblings.get(j).text());
				j=j+1;
				int failed = Integer.parseInt(testNameSiblings.get(j).text());
				j=j+1;
				String testTime = testNameSiblings.get(j).text();
				j=j+1;
				System.out.println("******************************");

				if (passed!=0) {
					search(testNames.get(i).text());
					CellContentUpdate(worksheetEntry, rowFromSearch, colFromSearch+2, "PASSED");
				}

				else if (skipped!=0) {
					search(testNames.get(i).text());
					CellContentUpdate(worksheetEntry, rowFromSearch, colFromSearch+2, "SKIPPED");	
				}

				else if (failed!=0) {
					search(testNames.get(i).text());
					CellContentUpdate(worksheetEntry, rowFromSearch, colFromSearch+2, "FAILED");	
				}
			}
		}
		else
		{
			System.out.println();
			System.out.println("googleReportParserExecution feature is turned off via googleReportParserExecutionFlag in config.properties file...");
		}
	}


	/*
	 * Method Definitions
	 */
	//Method for authentication to access google doc
	public ReportParseGoogleUpdate(String username, String password) throws AuthenticationException {
		service = new SpreadsheetService("SpreadSheet-Demo");
		factory = FeedURLFactory.getDefault();        
		System.out.println("Authenticating...");
		service.setUserCredentials(username, password);
		System.out.println("Successfully authenticated");

	}
	//Method to get all the spreadsheets associated to the given account
	public void GetAllSpreadsheets() throws IOException, ServiceException{
		SpreadsheetFeed feed = service.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
		List<SpreadsheetEntry> spreadsheets = feed.getEntries();
		System.out.println("Total number of SpreadSheets found : " + spreadsheets.size());
		for (int i = 0; i < spreadsheets.size(); ++i) {
			System.out.println("("+i+") : "+spreadsheets.get(i).getTitle().getPlainText());
		}

	}	
	// Returns spreadsheet
	public SpreadsheetEntry getSpreadSheet(int spreadsheetIndex) throws IOException, ServiceException {
		SpreadsheetFeed feed = service.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
		SpreadsheetEntry spreadsheet = feed.getEntries().get(spreadsheetIndex);
		return spreadsheet;
	}

	//Method to get Spread sheet details
	public void GetSpreadsheetDetails(SpreadsheetEntry spreadsheet) throws IOException, ServiceException{ 
		System.out.println("SpreadSheet Title : "+spreadsheet.getTitle().getPlainText());
		List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();
		for (int i = 0; i < worksheets.size(); ++i) {
			WorksheetEntry worksheetEntry = worksheets.get(i);
			System.out.println("("+i+") Worksheet Title : "+worksheetEntry.getTitle().getPlainText()+", num of Rows : "+worksheetEntry.getRowCount()+", num of Columns : "+worksheetEntry.getColCount());
		}

	}


	public static void CellContentUpdate(WorksheetEntry worksheetEntry,int row, int col, String formulaOrValue) throws IOException, ServiceException{
		URL cellFeedUrl = worksheetEntry.getCellFeedUrl();
		CellEntry newEntry = new CellEntry(row, col, formulaOrValue);
		service.insert(cellFeedUrl, newEntry);
		System.out.println("Cell Updated!");

	}

	//		public static void searchTestNameGDoc(String testNameToSearch) throws IOException, ServiceException{
	//			
	//			CellFeed feed = service.query(query, CellFeed.class);
	//
	//			for (CellEntry entry : feed.getEntries()) {
	//				searchArr.add(entry.getCell().getInputValue());
	//				objects.add(entry.getCell());
	//			}
	//			
	//			for (int i = 0; i < objects.size(); i++) {
	//				if (searchArr.get(i)=="BalanaceEnquiry_MandatoryField" ){
	//					rowFromSearch = feed.getEntries().get(i).getCell().getRow();
	//					colFromSearch = feed.getEntries().get(i).getCell().getCol();
	//					break;
	//				}
	//				
	//			}
	//			
	//		}

	//		public static void printCell(CellEntry cell) {
	//		  	System.out.println(cell.getCell().getValue());
	//  }

	public static void printCell(CellEntry cell) {
		String shortId = cell.getId().substring(cell.getId().lastIndexOf('/') + 1);
		System.out.println(" -- Cell(" + shortId + "/" + cell.getTitle().getPlainText()+ ") formula(" + cell.getCell().getInputValue() + ") numeric("+ cell.getCell().getNumericValue() + ") value("+ cell.getCell().getValue() + ")");
		rowFromSearch = cell.getCell().getRow();
		colFromSearch = cell.getCell().getCol();
		System.out.println("row :: "+rowFromSearch);
		System.out.println("col :: "+colFromSearch);
	}

	public static void search(String fullTextSearchString) throws IOException,ServiceException {
		CellQuery query = new CellQuery(url);
		query.setFullTextQuery(fullTextSearchString);
		CellFeed feed = service.query(query, CellFeed.class);

		System.out.println("Results for [" + fullTextSearchString + "]");

		for (CellEntry entry : feed.getEntries()) {
			printCell(entry);
		}
	}

}
