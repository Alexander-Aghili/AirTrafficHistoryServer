package com.airtraffic.history.database;

import java.util.ArrayList;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;

import com.airtraffic.history.models.AircraftDataStorage;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

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
	
	//Possibly make this private and have a public exposing method with concurrent 
	//write/read protection
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
	
	//This also might just be a bad way of doing this
	public void clearTimestamp(Long timestamp) {
		MongoCollection<Document> timesCollection = mongoClient.getDatabase(ATCH_DB).getCollection(TIMES_COLLECTION);		
		BsonDocument doc = new BsonDocument().append("time", new BsonInt64(timestamp));
		timesCollection.deleteOne(doc);
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
