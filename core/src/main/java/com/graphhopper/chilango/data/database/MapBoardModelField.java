package com.graphhopper.chilango.data.database;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.github.filosganga.geogson.model.LinearRing;
import com.github.filosganga.geogson.model.Point;
import com.graphhopper.chilango.data.MapBoard;
import com.vividsolutions.jts.geom.Coordinate;

public class MapBoardModelField implements Serializable{
	public MapBoardModelField(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		// define Geometry through fields 
		Coordinate leftTop=MapBoard.getCoordinateOfGrid(x, y);
		Coordinate rightTop =MapBoard.getCoordinateOfGrid(x+1, y);
		Coordinate leftBottom = MapBoard.getCoordinateOfGrid(x, y+1);
		Coordinate rightBottom = MapBoard.getCoordinateOfGrid(x+1, y+1);
		ring=LinearRing.of(Point.from(leftTop.y, leftTop.x),Point.from(rightTop.y, rightTop.x),Point.from(rightBottom.y, rightBottom.x),Point.from(leftBottom.y, leftBottom.x),Point.from(leftTop.y, leftTop.x));
	
		for(int i=0;i<MapBoard.contentSize;i++){
			points[i]=new LinkedList<>();	
		}
	}

	public final int x;
	
	public final int y;
	
	private final LinearRing ring;
	
	private List<PointModel>[] points=new LinkedList[MapBoard.contentSize];
	

	public List<PointModel>[] getPoints() {
		return points;
	}

	public void addPointModel(int team,PointModel pointValue){
		if(team>0&&team<MapBoard.contentSize)
		{
			points[team].add(pointValue);
		}
	}
	
	public short[] calculateFieldValues(){
		short values[]=new short[MapBoard.contentSize];
		
		for(int i=0;i<MapBoard.contentSize;i++){
			for(PointModel point:points[i]){
				// check age?
				values[i]+=point.getCreatorPoints()+point.getRevisorPoints();
			}
		}
		
		return values;
	}
	
}
