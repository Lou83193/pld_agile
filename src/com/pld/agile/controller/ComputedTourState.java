package com.pld.agile.controller;

import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.StopType;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.exception.PathException;
import com.pld.agile.utils.exception.SyntaxException;
import com.pld.agile.utils.parsing.RequestLoader;
import com.pld.agile.view.ButtonEventType;
import com.pld.agile.view.ButtonListener;
import com.pld.agile.view.Window;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;

import java.io.IOException;

/**
 * State when the map and a list of requests are loaded.
 * User can load another map, load another list of requests or ask the app to compute the tour.
 */
public class ComputedTourState implements State {

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
                c.setCurrState(c.loadedRequestsState);

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
    public void doDeleteRequest(Controller c, Window window, Request request) {
        TourData tourData = window.getTourData();
        boolean success = tourData.deleteRequest(request);
        if (!success) {
            window.switchToMainPane();
            window.toggleFileMenuItem(1, true);
            window.toggleFileMenuItem(2, false);
            window.setMainSceneButton(
                    "Load requests",
                    new ButtonListener(c, ButtonEventType.LOAD_REQUESTS)
            );
            window.placeMainSceneButton(true);
            c.setCurrState(c.loadedMapState);
        }
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
        window.unhighlightStops();
        window.getScene().setCursor(Cursor.CROSSHAIR);
        window.toggleFileMenuItem(0, false);
        window.toggleFileMenuItem(1, false);
        window.toggleFileMenuItem(2, false);
        window.toggleMainSceneButton(false);
        c.setCurrState(c.addingRequestState1);
    }

    @Override
    public void doDragOnGraphicalStop(Controller c, Window window, Stop stop) {
        window.unhighlightStops();
        c.setCurrState(c.movingStopState);
    }

}
