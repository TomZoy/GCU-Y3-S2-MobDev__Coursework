package org.me.myandroidstuff;


import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CarParkListingTestActivity extends Activity 
{
	private TextView response;
	private TextView errorText;
	private String result;
    private String sourceListingURL = "http://open.glasgow.gov.uk/api/live/parking.php?type=xml";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Get the TextView object on which to display the results
        response = (TextView)findViewById(R.id.urlResponse);
        try
        {
        	// Get the data from the RSS stream as a string
        	result =  sourceListingString(sourceListingURL);
        	
        	// Do some processing of the data to get the individual parts of the XML stream
        	// At some point put this processing into a separate thread of execution
        	
        	// Display the string in the TextView object just to demonstrate this capability
        	// This will need to be removed at some point
        	response.setText(result);
        }
        catch(IOException ae)
        {
        	// Handle error
        	response.setText("Error");
        	// Add error info to log for diagnostics
        	errorText.setText(ae.toString());
        } 
        
    } // End of onCreate
    
    // Method to handle the reading of the data from the XML stream
    private static String sourceListingString(String urlString)throws IOException
    {
	 	String result = "";
    	InputStream anInStream = null;
    	int response = -1;
    	URL url = new URL(urlString);
    	URLConnection conn = url.openConnection();
    	
    	// Check that the connection can be opened
    	if (!(conn instanceof HttpURLConnection))
    			throw new IOException("Not an HTTP connection");
    	try
    	{
    		// Open connection
    		HttpURLConnection httpConn = (HttpURLConnection) conn;
    		httpConn.setAllowUserInteraction(false);
    		httpConn.setInstanceFollowRedirects(true);
    		httpConn.setRequestMethod("GET");
    		httpConn.connect();
    		response = httpConn.getResponseCode();
    		// Check that connection is Ok
    		if (response == HttpURLConnection.HTTP_OK)
    		{
    			// Connection is Ok so open a reader 
    			anInStream = httpConn.getInputStream();
    			InputStreamReader in= new InputStreamReader(anInStream);
    			BufferedReader bin= new BufferedReader(in);
    			
    			// Read in the data from the XML stream
    			String line = new String();
    			while (( (line = bin.readLine())) != null)
    			{
    				result = result + "\n" + line;
    			}
    		}
    	}
    	catch (Exception ex)
    	{
    			throw new IOException("Error connecting");
    	}
    	
    	// Return result as a string for further processing
    	return result;
    	
    } // End of sourceListingString
    
} // End of Activity class