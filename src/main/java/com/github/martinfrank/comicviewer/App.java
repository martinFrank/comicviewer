package com.github.martinfrank.comicviewer;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class App extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static final int CORE_POOL_SIZE = 1;
    private static final int MAXIMUM_POOL_SIZE = 1;
    private static final long KEEP_ALIVE_TIME = 1L;
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    private final BlockingQueue<Runnable> pool = new LinkedBlockingQueue<>();

    private Button button;
    private Canvas canvas;

    private HBox hbox;
    private ComicLoader comicLoader;

    private ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, pool);

    public static void main(String[] parameters) {
        launch(parameters);
    }

    @Override
    public void init() {
        button = new Button("shake my snake");
        button.setOnAction(event -> doIt());
        canvas = new Canvas();
        canvas.setWidth(300);
        canvas.setHeight(300);


        ResourceManager resourceManager = new ResourceManager(getClass().getClassLoader());
        try {
            comicLoader = new ComicLoader(resourceManager.getInput());
            FXMLLoader fxmlLoader = new FXMLLoader(resourceManager.getGuiRoot());
            fxmlLoader.setControllerFactory(new ControllerFactory(comicLoader));
            hbox = fxmlLoader.load();
            ViewerController viewerController = fxmlLoader.getController();
            viewerController.init();
        } catch (IOException e) {
            LOGGER.debug("error {}", e);
        }
    }

    private void doIt() {
        if (threadPoolExecutor.getActiveCount() == 0) {
            LOGGER.debug("do it!!");
        } else {
            LOGGER.debug("still busy, skipping request");
        }
    }

    @Override
    public void start(Stage stage) {
//        VBox root = new VBox();
//        root.getChildren().add(button);
//        root.getChildren().add(canvas);
//        Scene scene = new Scene(root);
        Scene scene = new Scene(hbox);
        stage.setTitle("tbd title");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        threadPoolExecutor.shutdown();
    }

}
