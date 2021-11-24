package com.pld.agile.view;

import com.pld.agile.controller.Controller;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.TourData;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class Window extends Application {

    private MenuItem fileMenu1;
    private MenuItem fileMenu2;
    private MenuItem fileMenu3;
    private Scene homeScene;
    private Scene mainScene;
    private Stage stage;

    private final Controller controller;
    private final MapData mapData;
    private final TourData tourData;

    public Window() {
        this.mapData = new MapData();
        this.tourData = new TourData();
        this.controller = new Controller(this);
    }


    @Override
    public void start(Stage s) throws IOException {

        stage = s;
        constructHomeScene();
        constructMainScene();
        stage.setScene(homeScene);
        stage.setTitle("COLIFFIMO - Route Planner");
        stage.show();

    }

    public void constructHomeScene() {

        MenuBar menuBar = constructMenuBar();

        /* CENTER ELEMENTS */
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

        /* BOTTOM TEXT */
        Text bottomText = new Text("v0.1 • by Hexanom-nom");
        bottomText.setFont(new Font(16));
        HBox bottom = new HBox();
        bottom.setPadding(new Insets(20));
        bottom.setAlignment(Pos.CENTER_RIGHT);
        bottom.getChildren().addAll(bottomText);

        /* LAYOUT ELEMENTS */
        BorderPane pane = new BorderPane();
        pane.setTop(menuBar);
        pane.setCenter(homePage);
        pane.setBottom(bottom);
        pane.setId("home-pane");

        /* MAIN SCENE */
        Scene scene = new Scene(pane,1060, 720);
        scene.getStylesheets().add("stylesheet.css");

        homeScene = scene;

    }

    public void constructMainScene() {

        BorderPane pane = new BorderPane();
        Scene scene = new Scene(pane,1060, 720);
        scene.getStylesheets().add("stylesheet.css");

        MenuBar menuBar = constructMenuBar();
        pane.setTop(menuBar);

        GraphicalView graphicalView = new GraphicalView(mapData, tourData, scene);
        pane.setCenter(graphicalView.getComponent());

        BorderPane sidePanel = new BorderPane();
        sidePanel.prefWidthProperty().bind(scene.widthProperty().subtract(graphicalView.getGraphicalViewMap().widthProperty()));
        sidePanel.setPadding(new Insets(0, 0, 20, 0));
        TextualView textualView = new TextualView(tourData);
        HBox buttonWrapper = new HBox();
        buttonWrapper.setAlignment(Pos.CENTER);
        Button button = new Button("Compute Tour");
        button.getStyleClass().add("button");
        button.setOnAction(new ButtonListener(controller, ButtonEventType.COMPUTE_TOUR));
        buttonWrapper.getChildren().add(button);
        sidePanel.setCenter(textualView.getComponent());
        sidePanel.setBottom(buttonWrapper);
        pane.setRight(sidePanel);

        pane.getStyleClass().add("white-background");

        mainScene = scene;

    }

    public MenuBar constructMenuBar() {

        // File menu
        Menu fileMenu = new Menu("File");
        fileMenu1 = new MenuItem("Load map");
        fileMenu2 = new MenuItem("Load requests");
        fileMenu3 = new MenuItem("Compute tour");
        fileMenu1.setOnAction(new ButtonListener(controller, ButtonEventType.LOAD_MAP));
        fileMenu2.setOnAction(new ButtonListener(controller, ButtonEventType.LOAD_TOUR));
        fileMenu3.setOnAction(new ButtonListener(controller, ButtonEventType.COMPUTE_TOUR));
        fileMenu2.setDisable(true);
        fileMenu3.setDisable(true);
        fileMenu.getItems().addAll(fileMenu1, fileMenu2, fileMenu3);

        // Edit menu
        Menu editMenu = new Menu("Edit");
        MenuItem editMenu1 = new MenuItem("Undo");
        MenuItem editMenu2 = new MenuItem("Redo");
        editMenu.getItems().addAll(editMenu1, editMenu2);

        // About menu
        Menu aboutMenu = new Menu("About");
        MenuItem aboutMenu1 = new MenuItem("Help");
        MenuItem aboutMenu2 = new MenuItem("Credits");
        aboutMenu.getItems().addAll(aboutMenu1, aboutMenu2);

        // Menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, editMenu, aboutMenu);

        return menuBar;

    }

    public void switchSceneToMainScene() {
        stage.setScene(mainScene);
    }
    public void toggleFileMenuItem(int num, boolean enabled) {
        switch (num) {
            case 1 -> fileMenu1.setDisable(!enabled);
            case 2 -> fileMenu2.setDisable(!enabled);
            case 3 -> fileMenu3.setDisable(!enabled);
        }
    }

    public Stage getStage() {
        return stage;
    }
    public Controller getController() {
        return controller;
    }
    public MapData getMapData() {
        return mapData;
    }
    public TourData getTourData() {
        return tourData;
    }

    public static void main(String[] args) {
        launch();
    }
}