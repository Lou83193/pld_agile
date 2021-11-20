package com.pld.agile.view;

import com.pld.agile.controller.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ButtonListener implements EventHandler<ActionEvent> {

    private ButtonEventType buttonType;

    public ButtonListener(ButtonEventType buttonType) {
        this.buttonType = buttonType;
    }

    public void handle(ActionEvent e) {

        Controller controller = Controller.getInstance();

        switch(buttonType) {

            case LOAD_MAP:
                controller.loadMap();
                break;

        }

        e.consume();
    }

}
