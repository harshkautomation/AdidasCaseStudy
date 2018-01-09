package APITest;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static test.oracle.cloud.adminui.framework.FrameworkReporter.reportLog;

import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.ClientResponse;
import static framework.FrameworkReporter.*;

import APITest.RestManager;

/**
 * RestService class provides various methods to implement REST calls for individual modules as applicable for a business scenario (or use case scenario).
 */
public class RestService {
	
	/**
	 * This method validates the response time of the URL passed as input parameter to be below 1 second. 
	 * The buildRequest() method defined in RestManager class defines the timeout values for connection timeout and read timeout.
	 * The method returns boolean value true if the GET call returns 200 response code as success within 1 second of response time, else it throws SocketTimeoutException
	 * @param requestURL as input parameter
	 * @return boolean value true if response is below 1 second and GET call return 200 response code, else false value returned.
	 * @throws SocketTimeoutException
	 */
	public static boolean getClientResponseTime(String requestURL) throws SocketTimeoutException{
		RestManager rm = RestManager.buildRequest(requestURL);
		
		boolean flag=false;

		ClientResponse resp = rm.get();
		rm.dispose();

		if(resp.getStatus()!=200)
			throw new RuntimeException("Rest GET call is not successful. Http status code is not 200 and status code returned is: "+resp.getStatus());
		
		reportInfo("Status code is-->"+resp.getStatus());
		
		if(resp.getStatus()==200)
			flag=true;
		else
			flag=false;
		
		String str = resp.getEntity(String.class);
		reportLog("JSON String-->"+str);
		return flag;
	}
	
