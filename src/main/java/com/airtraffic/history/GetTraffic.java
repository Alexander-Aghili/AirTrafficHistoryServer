package com.airtraffic.history;

import java.io.IOException;
import java.time.Instant;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.airtraffic.history.models.Aircraft;
import com.airtraffic.history.models.AircraftData;
import com.airtraffic.history.models.AreaBounds;


//Testing ONLY
import java.util.Scanner;


public class GetTraffic 
{
	//For testing ONLY, do not use in production
	private static Scanner input = new Scanner(System.in);
	
	
	private static OkHttpClient client = new OkHttpClient();
	private static final String HOST_URL = "https://opensky-network.org/api";
	private static final String OPENSKY_API_PASSWORD = System.getenv("OPENSKY_API_PASSWORD");
	
	
	//Write description here
	//@Param String icao24: ICAO24 hex code for the aircraft
	//@Param long firstTimestamp: First UNIX time for the request (is limited to < 1 hour behind current UNIX time)
	//@Param int interval: The interval (in seconds) for each additional request of historical data
	public static ArrayList<AircraftData> getElapsedAircraftData(String icao24, int firstTimestamp, int interval, int elapsedTime) {
		if ((Instant.now().getEpochSecond() - firstTimestamp) > 3600) return null; //If data requested is more than 1 hour old it won't work
		
		ArrayList<AircraftData> dataList = new ArrayList<AircraftData>();
		
		String baseURL = HOST_URL + "/states/all?icao24=" + icao24;
		
		//For loop that increments by interval
		for (int i = 0; i < elapsedTime; i += interval) {
			String requestURL = baseURL + "&time=" + (firstTimestamp + i);
							
			//Makes the request and turns it into JSON
			//This may cause errors if the response isn't 200 or isn't JSON which can happen and isn't checked right now
			JSONObject response = new JSONObject(makeRequest(requestURL)); 
			
			JSONArray aircraftJson = (JSONArray) response.getJSONArray("states").get(0);
			dataList.add(new AircraftData(aircraftJson));	
		}
		
		return dataList;
	}
	
	//Currently this operates by getting making a request for constant bounds across many timestamps.
	//This is bad because we get a json array, we must go through each response by ...
	// 1. Getting ICAO 24 code
	// 2. Finding that aircraft in the ArrayList (with the same ICAO24 code)
	// 3. Adding that data to the aircraft
	//and repeat for each input of data
	//This can get quite large quite fast, maybe look for alternative ways of doing this
	public static ArrayList<Aircraft> getElapsedAreaData(AreaBounds area, int firstTimestamp, int interval, int elapsedTime) {
		if ((Instant.now().getEpochSecond() - firstTimestamp) > 3600) return null;
		
		ArrayList<Aircraft> aircraftList = new ArrayList<Aircraft>();
		
		String baseURL = HOST_URL + "/states/all?" + area.toURL();
		
		//O(n^2) which is very bad for large or dense areas :(
		for (int i = 0; i < elapsedTime; i+= interval) {
			String requestURL = baseURL + "&time=" + (firstTimestamp + i);
			//System.out.println("Request: " + requestURL);
			
			//Possible error if response body isn't JSON, not caught right now
			//TODO Catch error
			JSONObject response = new JSONObject(makeRequest(requestURL));
			
			JSONArray listOfStates = (JSONArray) response.getJSONArray("states");
			
			for (int j = 0; j < listOfStates.length(); j++) {
				JSONArray aircraftJson = listOfStates.getJSONArray(j);
				String icao24 = aircraftJson.getString(0).trim();
				
				try {

					Aircraft aircraft = getAircraftInListFromIcao(aircraftList, icao24);
					aircraft.addAircraftData(new AircraftData(aircraftJson));
				} catch (RuntimeException e) {
					//This exception occurs when the aircraft doesn't exist. 
					//This means we must create an aircraft and add it to the list.
					aircraftList.add(new Aircraft(icao24, new AircraftData(aircraftJson)));
				}	
			}
		}
		
		return aircraftList;
		
	}
	
	//Returns Aircraft (same location in memory of Aircraft in List) that has same icao24 code of input 
	private static Aircraft getAircraftInListFromIcao(ArrayList<Aircraft> aircraftList, String icao24) {
		for (int i = 0; i < aircraftList.size(); i++) {
			if (aircraftList.get(i).getIcaoCode().equals(icao24)) return aircraftList.get(i);
		}
		throw new IndexOutOfBoundsException();
	}
	
	
	
	//Makes Authenticated Request and returns body(as string)
	private static String makeRequest(String url) {
		String credential = Credentials.basic("Alexsky2", OPENSKY_API_PASSWORD);
		
		Request request = new Request.Builder()
			  .header("Authorization", credential)
		      .url(url)
		      .build();

		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
