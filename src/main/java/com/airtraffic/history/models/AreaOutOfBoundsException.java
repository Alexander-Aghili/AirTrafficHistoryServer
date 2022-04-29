package com.airtraffic.history.models;

@SuppressWarnings("serial")
public class AreaOutOfBoundsException extends RuntimeException {
	
	public AreaOutOfBoundsException(String message) {
		super(message);
	}

	public AreaOutOfBoundsException() {}
	
}
