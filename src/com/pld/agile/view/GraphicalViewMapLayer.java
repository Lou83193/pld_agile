package com.pld.agile.view;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
import com.pld.agile.model.tour.Path;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.tsp.Graph;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import java.util.List;

public class GraphicalViewMapLayer extends Pane {

    private MapData mapData;
    private TourData tourData;
    private Window window;
    private boolean drawTour = false;

    public GraphicalViewMapLayer(MapData mapData, TourData tourData, Window window) {
        this.mapData = mapData;
        this.tourData = tourData;
        this.window = window;
        this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void draw() {
        drawMap();
        if (drawTour) {
            drawTour();
        }
    }

    public void drawMap() {

        double screenScale = ViewUtilities.mapValue(getHeight(), 0, 720, 0, 1);
        double mapScale = ViewUtilities.mapValue(mapData.getMaxLon() - mapData.getMinLon(), 0.02235, 0.07610, 1.25, 0.75);

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

    public void drawTour() {

        double screenScale = ViewUtilities.mapValue(getHeight(), 0, 720, 0, 1);
        double mapScale = ViewUtilities.mapValue(mapData.getMaxLon() - mapData.getMinLon(), 0.02235, 0.07610, 1.25, 0.75);

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

    private double[] projectLatLon(Intersection intersection) {
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

    public void setDrawTour(boolean drawTour) {
        this.drawTour = drawTour;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

}
