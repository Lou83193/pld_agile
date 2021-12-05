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
        window.getScene().setCursor(Cursor.DEFAULT);
        window.toggleMainSceneButton(true);
        window.toggleFileMenuItem(0, true);
        window.toggleFileMenuItem(1, true);
        window.toggleFileMenuItem(2, false);
        c.setCurrState(c.computedTourState);
    }

}
