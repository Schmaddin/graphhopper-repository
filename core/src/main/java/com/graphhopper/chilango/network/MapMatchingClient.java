package com.graphhopper.chilango.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import com.graphhopper.chilango.data.MapMatchMessage;
import com.graphhopper.chilango.data.gps.GPSPoint;
import com.graphhopper.util.GPXEntry;

public class MapMatchingClient {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket client;

	public MapMatchingClient(String hostname) throws SocketTimeoutException {
		client = new Socket();
		
		SocketAddress serverAddress = new InetSocketAddress(hostname, Constants.PORT_MAP_MATCH);
		try {
			client.connect(serverAddress,ServerConnection.timeout);
			System.out.print("connection to server established");

		} catch(SocketTimeoutException socketTimeout)
		{
			System.out.print("TimeOut Server Connection");
			throw socketTimeout;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public Map<Long,GPSPoint> match(Map<Long,GPSPoint> gpsList){
		
		try {
			out = new ObjectOutputStream(client.getOutputStream());
			in = new ObjectInputStream(client.getInputStream());
			System.out.println("in/out-stream created");
			out.writeObject(new MapMatchMessage(gpsList));

			MapMatchMessage message=(MapMatchMessage)in.readObject();
			
			
			return message.getGpsList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}

	public void close() {
		try {
			out.writeObject(new MapMatchMessage(2));
			in.close();

			out.close();

			client.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
