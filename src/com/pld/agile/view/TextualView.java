package com.pld.agile.view;

import com.pld.agile.Observable;
import com.pld.agile.Observer;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.TourData;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class TextualView implements Observer {

    public VBox component;

    public TextualView(TourData tourData, Scene parent) {

        // Add observers
        tourData.addObserver(this);

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
