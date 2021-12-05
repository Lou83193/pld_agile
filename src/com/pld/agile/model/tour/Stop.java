/*
 * Stop
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.model.tour;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.utils.observer.Observable;

import java.time.LocalTime;

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
    private long duration;
    /**
     * The stop type (pickup, delivery, or warehouse)
     */
    private StopType type;
    /**
     * The associated request
     */
    private Request request;
    /**
     * The time of arrival at the stop
     */
    private LocalTime arrivalTime;
    /**
     * The time of departure at the stop
     */
    private LocalTime departureTime;
    /**
     * The stop is visited in stopNumber position
     */
    private int stopNumber;

    /**
     * Stop constructor.
     * @param address the address
     * @param duration the duration
     */
    public Stop(Request request, Intersection address, long duration, StopType type) {
        this.request = request;
        this.type = type;
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
    public long getDuration() {
        return duration;
    }

    /**
     * Setter for attribute duration.
     * @param duration the pickup or delivery duration at the stop
     */
    public void setDuration(long duration) {
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
     * Getter for attribute address.
     * @return arrivalTime
     */
    public LocalTime getArrivalTime() { return arrivalTime; }

    /**
     * Setter for attribute type.
     * @param arrivalTime the time of arrival of the stop
     */
    public void setArrivalTime(LocalTime arrivalTime){ this.arrivalTime=arrivalTime; }

    /**
     * Getter for attribute address.
     * @return departureTime
     */
    public LocalTime getDepartureTime() { return departureTime; }

    /**
     * Setter for attribute type.
     * @param departureTime the time of departure of the stop
     */
    public void setDepartureTime(LocalTime departureTime){ this.departureTime=departureTime; }

    /**
     * Getter for attribute address.
     * @return stopNumber
     */
    public int getStopNumber() { return stopNumber; }

    /**
     * Setter for attribute type.
     * @param stopNumber the number of visit of the stop
     */
    public void setStopNumber(int stopNumber){ this.stopNumber=stopNumber; }

    /**
     * Getter for attribute request.
     * @return request
     */
    public Request getRequest() {
        return request;
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
