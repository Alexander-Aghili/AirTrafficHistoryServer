package com.airtraffic.history.database;

import java.util.ArrayList;

import com.airtraffic.history.models.AircraftDataStorage;

/**
 * Simple Key Value store with the Key being the Timestamp and the Value being an ArrayList of AircraftDataStorage
 * 
 * @author Alexander Aghili
 * 
 */
public class DocumentKeyValueStore 
{
	private Long key;
	private ArrayList<AircraftDataStorage> value;
	
	/**
	 * 
	 * @param key Timestamp as a long
	 * @param value ArrayList of AircraftDataStorage
	 * 
	 */
	public DocumentKeyValueStore(Long key, ArrayList<AircraftDataStorage> value) {
		this.key = key;
		this.value = value;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public ArrayList<AircraftDataStorage> getValue() {
		return value;
	}

	public void setValue(ArrayList<AircraftDataStorage> value) {
		this.value = value;
	}
	
}
