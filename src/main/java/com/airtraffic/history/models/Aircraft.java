package com.airtraffic.history.models;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

//Aircraft class
//For Future: Maybe get Aircraft Type via a database or API if possible
public class Aircraft 
{
	private String icao24;
	private ArrayList<AircraftData> data;
	
	public Aircraft(String icao24) {
		this.icao24 = icao24;
		data = new ArrayList<AircraftData>();
	}
	
	//@Param allAircraftJsonData: 
	//JSONArray of all the json data for one aircraft in specified time interval 
	public Aircraft(String icao24, ArrayList<JSONArray> allAircraftJsonData) {
		this.icao24 = icao24;
		data = new ArrayList<AircraftData>();

		//Adds each timestamped data to list of all data for this Aircraft
		for (int i = 0; i < allAircraftJsonData.size(); i++) {
			data.add(new AircraftData(allAircraftJsonData.get(i)));
		}
		
	}
	
	public Aircraft(String icao24, AircraftData initAircraftDataPoint) {
		this.icao24 = icao24;
		data = new ArrayList<AircraftData>();
		data.add(initAircraftDataPoint);
	}
	
	
	public String getIcaoCode() {
		return this.icao24;
	}
	
	/*
	 * This is going to be done with a for loop at first since I don't know if 
	 * intervals are consistent for the timestamp. However, if the interval is 
	 * constant (eg. 1 second updates), then I can just subtract the parameter's 
	 * timestamp from the first timestamp and that will be the # in the arraylist 
	 * with that timestamp
	 */
	public AircraftData getAircraftAtTimestamp(int timestamp) {
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getTimestamp() == timestamp) return data.get(i);
		}
		return null;
		//Theoritical implementaion
		//return (timestamp - data.get(0).getTimestamp())/interval
	}
	
	public void addAircraftData(AircraftData newData) {
		data.add(newData);
	}
	
	@Override
	public boolean equals(Object o) {
		/* Check if o is an instance of Aircraft or not
		"null instanceof [type]" also returns false */
		if (!(o instanceof Aircraft)) {
			return false;
		}
		   
		// typecast o to Aircraft so that we can compare data members
		Aircraft c = (Aircraft) o;
		  
		if (c.icao24 == this.icao24) return true; else return false;  //Equals only compares ICAO24, data is irrelevant for this purpose
	}
	
	
	//Look into iterators and not having a for loop here
	@Override 
	public String toString() {
		String consoleString = "\tICAO24: " + icao24 + "\n";
		for (AircraftData aircraftDataPoint: data) {
			consoleString += aircraftDataPoint.toString() + "\n";
		}
		return consoleString + "\n";
		
	}
	
	//TODO better naming for the shit in this method
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		
		json.put("icao24", icao24);
		json.put("aircraftData", ModelListToJson.aircraftDataListToJson(data));
		
		return json;
	}
	
}
