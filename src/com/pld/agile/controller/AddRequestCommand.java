/*
 * AddRequestCommand
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.controller;

import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.TourData;

/**
 * Command adding a request to the tour.
 */
public class AddRequestCommand implements Command {

    /**
     * The TourData instance the command is operating on.
     */
    private TourData tourData;
    /**
     * The request to be added to the tour.
     */
    private Request request;

    /**
     * Constructor for AddRequestCommand.
     * @param tourData The tourData instance the command is operating on.
     * @param request The request to be added to the tour.
     */
    public AddRequestCommand(TourData tourData, Request request) {
        this.tourData = tourData;
        this.request = request;
    }

    /**
     * Adds the request to the tour
     */
    @Override
    public void doCommand() {

    }

    /**
     * Deletes the request from the tour
     */
    @Override
    public void undoCommand() {

    }

}
