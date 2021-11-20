package com.pld.agile.view;

import com.pld.agile.model.Intersection;
import com.pld.agile.model.MapData;
import com.pld.agile.model.Segment;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.List;

public class GraphicalViewMap extends Canvas {

    public GraphicalViewMap(Scene parent) {
        widthProperty().bind(parent.heightProperty());
        heightProperty().bind(parent.heightProperty());
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    public void draw() {

        double width = getWidth();
        double height = getHeight();
        System.out.println(width + "; " + height);
        MapData mapData = MapData.getInstance();

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        gc.setFill(Color.LIGHTGREY);
        gc.fillRect(0, 0, width, height);

        gc.setLineWidth(height/250);
        gc.setStroke(Color.BLACK);

        List<Segment> segments = mapData.getSegments();

        for (Segment s : segments) {

            Intersection origin = s.getOrigin();
            double[] originPos = simpleProjection(
                    origin.getLatitude(),
                    origin.getLongitude(),
                    mapData.getMinLat(),
                    mapData.getMinLon(),
                    mapData.getMaxLat(),
                    mapData.getMaxLon(),
                    width,
                    height
            );

            Intersection destination = s.getDestination();
            double[] destinationPos = simpleProjection(
                    destination.getLatitude(),
                    destination.getLongitude(),
                    mapData.getMinLat(),
                    mapData.getMinLon(),
                    mapData.getMaxLat(),
                    mapData.getMaxLon(),
                    width,
                    height
            );

            gc.strokeLine(originPos[0], originPos[1], destinationPos[0], destinationPos[1]);

        }
    }

    private double mapValue(double x, double min1, double max1, double min2, double max2) {
        return min2 + (x-min1)/(max1-min1) * (max2-min2);
    }

    private double[] simpleProjection(double lat, double lon, double minLat, double minLon, double maxLat, double maxLon, double width, double height) {
        double x = mapValue(lon, minLon, maxLon, 0, width);
        double y = mapValue(lat, minLat, maxLat, height, 0);
        return new double[] {x, y};
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    /*
    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
    */

}
