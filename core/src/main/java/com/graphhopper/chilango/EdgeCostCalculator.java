package com.graphhopper.chilango;

public class EdgeCostCalculator {

	static String operatorToString(int type) {
		switch (type) {
		case 0:
			return "foot";
		case 1:
			return "Bus (hop on off)";
		case 2:
			return "Metrobus";
		case 3:
			return "Metro";

		}
		return "";
	}

	static double getExitTime(int operator, int type, int time) {
		//System.out.print(operatorToString(type));
		return (long) 0.5 * getAccessTime(operator, type, time, false);
	}

	static double getAccessTime(int operator, int type, int time, boolean text) {
	//	if (text == true)
	//		System.out.println(" -> " + operatorToString(type));
		switch (type) {
		case 0:
			return 0;
		case 1:
			return 0;
		case 2:
			if (time > 70 && time < 90)
				return 60000;
			else if (time > 400 && time < 750)
				return 60000;
			else
				return 30000;
		case 3:
			if (time > 70 && time < 90)
				return 200000;
			else if (time > 400 && time < 750)
				return 300000;
			else
				return 200000;

		}

		return 0;
	}

	// if it is the same route
	static double getContinueTime(int type, int time) {

		double t = 0;
		if (type == 0)
			t = 0;

		else if (type == 1) {
			t = 0L;
		} else if (type == 2) {

			t = 30000;

		} else if (type == 3) {
			if (time > 70 && time < 90)
				t = 50000;
			else if (time > 400 && time < 750)
				t = 30000;
			else
				t = 50000;
		}
		//System.out.println("-> continue " + operatorToString(type) + "(wait: " + t + ") ->");
		return t;
	}

	// TODO time
	static double getWaitingTime(int operator, int type, int time, long edge) {
		if(operator !=-1)
		{
			EdgeInformation.getRouteEdgeValue(operator,12, 13);
			return 0;
		}
		else if (edge > 0 && edge < 500000) {
			byte edgeInfo = EdgeInformation.getEdgeValue((int) edge, 12, 13);
			switch (edgeInfo) {

			case 7:
				return 3 * 60000;
			case 6:
				return 5 * 60000;
			case 5:
				return 7 * 60000;
			case 4:
				return 10 * 60000;
			case 3:
				return 15 * 60000;
			case 2:
				return 30 * 60000;
			case 1:
				return 45 * 60000;
			default:
				return Double.POSITIVE_INFINITY;
			}

		} else {
			switch (type) {
			case 0:
				return 0L;
			case 1:
				// TODO get Bus Waiting Time
				// TODO estimated: 5 minutes
				if (time > 0 && time < 50)
					return -1;
				else if (time >= 50 && time <= 65)
					return 600000;
				else if (time > 65 && time <= 220)
					return 300000;
				else if (time > 220 && time <= 240)
					return 600000;
				else
					return -1L;

			case 2:
				if (time > 5 && time < 50)
					return -1;
				else if (time >= 50 && time <= 65)
					return 400000;
				else if (time > 65 && time <= 220)
					return 250000;
				else if (time > 220 && time <= 240)
					return 400000;
				else
					return -1;

			case 3:
				if (time > 5 && time < 50)
					return -1L;
				else if (time >= 50 && time <= 65)
					return 400000;
				else if (time > 65 && time <= 220)
					return 250000;
				else if (time > 220 && time <= 240)
					return 400000;
				else
					return -1;
			}
			return -1;
		}
	}

}
