package com.pld.agile.view;

import com.pld.agile.Observable;
import com.pld.agile.Observer;
import com.pld.agile.model.MapData;
import javafx.scene.Group;
import javafx.scene.Node;

public class GraphicalView implements Observer { //TODO: create our own Observer interface

    private GraphicalViewMap graphicalViewMap;
    private Group component;

    public GraphicalView() {

        // Add observers
        MapData mapData = MapData.getInstance();
        mapData.addObserver(this);

        graphicalViewMap = new GraphicalViewMap(720, 720);

        component = new Group();
        component.getChildren().add(graphicalViewMap);

    }

    @Override
    public void update(Observable o, Object arg) {
        graphicalViewMap.draw();
    }

    public Node getComponent() {
        return component;
    }
}
