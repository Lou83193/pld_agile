/*
 * Request
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.model.tour;

/**
 * Represents a delivery request.
 */
public class Request {
    /**
     * Pickup stop.
     */
    private Stop pickup;
    /**
     * Delivery Stop.
     */
    private Stop delivery;

    /**
     * Request constructor.
     * @param pickup pickup Stop
     * @param delivery delivery Stop
     */
    public Request(Stop pickup, Stop delivery) {
        this.pickup = pickup;
        this.delivery = delivery;
    }

    /**
     * Getter for attribute pickup
     * @return pickup
     */
    public Stop getPickup() {
        return pickup;
    }

    /**
     * Setter for attribute pickup
     * @param pickup Pickup stop
     */
    public void setPickup(Stop pickup) {
        this.pickup = pickup;
    }

    /**
     * Getter for attribute delivery
     * @return delivery
     */
    public Stop getDelivery() {
        return delivery;
    }

    /**
     * Setter for attribute delivery
     * @param delivery Delivery stop
     */
    public void setDelivery(Stop delivery) {
        this.delivery = delivery;
    }

    /**
     * Generates a String which describes the object
     * @return type String
     */
    @Override
    public String toString() {
        return "Request{" +
                "pickup=" + pickup +
                ", delivery=" + delivery +
                '}';
    }
}
