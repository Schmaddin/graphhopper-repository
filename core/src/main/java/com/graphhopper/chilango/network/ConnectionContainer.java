package com.graphhopper.chilango.network;

public class ConnectionContainer {
	private ServerMessageAuth auth;
	private RequestMessage message;
	private ConnectionMessage status;
	
	public ServerMessageAuth getAuth() {
		return auth;
	}
	public void setAuth(ServerMessageAuth auth) {
		this.auth = auth;
	}
	public RequestMessage getMessage() {
		return message;
	}
	public void setMessage(RequestMessage message) {
		this.message = message;
	}
	public ConnectionMessage getConnectionStatus() {
		return status;
	}
	public void setConnectionStatus(ConnectionMessage connectionStatus) {
		this.status = connectionStatus;
	}
	
	public ConnectionContainer(ServerMessageAuth auth, RequestMessage message, ConnectionMessage connectionStatus) {
		super();
		this.auth = auth;
		this.message = message;
		this.status = connectionStatus;
	}


}
