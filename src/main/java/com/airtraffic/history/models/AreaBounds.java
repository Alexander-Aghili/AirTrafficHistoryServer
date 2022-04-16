package com.airtraffic.history.models;

import org.json.JSONObject;

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
	
	public AreaBounds(JSONObject bounds) {
		this.lamin = bounds.getDouble("lamin");
		this.lamax = bounds.getDouble("lamax");
		this.lomin = bounds.getDouble("lomin");
		this.lomax = bounds.getDouble("lomax");
	}
	
	public void changeLengthByFactor(double factor) {
		lamin -= ((lamax-lamin)*factor)/2;
		lamax += ((lamax-lamin)*factor)/2;
		lomin -= ((lomax-lomin)*factor)/2;
		lomax += ((lamax-lamin)*factor)/2;
	}
	
	public double getArea() {
		return (lamax-lamin)*(lomax-lomin);
	}
	
	public String toURL() {
		return "lamin=" + this.lamin 
				+ "&lamax=" + this.lamax 
				+ "&lomin=" + this.lomin
				+ "&lomax=" + this.lomax;
	}
	
}
