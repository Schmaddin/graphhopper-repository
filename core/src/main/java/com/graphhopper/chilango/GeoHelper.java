package com.graphhopper.chilango;


import com.graphhopper.chilango.data.gps.GPSPoint;
import com.graphhopper.util.DistanceCalc3D;
import com.graphhopper.util.DistanceCalcEarth;
import com.graphhopper.util.GPXEntry;
import com.graphhopper.util.shapes.GHPoint;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by martinwurflein on 13.06.17.
 */

public class GeoHelper {
    static DistanceCalcEarth distanceCalc = new DistanceCalcEarth();

    public static Coordinate snapPointToLine(double lat1, double lon1, double lat2, double lon2, double latPoint, double lonPoint)
    {
        // create Line
        LineSegment ls = new LineSegment(lat1, lon1, lat2, lon2);
        //calculate distance between Line and Point
        Coordinate point = new Coordinate(latPoint, lonPoint);
        Coordinate project = ls.project(point);

        if(ls.distance(project)>0.000001)
            project = ls.closestPoint(project);

        return new Coordinate(project.x,project.y);
    }
    
    public static int findClosestSegment(Coordinate p,List<Coordinate> geoPoints){
        double shortest = Double.MAX_VALUE;
        int closestSegment = -1;
        //find closest segment
        for (int i = 1; i < geoPoints.size(); i++) {
            double current = shortestDistanceToLine(geoPoints.get(i - 1).x, geoPoints.get(i - 1).y, geoPoints.get(i).x, geoPoints.get(i).y, p.x, p.y);
            if (current < shortest) {
                shortest = current;
                closestSegment = i - 1;
            }
        }
        return closestSegment;
    }
    
    public static Coordinate findOrthogonalPointOnLine(Coordinate p,List<Coordinate> geoPoints) {
        
        int closestSegment=findClosestSegment(p,geoPoints);

        if (closestSegment != -1) {
            return snapPointToLine(geoPoints.get(closestSegment).x, geoPoints.get(closestSegment).y, geoPoints.get(closestSegment + 1).x, geoPoints.get(closestSegment + 1).y, p.x, p.y);

        }
        return null;
    }


    public static double shortestDistanceToLine(double lat1, double lon1, double lat2, double lon2, double latPoint, double lonPoint)
    {
        // create Line
        LineSegment ls = new LineSegment(lat1, lon1, lat2, lon2);
        //calculate distance between Line and Point
        Coordinate point = new Coordinate(latPoint, lonPoint);
        return ls.distance(point);
    }


    public static float distance(double lat1, double lon1,double lat2,double lon2) {
        return (float)distanceCalc.calcDist(lat1,lon1,lat2,lon2);
    }
    
    public static double bearing(double lat1, double lon1, double lat2, double lon2){
    	  double longitude1 = lon1;
    	  double longitude2 = lon2;
    	  double latitude1 = Math.toRadians(lat1);
    	  double latitude2 = Math.toRadians(lat2);
    	  double longDiff= Math.toRadians(longitude2-longitude1);
    	  double y= Math.sin(longDiff)*Math.cos(latitude2);
    	  double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

    	  return (Math.toDegrees(Math.atan2(y, x))+360)%360;
    	}
    
    public static float distanceToMultiLine(Coordinate p, List<Coordinate> geoPoints)
    {
        Coordinate closestPoint=findOrthogonalPointOnLine(p,geoPoints);
        
        if(closestPoint!=null)
        return distance(p.x,p.y,closestPoint.x,closestPoint.y);
        else
        	return Float.MAX_VALUE;
    }


    public static List<GPXEntry> convertMapToGPXList(Map<Long,GPSPoint> points){
        List<GPXEntry> GPXList = new LinkedList<>();

        for (Map.Entry<Long,GPSPoint> entry : points.entrySet()) {
            GPXList.add(new GPXEntry(entry.getValue().lat, entry.getValue().lon, entry.getKey()));
        }

        return GPXList;
    }

    public static Map<Long,GPSPoint> convertGPXListTOMap(List<GPXEntry> points){
        Map<Long,GPSPoint> pointMap= new TreeMap<>();
        for(GPXEntry entry:points){
            pointMap.put(entry.getTime(),new GPSPoint(entry.getLat(),entry.getLon(),entry.getElevation(),0.0f));
        }
        return pointMap;
    }
}
