package com.graphhopper.chilango.data.database;

import com.graphhopper.chilango.data.RouteQuestionary;

public class QuestionaryModel {
	public RouteQuestionary getQuestionary() {
		return questionary;
	}

	public void setQuestionary(RouteQuestionary questionary) {
		this.questionary = questionary;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	private RouteQuestionary questionary;
	private int routeId;
	private int transactionId;
	private boolean valid;
	
	public QuestionaryModel(RouteQuestionary questionary,int routeId,int transactionId){
		this(questionary,routeId,transactionId,true);
	}
	
	public QuestionaryModel(RouteQuestionary questionary,int routeId,int transactionId,boolean valid){
		this.questionary=questionary;
		this.routeId=routeId;
		this.transactionId=transactionId;
		this.valid=valid;
	}
	
}
