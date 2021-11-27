package com.pld.agile.view;

import com.pld.agile.utils.observer.Observable;
import com.pld.agile.utils.observer.Observer;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.observer.UpdateType;
import com.pld.agile.utils.view.ZoomableScrollPane;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GraphicalView implements Observer {

    private GraphicalViewMapLayer graphicalViewMapLayer;
    private GraphicalViewRequestsLayer graphicalViewRequestsLayer;
    private ZoomableScrollPane component;

    public GraphicalView(MapData mapData, TourData tourData, Window window) {

        // Add observers
        mapData.addObserver(this);
        tourData.addObserver(this);

        Pane pane = new Pane();
        component = new ZoomableScrollPane(pane);
        component.setBackground(new Background(new BackgroundFill(Color.web("#DEDEDE"), CornerRadii.EMPTY, Insets.EMPTY)));

        graphicalViewMapLayer = new GraphicalViewMapLayer(mapData, tourData, window);
        graphicalViewRequestsLayer = new GraphicalViewRequestsLayer(mapData, tourData, graphicalViewMapLayer);

        DoubleBinding graphicalViewHeight = window.getStage().getScene().heightProperty().subtract(50);
        component.prefWidthProperty().bind(graphicalViewHeight);
        component.prefHeightProperty().bind(graphicalViewHeight);
        graphicalViewMapLayer.prefWidthProperty().bind(component.heightProperty());
        graphicalViewMapLayer.prefHeightProperty().bind(component.heightProperty());

        pane.getChildren().addAll(graphicalViewMapLayer, graphicalViewRequestsLayer);

        component.widthProperty().addListener(evt -> {
            graphicalViewMapLayer.draw();
            graphicalViewRequestsLayer.draw();
            System.out.println("W");
        });
        component.heightProperty().addListener(evt -> {
            graphicalViewMapLayer.draw();
            graphicalViewRequestsLayer.draw();
            System.out.println("H");
        });

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
