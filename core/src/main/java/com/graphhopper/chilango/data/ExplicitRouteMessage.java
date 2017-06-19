package com.graphhopper.chilango.data;

import java.io.Serializable;

public class ExplicitRouteMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum QuestionaryTypes implements Serializable{
		route_not_found(0),route_not_exist(1),route_meta_wrong(2),route_time_wrong(3),route_points_wrong(4),route_frequency(5),route_ok(100),invalid(-1);
		
		private final int value;
		
		
		QuestionaryTypes(int value)
		{
			this.value=value;
		}
		
		public int getValue(){ return value; }
		
		public static QuestionaryTypes getByValue(int value){
			switch(value){
			case 0:
				return route_not_found;
			case 1:
				return route_not_exist;
			case 2:
				return route_meta_wrong;
			case 3:
				return route_time_wrong;
			case 4:
				return route_points_wrong;
			case 100:
				return route_ok;
			default:
				return invalid;
				
			}
			
		}
	}
	
	public ExplicitRouteMessage(ExplicitRouteMessage message,long ticket)
	{
		type=message.getType();
		route=message.getRoute();
		routeId=message.getRouteId();
		timestamp=message.getTimestamp();
		lat=message.getLat();
		lon=message.getLon();
		bound=message.getBound();
		questionary=message.getQuestionary();
		suggestion=message.isSuggestion();
		comment=message.getComment();
		extra=message.getExtra();
		this.ticket=ticket;
	}

	
	public ExplicitRouteMessage(QuestionaryTypes type,Route route, int routeId, long timestamp, double lat, double lon,
			RouteTimeBound bound, RouteQuestionary questionary, boolean suggestion, String comment,
			Serializable[] extra,long ticket) {
		super();
		this.type = type.getValue();
		this.route = route;
		this.routeId = routeId;
		this.timestamp = timestamp;
		this.lat = lat;
		this.lon = lon;
		this.bound = bound;
		this.questionary = questionary;
		this.suggestion = suggestion;
		this.comment = comment;
		this.extra = extra;
		this.ticket = ticket;
	}
	
	public Route getRoute() {
		return route;
	}
	public int getRouteId() {
		return routeId;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public double getLat() {
		return lat;
	}
	public double getLon() {
		return lon;
	}
	public RouteTimeBound getBound() {
		return bound;
	}
	public RouteQuestionary getQuestionary() {
		return questionary;
	}
	public boolean isSuggestion() {
		return suggestion;
	}
	public String getComment() {
		return comment;
	}
	public Serializable[] getExtra() {
		return extra;
	}
	public long getTicket() {
		return ticket;
	}
	public int getType(){
		return type;
	}
	
	public QuestionaryTypes getQuestionaryType(){
		return QuestionaryTypes.getByValue(type);
	}

	private final int type;
	private final Route route;
	private final int routeId;
	private final long timestamp;
	private final double lat;
	private final double lon;
	private final RouteTimeBound bound;
	private final RouteQuestionary questionary;
	private final boolean suggestion;
	private final String comment;
	private final Serializable extra[];
	private final long ticket;

}
