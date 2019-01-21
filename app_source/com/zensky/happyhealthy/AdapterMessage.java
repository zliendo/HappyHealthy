package com.zensky.happyhealthy;

import java.text.SimpleDateFormat;
import java.util.Locale;


import com.zensky.happyhealthy.wsclient.liferay.service.MessageBoardService;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterMessage extends ArrayAdapter<PostMessage> {
	
	
	private final PostMessage[] values;
	private LayoutInflater inflater;
	private Session session;

	
	  public AdapterMessage(Context context, PostMessage[] values, Session session) {
		    super(context, R.layout.display_msg_item, values);
		    this.values = values;
		    this.inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    this.session = session;
		  }
	  
		private static class ParamHolder {
			ImageView imageView;
			TextView userNameView;
			TextView msgDateView;
			TextView subjectView;
			TextView bodyView;
			
			Session session;
			Bitmap imageBitmap;
			int messageId;
			boolean downloaded;
		}
	  
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		  
		View rowView = convertView;
		ParamHolder params;
		
		if (rowView == null) {
		  
	    rowView = inflater.inflate(R.layout.display_msg_item, parent, false);
	    //getting elements
	    params = new ParamHolder();

	    params.userNameView = (TextView) rowView.findViewById(R.id.message_username);
	    params.msgDateView = (TextView) rowView.findViewById(R.id.message_date);	    
	    params.subjectView = (TextView) rowView.findViewById(R.id.message_subject);
	    params.bodyView = (TextView) rowView.findViewById(R.id.message_body);	
	    params.imageView = (ImageView) rowView.findViewById(R.id.message_image);
	    
		params.session = this.session;
		int messageId = values[position].getPostMessageId();
		params.messageId = messageId;
		params.downloaded = false;
		
	    rowView.setTag(params);
	    
		} else {
			params = (ParamHolder) rowView.getTag();
		}
		
	    	    
		params.userNameView.setText(values[position].getUserName());
	    String formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(values[position].getPostMessageDate());
	    params.msgDateView.setText(formattedDate);
	    params.subjectView.setText(values[position].getSubject());
	    params.bodyView.setText(values[position].getMessage());
	    
	 // download image only when it is not downloaded already
	    // and send the request to an async task
	 		if (!params.downloaded) {
	 			params.downloaded = true;
	 			new DownloadMsgImagesAsyncTask().execute(params);
	 			 Log.d("LIFERAYSERVICE" , "loading image: " + position);
	 		}
	    
	    Log.d("LIFERAYSERVICE" , "loading position: " + position);
	    


	    return rowView;
	  }
	  
	  private class DownloadMsgImagesAsyncTask extends
		AsyncTask<ParamHolder, Void, ParamHolder> {

	@Override
	protected ParamHolder doInBackground(ParamHolder... params) {
		try {

			int messageId = params[0].messageId;
			Log.d("LIFERAYSERVICE", "about to download " 
					+ " messageId: " + messageId);
			MessageBoardService service = new MessageBoardService(
					params[0].session);
			Bitmap image = service.getImage(messageId);
			params[0].imageBitmap = image;

		} catch (Exception e) {

			Log.d("LIFERAYSERVICE",
					" background download image " + e.getMessage());
			return null;
		}
		return params[0];

	}

	@Override
	protected void onPostExecute(ParamHolder image) {
		Log.d("LIFERAYSERVICE",
				" got image " );
		if (image != null && image.imageBitmap != null) {
			image.imageView.setImageBitmap(image.imageBitmap);
		}

	}

}



}
