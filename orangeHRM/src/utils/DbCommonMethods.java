/*
 * Date: September 1st 2014
 * Author: Adil Imroz
 * Twitter handle: @YagneshHShah
 * Organization: Moolya Software Testing Pvt Ltd
 * License Type: MIT
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

public class DbCommonMethods 
{
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DbUrlReportDashboard = "jdbc:mysql://localhost:3306/reportdashboard"; //master schema db
	static final String DbUrl2 = "jdbc:mysql://localhost:3306/db2"; //

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "root";

	@Test
	public static void deleteOrganizations(String[] orgNames) //from Master Schema
	{
		Connection conn = null;
		Statement stmt = null;
		int flag=1; //0=indicates no failure, 1=indicates success of all query execution

		try{
			//STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			//STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DbUrlReportDashboard, USER, PASS);
			System.out.println("Connected database successfully...");

			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();

			for (int i=0; i<orgNames.length; i++) 
			{	
				System.out.println("deleteOrganizations Round: " + (i+1));

				String sql = "SET @orgCode = '" + orgNames[i] + "'";
				stmt.executeUpdate(sql);
				String sql1 = "DELETE FROM con WHERE organization IN (SELECT id FROM organization WHERE CODE = @orgCode);";
				stmt.executeUpdate(sql1);

				String sql2 = "DELETE FROM organization_attribute WHERE orgnizationId IN (SELECT id FROM organization WHERE CODE = @orgCode);";
				stmt.executeUpdate(sql2);

				String sql3 = "DELETE FROM mapping_organization_address WHERE organization IN (SELECT id FROM organization WHERE CODE = @orgCode);";
				stmt.executeUpdate(sql3);

				String sql4 = "DELETE FROM organization_devices WHERE org_code = @code;";
				stmt.executeUpdate(sql4);

				String sql5 = "DELETE FROM organization WHERE CODE=@orgCode;";
				stmt.executeUpdate(sql5);

				System.out.println("Master Schema :: Deleted Organization having unique orgCode - " + orgNames[i]);
			}
		} //end try
		catch(SQLException se){
			//Handle errors for JDBC
			flag=0;
			se.printStackTrace();
		}

		catch(Exception e){
			//Handle errors for Class.forName
			flag=0;
			e.printStackTrace();
		}

		finally
		{
			//finally block used to close resources
			try
			{
				if(stmt!=null)
					conn.close();
			}
			catch(SQLException se){
			}// do nothing
			try
			{
				if(conn!=null)
					conn.close();
			}
			catch(SQLException se)
			{
				se.printStackTrace();
			}

			Assert.assertTrue(flag==1, "Error!! deleteOrganizations Query execution failed...");
		}//end finally
		System.out.println("Goodbye!");
	}


	@BeforeMethod
	public void beforeMethod() {
	}

	@AfterMethod
	public void afterMethod() {
	}


}
