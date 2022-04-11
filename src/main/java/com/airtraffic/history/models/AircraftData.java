package com.airtraffic.history.models;

import org.json.JSONArray;

//Reference https://openskynetwork.github.io/opensky-api/rest.html for API


//Aircraft Data Class based on JSON response information from OpenSkyApi
//This hosts data for each timestamp.

//TODO
//Consider whether you need to store originCountry
//Also consider better ways to store callsign, 
//since it can change within a time period but sending it every update is kinda terrible
public class AircraftData 
{
	private int timestamp;			//Unix timestamp (seconds) for the last position updated
	private String callsign; 		//8 char callsign, can be null
	private String originCountry; 	//Country name inferred from the ICAO 24-bit address
	private float longitude;		//WGS-84 longitude in decimal degrees
	private float latitiude;		//WGS-84 latitude in decimal degrees
	private float baroAltitude;		//Barometric altitude in meters
	private boolean onGround;		//Boolean value which indicates if the position was retrieved from a surface position report
	private float velocity;			//Velocity over ground in m/s
	private float trueTrack; 		//True track in decimal degrees clockwise from north
	private float verticalRate; 	//Vertical rate in m/s. + = climbing. - = decending
	private float geoAltitude;		//Geometric altitude in meters
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
		
		this.callsign 			= json.getString(1);
		this.originCountry 		= json.getString(2);
		this.timestamp			= json.getInt(3);
		this.longitude 			= json.getFloat(5);
		this.latitiude 			= json.getFloat(6);
		this.baroAltitude 		= json.getFloat(7);
		this.onGround 			= json.getBoolean(8);
		this.velocity 			= json.getFloat(9);
		this.trueTrack			= json.getFloat(10);
		this.verticalRate		= json.getFloat(11);
		this.geoAltitude		= json.getFloat(13);
		this.squawk 			= json.getString(14);
	}

	public String getCallsign() {
		return callsign;
	}


	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}


	public String getOriginCountry() {
		return originCountry;
	}


	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}


	public int getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public float getLongitude() {
		return longitude;
	}


	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}


	public float getLatitiude() {
		return latitiude;
	}


	public void setLatitiude(float latitiude) {
		this.latitiude = latitiude;
	}


	public float getBaroAltitude() {
		return baroAltitude;
	}


	public void setBaroAltitude(float baroAltitude) {
		this.baroAltitude = baroAltitude;
	}


	public boolean isOnGround() {
		return onGround;
	}


	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}


	public float getVelocity() {
		return velocity;
	}


	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}


	public float getTrueTrack() {
		return trueTrack;
	}


	public void setTrueTrack(float trueTrack) {
		this.trueTrack = trueTrack;
	}


	public float getVerticalRate() {
		return verticalRate;
	}


	public void setVerticalRate(float verticalRate) {
		this.verticalRate = verticalRate;
	}


	public float getGeoAltitude() {
		return geoAltitude;
	}


	public void setGeoAltitude(float geoAltitude) {
		this.geoAltitude = geoAltitude;
	}


	public String getSquawk() {
		return squawk;
	}


	public void setSquawk(String squawk) {
		this.squawk = squawk;
	}
	
	@Override
	public String toString() {
		return "\t\t" + callsign + " at " + timestamp + "; Speed: " + velocity + "; Altitude:" + baroAltitude; 
	}
	
	public String toJson() {	
		//TODO implement
	}
}	
