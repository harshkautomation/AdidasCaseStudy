package AdidasUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.Test;

import base.AdidasService;
import base.BaseTestCase;
import base.DataProviderMethods;
import framework.ExcelReader;


import static framework.FrameworkReporter.*;
import static framework.FrameworkReporter.reportPass;
import static framework.FrameworkReporter.assertTest;
import static framework.FrameworkReporter.takeScreenshot;

/**
 * This class contains all the test methods of TestNG class to perform Adidas UI tests.
 * The test methods that require data are executed by accessing DataProvider methods written in DataProviderMethods class in base package.
 * The data received as input parameter in tests is further processed to cater to individual test scenarios.
 * 
 * This class extends BaseTestCase class to inherit BaseTestCase methods. 
 *
 */
public class AdidasUITest extends BaseTestCase
{
	
     /**
      * This test method verifies the Product Item Description (Ex: Spain Home Authentic Jersey in Men->Jerseys section) and it's price
      * by accepting the input data from DataProvider 'productItemAndPriceData' in DataProviderMethods class.
      * @param data input data containing Category and Sub Category to be selected, product item and its expected price to be verified in UI.
      */
     @Test(dataProvider = "productItemAndPriceData", dataProviderClass = DataProviderMethods.class, priority=0)
     public void AdidasUI_Category_VerifyProductItemAndPriceTest(String data){
    	 
      String[] dataStorage = data.split("-");   	 

      String categoryToSelect = dataStorage[0];
      String subCategoryToSelect = dataStorage[1];
      String productItemToSelect = dataStorage[2];
      String expectedProductItemPrice = dataStorage[3];
      
      
      reportInfo("Category to be selected: "+categoryToSelect);
      reportInfo("Sub Category to be selected: "+subCategoryToSelect);
      reportInfo("Product Item to be selected: "+productItemToSelect);
      reportInfo("Expected Product Item price to be verified in UI: "+expectedProductItemPrice);
      
      
      String productItemPrice;	 
    	 
    //  productItemPrice = AdidasService.adidasMenService(driver).navigate("Men").selectCategory("Jerseys").getPrice("Spain Home Authentic Jersey");
    	 
      productItemPrice = AdidasService.adidasUIService(driver).navigate(categoryToSelect).selectCategory(subCategoryToSelect).getPrice(productItemToSelect);
      
      if(productItemPrice.equals(expectedProductItemPrice)){
    	  reportPass(String.format("Verified Actual and Expected product item price is same for the Product: { %s }. Actual { %s } | Expected { %s }",
    			  productItemToSelect, productItemPrice, expectedProductItemPrice));
      }
      else{
    	  reportFail(String.format("Verified Actual and Expected product item price is not same for the Product: { %s }. Actual { %s } | Expected { %s }",
    			  productItemToSelect, productItemPrice, expectedProductItemPrice));
      }
  
        takeScreenshot();
		assertTest();
   }
	
     
     /**
      * This test method verifies the Product Item Description (Ex: Spain Home Authentic Jersey in Men->Jerseys section) and it's price
      * in Product Item Page and proceeds to verify the item text and price in Order Summary and View Bag sections.
      * 
      * Input data is passed from DataProvider 'productItemAndPriceData' in DataProviderMethods class.
      * @param data input data containing Category and Sub Category to be selected, product item and its expected price to be verified in UI.
      */
     @Test(dataProvider = "productItemAndPriceData", dataProviderClass = DataProviderMethods.class, priority=1)
     public void AdidasUI_Category_VerifyProductItemAndPriceInOrderSummaryAndViewBag(String data){
    	 	    
    	      String[] dataStorage = data.split("-");   	 

    	      String categoryToSelect = dataStorage[0];
    	      String subCategoryToSelect = dataStorage[1];
    	      String productItemToSelect = dataStorage[2];
    	      String expectedProductItemPrice = dataStorage[3];
    	      
    	      
    	      reportInfo("Category to be selected: "+categoryToSelect);
    	      reportInfo("Sub Category to be selected: "+subCategoryToSelect);
    	      reportInfo("Product Item to be selected: "+productItemToSelect);
    	      reportInfo("Expected Product Item price to be verified in UI: "+expectedProductItemPrice);
    	      
    	   
    	      String productItemPrice;
    	      String expectedConfirmationMessage,actualConfirmationMessage;
    	      
    	      List<String> itemTextAndPriceOrderSummary = new ArrayList<String>();
    	      List<String> itemTextAndPriceViewBag = new ArrayList<String>();
    	      
    	      productItemPrice = AdidasService.adidasUIService(driver).navigate(categoryToSelect).selectCategory(subCategoryToSelect).getPrice(productItemToSelect);
    	      
    	      if(productItemPrice.equals(expectedProductItemPrice)){
    	    	  reportPass(String.format("Verified Actual and Expected product item price is same for the Product: { %s }. Actual { %s } | Expected { %s }",
    	    			  productItemToSelect, productItemPrice, expectedProductItemPrice));
    	      }
    	      else{
    	    	  reportFail(String.format("Verified Actual and Expected product item price is not same for the Product: { %s }. Actual { %s } | Expected { %s }",
    	    			  productItemToSelect, productItemPrice, expectedProductItemPrice));
    	      }
    	  
    	      
      
    	      //Verifying item description in Product Item Page and Order Summary Page.
    	      itemTextAndPriceOrderSummary = AdidasService.adidasUIService(driver).selectProductItem(productItemToSelect).getProductItemTextAndPriceFromOrderSummary();
      
    	      
    	      
    	      if(itemTextAndPriceOrderSummary.contains(productItemToSelect.toUpperCase())){
    	    	  reportPass(String.format("Verified Actual and Expected product item description is same in Product Items page and Order Summary Page. Actual { %s } | Expected { %s }",
    	    			  itemTextAndPriceOrderSummary.get(0), productItemToSelect.toUpperCase()));
    	      }
    	      else{
    	    	  reportFail(String.format("Verified Actual and Expected product item description is not same in Product Items page and Order Summary Page. Actual { %s } | Expected { %s }",
    	    			  itemTextAndPriceOrderSummary.get(0), productItemToSelect));
    	      }
    	    	  
    	      
    	      if(itemTextAndPriceOrderSummary.contains(expectedProductItemPrice)){
    	    	  reportPass(String.format("Verified Actual and Expected product item price is same in Product Items page and Order Summary Page. Actual { %s } | Expected { %s }",
    	    			  itemTextAndPriceOrderSummary.get(1), expectedProductItemPrice));
    	      }
    	      else{
    	    	  reportFail(String.format("Verified Actual and Expected product item price is not same in Product Items page and Order Summary Page. Actual { %s } | Expected { %s }",
    	    			  itemTextAndPriceOrderSummary.get(1), expectedProductItemPrice));
    	      }
    	    	  
    	 	 takeScreenshot();
    	      
    	      AdidasService.adidasUIService(driver).clickReloadButton(); //Perform this action if there is Reload button displayed in Order Summary Page.
    		  
    	      expectedConfirmationMessage = getExpectedMessageAddToBagConfirmationMessage(); 
    	      
    	      
    	      actualConfirmationMessage = AdidasService.adidasUIService(driver).addItemToBag();
    	 	 
    	 	 if(actualConfirmationMessage.equalsIgnoreCase(expectedConfirmationMessage)){
    	 		 reportPass(String.format("Verified Actual and Expected confirmation message is same for the Product: { %s } upon adding it to the bag. Actual { %s } | Expected { %s }",
   	    			  productItemToSelect, actualConfirmationMessage, expectedConfirmationMessage.toUpperCase()));
    	 	 }
    	 	 else{
    	 		reportFail(String.format("Actual and Expected confirmation message is not same for the Product: { %s } upon adding it to the bag. Actual { %s } | Expected { %s }",
     	    			  productItemToSelect, actualConfirmationMessage, expectedConfirmationMessage.toUpperCase()));
      	 	 }
    	 			 
    	 	 
    	 	takeScreenshot();
    	 	  
    	 	 itemTextAndPriceViewBag = AdidasService.adidasUIService(driver).selectViewBagButton().getItemTextAndPriceFromViewBagPage();
    	 	 
    	 	 if(itemTextAndPriceViewBag.contains(productItemToSelect.toUpperCase()))
    	 		reportPass(String.format("Verified Actual and Expected product item description is same in Product Items page and View Bag Page. Actual { %s } | Expected { %s }",
    	 				itemTextAndPriceViewBag.get(0), productItemToSelect.toUpperCase()));
    	 	  else
    	 		 reportFail(String.format("Verified Actual and Expected product item description is not same in Product Items page and View Bag Page. Actual { %s } | Expected { %s }",
    	 				itemTextAndPriceViewBag.get(0), productItemToSelect.toUpperCase()));
    	 	 
    	 	 
    	 	if(itemTextAndPriceViewBag.contains(expectedProductItemPrice))
  	    	  reportPass(String.format("Verified Actual and Expected product item price is same in Product Items page and View Bag Page. Actual { %s } | Expected { %s }",
  	    			itemTextAndPriceViewBag.get(1), expectedProductItemPrice));
    	 	else
  	    	  reportFail(String.format("Verified Actual and Expected product item price is not same in Product Items page and View Bag Page. Actual { %s } | Expected { %s }",
  	    			itemTextAndPriceViewBag.get(1), expectedProductItemPrice));
    	 		
    	 	takeScreenshot();
    		assertTest();

   }
     
     
    /**
     * This test method verifies Sort By functionality by selecting the Sort By option as 'Price (Low to High)'. 
     * The prices of the product items displayed in Items page are grouped in a List collection and verified if they are sorted from low to high prices.
     *
     * Input data is passed from DataProvider 'sortData' in DataProviderMethods class.
     * @param data input data containing Category and Sub Category to be selected.
     */
 	@Test(dataProvider = "sortData", dataProviderClass = DataProviderMethods.class, priority=2)
 	public void AdidasUI_Category_SortBy_Price_LowToHigh(String data){
 		
 		String[] dataStorage = data.split("-");   	 

 		String categoryToSelect = dataStorage[0];
 	      String subCategoryToSelect = dataStorage[1];
 	    
 	    
 	      reportInfo("Category to be selected: "+categoryToSelect);
 	      reportInfo("Sub Category to be selected: "+subCategoryToSelect);
 	      
 	      
 	      List<String> priceList = new ArrayList<String>();
 	      List<String> copyPriceList = new ArrayList<String>();
 	
 	      priceList = AdidasService.adidasUIService(driver).navigate(categoryToSelect).selectCategory(subCategoryToSelect).selectSortByFieldOption("Price (low/high)").validatePricesSortedFromLowToHigh();
 		
 	      copyPriceList = priceList;
 	      
 	      Collections.sort(priceList);
 	      
 	      if(priceList.equals(copyPriceList))
 	    	  reportPass("Verified all the prices displayed in "+categoryToSelect+" category are in sorted order from Low Price to High Price.");
 	      else
 	    	  reportFail("All the prices displayed in "+categoryToSelect+" category are not in sorted order from Low Price to High Price.");

 		takeScreenshot();
 		assertTest();
 		
 	 }
     
