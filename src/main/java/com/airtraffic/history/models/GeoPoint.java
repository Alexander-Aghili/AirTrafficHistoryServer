package com.airtraffic.history.models;


public class GeoPoint {
    private double lat;
    private double lon;

    public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}
 
	public void setLon(double lon) {
		this.lon = lon;
	}

	public GeoPoint(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return  "[" + lon + ", " + lat + "],";
    }

    // @Override
    // public int compare(Object o) {
    //     GeoPoint geo = (GeoPoint)o;
    //     return (this.lat == geo.lat && this.lon == geo.lon) ? 0 : 1; 
    // }
}
