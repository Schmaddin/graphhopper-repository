package com.graphhopper.chilango.network;

import java.io.Serializable;

public class RequestMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RequestType getType() {
		return type;
	}
	public Serializable getInformation() {
		return information;
	}
	public RequestMessage(RequestType type, Serializable information) {
		super();
		this.type = type;
		this.information = information;
	}
	
	private final RequestType type;
	
	private final Serializable information;
	
}
