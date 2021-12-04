package com.pld.agile.controller;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.TourData;
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
        MapData mapData = window.getMapData();
        TourData tourData = window.getTourData();
        //Intersection intersection = mapData.getIntersections().get(1022);
        Intersection intersection = mapData.findClosestIntersection(latLonPos);
        tourData.constructNewRequest2(intersection);
        window.getScene().setCursor(Cursor.DEFAULT);
        window.toggleMainSceneButton(true);
        c.setCurrState(c.computedTourState);
    }

}
