package com.graphhopper.chilango;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TreeMap;

import javax.crypto.SealedObject;

import org.apache.commons.logging.Log;

import com.graphhopper.chilango.data.Route;
import com.graphhopper.chilango.network.EasyCrypt;

public class FileHelper {
	
    static public DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm");

    static public DateFormat recoverDate = new SimpleDateFormat("yyyy-MM-dd");

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    static private DateFormat dfSeconds = new SimpleDateFormat(DATE_FORMAT);

    public static Serializable readObject(File fileName)
    {

        Serializable object=null;
    	// Exclude in BuildHelper?
    			try (ObjectInputStream in=new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)))){
    				object = (Serializable)in.readObject();
    			} catch (FileNotFoundException e) {
    				// TODO Auto-generated catch block

    	            System.err.println("Error reading+ "+ e.getMessage());
    				return null;
    			} catch (IOException e) {
    				// TODO Auto-generated catch block

    	            System.err.println("Error reading+ "+ e.getMessage());
    				return null;
    			} catch (ClassNotFoundException e) {
    				// TODO Auto-generated catch block

    	            System.err.println("Error reading+ "+ e.getMessage());
    				return null;
    			}
    			
    	return object;

    }


    public static void writeObject(File fileName, Serializable object){

        if (fileName.exists())
            fileName.delete();

/*
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            String file = Environment.getExternalStorageDirectory()+fileName.getName();

            fileName = new File(file);
            if(fileName.exists()){
                try {
                    fileName.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/

        FileOutputStream stream = null;
        ObjectOutputStream objectStream = null;
        try {
            stream = new FileOutputStream(fileName);
            objectStream = new ObjectOutputStream(stream);

            objectStream.writeObject(object);

            objectStream.flush();


        } catch (FileNotFoundException e) {

            System.err.println("Error writing to path: "+ e.getMessage());
        } catch (IOException e) {

            System.err.println("Error writing to path: "+ e.getMessage());
        } finally {
            try {
                if(objectStream!=null)
                    objectStream.close();
                if(stream!=null)
                    stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    
	public static void writeCryptedObject(ObjectOutputStream out, EasyCrypt cryption, Serializable object) throws Exception {
		
		out.writeObject(cryption.encrypt(object));
		byte[] bytes = new byte[128];
		//out.writeObject(bytes);
		//cryption.getOuputCipher().doFinal();
		out.flush();
		System.out.println("object written");
	}

	public static Serializable readCryptedObject(ObjectInputStream inputStream, EasyCrypt cryption) throws Exception {
		SealedObject readIn = (SealedObject) inputStream.readObject();
		byte[] bytes = new byte[128];
		//inputStream.read(bytes);

		System.out.println("object read");
		return cryption.decrypt(readIn);
	}
}