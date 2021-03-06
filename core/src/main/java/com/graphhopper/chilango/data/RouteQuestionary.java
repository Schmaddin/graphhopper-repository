package com.graphhopper.chilango.data;

import java.io.Serializable;
import java.util.Date;

public class RouteQuestionary implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int routeId;
	public long time;
	public float comfortableRating;
	public float driverRating;
	public float securityRating;
	public float accessibilityRating;
	public float selfEstimationOfUser;
	public int rushHourFrequency;
	public int normalFrequency;
	public int nightFrequency;
	public int weekendFrequency;
}
