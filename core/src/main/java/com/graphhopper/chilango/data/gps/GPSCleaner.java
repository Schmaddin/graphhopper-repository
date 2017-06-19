package com.graphhopper.chilango.data.gps;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.graphhopper.util.DistanceCalcEarth;

public class GPSCleaner {
	public static Map<Long, GPSPoint> applyTreshhold(Map<Long, GPSPoint> gpsMap, int accuracy) {
		Map<Long, GPSPoint> newGPSMap = new TreeMap<>();

		for (Entry<Long, GPSPoint> time : gpsMap.entrySet()) {
			if (time.getValue().accuracy < accuracy)
				newGPSMap.put(time.getKey(), time.getValue());
		}
		System.out.println("Old size: " + gpsMap.size() + " new size: " + newGPSMap.size());
		return newGPSMap;
	}

	public static Map<Long, GPSPoint> applySpeedKiller(Map<Long, GPSPoint> gpsMap, double minSpeed, double maxSpeed) {
		Map<Long, GPSPoint> newGPSMap = new TreeMap<>();

		DistanceCalcEarth calc = new DistanceCalcEarth();
		Entry<Long, GPSPoint> old = null;
		boolean ignore = true;

		for (Entry<Long, GPSPoint> current : gpsMap.entrySet()) {
			if (!ignore) {
				double dist = calc.calcDist(old.getValue().lat, old.getValue().lon, current.getValue().lat,
						current.getValue().lon);
				double time = (current.getKey() - old.getKey()) / 1000.0;
				double speed = dist / time;
				if (dist > 15.0 && speed < maxSpeed && (speed > minSpeed || dist > 30.0)) {
					newGPSMap.put(current.getKey(), current.getValue());
					old = current;
					System.out.print("added: ");

				} else {
					if (speed >= maxSpeed)
						System.out.print("too fast ");
					if (speed <= minSpeed)
						System.out.print("too slow ");
					System.out.print("deleted: ");

				}
				ignore = false;
				System.out.println(speed + " " + dist + " " + time + " " + current.getValue().lat + " "
						+ current.getValue().lon + " " + current.getKey());
			} else {

				ignore = false;

				old = current;
				newGPSMap.put(current.getKey(), current.getValue());
			}
		}
		System.out.println("Old size: " + gpsMap.size() + " new size: " + newGPSMap.size());

		return newGPSMap;
	}
	
	public static Map<Long, GPSPoint> applyMinDistance(Map<Long, GPSPoint> gpsMap,double distance) {
		Map<Long, GPSPoint> newGPSMap = new TreeMap<>();
		
		DistanceCalcEarth calc = new DistanceCalcEarth();
		Entry<Long, GPSPoint> old = null;

		for (Entry<Long, GPSPoint> current : gpsMap.entrySet()) {
			if (old!=null) {
				double dist = calc.calcDist(old.getValue().lat, old.getValue().lon, current.getValue().lat,
						current.getValue().lon);
				
				if(dist>distance)
				{
					newGPSMap.put(current.getKey(), current.getValue());
					old=current;
				}
				
			}else
				old=current;
		}
		
		return newGPSMap;
		
	}
}
