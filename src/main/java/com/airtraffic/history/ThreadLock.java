package com.airtraffic.history;

public class ThreadLock {

	private static Object LOCK = new Object();
	
	public static Object getLock() { return LOCK; } 
	
}
