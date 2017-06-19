package com.graphhopper.chilango.data;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.graphhopper.chilango.data.gps.GPSPoint;

public class ReadRoute {

	static public TreeMap<Integer, Route> readRouteMap(File file) {
		TreeMap<Integer, Route> routes = new TreeMap<Integer, Route>();

		// Exclude in BuildHelper?
		try (ObjectInputStream in=new ObjectInputStream(new BufferedInputStream(new FileInputStream(file.getAbsolutePath())))){
			 routes = (TreeMap<Integer,Route>)in.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return routes;
	}

	static public RouteOutdatedFormat readRoute(File file) {

		RouteOutdatedFormat route = null;
		// Exclude in BuildHelper?
		try {
			FileInputStream stream = new FileInputStream(file.getAbsolutePath());
			ObjectInputStream objectStream = new ObjectInputStream(stream);

			route = (RouteOutdatedFormat) objectStream.readObject();

			stream.close();
			objectStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return route;
	}


	FileInputStream stream;
	ObjectInputStream objectStream;

	public ReadRoute(File file) {
		// Exclude in BuildHelper?
		try {
			stream = new FileInputStream(file.getAbsolutePath());
			objectStream = new ObjectInputStream(stream);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public RouteOutdatedFormat readRoute() {
		RouteOutdatedFormat route = null;

		try {
			route = (RouteOutdatedFormat) objectStream.readObject();

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return route;
	}
	
	public void closeFile() {
		try {
			stream.close();

			objectStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Map<Long,GPSPoint> readGPSMap() {
		 Map<Long,GPSPoint> gpsMap = null;

		try {
			gpsMap = (Map<Long,GPSPoint>) objectStream.readObject();


		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return gpsMap;
	}
	
	public Map<Long,Integer> readFullness() {
		 Map<Long,Integer> fullnessMap = null;

		try {
			fullnessMap = (Map<Long,Integer>) objectStream.readObject();


		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fullnessMap;
	}
}