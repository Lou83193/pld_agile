package com.pld.agile.view;

import com.pld.agile.controller.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ButtonListener implements EventHandler<ActionEvent> {

    private Controller controller;
    private String buttonType; //TODO: make enum

    public ButtonListener(Controller controller, String buttonType) {
        this.controller = controller;
        this.buttonType = buttonType;
    }

    public void handle(ActionEvent e) {

        switch(buttonType) {

            case "loadMap" :
                controller.loadMap();
                break;

        }

        e.consume();
    }

}