    /**
     * This test verifies first product item and its price displayed in Men->Jerseys section.
     * The product item and price are verified to be same in Product Item Page, Order Summary Page and View Bag page.  
     */
	@Test(priority=3)
	public void AdidasUI_Men_Jerseys_VerifyFirstProductSelectionAndPriceTest(){
	
		List<String> firstItemTextAndPrice = new ArrayList<String>();
		List<String> itemTextAndPriceOrderSummary = new ArrayList<String>();
		List<String> itemTextAndPriceViewBag = new ArrayList<String>();
		
		String actualConfirmationMessage,expectedConfirmationMessage;
		
		firstItemTextAndPrice = AdidasService.adidasUIService(driver).navigate("Men").selectCategory("Jerseys").getFirstProductItemTextAndPrice();
		
	  reportLog("First Item->"+firstItemTextAndPrice);

	  itemTextAndPriceOrderSummary = AdidasService.adidasUIService(driver).selectFirstProductItem().getProductItemTextAndPriceFromOrderSummary();
	  reportLog("Order summary->"+itemTextAndPriceOrderSummary);
	  
	  if(firstItemTextAndPrice.equals(itemTextAndPriceOrderSummary))
		 // reportPass("Verified the first item selected text and price is same in Product Item page and Order Summary Page: "+firstItemTextAndPrice);
		  reportPass(String.format("Verified Actual and Expected product item description and item price is same in Product Item page and Order Summary Page. Actual Product Item Description { %s } | Expected Product Item Description { %s }."+
				  "Actual Product Item Price { %s } | Expected Product Item Price { %s }.",
				  itemTextAndPriceOrderSummary.get(0), firstItemTextAndPrice.get(0),itemTextAndPriceOrderSummary.get(1), firstItemTextAndPrice.get(1)));
	  else
		  reportFail(String.format("Actual and Expected product item description and item price is not same in Product Item page and Order Summary Page. Actual Product Item Description { %s } | Expected Product Item Description { %s }."+
				  "Actual Product Item Price { %s } | Expected Product Item Price { %s }.",
				  itemTextAndPriceOrderSummary.get(0), firstItemTextAndPrice.get(0),itemTextAndPriceOrderSummary.get(1), firstItemTextAndPrice.get(1)));
	  
	  AdidasService.adidasUIService(driver).clickReloadButton(); //Perform this action if there is Reload button displayed in Order Summary Page.
	 
	  
	  expectedConfirmationMessage = getExpectedMessageAddToBagConfirmationMessage(); 
      
      
      actualConfirmationMessage = AdidasService.adidasUIService(driver).addItemToBag();
 	 
 	 if(actualConfirmationMessage.equalsIgnoreCase(expectedConfirmationMessage)){
 		 reportPass(String.format("Verified Actual and Expected confirmation message is same for the Product: { %s } upon adding it to the bag. Actual { %s } | Expected { %s }",
 				firstItemTextAndPrice.get(0), actualConfirmationMessage, expectedConfirmationMessage));
 	 }
 	 else{
 		reportFail(String.format("Actual and Expected confirmation message is not same for the Product: { %s } upon adding it to the bag. Actual { %s } | Expected { %s }",
 				firstItemTextAndPrice.get(0), actualConfirmationMessage, expectedConfirmationMessage));
	 	 }
 		 
	 	 
	  
	 itemTextAndPriceViewBag = AdidasService.adidasUIService(driver).selectViewBagButton().getItemTextAndPriceFromViewBagPage();
	 
	 if(itemTextAndPriceViewBag.contains(firstItemTextAndPrice.get(0).toUpperCase()))
	 		reportPass(String.format("Verified Actual and Expected product item description is same in Product Items page and View Bag Page. Actual { %s } | Expected { %s }",
	 				itemTextAndPriceViewBag.get(0), firstItemTextAndPrice.get(0).toUpperCase()));
	 	  else
	 		 reportFail(String.format("Verified Actual and Expected product item description is not same in Product Items page and View Bag Page. Actual { %s } | Expected { %s }",
	 				itemTextAndPriceViewBag.get(0), firstItemTextAndPrice.get(0).toUpperCase()));
	 	 
	 	 
	 	if(itemTextAndPriceViewBag.contains(firstItemTextAndPrice.get(1).toUpperCase()))
   	  reportPass(String.format("Verified Actual and Expected product item price is same in Product Items page and View Bag Page. Actual { %s } | Expected { %s }",
   			itemTextAndPriceViewBag.get(1), firstItemTextAndPrice.get(1).toUpperCase()));
	 	else
   	  reportFail(String.format("Verified Actual and Expected product item price is not same in Product Items page and View Bag Page. Actual { %s } | Expected { %s }",
   			itemTextAndPriceViewBag.get(1), firstItemTextAndPrice.get(1).toUpperCase()));
	 	
	 	takeScreenshot();
		assertTest();
	 
	}
	
	
	/**
	 * This test performs search operation in Home Page by accepting search value as input parameter.
	 * The test verifies for the presence of the search input text appearing or displayed in the Product Items listed as search results.
	 * NOTE: Presently verified this test scenario by passing the search text input as 'Shoes'. It's observed in the Adidas UI application that not all 
	 * the search results contain the exact matching text of the input search parameter. For example: If input search text is 'Shirts', then all search results 
	 * displayed in UI may not contain 'Shirts' text.
	 * Hence presently the below test automation has a limitation on verifying the matching texts provided as input search parameter and can 
	 * verify only those search results in UI that contain exact matching input search text passed from adidas.xls excel document.
	 *
	 * @param data accepts input parameter to perform search operation.
	 */
	@Test(dataProvider = "searchData", dataProviderClass = DataProviderMethods.class, priority=4)
	public void AdidasUI_HomePage_Search(String data){
			
		
			String[] searchArray = data.split("-");
			
			String searchText = searchArray[0];
			
			List<String> searchResults = new ArrayList<String>();
			
			searchResults = AdidasService.adidasUIService(driver).performSearch(searchText).getSearchResultsData();
			
		
			for(String searchData: searchResults){
			 if(searchData.contains(searchText.toUpperCase()))
				reportPass(String.format("Verified Actual value contains Expected search value in search results in UI. Actual { %s } | Expected { %s }",
						searchData, searchText.toUpperCase()));
			else
				reportFail(String.format("Actual value does not contain Expected search value in search results in UI. Actual { %s } | Expected { %s }",
						searchData, searchText.toUpperCase()));
		  }//end of for loop
		
			takeScreenshot();
			assertTest();
		 }
		
	
		/**
		 * When there no orders placed, the view bag is expected to be empty. This test verifies the view bag to be empty.
		 * For example, when user launches the home page for the first time, there shall be no orders present in View Bag and it displays message as 'Your Bag is Empty'. 
		 */
		@Test(priority=5)
		public void AdidasUI_Verify_Empty_Bag(){
			
			String actualEmptyBagMessage, expectedEmptyBagConfirmationMessage;

			expectedEmptyBagConfirmationMessage = getExpectedMessageBagEmptyConfirmationMessage();
			
			actualEmptyBagMessage = AdidasService.adidasUIService(driver).selectViewBagIcon().verifyEmptyBag();
			
			if(actualEmptyBagMessage.equalsIgnoreCase(expectedEmptyBagConfirmationMessage))
				reportPass(String.format("Verified Actual and Expected message are same for the Empty Bag. Actual { %s } | Expected { %s }",
						actualEmptyBagMessage, expectedEmptyBagConfirmationMessage));
			else
				reportFail(String.format("Actual and Expected message are not same for the Empty Bag. Actual { %s } | Expected { %s }",
						actualEmptyBagMessage, expectedEmptyBagConfirmationMessage));
			
			takeScreenshot();
			assertTest();
			
		 }

