package com.pld.agile.controller;

import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.StopType;
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

/**
 * State when the map and a list of requests are loaded, and the tour is computing.
 * User can either wait or stop the tour computation.
 */
public class ComputingTourState implements State {

    /**
     * Stops computing the tour and sets the buttons for the new states.
     * @param c the controller
     * @param w the application window
     */
    @Override
    public void doStopComputingTour(Controller c, Window w) {
        TourData tourData = w.getTourData();
        tourData.stopComputingTour();
        w.toggleFileMenuItem(0, true);
        w.toggleFileMenuItem(1, true);
        w.toggleFileMenuItem(2, false);
        w.setMainSceneButton(
                "Add request",
                new ButtonListener(c, ButtonEventType.ADD_REQUEST)
        );
        c.setCurrState(c.computedTourState);
    }

    /**
     * Does nothing because it is impossible to load a map in this state.
     * @param c the controller
     * @param w the application window
     * @return false.
     */
    @Override
    public boolean doLoadMap(Controller c, Window w) {
        return false;
    }

    /**
     * Does nothing because it is impossible to load requests in this state.
     * @param c the controller
     * @param w the application window
     * @return false.
     */
    @Override
    public boolean doLoadRequests(Controller c, Window w) {
        return false;
    }

}
