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
     * The list of adjacent Stops.
     */
    private List<Stop> neighbouringStops;

    /**
     * Stop constructor.
     * @param address the address
     * @param duration the duration
     * @param neighbouringStops the adjacent stops
     */
    public Stop(Intersection address, double duration, List<Stop> neighbouringStops) {
        this.address = address;
        this.duration = duration;
        this.neighbouringStops = neighbouringStops;
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

    public List<Stop> getNeighbouringStops() {
        return neighbouringStops;
    }

    public void setNeighbouringStops(List<Stop> neighbouringStops) {
        this.neighbouringStops = neighbouringStops;
    }

    @Override
    public String toString() {
        return "Stop{" +
                "address=" + address +
                ", duration=" + duration +
                ", neighbouringStops=" + neighbouringStops +
                '}';
    }
}
