package com.graphhopper.chilango.data;

import java.io.Serializable;
import java.util.List;

import com.graphhopper.chilango.data.database.PointModel;

public class UserStatus implements Serializable {

	
	public int getPoints() {
		return points;
	}
	public int getPlaceExplore() {
		return placeExplore;
	}
	public int getPlaceRevise() {
		return placeRevise;
	}
	public String getName() {
		return name;
	}
	public Status getStatus() {
		return Status.getStatusByValue(status);
	}
	public long getPointsUpdate() {
		return pointsUpdate;
	}
	public List<PointModel> getPointSinceUpdate() {
		return pointSinceUpdate;
	}
	
	public double getLat() {
		return homeLat;
	}
	
	public double getLon() {
		return homeLon;
	}
	
	
	public UserStatus(int points, int placeExplore, int placeRevise, String name, int status, long pointsUpdate,
			List<PointModel> pointSinceUpdate,double homeLat,double homeLon,double workLat,double workLon,int team,byte trust) {
		super();
		this.points = points;
		this.placeExplore = placeExplore;
		this.placeRevise = placeRevise;
		this.name = name;
		this.status = status;
		this.pointsUpdate = pointsUpdate;
		this.pointSinceUpdate = pointSinceUpdate;
		this.homeLat=homeLat;
		this.homeLon=homeLon;
		this.workLat=workLat;
		this.workLon=workLon;
		this.trust=trust;
		this.team=team;
	}
	
	public byte getTrust() {
		return trust;
	}
	public int getTeam() {
		return team;
	}
	public double getHomeLat() {
		return homeLat;
	}
	public double getHomeLon() {
		return homeLon;
	}
	public double getWorkLat() {
		return workLat;
	}
	public double getWorkLon() {
		return workLon;
	}

	private final int points;
	private final int placeExplore;
	private final int placeRevise;
	private final String name;
	private final int status;
	private final byte trust;
	private final int team;
	private final long pointsUpdate;
	private final List<PointModel> pointSinceUpdate;
	private final double homeLat;
	private final double homeLon;
	private final double workLat;
	private final double workLon;
}