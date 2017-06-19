package com.graphhopper.chilango;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.graphhopper.chilango.data.Route;
import com.graphhopper.util.GPXEntry;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.strtree.STRtree;
import com.vividsolutions.jts.linearref.LinearLocation;
import com.vividsolutions.jts.linearref.LocationIndexedLine;

public class RoutesSpatialIndex {

	private static Map<Integer, Route> routes;
	final static SpatialIndex index = new STRtree();

	private static boolean isInitialized = false;
	private static Map<LocationIndexedLine, Integer> indexLines = new HashMap<LocationIndexedLine, Integer>();

	public static void init(Map<Integer, Route> routes) {
		if (!isInitialized) {
			for (Integer routeId : routes.keySet()) {
				List<GPXEntry> gpx = routes.get(routeId).getGpxTrace();
				Coordinate[] points = new Coordinate[gpx.size()];

				// init to a refernce point
				double minLat = gpx.get(0).lat;
				double minLon = gpx.get(0).lon;
				double maxLat = gpx.get(0).lat;
				double maxLon = gpx.get(0).lon;

				int i = 0;
				for (GPXEntry point : gpx) {
					points[i] = new Coordinate(point.getLat(), point.getLon());
					i++;

					minLat = (point.getLat() < minLat) ? point.getLat() : minLat;
					minLon = (point.getLon() < minLon) ? point.getLon() : minLon;
					maxLat = (point.getLat() > maxLat) ? point.getLat() : maxLat;
					maxLon = (point.getLon() > maxLon) ? point.getLon() : maxLon;
				}

				GeometryFactory gFactory = new GeometryFactory();
				// GeometryTransformer gTransform = new GeometryTransformer();

				Geometry geom = gFactory.createLineString(points);

				LocationIndexedLine toIndex = new LocationIndexedLine(geom);

				indexLines.put(toIndex, routeId);

				index.insert(geom.getEnvelopeInternal(), toIndex);

			}
			isInitialized = true;
		}
	}

	public static List<Integer> search(Coordinate pt, int factor) {
		if (isInitialized == false)
			return null;

		double MAX_SEARCH_DISTANCE = 0.000013;
		if (factor > 0)
			MAX_SEARCH_DISTANCE *= factor;
		else
			factor = 1;

		if (MAX_SEARCH_DISTANCE > 0.1)
			return new LinkedList<Integer>();

		List<Integer> results = new LinkedList<>();

		// Get point and create search envelope
		Envelope search = new Envelope(pt);
		search.expandBy(MAX_SEARCH_DISTANCE);

		/*
		 * Query the spatial index for objects within the search envelope. Note
		 * that this just compares the point envelope to the line envelopes so
		 * it is possible that the point is actually more distant than
		 * MAX_SEARCH_DISTANCE from a line.
		 */
		List<LocationIndexedLine> lines = index.query(search);

		// Initialize the minimum distance found to our maximum acceptable
		// distance plus a little bit
		double minDist = MAX_SEARCH_DISTANCE + 1.0e-6;
		Coordinate minDistPoint = null;
		LocationIndexedLine closestLine = null;

		System.out.println("total found: " + lines.size());
		for (LocationIndexedLine line : lines) {

			results.add(indexLines.get(line));

			LinearLocation here = line.project(pt);
			Coordinate point = line.extractPoint(here);
			double dist = point.distance(pt);
			if (dist < minDist) {
				minDist = dist;
				minDistPoint = point;
				closestLine = line;
			}
		}

		if (minDistPoint == null) {
			// No line close enough to snap the point to
			System.out.println(pt + "- X");

		} else {
			System.out.printf("%s - snapped by moving %.4f\n", pt.toString(), minDist);

			System.out.println("closest line: " + indexLines.get(closestLine));
		}

		if (results.size() == 0)
			return search(pt, factor * 2);
		else
			return results;
	}

}
