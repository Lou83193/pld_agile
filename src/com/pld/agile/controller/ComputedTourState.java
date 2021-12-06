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
     * @param loc the list of commands
     * @param request the request to delete from the tour
     */
    @Override
    public void doDeleteRequest(Controller c, Window w, ListOfCommands loc, Request request) {
        TourData tourData = w.getTourData();
        loc.add(new DeleteRequestCommand(tourData, request));
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
        loc.add(new ShiftStopOrderCommand(tourData, stop, +1));
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
        w.setMainSceneButton(
                "Cancel",
                new ButtonListener(c, ButtonEventType.CANCEL_ADD_REQUEST)
        );
        c.setCurrState(c.addingRequestState1);
    }

    /**
     * Starts moving a stop on the map by entering the moving stop state.
     * @param c the controller
     * @param w the application window
     */
    @Override
    public void doDragOnGraphicalStop(Controller c, Window w) {
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
    public void doChangeStopDuration(Window w, ListOfCommands loc, Stop stop, long newDuration) {
        loc.add(new ChangeStopDurationCommand(w.getTourData(), stop, newDuration));
    }

    /**
     * Changes the departure time from the warehouse and recomputes hours of passage.
     * @param c the controller
     * @param w the application window
     * @param time the new departure time
     */
    @Override
    public void doChangeWarehouseDepartureTime(Window w, ListOfCommands loc, LocalTime time) {
        loc.add(new ChangeWarehouseDepartureTimeCommand(w.getTourData(), time));
    }

    /**
     * Undoes the last command.
     */
    @Override
    public void doUndo(Window w, ListOfCommands loc) {
        loc.undo();
        w.toggleMenuItem(1, 0, loc.canUndo());
        w.toggleMenuItem(1, 1, loc.canRedo());
    }

    /**
     * Redoes the last command.
     */
    @Override
    public void doRedo(Window w, ListOfCommands loc) {
        loc.redo();
        w.toggleMenuItem(1, 0, loc.canUndo());
        w.toggleMenuItem(1, 1, loc.canRedo());
    }

}
