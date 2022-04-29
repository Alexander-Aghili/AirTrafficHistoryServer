package com.airtraffic.history.models;

import org.json.JSONObject;

public class AreaBounds {
	private double lamin;
	private double lamax;
	private double lomin;
	private double lomax;
	
	public AreaBounds(double lamin, double lamax, double lomin, double lomax) throws AreaOutOfBoundsException {
		validateCoordinates(lamin, lamax, lomin, lomax);
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
	
	private void validateCoordinates(double lamin, double lamax, double lomin, double lomax) throws AreaOutOfBoundsException {
		if (lamin>=lamax) throw new AreaOutOfBoundsException();
		if (lomin>=lomax) throw new AreaOutOfBoundsException();
	}
	
	public double getArea() {
		return (lamax-lamin)*(lomax-lomin);
	}
	
	public double getLamin() {
		return lamin;
	}

	public void setLamin(double lamin) {
		this.lamin = lamin;
	}

	public double getLamax() {
		return lamax;
	}

	public void setLamax(double lamax) {
		this.lamax = lamax;
	}

	public double getLomin() {
		return lomin;
	}

	public void setLomin(double lomin) {
		this.lomin = lomin;
	}

	public double getLomax() {
		return lomax;
	}

	public void setLomax(double lomax) {
		this.lomax = lomax;
	}

	public String toURL() {
		return "lamin=" + this.lamin 
				+ "&lamax=" + this.lamax 
				+ "&lomin=" + this.lomin
				+ "&lomax=" + this.lomax;
	}
	
}
