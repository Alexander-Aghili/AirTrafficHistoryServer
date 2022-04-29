package com.airtraffic.history.models;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;

//Reference https://openskynetwork.github.io/opensky-api/rest.html for API


//Aircraft Data Class based on JSON response information from OpenSkyApi
//This hosts data for each timestamp.

//TODO
//Also consider better ways to store callsign, 
//since it can change within a time period but sending it every update is kinda terrible
public abstract class AircraftData 
{
	private String callsign; 		//8 char callsign, can be null
	private double longitude;		//WGS-84 longitude in decimal degrees
	private double latitude;			//WGS-84 latitude in decimal degrees
	private double baroAltitude;		//Barometric altitude in meters
	private boolean onGround;		//Boolean value which indicates if the position was retrieved from a surface position report
	private double velocity;			//Velocity over ground in m/s
	private double trueTrack; 		//True track in decimal degrees clockwise from north
	private double verticalRate; 	//Vertical rate in m/s. + = climbing. - = decending
	private double geoAltitude;		//Geometric altitude in meters
	private String squawk;			//Squawk code
	
	
	//JSON input for Aircraft
	public AircraftData(JSONArray json) {
		
		//Null Checking
		//Only floats, squawk, and callsign can be null 
		//Therefore that is checked and replaced if necessary
		for (int i = 0; i < json.length(); i++) {
			if (json.get(i).equals(null)) {
				
				if (i != 14 || i != 1) 		//Checks if not Squawk or Callsign as those are Strings
					json.put(i, 0);		 	//0 Represents null value for floats
				else
					json.put(i, "");		//Empty String represents null value for Strings
			}
		}
		
		
		this.callsign 			= json.getString(1).trim();
		this.longitude 			= json.getDouble(5);
		this.latitude 			= json.getDouble(6);
		this.baroAltitude 		= json.getDouble(7);
		this.onGround 			= json.getBoolean(8);
		this.velocity 			= json.getDouble(9);
		this.trueTrack			= json.getDouble(10);
		this.verticalRate		= json.getDouble(11);
		this.geoAltitude		= json.getDouble(13);
		
		if (json.get(14) instanceof String)
			this.squawk 			= json.getString(14).trim();
		else 
			this.squawk = "";
		
	}

	//TODO: Finish this and the AircraftDataClient aspect
	public AircraftData(Document document) {
		this.callsign 				= document.getString("callsign");
		this.longitude				= document.getDouble("longitude");
		this.latitude				= document.getDouble("latitude");
		this.baroAltitude 			= document.getDouble("baroAltitude");
		this.onGround				= document.getBoolean("onGround");
		this.velocity 				= document.getDouble("velocity");
		this.trueTrack 				= document.getDouble("trueTrack");
		this.verticalRate 			= document.getDouble("verticalRate");
		this.geoAltitude 			= document.getDouble("geoAltitude");
		this.squawk 				= document.getString("squawk");
	}
	
	public String getCallsign() {
		return callsign;
	}


	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}

	public double getLongitude() {
		return longitude;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double latitiude) {
		this.latitude = latitiude;
	}


	public double getBaroAltitude() {
		return baroAltitude;
	}


	public void setBaroAltitude(double baroAltitude) {
		this.baroAltitude = baroAltitude;
	}


	public boolean isOnGround() {
		return onGround;
	}


	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}


	public double getVelocity() {
		return velocity;
	}


	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}


	public double getTrueTrack() {
		return trueTrack;
	}


	public void setTrueTrack(double trueTrack) {
		this.trueTrack = trueTrack;
	}


	public double getVerticalRate() {
		return verticalRate;
	}


	public void setVerticalRate(double verticalRate) {
		this.verticalRate = verticalRate;
	}


	public double getGeoAltitude() {
		return geoAltitude;
	}


	public void setGeoAltitude(double geoAltitude) {
		this.geoAltitude = geoAltitude;
	}


	public String getSquawk() {
		return squawk;
	}


	public void setSquawk(String squawk) {
		this.squawk = squawk;
	}
	
	@Override
	public abstract String toString();
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		
		json.put("callsign", this.callsign);
		json.put("longtitude", this.longitude);
		json.put("latitude", this.latitude);
		json.put("baroAlt", this.baroAltitude);
		json.put("onGround", this.onGround);
		json.put("velocity", this.velocity);
		json.put("trueTrack", this.trueTrack);
		json.put("verticalRate", this.verticalRate);
		json.put("geoAlt", this.geoAltitude);
		json.put("Squawk", this.squawk);
		
		return json;
	}
	
	
	public JSONArray toJsonArray() {
		JSONArray json = new JSONArray();
		json.put(0);
		json.put(callsign);
		json.put(longitude);
		json.put(latitude);
		json.put(baroAltitude);
		json.put(onGround);
		json.put(velocity);
		json.put(trueTrack);
		json.put(verticalRate);
		json.put(geoAltitude);
		json.put(squawk);
		
		return json;
	}
}	
