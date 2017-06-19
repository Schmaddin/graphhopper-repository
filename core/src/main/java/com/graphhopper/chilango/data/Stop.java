package com.graphhopper.chilango.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Stop implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Stop(String name, int type, double lat, double lon, String otherInformation) {
		super();
		this.name = name;
		this.type = type;
		this.lat = lat;
		this.lon = lon;
		this.otherInformation = otherInformation;
		routes=new LinkedList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public int getType() {
		return type;
	}


	public double getLat() {
		return lat;
	}


	public double getLon() {
		return lon;
	}


	public String getOtherInformation() {
		return otherInformation;
	}

	
	public List<Integer> getRoutes(){
		return routes;
	}

	private final String name;
	private final int type;
	private final double lat;
	private final double lon;
	private final String otherInformation;
	private final List<Integer> routes;

	@Override
	public int hashCode(){
		return (int)(lat*100000)+(int)(lon*100000)+name.hashCode()+type;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof Stop))
			return false;

		Stop otherStop=(Stop)other;
		
		if(!otherStop.getName().equals(name))
			return false;
		
		if(otherStop.getType()!=otherStop.type)
			return false;
		
		if(otherStop.getLat()!=lat)
			return false;
		
		if(otherStop.getLon()!=lon)
			return false;
		
		return true;
	}
}
