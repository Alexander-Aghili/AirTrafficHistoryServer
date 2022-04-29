package com.airtraffic.history.automated;

import com.airtraffic.history.database.DatabaseConnector;

//ClearDatabase Runnable
public class ClearDatabase implements Runnable {
	
	//DatabaseConnector that is given by ThreadManager
	//Using ThreadManagers DatabaseConnector in order to ensure that the locking mechanism works
	//Building a new DatabaseConnector could lead to multiple threads writing simultaneously
	private DatabaseConnector databaseConnector;
	
	//Constant defining how long data can remain in database
	//Right now: 3600 seconds = 1 Hour
	private final int DELETE_TIME_SECONDS = 3600;
	
	public ClearDatabase(DatabaseConnector dbConnector) {
		this.databaseConnector = dbConnector;
	}
	
	@Override
	public void run() {
		//Deletes old data
		databaseConnector.checkAndClearOldData(DELETE_TIME_SECONDS);
	}

}
