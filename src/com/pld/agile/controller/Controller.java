/*
 * Controller
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.controller;

import com.pld.agile.utils.parsing.RequestLoader;
import com.pld.agile.view.Window;
import javafx.stage.FileChooser;
import java.io.File;

/**
 * Controller class.
 */
public class Controller {
    /**
     * Current State of the Controller.
     * The response of the controller to same inputs might change depending on the current State.
     */
    private State currState;
    /**
     * Application Window
     */
    private Window window;

    // Available states :
    /**
     * Initial state.
     * The user can only load a map xml file.
     */
    protected State awaitMapState = new AwaitMapState();
    /**
     * State when the map is loaded.
     * User can load another map, or load a request xml file.
     */
    protected State awaitRequestsState = new AwaitRequestsState();
    /**
     * State when the map and a list of requests are loaded.
     * User can load another map, load another list of requests or ask the app to compute the tour.
     */
    protected State displayedRequestsState = new DisplayedRequestsState();

    /**
     * Constructor of the controller.
     * Initialises the parameters.
     * @param window application Window
     */
    public Controller (Window window) {
        this.window = window;
        this.currState = awaitMapState;
    }

    /**
     * Setter for attribute currState.
     * @param s new State
     */
    public void setCurrState(State s) {
        currState = s;
    }

    /**
     * Calls method doLoadMap() of the current state
     */
    public void loadMap() {
        currState.doLoadMap(this, window);
    }
    /**
     * Calls method doLoadRequests() of the current state
     */
    public void loadTour() {
        currState.doLoadRequests(this, window);
    }
    /**
     * Calls method doComputeTour() of the current state
     */
    public void computeTour() {
        currState.doComputeTour(this, window);
    }

}
