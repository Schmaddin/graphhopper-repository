package com.graphhopper.chilango.data.database;

import java.util.ArrayList;
import java.util.List;

import com.github.filosganga.geogson.model.Geometry;

public class RouteModel {
	private List<RouteVersionModel> routes=new ArrayList<>();

	private int routeId;
	
	private boolean invalid=false;
	
	public boolean isInvalid() {
		return invalid;
	}

	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}

	private Geometry geometry;
	
	
	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	
	public List<RouteVersionModel> getRoutes() {
		return routes;
	}

	public void setRoutes(List<RouteVersionModel> routes) {
		this.routes = routes;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public void addToRoutes(RouteVersionModel route) {

		routes.add(route);
		
	}
}
