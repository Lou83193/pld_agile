package com.pld.agile.controller;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.view.Window;

/**
 * State when the map and a list of requests are loaded, the corresponding
 * tour is computed, and the user has dragged on a stop to move it. User
 * can only release the mouse to place the stop at its new location.
 */
public class MovingStopState implements State {

    @Override
    public void doDragOffGraphicalStop(Controller c, Window window, Stop stop, double[] latLonPos) {
        MapData mapData = window.getMapData();
        TourData tourData = window.getTourData();
        Intersection intersection = mapData.findClosestIntersection(latLonPos);
        tourData.moveStop(stop, intersection);
        c.setCurrState(c.displayedTourState);
    }

}
