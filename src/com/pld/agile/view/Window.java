package com.pld.agile.view;

import com.pld.agile.controller.Controller;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.beans.binding.DoubleBinding;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
     * Map associating stops to their graphical or textual view components
     */
    private HashMap<Stop, Node[]> graphicalStopsMap;
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
        scene = new Scene(wrapperPane, 1040, 720);
        scene.getStylesheets().add("stylesheet.css");

        constructMenuBar();
        constructHomePane();
        constructMainPane();

        wrapperPane.setTop(menuBar);
        wrapperPane.setCenter(homePane);

        stage.setScene(scene);
        stage.setTitle("COLIFFIMO - Route Planner");
        stage.getIcons().add(new Image("icon.png"));
        stage.show();

    }

    /**
     * Constructs the content pane of the home page.
     * Directly sets the homePane attribute.
     */
    private void constructHomePane() {

        homePane = new BorderPane();
        homePane.setId("home-pane");
        homePane.maxHeightProperty().bind(scene.heightProperty());
        homePane.maxWidthProperty().bind(scene.heightProperty().multiply(1040/720.0));

        // Logo
        ImageView logo = new ImageView(new Image("logo.png"));
        logo.setPreserveRatio(true);
        logo.setFitWidth(600);
        logo.setSmooth(true);
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
        Text bottomText = new Text("v2.0 • by Hexanom-nom");
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
    private void constructMainPane() {

        graphicalStopsMap = new HashMap<>();

        mainPane = new BorderPane();
        mainPane.getStyleClass().add("white-background");
        mainPane.maxHeightProperty().bind(scene.heightProperty());
        mainPane.maxWidthProperty().bind(scene.heightProperty().add(320));

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
        mainSceneButton.setCursor(Cursor.DEFAULT);
        buttonWrapper.getChildren().add(mainSceneButton);
        sidePanel.setBottom(buttonWrapper);
        mainPane.setRight(sidePanel);

        final KeyCombination undoKeyboardShortcut = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, (event) -> {
            if (undoKeyboardShortcut.match(event)) {
                menuBar.getMenus().get(1).getItems().get(0).fire();
            }
        });
        final KeyCombination redoKeyboardShortcut = new KeyCodeCombination(KeyCode.Y, KeyCombination.SHORTCUT_DOWN);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, (event) -> {
            if (redoKeyboardShortcut.match(event)) {
                menuBar.getMenus().get(1).getItems().get(1).fire();
            }
        });

    }

    /**
     * Constructs the application's menu bar.
     * Directly sets the menuBar attribute.
     */
    private void constructMenuBar() {

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
        MenuItem editMenu1 = new MenuItem("Undo (Ctrl+Z)");
        MenuItem editMenu2 = new MenuItem("Redo (Ctrl+Y)");
        editMenu1.setOnAction(
                new ButtonListener(controller, ButtonEventType.UNDO)
        );
        editMenu2.setOnAction(
                new ButtonListener(controller, ButtonEventType.REDO)
        );
        editMenu1.setDisable(true);
        editMenu2.setDisable(true);
        editMenu.getItems().addAll(editMenu1, editMenu2);

        // About menu
        Menu aboutMenu = new Menu("About");
        MenuItem aboutMenu1 = new MenuItem("Help");
        MenuItem aboutMenu2 = new MenuItem("Credits");
        aboutMenu1.setOnAction((e) -> {
            String path = "/sample_pdf.pdf";
            HostServices hostServices = getHostServices();
            hostServices.showDocument(getClass().getResource(path).toString());
        });
        aboutMenu2.setOnAction((e) -> {
            Stage creditsStage = new Stage();
            creditsStage.setTitle("Credits");
            VBox creditsPane = constructCreditsPane();
            creditsStage.setScene(new Scene(creditsPane, 400, 560));
            creditsStage.show();
        });
        aboutMenu.getItems().addAll(aboutMenu1, aboutMenu2);

        // Menu bar
        menuBar = new MenuBar();
        menuBar.setCursor(Cursor.DEFAULT);
        menuBar.getMenus().addAll(fileMenu, editMenu, aboutMenu);

    }

    private VBox constructCreditsPane() {

        List<String[]> creditsStrings = new ArrayList<>();
        creditsStrings.add(new String[] {"Daniel Blasko", "Scrum Master"});
        creditsStrings.add(new String[] {"Emilien Marchet", "Quality Manager"});
        creditsStrings.add(new String[] {"Laetitia Dodo", "Lead Tests Engineer"});
        creditsStrings.add(new String[] {"Nolwenn Deschand", "Backend Developer"});
        creditsStrings.add(new String[] {"Lou Bezzina", "Backend Developer"});
        creditsStrings.add(new String[] {"Damien-Joseph Rispal", "Algorithms Developer"});
        creditsStrings.add(new String[] {"Marcus Toma", "Frontend Developer"});

        VBox pane = new VBox(25);
        pane.setAlignment(Pos.CENTER);

        Text header = new Text("Made with love by:");
        header.getStyleClass().add("credit-header");
        pane.getChildren().add(header);

        VBox listPane = new VBox(8);
        listPane.setAlignment(Pos.CENTER);
        for (String[] credit : creditsStrings) {
            VBox creditPane = new VBox(2);
            creditPane.setAlignment(Pos.CENTER);
            Text name = new Text(credit[0]);
            Text role = new Text(credit[1]);
            name.getStyleClass().add("credit-name");
            role.getStyleClass().add("credit-role");
            creditPane.getChildren().addAll(name, role);
            listPane.getChildren().add(creditPane);
        }
        pane.getChildren().add(listPane);

        pane.getStylesheets().add("stylesheet.css");

        return pane;

    }

    /**
     * Switches the view from the home page to the main page.
     */
    public void switchToMainPane() {
        wrapperPane.setCenter(mainPane);
    }

    /**
     * Toggles menu items to be either enabled or disabled.
     * @param menuId The id of the menu.
     * @param itemId The id of menu item within the menu.
     * @param enabled Whether the menu item should be enabled or disabled.
     */
    public void toggleMenuItem(final int menuId, final int itemId, final boolean enabled) {
        menuBar.getMenus().get(menuId).getItems().get(itemId).setDisable(!enabled);
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
     * Unhighlights all graphical stops
     */
    public void unhighlightStops() {
        for (Node[] nodePair : graphicalStopsMap.values()) {
            GraphicalViewStop graphicalViewStop = (GraphicalViewStop) nodePair[0];
            TextualViewStop textualViewStop = (TextualViewStop) nodePair[1];
            if (graphicalViewStop != null) {
                graphicalViewStop.setHighlight(0);
            }
            if (textualViewStop != null) {
                textualViewStop.setHighlight(0);
            }
        }
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
     * Getter for graphicalStopsMap.
     * @return graphicalStopsMap
     */
    public HashMap<Stop, Node[]> getGraphicalStopsMap() {
        return graphicalStopsMap;
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
