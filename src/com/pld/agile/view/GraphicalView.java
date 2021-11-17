package com.pld.agile.view;

import com.pld.agile.model.Intersection;
import com.pld.agile.model.MapData;
import com.pld.agile.model.Segment;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class GraphicalView implements Observer { //TODO: create our own Observer interface

    private Canvas mapCanvas;
    private Group component;

    public GraphicalView() {

        // Add Observers
        //modelObj.addObserver(this);

        // Create and populate map canvas
        MapData mapData = MapData.getInstance();
        mapCanvas = createCanvasFromMap(mapData);

        System.out.println(mapCanvas);

        component = new Group();
        component.getChildren().add(mapCanvas);

    }

    @Override
    public void update(Observable o, Object arg) {
    }

    public Canvas createCanvasFromMap(MapData mapData) {

        Canvas canvas = new Canvas(720, 720);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.LIGHTGREY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK);

        List<Segment> segments = mapData.getSegments();

        for (Segment s : segments) {

            Intersection origin = s.getOrigin();
            double[] originPos = simpleProjection(
                canvas,
                origin.getLatitude(),
                origin.getLongitude(),
                mapData.getMinLat(),
                mapData.getMinLon(),
                mapData.getMaxLat(),
                mapData.getMaxLon()
            );

            Intersection destination = s.getDestination();
            double[] destinationPos = simpleProjection(
                canvas,
                destination.getLatitude(),
                destination.getLongitude(),
                mapData.getMinLat(),
                mapData.getMinLon(),
                mapData.getMaxLat(),
                mapData.getMaxLon()
            );

            gc.strokeLine(originPos[0], originPos[1], destinationPos[0], destinationPos[1]);

        }

        return canvas;

    }

    private double map(double x, double min1, double max1, double min2, double max2) {
        return min2 + (x-min1)/(max1-min1) * (max2-min2);
    }

    private double[] simpleProjection(Canvas canvas, double lat, double lon, double minLat, double minLon, double maxLat, double maxLon) {
        double x = map(lon, minLon, maxLon, 0, canvas.getWidth());
        double y = map(lat, minLat, maxLat, canvas.getHeight(), 0);
        return new double[] {x, y};
    }

    public Canvas getComponent() {
        return mapCanvas;
    }
}
