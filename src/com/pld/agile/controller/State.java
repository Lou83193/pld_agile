package com.pld.agile.controller;

import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.parsing.MapLoader;
import com.pld.agile.utils.exception.SyntaxException;
import com.pld.agile.view.ButtonEventType;
import com.pld.agile.view.ButtonListener;
import com.pld.agile.view.Window;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * State design pattern interface.
 */
public interface State {

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
            try {
                mapLoader.load();
                window.getTourData().setRequestList(new ArrayList<>());
                window.getTourData().setAssociatedMap(window.getMapData());
                window.switchToMainPane();
                window.toggleFileMenuItem(1, true);
                window.toggleFileMenuItem(2, false);
                window.setMainSceneButton(
                        "Load requests",
                        new ButtonListener(c, ButtonEventType.LOAD_REQUESTS)
                );
                window.placeMainSceneButton(true);
                // switch controller state to Await RequestsState
                c.setCurrState(c.awaitRequestsState);

                return true;
            } catch (IOException | SyntaxException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.setTitle("Error"); // force english
                alert.setHeaderText("Map loading error");
                alert.showAndWait();
                return false;
            }
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
     * Computes a tour and displays it (default doesn't do it
     * since there is no guarantee that requests are loaded).
     * @param c the controller
     * @param window the application window
     * @return boolean success
     */
    default boolean doComputeTour(Controller c, Window window) {
        return false;
    }

    default void doClickOnGraphicalStop(Controller c, Window window, Stop stop) {
    }

    default void doClickOnTextualStop(Controller c, Window window, Stop stop) {
    }

    default void doClickOnGraphicalView(Controller c, Window window, double[] latLonPos) {
        System.out.println(latLonPos[0]+  "; " + latLonPos[1]);
    }

    default void doDragOnGraphicalStop(Controller c, Window window, Stop stop) {
    }

    default void doDragOffGraphicalStop(Controller c, Window window, double[] latLonPos) {
    }

    default void doDeleteRequest(Controller c, Window window, Request request) {
    }

    default boolean doShiftStopOrderUp(Controller c, Window window, Stop stop) {
        return false;
    }

    default boolean doShiftStopOrderDown(Controller c, Window window, Stop stop) {
        return false;
    }

    default void doChangeStopDuration(Controller c, Window window, Stop stop, int newDuration) {
        TourData tourData = window.getTourData();
        stop.setDuration(newDuration);
        tourData.setStopTimeAndNumber();
    }

    default void doStartAddRequest(Controller c, Window window) {
    }

    default void doChangeWarehouseDepartureTime(Controller c, Window window, LocalTime time) {
        TourData tourData = window.getTourData();
        tourData.setDepartureTime(time);
        tourData.setStopTimeAndNumber();
    }

}
