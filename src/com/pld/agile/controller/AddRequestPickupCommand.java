/*
 * AddRequestPickupCommand
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.controller;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.TourData;

/**
 * Command constructing a new request with a pickup positioned at the closest intersection of the given position.
 */
public class AddRequestPickupCommand implements Command {

    /**
     * The Controller instance
     */
    private Controller controller;
    /**
     * The MapData instance the command is operating on.
     */
    private MapData mapData;
    /**
     * The TourData instance the command is operating on.
     */
    private TourData tourData;
    /**
     * The desired position of the new request's pickup stop.
     */
    private double[] latLonPos;

    /**
     * Constructor for AddRequestPickupCommand.
     * @param controller The Controller instance.
     * @param mapData The MapData instance the command is operating on.
     * @param tourData The TourData instance the command is operating on.
     * @param latLonPos The desired position of the new request's pickup stop.
     */
    public AddRequestPickupCommand(Controller controller, MapData mapData, TourData tourData, double[] latLonPos) {
        this.controller = controller;
        this.mapData = mapData;
        this.tourData = tourData;
        this.latLonPos = latLonPos;
    }

    /**
     * Start constructing the new request
     */
    @Override
    public void doCommand() {
        Intersection intersection = mapData.findClosestIntersection(latLonPos);
        tourData.constructNewRequest1(intersection);
        controller.setCurrState(controller.addingRequestState2);
    }

    /**
     * Deconstruct the latest request
     */
    @Override
    public void undoCommand() {
        tourData.deconstructNewRequest();
        controller.setCurrState(controller.addingRequestState1);
    }

}
