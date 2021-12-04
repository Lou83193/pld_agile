package com.pld.agile.view;

import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
import com.pld.agile.model.tour.Path;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.view.MouseClickNotDragDetector;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

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
    }

    /**
     * Draws the tour trace, by populating the pane with graphical segments.
     */
    public void draw(boolean finished) {

        MapData mapData = graphicalView.getWindow().getMapData();
        TourData tourData = graphicalView.getWindow().getTourData();

        double screenScale = ViewUtilities.mapValue(
                getHeight(),
                0, 1000,
                0, 1
        );
        double mapScale = ViewUtilities.mapValue(
                mapData.getMaxLon() - mapData.getMinLon(),
                0.02235, 0.07610,
                1.25, 0.75
        );

        List<Request> requests = tourData.getRequestList();
        List<Path> tourPaths = tourData.getTourPaths();

        this.getChildren().clear();

        if (requests.size() == 0) {
            return;
        }

        for (Path path : tourPaths) {

            GraphicalViewPath graphicalViewPath = new GraphicalViewPath(
                graphicalView,
                path,
                6 * screenScale * mapScale,
                    finished
            );
            this.getChildren().add(graphicalViewPath);

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
