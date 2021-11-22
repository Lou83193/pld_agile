package com.pld.agile.model.map;

import java.util.ArrayList;
import java.util.List;

public class Intersection {
    /**
     * ID of the intersection.
     */
    private String id;

    /**
     * Geographical latitude of the intersection.
     */
    private double latitude;

    /**
     * Geographical longitude of the intersection.
     */
    private double longitude;

    /**
     * List of segments with this intersection as an origin.
     */
    private List<Segment> originOf;

    /**
     * Constructor of the class Intersection, initialises the attributes
     * @param id ID of the intersection
     * @param latitude geographical latitude of the intersection
     * @param longitude geographical longitude of the intersection
     */
    public Intersection(String id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        originOf = new ArrayList<>();
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
    public String getId() {
        return id;
    }

    // SETTERS
    /**
     * Setter for attribute ID
     * @param id ID of the Intersection
     */
    public void setId(String id) {
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

    public List<Segment> getOriginOf() {
        return originOf;
    }

    public void setOriginOf(List<Segment> originOf) {
        this.originOf = originOf;
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "id='" + id + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                //", originOf=" + originOf +
                '}';
    }
}
