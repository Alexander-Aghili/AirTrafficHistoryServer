package com.airtraffic.history.models;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

public class AircraftDataClient extends AircraftData {
	
	private long timestamp;			//Unix timestamp (seconds) for the last position updated
	
	
	public AircraftDataClient(JSONArray json) {
		super(json);
		this.timestamp			= json.getLong(3);
	}
	
	public AircraftDataClient(Document document, long timestamp) {
		super(document);
		this.timestamp = timestamp;
	}
	
	public long getTimestamp() {
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
		return timestamp + ": (" + super.getLatitude() + ", " + super.getLongitude() + ")";
	}

}
