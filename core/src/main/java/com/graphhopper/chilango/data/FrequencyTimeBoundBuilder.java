package com.graphhopper.chilango.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FrequencyTimeBoundBuilder {

	static SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm:ss");

	static public void build(TransportType transportType, RouteTimeBound timeBound, RouteFrequency frequency) {

		timeBound.trustLevel = -1;

		frequency.trustLevel = -1;

		switch (transportType) {
		case metrobús:
			for (int i = 0; i < 5; i++) {
				timeBound.startHour[i] = 4;
				timeBound.startMinute[i] = 30;
				timeBound.lastHour[i] = 22;
				timeBound.lastMinute[i] = 59;
			}
			timeBound.startHour[5] = 5;
			timeBound.startMinute[5] = 0;
			timeBound.lastHour[5] = 22;
			timeBound.lastMinute[5] = 59;

			timeBound.startHour[6] = 5;
			timeBound.startMinute[6] = 40;
			timeBound.lastHour[6] = 22;
			timeBound.lastMinute[6] = 59;

			timeBound.trustLevel = 0;

			frequency.applyByBound(timeBound, (byte) 5);
			break;
		case metro:
			for (int i = 0; i < 5; i++) {
				timeBound.startHour[i] = 5;
				timeBound.startMinute[i] = 0;
				timeBound.lastHour[i] = 23;
				timeBound.lastMinute[i] = 59;

			}
			timeBound.startHour[5] = 6;
			timeBound.startMinute[5] = 0;
			timeBound.lastHour[5] = 23;
			timeBound.lastMinute[5] = 59;

			timeBound.startHour[6] = 7;
			timeBound.startMinute[6] = 0;
			timeBound.lastHour[6] = 23;
			timeBound.lastMinute[6] = 59;

			timeBound.trustLevel = 0;

			frequency.applyByBound(timeBound, (byte) 4);
			break;

		default:
			for (int i = 0; i < 6; i++) {
				timeBound.startHour[i] = 5;
				timeBound.startMinute[i] = 0;
				timeBound.lastHour[i] = 22;
				timeBound.lastMinute[i] = 59;

			}
			timeBound.startHour[6] = 6;
			timeBound.startMinute[6] = 0;
			timeBound.lastHour[6] = 22;
			timeBound.lastMinute[6] = 59;

			timeBound.trustLevel = 0;

			frequency.applyByBound(timeBound, (byte) 10);
			frequency.printIndex();
			break;

		}

	}

	public static void copyTimeBound(RouteTimeBound from, RouteTimeBound to) {
		for (int i = 0; i < from.startHour.length; i++) {
			to.startHour[i] = from.startHour[i];
			to.startMinute[i] = from.startMinute[i];
			to.lastHour[i] = from.lastHour[i];
			to.lastMinute[i] = from.lastMinute[i];
			to.trustLevel = from.trustLevel;

		}
		to.trustLevel = from.trustLevel;
	}

	public static void copyFrequency(RouteFrequency from, RouteFrequency to) {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 48; j++) {
				to.frequency[i][j]=from.frequency[i][j];
			}
			to.trustLevel = from.trustLevel;

		}
	}

	public static Date createFormat(String parseHHmmss, boolean withSeconds) {

		try {
			return dataFormat.parse(parseHHmmss);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
