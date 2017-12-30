package framework;

import java.io.File;
import java.util.Properties;

import org.testng.Reporter;

import base.BaseTestCase;
import framework.DefaultLocators;

/**
 * DefaultLocators class enables to read the locators used to identity web elements.
 */

public class DefaultLocators {

	private Properties locator;
	private static final DefaultLocators locatorsInstance = new DefaultLocators();
	

	
	/**
	 * Constructor that reads locatorsAll.xml file and initializes locator variable of Properties class.
	 */
	private DefaultLocators(){
		Reporter.log("Locators initialized", 0, true);
		
		String pathSeperator = File.separator;
		String projectPath = System.getProperty("user.dir") + pathSeperator; 
		String filePath = String.format("%sdata%s", projectPath , pathSeperator)+"locatorsAll.xml";
		
		locator = BaseTestCase.propertiesReadAsXml(filePath);
		Reporter.log("Locators set", 0, true);
	}
	
	/**
	 * Method returns the instance of DefaultLocators used in BaseTestCase class.
	 * @return returns the instance of DefaultLocators used in BaseTestCase class
	 */
	public static DefaultLocators getInstance(){
		return locatorsInstance;
	}
	
	/**
	 * Searches for the property with the specified name in this property list. If the name is not found in this property list, the default property list, 
	 * and its defaults, recursively, are then checked. The method returns null if the property is not found.  
	 * @param Name the property name as input parameter
	 * @return the value in this property list with the specified name value.
	 */
	public String get(String Name){
		return locator.getProperty(Name);
	}
	
}
