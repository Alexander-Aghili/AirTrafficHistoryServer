package com.airtraffic.history.models;

import java.util.ArrayList;

import org.json.JSONArray;

/*
 * Return JSONArray because its easy to turn these into strings later for returning HTTP Requests
 * Better for JSONObject.put("key", aircraftDataListToJson(aircraftList)); as seen in toJson method of Aircraft class
 * 
 * These JSON are "pancaked"
 * That is to say...
 * 	1. aircraftListToJson will call the toJson method in the Aircraft class for each aircraft in the list
 * 	2. The toJson method in the Aircraft class class the aircraftDataListToJson method for the list of AircraftData it has
 *  3. aircraftDataListToJson will call the toJson method in the AircraftData class for each AircraftData in the list
 * and that is where the pancaking stops.
 * This is relevant for those attempting to debug a stacktrace of Json or other errors relating to this pancaking
 */
public class ModelListToJson 
{
	
	public static JSONArray aircraftListToJson(ArrayList<Aircraft> aircraftList) {
		JSONArray aircraftJson = new JSONArray();
		
		for (Aircraft aircraft : aircraftList) {
			aircraftJson.put(aircraft.toJson());
		}
		
		return aircraftJson;
		
	}
	
	//Dumbass method names
	public static JSONArray aircraftDataListToJSONArrayArray(ArrayList<AircraftDataClient> aircraftDataList) { 
		JSONArray aircraftDataJson = new JSONArray();
		
		for (AircraftDataClient aircraftData : aircraftDataList) {
			aircraftDataJson.put(aircraftData.toJsonArray());
		}
		
		return aircraftDataJson;
	}
	
	public static JSONArray aircraftDataListToJSONObjectArray(ArrayList<AircraftDataClient> aircraftDataList) {
		JSONArray aircraftDataJson = new JSONArray();
		
		for (AircraftDataClient aircraftData : aircraftDataList) {
			aircraftDataJson.put(aircraftData.toJson());
		}
		
		return aircraftDataJson;
	}
}
