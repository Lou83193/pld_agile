/*
 * AddRequestDeliveryCommand
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.controller;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.exception.PathException;
import com.pld.agile.view.Window;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.List;

/**
 * Command continuing the construction of the new request with a delivery positioned at the closest intersection of the given position.
 * The new request is then added to the tour.
 */
public class AddRequestDeliveryCommand implements Command {

    /**
     * The Controller instance
     */
    private Controller controller;
    /**
     * The Window instance
     */
    private Window window;
    /**
     * The MapData instance the command is operating on.
     */
    private MapData mapData;
    /**
     * The TourData instance the command is operating on.
     */
    private TourData tourData;
    /**
     * The desired position of the new request's delivery stop.
     */
    private double[] latLonPos;
    /**
     * The request that is added
     */
    private Request newRequest;

    /**
     * Constructor for AddRequestPickupCommand.
     * @param controller The Controller instance.
     * @param window The Window instance.
     * @param mapData The MapData instance the command is operating on.
     * @param tourData The TourData instance the command is operating on.
     * @param latLonPos The desired position of the new request's delivery stop.
     */
    public AddRequestDeliveryCommand(Controller controller, Window window, MapData mapData, TourData tourData, double[] latLonPos) {
        this.controller = controller;
        this.window = window;
        this.mapData = mapData;
        this.tourData = tourData;
        this.latLonPos = latLonPos;
        this.newRequest = null;
    }

    /**
     * Finish constructing the new request and add it to the tour
     */
    @Override
    public void doCommand() {
        Intersection intersection = mapData.findClosestIntersection(latLonPos);
        newRequest = tourData.constructNewRequest2(intersection);
        try {
            tourData.addRequest(newRequest);
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
        tourData.deleteRequest(newRequest);
        newRequest = tourData.constructNewRequest1(newRequest.getPickup().getAddress());
        window.unhighlightStops();
        window.getScene().setCursor(Cursor.CROSSHAIR);
        window.toggleMenuItem(0, 0, false);
        window.toggleMenuItem(0, 1, false);
        window.toggleMenuItem(0, 2, false);
        window.toggleMainSceneButton(false);
        controller.setCurrState(controller.addingRequestState2);
    }

}