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

    @Override
    public void doStopComputingTour(Controller c, Window window) {
        TourData tourData = window.getTourData();
        tourData.stopComputingTour();
        window.toggleFileMenuItem(0, true);
        window.toggleFileMenuItem(1, true);
        window.toggleFileMenuItem(2, false);
        window.setMainSceneButton(
                "Add request",
                new ButtonListener(c, ButtonEventType.ADD_REQUEST)
        );
        c.setCurrState(c.computedTourState);
    }

    @Override
    public boolean doLoadMap(Controller c, Window window) {
        return false;
    }

}