	/**	
	 * Below are private methods that return expected message for individual scenarios.
	 * 
	 */
	private String getExpectedMessageAddToBagConfirmationMessage(){
		String expectedMessage = null;

				expectedMessage = getMessage("addToBagConfirmMessage");
		return expectedMessage;
	}
	
	private String getExpectedMessageBagEmptyConfirmationMessage(){
		String expectedMessage = null;

				expectedMessage = getMessage("bagEmptyMessage");
		return expectedMessage;
	}
	
	private String getExpectedMessageOrderNumberValidationMessage(){
		String expectedMessage = null;

				expectedMessage = getMessage("orderNumberValidationMessageInOrderTracker");
		return expectedMessage;
	}
	
	private String getExpectedMessageEmailAddressValidationMessage(){
		String expectedMessage = null;

				expectedMessage = getMessage("emailAddressValidationMessageInOrderTracker");
		return expectedMessage;
	}
	
	private String getExpectedMessageInvalidCombinationMessage(){
		String expectedMessage = null;

				expectedMessage = getMessage("invalidCombinationWarningOrderTracker");
		return expectedMessage;
	}
	
	private String getExpectedMessageIncorrectEmailOrderNumberMessage(){
		String expectedMessage = null;

				expectedMessage = getMessage("incorrectEmailOrderNumberOrderTracker");
		return expectedMessage;
	}
}
