package com.pld.agile.controller;

import com.pld.agile.utils.parsing.MapLoader;
import com.pld.agile.view.ButtonEventType;
import com.pld.agile.view.ButtonListener;
import com.pld.agile.view.Window;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;

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
                window.getTourData().setRequestList(new ArrayList<>());
                window.getTourData().setAssociatedMap(window.getMapData());
                window.switchToMainPane();
                window.toggleFileMenuItem(1, true);
                window.toggleFileMenuItem(2, false);
                window.setMainSceneButton("Load requests", new ButtonListener(c, ButtonEventType.LOAD_REQUESTS));
                window.placeMainSceneButton(true);
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
