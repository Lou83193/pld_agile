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

    public Intersection getAddress() {
        return address;
    }

    public void setAddress(Intersection address) {
        this.address = address;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Stop{" +
                "address=" + address +
                ", duration=" + duration +
                '}';
    }
}
