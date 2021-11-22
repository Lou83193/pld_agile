package com.pld.agile.view;

import com.pld.agile.Observable;
import com.pld.agile.Observer;
import com.pld.agile.model.map.MapData;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;

public class GraphicalView implements Observer {

    private GraphicalViewMap graphicalViewMap;
    private Group component;

    public GraphicalView(Scene parent) {

        // Add observers
        MapData mapData = new MapData();
        mapData.addObserver(this);

        graphicalViewMap = new GraphicalViewMap(parent);

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
