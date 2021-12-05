package com.pld.agile.view;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
import com.pld.agile.model.tour.Path;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.view.MouseClickNotDragDetector;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import java.util.List;

/**
 * Class handling the graphical map and the tour's trace on it.
 */
public class GraphicalViewMapLayer extends Pane {

    /**
     * The parent GraphicalView instance.
     */
    private GraphicalView graphicalView;

    /**
     * GraphicalViewMapLayer constructor.
     * @param graphicalView The parent GraphicalView instance
     */
    public GraphicalViewMapLayer(GraphicalView graphicalView) {
        this.graphicalView = graphicalView;
        this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        MapData mapData = graphicalView.getWindow().getMapData();
        MouseClickNotDragDetector.clickNotDragDetectingOn(this)
                .withPressedDurationThreshold(150)
                .setOnMouseClickedNotDragged((mouseEvent) -> {
                    graphicalView.getWindow().unhighlightStops();
                    double[] latLonPos = ViewUtilities.projectMercatorLatLonInv(
                            mouseEvent.getX(),
                            mouseEvent.getY(),
                            mapData.getMinLat(),
                            mapData.getMinLon(),
                            mapData.getMaxLat(),
                            mapData.getMaxLon(),
                            ((ScrollPane) graphicalView.getComponent()).getHeight()
                    );
                    graphicalView.getWindow().getController().clickOnGraphicalView(latLonPos);
                });

    }

    /**
     * Draws the map, by populating the pane with graphical segments.
     */
    public void draw() {

        MapData mapData = graphicalView.getWindow().getMapData();

        double screenScale = ViewUtilities.mapValue(
                getHeight(),
                0, 1080,
                0, 1
        );
        double mapScale = ViewUtilities.mapValue(
                mapData.getMaxLon() - mapData.getMinLon(),
                0.02235, 0.07610,
                1.25, 0.75
        );

        this.getChildren().clear();

        List<Segment> segments = mapData.getSegments();

        for (Segment segment : segments) {

            Line graphicalViewSegment = new GraphicalViewSegment(
                    graphicalView,
                    segment,
                    3 * screenScale * mapScale,
                    graphicalView.getWindow().getStreetNameLabel()
            );
            this.getChildren().add(graphicalViewSegment);

        }

    }

    /**
     * Overridden method to ensure the pane is resizeable.
     * @return true.
     */
    @Override
    public boolean isResizable() {
        return true;
    }

}
