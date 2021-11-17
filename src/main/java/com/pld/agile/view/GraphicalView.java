package com.pld.agile.view;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;

import java.util.Observable;
import java.util.Observer;

public class GraphicalView implements Observer { //TODO: create our own Observer interface

    private Canvas mapCanvas;
    private Group component;

    public GraphicalView() {

        // Add Observers
        //modelObj.addObserver(this);

        // Create and populate map canvas
        mapCanvas = new Canvas();

        component = new Group();
        component.getChildren().add(mapCanvas);

    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public Group getComponent() {
        return component;
    }
}
