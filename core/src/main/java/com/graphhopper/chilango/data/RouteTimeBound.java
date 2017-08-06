package com.graphhopper.chilango.data;

import java.io.Serializable;


public class RouteTimeBound implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public byte[] startHour=new byte[7];
	public byte[] startMinute=new byte[7];
	public byte[] lastHour=new byte[7];
	public byte[] lastMinute=new byte[7];
	public byte trustLevel;

	public RouteTimeBound(){
		
	}

}
