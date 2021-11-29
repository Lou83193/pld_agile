/*
 * Stop
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.model.tour;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.utils.observer.Observable;
import com.pld.agile.utils.observer.UpdateType;

import java.util.List;

/**
 * Represents a stop in a tour.
 */
public class Stop extends Observable {

    /**
     * Intersection where the Stop is located.
     */
    private Intersection address;
    /**
     * The pickup or delivery duration at the stop.
     */
    private double duration;
    /**
     * The stop type (pickup, delivery, or warehouse)
     */
    private StopType type;
    /**
     * The associated request
     */
    private Request request;
    /**
     * Whether the stop is highlighted in the view or not.
     */
    private boolean highlighted;

    /**
     * Stop constructor.
     * @param address the address
     * @param duration the duration
     */
    public Stop(Request request, Intersection address, double duration, StopType type) {
        this.request = request;
        this.type = type;
        this.address = address;
        this.duration = duration;
        this.highlighted = false;
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
     * Getter for attribute type.
     * @return type
     */
    public StopType getType() {
        return type;
    }

    /**
     * Setter for attribute type.
     * @param type the type of the stop
     */
    public void setType(StopType type) {
        this.type = type;
    }

    /**
     * Getter for attribute request.
     * @return request
     */
    public Request getRequest() {
        return request;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
        notifyObservers(UpdateType.STOP_HIGHLIGHT);
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
