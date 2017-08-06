package com.graphhopper.chilango.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import com.github.filosganga.geogson.gson.GeometryAdapterFactory;
import com.github.filosganga.geogson.jts.JtsAdapterFactory;
import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import com.graphhopper.chilango.data.database.SubmitTypeInterface;

public class JsonHelper {

	public static boolean writeObjectAsJson(String file, Object object) {

		return writeJsonToFile(createJsonFromObject(object), file);
	}

	public static String createJsonFromObject(Object object) {

		GsonBuilder newGsonBuilder = new GsonBuilder().serializeSpecialFloatingPointValues().registerTypeAdapterFactory(new GeometryAdapterFactory())
				.registerTypeAdapterFactory(new JtsAdapterFactory()).registerTypeAdapter(SubmitTypeInterface.class, new InterfaceAdapter<SubmitTypeInterface>());
		Gson gson = newGsonBuilder.create();
		
		return gson.toJson(object,object.getClass());
	}

	public static String createJsonFromObjectAndroid(Object object) {

		GsonBuilder newGsonBuilder = new GsonBuilder().serializeSpecialFloatingPointValues().registerTypeAdapter(SubmitTypeInterface.class, new InterfaceAdapter<SubmitTypeInterface>());
		Gson gson = newGsonBuilder.create();
		
		return gson.toJson(object,object.getClass());
	}
	
	public static Object parseJsonAndroid(String json,Type type){
		GsonBuilder newGsonBuilder = new GsonBuilder().serializeSpecialFloatingPointValues().registerTypeAdapter(SubmitTypeInterface.class, new InterfaceAdapter<SubmitTypeInterface>());
		Gson gson = newGsonBuilder.create();

		return (Object) gson.fromJson(json, type);
	}
	
	public static boolean writeJsonToFile(String jsonString, String saveTo) {

		try (BufferedWriter w = new BufferedWriter(new FileWriter(saveTo))) {
			w.write(jsonString);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Object readJsonObjectFromFile(File file, Class classType) {
		String json = "";
		try (BufferedReader r = new BufferedReader(new FileReader(file))) {

			for (String input; (input = r.readLine()) != null;) {
				json += input;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}


		return parseJson(json,classType);
	}

	public static Object parseJson(String json,Type type){
		GsonBuilder newGsonBuilder = new GsonBuilder().serializeSpecialFloatingPointValues().registerTypeAdapterFactory(new GeometryAdapterFactory())
				.registerTypeAdapterFactory(new JtsAdapterFactory()).registerTypeAdapter(SubmitTypeInterface.class, new InterfaceAdapter<SubmitTypeInterface>());
		Gson gson = newGsonBuilder.create();
		return (Object) gson.fromJson(json, type);
	}

}
