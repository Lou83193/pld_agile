package com.pld.agile.view;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.List;

public class GraphicalViewRequests extends Group {

    private Canvas parentCanvas;
    private MapData mapData;
    private TourData tourData;
    private boolean drawAsTour = false;

    public GraphicalViewRequests(MapData mapData, TourData tourData, Canvas parentCanvas) {
        this.mapData = mapData;
        this.tourData = tourData;
        this.parentCanvas = parentCanvas;
        parentCanvas.widthProperty().addListener(evt -> draw());
        parentCanvas.heightProperty().addListener(evt -> draw());
    }

    public void draw() {
        if (drawAsTour) {
            drawTour();
        } else {
            drawInitial();
        }
    }

    public void drawInitial() {

        double width = parentCanvas.getWidth();
        double height = parentCanvas.getHeight();
        double screenScale = ViewUtilities.mapValue(height, 0, 720, 0, 1);
        double mapScale = ViewUtilities.mapValue(mapData.getMaxLon() - mapData.getMinLon(), 0.02235, 0.07610, 1.25, 0.75);

        List<Request> requests = tourData.getRequestList();
        this.getChildren().clear();

        if (requests.size() == 0) {
            return;
        }

        double graphicSize = 24 * screenScale * mapScale;

        Stop warehouse = tourData.getWarehouse();
        GraphicalViewStop warehouseGraphic = new GraphicalViewStop(warehouse, graphicSize, 0);
        double[] warehousePos = projectLatLon(warehouse.getAddress());
        warehouseGraphic.place(warehousePos);
        this.getChildren().add(warehouseGraphic);

        for (Request request : requests) {

            Stop pickup = request.getPickup();
            GraphicalViewStop pickupGraphic = new GraphicalViewStop(pickup, graphicSize, 0);
            double[] pickupPos = projectLatLon(pickup.getAddress());
            pickupGraphic.place(pickupPos);
            this.getChildren().add(pickupGraphic);

            Stop delivery = request.getDelivery();
            GraphicalViewStop deliveryGraphic = new GraphicalViewStop(delivery, graphicSize, 0);
            double[] deliveryPos = projectLatLon(delivery.getAddress());
            deliveryGraphic.place(deliveryPos);
            this.getChildren().add(deliveryGraphic);

        }

    }

    public void drawTour() {

        double width = parentCanvas.getWidth();
        double height = parentCanvas.getHeight();
        double screenScale = ViewUtilities.mapValue(height, 0, 720, 0, 1);
        double mapScale = ViewUtilities.mapValue(mapData.getMaxLon() - mapData.getMinLon(), 0.02235, 0.07610, 1.25, 0.75);

        List<Request> requests = tourData.getRequestList();
        List<Integer> tourOrder = tourData.getComputedPath();
        this.getChildren().clear();

        if (requests.size() == 0) {
            return;
        }

        double graphicSize = 24 * screenScale * mapScale;

        int count = 0;
        for (Integer i : tourOrder) {
            Integer stopId = tourData.getStops().get(i);
            Stop stop = tourData.getStopMap().get(stopId);
            GraphicalViewStop stopGraphic = new GraphicalViewStop(stop, graphicSize, count);
            double[] stopPos = projectLatLon(stop.getAddress());
            stopGraphic.place(stopPos);
            this.getChildren().add(stopGraphic);
            count++;
        }

    }

    private double[] projectLatLon(final Intersection intersection) {
        return ViewUtilities.projectLatLon(
                intersection.getLatitude(),
                intersection.getLongitude(),
                mapData.getMinLat(),
                mapData.getMinLon(),
                mapData.getMaxLat(),
                mapData.getMaxLon(),
                parentCanvas.getWidth(),
                parentCanvas.getHeight()
        );
    }

    public void setDrawAsTour(boolean drawAsTour) {
        this.drawAsTour = drawAsTour;
    }
}
