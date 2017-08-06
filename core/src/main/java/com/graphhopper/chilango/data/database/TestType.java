package com.graphhopper.chilango.data.database;

import com.graphhopper.chilango.data.JsonHelper;
import com.graphhopper.chilango.network.ConnectionMessage.ConnectionInformation;
import com.graphhopper.chilango.network.HTTPServerConnection;
import com.graphhopper.chilango.network.ServerMessageAuth;

public class TestType extends SubmitTypeInterface{

	public SubmitType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	private int coolCat=0;
	
	public int logo=2;
	
	public static void main(String[] args){

		try {
			HTTPServerConnection conn=new HTTPServerConnection(new ServerMessageAuth(null, "wolfgang", "hi@byom.de", null, ConnectionInformation.LOG_IN));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
