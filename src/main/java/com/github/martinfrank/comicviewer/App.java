package com.github.martinfrank.comicviewer;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class App extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private SplitPane root;
    private ViewerController viewerController;

    public static void main(String[] parameters) {
        launch(parameters);
    }

    @Override
    public void init() {
        ResourceManager resourceManager = new ResourceManager(getClass().getClassLoader());
        try {
            StructureLoader structureLoader = new StructureLoader(resourceManager.getInput());
            FXMLLoader fxmlLoader = new FXMLLoader(resourceManager.getGuiRoot());
            fxmlLoader.setControllerFactory(new ControllerFactory(structureLoader));
            root = fxmlLoader.load();
            viewerController = fxmlLoader.getController();
            viewerController.init();
        } catch (IOException e) {
            LOGGER.debug("error {}", e);
        }
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(root);
        stage.setTitle("tbd title");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        if (viewerController != null) {
            viewerController.stop();
        }
    }

}
