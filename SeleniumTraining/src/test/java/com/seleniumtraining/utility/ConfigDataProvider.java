package com.seleniumtraining.utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfigDataProvider {
	Properties pro;
	
	//Loads the config file
	public ConfigDataProvider() {
		File src = new File("./Configuration/config.properties");
		
		try {
			FileInputStream file = new FileInputStream(src);
			
			pro = new Properties();
			
			pro.load(file);
			
		} catch(Exception e) {
			System.out.println("Not able to load config file " + e.getMessage());
		}
	}
	
	// Reads the data from the config file
	public String getBrowser() {
		return pro.getProperty("browser");
	}
	
	// Reads the data from the config file
	public String getURL() {
		return pro.getProperty("testURL");
	}
	
	public String getUsername() {
		return pro.getProperty("username");
	}
	
	public String getPassword() {
		return pro.getProperty("password");
	}
}
