package APITest;


import java.util.Map;
import java.util.Map.Entry;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

/**
 * RestManager class provides mechanism to perform REST operations by making REST calls to host servers.
 * This class includes methods to perform GET, POST, PATCH and PUT methods.
 *
 */
public class RestManager{

	private Client client;
	private Builder requestBuilder;
	private String bodyContent;	
	
	public static final String APPLICATION_JSON = "application/json";
	public static final String TEXT_PLAIN = "text/plain";
	public static final String APPLICATION_XML = "application/xml";
	
	/**
	 * Constructor that initializes the Builder and Client variables.
	 * @param requestBuilder Builder instance
	 * @param client instance
	 */
	protected RestManager(Builder requestBuilder, Client client){
		this.requestBuilder = requestBuilder;
		this.client = client;
	}

	/**
	 * Prepares the URI Request by accepting http type, host, port and url provided as input parameters.
	 * @param httpType Type of http (http or https)
	 * @param host Hostname
	 * @param port Port Number
	 * @param url URL information
	 * @return returns an instance of RestManager class
	 */
    public static RestManager buildRequest(String httpType, String host, String port, String url){
    	String requestType = httpType.equals("http") ? "http" : "https";
    	String requestURI = String.format("%s://%s:%s/%s", requestType, host, port, url);
    	Client client = Client.create();
    	WebResource webResource = client.resource(requestURI);
    	return new RestManager(webResource.getRequestBuilder(), client);
    }
	
    /**
     * Prepares the client URI request by using create method of Client class and getRequestBuilder method of WebResource class
     * @param requestURI URI string passed as input
     * @return returns an instance of RestManager class
     */
    public static RestManager buildRequest(String requestURI){
    	Client client = Client.create();
    	client.setConnectTimeout(1000);
		client.setReadTimeout(1000);  //Setting the read time out for 1s for the response time.
    	WebResource webResource = client.resource(requestURI);
    	return new RestManager(webResource.getRequestBuilder(), client);
    }
    
    /**
     * Method to add authorization for the username and password provided as inputs.
     * @param userName Username value
     * @param password Password value
     */
    public void addAuthorization(String userName, String password){
    	client.addFilter(new HTTPBasicAuthFilter(userName, password));
    }
    
    /**
     * Adding header to perform the REST operation. 
     * @param key the HTTP header name.
     * @param value the HTTP header value.
     */
    public void addHeader(String key, String value){
    	requestBuilder.header(key, value);
    }

    /**
    * Adding header for a collection of header key and value pairs to perform the REST operation. 
    * @param headersMap Collection of HTTP header name and their values.
    */
    public void addHeader(Map<String, String> headersMap){
       for ( Entry<String, String> headerEntry : headersMap.entrySet()) {
		this.addHeader(headerEntry.getKey(), headerEntry.getValue());
       }
	}   
      
    /**
     * Adds a media type (Example: application/json) to the request builder and initializes bodyContent variable with content details in the payload.
     * @param type MediaType
     * @param content Content in the body of the payload.
     */
    public void addBody(String type, String content){
    	requestBuilder.accept(type).type(type);
       	bodyContent = content;
    }
    
    /**
     * Adds a media type (Example: application/json) and Content Type to the request builder and initializes bodyContent variable with content details in the payload.
     * @param acceptType MediaType value
     * @param contentType ContentType value
     * @param content Content in the body of the payload.
     */
    public void addBody(String acceptType, String contentType, String content){
    	Builder hm = requestBuilder.accept(acceptType).type(contentType);
    	bodyContent = content;
    }
    
    /**
     * Method to get (GET operation) the REST payload.
     * @return client response instance containing REST GET status and the JSON response.
     */
    public ClientResponse get(){
    	ClientResponse response = requestBuilder.get(ClientResponse.class);
    	return response;
    }
    
    
    /**
     * Destroy the Client instance created to execute REST calls.
     */
    public void dispose() {
		client.destroy();
	}
}
