package com.graphhopper.chilango.network.livedata;

import java.io.Serializable;

public class LiveRide implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double getLat() {
		return lat;
	}
	public double getLon() {
		return lon;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public int getTransportId() {
		return transportId;
	}
	public float getHeading() {
		return heading;
	}
	
	public LiveRide(double lat, double lon, long timeStamp, int transportId, float heading) {
		super();
		this.lat = lat;
		this.lon = lon;
		this.timeStamp = timeStamp;
		this.transportId = transportId;
		this.heading=heading;
	}

	private final double lat;
	private final double lon;
	private final long timeStamp;
	private final int transportId;
	private final float heading;

}
