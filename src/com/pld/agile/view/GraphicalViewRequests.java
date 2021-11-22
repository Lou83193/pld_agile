package com.pld.agile.view;

import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class GraphicalViewRequests extends Group {

    private Canvas parentCanvas;
    private MapData mapData;
    private TourData tourData;

    public GraphicalViewRequests(MapData mapData, TourData tourData, Canvas parentCanvas) {
        this.mapData = mapData;
        this.tourData = tourData;
        this.parentCanvas = parentCanvas;
        parentCanvas.widthProperty().addListener(evt -> draw());
        parentCanvas.heightProperty().addListener(evt -> draw());
    }

    public void draw() {

        double width = parentCanvas.getWidth();
        double height = parentCanvas.getHeight();
        double screenScale = ViewUtilities.mapValue(height, 0, 720, 0, 1);
        double mapScale = ViewUtilities.mapValue(mapData.getMaxLon() - mapData.getMinLon(), 0.02235, 0.07610, 1.25, 0.75);

        List<Request> requests = tourData.getRequestList();

        if (requests.size() == 0) return;

        this.getChildren().clear();
        double graphicSize = 20*screenScale*mapScale;
        double outlineSize = 2*screenScale*mapScale;

        Stop warehouse = tourData.getWarehouse();

        double[] warehousePos = ViewUtilities.projectLatLon(
                warehouse.getAddress().getLatitude(),
                warehouse.getAddress().getLongitude(),
                mapData.getMinLat(),
                mapData.getMinLon(),
                mapData.getMaxLat(),
                mapData.getMaxLon(),
                width,
                height
        );

        Rectangle warehouseGraphic = new Rectangle(graphicSize, graphicSize);
        warehouseGraphic.setFill(Color.BLACK);
        warehouseGraphic.setStroke(Color.WHITE);
        warehouseGraphic.setStrokeWidth(outlineSize);
        warehouseGraphic.setRotate(45);
        warehouseGraphic.relocate(warehousePos[0] - graphicSize/2, warehousePos[1] - graphicSize/2);
        this.getChildren().add(warehouseGraphic);

        for (Request request : requests) {

            Stop pickup = request.getPickup();
            Stop delivery = request.getDelivery();

            double[] pickupPos = ViewUtilities.projectLatLon(
                    pickup.getAddress().getLatitude(),
                    pickup.getAddress().getLongitude(),
                    mapData.getMinLat(),
                    mapData.getMinLon(),
                    mapData.getMaxLat(),
                    mapData.getMaxLon(),
                    width,
                    height
            );

            double[] deliveryPos = ViewUtilities.projectLatLon(
                    delivery.getAddress().getLatitude(),
                    delivery.getAddress().getLongitude(),
                    mapData.getMinLat(),
                    mapData.getMinLon(),
                    mapData.getMaxLat(),
                    mapData.getMaxLon(),
                    width,
                    height
            );

            Color colour = ViewUtilities.stringToColour(pickup.getAddress().toString());

            Circle pickupGraphic = new Circle(graphicSize/2);
            pickupGraphic.setFill(colour);
            pickupGraphic.setStroke(Color.BLACK);
            pickupGraphic.setStrokeWidth(outlineSize);
            pickupGraphic.relocate(pickupPos[0] - graphicSize/2, pickupPos[1] - graphicSize/2);
            this.getChildren().add(pickupGraphic);

            Rectangle deliveryGraphic = new Rectangle(graphicSize, graphicSize);
            deliveryGraphic.setFill(colour);
            deliveryGraphic.setStroke(Color.BLACK);
            deliveryGraphic.setStrokeWidth(outlineSize);
            deliveryGraphic.relocate(deliveryPos[0] - graphicSize/2, deliveryPos[1] - graphicSize/2);
            this.getChildren().add(deliveryGraphic);

        }


    }

}
