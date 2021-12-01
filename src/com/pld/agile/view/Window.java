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

/**
 * Window class of the application.
 */
public class Window extends Application {

    /**
     * The window's stage.
     */
    private Stage stage;
    /**
     * The window's only scene.
     */
    private Scene scene;
    /**
     * The border pane containing the top menu bar
     * and the contents of the window.
     */
    private BorderPane wrapperPane;
    /**
     * The top menu bar containing the file, edit, and about menus.
     */
    private MenuBar menuBar;
    /**
     * The contents of the home page.
     */
    private BorderPane homePane;
    /**
     * The contents of the main page.
     */
    private BorderPane mainPane;
    /**
     * Text field displaying the street name
     * of the currently hovered map segment.
     */
    private TextField streetNameLabel;
    /**
     * Action button of the main page's side panel.
     */
    private Button mainSceneButton;
    /**
     * Main page's side panel.
     */
    private BorderPane sidePanel;

    /**
     * The application's Controller instance.
     */
    private final Controller controller;
    /**
     * The application's MapData instance.
     */
    private final MapData mapData;
    /**
     * The application's TourData instance.
     */
    private final TourData tourData;

    /**
     * Window constructor.
     */
    public Window() {
        this.mapData = new MapData();
        this.tourData = new TourData();
        this.controller = new Controller(this);
    }

    /**
     * JavaFX launch method.
     * @param s The window's stage.
     * @throws IOException JavaFX exception.
     */
    @Override
    public void start(final Stage s) throws IOException {

        stage = s;

        wrapperPane = new BorderPane();
        scene = new Scene(wrapperPane, 1020, 720);
        scene.getStylesheets().add("stylesheet.css");

        constructMenuBar();
        constructHomePane();
        constructMainPane();

        wrapperPane.setTop(menuBar);
        wrapperPane.setCenter(homePane);

        //wrapperPane.maxHeightProperty().bind(scene.heightProperty());
        //wrapperPane.maxWidthProperty().bind(scene.heightProperty().multiply(1020/720));

        stage.setScene(scene);
        stage.setTitle("COLIFFIMO - Route Planner");
        stage.getIcons().add(new Image("icon.png"));
        stage.show();

    }

    /**
     * Constructs the content pane of the home page.
     * Directly sets the homePane attribute.
     */
    public void constructHomePane() {

        homePane = new BorderPane();
        homePane.setId("home-pane");

        // Logo
        ImageView logo = new ImageView(new Image("logo.png"));
        logo.setPreserveRatio(true);
        logo.setFitWidth(600);
        // Button
        Button button = new Button("Load Map");
        button.getStyleClass().add("main-button");
        button.setOnAction(
            new ButtonListener(controller, ButtonEventType.LOAD_MAP)
        );
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

    /**
     * Constructs the content pane of the main page.
     * Directly sets the mainPane attribute.
     */
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
        GraphicalView graphicalView = new GraphicalView(this);
        centerPanel.setCenter(graphicalView.getComponent());
        mainPane.setCenter(centerPanel);

        sidePanel = new BorderPane();
        DoubleBinding sidePanelWidth = mainPane.widthProperty().subtract(graphicalView.getGraphicalViewMapLayer().widthProperty());
        sidePanel.prefWidthProperty().bind(sidePanelWidth);
        // Textual view
        TextualView textualView = new TextualView(this);
        sidePanel.setCenter(textualView.getComponent());
        // Side panel button
        HBox buttonWrapper = new HBox();
        buttonWrapper.setAlignment(Pos.CENTER);
        buttonWrapper.setPadding(new Insets(0, 20, 20, 20));
        mainSceneButton = new Button("Compute Tour");
        mainSceneButton.getStyleClass().add("main-button");
        mainSceneButton.setOnAction(
            new ButtonListener(controller, ButtonEventType.COMPUTE_TOUR)
        );
        mainSceneButton.prefWidthProperty().bind(sidePanelWidth);
        buttonWrapper.getChildren().add(mainSceneButton);
        sidePanel.setBottom(buttonWrapper);
        mainPane.setRight(sidePanel);

    }

    /**
     * Constructs the application's menu bar.
     * Directly sets the menuBar attribute.
     */
    public void constructMenuBar() {

        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem fileMenu1 = new MenuItem("Load map");
        MenuItem fileMenu2 = new MenuItem("Load requests");
        MenuItem fileMenu3 = new MenuItem("Compute tour");
        fileMenu1.setOnAction(
            new ButtonListener(controller, ButtonEventType.LOAD_MAP)
        );
        fileMenu2.setOnAction(
            new ButtonListener(controller, ButtonEventType.LOAD_REQUESTS)
        );
        fileMenu3.setOnAction(
            new ButtonListener(controller, ButtonEventType.COMPUTE_TOUR)
        );
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

    /**
     * Switches the view from the home page to the main page.
     */
    public void switchToMainPane() {
        wrapperPane.setCenter(mainPane);
    }

    /**
     * Toggles file menu items to either enabled or disabled.
     * @param num The id of the file menu item.
     * @param enabled Whether the file menu item should be enabled or disabled.
     */
    public void toggleFileMenuItem(final int num,
                                   final boolean enabled) {
        menuBar.getMenus().get(0).getItems().get(num).setDisable(!enabled);
    }

    /**
     * Sets the label and the buttonListener of the main page's action button.
     * @param label The label of the button.
     * @param buttonListener The buttonListener of the button.
     */
    public void setMainSceneButton(final String label,
                                   final ButtonListener buttonListener) {
        mainSceneButton.setText(label);
        mainSceneButton.setOnAction(buttonListener);
    }

    /**
     * Places the main page's action button either at the top of the side panel,
     * or at the bottom.
     * @param top Whether the action button should be placed at the top or not.
     */
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

    /**
     * Deactivates the main scene's button
     */
    public void toggleMainSceneButton(boolean enabled) {
        mainSceneButton.setDisable(!enabled);
    }

    /**
     * Getter for stage.
     * @return stage
     */
    public Stage getStage() {
        return stage;
    }
    /**
     * Getter for scene.
     * @return scene
     */
    public Scene getScene() {
        return scene;
    }
    /**
     * Getter for mapData.
     * @return mapData
     */
    public MapData getMapData() {
        return mapData;
    }
    /**
     * Getter for tourData.
     * @return tourData
     */
    public TourData getTourData() {
        return tourData;
    }
    /**
     * Getter for streetNameLabel.
     * @return streetNameLabel
     */
    public TextField getStreetNameLabel() {
        return streetNameLabel;
    }
    /**
     * Getter for controller.
     * @return controller
     */
    public Controller getController() {
        return controller;
    }

    /**
     * Main method of the application.
     * @param args Launch arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}
