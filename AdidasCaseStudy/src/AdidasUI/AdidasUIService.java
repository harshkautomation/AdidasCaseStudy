package AdidasUI;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SoftAssert;

import static framework.FrameworkReporter.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import base.BaseTestCase;


/**
 * This class contains all the business specific methods that are required by the test scenarios executed in AdidasUITest class.
 * For example, all the business scenarios like performing search operation, selecting sort by options (like price low to high), 
 * navigation to various sub categories like Jerseys, Jackets etc in Men and Women section are written in this class.
 * It extends BaseTestCase class to inherit all the BaseTestCase methods like locator of DefaultLocators class.
 */
public class AdidasUIService extends BaseTestCase{

	private WebDriver webDriver;
	
	static int waitTime = 60;  //Wait time variable for explicit wait conditions. 
	static int implicitWaitInSecs = 20;   //Wait time variable for implicit wait conditions. 
	WebDriverWait wait; //WebDriverWait variable defined for explicit wait conditions.
	
	Actions action;
	
	/**
	 * Constructor to initialize the variables for WebDriver, Actions, and WebDriverWait
	 * @param accepts WebDriver driver
	 */
	 public AdidasUIService(WebDriver driver){
		 webDriver = driver;
		 action = new Actions(webDriver);
		 wait=new WebDriverWait(webDriver,waitTime);
	 }
			
