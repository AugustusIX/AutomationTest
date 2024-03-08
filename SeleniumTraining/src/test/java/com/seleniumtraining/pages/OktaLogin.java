package com.seleniumtraining.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OktaLogin {
  WebDriver driver;
  
  public OktaLogin (WebDriver lDriver) {
	  this.driver = lDriver;
	  
	  PageFactory.initElements(driver, this);
  }
  
  @FindBy (xpath="/html/body/main/section/div/div/div/div[2]/form[1]/button") WebElement googleLogin;
  @FindBy (id="okta-signin-username") WebElement uname;
  @FindBy (id="okta-signin-password") WebElement pword;
  @FindBy (id="okta-signin-submit") WebElement submitButton;
	
	public void LoginPortal (String username, String password) {
		googleLogin.click();
		uname.sendKeys(username);
		pword.sendKeys(password);
		submitButton.click();
	}
}
