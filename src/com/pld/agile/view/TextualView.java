package com.pld.agile.view;

import com.pld.agile.model.tour.Path;
import com.pld.agile.utils.observer.Observable;
import com.pld.agile.utils.observer.Observer;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.observer.UpdateType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.util.List;

/**
 * Class handling the textual view of the model.
 */
public class TextualView implements Observer {

    /**
     * The application's TourData instance.
     */
    private TourData tourData;
    /**
     * The application's Window instance.
     */
    private Window window;
    /**
     * Wrapper component encapsulating the textual view.
     */
    private ScrollPane component;

    /**
     * TextualView constructor.
     * Adds observers on the model objects, populates the graphical components.
     * @param window The application's Window instance.
     */
    public TextualView(final Window window) {

        this.window = window;
        this.tourData = window.getTourData();
        tourData.addObserver(this);

        // Create ScrollPane
        component = new ScrollPane();
        component.setPadding(new Insets(20));
        component.setFitToWidth(true);
        component.setMaxWidth(Double.MAX_VALUE);
        component.getStyleClass().add("white-background");

    }

    /**
     * Populates the textual view in no particular order,
     * by iterating through the tour data's requests list.
     */
    public void populateInitialTextualView() {

        VBox requestListContainer = new VBox(20);
        requestListContainer.getStyleClass().add("white-background");

        List<Request> requests = tourData.getRequestList();

        if (requests.size() == 0) {
            component.setContent(requestListContainer);
            return;
        }

        VBox warehousePanel = new TextualViewStop(tourData.getWarehouse(), component, false);
        warehousePanel.setOnMouseClicked(
            e -> window.getController().clickOnTextualStop(tourData.getWarehouse())
        );
        requestListContainer.getChildren().add(warehousePanel);

        for (Request request : requests) {
            VBox requestPanel1 = new TextualViewStop(request.getPickup(), component, false);
            requestPanel1.setOnMouseClicked(
                e -> window.getController().clickOnTextualStop(request.getPickup())
            );
            VBox requestPanel2 = new TextualViewStop(request.getDelivery(), component, false);
            requestPanel2.setOnMouseClicked(
                e -> window.getController().clickOnTextualStop(request.getDelivery())
            );
            requestListContainer.getChildren().addAll(
                    requestPanel1,
                    requestPanel2
            );
        }

        component.setContent(requestListContainer);

    }

    /**
     * Populates the textual view in the calculated tour's order,
     * by iterating through the tour data's computed path.
     */
    public void populateTourTextualView() {

        VBox requestListContainer = new VBox(20);
        requestListContainer.getStyleClass().add("white-background");

        List<Request> requests = tourData.getRequestList();
        List<Path> tourPaths = tourData.getTourPaths();

        if (requests.size() == 0) {
            component.setContent(requestListContainer);
            return;
        }
      
        for (Path path : tourPaths) {
            Stop stop = path.getOrigin();
            VBox requestPanel = new TextualViewStop(stop, component, true);
            requestPanel.setOnMouseClicked(
                e -> window.getController().clickOnTextualStop(stop)
            );
            requestListContainer.getChildren().add(requestPanel);
        }

        component.setContent(requestListContainer);

    }

    /**
     * Updates the views whenever the model notifies a change.
     * @param o The observable object who notified the view.
     * @param updateType The type of update that has been made.
     */
    @Override
    public void update(Observable o, UpdateType updateType) {
        switch (updateType) {
            case REQUESTS -> populateInitialTextualView();
            case TOUR -> populateTourTextualView();
        }
    }

    /**
     * Getter for component.
     * @return component
     */
    public Node getComponent() {
        return component;
    }
}
