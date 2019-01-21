package com.zensky.happyhealthy.wsclient.liferay.service;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Log;

import com.zensky.happyhealthy.PostMessage;
import com.zensky.happyhealthy.Session;
import com.zensky.happyhealthy.wsclient.liferay.*;

public class MessageBoardService extends BaseService {

	public MessageBoardService(Session session) {
		super(session);
	}
	
	public Bitmap getImage (int messageId) throws Exception {
		

		// get document Id
		/*int groupId = LiferayServerInfo.GROUP_ID;
		int folderId = LiferayServerInfo.PUBLIC_FOLDER_ID;// placing images in the root folder
		String title = messageId + "";
		JSONObject fileEntry = this.getFileEntry(groupId, folderId, title);
		
		// get document as a stream using the documentId + version = 0
		int documentId = fileEntry.getInt("fileEntryId");
		Log.d("LIFERAYSERVICE" , "documentId: " + documentId);*/
		
		// the JSON get-file-as-stream is not working
		// we will get the file with a direct HTTP request with no authentication.. 
		// images will have the permission of guest 

		String imageURL = LiferayServerInfo.SERVER_ADDRESS + "/documents/" + 
		LiferayServerInfo.GROUP_ID + "/" +LiferayServerInfo.PUBLIC_FOLDER_ID + "/" + messageId;
		Log.d("LIFERAYSERVICE" , imageURL);
		Bitmap image = LiferayJsonClientUtil.getBitmapFromURL(imageURL);
		
		
		return image;
	}
	
	public PostMessage[] getGroupPosts( long userId, int offsetInDays, int max) throws Exception {
		
		int groupId = LiferayServerInfo.GROUP_ID;
		int status = 0;
		int start = 0;
		int end = max - 1;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -offsetInDays);
		long modifiedDate = calendar.getTimeInMillis();
		
		JSONArray threadList = this.getGroupThreads(groupId, userId, modifiedDate, status, start, end);
		int[] mainMsgIdList = this.getListOfMessageIds(threadList);
		JSONArray mainMsgList = this.getMessages(mainMsgIdList);
		
		PostMessage[] result = new PostMessage[mainMsgList.length()];
		for (int i = 0; i < mainMsgList.length(); i++) {
			JSONObject message = mainMsgList.getJSONObject(i);
			PostMessage post = new PostMessage();
			post.setPostMessageId(mainMsgIdList[i]);
			post.setUserName(message.getString("userName"));
			post.setUserId(message.getInt("userId"));
			post.setSubject(message.getString("subject"));
			post.setMessage(message.getString("body"));
			long date = message.getLong("createDate");
			post.setPostMessageDate(new Date(date));
			post.setIncludeImage(message.getBoolean("attachments"));
			
		
			result[i] = post;
			
		}
		
		
		return result;
	}
	
	
	private int[] getListOfMessageIds(JSONArray threadList) throws Exception{
		
		int[] msgId = new int[threadList.length()];
	
		for (int i = 0; i < threadList.length(); i++) {
			JSONObject thread = threadList.getJSONObject(i);
			int rootMsgId = thread.getInt("rootMessageId");			
			msgId[i] = rootMsgId;
			
			  
			}   
		return msgId;	
		
	}
	
	// ================================================
	// JSON calls
	//=======================================================
	
	private JSONArray getMessages(int[] messagesIds) throws Exception {
		
		JSONArray commands = new JSONArray();

		for (int i = 0; i < messagesIds.length; i++) {
			JSONObject _params = new JSONObject();
			_params.put("messageId" , messagesIds[i]);
			JSONObject _command = new JSONObject();
			_command.put("/mbmessage/get-message", _params);
			commands.put(_command);			
		}
		JSONArray jsonResponse = LiferayJsonClientUtil.makeLiferayJSONArrayInvokerCall(session, commands);
		
		return jsonResponse;
	}
	
	private JSONArray getGroupThreads(long groupId, long userId, long modifiedDate, int status, int start, int end) throws Exception {
		JSONObject _command = new JSONObject();

		try {
			JSONObject _params = new JSONObject();

			_params.put("groupId", groupId);
			_params.put("userId", userId);
			_params.put("modifiedDate", modifiedDate);
			_params.put("status", status);
			_params.put("start", start);
			_params.put("end", end);

			_command.put("/mbthread/get-group-threads", _params);
		}
		catch (JSONException _je) {
			throw new Exception(_je);
		}

		// the result is an array of threads
		JSONArray resultObject = (JSONArray) LiferayJsonClientUtil.makeLiferayJSONInvokerCall(session, _command);
		return resultObject;
	}

	public JSONObject getFileEntry(long groupId, long folderId, String title) throws Exception {
		JSONObject _command = new JSONObject();

		try {
			JSONObject _params = new JSONObject();

			_params.put("groupId", groupId);
			_params.put("folderId", folderId);
			_params.put("title", title);

			_command.put("/dlapp/get-file-entry", _params);
		}
		catch (JSONException _je) {
			throw new Exception(_je);
		}

		JSONObject resultObject = (JSONObject) LiferayJsonClientUtil.makeLiferayJSONInvokerCall(session, _command);
		
		return resultObject;
	}
	
	public java.io.InputStream  getFileAsStream(long groupId, long folderId, String title) throws Exception {
		JSONObject _command = new JSONObject();

		try {
			JSONObject _params = new JSONObject();

			_params.put("groupId", groupId);
			_params.put("folderId", folderId);
			_params.put("title", title);

			_command.put("/dlfileentry/get-file-entry", _params);
		}
		catch (JSONException _je) {
			throw new Exception(_je);
		}

		//JSONObject resultObject = (JSONObject) LiferayJsonClientUtil.makeLiferayJSONInvokerCall(session, _command);
		// the json call for 6.1 EE GA2 is returning null for files :(
		return null;
	}


	
}