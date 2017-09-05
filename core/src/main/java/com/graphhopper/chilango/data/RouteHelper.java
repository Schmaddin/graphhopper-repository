package com.graphhopper.chilango.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.LineString;
import com.github.filosganga.geogson.model.Point;

public class RouteHelper {

	static SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm:ss");
	
	static public RouteFrequency frequencyFromQuestionary(RouteTimeBound bound,RouteQuestionary questionary){
		RouteFrequency freq=new RouteFrequency();
		int weekday=questionary.normalFrequency;
		if(weekday<1)
			weekday=10;
		freq.applyByBound(bound, (byte)weekday);
		
		if(questionary.rushHourFrequency>0){
			for(int i=0;i<5;i++){
				for(int j=7*2;j<9*2;j++){
					if(freq.frequency[i][j]!=0)
					freq.frequency[i][j]=(byte)questionary.rushHourFrequency;
				}
				for(int j=17*2+1;j<20*2;j++){

					freq.frequency[i][j]=(byte)questionary.rushHourFrequency;
				}
			}
		}
		
		if(questionary.weekendFrequency>0){
			for(int i=5;i<7;i++){
				for(int j=0;j<48;j++){

					if(freq.frequency[i][j]!=0)
					freq.frequency[i][j]=(byte)questionary.weekendFrequency;
				}
			}
		}
		
		if(questionary.nightFrequency>0){
			for(int i=0;i<7;i++){
				for(int j=0;j<10;j++){
					if(freq.frequency[i][j]!=0 && freq.frequency[i][j]<questionary.nightFrequency)
					freq.frequency[i][j]=(byte)questionary.nightFrequency;
				}
				for(int j=20*2;j<48;j++){
					if(freq.frequency[i][j]!=0 && freq.frequency[i][j]<questionary.nightFrequency)
					freq.frequency[i][j]=(byte)questionary.nightFrequency;
				}
			}
		}
		return freq;
	
		
	}
	
	public static int[] calculateFrequenciesPerTimes(RouteFrequency freq)
	{

		int returnValue[]=new int[5];
		
		int n=0;
		int counter=0;
		
			for(int i=0;i<7;i++){ // night
				for(int j=0;j<10;j++){
					if(freq.frequency[i][j]>0){
						n++;
						counter+=freq.frequency[i][j];
					}
				}
				for(int j=20*2;j<48;j++){
					if(freq.frequency[i][j]>0){
						n++;
						counter+=freq.frequency[i][j];
					}
				}
			}
			if(n>0)
			returnValue[4]=counter/n;
			
			n=0;
			counter=0;

		for(int i=0;i<5;i++){  // rush hour
			for(int j=7*2;j<9*2;j++){
				if(freq.frequency[i][j]>0){
					n++;
					counter+=freq.frequency[i][j];
				}
			}
			for(int j=17*2+1;j<20*2;j++){
				if(freq.frequency[i][j]>0){
					n++;
					counter+=freq.frequency[i][j];
				}
			}
		}

		if(n>0)
		returnValue[3]=counter/n;
		
		n=0;
		counter=0;
	

		for(int i=5;i<7;i++){  // weekend
			for(int j=0;j<48;j++){
				if(freq.frequency[i][j]>0){
					n++;
					counter+=freq.frequency[i][j];
				}
			}
		}

		if(n>0)
		returnValue[2]=counter/n;
		
		n=0;
		counter=0;
		
		for(int i=0;i<7;i++){  // day
			for(int j=14;j<40;j++){
				if(freq.frequency[i][j]>0){
					n++;
					counter+=freq.frequency[i][j];
				}
			}
		}

		if(n>0)
		returnValue[1]=counter/n;
		
		n=0;
		counter=0;
		
		for(int i=0;i<7;i++){  // total
			for(int j=0;j<48;j++){
				if(freq.frequency[i][j]>0){
					n++;
					counter+=freq.frequency[i][j];
				}
			}
		}

		if(n>0)
		returnValue[0]=counter/n;
		
		n=0;
		counter=0;
	
	return returnValue;
			
}

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
			
		case mexibús:
			for (int i = 0; i < 7; i++) {
				timeBound.startHour[i] = 4;
				timeBound.startMinute[i] = 30;
				timeBound.lastHour[i] = 0;
				timeBound.lastMinute[i] = 30;

			}

			timeBound.trustLevel = 30;

			frequency.applyByBound(timeBound, (byte) 20);
			frequency.printIndex();
			break;
			
			
		case pumabús:
			for (int i = 0; i < 7; i++) {
				timeBound.startHour[i] = 6;
				timeBound.startMinute[i] = 00;
				timeBound.lastHour[i] = 22;
				timeBound.lastMinute[i] = 00;

			}

			timeBound.trustLevel = 0;

			frequency.applyByBound(timeBound, (byte) 10);
			frequency.printIndex();
			break;

		case rtp:
			for (int i = 0; i < 7; i++) {
				timeBound.startHour[i] = 5;
				timeBound.startMinute[i] = 30;
				timeBound.lastHour[i] = 22;
				timeBound.lastMinute[i] = 30;

			}

			timeBound.trustLevel = 0;

