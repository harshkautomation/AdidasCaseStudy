package APITest;

import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.auth.AuthScope;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import org.openqa.selenium.remote.internal.ApacheHttpClient;
import org.testng.annotations.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import static framework.FrameworkReporter.*;

/**
 * RestTest contains all the API test methods for Adidas Home page.
 */

public class RestTest {

	/**
	 * This test validates the response time of the given URL to be below 1 second. 
	 * @throws SocketTimeoutException if response time is not below 1 second.
	 */
	@Test(priority=0)
	public void Validate_Response_Time() throws SocketTimeoutException
	{
		//Store given URL in a string.
		String URL = "https://www.adidas.fi/api/pages/landing?url=index.html";
		boolean flag=false;
		
		reportLog("printing the data");
		flag = RestService.getClientResponseTime(URL);
		
		if(flag)
			reportPass("Verified response time is below 1s.");
		else
			reportFail("Failed: Response time is above 1s.");
   }
	
	/**
	 * This test validates all the Image URLs to be accessible with response code as 200 when GET call is performed with the Image URLs.
	 * GET call is performed on all the image URLs obtained from JSON response and response status is verified to be 200 for success.
	 * @throws SocketTimeoutException if response time is not below 1 second.
	 */
	@Test(priority=1)
	public void Validate_Image_URL() throws SocketTimeoutException
	{
		boolean flag=false;
		
		List<Integer> statusCodes = new ArrayList<Integer>();
		
		//Presently storing URL in a string variable.
		String URL = "https://www.adidas.fi/api/pages/landing?url=index.html";
		List<List<String>> imageURLCollection = new ArrayList<List<String>>();
	
		imageURLCollection = RestService.getImageURLFromClientResponse(URL);	
		statusCodes = RestService.validateImageURLs(imageURLCollection);
		
		//Perform check of each status code to be 200 in the statusCodes collection indicating successful 
		//GET command for Image URLs.
		for(Integer status: statusCodes) {
			
			if(status==200)
				flag=true;
			else {
				flag=false;
				break;   //Break and exit the loop if any single instance of GET command has returned any other status other than 200.
			}
		}
		
		
		if(flag)
			reportPass("Verified all the image URLs returned Http Status code as 200.");
		else
			reportFail("The image URLs failed to return Http Status code as 200.");
		
	}	
		
	/**
	 * This test validates if each component present in JSON response contain analytics data “analytics_name” in it.
	 * Tests shall PASS if the component contains “analytics_name” in it, else tests will FAIL if “analytics_name” is not present.
	 * @throws SocketTimeoutException if response time is not below 1 second.
	 */
	@Test(priority=2)
	public void Validate_Component_AnalyticsData() throws SocketTimeoutException
	{
		//Presently storing URL in a string variable.
		String URL = "https://www.adidas.fi/api/pages/landing?url=index.html";
		
		reportInfo("Verifying analytics data..");
		RestService.getAnalyticsData(URL);
	}	
	
} //end of class

