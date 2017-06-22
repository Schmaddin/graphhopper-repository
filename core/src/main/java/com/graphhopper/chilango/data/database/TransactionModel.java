package com.graphhopper.chilango.data.database;

import java.sql.Connection;
import java.util.Date;

public class TransactionModel {
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public Date getInputTime() {
		return inputTime;
	}

	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	private int routeId;
	private String path;
	private int type;
	private String userId;
	private Date inputTime;
	private Date creationTime;
	private int status;
	private int transactionId;

	public TransactionModel(int routeId, String path, int type, String userId, int transactionId, Date creationTime) {
		super();
		this.routeId = routeId;
		this.path = path;
		this.type = type;
		this.userId = userId;
		this.transactionId = transactionId;
		this.inputTime = new Date(System.currentTimeMillis());
		this.creationTime = creationTime;
		this.status = 0;
	}


}
