package com.graphhopper.chilango.tasks;

import com.graphhopper.chilango.data.Route;
import com.graphhopper.chilango.data.RouteQuestionary;
import com.graphhopper.chilango.data.database.SubmitType;
import com.graphhopper.chilango.data.gps.GPSPoint;

import java.util.Map;

/**
 * Created by martinwurflein on 10.05.17.
 */

public class GPSRecordTask extends RecordTask{


	public Map<Long, GPSPoint> getGpsMap() {
        return gpsMap;
    }

    public Map<Long, GPSPoint> getOriginalGPSMap() {
        return originalGPSMap;
    }

    public Map<Long, Integer> getFullness() {
        return fullness;
    }

    public RouteQuestionary getQuestionary() {  return questionary; }

    private final Map<Long,GPSPoint> gpsMap;
    private final Map<Long, GPSPoint> originalGPSMap;
    private final Map<Long, Integer> fullness;
    private final RouteQuestionary questionary;

    public GPSRecordTask(GPSRecordTask task, long uploadTime,long ticket)
    {
    	super(task,uploadTime,ticket);
    	
    	gpsMap=task.getGpsMap();
    	originalGPSMap=task.getOriginalGPSMap();
    	fullness=task.getFullness();
    	questionary=task.getQuestionary();
    	
    }

    public static final SubmitType typeName=SubmitType.submit_new_gps_route;

    public GPSRecordTask(Route route, String path, Map<Long,GPSPoint> gpsMap, Map<Long, Integer> fullness,Map<Long,GPSPoint> originalGPSMap,RouteQuestionary questionary,long uploadTime,long lastEdit,long ticket,String imagePath) {
        super(route,path,typeName,lastEdit,uploadTime,ticket,imagePath);
        this.gpsMap=gpsMap;
        this.fullness=fullness;
        this.originalGPSMap=originalGPSMap;
        this.questionary=questionary;
    }


}
