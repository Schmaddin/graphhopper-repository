package com.graphhopper.chilango.tasks;

import com.graphhopper.chilango.data.BusBase;
import com.graphhopper.chilango.data.database.SubmitType;

/**
 * Created by martinwurflein on 08.06.17.
 */

public class BusBaseTask extends ChilangoTask{

    public static final SubmitType typeName=SubmitType.submit_base_indication;

    private final BusBase base;

    public BusBaseTask(BusBaseTask task,long uploadTime,long ticket)
    {
    	super(task,uploadTime,ticket);
    	base=task.getBusBase();
    }
    
    public BusBaseTask(String path, BusBase base, long uploadTime, long lastEdit, long ticket,String imagePath) {
        super(path, typeName, base.getBaseName(), base.getLat(),base.getLon(),uploadTime, lastEdit, ticket,imagePath);
        this.base=base;
    }

    public BusBase getBusBase() { return base; }
}
