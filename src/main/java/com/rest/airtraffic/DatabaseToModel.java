package com.rest.airtraffic;

import java.util.ArrayList;
import java.util.Iterator;

import org.bson.Document;

import com.airtraffic.history.database.DatabaseConnector;
import com.airtraffic.history.models.Aircraft;
import com.airtraffic.history.models.AircraftDataClient;
import com.airtraffic.history.models.AreaBounds;

public class DatabaseToModel {
	
	private DatabaseConnector databaseConnector = DatabaseConnector
			.Builder
			.newBuilder()
			.setDefault()
			.build();
	
	public DatabaseToModel() {}
	
	//Check if databaseConnector.getArea
	public ArrayList<Aircraft> getElapsedAreaTraffic(AreaBounds area, long firstTimestamp, long lastTimestamp) {
		Iterator<Document> timeDocuments = databaseConnector.getDocumentsInTimeFrame(firstTimestamp, lastTimestamp);
		ArrayList<Aircraft> aircraftList = new ArrayList<Aircraft>();		
		
		while (timeDocuments.hasNext()) {
			Document currentTimeDocument = timeDocuments.next();
			
			@SuppressWarnings("unchecked")
			ArrayList<Document> statesDocuments = (ArrayList<Document>) currentTimeDocument.get("states");
			
			Long timestamp = currentTimeDocument.getLong("time");
			
			for (Document currentStateDocument : statesDocuments) {
				if (currentStateDocument.getDouble("latitude") > area.getLamin() 
						&& currentStateDocument.getDouble("latitude") < area.getLamax()
						&& currentStateDocument.getDouble("longitude") > area.getLomin()
						&& currentStateDocument.getDouble("longitude") < area.getLomax()) {
					try {
						getAircraftInListFromIcao(aircraftList, currentStateDocument.getString("icao_id")).addAircraftData(new AircraftDataClient(currentStateDocument, timestamp));
					} catch (IndexOutOfBoundsException e) {
						aircraftList.add(new Aircraft(currentStateDocument.getString("icao_id"), new AircraftDataClient(currentStateDocument, timestamp)));
					}
				}
			}
		}
		
		return aircraftList;
	}
	
	//Returns Aircraft (same location in memory of Aircraft in List) that has same icao24 code of input 
	private Aircraft getAircraftInListFromIcao(ArrayList<Aircraft> aircraftList, String icao24) {
		for (int i = 0; i < aircraftList.size(); i++) {
			if (aircraftList.get(i).getIcaoCode().equals(icao24)) return aircraftList.get(i);
		}
		throw new IndexOutOfBoundsException();
	}
	
}
