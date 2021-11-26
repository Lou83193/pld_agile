package com.pld.agile.view;

import com.pld.agile.utils.observer.Observable;
import com.pld.agile.utils.observer.Observer;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.observer.UpdateType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class GraphicalView implements Observer {

    private GraphicalViewMapLayer graphicalViewMapLayer;
    private GraphicalViewRequestsLayer graphicalViewRequestsLayer;
    private Pane component;

    public GraphicalView(MapData mapData, TourData tourData, Scene parent) {

        // Add observers
        mapData.addObserver(this);
        tourData.addObserver(this);

        graphicalViewMapLayer = new GraphicalViewMapLayer(mapData, tourData, parent);
        graphicalViewRequestsLayer = new GraphicalViewRequestsLayer(mapData, tourData, graphicalViewMapLayer);

        component = new Pane();
        component.getChildren().addAll(graphicalViewMapLayer, graphicalViewRequestsLayer);

    }

    @Override
    public void update(Observable o, UpdateType updateType) {
        switch (updateType) {
            case MAP -> graphicalViewMapLayer.drawMap();
            case REQUESTS -> {
                graphicalViewRequestsLayer.setDrawAsTour(false);
                graphicalViewRequestsLayer.drawInitial();
            }
            case TOUR -> {
                graphicalViewMapLayer.drawTour();
                graphicalViewRequestsLayer.setDrawAsTour(true);
                graphicalViewRequestsLayer.drawTour();
            }
        }
    }

    public Node getComponent() {
        return component;
    }
    public GraphicalViewMapLayer getGraphicalViewMap() {
        return graphicalViewMapLayer;
    }

}
