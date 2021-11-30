package com.pld.agile.controller;

import com.pld.agile.utils.parsing.RequestLoader;
import com.pld.agile.view.ButtonEventType;
import com.pld.agile.view.ButtonListener;
import com.pld.agile.view.Window;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * State when the map and a list of requests are loaded.
 * User can load another map, load another list of requests or ask the app to compute the tour.
 */
public class DisplayedRequestsState implements State {

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
            boolean success = requestsLoader.load();

            if (success) {
                window.toggleFileMenuItem(2, true);
                window.setMainSceneButton(
                        "Compute tour",
                        new ButtonListener(c, ButtonEventType.COMPUTE_TOUR)
                );
                window.placeMainSceneButton(false);
                c.setCurrState(c.displayedRequestsState);
            }
            return success;

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
        // Compute TSP
        window.getTourData().computeTour();
        window.setMainSceneButton(
                "Add request",
                new ButtonListener(c, ButtonEventType.ADD_REQUEST)
        );
        c.setCurrState(c.displayedTourState);
        return true;
    }
}
