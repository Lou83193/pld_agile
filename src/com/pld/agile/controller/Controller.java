/*
 * Controller
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.controller;

import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.view.Window;

import java.time.LocalTime;

/**
 * Controller class.
 */
public class Controller {
    /**
     * Current State of the Controller.
     * The response of the controller to same inputs might change
     * depending on the current State.
     */
    private State currState;

    /**
     * Application Window.
     */
    private Window window;

    /**
     * The list of commands instance.
     */
    private ListOfCommands listOfCommands;

    // Available states :
    /**
     * Initial state.
     * The user can only load a map xml file.
     */
    protected State initialState = new InitialState();
    /**
     * State when the map is loaded.
     * User can load another map, or load a request xml file.
     */
    protected State loadedMapState = new LoadedMapState();
    /**
     * State when the map and a list of requests are loaded.
     * User can load another map, load another list of requests
     * or ask the app to compute the tour.
     */
    protected State loadedRequestsState = new LoadedRequestsState();
    /**
     * State when the map and a list of requests are loaded, and the tour is computing.
     * User can either wait or stop the tour computation.
     */
    protected State computingTourState = new ComputingTourState();
    /**
     * State when the map and a list of requests are loaded,
     * and the corresponding tour is computed. User can load another map,
     * load another list of requests, or modify the tour.
     */
    protected State computedTourState = new ComputedTourState();
    /**
     * State when the map and a list of requests are loaded, the corresponding
     * tour is computed, and the user has asked to add another request.
     * User can only click on the graphical view (to add a pickup stop).
     */
    protected State addingRequestState1 = new AddingRequestState1();
    /**
     * State when the map and a list of requests are loaded, the corresponding
     * tour is computed, and the user has already started to add a new request.
     * (ie a pickup has been placed). User can only click on the graphical view
     * (to add a delivery stop).
     */
    protected State addingRequestState2 = new AddingRequestState2();
    /**
     * State when the map and a list of requests are loaded, the corresponding
     * tour is computed, and the user has dragged on a stop to move it. User
     * can only release the mouse to place the stop at its new location.
     */
    protected State movingStopState = new MovingStopState();

    /**
     * Constructor of the controller.
     * Initialises the parameters.
     * @param window application Window
     */
    public Controller(Window window) {
        this.window = window;
        this.currState = initialState;
        this.listOfCommands = new ListOfCommands();
    }

    /**
     * Getter for attribute window.
     */
    public Window getWindow() { return window; }

    /**
     * Getter for attribute currState.
     */
    public State getCurrState() { return currState; }

    /**
     * Getter for attribute awaitMapState.
     */
    public State getInitialState() { return initialState; }

    /**
     * Setter for attribute currState.
     * @param s new State
     */
    public void setCurrState(State s) {
        currState = s;
    }

    /**
     * Calls method doLoadMap() of the current state.
     */
    public void loadMap() {
        currState.doLoadMap(this, window);
    }
    /**
     * Calls method doLoadRequests() of the current state.
     */
    public void loadTour() {
        currState.doLoadRequests(this, window);
    }
    /**
     * Calls method doComputeTour() of the current state.
     */
    public void computeTour() {
        currState.doComputeTour(this, window);
    }
    /**
     * Calls method doStopComputingTour() of the current state.
     */
    public void stopComputingTour() {
        currState.doStopComputingTour(this, window);
    }
    /**
     * Calls method clickOnGraphicalView() of the current state.
     */
    public void clickOnGraphicalView(double[] latLonPos) {
        currState.doClickOnGraphicalView(this, window, listOfCommands, latLonPos);
    }
    /**
     * Calls method deleteRequest() of the current state.
     */
    public void deleteRequest(Request request) {
        currState.doDeleteRequest(this, window, listOfCommands, request);
    }
    /**
     * Calls method shiftStopOrderUp() of the current state.
     */
    public void shiftStopOrderUp(Stop stop) {
        currState.doShiftStopOrderUp(this, window, listOfCommands, stop);
    }
    /**
     * Calls method shiftStopOrderDown() of the current state.
     */
    public void shiftStopOrderDown(Stop stop) {
        currState.doShiftStopOrderDown(this, window, listOfCommands, stop);
    }
    /**
     * Calls method changeStopDuration() of the current state.
     */
    public void changeStopDuration(Stop stop, int newDuration) {
        currState.doChangeStopDuration(this, window, listOfCommands, stop, newDuration);
    }
    /**
     * Calls method startAddRequest() of the current state.
     */
    public void startAddRequest() {
        currState.doStartAddRequest(this, window);
    }
    /**
     * Calls method doCancelAddRequest() of the current state.
     */
    public void cancelAddRequest() {
        currState.doCancelAddRequest(this, window);
    }
    /**
     * Calls method dragOnGraphicalStop() of the current state.
     */
    public void dragOnGraphicalStop() {
        currState.doDragOnGraphicalStop(this, window);
    }
    /**
     * Calls method releaseOnGraphicalView() of the current state.
     */
    public void dragOffGraphicalStop(Stop stop, double[] latLonPos) {
        currState.doDragOffGraphicalStop(this, window, listOfCommands, stop, latLonPos);
    }
    /**
     * Calls method changeWarehouseDepartureTime() of the current state.
     */
    public void changeWarehouseDepartureTime(LocalTime time) {
        currState.doChangeWarehouseDepartureTime(this, window, time);
    }
    /**
     * Calls method undo() of the current state.
     */
    public void undo() {
        currState.undo(listOfCommands);
    }
    /**
     * Calls method redo() of the current state.
     */
    public void redo() {
        currState.redo(listOfCommands);
    }

}
