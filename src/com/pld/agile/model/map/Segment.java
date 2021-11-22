package com.pld.agile.model.map;

/**
 * Represents a segment from one Intersection to another.
 */
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
     * Getter for attribute length
     * @return returns the length of the Segment
     */
    public double getLength() {
        return length;
    }

    /**
     * Getter for attribute destination
     * @return returns the destination of the Segment
     */
    public Intersection getDestination() {
        return destination;
    }

    /**
     * Getter for attribute origin
     * @return returns the origin of the Segment
     */
    public Intersection getOrigin() {
        return origin;
    }

    /**
     * Getter for attribute name
     * @return returns the name of the Segment
     */
    public String getName() {
        return name;
    }

    // SETTERS
    /**
     * Setter for attribute destination
     * @param destination Intersection at the end of the Segment
     */
    public void setDestination(Intersection destination) {
        this.destination = destination;
    }

    /**
     * Setter for attribute length
     * @param length length of the Segment
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * Setter for attribute name
     * @param name name of the Segment
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for attribute origin
     * @param origin Intersection at the origin of the Segment
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
