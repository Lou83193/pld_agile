package com.pld.agile.model;

public class Segment {
    /**
     * Length of the Segment, distance between the origin and destination
     */
    private double length;

    /**
     * Name of the street represented by the Segment
     */
    private String name;

    /**
     * Intersection at the origin of the Segment
     */
    private Intersection origin;

    /**
     * Intersection at the end of the Segment
     */
    private Intersection destination;

    /**
     * Constructor of the class Segment, initialises the attributes
     * @param name name of the Segment
     * @param length length of the Segment
     * @param origin Intersection at the origin of the Segment
     * @param destination Intersection at the end of the Segment
     */
    public Segment(String name, double length, Intersection origin, Intersection destination) {
        this.name = name;
        this.length = length;
        this.origin = origin;
        this.destination = destination;
    }

    // GETTERS
    /**
     * getter for attribute length
     * @return returns the length of the Segment
     */
    public double getLength() {
        return length;
    }

    /**
     * getter for attribute destination
     * @return returns the destination of the Segment
     */
    public Intersection getDestination() {
        return destination;
    }

    /**
     * getter for attribute origin
     * @return returns the origin of the Segment
     */
    public Intersection getOrigin() {
        return origin;
    }

    /**
     * getter for attribute name
     * @return returns the name of the Segment
     */
    public String getName() {
        return name;
    }

    // SETTERS
    /**
     * setter for attribute destination
     * @param destination
     */
    public void setDestination(Intersection destination) {
        this.destination = destination;
    }

    /**
     * setter for attribute length
     * @param length
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * setter for attribute name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setter for attribute origin
     * @param origin
     */
    public void setOrigin(Intersection origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "length=" + length +
                ", name='" + name + '\'' +
                ", origin=" + origin +
                ", destination=" + destination +
                '}';
    }
}
