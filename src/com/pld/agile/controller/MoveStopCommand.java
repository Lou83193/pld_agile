/*
 * AddRequestCommand
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.controller;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;

/**
 * Command moving a stop on the map.
 */
public class MoveStopCommand implements Command {

    /**
     * The TourData instance the command is operating on.
     */
    private TourData tourData;
    /**
     * The Stop to be moved.
     */
    private Stop stop;
    /**
     * The previous Intersection the Stop was on.
     */
    private Intersection oldIntersection;
    /**
     * The new Intersection of the Stop.
     */
    private Intersection newIntersection;

    /**
     * Constructor for AddRequestCommand.
     * @param tourData The tourData instance the command is operating on.
     * @param stop The Stop to be moved.
     * @param intersection The new intersection of the Stop.
     */
    public MoveStopCommand(TourData tourData, Stop stop, Intersection intersection) {
        this.tourData = tourData;
        this.stop = stop;
        this.newIntersection = intersection;
        this.oldIntersection = stop.getAddress();
    }

    /**
     * Move the stop to the new intersection.
     */
    @Override
    public void doCommand() {

    }

    /**
     * Move back the stop to its old intersection.
     */
    @Override
    public void undoCommand() {

    }

}
