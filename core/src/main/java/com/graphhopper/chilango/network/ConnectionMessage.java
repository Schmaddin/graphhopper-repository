package com.graphhopper.chilango.network;

import java.io.Serializable;

public class ConnectionMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum ConnectionInformation implements Serializable{
		WRONG_LOGIN,VERFIY_MAIL,EXPIRED_TOKEN,WRONG_TOKEN,LOGIN_OK,ERROR,CORRECT_TOKEN, CREATE_USER, LOG_IN, LOG_IN_TOKEN, CLOSE
	}
	
	public ConnectionMessage(ConnectionInformation infoConnection, String additionalField) {
		super();
		this.infoConnection = infoConnection;
		this.additionalField = additionalField;
	}
	
	public ConnectionMessage(ConnectionInformation infoConnection) {
		super();
		this.infoConnection = infoConnection;
		this.additionalField = "";
	}
	
	public ConnectionInformation getInfoConnection() {
		return infoConnection;
	}
	
	public String getAdditionalField() {
		return additionalField;
	}
	
	private final ConnectionInformation infoConnection;
	private final String additionalField;
	
	
	
}
