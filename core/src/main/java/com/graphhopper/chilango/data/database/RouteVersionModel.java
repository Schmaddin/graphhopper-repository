package com.graphhopper.chilango.data.database;

import java.io.Serializable;

import com.graphhopper.chilango.data.Route;

public class RouteVersionModel implements Serializable {

	private final Route route;

	private final String userId;

	private final int creationMode;

	// 0: automatic 1: new -1: reset 2: update

	public int getCreationMode() {
		return creationMode;
	}

	public Route getRoute() {
		return route;
	}

	public String getUserId() {
		return userId;
	}

	public long getLastEditTime() {
		return route.getLastEdit();
	}

	public int getRouteId() {
		return route.getId();
	}

	public RouteVersionModel(Route route, String userId, int creationMode) {
		super();
		this.route = route;
		this.userId = userId;
		this.creationMode = creationMode;
	}

}
