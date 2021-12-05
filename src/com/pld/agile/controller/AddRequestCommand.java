/*
 * AddRequestCommand
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.controller;

import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.exception.PathException;
import com.pld.agile.view.Window;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Command continuing the construction of the new request with a delivery positioned at the closest intersection of the given position.
 * The new request is then added to the tour.
 */
public class AddRequestCommand implements Command {

    /**
     * The Controller instance
     */
    private Controller controller;
    /**
     * The Window instance
     */
    private Window window;
    /**
     * The TourData instance the command is operating on.
     */
    private TourData tourData;
    /**
     * The request that is added
     */
    private Request request;
    /**
     * Whether the request needs to be reconstructed or not
     */
    private boolean reconstructRequest;

    /**
     * Constructor for AddRequestPickupCommand.
     * @param controller The Controller instance.
     * @param window The Window instance.
     * @param tourData The TourData instance the command is operating on.
     * @param request The new request to be added.
     */
    public AddRequestCommand(Controller controller, Window window, TourData tourData, Request request) {
        this.controller = controller;
        this.window = window;
        this.tourData = tourData;
        this.request = request;
        reconstructRequest = false;
    }

    /**
     * Finish constructing the new request and add it to the tour
     */
    @Override
    public void doCommand() {
        if (reconstructRequest) {
            tourData.getStopsList().add(request.getPickup());
            tourData.getStopsList().add(request.getDelivery());
            reconstructRequest = false;
        }
        try {
            tourData.addLatestRequest();
        } catch (PathException e) {
            undoCommand();
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Error"); // force english
            alert.setHeaderText("Computing path error");
            alert.showAndWait();
        }
        window.getScene().setCursor(Cursor.DEFAULT);
        window.toggleMainSceneButton(true);
        window.toggleMenuItem(0, 0, true);
        window.toggleMenuItem(0, 1, true);
        window.toggleMenuItem(0, 2, false);
        controller.setCurrState(controller.computedTourState);
    }

    /**
     * Remove the added request, Go back to the adding request state, reconstruct the request with just the pickup.
     */
    @Override
    public void undoCommand() {
        tourData.deleteRequest(request);
        reconstructRequest = true;
    }

}