	/**
	 * Method to select bag icon in home page. 		
	 * @return return the current instance.
	 */
	 public AdidasUIService selectViewBagIcon(){
			
		 WebElement viewBagElement = webDriver.findElement(By.xpath(locator.get("homepage.viewbagicon")));
			
			action.moveToElement(viewBagElement).click().perform();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator.get("viewbagsection.acceptedpaymentssection"))));
		
			return this;
     }

	 /**
	  * Verifies if the bag is empty. 
	  * @return the text as 'Your Bag is Empty'
	  */
	 public String verifyEmptyBag(){
		 
		 String emptyBagText;
		 
		 emptyBagText = webDriver.findElement(By.xpath(locator.get("viewbagsection.emptybag"))).getText();
		 implicitWait(implicitWaitInSecs);
		 return emptyBagText;
		 
	 }	 
	 
	 /**
	  * This method performs search operation in search text field in home page by accepting a search value as input parameter. 
	  * @param searchValue as input parameter.
	  * @return return the current instance.
	  */
	 public AdidasUIService performSearch(String searchValue){
		 
		WebElement searchElement = webDriver.findElement(By.xpath(locator.get("homepage.searchtextfield")));
		
		searchElement.sendKeys(searchValue);
		searchElement.sendKeys(Keys.ENTER);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator.get("homepage.searchresultstext"))));
		 
		 return this;
	 }
	 
	 
	 /**
	  * This method returns all the search results as a List collection with Product Titles (Example: Spain Home Authentic Jersey in Men->Jerseys section) 
	  * @return List collection with Product Titles.
	  */
	 public List<String> getSearchResultsData(){
		 
		 List<String> searchResultsData = new ArrayList<String>();
		 
		 List<WebElement> searchElements = webDriver.findElements(By.xpath(locator.get("homepage.producttitlesassearchresults")));
		 
		 for(WebElement elts: searchElements){
			 searchResultsData.add(elts.getText());
		 }
		 
		 return searchResultsData;
	 }
	 
	 
	 /**
	  * This selects the Order Tracker link present in Home Page to navigate to Order Tracker page.
	  * @return return the current instance.
	 * @throws InterruptedException 
	  */
	 public AdidasUIService selectOrderTracker() {
	
		WebElement orderTracker = webDriver.findElement(By.xpath(locator.get("homepage.ordertrackerlink")));
		action.moveToElement(orderTracker).click().perform();
		 
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator.get("homepage.ordertrackervieworderbutton"))));	
		return this; 
	 }
	 
	 /**
	  * This method enters input parameter data in Order Number and Email Address fields respectively.
	  * @param orderNumber input data for Order Number
	  * @param email input data for Email Address
	  * @return return the current instance.
	  */
	 
	 public AdidasUIService enterDataInOrderNumberAndEmailAddress(String orderNumber, String email){
		 
		 webDriver.findElement(By.xpath(locator.get("homepage.ordernumbertextfield"))).clear();
		 webDriver.findElement(By.xpath(locator.get("homepage.ordernumbertextfield"))).sendKeys(orderNumber);
		 
		 webDriver.findElement(By.xpath(locator.get("homepage.emailaddresstextfield"))).clear();
		 webDriver.findElement(By.xpath(locator.get("homepage.emailaddresstextfield"))).sendKeys(email);
		 webDriver.findElement(By.xpath(locator.get("homepage.emailaddresstextfield"))).sendKeys(Keys.TAB);
		 return this;
	 }
	 
	 
	 /**
	  * This method selects View Order button in Order Tracker page. 
	  * @return return the current instance.
	 * @throws InterruptedException 
	  */
	 public AdidasUIService selectViewOrderButtonInOrderTracker(){
		 
		 webDriver.findElement(By.xpath(locator.get("homepage.ordertrackervieworderbutton"))).click();
		 implicitWait(implicitWaitInSecs);
		 return this;
	 }
	 
	 
	 /**
	  * Mechanism to validate the mandatory text fields Order Number and Email Address fields. 
	  * This method stores the mandatory validation messages of Order Number and Email Address when user attempts to click View Order button 
	  * without entering data in these two fields.
	  * It throws a RuntimeException when the validation messages are not displayed. 
	  * @return List collection containing mandatory validation messages of Order Number and Email Address. 
	  */
	 public List<String> validateOrderNumberAndEmailAddressFields(){
		  
		 List<String> validationMessages = new ArrayList<String>();
		
		 WebElement orderNumberElement = webDriver.findElement(By.xpath(locator.get("homepage.ordertrackerordernumbervalidationmsg")));
		 
		 WebElement emailAddressElement = webDriver.findElement(By.xpath(locator.get("homepage.ordertrackeremailvalidationmsg")));
		 
		 if(orderNumberElement.isDisplayed() && emailAddressElement.isDisplayed()){
			 validationMessages.add(orderNumberElement.getText());
			 validationMessages.add(emailAddressElement.getText());
		 }
		 else
			 throw new RuntimeException("Order Number and Email Address madatory field validation messages are not displayed.");
		 
		 return validationMessages;
	 }
	 
	 /**
	  * This method displays invalid combination warning message and incorrect values provided in Order Number and Email address fields. 
	  * In case of a successful validation of Order Number and Email address fields, there is no invalid combination warning displayed. 
	  * This scenario is handled in the try and catch blocks defined in the method.
	  * @return List collection with invalid combination warning message and incorrect values provided in Order Number and Email address fields.
	  */
	 public List<String> validateInvalidOrderNumberEmailAddressMessage(){
		  
		 List<String> validationMessages = new ArrayList<String>();
		
		 try{
		 WebElement invalidCombinationElement = webDriver.findElement(By.xpath(locator.get("homepage.ordertrackerinvalidcombinationheader")));
		 
		 WebElement incorrectElement = webDriver.findElement(By.xpath(locator.get("homepage.ordertrackerinvalidcombinationmessage")));
		 
		 if(invalidCombinationElement.isDisplayed() && incorrectElement.isDisplayed()){
			 validationMessages.add(invalidCombinationElement.getText());
			 validationMessages.add(incorrectElement.getText());
		  }
		}
		 catch(Exception e){
			 validationMessages.add("Successful validation of order number and email address fields.");
		 }
		
		 return validationMessages;
	 }
	 
	 
	 /**
	  * Method to select Sort By field option accepted as input parameter in sub categories. 
	  * For example: Select option 'Price (low/high)' in Sort By field in Jackets section.
	  * @param option to select Sort By field option.
	  * @return return the current instance.
	  */
	 public AdidasUIService selectSortByFieldOption(String option){
		 
		webDriver.findElement(By.xpath(locator.get("mensection.sortbyfield"))).click();
		implicitWait(implicitWaitInSecs);
		 
		List<WebElement> sortOptions = webDriver.findElements(By.xpath(locator.get("mensection.sortbyfieldoptions")));
		
		for(WebElement elt: sortOptions){
			if(elt.getText().equals(option)){
				elt.click();
				implicitWait(implicitWaitInSecs);
				break;
			}
		}
		 
		return this;
	 }
	 
	 /**
	  * This method prepares a collection of all the prices displayed in Item page to be validated for sorting of prices from say low to high. 
	  * @return a List collection of all the prices displayed in Item page.
	  */
	 public List<String> validatePricesSortedFromLowToHigh(){
		 
		 List<WebElement> productItems = webDriver.findElements(By.xpath(locator.get("mensection.sortfeatureproductitemsprice")));
		 
		 List<String> priceList = new ArrayList<String>();

	     for(WebElement elt: productItems){
	    	 
	         priceList.add(elt.getText());	 
	     }
		 
	     reportLog("Original Price list: "+priceList);
	     
		 return priceList;
	 }
	 
	 /**
	  * This method provides mechanism to navigate to different categories like Men, Women etc from Home Page.
	  * @param categorylink input parameter with Category value like Men or Women.
	  * @return return the current instance.
	  */
	 public AdidasUIService navigate(String categorylink) {

		 String category = locator.get("fromhomepage.toselectedtabsection");
		 category = String.format(category, categorylink);

		 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator.get("homepage.adidaslogo"))));
		 
		 WebElement categoryWebElement = webDriver.findElement(By.xpath(category));
		 action.moveToElement(categoryWebElement).perform();
		
		 implicitWait(implicitWaitInSecs);
		return this;
	}
	 
	 /**
	  * This method navigates to sub-categories like Jerseys in Men section.
	  * @param categoryName input parameter containing sub category.
	  * @return return the current instance.
	  */
	 public AdidasUIService selectCategory(String categoryName){
		 
		 WebElement categoryNameElement = webDriver.findElement(By.linkText(categoryName));
		 action.moveToElement(categoryNameElement).click().perform();
		 
		 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'"+categoryName+"')]")));
		 	
		 reportLog("Category: "+categoryName+" is displayed.");
		 return this;
	 }
	 
	 
	 /**
	  * This method selects the first product item displayed in Men->Jerseys section.
	  * @return return the current instance.
	  */
	 public AdidasUIService selectFirstProductItem(){
		 
		 WebElement firstItemElement = webDriver.findElement(By.xpath(locator.get("mensectionjersey.getfirstitemtext")));	
		 
			if(firstItemElement.isDisplayed()){
				firstItemElement.click();
				 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator.get("mensection.addtobagbutton"))));
			}
			else
				throw new RuntimeException("There are no product items displayed in the Men's Jersey section.");
			
		 return this;
	 }
	 
	 
	 /**
	  * Method to select the product item passed as input parameter in Items page.
	  * @param productItemToBeSelected input parameter specifying the product item to be selected.
	  * @return return the current instance.
	  */
	 public AdidasUIService selectProductItem(String productItemToBeSelected){
		 
	     String productItem = locator.get("mensection.selectproductitem");
	     productItem = String.format(productItem, productItemToBeSelected);
	     
		 WebElement productItemElement = webDriver.findElement(By.xpath(productItem));	
		 
			if(productItemElement.isDisplayed()){
				productItemElement.click();
				 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator.get("mensection.addtobagbutton"))));
			}
			else
				throw new RuntimeException("The specified product item: {"+productItemToBeSelected+"} is not displayed in the Men's Jersey section.");
			
		 return this;
	 }
	 
	 
	 /**
	  * Method to return Product Item Description and it's price from Order Summary page after selecting the item from Item Page.
	  * @return List collection containing Product Item Description and it's price from Order Summary page.
	  */
	 public List<String> getProductItemTextAndPriceFromOrderSummary(){
		 
		 List<String> productItemAndPrice = new ArrayList<String>(); 
		 String itemText = webDriver.findElement(By.xpath(locator.get("mensection.ordersummaryitemtext"))).getText();
		 String itemPrice = webDriver.findElement(By.xpath(locator.get("mensection.ordersummaryitemprice"))).getText();
		 productItemAndPrice.add(itemText);
		 productItemAndPrice.add(itemPrice);
		 		 
		 return productItemAndPrice;
	 }
	 
	 /**
	  * This method returns first product item and it's price displayed in Men->Jerseys section as a List collection
	  * @return List collection of first product item title and corresponding price displayed in Men->Jerseys section.
	  */
	 public List<String> getFirstProductItemTextAndPrice(){
		 
		 List<String> firstProductItemAndPrice = new ArrayList<String>(); 
		 
		 String itemText = webDriver.findElement(By.xpath(locator.get("mensectionjersey.getfirstitemtext"))).getText();
		 
		 String firstItemCurrency = webDriver.findElement(By.xpath(locator.get("mensectionjersey.getfirstitemcurrency"))).getText();
		 
		 String firstItemPrice = webDriver.findElement(By.xpath(locator.get("mensectionjersey.getfirstitemsalesprice"))).getText();
		 
		 firstProductItemAndPrice.add(itemText);
		 firstProductItemAndPrice.add(firstItemCurrency+" "+firstItemPrice); //Adding the first item currency and price to the list collection.
		 
		 takeScreenshot();
 		
		 return firstProductItemAndPrice;
		 
	 }
	 
	 /**
	  * This method gets the item price of the product item passed as input parameter in Items page.
	  * @param productItem input product item of whose price needs to be fetched.
	  * @return String value containing currency sign and the price of the product item passed as input parameter.
	  */
	 public String getPrice(String productItem){
		 
		 String getPrice = null;
		 
		 String currencySign = locator.get("mensection.getitemcurrencysign");
		 currencySign = String.format(currencySign, productItem);
		
		 String itemPrice = locator.get("mensection.getitemprice");
		 itemPrice = String.format(itemPrice, productItem);
	
		 String productItemCurrency = webDriver.findElement(By.xpath(currencySign)).getText().trim();
		 String productItemPrice = webDriver.findElement(By.xpath(itemPrice)).getText().trim();
		 
		 getPrice = productItemCurrency+" "+productItemPrice;
		 
		 reportLog("Price: "+getPrice);
		 
		 takeScreenshot();
		 return getPrice;
	 }
	 
	 /**
	  * Method selects Reload button displayed in Order Summary Page during latency or down time. 
	  * It was observed that when application is slow or facing peak times, the sizes of the product items (like Small, Medium etc)
	  * are not displayed in Order Summary page and instead a Reload button is displayed. Upon selecting Reload, the sizes are displayed again.
	  * @return return the current instance.
	  */
	 public AdidasUIService clickReloadButton() {
		 
		try{
		 
		 WebElement reloadElement = webDriver.findElement(By.xpath(locator.get("mensection.reloadbutton")));
		 if(reloadElement.isDisplayed()){
			 reloadElement.click();
			 implicitWait(implicitWaitInSecs);
		  }
	    }
		catch(Exception e){
			reportLog("Reload button is not displayed.");
		}
		 
		return this;
	 }
	 
	 /**
	  * This method selects ADD TO BAG button in Order Summary Page.
	  * @return returns a confirmation message as 'Successfully added to bag' displayed in Order Added to bag confirmation pop-up. 
	  */
	 public String addItemToBag(){
	 	 		 
		WebElement webElement = webDriver.findElement(By.xpath(locator.get("mensection.ordersummaryitemselectsize")));
		action.moveToElement(webElement).click().perform();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(locator.get("mensection.ordersummaryitemsizes"))));
	
		List<WebElement> sizes = webDriver.findElements(By.xpath(locator.get("mensection.ordersummaryitemsizes")));
		
		
		for(WebElement size: sizes){
			reportInfo("Selecting size-->"+size.getText());
			
			size.click();
			break;
		}
		 
		webDriver.findElement(By.xpath(locator.get("mensection.addtobagbutton"))).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator.get("mensection.successfullyaddedtobag"))));
		
		String confirmationText = webDriver.findElement(By.xpath(locator.get("mensection.successfullyaddedtobag"))).getText();
		
		 return confirmationText;
	 }
  
	 
	 /**
	  * Method to select View Bag button in Order added to bag confirmation pop-up.
	  * @return return the current instance.
	  */
	 public AdidasUIService selectViewBagButton(){
		
		 WebElement viewBagElement = webDriver.findElement(By.xpath(locator.get("mensection.viewbagbutton")));
		 
		 action.moveToElement(viewBagElement).click().perform();
		 
		 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator.get("mensection.viewbagtitle"))));
		 return this;
	 }
	 
     /**
      * This method gets the Product Item description and it's price from the View Bag section and returns them as a List collection. 
      * NOTE: The price fetched by this method includes only the price displayed initially in the Items page and doesn't include total charges  
      * with taxes and shipping charges.
      * @return List collection containing Product Item description and it's price from the View Bag section.
      */
	 public List<String> getItemTextAndPriceFromViewBagPage(){
		 
		 List<String> productItemAndPrice = new ArrayList<String>(); 
		 String itemText = webDriver.findElement(By.xpath(locator.get("mensection.viewbagitemtext"))).getText();
		 String itemPrice = webDriver.findElement(By.xpath(locator.get("mensection.viewbagitemprice"))).getText();
		 productItemAndPrice.add(itemText);
		 productItemAndPrice.add(itemPrice);
		 
		 return productItemAndPrice;
	 }
	 
	 /**
	  * This method is an internal method to this class that provides mechanism for implicit wait conditions for the elements to be displayed.
	  * @param secs input parameter in seconds. 
	  */
	 public void implicitWait(int secs){
		
		 webDriver.manage().timeouts().implicitlyWait(secs, TimeUnit.SECONDS);
	}
	
}
