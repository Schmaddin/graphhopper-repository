package com.graphhopper.chilango.tasks;

import com.graphhopper.chilango.data.Route;
import com.graphhopper.chilango.data.RouteQuestionary;
import com.graphhopper.chilango.data.database.SubmitType;

public class DrawRouteTask extends RecordTask{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6741891063520017763L;

	private final RouteQuestionary questionary;

	private final boolean gpsProoved;
	
	public boolean isGpsProoved(){
		return gpsProoved;
	}
	
	public RouteQuestionary getQuestionary() {
		return questionary;
	}
	
    public static final SubmitType typeName=SubmitType.submit_new_draw_route;

	public DrawRouteTask(Route route,RouteQuestionary questionary, String path, boolean gpsProoved, long lastEdit, long uploadTime, int transactionId,String imagePath) {
		super(route, path, typeName, lastEdit, uploadTime, transactionId,imagePath);
		this.questionary=questionary;
		this.gpsProoved=gpsProoved;
	}
	
	public DrawRouteTask(DrawRouteTask task,long uploadTime, int transactionId){
		super(task,uploadTime,transactionId);
		gpsProoved=task.gpsProoved;
		questionary=task.getQuestionary();
	}

}

