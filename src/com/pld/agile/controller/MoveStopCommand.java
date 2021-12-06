/*
 * AddRequestCommand
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.controller;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.exception.PathException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Command moving a stop on the map.
 */
public class MoveStopCommand implements Command {

    /**
     * The Controller instance
     */
    private Controller controller;
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
     * @param controller The Controller instance
     * @param tourData The tourData instance the command is operating on.
     * @param stop The Stop to be moved.
     * @param intersection The new intersection of the Stop.
     */
    public MoveStopCommand(Controller controller, TourData tourData, Stop stop, Intersection intersection) {
        this.controller = controller;
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
        try {
            tourData.moveStop(stop, newIntersection);
        } catch (PathException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Error"); // force english
            alert.setHeaderText("Computing path error");
            alert.showAndWait();
        }
        controller.setCurrState(controller.computedTourState);
    }

    /**
     * Move back the stop to its old intersection.
     */
    @Override
    public void undoCommand() {
        try {
            tourData.moveStop(stop, oldIntersection);
        } catch(Exception e) {}
    }

}
