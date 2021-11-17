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

    public Intersection(int id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
