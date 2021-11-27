package com.pld.agile.view;

import com.pld.agile.controller.Controller;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.TourData;
import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Window extends Application {

    private Stage stage;
    private Scene scene;
    private BorderPane wrapperPane;
    private MenuBar menuBar;
    private BorderPane homePane;
    private BorderPane mainPane;

    private TextField streetNameLabel;
    private Button mainSceneButton;
    private BorderPane sidePanel;

    private final Controller controller;
    private final MapData mapData;
    private final TourData tourData;

    private final int initialW = 1020;
    private final int initialH = 720;

    public Window() {
        this.mapData = new MapData();
        this.tourData = new TourData();
        this.controller = new Controller(this);
    }


    @Override
    public void start(final Stage s) throws IOException {

        stage = s;

        wrapperPane = new BorderPane();
        scene = new Scene(wrapperPane, initialW, initialH);
        scene.getStylesheets().add("stylesheet.css");

        constructMenuBar();
        constructHomePane();
        constructMainPane();

        wrapperPane.setTop(menuBar);
        wrapperPane.setCenter(homePane);

        stage.setScene(scene);
        stage.setTitle("COLIFFIMO - Route Planner");
        stage.show();

    }

    public void constructHomePane() {

        homePane = new BorderPane();
        homePane.setId("home-pane");

        // Logo
        ImageView logo = new ImageView(new Image("logo.png"));
        logo.setPreserveRatio(true);
        logo.setFitWidth(600);
        // Button
        Button button = new Button("Load Map");
        button.getStyleClass().add("button");
        button.setOnAction(new ButtonListener(controller, ButtonEventType.LOAD_MAP));
        // Group
        VBox homePage = new VBox(15);
        homePage.setAlignment(Pos.CENTER);
        homePage.getChildren().addAll(logo, button);
        homePane.setCenter(homePage);

        // Bottom text
        Text bottomText = new Text("v0.1 • by Hexanom-nom");
        bottomText.setFont(new Font(16));
        HBox bottom = new HBox();
        bottom.setPadding(new Insets(20));
        bottom.setAlignment(Pos.CENTER_RIGHT);
        bottom.getChildren().addAll(bottomText);
        homePane.setBottom(bottom);

    }

    public void constructMainPane() {

        mainPane = new BorderPane();
        mainPane.getStyleClass().add("white-background");

        BorderPane centerPanel = new BorderPane();
        // Street name label
        streetNameLabel = new TextField("Street Name");
        streetNameLabel.setAlignment(Pos.CENTER);
        streetNameLabel.setEditable(false);
        streetNameLabel.setMouseTransparent(true);
        streetNameLabel.setFocusTraversable(false);
        streetNameLabel.setId("street-name");
        centerPanel.setBottom(streetNameLabel);
        // Graphical view
        GraphicalView graphicalView = new GraphicalView(mapData, tourData, this);
        centerPanel.setCenter(graphicalView.getComponent());
        mainPane.setCenter(centerPanel);

        sidePanel = new BorderPane();
        DoubleBinding sidePanelWidth = mainPane.widthProperty().subtract(graphicalView.getGraphicalViewMap().widthProperty());
        sidePanel.prefWidthProperty().bind(sidePanelWidth);
        // Textual view
        TextualView textualView = new TextualView(tourData);
        sidePanel.setCenter(textualView.getComponent());
        // Side panel button
        HBox buttonWrapper = new HBox();
        buttonWrapper.setAlignment(Pos.CENTER);
        buttonWrapper.setPadding(new Insets(0, 20, 20, 20));
        mainSceneButton = new Button("Compute Tour");
        mainSceneButton.getStyleClass().add("button");
        mainSceneButton.setOnAction(new ButtonListener(controller, ButtonEventType.COMPUTE_TOUR));
        mainSceneButton.prefWidthProperty().bind(sidePanelWidth);
        buttonWrapper.getChildren().add(mainSceneButton);
        sidePanel.setBottom(buttonWrapper);
        mainPane.setRight(sidePanel);

    }

    public void constructMenuBar() {

        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem fileMenu1 = new MenuItem("Load map");
        MenuItem fileMenu2 = new MenuItem("Load requests");
        MenuItem fileMenu3 = new MenuItem("Compute tour");
        fileMenu1.setOnAction(new ButtonListener(controller, ButtonEventType.LOAD_MAP));
        fileMenu2.setOnAction(new ButtonListener(controller, ButtonEventType.LOAD_REQUESTS));
        fileMenu3.setOnAction(new ButtonListener(controller, ButtonEventType.COMPUTE_TOUR));
        fileMenu2.setDisable(true);
        fileMenu3.setDisable(true);
        fileMenu.getItems().addAll(fileMenu1, fileMenu2, fileMenu3);

        // Edit menu
        Menu editMenu = new Menu("Edit");
        MenuItem editMenu1 = new MenuItem("Undo");
        MenuItem editMenu2 = new MenuItem("Redo");
        editMenu1.setDisable(true);
        editMenu2.setDisable(true);
        editMenu.getItems().addAll(editMenu1, editMenu2);

        // About menu
        Menu aboutMenu = new Menu("About");
        MenuItem aboutMenu1 = new MenuItem("Help");
        MenuItem aboutMenu2 = new MenuItem("Credits");
        aboutMenu.getItems().addAll(aboutMenu1, aboutMenu2);

        // Menu bar
        menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, editMenu, aboutMenu);

    }

    public void switchToMainPane() {
        wrapperPane.setCenter(mainPane);
    }
    public void toggleFileMenuItem(final int num, final boolean enabled) {
        menuBar.getMenus().get(0).getItems().get(num).setDisable(!enabled);
    }
    public void setMainSceneButton(final String label, final ButtonListener listener) {
        mainSceneButton.setText(label);
        mainSceneButton.setOnAction(listener);
    }
    public void placeMainSceneButton(final boolean top) {
        if (top) {
            HBox bottomNode = (HBox) sidePanel.getBottom();
            if (bottomNode != null) {
                sidePanel.setBottom(null);
                sidePanel.setTop(bottomNode);
                bottomNode.setPadding(new Insets(20, 20, 0, 20));
            }
        } else {
            HBox topNode = (HBox) sidePanel.getTop();
            if (topNode != null) {
                sidePanel.setTop(null);
                sidePanel.setBottom(topNode);
                topNode.setPadding(new Insets(0, 20, 20, 20));
            }
        }
    }

    public Stage getStage() {
        return stage;
    }
    public Scene getScene() {
        return scene;
    }
    public MapData getMapData() {
        return mapData;
    }
    public TourData getTourData() {
        return tourData;
    }

    public TextField getStreetNameLabel() {
        return streetNameLabel;
    }

    public static void main(String[] args) {
        launch();
    }
}