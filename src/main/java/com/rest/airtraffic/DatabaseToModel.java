package com.rest.airtraffic;

import java.util.ArrayList;
import java.util.Iterator;

import org.bson.Document;

import com.airtraffic.history.database.DatabaseConnector;
import com.airtraffic.history.models.Aircraft;
import com.airtraffic.history.models.AircraftDataClient;
import com.airtraffic.history.models.AreaBounds;

//Never forget to close databaseConnector prior to returning data or else memory leak
public class DatabaseToModel {
	
	//Database Connector
	//Built for each client connection
	private DatabaseConnector databaseConnector = DatabaseConnector
			.Builder
			.newBuilder()
			.setDefault()
			.build();
	
	public DatabaseToModel() {}
	
	public ArrayList<Aircraft> getElapsedAreaTraffic(AreaBounds area, long firstTimestamp, long lastTimestamp) {
		Iterator<Document> timeDocuments = databaseConnector.getDocumentsInTimeFrame(firstTimestamp, lastTimestamp);
		ArrayList<Aircraft> aircraftList = new ArrayList<Aircraft>();		
		
		while (timeDocuments.hasNext()) {
			
			Document currentTimeDocument = timeDocuments.next();
			
			@SuppressWarnings("unchecked")
			ArrayList<Document> statesDocuments = (ArrayList<Document>) currentTimeDocument.get("states");
			
			Long timestamp = currentTimeDocument.getLong("time");
			
			
			for (Document currentStateDocument : statesDocuments) {
				//If getDocumentsInTimeFrameAndArea works, this if statement is unnecessary
				if (currentStateDocumentInArea(currentStateDocument, area)) {
					
					try {		
						getAircraftInListFromIcao(aircraftList, currentStateDocument.getString("icao_id")).addAircraftData(new AircraftDataClient(currentStateDocument, timestamp));
					} catch (IndexOutOfBoundsException e) {
						aircraftList.add(new Aircraft(currentStateDocument.getString("icao_id"), new AircraftDataClient(currentStateDocument, timestamp)));
					}
				
				}
			}
		}
		
		//Must close database prior to returning data from REST API 
		//or else there is a memory leak as the database stays connected and keeps this thread alive.
		databaseConnector.close(); 
		
		return aircraftList;
	}
	
	private boolean currentStateDocumentInArea(Document currentStateDocument, AreaBounds area) {
		if (currentStateDocument.getDouble("latitude") > area.getLamin() 
						&& currentStateDocument.getDouble("latitude") < area.getLamax()
						&& currentStateDocument.getDouble("longitude") > area.getLomin()
						&& currentStateDocument.getDouble("longitude") < area.getLomax()) {
			return true;
		} else return false;
	}
	
	//Returns Aircraft (same location in memory of Aircraft in List) that has same icao24 code of input
	//@Throw IndexOutOfBoundsException when Aircraft is not in list
	//Goes throw list linearly
	//TODO: Consider if sorting aircraft by ICAO and doing binary search is faster
	private Aircraft getAircraftInListFromIcao(ArrayList<Aircraft> aircraftList, String icao24) {
		for (int i = 0; i < aircraftList.size(); i++) {
			if (aircraftList.get(i).getIcaoCode().equals(icao24)) return aircraftList.get(i);
		}
		throw new IndexOutOfBoundsException();
	}
	
}
