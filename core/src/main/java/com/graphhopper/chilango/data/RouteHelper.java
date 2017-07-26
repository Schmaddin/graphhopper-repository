package com.graphhopper.chilango.data;

import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.LineString;
import com.github.filosganga.geogson.model.Point;
import com.graphhopper.chilango.data.Route;

import java.util.LinkedList;
import java.util.List;

public class RouteHelper {

	public static Geometry getGeometry(Route route) {

		List<Point> points = new LinkedList<>();
		for (int i = 0; i < route.getLat().length; i++) {
			points.add(Point.from(route.getLon()[i], route.getLat()[i]));
		}

		return LineString.of(points);

	}

	public static Geometry getGeneralizedGeometry(Route route, float accuracy) {

		if (route.getLat().length < 6)
			return getGeometry(route);

		List<Point> points = new LinkedList<>();
		int oldValue = 0;
		for (float i = 0; i < route.getLat().length;) {
			if (oldValue != (int) i) {
				points.add(Point.from(route.getLon()[(int) i], route.getLat()[(int) i]));
				oldValue = (int) i;
			}

			i = i + 1.0f / accuracy;
		}

		return LineString.of(points);

	}

}
