/*
 * ChangeWarehouseDepartureTimeCommand
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.controller;

import com.pld.agile.model.tour.TourData;

import java.time.LocalTime;

public class ChangeWarehouseDepartureTimeCommand implements Command {
    /**
     * Tour data of the application.
     */
    private final TourData tourData;

    /**
     * The new departure time.
     */
    private final LocalTime time;

    /**
     * Stores the departure time of the warehouse before change made by the command.
     */
    private final LocalTime lastTime;

    /**
     * Constructor for {@link ChangeWarehouseDepartureTimeCommand}.
     * @param td the tour data of the application.
     * @param time the new departure time.
     */
    public ChangeWarehouseDepartureTimeCommand(final TourData td, final LocalTime time) {
        this.tourData = td;
        this.time = time;
        this.lastTime = td.getDepartureTime();
    }

    /**
     * Changes the departure time from the warehouse and recomputes hours of passage.
     */
    @Override
    public void doCommand() {
        tourData.setDepartureTime(time);
        tourData.updateStopsTimesAndNumbers();
    }

    /**
     * Undoes the command.
     */
    @Override
    public void undoCommand() {
        tourData.setDepartureTime(lastTime);
        tourData.updateStopsTimesAndNumbers();
    }
}
