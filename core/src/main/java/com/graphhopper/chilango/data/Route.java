package com.graphhopper.chilango.data;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.graphhopper.chilango.FileHelper;
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

	public long getRecordTime() {
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

	public Route(double lat[], double[] lon, String from, String to, String headSign, String routeName, String origin,
			int chilangoId, long recordTime, float duration, int transportType, float tarifMax, float longKm,
			int backwardRoute, int operatorId, String extraInformation, StopInformation stopInformation,
			byte trustLevel, int color, long lastEdit) {
		this(lat, lon, new long[lat.length], from, to, headSign, routeName, origin, chilangoId, recordTime, duration,
				transportType, tarifMax, longKm, backwardRoute, operatorId, extraInformation, stopInformation,
				trustLevel, color, lastEdit);

	}

	public Route(Route route, int routeId) {
		this(route, routeId, route.getTrustLevel(), route.getLastEdit(), route.getOrigin());
	}
	
	public Route(Route route, byte trustLevel, long lastEdit) {
		this(route, route.getChilangoId(), trustLevel, lastEdit, route.getOrigin());
	}

	public Route(Route route, int routeId, byte trustLevel, long lastEdit, String origin) {
		this.lat = route.getLat();
		this.lon = route.getLon();
		this.timeStamp = route.getTimeStamp();
		this.origin = origin;
		this.recordTime = route.getRecordTime();
		this.duration = route.getDuration();
		this.transportType = route.getTransportType();
		this.tarifMax = route.getTarifMax();
		this.longKm = route.getLongKm();
		this.from = route.getFrom();
		this.to = route.getTo();
		this.chilangoId = route.getChilangoId();
		this.routeName = route.getRouteName();
		this.headSign = route.getHeadSign();
		this.frequency = route.getFrequency();
		this.timeBound = route.getTimeBound();
		this.backWardRoute = route.getBackWardRoute();
		this.extraInformation = route.getExtraInformation();
		this.stopInformation = route.getStopInformation();
		this.trustLevel = trustLevel;
		this.operatorId = route.getOperatorId();
		this.lastEdit = lastEdit;
		this.color = route.getColor();
	}

	public Route(double lat[], double[] lon, long[] timeStamp, String from, String to, String headSign,
			String routeName, String origin, int chilangoId, long recordTime, float duration, int transportType,
			float tarifMax, float longKm, int backwardRoute, int operatorId, String extraInformation,
			StopInformation stopInformation, byte trustLevel, int color, long lastEdit) {
		super();

		this.lat = lat;
		this.lon = lon;
		this.timeStamp = timeStamp;
		this.origin = origin;
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
		this.color = color;
	}

	public Route(List<GPXEntry> gpxTrace, String from, String to, String headSign, String routeName, String origin,
			int chilangoId, long recordTime, float duration, int transportType, float tarifMax, float longKm,
			int backwardRoute, int operatorId, String extraInformation, StopInformation stopInformation,
			byte trustLevel, int color, long lastEdit) {

		this(gpxTrace == null ? new double[0] : new double[gpxTrace.size()],
				gpxTrace == null ? new double[0] : new double[gpxTrace.size()],
				gpxTrace == null ? new long[0] : new long[gpxTrace.size()], from, to, headSign, routeName, origin,
				chilangoId, recordTime, duration, transportType, tarifMax, longKm, backwardRoute, operatorId,
				extraInformation, stopInformation, trustLevel, color, lastEdit);

		if (gpxTrace != null) {
			for (int i = 0; i < gpxTrace.size(); i++) {
				lat[i] = gpxTrace.get(i).lat;
				lon[i] = gpxTrace.get(i).lon;
				timeStamp[i] = gpxTrace.get(i).getTime();
			}
		}

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

	public double[] getLat() {
		return lat;
	}

	public double[] getLon() {
		return lon;
	}

	public long[] getTimeStamp() {
		return timeStamp;
	}

	public int getChilangoId() {
		return chilangoId;
	}

	private final String from;
	private final String to;
	private final String origin;
	private final long recordTime;
	private final float duration;
	private final int transportType;
	private final float tarifMax;
	private final float longKm;
	private final double lat[];
	private final double lon[];
	private final long timeStamp[];
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
									 * String edgesString = ""; for (Integer osm
									 * : osmEdges) { edgesString += "->" + osm;
									 * }
									 */

		String stopInformationString = "null";
		if (stopInformation != null)
			stopInformationString = stopInformation.toCSV();

		return origin + "," + chilangoId + "," + from + "," + to + "," + headSign + "," + routeName + "," + operatorId
				+ "," + extraInformation + "," + FileHelper.df.format(new Date(recordTime)) + "," + duration + ","
				+ TransportType.getTypeByValue(transportType) + "," + tarifMax + "," + longKm + "," + backWardRoute
				+ "," + stopInformation + "," + trustLevel;
	}

	public static String toCSVHeader() {
		return "origin" + "," + "chilangoId" + "," + "from," + "to," + "headSign," + "routeName" + "," + "operatorId,"
				+ "extraInformation" + "," + "recordTime" + "," + "recordDuration" + "," + "transportType" + ","
				+ "tarifMax" + "," + "longKm" + ",backwardRoute" + ",stopInformation" + ",trustLevel";
	}

	@Override
	public int hashCode() {
		return from.hashCode() + to.hashCode() + origin.hashCode() + (int) recordTime + chilangoId;
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
				&& toCompare.getRecordTime() == this.getRecordTime() && this.getLastEdit() == toCompare.getLastEdit())
			return true;

		return false;

	}

	public List<GPXEntry> getGpxTrace() {
		List<GPXEntry> trace = new LinkedList<>();
		for (int i = 0; i < lon.length; i++) {
			trace.add(new GPXEntry(lat[i], lon[i], 0.0, timeStamp[i]));
		}
		return trace;
	}

	public List<GPXEntry> getReversedGpxTrace() {
		List<GPXEntry> trace = new LinkedList<>();
		for (int i = lon.length - 1; i >= 0; i--) {
			trace.add(new GPXEntry(lat[i], lon[i], 0.0, timeStamp[(lon.length - 1) - i]));
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

	public Route(Route route, String nameConvention, Object value) {
		if (nameConvention.equals("lat"))
			this.lat = (double[]) value;
		else
			this.lat = route.getLat();

		if (nameConvention.equals("lon"))
			this.lon = (double[]) value;
		else
			this.lon = route.getLon();

		if (nameConvention.equals("timeStamp"))
			this.timeStamp = (long[]) value;
		else
			this.timeStamp = route.getTimeStamp();

		if (nameConvention.equals("origin"))
			this.origin = (String) value;
		else
			this.origin = route.getOrigin();

		if (nameConvention.equals("recordTime"))
			this.recordTime = (long) value;
		else
			this.recordTime = route.getRecordTime();

		if (nameConvention.equals("duration"))
			this.duration = (float) value;
		else
			this.duration = route.getDuration();

		if (nameConvention.equals("transportType"))
			this.transportType = (int) value;
		else
			this.transportType = route.getTransportType();

		if (nameConvention.equals("tarifMax"))
			this.tarifMax = (float) value;
		else
			this.tarifMax = route.getTarifMax();

		if (nameConvention.equals("longKm"))
			this.longKm = (float) value;
		else
			this.longKm = route.getLongKm();

		if (nameConvention.equals("from"))
			this.from = (String) value;
		else
			this.from = route.getFrom();

		if (nameConvention.equals("to"))
			this.to = (String) value;
		else
			this.to = route.getTo();

		if (nameConvention.equals("chilangoId"))
			this.chilangoId = (int) value;
		else
			this.chilangoId = route.getChilangoId();

		if (nameConvention.equals("routeName"))
			this.routeName = (String) value;
		else
			this.routeName = route.getRouteName();

		if (nameConvention.equals("headSign"))
			this.headSign = (String) value;
		else
			this.headSign = route.getHeadSign();

		if (nameConvention.equals("frequency"))
			this.frequency = (RouteFrequency) value;
		else
			this.frequency = route.getFrequency();

		if (nameConvention.equals("timeBound"))
			this.timeBound = (RouteTimeBound) value;
		else
			this.timeBound = route.getTimeBound();

		if (nameConvention.equals("backWardRoute"))
			this.backWardRoute = (int) value;
		else
			this.backWardRoute = route.getBackWardRoute();

		if (nameConvention.equals("extraInformation"))
			this.extraInformation = (String) value;
		else
			this.extraInformation = route.getExtraInformation();

		if (nameConvention.equals("stopInformation"))
			this.stopInformation = (StopInformation) value;
		else
			this.stopInformation = route.getStopInformation();

		if (nameConvention.equals("trustLevel"))
			this.trustLevel = (byte) value;
		else
			this.trustLevel = route.getTrustLevel();

		if (nameConvention.equals("operatorId"))
			this.operatorId = (int) value;
		else
			this.operatorId = route.getOperatorId();

		if (nameConvention.equals("lastEdit"))
			this.lastEdit = (long) value;
		else
			this.lastEdit = route.getLastEdit();

		if (nameConvention.equals("color"))
			this.color = (int) value;
		else
			this.color = route.getColor();
	}
}
