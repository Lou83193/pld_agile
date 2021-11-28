package com.pld.agile.view;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
import com.pld.agile.model.tour.Path;
import com.pld.agile.model.tour.TourData;
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
     * Boolean switch instructing whether to draw the tour's trace or not.
     */
    private boolean drawTour = false;

    /**
     * GraphicalViewMapLayer constructor.
     * @param mapData The application's MapData instance
     * @param tourData The application's TourData instance
     * @param window The application's Window instance
     */
    public GraphicalViewMapLayer(MapData mapData, TourData tourData, Window window) {
        this.mapData = mapData;
        this.tourData = tourData;
        this.window = window;
        this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
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

            double[] originPos = projectLatLon(segment.getOrigin());
            double[] destinationPos = projectLatLon(segment.getDestination());
            Line graphicalViewSegment = new GraphicalViewSegment(
                segment,
                originPos[0],
                originPos[1],
                destinationPos[0],
                destinationPos[1],
                2 * screenScale * mapScale,
                Color.web("#545454"),
                window.getStreetNameLabel()
            );
            this.getChildren().add(graphicalViewSegment);

        }

    }

    /**
     * Draws the tour trace, by populating the pane with graphical segments.
     */
    public void drawTour() {

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

            List<Segment> pathSegments = path.getSegments();
            for (Segment segment : pathSegments) {

                double[] originPos = projectLatLon(segment.getOrigin());
                double[] destinationPos = projectLatLon(segment.getDestination());
                Line graphicalViewSegment = new GraphicalViewSegment(
                        segment,
                        originPos[0],
                        originPos[1],
                        destinationPos[0],
                        destinationPos[1],
                        4 * screenScale * mapScale,
                        Color.web("#ED6A08"),
                        null
                );
                this.getChildren().add(graphicalViewSegment);

            }

        }

    }

    /**
     * Projects an intersection's (lat; lon) address to a pixel coordinate
     * based on the container's size and the mapData's bounds.
     * @param intersection The intersection whose address to project.
     * @return A double[] containing the {x, y} projection.
     */
    private double[] projectLatLon(final Intersection intersection) {
        return ViewUtilities.projectMercatorLatLon(
            intersection.getLatitude(),
            intersection.getLongitude(),
            mapData.getMinLat(),
            mapData.getMinLon(),
            mapData.getMaxLat(),
            mapData.getMaxLon(),
            getHeight()
        );
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
