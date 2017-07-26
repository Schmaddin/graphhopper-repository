package com.graphhopper.chilango.data.database;

import java.io.Serializable;

public class PointModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public PointModel(int transactionId, int creatorPoints, int revisorPoints, long time,int submitType) {
		super();
		this.transactionId = transactionId;
		this.creatorPoints = creatorPoints;
		this.revisorPoints = revisorPoints;
		this.submitType = submitType;
		this.time = time;
	}
	public int getTransactionId() {
		return transactionId;
	}

	public int getCreatorPoints() {
		return creatorPoints;
	}

	public int getRevisorPoints() {
		return revisorPoints;
	}

	public long getTime() {
		return time;
	}
	
	public int getSubmitType() {
		return submitType;
	}
	
	private final int transactionId;
	private final int creatorPoints;
	private final int revisorPoints;
	private final long time;
	private final int submitType;

}
