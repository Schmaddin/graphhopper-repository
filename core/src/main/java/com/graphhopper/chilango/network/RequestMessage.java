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

	public RequestMessage(RequestType type, Serializable information) {
		super();
		this.type = type.getValue();
		if (information != null)
			this.info = JsonHelper.createJsonFromObjectAndroid(information);
		else
			this.info = null;
	}

	private final int type;

	private final String info;

}
