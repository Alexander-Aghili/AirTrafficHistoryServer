package com.airtraffic.history.models;

import org.json.JSONArray;
import org.json.JSONObject;

public class AircraftDataClient extends AircraftData {
	
	private int timestamp;			//Unix timestamp (seconds) for the last position updated
	
	
	public AircraftDataClient(JSONArray json) {
		super(json);
		this.timestamp			= json.getInt(3);
	}
	
	public int getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public JSONObject toJson() {
		JSONObject json = super.toJson();
		json.put("timestamp", this.timestamp);
		return json;
	}
	
	@Override
	public JSONArray toJsonArray() {
		JSONArray json = super.toJsonArray();
		json.put(0, timestamp);
		return json;
	}

	@Override
	public String toString() {
		return "AircraftData";
	}

}
