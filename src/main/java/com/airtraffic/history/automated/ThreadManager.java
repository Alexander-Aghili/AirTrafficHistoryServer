package com.airtraffic.history.automated;

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
	    
		databaseConnector.checkAndClearOldData(0);
		runScheduledServices();
	}
	
	public static void runScheduledServices() {
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	    
	    RequestAndStore requestingRunnable = new RequestAndStore(databaseConnector);
	    ClearDatabase clearingRunnable = new ClearDatabase(databaseConnector);
	    
	    //request data every two seconds
	    executorService.scheduleAtFixedRate(new Runnable() {
			
	    	@Override
			public void run() {
	    		//Sometimes get SocketExceptions
	    		Thread requestingThread = new Thread(requestingRunnable);
	    		
	    		//OKAY so threads going up in increments which seems bad so fix that
	    		//ei Thread-n++
	    		requestingThread.start();
			}
	    	
	    }, 0, 2, TimeUnit.SECONDS);
	    
	    
	    //Check documents to delete every 10 seconds
	    executorService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
	    		Thread clearingThread = new Thread(clearingRunnable);
	    		clearingThread.start();

			}
	    	
	    }, 3600, 10, TimeUnit.SECONDS); //Initial delay of one hour to start clearing
	}
}
