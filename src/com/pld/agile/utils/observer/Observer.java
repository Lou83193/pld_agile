/*
 * Observer
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.observer;

/**
 * A class that implements Observer must implement an update method called when the observed object is updated.
 */
public interface Observer {
    /**
     * Called by the observed Observable when he is updated, with the UpdateType to specify what kind of update happens.
     * @param observed Observable the caller
     * @param updateType UpdateType the type of update
     */
    public void update(Observable observed, UpdateType updateType);

}
