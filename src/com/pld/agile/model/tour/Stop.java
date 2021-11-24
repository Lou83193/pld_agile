/*
 * Stop
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.model.tour;

import com.pld.agile.model.map.Intersection;

import java.util.List;

/**
 * Represents a stop in a tour.
 */
public class Stop {
    /**
     * Intersection where the Stop is located.
     */
    private Intersection address;
    /**
     * The pickup or delivery duration at the stop.
     */
    private double duration;

    /**
     * Stop constructor.
     * @param address the address
     * @param duration the duration
     */
    public Stop(Intersection address, double duration) {
        this.address = address;
        this.duration = duration;
    }

    /**
     * Getter for attribute address.
     * @return address
     */
    public Intersection getAddress() {
        return address;
    }

    /**
     * Setter for attribute address.
     * @param address Intersection where the Stop is located
     */
    public void setAddress(Intersection address) {
        this.address = address;
    }

    /**
     * Getter for attribute duration.
     * @return duration
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Setter for attribute duration.
     * @param duration the pickup or delivery duration at the stop
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * Generates a String which describes the object.
     * @return type String
     */
    @Override
    public String toString() {
        return "Stop{" +
                "address=" + address +
                ", duration=" + duration +
                '}';
    }
}
