package com.airtraffic.history.database;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;

import com.airtraffic.history.ThreadLock;
import com.airtraffic.history.models.AircraftDataStorage;
import com.airtraffic.history.models.AreaBounds;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;

public class DatabaseConnector 
{		
	//constant
	private final String ATCH_DB = "airTrafficHistoryDB";
	private final String TIMES_COLLECTION = "times";
	private final Object LOCK = new Object();	
	//To be established at build time
	private MongoClient mongoClient;
	
	
	public DatabaseConnector(Builder builder) {
		mongoClient = builder.mongoClient;
	}
	
	public void addData(DocumentKeyValueStore documentData) {
		Long timestamp = documentData.getKey();
		ArrayList<AircraftDataStorage> aircraftDataList = documentData.getValue();
		
		MongoCollection<Document> timesCollection = mongoClient.getDatabase(ATCH_DB).getCollection(TIMES_COLLECTION);
		Document document = new Document().append("time", timestamp);
		
		ArrayList<Document> states = new ArrayList<Document>();
		
		for (AircraftDataStorage aircraftData : aircraftDataList) {
			states.add(aircraftData.toDocument());
		}
		
		document.append("states", states);
		
		//Locked for adjusting database due to multithreading
		synchronized (LOCK) {
			timesCollection.insertOne(document);
		}
	}
	
	
	public void checkAndClearOldData(int deleteTime) {
		MongoCollection<Document> timesCollection = mongoClient.getDatabase(ATCH_DB).getCollection(TIMES_COLLECTION);
		Document deleteParameters = new Document().append("time", new Document().append("$lt", (Instant.now().getEpochSecond() - deleteTime)));
		synchronized(LOCK) {
			DeleteResult delete = timesCollection.deleteMany(deleteParameters);	
			System.out.println("Deleted " + delete.getDeletedCount() + " doucments");
		}
	}
	
	public Iterator<Document> getDocumentsInTimeFrame(long firstTimestamp, long lastTimestamp) {
		//If last < first throw timebad exception
		
		MongoCollection<Document> timesCollection = mongoClient.getDatabase(ATCH_DB).getCollection(TIMES_COLLECTION);
		Document parameters = new Document()
				.append("time", new Document()
						.append("$lt", lastTimestamp)
						.append("$gt", firstTimestamp));
		
		synchronized(LOCK) {
			return timesCollection.find(parameters).iterator();
		}
	}
	
	public Iterator<Document> getDocumentsInTimeFrameAndArea(AreaBounds area, long firstTimestamp, long lastTimestamp) {
		MongoCollection<Document> timesCollection = mongoClient.getDatabase(ATCH_DB).getCollection(TIMES_COLLECTION);
		Document parameters = new Document()
				.append("time", new Document()
						.append("$lt", lastTimestamp)
						.append("$gt", firstTimestamp))
				.append("states", new Document()
						.append("longitude", new Document()
								.append("$lt", area.getLomax())
								.append("$gt", area.getLomin()))
						.append("latitude", new Document())
								.append("$lt", area.getLamax())
								.append("$gt", area.getLamin())
				);
		
		//Might be unneccesary to lock while reading, might cause more delays
		synchronized(LOCK) {
			return timesCollection.find(parameters).iterator();
		}
	}
	
	
	//Builder class for builder architecture
	public static class Builder {
		private MongoClient mongoClient;

		public static Builder newBuilder() {
			return new Builder();
		}
		
		private Builder() {}
		
		public Builder setDefault() {
			mongoClient = new MongoClient();
			return this;
		}
		
		public DatabaseConnector build() {
			return new DatabaseConnector(this);
		}
	}
}
