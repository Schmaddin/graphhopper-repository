package com.graphhopper.chilango.network;

import java.io.Serializable;

public enum RequestType implements Serializable {
	close(-1),whenLastUpdate(0),SubmitTask(50),SubmitChange(100),UserData(150),TaskAvailable(200),PutLiveGPS(220),GetLiveGPS(221),TransactionConfrimation(500);
	
	private final int value;
	RequestType(int value){
		this.value=value;
	}
	
	public int getValue() {
		return value;
	}
}
