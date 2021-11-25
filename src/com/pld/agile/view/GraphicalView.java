package com.pld.agile.view;

import com.pld.agile.utils.observer.Observable;
import com.pld.agile.utils.observer.Observer;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.observer.UpdateType;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class GraphicalView implements Observer {

    private GraphicalViewMap graphicalViewMap;
    private GraphicalViewRequests graphicalViewRequests;
    private Pane component;

    public GraphicalView(MapData mapData, TourData tourData, Scene parent) {

        // Add observers
        mapData.addObserver(this);
        tourData.addObserver(this);

        graphicalViewMap = new GraphicalViewMap(mapData, tourData, parent);
        graphicalViewRequests = new GraphicalViewRequests(mapData, tourData, graphicalViewMap);

        component = new Pane();
        component.getChildren().addAll(graphicalViewMap, graphicalViewRequests);

    }

    @Override
    public void update(Observable o, UpdateType updateType) {
        switch (updateType) {
            case MAP -> graphicalViewMap.drawMap();
            case REQUESTS -> {
                graphicalViewRequests.setDrawAsTour(false);
                graphicalViewRequests.drawInitial();
            }
            case TOUR -> {
                graphicalViewMap.drawTour();
                graphicalViewRequests.setDrawAsTour(true);
                graphicalViewRequests.drawTour();
            }
        }
    }

    public Node getComponent() {
        return component;
    }
    public GraphicalViewMap getGraphicalViewMap() {
        return graphicalViewMap;
    }

}
