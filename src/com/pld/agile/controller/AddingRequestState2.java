package com.pld.agile.controller;

import com.pld.agile.model.tour.Stop;
import com.pld.agile.view.Window;

/**
 * State when the map and a list of requests are loaded, the corresponding
 * tour is computed, and the user has already started to add a new request.
 * (ie a pickup has been placed). User can only click on the graphical view
 * (to add a delivery stop).
 */
public class AddingRequestState2 implements State {

    @Override
    public void doClickOnGraphicalView(Controller c, Window window, double[] latLonPos) {
        // loop through all intersections
        // calculate distance between latLonPos and the intersection's pos, using ViewUtilities.distanceLatLon()
        // find the smallest distance
        // create a delivery stop at that position, associated to the request that's in construction
        c.setCurrState(c.displayedTourState);
    }

    @Override
    public void doClickOnGraphicalStop(Controller c, Window window, Stop stop) {
    }

    @Override
    public void doClickOnTextualStop(Controller c, Window window, Stop stop) {
    }

}
