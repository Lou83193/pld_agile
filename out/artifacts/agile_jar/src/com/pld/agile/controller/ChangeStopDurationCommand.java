/*
 * EditStopCommand
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.controller;

import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;

public class ChangeStopDurationCommand implements Command {

    /**
     * Tour data of the application.
     */
    private final TourData tourData;

    /**
     * The Stop that is to be edited.
     */
    private final Stop stop;

    /**
     * The new duration assigned to the stop.
     */
    private final long newDuration;

    /**
     * Stores the duration of the stop before change made by the command.
     */
    private final long lastDuration;

    /**
     * Constructor for {@link ChangeStopDurationCommand}.
     * @param td the tour data of the application.
     * @param stop the stop that is to be edited.
     * @param newDuration the new duration assigned to the stop.
     */
    public ChangeStopDurationCommand(final TourData td, final Stop stop, final long newDuration) {
        this.tourData = td;
        this.stop = stop;
        this.newDuration = newDuration;
        this.lastDuration = stop.getDuration();
    }

    /**
     * Changes the duration of a stop and recomputes hours of passage.
     */
    @Override
    public void doCommand() {
        stop.setDuration(newDuration);
        tourData.updateStopsTimesAndNumbers();
    }

    /**
     * Changes the duration of a stop back to what it was before and recomputes hours of passage.
     */
    @Override
    public void undoCommand() {
        stop.setDuration(lastDuration);
        tourData.updateStopsTimesAndNumbers();
    }
}
