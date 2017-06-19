package com.graphhopper.chilango.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHelper {

	public static boolean writeObjectAsJson(String file, Object object) {

		return writeJsonToFile(createJsonFromObject(object), file);
	}

	public static String createJsonFromObject(Object object) {
		GsonBuilder newGsonBuilder = new GsonBuilder().serializeSpecialFloatingPointValues();
		Gson gson = newGsonBuilder.create();
		return gson.toJson(object);
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

	public static Object parseJson(String json,Class classType){
		GsonBuilder newGsonBuilder = new GsonBuilder().serializeSpecialFloatingPointValues();
		Gson gson = newGsonBuilder.create();
		return (Object) gson.fromJson(json, classType);
	}

}
