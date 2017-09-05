package com.graphhopper.chilango.data.database;

import java.io.Serializable;

import com.graphhopper.chilango.data.Feedback;
import com.graphhopper.chilango.data.Route;
import com.graphhopper.chilango.data.RouteQuestionary;
import com.graphhopper.chilango.data.RouteTimeBound;

public class FeedbackModel extends Feedback{
	
	private boolean valid;

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public FeedbackModel(Feedback message, int transactionId, int routeId, boolean valid) {
		super(message, transactionId, routeId);
		this.valid = valid;
	}

	public FeedbackModel(SubmitType type, Route route, int routeId, long routeEdit, long timestamp, double lat, double lon,
			RouteTimeBound bound, RouteQuestionary questionary, boolean suggestion, String comment,
			String[] extra, int transactionId, boolean valid) {
		super(type, route, routeId, timestamp,routeEdit, lat, lon, bound, questionary, suggestion, comment, extra, transactionId);
		this.valid = valid;
	}

	public FeedbackModel(Feedback message, int transactionId, boolean valid) {
		super(message, transactionId);
		
		this.valid=valid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
