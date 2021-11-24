package com.pld.agile.controller;

import com.pld.agile.controller.Controller;
import com.pld.agile.utils.parsing.MapLoader;
import com.pld.agile.view.Window;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * State design pattern interface.
 */
public interface State {
    // todo : javadoc

    /**
     * Loads the map to the mapData object (default loads a map).
     * @param window the application window
     * @param c the controller
     * @return boolean success
     */
    default boolean doLoadMap(Controller c, Window window) {
        // Fetch file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map File");
        fileChooser.setInitialDirectory(new File("./src/resources/xml/maps"));
        File mapFile = fileChooser.showOpenDialog(window.getStage());

        if (mapFile != null) {

            MapLoader mapLoader = new MapLoader(mapFile.getPath(), window.getMapData());
            boolean success = mapLoader.load();

            if (success) {
                window.getTourData().setAssociatedMap(window.getMapData());
                window.switchSceneToMainScene();
                window.toggleFileMenuItem(2, true);
                // switch controller state to Await RequestsState
                c.setCurrState(c.awaitRequestsState);
            }
            return success;
        }
        return false;
    }

    /**
     * Loads the requests to tourData if map is loaded (default doesn't load).
     * @param c the controller
     * @param window the application window
     * @return boolean success
     */
    default boolean doLoadRequests(Controller c, Window window) {
        return false;
    }

    /**
     * Computes a tour and displays it (default doesn't do it since there is no guarantee that requests are loaded).
     * @param c the controller
     * @param window the application window
     * @return boolean success
     */
    default boolean doComputeTour(Controller c, Window window) {
        return false;
    }
}
