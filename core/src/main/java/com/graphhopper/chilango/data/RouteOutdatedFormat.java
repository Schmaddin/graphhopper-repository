package com.graphhopper.chilango.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.graphhopper.util.GPXEntry;

public final class RouteOutdatedFormat implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;


	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getTrailId() {
		return trailId;
	}

	public String getRecordDate() {
		return recordDate;
	}

	public String getRecordTime() {
		return recordTime;
	}

	public double getRecordDuration() {
		return recordDuration;
	}

	public String getTransportType() {
		return transportType;
	}

	public double getTarifMax() {
		return tarifMax;
	}

	public double getLongKm() {
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


	public RouteOutdatedFormat(List<GPXEntry> gpxTrace, String from, String to, String headSign, String routeName,
			String originalTrailId, int chilangoId, String recordDate, String recordTime, double recordDuration,
			String transportType, double tarifMax, double longKm, int backwardRoute, String operatorInformation,
			StopInformation stopInformation, byte trustLevel) {
		super();
		if (gpxTrace != null) {
			this.gpxEle = new double[gpxTrace.size()];
			this.gpxLat = new double[gpxTrace.size()];
			this.gpxLon = new double[gpxTrace.size()];
			this.gpxTime = new long[gpxTrace.size()];

			for (int i = 0; i < gpxTrace.size(); i++) {
				gpxEle[i] = gpxTrace.get(i).ele;
				gpxLat[i] = gpxTrace.get(i).lat;
				gpxLon[i] = gpxTrace.get(i).lon;
				gpxTime[i] = gpxTrace.get(i).getTime();
			}
		}else{
			this.gpxEle = new double[0];
			this.gpxLat = new double[0];
			this.gpxLon = new double[0];
			this.gpxTime = new long[0];
		}

		this.trailId = originalTrailId;
		this.recordDate = recordDate;
		this.recordTime = recordTime;
		this.recordDuration = recordDuration;
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
		this.osmEdges = new LinkedList<>();
		this.backWardRoute = backwardRoute;
		this.operatorInformation = operatorInformation;
		this.stopInformation = stopInformation;
		this.trustLevel = trustLevel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public double[] getGpxEle() {
		return gpxEle;
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
	private final String trailId;
	private final String recordDate;
	private final String recordTime;
	private final double recordDuration;
	private final String transportType;
	private final double tarifMax;
	private final double longKm;
	private final double gpxEle[];
	private final double gpxLat[];
	private final double gpxLon[];
	private final long gpxTime[];
	private final int chilangoId;
	private final String routeName;
	private final String headSign;
	private final String operatorInformation;
	private final RouteTimeBound timeBound;
	private final RouteFrequency frequency;
	private final List<Integer> osmEdges;
	private final int backWardRoute;
	private final byte trustLevel;

	public byte getTrustLevel() {
		return trustLevel;
	}

	public String getOperatorInformation() {
		return operatorInformation;
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

	public String toCSVString() {
		String edgesString = "";
		for (Integer osm : osmEdges) {
			edgesString += "->" + osm;
		}

		String stopInformationString="null";
		if(stopInformation!=null)
			stopInformationString=stopInformation.toCSV();
		
		return trailId + "," + chilangoId + "," + from + "," + to + "," + headSign + "," + routeName + ","
				+ operatorInformation + "," + recordDate + "," + recordTime + "," + recordDuration + "," + transportType
				+ "," + tarifMax + "," + longKm + "," + backWardRoute + ","+stopInformation+","+trustLevel+ "," + edgesString;
	}

	public static String toCSVHeader() {
		return "trailId" + "," + "chilangoId" + "," + "from," + "to," + "headSign," + "routeName" + ","
				+ "operatorInformation" + "," + "recordDate" + "," + "recordTime" + "," + "recordDuration" + ","
				+ "transportType" + "," + "tarifMax" + "," + "longKm" + ",backwardRoute"+",stopInformation" +",trustLevel"+ ",edges";
	}

	@Override
	public int hashCode() {
		return from.hashCode() + to.hashCode() + trailId.hashCode() + recordDate.hashCode() + recordTime.hashCode()
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

		RouteOutdatedFormat toCompare = (RouteOutdatedFormat) other;

		if (toCompare.getFrom().equals(this.getFrom()) && toCompare.getTo().equals(this.getTo())
				&& toCompare.getRecordDate().equals(this.getRecordDate()))
			return true;

		return false;

	}

	public List<GPXEntry> getGpxTrace() {
		List<GPXEntry> trace = new LinkedList<>();
		for (int i = 0; i < gpxEle.length; i++) {
			trace.add(new GPXEntry(gpxLat[i], gpxLon[i], gpxEle[i], gpxTime[i]));
		}
		return trace;
	}

	public List<GPXEntry> getReversedGpxTrace() {
		List<GPXEntry> trace = new LinkedList<>();
		for (int i = gpxEle.length - 1; i >= 0; i--) {
			trace.add(new GPXEntry(gpxLat[i], gpxLon[i], gpxEle[i], gpxTime[(gpxEle.length - 1) - i]));
		}
		return trace;
	}

	public int getId() {

		return chilangoId;
	}

	public RouteTimeBound getTimeBound() {
		return timeBound;
	}

	public RouteFrequency getFrequency() {
		return frequency;
	}

	public List<Integer> getOsmEdges() {
		return osmEdges;
	}

	public StopInformation getStopInformation() {
		return stopInformation;
	}
}
