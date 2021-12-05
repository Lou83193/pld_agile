package com.pld.agile.controller;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.exception.PathException;
import com.pld.agile.view.Window;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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
     * @param latLonPos the desired latitude and longitude of the delivery
     */
    @Override
    public void doClickOnGraphicalView(Controller c, Window w, double[] latLonPos) {
        MapData mapData = w.getMapData();
        TourData tourData = w.getTourData();
        Intersection intersection = mapData.findClosestIntersection(latLonPos);
        try {
            tourData.constructNewRequest2(intersection);
        } catch (PathException e) {
            tourData.deleteRequest(tourData.getRequestList().get(tourData.getRequestList().size() - 1));
            // dijkstra iis broken after this error, try to redo it?
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Error"); // force english
            alert.setHeaderText("Computing path error");
            alert.showAndWait();
        }
        w.getScene().setCursor(Cursor.DEFAULT);
        w.toggleMainSceneButton(true);
        w.toggleMenuItem(0, 0, true);
        w.toggleMenuItem(0, 1, true);
        w.toggleMenuItem(0, 2, false);
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

}
