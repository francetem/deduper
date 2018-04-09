package org.ehu.dedupe.derive.geo;

import java.math.BigDecimal;

public class Coordinates {

    private final float lat;
    private final float lon;

    public Coordinates(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public BigDecimal distFrom(Coordinates coordinates2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(coordinates2.getLat() - getLat());
        double dLng = Math.toRadians(coordinates2.getLon() - getLon());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(getLat())) * Math.cos(Math.toRadians(coordinates2.getLat())) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return BigDecimal.valueOf((float) (earthRadius * c));
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return lat + "," + lon;
    }
}
