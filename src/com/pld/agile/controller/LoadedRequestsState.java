package com.pld.agile.controller;

import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.StopType;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.exception.PathException;
import com.pld.agile.utils.exception.SyntaxException;
import com.pld.agile.utils.observer.UpdateType;
import com.pld.agile.utils.parsing.RequestLoader;
import com.pld.agile.utils.tsp.TSP;
import com.pld.agile.utils.tsp.TSP3;
import com.pld.agile.view.ButtonEventType;
import com.pld.agile.view.ButtonListener;
import com.pld.agile.view.Window;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

/**
 * State when the map and a list of requests are loaded.
 * User can load another map, load another list of requests or ask the app to compute the tour.
 */
public class LoadedRequestsState implements State {

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

    /**
     * Computes a tour and displays it (default doesn't do it
     * since there is no guarantee that requests are loaded).
     * @param c the controller
     * @param window the application window
     * @return boolean success
     */
    @Override
    public boolean doComputeTour(Controller c, Window window) {
        TourData tourData = window.getTourData();
        Thread computingThread = new Thread(() -> {
            try {
                tourData.computeTour();
            } catch (PathException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.setTitle("Error"); // force english
                alert.setHeaderText("Computing path error");
                alert.showAndWait();
            }
            Platform.runLater(() -> {
                c.computingTourState.doStopComputingTour(c, window);
            });
        });
        tourData.setTourComputingThread(computingThread);
        computingThread.setDaemon(true);
        computingThread.start();
        window.toggleFileMenuItem(0, false);
        window.toggleFileMenuItem(1, false);
        window.toggleFileMenuItem(2, false);
        window.setMainSceneButton(
                "Stop computing",
                new ButtonListener(c, ButtonEventType.STOP_COMPUTING_TOUR)
        );
        c.setCurrState(c.computingTourState);
        return true;
    }

}
