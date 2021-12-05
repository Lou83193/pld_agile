/*
 * Intersection
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.model.map;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an intersection on the map.
 */
public class Intersection {

    private static int idCounter = 0;

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
     * List of segments with this intersection as an origin.
     */
    private List<Segment> originOf;

    /**
     * Constructor of the class Intersection, initialises the attributes
     * @param latitude geographical latitude of the intersection
     * @param longitude geographical longitude of the intersection
     */
    public Intersection(double latitude, double longitude) {
        this.id = idCounter;
        this.latitude = latitude;
        this.longitude = longitude;
        this.originOf = new ArrayList<>();
        idCounter++;
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

    /**
     * Getter for attribute originOf
     * @return the attribute originOf
     */
    public List<Segment> getOriginOf() {
        return originOf;
    }

    /**
     * Setter for attribute originOf
     * @param originOf attribute value
     */
    public void setOriginOf(List<Segment> originOf) {
        this.originOf = originOf;
    }

    /**
     * Finds the segment that goes from this intersection to the given intersection
     * @param destination the destination intersection
     * @return the linking segment
     */
    public Segment findSegmentTo(Intersection destination) {
        for (Segment s : originOf) {
            if (s.getDestination().equals(destination)) {
                return s;
            }
        }
        return null;
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
