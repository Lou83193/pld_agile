/*
 * AddRequestCommand
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.controller;

import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;

/**
 * Command deleting a request from the tour.
 */
public class ShiftStopOrderCommand implements Command {

    /**
     * The TourData instance the command is operating on.
     */
    private TourData tourData;
    /**
     * The Stop whose order to be shifted.
     */
    private Stop stop;
    /**
     * The direction of the shift (up: -1; down: +1).
     */
    private int dir;

    /**
     * Constructor for AddRequestCommand.
     * @param tourData The tourData instance the command is operating on.
     * @param stop The Stop whose order to be shifted.
     * @param dir The direction of the shift.
     */
    public ShiftStopOrderCommand(TourData tourData, Stop stop, int dir) {
        this.tourData = tourData;
        this.stop = stop;
        this.dir = dir;
    }

    /**
     * Shift the stop's order in the given direction
     */
    @Override
    public void doCommand() {

    }

    /**
     * Shift the stop's order in the other direction
     */
    @Override
    public void undoCommand() {

    }

}
