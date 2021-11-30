package com.pld.agile.controller;

import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.view.Window;

/**
 * State when the map and a list of requests are loaded, the corresponding
 * tour is computed, and the user has asked to add another request.
 * User can only click on the graphical view (to add a pickup stop).
 */
public class AddingRequestState1 implements State {

    @Override
    public void doClickOnGraphicalView(Controller c, Window window, double[] latLonPos) {
        // loop through all intersections
        // calculate distance between latLonPos and the intersection's pos, using ViewUtilities.distanceLatLon()
        // find the smallest distance
        // create a pickup stop at that position, associated to the request that's in construction
        c.setCurrState(c.addingRequestState2);
    }

}
