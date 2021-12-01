package com.pld.agile.view;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.utils.observer.Observable;
import com.pld.agile.utils.observer.Observer;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.observer.UpdateType;
import com.pld.agile.utils.view.ViewUtilities;
import com.pld.agile.utils.view.ZoomableScrollPane;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Class handling the graphical view of the model.
 */
public class GraphicalView implements Observer {

    /**
     * The application's MapData instance.
     */
    private MapData mapData;
    /**
     * The application's TourData instance.
     */
    private TourData tourData;
    /**
     * The application's Window instance.
     */
    private Window window;
    /**
     * Layer containing the map segments
     */
    private GraphicalViewMapLayer graphicalViewMapLayer;
    /**
     * Layer containing the tour highlights
     */
    private GraphicalViewTourLayer graphicalViewTourLayer;
    /**
     * Layer containing the request stops.
     */
    private GraphicalViewRequestsLayer graphicalViewRequestsLayer;
    /**
     * Wrapper zoomable component encapsulating both layers.
     */
    private ZoomableScrollPane component;

    /**
     * GraphicalView constructor.
     * Adds observers on the model objects, populates the graphical components,
     * Sets size bindings and adds resizing listeners.
     * @param window The application's Window instance.
     */
    public GraphicalView(Window window) {

        this.window = window;
        this.mapData = window.getMapData();
        this.tourData = window.getTourData();

        mapData.addObserver(this);
        tourData.addObserver(this);

        Pane pane = new Pane();
        component = new ZoomableScrollPane(pane);
        component.setId("map");

        graphicalViewMapLayer = new GraphicalViewMapLayer(this);
        graphicalViewTourLayer = new GraphicalViewTourLayer(this);
        graphicalViewRequestsLayer = new GraphicalViewRequestsLayer(this);

        DoubleBinding graphicalViewHeight = window.getScene().heightProperty().subtract(50);
        component.prefWidthProperty().bind(graphicalViewHeight);
        component.prefHeightProperty().bind(graphicalViewHeight);
        graphicalViewTourLayer.prefWidthProperty().bind(component.heightProperty());
        graphicalViewTourLayer.prefHeightProperty().bind(component.heightProperty());
        graphicalViewMapLayer.prefWidthProperty().bind(component.heightProperty());
        graphicalViewMapLayer.prefHeightProperty().bind(component.heightProperty());

        pane.getChildren().addAll(
            graphicalViewMapLayer,
            graphicalViewTourLayer,
            graphicalViewRequestsLayer
        );

        component.widthProperty().addListener(evt -> {
            graphicalViewMapLayer.draw();
            graphicalViewTourLayer.draw();
            graphicalViewRequestsLayer.draw();
        });
        component.heightProperty().addListener(evt -> {
            graphicalViewMapLayer.draw();
            graphicalViewTourLayer.draw();
            graphicalViewRequestsLayer.draw();
        });

    }

    /**
     * Projects an intersection's (lat; lon) address to a pixel coordinate
     * based on the container's size and the mapData's bounds.
     * @param intersection The intersection whose address to project.
     * @return A double[] containing the {x, y} projection.
     */
    public double[] projectLatLon(final Intersection intersection) {
        return ViewUtilities.projectMercatorLatLon(
                intersection.getLatitude(),
                intersection.getLongitude(),
                mapData.getMinLat(),
                mapData.getMinLon(),
                mapData.getMaxLat(),
                mapData.getMaxLon(),
                component.getHeight()
        );
    }

    /**
     * Updates the views whenever the model notifies a change.
     * @param o The observable object who notified the view.
     * @param updateType The type of update that has been made.
     */
    @Override
    public void update(final Observable o, final UpdateType updateType) {
        switch (updateType) {
            case MAP -> {
                graphicalViewMapLayer.draw();
            }
            case REQUESTS -> {
                graphicalViewRequestsLayer.draw();
            }
            case TOUR -> {
                graphicalViewTourLayer.draw();
                graphicalViewRequestsLayer.draw();
            }
        }
    }

    /**
     * Getter for component.
     * @return component
     */
    public Node getComponent() {
        return component;
    }
    /**
     * Getter for graphicalViewMapLayer.
     * @return graphicalViewMapLayer
     */
    public GraphicalViewMapLayer getGraphicalViewMapLayer() {
        return graphicalViewMapLayer;
    }
    /**
     * Getter for mapData.
     * @return mapData
     */
    public MapData getMapData() {
        return mapData;
    }
    /**
     * Getter for tourData.
     * @return tourData
     */
    public TourData getTourData() {
        return tourData;
    }
    /**
     * Getter for window.
     * @return window
     */
    public Window getWindow() {
        return window;
    }
}
