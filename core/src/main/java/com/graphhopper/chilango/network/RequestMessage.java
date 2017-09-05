package com.graphhopper.chilango.network;

import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.graphhopper.chilango.data.JsonHelper;

public class RequestMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RequestType getType() {

		return RequestType.fromValue(type);
	}

	public String getInformation() {
		return info;
	}

	public RequestMessage(RequestType type, Serializable information,boolean android) {
		super();
		this.type = type.getValue();
		if (information != null){
			if(android)
			this.info = JsonHelper.createJsonFromObjectAndroid(information);
			else
			this.info = JsonHelper.createJsonFromObject(information);
		}
		else
			this.info = null;
	}
	
	public RequestMessage(RequestType type, Serializable information) {
		this(type,information,true);
	}
	

	private final int type;

	private final String info;

}
