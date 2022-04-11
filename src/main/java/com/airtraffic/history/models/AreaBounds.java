package com.airtraffic.history.models;

public class AreaBounds {
	private double lamin;
	private double lamax;
	private double lomin;
	private double lomax;
	
	public AreaBounds(double lamin, double lamax, double lomin, double lomax) {
		this.lamin = lamin;
		this.lamax = lamax;
		this.lomin = lomin;
		this.lomax = lomax;
	}
	
	public String toURL() {
		return "lamin=" + this.lamin 
				+ "&lamax=" + this.lamax 
				+ "&lomin=" + this.lomin
				+ "&lomax=" + this.lomax;
	}
	
}
