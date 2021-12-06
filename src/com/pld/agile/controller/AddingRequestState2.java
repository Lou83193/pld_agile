package com.pld.agile.controller;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.view.ButtonEventType;
import com.pld.agile.view.ButtonListener;
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
     * Adds the delivery point to the new request, adds the new request to the tour,
     * and goes back to the computed tour state.
     * @param c the controller
     * @param w the application window
     * @param loc the list of commands
     * @param latLonPos the desired latitude and longitude of the delivery
     */
    @Override
    public void doClickOnGraphicalView(Controller c, Window w, ListOfCommands loc, double[] latLonPos) {
        MapData mapData = w.getMapData();
        TourData tourData = w.getTourData();
        Intersection intersection = mapData.findClosestIntersection(latLonPos);
        Request newRequest = tourData.constructNewRequest2(intersection);
        loc.add(new AddRequestCommand(c, w, tourData, newRequest));
    }

    /**
     * Removes the newly added stops (pickup and delivery)
     * and goes back to the computed tour state.
     * @param c the controller
     * @param w the application window
     */
    @Override
    public void doCancelAddRequest(Controller c, Window w) {
        TourData tourData = w.getTourData();
        tourData.deconstructNewRequest1();
        w.getScene().setCursor(Cursor.DEFAULT);
        w.toggleMenuItem(0, 0, true);
        w.toggleMenuItem(0, 1, true);
        w.toggleMenuItem(0, 2, false);
        w.setMainSceneButton(
                "Add request",
                new ButtonListener(c, ButtonEventType.ADD_REQUEST)
        );
        c.setCurrState(c.computedTourState);
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

    /**
     * Undoes the last command.
     */
    @Override
    public void undo(ListOfCommands listOfCommands) {
        listOfCommands.undo();
    }

    /**
     * Redoes the last command.
     */
    @Override
    public void redo(ListOfCommands listOfCommands) {
        listOfCommands.redo();
    }

}
