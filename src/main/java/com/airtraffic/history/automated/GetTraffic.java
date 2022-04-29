package com.airtraffic.history.automated;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.airtraffic.history.database.DocumentKeyValueStore;
import com.airtraffic.history.models.AircraftDataStorage;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetTraffic {
		
	//TODO Configure HTTP Client
	private static OkHttpClient client = 
				new OkHttpClient()
				.newBuilder()
				.readTimeout(30, TimeUnit.SECONDS) //30 second timeout
				.build();
		
	private static final String HOST_URL = "https://opensky-network.org/api";
	private static final String OPENSKY_API_PASSWORD = System.getenv("OPENSKY_API_PASSWORD");
	
	public static DocumentKeyValueStore getAllStates() {
		ArrayList<AircraftDataStorage> aircraftDataList = new ArrayList<AircraftDataStorage>();
		
		String requestURL = HOST_URL + "/states/all";			
		
		//Possible error if response body isn't JSON, not caught right now
		//TODO Catch error
		JSONObject response = new JSONObject(makeRequest(requestURL));
		
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
