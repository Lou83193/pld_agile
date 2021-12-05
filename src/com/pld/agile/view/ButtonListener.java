package com.pld.agile.view;

import com.pld.agile.controller.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Generic button listener.
 */
public class ButtonListener implements EventHandler<ActionEvent> {

    /**
     * Button press event type.
     */
    private ButtonEventType buttonType;
    /**
     * The application's Controller instance.
     */
    private Controller controller;

    /**
     * Constructor for the button listener.
     * @param controller The application's Controller instance.
     * @param buttonType The button press event type.
     */
    public ButtonListener(final Controller controller, final ButtonEventType buttonType) {
        this.controller = controller;
        this.buttonType = buttonType;
    }

    /**
     * Handles button presses depending on the button's event type.
     * @param e The button press event.
     */
    public void handle(ActionEvent e) {

        switch (buttonType) {
            case LOAD_MAP -> controller.loadMap();
            case LOAD_REQUESTS -> controller.loadTour();
            case COMPUTE_TOUR -> controller.computeTour();
            case STOP_COMPUTING_TOUR -> controller.stopComputingTour();
            case ADD_REQUEST -> controller.startAddRequest();
            case UNDO -> controller.undo();
            case REDO -> controller.redo();
        }

        e.consume();
    }

}
