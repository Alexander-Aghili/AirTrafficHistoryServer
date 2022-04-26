package com.airtraffic.history.database;

import java.util.ArrayList;

import com.airtraffic.history.models.AircraftDataStorage;

//Key value store easy for transporation
public class DocumentKeyValueStore 
{
	private Long key;
	private ArrayList<AircraftDataStorage> value;
	
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
