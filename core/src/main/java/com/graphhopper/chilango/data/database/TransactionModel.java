package com.graphhopper.chilango.data.database;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.github.filosganga.geogson.model.*;

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

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public int getBasedTransactionId() {
		return basedTransactionId;
	}

	public void setBasedTransactionId(int basedTransactionId) {
		this.basedTransactionId = basedTransactionId;
	}

	public byte getTrust() {
		return trust;
	}

	public void setTrust(byte trust) {
		this.trust = trust;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	private int routeId;
	private String path;
	private int type;
	private Date inputTime;
	private Date creationTime;
	private int status;
	private int transactionId;



	private int basedTransactionId;
	private byte trust;
	private String userId;
	private Geometry geometry;

	public TransactionModel(int routeId, String path, SubmitType type, String userId, int transactionId,
			int status, Date creationTime, byte trust, Geometry geometry,int basedTransactionId) {
		super();
		this.routeId = routeId;
		this.path = path;
		this.type = type.getValue();
		this.userId=userId;
		this.trust=trust;
		this.transactionId = transactionId;
		this.inputTime = new Date(System.currentTimeMillis());
		this.creationTime = creationTime;
		this.status = status;
		this.geometry = geometry;
		this.basedTransactionId=basedTransactionId;
	}



	public SubmitType retrieveType() {

		return SubmitType.getByValue(type);
	}

}
