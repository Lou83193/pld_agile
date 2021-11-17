package com.pld.agile.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapData {
    /**
     * Map of all intersections, key is Pair<lat, lon>.
     */
    HashMap <String, Intersection> intersections;

    /**
     * List of all segments
     */
    private List<Segment> segments;

    public HashMap<String, Intersection> getIntersections() {
        return intersections;
    }

    public void setIntersections(HashMap<String, Intersection> intersections) {
        this.intersections = intersections;
        this.segments = segments;
    }

    // GETTERS
    /**
     * Getter for attribute intersections
     * @return returns the map of all Intersections
     */
    public HashMap<Pair<Double, Double>, Intersection> getIntersections() {
        return intersections;
    }

    /**
     * Getter for attribute segments
     * @return returns the list of all Segments
     */
    public List<Segment> getSegments() {
        return segments;
    }

    // SETTERS
    /**
     * Setter for attribute intersections
     * @param intersections Map of all Intersections
     */
    public void setIntersections(HashMap<Pair<Double, Double>, Intersection> intersections) {
        this.intersections = intersections;
    }

    /**
     * Setter for attribute segments
     * @param segments List of all Segments
     */
    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    @Override
    public String toString() {
        return "MapData{" +
                "intersections=" + intersections +
                ", segments=" + segments +
                '}';
    }
}
