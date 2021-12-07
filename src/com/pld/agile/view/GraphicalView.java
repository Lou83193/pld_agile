package com.pld.agile.view;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.utils.observer.Observable;
import com.pld.agile.utils.observer.Observer;
import com.pld.agile.model.map.MapData;
import com.pld.agile.utils.observer.UpdateType;
import com.pld.agile.utils.view.ViewUtilities;
import com.pld.agile.utils.view.ZoomableScrollPane;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Class handling the graphical view of the model.
 */
public class GraphicalView implements Observer {

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
        window.getMapData().addObserver(this);
        window.getTourData().addObserver(this);

        Pane pane = new Pane();
        pane.addEventHandler(MouseEvent.ANY, event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                event.consume();
            }
        });
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
            graphicalViewTourLayer.draw(true);
            graphicalViewRequestsLayer.draw();
        });
        component.heightProperty().addListener(evt -> {
            graphicalViewMapLayer.draw();
            graphicalViewTourLayer.draw(true);
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
        MapData mapData = window.getMapData();
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
                graphicalViewTourLayer.clear();
                graphicalViewMapLayer.draw();
            }
            case REQUESTS -> {
                graphicalViewTourLayer.clear();
                graphicalViewRequestsLayer.draw();
            }
            case TOUR -> {
                graphicalViewTourLayer.draw(true);
                graphicalViewRequestsLayer.draw();
            }
            case INTERMEDIARY_TOUR -> {
                graphicalViewTourLayer.draw(false);
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
     * Getter for graphicalViewTourLayer.
     * @return graphicalViewTourLayer
     */
    public GraphicalViewTourLayer getGraphicalViewTourLayer() {
        return graphicalViewTourLayer;
    }
    /**
     * Getter for window.
     * @return window
     */
    public Window getWindow() {
        return window;
    }
}
