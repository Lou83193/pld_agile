package com.pld.agile.view;

import javafx.scene.layout.VBox;

import java.util.Observable;
import java.util.Observer;

public class TextualView implements Observer { //TODO: create our own Observer interface

    public VBox component;

    public TextualView() {

        // Add Observers
        //modelObj.addObserver(this);

        // Create VBox
        component = new VBox();

    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public VBox getComponent() {
        return component;
    }
}
