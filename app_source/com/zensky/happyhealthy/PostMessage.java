package com.zensky.happyhealthy;


import java.util.Date;

public class PostMessage{
	
	private int postMessageId;
	private String userName;
	private int userId;
	private Date postMessageDate;
	private String subject;
	private String message;
	private boolean includeImage;
	

	public int getPostMessageId() {
		return postMessageId;
	}
	public void setPostMessageId(int postMessageId) {
		this.postMessageId = postMessageId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getPostMessageDate() {
		return postMessageDate;
	}
	public void setPostMessageDate(Date postMessageDate) {
		this.postMessageDate = postMessageDate;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isIncludeImage() {
		return includeImage;
	}
	public void setIncludeImage(boolean includeImage) {
		this.includeImage = includeImage;
	}
	
}