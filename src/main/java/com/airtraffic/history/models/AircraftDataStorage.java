package com.airtraffic.history.models;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;


public class AircraftDataStorage extends AircraftDataClient{

	private String icaoCode;
	
	public AircraftDataStorage(JSONArray json) {
		super(json);
		this.icaoCode = json.getString(0);
	}
	
	public String getIcaoCode() {
		return icaoCode;
	}

	public void setIcaoCode(String icaoCode) {
		this.icaoCode = icaoCode;
	}
	
	public Document toDocument() {
		return new Document("icao_id", icaoCode)
				.append("callsign", super.getCallsign())
				.append("longtitude", super.getLongitude())
				.append("latitude", super.getLatitude())
				.append("baroAltitude", super.getBaroAltitude())
				.append("onGround", super.isOnGround())
				.append("velocity", super.getVelocity())
				.append("trueTrack", super.getTrueTrack())
				.append("verticalRate", super.getVerticalRate())
				.append("geoAltitude", super.getGeoAltitude())
				.append("squawk", super.getSquawk());
	}
	
	@Override
	public JSONObject toJson() {
		JSONObject json = super.toJson();
		json.append("icao_id", icaoCode);
		return json;
	}
	
	@Override
	public String toString() {
		return icaoCode;
	}

}
