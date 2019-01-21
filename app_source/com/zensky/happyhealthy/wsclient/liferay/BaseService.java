package com.zensky.happyhealthy.wsclient.liferay;

import com.zensky.happyhealthy.Session;

public class BaseService {

	public BaseService(Session session) {
		this.session = session;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	protected Session session;

}