/*
 * Path
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.model.tour;

import com.pld.agile.model.map.Segment;

import java.util.List;

public class Path {

    /**
     * List of segments composing the path.
     */
    private List<Segment> segments;

    /**
     * Length of path as calculated by dijkstra.
     */
    private double length;

    /**
     * Origin stop of the path.
     */
    private Stop origin;

    /**
     * Destination stop of the path.
     */
    private Stop destination;

    /**
     * Path constructor.
     * @param origin path origin
     * @param destination path destination
     */
    public Path(Stop origin, Stop destination) {
        this.origin = origin;
        this.destination = destination;
    }

    /**
     * Getter for attribute segments
     * @return segments
     */
    public List<Segment> getSegments() {
        return segments;
    }

    /**
     * Setter for attribute segments
     * @param segments List of segments
     */
    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    /**
     * Getter for attribute length
     * @return length
     */
    public double getLength() {
        return length;
    }

    /**
     * Setter for attribute length
     * @param length Length of the path
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * Getter for attribute origin
     * @return origin
     */
    public Stop getOrigin() {
        return origin;
    }

    /**
     * Setter for attribute origin
     * @param origin Origin stop of the path
     */
    public void setOrigin(Stop origin) {
        this.origin = origin;
    }

    /**
     * Getter for attribute destination
     * @return destination
     */
    public Stop getDestination() {
        return destination;
    }

    /**
     * Setter for attribute destination
     * @param destination Destination stop of the path
     */
    public void setDestination(Stop destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "Path {"
                + "origin =" + origin.getAddress().getId()
                + ", destination=" + destination.getAddress().getId() + '\''
                + '}';
    }
}
