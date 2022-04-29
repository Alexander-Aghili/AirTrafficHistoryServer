package com.airtraffic.history.automated;

import java.util.ArrayList;

import com.airtraffic.history.database.DatabaseConnector;
import com.airtraffic.history.database.DocumentKeyValueStore;

//RequestAndStore Runnable
//Requests Data from API and Stores it in MongoDB database
public class RequestAndStore implements Runnable {

	//DatabaseConnector that is given by ThreadManager
	//Using ThreadManagers DatabaseConnector in order to ensure that the locking mechanism works
	//Building a new DatabaseConnector could lead to multiple threads writing simultaneously	
	private DatabaseConnector databaseConnector;
	
	//An ArrayList of previous timestamps used to avoid restoring the same data in the database
	private ArrayList<Long> previousTimestamps = new ArrayList<Long>();
	
	//Constant of maximum timestamps to be held in the past
	private static final int MAXIMUM_TIMESTAMPS = 50;
	
	
	public RequestAndStore(DatabaseConnector dbConnector) {
		this.databaseConnector = dbConnector;
	}
	
	@Override
	public void run() {
		//Gets the current states (In DocumentKeyValueStore)
		//This Thread waits synchronously for the data to be returned
		DocumentKeyValueStore requestData = GetTraffic.getAllStates();
		
		//@Future
		// Lock thread if necessary at first if
		// Log properly
		
		//The data of this timestamp is only added if it hasn't been added in the past
		//If it has been added in the past, the thread will end
		//Otherwise it will add the data, add the timestamp to the list
		if (!previousTimestamps.contains(requestData.getKey())) {
			
			databaseConnector.addData(requestData);
			//If a thread switchs here and the two threads it switches between has the same
			//timestamp, it is possible that the data is added twice. 
			//Might want to lock this between threads for saftey.
			previousTimestamps.add(requestData.getKey());
			
			System.out.println(requestData.getKey());
		}
		
		//Requests have a maxmimum keep alive time of 30 seconds
		//This means that previousTimestamps should only hold 15 possible past request (At 1 requests every 2 seconds)
		//After that, holding those timestamps doesn't make sense because the data cannot come in
		//I use 50 in case there are other issues with timestamps (Low memory usage anyway)
		if (previousTimestamps.size() > MAXIMUM_TIMESTAMPS) {
			previousTimestamps.remove(0); //Removes the first timestamp in the list
			//Might be better to remove the lowest value instead of first index
		}
		
	}

}
