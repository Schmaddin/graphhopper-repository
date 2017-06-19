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

import com.graphhopper.chilango.FileHelper;
import com.graphhopper.chilango.data.MapMatchMessage;
import com.graphhopper.chilango.data.gps.GPSPoint;
import com.graphhopper.chilango.network.ConnectionMessage.ConnectionInformation;

public class ServerConnection {

	public final static String encText = "thisIstheFirstKK";
	public final static SecretKeySpec encryptionKey = new SecretKeySpec(encText.getBytes(), "Blowfish");

	public final static String hostname = Constants.SERVER_IP;//"192.168.178.25";

	public final static int timeout = 5000;

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket client;
	private EasyCrypt cryption;
	private ConnectionMessage conncetionMessage = null;

	public ServerConnection(EasyCrypt cryption, ServerMessageAuth auth) throws Exception {
		this.cryption = cryption;

		SocketAddress serverAddress = new InetSocketAddress(hostname, Constants.SERVER_PORT_NUMBER);
		try {
			client = new Socket();
			client.connect(serverAddress, timeout);
			System.out.print("connection to server established");

		} catch (SocketTimeoutException socketTimeout) {
			System.out.print("TimeOut Server Connection");
			throw socketTimeout;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

		try {
		//	CipherOutputStream cryptionStream = (CipherOutputStream) cryption
		//			.encryptOutputStream();

			out = new ObjectOutputStream(client.getOutputStream());
			out.flush();
			System.out.println("ObjectOutputStream created");

			// write Operations
			System.out.println("encrypt Object");

			FileHelper.writeCryptedObject(out, cryption, auth);

			in = new ObjectInputStream(/*cryption.decryptInputStream(*/client.getInputStream()/*)*/);
			System.out.println("ObjectInputtStream created");

			conncetionMessage = (ConnectionMessage) FileHelper.readCryptedObject(in, cryption);

			System.out.println("object received");

			System.out.println("status: " + conncetionMessage.getInfoConnection().name() + " "
					+ conncetionMessage.getAdditionalField());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	public RequestMessage request(RequestMessage message) {
		try {
			FileHelper.writeCryptedObject(out, cryption, message);

			return (RequestMessage) FileHelper.readCryptedObject(in, cryption);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new RequestMessage(RequestType.close, null);
		}

	}

	public ConnectionMessage getConnectionMessage() {
		return conncetionMessage;
	}

	public void close() {
		try {
			if (in != null)
				in.close();

			if (out != null)
				out.close();

			if (client != null)
				client.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
