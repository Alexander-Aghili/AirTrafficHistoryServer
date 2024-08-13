package com.airtraffic.history.automated;
import com.airtraffic.history.models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; 

public class ZoneCover {

    public static List<GeoPoint> generateCircleCenters(GeoPoint[] quadrilateral, double radiusMiles) {
        List<GeoPoint> centers = new ArrayList<>();

        // Calculate the bounding box of the quadrilateral
        double minLat = Double.MAX_VALUE, maxLat = Double.MIN_VALUE;
        double minLon = Double.MAX_VALUE, maxLon = Double.MIN_VALUE;
        for (GeoPoint point : quadrilateral) {
            minLat = Math.min(minLat, point.getLat());
            maxLat = Math.max(maxLat, point.getLat());
            minLon = Math.min(minLon, point.getLon());
            maxLon = Math.max(maxLon, point.getLon());
        }

        // Convert radius in miles to degrees latitude and longitude
        double radiusLat = radiusMiles / 68.8636; // Convert miles to degrees latitude
        double avgLat = (minLat + maxLat) / 2;
        double radiusLon = radiusMiles / (68.8636 * Math.cos(Math.toRadians(avgLat))); // Convert miles to degrees longitude

        // Calculate the horizontal and vertical distances in the hexagonal grid
        double hexHeight = Math.sqrt(3) * radiusLat;
        double hexWidth = 2 * radiusLon;

        // Iterate over the extended bounding box to generate points on the hexagonal grid
        for (double lat = minLat; lat <= maxLat + hexHeight; lat += hexHeight) {
            for (double lon = minLon; lon <= maxLon + hexWidth; lon += hexWidth) {
                GeoPoint center1 = new GeoPoint(lat, lon);
                if (isInsideQuadrilateral(center1, quadrilateral)) {
                    centers.add(center1);
                }

                // Add offset points for every other row
                GeoPoint center2 = new GeoPoint(lat + hexHeight / 2, lon + hexWidth / 2);
                if (isInsideQuadrilateral(center2, quadrilateral)) {
                    centers.add(center2);
                }
            }
        }

        return centers;
    }

    public static boolean isInsideQuadrilateral(GeoPoint p, GeoPoint[] quadrilateral) {
        // Ray-casting algorithm to check if a point is inside a polygon
        boolean result = false;
        int j = quadrilateral.length - 1;
        for (int i = 0; i < quadrilateral.length; i++) {
            if ((quadrilateral[i].getLon() > p.getLon()) != (quadrilateral[j].getLon() > p.getLon()) &&
                (p.getLat() < (quadrilateral[j].getLat() - quadrilateral[i].getLat()) * 
                (p.getLon() - quadrilateral[i].getLon()) / 
                (quadrilateral[j].getLon() - quadrilateral[i].getLon()) + quadrilateral[i].getLat())) {
                result = !result;
            }
            j = i;
        }
        return result;
    }
}
