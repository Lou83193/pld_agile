/*
 * EditStopCommand
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.controller;

import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.view.Window;

public class ChangeStopDurationCommand implements Command {
    private Controller controller;
    private Window window;
    private Stop stop;
    private long newDuration;
    private long lastDuration = -1;

    /**
     * Constructor for {@link ChangeStopDurationCommand}
     * @param c controller
     * @param w the application window
     * @param stop the stop that is to be edited
     * @param newDuration the new duration assigned to the stop
     */
    public ChangeStopDurationCommand(Controller c, Window w, Stop stop, long newDuration) {
        this.controller = c;
        this.window = w;
        this.stop = stop;
        this.newDuration = newDuration;
        this.lastDuration = stop.getDuration();
    }

    /**
     * Changes the duration of a stop and recomputes hours of passage.
     */
    @Override
    public void doCommand() {
        TourData tourData = window.getTourData();
        stop.setDuration(newDuration);
        tourData.updateStopsTimesAndNumbers();
    }

    /**
     * undoes the command.
     */
    @Override
    public void undoCommand() {
        TourData tourData = window.getTourData();
        stop.setDuration(lastDuration);
        tourData.updateStopsTimesAndNumbers();
    }
}
