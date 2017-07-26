package com.graphhopper.chilango.network;

public class Constants {
	//Transaction-Types
	public static final int TRANSACTION_TASK = 1;
	public static final int TRANSACTION_MESSAGE = 2;
	
	//Transaction-Status
	public static final int TRANSACTION_PENDING = 0;
	public static final int TRANSACTION_DENIED = -1;
	public static final int TRANSACTION_ACCEPTED = 1;
	public static final int TRANSACTION_CHANGE_REQUEST = 2;
	
	
	
	
	//Server-IP
	public static final String SERVER_IP = "chilango.me";
	
	public static final String MAP_MATCH_SERVER = "mapmatch."+SERVER_IP;

	public static final String MAIL_SERVER = "https://mailconf."+SERVER_IP;
	
	public static final String SERVICE_SERVER = "https://srv."+SERVER_IP;
	
	
	// Ports
	//server
	public final static int SERVER_PORT_NUMBER_INTERN = 3333;
	//mail
	public final static int PORT_MAIL_CHECK_INTERN = 8090;
	//map-match
	public final static int PORT_MAP_MATCH_INTERN = 1337;
	
	public final static int PORT_FROM_OUTSIDE = 443;
}
