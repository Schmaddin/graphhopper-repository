package com.graphhopper.chilango.tasks;

import java.util.LinkedList;
import java.util.List;

import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.LineString;
import com.github.filosganga.geogson.model.Point;
import com.graphhopper.chilango.data.Route;

public class TaskHelper {

	public static Geometry getGeometry(ChilangoTask task) {
		if (task instanceof RecordTask) {
			Route route = ((RecordTask) task).getRoute();

			List<Point> points = new LinkedList<>();
			for (int i = 0; i < route.getLat().length; i++) {
				points.add(Point.from(route.getLon()[i], route.getLat()[i]));
			}

			return LineString.of(points);
		} else
			return Point.from(task.getLongitude(), task.getLatitude());
	}

	public static Geometry getGeneralizedGeometry(ChilangoTask task, float accuracy) {
		if (task instanceof RecordTask) {
			Route route = ((RecordTask) task).getRoute();
			if (route.getLat().length < 6)
				return getGeometry(task);

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
		else
			return getGeometry(task);
	}
}
