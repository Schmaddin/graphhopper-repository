package com.graphhopper.chilango.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.graphhopper.chilango.data.gps.GPSPoint;

public class HTTPSRequest {

	static TrustManager[] dummyTrustManager = new TrustManager[] { new X509TrustManager() {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] certs, String authType) {
		}
	} };

	public static String call(final String hostname, final String json) {

		try {
			Map<Long, GPSPoint> message = new HashMap<Long, GPSPoint>();

			try {

				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, dummyTrustManager, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				URL url = new URL(hostname);

				HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			
				HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier(){
				    public boolean verify(String string,SSLSession ssls) {
				        return true;
				    }


				});
				
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
				out.write(json);
				out.close();
				System.out.println("everything written");
				
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String readLine = in.readLine();
				String total=readLine;
				while (readLine != null) {
					System.out.println(readLine);
					readLine = in.readLine();
					if(readLine!=null)
					total += readLine;
				}
				System.out.println("REST Service Invoked Successfully..");
				in.close();
				
				return total;
			} catch (Exception e) {
				System.out.println("\nError while calling REST Service");
				System.out.println(e);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
