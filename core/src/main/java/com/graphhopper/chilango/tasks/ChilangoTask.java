package com.graphhopper.chilango.tasks;

import java.io.Serializable;

/**
 * Created by martinwurflein on 25.04.17.
 */

public abstract class ChilangoTask implements Serializable{


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final String name;
    protected final String path;
    protected final double latitude;
    protected final double longitude;
    protected final String type;
    protected final long uploadTime;
    protected final long lastEdit;
    protected final long ticket;
    protected final String imagePath;



    protected transient int iconId;
    
    protected ChilangoTask(ChilangoTask task,long uploadTime,long ticket){
    	name=task.getName();
    	path=task.getPath();
    	latitude=task.getLatitude();
    	longitude=task.getLongitude();
    	type=task.getType();
    	lastEdit=task.getLastEdit();
    	imagePath=task.getImagePath();
    	this.uploadTime=uploadTime;
    	this.ticket=ticket;
    }
    
    protected ChilangoTask(String path,String type,String name,double latitude,double longitude,long uploadTime,long lastEdit,long ticket,String imagePath) {
        this.type = type;
        this.path = path;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.uploadTime = uploadTime;
        this.lastEdit = lastEdit;
        this.ticket = ticket;
        this.imagePath = imagePath;
    }

    public String getType(){ return type; }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getPath() {
        return path;
    }

    public String getImagePath() {
        return imagePath;
    }
    
    public String getName() {  return name; }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getIconId() {
        return iconId;
    }

    public long getUploadTime() {
        return uploadTime;
    }

    public long getLastEdit() {
        return lastEdit;
    }

    public long getTicket() { return ticket; }

}
