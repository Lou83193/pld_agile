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

    private static Window singletonInstance;

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
        homeScene = constructHomeScene();
        mainScene = constructMainScene();
        stage.setScene(homeScene);
        stage.setTitle("AGILE Project");
        stage.show();

    }

    public Scene constructHomeScene() {

        MenuBar menuBar = constructMenuBar(true);

        /* MAIN SCENE */
        BorderPane pane = new BorderPane();
        Scene scene = new Scene(pane,1080, 720);
        scene.getStylesheets().add("stylesheet.css");

        /* CENTER ELEMENTS */
        // Logo
        ImageView logo = new ImageView(new Image("logo.png"));
        logo.setPreserveRatio(true);
        logo.setFitWidth(600);
        // Button
        Button button = new Button("Load Map");
        button.setPrefSize(200, 50);
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
        pane.setTop(menuBar);
        pane.setCenter(homePage);
        pane.setBottom(bottom);

        return scene;

    }

    public Scene constructMainScene() {

        MenuBar menuBar = constructMenuBar(false);

        BorderPane pane = new BorderPane();
        Scene scene = new Scene(pane,1080, 720);
        scene.getStylesheets().add("stylesheet.css");

        HBox mainPanel = new HBox();
        GraphicalView graphicalView = new GraphicalView(mapData, tourData, scene);
        TextualView textualView = new TextualView(tourData, scene);
        mainPanel.getChildren().add(graphicalView.getComponent());
        mainPanel.getChildren().add(textualView.getComponent());

        pane.setTop(menuBar);
        pane.setCenter(mainPanel);

        return scene;

    }

    public MenuBar constructMenuBar(boolean hideLoadTour) {

        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem fileMenu1 = new MenuItem("Load map");
        MenuItem fileMenu2 = new MenuItem("Load tour");
        MenuItem fileMenu3 = new MenuItem("Compute tour");
        fileMenu1.setOnAction(new ButtonListener(controller, ButtonEventType.LOAD_MAP));
        fileMenu2.setOnAction(new ButtonListener(controller, ButtonEventType.LOAD_TOUR));
        fileMenu3.setOnAction(new ButtonListener(controller, ButtonEventType.COMPUTE_TOUR));
        if (hideLoadTour) {
            fileMenu2.setDisable(true);
            fileMenu3.setDisable(true);
        }
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