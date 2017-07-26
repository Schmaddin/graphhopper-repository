package com.graphhopper.chilango;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.graphhopper.GraphHopper;
import com.graphhopper.chilango.data.Route;

public class EdgeInformation {

	private static int day;
	public static int maxIndex = 31268;
	private static byte[][][] edgeInformation = new byte[2][maxIndex][24 * 4];
	private static Map<Integer, Integer[]> routesOnEdges;
	private static Map<Integer, Route> routes = new HashMap<>();
	private static String path = "";
	private static boolean init = false;
	private static GraphHopper hopper;

	public static void initialize(GraphHopper graphHopper) {
		path = graphHopper.getGraphHopperLocation();
		hopper = graphHopper;
		Calendar c = Calendar.getInstance();
		setAndInitialiseDay(c.get(Calendar.DAY_OF_WEEK) - 1);

		try {
			FileInputStream readRoutesOnEdgeStream = new FileInputStream(path + "/routesOnEdges.dat");
			ObjectInputStream readRoutesOnEdgeObject = new ObjectInputStream(readRoutesOnEdgeStream);

			routesOnEdges = (Map<Integer, Integer[]>) readRoutesOnEdgeObject.readObject();

			readRoutesOnEdgeObject.close();
			readRoutesOnEdgeStream.close();

			FileInputStream readRoutesStream = new FileInputStream(path + "/routes.dat");
			ObjectInputStream readRoutesObject = new ObjectInputStream(readRoutesStream);

			Set<Route> routeSet = (Set<Route>) readRoutesObject.readObject();
			for (Route current : routeSet) {
				routes.put(current.getId(), current);
			}

			readRoutesStream.close();
			readRoutesObject.close();

		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		init = true;
	}

	public static void setAndInitialiseDay(int dayValue) {
		if (dayValue >= 0 && dayValue < 7)
			day = dayValue;

		try {
			FileInputStream stream = new FileInputStream(path + "/" + (day % 7) + ".dat");
			FileInputStream stream2 = new FileInputStream(path + "/" + ((day + 1) % 7) + ".dat");
			for (int i = 0; i < maxIndex; i++) {
				stream.read(edgeInformation[0][i]);
				stream2.read(edgeInformation[1][i]);
			}
			stream.close();
			stream2.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static byte getEdgeValue(int edgeId, int hour, int minute) {

		int dayIndex = 0;
		if (hour > 24) {
			dayIndex++;
			hour -= 24;
		}

		return edgeInformation[dayIndex][edgeId - 1][hour * 4 + minute / 15];

	}

	public static long getOriginalOSM(int internId) {
		return hopper.getOSMWay(internId);
	}

	public static byte getRouteEdgeValue(int routeId, int hour, int minute) {

		int dayIndex = day;
		if (hour > 24) {
			dayIndex++;
			hour -= 24;
		}
		// TODO fehlt die Calculation....bis der Bus hier sein...müste
		// (ende/anfang)
		// routesOnEdges.get(routeId)[dayIndex][edgeId - 1][hour * 2 + minute /
		// 30];

		// TODO löschen
		System.out.println("getRouteEdgeValue aufgerufen");

		Route route = routes.get(routeId);

		return route.getFrequency().frequency[dayIndex][hour * 2 + minute / 30];

	}

	public static Route getRoute(int id) {
		return routes.get(id);
	}

	public static Integer[] getRoutes(int edgeId) {
		return routesOnEdges.get(edgeId);
	}

	public static Integer[] validateRoutes(int edgeId, int hour, int minute) {
		int dayIndex = day;
		if (hour > 24) {
			dayIndex++;
			hour -= 24;
		}

		// TODO genaue Calculation!!!
		Integer[] notCleared = getRoutes(edgeId);
		if (notCleared.length == 0)
			System.out.println("empty");

		List<Integer> cleared = new LinkedList<>();

		for (Integer currentRoute : notCleared) {
			if (routes.get(currentRoute).getTimeBound().startHour[dayIndex] >= hour
					&& routes.get(currentRoute).getTimeBound().lastHour[dayIndex] <= hour) {
				if (routes.get(currentRoute).getTimeBound().startHour[dayIndex] == hour
						&& routes.get(currentRoute).getTimeBound().startMinute[dayIndex] > minute) {
					// is skipped not in timebound
					System.out.println(currentRoute + " skipped");
				} else if (routes.get(currentRoute).getTimeBound().lastHour[dayIndex] == hour
						&& routes.get(currentRoute).getTimeBound().lastMinute[dayIndex] < minute) {
					// is skipped out
					System.out.println(currentRoute + " skipped");
				} else {
					cleared.add(currentRoute);
					System.out.println(currentRoute);

				}

			}

		}
		Integer clearedArray[] = new Integer[cleared.size()];
		cleared.toArray(clearedArray);
		return clearedArray;
	}

	public static int getWaiting(Set<Integer> set, int hour, int minute) {
		// TODO Auto-generated method stub
		int waiting = 0;

		if (set.size() > 0) {
			for (Integer route : set) {
				waiting += getRouteEdgeValue(route, hour, minute);
			}
			waiting = waiting / set.size() / set.size();
		}
		return waiting;
	}
}
