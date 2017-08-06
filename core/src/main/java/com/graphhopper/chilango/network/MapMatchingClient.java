package com.graphhopper.chilango.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.graphhopper.chilango.data.MapMatchMessage;
import com.graphhopper.chilango.data.gps.GPSPoint;

public class MapMatchingClient {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket client;

	/*
	 * SocketAddress serverAddress = new InetSocketAddress(hostname, port);
	 */

	public static void main(String[] args) throws Exception {
		MapMatchingClient client = new MapMatchingClient("localhost", Constants.PORT_MAP_MATCH_INTERN);

		Map<Long, GPSPoint> gpsList = new TreeMap<Long, GPSPoint>();
		System.out.println("lala");
		gpsList.put(0L, new GPSPoint(23.2, 20.0, 0, 0));
		Map<Long, GPSPoint> result = client.match(gpsList);
		System.out.println(result.size());
	}

	public MapMatchingClient(String hostname, int port) throws SocketTimeoutException {
		client = new Socket();

		SocketAddress serverAddress = new InetSocketAddress(hostname, port);

		//Proxy proxy = new Proxy(Proxy.Type.HTTP, serverAddress);
		try {
			// System.setProperty("java.protocol.handler.pkgs",
			// "com.sun.net.ssl.internal.www.protocol");
			//System.setProperty("javax.net.ssl.trustStore", "clienttrust");
			//SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
			//client = (SSLSocket)ssf.createSocket(hostname,port);
		/*	for(String current:client.getEnabledCipherSuites())
				System.out.println(current);
			System.out.println("----");
			for(String current:client.getEnabledProtocols())
				System.out.println(current);
			//client.
			client.setEnableSessionCreation(true);
			client.startHandshake();*/
			client.connect(serverAddress, 5000);

			// client = SSLSocketFactory.getDefault()
			// .createSocket(hostname, port);

			//client.connect(serverAddress);
			System.out.println(client.isConnected());
			// client.connect(serverAddress,ServerConnection.timeout);
			System.out.println("connection to server established");

		} catch (SocketTimeoutException socketTimeout) {
			System.out.println("TimeOut Server Connection");
			throw socketTimeout;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Map<Long, GPSPoint> match(Map<Long, GPSPoint> gpsList) {
		try {
			System.out.println(gpsList.size()+" size of input");
			out = new ObjectOutputStream(client.getOutputStream());
			System.out.println("in/out-stream created");

			out.writeObject(new MapMatchMessage(gpsList));
			in = new ObjectInputStream(client.getInputStream());
			MapMatchMessage message = (MapMatchMessage) in.readObject();

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
