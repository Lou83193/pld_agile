package com.pld.agile.view;

import com.pld.agile.controller.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ButtonListener implements EventHandler<ActionEvent> {

    private Controller controller;
    private ButtonEventType buttonType;

    public ButtonListener(Controller controller, ButtonEventType buttonType) {
        this.controller = controller;
        this.buttonType = buttonType;
    }

    public void handle(ActionEvent e) {

        switch(buttonType) {

            case LOAD_MAP:
                controller.loadMap();
                break;

        }

        e.consume();
    }

}
