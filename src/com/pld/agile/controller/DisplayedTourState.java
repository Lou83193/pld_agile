package com.pld.agile.controller;

import com.pld.agile.model.tour.Path;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.StopType;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.exception.SyntaxException;
import com.pld.agile.utils.parsing.RequestLoader;
import com.pld.agile.utils.tsp.*;
import com.pld.agile.view.ButtonEventType;
import com.pld.agile.view.ButtonListener;
import com.pld.agile.view.Window;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static com.pld.agile.model.tour.StopType.DELIVERY;
import static com.pld.agile.model.tour.StopType.PICKUP;

/**
 * State when the map and a list of requests are loaded.
 * User can load another map, load another list of requests or ask the app to compute the tour.
 */
public class DisplayedTourState implements State {

    /**
     * Loads the requests to tourData if map is loaded (default doesn't load).
     * @param c the controller
     * @param window the application window
     * @return boolean success
     */
    @Override
    public boolean doLoadRequests(Controller c, Window window) {
        // Fetch file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Tour File");
        fileChooser.setInitialDirectory(new File("./src/resources/xml/requests"));
        File requestsFile = fileChooser.showOpenDialog(window.getStage());

        if (requestsFile != null) {

            RequestLoader requestsLoader = new RequestLoader(requestsFile.getPath(), window.getTourData());
            try {
                requestsLoader.load();
                window.toggleFileMenuItem(2, true);
                window.setMainSceneButton(
                        "Compute tour",
                        new ButtonListener(c, ButtonEventType.COMPUTE_TOUR)
                );
                window.placeMainSceneButton(false);
                c.setCurrState(c.displayedRequestsState);

                return true;
            } catch (SyntaxException | IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.setTitle("Error"); // force english
                alert.setHeaderText("Requests loading error");
                alert.showAndWait();
                return false;
            }
        }
        return false;
    }

    @Override
    public void doClickOnGraphicalStop(Controller c, Window window, Stop stop) {
        TourData tourData = window.getTourData();
        tourData.unHighlightStops();
        stop.setHighlighted(2);
        if (stop.getType() != StopType.WAREHOUSE) {
            if (stop.getRequest().getPickup().equals(stop)) {
                stop.getRequest().getDelivery().setHighlighted(1);
            }
            else {
                stop.getRequest().getPickup().setHighlighted(1);
            }
        }
    }

    @Override
    public void doClickOnTextualStop(Controller c, Window window, Stop stop) {
        TourData tourData = window.getTourData();
        tourData.unHighlightStops();
        stop.setHighlighted(2);
        if (stop.getType() != StopType.WAREHOUSE) {
            if (stop.getRequest().getPickup().equals(stop)) {
                stop.getRequest().getDelivery().setHighlighted(1);
            }
            else {
                stop.getRequest().getPickup().setHighlighted(1);
            }
        }
    }

    @Override
    public void doClickOnGraphicalView(Controller c, Window window, double[] latLonPos) {
        TourData tourData = window.getTourData();
        tourData.unHighlightStops();
    }

    @Override
    public void doDeleteRequest(Controller c, Window window, Request request) {
        TourData tourData = window.getTourData();
        tourData.deleteRequest(request);
    }

    @Override
    public boolean doShiftStopOrderUp(Controller c, Window window, Stop stop) {
        TourData tourData = window.getTourData();
        boolean success = tourData.shiftStopOrder(stop, -1);
        return success;
    }

    @Override
    public boolean doShiftStopOrderDown(Controller c, Window window, Stop stop) {
        TourData tourData = window.getTourData();
        boolean success = tourData.shiftStopOrder(stop, +1);
        return success;
    }

    @Override
    public void doStartAddRequest(Controller c, Window window) {
        System.out.println("DoSstartAddRequest");
        TourData tourData = window.getTourData();
        int nbRequests = tourData.getRequestList().size();
        tourData.unHighlightStops();
        window.getScene().setCursor(Cursor.CROSSHAIR);
        window.toggleMainSceneButton(false);
        c.setCurrState(c.addingRequestState1);
    }

    @Override
    public void doDragOnGraphicalStop(Controller c, Window window, Stop stop) {
        TourData tourData = window.getTourData();
        tourData.unHighlightStops();
        c.setCurrState(c.movingStopState);
    }

}
