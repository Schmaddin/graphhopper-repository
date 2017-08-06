package com.graphhopper.chilango.tasks;

import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.Point;

import com.github.filosganga.geogson.model.LineString;

import com.graphhopper.chilango.data.Route;
import com.graphhopper.chilango.data.database.SubmitType;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by martinwurflein on 25.04.17.
 */

public class RecordTask extends ChilangoTask {

    public Route getRoute() {
        return route;
    }

    protected final Route route;

    public RecordTask(RecordTask task,long uploadTime,int transactionId){
    	super(task,uploadTime,transactionId);
    	route=task.getRoute();
    }
    
    public RecordTask(Route route,String path,SubmitType type,long lastEdit,long uploadTime,int transactionId,String imagePath) {
        super(path,type,route.getRouteName()+" "+route.getHeadSign(),route.getLat()[0],route.getLon()[0],uploadTime,lastEdit,transactionId,imagePath);
        this.route = route;
    }

    public RecordTask(Route route, String path,long lastEdit,long uploadTime,int transactionId,String imagePath) {
        this(route,path,SubmitType.submit_route,lastEdit,uploadTime,transactionId,imagePath);
    }

    @Override
    public String toString(){
        return this.getType()+": "+this.getName();
    }


}
