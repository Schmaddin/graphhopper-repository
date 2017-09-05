package com.graphhopper.chilango.network.livedata;

import java.io.Serializable;

public class LiveRideUser extends LiveRide implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LiveRideUser(double lat, double lon, long timeStamp, int transportId, float heading,float full, String userId
			,int rat,boolean game) {
		super(lat, lon, timeStamp, transportId, heading,full,rat);
		this.userId = userId;
		this.game = game;
	}

	public String getUserId() {
		return userId;
	}

	public boolean isGame() {
		return game;
	}

	private final String userId;
	private final boolean game;

}
