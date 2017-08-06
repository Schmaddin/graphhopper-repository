package com.graphhopper.chilango.tasks;

import java.io.Serializable;

import com.github.filosganga.geogson.model.Geometry;
import com.github.filosganga.geogson.model.Point;
import com.graphhopper.chilango.data.database.SubmitType;
import com.graphhopper.chilango.data.database.SubmitTypeInterface;

/**
 * Created by martinwurflein on 25.04.17.
 */

public abstract class ChilangoTask extends SubmitTypeInterface implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final String name;
    protected final String path;
    protected final double latitude;
    protected final double longitude;
    protected final int type;
    protected final long uploadTime;
    protected final long lastEdit;
    protected final int transactionId;
    protected final String imagePath;



    protected transient int iconId;
    
    protected ChilangoTask(ChilangoTask task,long uploadTime,int transactionId){
    	name=task.getName();
    	path=task.getPath();
    	latitude=task.getLatitude();
    	longitude=task.getLongitude();
    	type=task.getType().getValue();
    	lastEdit=task.getLastEdit();
    	imagePath=task.getImagePath();
    	this.uploadTime=uploadTime;
    	this.transactionId=transactionId;
    }
    
    protected ChilangoTask(String path,SubmitType type,String name,double latitude,double longitude,long uploadTime,long lastEdit,int transactionId,String imagePath) {
        this.type = type.getValue();
        this.path = path;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.uploadTime = uploadTime;
        this.lastEdit = lastEdit;
        this.transactionId = transactionId;
        this.imagePath = imagePath;
    }

    public SubmitType getType(){ return SubmitType.getByValue(type); }

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

    public int getTransactionId() { return transactionId; }
    

}
