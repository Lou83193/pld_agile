/*
 * StartAddRequestCommand
 *
 * Copyright (c) 2021-2021. Hexanomnom
 */

package com.pld.agile.controller;

import com.pld.agile.view.Window;
import javafx.scene.Cursor;

/**
 *
 */
public class StartAddRequestCommand implements Command {

    /**
     * The application window.
     */
    private Window window;

    /**
     * The controller instance.
     */
    private Controller controller;

    /**
     * Constructor for {@link StartAddRequestCommand}.
     * @param c the controller
     * @param w the application window
     */
    public StartAddRequestCommand(Controller c, Window w) {
        this.controller = c;
        this.window = w;
    }

    /**
     *
     */
    @Override
    public void doCommand() {
        window.unhighlightStops();
        window.getScene().setCursor(Cursor.CROSSHAIR);
        window.toggleMenuItem(0, 0, false);
        window.toggleMenuItem(0, 1, false);
        window.toggleMenuItem(0, 2, false);
        window.toggleMainSceneButton(false);
        controller.setCurrState(controller.addingRequestState1);
    }

    /**
     *
     */
    @Override
    public void undoCommand() {
        window.getScene().setCursor(Cursor.DEFAULT);
        window.toggleMenuItem(0, 0, true);
        window.toggleMenuItem(0, 1, true);
        window.toggleMenuItem(0, 2, false);
        window.toggleMainSceneButton(true);
        controller.setCurrState(controller.computedTourState);
    }
}
