package com.pld.agile.view;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.VBox;
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
        this.getChildren().clear();

        if (requests.size() == 0) {
            return;
        }

        double graphicSize = 20 * screenScale * mapScale;

        Stop warehouse = tourData.getWarehouse();
        VBox warehouseGraphic = new GraphicalViewStop(warehouse, graphicSize);
        double[] warehousePos = projectLatLon(warehouse.getAddress());
        warehouseGraphic.relocate(warehousePos[0] - graphicSize / 2, warehousePos[1] - graphicSize / 2);
        this.getChildren().add(warehouseGraphic);

        for (Request request : requests) {

            Stop pickup = request.getPickup();
            VBox pickupGraphic = new GraphicalViewStop(pickup, graphicSize);
            double[] pickupPos = projectLatLon(pickup.getAddress());
            pickupGraphic.relocate(pickupPos[0] - graphicSize / 2, pickupPos[1] - graphicSize / 2);
            this.getChildren().add(pickupGraphic);

            Stop delivery = request.getDelivery();
            VBox deliveryGraphic = new GraphicalViewStop(delivery, graphicSize);
            double[] deliveryPos = projectLatLon(delivery.getAddress());
            deliveryGraphic.relocate(deliveryPos[0] - graphicSize / 2, deliveryPos[1] - graphicSize / 2);
            this.getChildren().add(deliveryGraphic);

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

}
