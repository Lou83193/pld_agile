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

public class Controller {

    private State currState;

    private Window window;

    // Available states :
    protected State awaitMapState = new AwaitMapState();
    protected State awaitRequestsState = new AwaitRequestsState();
    protected State displayedRequestsState = new DisplayedRequestsState();

    public Controller (Window window) {
        this.window = window;
        this.currState = awaitMapState;
    }

    public void setCurrState(State s) {
        currState = s;
    }

    public void loadMap() {
        currState.doLoadMap(this, window);
    }

    public void loadTour() {
        currState.doLoadRequests(this, window);
    }

    public void computeTour() {
        currState.doComputeTour(this, window);
    }

}
