package com.pld.agile.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Window extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        /* MENU BAR */
        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem fileMenu1 = new MenuItem("Load map");
        MenuItem fileMenu2 = new MenuItem("Load tour");
        fileMenu2.setDisable(true);
        fileMenu.getItems().add(fileMenu1);
        fileMenu.getItems().add(fileMenu2);
        // Edit menu
        Menu editMenu = new Menu("Edit");
        MenuItem editMenu1 = new MenuItem("Undo");
        MenuItem editMenu2 = new MenuItem("Redo");
        editMenu.getItems().add(editMenu1);
        editMenu.getItems().add(editMenu2);
        // About menu
        Menu aboutMenu = new Menu("About");
        MenuItem aboutMenu1 = new MenuItem("Help");
        MenuItem aboutMenu2 = new MenuItem("Credits");
        aboutMenu.getItems().add(aboutMenu1);
        aboutMenu.getItems().add(aboutMenu2);
        // Menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(editMenu);
        menuBar.getMenus().add(aboutMenu);

        /* CENTER ELEMENTS */
        // Logo
        Text logo = new Text("LOGO");
        logo.setFont(new Font(120));
        // Button
        Button button = new Button("Load Map");
        button.setPrefSize(200, 60);
        button.getStyleClass().add("button");
        // Group
        VBox homePage = new VBox(50);
        homePage.setAlignment(Pos.CENTER);
        homePage.getChildren().add(logo);
        homePage.getChildren().add(button);

        /* BOTTOM TEXT */
        Text bottomText = new Text("v0.1 • by Hexanom-nom");
        bottomText.setFont(new Font(16));
        HBox bottom = new HBox();
        bottom.setPadding(new Insets(20));
        bottom.setAlignment(Pos.CENTER_RIGHT);
        bottom.getChildren().add(bottomText);

        /* LAYOUT PANEL */
        BorderPane pane = new BorderPane();
        pane.setTop(menuBar);
        pane.setCenter(homePage);
        pane.setBottom(bottom);
        pane.getStyleClass().add("background");

        /* MAIN STAGE */
        Scene scene = new Scene(pane,1280, 720);
        scene.getStylesheets().add("/stylesheet.css");
        stage.setScene(scene);
        stage.setTitle("TEST");
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}