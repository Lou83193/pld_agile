package com.pld.agile.controller;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.StopType;
import com.pld.agile.view.Window;

/**
 * State when the map and a list of requests are loaded, the corresponding
 * tour is computed, and the user has asked to add another request.
 * User can only click on the graphical view (to add a pickup stop).
 */
public class AddingRequestState1 implements State {

    /**
     * Start new request setting and going to addingRequestState2.
     * @param c the controller
     * @param window the application window
     * @param latLonPos latitude and longitude of selected point
     */
    @Override
    public void doClickOnGraphicalView(Controller c, Window window, double[] latLonPos) {
        Intersection intersection = window.getMapData().findClosestIntersection(latLonPos);
        // create a pickup stop at that position, associated to the request that's in construction

        //Test :
        //Intersection intersection = window.getMapData().getIntersections().get(506);

        Request tmp = new Request();
        Stop newPickup = new Stop(tmp, intersection, 0, StopType.PICKUP );
        tmp.setPickup(newPickup);
        window.getTourData().getRequestList().add(tmp);
        c.setCurrState(c.addingRequestState2);
    }

}
