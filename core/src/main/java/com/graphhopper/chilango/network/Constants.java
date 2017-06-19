package com.graphhopper.chilango.network;

public class Constants {
	//Transaction-Types
	public static final int TRANSACTION_TASK = 1;
	public static final int TRANSACTION_MESSAGE = 2;
	
	//Transaction-Status
	public static final int TRANSACTION_PENDING = 0;
	public static final int TRANSACTION_DENIED = -1;
	public static final int TRANSACTION_ACCEPTED = 1;
	
	
	
	
	//Server-IP
	public static final String SERVER_IP = "213.133.103.173";
	
	// Ports
	//server
	public final static int SERVER_PORT_NUMBER = 3333;
	//mail
	public final static int PORT_MAIL_CHECK = 8090;
	//map-match
	public final static int PORT_MAP_MATCH = 1337;
}
