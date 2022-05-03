package com.airtraffic.history.database;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;

import com.airtraffic.history.models.AircraftDataStorage;
import com.airtraffic.history.models.AreaBounds;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;

/**
 * Defines all methods to communicate with MongoDB database
 *  
 * @author Alexander Aghili 
 *
 */
public class DatabaseConnector 
{		
	//Constants
	private final String ATCH_DB = "airTrafficHistoryDB";	//Database name
	private final String TIMES_COLLECTION = "times";		//Times collection name
	
	//This does not need to be a static objects because all clients can only read
	//Is only useful for writing and reading that the bot thread will do
	private final Object LOCK = new Object();
	
	//To be established at build time
	private MongoClient mongoClient;
	private MongoCollection<Document> timesCollection;
	
	
	public DatabaseConnector(Builder builder) {
		mongoClient = builder.mongoClient;
		timesCollection = mongoClient.getDatabase(ATCH_DB).getCollection(TIMES_COLLECTION);
	}
	
	/**
	 * Adds all the data from a single OpenSky API Request into database,
	 * formatted in DocumentKeyValueStore, detailed below.
	 * 
	 * @param 		documentData Timestamp and ArrayList of AircraftDataStorage
	 * @see 		DocumentKeyValueStore
	 * 
	 */
	public void addData(DocumentKeyValueStore documentData) {
		//Data into readable format from DocumentKeyValueStore
		Long timestamp = documentData.getKey();
		ArrayList<AircraftDataStorage> aircraftDataList = documentData.getValue();
		
		//Creations of document with timestamp
		Document document = new Document().append("time", timestamp);
		
		//Sub-documents to be inserted into main document as an array of those documents
		ArrayList<Document> states = new ArrayList<Document>();
		
		for (AircraftDataStorage aircraftData : aircraftDataList) {
			//Adds aircraftData as a document to ArrayList of documents
			states.add(aircraftData.toDocument());
		}
		
		//Adds all documents of Aircraft States into document
		document.append("states", states);
		
		//Locked for adjusting database due to multithreading
		synchronized (LOCK) {
			timesCollection.insertOne(document);
		}
	}
	
	
	/**
	 * A document with a timestamp that is older than deleteTime seconds in the past will be deleted.
	 * 
	 * @param deleteTime Maximum number of seconds old that a document can be to not be deleted
	 * 
	 * 
	 */
	
	/* 
	 * @Special
	 * 0 will delete all current documents in the database
	 *  
	 * @Future
	 * Ensure deleteTime is not negative (Though it shouldn't be)
	 * Log via other format
	 */
	public void checkAndClearOldData(int deleteTime) {
		//Parameters of deleting in document form
		Document deleteParameters = new Document().append("time", new Document().append("$lt", (Instant.now().getEpochSecond() - deleteTime)));
		
		synchronized(LOCK) {
			//Delete all documents older than deleteTime seconds ago
			DeleteResult delete = timesCollection.deleteMany(deleteParameters);
			//Sysout for now to be removed later
			System.out.println("Deleted " + delete.getDeletedCount() + " doucments");
		}
	}
	
	
	/**
	 * Gets all possible documents between first and last timestamp and returns them (As an Iterator).
	 * 
	 * @return 		An Iterator of Documents that are within the timestamps defined
	 * @param 		firstTimestamp Long value in Unix Epoch time. First time boundary.
	 * @param 		lastTimestamp Long value in Unix Epoch time. Last time boundary.
	 */
	public Iterator<Document> getDocumentsInTimeFrame(long firstTimestamp, long lastTimestamp) {
		//If last < first throw timebad exception
		
		Document parameters = new Document()
				.append("time", new Document()
						.append("$lt", lastTimestamp)
						.append("$gt", firstTimestamp));

		//Might be unneccesary to lock while reading, might cause more delays
		synchronized(LOCK) {
			return timesCollection.find(parameters).iterator();
		}
	}
	
	/* 
	 * Non functioning right now
	 * Possible future implementation to reduce amount of output by query, 
	 * limiting return to only aircraft that are within area.
	 * This would signfigantly reduce client waiting time since
	 * going through all the aircraft linearly in a for loop is very inefficient
	 * (O(n^2) time, since it is for all documents)
	 * 
	 */
	private Iterator<Document> getDocumentsInTimeFrameAndArea(AreaBounds area, long firstTimestamp, long lastTimestamp) {
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
	
	public void close() {
		mongoClient.close();
	}
	
	/*
	 * Builder architecture is used for future when MongoDB might be more complex
	 * This could include details such as certain databases to access and details in querying.
	 * Leaving it open for future use.
	 * 
	 * Right now, just use builder architecture and add setDefault before building.
	 */
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
