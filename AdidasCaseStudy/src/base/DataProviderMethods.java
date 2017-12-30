package base;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;
import framework.ExcelReader;

/**
 * This class contains all the DataProvider methods required to provide data for the various test methods defined in AdidasUITest class. 
 * The input data is obtained from an excel file adidas.xls located in workspace\AdidasCaseStudy\data folder. 
 * All the DataProvider methods access ExcelReader class file to read data from excel file.
 */
public class DataProviderMethods {

	/**
	 * This data provider method provides data related to Product Item and Price. 
	 * @return iterator over object array prepared from the List collection of data received from getting all the rows from excel sheet.
	 */
	@DataProvider(name = "productItemAndPriceData")
	public static Iterator<Object[]> MenJerseyDataProvider(){
		
		List<Object[]> dataCollector = new ArrayList<Object[]>();
	
		String pathSeperator = File.separator;
		String projectPath = System.getProperty("user.dir") + pathSeperator; 
		String filePath = String.format("%sdata%s", projectPath , pathSeperator)+"adidas.xls";
		
		ExcelReader er = new ExcelReader(filePath,"ProductItemAndPrice");
				
		List<String> data = er.getAllRowsData();
		
		for (String str : data) {
			dataCollector.add(new Object[] { str });
	    }
		
		return dataCollector.iterator();
	}
	
	/**
	 * This data provider method provides search related data. 
	 * @return iterator over object array prepared from the List collection of data received from getting all the rows from excel sheet.
	 */
	@DataProvider(name = "searchData")
	public static Iterator<Object[]> AdidasUISearchDataProvider(){
		
		List<Object[]> dataCollector = new ArrayList<Object[]>();
	
		String pathSeperator = File.separator;
		String projectPath = System.getProperty("user.dir") + pathSeperator; 
		String filePath = String.format("%sdata%s", projectPath , pathSeperator)+"adidas.xls";
		
		ExcelReader er = new ExcelReader(filePath,"Search");
				
		List<String> data = er.getAllRowsData();
		
		for (String str : data) {
			dataCollector.add(new Object[] { str });
	    }
		
		return dataCollector.iterator();
	}
	
	/**
	 * This data provider method provides data related to Category (Ex: Men/Women) and Sub Category(Ex: Jackets/Jerseys) where the sort functionality needs to be verified.
	 * @return iterator over object array prepared from the List collection of data received from getting all the rows from excel sheet.
	 */
	@DataProvider(name = "sortData")
	public static Iterator<Object[]> AdidasUISortDataProvider(){
		
		List<Object[]> dataCollector = new ArrayList<Object[]>();
	
		String pathSeperator = File.separator;
		String projectPath = System.getProperty("user.dir") + pathSeperator; 
		String filePath = String.format("%sdata%s", projectPath , pathSeperator)+"adidas.xls";
		
		ExcelReader er = new ExcelReader(filePath,"SortOrder");
				
		List<String> data = er.getAllRowsData();
		
		for (String str : data) {
			dataCollector.add(new Object[] { str });
	    }
		
		return dataCollector.iterator();
	}
	
	/**
	 * This data provider method provides Order Number and Email Address data for Order Tracking purpose.
	 * @return iterator over object array prepared from the List collection of data received from getting all the rows from excel sheet.
	 */
	@DataProvider(name = "orderTrackerData")
	public static Iterator<Object[]> AdidasUIOrderTrackerDataProvider(){
		
		List<Object[]> dataCollector = new ArrayList<Object[]>();
	
		String pathSeperator = File.separator;
		String projectPath = System.getProperty("user.dir") + pathSeperator; 
		String filePath = String.format("%sdata%s", projectPath , pathSeperator)+"adidas.xls";
		
		ExcelReader er = new ExcelReader(filePath,"OrderTracker");
				
		List<String> data = er.getAllRowsData();
		
		for (String str : data) {
			dataCollector.add(new Object[] { str });
	    }
		
		return dataCollector.iterator();
	}
	
}
