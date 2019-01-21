package com.zensky.happyhealthy;

import java.io.Serializable;

public class Session implements Serializable {
	
	protected String password;
	protected String username;
	protected int userId;
	
	private static final long serialVersionUID = 1L;
	public final static String SESSION = "com.zensky.happyhealthy.session";

	public Session (String userName, String password) {
		this.username = userName;
		this.password = password;
	}
	public String getPassword() {
		return password;
	}



	public String getUsername() {
		return username;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public void setUsername(String username) {
		this.username = username;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}





}