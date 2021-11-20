package com.pld.agile.view;

import com.pld.agile.Observable;
import com.pld.agile.Observer;
import com.pld.agile.model.MapData;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class TextualView implements Observer { //TODO: create our own Observer interface

    public VBox component;

    public TextualView() {

        // Add observers
        MapData mapData = MapData.getInstance();
        mapData.addObserver(this);

        // Create VBox
        component = new VBox();

    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public Node getComponent() {
        return component;
    }
}
