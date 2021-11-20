package com.pld.agile.view;

import com.pld.agile.controller.Controller;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Window extends Application {

    private static Window singletonInstance;

    private Scene homeScene;
    private Scene mainScene;
    private Stage stage;

    public Window() { singletonInstance = this; }

    public static Window getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new Window();
        }
        return singletonInstance;
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

        MenuBar menuBar = constructMenuBar();

        /* CENTER ELEMENTS */
        // Logo
        Text logo = new Text("LOGO");
        logo.setFont(new Font(120));
        // Button
        Button button = new Button("Load Map");
        button.setPrefSize(200, 60);
        button.getStyleClass().add("button");
        button.setOnAction(new ButtonListener(ButtonEventType.LOAD_MAP));
        // Group
        VBox homePage = new VBox(50);
        homePage.setAlignment(Pos.CENTER);
        homePage.getChildren().addAll(logo, button);

        /* BOTTOM TEXT */
        Text bottomText = new Text("v0.1 • by Hexanom-nom");
        bottomText.setFont(new Font(16));
        HBox bottom = new HBox();
        bottom.setPadding(new Insets(20));
        bottom.setAlignment(Pos.CENTER_RIGHT);
        bottom.getChildren().addAll(bottomText);

        /* LAYOUT PANEL */
        BorderPane pane = new BorderPane();
        pane.setTop(menuBar);
        pane.setCenter(homePage);
        pane.setBottom(bottom);

        /* MAIN SCENE */
        Scene scene = new Scene(pane,1280, 720);
        scene.getStylesheets().add("stylesheet.css");
        return scene;

    }

    public Scene constructMainScene() {

        MenuBar menuBar = constructMenuBar();

        /* MAIN PANEL */
        HBox mainPanel = new HBox();
        GraphicalView graphicalView = new GraphicalView();
        TextualView textualView = new TextualView();
        mainPanel.getChildren().add(graphicalView.getComponent());
        mainPanel.getChildren().add(textualView.getComponent());

        /* LAYOUT PANEL */
        BorderPane pane = new BorderPane();
        pane.setTop(menuBar);
        pane.setCenter(mainPanel);

        /* MAIN SCENE */
        Scene scene = new Scene(pane,1280, 720);
        scene.getStylesheets().add("stylesheet.css");
        return scene;

    }

    public MenuBar constructMenuBar() {

        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem fileMenu1 = new MenuItem("Load map");
        MenuItem fileMenu2 = new MenuItem("Load tour");
        fileMenu1.setOnAction(new ButtonListener(ButtonEventType.LOAD_MAP));
        fileMenu2.setDisable(true);
        fileMenu.getItems().addAll(fileMenu1, fileMenu2);

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

    public static void main(String[] args) {
        launch();
    }
}