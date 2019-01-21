package com.zensky.happyhealthy;

import java.util.ArrayList;

import com.zensky.happyhealthy.wsclient.liferay.service.MessageBoardService;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;

public class DisplayNewsFeedActivity extends Activity {
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		Session session = (Session) intent
				.getSerializableExtra(Session.SESSION);

		setContentView(R.layout.activity_display_news_feed);
		
		// load text about feed messages
		new getFeedMessages().execute(session); 
		
		// load images in the list
		
		

	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_news_feed, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class getFeedMessages extends
			AsyncTask<Session, Void, PostMessage[]> {

		int progress_status;
		Session session;

		@Override
		protected void onPreExecute() {
			// update the UI immediately after the task is executed
			super.onPreExecute();

			Toast.makeText(DisplayNewsFeedActivity.this,
					"Invoke onPreExecute()", Toast.LENGTH_SHORT).show();

			progress_status = 0;
			// txt_percentage.setText("downloading 0%");

		}

		@Override
		protected PostMessage[] doInBackground(Session... params) {

			Session session = params[0];

			
			try {
				this.session = session;
				MessageBoardService msgService = new MessageBoardService(
						session);
				int maxNumberOfMessages = 20;
				int offsetInDays = 30;
				PostMessage[] msgList = msgService.getGroupPosts(
						session.getUserId(), offsetInDays, maxNumberOfMessages);
				
				Log.d("LIFERAYSERVICE", "message list size " + msgList.length);

				return msgList;
			} catch (Exception e) {
				Log.d("LIFERAYSERVICE", e.getMessage());
				return null;
			}

		}

		@Override
		protected void onPostExecute(PostMessage[] msgList) {
			
			ListView myListView = (ListView) findViewById(R.id.listview);

			AdapterMessage adapter = new AdapterMessage(
					getApplicationContext(), msgList,session);			
			myListView.setAdapter(adapter);
			
			//AdapterMessageImage imageAdapter = new AdapterMessageImage (
			//		getApplicationContext(), msgList, this.session);
			
			//myListView.setAdapter(imageAdapter);
			
			

		}

	}

}
