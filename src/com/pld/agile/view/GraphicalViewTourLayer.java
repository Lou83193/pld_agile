package com.pld.agile.view;

import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
import com.pld.agile.model.tour.Path;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.view.MouseClickNotDragDetector;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import java.util.List;

/**
 * Class handling the graphical map and the tour's trace on it.
 */
public class GraphicalViewTourLayer extends Pane {

    /**
     * The parent GraphicalView instance.
     */
    private GraphicalView graphicalView;

    /**
     * GraphicalViewTourLayer constructor.
     * @param graphicalView The parent GraphicalView instance
     */
    public GraphicalViewTourLayer(GraphicalView graphicalView) {
        this.graphicalView = graphicalView;
        this.setPickOnBounds(false);

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
     * Draws the tour trace, by populating the pane with graphical segments.
     * @param finished boolean indicating the view state
     */
    public void draw(boolean finished) {

        MapData mapData = graphicalView.getWindow().getMapData();
        TourData tourData = graphicalView.getWindow().getTourData();

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

        List<Path> tourPaths = tourData.getTourPaths();

        this.getChildren().clear();

        for (Path path : tourPaths) {

            // Create path
            GraphicalViewPath graphicalViewPath = new GraphicalViewPath(
                graphicalView,
                path,
                6 * screenScale * mapScale,
                finished
            );
            this.getChildren().add(graphicalViewPath);

            // Create invisible segments
            List<Segment> segments = path.getSegments();
            for (Segment segment : segments) {

                if (segment == null) {
                    continue;
                }

                GraphicalViewSegment hitboxSegment = new GraphicalViewSegment(
                        graphicalView,
                        segment,
                        6 * screenScale * mapScale,
                        graphicalViewPath,
                        graphicalView.getWindow().getStreetNameLabel()
                );
                this.getChildren().add(hitboxSegment);

            }

        }

    }

    /**
     * Clear the tour trace
     */
    public void clear() {
        this.getChildren().clear();
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
