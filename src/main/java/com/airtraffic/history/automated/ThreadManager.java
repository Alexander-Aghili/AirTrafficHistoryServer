package com.airtraffic.history.automated;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.airtraffic.history.database.DatabaseConnector;


/**
 * ThreadManager that manages Bot threads, performing retrieving and clearing of data.
 * 
 * @author Alexander Aghili
 *
 */

//ThreadManager class, manages Bot threads, which perform retrieving and clearing of data
public class ThreadManager 
{
	/*
	 * Database connector to be used by sub-threads to ensure synchronized writing and deleting
	 * Prevents Multithreading read/write errors to database
	 * 
	 * @Builder:
	 * 	Default
	*/	
	private static DatabaseConnector databaseConnector = DatabaseConnector
														.Builder
														.newBuilder()
														.setDefault()
														.build();
	
	
	//ThreadManager is also the Driver for access to start the processes
	//Could be altered to have a seperate driver but that would immediatly call 
	//runScheduledServices() method anyway
	public static void main(String[] args) {
		runScheduledServices();
	}
	
	/**
	 * Schedules the seperate Threads for data collection and removal.
	 * 
	 */
	public static void runScheduledServices() {
		//Creating executor for threads
		//Possibly look into different executors that might serve this purpose better
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	    
		//Instantiating the runnables
		//They are instantiated here so that class based objects can share data between threads
		//Technically not thread safe but an issues hasn't come up yet so I think its fine
	    RequestAndStore requestingRunnable = new RequestAndStore(databaseConnector);
	    ClearDatabase clearingRunnable = new ClearDatabase(databaseConnector);
	    
	    // Code below theoretically works and is much clearer but I struggled to ensure it worked
	    // over the code in implementation which I have done a bunch of tests on.
	    
	   // executorService.scheduleAtFixedRate(requestingRunnable, 0, 2, TimeUnit.SECONDS);
	   // executorService.scheduleAtFixedRate(clearingRunnable, 3600, 10, TimeUnit.SECONDS);

	    /*
	     * Running RequestAndStore runnable as a parameter of a new Runnable 
	     * with requestingRunnable opened as a new thread within that.
	     * 
	     * Scheduled to run immediatly upon being called and at every other second interval
	     * The hope is to get every 2 seconds of data but it varies to a margin not yet determined
	     * Sometimes it is 1 second differences, sometimes up to 10.
	     * 
	     * Also sometimes a SocketException is possible if a request takes more than 30 seconds to respond
	     * Currently nothing catches those errors, but since its in one thread, the program continues despite the exception
	     * 
	     * @Future
	     * 	Solve timing issues with API (If they turn out to be a big deal)
	     * 	Protect SocketException
	     * 
	     */
	    executorService.scheduleAtFixedRate(new Runnable() {
			
	    	@Override
			public void run() {
	    		//Sometimes get SocketExceptions
	    		Thread requestingThread = new Thread(requestingRunnable);
	    		requestingThread.start();
			}
	    	
	    }, 0, 2, TimeUnit.SECONDS);
	    
	    
	    //Check documents to delete every 10 seconds
	    executorService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				
	    		Thread clearingThread = new Thread(clearingRunnable);
	    		clearingThread.start();

			}
	    	
	    }, 3600, 10, TimeUnit.SECONDS); //Initial delay of one hour to start clearing
	}
}
