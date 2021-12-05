package com.pld.agile.controller;

import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
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
     * Fires when the user presses on the "Load map" button.
     * @param w the application window
     * @param c the controller
     * @return boolean success
     */
    default boolean doLoadMap(Controller c, Window w) {
        // Fetch file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map File");
        fileChooser.setInitialDirectory(new File("./src/resources/xml/maps"));
        File mapFile = fileChooser.showOpenDialog(w.getStage());

        if (mapFile != null) {

            MapLoader mapLoader = new MapLoader(mapFile.getPath(), w.getMapData());
            try {
                mapLoader.load();
                w.getTourData().setRequestList(new ArrayList<>());
                w.getTourData().setAssociatedMap(w.getMapData());
                w.switchToMainPane();
                w.toggleFileMenuItem(1, true);
                w.toggleFileMenuItem(2, false);
                w.setMainSceneButton(
                        "Load requests",
                        new ButtonListener(c, ButtonEventType.LOAD_REQUESTS)
                );
                w.placeMainSceneButton(true);
                // switch controller state to Await RequestsState
                c.setCurrState(c.loadedMapState);
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
     * Fires when the user presses the "Load requests" button.
     * @param c the controller
     * @param w the application window
     * @return boolean success
     */
    default boolean doLoadRequests(Controller c, Window w) {
        return false;
    }

    /**
     * Fires when the user presses the "Compute tour" button.
     * @param c the controller
     * @param w the application window
     * @return boolean success
     */
    default void doComputeTour(Controller c, Window w) {
    }

    /**
     * Fires when the user presses the "Stop computing" button.
     * @param c the controller
     * @param w the application window
     */
    default void doStopComputingTour(Controller c, Window w) {
    }

    /**
     * Fires when a click is triggered on the graphical view.
     * @param c the controller
     * @param w the application window
     * @param latLonPos the position of the click, in lat/lon coordinates
     */
    default void doClickOnGraphicalView(Controller c, Window w, double[] latLonPos) {
    }

    /**
     * Fires when a graphical stop is dragged across the graphical view.
     * @param c the controller
     * @param w the application window
     * @param stop the concerned stop
     */
    default void doDragOnGraphicalStop(Controller c, Window w, Stop stop) {
    }

    /**
     * Fires when a graphical stop is dropped on the graphical view (after a drag)
     * @param c the controller
     * @param w the application window
     * @param latLonPos the position where the stop was dropped, in lat/lon coordinates
     */
    default void doDragOffGraphicalStop(Controller c, Window w, double[] latLonPos) {
    }

    /**
     * Fires when the user clicks on the "delete" icon of a textual view stop.
     * @param c the controller
     * @param w the application window
     * @param request the request to delete.
     */
    default void doDeleteRequest(Controller c, Window w, Request request) {
    }

    /**
     * Fires when the user clicks on the "up" arrow of a textual view stop.
     * @param c the controller
     * @param w the application window
     * @param stop the concerned stop.
     */
    default void doShiftStopOrderUp(Controller c, Window w, Stop stop) {
    }

    /**
     * Fires when the user clicks on the "down" arrow of a textual view stop.
     * @param c the controller
     * @param w the application window
     * @param stop the concerned stop.
     */
    default void doShiftStopOrderDown(Controller c, Window w, Stop stop) {
    }

    /**
     * Fires when the user clicks on the "Add request" button.
     * @param c the controller
     * @param w the application window
     */
    default void doStartAddRequest(Controller c, Window w) {
    }

    /**
     * Fires when the user has modified the duration of a stop
     * (after pressing enter or clicking out of the text field)
     * @param c the controller
     * @param w the application window
     * @param stop the concerned stop
     * @param newDuration the new duration of the stop
     */
    default void doChangeStopDuration(Controller c, Window w, Stop stop, int newDuration) {
    }

    /**
     * Fires when the user has modified the departure time from the warehouse
     * (after pressing enter or clicking out of the text field)
     * @param c the controller
     * @param w the application window
     * @param time the new departure time
     */
    default void doChangeWarehouseDepartureTime(Controller c, Window w, LocalTime time) {
    }

}
