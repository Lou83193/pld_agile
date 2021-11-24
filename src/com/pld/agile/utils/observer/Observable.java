/*
 * Observable
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.observer;

import java.util.ArrayList;
import java.util.Collection;

public class Observable {

    private Collection<Observer> obs;

    public Observable() {
        obs = new ArrayList<>();
    }

    public void addObserver(Observer o) {
        if (!obs.contains(o)) obs.add(o);
    }

    public void notifyObservers(UpdateType updateType) {
        for (Observer o : obs) {
            o.update(this, updateType);
        }
    }

}
