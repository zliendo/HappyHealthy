package com.zensky.happyhealthy.wsclient.liferay.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.zensky.happyhealthy.Session;
import com.zensky.happyhealthy.User;
import com.zensky.happyhealthy.wsclient.liferay.*;

public class UserService extends BaseService {

	public UserService(Session session) {
		super(session);
	}
	
	public  User getUserInfo (String screenName) throws Exception {
		JSONObject user = this.getLiferayUserInfo(screenName);
		
	    User userInfo = new User();
	    userInfo.setUsername(screenName);
	    userInfo.setFirstName(user.getString("firstName"));
	    userInfo.setUserId(user.getInt("userId"));
		return userInfo;
	}
	
	public JSONObject getLiferayUserInfo( String screenName)
			throws Exception {
		// making the JSON URL to Liferay getUserByScreenName
		JSONObject _command = new JSONObject();
		JSONObject _params = new JSONObject();

		try {
			_params.put("companyId", LiferayServerInfo.COMPANY_ID);
			_params.put("screenName", screenName);

			_command.put("/user/get-user-by-screen-name", _params);
		} catch (JSONException _je) {
			throw new Exception(_je);
		}

		// Making the call to Liferay by sending JSON info
		JSONObject resultObject = (JSONObject) LiferayJsonClientUtil.makeLiferayJSONInvokerCall(session, _command);
		
		return resultObject;

	}

}
