package com.pld.agile.model;

public class Intersection {
    /**
     * ID of the intersection.
     */
    private int id;

    /**
     * Geographical latitude of the intersection.
     */
    private double latitude;

    /**
     * Geographical longitude of the intersection.
     */
    private double longitude;

    // CONSTRUCTOR
    public Intersection(int id, int latitude, int longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // GETTERS
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getId() {
        return id;
    }

    // SETTERS
    public void setId(int id) {
        this.id = id;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
