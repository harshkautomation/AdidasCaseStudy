package framework;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

/**
 * Navigator class contains methods to launch browsers of specific type (Example: Firefox, Chrome) and method to close the browsers.
 */
public class Navigator {

	
	/**
	 * This method launches browsers of specific type (Example: Firefox, Chrome) with URL provided as input.
	 * @param browserType Type of the browser (Example: Firefox, Chrome)
	 * @param url URL to be provided in the browser window.
	 * @return WebDriver instance.
	 */
	public static WebDriver launchBrowser(String browserType, String url){
		
		WebDriver driver = null;
		
		String pathSeperator = File.separator;
		String projectPath = System.getProperty("user.dir") + pathSeperator; 
		String filePath = String.format("%sutilityDrivers%s", projectPath , pathSeperator);
		
		
	 if(browserType.equals("googlechrome")){
		 System.setProperty("webdriver.chrome.driver", filePath+"chromedriver.exe");
		 driver = new ChromeDriver();
		 
	 }
	 
	 else if(browserType.equals("ie")){
		 System.setProperty("webdriver.ie.driver", filePath+"IEDriverServer.exe");
		 driver = new InternetExplorerDriver();
	 }
	 
	 else if(browserType.equals("firefox")){
		 
		 FirefoxProfile profile = new FirefoxProfile();
			
		 profile.setPreference("browser.startup.page", 0); // Empty start page
		 profile.setPreference("browser.startup.homepage_override.mstone", "ignore");
		 profile.setPreference("startup.homepage_welcome_url.additional",  "about:blank");
		
		 profile.setPreference("xpinstall.signatures.required", false);
		 profile.setPreference("toolkit.telemetry.reportingpolicy.firstRun", false);			
		 profile.setPreference("network.proxy.type", 4);
		 
		 driver = new FirefoxDriver();
	 }
	 
	  driver.get(url);
	  driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
	  driver.manage().window().maximize();
      return driver;
	}
	
	
	/**
     * This method closes the browser window. 
     * @param driver WebDriver instance as input parameter.
     */
    public static void quitBrowser(WebDriver driver){
		if(driver != null){
			driver.close();
			driver.quit();
		}
	}
	
}
