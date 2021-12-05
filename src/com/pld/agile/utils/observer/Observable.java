/*
 * Observable
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.observer;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class that implements Observable can be observed by an Observer to monitor its changes.
 */
public class Observable {

    /**
     * The Observers observing the implementer.
     */
    private Collection<Observer> obs;

    /**
     * Constructor.
     */
    public Observable() {
        obs = new ArrayList<>();
    }

    /**
     * To add an Observer.
     * @param o Observer
     */
    public void addObserver(Observer o) {
        if (!obs.contains(o)) obs.add(o);
    }

    /**
     * To remove an Observer.
     * @param o Observer
     */
    public void removeObserver(Observer o) {
        if (obs.contains(o)) obs.remove(o);
    }

    /**
     * To notify all registered Observers of an update of type updateType.
     * @param updateType UpdateType the type of update happening
     */
    public void notifyObservers(UpdateType updateType) {
        for (Observer o : obs) {
            o.update(this, updateType);
        }
    }

}
