package com.graphhopper.chilango.network.livedata;

import java.io.Serializable;

public class LiveRideUser extends LiveRide implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public LiveRideUser(double lat, double lon, long timeStamp, int transportId, float heading, int userId, boolean game) {
		super(lat, lon, timeStamp, transportId, heading);
		this.userId = userId;
		this.game = game;
	}
	
	public int getUserId() {
		return userId;
	}
	public boolean isGame() {
		return game;
	}

	private final int userId;
	private final boolean game;
	
}
