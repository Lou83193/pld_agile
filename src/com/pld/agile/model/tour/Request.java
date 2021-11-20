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

    public Stop getPickup() {
        return pickup;
    }

    public void setPickup(Stop pickup) {
        this.pickup = pickup;
    }

    public Stop getDelivery() {
        return delivery;
    }

    public void setDelivery(Stop delivery) {
        this.delivery = delivery;
    }

    @Override
    public String toString() {
        return "Request{" +
                "pickup=" + pickup +
                ", delivery=" + delivery +
                '}';
    }
}
