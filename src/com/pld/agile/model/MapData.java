package com.pld.agile.model;

import javafx.util.Pair;

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

    public HashMap<Pair<Double, Double>, Intersection> getIntersections() {
        return intersections;
    }

    public void setIntersections(HashMap<Pair<Double, Double>, Intersection> intersections) {
        this.intersections = intersections;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }
}
