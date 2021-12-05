package com.pld.agile.controller;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.view.Window;

/**
 * State when the map and a list of requests are loaded, the corresponding
 * tour is computed, and the user has asked to add another request.
 * User can only click on the graphical view (to add a pickup stop).
 */
public class AddingRequestState1 implements State {

    /**
     * Creates a new request with a pickup at the given position,
     * and goes to addingRequestState2.
     * @param c the controller
     * @param w the application window
     * @param loc the list of commands
     * @param latLonPos the desired latitude and longitude of the pickup
     */
    @Override
    public void doClickOnGraphicalView(Controller c, Window w, ListOfCommands loc, double[] latLonPos) {
        MapData mapData = w.getMapData();
        TourData tourData = w.getTourData();
        loc.add(new AddRequestPickupCommand(c, mapData, tourData, latLonPos));
    }

    /**
     * Does nothing because it is impossible to load a map in this state.
     * @param c the controller
     * @param w the application window
     * @return false.
     */
    @Override
    public boolean doLoadMap(Controller c, Window w) {
        return false;
    }

    /**
     * Does nothing because it is impossible to load requests in this state.
     * @param c the controller
     * @param w the application window
     * @return false.
     */
    @Override
    public boolean doLoadRequests(Controller c, Window w) {
        return false;
    }

}
