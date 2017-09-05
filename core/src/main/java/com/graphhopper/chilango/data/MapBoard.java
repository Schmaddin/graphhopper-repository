package com.graphhopper.chilango.data;

import java.io.Serializable;

import com.graphhopper.chilango.GeoHelper;
import com.vividsolutions.jts.geom.Coordinate;

public class MapBoard implements Serializable{
	public transient static final int contentSize = 4;
	public transient static final double minlat=19.0043201;
	public transient static final double minlon=-99.4113663;
	public transient static final double maxlat=19.636662;
	public transient static final double maxlon=-98.8936806;
	
	public transient static final double distanceLat=GeoHelper.distance(minlat, minlon, maxlat, minlon);

	public transient static final double distanceLon=GeoHelper.distance(minlat, minlon, minlat, maxlon);

	public transient static final double step=1500.0;
	
	public transient static final int gridSizeLat=(int)(distanceLat/step);
	public transient static final double degreeStepsLat=(maxlat-minlat)/gridSizeLat;
	public transient static final int gridSizeLon=(int)(distanceLon/step);
	public transient static final double degreeStepsLon=(maxlon-minlon)/gridSizeLon;
	
	private final short[][][]content=new short[gridSizeLat][gridSizeLon][contentSize];
			
	public static int getLatField(double lat){
		return (int)(GeoHelper.distance(minlat, minlon, lat, minlon)/step);
	}
	
	public static int getLonField(double lon){
		return (int)(GeoHelper.distance(minlat, minlon, minlat, lon)/step);
	}
	
	public static Coordinate getCoordinateOfGrid(int x,int y){
		Coordinate coordinate=new Coordinate();
		coordinate.x=minlat+x*degreeStepsLat;
		coordinate.y=minlon+y*degreeStepsLon;
		return coordinate;
	}
	
	public short[][][] getMapContent(){return content;}
	
	public void setMapContentValue(int x, int y,int type,short value){
		content[x][y][type]=value;
	}
	
	public void addMapContent(int x, int y,int type,short value){
		content[x][y][type]+=value;
	}
	
	
	
}
