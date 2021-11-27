package com.pld.agile.controller;

import com.pld.agile.utils.parsing.RequestLoader;
import com.pld.agile.view.ButtonEventType;
import com.pld.agile.view.ButtonListener;
import com.pld.agile.view.Window;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * State when the map is loaded.
 * User can load another map, or load a request xml file.
 */
public class AwaitRequestsState implements State {

    @Override
    public boolean doLoadRequests(final Controller c, final Window window) {
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
                window.setMainSceneButton("Compute tour", new ButtonListener(c, ButtonEventType.COMPUTE_TOUR));
                window.placeMainSceneButton(false);
                c.setCurrState(c.displayedRequestsState);
            }
            return success;

        }
        return false;
    }

}