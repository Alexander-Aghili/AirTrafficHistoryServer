package com.airtraffic.history.automated;

import com.airtraffic.history.database.DatabaseConnector;

public class ClearDatabase implements Runnable {

	private DatabaseConnector databaseConnector;
	
	public ClearDatabase(DatabaseConnector dbConnector) {
		this.databaseConnector = dbConnector;
	}
	
	@Override
	public void run() {
		
	}

}
