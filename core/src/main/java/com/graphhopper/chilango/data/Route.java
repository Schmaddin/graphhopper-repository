package com.graphhopper.chilango.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.graphhopper.util.GPXEntry;

public final class Route implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	private int color;



	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getOrigin() {
		return origin;
	}

	public String getRecordDate() {
		return recordDate;
	}

	public String getRecordTime() {
		return recordTime;
	}

	public float getDuration() {
		return duration;
	}

	public int getTransportType() {
		return transportType;
	}

	public float getTarifMax() {
		return tarifMax;
	}

	public float getLongKm() {
		return longKm;
	}

	/*
	 * public MapatonRoute(List<GPXEntry> gpxTrace, String from, String to,
	 * String headSign, String routeName, String originalTrailId, int
	 * chilangoId, String recordDate, String recordTime, double recordDuration,
	 * String vehicleTipo, double tarifMax, double longKm, boolean virtualRoute,
	 * boolean addByType, RouteTimeBound timeBound, RouteFrequency frequency) {
	 * 
	 * this(gpxTrace, from, to, headSign, routeName, originalTrailId,
	 * chilangoId, recordDate, recordTime, recordDuration, vehicleTipo,
	 * tarifMax, longKm, virtualRoute);
	 * 
	 * this.timeBound.setByOther(timeBound);
	 * this.frequency.setByOther(frequency);
	 * 
	 * }
	 */


	public Route(List<GPXEntry> gpxTrace, String from, String to, String headSign, String routeName,
			String origin, int chilangoId, String recordDate, String recordTime, float duration,
			int transportType, float tarifMax, float longKm, int backwardRoute, int operatorId, String extraInformation,
			StopInformation stopInformation, byte trustLevel,int color,long lastEdit) {
		super();
		if (gpxTrace != null) {
			this.gpxLat = new double[gpxTrace.size()];
			this.gpxLon = new double[gpxTrace.size()];
			this.gpxTime = new long[gpxTrace.size()];

			for (int i = 0; i < gpxTrace.size(); i++) {
				gpxLat[i] = gpxTrace.get(i).lat;
				gpxLon[i] = gpxTrace.get(i).lon;
				gpxTime[i] = gpxTrace.get(i).getTime();
			}
		}else{
			this.gpxLat = new double[0];
			this.gpxLon = new double[0];
			this.gpxTime = new long[0];
		}

		this.origin = origin;
		this.recordDate = recordDate;
		this.recordTime = recordTime;
		this.duration = duration;
		this.transportType = transportType;
		this.tarifMax = tarifMax;
		this.longKm = longKm;
		this.from = from;
		this.to = to;
		this.chilangoId = chilangoId;
		this.routeName = routeName;
		this.headSign = headSign;
		this.frequency = new RouteFrequency();
		this.timeBound = new RouteTimeBound();
		this.backWardRoute = backwardRoute;
		this.extraInformation = extraInformation;
		this.stopInformation = stopInformation;
		this.trustLevel = trustLevel;
		this.operatorId = operatorId;
		this.lastEdit = lastEdit;
		this.color=color;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public double[] getGpxLat() {
		return gpxLat;
	}

	public double[] getGpxLon() {
		return gpxLon;
	}

	public long[] getGpxTime() {
		return gpxTime;
	}

	public int getChilangoId() {
		return chilangoId;
	}

	private final String from;
	private final String to;
	private final String origin;
	private final String recordDate;
	private final String recordTime;
	private final float duration;
	private final int transportType;
	private final float tarifMax;
	private final float longKm;
	private final double gpxLat[];
	private final double gpxLon[];
	private final long gpxTime[];
	private final int chilangoId;
	private final String routeName;
	private final String headSign;
	private final String extraInformation;
	private final int operatorId;
	private final RouteTimeBound timeBound;
	private final RouteFrequency frequency;
	private final int backWardRoute;
	private final byte trustLevel;
	private final long lastEdit;

	public byte getTrustLevel() {
		return trustLevel;
	}

	public String getExtraInformation() {
		return extraInformation;
	}

	public int getBackWardRoute() {
		return backWardRoute;
	}

	private final StopInformation stopInformation;

	public String getRouteName() {
		return routeName;
	}

	public String getHeadSign() {
		return headSign;
	}

	public String toCSVString() {/*
		String edgesString = "";
		for (Integer osm : osmEdges) {
			edgesString += "->" + osm;
		}*/

		String stopInformationString="null";
		if(stopInformation!=null)
			stopInformationString=stopInformation.toCSV();
		
		return origin + "," + chilangoId + "," + from + "," + to + "," + headSign + "," + routeName + ","+operatorId+","+
				 extraInformation + "," + recordDate + "," + recordTime + "," + duration + "," + transportType
				+ "," + tarifMax + "," + longKm + "," + backWardRoute + ","+stopInformation+","+trustLevel;
	}

	public static String toCSVHeader() {
		return "trailId" + "," + "chilangoId" + "," + "from," + "to," + "headSign," + "routeName" + ","
				+ "operatorId,"+ "operatorInformation" + "," + "recordDate" + "," + "recordTime" + "," + "recordDuration" + ","
				+ "transportType" + "," + "tarifMax" + "," + "longKm" + ",backwardRoute"+",stopInformation" +",trustLevel";
	}

	@Override
	public int hashCode() {
		return from.hashCode() + to.hashCode() + origin.hashCode() + recordDate.hashCode() + recordTime.hashCode()
				+ chilangoId;
	}

	@Override
	public boolean equals(Object other) {

		if (other == null)
			return false;

		if (other == this)
			return true;

		if (other.getClass() != getClass())
			return false;

		Route toCompare = (Route) other;

		if (toCompare.getFrom().equals(this.getFrom()) && toCompare.getTo().equals(this.getTo())
				&& toCompare.getRecordDate().equals(this.getRecordDate()))
			return true;

		return false;

	}

	public List<GPXEntry> getGpxTrace() {
		List<GPXEntry> trace = new LinkedList<>();
		for (int i = 0; i < gpxLon.length; i++) {
			trace.add(new GPXEntry(gpxLat[i], gpxLon[i], 0.0, gpxTime[i]));
		}
		return trace;
	}

	public List<GPXEntry> getReversedGpxTrace() {
		List<GPXEntry> trace = new LinkedList<>();
		for (int i = gpxLon.length - 1; i >= 0; i--) {
			trace.add(new GPXEntry(gpxLat[i], gpxLon[i], 0.0, gpxTime[(gpxLon.length - 1) - i]));
		}
		return trace;
	}

	public int getId() {

		return chilangoId;
	}

	public int getOperatorId() {
		return operatorId;
	}

	public RouteTimeBound getTimeBound() {
		return timeBound;
	}

	public RouteFrequency getFrequency() {
		return frequency;
	}

	public StopInformation getStopInformation() {
		return stopInformation;
	}

	public long getLastEdit() {
		return lastEdit;
	}
}
