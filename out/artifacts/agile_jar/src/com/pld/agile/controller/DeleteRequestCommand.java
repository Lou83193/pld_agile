/*
 * AddRequestCommand
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.controller;

import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.TourData;

/**
 * Command deleting a request from the tour.
 */
public class DeleteRequestCommand implements Command {

    /**
     * The TourData instance the command is operating on.
     */
    private TourData tourData;
    /**
     * The request to be deleted from the tour.
     */
    private Request request;

    /**
     * Constructor for AddRequestCommand.
     * @param tourData The tourData instance the command is operating on.
     * @param request The request to be added to the tour.
     */
    public DeleteRequestCommand(TourData tourData, Request request) {
        this.tourData = tourData;
        this.request = request;
    }

    /**
     * Delete the request to the tour
     */
    @Override
    public void doCommand() {
        tourData.deleteRequest(request);
    }

    /**
     * Adds the request back to the tour (at the right position)
     */
    @Override
    public void undoCommand() {
       tourData.getStopsList().add(request.getPickup());
       tourData.getStopsList().add(request.getDelivery());
       tourData.recomputeStopIDs();
        try {
          tourData.addLatestRequest(request.getPickup().getStopNumber(), request.getDelivery().getStopNumber());
        } catch (Exception e) { }
    }

}
