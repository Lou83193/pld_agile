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
     * getter for attribute latitude
     * @return returns the latitude of the Intersection
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * getter for attribute longitude
     * @return returns the longitude of the Intersection
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * getter for attribute ID
     * @return returns the ID of the Intersection
     */
    public int getId() {
        return id;
    }

    // SETTERS
    /**
     * setter for attribute ID
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * setter for attribute latitude
     * @param latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * setter for attribute longitude
     * @param longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
