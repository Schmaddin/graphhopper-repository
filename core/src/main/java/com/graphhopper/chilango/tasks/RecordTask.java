package com.graphhopper.chilango.tasks;

import com.graphhopper.chilango.data.Route;

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

    public RecordTask(RecordTask task,long uploadTime,long ticket){
    	super(task,uploadTime,ticket);
    	route=task.getRoute();
    }
    
    public RecordTask(Route route,String path,String type,long lastEdit,long uploadTime,long ticket,String imagePath) {
        super(path,type,route.getRouteName()+" "+route.getHeadSign(),route.getGpxLat()[0],route.getGpxLon()[0],uploadTime,lastEdit,ticket,imagePath);
        this.route = route;
    }

    public RecordTask(Route route, String path,long lastEdit,long uploadTime,long ticket,String imagePath) {
        this(route,path,"RecordTask",lastEdit,uploadTime,ticket,imagePath);
    }

    @Override
    public String toString(){
        return this.getType()+": "+this.getName();
    }


}
