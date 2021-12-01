package com.pld.agile.view;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
import com.pld.agile.model.tour.Path;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.view.MouseClickNotDragDetector;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.geometry.Insets;
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
     * Boolean switch instructing whether to draw the tour's trace or not.
     */
    private boolean drawTour = false;

    /**
     * GraphicalViewMapLayer constructor.
     * @param graphicalView The parent GraphicalView instance
     */
    public GraphicalViewMapLayer(GraphicalView graphicalView) {
        this.graphicalView = graphicalView;
        this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        MouseClickNotDragDetector.clickNotDragDetectingOn(this)
                .withPressedDurationThreshold(150)
                .setOnMouseClickedNotDragged((mouseEvent) -> {
                    graphicalView.getWindow().getController().clickOnGraphicalView(new double[] {0, 0});
                });

    }

    /**
     * Draws the map, and (if needed) the tour's trace on it.
     */
    public void draw() {
        drawMap();
        if (drawTour) {
            drawTour();
        }
    }

    /**
     * Draws the map, by populating the pane with graphical segments.
     */
    public void drawMap() {

        MapData mapData = graphicalView.getMapData();

        double screenScale = ViewUtilities.mapValue(
                getHeight(),
                0, 720,
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
                    2 * screenScale * mapScale,
                    Color.web("#545454"),
                    graphicalView.getWindow().getStreetNameLabel()
            );
            this.getChildren().add(graphicalViewSegment);

        }

    }

    /**
     * Draws the tour trace, by populating the pane with graphical segments.
     */
    public void drawTour() {

        MapData mapData = graphicalView.getMapData();
        TourData tourData = graphicalView.getTourData();

        double screenScale = ViewUtilities.mapValue(
                getHeight(),
                0, 720,
                0, 1
        );
        double mapScale = ViewUtilities.mapValue(
                mapData.getMaxLon() - mapData.getMinLon(),
                0.02235, 0.07610,
                1.25, 0.75
        );

        List<Path> tourPaths = tourData.getTourPaths();
        for (Path path : tourPaths) {

            GraphicalViewPath graphicalViewPath = new GraphicalViewPath(
                    graphicalView,
                    path,
                    4 * screenScale * mapScale,
                    Color.web("#ED6A08")
            );
            this.getChildren().add(graphicalViewPath);

        }

    }

    /**
     * Setter for attribute drawTour.
     * @param drawTour Whether to draw the tour's trace or not
     */
    public void setDrawTour(final boolean drawTour) {
        this.drawTour = drawTour;
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
