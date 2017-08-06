package com.graphhopper.chilango.data;

import java.io.Serializable;

import com.graphhopper.chilango.data.database.SubmitType;
import com.graphhopper.chilango.data.database.SubmitTypeInterface;

public class Feedback extends SubmitTypeInterface implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Feedback(Feedback message, int transactionId, int routeId) {
		type = message.getType().getValue();
		route = message.getRoute();
		this.routeId = routeId;
		timestamp = message.getTimestamp();
		lat = message.getLat();
		lon = message.getLon();
		bound = message.getBound();
		questionary = message.getQuestionary();
		suggestion = message.isSuggestion();
		comment = message.getComment();
		extra = message.getExtra();
		this.transactionId = transactionId;
	}

	public Feedback(Feedback message, int transactionId) {
		this(message, transactionId, message.getRouteId());
	}

	public Feedback(SubmitType type, Route route, int routeId, long timestamp, double lat, double lon,
			RouteTimeBound bound, RouteQuestionary questionary, boolean suggestion, String comment,
			String[] extra, int transactionId) {
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
		this.transactionId = transactionId;
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

	/**
	 * Extra as Json objects
	 * @return
	 */
	public String[] getExtra() {
		return extra;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public SubmitType getType() {
		return SubmitType.getByValue(type);
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
	private final String extra[];
	private final int transactionId;

}
