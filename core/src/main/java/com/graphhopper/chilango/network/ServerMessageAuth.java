package com.graphhopper.chilango.network;

import java.io.Serializable;

import com.graphhopper.chilango.network.ConnectionMessage.ConnectionInformation;

public class ServerMessageAuth implements Serializable {

	
	public String getUser() {
		return user;
	}


	public String getPw() {
		return pw;
	}


	public String getMail() {
		return mail;
	}


	public String getAuth() {
		return auth;
	}


	public ConnectionInformation getInformation() {
		return information;
	}


	private final String user;
	private final String pw;
	private final String mail;
	private final String auth;
	private final ConnectionInformation information;
	
	
	public ServerMessageAuth(String user, String pw, String mail, String auth, ConnectionInformation information) {
		super();
		this.user = user;
		this.pw = pw;
		this.mail = mail;
		this.auth = auth;
		this.information = information;
	}
}
