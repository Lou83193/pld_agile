package com.pld.agile.model.map;

import com.pld.agile.Observable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Stores the data of a loaded map.
 */
public class MapData extends Observable {
    /**
     * List of intersections, their index should be their ID.
     */
    private List<Intersection> intersections;

    /**
     * Map of all intersections by their XML-ID (to be able to link them to XML-loaded requests). Only used in loading.
     */
    private HashMap<String, Intersection> intersectionsByOldID;

    /**
     * List of all segments
     */
    private List<Segment> segments;

    /**
     * Maximum latitude of the map
     */
    private double maxLat = Integer.MIN_VALUE;

    /**
     * Minimum latitude of the map
     */
    private double minLat = Integer.MAX_VALUE;

    /**
     * Maximum longitude of the map
     */
    private double maxLon = Integer.MIN_VALUE;

    /**
     * Minimum longitude of the map
     */
    private double minLon = Integer.MAX_VALUE;


    /**
     * Constructor for the class MapData, initializes the attributes intersections and segments
     * @param intersections List of all Intersections
     * @param segments List of all Segments
     */
    public MapData(List intersections, List segments){
        this.intersections = intersections;
        this.segments = segments;
    }

    /**
     * Constructor for the class MapData, initializes the attributes intersections and segments
     */
    public MapData() {
        intersectionsByOldID = new HashMap<String, Intersection>();
        segments = new ArrayList<>();
    }


    /**
     * Update bounds of map (minimum / maximum latitude / longitude)
     * @param lat new latitude
     * @param lon new longitude
     */
    public void updateBounds(double lat, double lon) {
        maxLat = Math.max(maxLat, lat);
        maxLon = Math.max(maxLon, lon);
        minLat = Math.min(minLat, lat);
        minLon = Math.min(minLon, lon);
        notifyObservers(this);
    }

    /**
     * Reset the bounds of map to infinitely big / small values (minimum / maximum latitude / longitude)
     */
    public void resetBounds() {
        maxLat = Integer.MIN_VALUE;
        maxLon = Integer.MIN_VALUE;
        minLat = Integer.MAX_VALUE;
        minLon = Integer.MAX_VALUE;
        notifyObservers(this);
    }

    // GETTERS
    /**
     * Getter for attribute intersections
     * @return returns the map of all Intersections
     */
    public HashMap<String, Intersection> getIntersectionsByOldID() {
        return intersectionsByOldID;
    }

    /**
     * Getter for attribute segments
     * @return returns the list of all Segments
     */
    public List<Segment> getSegments() {
        return segments;
    }

    /**
     * Getter for intersections.
     * @return intersections
     */
    public List<Intersection> getIntersections() {
        return intersections;
    }

    /**
     * Setter for intersections.
     * @param intersections list of intersections
     */
    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
    }

    // SETTERS
    /**
     * Setter for attribute intersections
     * @param intersectionsByOldID Map of all Intersections
     */
    public void setIntersectionsByOldID(HashMap<String, Intersection> intersectionsByOldID) {
        this.intersectionsByOldID = intersectionsByOldID;
        notifyObservers(this);
    }

    /**
     * Setter for attribute segments
     * @param segments List of all Segments
     */
    public void setSegments(List<Segment> segments) {
        this.segments = segments;
        notifyObservers(this);
    }

    /**
     * Getter for attribute maxLat
     * @return maxLat
     */
    public double getMaxLat() {
        return maxLat;
    }

    /**
     * Setter for attribute maxLat
     * @param maxLat maximum latitude
     */
    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
        notifyObservers(this);
    }

    /**
     * Getter for attribute minLat
     * @return minLat
     */
    public double getMinLat() {
        return minLat;
    }

    /**
     * Setter for attribute minLat
     * @param minLat minimum latitude
     */
    public void setMinLat(double minLat) {
        this.minLat = minLat;
        notifyObservers(this);
    }

    /**
     * Getter for attribute maxLon
     * @return maxLon
     */
    public double getMaxLon() {
        return maxLon;
    }

    /**
     * Setter for attribute maxLon
     * @param maxLon maximum longitude
     */
    public void setMaxLon(double maxLon) {
        this.maxLon = maxLon;
        notifyObservers(this);
    }

    /**
     * Getter for attribute minLon
     * @return minLon
     */
    public double getMinLon() {
        return minLon;
    }

    /**
     * Setter for attribute minLon
     * @param minLon minimum longitude
     */
    public void setMinLon(double minLon) {
        this.minLon = minLon;
        notifyObservers(this);
    }

    @Override
    public String toString() {
        return "MapData{" +
                "intersections=" + intersectionsByOldID +
                ", segments=" + segments +
                '}';
    }
}
