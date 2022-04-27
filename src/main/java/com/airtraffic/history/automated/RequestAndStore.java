package com.airtraffic.history.automated;

import java.util.ArrayList;

import com.airtraffic.history.database.DatabaseConnector;
import com.airtraffic.history.database.DocumentKeyValueStore;

public class RequestAndStore implements Runnable {

	private DatabaseConnector databaseConnector;
	private ArrayList<Long> previousTimestamps = new ArrayList<Long>();
	
	public RequestAndStore(DatabaseConnector dbConnector) {
		this.databaseConnector = dbConnector;
	}
	
	@Override
	public void run() {
		DocumentKeyValueStore requestData = GetTraffic.getAllStates();
		

		//either wait and re-request or just disregard data
		//Right now I am just going to disregard the data
		if (!previousTimestamps.contains(requestData.getKey())) {
			databaseConnector.addData(requestData);
			previousTimestamps.add(requestData.getKey());
			System.out.println(requestData.getKey());
		}
		
		if (previousTimestamps.size() > 50) {
			previousTimestamps.remove(0);
			//Might be better to remove the lowest value instead of first index
		}
		
	}

}
