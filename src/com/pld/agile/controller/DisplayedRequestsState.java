package com.pld.agile.controller;

import com.pld.agile.utils.parsing.RequestLoader;
import com.pld.agile.view.Window;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * State when the map and a list of requests are loaded.
 * User can load another map, load another list of requests or ask the app to compute the tour.
 */
public class DisplayedRequestsState implements State {

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
                window.toggleFileMenuItem(3, true);
                c.setCurrState(c.displayedRequestsState);
            }
            return success;

        }
        return false;
    }

    @Override
    public boolean doComputeTour(Controller c, Window window) {
        // Compute TSP
        window.getTourData().setStops();
        window.getTourData().dijkstra();
        window.getTourData().tsp();
        return true;
    }
}
