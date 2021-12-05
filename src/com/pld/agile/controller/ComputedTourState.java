package com.pld.agile.controller;

import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
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
import java.time.LocalTime;

/**
 * State when the map and a list of requests are loaded, and the tour is computed.
 * User can load another map, load another list of requests or change the tour.
 */
public class ComputedTourState implements State {

    /**
     * Loads the requests to tourData if map is loaded (default doesn't load).
     * @param c the controller
     * @param w the application window
     * @return boolean success
     */
    @Override
    public boolean doLoadRequests(Controller c, Window w) {
        // Fetch file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Tour File");
        fileChooser.setInitialDirectory(new File("./src/resources/xml/requests"));
        File requestsFile = fileChooser.showOpenDialog(w.getStage());

        if (requestsFile != null) {

            RequestLoader requestsLoader = new RequestLoader(requestsFile.getPath(), w.getTourData());
            try {
                requestsLoader.load();
                w.toggleMenuItem(0, 2, true);
                w.setMainSceneButton(
                        "Compute tour",
                        new ButtonListener(c, ButtonEventType.COMPUTE_TOUR)
                );
                w.placeMainSceneButton(false);
                w.toggleMenuItem(1, 0, false);
                w.toggleMenuItem(1, 1, false);
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
     * Deletes a request from the tour.
     * @param c the controller
     * @param w the application window
     * @param request the request to delete from the tour
     */
    @Override
    public void doDeleteRequest(Controller c, Window w, Request request) {
        TourData tourData = w.getTourData();
        boolean success = tourData.deleteRequest(request);
        if (!success) {
            w.switchToMainPane();
            w.toggleMenuItem(0, 1, true);
            w.toggleMenuItem(0, 2, false);
            w.setMainSceneButton(
                    "Load requests",
                    new ButtonListener(c, ButtonEventType.LOAD_REQUESTS)
            );
            w.placeMainSceneButton(true);
            c.setCurrState(c.loadedMapState);
        }
    }

    /**
     * Shifts a stop's order one stop upwards (earlier) in the tour.
     * @param c the controller
     * @param w the application window
     * @param loc the list of commands
     * @param stop the stop to shift
     */
    @Override
    public void doShiftStopOrderUp(Controller c, Window w, ListOfCommands loc, Stop stop) {
        TourData tourData = w.getTourData();
        loc.add(new ShiftStopOrderCommand(tourData, stop, -1));
    }

    /**
     * Shifts a stop's order one stop downwards (later) in the tour.
     * @param c the controller
     * @param w the application window
     * @param loc the list of commands
     * @param stop the stop to shift
     */
    @Override
    public void doShiftStopOrderDown(Controller c, Window w, ListOfCommands loc, Stop stop) {
        TourData tourData = w.getTourData();
        l.add(new ShiftStopOrderCommand(tourData, stop, +1));
    }

    /**
     * Starts adding a request to the tour by entering the adding request state.
     * @param c the controller
     * @param w the application window
     */
    @Override
    public void doStartAddRequest(Controller c, Window w) {
        w.unhighlightStops();
        w.getScene().setCursor(Cursor.CROSSHAIR);
        w.toggleMenuItem(0, 0, false);
        w.toggleMenuItem(0, 1, false);
        w.toggleMenuItem(0, 2, false);
        w.toggleMainSceneButton(false);
        c.setCurrState(c.addingRequestState1);
    }

    /**
     * Starts moving a stop on the map by entering the moving stop state.
     * @param c the controller
     * @param w the application window
     * @param stop the dragged stop
     */
    @Override
    public void doDragOnGraphicalStop(Controller c, Window w, Stop stop) {
        w.unhighlightStops();
        w.toggleMenuItem(1, 0, false);
        w.toggleMenuItem(1, 1, false);
        c.setCurrState(c.movingStopState);
    }

    /**
     * Changes the duration of a stop and recomputes hours of passage.
     * @param c the controller
     * @param w the application window
     * @param stop the modified stop
     * @param newDuration the new duration of the stop
     */
    @Override
    public void doChangeStopDuration(Controller c, Window w, Stop stop, int newDuration) {
        TourData tourData = w.getTourData();
        stop.setDuration(newDuration);
        tourData.updateStopsTimesAndNumbers();
    }

    /**
     * Changes the departure time from the warehouse and recomputes hours of passage.
     * @param c the controller
     * @param w the application window
     * @param time the new departure time
     */
    @Override
    public void doChangeWarehouseDepartureTime(Controller c, Window w, LocalTime time) {
        TourData tourData = w.getTourData();
        tourData.setDepartureTime(time);
        tourData.updateStopsTimesAndNumbers();
    }

    /**
     * Undoes the last command.
     */
    @Override
    public void undo(ListOfCommands listOfCommands) {
        listOfCommands.undo();
    }

    /**
     * Redoes the last command.
     */
    @Override
    public void redo(ListOfCommands listOfCommands) {
        listOfCommands.redo();
    }

}
