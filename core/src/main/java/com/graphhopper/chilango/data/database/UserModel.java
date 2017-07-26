package com.graphhopper.chilango.data.database;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.github.filosganga.geogson.model.Point;

public class UserModel {

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwHash() {
		return pwHash;
	}

	public void setPwHash(String pwHash) {
		this.pwHash = pwHash;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isMail_confirmation() {
		return mail_confirmation;
	}

	public void setMail_confirmation(boolean mail_confirmation) {
		this.mail_confirmation = mail_confirmation;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public long getPointsUpdateTime() {
		return pointsUpdateTime;
	}

	public void setPointsUpdateTime(long pointsUpdateTime) {
		this.pointsUpdateTime = pointsUpdateTime;
	}

	public long getUserCreationTime() {
		return userCreationTime;
	}

	public void setUserCreationTime(long userCreationTime) {
		this.userCreationTime = userCreationTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMailConfirmationToken() {
		return mailConfirmationToken;
	}

	public void setMailConfirmationToken(String mailConfirmationToken) {
		this.mailConfirmationToken = mailConfirmationToken;
	}


	public Point getHomePoint() {
		return homePoint;
	}

	public void setHomePoint(Point homePoint) {
		this.homePoint = homePoint;
	}
	
	public List<PointModel> getPointModel() {
		return pointModel;
	}

	public UserModel(String mail, String name, long userCreationTime, String pwHash, String token,
			boolean mail_confirmation, String mailConfirmationToken, String imagePath, int points,
			long pointsUpdateTime, int status, Point homePoint, Point workPoint, int team) {
		super();
		this.mail = mail;
		this.name = name;
		this.userCreationTime = userCreationTime;
		this.pwHash = pwHash;
		this.token = token;
		this.mail_confirmation = mail_confirmation;
		this.mailConfirmationToken = mailConfirmationToken;
		this.imagePath = imagePath;
		this.points = points;
		this.pointsUpdateTime = pointsUpdateTime;
		this.status = status;
		this.homePoint=homePoint;
		this.workPoint=workPoint;
		this.team=team;
		this.trust=0;

	}
	
	public Point getWorkPoint() {
		return workPoint;
	}

	public void setWorkPoint(Point workPoint) {
		this.workPoint = workPoint;
	}

	public byte getTrust() {
		return trust;
	}

	public void setTrust(byte trust) {
		this.trust = trust;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	public void setPointModel(List<PointModel> model) {
		pointModel=model;
	}

	private String mail;
	private String name;
	private long userCreationTime;
	private String pwHash;
	private String token;
	private boolean mail_confirmation;
	private String mailConfirmationToken;
	private String imagePath;
	private int points;
	private long pointsUpdateTime;
	private int status;
	private Point homePoint;
	private Point workPoint;
	private byte trust;
	private int team;
	private List<PointModel> pointModel=new LinkedList<>();



	public UserModel(String mail, String name, String pwHash) {
		super();
		this.mail = mail;
		this.name = name;
		this.pwHash = pwHash;
		mail_confirmation = false;
		points = 0;
		userCreationTime = System.currentTimeMillis();
		pointsUpdateTime = userCreationTime;
		status = 0;
	}

	

}