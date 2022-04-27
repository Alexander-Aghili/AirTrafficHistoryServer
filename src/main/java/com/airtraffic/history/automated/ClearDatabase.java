package com.airtraffic.history.automated;

import com.airtraffic.history.database.DatabaseConnector;

public class ClearDatabase implements Runnable {

	private DatabaseConnector databaseConnector;
	private final int DELETE_TIME_SECONDS = 3600;
	
	public ClearDatabase(DatabaseConnector dbConnector) {
		this.databaseConnector = dbConnector;
	}
	
	@Override
	public void run() {
		databaseConnector.checkAndClearOldData(DELETE_TIME_SECONDS);
	}

}
