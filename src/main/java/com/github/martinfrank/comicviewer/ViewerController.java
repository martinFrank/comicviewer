package com.github.martinfrank.comicviewer;

import com.github.martinfrank.comicbrowser.xml.Page;
import com.github.martinfrank.comicbrowser.xml.WebsiteStructure;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ViewerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewerController.class);

    private static final double[] ZOOM_FACTORS = {0.125, 0.25, 0.33, 0.5, 0.75, 1, 1.33, 1.5, 2, 4, 6, 8};
    private static final long POLL_TIME = 25;
    private static final TimeUnit POLL_TIME_UNIT = TimeUnit.MILLISECONDS;
    private int zoomFactorIndex = 6;
    private String imageStack;
    private String currentImage;
    private final BlockingQueue<Runnable> pool = new LinkedBlockingQueue<>();
    private final ScheduledThreadPoolExecutor scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    private final StructureLoader comicLoader;

    @FXML
    public TextField info;

    @FXML
    public TreeView<Page> tree;

    @FXML
    public ImageView imageView;


    public ViewerController(StructureLoader comicLoader) {
        this.comicLoader = comicLoader;
        scheduledExecutorService.scheduleAtFixedRate(createImageRefresher(), 0, POLL_TIME, POLL_TIME_UNIT);
    }

    private Runnable createImageRefresher() {
        return () -> {
            if (imageStack != null && !imageStack.equals(currentImage)) {
                currentImage = imageStack;
                imageView.setImage(new Image(currentImage));
                LOGGER.debug("update image");
            }
        };
    }


    public void prev(ActionEvent event) {
        TreeWalker.moveUp(tree);
        LOGGER.debug("event: prev");
    }

    public void next(ActionEvent event) {
        TreeWalker.moveDown(tree);
        LOGGER.debug("event: next");
    }

    private void initTree() {
        Page rootPage = new Page();
        rootPage.setDetail("root");
        TreeItem<Page> rootItem = new TreeItem<>(rootPage);
        LOGGER.debug("tree {}", tree);
        for (WebsiteStructure structure : comicLoader.getStructures()) {
            LOGGER.debug("adding structure {}", structure.getTitle());
            Page titlePage = new Page();
            titlePage.setDetail(structure.getTitle());
            TreeItem<Page> item = new TreeItem<>(titlePage);
            for (Page page : structure.getPages()) {
                TreeItem<Page> pageItem = new TreeItem<>(page);
                item.getChildren().add(pageItem);
            }
            rootItem.getChildren().add(item);
        }
        tree.setRoot(rootItem);
        tree.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> prepareImage(newValue.getValue()));
    }

    private void prepareImage(Page value) {
        String imageUrl = value.getUrl();
        LOGGER.debug("image url to catch: {}", imageUrl);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            imageStack = imageUrl;
            info.setText(value.toString());
        }
    }


    public void init() {
        initTree();
    }

    public void stop() {
        scheduledExecutorService.shutdown();
    }

    public void zoomIn(ActionEvent event) {
        zoomFactorIndex = zoomFactorIndex - 1;
        if (zoomFactorIndex < 0) {
            zoomFactorIndex = 0;
        }
        applyZoom();
    }

    public void zoomOut(ActionEvent event) {
        zoomFactorIndex = zoomFactorIndex + 1;
        if (zoomFactorIndex > ZOOM_FACTORS.length - 1) {
            zoomFactorIndex = ZOOM_FACTORS.length - 1;
        }
        applyZoom();
    }

    public void zoomReset(ActionEvent event) {
        zoomFactorIndex = 6;
        applyZoom();
    }

    private void applyZoom() {
        imageView.setScaleX(ZOOM_FACTORS[zoomFactorIndex]);
        imageView.setScaleY(ZOOM_FACTORS[zoomFactorIndex]);
    }
}
