package com.pld.agile.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapData {
    /**
     * Map of all intersections, key is Pair<lat, lon>.
     */
    HashMap <Pair<Double, Double>, Intersection> intersections;

    /**
     * List of all segments
     */
    private List<Segment> segments;

    public MapData(){
        intersections = new HashMap<Pair<Double, Double>, Intersection>();
        segments = new ArrayList<Segment>();
    }

    public MapData(HashMap intersections, List segments){
        this.intersections = intersections;
        this.segments = segments;
    }

    // GETTERS
    /**
     * getter for attribute intersections
     * @return returns the map of all Intersections
     */
    public HashMap<Pair<Double, Double>, Intersection> getIntersections() {
        return intersections;
    }

    /**
     * getter for attribute segments
     * @return returns the list of all Segments
     */
    public List<Segment> getSegments() {
        return segments;
    }

    // SETTERS
    /**
     * setter for attribute intersections
     * @param intersections
     */
    public void setIntersections(HashMap<Pair<Double, Double>, Intersection> intersections) {
        this.intersections = intersections;
    }

    /**
     * setter for attribute segments
     * @param segments
     */
    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }
}
