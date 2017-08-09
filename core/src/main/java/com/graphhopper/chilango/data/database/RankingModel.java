package com.graphhopper.chilango.data.database;

public class RankingModel {
	
	public RankingModel(String id, int points, int place) {
		this.id = id;
		this.points = points;
		this.place = place;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public int getPlace() {
		return place;
	}
	public void setPlace(int place) {
		this.place = place;
	}

	private String id;
	private int points;
	private int place;
}