package com.pld.agile.model;

public class Segment {
    /**
     * Length of the segment, distance between the origin and destination
     */
    private double length;

    /**
     * Name of the street represented by the segment
     */
    private String name;

    /**
     * Intersection at the origin of the segment
     */
    private Intersection origin;

    /**
     * Intersection which ends the segment
     */
    private Intersection destination;

}
