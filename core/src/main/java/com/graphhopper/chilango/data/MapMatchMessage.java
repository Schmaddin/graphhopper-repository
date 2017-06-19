package com.graphhopper.chilango.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.graphhopper.chilango.data.gps.GPSPoint;
import com.graphhopper.util.GPXEntry;

public class MapMatchMessage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int getMessageType() {
		return message;
	}

	public Map<Long,GPSPoint> getGpsList() {
		return gpsList;
	}

	private final int message;
	
	private final Map<Long,GPSPoint> gpsList;
	
	/**
	 * 2 for closing
	 * @param gpsList
	 */
	public MapMatchMessage(Map<Long, GPSPoint> gpsList)
	{
		this.message=1;
		this.gpsList=gpsList;
	}
	
	public MapMatchMessage(int message)
	{
		this.message=message;
		gpsList=new TreeMap<>();
	}

}
