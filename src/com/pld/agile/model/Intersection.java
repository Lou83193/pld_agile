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

    /**
     * Constructor of the class Intersection, initialises the attributes
     * @param id ID of the intersection
     * @param latitude geographical latitude of the intersection
     * @param longitude geographical longitude of the intersection
     */
    public Intersection(int id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // GETTERS
    /**
     * Getter for attribute latitude
     * @return returns the latitude of the Intersection
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Getter for attribute longitude
     * @return returns the longitude of the Intersection
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Getter for attribute ID
     * @return returns the ID of the Intersection
     */
    public int getId() {
        return id;
    }

    // SETTERS
    /**
     * Setter for attribute ID
     * @param id ID of the Intersection
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter for attribute latitude
     * @param latitude latitude of the Intersection
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Setter for attribute longitude
     * @param longitude longitude of the Intersection
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
