package com.pld.agile.controller;

import com.pld.agile.model.tour.Stop;
import com.pld.agile.view.Window;

/**
 * State when the map and a list of requests are loaded, the corresponding
 * tour is computed, and the user has dragged on a stop to move it. User
 * can only release the mouse to place the stop at its new location.
 */
public class MovingStopState implements State {

    @Override
    public void doDragOffGraphicalStop(Controller c, Window w, double[] latLonPos) {
        // loop through all intersections
        // calculate distance between latLonPos and the intersection's pos, using ViewUtilities.distanceLatLon()
        // find the smallest distance
        // change the intersection of the currently dragged stop to that
        w.toggleMenuItem(1, 0, true);
        w.toggleMenuItem(1, 1, true);
        c.setCurrState(c.computedTourState);
    }

}
