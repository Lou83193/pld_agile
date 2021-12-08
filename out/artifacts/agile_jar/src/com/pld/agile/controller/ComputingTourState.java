package com.pld.agile.controller;

import com.pld.agile.model.tour.TourData;
import com.pld.agile.view.ButtonEventType;
import com.pld.agile.view.ButtonListener;
import com.pld.agile.view.Window;

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
    public void doStopComputingTour(Controller c, Window w, ListOfCommands loc) {
        TourData tourData = w.getTourData();
        tourData.stopComputingTour();
        w.toggleMenuItem(0, 0, true);
        w.toggleMenuItem(0, 1, true);
        w.toggleMenuItem(0, 2, false);
        w.toggleMenuItem(1, 0, loc.canUndo());
        w.toggleMenuItem(1, 1, loc.canRedo());
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
