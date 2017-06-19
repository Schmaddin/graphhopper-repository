package com.graphhopper.chilango.tasks;

import com.graphhopper.chilango.data.Route;
import com.graphhopper.chilango.data.RouteQuestionary;
import com.graphhopper.chilango.data.gps.GPSPoint;

import java.util.Map;

/**
 * Created by martinwurflein on 10.05.17.
 */

public class CheckInGPSRecordTask extends GPSRecordTask{

    public CheckInGPSRecordTask(CheckInGPSRecordTask task, long uploadTime,long ticket)
    {
    	super(task,uploadTime,ticket);
    	
    }

    public static final String typeName="ReGPSRecordTask";

    public CheckInGPSRecordTask(Route route, String path, Map<Long,GPSPoint> gpsMap, Map<Long, Integer> fullness,Map<Long,GPSPoint> originalGPSMap,RouteQuestionary questionary,long uploadTime,long lastEdit,long ticket,String imagePath) {
        super(route,path,gpsMap,fullness,originalGPSMap,questionary,uploadTime,uploadTime,ticket,imagePath);
    }


}
