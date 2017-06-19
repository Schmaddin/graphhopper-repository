package com.graphhopper.chilango.data;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	
	/*
	public void setByOther(RouteTimeBound timeBound)
	{
		this.startHour=timeBound.startHour;
		this.startMinute=timeBound.startMinute;
		this.lastHour=timeBound.lastHour;
		this.lastMinute=timeBound.lastMinute;
		this.trustLevel=timeBound.trustLevel;
	}*/
	

	

}