	/**
	 * This method creates a List collection ImageURLCollection of individual image collections of all the Image URLs present in the JSON response from the GET call. 
	 * The Image URLs are categorized and added to specific collections like Desktop Image URL collection, Table Image URL collection and Mobile Image URL collection.
	 * All these individual collections are then added to main List collection ImageURLCollection.
	 * NOTE: List collection ImageURLCollection is later passed as input parameter to validateImageURLs method below to validate if all the image URLs are accessible.
	 * @param requestURL as input parameter
	 * @return List collection ImageURLCollection containing individual collections for Desktop Image URLs, Table Image URLs and Mobile Image URLs
	 * @throws SocketTimeoutException if response time is not below 1 second.
	 */
	public static List<List<String>> getImageURLFromClientResponse(String requestURL) throws SocketTimeoutException{
		RestManager rm = RestManager.buildRequest(requestURL);
        
		List<List<String>> ImageURLCollection = new ArrayList<List<String>>();
		
		List<String> desktopImageURLCollection = new ArrayList<String>(); 
		List<String> tabletImageURLCollection = new ArrayList<String>(); 
		List<String> mobileImageURLCollection = new ArrayList<String>(); 
		
		ClientResponse resp = rm.get();
		rm.dispose();

		if(resp.getStatus()!=200)
			throw new RuntimeException("Rest GET call is not successful. Http status code is not 200 and status code returned is: "+resp.getStatus());
		
		reportLog("Status->"+resp.getStatus());
		
		String str = resp.getEntity(String.class);
		reportLog("JSON String-->"+str);
		
		JsonArray arrayItems = toJsonObject(str).getAsJsonArray("component_presentations");
		   
 		for(JsonElement jsonElt: arrayItems){
 			
 			if(jsonElt.toString().contains(".jpg")) {
 				JsonArray componentItems = jsonElt.getAsJsonObject().get("component").getAsJsonObject().get("content_fields").getAsJsonObject().get("items").getAsJsonArray();
 				
 				for(JsonElement componentItemsElement: componentItems) {	
 					if(componentItemsElement.toString().contains("Image") && componentItemsElement.toString().contains("background_media")) {
 						
 						JsonElement imageElement = componentItemsElement.getAsJsonObject().get("background_media");
 		
 						if(imageElement.toString().contains("desktop_image")) {
 						JsonElement desktopImageElement = imageElement.getAsJsonObject().get("desktop_image").getAsJsonObject().get("url");
 						 						
 						desktopImageURLCollection.add(desktopImageElement.toString().replaceAll("^\"|\"$", ""));
 						}
 						
 						if(imageElement.toString().contains("tablet_image")) {
 	 						JsonElement tableImageElement = imageElement.getAsJsonObject().get("tablet_image").getAsJsonObject().get("url");
 	 						tabletImageURLCollection.add(tableImageElement.toString().replaceAll("^\"|\"$", ""));
 	 						}
 						
 						if(imageElement.toString().contains("mobile_image")) {
 	 						JsonElement mobileImageElement = imageElement.getAsJsonObject().get("mobile_image").getAsJsonObject().get("url");
 	 						mobileImageURLCollection.add(mobileImageElement.toString().replaceAll("^\"|\"$", ""));
 	 						}
 						
 					}
 					
 						if(componentItemsElement.toString().contains("Image") && componentItemsElement.toString().contains("media_items")) {
 						
 						JsonElement imageElement = componentItemsElement.getAsJsonObject().get("media_items");
 				
 						if(imageElement.toString().contains("desktop_image")) {
 						JsonElement desktopImageElement = imageElement.getAsJsonObject().get("desktop_image").getAsJsonObject().get("url");
 						desktopImageURLCollection.add(desktopImageElement.toString().replaceAll("^\"|\"$", ""));
 						
 						}
 						
 						if(imageElement.toString().contains("tablet_image")) {
 	 						JsonElement tableImageElement = imageElement.getAsJsonObject().get("tablet_image").getAsJsonObject().get("url");	
 	 						tabletImageURLCollection.add(tableImageElement.toString().replaceAll("^\"|\"$", ""));
 	 						}
 						
 						if(imageElement.toString().contains("mobile_image")) {
 	 						JsonElement mobileImageElement = imageElement.getAsJsonObject().get("mobile_image").getAsJsonObject().get("url");	
 	 						mobileImageURLCollection.add(mobileImageElement.toString().replaceAll("^\"|\"$", ""));
 	 						}
 					 }
 				  }
 			 }
 		}
 		
 		ImageURLCollection.add(desktopImageURLCollection);
 		ImageURLCollection.add(tabletImageURLCollection);
 		ImageURLCollection.add(mobileImageURLCollection);
 		
		return ImageURLCollection;
	}
	
	
	/**
	 * This method returns a collection of status codes after performing GET call of all the image URLs passed as input parameter as a List collection of image URLs.
	 * @param imageURLCollection List collection containing individual collections for Desktop Image URLs, Table Image URLs and Mobile Image URLs
	 * @return collection of response status codes to validate if all the image URLs were accessible.
	 * @throws SocketTimeoutException if response time is not below 1 second.
	 */
	public static List<Integer> validateImageURLs(List<List<String>> imageURLCollection) throws SocketTimeoutException{
		
	       List<Integer> statusCodes = new ArrayList<Integer>();
	       
	       for(List<String> imageURL: imageURLCollection) {
	    	   
	    	   for(String url: imageURL) {
	    		   
	    		   reportInfo("Image URL is->"+url);
	    		   RestManager rm = RestManager.buildRequest(url);

	    			ClientResponse resp = rm.get();
	    			rm.dispose();
	    			
	    			if(resp.getStatus()!=200)
	    				throw new RuntimeException("Rest GET call is not successful. Http status code is not 200 and status code returned is: "+resp.getStatus());
	    			
	    			reportInfo("Status is->"+resp.getStatus());
	    			
	    			statusCodes.add(resp.getStatus());  // Create the collection of status codes returned for Get command for Image URLs.
	    			
	    	   }   
	       }
	       
	       return statusCodes;
		}

	
	/**
	 * This method validates if every component has at least analytics data “analytics_name” in it.
	 * Tests shall PASS if the component contains “analytics_name” in it, else tests will FAIL if “analytics_name” is not present.
	 * @param requestURL as input parameter
	 * @throws SocketTimeoutException if response time is not below 1 second.
	 */
	public static void getAnalyticsData(String requestURL) throws SocketTimeoutException{
		RestManager rm = RestManager.buildRequest(requestURL);
		SoftAssert testSoftAssert = new SoftAssert();
       
		ClientResponse resp = rm.get();
		rm.dispose();

		if(resp.getStatus()!=200)
			throw new RuntimeException("Rest GET call is not successful. Http status code is not 200 and status code returned is: "+resp.getStatus());
		
		reportInfo("Http Get Status->"+resp.getStatus());
		
		String jsonString = resp.getEntity(String.class);

	    JsonArray arrayItems = toJsonObject(jsonString).getAsJsonArray("component_presentations");
		   
 	
 			for(int i=0;i<arrayItems.size();i++){
 			
 			String componentValue = arrayItems.get(i).toString();
 			
 			if(componentValue.contains("analytics_name")) {
 				
 				reportPass("Verified Component:"+(i+1)+" contains analytics_name");
 				
 				JsonArray componentItems = arrayItems.get(i).getAsJsonObject().get("component").getAsJsonObject().get("content_fields").getAsJsonObject().get("items").getAsJsonArray();
 				
 				JsonElement componentTypeElement = arrayItems.get(i).getAsJsonObject().get("component").getAsJsonObject().get("component_type");
 				
 				reportInfo("Component Type is-->"+componentTypeElement.toString());
 				
 				for(JsonElement componentItemsElement: componentItems) {
 			
 					//if(componentItemsElement.toString().contains("analytics_name") && componentItemsElement.toString().contains("background_media")) {			
 					if(componentItemsElement.toString().contains("analytics_name")) {
 						JsonElement analyticsNameElement = componentItemsElement.getAsJsonObject().get("supporting_fields").getAsJsonObject().get("supporting_fields").getAsJsonObject().get("standard_metadata").getAsJsonObject().get("analytics_name");		
 						reportInfo("Analytics Name is-->"+analyticsNameElement.toString());
 				 	}
 						
 				   } //end of componentItems for loop.	
 			  }//end of if loop checking for analytics_name
 			else {
 				testSoftAssert.fail("Verified Component:"+(i+1)+" does not contain analytics_name");	
 			  }
 		    } //end of arrayItems for loop.
 			
 			testSoftAssert.assertAll();
 			//assertTest();
	}
	

	/**
	 * Prepares a JsonObject instance from the input JSON string.
	 * @param jsonString input JSON string.
	 * @return a JsonObject instance from the input JSON string.
	 */
	private static JsonObject toJsonObject(String jsonString){
		
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		return gson.fromJson(jsonString, JsonObject.class);
	}
	
	/**
	 * Utility method to convert the input JSON string into a map form.
	 * @param jsonString input JSON string. 
	 * @return returns map form of the input JSON string.
	 */
	
	@SuppressWarnings("unchecked")
	private static Map<String,Object> getMapFromJSON(String jsonString){
 		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
 		
 		Map<String,Object> map = new HashMap<String,Object>();
 		map = (Map<String,Object>) gson.fromJson(jsonString, map.getClass());
 		return map;
 	}
}
