package com.zensky.happyhealthy;



import com.zensky.happyhealthy.wsclient.liferay.LoginException;
import com.zensky.happyhealthy.wsclient.liferay.service.UserService;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;



public class MainActivity extends Activity {
	

	private Session session;


	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		// check file
		// if file is empty => show login
		// if file with data =>authenticate and if ok => go to feeds activity 
		
		
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	// this method is called when user attempts to Login
	 public void login(View view) {
	        // Do something in response to button
		 
		// get userId and password from screen
		 EditText userIdText = (EditText) findViewById(R.id.user_id);
		 EditText passwordText = (EditText) findViewById(R.id.password);
		 
		 	 

		String userId = userIdText.getText().toString();
		String password = passwordText.getText().toString();
		
			
		session = new Session (userId, password);
		
		new AuthenticateUser().execute(session);	
		

			
	    	
	    }
	 
	 private class AuthenticateUser extends AsyncTask<Session, Void, String> {	 


			@Override
			protected String doInBackground(Session... params) {

				Session session = params[0];

				try { 					
					UserService service = new UserService (session);
					User user = service.getUserInfo(session.getUsername());
					session.setUserId(user.getUserId());
					return "";
				} catch (LoginException e) {					
					return "Invalid login";
				}
				catch (Exception e) {
					Log.d("LIFERAYSERVICE", e.getMessage() );
					return "Problems Connecting to Server";
				}
				
			}

			
			@Override
			protected void onPostExecute(String result) {	
				
				if (result.length() == 0) {
					// if authenticated => go to feed activity
					Intent intent = new Intent(getApplicationContext(), DisplayNewsFeedActivity.class);
					intent.putExtra(Session.SESSION, session);
					startActivity(intent);
					
				} else {
					// if not authenticated => show error message
					TextView msg = (TextView) findViewById(R.id.message);
					msg.setText(result);
				}
				
			}
		}
	 
	

}
