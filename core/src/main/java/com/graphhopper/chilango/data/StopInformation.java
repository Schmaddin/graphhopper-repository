package com.graphhopper.chilango.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class StopInformation implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public StopInformation(double[] latStop, double[] lonStop, String[] stopName, long[] time, short[] timeAtStop) {
		super();
		this.latStop = latStop;
		this.lonStop = lonStop;
		this.stopName = stopName;
		this.time = time;
		this.timeAtStop = timeAtStop;
	}
	
	
	public double[] getLatStop() {
		return latStop;
	}
	public double[] getLonStop() {
		return lonStop;
	}
	public String[] getStopName() {
		return stopName;
	}
	public long[] getTime() {
		return time;
	}
	public short[] getTimeAtStop() {
		return timeAtStop;
	}


	final double latStop[];
	final double lonStop[];
	final String stopName[];
	final long time[];
	final short timeAtStop[];
	public String toCSV() {
		String total="";
		if(stopName.length>0)
		{
			for(int i=0;i<stopName.length;i++)
			{
				if(!stopName[i].equals(""))
				total+=stopName[i];
				else
				total+=latStop[i]+" "+lonStop[i];
				
				total+="#";
			}
		}
		return total;
	}
}