			frequency.applyByBound(timeBound, (byte) 20);
			frequency.printIndex();
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

		}
		to.trustLevel = from.trustLevel;
	}

	public static void copyTimeBound(byte[] startH, byte[] startM, byte[] lastH, byte[] lastM, byte trust,
			RouteTimeBound to) {
		for (int i = 0; i < startH.length; i++) {
			to.startHour[i] = startH[i];
			to.startMinute[i] = startM[i];
			to.lastHour[i] = lastH[i];
			to.lastMinute[i] = lastM[i];

		}
		to.trustLevel = trust;
	}

	public static void copyFrequency(RouteFrequency from, RouteFrequency to) {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 48; j++) {
				to.frequency[i][j] = from.frequency[i][j];
			}
			to.trustLevel = from.trustLevel;

		}
	}

	public static void copyFrequency(byte[][] frequency, byte trustLevel, RouteFrequency to) {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 48; j++) {
				to.frequency[i][j] = frequency[i][j];
			}
			to.trustLevel = trustLevel;

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

	public static int differenceInRoutes(Route a, Route b) {
		if (a == b)
			return 0;
		if ((a == null && b != null) || (a != null && b == null))
			return Integer.MAX_VALUE;

		int difference = differenceInTimeBounds(a.getTimeBound(), b.getTimeBound());
		System.out.println("difference: " + difference);
		if (a.getBackWardRoute() != b.getBackWardRoute())
			difference++;

		if (!a.getFrom().equals(b.getFrom()))
			difference++;
		if (!a.getTo().equals(b.getTo()))
			difference++;

		if (!a.getHeadSign().equals(b.getHeadSign()))
			difference++;

		if (!a.getRouteName().equals(b.getRouteName()))
			difference++;

		if (a.getTransportType() != b.getTransportType())
			difference++;

		if (a.getOperatorId() != b.getOperatorId())
			difference++;
		System.out.println("difference: " + difference);

		difference += differenceInStops(a.getStopInformation(), b.getStopInformation());

		int nullValues = 0;
		if (a.getLat().length == b.getLat().length) {

			for (int i = 0; i < a.getLat().length; i++) {
				difference += almostEqualGPS(a.getLat()[i], b.getLat()[i]) ? 0 : 1;

				difference += almostEqualGPS(a.getLon()[i], b.getLon()[i]) ? 0 : 1;

				difference += a.getTimeStamp()[i] != b.getTimeStamp()[i] ? 1 : 0;

				nullValues += (a.getTimeStamp()[i] == 0L || b.getTimeStamp()[i] == 0L) ? 1 : 0;
			}

			if (difference == a.getTimeStamp().length && a.getTimeStamp().length == nullValues)
				difference -= a.getTimeStamp().length;

		} else
			difference += Math.abs(a.getLat().length - b.getLat().length);
		// maybe to compare more attributs?

		return difference;
	}

	public static int differenceInStops(StopInformation a, StopInformation b) {
		if (a == b)
			return 0;
		if ((a == null && b != null) || (a != null && b == null))
			return 10;

		int difference = 0;

		if (a.getLatStop().length != b.getLatStop().length)
			difference++;

		for (int i = 0; i < a.getLatStop().length; i++) {
			difference += almostEqualGPS(a.getLatStop()[i], b.getLatStop()[i]) ? 0 : 1;

			difference += almostEqualGPS(a.getLonStop()[i], b.getLonStop()[i]) ? 0 : 1;

			difference += !a.getStopName()[i].equals(b.getStopName()[i]) ? 1 : 0;

			difference += a.getTime()[i] != b.getTime()[i] ? 1 : 0;

			difference += a.getTimeAtStop()[i] != b.getTimeAtStop()[i] ? 1 : 0;
		}

		return difference;
	}

	public static List<Stop> takeCareForMinStops(Map<Integer, Route> routes, Set<TransportType> types) {
		List<Stop> stops = new LinkedList<>();
		for (Integer id : routes.keySet()) {
			if (types.contains(TransportType.getTypeByValue(routes.get(id).getTransportType()))) {
				Stop start=new Stop(routes.get(id).getFrom(), routes.get(id).getTransportType(), routes.get(id).getLat()[0], routes.get(id).getLon()[0], "");
				start.getRoutes().add(id);
				int lastIndex=routes.get(id).getLat().length-1;
				Stop stop=new Stop(routes.get(id).getTo(), routes.get(id).getTransportType(), routes.get(id).getLat()[lastIndex], routes.get(id).getLon()[lastIndex], "");
				stop.getRoutes().add(id);
				stops.add(start);
				stops.add(stop);
			}

		}

		return stops;

	}

	public static boolean almostEqualGPS(double a, double b) {
		return almostEqual(a, b, 0.000001);
	}

	public static boolean almostEqual(double a, double b, double eps) {
		return Math.abs(a - b) < eps;
	}

	public static int differenceInTimeBounds(RouteTimeBound bound1, RouteTimeBound bound2) {
		if (bound1 == bound2)
			return 0;
		if ((bound1 == null && bound2 != null) || (bound1 != null && bound2 == null))
			return 13;

		int difference = 0;

		for (int i = 0; i < bound1.startMinute.length; i++) {
			difference += bound1.startMinute[i] != bound2.startMinute[i] ? 1 : 0;
			difference += bound1.lastMinute[i] != bound2.lastMinute[i] ? 1 : 0;
			difference += bound1.startHour[i] != bound2.startHour[i] ? 1 : 0;
			difference += bound1.lastHour[i] != bound2.lastHour[i] ? 1 : 0;
		}
		difference += bound1.trustLevel != bound2.trustLevel ? 1 : 0;

		return difference;
	}

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

	public static boolean frequencyIsEmpty(RouteFrequency freq) {
		if(freq==null)
			return true;
		int value=0;
		for(int i=0;i<7;i++)
			for(int j=0;j<48;j++)
				value+=freq.frequency[i][j];
		
		return value>0?false:true;
		
	}
}
