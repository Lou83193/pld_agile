package com.pld.agile.view;

import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.view.ProjectionUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.Random;

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
        List<Request> requests = tourData.getRequestList();

        for (Request request : requests) {

            Stop pickup = request.getPickup();
            Stop delivery = request.getDelivery();

            double[] pickupPos = ProjectionUtils.projectLatLon(
                    pickup.getAddress().getLatitude(),
                    pickup.getAddress().getLongitude(),
                    mapData.getMinLat(),
                    mapData.getMinLon(),
                    mapData.getMaxLat(),
                    mapData.getMaxLon(),
                    width,
                    height
            );

            double[] deliveryPos = ProjectionUtils.projectLatLon(
                    delivery.getAddress().getLatitude(),
                    delivery.getAddress().getLongitude(),
                    mapData.getMinLat(),
                    mapData.getMinLon(),
                    mapData.getMaxLat(),
                    mapData.getMaxLon(),
                    width,
                    height
            );

            Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color randomColor = new Color(r, g, b, 1.0);

            Circle pickupGraphic = new Circle(20);
            pickupGraphic.setFill(randomColor);
            pickupGraphic.relocate(pickupPos[0], pickupPos[1]);

            Rectangle deliveryGraphic = new Rectangle(20, 20);
            deliveryGraphic.setFill(randomColor);
            deliveryGraphic.relocate(deliveryPos[0], deliveryPos[1]);

        }


    }

}
