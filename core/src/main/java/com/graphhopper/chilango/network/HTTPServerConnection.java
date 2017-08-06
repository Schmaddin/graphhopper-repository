package com.graphhopper.chilango.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

import com.graphhopper.chilango.FileHelper;
import com.graphhopper.chilango.data.JsonHelper;
import com.graphhopper.chilango.data.MapMatchMessage;
import com.graphhopper.chilango.data.gps.GPSPoint;
import com.graphhopper.chilango.network.ConnectionMessage.ConnectionInformation;

public class HTTPServerConnection implements ServerConnection {

	public final static String hostname = Constants.SERVICE_SERVER;

	public final static int timeout = 5000;

	private ConnectionMessage conncetionMessage = null;

	public HTTPServerConnection(ServerMessageAuth auth) throws Exception {
		this(auth, null,null);
	}

	public HTTPServerConnection(ServerMessageAuth auth, String host) throws Exception {
		this(auth,null,host);
	}
	
	
	public HTTPServerConnection(ServerMessageAuth auth,RequestMessage request) throws Exception{
		this(auth,request,null);
	}

	public HTTPServerConnection(ServerMessageAuth auth,RequestMessage request, String host) throws Exception {

		if (host == null)
			host = hostname;

			ConnectionContainer fullMessage = new ConnectionContainer(auth,request,null);
			String json=JsonHelper.createJsonFromObjectAndroid(fullMessage);


			// write Operations
			System.out.println(json);
			String result=HTTPSRequest.call(host, json);
			System.out.println("object received");
			System.out.println(result);

//			conncetionMessage = (ConnectionMessage) FileHelper.readCryptedObject(in, cryption);



		//	System.out.println("status: " + conncetionMessage.getInfoConnection().name() + " "
		//			+ conncetionMessage.getAdditionalField());

	}



	public ConnectionMessage getConnectionMessage() {
		return conncetionMessage;
	}


}
