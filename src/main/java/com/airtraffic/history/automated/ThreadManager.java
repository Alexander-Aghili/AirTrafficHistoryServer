package com.airtraffic.history.automated;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.airtraffic.history.database.DatabaseConnector;


//ThreadManager class, manages Bot threads, which perform retrieving and clearing of data
public class ThreadManager 
{
	private static DatabaseConnector databaseConnector = DatabaseConnector
														.Builder
														.newBuilder()
														.setDefault()
														.build();
	
	public static void main(String[] args) {
	    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	    
	    RequestAndStore requestingRunnable = new RequestAndStore(databaseConnector);
	    ClearDatabase clearingRunnable = new ClearDatabase(databaseConnector);
	    
	    executorService.scheduleAtFixedRate(new Runnable() {
			
	    	@Override
			public void run() {
	    		Thread requestingThread = new Thread(requestingRunnable);
	    		Thread clearingThread = new Thread(clearingRunnable);
	    		
	    		//OKAY so threads going up in increments which seems bad so fix that
	    		//ei Thread-n++
	    		requestingThread.start();
	    		//System.out.println(requestingThread.getName());
	    		clearingThread.start();
			}
	    	
	    }, 0, 1, TimeUnit.SECONDS);
	}
}
