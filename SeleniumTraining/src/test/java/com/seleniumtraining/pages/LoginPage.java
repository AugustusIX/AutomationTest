package com.seleniumtraining.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.FindBy;

public class LoginPage {
	WebDriver driver;
	
	public LoginPage (WebDriver lDriver) {
		this.driver = lDriver;
		
		PageFactory.initElements(driver, this);
	}
	@FindBy (id="username") WebElement uname;
	@FindBy (id="password") WebElement pword;
	@FindBy (name="action") WebElement loginButton;
	
	public void LoginPortal (String username, String password) {
		uname.sendKeys(username);
		pword.sendKeys(password);
		loginButton.click();
	}
}
