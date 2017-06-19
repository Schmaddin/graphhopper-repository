package com.graphhopper.chilango.data;

import java.io.Serializable;
import java.util.List;

public class BusBase implements Serializable{
	

	private final double lat;
	private final double lon;
	private final String baseName;
	private final String information;
	private final byte trust; 
	private final int[] leavingRoutes;
	private final int[] incomingRoutes;
	private final float importance;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BusBase(double lat, double lon, String baseName, String information, byte trust, int[] leavingRoutes,
			int[] incomingRoutes,float importance) {
		super();
		this.lat = lat;
		this.lon = lon;
		this.baseName = baseName;
		this.information = information;
		this.trust = trust;
		this.leavingRoutes = leavingRoutes;
		this.incomingRoutes = incomingRoutes;
		this.importance = importance;
	}
	
	
	public float getImportance() {
		return importance;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public double getLat() {
		return lat;
	}
	public double getLon() {
		return lon;
	}
	public String getBaseName() {
		return baseName;
	}
	public String getInformation() {
		return information;
	}
	public byte getTrust() {
		return trust;
	}
	public int[] getLeavingRoutes() {
		return leavingRoutes;
	}
	public int[] getIncomingRoutes() {
		return incomingRoutes;
	}



}
