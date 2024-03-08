package com.seleniumtraining.testcases;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.seleniumtraining.pages.*;
import com.seleniumtraining.utility.ConfigDataProvider;
import com.seleniumtraining.utility.ReadExcelFile;

public class LoginTest extends BaseTest {
	
	String fileName = System.getProperty("user.dir")+"\\TestData\\TestInfo.xls";
	@Test(priority = 1, dataProvider = "LoginDataProvider")
	void verifyLogin(String username, String password) {
		LoginPage lPage= new LoginPage(driver);
		
//		String username = config.getUsername();
//		String password = config.getPassword();
		
		lPage.LoginPortal(username, password);
	}
	
//	@Test(priority = 1, dataProvider = "LoginDataProvider")
//	void verifyOktaLogin(String username, String password) {
//		OktaLogin oktaLogin = new OktaLogin(driver);
//		
//		oktaLogin.LoginPortal(username, password);
//	}
	
	@DataProvider(name="LoginDataProvider")
	public String[][] LoginDataProvider() {
		
		int sheetRows = ReadExcelFile.getRowCount(fileName, "LoginData");
		int sheetCols = ReadExcelFile.getColCount(fileName, "LoginData");
		
		String data[][] = new String[sheetRows-1][sheetCols];
		
		for (int i=1; i < sheetRows; i++) {
			for (int j=0; j < sheetCols; j++) {
				data[i-1][j] = ReadExcelFile.getCellValue(fileName, "LoginData", i, j);
			}
		}
//		data[0][0] = ReadExcelFile.getCellValue(fileName, "LoginData", 1, 2); 
		
		return data;
	}
}
