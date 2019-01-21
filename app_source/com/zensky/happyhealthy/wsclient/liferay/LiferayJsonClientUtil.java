package com.zensky.happyhealthy.wsclient.liferay;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.zensky.happyhealthy.Session;



/**
 * @author Zenobia Liendo
 */
public class LiferayJsonClientUtil {
	
	private static final int   DEFAULT_CONNECTION_TIMEOUT = 15000;
	private static String invoker =  "api/secure/jsonws/invoke"; 

								
	
	public static Object  makeLiferayJSONInvokerCall(Session session, 
			JSONObject command) throws Exception {
		
		JSONArray commands = new JSONArray();
		commands.put(command);
		JSONArray jsonResponse = LiferayJsonClientUtil.makeLiferayJSONArrayInvokerCall(session, commands);
        Object resultObject = jsonResponse.get(0);
		return resultObject;
		
	}
	

	public static JSONArray makeLiferayJSONArrayInvokerCall(Session session, 
			JSONArray commands) throws Exception {
		
		String url = LiferayServerInfo.SERVER_ADDRESS + "/" + invoker;
		
		// which user has login to the app?
		String userId = session.getUsername();
		String password = session.getPassword();

		// converting JSONArray to a string
		String commandsString = commands.toString();

		// Making the HTTP connection to the Liferay JSON Web Service
		String json = makeHTTPCall(url, userId, password, commandsString);

		// Converting String into a JSONArray, which is the expected response
		// from Liferay invoke
		JSONArray jsonResponse = null;
		try {
			if (json != null) {
				if (json.startsWith("{")) {
					JSONObject jsonObj = new JSONObject(json);

					if (jsonObj.has("exception")) {
						String message = jsonObj.getString("exception");

						throw new Exception(message);
					} else {
						throw new Exception("Unexpected return type: "
								+ json.toString());
					}
				} else if (json.startsWith("[")) {
					jsonResponse = new JSONArray(json); // converting String to
														// JSONArray
				}
			}
		} catch (JSONException je) {
			throw new Exception(je);
		}
		return jsonResponse;

	}

	private static String makeHTTPCall(String url, String userId, String password,
			String commandsString) throws Exception {

		// authentication credentials
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
				userId, password);
		Header authorization = BasicScheme.authenticate(credentials, "UTF-8",
				false);

		// build post
		HttpPost post = new HttpPost(url);
		post.addHeader(authorization);
		post.setEntity(new StringEntity(commandsString)); // adding post params

		// HTTPClient

		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(),
				DEFAULT_CONNECTION_TIMEOUT);
		HttpResponse response = client.execute(post);

		// Handle Response status
		int status = response.getStatusLine().getStatusCode();
		
		if (status == HttpStatus.SC_UNAUTHORIZED) {
			throw new LoginException("Authentication failed.");
		}
		if (status != HttpStatus.SC_OK) {
			throw new Exception("Request failed. Response code: " + status);
		}

		HttpEntity entity = response.getEntity();
		if (entity == null) {
			return null;
		}
		String responseString = EntityUtils.toString(entity);

		return responseString;

	}
	
	public static Bitmap getBitmapFromURL(String link) {
	    /*--- this method downloads an Image from the given URL, 
	     *  then decodes and returns a Bitmap object
	     ---*/
	    try {
	        URL url = new URL(link);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);

	        return myBitmap;

	    } catch (IOException e) {
	    	Log.e("LIFERAYSERVICE", "link " + link);
	        Log.e("LIFERAYSERVICE", "bitmap download " + e.getMessage());
	        return null;
	    }
	}


	
}