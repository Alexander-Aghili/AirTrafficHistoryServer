package com.airtraffic.history.automated;

import com.airtraffic.history.automated.ZoneCover;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.airtraffic.history.database.DocumentKeyValueStore;
import com.airtraffic.history.models.AircraftDataStorage;
import com.airtraffic.history.models.GeoPoint;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetTraffic {
	
	private static OkHttpClient client = 
				new OkHttpClient()
				.newBuilder()
				.readTimeout(30, TimeUnit.SECONDS) //30 second timeout
				.build();
		
	
	private static final String HOST_URL = "https://api.adsb.lol/v2/";
	private static final int DELAY = 5; 
	
	public static DocumentKeyValueStore getAllStates() throws NullPointerException {
		ArrayList<AircraftDataStorage> aircraftDataList = new ArrayList<AircraftDataStorage>();
		
		String requestURL = HOST_URL + "/states/all?time=" + (Instant.now().getEpochSecond()-DELAY);			
		
		String s = makeRequest(requestURL);
		//System.out.println(s);
		JSONObject response = new JSONObject(s);
		//TODO Catch error if no states returned
		//This terrible try catch hopefully is temporary
		try {
			
			JSONArray listOfStates = (JSONArray) response.getJSONArray("states");
			
			for (int j = 0; j < listOfStates.length(); j++) {
				JSONArray aircraftJson = listOfStates.getJSONArray(j);
				aircraftDataList.add(new AircraftDataStorage(aircraftJson));
			}

		} catch (JSONException e) {
			e.printStackTrace();
			System.exit(0);
		}

		return new DocumentKeyValueStore(response.getLong("time"), aircraftDataList);
		
	}
	
	public static void main(String[] args) {
		GeoPoint[] list = new GeoPoint[4];
	    list[0] = new GeoPoint(55.730905, -142.308217);
        list[1] = new GeoPoint(55.004438, -46.589325);
        list[2] = new GeoPoint(17.986967, -72.038340);
        list[3] = new GeoPoint(20.283103, -122.9337958);

        double radiusMiles = 250;
        List<GeoPoint> ret = ZoneCover.generateCircleCenters(list, radiusMiles);
        List<GeoPoint> uni = ret.stream() 
                            .distinct() 
                            .collect(Collectors.toList()); 

        for (GeoPoint g : uni) {
    		String requestURL = HOST_URL + "point/" + g.getLat() + "/" + g.getLon() + "/250";
            System.out.println(requestURL);
    		String s = makeRequest(requestURL);
    		JSONObject response = new JSONObject(s);
    		
            System.out.println(response);
        }
	}

	//Makes Authenticated Request and returns body(as string)
	private static String makeRequest(String url) {
		//Building OKHTTP request
		Request request = new Request.Builder()
		      .url(url)
		      .build();

		
		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Return nothing if execution of request fails 
		return null;
	}

}
