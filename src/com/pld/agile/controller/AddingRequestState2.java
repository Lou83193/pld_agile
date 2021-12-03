package com.pld.agile.controller;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.StopType;
import com.pld.agile.utils.view.ViewUtilities;
import com.pld.agile.view.Window;
import javafx.scene.Cursor;

/**
 * State when the map and a list of requests are loaded, the corresponding
 * tour is computed, and the user has already started to add a new request.
 * (ie a pickup has been placed). User can only click on the graphical view
 * (to add a delivery stop).
 */
public class AddingRequestState2 implements State {

    /**
     * Finishes new request setting and returning to displayedTourState.
     * @param c the controller
     * @param window the application window
     * @param latLonPos latitude and longitude of selected point
     */
    @Override
    public void doClickOnGraphicalView(Controller c, Window window, double[] latLonPos) {
        Intersection intersection = window.getMapData().findClosestIntersection(latLonPos);
        Request tmp = window.getTourData().getRequestList().get(window.getTourData().getRequestList().size()-1);
        Stop newDelivery = new Stop(tmp, intersection, 0, StopType.DELIVERY );
        tmp.setDelivery(newDelivery);
        window.getScene().setCursor(Cursor.DEFAULT);
        window.toggleMainSceneButton(true);
        c.setCurrState(c.displayedTourState);
    }

}
