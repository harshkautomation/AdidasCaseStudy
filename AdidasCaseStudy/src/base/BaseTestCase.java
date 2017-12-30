package base;


import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import framework.Navigator;
import framework.DefaultLocators;
import framework.FrameworkReporter;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;

import static framework.FrameworkReporter.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;

/**
 * BaseTestCase class contains common aspects of all tests related to launching different browsers (Ex: Firefox, Chrome etc) and closing the browsers,
 * generic methods to read data from xml and properties files
 */
public class BaseTestCase {
	
	protected WebDriver driver;
	protected DefaultLocators locator = DefaultLocators.getInstance();
	
	String browserType;
	String url;
	
	
	/**
	 * This method is executed before every test and accepts input parameters for Browser Type (Ex: Firefox, Chrome etc) and Adidas UI URL.
	 * @param browserType
	 * @param url
	 */
	@Parameters({"browser","url"})
	@BeforeTest
	public final void beforeTest(@Optional("firefox")String browserType, @Optional("https://www.adidas.fi/")String url){
		reportInfo("Browser type on which present tests are executed-->"+browserType);
		reportInfo("URL->"+url);
	
		this.browserType = browserType;
		this.url = url;
	}
	
	/**
	 * This method is executed before every test method and launches the browser with the URL provided as input. 
	 */
	@BeforeMethod
	public void beforeMethod(){
		
		driver = Navigator.launchBrowser(browserType, url);
		FrameworkReporter.init(driver);
	}

	/**
	 * Method to quit the browser after performing a test execution.
	 * It closes the browser window. 
	 */
	@AfterMethod()
	public void afterMethod() {
		Navigator.quitBrowser(driver);
	}
	
	/**
	 * This method is executed after every test method and resets the browser and url variables. 
	 */
	@AfterTest
	public void afterTest(){
		
		if(browserType!=null && url!=null){
			browserType = null;
			url=null;
		}
	}
	
	/**
	 * Method returns an instance of WebDriver
	 * @return instance of WebDriver
	 */
	public WebDriver getDriver(){
		return driver;
	}
	
	/**
	 * Performs reading of the properties file as xml.
	 * @param path of the xml file
	 * @return an instance of Properties class
	 */
	public static Properties propertiesReadAsXml(String path) {
		Properties props = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(path);
			props.loadFromXML(fis);
		} catch (FileNotFoundException e1) {

			e1.printStackTrace();
		} catch (InvalidPropertiesFormatException e1) {

			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return props;

	}
	
	/**
	 * This method constructs the expected messages displayed in Adidas UI after an operation is performed (Example: Add to Bag confirmation message, Bag is empty message etc).
	 * The expected message constructed by this method is then compared with the actual message.
	 * It reads all the message keys from messages.properties file placed under data folder.
	 * @param messageKey message key that contains description of the expected message format.
	 * @param args list of arguments passed as parameter that dynamically helps in constructing the expected messages by replacing the values in the message key in real time.
	 * @return a text format of the expected message.
	 */
    public String getMessage(String messageKey, Object...args){
    	
    	String pathSeperator = File.separator;
		String projectPath = System.getProperty("user.dir") + pathSeperator; 
		String filePath = String.format("%sdata%s", projectPath , pathSeperator)+"messages.properties";
		
    	
    	Properties msgProp = propertiesRead(filePath);
    	String message = msgProp.getProperty(messageKey).trim();
    	reportLog("Reading message " + messageKey + " = " + message);
    	return args.length > 0 ? String.format(message, args) : message;
    	
	}
	
    /**
	 * Performs reading of the properties file.
	 * @param path of the properties file
	 * @return an instance of Properties class
	 */
	public static Properties propertiesRead(String path) {
		Properties props = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(path);
			props.load(fis);
		} catch (FileNotFoundException e1) {

			e1.printStackTrace();
		} catch (InvalidPropertiesFormatException e1) {

			e1.printStackTrace();
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		return props;
	}
	
}
